package bytesnortenhos.projetolp3;

import java.sql.SQLException;

import AuxilierXML.Athletes;
import AuxilierXML.Teams;
import AuxilierXML.Sports;
import AuxilierXML.UploadXmlDAO;
import Utils.XMLUtils;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
         //TestXML.main();
        //AthleteDao.getAthletes();
        XMLUtils xmlUtils = new XMLUtils();



        //-> Falta:
        //System.out.println(xmlUtils.validateXML("sports"));
        Sports sports = xmlUtils.getSportsDataXML();
        System.out.println(UploadXmlDAO.addSports(sports));

        //-> Feito:
        //System.out.println(xmlUtils.validateXML("athletes"));
        Athletes athletes = xmlUtils.getAthletesDataXML();
        System.out.println(UploadXmlDAO.addAthletes(athletes));

        //System.out.println(xmlUtils.validateXML("teams"));
        Teams teams = xmlUtils.getTeamsDataXML();
        System.out.println(UploadXmlDAO.addTeams(teams));

        //->
        System.out.println(UploadXmlDAO.addSportsExtra(sports));
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