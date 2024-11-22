package controllers.admin;

import Dao.AthleteDao; // Importar a classe AthleteDao
import Dao.CountryDao; // Importar a classe CountryDao
import Dao.GenderDao;
import java.time.LocalDate;
import java.time.Period;
import Models.Athlete; // Importar a classe Athlete
import Models.Country; // Importar a classe Country
import Models.Gender;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RegisterController {
    private static Stage stage;
    private static Scene scene;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = cssDarkURL.toExternalForm();
    String cssLight = cssLightURL.toExternalForm();
    @FXML
    private ImageView iconOlympic;
    @FXML
    private ImageView iconLogoutNav;
    @FXML
    private BorderPane parent;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    private static boolean isDarkMode = true;

    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private SplitMenuButton teamSplitButton;
    @FXML
    private ComboBox<String> genderDrop; // ComboBox de gênero
    @FXML
    private ComboBox<String> nacDrop; // ComboBox de nacionalidade
    @FXML
    private TextField userNameText; // Campo de texto para nome de utilizador
    @FXML
    private TextField weightText; // Campo de texto para peso
    @FXML
    private TextField heightText; // Campo de texto para altura
    @FXML
    private DatePicker datePicker;

    public void initialize() {
        loadCountries();
        loadIcons();
        loadGenders();
        URL iconOlympicURL = Main.class.getResource("img/iconAthlete.png");
        String iconOlympicStr = ((URL) iconOlympicURL).toExternalForm();
        Image image = new Image(iconOlympicStr);
        if(iconOlympic != null) iconOlympic.setImage(image);
        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        teamSplitButton.setOnMouseClicked(mouseEvent -> teamSplitButton.show());
    }

    private void loadIcons() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);
    }

    private void loadGenders() {
        try {
            // Limpar qualquer item existente em genderDrop
            genderDrop.getItems().clear();

            // Obter a lista de gêneros do banco de dados
            List<Gender> genders = GenderDao.getGenders();
            ObservableList<String> genderOptions = FXCollections.observableArrayList();

            // Adicionar cada descrição de gênero à lista
            for (Gender gender : genders) {
                genderOptions.add(gender.getDesc());
            }

            // Definir itens da ComboBox de gênero
            genderDrop.setItems(genderOptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Novo método para carregar os países
    private void loadCountries() {
        try {
            nacDrop.getItems().clear();

            List<Country> countries = CountryDao.getCountries();
            ObservableList<String> countryOptions = FXCollections.observableArrayList();

            for (Country country : countries) {
                countryOptions.add(country.getName());
            }

            nacDrop.setItems(countryOptions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registerAthlete(ActionEvent event) {
        try {
            // Obter os valores dos campos
            String userName = userNameText.getText();
            String selectedGender = genderDrop.getValue();
            String selectedCountry = nacDrop.getValue();
            LocalDate dateOfBirth = datePicker.getValue();

            // Verificar data de nascimento
            if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
                showAlert("Erro", "A data de nascimento não pode ser posterior à data atual!", Alert.AlertType.ERROR);
                return;
            }

            // Verificar idade mínima
            int idade = Period.between(dateOfBirth, LocalDate.now()).getYears();
            if (idade < 13) {
                showAlert("Erro", "A idade mínima para registo é de 13 anos!", Alert.AlertType.ERROR);
                return;
            }

            // Validar peso
            float weight;
            try {
                weight = Float.parseFloat(weightText.getText());
                if (weight < 30 || weight > 200) {
                    showAlert("Erro", "Por favor insira um peso realista (entre 30kg e 200kg).", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erro", "O peso inserido não é válido!", Alert.AlertType.ERROR);
                return;
            }

            // Validar altura
            int height;
            try {
                height = Integer.parseInt(heightText.getText());
                if (height < 100 || height > 250) {
                    showAlert("Erro", "Por favor insira uma altura realista (entre 100cm e 250cm).", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erro", "A altura inserida não é válida!", Alert.AlertType.ERROR);
                return;
            }

            // Validar seleção de gênero e país
            if (selectedGender == null || selectedCountry == null) {
                showAlert("Erro", "Por favor, selecione um gênero e um país válidos.", Alert.AlertType.ERROR);
                return;
            }

            // Obter o id do gênero e do país usando as listas carregadas
            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals(selectedGender))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals(selectedCountry))
                    .findFirst().orElse(null);

            if (gender != null && country != null) {
                // Criar o atleta com um id inicial (0 porque será gerado automaticamente)
                Athlete athlete = new Athlete(0, "", userName, country, gender, height, weight, java.sql.Date.valueOf(dateOfBirth));

                // Registrar o atleta no banco de dados e obter o id gerado
                int generatedId = AthleteDao.addAthlete(athlete);

                // A senha do atleta será o id gerado
                String generatedPassword = String.valueOf(generatedId); // Definir a senha como o ID gerado

                // Atualizar o atleta no banco de dados com a nova senha (que é o id gerado)
                AthleteDao.updateAthletePassword(generatedId, generatedPassword); // Passa o id e a senha gerada

                // Exibir uma mensagem de sucesso ou redirecionar para outra tela
                showAlert("Sucesso!", "Atleta registado com sucesso! ID gerado: " + generatedId, Alert.AlertType.INFORMATION);

                // Redirecionar após o sucesso
                returnHomeMenu(event);
            } else {
                showAlert("Erro", "Gênero ou país inválido. Por favor, selecione valores válidos.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erro", "Ocorreu um erro ao aceder à base de dados: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método para exibir alertas
    private void showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
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

    private String generatePassword() {
        // Implementar lógica para gerar uma senha, ou coletar do usuário
        return "defaultPassword"; // Exemplo de senha padrão
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
