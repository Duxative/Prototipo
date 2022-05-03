package Components;

import DB.DBConnection;
import Extra.Alerta;
import Extra.Fila;
import Extra.InfoBox;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Cliente {

    public int id;
    public String nombre;
    public String apellidoP;
    public String apellidoM;
    public String puesto;
    public String contratista;
    public float creditoMax;
    public float adeudo;
    public String estado;

    static Connection con;

    public static void createClient(String nombre, String apellidoP, String apellidoM, String puesto, String contratista, float credito, float adeudo) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL createClient(?,?,?,?,?,?,?)}");
            cs.setString(1, nombre);
            cs.setString(2, apellidoP);
            cs.setString(3, apellidoM);
            cs.setString(4, puesto);
            cs.setString(5, contratista);
            cs.setFloat(6, credito);
            cs.setFloat(7, adeudo);
            cs.executeQuery();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getClients(ListView listView, VBox infoBox) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getClients()}");
            ResultSet rs = cs.executeQuery();
            listView.getItems().add(Fila.primeraFilaClientes());
            while (rs.next()) {
                listView.getItems().add(
                        new Fila(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getFloat(7),
                                rs.getFloat(8),
                                rs.getString(9),
                                infoBox,
                                listView
                        )
                );
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getClientByID(int id, Cliente current) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getClientByID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                current.id = rs.getInt(1);
                current.nombre = rs.getString(2);
                current.apellidoP = rs.getString(3);
                current.apellidoM = rs.getString(4);
                current.puesto = rs.getString(5);
                current.contratista = rs.getString(6);
                current.creditoMax = rs.getFloat(7);
                current.adeudo = rs.getFloat(8);
                current.estado = rs.getString(9);
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getVentasCredito(ListView notificationList){
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getSumOfCreditSales()}");
            ResultSet rs = cs.executeQuery();
            notificationList.getItems().addAll(
                    new Fila(
                            Fila.longLabel("Ventas a Credito")
                    ),
                    new Fila(
                            Fila.bigLabel("Semana"), Fila.bigLabel("Ventas")
                    )
            );
            while (rs.next()){
                notificationList.getItems().add(new Fila(
                        Fila.bigLabel(""+ rs.getInt(1)),
                        Fila.bigLabel(""+ rs.getInt(2))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getVentasContado(ListView notificationList){
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getSumOfCashSales()}");
            ResultSet rs = cs.executeQuery();
            notificationList.getItems().addAll(
                    new Fila(
                            Fila.longLabel("Ventas a Contado")
                    ),
                    new Fila(
                            Fila.bigLabel("Semana"), Fila.bigLabel("Ventas")
                    )
            );
            while (rs.next()){
                notificationList.getItems().add(new Fila(
                        Fila.bigLabel(""+ rs.getInt(1)),
                        Fila.bigLabel(""+ rs.getInt(2))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getAverageCustomerSpend(ListView notificationList){
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getAverageOfSalePerClient()}");
            ResultSet rs = cs.executeQuery();
            notificationList.getItems().addAll(
                    new Fila(
                            Fila.longLabel("Promedio de consumo individual")
                    ),
                    new Fila(
                            Fila.bigLabel("Semana"), Fila.bigLabel("Promedio")
                    )
            );
            while (rs.next()){
                notificationList.getItems().add(new Fila(
                        Fila.bigLabel(""+ rs.getInt(1)),
                        Fila.bigLabel(""+rs.getFloat(2))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getActiveClients(ListView notificationList){
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getSumOfActiveClients()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()){
                notificationList.getItems().add(new Fila(
                        Fila.longLabel("Clientes activos: "+ rs.getInt(1))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDebt(int idCliente, float compra) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL updateDebt(?,?)}");
            cs.setInt(1, idCliente);
            cs.setFloat(2, compra);
            cs.executeQuery();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateInfo(int idCliente, String nombre, String apellidoP, String apellidoM, String puesto, String contratista, float credito, float adeudo, String estado) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL updateInfoClient(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, idCliente);
            cs.setString(2, nombre);
            cs.setString(3, apellidoP);
            cs.setString(4, apellidoM);
            cs.setString(5, puesto);
            cs.setString(6, contratista);
            cs.setFloat(7, credito);
            cs.setFloat(8, adeudo);
            cs.setString(9, estado);
            cs.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean hasCreditAvailable() {
        return this.adeudo < this.creditoMax && creditoMax != 0;
    }


    public static void keyEvents(KeyEvent e, StackPane stackPane, ListView listView, VBox infoBox) {
        if (e.getCode() == KeyCode.F1) {
            Alerta.registrarCliente(stackPane, listView, infoBox);
        }
    }

    public static void readCodeClient(TextField barCodeReader, VBox infoBox, Cliente current) {

        ChangeListener<String> listener = (observableValue, o, t1) -> {
            if (barCodeReader.getText().matches("[0-9]*") && !barCodeReader.getText().isEmpty() && (t1.length() == 7)) {
                if (clientExist(t1)) {
                    infoBox.getChildren().clear();
                    getClientByID(Integer.parseInt(t1), current);
                    InfoBox.infoCliente(
                            infoBox,
                            current.id,
                            current.nombre,
                            current.apellidoP,
                            current.apellidoM,
                            current.puesto,
                            current.contratista,
                            current.creditoMax,
                            current.adeudo,
                            current.estado
                    );
                    Platform.runLater(barCodeReader::clear);
                }
            }
        };
        barCodeReader.textProperty().addListener(listener);
    }

    public static boolean clientExist(String id) {
        boolean clientExists = false;
        try {
            con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareCall("SELECT id FROM cliente");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == Integer.parseInt(id)) {
                    clientExists = true;
                    break;
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return clientExists;
    }

}
