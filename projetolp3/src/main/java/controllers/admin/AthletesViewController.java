package controllers.admin;

import Dao.AthleteDao;
import Dao.CountryDao;
import Dao.ResultDao;
import Models.Athlete;
import Models.Registration;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import java.util.List;
import java.util.Objects;

public class AthletesViewController {
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    private static boolean isDarkMode = true;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    @FXML
    private ImageView iconLogoutNav;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private SplitMenuButton teamSplitButton;

    @FXML
    private FlowPane showAthletesContainer;
    @FXML
    private Label noSportsLabel;
    @FXML
    private ComboBox<String> countryDrop;

    private String country;

    public void initialize() throws SQLException {
        loadIcons();
        loadCountrys();
        athleteSplitButton.setOnMouseClicked(event -> {
            athleteSplitButton.show();
        });
        sportSplitButton.setOnMouseClicked(mouseEvent -> {
            sportSplitButton.show();
        });
        teamSplitButton.setOnMouseClicked(mouseEvent -> {
            sportSplitButton.show();
        });
    }
    public void loadCountrys() throws SQLException {
        countryDrop.getItems().clear();
        CountryDao countryDao = new CountryDao();
        List<String> countrys = countryDao.getCountrys();
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        countryOptions.addAll(countrys);
        countryDrop.setItems(countryOptions);
    }
    public void loadIcons() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if (iconLogoutNav != null) iconLogoutNav.setImage(image);
    }

    private List<List> getAthletes() throws SQLException {
        AthleteDao athleteDao = new AthleteDao();
        return athleteDao.getAthletesToShow();
    }

    private void showNoSportsMessage() {
        noSportsLabel.setVisible(true);

    }
    public void showAthlete(ActionEvent event) throws SQLException {
        showAthletesContainer.getChildren().clear();
        List<List> athletes = null;
        try {
            athletes = getAthletes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (athletes.isEmpty()) {
            showNoSportsMessage();
        } else {
            displayAthletes(athletes);
        }
    }
    private void displayAthletes(List<List> athletes) throws SQLException {
        noSportsLabel.setVisible(false);
        showAthletesContainer.setVisible(true);
        showAthletesContainer.getChildren().clear();
        for (List athlete : athletes) {
            if(Objects.equals(athlete.get(2).toString(), countryDrop.getValue())){
                VBox athleteItem = createSportsItem(athlete);
                showAthletesContainer.getChildren().add(athleteItem);
            }

        }
    }

    private VBox createSportsItem(List athlete) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(athlete.get(1).toString());
        nameLabel.getStyleClass().add("name-label");

        Label genderLabel = new Label(athlete.get(3).toString());
        genderLabel.getStyleClass().add("gender-label");

        Label heightLabel = new Label("Altura: " + athlete.get(4).toString() + " cm");
        heightLabel.getStyleClass().add("height-label");

        Label weightLabel = new Label("Peso: " + athlete.get(5).toString() + " kg");
        weightLabel.getStyleClass().add("weight-label");

        Label birthLabel = new Label("Data de nascimento: " + athlete.get(6).toString());
        birthLabel.getStyleClass().add("birth-label");

        ImageView resultsImageView = new ImageView();
        URL iconResuURL = Main.class.getResource("img/iconResults.png");
        if (iconResuURL != null) {
            Image image = new Image(iconResuURL.toExternalForm());
            resultsImageView.setImage(image);
            resultsImageView.setFitWidth(60);
            resultsImageView.setFitHeight(60);
        }
        Button viewResultsButton = new Button();
        viewResultsButton.setGraphic(resultsImageView);
        viewResultsButton.getStyleClass().add("startButton");
        viewResultsButton.setOnAction(event -> {
            try {
                historyPopUp(event, Integer.parseInt(athlete.getFirst().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        ImageView editImageView = new ImageView();
        URL iconEditURL = Main.class.getResource("img/iconEdit.png");
        if (iconEditURL != null) {
            Image image = new Image(iconEditURL.toExternalForm());
            editImageView.setImage(image);
            editImageView.setFitWidth(60);
            editImageView.setFitHeight(60);
        }
        Button viewEditButton = new Button();
        viewEditButton.setGraphic(editImageView);
        viewEditButton.getStyleClass().add("startButton");
        viewEditButton.setOnAction(event -> {
            try {
                editPopUp(Integer.parseInt(athlete.getFirst().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(viewResultsButton, viewEditButton);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setPadding(new Insets(10));

        requestItem.getChildren().addAll(nameLabel, genderLabel, heightLabel, weightLabel, birthLabel, buttonContainer);
        requestItem.setPrefWidth(500);
        return requestItem;
    }

    public void historyPopUp(ActionEvent event, int idAthlete) throws IOException, SQLException {
        ResultDao resultDao = new ResultDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Histórico de participações");

        VBox vbox = new VBox(500);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        List<List> results = resultDao.getResultByAthlete(idAthlete);
        displayResults(vbox, results, idAthlete);



        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    private void displayResults(VBox vbox, List<List> results, int idAthlete) throws SQLException {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");
        for (List result : results) {
            VBox resultItem = createResultItem(result, idAthlete);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }
    private VBox createResultItem(List result, int idAthlete) throws SQLException {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);
        Label nameLabel = new Label("Modalidade: " + (result.get(1) != null ? result.get(1).toString() : "N/A"));
        nameLabel.getStyleClass().add("name-label");

        long resultValue = Long.parseLong(result.get(0).toString());
        double resultSeconds = resultValue / 1000.0;
        Label resultLabel = new Label("Resultado: " + resultSeconds + " segundos");
        resultLabel.getStyleClass().add("result-label");

        Label typeLabel = new Label("Tipo de modalidade: " + (result.get(2) != null ? result.get(2).toString() : "N/A"));
        typeLabel.getStyleClass().add("type-label");

        if (result.get(3) != null) {
            Label teamLabel = new Label("Equipa: " + result.get(3).toString());
            teamLabel.getStyleClass().add("team-label");
            resultItem.getChildren().add(teamLabel);
        }

        Label dateLabel = new Label("Data: " + (result.get(4) != null ? result.get(4).toString() : "N/A"));
        dateLabel.getStyleClass().add("date-label");

        Label localLabel = new Label("Local: " + (result.get(5) != null ? result.get(5).toString() : "N/A"));
        localLabel.getStyleClass().add("local-label");

        int pos = 0;
        ResultDao resultDao = new ResultDao();
        List<List> positions = resultDao.getPositionById((Integer) result.get(6), result.get(4).toString());
        for (List position : positions) {
            if((Integer) position.get(1) == idAthlete){
                pos = (Integer) position.get(0);
            }
        }
        Label postionLabel = new Label("Posição: " + pos + "º lugar");
        postionLabel.getStyleClass().add("local-label");

        resultItem.getChildren().addAll(nameLabel, resultLabel, typeLabel, dateLabel, localLabel, postionLabel);
        return resultItem;
    }


    private void editPopUp(int idAthlete) throws IOException, SQLException {
        Stage popupStage = new Stage();
        popupStage.setTitle("Editar atleta");


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("popup-vbox");

        AthleteDao athleteDao = new AthleteDao();
        Athlete athlete = athleteDao.getAthleteById(idAthlete);

        Label titleLabel = new Label("Editar o atleta " + athlete.getName());
        titleLabel.getStyleClass().add("popup-title");
        titleLabel.setTranslateY(-30);


        Label weightLabel = new Label("Peso(KG):");
        weightLabel.getStyleClass().add("popup-label");
        TextField weightField = new TextField();
        weightField.getStyleClass().add("popup-text");
        weightField.setText("" + athlete.getWeight());

        Label heightLabel = new Label("Altura(cm):");
        heightLabel.getStyleClass().add("popup-label");
        TextField heightField = new TextField();
        heightField.getStyleClass().add("popup-text");
        heightField.setText("" + athlete.getHeight());

        HBox setImageContainer = new HBox(10);
        ImageView image = new ImageView();
        URL iconImageURL = Main.class.getResource(athlete.getImage());
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            image.setImage(images);
            image.setFitWidth(60);
            image.setFitHeight(60);
            Button setImageButton = new Button("Alterar imagem");
            setImageButton.setOnAction(event -> {
                try {
                    updateImageAthlete(event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            setImageContainer.getChildren().addAll(image, setImageButton);
        }
        else{
            Label noImage = new Label("O atleta ainda não tem imagem!");
            setImageContainer.getChildren().addAll(noImage);
        }
        setImageContainer.setAlignment(Pos.CENTER_LEFT);
        setImageContainer.setPadding(new Insets(10));



        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("popup-button");
        submitButton.setOnAction(event -> {
            try {
                String minParticipantsText = weightField.getText();
                String maxParticipantsText = heightField.getText();

                if (!minParticipantsText.matches("\\d+") || !maxParticipantsText.matches("\\d+")) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Por favor, insira apenas números nos campos de participantes!");
                    alerta.show();
                    return;
                }

                int minParticipants = Integer.parseInt(minParticipantsText);
                int maxParticipants = Integer.parseInt(maxParticipantsText);
                if(minParticipants < 2){
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("A equipa tem de ter um mínimo de participantes superior a 1!");
                    alerta.show();
                }
//                teamCreate(request, minParticipants, maxParticipants);
                popupStage.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll(titleLabel, weightLabel, weightField, heightLabel, heightField,setImageContainer, submitButton);

        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void updateImageAthlete(ActionEvent event) throws SQLException {
        String pathSave = Main.class.getResource("ImagesAthlete").toExternalForm().replace("file:", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");
        String pathToSave = "ImagesAthlete/";

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long pngCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".png")).count();
            long jpgCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpg")).count();
            long jpegCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpeg")).count();

            if (selectedFiles.size() == 1 && (pngCount == 1 || jpgCount == 1 || jpegCount == 1)) {
                File selectedFile = selectedFiles.get(0);

                int tempAthleteId = 1000;
                boolean saved = saveAthleteImage(tempAthleteId, pathSave, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();

                    AthleteDao athleteDao = new AthleteDao();
                    athleteDao.updateAthleteImage(tempAthleteId, pathToSave, "." + selectedFile.getName().split("\\.")[1]);

                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Ocorreu um erro ao guardar a imagem!");
                    alerta.show();
                }

            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("Selecione 1 ficheiro! Extensões válidas: .png, .jpeg, .jpg");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("Selecione 1 ficheiro! Extensões válidas: .png, .jpeg, .jpg");
            alerta.show();
        }
    }

    public boolean saveAthleteImage(int tempAthleteId, String pathSave, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(tempAthleteId) + "." + fileImage.getName().split("\\.")[1];
        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
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
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconModeNav.setImage(image);
    }

    public void setDarkMode() {
        parent.getStylesheets().remove(String.valueOf(cssLight));
        parent.getStylesheets().add(String.valueOf(cssDark));
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportRegister.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
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

    public void showSports(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportsView.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
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
    public void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
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