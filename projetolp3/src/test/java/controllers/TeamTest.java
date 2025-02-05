package controllers;

import Dao.*;
import Models.*;
import Utils.ConnectionsUtlis;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    @Test
    void testCrudTeam() throws SQLException {
        LocalDao ld = new LocalDao();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();

        try (Connection connection = connectionsUtlis.dbConnect()) {
            // Adicionar um novo Sport
            String type = "Individual";

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Male"))
                    .findFirst().orElse(null);

            String name = "Sport Test";
            String description = "This is a sport for test purposes";
            int minParticipants = 1;
            String scoringMeasure = "Time";
            String oneGame = "One";
            int resultMin = 1000;
            int resultMax = 2000;
            int idStatus = 1;
            String metrica = "Minutos";
            LocalDateTime dataInicio = LocalDateTime.of(2024, 1, 5, 15, 30);
            LocalDateTime dataFim = LocalDateTime.of(2024, 1, 5, 17, 30);

            Local local = ld.getLocals().stream()
                    .filter(l -> l.getIdLocal() == 3)
                    .findFirst().orElse(null);

            Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

            sportAdd.setIdSport(SportDao.addSport(sportAdd));
            System.out.println("Sport inserido com sucesso");

            SportDao sportDao = new SportDao();
            Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());

            // Configuração dos dados Team
            String nameTeam = "Team added for test purposes";
            int yearFounded = 2000;

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Portugal"))
                    .findFirst().orElse(null);

            int minParticipantsTeam = 4;
            int maxParticipantsTeam = 8;


            Team teamAdd = new Team(0,nameTeam, country, gender, sportAdd, yearFounded,minParticipantsTeam,maxParticipantsTeam);
            TeamDao teamDao = new TeamDao();
            teamAdd.setIdTeam(teamDao.addTeam(teamAdd));

            Team teamAdded = TeamDao.getTeamById(teamAdd.getIdTeam());

            assertEqualsTeam(teamAdd, teamAdded);
            System.out.println("Team inserido com sucesso");

            //Dados Update Team
            String nameTeamUpdate = "Team Update added for test purposes";
            int yearFoundedUpdate = 2010;
            int minParticipantsTeamUpdate = 3;
            int maxParticipantsTeamUpdate = 6;

            Team teamAddUpdate = new Team(teamAdded.getIdTeam(),nameTeamUpdate, country, gender, sportAdd, yearFoundedUpdate,minParticipantsTeamUpdate,maxParticipantsTeamUpdate);
            teamDao.updateTeam(teamAddUpdate);

            Team teamAddedUpdate = TeamDao.getTeamById(teamAddUpdate.getIdTeam());

            assertEqualsTeam(teamAddUpdate, teamAddedUpdate);
            System.out.println("Team atualizado com sucesso");

            //Remover Team
            TeamDao.removeTeam(teamAdded.getIdTeam());
            System.out.println("Team removido com sucesso");

            //Remover Sport
            SportDao.removeSport(sportAdded.getIdSport());
            System.out.println("Sport removido com sucesso");
        }
    }

    public static void assertEqualsTeam(Team expected, Team actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        // Compara os atributos primitivos e strings diretamente
        assertEquals(expected.getIdTeam(), actual.getIdTeam(), "ID da Team não é igual");
        assertEquals(expected.getName(), actual.getName(), "Name da Team não é igual");
        assertEquals(expected.getYearFounded(), actual.getYearFounded(), "Year da Team não é igual");
        assertEquals(expected.getMinParticipants(), actual.getMinParticipants(), "MinParticipants da Team não é igual");
        assertEquals(expected.getMaxParticipants(), actual.getMaxParticipants(), "MaxParticipants da Team não é igual");

        // Compara os objetos Country
        assertNotNull(expected.getCountry(), "O país da Team não pode ser nulo");
        assertNotNull(actual.getCountry(), "O país da Team não pode ser nulo");
        assertEquals(expected.getCountry().getName(), actual.getCountry().getName(), "Nome do país não é igual");

        // Compara os objetos Gender
        assertNotNull(expected.getGenre(), "O gênero da Team não pode ser nulo");
        assertNotNull(actual.getGenre(), "O gênero da Team não pode ser nulo");
        assertEquals(expected.getGenre().getDesc(), actual.getGenre().getDesc(), "Descrição do gênero não é igual");

        //Compara os objetos Sport
        assertNotNull(expected.getSport(), "O sport da Team não pode ser nulo");
        assertNotNull(actual.getSport(), "O sport da Team não pode ser nulo");
        assertEquals(expected.getSport().getName(), actual.getSport().getName(), "Nome do sport não é igual");
    }
}
