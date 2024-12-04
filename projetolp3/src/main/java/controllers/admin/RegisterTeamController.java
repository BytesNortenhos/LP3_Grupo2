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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterTeamController {
    private static Scene scene;
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
    @FXML
    private ImageView iconSports;
    @FXML
    private ImageView iconLogoutNav;


    private static boolean isDarkMode = true;


    @FXML
    private ComboBox<Country> countryDrop;

    @FXML
    private ComboBox<Gender> genderDrop;
    @FXML
    private ComboBox<Sport> sportDrop;
    @FXML
    private TextField teamNameText, yearFoundedText, minParticipantsText, maxParticipantsText;

    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private SplitMenuButton teamSplitButton;

    public void initialize() {
        loadIcons();
        loadGenders();
        loadCountries();
        loadSports();
        athleteSplitButton.setOnMouseClicked(event -> athleteSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
        teamSplitButton.setOnMouseClicked(event -> teamSplitButton.show());

    }

    private void loadIcons() {

        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconOlympicURL = Main.class.getResource("img/iconSports.png");
        image = new Image(iconOlympicURL.toExternalForm());
        if(iconSports != null) iconSports.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);
    }

    private void loadGenders() {
        try {
            List<Gender> genders = GenderDao.getGenders();

            genderDrop.setItems(FXCollections.observableArrayList(genders));

            genderDrop.setCellFactory(param -> new ListCell<Gender>() {
                @Override
                protected void updateItem(Gender gender, boolean empty) {
                    super.updateItem(gender, empty);
                    if (empty || gender == null) {
                        setText(null);
                    } else {
                        setText(gender.getDesc());
                    }
                }
            });

            genderDrop.setButtonCell(genderDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCountries() {
        try {
            List<Country> countries = CountryDao.getCountries();

            countryDrop.setItems(FXCollections.observableArrayList(countries));

            countryDrop.setCellFactory(param -> new ListCell<Country>() {
                @Override
                protected void updateItem(Country country, boolean empty) {
                    super.updateItem(country, empty);
                    if (empty || country == null) {
                        setText(null);
                    } else {
                        setText(country.getName());
                    }
                }
            });

            countryDrop.setButtonCell(countryDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSports() {
        try {
            SportDao sportdao = new SportDao();
            List<Sport> sports = sportdao.getAllSportsV2();

            sportDrop.setItems(FXCollections.observableArrayList(sports));


            sportDrop.setCellFactory(param -> new ListCell<Sport>() {
                @Override
                protected void updateItem(Sport sport, boolean empty) {
                    super.updateItem(sport, empty);
                    if (empty || sport == null) {
                        setText(null);
                    } else {
                        setText(sport.getName());
                    }
                }
            });

            sportDrop.setButtonCell(sportDrop.getCellFactory().call(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void registerTeam() {
        try {
            String name = teamNameText.getText();
            Country selectedCountry = countryDrop.getValue();
            Gender selectedGender = genderDrop.getValue();
            Sport selectedSport = sportDrop.getValue();
            int yearFounded = Integer.parseInt(yearFoundedText.getText());
            int minParticipants = Integer.parseInt(minParticipantsText.getText());
            int maxParticipants = Integer.parseInt(maxParticipantsText.getText());

            if (name.isEmpty() || selectedCountry == null || selectedGender == null || selectedSport == null) {
                showAlert("Validation Error", "Please fill all required fields!", Alert.AlertType.ERROR);
                return;
            }

            Team newTeam = new Team(
                    0,
                    name,
                    selectedCountry,
                    selectedGender,
                    selectedSport,
                    yearFounded,
                    minParticipants,
                    maxParticipants
            );

            TeamDao teamdao = new TeamDao();
            teamdao.addTeam(newTeam);

            showAlert("Success", "Team registered successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid number format!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while saving the team.", Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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


    public void showTeamsEdit(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/teamRegister.fxml")));
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
