package controllers.athletes;

import Dao.*;
import Models.*;
import Models.Athlete;
import bytesnortenhos.projetolp3.Main;
import controllers.LoginController;
import controllers.ViewsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class SportRegisterController {
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
    private SplitMenuButton theamsSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private ComboBox sportsDrop;
    @FXML
    private ComboBox<String> eventsDrop;


    public void initialize() {
        loadIcons();
        loadSports();
        loadEvents();
        theamsSplitButton.setOnMouseClicked(event -> theamsSplitButton.show());
        sportSplitButton.setOnMouseClicked(mouseEvent -> sportSplitButton.show());
    }

    private void loadIcons() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        Image image = new Image(iconMoonNavURL.toExternalForm());
        if (iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        image = new Image(iconHomeNavURL.toExternalForm());
        if (iconHomeNav != null) iconHomeNav.setImage(image);

        URL iconLogoutNavURL = Main.class.getResource("img/iconLogoutDark.png");
        image = new Image(iconLogoutNavURL.toExternalForm());
        if(iconLogoutNav != null) iconLogoutNav.setImage(image);
    }
    private void loadSports() {
        try {
            sportsDrop.getItems().clear();
            SportDao sportDao = new SportDao();
            List<List> sports = sportDao.getSportsToShow();
            ObservableList<String> sportsOptions = FXCollections.observableArrayList();

            // Filtrar esportes pelo gênero do atleta e tipo "individual"
            if (LoginController.gender.equals("Female")) {
                sports.removeIf(sport -> sport.get(2).toString().equals("Male"));
            } else {
                sports.removeIf(sport -> sport.get(2).toString().equals("Female"));
            }

            // Adicionar apenas esportes individuais
            for (List sport : sports) {
                String sportType = sport.get(1).toString(); // Supondo que o índice 1 seja o tipo
                if (sportType.equalsIgnoreCase("individual")) {
                    sportsOptions.add(sport.get(3).toString()); // Supondo que o índice 3 seja o nome
                }
            }

            sportsDrop.setItems(sportsOptions);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar esportes.");
        }
    }


    private void loadEvents() {
        try {
            eventsDrop.getItems().clear();
            EventDao eventDao = new EventDao();
            List<Event> events = eventDao.getEvents();
            ObservableList<String> eventsOptions = FXCollections.observableArrayList();

            // Obter o ano atual
            int currentYear = LocalDate.now().getYear();

            // Filtrar eventos com ano atual ou posterior
            for (Event event : events) {
                int eventYear = event.getYear();  // Usando o método getYear() da classe Event

                // Verificar se o evento é no ano atual ou em anos posteriores
                if (eventYear >= currentYear) {
                    String eventDisplay = event.getYear() + " - " + event.getCountry().getName();
                    eventsOptions.add(eventDisplay);
                }
            }

            // Adicionar os eventos filtrados à ComboBox
            eventsDrop.setItems(eventsOptions);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar eventos.");
        }
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
    @FXML
    private void registerSport(ActionEvent event) {
        try {
            // Exibir passo de depuração
            System.out.println("Iniciando registro...");

            // Obter o esporte selecionado
            Sport selectedSport = getSelectedSport();
            System.out.println("Passo 1");
            if (selectedSport == null) {
                System.out.println("Nenhuma modalidade selecionada.");
                return;
            }

            // Exibir o ID da modalidade
            System.out.println("ID da Modalidade (Sport): " + selectedSport.getIdSport());
            System.out.println("Passo 2");

            // Obter o atleta logado usando o ID armazenado na sessão
            int athleteId = LoginController.idAthlete;
            System.out.println("ID do Atleta: " + athleteId);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(athleteId);
            System.out.println("Passo 3");

            if (athlete == null) {
                System.out.println("Atleta não encontrado.");
                return;
            }

            // Obter o status com ID 1
            RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();
            RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(1); // Status com id 1
            System.out.println("Passo 4");

            if (status == null) {
                System.out.println("Status não encontrado.");
                return;
            }

            // Obter o evento selecionado na ComboBox
            Event selectedEvent = getSelectedEvent(); // Método que retorna o objeto evento selecionado
            if (selectedEvent == null) {
                System.out.println("Nenhum evento selecionado.");
                return;
            }

            // Obter o ano do evento usando o método getYear()
            int year = selectedEvent.getYear();
            System.out.println("Ano do evento selecionado: " + year);

            // Criar o objeto Registration com o ano
            Registration registration = new Registration(0, athlete, selectedSport, status, year);

            // Registrar a inscrição
            RegistrationDao.addRegistrationSolo(registration);

            // Mensagem de sucesso
            System.out.println("Inscrição realizada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao realizar a inscrição.");
        } catch (NumberFormatException e) {
            System.out.println("Erro ao processar o ano do evento.");
        }
    }

    private Event getSelectedEvent() {
        String selectedEventDisplay = eventsDrop.getSelectionModel().getSelectedItem();
        if (selectedEventDisplay != null) {
            try {
                EventDao eventDao = new EventDao();
                List<Event> events = eventDao.getEvents(); // Recupera todos os eventos do DAO

                // Busca o evento correspondente ao item selecionado na ComboBox
                for (Event event : events) {
                    String eventDisplay = event.getYear() + " - " + event.getCountry().getName();
                    if (eventDisplay.equals(selectedEventDisplay)) {
                        return event;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erro ao buscar eventos no banco de dados.");
            }
        }
        return null; // Retorna null se nenhum evento for encontrado
    }


    private Sport getSelectedSport() {
        String selectedSportName = sportsDrop.getSelectionModel().getSelectedItem().toString();
        if (selectedSportName != null) {
            try {
                SportDao sportDao = new SportDao();
                // Pass the selectedSportName as the argument to getSportsByName
                for (Sport sport : sportDao.getSportsByName(selectedSportName)) {
                    if (sport.getName().equals(selectedSportName)) {
                        return sport;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    public void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/home.fxml")));
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
    public void mostrarRegistaModalidadesEquipa(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/sportsTeamRegister.fxml")));
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
    public void mostrarRegistaEquipas(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/teamsRegister.fxml")));
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
}
