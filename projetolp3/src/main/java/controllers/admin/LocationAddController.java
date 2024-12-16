package controllers.admin;

import Dao.EventDao;
import Dao.LocalDao;
import Models.Event;
import Models.Local;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class LocationAddController {
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField capacityTextField;
    @FXML
    private TextField constructionYearTextField;
    @FXML
    private ComboBox<Event> eventComboBox;
    EventDao eventDao = new EventDao();

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("Interior", "Exterior");

        try {
            List<Event> events = eventDao.getEvents();
            eventComboBox.getItems().addAll(events);
            eventComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Event event) {
                    return event != null ? event.getYear() + " - " + event.getCountry() : null;
                }

                @Override
                public Event fromString(String s) {
                    return null;
                }
            });
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao carregar eventos: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddLocal() {
        try {
            if (nameTextField.getText().isEmpty() || typeComboBox.getValue() == null ||
                    addressTextField.getText().isEmpty() || cityTextField.getText().isEmpty() ||
                    capacityTextField.getText().isEmpty() || constructionYearTextField.getText().isEmpty() ||
                    eventComboBox.getValue() == null) {
                showAlert("Erro", "Todos os campos são obrigatórios!", AlertType.ERROR);
                return;
            }

            String name = nameTextField.getText();
            String type = typeComboBox.getValue();
            String address = addressTextField.getText();
            String city = cityTextField.getText();

            if (containsNumbers(name)) {
                showAlert("Erro", "O nome do local não pode conter números!", AlertType.ERROR);
                return;
            }

            int capacity = Integer.parseInt(capacityTextField.getText());
            int constructionYear = Integer.parseInt(constructionYearTextField.getText());
            Event selectedEvent = eventComboBox.getValue();

            int currentYear = java.time.Year.now().getValue();
            if (constructionYear > currentYear) {
                showAlert("Erro", "O ano de construção não pode ser maior que o ano atual!", AlertType.ERROR);
                return;
            }

            if (capacity > 200000) {
                showAlert("Erro", "A capacidade não pode exceder 200.000!", AlertType.ERROR);
                return;
            }

            Local local = new Local(0, name, type, address, city, capacity, constructionYear, selectedEvent);

            LocalDao localdao = new LocalDao();
            if (localdao.addLocal(local)) {
                showAlert("Sucesso", "Local adicionado com sucesso!", AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Erro", "Falha ao adicionar o local!", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erro", "Capacidade e Ano de Construção devem ser números!", AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao adicionar local: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void clearFields() {
        nameTextField.clear();
        typeComboBox.setValue(null);
        addressTextField.clear();
        cityTextField.clear();
        capacityTextField.clear();
        constructionYearTextField.clear();
        eventComboBox.setValue(null);
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean containsNumbers(String text) {
        return text.matches(".*\\d.*");
    }
}
