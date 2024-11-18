package controllers;

import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class LoginControllerTest {

    @Test
    void testLoginTrue() throws Exception {
        LoginController loginController = new LoginController();
        int idAtleta = 1001;
        String password = "password123";

        boolean loginSucess = loginController.verificaLogin(idAtleta,password,new ActionEvent());

        assertTrue(loginSucess);
    }

    @Test
    void testLoginFalse() throws Exception {
        LoginController loginController = new LoginController();
        int idAtleta = 1009;
        String password = "password123";

        boolean loginSucess = loginController.verificaLogin(idAtleta,password,new ActionEvent());

        assertFalse(loginSucess);
    }


}