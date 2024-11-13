package controllers;

import Dao.AdminDao;
import Dao.AthleteDao;
import Models.Admin;
import Models.Athlete;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoginController {

    public static int cargo = 0;

    public static String gender = "";

    static List<Athlete> athlete;

    static {
        try {
            athlete = AthleteDao.getAthletes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void verificaLogin(int idTemp, String senhaTemp, ActionEvent event) throws Exception {
        boolean loginSucesso = false;
        if (idTemp < 1000) {
            Admin admin = AdminDao.getAdminById(idTemp);
            if (admin != null && senhaTemp.equals(admin.getPassword())) {
                cargo = 1;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Login efetuado com sucesso!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ViewsController.verificaCargo(event);
                }
                loginSucesso = true;
            }
        } else {
            Athlete athlete = AthleteDao.getAthleteById(idTemp);
            if (athlete != null && senhaTemp.equals(athlete.getPassword())) {
                cargo = 2;
                gender = athlete.getGenre().getDesc();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Login efetuado com sucesso!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ViewsController.verificaCargo(event);
                }
                loginSucesso = true;
            }
        }
        if (!loginSucesso) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("O email ou password inserido não está correto!");
            alerta.show();
        }
    }

}
