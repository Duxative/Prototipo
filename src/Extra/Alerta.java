package Extra;

import Components.Cliente;
import Components.Producto;
import Components.Venta;
import com.jfoenix.controls.*;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Alerta {

    public static void Dialog(StackPane stackPane, String titulo, String texto) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(titulo));
        dialogLayout.setBody(new Text(texto));
        JFXButton aceptar = new JFXButton("Aceptar");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        aceptar.setOnAction(event -> dialog.close());
        dialogLayout.setActions(aceptar);
        dialog.show();
    }

    public static void registrarCliente(StackPane stackPane,ListView listView,VBox infoBox){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        TextField nombre = new TextField();
        nombre.setPromptText("Nombre(s)");
        TextField apellidoPaterno = new TextField();
        apellidoPaterno.setPromptText("Apellido Paterno");
        TextField apellidoMaterno = new TextField();
        apellidoMaterno.setPromptText("Apellido Materno");
        ComboBox<String> puestos = new ComboBox<>();
        puestos.getItems().addAll("Jornalero","Mayordomo","Ingeniero","Contratista","Secretaria","Visitante");
        ComboBox<String> contratista = new ComboBox<>();
        contratista.getItems().addAll("Campo","Rufino","Moro","Tienda","Don Gollo");

        JFXSlider credito = new JFXSlider();
        credito.setMax(1000);
        credito.setMin(0);
        credito.setOrientation(Orientation.HORIZONTAL);
        credito.setShowTickLabels(true);
        credito.setSnapToTicks(true);
        credito.setValue(300);

        vBox.getChildren().addAll(nombre,apellidoPaterno,apellidoMaterno,puestos,contratista,new Text("Credito:"),credito);
        contratista.setPrefWidth(250);
        contratista.setPromptText("Contratista");
        puestos.setPrefWidth(250);
        puestos.setPromptText("Puesto");

        dialogLayout.setHeading(new Text("REGISTRAR CLIENTE"));
        dialogLayout.setBody(vBox);

        JFXButton salir = new JFXButton("Salir");
        JFXButton aceptar = new JFXButton("Aceptar");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.CENTER);
        salir.setOnAction((e)->dialog.close());
        aceptar.setOnAction((e)-> {
                    Cliente.createClient(nombre.getText(), apellidoPaterno.getText(), apellidoMaterno.getText(),
                    puestos.getValue(),contratista.getValue(),(int)credito.getValue(),0
                    );

            dialog.close();
            listView.getItems().clear();
            infoBox.getChildren().clear();
            Cliente.getClients(listView,infoBox);

        });
        dialog.setOverlayClose(false);
        dialogLayout.setActions(salir,aceptar);
        dialog.show();
    }

    public static void registrarProducto(StackPane stackPane,ListView listView,VBox infoBox){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        VBox vBox = new VBox();
        vBox.setSpacing(20);

        TextField id = new TextField();
        id.setPromptText("Código de barra");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre");

        TextField cantidad = new TextField();
        cantidad.setPromptText("Cantidad de producto");

        TextField precio = new TextField();
        precio.setPromptText("Precio");

        TextField stock = new TextField();
        stock.setPromptText("cantidad mínima de producto");

        vBox.getChildren().addAll(id,nombre,cantidad,precio,stock);

        dialogLayout.setHeading(new Text("REGISTRAR PRODUCTO"));
        dialogLayout.setBody(vBox);

        JFXButton salir = new JFXButton("Salir");
        JFXButton aceptar = new JFXButton("Aceptar");
        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.CENTER);
        salir.setOnAction((e)->dialog.close());
        aceptar.setOnAction((e)-> {
            Producto.createProduct(
                    Long.parseLong(id.getText()),
                    nombre.getText(),
                    Integer.parseInt(cantidad.getText()),
                    Float.parseFloat(precio.getText()),
                    Integer.parseInt(stock.getText()),
                    0,
                    "ACTIVO"
            );
            dialog.close();
            listView.getItems().clear();
            infoBox.getChildren().clear();
            Producto.getProducts(listView,infoBox);
        });
        dialog.setOverlayClose(false);
        dialogLayout.setActions(salir,aceptar);
        dialog.show();
    }

    public static void compra(StackPane stackPane,ListView listView,VBox infoBox, int idCliente, Label total, String tipoDePago){

        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        Text heading = new Text("COMPRA A " + tipoDePago);
        Text body = new Text("¿Desea concretar la compra con un total de " + total.getText() +" ?");

        total.setText(total.getText().replace("$",""));
        heading.setFont(new Font(18));
        body.setFont(new Font(18));

        dialogLayout.setHeading(heading);
        dialogLayout.setBody(body);

        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton cancelar = new JFXButton("Cancelar");
        JFXButton aceptar = new JFXButton("Aceptar");

        cancelar.setOnMouseClicked(mouseEvent -> dialog.close());
        aceptar.setOnMouseClicked(mouseEvent -> {
            for (int i = 1; i < listView.getItems().size() ; i++) {
                Fila fila = (Fila) listView.getItems().get(i);
                Label idProducto = (Label) fila.getChildren().get(0);
                Label cantidadProducto = (Label) fila.getChildren().get(2);
                Venta.registrarVenta(idCliente,Long.parseLong(idProducto.getText()),Integer.parseInt(cantidadProducto.getText()),tipoDePago);
                Producto.updateStock(Long.parseLong(idProducto.getText()),Integer.parseInt(cantidadProducto.getText()));
                Producto.updatePopularidad(Long.parseLong(idProducto.getText()));
            }
            if (tipoDePago.equals("CREDITO")) {
                Cliente.updateDebt(idCliente,Float.parseFloat(total.getText()));
            }
            listView.getItems().clear();
            infoBox.getChildren().clear();
            total.setText("");
            dialog.close();
        });

        dialogLayout.setActions(cancelar,aceptar);
        dialog.setOverlayClose(false);
        dialog.show();
    }

    public static void mostrarComprasDeCliente(StackPane stackPane, String nombre, int idCliente, TextField textField){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        ListView<Fila> listView = new ListView<>();
        listView.setMinHeight(600);
        listView.setMinWidth(900);

        Venta.getComprasCliente(idCliente,listView,stackPane);

        Text heading = new Text("Historial de compras de:   " + nombre);
        heading.setFont(new Font(16));

        dialogLayout.setHeading(heading);
        dialogLayout.setBody(listView);

        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);

        JFXButton aceptar = new JFXButton("Aceptar");
        aceptar.setOnMouseClicked(mouseEvent -> {
            dialog.close();
            textField.setText("2022000");

        });

        dialogLayout.setActions(aceptar);
        dialog.show();
    }

    public static void abonar(StackPane stackPane, Cliente cliente, ListView listView, TextField textField){
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        Text heading = new Text("Abonar de " + cliente.nombre);
        heading.setFont(new Font(18));

        TextField abono = new TextField();
        abono.setPromptText("Abono");

        JFXDialog dialog = new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.CENTER);
        dialogLayout.setHeading(heading);
        dialogLayout.setBody(abono);

        JFXButton cancelar = new JFXButton("Cancelar");
        cancelar.setOnMouseClicked(mouseEvent -> {
            dialog.close();
        });
        JFXButton aceptar = new JFXButton("Aceptar");
        aceptar.setOnMouseClicked(mouseEvent -> {
            if (abono.getText().matches("[0-9]*") && !abono.getText().isEmpty()){
                Cliente.updateDebt(cliente.id, - Float.parseFloat(abono.getText()));
                listView.getItems().clear();
                textField.setText("2022000");
                dialog.close();
            } else{
                ContextMenu numbers = new ContextMenu();
                MenuItem menuItem = new MenuItem("sólo debe de ingresar números");
                numbers.setAutoHide(false);
                numbers.getItems().clear();
                numbers.getItems().addAll(menuItem);
                numbers.show(abono, Side.BOTTOM,0,0);
            }

        });
        dialogLayout.setActions(cancelar,aceptar);
        dialog.setOverlayClose(false);
        dialog.show();

    }

}
