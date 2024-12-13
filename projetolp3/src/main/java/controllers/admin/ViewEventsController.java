package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.Event;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ViewEventsController {
    @FXML
    private FlowPane eventsContainer;
    @FXML
    private Label noEventsLabel;

    public void initialize() throws SQLException {
        List<Event> events = null;
        try {
            events = getEvents();  
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (events.isEmpty()) {
            showNoEventsMessage();
        } else {
            displayEvents(events);
        }
    }

    public List<Event> getEvents() throws SQLException {
        EventDao eventDao = new EventDao();
        return eventDao.getEvents();
    }


    private void showNoEventsMessage() {
        noEventsLabel.setVisible(true);

    }

    private void displayEvents(List<Event> events) throws SQLException {
        noEventsLabel.setVisible(false);
        eventsContainer.setVisible(true);
        eventsContainer.getChildren().clear();


        for (Event event : events) {

            VBox eventItem = createEventItem(event);


            eventsContainer.getChildren().add(eventItem);
        }
    }


    private VBox createEventItem(Event event) throws SQLException {
        VBox eventItem = new VBox();
        eventItem.setSpacing(10);
        eventItem.getStyleClass().add("request-item");

        HBox nameContainer = new HBox(10);
        String logoPath = event.getLogo();
        ImageView profileImage = new ImageView();
        URL iconImageURL = Main.class.getResource(logoPath);
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            profileImage.setImage(images);
            profileImage.setFitWidth(60);
            profileImage.setFitHeight(60);
            Circle clip = new Circle(30, 30, 30);
            profileImage.setClip(clip);
        }
        Label nameLabel = new Label("" + event.getYear());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);
        nameContainer.getChildren().addAll(profileImage, nameLabel);



        Label countryNameLabel = new Label("País: " + event.getCountry().getName());
        countryNameLabel.getStyleClass().add("name-label");


        Label continentLabel = new Label("Continente: " + event.getCountry().getContinent());
        continentLabel.getStyleClass().add("name-label");



        eventItem.getChildren().addAll(nameContainer, countryNameLabel, continentLabel);

        eventItem.setPrefWidth(500);

        return eventItem;
    }

}