package controllers.admin;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.CountryDao;
import Dao.EventDao;
import Models.Country;
import Models.Event;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
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
import javafx.util.StringConverter;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class EventAddController {
    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    private String logoPath = "";

    @FXML
    public void initialize() {
        List<Country> countries = null;
        try {
            countries = CountryDao.getCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        countryComboBox.getItems().addAll(countries);

        int currentYear = java.time.LocalDate.now().getYear();
        for (int i = currentYear; i <= currentYear + 100; i++) {
            yearComboBox.getItems().add(i);
        }
        yearComboBox.setValue(currentYear);
    }

    @FXML
    private void updateImageEvent(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher Logo do Evento");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Arquivos de Imagem (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Integer selectedYear = yearComboBox.getValue();
            Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();

            if (selectedYear != null && selectedCountry != null) {
                String extension = getFileExtension(selectedFile);
                String newFileName = selectedYear + extension;

                String destinationFolder = "src/main/java/ImagesEvent";
                Path destinationPath = Path.of(destinationFolder, newFileName);

                try {
                    Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);


                    logoPath = destinationPath.toString();

                    showAlert("Sucesso", "Imagem selecionada com sucesso!", AlertType.INFORMATION);

                } catch (IOException e) {
                    Alert alerta = new Alert(AlertType.ERROR);
                    alerta.setTitle("Erro ao copiar imagem");
                    alerta.setHeaderText("Não foi possível copiar a imagem para o diretório.");
                    alerta.setContentText("Erro: " + e.getMessage());
                    alerta.show();
                }
            }
        } else {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Nenhum arquivo de imagem selecionado");
            alerta.show();
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
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
            showAlert("Erro", "Erro ao adicionar o evento ao banco de dados:\n" + e.getMessage(), AlertType.ERROR);
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

