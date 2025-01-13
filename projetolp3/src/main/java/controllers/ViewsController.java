package controllers;

import Dao.RegistrationDao;
import bytesnortenhos.projetolp3.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

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
    private TextField idText;
    @FXML
    private PasswordField passwordText;
    private static boolean isDarkMode = true;

    public static String gender = "";
    URL cssDarkURL = Main.class.getResource("css/dark.css");
    URL cssLightURL = Main.class.getResource("css/light.css");
    String cssDark = ((URL) cssDarkURL).toExternalForm();
    String cssLight = ((URL) cssLightURL).toExternalForm();

    private final LoginController loginController;

    public ViewsController() {
        loginController = new LoginController();
    }

    public void initialize() {
        URL iconOlympicURL = Main.class.getResource("img/iconOlympic.png");
        String iconOlympicStr = ((URL) iconOlympicURL).toExternalForm();
        Image image = new Image(iconOlympicStr);
        if (iconOlympic != null) iconOlympic.setImage(image);
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        image = new Image(iconMoonStr);
        if (iconMode != null) iconMode.setImage(image);
    }

    public boolean changeMode(ActionEvent event) {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            setDarkMode();
        } else {
            setLightMode();
        }
        return isDarkMode;
    }

    public void setLightMode() {
        parent.getStylesheets().remove(cssDark);
        parent.getStylesheets().add(cssLight);
        URL iconMoonURL = Main.class.getResource("img/iconMoon.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconMode.setImage(image);
    }

    public void setDarkMode() {
        parent.getStylesheets().remove(String.valueOf(cssLight));
        parent.getStylesheets().add(String.valueOf(cssDark));
        URL iconMoonURL = Main.class.getResource("img/iconMoonLight.png");
        String iconMoonStr = ((URL) iconMoonURL).toExternalForm();
        Image image = new Image(iconMoonStr);
        iconMode.setImage(image);
    }

    @FXML
    private void handleEnterButtonAction(ActionEvent event) throws Exception {
        String idTemp = idText.getText();
        if (idTemp.matches("\\d+")) {
            int id = Integer.parseInt(idTemp);
            String password = "";
            if (passwordText.getText() == null) {
                password = passwordText.getText();
            } else {
                password = passwordText.getText();
            }
            boolean sucess = loginController.loginVerify(id, password, event);
            System.out.println(id);
            if (sucess) {
                //teste(id);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Login efetuado com sucesso!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    roleVerify(event);
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Erro!");
                alerta.setHeaderText("O email ou password inserido não está correto!");
                alerta.show();
            }
        }
    }

    public void teste(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        /*System.out.print("Data Inicio: ");
        String dataInicio = scanner.nextLine();
        System.out.print("Data Fim: ");
        String dataFim = scanner.nextLine();*/
        RegistrationDao registrationDao = new RegistrationDao();
        List<List> sports = registrationDao.getCalendarAthlete(id);
        System.out.println();
        /*for (List sport : sports) {
            System.out.println(sport.get(0));
            System.out.println(sport.get(1));
            System.out.println(sport.get(2));
            System.out.println(sport.get(3));
            System.out.println(sport.get(4));
        }*/
        System.out.println("Terminado: ");
        for (List sport : sports) {
            if (sport.get(4).equals("4")){
                System.out.println(sport.get(1));
                System.out.println(sport.get(2));
                System.out.println(sport.get(3));
            }
        }
        System.out.println();
        System.out.println("Por começar: ");
        for (List sport : sports) {
            if (sport.get(4).equals("3")){
                System.out.println(sport.get(1));
                System.out.println(sport.get(2));
                System.out.println(sport.get(3));
            }
        }
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        List<List> sportsA = registrationDao.getCalendarAdmin(ano);
        System.out.println();
        System.out.println("Terminado: ");
        for (List sport : sportsA) {
            if (sport.get(4).equals("4")){
                System.out.println(sport.get(1));
                System.out.println(sport.get(2));
                System.out.println(sport.get(3));
            }
        }
        System.out.println();
        System.out.println("Por começar: ");
        for (List sport : sportsA) {
            if (sport.get(4).equals("3")){
                System.out.println(sport.get(1));
                System.out.println(sport.get(2));
                System.out.println(sport.get(3));
            }
        }
    }

    public static void roleVerify(ActionEvent event) throws Exception {
        switch (LoginController.role) {
            case 0:
                showLoginView(event);
                break;
            case 1:
                showAdminView(event);
                break;
            case 2:
                showAthleteView(event);
                break;
            default:
                System.out.println("Invalid cargo value");
        }
        gender = LoginController.gender;
    }

    private static void showLoginView(ActionEvent event) throws Exception {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    private static void showAdminView(ActionEvent event) throws Exception {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/admin/home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        if (isDarkMode) {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/dark.css")).toExternalForm());
        } else {
            scene.getStylesheets().add(((URL) Main.class.getResource("css/light.css")).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    private static void showAthleteView(ActionEvent event) throws Exception {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ViewsController.class.getResource("/bytesnortenhos/projetolp3/athlete/home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
