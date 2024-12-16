package controllers.admin;

import Dao.LocalDao;
import Models.Local;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

public class EditLocationController {

    @FXML
    private ComboBox<Local> locationsListDropdown;

    @FXML
    private TextField locationNameText;

    @FXML
    private TextField locationCapacityText;

    @FXML
    public void initialize() {
        loadAvailableLocals();
    }

    private void loadAvailableLocals() {
        try {
            LocalDao localDao = new LocalDao();
            List<Local> locals = localDao.getLocals();

            if (locals.isEmpty()) {
                showAlert("No Data", "Não há locais disponíveis.", AlertType.INFORMATION);
            } else {
                ObservableList<Local> localList = FXCollections.observableArrayList(locals);
                locationsListDropdown.setItems(localList);
                locationsListDropdown.setCellFactory(param -> new javafx.scene.control.ListCell<Local>() {
                    @Override
                    protected void updateItem(Local item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });
                locationsListDropdown.setButtonCell(new javafx.scene.control.ListCell<Local>() {
                    @Override
                    protected void updateItem(Local item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Erro ao carregar locais: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onLocationSelected() {
        Local selectedLocal = locationsListDropdown.getSelectionModel().getSelectedItem();

        if (selectedLocal != null) {
            locationNameText.setText(selectedLocal.getName());
            locationCapacityText.setText(String.valueOf(selectedLocal.getCapacity()));
        }
    }

    @FXML
    private void updateLocation() {
        String locationName = locationNameText.getText();
        String locationCapacityStr = locationCapacityText.getText();

        if (locationName.isEmpty() || locationCapacityStr.isEmpty()) {
            showAlert("Validation Error", "Por favor, preencha todos os campos.", AlertType.WARNING);
            return;
        }

        if (containsNumbers(locationName)) {
            showAlert("Validation Error", "O nome do local não pode conter números.", AlertType.WARNING);
            return;
        }

        int locationCapacity;
        try {
            locationCapacity = Integer.parseInt(locationCapacityStr);
            if (locationCapacity > 200000) {
                showAlert("Validation Error", "A capacidade não pode ser maior que 200.000.", AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Capacidade deve ser um número válido.", AlertType.WARNING);
            return;
        }

        Local selectedLocal = locationsListDropdown.getSelectionModel().getSelectedItem();
        if (selectedLocal != null) {
            try {
                selectedLocal.setName(locationName);
                selectedLocal.setCapacity(locationCapacity);

                LocalDao localDao = new LocalDao();
                boolean updated = localDao.updateLocal(selectedLocal);
                if (updated) {
                    showAlert("Success", "Local atualizado com sucesso!", AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Erro ao atualizar o local.", AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Erro ao atualizar o local: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    private boolean containsNumbers(String text) {
        return text.matches(".*\\d.*");
    }
}
