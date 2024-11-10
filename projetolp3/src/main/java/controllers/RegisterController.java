package controllers;

import Dao.AthleteDao; // Importar a classe AthleteDao
import Dao.CountryDao; // Importar a classe CountryDao
import Dao.GenderDao;
import Models.Athlete; // Importar a classe Athlete
import Models.Country; // Importar a classe Country
import Models.Gender;
import bytesnortenhos.projetolp3.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class RegisterController {

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
    private static boolean isDarkMode = true;

    @FXML
    private SplitMenuButton splitMenuButton;

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
    private DatePicker datePicker; // DatePicker para data de nascimento

    public void initialize() {
        // Configuração dos ícones
        loadIcons();

        // Carregar gêneros na ComboBox de gênero
        loadGenders();

        // Carregar países na ComboBox de nacionalidade
        loadCountries();

        // Configuração do SplitMenuButton
        splitMenuButton.setOnMouseClicked(event -> splitMenuButton.show());
    }

    private void loadIcons() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);
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
            // Limpar qualquer item existente em nacDrop
            nacDrop.getItems().clear();

            // Obter a lista de países do banco de dados
            List<Country> countries = CountryDao.getCountries();
            ObservableList<String> countryOptions = FXCollections.observableArrayList();

            // Adicionar cada nome de país à lista
            for (Country country : countries) {
                countryOptions.add(country.getName());
            }

            // Definir itens da ComboBox de nacionalidade
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
            float weight = Float.parseFloat(weightText.getText());
            int height = Integer.parseInt(heightText.getText());
            java.sql.Date dateOfBirth = java.sql.Date.valueOf(datePicker.getValue());

            // Obter o id do gênero e do país usando as listas carregadas
            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals(selectedGender))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals(selectedCountry))
                    .findFirst().orElse(null);

            if (gender != null && country != null) {
                // Criar o atleta com um id inicial (0 porque será gerado automaticamente)
                Athlete athlete = new Athlete(0, "", userName, country, gender, height, weight, dateOfBirth);

                // Registrar o atleta no banco de dados e obter o id gerado
                int generatedId = AthleteDao.addAthlete(athlete);

                // A senha do atleta será o id gerado
                String generatedPassword = String.valueOf(generatedId); // Definir a senha como o ID gerado

                // Atualizar o atleta no banco de dados com a nova senha (que é o id gerado)
                AthleteDao.updateAthletePassword(generatedId, generatedPassword); // Passa o id e a senha gerada

                // Exibir uma mensagem de sucesso ou redirecionar para outra tela
                System.out.println("Atleta registrado com sucesso! ID gerado: " + generatedId);

            } else {
                System.out.println("Por favor, selecione um gênero e um país válidos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Por favor, insira valores válidos para peso e altura.");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
