package controllers.athletes;

import Dao.AthleteDao;
import Dao.MedalDao;
import Dao.OlympicRecordDao;
import Dao.RegistrationDao;
import Models.Medal;
import Models.Registration;
import bytesnortenhos.projetolp3.Main;
import controllers.LoginController;
import controllers.ViewsController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeControllerAthlete {
    @FXML
    private Label goldMedals;
    @FXML
    private Label silverMedals;
    @FXML
    private Label bronzeMedals;
    @FXML
    private Label certificate;
    @FXML
    private Label textWelcome;
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
    @FXML
    private Label textSportsAthlete;
    @FXML
    private VBox sportsContainerAthlete;

    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    int idAthlete = 0;
    @FXML
    private SplitMenuButton theamsSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private ScrollPane scrollPaneAthlete;

    public void initialize() throws SQLException {
        sportsContainerAthlete.minHeightProperty().bind(scrollPaneAthlete.heightProperty());
        sportsContainerAthlete.minWidthProperty().bind(scrollPaneAthlete.widthProperty());
        scrollPaneAthlete.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        loadIcons();
        idAthlete = LoginController.idAthlete;
        displayWelcomeMessasge();
        displayMedals();
        theamsSplitButton.setOnMouseClicked(event -> theamsSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());

        List<List> registrations = null;
        try {
            registrations = getPendingRegistrations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!registrations.isEmpty()) {
            displayRequests(registrations);
        }
    }
    private List<List> getPendingRegistrations() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<List> registrations = registrationDao.getUserRegistration(idAthlete);
        return registrations;
    }
    private void displayRequests(List<List> registrations) {
        textSportsAthlete.setVisible(true);
        sportsContainerAthlete.setVisible(true);
        sportsContainerAthlete.getChildren().clear();
        sportsContainerAthlete.setSpacing(20);
        for (List registration : registrations) {
            VBox requestItem = createRequestItem(registration);
            sportsContainerAthlete.getChildren().add(requestItem);
        }
    }
    private VBox createRequestItem(List request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(20);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(request.get(6).toString());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label(request.get(7).toString());
        typeLabel.getStyleClass().add("type-label");

        requestItem.getChildren().addAll(nameLabel,typeLabel);
        return requestItem;
    }
    public void loadIcons(){
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if(iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if(iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);

    }

    private void displayWelcomeMessasge() throws SQLException{
        AthleteDao athleteDao = new AthleteDao();
        textWelcome.setText("Bem vindo "+athleteDao.getAthlheteNameByID(idAthlete)+"!");
    }
    private void displayMedals() throws SQLException {
        MedalDao medalDao = new MedalDao();
        OlympicRecordDao olympicRecordDao = new OlympicRecordDao();
        goldMedals.setText((medalDao.countGoldMedals(idAthlete))+" medalhas de ouro🥇");
        silverMedals.setText((medalDao.countSilverMedals(idAthlete))+" medalhas de prata🥈");
        bronzeMedals.setText((medalDao.countBronzeMedals(idAthlete))+" medalhas de bronze🥉");
        certificate.setText((medalDao.countCertificate(idAthlete))+" certificados📜");
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

    public void mostrarRegistaModalidadesEquipa(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/sportsTeamRegister.fxml")));
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