package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
import Models.*;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeController {
    public SplitMenuButton testeSplitButton;
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
    private SplitMenuButton teamSplitButton;
    @FXML
    private SplitMenuButton eventSplitButton;
    @FXML
    private SplitMenuButton xmlSplitButton;

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
        teamSplitButton.setOnMouseClicked(mouseEvent -> teamSplitButton.show());
        eventSplitButton.setOnMouseClicked(event -> eventSplitButton.show());
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
        return registrations;
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

        Label countryLabel = new Label("País: " + request.getAthlete().getCountry().getName());
        countryLabel.getStyleClass().add("country-label");

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
                verifyTeam(request);
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        rejectButton.setOnAction(event -> {
            try {
                RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 2, 0);
                Platform.runLater(() -> mainContainer.getChildren().remove(requestItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(acceptButton, rejectButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10));

        requestItem.getChildren().addAll(nameLabel, ageLabel, sportLabel, countryLabel, buttonContainer);
        return requestItem;
    }
    public void verifyTeam(Registration request) throws SQLException{
        RegistrationDao registrationDao = new RegistrationDao();
        if(!registrationDao.verfiyTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport())){
            popWindow(request);
        }
        else{
            RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 3, registrationDao.getIdTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport()));
        }
    }
    private void popWindow(Registration request) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Criar equipa");


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");


        Label minParticipantsLabel = new Label("Min Participants:");
        minParticipantsLabel.getStyleClass().add("popup-label");
        TextField minParticipantsField = new TextField();
        minParticipantsField.getStyleClass().add("popup-text");

        Label maxParticipantsLabel = new Label("Max Participants:");
        maxParticipantsLabel.getStyleClass().add("popup-label");
        TextField maxParticipantsField = new TextField();
        maxParticipantsField.getStyleClass().add("popup-text");

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("popup-button");
        submitButton.setOnAction(event -> {
            try {
                String minParticipantsText = minParticipantsField.getText();
                String maxParticipantsText = maxParticipantsField.getText();

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
                teamCreate(request, minParticipants, maxParticipants);
                popupStage.close();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        });

        vbox.getChildren().addAll( minParticipantsLabel, minParticipantsField, maxParticipantsLabel, maxParticipantsField, submitButton);

        Scene scene = new Scene(vbox, 300, 250);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    public void teamCreate(Registration request, int minParticipants, int maxParticipants) throws SQLException {
        TeamDao teamDao = new TeamDao();
        String g = "";
        if(request.getAthlete().getGenre().getIdGender()==1){
            g = "Men's";
        }else{
            g = "Women's";
        }
        String name = request.getAthlete().getCountry().getName() + " " + g + " " + request.getSport().getName() + " Team";
        teamDao.addTeam(new Team(0, name, request.getAthlete().getCountry(), request.getAthlete().getGenre(), request.getSport(), 2024, minParticipants, maxParticipants));
        RegistrationDao registrationDao = new RegistrationDao();
        RegistrationDao.updateRegistrationStatus(request.getIdRegistration(), 3, registrationDao.getIdTeam(request.getAthlete().getCountry().getIdCountry(), request.getSport().getIdSport()));
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso!");
        alerta.setHeaderText("Equipa criada com sucesso!");
        alerta.show();
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

    public void showSports(ActionEvent event) throws IOException {
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
    public void showSportsRegister(ActionEvent event) throws IOException {
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

    @FXML
    public void loadTeams(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");


        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);


        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            // Valida se os arquivos têm os nomes esperados
            boolean hasXmlFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("teams.xml"));
            boolean hasXsdFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("teams_xsd.xml"));

            if (hasXmlFile && hasXsdFile) {
                String xsdPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("teams_xsd.xml"))
                        .findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("teams.xml"))
                        .findFirst().get().getAbsolutePath();


                showPreviewBeforeInsertion(xmlPath, xsdPath);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Arquivos incorretos selecionados!");
                alert.setContentText("Certifique-se de selecionar os arquivos:\n- teams.xml\n- teams_xsd.xml");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nenhum Arquivo Selecionado");
            alert.setHeaderText("Por favor, selecione os arquivos corretos.");
            alert.setContentText("Você precisa selecionar um arquivo teams.xml e um arquivo teams_xsd.xml.");
            alert.show();
        }
    }


    // Método para exibir uma prévia antes de inserir na base de dados
    private void showPreviewBeforeInsertion(String xmlPath, String xsdPath) {
        XMLUtils xmlUtils = new XMLUtils();
        boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

        if (xmlValid) {
            Teams teams = xmlUtils.getTeamsDataXML(xmlPath);


            String teamsPreview = teams.toString();


            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Confirmação de Inserção");
            dialog.setHeaderText("Os seguintes dados serão inseridos na base de dados:");
            dialog.setContentText("Verifique os detalhes antes de confirmar.");


            TextArea textArea = new TextArea(teamsPreview);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setPrefSize(600, 400);

            dialog.getDialogPane().setContent(textArea);


            ButtonType insertButton = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getButtonTypes().setAll(insertButton, cancelButton);


            dialog.showAndWait().ifPresent(response -> {
                if (response == insertButton) {
                    insertIntoDatabaseTeams(teams);
                } else {
                    Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                    cancelAlert.setTitle("Operação Cancelada");
                    cancelAlert.setHeaderText("Nenhuma alteração foi feita.");
                    cancelAlert.show();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Baseado no XSD, o XML está incorreto!");
            alert.show();
        }
    }


    private void insertIntoDatabaseTeams(Teams teams) {
        try {
            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
            boolean uploaded = uploadXmlDAO.addTeams(teams);
            if (uploaded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Os dados foram inseridos na base de dados com sucesso!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
            alert.show();
        }
    }
    private void showPreviewBeforeInsertionSports(String xmlPath, String xsdPath) {
        XMLUtils xmlUtils = new XMLUtils();
        boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

        if (xmlValid) {
            Sports sports = xmlUtils.getSportsDataXML(xmlPath); // Processa o XML para obter os dados

            // Converte os dados para uma string compreensível
            String sportsPreview = sports.toString(); // Certifique-se de que Sports tem um método toString() adequado

            // Exibe o dialog com os dados a serem inseridos
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Confirmação de Inserção");
            dialog.setHeaderText("Os seguintes dados serão inseridos na base de dados:");
            dialog.setContentText("Verifique os detalhes antes de confirmar.");

            // Usa um TextArea para exibir os detalhes do objeto Sports
            TextArea textArea = new TextArea(sportsPreview);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setPrefSize(600, 400); // Tamanho da área de texto

            dialog.getDialogPane().setContent(textArea);

            // Botões do dialog
            ButtonType insertButton = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getButtonTypes().setAll(insertButton, cancelButton);

            // Processa a escolha do usuário
            dialog.showAndWait().ifPresent(response -> {
                if (response == insertButton) {
                    // Realiza a inserção na base de dados
                    insertIntoDatabaseSports(sports);
                } else {
                    // Apenas fecha o dialog
                    Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                    cancelAlert.setTitle("Operação Cancelada");
                    cancelAlert.setHeaderText("Nenhuma alteração foi feita.");
                    cancelAlert.show();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Baseado no XSD, o XML está incorreto!");
            alert.show();
        }
    }

    // Método para realizar a inserção na base de dados
    private void insertIntoDatabaseSports(Sports sports) {
        try {
            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
            boolean uploaded = uploadXmlDAO.addSports(sports);
            if (uploaded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Os dados foram inseridos na base de dados com sucesso!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
            alert.show();
        }
    }

    @FXML
    public void loadSports(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        // Permite selecionar apenas arquivos XML
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);

        // Obtém a janela do Stage a partir do evento
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            // Verifica se os arquivos têm os nomes esperados
            boolean hasXmlFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("sports.xml"));
            boolean hasXsdFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("sports_xsd.xml"));

            if (hasXmlFile && hasXsdFile) {
                String xsdPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("sports_xsd.xml"))
                        .findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("sports.xml"))
                        .findFirst().get().getAbsolutePath();

                // Valida o XML contra o XSD
                XMLUtils xmlUtils = new XMLUtils();
                boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

                if (xmlValid) {
                    // Obtém os dados do arquivo XML e exibe a pré-visualização
                    Sports sports = xmlUtils.getSportsDataXML(xmlPath);
                    showPreviewBeforeInsertionSports(sports, xmlPath, xsdPath);
                } else {
                    // Alerta caso a validação falhe
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro!");
                    alert.setHeaderText("O XML não é válido com base no arquivo XSD fornecido.");
                    alert.setContentText("Verifique o conteúdo do arquivo sports.xml e sports_xsd.xml.");
                    alert.show();
                }
            } else {
                // Alerta caso os arquivos selecionados não sejam os esperados
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Arquivos incorretos selecionados!");
                alert.setContentText("Certifique-se de selecionar:\n- sports.xml\n- sports_xsd.xml");
                alert.show();
            }
        } else {
            // Alerta caso o usuário não selecione nenhum arquivo
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nenhum Arquivo Selecionado");
            alert.setHeaderText("Por favor, selecione os arquivos corretos.");
            alert.setContentText("Você precisa selecionar um arquivo sports.xml e um arquivo sports_xsd.xml.");
            alert.show();
        }
    }

    // Método para exibir a pré-visualização e confirmar a inserção na base de dados
    private void showPreviewBeforeInsertionSports(Sports sports, String xmlPath, String xsdPath) {
        // Converte os dados para uma string compreensível
        String sportsPreview = sports.toString();

        // Exibe o dialog com os dados a serem inseridos
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Confirmação de Inserção");
        dialog.setHeaderText("Os seguintes dados serão inseridos na base de dados:");
        dialog.setContentText("Verifique os detalhes antes de confirmar.");

        // Usa um TextArea para exibir os detalhes do objeto Sports
        TextArea textArea = new TextArea(sportsPreview);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setPrefSize(600, 400); // Tamanho da área de texto

        dialog.getDialogPane().setContent(textArea);

        // Botões do dialog
        ButtonType insertButton = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(insertButton, cancelButton);

        // Processa a escolha do usuário
        dialog.showAndWait().ifPresent(response -> {
            if (response == insertButton) {
                // Realiza a inserção na base de dados
                insertIntoDatabaseSports(sports);
            } else {
                // Apenas fecha o dialog
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Operação Cancelada");
                cancelAlert.setHeaderText("Nenhuma alteração foi feita.");
                cancelAlert.show();
            }
        });
    }


    @FXML
    public void loadAthletes(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");

        // Permite selecionar apenas arquivos XML
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);

        // Obtém a janela do Stage a partir do evento
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            // Verifica se os arquivos têm os nomes esperados
            boolean hasXmlFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("athletes.xml"));
            boolean hasXsdFile = selectedFiles.stream()
                    .anyMatch(file -> file.getName().equalsIgnoreCase("athletes_xsd.xml"));

            if (hasXmlFile && hasXsdFile) {
                String xsdPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("athletes_xsd.xml"))
                        .findFirst().get().getAbsolutePath();
                String xmlPath = selectedFiles.stream()
                        .filter(file -> file.getName().equalsIgnoreCase("athletes.xml"))
                        .findFirst().get().getAbsolutePath();

                // Valida o XML contra o XSD
                XMLUtils xmlUtils = new XMLUtils();
                boolean xmlValid = xmlUtils.validateXML(xsdPath, xmlPath);

                if (xmlValid) {
                    // Obtém os dados do arquivo XML e exibe a pré-visualização
                    Athletes athletes = xmlUtils.getAthletesDataXML(xmlPath);
                    showPreviewBeforeInsertionAthletes(athletes, xmlPath, xsdPath);
                } else {
                    // Alerta caso a validação falhe
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro!");
                    alert.setHeaderText("O XML não é válido com base no arquivo XSD fornecido.");
                    alert.setContentText("Verifique o conteúdo do arquivo athletes.xml e athletes_xsd.xml.");
                    alert.show();
                }
            } else {
                // Alerta caso os arquivos selecionados não sejam os esperados
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Arquivos incorretos selecionados!");
                alert.setContentText("Certifique-se de selecionar:\n- athletes.xml\n- athletes_xsd.xml");
                alert.show();
            }
        } else {
            // Alerta caso o usuário não selecione nenhum arquivo
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nenhum Arquivo Selecionado");
            alert.setHeaderText("Por favor, selecione os arquivos corretos.");
            alert.setContentText("Você precisa selecionar um arquivo athletes.xml e um arquivo athletes_xsd.xml.");
            alert.show();
        }
    }

    // Método para exibir a pré-visualização e confirmar a inserção na base de dados
    private void showPreviewBeforeInsertionAthletes(Athletes athletes, String xmlPath, String xsdPath) {
        // Converte os dados para uma string compreensível
        String athletesPreview = athletes.toString(); // Certifique-se de que Athletes tem um método toString() adequado

        // Exibe o dialog com os dados a serem inseridos
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Confirmação de Inserção");
        dialog.setHeaderText("Os seguintes dados serão inseridos na base de dados:");
        dialog.setContentText("Verifique os detalhes antes de confirmar.");

        // Usa um TextArea para exibir os detalhes do objeto Athletes
        TextArea textArea = new TextArea(athletesPreview);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setPrefSize(600, 400); // Tamanho da área de texto

        dialog.getDialogPane().setContent(textArea);


        ButtonType insertButton = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(insertButton, cancelButton);


        dialog.showAndWait().ifPresent(response -> {
            if (response == insertButton) {
                // Realiza a inserção na base de dados
                insertIntoDatabaseAthletes(athletes);
            } else {
                // Apenas fecha o dialog
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Operação Cancelada");
                cancelAlert.setHeaderText("Nenhuma alteração foi feita.");
                cancelAlert.show();
            }
        });
    }

    // Método para realizar a inserção na base de dados
    private void insertIntoDatabaseAthletes(Athletes athletes) {
        try {
            UploadXmlDAO uploadXmlDAO = new UploadXmlDAO();
            boolean uploaded = uploadXmlDAO.addAthletes(athletes);
            if (uploaded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Os dados foram inseridos na base de dados com sucesso!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("Ocorreu um erro ao adicionar os dados à Base de Dados!");
            alert.show();
        }
    }

    public void updateImageAthlete(ActionEvent event) throws SQLException {
        String pathSave = "src/main/java/ImagesAthlete/";
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

                int tempAthleteId = 1000;
                boolean saved = saveAthleteImage(tempAthleteId, pathSave, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();

                    AthleteDao athleteDao = new AthleteDao();
                    athleteDao.updateAthleteImage(tempAthleteId, pathSave, "." + selectedFile.getName().split("\\.")[1]);
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
}