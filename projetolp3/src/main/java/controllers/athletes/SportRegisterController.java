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
    @FXML
    private ComboBox sportsDrop;
    @FXML
    private ComboBox<String> eventsDrop;


    public void initialize() {
        loadSports();
        loadEvents();
    }

    private void loadSports() {
        try {
            sportsDrop.getItems().clear();
            SportDao sportDao = new SportDao();
            List<List> sports = sportDao.getSportsToShow();
            ObservableList<String> sportsOptions = FXCollections.observableArrayList();

            if (LoginController.gender.equals("Female")) {
                sports.removeIf(sport -> sport.get(2).toString().equals("Male"));
            } else {
                sports.removeIf(sport -> sport.get(2).toString().equals("Female"));
            }

            for (List sport : sports) {
                    sportsOptions.add(sport.get(3).toString());
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

    @FXML
    private void registerSport(ActionEvent event) {
        int idStatus = 0;
        try {

            Sport selectedSport = getSelectedSport();
            if (selectedSport == null) {
                System.out.println("Nenhuma modalidade selecionada.");
                return;
            }



            int athleteId = LoginController.idAthlete;
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(athleteId);

            if (athlete == null) {
                System.out.println("Atleta não encontrado.");
                return;
            }
            if(selectedSport.getType().equals("Individual")) {
                idStatus = 3;
            }else{
                idStatus = 1;
            }

            // Obter o status com ID 1
            RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();

            RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(idStatus); // Status com id 1

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
}
