package Controllers;

import Components.Cliente;
import Components.Producto;
import Components.Venta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private VBox infoBox;
    @FXML
    private ListView listView, notificationList;
    @FXML
    private TextField barCodeReader;
    @FXML
    private Label title,total,help;

    Cliente currentClient = new Cliente();
    Producto currentProduct = new Producto();

    public void keyPressedActions(KeyEvent e) {
       switch (title.getText()){
            case "VENTAS":
                Venta.ventasKeyEvents(e,listView,infoBox,stackPane,total,currentClient,barCodeReader);
                break;
            case "CLIENTES":
                Cliente.keyEvents(e,stackPane,listView,infoBox);
                break;
            case "INVENTARIO":
                Producto.inventarioKeyEvents(e,stackPane,listView,infoBox);
                break;
            default:
                break;
        }
    }

    public void clearAllFrames() {
        if (!listView.getItems().isEmpty()) {
            listView.getItems().clear();
        }
        if (!infoBox.getChildren().isEmpty()) {
            infoBox.getChildren().clear();
        }
        if (!notificationList.getItems().isEmpty()) {
            notificationList.getItems().clear();
        }
        total.setText("");
    }

    public void setClientes(MouseEvent e) {

        title.setText("CLIENTES");
        clearAllFrames();
        Cliente.getClients(listView,infoBox);
        barCodeReader.setVisible(false);
        notificationList.setMinHeight(293);
        total.setMinHeight(0);
        notificationList.setVisible(true);
        help.setText("F1: Registrar cliente");
        Cliente.getActiveClients(notificationList);
        Cliente.getAverageCustomerSpend(notificationList);
        Cliente.getVentasCredito(notificationList);
        Cliente.getVentasContado(notificationList);
    }

    public void setInventario(MouseEvent e) {

        title.setText("INVENTARIO");
        clearAllFrames();
        Producto.getProducts(listView,infoBox);
        barCodeReader.setVisible(false);
        notificationList.setMinHeight(293);
        total.setMinHeight(0);
        help.setText("F1: Registrar producto");
        notificationList.setVisible(true);
        Producto.getFiveMostPopular(notificationList);
        Producto.getFiveLessPopular(notificationList);

    }

    public void setVentas(MouseEvent e) {

        title.setText("VENTAS");
        clearAllFrames();
        barCodeReader.setVisible(true);
        barCodeReader.setText("2022000");
        Producto.suma = 0;
        notificationList.setMinHeight(94);
        notificationList.setVisible(false);
        total.setMinHeight(60);
        help.setText("F1: Compra a contado   F2: Compra a credito   F3: Ver historial de compras   F4: Reiniciar compra   F5: Abonar");



    }

    public void exit(MouseEvent e) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Cliente.readCodeClient(barCodeReader,infoBox,currentClient);
        Producto.readCodeProduct(barCodeReader,listView,currentProduct,total);
        barCodeReader.setVisible(false);
    }
}
