package controllers.admin;

import Dao.MedalDao;
import Dao.RegistrationDao;
import Dao.ResultDao;
import Dao.SportDao;
import Models.*;
import bytesnortenhos.projetolp3.Main;
import controllers.ViewsController;
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
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.foreign.MemoryLayout;
import java.net.URL;
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
        System.out.println(sport);
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

        if(sportType.equals("Individual")){
            nPart = sportDao.getNumberParticipantsSport(idSport, year);
            minPart = new Label("Minímo de participantes: " + mPart);
            minPart.getStyleClass().add("minPart-label");

            numPart = new Label("Número de participantes: " + nPart);
            numPart.getStyleClass().add("numPart-label");
        }
        else{

            nPart = sportDao.getNumberTeamsSport(idSport, year);
            minPart = new Label("Minímo de equipas: " + mPart);
            minPart.getStyleClass().add("minPart-label");

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
                        if (sportDao.verifyRanges(idSport)) {
                            if (iniciarModalidades(idSport, year)) {
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
                        e.printStackTrace();
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
            requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, viewResultsButton);
        }

        requestItem.setPrefWidth(500); // Ensure this width allows two items per line
        return requestItem;
    }

    public void showRegistedAthletes(int idSport, int year) throws SQLException {
        SportDao sportDao = new SportDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Atletas inscritos");


        VBox vbox = new VBox(600);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        if(sportDao.verifyIfIsTeam(idSport, year)){
            List<List> results = sportDao.getTeamsAndthletes(idSport, year);
            displayTeams(vbox, results);
            Scene scene = new Scene(vbox, 600, 450);
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
            popupStage.setScene(scene);
            popupStage.show();
        }
        else{
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
        for(Athlete athlete : results){
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

    public boolean iniciarModalidades(int idSport, int year) throws SQLException {
        List<Integer> IdsParticipants;

        if (sportDao.getType(idSport).equals("Individual")) {
            IdsParticipants = registrationDao.getRegisteredAthletes(idSport, year);
            if (sportDao.getOneGame(idSport).equals("One")) {
                System.out.println("Individual One");
                individualOne(idSport, IdsParticipants, year);
            }
            if (sportDao.getOneGame(idSport).equals("Multiple")) {
                System.out.println("Individual Multiple");
                individualMultiple(idSport, IdsParticipants);
            }
        }
        if (sportDao.getType(idSport).equals("Collective")) {
            IdsParticipants = registrationDao.getRegisteredTeams(idSport, year);
            if (sportDao.getOneGame(idSport).equals("One")) {
                System.out.println("Collective One");
                CollectiveOne(idSport, IdsParticipants, year);
            }
            if (sportDao.getOneGame(idSport).equals("Multiple")) {
                System.out.println("Collective Multiple");
                CollectiveMultiple(idSport, IdsParticipants);
            }
        }

        registrationDao.setStatusFinished(idSport, year);

        return true;
    }

    public boolean individualOne(int idSport, List<Integer> IdsParticipants, int year) throws SQLException {
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
            try {
                resultDao.addResultAthlete(idSport, idAthlete, date, resultadoInserir, 2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Atribuir Medalhas
        medalDao.addTopMedalAthlete(IdsParticipants.getFirst(), year, 1);
        medalDao.addTopMedalAthlete(IdsParticipants.get(1), year, 2);
        medalDao.addTopMedalAthlete(IdsParticipants.get(2), year, 3);
        for (int i = 3; i < IdsParticipants.size(); i++) {
            try {
                medalDao.addTopMedalAthlete(IdsParticipants.get(i), year, 4);
            } catch (SQLException e) {
            }
        }

        //Verificar Recorde Olímpico

        return true;
    }

    public boolean CollectiveOne(int idSport, List<Integer> IdsParticipants, int year) throws SQLException {
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
            try {
                //resultDao.addResultTeam(idSport, idTeam, date, resultadoInserir, 2);
                athletes.clear();
                athletes = registrationDao.getAthletesByTeam(idTeam, idSport, year);
                for (int j = 0; j < athletes.size(); j++) {
                    resultDao.addResultAthleteTeam(idSport, athletes.get(j), idTeam, date, resultadoInserir, 2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
            try {
                //medalDao.addTopMedalTeam(IdsParticipants.get(i), year, 4);
                athletes.clear();
                athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(i), idSport, year);
                for (int j = 0; j < athletes.size(); j++) {
                    medalDao.addTopMedalAthleteTeam(athletes.get(j), IdsParticipants.get(i), year, 4);
                }
            } catch (SQLException e) {
            }
        }

        //Verificar Recorde Olímpico

        return true;
    }

    public boolean individualMultiple(int idSport, List<Integer> IdsParticipants) throws SQLException {
        return true;
    }

    public boolean CollectiveMultiple(int idSport, List<Integer> IdsParticipants) throws SQLException {
        return true;
    }

}
