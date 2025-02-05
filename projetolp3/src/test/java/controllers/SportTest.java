package controllers;

import Dao.*;
import Models.*;
import Utils.ConnectionsUtlis;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SportTest {

    @Test
    void testCrudSport() throws SQLException {
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        LocalDao ld = new LocalDao();

        try (Connection connection = connectionsUtlis.dbConnect()) {
            // Configuração dos dados
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
            String metrica = "Segundos";
            LocalDateTime dataInicio = LocalDateTime.of(2024, 2, 5, 10, 0);
            LocalDateTime dataFim = LocalDateTime.of(2024, 2, 10, 18, 30);


            Local local = ld.getLocals().stream()
                    .filter(l -> l.getIdLocal() == 3)
                    .findFirst().orElse(null);



            Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

            sportAdd.setIdSport(SportDao.addSport(sportAdd));

            SportDao sportDao = new SportDao();
            Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());

            assertEqualsSport(sportAdd, sportAdded);
            System.out.println("Sport inserido com sucesso");

            //Dados para atualizar

            String typeUpdate = "Collective";

            Gender genderUpdate = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Female"))
                    .findFirst().orElse(null);

            String nameUpdate = "Sport Test Update";
            String descriptionUpdate = "This is a sport Update for test purposes";
            int minParticipantsUpdate = 2;
            String scoringMeasureUpdate = "Points";
            String oneGameUpdate = "Multiple";
            int resultMinUpdate = 4000;
            int resultMaxUpdate = 6000;

            Sport sportAddUpdate = new Sport(0, typeUpdate, genderUpdate, nameUpdate, descriptionUpdate, minParticipantsUpdate, scoringMeasureUpdate, oneGameUpdate, resultMinUpdate, resultMaxUpdate);

            sportAddUpdate.setIdSport(sportAdd.getIdSport());
            SportDao.updateSport(sportAddUpdate);

            Sport sportAddedUpdate = sportDao.getSportById(sportAddUpdate.getIdSport());

            assertEqualsSport(sportAddUpdate, sportAddedUpdate);
            System.out.println("Sport atualizado com sucesso");

            //Remover o Sport
            SportDao.removeSport(sportAdd.getIdSport());
            System.out.println("Sport removido com sucesso");
        }
    }

    @Test
    void testGetNumberParticipantsSport() throws SQLException {
        LocalDao ld = new LocalDao();
        // Configuração dos dados
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
        String metrica = "Segundos";
        LocalDateTime dataInicio = LocalDateTime.of(2024, 2, 5, 10, 0);
        LocalDateTime dataFim = LocalDateTime.of(2024, 2, 10, 18, 30);


        Local local = ld.getLocals().stream()
                .filter(l -> l.getIdLocal() == 3)
                .findFirst().orElse(null);

        Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

        sportAdd.setIdSport(SportDao.addSport(sportAdd));

        SportDao sportDao = new SportDao();
        Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());

        assertEqualsSport(sportAdd, sportAdded);
        System.out.println("Sport inserido com sucesso");

        //Adicionar atletas

        Country country = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);

        Athlete atleta1 = new Athlete(0, "Teste123", "João Teste", country, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"),null);
        Athlete atleta2 = new Athlete(0, "Teste123", "Dinis Teste", country, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"),null);
        Athlete atleta3 = new Athlete(0, "Teste123", "Samuel Teste", country, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"),null);
        Athlete atleta4 = new Athlete(0, "Teste123", "Roberto Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"),null);

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atleta1);
        athletesAdicionados.add(atleta2);
        athletesAdicionados.add(atleta3);
        athletesAdicionados.add(atleta4);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }
        System.out.println("Atletas inseridos com sucesso");

        //Guardar status de registo
        RegistrationStatus status3 = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        RegistrationStatus status4 = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 4)
                .findFirst().orElse(null);


        //Adicionar registo
        int year = 2024;

        Registration registration1 = new Registration(0, atleta1, null, sportAdded, status3, year);
        Registration registration2 = new Registration(0, atleta2, null, sportAdded, status3, year);
        Registration registration3 = new Registration(0, atleta3, null, sportAdded, status4, year);
        Registration registration4 = new Registration(0, atleta4, null, sportAdded, status4, year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);
        RegistrationDao registrationDao = new RegistrationDao();
        for (Registration reg : registrationsAdicionados) {
            reg.setIdRegistration(registrationDao.addRegistrationSolo(reg));
        }
        System.out.println("Registos de atletas adicionados com sucesso");

        //Testar o método
        int expectedNumber = 4;
        int numberParticipants = sportDao.getNumberParticipantsSport(sportAdded.getIdSport(), year);
        assertEquals(expectedNumber, numberParticipants);
        System.out.println("ExpectedNumber: " + expectedNumber + "\nNumberParticipants: " + numberParticipants);
        System.out.println("Número de participantes retornado corretamente");


        //Remover os registos
        for(Registration reg: registrationsAdicionados){
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }
        System.out.println("Registos removidos com sucesso");

        //Remover os atletas
        for(Athlete at: athletesAdicionados){
            AthleteDao.removeAthlete(at.getIdAthlete());
        }
        System.out.println("Atletas removidos com sucesso");

        //Remover o Sport
        SportDao.removeSport(sportAdd.getIdSport());
        System.out.println("Sport removido com sucesso");
    }

    public static void assertEqualsSport(Sport expected, Sport actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        // Compara os atributos primitivos e strings diretamente
        assertEquals(expected.getIdSport(), actual.getIdSport(), "ID do sport não é igual");
        assertEquals(expected.getType(), actual.getType(), "Type do sport não é igual");
        assertEquals(expected.getName(), actual.getName(), "Name do sport não é igual");
        assertEquals(expected.getDesc(), actual.getDesc(), "Description do sport não é igual");
        assertEquals(expected.getMinParticipants(), actual.getMinParticipants(), "MinParticipants do sport não é igual");
        assertEquals(expected.getScoringMeasure(), actual.getScoringMeasure(), "ScoringMeasure do sport não é igual");
        assertEquals(expected.getOneGame(), actual.getOneGame(), "OneGame do sport não é igual");

        // Compara os objetos Gender
        assertNotNull(expected.getGenre(), "O gênero do atleta não pode ser nulo");
        assertNotNull(actual.getGenre(), "O gênero do atleta não pode ser nulo");
        assertEquals(expected.getGenre().getDesc(), actual.getGenre().getDesc(), "Descrição do gênero não é igual");
    }
}