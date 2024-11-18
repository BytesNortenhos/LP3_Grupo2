package controllers;

import Dao.AthleteDao;
import Dao.GenderDao;
import Dao.OlympicRecordDao;
import Dao.SportDao;
import Models.*;
import Utils.ConnectionsUtlis;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
}
