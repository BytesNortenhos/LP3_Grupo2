package controllers.admin;

import Utils.ErrorHandler;
import Utils.OpoUtils;
import bytesnortenhos.projetolp3.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
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

        requestItem.getChildren().addAll(nameText, startDateLabel, endDateLabel, locationLabel, capacityLabel);
        requestItem.setPrefWidth(500);

        return requestItem;
    }
    public void viewBan(String id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Banir espectador");
        alert.setHeaderText("Tem certeza que deseja banir este espectador?");
        alert.setContentText("Esta ação não pode ser desfeita.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            OpoUtils opoUtils = new OpoUtils();
            opoUtils.banEspectador(id);
            try {
                showGames();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
