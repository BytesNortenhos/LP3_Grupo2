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

public class RegisterSportController {
    @FXML
    private TextArea rulesTextArea;

    @FXML
    private TextField minText;

    @FXML
    private TextField nameText;
    @FXML
    private TextField descText;


    private List<String> rules = new ArrayList<>();

    @FXML
    private ComboBox<String> genderDrop;
    @FXML
    private ComboBox<String> typeDrop;
    @FXML
    private ComboBox<String> scoringDrop;
    @FXML
    private ComboBox<String> oneGameDrop;

    public void initialize() {
        loadGenders();

        loadTypes();
        loadScoringMeasures();
        loadOneGameOptions();
    }

    private void loadGenders() {
        try {
            genderDrop.getItems().clear();

            List<Gender> genders = GenderDao.getGenders();
            ObservableList<String> genderOptions = FXCollections.observableArrayList();

            for (Gender gender : genders) {
                genderOptions.add(gender.getDesc());
            }

            genderDrop.setItems(genderOptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void registerSport() {
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

            Sport newSport = new Sport(
                    0,
                    type,
                    selectedGender,
                    name,
                    description,
                    minParticipants,
                    scoringMeasure,
                    oneGame,
                    null,
                    null,
                    null
            );

            int sportId = SportDao.addSport(newSport);

            if (rules.isEmpty()) {
                showAlert("Erro de Validação", "Por favor, adicione pelo menos uma regra!", Alert.AlertType.ERROR);
                return;
            }

            for (String ruleDesc : rules) {
                if (ruleDesc.isEmpty()) {
                    showAlert("Erro de Validação", "A descrição de cada regra não pode ser vazia!", Alert.AlertType.ERROR);
                    return;
                }

                if (ruleDesc.length() > 200) {
                    showAlert("Erro de Validação", "A descrição da regra não pode ter mais de 200 caracteres!", Alert.AlertType.ERROR);
                    return;
                }
            }

            for (String ruleDesc : rules) {
                Rule newRule = new Rule(0, sportId, ruleDesc);
                RuleDao.addRule(newRule);
            }

            rules.clear();
            showAlert("Sucesso", "Modalidade e regras registradas com sucesso!", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Erro na BD", "Ocorreu um erro ao acessar a BD: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void addRule() {
        String ruleDescription = rulesTextArea.getText().trim();

        if (ruleDescription.isEmpty()) {
            showAlert("Erro", "A descrição da regra não pode estar vazia!", Alert.AlertType.ERROR);
            return;
        }

        if (ruleDescription.length() > 200) {
            showAlert("Erro", "A descrição da regra não pode ter mais de 200 caracteres!", Alert.AlertType.ERROR);
            return;
        }

        if (rules.size() >= 10) {
            showAlert("Erro", "Você não pode adicionar mais de 10 regras!", Alert.AlertType.ERROR);
            return;
        }

        rules.add(ruleDescription);

        rulesTextArea.clear();

        showAlert("Sucesso", "Regra adicionada com sucesso!", Alert.AlertType.INFORMATION);
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
    private void loadScoringMeasures() {
        ObservableList<String> scoringMeasures = FXCollections.observableArrayList("Points", "Time");
        scoringDrop.setItems(scoringMeasures);
    }
    private void loadOneGameOptions() {
        ObservableList<String> gameOptions = FXCollections.observableArrayList("One", "Multiple");
        oneGameDrop.setItems(gameOptions);
    }
}
