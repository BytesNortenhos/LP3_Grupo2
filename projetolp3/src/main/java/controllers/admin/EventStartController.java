package controllers.admin;

import Dao.EventDao;
import Dao.RegistrationDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EventStartController {
    @FXML
    private ComboBox<Integer> yearComboBox;

    public void initialize() throws SQLException {
        EventDao eventDao = new EventDao();
        List<Integer> yearsToStart = eventDao.getEventsToStart();
        for(int year : yearsToStart) {
            yearComboBox.getItems().add(year);
        }
    }

    public void startEvent() throws SQLException {
        EventDao eventDao = new EventDao();
        int currentEvent = eventDao.getActualYear();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Iniciar evento");
        alert.setHeaderText("Ao iniciar um novo evento irá encerrar permanentemente o evento atual!");
        alert.setContentText("Deseja iniciar um novo evento?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            verifyEvent(currentEvent);
        }
    }

    public void verifyEvent(int currentYear) throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        if(registrationDao.verifyIfRegistrations(currentYear)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao iniciar evento");
            alert.setHeaderText("Não é possível iniciar um evento com atletas inscritos em competições!");
            alert.showAndWait();
        } else {
            int selectedEvent = yearComboBox.getSelectionModel().getSelectedItem();
            startNewEvent(currentYear, selectedEvent);
        }
    }
    public void startNewEvent(int currentEvent, int selectedEvent) throws SQLException {
        EventDao eventDao = new EventDao();
        eventDao.startNewEvent(currentEvent, selectedEvent);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evento iniciado");
        alert.setHeaderText("O evento foi iniciado com sucesso!");
        alert.showAndWait();
    }
}
