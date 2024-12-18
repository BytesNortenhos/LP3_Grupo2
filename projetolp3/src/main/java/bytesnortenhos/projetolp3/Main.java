package bytesnortenhos.projetolp3;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.Team;
import Utils.XMLUtils;
import controllers.admin.HomeController;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import static javafx.application.Platform.exit;

//public class Main {
//    public void main(String[] args) throws SQLException, ClassNotFoundException {
//        AthleteDao.getAthletes();
//    }
//}

public class Main extends Application {
    private static final String LOGIN_VIEW_FXML = "loginView.fxml";
    @Override

    /**
     * Start method
     * @param primaryStage {Stage} Primary stage
     * @throws SQLException, ClassNotFoundException
     */
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(LOGIN_VIEW_FXML));
            URL cssStyles = this.getClass().getResource("css/dark.css");
            Parent loginView = loader.load();
            if (cssStyles != null) {
                Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                String css = ((URL) cssStyles).toExternalForm();
                Scene scene = new Scene(loginView, screenSize.getWidth(), screenSize.getHeight());
                scene.getStylesheets().add(css);

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

    /**
     * Create scene
     * @param root {Parent} Root
     * @return Scene
     */
    private Scene createScene(Parent root) {
        return new Scene(root);
    }


    /**
     * Main method
     * @param args {String[]} Arguments
     * @throws SQLException, ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Dotenv dotenv = Dotenv.load();

        if(
            dotenv.get("DB_HOST") == "" ||
            dotenv.get("DB_NAME") == "" ||
            dotenv.get("DB_USER") == "" ||
            dotenv.get("DB_PASS") == ""
        ) {
            System.out.println("> Ficheiro .ENV não configurado!");
            exit();
        } else {
            launch();
        }
    }
}