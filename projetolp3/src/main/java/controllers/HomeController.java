package controllers;

import bytesnortenhos.projetolp3.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class HomeController {
    @FXML
    private VBox mainContainer;
    @FXML
    private Label noRequestsLabel;
    @FXML
    private VBox requestsContainer;
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
    private SplitMenuButton splitMenuButton;
    @FXML
    private ComboBox<String> athleteDrop;
    public void initialize() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonNavStr = ((URL) iconMoonNavURL).toExternalForm();
        Image image = new Image(iconMoonNavStr);
        if(iconModeNav != null) iconModeNav.setImage(image);
        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        String iconHomeNavStr = ((URL) iconHomeNavURL).toExternalForm();
        image = new Image(iconHomeNavStr);
        if(iconHomeNav != null) iconHomeNav.setImage(image);
        List<Request> requests = getPendingRequests();

        if (requests.isEmpty()) {
            showNoRequestsMessage();
        } else {
            displayRequests(requests);
        }
        splitMenuButton.setOnMouseClicked(event -> {
            // Open the dropdown menu when clicking on the button's text
            splitMenuButton.show();
        });
    }
    public class Request {
        private final String name;
        private final int age;
        private final String sport;
        public Request(String name, int age, String sport) {
            this.name = name;
            this.age = age;
            this.sport = sport;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
        public String getSport() {
            return sport;
        }
    }
    private List<Request> getPendingRequests() {
        // Replace this with actual logic to fetch your requests
        return List.of(
                new Request("Cristiano Ronaldo", 39, "Futebol"),
                new Request("Rayssa Leal", 16, "Skate"),
                new Request("Cristiano Ronaldo", 39, "Futebol"),
                new Request("Cristiano Ronaldo", 39, "Futebol")
        );
    }
    private void showNoRequestsMessage() {
        noRequestsLabel.setVisible(true);
        requestsContainer.setVisible(false);
    }

    private void displayRequests(List<Request> requests) {
        noRequestsLabel.setVisible(false);
        requestsContainer.setVisible(true);

        // Create and add request nodes dynamically
        for (Request request : requests) {
            HBox requestItem = createRequestItem(request);
            requestsContainer.getChildren().add(requestItem);
        }
    }

    private HBox createRequestItem(Request request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        // Create the label for the name
        Label nameLabel = new Label(request.getName());
        nameLabel.getStyleClass().add("name-label");

        // Create the label for the age
        Label ageLabel = new Label("Idade: " + request.getAge());
        ageLabel.getStyleClass().add("age-label");

        Label sportLabel = new Label("Modalidade: " + request.getSport());
        sportLabel.getStyleClass().add("sport-label");

        // Create the accept button with an image
        ImageView acceptImageView = new ImageView();
        URL iconAcceptURL = Main.class.getResource("img/iconAccept.png");
        if (iconAcceptURL != null) {
            String iconAcceptStr = iconAcceptURL.toExternalForm();
            Image image = new Image(iconAcceptStr);
            acceptImageView.setImage(image);
            acceptImageView.setFitWidth(80);
            acceptImageView.setFitHeight(80);
        }
        Button acceptButton = new Button();
        acceptButton.setGraphic(acceptImageView);
        acceptButton.getStyleClass().add("acceptButton");

        // Create the reject button with an image
        ImageView rejectImageView = new ImageView();
        URL iconRejectURL = Main.class.getResource("img/iconReject.png");
        if (iconRejectURL != null) {
            String iconRejectStr = iconRejectURL.toExternalForm();
            Image image = new Image(iconRejectStr);
            rejectImageView.setImage(image);
            rejectImageView.setFitWidth(80);
            rejectImageView.setFitHeight(80);
        }
        Button rejectButton = new Button();
        rejectButton.setGraphic(rejectImageView);
        rejectButton.getStyleClass().add("rejectButton");

        // Place both buttons in an HBox
        HBox buttonContainer = new HBox(10); // Spacing between buttons (10px)
        buttonContainer.getChildren().addAll(acceptButton, rejectButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the center
        buttonContainer.setPadding(new Insets(10)); // Optional: Add padding around the buttons

        // Add the labels and button container to the request item
        requestItem.getChildren().addAll(nameLabel,sportLabel, ageLabel, buttonContainer);
        HBox container = new HBox(10); // Spacing between buttons (10px)
        container.setMinWidth(750);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(requestItem);
        return container;
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
    public static void returnHomeMenu(ActionEvent event) throws IOException {
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
