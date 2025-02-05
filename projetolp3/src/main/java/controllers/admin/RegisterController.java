package controllers.admin;

import Dao.AthleteDao; // Importar a classe AthleteDao
import Dao.CountryDao; // Importar a classe CountryDao
import Dao.GenderDao;
import Models.Athlete; // Importar a classe Athlete
import Models.Country; // Importar a classe Country
import Models.Gender;
import bytesnortenhos.projetolp3.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RegisterController {
    @FXML
    private ImageView iconOlympic;

    @FXML
    private ComboBox<String> genderDrop;
    @FXML
    private ComboBox<String> nacDrop;
    @FXML
    private TextField userNameText;
    @FXML
    private TextField weightText;

    @FXML
    private TextField heightText;

    @FXML
    private DatePicker datePicker;

    public void initialize() {
        loadCountries();
        loadGenders();
        URL iconOlympicURL = App.class.getResource("img/iconAthlete.png");
        String iconOlympicStr = ((URL) iconOlympicURL).toExternalForm();
        Image image = new Image(iconOlympicStr);
        if(iconOlympic != null) iconOlympic.setImage(image);
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
            String userName = userNameText.getText();
            String selectedGender = genderDrop.getValue();
            String selectedCountry = nacDrop.getValue();
            String weight = weightText.getText();
            String height = heightText.getText();
            LocalDate dateOfBirth = datePicker.getValue();

            if (userName.isEmpty() || selectedGender == null || selectedCountry == null ||
                    weight.isEmpty() || height.isEmpty() || dateOfBirth == null) {
                showAlert("Erro", "Todos os campos são obrigatórios.", Alert.AlertType.ERROR);
                return;
            }

            if (!userName.matches("[a-zA-Z\\s]+")) {
                showAlert("Erro", "O nome do atleta não pode conter números ou caracteres especiais.", Alert.AlertType.ERROR);
                return;
            }

            float parsedWeight;
            int parsedHeight;
            try {
                parsedWeight = Float.parseFloat(weight);
                parsedHeight = Integer.parseInt(height);
            } catch (NumberFormatException e) {
                showAlert("Erro", "O peso e a altura devem ser valores numéricos.", Alert.AlertType.ERROR);
                return;
            }

            int age = LocalDate.now().getYear() - dateOfBirth.getYear();
            if (age < 13 || age > 120) {
                showAlert("Erro", "A idade deve estar entre 13 e 120 anos.", Alert.AlertType.ERROR);
                return;
            }

            if (parsedHeight > 250) {
                showAlert("Erro", "A altura não pode exceder 2,50 metros.", Alert.AlertType.ERROR);
                return;
            }

            if (parsedWeight > 400) {
                showAlert("Erro", "O peso não pode exceder 400 kg.", Alert.AlertType.ERROR);
                return;
            }

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals(selectedGender))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals(selectedCountry))
                    .findFirst().orElse(null);

            if (gender != null && country != null) {
                Athlete athlete = new Athlete(0, "", userName, country, gender, parsedHeight, parsedWeight, java.sql.Date.valueOf(dateOfBirth), "ImagesAthlete/default.png");

                int generatedId = AthleteDao.addAthlete(athlete);

                String generatedPassword = String.valueOf(generatedId);

                AthleteDao.updateAthletePassword(generatedId, generatedPassword);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Atleta registado com sucesso! ID gerado: " + generatedId);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    controllers.admin.ViewsController viewsController = new controllers.admin.ViewsController();
                    viewsController.returnHomeMenu(event);
                }
            } else {
                showAlert("Erro", "Por favor, selecione um género e um país válidos.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao conectar com a base de dados.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro inesperado.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
