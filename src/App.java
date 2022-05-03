
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Views/Prototipo.fxml"));
        primaryStage.setTitle("Prototipo");
        primaryStage.setScene(new Scene(root,1600,900));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
