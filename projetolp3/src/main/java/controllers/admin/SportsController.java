package controllers.admin;

import Dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class SportsController {
    @FXML
    private FlowPane sportsContainer;
    @FXML
    private Label noSportsLabel;

    public void initialize() throws SQLException {
        List<List> sports = null;
        try {
            sports = getSports();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (sports.isEmpty()) {
            showNoSportsMessage();
        } else {
            displaySports(sports);
        }
    }


    private List<List> getSports() throws SQLException {
        SportDao sportDao = new SportDao();
        return sportDao.getSportsToShow();
    }

    private void showNoSportsMessage() {
        noSportsLabel.setVisible(true);

    }

    private void displaySports(List<List> sports) throws SQLException {
        noSportsLabel.setVisible(false);
        sportsContainer.setVisible(true);
        sportsContainer.getChildren().clear();
        for (List sport : sports) {
            VBox sportsItem = createSportsItem(sport);
            sportsContainer.getChildren().add(sportsItem);

        }
    }

    private VBox createSportsItem(List sport) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(sport.get(3).toString());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label(sport.get(1).toString());
        typeLabel.getStyleClass().add("text-label");

        Label description = new Label(sport.get(4).toString());
        description.setWrapText(true);
        description.getStyleClass().add("text-label");


        Label genderLabel = new Label(sport.get(2).toString());
        genderLabel.getStyleClass().add("text-label");

        Label minPart = new Label("Minímo de participantes: " + sport.get(5).toString());
        minPart.getStyleClass().add("text-label");

        Label scoringMeasure = new Label("Medida de pontuação: " + sport.get(6).toString());
        scoringMeasure.getStyleClass().add("text-label");

        Label oneGame = new Label("Quantidade de jogos: " + sport.get(7).toString());
        oneGame.getStyleClass().add("text-label");

        requestItem.getChildren().addAll(nameLabel, description, typeLabel, genderLabel, minPart, scoringMeasure, oneGame);
        requestItem.setPrefWidth(500);
        return requestItem;
    }

}