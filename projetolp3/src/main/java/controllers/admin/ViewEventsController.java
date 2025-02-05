package controllers.admin;

import Dao.*;
import Models.Event;
import Models.Local;
import bytesnortenhos.projetolp3.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class ViewEventsController {
    @FXML
    private FlowPane eventsContainer;
    @FXML
    private Label noEventsLabel;
    EventDao eventDao = new EventDao();

    public void initialize() throws SQLException {
        List<Event> events = null;
        try {
            events = getEvents();  
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (events.isEmpty()) {
            showNoEventsMessage();
        } else {
            displayEvents(events);
        }
    }

    public List<Event> getEvents() throws SQLException {
        return eventDao.getEvents();
    }


    private void showNoEventsMessage() {
        noEventsLabel.setVisible(true);

    }

    private void displayEvents(List<Event> events) throws SQLException {
        noEventsLabel.setVisible(false);
        eventsContainer.setVisible(true);
        eventsContainer.getChildren().clear();


        for (Event event : events) {

            VBox eventItem = createEventItem(event);


            eventsContainer.getChildren().add(eventItem);
        }
    }


    private VBox createEventItem(Event events) throws SQLException {
        VBox eventItem = new VBox();
        eventItem.setSpacing(10);
        eventItem.getStyleClass().add("request-item");
        int year = events.getYear();

        HBox nameContainer = new HBox(10);
        String logoPath = events.getLogo();
        ImageView profileImage = new ImageView();
        URL iconImageURL = App.class.getResource(logoPath);
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            profileImage.setImage(images);
            profileImage.setFitWidth(60);
            profileImage.setFitHeight(60);
            Circle clip = new Circle(30, 30, 30);
            profileImage.setClip(clip);
        }
        Label nameLabel = new Label("" + year);
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);
        nameContainer.getChildren().addAll(profileImage, nameLabel);



        Label countryNameLabel = new Label("País: " + events.getCountry().getName());
        countryNameLabel.getStyleClass().add("name-label");


        Label continentLabel = new Label("Continente: " + events.getCountry().getContinent());
        continentLabel.getStyleClass().add("name-label");


        HBox iconsContainer = new HBox(10);
        ImageView editImageView = new ImageView();
        URL iconEditImageURL = App.class.getResource("img/iconEdit.png");
        if (iconEditImageURL != null) {
            Image image = new Image(iconEditImageURL.toExternalForm());
            editImageView.setImage(image);
            editImageView.setFitWidth(60);
            editImageView.setFitHeight(60);
        }
        Button viewEditImageButton = new Button();
        viewEditImageButton.setGraphic(editImageView);
        viewEditImageButton.getStyleClass().add("startButton");
        viewEditImageButton.setOnAction(event -> {
            try {
                updateImageEvent(event, events.getYear());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ImageView localsImageView = new ImageView();
        URL iconLocalsImageURL = App.class.getResource("img/iconLocalsView.png");
        if (iconLocalsImageURL != null) {
            Image image = new Image(iconLocalsImageURL.toExternalForm());
            localsImageView.setImage(image);
            localsImageView.setFitWidth(60);
            localsImageView.setFitHeight(60);
        }
        Button viewLocalsImageButton = new Button();
        viewLocalsImageButton.setGraphic(localsImageView);
        viewLocalsImageButton.getStyleClass().add("startButton");
        viewLocalsImageButton.setOnAction(event -> {
            try {
                verifyLocals(event, year);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        iconsContainer.getChildren().addAll(viewEditImageButton, viewLocalsImageButton);
        eventItem.getChildren().addAll(nameContainer, countryNameLabel, continentLabel, iconsContainer);
        eventItem.setPrefWidth(500);

        return eventItem;
    }

    public void updateImageEvent(ActionEvent event, int year) throws SQLException {
        String pathSave = "src/main/resources/bytesnortenhos/projetolp3/ImagesEvent/";
        String pathSaveTemp =  App.class.getResource("ImagesEvent").toExternalForm().replace("file:", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");
        String pathToSave = "ImagesEvent/";

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null) {
            long pngCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".png")).count();
            long jpgCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpg")).count();
            long jpegCount = selectedFiles.stream().filter(file -> file.getName().endsWith(".jpeg")).count();

            if (selectedFiles.size() == 1 && (pngCount == 1 || jpgCount == 1 || jpegCount == 1)) {
                File selectedFile = selectedFiles.get(0);

                boolean saved = saveEventImage(year, pathSave, pathSaveTemp, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();


                    eventDao.updateEventImage(year, pathToSave, "." + selectedFile.getName().split("\\.")[1]);
                    displayEvents(getEvents());
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

    public boolean saveEventImage(int tempEventYear, String pathSave, String pathSaveTemp, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(tempEventYear) + "." + fileImage.getName().split("\\.")[1];

        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(fileImage.toPath(), Path.of(pathSaveTemp, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public void verifyLocals(ActionEvent event, int year) throws SQLException {
        if (!eventDao.getIfLocals(year)) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Informação");
            alerta.setHeaderText("Não existem locais associados a este evento!");
            alerta.show();
        } else {
            showLocalsPopUp(event, year);
        }
    }
    public void showLocalsPopUp(ActionEvent event, int year) throws SQLException {
        Stage popupStage = new Stage();
        LocalDao localDao = new LocalDao();
        popupStage.setTitle("Locais do evento");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");

        List<Local> locals =  localDao.getLocalsByYear(year);
        displayResults(vbox, locals);

        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) App.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void displayResults(VBox vbox, List<Local> locals) throws SQLException {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");
        for (Local local : locals) {
            VBox resultItem = createResultItem(local);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }

    private VBox createResultItem(Local local) throws SQLException {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);


        Label nameLabel = new Label(local.getName());
        nameLabel.getStyleClass().add("name-label");

        Label typeLabel = new Label("Tipo: " + local.getType());
        typeLabel.getStyleClass().add("text-label");

        Label address = new Label("Endereço: " + local.getAddress());
        address.getStyleClass().add("text-label");

        Label capacity = new Label("Capacidade: " + local.getCapacity());
        capacity.getStyleClass().add("text-label");

        Label contructionYear = new Label("Ano de construção: " + local.getConstructionYear());
        contructionYear.getStyleClass().add("text-label");

        resultItem.getChildren().addAll(nameLabel, typeLabel, address, capacity, contructionYear);
        return resultItem;
    }
}