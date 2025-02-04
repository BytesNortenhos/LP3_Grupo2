package controllers.admin;

import Dao.*;
import Models.Gender;
import Models.Local;
import Models.Rule;
import Models.Sport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RegisterSportController {

    @FXML
    private Spinner<Integer> startHourSpinner;
    @FXML
    private Spinner<Integer> startMinuteSpinner;
    @FXML
    private Spinner<Integer> endHourSpinner;
    @FXML
    private Spinner<Integer> endMinuteSpinner;
    @FXML
    private TextField rulesTextArea;

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
    private ComboBox<String> oneGameDrop;
    @FXML
    private ComboBox<String> scoringDrop;
    @FXML
    private ComboBox<String> metricDrop;
    @FXML
    private DatePicker startDataPicker;
    @FXML
    private DatePicker endDataPicker;
    @FXML
    private ComboBox<LocalWrapper> localDrop;
    @FXML
    private TextField scoreMinText;
    @FXML
    private TextField scoreMaxText;

    public void initialize() {
        loadGenders();
        loadLocals();
        loadTypes();
        loadOneGameOptions();
        SpinnerValueFactory<Integer> hourFactoryStart = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        SpinnerValueFactory<Integer> hourFactoryEnd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        startHourSpinner.setValueFactory(hourFactoryStart);
        endHourSpinner.setValueFactory(hourFactoryEnd);

        SpinnerValueFactory<Integer> minuteFactoryStart = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> minuteFactoryEnd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        startMinuteSpinner.setValueFactory(minuteFactoryStart);
        endMinuteSpinner.setValueFactory(minuteFactoryEnd);
    }

    private void loadGenders() {
        try {
            genderDrop.getItems().clear();
            List<Gender> genders = GenderDao.getGenders();
            ObservableList<String> genderOptions = FXCollections.observableArrayList();

            for (Gender gender : genders) {
                if (gender.getDesc().equalsIgnoreCase("male")) {
                    genderOptions.add("Masculino");
                } else if (gender.getDesc().equalsIgnoreCase("female")) {
                    genderOptions.add("Feminino");
                }
            }

            genderDrop.setItems(genderOptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class LocalWrapper {
        private final int idLocal;
        private final String name;

        public LocalWrapper(int idLocal, String name) {
            this.idLocal = idLocal;
            this.name = name;
        }

        public int getIdLocal() {
            return idLocal;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void loadLocals() {
        try {
            localDrop.getItems().clear();
            EventDao eventDao = new EventDao();
            int year = eventDao.getActualYear();
            LocalDao localDao = new LocalDao();
            List<Local> locals = localDao.getLocalsByYear(year);
            ObservableList<LocalWrapper> localOptions = FXCollections.observableArrayList();

            for (Local local : locals) {
                localOptions.add(new LocalWrapper(local.getIdLocal(), local.getName()));
            }

            localDrop.setItems(localOptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void registerSport() {
        try {
            String selectedGenderDesc = genderDrop.getValue();

            if (selectedGenderDesc == null || selectedGenderDesc.isEmpty()) {
                showAlert("Erro de Validação", "Por favor, selecione um gênero!", Alert.AlertType.ERROR);
                return;
            }

            String genderDbValue;
            if (selectedGenderDesc.equals("Masculino")) {
                genderDbValue = "male";
            } else if (selectedGenderDesc.equals("Feminino")) {
                genderDbValue = "female";
            } else {
                showAlert("Erro de Validação", "Gênero inválido selecionado!", Alert.AlertType.ERROR);
                return;
            }

            int genderId = genderDbValue.equals("male") ? 1 : 2;
            LocalWrapper selectedLocal = localDrop.getValue();
            if (selectedLocal == null) {
                showAlert("Erro de Validação", "Por favor, selecione um local!", Alert.AlertType.ERROR);
                return;
            }

            Gender selectedGender = new Gender(genderId, genderDbValue);
            String name = nameText.getText();
            String type = typeDrop.getValue();
            String description = descText.getText();
            String minParticipantsText = minText.getText();
            String scoringMeasure = scoringDrop.getValue();
            String oneGame = oneGameDrop.getValue();
            String metric = metricDrop.getValue();
            int idLocal = selectedLocal.getIdLocal();
            LocalDateTime startData = startDataPicker.getValue() != null ? startDataPicker.getValue().atStartOfDay() : null;
            LocalDateTime endData = endDataPicker.getValue() != null ? endDataPicker.getValue().atStartOfDay() : null;
            String scoreMin = scoreMinText.getText();
            String scoreMax = scoreMaxText.getText();
            String startTime = String.format("%02d:%02d", startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalTime parsedStartTime = LocalTime.parse(startTime);
            startData = startDataPicker.getValue().atTime(parsedStartTime);
            String endTime = String.format("%02d:%02d", endHourSpinner.getValue(), endMinuteSpinner.getValue());
            LocalTime parsedEndTime = LocalTime.parse(endTime);
            endData = endDataPicker.getValue().atTime(parsedEndTime);

            SportDao sportDao = new SportDao();
            if(sportDao.dataIsUsing(startData, endData, idLocal)){
                showAlert("Erro de Validação", "O local já está a ser utilizado nesse horário!", Alert.AlertType.ERROR);
                return;
            }

            if (name.isEmpty() || type == null || selectedGenderDesc == null || description.isEmpty() || minParticipantsText.isEmpty() ||
                    scoringMeasure == null || oneGame == null || metric == null || idLocal == 0 || scoreMin.isEmpty() || scoreMax.isEmpty()) {
                showAlert("Erro de Validação", "Por favor, preencha todos os campos obrigatórios!", Alert.AlertType.ERROR);
                return;
            }

            if(startData.isAfter(endData) || startData.isEqual(endData)){
                showAlert("Erro de Validação", "A data de início não pode ser depois da data de fim!", Alert.AlertType.ERROR);
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

            int minScoring;
            try {
                minScoring = Integer.parseInt(scoreMin);
                if (minScoring < 0) {
                    showAlert("Erro de Validação", "O número mínimo de resultado deve ser pelo menos 0!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erro de Validação", "Formato inválido para o número mínimo de resultado!", Alert.AlertType.ERROR);
                return;
            }

            int maxScoring;
            try {
                maxScoring = Integer.parseInt(scoreMax);
                if (maxScoring <= minScoring) {
                    showAlert("Erro de Validação", "O número máximo de resultado deve ser maior que o número mínimo!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erro de Validação", "Formato inválido para o número máximo de resultado!", Alert.AlertType.ERROR);
                return;
            }

            Sport newSport = new Sport(
                    0,
                    type,
                    selectedGender,
                    name,
                    description,
                    minParticipants,
                    scoringMeasure,
                    oneGame,
                    metric,
                    startData,
                    endData,
                    null,
                    null,
                    null,
                    idLocal,
                    minScoring,
                    maxScoring
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

    private void loadOneGameOptions() {
        ObservableList<String> gameOptions = FXCollections.observableArrayList("One", "Multiple");
        oneGameDrop.setItems(gameOptions);
    }

    @FXML
    private void getMetricDrop() {
        String scoreType = scoringDrop.getValue();
        if (scoreType.equals("Time")) {
            metricDrop.setItems(FXCollections.observableArrayList("Milisegundos", "Segundos", "Minutos", "Horas"));
        } else if (scoreType.equals("Points")) {
            metricDrop.setItems(FXCollections.observableArrayList("Pontos"));
        } else if (scoreType.equals("Distance")) {
            metricDrop.setItems(FXCollections.observableArrayList("Centímetros", "Metros", "Kilómetros"));
        }
    }
}