package controllers.athletes;

import Dao.RegistrationDao;
import controllers.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarController {
    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthLabel;

    private YearMonth currentYearMonth;
    private final Map<LocalDate, String> events = new HashMap<>();

    public void initialize() {
        currentYearMonth = YearMonth.now();
        loadSampleEvents();
        populateCalendar(currentYearMonth);
    }

    private void populateCalendar(YearMonth yearMonth) {
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
                Button eventButton = new Button(events.get(date));
                eventButton.setStyle("-fx-font-size: 20; -fx-background-color: lightblue;");
                eventButton.setOnAction(e -> {
                    try {
                        showDayEvents();
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
        populateCalendar(currentYearMonth);
    }

    private void showDayEvents() throws SQLException {
        RegistrationDao registrationDao = new RegistrationDao();
        List<List> calendars = registrationDao.getCalendarAthlete(LoginController.idAthlete);
        System.out.println(calendars.getFirst().getFirst());
    }

    @FXML
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    private void loadSampleEvents() {
        events.put(LocalDate.now(), String.valueOf(LocalDate.now().getDayOfMonth()));
        events.put(LocalDate.now().plusDays(2), "Conference");
    }
}