package controllers.admin;

import AuxilierXML.Athletes;
import AuxilierXML.Sports;
import AuxilierXML.Teams;
import AuxilierXML.UploadXmlDAO;
import Dao.AthleteDao;
import Dao.CountryDao;
import Dao.EventDao;
import Dao.ResultDao;
import Models.Athlete;
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
import javafx.geometry.Pos;
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

public class AthletesViewController {

    @FXML
    private ImageView profileImage;
    @FXML
    private FlowPane showAthletesContainer;
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

    private List<List> getAthletes() throws SQLException {
        AthleteDao athleteDao = new AthleteDao();
        return athleteDao.getAthletesToShow();
    }

    private void showNoAthletesMessage() {
        noSportsLabel.setVisible(true);

    }
    public void showAthlete(ActionEvent event) throws SQLException {
        showAthletesContainer.getChildren().clear();
        List<List> athletes = null;
        try {
            athletes = getAthletes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (athletes.isEmpty()) {
            showNoAthletesMessage();
        } else {
            displayAthletes(athletes);
        }
    }
    private void displayAthletes(List<List> athletes) throws SQLException {
        noSportsLabel.setVisible(false);
        showAthletesContainer.setVisible(true);
        showAthletesContainer.getChildren().clear();
        for (List athlete : athletes) {
            if(Objects.equals(athlete.get(2).toString(), countryDrop.getValue())){
                VBox athleteItem = createSportsItem(athlete);
                showAthletesContainer.getChildren().add(athleteItem);
            }

        }
    }

    private VBox createSportsItem(List athlete) throws SQLException {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        HBox nameContainer = new HBox(10);
        profileImage = new ImageView();
        URL iconImageURL = Main.class.getResource(athlete.get(7).toString());
        if (iconImageURL != null) {
            Image images = new Image(iconImageURL.toExternalForm());
            profileImage.setImage(images);
            profileImage.setFitWidth(60);
            profileImage.setFitHeight(60);
            Circle clip = new Circle(30, 30, 30);
            profileImage.setClip(clip);
        }

        Label nameLabel = new Label(athlete.get(1).toString());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setTranslateY(13);
        nameContainer.getChildren().addAll(profileImage, nameLabel);

        Label genderLabel = new Label(athlete.get(3).toString());
        genderLabel.getStyleClass().add("gender-label");

        Label heightLabel = new Label("Altura: " + athlete.get(4).toString() + " cm");
        heightLabel.getStyleClass().add("height-label");

        Label weightLabel = new Label("Peso: " + athlete.get(5).toString() + " kg");
        weightLabel.getStyleClass().add("weight-label");

        Label birthLabel = new Label("Data de nascimento: " + athlete.get(6).toString());
        birthLabel.getStyleClass().add("birth-label");

        ImageView resultsImageView = new ImageView();
        URL iconResuURL = Main.class.getResource("img/iconResults.png");
        if (iconResuURL != null) {
            Image image = new Image(iconResuURL.toExternalForm());
            resultsImageView.setImage(image);
            resultsImageView.setFitWidth(60);
            resultsImageView.setFitHeight(60);
        }
        Button viewResultsButton = new Button();
        viewResultsButton.setGraphic(resultsImageView);
        viewResultsButton.getStyleClass().add("startButton");
        viewResultsButton.setOnAction(event -> {
            try {
                historyPopUp(event, Integer.parseInt(athlete.getFirst().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        ImageView editIconView = new ImageView();
        URL iconEditURL = Main.class.getResource("img/iconEdit.png");
        if (iconEditURL != null) {
            Image image = new Image(iconEditURL.toExternalForm());
            editIconView.setImage(image);
            editIconView.setFitWidth(60);
            editIconView.setFitHeight(60);
        }
        Button viewEditButton = new Button();
        viewEditButton.setGraphic(editIconView);
        viewEditButton.getStyleClass().add("startButton");
        viewEditButton.setOnAction(event -> {
            try {
                editPopUp(Integer.parseInt(athlete.getFirst().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ImageView editImageView = new ImageView();
        URL iconEditImageURL = Main.class.getResource("img/iconImageEdit.png");
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
                updateImageAthlete(event, Integer.parseInt(athlete.getFirst().toString()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(viewResultsButton, viewEditButton, viewEditImageButton);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setPadding(new Insets(10));

        requestItem.getChildren().addAll(nameContainer, genderLabel, heightLabel, weightLabel, birthLabel, buttonContainer);
        requestItem.setPrefWidth(500);
        return requestItem;
    }

    public void historyPopUp(ActionEvent event, int idAthlete) throws IOException, SQLException {
        ResultDao resultDao = new ResultDao();
        Stage popupStage = new Stage();
        popupStage.setTitle("Histórico de participações");

        VBox vbox = new VBox(500);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("popup-vbox");
        List<List> results = resultDao.getResultByAthlete(idAthlete);
        displayResults(vbox, results, idAthlete);



        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }
    private void displayResults(VBox vbox, List<List> results, int idAthlete) throws SQLException {
        vbox.getChildren().clear();
        vbox.setSpacing(20);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        scrollPane.setFitToWidth(true);

        VBox scrollContent = new VBox();
        scrollContent.setSpacing(20);
        scrollContent.setFillWidth(true);
        scrollContent.getStyleClass().add("popup-scroll-pane");
        for (List result : results) {
            VBox resultItem = createResultItem(result, idAthlete);
            scrollContent.getChildren().add(resultItem);
        }

        scrollPane.setContent(scrollContent);
        vbox.getChildren().add(scrollPane);
    }
    private VBox createResultItem(List result, int idAthlete) throws SQLException {
        VBox resultItem = new VBox();
        resultItem.setSpacing(10);

        Label resultLabel = new Label();

        Label nameLabel = new Label("Modalidade: " + (result.get(1) != null ? result.get(1).toString() : "N/A"));
        nameLabel.getStyleClass().add("name-label");
        if(result.get(7).toString().equals("One")){
            long resultValue = Long.parseLong(result.getFirst().toString());
            double resultSeconds = resultValue / 1000.0;
            resultLabel = new Label("Resultado: " + resultSeconds + " segundos");
            resultLabel.getStyleClass().add("result-label");
        }
        else{
            resultLabel = new Label("Resultado: " + result.getFirst().toString());
            resultLabel.getStyleClass().add("result-label");
        }

        Label typeLabel = new Label("Tipo de modalidade: " + (result.get(2) != null ? result.get(2).toString() : "N/A"));
        typeLabel.getStyleClass().add("type-label");

        if (result.get(3) != null) {
            Label teamLabel = new Label("Equipa: " + result.get(3).toString());
            teamLabel.getStyleClass().add("type-label");
            resultItem.getChildren().add(teamLabel);
        }

        Label dateLabel = new Label("Data: " + (result.get(4) != null ? result.get(4).toString() : "N/A"));
        dateLabel.getStyleClass().add("date-label");

        Label localLabel = new Label("Local: " + (result.get(5) != null ? result.get(5).toString() : "N/A"));
        localLabel.getStyleClass().add("local-label");


        resultItem.getChildren().addAll(nameLabel, resultLabel, typeLabel, dateLabel, localLabel);
        return resultItem;
    }


    private void editPopUp(int idAthlete) throws IOException, SQLException {
        Stage popupStage = new Stage();
        popupStage.setTitle("Editar atleta");


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("popup-vbox");

        AthleteDao athleteDao = new AthleteDao();
        Athlete athlete = athleteDao.getAthleteById(idAthlete);

        Label titleLabel = new Label("Editar o atleta " + athlete.getName());
        titleLabel.getStyleClass().add("popup-title");
        titleLabel.setTranslateY(-30);


        Label weightLabel = new Label("Peso(KG):");
        weightLabel.getStyleClass().add("popup-label");
        TextField weightField = new TextField();
        weightField.getStyleClass().add("popup-text");
        weightField.setText("" + athlete.getWeight());

        Label heightLabel = new Label("Altura(cm):");
        heightLabel.getStyleClass().add("popup-label");
        TextField heightField = new TextField();
        heightField.getStyleClass().add("popup-text");
        heightField.setText("" + athlete.getHeight());




        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("popup-button");
        submitButton.setOnAction(event -> {
            try {
                String weightText = weightField.getText();
                String heightText = heightField.getText();

                if (!weightText.matches("\\d+(\\.\\d+)?") || !heightText.matches("\\d+")) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Por favor, insira apenas números nos campos acima!");
                    alerta.show();
                    return;
                }

                Float weight = Float.valueOf(weightText);
                int height = Integer.parseInt(heightText);
                if(weight < 50.0 || weight > 150.0){
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Insira valores para o peso válidos!");
                    alerta.show();
                }
                if(height < 140 || weight > 240){
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro!");
                    alerta.setHeaderText("Insira valores para a altura válidos!");
                    alerta.show();
                }
                athleteDao.updateHeightWeight(idAthlete, height, weight);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Sucesso!");
                alerta.setHeaderText("Dados atualizados com sucesso!");
                alerta.show();
                popupStage.close();
                showAthlete(event);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        vbox.getChildren().addAll(titleLabel, weightLabel, weightField, heightLabel, heightField, submitButton);

        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void updateImageAthlete(ActionEvent event, int idAthlete) throws SQLException {
        String pathSave = "src/main/resources/bytesnortenhos/projetolp3/ImagesAthlete/";
        String pathSaveTemp =  Main.class.getResource("ImagesAthlete").toExternalForm().replace("file:", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Files");
        String pathToSave = "ImagesAthlete/";

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

                boolean saved = saveAthleteImage(idAthlete, pathSave, pathSaveTemp, selectedFile.getAbsolutePath());
                if (saved) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Sucesso!");
                    alerta.setHeaderText("A imagem foi guardada com sucesso!");
                    alerta.show();

                    AthleteDao athleteDao = new AthleteDao();
                    athleteDao.updateAthleteImage(idAthlete, pathToSave, "." + selectedFile.getName().split("\\.")[1]);
                    showAthlete(event);
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


    public boolean saveAthleteImage(int tempAthleteId, String pathSave, String pathSaveTemp, String pathImage) {
        File fileImage = new File(pathImage);
        String filename = String.valueOf(tempAthleteId) + "." + fileImage.getName().split("\\.")[1];
        try {
            Files.copy(fileImage.toPath(), Path.of(pathSave, filename), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(fileImage.toPath(), Path.of(pathSaveTemp, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}