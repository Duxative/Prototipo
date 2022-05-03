package Components;

import DB.DBConnection;
import Extra.Alerta;
import Extra.Fila;
import Extra.InfoBox;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Producto {

    public Long id;
    public String nombre;
    public int cantidad;
    public float precioVenta;
    public int stockBajo;
    public float popularidad;
    public String estado;

    public static float suma = 0;

    static Connection con;

    public Producto() {
        super();
    }

    public static void createProduct(Long id, String nombre, int cantidad, float venta, int stock, float popularidad, String estado) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL createProduct(?,?,?,?,?,?,?)}");
            cs.setLong(1, id);
            cs.setString(2, nombre);
            cs.setInt(3, cantidad);
            cs.setFloat(4, venta);
            cs.setInt(5, stock);
            cs.setFloat(6, popularidad);
            cs.setString(7, estado);

            cs.executeQuery();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public static void getProducts(ListView listView, VBox infoBox) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getProducts()}");
            ResultSet rs = cs.executeQuery();
            Fila prueba = new Fila("");

            listView.getItems().addAll(prueba);
            while (rs.next()) {
                listView.getItems().add(new Fila(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getFloat(4),
                        rs.getFloat(6),
                        rs.getString(7),
                        rs.getInt(5),
                        infoBox,
                        listView
                ));
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getProductByID(Long id, Producto current) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getProductsByID(?)}");
            cs.setLong(1, id);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                current.id = rs.getLong(1);
                current.nombre = rs.getString(2);
                current.cantidad = rs.getInt(3);
                current.precioVenta = rs.getFloat(4);
                current.stockBajo = rs.getInt(5);
                current.popularidad = rs.getFloat(6);
                current.estado = rs.getString(7);
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getFiveMostPopular(ListView notificationList) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getFIveMostPopular()}");
            ResultSet rs = cs.executeQuery();
            notificationList.getItems().addAll(
                    new Fila(Fila.longLabel("Productos más populares")),
                    new Fila(Fila.bigLabel("Producto"), Fila.bigLabel("Popularidad")));
            while (rs.next()) {
                notificationList.getItems().add(
                        new Fila(
                                rs.getString(1), rs.getFloat(2)
                        )
                );
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getFiveLessPopular(ListView notificationList) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL getFiveLessPopular()}");
            ResultSet rs = cs.executeQuery();
            notificationList.getItems().addAll(
              new Fila( Fila.longLabel("Productos menos populares")),
              new Fila(Fila.bigLabel("Producto"), Fila.bigLabel("Popularidad"))
            );
            while (rs.next()) {
                notificationList.getItems().add(
                        new Fila(
                                rs.getString(1), rs.getFloat(2)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProductInfo(Long id, String nombre, int cantidad, float precioVenta, int stockBajo, String estado) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL updateInfoProduct(?,?,?,?,?,?)}");
            cs.setLong(1, id);
            cs.setString(2, nombre);
            cs.setInt(3, cantidad);
            cs.setFloat(4, precioVenta);
            cs.setInt(5, stockBajo);
            cs.setString(6, estado);

            cs.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void updateStock(Long id, int cambio) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL updateStock(?,?)}");
            cs.setLong(1, id);
            cs.setInt(2, cambio);
            cs.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void inventarioKeyEvents(KeyEvent e, StackPane stackPane, ListView listView, VBox infobox) {
        switch (e.getCode()) {
            case F1:
                Alerta.registrarProducto(stackPane, listView, infobox);
                break;
        }
    }

    public static void readCodeProduct(TextField barCodeReader, ListView listView, Producto current, Label total) {
        barCodeReader.textProperty().addListener((observableValue, s, t1) -> {
            if (barCodeReader.getText().matches("[0-9]*") && !barCodeReader.getText().isEmpty()) {
                if (productExists(t1)) {

                    if (listView.getItems().isEmpty()) {
                        Fila primera = new Fila();
                        primera.primeraFilaVenta();
                        listView.getItems().add(primera);
                    }

                    getProductByID(Long.parseLong(t1), current);
                    listView.getItems().addAll(new Fila(current.id, current.nombre, current.precioVenta));
                    suma += current.precioVenta;
                    Platform.runLater(barCodeReader::clear);
                    total.setText("$" + suma);
                    total.setMinWidth(440);
                    total.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    public static void updatePopularidad(Long idp) {
        try {
            con = DBConnection.getConnection();
            CallableStatement cs = con.prepareCall("{CALL updatePopularidad(?)}");
            cs.setLong(1, idp);
            cs.executeQuery();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean productExists(String id) {
        boolean productExists = false;
        try {
            con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareCall("SELECT id FROM producto");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong(1) == Long.parseLong(id)) {
                    productExists = true;
                    break;
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return productExists;
    }

}
