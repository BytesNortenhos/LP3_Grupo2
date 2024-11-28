package controllers.admin;

import Dao.GenderDao;
import Dao.RuleDao;
import Models.Gender;
import Dao.SportDao;
import Models.Rule;
import Models.Sport;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditSportController {
    private static Scene scene;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = cssDarkURL.toExternalForm();
    String cssLight = cssLightURL.toExternalForm();

    @FXML
    private BorderPane parent;

    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    @FXML
    private ImageView iconSports;
    @FXML
    private ImageView iconLogoutNav;

    private static boolean isDarkMode = true;


    @FXML
    private ComboBox<Sport> sportsListDropdown;

    @FXML
    private TextField nameText, descText, minText;

    @FXML
    private SplitMenuButton teamSplitButton;


    private List<String> rules = new ArrayList<>();

    @FXML
    private ComboBox<String> genderDrop;
    @FXML
    private ComboBox<String> typeDrop;
    @FXML
    private ComboBox<String> scoringDrop;
    @FXML
    private ComboBox<String> oneGameDrop;

    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;

    @FXML
    private TextField resultMinText;
    @FXML
    private TextField resultMaxText;





    public void initialize() {
        loadIcons();
        loadTypes();
        loadOneGameOptions();
        loadAvailableSports();

        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        teamSplitButton.setOnMouseClicked(mouseEvent -> teamSplitButton.show());
    }

    private void loadIcons() {

        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconOlympicURL = Main.class.getResource("img/iconSports.png");
        image = new Image(iconOlympicURL.toExternalForm());
        if(iconSports != null) iconSports.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);
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


    public void returnHomeMenu(ActionEvent event) throws IOException {
        HomeController.returnHomeMenu(event);
    }


    public boolean changeMode(ActionEvent event) {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            setDarkMode();
        } else {
            setLightMode();
        }
        return isDarkMode;
    }

    public void setLightMode() {
        parent.getStylesheets().remove(cssDark);
        parent.getStylesheets().add(cssLight);
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        Image image = new Image(iconMoonURL.toExternalForm());
        iconModeNav.setImage(image);
    }

    public void setDarkMode() {
        parent.getStylesheets().remove(cssLight);
        parent.getStylesheets().add(cssDark);
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonURL.toExternalForm());
        iconModeNav.setImage(image);
    }
    public void showRegister(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/register.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void showAthletes(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/athletesView.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void showSportsRegister(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportRegister.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void showSportsEdit(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportEdit.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    public void showTeamsEdit(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamEdit.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void showSports(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportsView.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    public void showStartSports(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/startSport.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if(isDarkMode){
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }


    public void logout(ActionEvent event) throws Exception {
        showLogin(event);
    }
    public void showLogin(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/loginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void loadTeams(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("File selected: " + file.getAbsolutePath());
        }
    }
    @FXML
    public void loadSports(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("File selected: " + file.getAbsolutePath());
        }
    }


    @FXML
    public void loadAthletes(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("File selected: " + file.getAbsolutePath());
        }
    }
}
