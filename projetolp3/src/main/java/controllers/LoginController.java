package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class LoginController {

    public static int cargo = 0;
    public static void verificaLogin(String emailTemp, String senhaTemp, ActionEvent event) throws Exception {
        boolean loginSucesso = false;

//        for (Funcionario fun : funcionarios) {
//            if (emailTemp.equals(fun.getEmail()) && senhaTemp.equals(fun.getSenha())) {
        if(emailTemp.equals("a") && senhaTemp.equals("a")){
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
//    }

}}
