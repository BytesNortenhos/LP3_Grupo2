package controllers.admin;

import Dao.*;
import Models.*;
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

public class RegisterTeamController {
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

    @FXML
    private TextField minText;

    private static boolean isDarkMode = true;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtMinParticipants;

    @FXML
    private ComboBox<Country> countryDrop;

    @FXML
    private ComboBox<Gender> genderDrop;
    @FXML
    private ComboBox<Sport> sportDrop;
    @FXML
    private TextField teamNameText, yearFoundedText, minParticipantsText, maxParticipantsText;
    @FXML
    private TextField nameText;  // Atualizado para 'nameText' conforme o FXML
    @FXML
    private TextField descText;  // Atualizado para 'descText' conforme o FXML

    @FXML
    private TextArea rulesText; // Campo para inserção de regras

    private List<String> rules = new ArrayList<>();

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
    private SplitMenuButton teamSplitButton;

    public void initialize() {
        loadIcons();
        loadGenders();
        loadCountries();
        loadSports();
        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        teamSplitButton.setOnMouseClicked(event -> teamSplitButton.show());

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

    private void loadGenders() {
        try {
            // Recupera a lista de objetos Gender do DAO
            List<Gender> genders = GenderDao.getGenders();

            // Define os itens do ComboBox para a lista de objetos Gender
            genderDrop.setItems(FXCollections.observableArrayList(genders));

            // Define a exibição do ComboBox para mostrar a descrição do Gender usando um CellFactory personalizado
            genderDrop.setCellFactory(param -> new ListCell<Gender>() {
                @Override
                protected void updateItem(Gender gender, boolean empty) {
                    super.updateItem(gender, empty);
                    if (empty || gender == null) {
                        setText(null);
                    } else {
                        setText(gender.getDesc());  // Exibe a descrição do gender (campo `getDesc()` do objeto Gender)
                    }
                }
            });

            // Define o botão do ComboBox para mostrar a descrição do Gender selecionado
            genderDrop.setButtonCell(genderDrop.getCellFactory().call(null));  // Exibe a descrição do Gender no botão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCountries() {
        try {
            // Recupera a lista de objetos Country do DAO
            List<Country> countries = CountryDao.getCountries();

            // Define os itens do ComboBox para os objetos de Country
            countryDrop.setItems(FXCollections.observableArrayList(countries));

            // Define a exibição personalizada do ComboBox para mostrar o nome do país
            countryDrop.setCellFactory(param -> new ListCell<Country>() {
                @Override
                protected void updateItem(Country country, boolean empty) {
                    super.updateItem(country, empty);
                    if (empty || country == null) {
                        setText(null);
                    } else {
                        setText(country.getName());  // Exibe o nome do país
                    }
                }
            });

            // Define o botão do ComboBox para mostrar o nome do país selecionado
            countryDrop.setButtonCell(countryDrop.getCellFactory().call(null));  // Exibe o nome do país no botão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSports() {
        try {
            // Recupera a lista de objetos Sport do DAO
            SportDao sportdao = new SportDao();
            List<Sport> sports = sportdao.getAllSportsV2();

            // Define os itens do ComboBox para a lista de objetos Sport
            sportDrop.setItems(FXCollections.observableArrayList(sports));

            // Define a exibição do ComboBox para mostrar a descrição do Sport usando um CellFactory personalizado
            sportDrop.setCellFactory(param -> new ListCell<Sport>() {
                @Override
                protected void updateItem(Sport sport, boolean empty) {
                    super.updateItem(sport, empty);
                    if (empty || sport == null) {
                        setText(null);
                    } else {
                        setText(sport.getName());  // Exibe o nome do esporte (campo `getName()` do objeto Sport)
                    }
                }
            });

            // Define o botão do ComboBox para mostrar o nome do Sport selecionado
            sportDrop.setButtonCell(sportDrop.getCellFactory().call(null));  // Exibe o nome do esporte no botão
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void registerTeam() {
        try {
            // Recupera os valores do formulário
            String name = teamNameText.getText();
            Country selectedCountry = countryDrop.getValue();  // Objeto completo de Country
            Gender selectedGender = genderDrop.getValue();    // Objeto completo de Gender
            Sport selectedSport = sportDrop.getValue();       // Objeto completo de Sport
            int yearFounded = Integer.parseInt(yearFoundedText.getText());
            int minParticipants = Integer.parseInt(minParticipantsText.getText());
            int maxParticipants = Integer.parseInt(maxParticipantsText.getText());

            // Validação de campos obrigatórios
            if (name.isEmpty() || selectedCountry == null || selectedGender == null || selectedSport == null) {
                showAlert("Validation Error", "Please fill all required fields!", Alert.AlertType.ERROR);
                return;
            }

            // Criar o novo time usando os objetos completos
            Team newTeam = new Team(
                    0,  // O ID será gerado automaticamente
                    name,
                    selectedCountry,  // Passando o objeto completo de Country
                    selectedGender,   // Passando o objeto completo de Gender
                    selectedSport,    // Passando o objeto completo de Sport
                    yearFounded,
                    minParticipants,
                    maxParticipants
            );

            // Inserir o novo time no banco de dados
            TeamDao teamdao = new TeamDao();
            teamdao.addTeam(newTeam);

            // Exibir mensagem de sucesso
            showAlert("Success", "Team registered successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid number format!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving the team.", Alert.AlertType.ERROR);
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

    public void mostrarEditaEquipas(ActionEvent event) throws IOException {
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
