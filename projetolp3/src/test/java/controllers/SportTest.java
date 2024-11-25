package controllers;

import Dao.*;
import Models.*;
import Utils.ConnectionsUtlis;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SportTest {

    @Test
    void testAddSport() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String type = "Teste";

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Male"))
                    .findFirst().orElse(null);

            String name = "Teste";
            String description = "Teste";
            int minParticipants = 1;
            String scoringMeasure = "Time";
            String oneGame = "Teste";

            Sport sport = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, null, null, null);

            SportDao.addSport(sport);

            boolean sportEncontrado = false;
            int idSport = 0;

            SportDao spd = new SportDao();
            for (Sport s : spd.getSports()) {
                if (s.getName().equals(name)) {
                    sportEncontrado = true;
                    idSport = s.getIdSport();
                    break;
                }
            }

            if (sportEncontrado) {
                SportDao.removeSport(idSport);
                assertTrue(sportEncontrado);
            }
        }
    }

    @Test
    void testUpdateSport() throws SQLException {
        String type = "TesteUpdate";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        String name = "TesteUpdate";
        String description = "TesteUpdate";
        int minParticipants = 3;
        String scoringMeasure = "Points";
        String oneGame = "TesteUp";

        Sport sportEncontrado = null;
        int idSport = 5;
        SportDao spd = new SportDao();
        for (Sport s : spd.getSports()) {
            if (s.getIdSport() == idSport) {
                sportEncontrado = s;
                break;
            }
        }

        Sport sport = new Sport(idSport,type,gender,name,description,minParticipants,scoringMeasure,oneGame,null,null,null);

        if(sportEncontrado != null){
            SportDao.updateSport(sport);
        } else {
            fail();
        }

        for (Sport s: spd.getSports()){
            if (s.getIdSport() == idSport) {
                if (s.getType().equals(type) && s.getGenre().getIdGender() == gender.getIdGender()
                        && s.getName().equals(name) && s.getDesc().equals(description) && s.getMinParticipants() == minParticipants
                        && s.getScoringMeasure().equals(scoringMeasure) && s.getOneGame().equals(oneGame)) {

                    assertTrue(true);
                    SportDao.updateSport(sportEncontrado);
                }else {
                    fail("Erro ao atualizar");
                }
            }
        }
    }

    @Test
    void testGetNumberParticipantsSport() throws SQLException {

        SportDao spd = new SportDao();
        List<Sport> sports = spd.getSports();
        Sport sportEncontrado = null;
        int idSport = 5;
        int year = 2024;
        int expectedNumber = 4;

        for (Sport sp : sports) {
            if (sp.getIdSport() == idSport) {
                sportEncontrado = sp;
            }
        }

        //Adicionar um atleta
        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        Country country = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);

        Athlete atleta1 = new Athlete(0, "Teste123", "João Teste", country, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"));
        Athlete atleta2 = new Athlete(0, "Teste123", "Dinis Teste", country, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"));
        Athlete atleta3 = new Athlete(0, "Teste123", "Samuel Teste", country, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"));
        Athlete atleta4 = new Athlete(0, "Teste123", "Roberto Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"));

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atleta1);
        athletesAdicionados.add(atleta2);
        athletesAdicionados.add(atleta3);
        athletesAdicionados.add(atleta4);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Guardar status de registo
        RegistrationStatus status3 = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        RegistrationStatus status4 = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 4)
                .findFirst().orElse(null);


        //Adicionar registo
        Registration registration1 = new Registration(0, atleta1, null, sportEncontrado, status3, year);
        Registration registration2 = new Registration(0, atleta2, null, sportEncontrado, status3, year);
        Registration registration3 = new Registration(0, atleta3, null, sportEncontrado, status4, year);
        Registration registration4 = new Registration(0, atleta4, null, sportEncontrado, status4, year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);

        for (Registration reg : registrationsAdicionados) {
            reg.setIdRegistration(RegistrationDao.addRegistrationSolo(reg));
        }


        int numberParticipants = spd.getNumberParticipantsSport(idSport, year);
        assertEquals(expectedNumber, numberParticipants);
        System.out.println("ExpectedNumber: " + expectedNumber + "\nNumberParticipants: " + numberParticipants);
        System.out.println("Sucesso");


        //Remover os registos
        for(Registration reg: registrationsAdicionados){
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }

        //Remover os atletas
        for(Athlete at: athletesAdicionados){
            AthleteDao.removeAthlete(at.getIdAthlete());
        }

    }
}
