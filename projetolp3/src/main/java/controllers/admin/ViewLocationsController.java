package controllers.admin;

import Dao.LocalDao;
import Models.Local;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ViewLocationsController {
    @FXML
    private FlowPane locationsContainer;
    @FXML
    private Label noLocationsLabel;

    public void initialize() throws SQLException {
        List<Local> locals = null;
        try {
            locals = getLocals();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (locals.isEmpty()) {
            showNoLocationsMessage();
        } else {
            displayLocations(locals);
        }
    }

    public List<Local> getLocals() throws SQLException {
        LocalDao localDao = new LocalDao();
        return localDao.getLocals();
    }

    private void showNoLocationsMessage() {
        noLocationsLabel.setVisible(true);
    }

    private void displayLocations(List<Local> locals) {
        noLocationsLabel.setVisible(false);
        locationsContainer.setVisible(true);
        locationsContainer.getChildren().clear();

        for (Local local : locals) {
            VBox locationItem = createLocationItem(local);
            locationsContainer.getChildren().add(locationItem);
        }
    }

    private VBox createLocationItem(Local local) {
        VBox locationItem = new VBox();
        locationItem.setSpacing(10);
        locationItem.getStyleClass().add("request-item");

        HBox nameContainer = new HBox(10);



        Label nameLabel = new Label(local.getName());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);

        nameContainer.getChildren().addAll(nameLabel);

        Label typeLabel = new Label("Tipo: " + local.getType());
        typeLabel.getStyleClass().add("name-label");

        Label cityLabel = new Label("Cidade: " + local.getCity());
        cityLabel.getStyleClass().add("name-label");

        Label capacityLabel = new Label("Capacidade: " + local.getCapacity());
        capacityLabel.getStyleClass().add("name-label");

        Label yearLabel = new Label("Ano de Construção: " + local.getConstructionYear());
        yearLabel.getStyleClass().add("name-label");

        Label eventLabel = new Label("Pais do Evento: " + local.getEventObject().getCountry());
        eventLabel.getStyleClass().add("name-label");

        locationItem.getChildren().addAll(nameContainer, typeLabel, cityLabel, capacityLabel, yearLabel, eventLabel);
        locationItem.setPrefWidth(500);

        return locationItem;
    }
}
