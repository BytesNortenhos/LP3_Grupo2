package controllers.athletes;

import Dao.RegistrationDao;
import bytesnortenhos.projetolp3.App;
import controllers.LoginController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarController {
    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthLabel;

    private YearMonth currentYearMonth;
    private final Map<LocalDate, EventDetails> events = new HashMap<>();

    public void initialize() throws SQLException, ParseException {
        currentYearMonth = YearMonth.now();
        loadDaysEvents();
        eventsCalendar(currentYearMonth);
    }
    public class EventDetails {
        private List<Integer> idSport;
        private String day;


        public EventDetails(List<Integer> idSport, String description) {
            this.idSport = idSport;
            this.day = description;
        }

        public List<Integer> getIdSport() {
            return idSport;
        }
        public String getDay() {
            return day;
        }

    }

    private void eventsCalendar(YearMonth yearMonth) {
        calendarGrid.getChildren().clear();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "PT"));
        monthLabel.setText(yearMonth.format(monthFormatter));
        monthLabel.setStyle("-fx-text-fill: #F7EED8; -fx-font-size: 32px;");


        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int daysInMonth = yearMonth.lengthOfMonth();

        int row = 0;
        int column = dayOfWeek % 7;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            StackPane dayPane = new StackPane();
            dayPane.setStyle("-fx-border-color: #F7EED8; -fx-border-width: 1; -fx-padding: 20;");

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-text-fill: #F7EED8; -fx-font-size: 28px;");
            dayPane.getChildren().add(dayLabel);

            if (events.containsKey(date)) {
                EventDetails EventDetails = events.get(date);
                Button eventButton = new Button(EventDetails.getDay());
                eventButton.setStyle("-fx-font-size: 20; -fx-background-color: lightblue;");
                eventButton.setOnAction(e -> {
                    try {
                        dayEventsPopUp(EventDetails.getIdSport());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                dayPane.getChildren().add(eventButton);
            }

            calendarGrid.add(dayPane, column, row);
            column++;
            if (column > 6) {
                column = 0;
                row++;
            }
        }
    }

    @FXML
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        eventsCalendar(currentYearMonth);
    }

    @FXML
    private void nextMonth() { 
        currentYearMonth = currentYearMonth.plusMonths(1);
        eventsCalendar(currentYearMonth);
    }

    private void loadDaysEvents() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<List> calendars = registrationDao.getCalendarAthlete(LoginController.idAthlete);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (List event : calendars) {
                String tempStartDate = String.valueOf(event.get(2));
                String tempEndDate = String.valueOf(event.get(3));

                String startDatePart = tempStartDate.split(" ")[0];
                String endDatePart = tempEndDate.split(" ")[0];

                LocalDate startDate = LocalDate.parse(startDatePart, formatter);
                LocalDate endDate = LocalDate.parse(endDatePart, formatter);

                LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                int eventId = Integer.parseInt(String.valueOf(event.getFirst()));
                EventDetails eventDetails = events.getOrDefault(current, new EventDetails(new ArrayList<>(), String.valueOf(current.getDayOfMonth())));

                if (!eventDetails.getIdSport().contains(eventId)) {
                    eventDetails.getIdSport().add(eventId);
                }

                events.put(current, eventDetails);
                current = current.plusDays(1);
            }
        }
    }

    private void dayEventsPopUp(List<Integer> selectedDay) throws SQLException {

        Stage popupStage = new Stage();
        popupStage.setTitle("Eventos do dia");

        VBox vbox = new VBox(400);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        displayEvents(vbox, selectedDay);


        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) App.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    private void displayEvents(VBox vbox, List<Integer> selectedDay) throws SQLException {
        vbox.getChildren().clear();
        vbox.setSpacing(20);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");

        RegistrationDao registrationDao = new RegistrationDao();

        for (int idSport : selectedDay) {
            List<List> calendars = registrationDao.getEventsDayAthlete(LoginController.idAthlete, idSport);

            for (List event : calendars) {
                Label eventLabel = new Label(String.valueOf(event.get(1)));
                eventLabel.getStyleClass().add("name-label");

                Label eventStartDate = new Label("Data de início: " + event.get(2));
                eventStartDate.getStyleClass().add("text-label");

                Label eventEndDate = new Label("Data de fim: " + event.get(3));
                eventEndDate.getStyleClass().add("text-label");

                Label eventLocalName = new Label("Local: " + event.get(5));
                eventLocalName.getStyleClass().add("text-label");

                Label eventLocalAddress = new Label("Morada: " + event.get(6) + ", " + event.get(7));
                eventLocalAddress.getStyleClass().add("text-label");

                scrollContent.getChildren().addAll(eventLabel, eventStartDate, eventEndDate, eventLocalName, eventLocalAddress);
            }
        }

        scrollPane.setContent(scrollContent);

        vbox.getChildren().add(scrollPane);
    }
}