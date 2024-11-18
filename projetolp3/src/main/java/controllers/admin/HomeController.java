package controllers.admin;

import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.RegistrationDao;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import controllers.LoginController;
import controllers.ViewsController;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import Models.Registration;

public class HomeController {
    @FXML
    private FlowPane mainContainer;
    @FXML
    private Label noRequestsLabel;
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
    private SplitMenuButton xmlSplitButton;
    @FXML
    private ComboBox<String> athleteDrop;

    public void initialize() {
        loadIcons();

        List<Registration> registrations = null;
        try {
            registrations = getPendingRegistrations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (registrations.isEmpty()) {
            showNoRequestsMessage();
        } else {
            displayRequests(registrations);
        }
        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        xmlSplitButton.setOnMouseClicked(mouseEvent -> xmlSplitButton.show());
    }

    public void loadIcons(){
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if(iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if(iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);

    }
    private List<Registration> getPendingRegistrations() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<Registration> registrations = registrationDao.getRegistrations();
        return registrations.stream()
                .filter(reg -> reg.getStatus().getIdStatus() == 1)
                .collect(Collectors.toList());
    }

    private void showNoRequestsMessage() {
        noRequestsLabel.setVisible(true);
    }

    private void displayRequests(List<Registration> registrations) {
        noRequestsLabel.setVisible(false);
        mainContainer.setVisible(true);
        mainContainer.getChildren().clear();

        for (Registration registration : registrations) {
            VBox requestItem = createRequestItem(registration);
            mainContainer.getChildren().add(requestItem);
        }
    }

    private VBox createRequestItem(Registration request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Label nameLabel = new Label(request.getAthlete().getName());
        nameLabel.getStyleClass().add("name-label");

        java.sql.Date birthDate = request.getAthlete().getDateOfBirth();
        LocalDate birthLocalDate = birthDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthLocalDate, currentDate).getYears();

        Label ageLabel = new Label("Idade: " + age);
        ageLabel.getStyleClass().add("age-label");

        Label sportLabel = new Label("Modalidade: " + request.getSport().getName());
        sportLabel.getStyleClass().add("sport-label");

        Label teamLabel = new Label("Equipa: " + request.getTeam().getName());
        teamLabel.getStyleClass().add("team-label");

        ImageView acceptImageView = new ImageView();
        URL iconAcceptURL = Main.class.getResource("img/iconAccept.png");
        if (iconAcceptURL != null) {
            String iconAcceptStr = iconAcceptURL.toExternalForm();
            Image image = new Image(iconAcceptStr);
            acceptImageView.setImage(image);
            acceptImageView.setFitWidth(80);
            acceptImageView.setFitHeight(80);
        }
        Button acceptButton = new Button();
        acceptButton.setGraphic(acceptImageView);
        acceptButton.getStyleClass().add("acceptButton");

        ImageView rejectImageView = new ImageView();
        URL iconRejectURL = Main.class.getResource("img/iconReject.png");
        if (iconRejectURL != null) {
            String iconRejectStr = iconRejectURL.toExternalForm();
            Image image = new Image(iconRejectStr);
            rejectImageView.setImage(image);
            rejectImageView.setFitWidth(80);
            rejectImageView.setFitHeight(80);
        }
        Button rejectButton = new Button();
        rejectButton.setGraphic(rejectImageView);
        rejectButton.getStyleClass().add("rejectButton");

        acceptButton.setOnAction(event -> {
            try {
                RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 3);
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        rejectButton.setOnAction(event -> {
            try {
                RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 2);
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(acceptButton, rejectButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10));

        requestItem.getChildren().addAll(nameLabel, sportLabel, ageLabel, buttonContainer);
        return requestItem;
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
    public static void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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

    @FXML
    public void loadTeams(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        FileChooser.ExtensionFilter xsdFilter = new FileChooser.ExtensionFilter("XSD Files (*.xsd)", "*.xsd");
        fileChooser.getExtensionFilters().addAll(xmlFilter, xsdFilter);

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
                    Teams teams = xmlUtils.getTeamsDataXML(xmlPath);
                    try {
                        UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                        boolean uploaded = uploadXmlDAO.addTeams(teams);
                        if (uploaded) {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Sucesso!");
                            alerta.setHeaderText("O upload foi efetuado!");
                            alerta.show();
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("Erro!");
                            alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                            alerta.show();
                        }
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

    private void showFilePreviewPopup(File file) {
        Stage popupStage = new Stage();
        popupStage.setTitle("File Preview");

        TextArea filePreviewTextArea = new TextArea();
        filePreviewTextArea.setEditable(false);

        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            filePreviewTextArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        VBox vbox = new VBox(filePreviewTextArea);
        Scene scene = new Scene(vbox, 400, 300);
        popupStage.setScene(scene);
        popupStage.show();
    }
    @FXML
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
                    Sports sports = xmlUtils.getSportsDataXML(xmlPath);
                    try {
                        UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                        boolean uploaded = uploadXmlDAO.addSports(sports);
                        if (uploaded) {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Sucesso!");
                            alerta.setHeaderText("O upload foi efetuado!");
                            alerta.show();
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("Erro!");
                            alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                            alerta.show();
                        }
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
    @FXML
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
                    Athletes athletes = xmlUtils.getAthletesDataXML(xmlPath);
                    try {
                        UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
                        boolean uploaded = uploadXmlDAO.addAthletes(athletes);
                        if (uploaded) {
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setTitle("Sucesso!");
                            alerta.setHeaderText("O upload foi efetuado!");
                            alerta.show();
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("Erro!");
                            alerta.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                            alerta.show();
                        }
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
}