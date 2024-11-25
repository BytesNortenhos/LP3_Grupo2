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
    private static Stage stage;
    private static Scene scene;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = cssDarkURL.toExternalForm();
    String cssLight = cssLightURL.toExternalForm();

    @FXML
    private BorderPane parent;
    @FXML
    private TextArea rulesTextArea;

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
    private TextField txtName;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtMinParticipants;

    @FXML
    private ComboBox<Sport> sportsListDropdown;

    @FXML
    private TextField nameText, descText, minText, typeText, genderText;

    @FXML
    private SplitMenuButton teamSplitButton;

    @FXML
    private TextArea rulesText; // Campo para inserção de regras

    private List<String> rules = new ArrayList<>();

    @FXML
    private ComboBox<String> genderDrop; // ComboBox de gênero
    @FXML
    private ComboBox<String> typeDrop;  // ComboBox para tipo
    @FXML
    private ComboBox<String> scoringDrop; // ComboBox para medida de pontuação
    @FXML
    private ComboBox<String> oneGameDrop; // ComboBox para "One Game"

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
            // Obter os esportes do banco de dados
            List<Sport> sports = SportDao.getAllSportsV2();

            // Adicionar ao ComboBox
            ObservableList<Sport> sportsOptions = FXCollections.observableArrayList(sports);
            sportsListDropdown.setItems(sportsOptions);
        } catch (SQLException e) {
            showAlert("Database Error", "Erro ao carregar esportes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Ao selecionar um esporte, preencher os campos
    @FXML
    private void onSportSelected() {
        Sport selectedSport = sportsListDropdown.getSelectionModel().getSelectedItem();

        if (selectedSport != null) {
            // Preenche os campos de texto com os valores do esporte selecionado
            nameText.setText(selectedSport.getName());
            descText.setText(selectedSport.getDesc());
            minText.setText(String.valueOf(selectedSport.getMinParticipants()));
            scoringDrop.setValue(selectedSport.getScoringMeasure());

            // Exibindo o tipo selecionado no ComboBox
            typeDrop.setValue(selectedSport.getType()); // "Individual" ou "Collective"

            // Exibindo o gênero selecionado no ComboBox
            if (selectedSport.getGenre() != null) {
                if (selectedSport.getGenre().getIdGender() == 1) {
                    genderDrop.setValue("Masculino");
                } else if (selectedSport.getGenre().getIdGender() == 2) {
                    genderDrop.setValue("Feminino");
                }
            }

            // Exibindo a opção de OneGame no ComboBox
            oneGameDrop.setValue(selectedSport.getOneGame());  // "One" ou "Multiple"

            // Exibindo os valores de resultMin e resultMax
            resultMinText.setText(String.valueOf(selectedSport.getResultMin()));
            resultMaxText.setText(String.valueOf(selectedSport.getResultMax()));
        }
    }





    @FXML
    private void updateSport() {
        try {
            // Capturar os dados do formulário
            String name = nameText.getText();
            String description = descText.getText();
            int minParticipants = Integer.parseInt(minText.getText());
            String selectedType = typeDrop.getValue(); // Captura o tipo selecionado (Individual ou Collective)
            String selectedGender = genderDrop.getValue(); // Captura o gênero selecionado
            String selectedOneGame = oneGameDrop.getValue(); // Captura a seleção de OneGame
            Sport selectedSport = sportsListDropdown.getSelectionModel().getSelectedItem();
            int resultMin = Integer.parseInt(resultMinText.getText());
            int resultMax = Integer.parseInt(resultMaxText.getText());

            // Validar seleção
            if (selectedSport == null) {
                showAlert("Validation Error", "Selecione uma modalidade para atualizar!", Alert.AlertType.ERROR);
                return;
            }

            // Verificar se resultMin é maior que resultMax
            if (resultMin > resultMax) {
                showAlert("Validation Error", "O valor mínimo do resultado não pode ser maior que o valor máximo!", Alert.AlertType.ERROR);
                return;
            }

            // Atribuindo o gênero com base na seleção
            int genderId = 0;
            if ("Masculino".equals(selectedGender)) {
                genderId = 1; // Gênero Masculino
            } else if ("Feminino".equals(selectedGender)) {
                genderId = 2; // Gênero Feminino
            }

            // Capturando o valor selecionado do ComboBox de Scoring Measure
            String selectedScoringMeasure = scoringDrop.getValue();

            // Atualizar informações do esporte
            selectedSport.setName(name);
            selectedSport.setDesc(description);
            selectedSport.setMinParticipants(minParticipants);
            selectedSport.setType(selectedType); // Atualiza o tipo do esporte
            selectedSport.getGenre().setIdGender(genderId); // Atualiza o gênero
            selectedSport.setScoringMeasure(selectedScoringMeasure); // Atualiza a medida de pontuação
            selectedSport.setOneGame(selectedOneGame); // Atualiza o valor de OneGame
            selectedSport.setResultMin(resultMin);
            selectedSport.setResultMax(resultMax);

            // Atualizar no banco
            SportDao.updateSport(selectedSport);

            showAlert("Success", "Modalidade atualizada com sucesso!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Formato inválido para um dos campos numéricos!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "Erro ao atualizar esporte: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }




    @FXML
    public void addRule() {
        // Obtendo a descrição da regra do TextArea
        String ruleDescription = rulesTextArea.getText().trim();

        if (!ruleDescription.isEmpty()) {
            // Adicionando a descrição da regra à lista
            rules.add(ruleDescription);

            // Limpar o TextArea após adicionar
            rulesTextArea.clear();

            // Exibir uma mensagem informando que a regra foi adicionada
            showAlert("Success", "Rule added successfully!", Alert.AlertType.INFORMATION);
        } else {
            // Exibir um alerta se a descrição da regra estiver vazia
            showAlert("Error", "Rule description cannot be empty!", Alert.AlertType.ERROR);
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

    private void loadScoringMeasures() {
        ObservableList<String> scoringMeasures = FXCollections.observableArrayList("Points", "Time");
        scoringDrop.setItems(scoringMeasures);
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
    public void mostrarRegistar(ActionEvent event) throws IOException {
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
    public void mostrarRegistaModalidades(ActionEvent event) throws IOException {
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
    public void mostrarEditaModalidades(ActionEvent event) throws IOException {
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

    public void mostrarEditaEquipas(ActionEvent event) throws IOException {
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
    public void mostrarModalidades(ActionEvent event) throws IOException {
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
    public void mostrarIniciarModalidades(ActionEvent event) throws IOException {
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
    public void mostrarRegistaEquipas(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamRegister.fxml")));
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

    public void mostraEditaEquipas(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamRegister.fxml")));
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
        mostrarLogin(event);
    }
    public void mostrarLogin(ActionEvent event) throws IOException {
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