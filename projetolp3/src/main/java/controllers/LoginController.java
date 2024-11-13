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



    public boolean verificaLogin(int idTemp, String senhaTemp, ActionEvent event) throws Exception {
        AdminDao adminDao = new AdminDao();
        boolean loginSucesso = false;
        if (idTemp < 1000) {
            Admin admin = adminDao.getAdminById(idTemp);
            if (admin != null && senhaTemp.equals(admin.getPassword())) {
                cargo = 1;
                loginSucesso = true;
            }
        } else {
            Athlete athlete = AthleteDao.getAthleteById(idTemp);
            if (athlete != null && senhaTemp.equals(athlete.getPassword())) {
                cargo = 2;
                gender = athlete.getGenre().getDesc();
                loginSucesso = true;
            }
        }
        return loginSucesso;
    }
}
