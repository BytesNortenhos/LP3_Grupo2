package controllers;
import bytesnortenhos.projetolp3.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ViewsController {
    @FXML
    private ImageView iconOlympic;
    @FXML
    private ImageView iconMode;
    private static Stage stage;
    private static Scene scene;
    @FXML
    private BorderPane parent;
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passwordText;
    private static boolean isDarkMode = true;
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();



    public void initialize() {
        URL iconOlympicURL = Main.class.getResource("img/iconOlympic.png");
        String iconOlympicStr = ((URL) iconOlympicURL).toExternalForm();
        Image image = new Image(iconOlympicStr);
        if(iconOlympic != null) iconOlympic.setImage(image);
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        image = new Image(iconMoonStr);
        if(iconMode != null) iconMode.setImage(image);
    }
    public void SwitchLoginToMenu(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
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
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconMode.setImage(image);
    }
    public void setDarkMode(){
        parent.getStylesheets().remove(String.valueOf(cssLight));
        parent.getStylesheets().add(String.valueOf(cssDark));
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconMode.setImage(image);
    }
    @FXML
    private void handleEntrarButtonAction(ActionEvent event) throws Exception {
        String username = userNameText.getText();
        String password= "";
        if(passwordText.getText() == null){
            password = passwordText.getText();
        }else{
            password = passwordText.getText();
        }
        LoginController.verificaLogin(username, password, event);
    }
    public static void verificaCargo(ActionEvent event) throws Exception {
        switch (LoginController.cargo) {
            case 0:
                mostrarLoginView(event);
                break;
            case 1:
                mostrarAdminView(event);
                break;
//            case 2:
//                mostrarAtletaView(currentStage);
//                break;
            default:
                System.out.println("Invalid cargo value");
        }
    }
    private static void mostrarLoginView(ActionEvent event) throws Exception {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }
    private static void mostrarAdminView(ActionEvent event) throws Exception {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root  = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/home.fxml")));
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
