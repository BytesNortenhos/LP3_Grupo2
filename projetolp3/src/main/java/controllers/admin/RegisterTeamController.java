package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.*;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterTeamController {
    @FXML
    private ComboBox<Country> countryDrop;

    @FXML
    private ComboBox<Gender> genderDrop;
    @FXML
    private ComboBox<Sport> sportDrop;
    @FXML
    private TextField teamNameText, yearFoundedText, minParticipantsText, maxParticipantsText;


    public void initialize() {
        loadGenders();
        loadCountries();
        loadSports();

    }


    private void loadGenders() {
        try {
            List<Gender> genders = GenderDao.getGenders();

            genderDrop.setItems(FXCollections.observableArrayList(genders));

            genderDrop.setCellFactory(param -> new ListCell<Gender>() {
                @Override
                protected void updateItem(Gender gender, boolean empty) {
                    super.updateItem(gender, empty);
                    if (empty || gender == null) {
                        setText(null);
                    } else {
                        setText(gender.getDesc());
                    }
                }
            });

            genderDrop.setButtonCell(genderDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCountries() {
        try {
            List<Country> countries = CountryDao.getCountries();

            countryDrop.setItems(FXCollections.observableArrayList(countries));

            countryDrop.setCellFactory(param -> new ListCell<Country>() {
                @Override
                protected void updateItem(Country country, boolean empty) {
                    super.updateItem(country, empty);
                    if (empty || country == null) {
                        setText(null);
                    } else {
                        setText(country.getName());
                    }
                }
            });

            countryDrop.setButtonCell(countryDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSports() {
        try {
            SportDao sportdao = new SportDao();
            List<Sport> sports = sportdao.getAllSportsV2();

            sportDrop.setItems(FXCollections.observableArrayList(sports));


            sportDrop.setCellFactory(param -> new ListCell<Sport>() {
                @Override
                protected void updateItem(Sport sport, boolean empty) {
                    super.updateItem(sport, empty);
                    if (empty || sport == null) {
                        setText(null);
                    } else {
                        setText(sport.getName());
                    }
                }
            });

            sportDrop.setButtonCell(sportDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void registerTeam() {
        try {
            String name = teamNameText.getText();
            Country selectedCountry = countryDrop.getValue();
            Gender selectedGender = genderDrop.getValue();
            Sport selectedSport = sportDrop.getValue();
            int yearFounded = Integer.parseInt(yearFoundedText.getText());
            int minParticipants = Integer.parseInt(minParticipantsText.getText());
            int maxParticipants = Integer.parseInt(maxParticipantsText.getText());

            if (name.isEmpty() || selectedCountry == null || selectedGender == null || selectedSport == null) {
                showAlert("Validation Error", "Please fill all required fields!", Alert.AlertType.ERROR);
                return;
            }

            Team newTeam = new Team(
                    0,
                    name,
                    selectedCountry,
                    selectedGender,
                    selectedSport,
                    yearFounded,
                    minParticipants,
                    maxParticipants
            );

            TeamDao teamdao = new TeamDao();
            teamdao.addTeam(newTeam);

            showAlert("Success", "Team registered successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid number format!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving the team.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
