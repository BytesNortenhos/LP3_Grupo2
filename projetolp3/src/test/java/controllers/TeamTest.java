package controllers;

import Dao.*;
import Models.*;
import Utils.ConnectionsUtlis;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TeamTest {

    @Test
    void testAddTeam() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String name = "Australia Men's 4x100m Freestyle Relay Team";
            int yearFounded = 2023;

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Male"))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Portugal"))
                    .findFirst().orElse(null);


            Sport sport = new SportDao().getSports().stream()
                    .filter(s -> s.getName().equals("4x100m Freestyle Relay") && s.getIdSport() == 13)
                    .findFirst().orElse(null);

            int minParticipants = 4;
            int maxParticipants = 8;


            Team team = new Team(0,name, country, gender, sport, yearFounded,minParticipants,maxParticipants);

            TeamDao.addTeam(team);

            // Verificar se foi inserido
            boolean teamEncontrado = false;
            int idTeamEncontado = 0;

            for (Team t : TeamDao.getTeams()) {
                if (t.getName().equals(name)) {
                    teamEncontrado = true;
                    idTeamEncontado = t.getIdTeam();
                    break;
                }
            }

            if(teamEncontrado){
                TeamDao.removeTeam(idTeamEncontado);
                assertTrue(teamEncontrado);
            }
        }
    }

    @Test
    void testUpdateTeam() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String name = "Brazil Women's 100m Sprint";
            int yearFounded = 2010;

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Female"))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Brazil"))
                    .findFirst().orElse(null);

            Sport sport = new SportDao().getSports().stream()
                    .filter(s -> s.getName().equals("100m Sprint") && s.getIdSport() == 39)
                    .findFirst().orElse(null);


            int idTeam = 0;
            Team teamEncontrado = null;
            for (Team t : TeamDao.getTeams()) {
                if (t.getName().equals("USA Men's 4x100m Relay Team") && t.getIdTeam() == 14) {
                    idTeam = t.getIdTeam();
                    teamEncontrado = t;
                    break;
                }
            }

            int minParticipants = 4;
            int maxParticipants = 8;

            Team teamUpdate = new Team(idTeam, name, country, gender, sport, yearFounded, minParticipants, maxParticipants);

            if (teamEncontrado != null) {
                TeamDao.updateTeam(teamUpdate);
            } else {
                fail();
            }

            for (Team t : TeamDao.getTeams()) {
                if (t.getIdTeam() == idTeam) {
                    if (t.getName().equals(name) && t.getCountry().getIdCountry().equals(country.getIdCountry()) && t.getGenre().getIdGender() == gender.getIdGender()
                            && t.getSport().getIdSport() == sport.getIdSport() && t.getYearFounded() == yearFounded) {
                        assertTrue(true);
                        TeamDao.updateTeam(teamEncontrado);
                    } else {
                        fail("Erro ao atualizar");
                    }
                }
            }
        }
    }
}

