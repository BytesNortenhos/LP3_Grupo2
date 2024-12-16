package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.EventDao;
import Dao.GenderDao;
import Dao.RuleDao;
import Models.Gender;
import Dao.SportDao;
import Models.Rule;
import Models.Sport;
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

public class EditSportController {

    @FXML
    private ComboBox<Sport> sportsListDropdown;

    @FXML
    private TextField nameText, descText, minText;


    @FXML
    private ComboBox<String> genderDrop;
    @FXML
    private ComboBox<String> typeDrop;
    @FXML
    private ComboBox<String> scoringDrop;
    @FXML
    private ComboBox<String> oneGameDrop;


    @FXML
    private TextField resultMinText;
    @FXML
    private TextField resultMaxText;





    public void initialize() {
        loadTypes();
        loadOneGameOptions();
        loadAvailableSports();
    }

    @FXML
    private void loadAvailableSports() {
        try {
            List<Sport> sports = SportDao.getAllSportsV2();

            ObservableList<Sport> sportsOptions = FXCollections.observableArrayList(sports);
            sportsListDropdown.setItems(sportsOptions);
        } catch (SQLException e) {
            showAlert("Database Error", "Erro ao carregar esportes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onSportSelected() {
        Sport selectedSport = sportsListDropdown.getSelectionModel().getSelectedItem();

        if (selectedSport != null) {
            nameText.setText(selectedSport.getName());
            descText.setText(selectedSport.getDesc());
            minText.setText(String.valueOf(selectedSport.getMinParticipants()));
            scoringDrop.setValue(selectedSport.getScoringMeasure());


            typeDrop.setValue(selectedSport.getType());


            if (selectedSport.getGenre() != null) {
                if (selectedSport.getGenre().getIdGender() == 1) {
                    genderDrop.setValue("Masculino");
                } else if (selectedSport.getGenre().getIdGender() == 2) {
                    genderDrop.setValue("Feminino");
                }
            }


            oneGameDrop.setValue(selectedSport.getOneGame());


            resultMinText.setText(String.valueOf(selectedSport.getResultMin()));
            resultMaxText.setText(String.valueOf(selectedSport.getResultMax()));
        }
    }





    @FXML
    private void updateSport() {
        try {
            String name = nameText.getText();
            String type = typeDrop.getValue();
            String description = descText.getText();
            String minParticipantsText = minText.getText();
            String scoringMeasure = scoringDrop.getValue();
            String oneGame = oneGameDrop.getValue();
            String selectedGenderDesc = genderDrop.getValue();

            if (name.isEmpty() || type == null || selectedGenderDesc == null || description.isEmpty() || minParticipantsText.isEmpty() ||
                    scoringMeasure == null || oneGame == null) {
                showAlert("Erro de Validação", "Por favor, preencha todos os campos obrigatórios!", Alert.AlertType.ERROR);
                return;
            }

            if (name.length() > 50) {
                showAlert("Erro de Validação", "O nome da modalidade não pode ter mais de 50 caracteres!", Alert.AlertType.ERROR);
                return;
            }

            if (description.length() > 200) {
                showAlert("Erro de Validação", "A descrição da modalidade não pode ter mais de 200 caracteres!", Alert.AlertType.ERROR);
                return;
            }

            int minParticipants;
            try {
                minParticipants = Integer.parseInt(minParticipantsText);
                if (minParticipants < 2) {
                    showAlert("Erro de Validação", "O número mínimo de participantes deve ser pelo menos 2!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erro de Validação", "Formato inválido para o número mínimo de participantes!", Alert.AlertType.ERROR);
                return;
            }

            int genderId = selectedGenderDesc.equals("Masculino") ? 1 : 2;
            Gender selectedGender = new Gender(genderId, selectedGenderDesc);

            Sport selectedSport = sportsListDropdown.getSelectionModel().getSelectedItem();
            if (selectedSport == null) {
                showAlert("Erro de Validação", "Selecione uma modalidade para atualizar!", Alert.AlertType.ERROR);
                return;
            }

            int resultMin = 0;
            int resultMax = 0;

            try {
                resultMin = Integer.parseInt(resultMinText.getText());
                resultMax = Integer.parseInt(resultMaxText.getText());
                if (resultMin < 0 || resultMax < 0) {
                    throw new NumberFormatException("Os valores de resultado não podem ser negativos.");
                }
            } catch (NumberFormatException e) {
                showAlert("Erro de Validação", "Formato inválido ou valor de resultado inválido. Os valores de resultado devem ser números não negativos.", Alert.AlertType.ERROR);
                return;
            }

            if (resultMin > resultMax) {
                showAlert("Erro de Validação", "O valor mínimo do resultado não pode ser maior que o valor máximo!", Alert.AlertType.ERROR);
                return;
            }

            selectedSport.setName(name);
            selectedSport.setDesc(description);
            selectedSport.setMinParticipants(minParticipants);
            selectedSport.setType(type);
            selectedSport.getGenre().setIdGender(genderId);
            selectedSport.setScoringMeasure(scoringMeasure);
            selectedSport.setOneGame(oneGame);
            selectedSport.setResultMin(resultMin);
            selectedSport.setResultMax(resultMax);

            SportDao.updateSport(selectedSport);

            showAlert("Sucesso", "Modalidade atualizada com sucesso!", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erro na BD", "Ocorreu um erro ao acessar a BD: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void loadTypes() {
        ObservableList<String> types = FXCollections.observableArrayList("Individual", "Collective");
        typeDrop.setItems(types);
    }

    private void loadOneGameOptions() {
        ObservableList<String> gameOptions = FXCollections.observableArrayList("One", "Multiple");
        oneGameDrop.setItems(gameOptions);
    }
}
