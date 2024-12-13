package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.Gender;
import Models.Sport;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
        typeLabel.getStyleClass().add("type-label");

        Label description = new Label(sport.get(4).toString());
        description.setWrapText(true);
        description.getStyleClass().add("description-label");


        Label genderLabel = new Label(sport.get(2).toString());
        genderLabel.getStyleClass().add("gender-label");

        Label minPart = new Label("Minímo de participantes: " + sport.get(5).toString());
        minPart.getStyleClass().add("minPart-label");

        Label scoringMeasure = new Label("Medida de pontuação: " + sport.get(6).toString());
        scoringMeasure.getStyleClass().add("scoringMeasure-label");

        Label oneGame = new Label("Quantidade de jogos: " + sport.get(7).toString());
        oneGame.getStyleClass().add("oneGame-label");

        requestItem.getChildren().addAll(nameLabel, description, typeLabel, genderLabel, minPart, scoringMeasure, oneGame);
        requestItem.setPrefWidth(500);
        return requestItem;
    }

}