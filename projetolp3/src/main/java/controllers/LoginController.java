package controllers;

import Dao.AdminDao;
import Dao.AthleteDao;
import Models.Admin;
import Models.Athlete;
import Utils.PasswordUtils;
import controllers.admin.SportsController;
import controllers.admin.StartSportsController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoginController {
    public static int role = 0;

    public static String gender = "";
    public static int idAthlete;
    public static String idCountry;


    public boolean loginVerify(int idTemp, String tempPass, ActionEvent event) throws Exception {
        PasswordUtils passwordUtils = new PasswordUtils();
        tempPass = passwordUtils.encriptarPassword(tempPass);
        AdminDao adminDao = new AdminDao();
        boolean sucessLogin = false;
        if (idTemp < 1000) {
            Admin admin = adminDao.getAdminById(idTemp);
            if (admin != null && tempPass.equals(admin.getPassword())) {
                role = 1;
                sucessLogin = true;
            }
        } else {
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idTemp);
            if (athlete != null && tempPass.equals(athlete.getPassword())) {
                role = 2;
                gender = athlete.getGenre().getDesc();
                idCountry = athlete.getCountry().getIdCountry();
                idAthlete = idTemp;
                sucessLogin = true;
            }
        }
        return sucessLogin;
    }
}
