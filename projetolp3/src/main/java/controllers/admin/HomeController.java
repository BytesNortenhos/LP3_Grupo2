package controllers.admin;

import Dao.*;
import Models.*;
import bytesnortenhos.projetolp3.App;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.Period;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class HomeController {
    @FXML
    private FlowPane mainContainer;
    @FXML
    private Label noRequestsLabel;
    public void initialize() {

        List<Registration> registrations = null;
        try {
            registrations = getPendingRegistrations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (registrations.isEmpty()) {
            showNoRequestsMessage();
        } else {
            displayRequests(registrations);
        }
    }


    private List<Registration> getPendingRegistrations() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<Registration> registrations = registrationDao.getRegistrations();
        return registrations;
    }

    private void showNoRequestsMessage() {
        noRequestsLabel.setVisible(true);
    }

    private void displayRequests(List<Registration> registrations) {
        noRequestsLabel.setVisible(false);
        mainContainer.setVisible(true);
        mainContainer.getChildren().clear();

        for (Registration registration : registrations) {
            VBox requestItem = createRequestItem(registration);
            mainContainer.getChildren().add(requestItem);
        }
    }

    private VBox createRequestItem(Registration request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(request.getAthlete().getName());
        nameLabel.getStyleClass().add("name-label");

        java.sql.Date birthDate = request.getAthlete().getDateOfBirth();
        LocalDate birthLocalDate = birthDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthLocalDate, currentDate).getYears();

        Label ageLabel = new Label("Idade: " + age);
        ageLabel.getStyleClass().add("text-label");

        Label sportLabel = new Label("Modalidade: " + request.getSport().getName());
        sportLabel.getStyleClass().add("text-label");

        Label countryLabel = new Label("País: " + request.getAthlete().getCountry().getName());
        countryLabel.getStyleClass().add("text-label");

        ImageView acceptImageView = new ImageView();
        URL iconAcceptURL = App.class.getResource("img/iconAccept.png");
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

        ImageView rejectImageView = new ImageView();
        URL iconRejectURL = App.class.getResource("img/iconReject.png");
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

        acceptButton.setOnAction(event -> {
            try {
                verifyTeam(request);
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        rejectButton.setOnAction(event -> {
            try {
                RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 2, 0);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Inscrição recusada com sucesso!");
                alert.show();
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(acceptButton, rejectButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10));

        requestItem.getChildren().addAll(nameLabel, ageLabel, sportLabel, countryLabel, buttonContainer);
        return requestItem;
    }
    public void verifyTeam(Registration request) throws SQLException{
        RegistrationDao registrationDao = new RegistrationDao();
        if(!registrationDao.verfiyTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport())){
            popWindow(request);
        }
        else{
            RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 3, registrationDao.getIdTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso!");
            alert.setHeaderText("Inscrição aprovada com sucesso na modalidade " + request.getSport().getName() + "!");
            alert.show();
        }
    }
    private void popWindow(Registration request) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Criar equipa");


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");


        Label minParticipantsLabel = new Label("Min Participants:");
        minParticipantsLabel.getStyleClass().add("popup-label");
        TextField minParticipantsField = new TextField();
        minParticipantsField.getStyleClass().add("popup-text");

        Label maxParticipantsLabel = new Label("Max Participants:");
        maxParticipantsLabel.getStyleClass().add("popup-label");
        TextField maxParticipantsField = new TextField();
        maxParticipantsField.getStyleClass().add("popup-text");

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("popup-button");
        submitButton.setOnAction(event -> {
            try {
                String minParticipantsText = minParticipantsField.getText();
                String maxParticipantsText = maxParticipantsField.getText();

                if (!minParticipantsText.matches("\\d+") || !maxParticipantsText.matches("\\d+")) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Por favor, insira apenas números nos campos de participantes!");
                    alerta.show();
                    return;
                }

                int minParticipants = Integer.parseInt(minParticipantsText);
                int maxParticipants = Integer.parseInt(maxParticipantsText);
                if(minParticipants < 2){
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("A equipa tem de ter um mínimo de participantes superior a 1!");
                    alerta.show();
                }
                teamCreate(request, minParticipants, maxParticipants);
                popupStage.close();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll( minParticipantsLabel, minParticipantsField, maxParticipantsLabel, maxParticipantsField, submitButton);

        Scene scene = new Scene(vbox, 300, 250);
        scene.getStylesheets().add(((URL) App.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    public void teamCreate(Registration request, int minParticipants, int maxParticipants) throws SQLException {
        TeamDao teamDao = new TeamDao();
        String g = "";
        if(request.getAthlete().getGenre().getIdGender()==1){
            g = "Men's";
        }else{
            g = "Women's";
        }
        String name = request.getAthlete().getCountry().getName() + " " + g + " " + request.getSport().getName() + " Team";
        teamDao.addTeam(new Team(0, name, request.getAthlete().getCountry(), request.getAthlete().getGenre(), request.getSport(), 2024, minParticipants, maxParticipants));
        RegistrationDao registrationDao = new RegistrationDao();
        RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 3, registrationDao.getIdTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport()));
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso!");
        alerta.setHeaderText("Equipa criada com sucesso!");
        alerta.show();
    }
}