package controllers.admin;

import Utils.ErrorHandler;
import Utils.OpoUtils;
import bytesnortenhos.projetolp3.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

public class SpectatorsController {

    @FXML
    private ImageView profileImage;

    @FXML
    private FlowPane showSpectatorsContainer;
    @FXML
    private Label noSportsLabel;

    @FXML
    private void initialize() throws SQLException {
        showSpectators();
    }
    private ErrorHandler getSpectators() throws SQLException {
        OpoUtils opoUtils = new OpoUtils();
        return opoUtils.getEspectadores();
    }
    private void showNoSpectatorsMessage() {
        noSportsLabel.setVisible(true);

    }
    public void showSpectators() throws SQLException {
        showSpectatorsContainer.getChildren().clear();
        ErrorHandler spectators = null;
        try {
            spectators = getSpectators();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (spectators.isSuccessful()) {
            displaySpectators(spectators);
        } else {
            showNoSpectatorsMessage();
        }
    }

    private void displaySpectators(ErrorHandler spectators) throws SQLException {
        noSportsLabel.setVisible(false);
        showSpectatorsContainer.setVisible(true);
        showSpectatorsContainer.getChildren().clear();
        for (Map<String, Object> spectator : spectators.getOpoData()) {
            String isActive = spectator.get("Active").toString();
            if(isActive.equals("true")) {
                VBox spectatorsItem = createTeamsItem(spectator);
                showSpectatorsContainer.getChildren().add(spectatorsItem);
            }
        }
    }
    private VBox createTeamsItem(Map<String, Object> spectator) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Text nameText = new Text(spectator.get("Name") != null ? spectator.get("Name").toString() : "N/A");
        nameText.getStyleClass().add("team-name");
        nameText.setWrappingWidth(300);

        Label emailLabel = new Label("Email: " + (spectator.get("Email") != null ? spectator.get("Email").toString() : "N/A"));
        emailLabel.getStyleClass().add("text-label");

        ImageView banImageView = new ImageView();
        URL iconBanURL = Main.class.getResource("img/iconBan.png");
        if (iconBanURL != null) {
            Image image = new Image(iconBanURL.toExternalForm());
            banImageView.setImage(image);
            banImageView.setFitWidth(50);
            banImageView.setFitHeight(50);
        }
        Button viewBanButton = new Button();
        viewBanButton.setGraphic(banImageView);
        viewBanButton.getStyleClass().add("startButton");
        viewBanButton.setOnAction(event -> {
            viewBan(spectator.get("Id").toString());
        });

        requestItem.getChildren().addAll(nameText, emailLabel, viewBanButton);
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
                showSpectators();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
