package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Utils.ErrorHandler;
import Dao.EventDao;
import Models.Registration;
import Utils.XMLUtils;
import bytesnortenhos.projetolp3.Main;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ViewsController {
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
    @FXML
    private SplitMenuButton locationSplitButton;



    public void initialize() {
        loadIcons();
        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        teamSplitButton.setOnMouseClicked(mouseEvent -> teamSplitButton.show());
        eventSplitButton.setOnMouseClicked(event -> eventSplitButton.show());
        xmlSplitButton.setOnMouseClicked(mouseEvent -> xmlSplitButton.show());
        locationSplitButton.setOnMouseClicked(mouseEvent -> locationSplitButton.show());
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/loginView.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/register.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/athletesView.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportsView.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/startSport.fxml")));
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
    public void showAddLocation(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/addLocation.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportRegister.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/addEvent.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/eventsView.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/sportEdit.fxml")));
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
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamEdit.fxml")));
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
    public void showTeamsView(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamsView.fxml")));
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
    public void showViewLocations(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/locationsView.fxml")));
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

    public void showUpdateLocations(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(controllers.ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/locationEdit.fxml")));
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
                            ErrorHandler uploaded = uploadXmlDAO.addTeams(teams);
                            if (uploaded.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Adicionar à Base de Dados:");
                                alerta.setHeaderText(uploaded.getMessage());
                                alerta.show();
                            }

                            ErrorHandler xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Guardar Ficheiro XML/XSD:");
                                alerta.setHeaderText(xmlSaved.getMessage());
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
                            ErrorHandler uploaded = uploadXmlDAO.addSports(sports);
                            if (uploaded.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Adicionar à Base de Dados:");
                                alerta.setHeaderText(uploaded.getMessage());
                                alerta.show();
                            }

                            ErrorHandler xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Guardar Ficheiro XML/XSD:");
                                alerta.setHeaderText(xmlSaved.getMessage());
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
                        alerta.setTitle("Erro relacionado ao XML!");
                        alerta.setHeaderText("Os dados do XML não coincidem com a opção escolhida!");

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
                            ErrorHandler uploaded = uploadXmlDAO.addAthletes(athletes);
                            if (uploaded.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                                alerta.setTitle("Sucesso!");
                                alerta.setHeaderText("Upload (apenas dos dados não repetidos) foi efetuado! Verifique pois se todos os dados forem repetidos, nada foi inserido!");
                                alerta.show();
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Adicionar à Base de Dados:");
                                alerta.setHeaderText(uploaded.getMessage());
                                alerta.show();
                            }

                            ErrorHandler xmlSaved = uploadXmlDAO.saveXML(xmlPath, xsdPath);
                            if (!xmlSaved.isSuccessful()) {
                                Alert alerta = new Alert(Alert.AlertType.ERROR);
                                alerta.setTitle("Erro ao Guardar Ficheiro XML/XSD:");
                                alerta.setHeaderText(xmlSaved.getMessage());
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
