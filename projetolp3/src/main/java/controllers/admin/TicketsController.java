package controllers.admin;

import Dao.ResultDao;
import Utils.ErrorHandler;
import Utils.OpoUtils;
import bytesnortenhos.projetolp3.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TicketsController {

    @FXML
    private ImageView profileImage;

    @FXML
    private FlowPane showGamesContainer;
    @FXML
    private Label noGamesLabel;

    @FXML
    private void initialize() throws SQLException {
        showGames();
    }
    private ErrorHandler getGames() throws SQLException {
        OpoUtils opoUtils = new OpoUtils();
        return opoUtils.getGames();
    }
    private ErrorHandler getTickets(String id) {
        OpoUtils opoUtils = new OpoUtils();
        return opoUtils.getTickets(id);
    }
    private void showNoGamesMessage() {
        noGamesLabel.setVisible(true);

    }
    public void showGames() throws SQLException {
        showGamesContainer.getChildren().clear();
        ErrorHandler games = null;
        try {
            games = getGames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (games.isSuccessful()) {
            displayGames(games);
        } else {
            showNoGamesMessage();
        }
    }

    private void displayGames(ErrorHandler games) throws SQLException {
        noGamesLabel.setVisible(false);
        showGamesContainer.setVisible(true);
        showGamesContainer.getChildren().clear();;
        for (Map<String, Object> game : games.getOpoData()) {
            String isActive = game.get("Active").toString();
            if(isActive.equals("true")) {
                VBox gamesItem = createGamesItem(game);
                showGamesContainer.getChildren().add(gamesItem);
            }
        }
    }
    private VBox createGamesItem(Map<String, Object> game) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Text nameText = new Text(game.get("Sport") != null ? game.get("Sport").toString() : "N/A");
        nameText.getStyleClass().add("team-name");
        nameText.setWrappingWidth(300);

        Label startDateLabel = new Label("Data de início: " + (game.get("StartDate") != null ? game.get("StartDate").toString() : "N/A"));
        startDateLabel.getStyleClass().add("text-label");

        Label endDateLabel = new Label("Data de fim: " + (game.get("EndDate") != null ? game.get("EndDate").toString() : "N/A"));
        endDateLabel.getStyleClass().add("text-label");

        Label locationLabel = new Label("Localização: " + (game.get("Location") != null ? game.get("Location").toString() : "N/A"));
        locationLabel.getStyleClass().add("text-label");

        Label capacityLabel = new Label("Capacidade: " + (game.get("Capacity") != null ? game.get("Capacity").toString() : "N/A"));
        capacityLabel.getStyleClass().add("text-label");

        ErrorHandler tickets = null;
        tickets = getTickets(game.get("Id").toString());

        if (tickets.isSuccessful()) {
            ImageView viewTicketsImageView = new ImageView();
            URL iconTicketsURL = Main.class.getResource("img/iconTickets.png");
            if (iconTicketsURL != null) {
                Image image = new Image(iconTicketsURL.toExternalForm());
                viewTicketsImageView.setImage(image);
                viewTicketsImageView.setFitWidth(50);
                viewTicketsImageView.setFitHeight(50);
            }
            Button viewTicketsButton = new Button();
            viewTicketsButton.setGraphic(viewTicketsImageView);
            viewTicketsButton.getStyleClass().add("startButton");
            ErrorHandler finalTickets = tickets;
            viewTicketsButton.setOnAction(event -> {
                popUpTickets(finalTickets);
            });

            requestItem.getChildren().addAll(nameText, startDateLabel, endDateLabel, locationLabel, capacityLabel, viewTicketsButton);
        }
        else{
            requestItem.getChildren().addAll(nameText, startDateLabel, endDateLabel, locationLabel, capacityLabel);
        }
        requestItem.setPrefWidth(500);

        return requestItem;
    }
    public void popUpTickets(ErrorHandler tickets)  {
        Stage popupStage = new Stage();
        popupStage.setTitle("Histórico de bilhetes");

        VBox vbox = new VBox(500);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        displayTickets(vbox, tickets);



        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    private void displayTickets(VBox vbox, ErrorHandler tickets) {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");
        for (Map<String, Object> ticket : tickets.getOpoData()) {
            VBox resultItem = createTicketsItem(ticket);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }
    private VBox createTicketsItem(Map<String, Object> ticket) {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        Label locationLabel = new Label("Localização: " + (ticket.get("Location") != null ? ticket.get("Location").toString() : "N/A"));
        locationLabel.getStyleClass().add("name-label");


        Label startDateLabel = new Label("Data de início: " + (ticket.get("StartDate") != null ? ticket.get("StartDate").toString() : "N/A"));
        startDateLabel.getStyleClass().add("text-label");

        Label endDateLabel = new Label("Data de fim: " + (ticket.get("EndDate") != null ? ticket.get("EndDate").toString() : "N/A"));
        endDateLabel.getStyleClass().add("text-label");

        Label seatLabel = new Label("Lugar: " + (ticket.get("Seat") != null ? ticket.get("Seat").toString() : "N/A"));
        seatLabel.getStyleClass().add("text-label");

        resultItem.getChildren().addAll(locationLabel, startDateLabel, endDateLabel, seatLabel);
        return resultItem;
    }
}
