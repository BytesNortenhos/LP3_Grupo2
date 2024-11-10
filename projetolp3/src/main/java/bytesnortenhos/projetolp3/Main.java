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
        AthleteDao.getAthletes();
        //XMLUtils xmlUtils = new XMLUtils();

        //System.out.println(xmlUtils.validateXML("sports"));
        //Sports sports = xmlUtils.getSportsDataXML();
        //System.out.println(UploadXmlDAO.addSports(sports));

        //System.out.println(xmlUtils.validateXML("athletes"));
        //Athletes athletes = xmlUtils.getAthletesDataXML();
        //System.out.println(UploadXmlDAO.addAthletes(athletes));

        //System.out.println(xmlUtils.validateXML("teams"));
        //Teams teams = xmlUtils.getTeamsDataXML();
        //System.out.println(UploadXmlDAO.addTeams(teams));

        //System.out.println(UploadXmlDAO.addSportsExtra(sports));
    }
}


public class Main extends Application {
    private static final String LOGIN_VIEW_FXML = "loginView.fxml";
    @Override
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
        /*AdminDao.getAdmins();
        AthleteDao.getAthletes();
        CountryDao.getCountries();
        EventDao.getEvents();
        GenderDao.getGenders();
        LocalDao.getLocals();
        MedalDao.getMedals();
        MedalTypeDao.getMedalTypes();
        OlympicRecordDao.getOlympicRecords();
        RegistrationDao.getRegistrations();
        RegistrationStatusDao.getRegistrationStatuses();
        ResultDao.getResults();
        RuleDao.getRules();
        SportDao.getSports();
        TeamDao.getTeams();
        WinnerOlympicDao.getWinnerOlympics();
        System.out.println("Correu!");*/
    }

    private Scene createScene(Parent root) {
        return new Scene(root);
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch();
    }
}