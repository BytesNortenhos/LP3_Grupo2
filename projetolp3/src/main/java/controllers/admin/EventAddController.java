package controllers.admin;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import Dao.CountryDao;
import Dao.EventDao;
import Models.Country;
import Models.Event;
import bytesnortenhos.projetolp3.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventAddController {
    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    private String logoPath = "";

    @FXML
    public void initialize() throws SQLException {
        List<Country> countries = null;
        try {
            countries = CountryDao.getCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        countryComboBox.getItems().addAll(countries);
        EventDao eventDao = new EventDao();
        int currentYear = eventDao.getActualYear() + 1;
        for (int i = currentYear; i <= currentYear + 100; i++) {
            yearComboBox.getItems().add(i);
        }
        yearComboBox.setValue(currentYear);
    }

    @FXML
    public void updateImageEvent(ActionEvent event) throws SQLException, IOException {
        String pathSave = "src/main/resources/bytesnortenhos/projetolp3/ImagesEvent/";
        String pathSaveTemp = App.class.getResource("ImagesEvent").toExternalForm().replace("file:", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");
        String pathToSave = "ImagesEvent/";

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        Integer selectedYear = yearComboBox.getValue();

        if (selectedFiles != null) {
            long pngCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".png")).count();
            long jpgCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpg")).count();
            long jpegCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpeg")).count();

            if (selectedFiles.size() == 1 && (pngCount == 1 || jpgCount == 1 || jpegCount == 1)) {
                File selectedFile = selectedFiles.get(0);

                boolean saved = saveEventImage(selectedYear, pathSave, pathSaveTemp, selectedFile.getAbsolutePath());
                if (saved) {
                    logoPath = pathToSave + selectedYear + "." + selectedFile.getName().split("\\.")[1];
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Ocorreu um erro ao guardar a imagem!");
                    alerta.show();
                }

            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("Selecione 1 ficheiro! Extensões válidas: .png, .jpeg, .jpg");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("Selecione 1 ficheiro! Extensões válidas: .png, .jpeg, .jpg");
            alerta.show();
        }
    }


    public boolean saveEventImage(int selectedYear, String pathSave, String pathSaveTemp, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(selectedYear) + "." + fileImage.getName().split("\\.")[1];
        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(fileImage.toPath(), Path.of(pathSaveTemp, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }

        return true;
    }


    @FXML
    private void handleAddEvent() {
        Integer selectedYear = yearComboBox.getValue();
        Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();

        if (selectedYear == null || selectedCountry == null || logoPath.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios!", AlertType.ERROR);
            return;
        }

        try {
            Event event = new Event(selectedYear, selectedCountry, logoPath);
            EventDao eventdao = new EventDao();
            boolean success = eventdao.addEvent(event);

            if (success) {
                showAlert("Sucesso", "Evento adicionado com sucesso!", AlertType.INFORMATION);

                yearComboBox.setValue(null);
                countryComboBox.getSelectionModel().clearSelection();
                logoPath = "";
            } else {
                showAlert("Erro", "Já existe um evento para o ano escolhido!", AlertType.ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao adicionar o evento:\n" + e.getMessage(), AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

