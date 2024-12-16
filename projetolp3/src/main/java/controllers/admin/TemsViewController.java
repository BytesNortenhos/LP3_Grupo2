package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.*;
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
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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

public class TemsViewController {

    @FXML
    private ImageView profileImage;

    @FXML
    private FlowPane showTeamsContainer;
    @FXML
    private Label noSportsLabel;
    @FXML
    private ComboBox<String> countryDrop;

    public void initialize() throws SQLException {
        loadCountrys();
    }
    public void loadCountrys() throws SQLException {
        countryDrop.getItems().clear();
        CountryDao countryDao = new CountryDao();
        List<String> countrys = countryDao.getCountrys();
        ObservableList<String> countryOptions = FXCollections.observableArrayList();
        countryOptions.addAll(countrys);
        countryDrop.setItems(countryOptions);
    }
    private List<List> getTeams() throws SQLException {
        TeamDao teamDao = new TeamDao();
        return teamDao.getTeamsToShow();
    }
    private void showNoTeamsMessage() {
        noSportsLabel.setVisible(true);

    }
    public void showTeams(ActionEvent event) throws SQLException {
        showTeamsContainer.getChildren().clear();
        List<List> teams = null;
        try {
            teams = getTeams();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (teams.isEmpty()) {
            showNoTeamsMessage();
        } else {
            displayTeams(teams);
        }
    }

    private void displayTeams(List<List> teams) throws SQLException {
        noSportsLabel.setVisible(false);
        showTeamsContainer.setVisible(true);
        showTeamsContainer.getChildren().clear();
        for (List team : teams) {
            if(Objects.equals(team.get(4).toString(), countryDrop.getValue())){
                VBox teamsItem = createTeamsItem(team);
                showTeamsContainer.getChildren().add(teamsItem);
            }

        }
    }
    private VBox createTeamsItem(List team) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        Text nameText = new Text(team.get(0) != null ? team.get(0).toString() : "N/A");
        nameText.getStyleClass().add("team-name");
        nameText.setWrappingWidth(300);

        Label sportLabel = new Label("Modalidade: " + (team.get(6) != null ? team.get(6).toString() : "N/A"));
        sportLabel.getStyleClass().add("text-label");

        Label genderLabel = new Label("Género: " + (team.get(5) != null ? team.get(5).toString() : "N/A"));
        genderLabel.getStyleClass().add("text-label");

        Label countryLabel = new Label("País: " + (team.get(4) != null ? team.get(4).toString() : "N/A"));
        countryLabel.getStyleClass().add("text-label");

        Label minParticipants = new Label("Min atletas: " + (team.get(2) != null ? team.get(2).toString() : "N/A"));
        minParticipants.getStyleClass().add("text-label");

        TeamDao teamDao = new TeamDao();
        int nPart = teamDao.getNAthletesOnTeam(Integer.parseInt(team.get(7).toString()));
        Label nParticipantes = new Label("Nº de atletas inscritos: " + nPart);
        nParticipantes.getStyleClass().add("text-label");

        Label maxParticipants = new Label("Max atletas: " + (team.get(3) != null ? team.get(3).toString() : "N/A"));
        maxParticipants.getStyleClass().add("text-label");

        Label yearFounded = new Label("Ano de fundação: " + (team.get(1) != null ? team.get(1).toString() : "N/A"));
        yearFounded.getStyleClass().add("text-label");

        if(nPart > 0){
            ImageView partImageView = new ImageView();
            URL iconPartURL = Main.class.getResource("img/iconReView.png");
            if (iconPartURL != null) {
                Image image = new Image(iconPartURL.toExternalForm());
                partImageView.setImage(image);
                partImageView.setFitWidth(50);
                partImageView.setFitHeight(50);
            }
            Button viewPartButton = new Button();
            viewPartButton.setGraphic(partImageView);
            viewPartButton.getStyleClass().add("startButton");
            viewPartButton.setOnAction(event -> {
                try {
                    partPopUp(event, Integer.parseInt(team.get(7).toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            requestItem.getChildren().addAll(nameText, sportLabel, genderLabel, countryLabel, minParticipants, nParticipantes, maxParticipants, yearFounded, viewPartButton);
        }
        else{
            requestItem.getChildren().addAll(nameText, sportLabel, genderLabel, countryLabel, minParticipants, nParticipantes, maxParticipants, yearFounded);
        }
        requestItem.setPrefWidth(500);
        return requestItem;
    }

    public void partPopUp(ActionEvent event, int idTeam) throws IOException, SQLException {
        AthleteDao athleteDao = new AthleteDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Lista de atletas");

        VBox vbox = new VBox(500);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        List<List> results = athleteDao.getPartToShow(idTeam);
        System.out.println(results);
        displayPart(vbox, results, idTeam);



        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void displayPart(VBox vbox, List<List> parts, int idTeam) throws SQLException {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");
        for (List part : parts) {
            VBox resultItem = createPartItem(part, idTeam);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }

    private VBox createPartItem(List part, int idTeam) throws SQLException {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        HBox nameContainer = new HBox(10);
        profileImage = new ImageView();
        URL iconImageURL = Main.class.getResource(part.get(4).toString());
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            profileImage.setImage(images);
            profileImage.setFitWidth(60);
            profileImage.setFitHeight(60);
            Circle clip = new Circle(30, 30, 30);
            profileImage.setClip(clip);
        }

        Label nameLabel = new Label(part.getFirst().toString());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);
        nameContainer.getChildren().addAll(profileImage, nameLabel);


        Label heightLabel = new Label("Altura: " + part.get(1) + " cm");
        heightLabel.getStyleClass().add("text-label");

        Label weightLabel = new Label("Peso: " + part.get(2) + " kg");
        weightLabel.getStyleClass().add("text-label");

        Label dateLabel = new Label("Data de nascimento: " + part.get(3));
        dateLabel.getStyleClass().add("text-label");



        resultItem.getChildren().addAll(nameContainer, heightLabel, weightLabel, dateLabel);
        return resultItem;
    }

}
