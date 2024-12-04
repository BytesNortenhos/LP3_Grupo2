package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.*;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import javafx.application.Platform;
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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.foreign.MemoryLayout;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.Year;
import java.util.*;

public class StartSportsController {
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    @FXML
    private ComboBox<String> yearDrop;
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
    private FlowPane startSportsContainer;
    @FXML
    private Label noSportsLabel;

    SportDao sportDao = new SportDao();
    RegistrationDao registrationDao = new RegistrationDao();
    ResultDao resultDao = new ResultDao();
    MedalDao medalDao = new MedalDao();
    OlympicRecordDao olympicRecordDao = new OlympicRecordDao();
    WinnerOlympicDao winnerOlympicDao = new WinnerOlympicDao();

    public void initialize() throws SQLException {
        loadIcons();
        loadYears();
        athleteSplitButton.setOnMouseClicked(event -> {
            // Open the dropdown menu when clicking on the button's text
            athleteSplitButton.show();
        });
        sportSplitButton.setOnMouseClicked(mouseEvent -> {
            sportSplitButton.show();
        });
    }

    public void loadYears() throws SQLException {
        yearDrop.getItems().clear();
        RegistrationDao registrationDao = new RegistrationDao();
        List<String> years = registrationDao.getYears();
        ObservableList<String> yearsOptions = FXCollections.observableArrayList();
        yearsOptions.addAll(years);
        yearDrop.setItems(yearsOptions);
    }

    public void showSport(ActionEvent event) throws SQLException {
        startSportsContainer.getChildren().clear();
        List<List> sports = null;
        int year = Integer.parseInt(yearDrop.getValue());
        try {
            sports = getSports(year);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (sports == null || sports.isEmpty()) {
            showNoSportsMessage();
        } else {
            displaySports(sports, year);
        }
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

    private List<List> getSports(int year) throws SQLException {
        return sportDao.getSportsToStart(year);
    }

    private void showNoSportsMessage() {
        startSportsContainer.getChildren().add(noSportsLabel);
        noSportsLabel.setVisible(true);
    }

    private void displaySports(List<List> sports, int year) throws SQLException {
        noSportsLabel.setVisible(false);
        startSportsContainer.setVisible(true);
        startSportsContainer.getChildren().clear();
        for (List sport : sports) {
            VBox sportsItem = createSportsItem(sport, year);
            startSportsContainer.getChildren().add(sportsItem);

        }
    }

    private VBox createSportsItem(List sport, int year) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");
        int nPart = 0;
        int idSport = Integer.parseInt(sport.get(0).toString());
        int mPart = Integer.parseInt(sport.get(5).toString());
        Label minPart = new Label();
        Label numPart = new Label();
        Label nameLabel = new Label(sport.get(3).toString());
        nameLabel.getStyleClass().add("name-label");

        String sportType = sport.get(1).toString();

        Label typeLabel = new Label(sportType);
        typeLabel.getStyleClass().add("type-label");

        Label genderLabel = new Label(sport.get(2).toString());
        genderLabel.getStyleClass().add("gender-label");

        if (sportType.equals("Individual")) {
            nPart = sportDao.getNumberParticipantsSport(idSport, year);
            minPart = new Label("Minímo de participantes: " + mPart);
            minPart.getStyleClass().add("minPart-label");

            numPart = new Label("Número de participantes: " + nPart);
            numPart.getStyleClass().add("numPart-label");
        } else {

            minPart = new Label("Minímo de equipas: " + mPart);
            minPart.getStyleClass().add("minPart-label");

            nPart = sportDao.getNumberTeamsSports(idSport, year);
            numPart = new Label("Número de equipas: " + nPart);
            numPart.getStyleClass().add("numPart-label");
        }

        ImageView athletesImageView = new ImageView();
        URL iconAttURL = Main.class.getResource("img/iconReView.png");
        if (iconAttURL != null) {
            Image image = new Image(iconAttURL.toExternalForm());
            athletesImageView.setImage(image);
            athletesImageView.setFitWidth(80);
            athletesImageView.setFitHeight(80);
        }
        Button viewRegistedButton = new Button();
        viewRegistedButton.setGraphic(athletesImageView);
        viewRegistedButton.getStyleClass().add("startButton");
        viewRegistedButton.setOnAction(event -> {
            try {
                showRegistedAthletes(idSport, year);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        if (sport.get(8).toString().equals("3")) {
            if (nPart >= mPart) {
                ImageView startImageView = new ImageView();
                URL iconStartURL = Main.class.getResource("img/iconStart.png");
                if (iconStartURL != null) {
                    Image image = new Image(iconStartURL.toExternalForm());
                    startImageView.setImage(image);
                    startImageView.setFitWidth(80);
                    startImageView.setFitHeight(80);
                }
                Button startButton = new Button();
                startButton.setGraphic(startImageView);
                startButton.getStyleClass().add("startButton");
                HBox buttonContainer = new HBox(10);
                buttonContainer.getChildren().addAll(viewRegistedButton, startButton);
                buttonContainer.setAlignment(Pos.CENTER_LEFT);
                buttonContainer.setPadding(new Insets(10));
                requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, buttonContainer);
                startButton.setOnAction(event -> {
                    try {
                        showPopUpLocal(event, year, idSport, mPart);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, viewRegistedButton);
            }
        } else {
            ImageView viewImageView = new ImageView();
            URL iconViewURL = Main.class.getResource("img/iconView.png");
            if (iconViewURL != null) {
                Image image = new Image(iconViewURL.toExternalForm());
                viewImageView.setImage(image);
                viewImageView.setFitWidth(80);
                viewImageView.setFitHeight(80);
            }
            Button viewResultsButton = new Button();
            viewResultsButton.setGraphic(viewImageView);
            viewResultsButton.getStyleClass().add("startButton");
            viewResultsButton.setOnAction(event -> {
                try {
                    historyPopUp(event, Integer.parseInt(sport.get(0).toString()) , sport.get(2).toString(), year);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, viewResultsButton);
        }

        requestItem.setPrefWidth(500); // Ensure this width allows two items per line
        return requestItem;
    }
    public void showPopUpLocal(ActionEvent event, int year, int idSport, int mPart) throws SQLException {
        Stage popupStage = new Stage();
        popupStage.setTitle("Escolher local");


        VBox vbox = new VBox(600);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");

        vbox.setSpacing(20);
        vbox.getChildren().clear();

        Label titleLabel = new Label("Selecione o local pretendido:");
        titleLabel.getStyleClass().add("name-label");

        ComboBox<String> localComboBox = new ComboBox<>();

        LocalDao localDao = new LocalDao();
        List<Local> locals = localDao.getLocalsByYear(year);
        for(Local local : locals){
            localComboBox.getItems().add(local.getName());
        }
        localComboBox.getStyleClass().add("registerDrop");

        Button confirmButton = new Button("Confirmar");
        confirmButton.getStyleClass().add("startButton");
        confirmButton.setOnAction(event1 -> {
            try {
                int idLocal = 0;
                if (sportDao.verifyRanges(idSport)) {
                    for(Local local : locals){
                        if(local.getName().equals(localComboBox.getValue())){
                            idLocal = local.getIdLocal();
                        }
                    }
                    if (sportStart(idSport, year, idLocal)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso!");
                        alert.setHeaderText("Modalidade inicada com sucesso!");
                        List<List> sports = getSports(year);
                        displaySports(sports, year);
                        Optional<ButtonType> result = alert.showAndWait();
//                           if (result.isPresent() && result.get() == ButtonType.OK) {
//                               Platform.runLater(() -> startSportsContainer.getChildren().remove(requestItem));
//                           }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Aviso!");
                    alert.setHeaderText("Modalidade não pode ser iniciada sem resultados registados!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        vbox.getChildren().addAll(titleLabel, localComboBox, confirmButton);

        Scene scene = new Scene(vbox, 600, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    public void showRegistedAthletes(int idSport, int year) throws SQLException {
        SportDao sportDao = new SportDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Atletas inscritos");


        VBox vbox = new VBox(600);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");



        if (sportDao.verifyIfIsTeam(idSport, year)) {
            List<List> results = sportDao.getTeamsAndthletes(idSport, year);
            displayTeams(vbox, results);
            Scene scene = new Scene(vbox, 600, 450);
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
            popupStage.setScene(scene);
            popupStage.show();
        } else {
            List<Athlete> results = sportDao.getAthletesBySport(idSport, year);
            displayAthletes(vbox, results);
            Scene scene = new Scene(vbox, 600, 450);
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
            popupStage.setScene(scene);
            popupStage.show();
        }
    }

    public void displayTeams(VBox vbox, List<List> results) {
        vbox.getChildren().clear();
        vbox.setSpacing(20);

        Map<String, List<String>> teamAthletesMap = new HashMap<>();
        for (List result : results) {
            String teamName = result.get(1) != null ? result.get(1).toString() : "N/A";
            String athleteName = result.get(3) != null ? result.get(3).toString() : "N/A";
            teamAthletesMap.computeIfAbsent(teamName, k -> new ArrayList<>()).add(athleteName);
        }

        for (Map.Entry<String, List<String>> entry : teamAthletesMap.entrySet()) {
            VBox resultItem = createTeamsResult(entry.getKey(), entry.getValue());
            vbox.getChildren().add(resultItem);
        }
    }

    public void displayAthletes(VBox vbox, List<Athlete> results) {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        for (Athlete athlete : results) {
            VBox resultItem = createAthleteResult(athlete.getName(), athlete.getCountry().getName());
            vbox.getChildren().add(resultItem);
        }
    }

    private VBox createTeamsResult(String teamName, List<String> athletes) {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        Label nameLabel = new Label("Equipa: " + teamName);
        nameLabel.getStyleClass().add("name-label");

        StringBuilder athletesText = new StringBuilder("Atletas: ");
        for (String athlete : athletes) {
            athletesText.append(athlete).append(", ");
        }
        if (!athletesText.isEmpty()) {
            athletesText.setLength(athletesText.length() - 2);
        }

        Label resultLabel = new Label(athletesText.toString());
        resultLabel.getStyleClass().add("result-label");

        resultItem.getChildren().addAll(nameLabel, resultLabel);
        return resultItem;
    }

    private VBox createAthleteResult(String athletes, String country) {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        Label nameLabel = new Label("Atletas registados: ");
        nameLabel.getStyleClass().add("name-label");


        Label resultLabel = new Label(athletes + " - " + country);
        resultLabel.getStyleClass().add("result-label");

        resultItem.getChildren().addAll(resultLabel);
        return resultItem;
    }


    public void historyPopUp(ActionEvent event, int idSport, String gender, int year) throws IOException, SQLException {
        ResultDao resultDao = new ResultDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Histórico de participações");

        VBox vbox = new VBox(400);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        List<List> results = resultDao.getResultBySport(idSport, gender, year);
        System.out.println(results);
        displayResults(vbox, results);


        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void displayResults(VBox vbox, List<List> results) {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");

        Map<String, List<String>> teamResultsMap = new HashMap<>();
        for (List result : results) {
            String teamName = result.get(5) != null ? result.get(5).toString() : null;
            String resultText = "Resultado: " + result.get(0).toString();
            String athleteName = result.get(4) != null ? result.get(4).toString() : "N/A";
            if (teamName != null) {
                teamResultsMap.computeIfAbsent(teamName, k -> new ArrayList<>()).add(resultText + " - " + athleteName);
            } else {
                VBox resultItem = createResultItem(result);
                scrollContent.getChildren().add(resultItem);
            }
        }

        for (Map.Entry<String, List<String>> entry : teamResultsMap.entrySet()) {
            VBox resultItem = createTeamResultItem(entry.getKey(), entry.getValue());
            scrollContent.getChildren().add(resultItem);
        }
        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }

    private VBox createResultItem(List result) {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        HBox nameContainer = new HBox(10);
        ImageView profileImage = new ImageView();
        URL iconImageURL = Main.class.getResource(result.get(8).toString());
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            profileImage.setImage(images);
            profileImage.setFitWidth(60);
            profileImage.setFitHeight(60);
            Circle clip = new Circle(30, 30, 30);
            profileImage.setClip(clip);
        }

        Label nameLabel = new Label(result.get(4).toString());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);
        nameContainer.getChildren().addAll(profileImage, nameLabel);

        Label resultLabel = new Label("Resultado: " + result.get(0).toString());
        resultLabel.getStyleClass().add("result-label");


        resultItem.getChildren().addAll(nameContainer, resultLabel);
        return resultItem;
    }

    private VBox createTeamResultItem(String teamName, List<String> results) {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        Label teamLabel = new Label("Equipa: " + teamName);
        teamLabel.getStyleClass().add("name-label");
        resultItem.getChildren().add(teamLabel);

        for (String result : results) {
            Label resultLabel = new Label(result);
            resultLabel.getStyleClass().add("result-label");
            resultItem.getChildren().add(resultLabel);
        }

        return resultItem;
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/register.fxml")));
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

    public void showAthletes(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/athletesView.fxml")));
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

    public void showTeamsEdit(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamEdit.fxml")));
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportEdit.fxml")));
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

    public void showStartSports(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/startSport.fxml")));
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

    /**
     * Preview the XML content before inserting it into the Database
     * @param content {String} XML Content
     * @return boolean
     * @throws JAXBException
     * @throws UnmarshalException
     */
    public boolean previewXmlContent(String content) throws JAXBException, UnmarshalException {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Confirmação de Inserção");
        dialog.setHeaderText("Os seguintes dados serão inseridos na base de dados:");
        dialog.setContentText("Verifique os detalhes antes de confirmar.");

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(600, 400);
        dialog.getDialogPane().setContent(textArea);
        ButtonType insertButton = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(insertButton, cancelButton);

        boolean[] finalResult = {false};
        dialog.showAndWait().ifPresent(response -> {
            if (response == insertButton) finalResult[0] = true;
        });
        return finalResult[0];
    }

    /**
     * Load the Athletes from an XML file
     * @param event {ActionEvent} Event
     * @throws IOException
     */
    @FXML
    public void loadTeams(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);
        //FileChooser.ExtensionFilter xsdFilter = new FileChooser.ExtensionFilter("XSD Files (*.xsd)", "*.xsd");
        //fileChooser.getExtensionFilters().addAll(xmlFilter, xsdFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).count();
            long xmlCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).count();
            //long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xsd")).count();

            if (selectedFiles.size() == 2 && xmlCount == 1 && xsdCount == 1) {
                System.out.println("Selected files are valid: " + selectedFiles);

                String xsdPath = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).findFirst().get().getAbsolutePath();

                XMLUtils xmlUtils = new XMLUtils();
                boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

                if(xmlValid) {
                    try {
                        Teams teams = xmlUtils.getTeamsDataXML(xmlPath);
                        String content = teams.toString();
                        if(previewXmlContent(content)) {
                            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                            boolean uploaded = uploadXmlDAO.addTeams(teams);
                            if (uploaded) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                                alerta.show();
                            }

                            boolean xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao guardar o ficheiro XML/XSD!");
                                alerta.show();
                            }
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Operação Cancelada!");
                            alerta.setHeaderText("Nenhum dado será inserido!");
                            alerta.show();
                        }
                    }  catch (JAXBException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Os dados do XML não coincidem com a opção escolhida!");
                        alerta.show();
                    } catch (SQLException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                        alerta.show();
                    }
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Baseado no XSD, o XML está incorreto!");
                    alerta.show();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("Selecione apenas 2 ficheiros! 1 .xml e 1 xsd.xml");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("Selecione 2 ficheiros! 1 .xml e 1 xsd.xml");
            alerta.show();
        }
    }

    /**
     * Load the Sports from an XML file
     * @param event {ActionEvent} Event
     * @throws IOException
     */
    public void loadSports(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);
        //FileChooser.ExtensionFilter xsdFilter = new FileChooser.ExtensionFilter("XSD Files (*.xsd)", "*.xsd");
        //fileChooser.getExtensionFilters().addAll(xmlFilter, xsdFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).count();
            long xmlCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).count();
            //long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xsd")).count();

            if (selectedFiles.size() == 2 && xmlCount == 1 && xsdCount == 1) {
                System.out.println("Selected files are valid: " + selectedFiles);

                String xsdPath = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).findFirst().get().getAbsolutePath();

                XMLUtils xmlUtils = new XMLUtils();
                boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

                if(xmlValid) {
                    try {
                        Sports sports = xmlUtils.getSportsDataXML(xmlPath);
                        String content = sports.toString();
                        if(previewXmlContent(content)) {
                            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                            boolean uploaded = uploadXmlDAO.addSports(sports);
                            if (uploaded) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                                alerta.show();
                            }

                            boolean xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao guardar o ficheiro XML/XSD!");
                                alerta.show();
                            }
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Operação Cancelada!");
                            alerta.setHeaderText("Nenhum dado será inserido!");
                            alerta.show();
                        }
                    }  catch (JAXBException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Os dados do XML não coincidem com a opção escolhida!");
                        alerta.show();
                    } catch (SQLException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                        alerta.show();
                    }
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Baseado no XSD, o XML está incorreto!");
                    alerta.show();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("Selecione apenas 2 ficheiros! 1 .xml e 1 xsd.xml");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("Selecione 2 ficheiros! 1 .xml e 1 xsd.xml");
            alerta.show();
        }
    }

    /**
     * Load the Athletes from an XML file
     * @param event {ActionEvent} Event
     * @throws IOException
     */
    public void loadAthletes(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);
        //FileChooser.ExtensionFilter xsdFilter = new FileChooser.ExtensionFilter("XSD Files (*.xsd)", "*.xsd");
        //fileChooser.getExtensionFilters().addAll(xmlFilter, xsdFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).count();
            long xmlCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).count();
            //long xsdCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".xsd")).count();

            if (selectedFiles.size() == 2 && xmlCount == 1 && xsdCount == 1) {
                System.out.println("Selected files are valid: " + selectedFiles);

                String xsdPath = selectedFiles.stream().filter(file -> file.getName().endsWith("xsd.xml")).findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream().filter(file -> file.getName().endsWith(".xml") && !file.getName().endsWith("_xsd.xml")).findFirst().get().getAbsolutePath();

                XMLUtils xmlUtils = new XMLUtils();
                boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

                if(xmlValid) {
                    try {
                        Athletes athletes = xmlUtils.getAthletesDataXML(xmlPath);
                        String content = athletes.toString();
                        if(previewXmlContent(content)) {
                            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                            boolean uploaded = uploadXmlDAO.addAthletes(athletes);
                            if (uploaded) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                                alerta.show();
                            }

                            boolean xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro!");
                                alerta.setHeaderText("Ocorreu um erro ao guardar o ficheiro XML/XSD!");
                                alerta.show();
                            }
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Operação Cancelada!");
                            alerta.setHeaderText("Nenhum dado será inserido!");
                            alerta.show();
                        }
                    }  catch (JAXBException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Os dados do XML não coincidem com a opção escolhida!");
                        alerta.show();
                    } catch (SQLException e) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("Erro!");
                        alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                        alerta.show();
                    }
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Baseado no XSD, o XML está incorreto!");
                    alerta.show();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("Selecione apenas 2 ficheiros! 1 .xml e 1 xsd.xml");
                alerta.show();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("Selecione 2 ficheiros! 1 .xml e 1 xsd.xml");
            alerta.show();
        }
    }

    public boolean sportStart(int idSport, int year, int idocal) throws SQLException {
        List<Integer> IdsParticipants;

        registrationDao.setStatusRejected(idSport, year);

        if (sportDao.getType(idSport).equals("Individual")) {
            IdsParticipants = registrationDao.getRegisteredAthletes(idSport, year);
            if (sportDao.getOneGame(idSport).equals("One")) {
                System.out.println("Individual One");
                individualOne(idSport, IdsParticipants, year, idocal);
            }
            if (sportDao.getOneGame(idSport).equals("Multiple")) {
                System.out.println("Individual Multiple");
                individualMultiple(idSport, IdsParticipants, year, idocal);
            }
        }
        if (sportDao.getType(idSport).equals("Collective")) {
            IdsParticipants = registrationDao.getRegisteredTeams(idSport, year);
            if (sportDao.getOneGame(idSport).equals("One")) {
                System.out.println("Collective One");
                CollectiveOne(idSport, IdsParticipants, year, idocal);
            }
            if (sportDao.getOneGame(idSport).equals("Multiple")) {
                System.out.println("Collective Multiple");
                CollectiveMultiple(idSport, IdsParticipants, year, idocal);
            }
        }

        registrationDao.setStatusFinished(idSport, year);

        return true;
    }

    public boolean individualOne(int idSport, List<Integer> IdsParticipants, int year, int idLocal) throws SQLException {
        List<Integer> resultados = new ArrayList<>();
        Random random = new Random();

        //Gerar Resultados
        int resultado;
        List<Integer> range = sportDao.getRange(idSport);
        for (int i = 0; i < IdsParticipants.size(); i++) {
            do {
                resultado = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
            } while (resultados.contains(resultado));
            resultados.add(resultado);
        }
        System.out.println(IdsParticipants);
        System.out.println(resultados);

        //Ordenar Resultados
        for (int i = 0; i < resultados.size(); i++) {
            for (int j = i + 1; j < resultados.size(); j++) {
                if (resultados.get(j) < resultados.get(i)) {
                    Collections.swap(resultados, i, j);
                    Collections.swap(IdsParticipants, i, j);
                }
            }
        }
        System.out.println(IdsParticipants);
        System.out.println(resultados);

        //Atribuír Resultados
        for (int i = 0; i < resultados.size(); i++) {
            int idAthlete = IdsParticipants.get(i);
            int resultadoInserir = resultados.get(i);
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            resultDao.addResultAthlete(idSport, idAthlete, date, String.valueOf(resultadoInserir), idLocal);
        }

        //Atribuir Medalhas
        medalDao.addTopMedalAthlete(IdsParticipants.getFirst(), year, 1);
        medalDao.addTopMedalAthlete(IdsParticipants.get(1), year, 2);
        medalDao.addTopMedalAthlete(IdsParticipants.get(2), year, 3);
        for (int i = 3; i < IdsParticipants.size(); i++) {
            medalDao.addTopMedalAthlete(IdsParticipants.get(i), year, 4);
        }

        //Inserir Vencedor Olímpico
        winnerOlympicDao.addWinnerOlympicAthleteOne(idSport, year, IdsParticipants.getFirst(), resultados.getFirst());

        //Verificar Recorde Olímpico
        Integer olympicRecord = olympicRecordDao.getOlympicRecord(idSport);
        if (olympicRecord != null) {
            if (resultados.getFirst() < olympicRecord) {
                olympicRecordDao.setNewOlympicRecordAthleteOne(idSport, year, IdsParticipants.getFirst(), resultados.getFirst());
            }
        }
        return true;
    }

    public boolean CollectiveOne(int idSport, List<Integer> IdsParticipants, int year, int idLocal) throws SQLException {
        List<Integer> resultados = new ArrayList<>();
        Random random = new Random();

        //Gerar Resultados
        int resultado;
        List<Integer> range = sportDao.getRange(idSport);
        for (int i = 0; i < IdsParticipants.size(); i++) {
            do {
                resultado = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
            } while (resultados.contains(resultado));
            resultados.add(resultado);
        }
        System.out.println(IdsParticipants);
        System.out.println(resultados);

        //Ordenar Resultados
        for (int i = 0; i < resultados.size(); i++) {
            for (int j = i + 1; j < resultados.size(); j++) {
                if (resultados.get(j) < resultados.get(i)) {
                    Collections.swap(resultados, i, j);
                    Collections.swap(IdsParticipants, i, j);
                }
            }
        }
        System.out.println(IdsParticipants);
        System.out.println(resultados);

        //Atribuír Resultados
        List<Integer> athletes = new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            int idTeam = IdsParticipants.get(i);
            int resultadoInserir = resultados.get(i);
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            //resultDao.addResultTeam(idSport, idTeam, date, resultadoInserir, idLocal);
            athletes.clear();
            athletes = registrationDao.getAthletesByTeam(idTeam, idSport, year);
            for (int j = 0; j < athletes.size(); j++) {
                resultDao.addResultAthleteTeam(idSport, athletes.get(j), idTeam, date, String.valueOf(resultadoInserir), idLocal);
            }
        }

        //Atribuir Medalhas
        //medalDao.addTopMedalTeam(IdsParticipants.getFirst(), year, 1);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.getFirst(), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.getFirst(), year, 1);
        }
        //medalDao.addTopMedalTeam(IdsParticipants.get(1), year, 2);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(1), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(1), year, 2);
        }
        //medalDao.addTopMedalTeam(IdsParticipants.get(2), year, 3);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(2), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(2), year, 3);
        }
        for (int i = 3; i < IdsParticipants.size(); i++) {
            //medalDao.addTopMedalTeam(IdsParticipants.get(i), year, 4);
            athletes.clear();
            athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(i), idSport, year);
            for (int j = 0; j < athletes.size(); j++) {
                medalDao.addTopMedalAthleteTeam(athletes.get(j), IdsParticipants.get(i), year, 4);
            }
        }

        //Inserir Vencedor Olímpico
        winnerOlympicDao.addWinnerOlympicTeamOne(idSport, year, IdsParticipants.getFirst(), resultados.getFirst());

        //Verificar Recorde Olímpico
        Integer olympicRecord = olympicRecordDao.getOlympicRecord(idSport);
        if (olympicRecord != null) {
            if (resultados.getFirst() < olympicRecord) {
                olympicRecordDao.setNewOlympicRecordTeamOne(idSport, year, IdsParticipants.getFirst(), resultados.getFirst());
            }
        }
        return true;
    }

    public boolean individualMultiple(int idSport, List<Integer> IdsParticipants, int year, int idLocal) throws SQLException {
        List<Integer> scores = new ArrayList<>(Collections.nCopies(IdsParticipants.size(), 0));
        List<List<String>> resultados = new ArrayList<>();
        Random random = new Random();

        //Gerar Resultados
        List<Integer> range = sportDao.getRange(idSport);
        int resultado1, resultado2;
        do {
            Collections.fill(scores, 0);
            resultados.clear();
            for (int i = 0; i < IdsParticipants.size(); i++) {
                resultados.add(new ArrayList<>());
            }
            //Jogos entre os Participantes
            for (int i = 0; i < IdsParticipants.size(); i++) {
                for (int j = i + 1; j < IdsParticipants.size(); j++) {
                    resultado1 = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
                    resultado2 = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
                    if (resultado1 > resultado2) {
                        scores.set(i, scores.get(i) + 3);
                    } else if (resultado1 < resultado2) {
                        scores.set(j, scores.get(j) + 3);
                    } else {
                        scores.set(i, scores.get(i) + 1);
                        scores.set(j, scores.get(j) + 1);
                    }

                    String resultadoP = "(" + resultado1 + ") - " + resultado2;
                    String resultadoS = resultado1 + " - (" + resultado2 + ")";
                    resultados.get(i).add(resultadoP);
                    resultados.get(j).add(resultadoS);


                    System.out.println(IdsParticipants.get(i) + " " + IdsParticipants.get(j) + " " + resultado1 + " " + resultado2);
                }
            }
            //Ordenar Resultados
            for (int i = 0; i < scores.size(); i++) {
                for (int j = i + 1; j < scores.size(); j++) {
                    if (scores.get(j) > scores.get(i)) {
                        Collections.swap(scores, i, j);
                        Collections.swap(IdsParticipants, i, j);
                        Collections.swap(resultados, i, j);
                    }
                }
            }
            System.out.println("Resultados após a ordenação:");
            for (int i = 0; i < IdsParticipants.size(); i++) {
                System.out.println("ID: " + IdsParticipants.get(i) + ", Pontuação: " + scores.get(i));
            }
            System.out.println();
        } while (scores.getFirst() == scores.get(1) || scores.get(1) == scores.get(2) || scores.get(2) == scores.get(3));


        //Atribuir Resultados
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        for (int i = 0; i < resultados.size(); i++) {
            for (String resultado : resultados.get(i)) {
                resultDao.addResultAthlete(idSport, IdsParticipants.get(i), date, resultado, idLocal);
                System.out.println("DAO: " + idSport + " " + IdsParticipants.get(i) + " " + date + " " + resultado);
            }
        }

        //Atribuir Medalhas
        medalDao.addTopMedalAthlete(IdsParticipants.getFirst(), year, 1);
        medalDao.addTopMedalAthlete(IdsParticipants.get(1), year, 2);
        medalDao.addTopMedalAthlete(IdsParticipants.get(2), year, 3);
        for (int i = 3; i < IdsParticipants.size(); i++) {
            medalDao.addTopMedalAthlete(IdsParticipants.get(i), year, 4);
        }

        //Inserir Vencedor Olímpico
        winnerOlympicDao.addWinnerOlympicAthleteMultiple(idSport, year, IdsParticipants.getFirst());

        //Verificar Recorde Olímpico
        for (int i = 0; i < 3; i++) {
            Integer olympicRecordMedals = olympicRecordDao.getOlympicRecordMultipleGames(idSport);
            if (olympicRecordMedals != null) {
                int totalMedals = winnerOlympicDao.getMedalsByAthlete(idSport, IdsParticipants.get(i));
                if (totalMedals > olympicRecordMedals) {
                    olympicRecordDao.setNewOlympicRecordAthleteMultiple(idSport, year, IdsParticipants.get(i), totalMedals);
                }
            }
        }

        return true;
    }


    public boolean CollectiveMultiple(int idSport, List<Integer> IdsParticipants, int year, int idLocal) throws SQLException {
        List<Integer> scores = new ArrayList<>(Collections.nCopies(IdsParticipants.size(), 0));
        List<List<String>> resultados = new ArrayList<>();
        Random random = new Random();

        //Gerar Resultados
        List<Integer> range = sportDao.getRange(idSport);
        int resultado1, resultado2;
        do {
            Collections.fill(scores, 0);
            resultados.clear();
            for (int i = 0; i < IdsParticipants.size(); i++) {
                resultados.add(new ArrayList<>());
            }
            //Jogos entre os Participantes
            for (int i = 0; i < IdsParticipants.size(); i++) {
                for (int j = i + 1; j < IdsParticipants.size(); j++) {
                    resultado1 = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
                    resultado2 = range.getFirst() + random.nextInt(range.getLast() - range.getFirst());
                    if (resultado1 > resultado2) {
                        scores.set(i, scores.get(i) + 3);
                    } else if (resultado1 < resultado2) {
                        scores.set(j, scores.get(j) + 3);
                    } else {
                        scores.set(i, scores.get(i) + 1);
                        scores.set(j, scores.get(j) + 1);
                    }

                    String resultadoP = "(" + resultado1 + ") - " + resultado2;
                    String resultadoS = resultado1 + " - (" + resultado2 + ")";
                    resultados.get(i).add(resultadoP);
                    resultados.get(j).add(resultadoS);


                    System.out.println(IdsParticipants.get(i) + " " + IdsParticipants.get(j) + " " + resultado1 + " " + resultado2);
                }
            }
            //Ordenar Resultados
            for (int i = 0; i < scores.size(); i++) {
                for (int j = i + 1; j < scores.size(); j++) {
                    if (scores.get(j) > scores.get(i)) {
                        Collections.swap(scores, i, j);
                        Collections.swap(IdsParticipants, i, j);
                        Collections.swap(resultados, i, j);
                    }
                }
            }
            System.out.println("Resultados após a ordenação:");
            for (int i = 0; i < IdsParticipants.size(); i++) {
                System.out.println("ID: " + IdsParticipants.get(i) + ", Pontuação: " + scores.get(i));
            }
            System.out.println();
        } while (scores.getFirst() == scores.get(1) || scores.get(1) == scores.get(2) || scores.get(2) == scores.get(3));

        //Atribuir Resultados
        System.out.println("Atribuir Resultados");
        List<Integer> athletes = new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            int idTeam = IdsParticipants.get(i);
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            athletes.clear();
            athletes = registrationDao.getAthletesByTeam(idTeam, idSport, year);
            for (int j = 0; j < athletes.size(); j++) {
                for (String resultado : resultados.get(i)) {
                    resultDao.addResultAthleteTeam(idSport, athletes.get(j), idTeam, date, resultado, idLocal);
                    System.out.println("DAO: " + idSport + " " + athletes.get(j) + " " + idTeam + " " + date + " " + resultado);
                }
            }
        }

        //Atribuir Medalhas
        System.out.println("Atribuir Medalhas");
        //medalDao.addTopMedalTeam(IdsParticipants.getFirst(), year, 1);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.getFirst(), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.getFirst(), year, 1);
        }
        //medalDao.addTopMedalTeam(IdsParticipants.get(1), year, 2);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(1), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(1), year, 2);
        }
        //medalDao.addTopMedalTeam(IdsParticipants.get(2), year, 3);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(2), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(2), year, 3);
        }
        for (int i = 3; i < IdsParticipants.size(); i++) {
            //medalDao.addTopMedalTeam(IdsParticipants.get(i), year, 4);
            athletes.clear();
            athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(i), idSport, year);
            for (int j = 0; j < athletes.size(); j++) {
                medalDao.addTopMedalAthleteTeam(athletes.get(j), IdsParticipants.get(i), year, 4);
            }
        }

        //Inserir Vencedor Olímpico
        System.out.println("Atribuir Vencedor Olímpico");
        winnerOlympicDao.addWinnerOlympicTeamMultiple(idSport, year, IdsParticipants.getFirst());

        //Verificar Recorde Olímpico
        System.out.println("Atribuir Recorde Olimpico");
        for (int i = 0; i < 3; i++) {
            Integer olympicRecordMedals = olympicRecordDao.getOlympicRecordMultipleGames(idSport);
            if (olympicRecordMedals != null) {
                int totalMedals = winnerOlympicDao.getMedalsByTeam(idSport, IdsParticipants.get(i));
                if (totalMedals > olympicRecordMedals) {
                    olympicRecordDao.setNewOlympicRecordTeamMultiple(idSport, year, IdsParticipants.get(i), totalMedals);
                }
            }
        }

        return true;
    }
    public void showAddEvent(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/addEvent.fxml")));
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
    public void showViewEvent(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/eventsView.fxml")));
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
    public void updateImageEvent(ActionEvent event) throws SQLException {
        String pathSave = "src/main/java/ImagesEvent/";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long pngCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".png")).count();
            long jpgCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpg")).count();
            long jpegCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpeg")).count();

            if (selectedFiles.size() == 1 && (pngCount == 1 || jpgCount == 1 || jpegCount == 1)) {
                File selectedFile = selectedFiles.get(0);

                int tempEventYear = 1900;
                boolean saved = saveEventImage(tempEventYear, pathSave, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();


                    EventDao eventDao = new EventDao();
                    eventDao.updateEventImage(tempEventYear, pathSave, "." + selectedFile.getName().split("\\.")[1]);
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
    public boolean saveEventImage(int tempEventYear, String pathSave, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(tempEventYear) + "." + fileImage.getName().split("\\.")[1];

        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
    public void showTeamsView(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamsView.fxml")));
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
}
