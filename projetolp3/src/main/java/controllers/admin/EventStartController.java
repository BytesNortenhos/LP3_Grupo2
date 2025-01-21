package controllers.admin;

import Dao.EventDao;
import Dao.RegistrationDao;
import Dao.SportDao;
import Dao.SportEventDao;
import Models.Sport;
import Models.SportEvent;
import Utils.ErrorHandler;
import Utils.OpoUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventStartController {
    @FXML
    private ComboBox<Integer> yearComboBox;

    /**
     * Initializes the yearComboBox with the next available event year.
     *
     * @throws SQLException If there is an error fetching the event years from the database.
     */
    public void initialize() throws SQLException {
        EventDao eventDao = new EventDao();
        List<Integer> yearsToStart = eventDao.getEventsToStart();

        if (!yearsToStart.isEmpty()) {
            int nextYear = yearsToStart.get(0);
            yearComboBox.getItems().add(nextYear);
        }
    }

    /**
     * Starts a new event by verifying if the current event can be closed.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public void startEvent() throws SQLException {
        EventDao eventDao = new EventDao();
        int currentEvent = eventDao.getActualYearToStart();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Iniciar evento");
        alert.setHeaderText("Ao iniciar um novo evento, o evento atual será encerrado permanentemente!");
        alert.setContentText("Deseja iniciar um novo evento?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            verifyEvent(currentEvent);
        }
    }

    /**
     * Verifies if there are any registrations for the current event before proceeding.
     *
     * @param currentYear The year of the current event.
     * @throws SQLException If there is an error accessing the database.
     */
    public void verifyEvent(int currentYear) throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();

        if (registrationDao.verifyIfRegistrations(currentYear)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao iniciar evento");
            alert.setHeaderText("Não é possível iniciar um evento com atletas inscritos em competições!");
            alert.showAndWait();
        } else {
            int selectedEvent = yearComboBox.getSelectionModel().getSelectedItem();
            showModalitiesSelectionPopup(currentYear, selectedEvent);
        }
    }

    /**
     * Displays a popup to select modalities for the next event.
     *
     * @param currentYear The current event year.
     * @param nextYear The next event year.
     * @throws SQLException If there is an error accessing the database.
     */
    private void showModalitiesSelectionPopup(int currentYear, int nextYear) throws SQLException {
        SportEventDao sportEventDao = new SportEventDao();
        SportDao sportdao = new SportDao();
        List<Sport> availableSports = sportdao.getAvailableSports();

        VBox vbox = new VBox(10);
        ObservableList<CheckBox> checkBoxes = FXCollections.observableArrayList();

        for (Sport sport : availableSports) {
            CheckBox checkBox = new CheckBox(sport.getName() + " (ID: " + sport.getIdSport() + ")");
            checkBox.setUserData(sport.getIdSport());
            checkBoxes.add(checkBox);
            vbox.getChildren().add(checkBox);
        }

        CheckBox selectAll = new CheckBox("Selecionar todas as modalidades");
        selectAll.setOnAction(event -> {
            boolean selected = selectAll.isSelected();
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setSelected(selected);
            }
        });

        vbox.getChildren().add(0, selectAll);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        Button confirmButton = new Button("Confirmar");
        confirmButton.setOnAction(event -> {
            try {
                List<Integer> selectedSports = new ArrayList<>();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        selectedSports.add((Integer) checkBox.getUserData());
                    }
                }

                Alert confirmTransferAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmTransferAlert.setTitle("Confirmar");
                confirmTransferAlert.setHeaderText("Você realmente deseja transferir as modalidades para o próximo ano?");
                confirmTransferAlert.setContentText("Após a confirmação, as modalidades selecionadas serão adicionadas ao próximo evento.");

                Optional<ButtonType> buttonType = confirmTransferAlert.showAndWait();

                if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                    addSelectedSportsToNextYear(selectedSports, nextYear);

                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                    startNewEvent(currentYear, nextYear);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Erro ao adicionar modalidades ao próximo evento.");
            }
        });

        VBox layout = new VBox(10, scrollPane, confirmButton);
        Scene scene = new Scene(layout, 400, 500);
        Stage stage = new Stage();
        stage.setTitle("Selecionar Modalidades");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Adds the selected sports to the next year's event.
     *
     * @param selectedSports The list of selected sports' IDs.
     * @param nextYear The year for the next event.
     * @throws SQLException If there is an error accessing the database.
     */
    private void addSelectedSportsToNextYear(List<Integer> selectedSports, int nextYear) throws SQLException {
        SportEventDao sportEventDao = new SportEventDao();
        for (int idSport : selectedSports) {
            SportEvent sportEvent = new SportEvent(idSport, nextYear);
            int insertedId = sportEventDao.addSportEvent(sportEvent);
            if(insertedId == -1 || insertedId == 0) { System.out.println("> Erro ao obter id adicionado."); continue; }

            OpoUtils opoUtils = new OpoUtils();
            ErrorHandler errorHandler = opoUtils.addNovaProva(idSport, insertedId);
            if(!errorHandler.isSuccessful()) System.out.println("> Erro ao adicionar prova: " + errorHandler.getMessage());
        }
    }

    /**
     * Starts a new event and closes the current one.
     *
     * @param currentEvent The current event year.
     * @param selectedEvent The selected year for the new event.
     * @throws SQLException If there is an error accessing the database.
     */
    public void startNewEvent(int currentEvent, int selectedEvent) throws SQLException {
        EventDao eventDao = new EventDao();
        eventDao.startNewEvent(currentEvent, selectedEvent);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evento iniciado");
        alert.setHeaderText("O evento foi iniciado com sucesso!");
        alert.showAndWait();
    }

    /**
     * Displays an error alert with a given message.
     *
     * @param message The error message to display.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
