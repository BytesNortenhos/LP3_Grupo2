package controllers.athletes;

import Dao.SportDao;
import Dao.AthleteDao;
import Dao.RegistrationStatusDao;
import Models.Country;
import Models.Sport;
import Models.Athlete;
import Models.Team;
import Models.Registration;
import Models.RegistrationStatus;
import Dao.RegistrationDao;
import Models.Athlete;
import bytesnortenhos.projetolp3.Main;
import controllers.LoginController;
import controllers.ViewsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SportRegisterController {
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    private static boolean isDarkMode = true;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    @FXML
    private ImageView iconLogoutNav;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    @FXML
    private SplitMenuButton theamsSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private ComboBox sportsDrop;

    public void initialize() {
        loadIcons();
        loadSports();
        theamsSplitButton.setOnMouseClicked(event -> theamsSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
    }
    private void loadIcons() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);
    }
    private void loadSports() {
        try{
            sportsDrop.getItems().clear();
            SportDao sportDao = new SportDao();
            List<List> sports = sportDao.getSportsToShow();
            ObservableList<String> sportsOptions = FXCollections.observableArrayList();
            if(LoginController.gender.equals("Female")){
                sports.removeIf(sport -> sport.get(2).toString().equals("Male"));
            }
            else{
                sports.removeIf(sport -> sport.get(2).toString().equals("Female"));
            }
            for (List sport : sports) {
                sportsOptions.add(sport.get(3).toString());
            }
            sportsDrop.setItems(sportsOptions);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean changeMode(ActionEvent event){
        isDarkMode = !isDarkMode;
        if(isDarkMode){
            setDarkMode();
        }
        else{
            setLightMode();
        }
        return isDarkMode;
    }
    @FXML
    private void registerSport(ActionEvent event) {
        try {
            // Obter o esporte selecionado
            Sport selectedSport = getSelectedSport();
            if (selectedSport == null) {
                System.out.println("Nenhuma modalidade selecionada.");
                return;
            }

            // Obter o atleta logado usando o ID armazenado na sessão
            int athleteId = LoginController.idAthlete; // Acessando o athleteId da sessão
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(athleteId);

            if (athlete == null) {
                System.out.println("Atleta não encontrado.");
                return;
            }

            Team team = null;

            // Obter o status com ID 1
            RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();
            RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(1); // Status com id 1

            if (status == null) {
                System.out.println("Status não encontrado.");
                return;
            }

            Registration registration = new Registration(0, athlete, selectedSport, status); // idRegistration pode ser 0 ou gerado pelo banco de dados

            RegistrationDao.addRegistrationSolo(registration);

            System.out.println("Inscrição realizada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao realizar a inscrição.");
        }
    }
    private Sport getSelectedSport() {
        String selectedSportName = sportsDrop.getSelectionModel().getSelectedItem().toString();
        if (selectedSportName != null) {
            try {
                SportDao sportDao = new SportDao();
                for (Sport sport : sportDao.getSports()) {
                    if (sport.getName().equals(selectedSportName)) {
                        return sport;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setLightMode(){
        parent.getStylesheets().remove(cssDark);
        parent.getStylesheets().add(cssLight);
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconModeNav.setImage(image);
    }

    public void setDarkMode(){
        parent.getStylesheets().remove(String.valueOf(cssLight));
        parent.getStylesheets().add(String.valueOf(cssDark));
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconModeNav.setImage(image);
    }


    public void mostrarModalidades(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportsView.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    public void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void mostrarRegistaModalidades(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/sportsRegister.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void mostrarRegistaEquipas(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/teamsRegister.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void logout(ActionEvent event) throws Exception {
        mostrarLogin(event);
    }
    public void mostrarLogin(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/loginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
}
