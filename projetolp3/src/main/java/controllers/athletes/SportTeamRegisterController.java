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
import javafx.scene.control.ListCell;
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

public class SportTeamRegisterController {
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
    private ComboBox<String> eventsDrop;
    @FXML
    private ComboBox<String> teamsDrop;

    public void initialize() {
        loadIcons();
        loadEvents();
        loadTeams();
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


    private void loadTeams() {
        try {
            teamsDrop.getItems().clear(); // Limpa as opções da ComboBox
            List<Team> teams = TeamDao.getTeams(); // Obtém todas as equipes do banco de dados
            ObservableList<String> teamOptions = FXCollections.observableArrayList();

            // Obtém o ID do país do atleta logado
            String athleteCountryId = LoginController.idCountry;

            // Filtra as equipes para mostrar apenas aquelas que pertencem ao mesmo país do atleta
            for (Team team : teams) {
                String teamCountryId = team.getCountry().getIdCountry(); // Obtém o ID do país da equipe

                // Verifica se o país da equipe é igual ao país do atleta
                if (teamCountryId.equals(athleteCountryId)) {
                    String displayName = team.getIdTeam() + " - " + team.getName() + " - " + team.getCountry().getName();
                    teamOptions.add(displayName); // Adiciona a equipe à lista de opções
                }
            }

            // Configura as opções filtradas na ComboBox
            teamsDrop.setItems(teamOptions);

            // Verifica se não há equipes para o país do atleta
            if (teamOptions.isEmpty()) {
                System.out.println("Nenhuma equipe disponível para o país do atleta.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar equipes.");
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
    private void registerSport(ActionEvent actionEvent) {
        try {
            // Obter o atleta logado usando o ID armazenado na sessão
            int athleteId = LoginController.idAthlete; // Acessando o athleteId da sessão
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(athleteId);

            if (athlete == null) {
                System.out.println("Atleta não encontrado.");
                return;
            }

            // Obter a equipe selecionada
            String selectedTeamName = teamsDrop.getSelectionModel().getSelectedItem();
            Team team = getSelectedTeamById(selectedTeamName); // Função para obter a equipe a partir do ID

            if (team == null) {
                System.out.println("Nenhuma equipe selecionada.");
                return;
            }

            // Obter o evento selecionado
            String selectedEventName = eventsDrop.getSelectionModel().getSelectedItem();
            Event selectedEvent = getSelectedEventByYear(selectedEventName); // Função para obter o evento a partir do ano

            if (selectedEvent == null) {
                System.out.println("Nenhum evento selecionado.");
                return;
            }

            // Obter o status com ID 1
            RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();
            RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(3); // aprovado automaticamente

            if (status == null) {
                System.out.println("Status não encontrado.");
                return;
            }

            // Criar a inscrição com o ano do evento e o ID da equipe selecionada
            Registration registration = new Registration(
                    0,
                    athlete,
                    team, // Passando a equipe selecionada
                    status,
                    selectedEvent.getYear() // Passando o ano do evento selecionado
            );

            // Registrar a inscrição
            RegistrationDao.addRegistrationTeam(registration);

            // Após registrar a inscrição, adicionar o atleta à equipe na tblTeamList
            TeamListDao teamlistDao = new TeamListDao();
            int statusId = 1;  // Status pendente
            int year = selectedEvent.getYear();  // Ano do evento
            teamlistDao.insertIntoTeamList(athlete.getIdAthlete(), team.getIdTeam(), statusId, year);

            System.out.println("Inscrição realizada com sucesso e atleta adicionado à equipe!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao realizar a inscrição.");
        }
    }



    private Team getSelectedTeamById(String teamName) {
        // Lógica para encontrar a equipe com base no ID
        try {
            TeamDao teamDao = new TeamDao();
            List<Team> teams = teamDao.getTeams();
            for (Team team : teams) {
                String teamDisplayName = team.getIdTeam() + " - " + team.getName() + " - " + team.getCountry().getName();
                if (teamDisplayName.equals(teamName)) {
                    return team; // Retorna a equipe com base no nome
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Event getSelectedEventByYear(String eventDisplayName) {
        // Lógica para encontrar o evento com base no ano
        try {
            EventDao eventDao = new EventDao();
            List<Event> events = eventDao.getEvents();
            for (Event event : events) {
                String eventDisplay = event.getYear() + " - " + event.getCountry().getName(); // Usando o ano
                if (eventDisplay.equals(eventDisplayName)) {
                    return event; // Retorna o evento com base no ano
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

