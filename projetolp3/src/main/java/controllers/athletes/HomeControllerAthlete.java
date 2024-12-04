package controllers.athletes;

import Dao.*;
import Models.Medal;
import Models.Registration;
import bytesnortenhos.projetolp3.Main;
import controllers.LoginController;
import controllers.ViewsController;
import javafx.application.Platform;
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
import javafx.scene.input.ScrollEvent;
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeControllerAthlete {
    @FXML
    private Label goldMedals;
    @FXML
    private Label silverMedals;
    @FXML
    private Label bronzeMedals;
    @FXML
    private Label certificate;
    @FXML
    private Label textWelcome;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    private static boolean isDarkMode = true;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    @FXML
    private ImageView iconAthlete;
    @FXML
    private Label textSportsAthlete;
    @FXML
    private VBox sportsContainerAthlete;
    @FXML
    private SplitMenuButton athleteImageSplitButton;

    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    int idAthlete = 0;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private ScrollPane scrollPaneAthlete;

    public void initialize() throws SQLException {
        sportsContainerAthlete.minHeightProperty().bind(scrollPaneAthlete.heightProperty());
        sportsContainerAthlete.minWidthProperty().bind(scrollPaneAthlete.widthProperty());
        scrollPaneAthlete.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        idAthlete = LoginController.idAthlete;
        loadIcons();
        displayWelcomeMessasge();
        displayMedals();
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        athleteImageSplitButton.setOnMouseClicked(mouseEvent -> athleteImageSplitButton.show());
        List<List> registrations = null;
        try {
            registrations = getPendingRegistrations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!registrations.isEmpty()) {
            displayRequests(registrations);
        }
    }
    private List<List> getPendingRegistrations() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<List> registrations = registrationDao.getUserRegistration(idAthlete);
        return registrations;
    }
    private void displayRequests(List<List> registrations) {
        textSportsAthlete.setVisible(true);
        sportsContainerAthlete.setVisible(true);
        sportsContainerAthlete.getChildren().clear();
        sportsContainerAthlete.setSpacing(20);
        for (List registration : registrations) {
            VBox requestItem = createRequestItem(registration);
            sportsContainerAthlete.getChildren().add(requestItem);
        }
    }
    private VBox createRequestItem(List request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(20);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(request.get(6).toString());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label(request.get(7).toString());
        typeLabel.getStyleClass().add("type-label");

        requestItem.getChildren().addAll(nameLabel,typeLabel);
        return requestItem;
    }

    @FXML
    private void updateImage(ActionEvent event) throws SQLException, IOException {
        String pathSave = "src/main/resources/bytesnortenhos/projetolp3/ImagesAthlete/";
        String pathSaveTemp = Main.class.getResource("ImagesAthlete").toExternalForm().replace("file:", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");
        String pathToSave = "ImagesAthlete/";

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

                boolean saved = saveAthleteImage(idAthlete, pathSave, pathSaveTemp, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();

                    AthleteDao athleteDao = new AthleteDao();
                    athleteDao.updateAthleteImage(idAthlete, pathToSave, "." + selectedFile.getName().split("\\.")[1]);
                    returnHomeMenu(event);
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


    public boolean saveAthleteImage(int tempAthleteId, String pathSave, String pathSaveTemp, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(tempAthleteId) + "." + fileImage.getName().split("\\.")[1];
        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(fileImage.toPath(), Path.of(pathSaveTemp, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }

        return true;
    }


    public void loadIcons() throws SQLException {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if(iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if(iconHomeNav != null) iconHomeNav.setImage(image);

        AthleteDao athleteDao = new AthleteDao();
        String athleteImage = athleteDao.getImageAthlete(idAthlete);

        URL iconLogoutNavURL = Main.class.getResource(athleteImage);
        image = new Image(iconLogoutNavURL.toExternalForm());
        iconAthlete.setFitWidth(50);
        iconAthlete.setFitHeight(50);

        if (iconAthlete != null) {
            iconAthlete.setImage(image);
        }
    }

    private void displayWelcomeMessasge() throws SQLException{
        AthleteDao athleteDao = new AthleteDao();
        textWelcome.setText("Bem vindo "+athleteDao.getAthlheteNameByID(idAthlete)+"!");
    }
    private void displayMedals() throws SQLException {
        MedalDao medalDao = new MedalDao();
        OlympicRecordDao olympicRecordDao = new OlympicRecordDao();
        goldMedals.setText((medalDao.countGoldMedals(idAthlete))+" medalhas de ouro🥇");
        silverMedals.setText((medalDao.countSilverMedals(idAthlete))+" medalhas de prata🥈");
        bronzeMedals.setText((medalDao.countBronzeMedals(idAthlete))+" medalhas de bronze🥉");
        certificate.setText((medalDao.countCertificate(idAthlete))+" certificados📜");
    }


    public boolean changeMode(ActionEvent event){
        isDarkMode = !isDarkMode;
        if(isDarkMode){
            setDarkMode();
        }
        else{
            setLightMode();
        }
        return isDarkMode;
    }

    public void setLightMode(){
        parent.getStylesheets().remove(cssDark);
        parent.getStylesheets().add(cssLight);
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconModeNav.setImage(image);
    }

    public void setDarkMode(){
        parent.getStylesheets().remove(String.valueOf(cssLight));
        parent.getStylesheets().add(String.valueOf(cssDark));
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconModeNav.setImage(image);
    }


    public void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/home.fxml")));
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(Main.class.getResource("css/dark.css").toExternalForm());
        } else {
            scene.getStylesheets().add(Main.class.getResource("css/light.css").toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    public void showSportsRegister(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/sportsRegister.fxml")));
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

    public void showLogin(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/loginView.fxml")));
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

    public void historyPopUp(ActionEvent event) throws IOException, SQLException {
        ResultDao resultDao = new ResultDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Histórico de participações");

        VBox vbox = new VBox(400);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        List<List> results = resultDao.getResultByAthlete(idAthlete);
        displayResults(vbox, results);


        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    private void displayResults(VBox vbox, List<List> results) throws SQLException {
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
            VBox resultItem = createResultItem(result);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }
    private VBox createResultItem(List result) throws SQLException {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);
        Label nameLabel = new Label("Modalidade: " + (result.get(1) != null ? result.get(1).toString() : "N/A"));
        nameLabel.getStyleClass().add("name-label");
        Label resultLabel = new Label("");

        if(result.get(2).toString().equals("Individual")){
            double resultValue = Float.parseFloat(result.get(0).toString());
            double resultSeconds = resultValue / 1000.0;
            resultLabel = new Label("Resultado: " + resultSeconds + " segundos");
        }
        else{
            String resultValue = String.valueOf(result.get(0).toString());
            resultLabel = new Label("Resultado: " + resultValue + " segundos");
        }
        resultLabel.getStyleClass().add("result-label");

        Label typeLabel = new Label("Tipo de modalidade: " + (result.get(2) != null ? result.get(2).toString() : "N/A"));
        typeLabel.getStyleClass().add("type-label");

        if (result.get(3) != null) {
            Label teamLabel = new Label("Equipa: " + result.get(3).toString());
            teamLabel.getStyleClass().add("type-label");
            resultItem.getChildren().add(teamLabel);
        }

        Label dateLabel = new Label("Data: " + (result.get(4) != null ? result.get(4).toString() : "N/A"));
        dateLabel.getStyleClass().add("date-label");

        Label localLabel = new Label("Local: " + (result.get(5) != null ? result.get(5).toString() : "N/A"));
        localLabel.getStyleClass().add("local-label");

        /*int pos = 0;
        ResultDao resultDao = new ResultDao();
        List<List> positions = new ArrayList<>();
        if (result.get(2).equals("Collective")){
            if (result.get(7).equals("One")){
                positions = resultDao.getPositionByIdCollective((Integer) result.get(6), result.get(4).toString());
            }
            if (result.get(7).equals("Multiple")){
                positions = resultDao.getPositionByIdCollective((Integer) result.get(6), result.get(4).toString());
            }
        }
        if (result.get(2).equals("Individual")){
            if (result.get(7).equals("One")){
                positions = resultDao.getPositionByIdIndividualOne((Integer) result.get(6), result.get(4).toString());
            }
            if (result.get(7).equals("Multiple")){
                positions = resultDao.getPositionByIdIndividualMultiple((Integer) result.get(6), result.get(4).toString());
            }
        }
        for (List position : positions) {
            if(position != null && result.get(8) != null){
                if(Integer.parseInt(position.get(1).toString()) == idAthlete || Integer.parseInt(position.get(1).toString()) == Integer.parseInt(result.get(8).toString())){
                    pos = Integer.parseInt(position.get(0).toString());
                    System.out.println("siuuu");
                }
            }

        }
        Label postionLabel = new Label("Posição: " + pos + "º lugar");
        postionLabel.getStyleClass().add("local-label");*/

        resultItem.getChildren().addAll(nameLabel, resultLabel, typeLabel, dateLabel, localLabel/*, postionLabel*/);
        return resultItem;
    }

}