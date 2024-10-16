package bytesnortenhos.projetolp2;
import Dao.ClientesDao;
import Utils.ConnectionsUtlis;
import controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Funcionario;
import models.Stand;


import javax.sql.rowset.CachedRowSet;
import java.net.URL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {
    private static final String LOGIN_VIEW_FXML = "LoginView.fxml";

    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {


        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(LOGIN_VIEW_FXML));
            URL cssStyles = this.getClass().getResource("css/main.css");

            if (cssStyles != null) {
                String css = ((URL) cssStyles).toExternalForm();
                Scene scene = createScene(loginView);
                scene.getStylesheets().add(css);
                primaryStage.setMaximized(true);

                primaryStage.setTitle("Olimpiadas");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                System.out.println("Arquivo CSS não encontrado.");

            }
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    private Scene createScene(Parent root) {
        return new Scene(root);
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {






       launch();
    }
}