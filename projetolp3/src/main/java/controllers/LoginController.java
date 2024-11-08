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



    static List<Athlete> athlete;

    static {
        try {
            athlete = AthleteDao.getAthletes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void verificaLogin(int idTemp, String senhaTemp, ActionEvent event) throws Exception {
        boolean loginSucesso = false;

//        for (Funcionario fun : funcionarios) {
//            if (emailTemp.equals(fun.getEmail()) && senhaTemp.equals(fun.getSenha())) {
        if(senhaTemp.equals(AthleteDao.getAthleteById(idTemp).getPassword())){
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
        if (!loginSucesso) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText("O email ou password inserido não está correto!");
            alerta.show();
        }

    }

}
