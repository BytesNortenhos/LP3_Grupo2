package controllers;

import Dao.RegistrationDao;
import bytesnortenhos.projetolp3.Main;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.time.LocalDate;
import java.time.Period;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import Models.Registration; // Importa a classe Registration

public class HomeController {
    @FXML
    private FlowPane mainContainer;
    @FXML
    private Label noRequestsLabel;
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    private static boolean isDarkMode = true;
    @FXML
    private ImageView iconModeNav;
    @FXML
    private ImageView iconHomeNav;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();
    @FXML
    private SplitMenuButton athleteSplitButton;
    @FXML
    private SplitMenuButton sportSplitButton;
    @FXML
    private ComboBox<String> athleteDrop;
    public void initialize() {
        URL iconMoonNavURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonNavStr = ((URL) iconMoonNavURL).toExternalForm();
        Image image = new Image(iconMoonNavStr);
        if(iconModeNav != null) iconModeNav.setImage(image);

        URL iconHomeNavURL = Main.class.getResource("img/iconOlympic.png");
        String iconHomeNavStr = ((URL) iconHomeNavURL).toExternalForm();
        image = new Image(iconHomeNavStr);
        if(iconHomeNav != null) iconHomeNav.setImage(image);

        // Chama o método para obter apenas as inscrições pendentes (idStatus = 1)
        List<Registration> registrations = null;
        try {
            registrations = getPendingRegistrations(); // Atualizado para usar o filtro
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (registrations.isEmpty()) {
            showNoRequestsMessage();
        } else {
            displayRequests(registrations);
        }
        athleteSplitButton.setOnMouseClicked(event -> {
            // Open the dropdown menu when clicking on the button's text
            athleteSplitButton.show();
        });
        sportSplitButton.setOnMouseClicked(mouseEvent -> {
            sportSplitButton.show();
        });
    }

    // Método atualizado para obter apenas as inscrições pendentes (idStatus = 1)
    private List<Registration> getPendingRegistrations() throws SQLException {
        List<Registration> registrations = RegistrationDao.getRegistrations(); // Busca todos os registros
        // Filtra os registros para incluir apenas os que têm idStatus = 1
        return registrations.stream()
                .filter(reg -> reg.getStatus().getIdStatus() == 1)  // Filtra pelo idStatus igual a 1
                .collect(Collectors.toList());
    }

    private void showNoRequestsMessage() {
        noRequestsLabel.setVisible(true);
        mainContainer.setVisible(false);
    }

    private void displayRequests(List<Registration> registrations) {
        noRequestsLabel.setVisible(false);
        mainContainer.setVisible(true);

        // Clear existing children
        mainContainer.getChildren().clear();

        // Create and add request nodes dynamically
        for (Request request : requests) {
            VBox requestItem = createRequestItem(request);
            mainContainer.getChildren().add(requestItem);
        // Cria e adiciona os itens de registro dinamicamente
        for (Registration registration : registrations) {
            HBox requestItem = createRequestItem(registration);
            requestsContainer.getChildren().add(requestItem);
        }
    }

    private VBox createRequestItem(Request request) {
        VBox requestItem = new VBox();
        requestItem.setSpacing(10);
        requestItem.getStyleClass().add("request-item");

        // Pega as informações da inscrição e as exibe
        Label nameLabel = new Label(registration.getAthlete().getName()); // Nome do atleta
        nameLabel.getStyleClass().add("name-label");

        // Calcula a idade diretamente (sem método separado)
        java.sql.Date birthDate = registration.getAthlete().getDateOfBirth();
        LocalDate birthLocalDate = birthDate.toLocalDate(); // Converte para LocalDate
        LocalDate currentDate = LocalDate.now(); // Data atual
        int age = Period.between(birthLocalDate, currentDate).getYears(); // Calcula a idade

        // Exibe a idade do atleta
        Label ageLabel = new Label("Idade: " + age); // Exibe a idade
        ageLabel.getStyleClass().add("age-label");

        Label sportLabel = new Label("Modalidade: " + registration.getSport().getName()); // Modalidade
        sportLabel.getStyleClass().add("sport-label");

        Label teamLabel = new Label("Equipa: " + registration.getTeam().getName()); // Nome da equipe
        teamLabel.getStyleClass().add("team-label");

        // Criação do botão de aceitar
        ImageView acceptImageView = new ImageView();
        URL iconAcceptURL = Main.class.getResource("img/iconAccept.png");
        if (iconAcceptURL != null) {
            String iconAcceptStr = iconAcceptURL.toExternalForm();
            Image image = new Image(iconAcceptStr);
            acceptImageView.setImage(image);
            acceptImageView.setFitWidth(80);
            acceptImageView.setFitHeight(80);
        }
        Button acceptButton = new Button();
        acceptButton.setGraphic(acceptImageView);
        acceptButton.getStyleClass().add("acceptButton");

        // Criação do botão de rejeitar
        ImageView rejectImageView = new ImageView();
        URL iconRejectURL = Main.class.getResource("img/iconReject.png");
        if (iconRejectURL != null) {
            String iconRejectStr = iconRejectURL.toExternalForm();
            Image image = new Image(iconRejectStr);
            rejectImageView.setImage(image);
            rejectImageView.setFitWidth(80);
            rejectImageView.setFitHeight(80);
        }
        Button rejectButton = new Button();
        rejectButton.setGraphic(rejectImageView);
        rejectButton.getStyleClass().add("rejectButton");

        // Definir o que acontece quando os botões são clicados
        acceptButton.setOnAction(event -> {
            try {
                // Atualiza o status da inscrição para 3 (aceito)
                RegistrationDao.updateRegistrationStatus(registration.getIdRegistration(), 3); // ID da inscrição e novo status
                // Atualizar a interface (remover ou alterar o item na interface)
                Platform.runLater(() -> {
                    // Remover o HBox que contém o requestItem
                    requestsContainer.getChildren().remove(requestItem.getParent());
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        rejectButton.setOnAction(event -> {
            try {
                // Atualiza o status da inscrição para 2 (rejeitado)
                RegistrationDao.updateRegistrationStatus(registration.getIdRegistration(), 2); // ID da inscrição e novo status
                // Atualizar a interface (remover ou alterar o item na interface)
                Platform.runLater(() -> {
                    // Remover o HBox que contém o requestItem
                    requestsContainer.getChildren().remove(requestItem.getParent());
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Coloca os botões de aceitar e rejeitar dentro de um HBox
        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(acceptButton, rejectButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10));

        // Add the labels and button container to the request item
        requestItem.getChildren().addAll(nameLabel, sportLabel, ageLabel, buttonContainer);
        return requestItem;
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

    public void mostrarRegistar(ActionEvent event) throws IOException {
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

    public static void returnHomeMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
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
}
