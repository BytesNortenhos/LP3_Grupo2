package bytesnortenhos.projetolp3;

import Models.*;

import java.sql.SQLException;

import Utils.XMLUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
         //TestXML.main();
        //AthleteDao.getAthletes();
        XMLUtils xmlUtils = new XMLUtils();
        xmlUtils.getTeamsDataXML();
        System.out.println("================");
        xmlUtils.getSportsDataXML();
        System.out.println("================");
        xmlUtils.getAthletesDataXML();
        System.out.println("================");
    }
}

/*public class Main extends Application {
    private static final String LOGIN_VIEW_FXML = "LoginView.fxml";

    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(LOGIN_VIEW_FXML));
            URL cssStyles = this.getClass().getResource("css/main.css");
            Parent loginView = loader.load();
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
        AthleteDao.getAthletes();
    }

    private Scene createScene(Parent root) {
        return new Scene(root);
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch();
    }
}*/