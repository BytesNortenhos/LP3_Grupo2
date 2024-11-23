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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
        for (String year : years) {
            yearsOptions.add(year);
        }
        yearDrop.setItems(yearsOptions);
    }

    public void showSports(ActionEvent event) throws SQLException {
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

        int idSport = Integer.parseInt(sport.get(0).toString());
        int nPart = sportDao.getNumberParticipantsSport(idSport, year);
        int mPart = Integer.parseInt(sport.get(5).toString());

        Label nameLabel = new Label(sport.get(3).toString());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label(sport.get(1).toString());
        typeLabel.getStyleClass().add("type-label");

        Label genderLabel = new Label(sport.get(2).toString());
        genderLabel.getStyleClass().add("gender-label");

        Label minPart = new Label("Minímo de participantes: " + mPart);
        minPart.getStyleClass().add("minPart-label");

        Label numPart = new Label("Número de participantes: " + nPart);
        numPart.getStyleClass().add("numPart-label");

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
                requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, startButton);
                startButton.setOnAction(event -> {
                    try {
                        if (sportDao.verifyRanges(idSport)) {
                            if (iniciarModalidades(idSport, year)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Sucesso!");
                                alert.setHeaderText("Modalidade inicada com sucesso!");
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
                requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart);
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
            Button viewButton = new Button();
            viewButton.setGraphic(viewImageView);
            viewButton.getStyleClass().add("viewButton");
            requestItem.getChildren().addAll(nameLabel, typeLabel, genderLabel, minPart, numPart, viewButton);
        }

        requestItem.setPrefWidth(500); // Ensure this width allows two items per line
        return requestItem;
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

    public void mostrarRegistar(ActionEvent event) throws IOException {
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

    public void mostrarRegistaModalidades(ActionEvent event) throws IOException {
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

    public void mostrarModalidades(ActionEvent event) throws IOException {
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

    public void mostrarIniciarModalidades(ActionEvent event) throws IOException {
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

        //ALTERAR TBLREGISTRATION PARA IDSTATUS 4

        return true;
    }

    public void individualOne(int idSport, List<Integer> IdsParticipants, int year) throws SQLException {
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
    }

    public void CollectiveOne(int idSport, List<Integer> IdsParticipants, int year) throws SQLException {
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
                resultDao.addResultTeam(idSport, idTeam, date, resultadoInserir, 2);
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
        medalDao.addTopMedalTeam(IdsParticipants.getFirst(), year, 1);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.getFirst(), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.getFirst(), year, 1);
        }
        medalDao.addTopMedalTeam(IdsParticipants.get(1), year, 2);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(1), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(1), year, 2);
        }
        medalDao.addTopMedalTeam(IdsParticipants.get(2), year, 3);
        athletes.clear();
        athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(2), idSport, year);
        for (int i = 0; i < athletes.size(); i++) {
            medalDao.addTopMedalAthleteTeam(athletes.get(i), IdsParticipants.get(2), year, 3);
        }
        for (int i = 3; i < IdsParticipants.size(); i++) {
            try {
                medalDao.addTopMedalTeam(IdsParticipants.get(i), year, 4);
                athletes.clear();
                athletes = registrationDao.getAthletesByTeam(IdsParticipants.get(i), idSport, year);
                for (int j = 0; j < athletes.size(); j++) {
                    medalDao.addTopMedalAthleteTeam(athletes.get(j), IdsParticipants.get(i), year, 4);
                }
            } catch (SQLException e) {
            }
        }

        //Verificar Recorde Olímpico
    }

    public void individualMultiple(int idSport, List<Integer> IdsParticipants) throws SQLException {
    }

    public void CollectiveMultiple(int idSport, List<Integer> IdsParticipants) throws SQLException {
    }

}
