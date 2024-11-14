package controllers;

import Dao.GenderDao;
import Dao.SportDao;
import Models.Gender;
import Models.Sport;
import bytesnortenhos.projetolp3.Main;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SportsController {
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    private static boolean isDarkMode = true;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;

    @FXML
    private FlowPane sportsContainer;
    @FXML
    private Label noSportsLabel;

    public void initialize() throws SQLException {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonNavStr = ((URL) iconMoonNavURL).toExternalForm();
        Image image = new Image(iconMoonNavStr);
        if (iconModeNav != null) iconModeNav.setImage(image);
        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        String iconHomeNavStr = ((URL) iconHomeNavURL).toExternalForm();
        image = new Image(iconHomeNavStr);
        if (iconHomeNav != null) iconHomeNav.setImage(image);
        athleteSplitButton.setOnMouseClicked(event -> {
            // Open the dropdown menu when clicking on the button's text
            athleteSplitButton.show();
        });
        sportSplitButton.setOnMouseClicked(mouseEvent -> {
            sportSplitButton.show();
        });
        List<Sport> sports = null;
        try{
            sports = getSports();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (sports.isEmpty()) {
            showNoSportsMessage();
        } else {
            displaySports(sports);
        }
    }
    private List<Sport> getSports() throws SQLException {
        return SportDao.getSports();
    }
    private void showNoSportsMessage() {
        noSportsLabel.setVisible(true);

    }
    private void displaySports(List<Sport> sports) throws SQLException {
        noSportsLabel.setVisible(false);
        sportsContainer.setVisible(true);

        // Cria e adiciona os itens de registro dinamicamente
        for (Sport sport : sports) {
            VBox sportsItem = createSportsItem(sport);
            sportsContainer.getChildren().add(sportsItem);

        }
    }
    private VBox createSportsItem(Sport sport) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(sport.getName());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label(sport.getType());
        typeLabel.getStyleClass().add("type-label");

        Label description = new Label(sport.getDesc());
        description.setWrapText(true);
        description.getStyleClass().add("description-label");


        Label genderLabel = new Label(sport.getGenre().getDesc());
        genderLabel.getStyleClass().add("gender-label");

        Label minPart = new Label("Minímo de participantes: " + sport.getMinParticipants());
        minPart.getStyleClass().add("minPart-label");

        Label scoringMeasure = new Label("Medida de pontuação: " + sport.getScoringMeasure());
        scoringMeasure.getStyleClass().add("scoringMeasure-label");

        Label oneGame = new Label("Quantidade de jogos: " + sport.getOneGame());
        oneGame.getStyleClass().add("oneGame-label");

        requestItem.getChildren().addAll(nameLabel, description, typeLabel, genderLabel, minPart, scoringMeasure, oneGame);
        requestItem.setPrefWidth(500); // Ensure this width allows two items per line
        return requestItem;
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
    public void mostrarRegistar(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/register.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportRegister.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
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
}