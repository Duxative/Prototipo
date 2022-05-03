package DB;

import javafx.scene.layout.StackPane;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static Connection connection = null;

    public static Connection getConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/prototipo","root","4e7!#4es");
        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }


}
