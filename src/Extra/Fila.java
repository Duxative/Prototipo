package Extra;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Date;

public class Fila extends HBox {

    public Fila(){
        super();
    }

    public Fila(Node... children) {
        super(children);
    }

    public Fila(int id, String nombre, String apellidoP, String apellidoM, String puesto, String contratista, float credito, float adeudo, String estado, VBox infoBox, ListView listView) {
        super();
        getChildren().addAll(label(""+id),label(nombre),label(apellidoP),label(apellidoM),label(puesto),label(contratista),label(""+credito),label(adeudo+""),label(estado));

        float porcentaje = adeudo/credito;
        if (porcentaje >= 0.5 && porcentaje < 0.7){
            setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));
        }
        if (porcentaje > .7 && porcentaje < .85){
            setBackground(new Background(new BackgroundFill(Color.ORANGE,null,null)));
        }
        if (porcentaje > .85){
            setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        }

        setOnMouseClicked(mouseEvent -> InfoBox.editarCliente(infoBox, listView, id, nombre, apellidoP, apellidoM, puesto, contratista, credito, adeudo, estado));
    }

    public Fila(Long id, String nombre, int cantidad, float precioVenta, float popularidad, String estado, int stockBajo,VBox infoBox,ListView listView){
        super();
        agregarProducto(id,nombre,cantidad,precioVenta,popularidad,estado);
        stockColor(stockBajo,cantidad);
        setOnMouseClicked(mouseEvent -> InfoBox.editarProducto(infoBox,listView,id,nombre,cantidad,precioVenta,stockBajo,estado) );
    }

    public Fila(Long id,String nombre, float precio ){
        super();
        getChildren().addAll(
                bigLabel(""+id),bigLabel(nombre),bigLabel(""+(1)),bigLabel(""+precio)
        );
    }

    public Fila(String nombre, float popularidad){
        super();
        getChildren().addAll(
                bigLabel(nombre),bigLabel(popularidad+"")
        );
    }

    public static HBox primeraFilaClientes(){
        return new HBox(label("ID"),label("Nombre"),label("A. Paterno"),label("A. Materno"),label("Puesto"),label("Contratista"),label("Credito"),label("Adeudo"),label("Estado"));
    }
    public Fila(String view){
        super();
        getChildren().addAll(
                label("ID"),
                label("Nombre"),
                label("Cantidad"),
                label("Precio"),
                label("Popularidad"),
                label("Estado")
        );
        }

    public static Label label(String info){
        Label label = new Label(info);
        label.setMinWidth(90);
        label.setMaxWidth(90);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial",16));
        return label;
    }

    public static Label bigLabel(String info){
        Label label = new Label(info);
        label.setMinWidth(150);
        label.setMaxWidth(150);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial",20));
        return label;
    }
    public static Label longLabel(String text){
        Label label = new Label(text);
        label.autosize();
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial",20));
        return label;
    }

    public static Label labelHistorial(String info){
        Label label = new Label(info);
        label.setMinWidth(180);
        label.setMaxWidth(200);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial",16));
        return label;
    }
    public void agregarProducto(Long id, String nombre, int cantidad, float precioVenta, float popularidad, String estado){
        getChildren().addAll(
                label(""+id),
                label(nombre),
                label(""+cantidad),
                label(""+precioVenta),
                label(""+popularidad),
                label(estado)
        );
    }

    public Fila(Date fecha,  String nombre, String producto, int cantidad, float precio){
        super();
        getChildren().addAll(
          labelHistorial(fecha.toString()), labelHistorial(nombre), labelHistorial(producto),labelHistorial(""+cantidad), labelHistorial(precio+"")
        );
    }

    public void primeraFilaVenta(){
        getChildren().addAll(
              bigLabel("ID"),  bigLabel("Nombre"),bigLabel("Cantidad"),bigLabel("Precio")
        );
    }

    public void primerFilaHistorial(){
        getChildren().addAll(
            labelHistorial("Fecha"),labelHistorial("Cliente"),labelHistorial("Producto"),labelHistorial("Cantidad"),labelHistorial("Precio")
        );
    }
    public void stockColor(int stockBajo, int cantidad){
        if (stockBajo == cantidad){
            setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));
        }
        if (cantidad == 0){
            setBackground(new Background(new BackgroundFill(Color.RED,null,null)));

        }
    }

}