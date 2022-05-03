package Components;

import DB.DBConnection;
import Extra.Alerta;
import Extra.Fila;
import com.mysql.cj.protocol.Resultset;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import static java.time.Instant.now;

public class Venta {

    static  Connection con;

    public Venta(){
        super();
    }
    public static void registrarVenta( int idCliente, Long idProducto, int cantidadProducto, String tipoDePago){

        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL createSale(?,?,?,?)}");
            cs.setInt(1,idCliente);
            cs.setLong(2,idProducto);
            cs.setInt(3,cantidadProducto);
            cs.setString(4,tipoDePago);
            cs.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getComprasCliente(int idCliente, ListView listView, StackPane stackPane){
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getComprasCliente(?)}");
            cs.setInt(1, idCliente);
            Fila fila = new Fila();
            fila.primerFilaHistorial();
            listView.getItems().add(fila);
            ResultSet rs = cs.executeQuery();
            while (rs.next()){
                    listView.getItems().add(new Fila(rs.getDate(1),rs.getString(2), rs.getString(3),rs.getInt(4), rs.getFloat(5)));
            }
        } catch (SQLException e) {
            Alerta.Dialog(stackPane,"Error "+ e.getErrorCode(),"Ups! no pudimos conectarnos a la base de datos");
        }
    }
    public static void  ventasKeyEvents(KeyEvent e, ListView listView, VBox infoBox, StackPane stackPane, Label total, Cliente cliente, TextField textField){
        switch (e.getCode()){
            case F1:
                if (!total.getText().isEmpty()){
                    Alerta.compra(stackPane,listView,infoBox,cliente.id,total,"CONTADO");
                }else {
                    Alerta.Dialog(stackPane,"Ups!","Debe de ingresar productos para poder hacer una compra");
                }
                textField.setText("2022000");
                break;
            case F2:

                if (cliente.hasCreditAvailable() && !total.getText().isEmpty()){
                    Alerta.compra(stackPane,listView,infoBox,cliente.id,total,"CREDITO");
                }else {
                    Alerta.Dialog(stackPane,"Ups!","No se puede realizar la compra dado que el cliente no cuenta con el crédito suficiente o no ha seleccionado ningún producto");
                }
                break;
            case F3:
                Alerta.mostrarComprasDeCliente(stackPane,cliente.nombre,cliente.id,textField);
                break;
            case F4:
                listView.getItems().clear();
                total.setText("");
                textField.setText("2022000");
                break;
            case F5:
                if (cliente.adeudo > 0){
                    Alerta.abonar(stackPane,cliente,listView,textField);
                } else {
                    Alerta.Dialog(stackPane,"Ups!","Este cliente no cuenta con adeudo");
                }
                break;
        }
    }
}
