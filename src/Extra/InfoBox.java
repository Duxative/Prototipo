package Extra;

import Components.Cliente;
import Components.Producto;
import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.nio.file.Paths;

public class InfoBox {

    public static void infoCliente(VBox infoBox, int id, String nombre, String apellidoP, String apellidoM, String puesto, String contratista, float credito, float adeudo, String estado) {
        infoBox.getChildren().clear();

        infoBox.getChildren().addAll(
          infoLabel("Número: "+ id),
          infoLabel("Estado: "+estado),
          infoLabel("Nombre: "+nombre + " " + apellidoP + " " + apellidoM),
          infoLabel("Contratista: "+ contratista),
          infoLabel("Credito: "+adeudo + "/ " + credito)

        );

        infoBox.setSpacing(30);
        infoBox.setAlignment(Pos.TOP_CENTER);
        infoBox.setPadding(new Insets(30,15,30,15));
    }

    public static void editarCliente(VBox infoBox, ListView listView, int id, String nombre, String apellidoP, String apellidoM, String puesto, String contratista, float credito, float adeudo, String estado) {
        infoBox.getChildren().clear();

        TextField tnombre = new TextField(nombre);
        tnombre.setDisable(true);
        tnombre.setPromptText("Nombre");

        TextField tapellidp = new TextField(apellidoP);
        tapellidp.setDisable(true);
        tapellidp.setPromptText("Apellido paterno");

        TextField tapellidoM = new TextField(apellidoM);
        tapellidoM.setDisable(true);
        tapellidoM.setPromptText("Apellido Materno");

        ComboBox<String> cpuesto = new ComboBox<>();
        cpuesto.getItems().addAll("Jornalero", "Mayordomo", "Ingeniero", "Contratista", "Secretaria", "Visitante");
        cpuesto.setDisable(true);
        cpuesto.setPrefWidth(250);
        cpuesto.getSelectionModel().select(cpuesto.getItems().indexOf(puesto));

        ComboBox<String> ccontratista = new ComboBox<>();
        ccontratista.getItems().addAll("Campo", "Rufino", "Moro", "Tienda", "Don Gollo");
        ccontratista.setDisable(true);
        ccontratista.setPrefWidth(250);
        ccontratista.getSelectionModel().select(ccontratista.getItems().indexOf(contratista));

        JFXSlider scredito = new JFXSlider();
        scredito.setMax(1000);
        scredito.setMin(0);
        scredito.setOrientation(Orientation.HORIZONTAL);
        scredito.setMajorTickUnit(100);
        scredito.setShowTickLabels(true);
        scredito.setSnapToTicks(true);
        scredito.setValue(credito);
        scredito.setDisable(true);

        CheckBox cestado = new CheckBox("ACTIVO");
        if (estado.equals("ACTIVO")) {
            cestado.setSelected(true);
        }
        cestado.setDisable(true);

        Button editar = new Button("Editar");
        editar.setPrefWidth(440);
        editar.setPrefHeight(50);

        Button aceptar = new Button("Aceptar");
        aceptar.setPrefWidth(440);
        aceptar.setPrefHeight(50);

        editar.setOnMouseClicked(mouseEvent -> {
            tnombre.setDisable(false);
            tapellidp.setDisable(false);
            tapellidoM.setDisable(false);
            cpuesto.setDisable(false);
            ccontratista.setDisable(false);
            scredito.setDisable(false);
            cestado.setDisable(false);
            infoBox.getChildren().remove(infoBox.getChildren().size() - 1);
            infoBox.getChildren().add(aceptar);
        });

        aceptar.setOnMouseClicked(mouseEvent -> {
            if (cestado.isSelected()) {
                Cliente.updateInfo(id,tnombre.getText(), tapellidp.getText(), tapellidoM.getText(), cpuesto.getValue(), ccontratista.getValue(), Float.parseFloat("" + scredito.getValue()), adeudo, "ACTIVO");
            } else {
                Cliente.updateInfo(id,tnombre.getText(), tapellidp.getText(), tapellidoM.getText(), cpuesto.getValue(), ccontratista.getValue(), Float.parseFloat("" + scredito.getValue()), adeudo, "INACTIVO");
            }
            infoBox.getChildren().clear();
            listView.getItems().clear();
            Cliente.getClients(listView,infoBox);

        });
        infoBox.getChildren().addAll(tnombre,tapellidp,tapellidoM,cpuesto,ccontratista,scredito,cestado,editar);
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(30,15,30,15));
    }



    public static void editarProducto(VBox infoBox, ListView listView, Long id, String nombre, int cantidad, float precioVenta, int stockBajo, String estado) {
        infoBox.getChildren().clear();

        TextField Tid = new TextField(""+id);
        Tid.setDisable(true);
        Tid.setPromptText("ID");

        TextField tnombre = new TextField(nombre);
        tnombre.setDisable(true);
        tnombre.setPromptText("Nombre");

        TextField tcantidad = new TextField(""+cantidad);
        tcantidad.setDisable(true);
        tcantidad.setPromptText("Cantidad");

        TextField tprecio = new TextField(""+precioVenta);
        tprecio.setDisable(true);
        tprecio.setPromptText("Precio");

        TextField stock = new TextField(""+stockBajo);
        stock.setDisable(true);
        stock.setPromptText("Alerta de producto bajo");

        CheckBox cestado = new CheckBox("ACTIVO");
        if (estado.equals("ACTIVO")) {
            cestado.setSelected(true);
        }
        cestado.setDisable(true);

        Button editar = new Button("Editar");
        editar.setPrefWidth(440);
        editar.setPrefHeight(50);

        Button aceptar = new Button("Aceptar");
        aceptar.setPrefWidth(440);
        aceptar.setPrefHeight(50);

        editar.setOnMouseClicked(mouseEvent -> {
            Tid.setDisable(false);
            tnombre.setDisable(false);
            tcantidad.setDisable(false);
            tprecio.setDisable(false);
            stock.setDisable(false);
            cestado.setDisable(false);
            infoBox.getChildren().remove(infoBox.getChildren().size() - 1);
            infoBox.getChildren().add(aceptar);
        });

        aceptar.setOnMouseClicked(mouseEvent -> {
            if (cestado.isSelected()) {
                Producto.updateProductInfo(Long.parseLong(Tid.getText()),tnombre.getText(),Integer.parseInt(tcantidad.getText()),Float.parseFloat(tprecio.getText()),Integer.parseInt(stock.getText()),"ACTIVO");
            } else {
                Producto.updateProductInfo(Long.parseLong(Tid.getText()),tnombre.getText(),Integer.parseInt(tcantidad.getText()),Float.parseFloat(tprecio.getText()),Integer.parseInt(stock.getText()),"INACTIVO");

            }
            infoBox.getChildren().clear();
            listView.getItems().clear();
            Producto.getProducts(listView,infoBox);

        });

        infoBox.getChildren().addAll(Tid,tnombre,tcantidad,tprecio,stock,cestado,editar);
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(30,15,30,15));

    }

    public static Label infoLabel(String info){
        Label text = new Label(info);
        text.setFont(new Font("ARIAL",24));
        text.setAlignment(Pos.BASELINE_LEFT);
        text.setPrefWidth(300);
        return text;
    }

}
