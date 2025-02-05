package controllers;

import Dao.*;
import Models.*;
import Utils.PasswordUtils;
import controllers.admin.StartSportsController;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controllers.AthleteTest.assertEqualsAthlete;
import static controllers.SportTest.assertEqualsSport;
import static controllers.TeamTest.assertEqualsTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IniciarModalidadesTest {
    @Test
    void testIniciarModalidadesIndividual() throws SQLException {
        LocalDao ld = new LocalDao();
        //Criar o Sport
        String type = "Individual";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        String name = "Sport Test Individual";
        String description = "This is a sport for test purposes Individual";
        int minParticipants = 4;
        String scoringMeasure = "Time";
        String oneGame = "One";
        int resultMin = 1000;
        int resultMax = 2000;
        int idStatus = 1;
        String metrica = "Minutos";
        LocalDateTime dataInicio = LocalDateTime.of(2025, 1, 5, 15, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 1, 5, 17, 30);

        Local local = ld.getLocals().stream()
                .filter(l -> l.getIdLocal() == 3)
                .findFirst().orElse(null);

        Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

        sportAdd.setIdSport(SportDao.addSport(sportAdd));

        //Verificar se foi inserido
        SportDao sportDao = new SportDao();
        Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());
        assertEqualsSport(sportAdd, sportAdded);
        System.out.println("Sport inserido com sucesso");

        //Adicionar atletas

        Country country = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);

        Athlete atleta1 = new Athlete(0, "Teste123", "João Teste", country, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"), "image1");
        Athlete atleta2 = new Athlete(0, "Teste123", "Dinis Teste", country, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"), "image2");
        Athlete atleta3 = new Athlete(0, "Teste123", "Samuel Teste", country, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"), "image3");
        Athlete atleta4 = new Athlete(0, "Teste123", "Roberto Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"), "image4");
        Athlete atleta5 = new Athlete(0, "Teste123", "Gonçalo Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-12-30"), "image5");

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atleta1);
        athletesAdicionados.add(atleta2);
        athletesAdicionados.add(atleta3);
        athletesAdicionados.add(atleta4);
        athletesAdicionados.add(atleta5);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Colocar a Password encriptada para poder comparar objetos
        PasswordUtils passwordUtils = new PasswordUtils();
        for (Athlete athlete : athletesAdicionados) {
            String passwordEncriptadaAdd = passwordUtils.encriptarPassword(athlete.getPassword());
            athlete.setPassword(passwordEncriptadaAdd);
        }

        // Verificar se foi inserido
        AthleteDao athleteDao = new AthleteDao();
        for (Athlete athlete : athletesAdicionados) {
            Athlete athleteAdded = athleteDao.getAthleteById(athlete.getIdAthlete());
            assertEqualsAthlete(athleteAdded, athlete);
            System.out.println("Atleta " + athlete.getName() + "inserido com sucesso");
        }
        System.out.println("Atletas inseridos com sucesso");


        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        int year = 2024;

        Registration registration1 = new Registration(0, atleta1, null, sportAdd, status, year);
        Registration registration2 = new Registration(0, atleta2, null, sportAdd, status, year);
        Registration registration3 = new Registration(0, atleta3, null, sportAdd, status, year);
        Registration registration4 = new Registration(0, atleta4, null, sportAdd, status, year);
        Registration registration5 = new Registration(0, atleta5, null, sportAdd, status, year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);
        registrationsAdicionados.add(registration5);

        RegistrationDao registrationDao = new RegistrationDao();
        for (Registration reg : registrationsAdicionados) {
            reg.setIdRegistration(registrationDao.addRegistrationSolo(reg));
        }

        for (Athlete athlete : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athlete.getIdAthlete());
            assertEquals(1, regs.size());
            System.out.println("Registo do atleta " + athlete.getName() + " inserido com sucesso");
        }
        System.out.println("Registos inseridos com sucesso");

        System.out.println("A gerar ids...");


        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.sportStart(sportAdd.getIdSport(), year, 3);
        assertTrue(result);
        System.out.println("Modalidade Iniciada");

        //Verifica se os registos foram adicionados
        int ExpectedResults = 5;
        List<Registration> registrationsFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athl.getIdAthlete());
            registrationsFound.addAll(regs);
        }
        assertEquals(ExpectedResults, registrationsFound.size());
        System.out.println("Teste dos registos passou");

        //Verificar se os resultados foram adicionados
        ResultDao rsDao = new ResultDao();
        List<Result> resultFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Result> results = rsDao.getResultByAthleteJunit(athl.getIdAthlete());
            resultFound.addAll(results);
        }
        assertEquals(ExpectedResults, resultFound.size());
        System.out.println("Teste dos resultados passou");

        //verificar se as medalhas foram atribuídas
        List<Medal> medalsFound = new ArrayList<>();
        for (Athlete athlete : athletesAdicionados) {
            List<Medal> medalsById = MedalDao.getMedalsByAthleteId(athlete.getIdAthlete());
            medalsFound.addAll(medalsById);
        }
        assertEquals(ExpectedResults, medalsFound.size());
        System.out.println("Teste das medalhas passou");

        //Verificar WinnerOlympic
        WinnerOlympicDao wo = new WinnerOlympicDao();
        WinnerOlympic winnersOlympic = wo.getWinnerOlympicById(sportAdd.getIdSport(), year);
        assertEquals(sportAdd.getIdSport(), winnersOlympic.getSport().getIdSport());
        System.out.println("Teste de WinnnerOlympic passou");

        System.out.println("A remover dados inseridos na BD...");
        //Remover os resultados
        for (Result res : resultFound) {
            ResultDao.removeResult(res.getIdResult());
        }
        System.out.println("Resultados removidos");

        //Remover os registos
        for (Registration reg : registrationsAdicionados) {
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }
        System.out.println("Registos removidos");

        //Remover medals
        for (Medal m : medalsFound) {
            MedalDao.removeMedal(m.getIdMedal());
        }
        System.out.println("Medalhas removidas");

        //Remover winnersOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removidos");

        //Remover os atletas
        for (Athlete at : athletesAdicionados) {
            AthleteDao.removeAthlete(at.getIdAthlete());
        }
        System.out.println("Atletas removidos");

        //Remover o Sport
        SportDao.removeSport(sportAdd.getIdSport());
        System.out.println("Sport removido");
    }

    @Test
    void testIniciarModalidadesCollective() throws SQLException {
        LocalDao ld = new LocalDao();
        //Criar o Sport
        String type = "Collective";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        String name = "Sport Test Collective";
        String description = "This is a sport for test purposes Collective";
        int minParticipants = 4;
        String scoringMeasure = "Time";
        String oneGame = "One";
        int resultMin = 1000;
        int resultMax = 2000;
        int idStatus = 0;
        String metrica = "Minutos";
        LocalDateTime dataInicio = LocalDateTime.of(2025, 1, 5, 15, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 1, 5, 17, 30);

        Local local = ld.getLocals().stream()
                .filter(l -> l.getIdLocal() == 3)
                .findFirst().orElse(null);

        Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

        sportAdd.setIdSport(SportDao.addSport(sportAdd));

        //Verificar se foi inserido
        SportDao sportDao = new SportDao();
        Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());
        assertEqualsSport(sportAdd, sportAdded);
        System.out.println("Sport inserido com sucesso");

        //Adicionar equipas
        String nameAus = "Australia Men's 4x100m Freestyle Relay Team Test";
        String nameBra = "Brazil Men's 4x100m Freestyle Relay Team Test";
        String namePrt = "Portugal Men's 4x100m Freestyle Relay Team Test";
        String nameUsa = "Usa men's 4x100m Freestyle Relay Team Test";

        Country countryAus = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Australia"))
                .findFirst().orElse(null);

        Country countryBra = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Brazil"))
                .findFirst().orElse(null);

        Country countryPrt = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);


        Country countryUsa = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("United States of America"))
                .findFirst().orElse(null);


        Team teamAus = new Team(0, nameAus, countryAus, gender, sportAdd, 2000, 2, 8);
        Team teamBra = new Team(0, nameBra, countryBra, gender, sportAdd, 2000, 2, 8);
        Team teamPrt = new Team(0, namePrt, countryPrt, gender, sportAdd, 2000, 2, 8);
        Team teamUsa = new Team(0, nameUsa, countryUsa, gender, sportAdd, 2000, 2, 8);

        List<Team> teamsAdicionadas = new ArrayList<>();
        teamsAdicionadas.add(teamAus);
        teamsAdicionadas.add(teamBra);
        teamsAdicionadas.add(teamPrt);
        teamsAdicionadas.add(teamUsa);

        TeamDao td = new TeamDao();
        for (Team t : teamsAdicionadas) {
            t.setIdTeam(td.addTeam(t));
        }

        for (Team t : teamsAdicionadas) {
            Team teamAdded = TeamDao.getTeamById(t.getIdTeam());
            assertEqualsTeam(t, teamAdded);
            System.out.println("Equipa " + t.getName() + " inserida com sucesso");
        }
        System.out.println("Equipas inseridas com sucesso");

        //Registar atletas para as equipas

        Athlete atletaAus1 = new Athlete(0, "Teste123", "João Teste", countryAus, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"), "image1");
        Athlete atletaAus2 = new Athlete(0, "Teste123", "Dinis Teste", countryAus, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"), "image2");
        Athlete atletaBra1 = new Athlete(0, "Teste123", "Samuel Teste", countryBra, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"), "image3");
        Athlete atletaBra2 = new Athlete(0, "Teste123", "Roberto Teste", countryBra, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"), "image4");
        Athlete atletaPrt1 = new Athlete(0, "Teste123", "Gonçalo Teste", countryPrt, gender, 155, 59, java.sql.Date.valueOf("2000-03-22"), "image5");
        Athlete atletaPrt2 = new Athlete(0, "Teste123", "Pedro Teste", countryPrt, gender, 179, 80, java.sql.Date.valueOf("2004-09-30"), "image6");
        Athlete atletaUsa1 = new Athlete(0, "Teste123", "Tiago Teste", countryUsa, gender, 183, 86, java.sql.Date.valueOf("2005-10-24"), "image7");
        Athlete atletaUsa2 = new Athlete(0, "Teste123", "Rui Teste", countryUsa, gender, 198, 102, java.sql.Date.valueOf("2004-01-16"), "image8");

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atletaAus1);
        athletesAdicionados.add(atletaAus2);
        athletesAdicionados.add(atletaBra1);
        athletesAdicionados.add(atletaBra2);
        athletesAdicionados.add(atletaPrt1);
        athletesAdicionados.add(atletaPrt2);
        athletesAdicionados.add(atletaUsa1);
        athletesAdicionados.add(atletaUsa2);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Colocar a Password encriptada para poder comparar objetos
        PasswordUtils passwordUtils = new PasswordUtils();
        for (Athlete athlete : athletesAdicionados) {
            String passwordEncriptadaAdd = passwordUtils.encriptarPassword(athlete.getPassword());
            athlete.setPassword(passwordEncriptadaAdd);
        }

        // Verificar se foi inserido
        AthleteDao athleteDao = new AthleteDao();
        for (Athlete athlete : athletesAdicionados) {
            Athlete athleteAdded = athleteDao.getAthleteById(athlete.getIdAthlete());
            assertEqualsAthlete(athleteAdded, athlete);
            System.out.println("Atleta " + athlete.getName() + "inserido com sucesso");
        }
        System.out.println("Atletas inseridos com sucesso");

        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        int year = 2024;

        Registration registrationAus1 = new Registration(0, atletaAus1, teamAus, sportAdd, status, year);
        Registration registrationAus2 = new Registration(0, atletaAus2, teamAus, sportAdd, status, year);
        Registration registrationBra1 = new Registration(0, atletaBra1, teamBra, sportAdd, status, year);
        Registration registrationBra2 = new Registration(0, atletaBra2, teamBra, sportAdd, status, year);
        Registration registrationPrt1 = new Registration(0, atletaPrt1, teamPrt, sportAdd, status, year);
        Registration registrationPrt2 = new Registration(0, atletaPrt2, teamPrt, sportAdd, status, year);
        Registration registrationUsa1 = new Registration(0, atletaUsa1, teamUsa, sportAdd, status, year);
        Registration registrationUsa2 = new Registration(0, atletaUsa2, teamUsa, sportAdd, status, year);

        List<Registration> registrationsAdicionadosTeam = new ArrayList<>();
        registrationsAdicionadosTeam.add(registrationAus1);
        registrationsAdicionadosTeam.add(registrationAus2);
        registrationsAdicionadosTeam.add(registrationBra1);
        registrationsAdicionadosTeam.add(registrationBra2);
        registrationsAdicionadosTeam.add(registrationPrt1);
        registrationsAdicionadosTeam.add(registrationPrt2);
        registrationsAdicionadosTeam.add(registrationUsa1);
        registrationsAdicionadosTeam.add(registrationUsa2);

        RegistrationDao rgDao = new RegistrationDao();
        for (Registration reg : registrationsAdicionadosTeam) {
            reg.setIdRegistration(rgDao.addRegistrationTeam(reg));
        }

        for (Athlete athlete : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athlete.getIdAthlete());
            assertEquals(1, regs.size());
            System.out.println("Registo do atleta " + athlete.getName() + " inserido com sucesso");
        }
        System.out.println("Registos inseridos com sucesso");


        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.sportStart(sportAdd.getIdSport(), year, 3);
        assertTrue(result);
        System.out.println("Modalidade Iniciada");

        //Verifica se os registos foram adicionados
        int ExpectedResults = 8;
        List<Registration> registrationsFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athl.getIdAthlete());
            registrationsFound.addAll(regs);
        }
        assertEquals(ExpectedResults, registrationsFound.size());
        System.out.println("Teste dos registos passou");

        //Verificar se os resultados foram adicionados
        ResultDao rsDao = new ResultDao();
        List<Result> resultFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Result> results = rsDao.getResultByAthleteJunit(athl.getIdAthlete());
            resultFound.addAll(results);
        }
        assertEquals(ExpectedResults, resultFound.size());
        System.out.println("Teste dos resultados passou");

        //verificar se as medalhas aos atletas foram atribuídas
        List<Medal> medalsFound = new ArrayList<>();
        for (Athlete athlete : athletesAdicionados) {
            List<Medal> medalsById = MedalDao.getMedalsByAthleteId(athlete.getIdAthlete());
            medalsFound.addAll(medalsById);
        }
        assertEquals(ExpectedResults, medalsFound.size());
        System.out.println("Teste das medalhas passou");

        //Verificar se as medalhas ás equipas foram atribuídas
        int expectedCount = 4;
        int medalsCountTeam = 0;
        for (Team team : teamsAdicionadas) {
            medalsCountTeam += MedalDao.getMedalsByTeamId(team.getIdTeam());
        }
        assertEquals(expectedCount, medalsCountTeam);
        System.out.println("Teste das medalhas das equipas passou");

        //Verificar WinnerOlympic
        WinnerOlympicDao wo = new WinnerOlympicDao();
        WinnerOlympic winnersOlympic = wo.getWinnerOlympicById(sportAdd.getIdSport(), year);
        assertEquals(sportAdd.getIdSport(), winnersOlympic.getSport().getIdSport());
        System.out.println("Teste de WinnnerOlympic passou");

        System.out.println("A remover dados inseridos na BD...");
        //Remover os resultados
        for (Result res : resultFound) {
            ResultDao.removeResult(res.getIdResult());
        }
        System.out.println("Resultados removidos");

        //Remover os registos
        for (Registration reg : registrationsAdicionadosTeam) {
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }
        System.out.println("Registos removidos");

        //Remover medals
        for (Medal m : medalsFound) {
            MedalDao.removeMedal(m.getIdMedal());
        }
        System.out.println("Medalhas removidas");

        //Remover winnersOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removidos");

        //Remover os atletas
        for (Athlete at : athletesAdicionados) {
            AthleteDao.removeAthlete(at.getIdAthlete());
        }
        System.out.println("Atletas removidos");

        //Remover as equipas
        for (Team t : teamsAdicionadas) {
            TeamDao.removeTeam(t.getIdTeam());
        }
        System.out.println("Equipas removidas");

        //Remover o Sport
        SportDao.removeSport(sportAdd.getIdSport());
        System.out.println("Sport removido");
    }

    @Test
    void testIniciarModalidadesMultipleGamesTeam() throws SQLException {
        LocalDao ld = new LocalDao();
        //Criar o Sport
        String type = "Collective";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        String name = "Sport Test Collective Multiple Games";
        String description = "This is a sport for test purposes Collective Multiple Games";
        int minParticipants = 4;
        String scoringMeasure = "Time";
        String oneGame = "Multiple";
        int resultMin = 50;
        int resultMax = 100;
        int idStatus = 0;
        String metrica = "Minutos";
        LocalDateTime dataInicio = LocalDateTime.of(2025, 1, 5, 15, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 1, 5, 17, 30);

        Local local = ld.getLocals().stream()
                .filter(l -> l.getIdLocal() == 3)
                .findFirst().orElse(null);

        Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

        sportAdd.setIdSport(SportDao.addSport(sportAdd));

        //Verificar se foi inserido
        SportDao sportDao = new SportDao();
        Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());
        assertEqualsSport(sportAdd, sportAdded);
        System.out.println("Sport inserido com sucesso");

        //Adicionar equipas
        String nameAus = "Australia Men's 4x100m Freestyle Relay Team Test";
        String nameBra = "Brazil Men's 4x100m Freestyle Relay Team Test";
        String namePrt = "Portugal Men's 4x100m Freestyle Relay Team Test";
        String nameUsa = "Usa men's 4x100m Freestyle Relay Team Test";

        Country countryAus = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Australia"))
                .findFirst().orElse(null);

        Country countryBra = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Brazil"))
                .findFirst().orElse(null);

        Country countryPrt = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);


        Country countryUsa = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("United States of America"))
                .findFirst().orElse(null);


        Team teamAus = new Team(0, nameAus, countryAus, gender, sportAdd, 2000, 2, 8);
        Team teamBra = new Team(0, nameBra, countryBra, gender, sportAdd, 2000, 2, 8);
        Team teamPrt = new Team(0, namePrt, countryPrt, gender, sportAdd, 2000, 2, 8);
        Team teamUsa = new Team(0, nameUsa, countryUsa, gender, sportAdd, 2000, 2, 8);

        List<Team> teamsAdicionadas = new ArrayList<>();
        teamsAdicionadas.add(teamAus);
        teamsAdicionadas.add(teamBra);
        teamsAdicionadas.add(teamPrt);
        teamsAdicionadas.add(teamUsa);

        TeamDao td = new TeamDao();
        for (Team t : teamsAdicionadas) {
            t.setIdTeam(td.addTeam(t));
        }

        for (Team t : teamsAdicionadas) {
            Team teamAdded = TeamDao.getTeamById(t.getIdTeam());
            assertEqualsTeam(t, teamAdded);
            System.out.println("Equipa " + t.getName() + " inserida com sucesso");
        }
        System.out.println("Equipas inseridas com sucesso");

        //Registar atletas para as equipas

        Athlete atletaAus1 = new Athlete(0, "Teste123", "João Teste", countryAus, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"), "image1");
        Athlete atletaAus2 = new Athlete(0, "Teste123", "Dinis Teste", countryAus, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"), "image2");
        Athlete atletaBra1 = new Athlete(0, "Teste123", "Samuel Teste", countryBra, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"), "image3");
        Athlete atletaBra2 = new Athlete(0, "Teste123", "Roberto Teste", countryBra, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"), "image4");
        Athlete atletaPrt1 = new Athlete(0, "Teste123", "Gonçalo Teste", countryPrt, gender, 155, 59, java.sql.Date.valueOf("2000-03-22"), "image5");
        Athlete atletaPrt2 = new Athlete(0, "Teste123", "Pedro Teste", countryPrt, gender, 179, 80, java.sql.Date.valueOf("2004-09-30"), "image6");
        Athlete atletaUsa1 = new Athlete(0, "Teste123", "Tiago Teste", countryUsa, gender, 183, 86, java.sql.Date.valueOf("2005-10-24"), "image7");
        Athlete atletaUsa2 = new Athlete(0, "Teste123", "Rui Teste", countryUsa, gender, 198, 102, java.sql.Date.valueOf("2004-01-16"), "image8");

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atletaAus1);
        athletesAdicionados.add(atletaAus2);
        athletesAdicionados.add(atletaBra1);
        athletesAdicionados.add(atletaBra2);
        athletesAdicionados.add(atletaPrt1);
        athletesAdicionados.add(atletaPrt2);
        athletesAdicionados.add(atletaUsa1);
        athletesAdicionados.add(atletaUsa2);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Colocar a Password encriptada para poder comparar objetos
        PasswordUtils passwordUtils = new PasswordUtils();
        for (Athlete athlete : athletesAdicionados) {
            String passwordEncriptadaAdd = passwordUtils.encriptarPassword(athlete.getPassword());
            athlete.setPassword(passwordEncriptadaAdd);
        }

        // Verificar se foi inserido
        AthleteDao athleteDao = new AthleteDao();
        for (Athlete athlete : athletesAdicionados) {
            Athlete athleteAdded = athleteDao.getAthleteById(athlete.getIdAthlete());
            assertEqualsAthlete(athleteAdded, athlete);
            System.out.println("Atleta " + athlete.getName() + "inserido com sucesso");
        }
        System.out.println("Atletas inseridos com sucesso");

        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        int year = 2024;

        Registration registrationAus1 = new Registration(0, atletaAus1, teamAus, sportAdd, status, year);
        Registration registrationAus2 = new Registration(0, atletaAus2, teamAus, sportAdd, status, year);
        Registration registrationBra1 = new Registration(0, atletaBra1, teamBra, sportAdd, status, year);
        Registration registrationBra2 = new Registration(0, atletaBra2, teamBra, sportAdd, status, year);
        Registration registrationPrt1 = new Registration(0, atletaPrt1, teamPrt, sportAdd, status, year);
        Registration registrationPrt2 = new Registration(0, atletaPrt2, teamPrt, sportAdd, status, year);
        Registration registrationUsa1 = new Registration(0, atletaUsa1, teamUsa, sportAdd, status, year);
        Registration registrationUsa2 = new Registration(0, atletaUsa2, teamUsa, sportAdd, status, year);

        List<Registration> registrationsAdicionadosTeam = new ArrayList<>();
        registrationsAdicionadosTeam.add(registrationAus1);
        registrationsAdicionadosTeam.add(registrationAus2);
        registrationsAdicionadosTeam.add(registrationBra1);
        registrationsAdicionadosTeam.add(registrationBra2);
        registrationsAdicionadosTeam.add(registrationPrt1);
        registrationsAdicionadosTeam.add(registrationPrt2);
        registrationsAdicionadosTeam.add(registrationUsa1);
        registrationsAdicionadosTeam.add(registrationUsa2);

        RegistrationDao rgDao = new RegistrationDao();
        for (Registration reg : registrationsAdicionadosTeam) {
            reg.setIdRegistration(rgDao.addRegistrationTeam(reg));
        }

        for (Athlete athlete : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athlete.getIdAthlete());
            assertEquals(1, regs.size());
            System.out.println("Registo do atleta " + athlete.getName() + " inserido com sucesso");
        }
        System.out.println("Registos inseridos com sucesso");

        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.sportStart(sportAdd.getIdSport(), year, 3);
        assertTrue(result);
        System.out.println("Modalidade Iniciada");

        //Verifica se os registos foram adicionados
        int ExpectedResults = 8;
        List<Registration> registrationsFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athl.getIdAthlete());
            registrationsFound.addAll(regs);
        }
        assertEquals(ExpectedResults, registrationsFound.size());
        System.out.println("Teste dos registos passou");

        //Verificar se os resultados foram adicionados
        int ExpectedResultsResults = 24;
        ResultDao rsDao = new ResultDao();
        List<Result> resultFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Result> results = rsDao.getResultByAthleteJunit(athl.getIdAthlete());
            resultFound.addAll(results);
        }
        assertEquals(ExpectedResultsResults, resultFound.size());
        System.out.println("Teste dos resultados passou");

        //verificar se as medalhas aos atletas foram atribuídas
        List<Medal> medalsFound = new ArrayList<>();
        for (Athlete athlete : athletesAdicionados) {
            List<Medal> medalsById = MedalDao.getMedalsByAthleteId(athlete.getIdAthlete());
            medalsFound.addAll(medalsById);
        }
        assertEquals(ExpectedResults, medalsFound.size());
        System.out.println("Teste das medalhas passou");

        //Verificar se as medalhas ás equipas foram atribuídas
        int expectedCount = 4;
        int medalsCountTeam = 0;
        for (Team team : teamsAdicionadas) {
            medalsCountTeam += MedalDao.getMedalsByTeamId(team.getIdTeam());
        }
        assertEquals(expectedCount, medalsCountTeam);
        System.out.println("Teste das medalhas das equipas passou");

        //Verificar WinnerOlympic
        WinnerOlympicDao wo = new WinnerOlympicDao();
        WinnerOlympic winnersOlympic = wo.getWinnerOlympicById(sportAdd.getIdSport(), year);
        assertEquals(sportAdd.getIdSport(), winnersOlympic.getSport().getIdSport());
        System.out.println("Teste de WinnnerOlympic passou");

        System.out.println("A remover dados inseridos na BD...");

        //Remover WinnerOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removido");

        //Remover os resultados
        for (Result res : resultFound) {
            ResultDao.removeResult(res.getIdResult());
        }
        System.out.println("Resultados removidos");

        //Remover os registos
        for (Registration reg : registrationsAdicionadosTeam) {
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }
        System.out.println("Registos removidos");

        //Remover medals
        for (Medal m : medalsFound) {
            MedalDao.removeMedal(m.getIdMedal());
        }
        System.out.println("Medalhas removidas");

        //Remover winnersOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removidos");

        //Remover os atletas
        for (Athlete at : athletesAdicionados) {
            AthleteDao.removeAthlete(at.getIdAthlete());
        }
        System.out.println("Atletas removidos");

        //Remover as equipas
        for (Team t : teamsAdicionadas) {
            TeamDao.removeTeam(t.getIdTeam());
        }
        System.out.println("Equipas removidas");

        //Remover o Sport
        SportDao.removeSport(sportAdd.getIdSport());
        System.out.println("Sport removido");
    }

    @Test
    void testIniciarModalidadesIndividualMultipleGames() throws SQLException {
        LocalDao ld = new LocalDao();
        //Criar o Sport
        String type = "Individual";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

        String name = "Sport Test Individual Multiple Games";
        String description = "This is a sport for test purposes Individual Multiple Games";
        int minParticipants = 4;
        String scoringMeasure = "Time";
        String oneGame = "Multiple";
        int resultMin = 3000;
        int resultMax = 6000;
        int idStatus = 1;
        String metrica = "Minutos";
        LocalDateTime dataInicio = LocalDateTime.of(2025, 1, 5, 15, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 1, 5, 17, 30);

        Local local = ld.getLocals().stream()
                .filter(l -> l.getIdLocal() == 3)
                .findFirst().orElse(null);

        Sport sportAdd = new Sport(0, type, gender, name, description, minParticipants, scoringMeasure, oneGame, resultMin, resultMax, idStatus, metrica, dataInicio, dataFim, local.getIdLocal());

        sportAdd.setIdSport(SportDao.addSport(sportAdd));

        //Verificar se foi inserido
        SportDao sportDao = new SportDao();
        Sport sportAdded = sportDao.getSportById(sportAdd.getIdSport());
        assertEqualsSport(sportAdd, sportAdded);
        System.out.println("Sport inserido com sucesso");

        //Adicionar atletas

        Country country = CountryDao.getCountries().stream()
                .filter(c -> c.getName().equals("Portugal"))
                .findFirst().orElse(null);

        Athlete atleta1 = new Athlete(0, "Teste123", "João Teste", country, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"), "image1");
        Athlete atleta2 = new Athlete(0, "Teste123", "Dinis Teste", country, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"), "image2");
        Athlete atleta3 = new Athlete(0, "Teste123", "Samuel Teste", country, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"), "image3");
        Athlete atleta4 = new Athlete(0, "Teste123", "Roberto Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"), "image4");
        Athlete atleta5 = new Athlete(0, "Teste123", "Gonçalo Teste", country, gender, 180, 75, java.sql.Date.valueOf("2004-12-30"), "image5");

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atleta1);
        athletesAdicionados.add(atleta2);
        athletesAdicionados.add(atleta3);
        athletesAdicionados.add(atleta4);
        athletesAdicionados.add(atleta5);

        for (Athlete at : athletesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Colocar a Password encriptada para poder comparar objetos
        PasswordUtils passwordUtils = new PasswordUtils();
        for (Athlete athlete : athletesAdicionados) {
            String passwordEncriptadaAdd = passwordUtils.encriptarPassword(athlete.getPassword());
            athlete.setPassword(passwordEncriptadaAdd);
        }

        // Verificar se foi inserido
        AthleteDao athleteDao = new AthleteDao();
        for (Athlete athlete : athletesAdicionados) {
            Athlete athleteAdded = athleteDao.getAthleteById(athlete.getIdAthlete());
            assertEqualsAthlete(athleteAdded, athlete);
            System.out.println("Atleta " + athlete.getName() + "inserido com sucesso");
        }
        System.out.println("Atletas inseridos com sucesso");


        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        int year = 2024;

        Registration registration1 = new Registration(0, atleta1, null, sportAdd, status, year);
        Registration registration2 = new Registration(0, atleta2, null, sportAdd, status, year);
        Registration registration3 = new Registration(0, atleta3, null, sportAdd, status, year);
        Registration registration4 = new Registration(0, atleta4, null, sportAdd, status, year);
        Registration registration5 = new Registration(0, atleta5, null, sportAdd, status, year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);
        registrationsAdicionados.add(registration5);

        RegistrationDao registrationDao = new RegistrationDao();
        for (Registration reg : registrationsAdicionados) {
            reg.setIdRegistration(registrationDao.addRegistrationSolo(reg));
        }

        for (Athlete athlete : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athlete.getIdAthlete());
            assertEquals(1, regs.size());
            System.out.println("Registo do atleta " + athlete.getName() + " inserido com sucesso");
        }
        System.out.println("Registos inseridos com sucesso");

        System.out.println("A gerar ids...");


        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.sportStart(sportAdd.getIdSport(), year, 3);
        assertTrue(result);
        System.out.println("Modalidade Iniciada");

        //Verifica se os registos foram adicionados
        int ExpectedResults = 5;
        List<Registration> registrationsFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Registration> regs = RegistrationDao.getRegistrationByIdAthlete(athl.getIdAthlete());
            registrationsFound.addAll(regs);
        }
        assertEquals(ExpectedResults, registrationsFound.size());
        System.out.println("Teste dos registos passou");

        //Verificar se os resultados foram adicionados
        int ExpectedResultsResults = 20;
        ResultDao rsDao = new ResultDao();
        List<Result> resultFound = new ArrayList<>();
        for (Athlete athl : athletesAdicionados) {
            List<Result> results = rsDao.getResultByAthleteJunit(athl.getIdAthlete());
            resultFound.addAll(results);
        }
        assertEquals(ExpectedResultsResults, resultFound.size());
        System.out.println("Teste dos resultados passou");

        //verificar se as medalhas foram atribuídas
        List<Medal> medalsFound = new ArrayList<>();
        for (Athlete athlete : athletesAdicionados) {
            List<Medal> medalsById = MedalDao.getMedalsByAthleteId(athlete.getIdAthlete());
            medalsFound.addAll(medalsById);
        }
        assertEquals(ExpectedResults, medalsFound.size());
        System.out.println("Teste das medalhas passou");

        //Verificar WinnerOlympic
        WinnerOlympicDao wo = new WinnerOlympicDao();
        WinnerOlympic winnersOlympic = wo.getWinnerOlympicById(sportAdd.getIdSport(), year);
        assertEquals(sportAdd.getIdSport(), winnersOlympic.getSport().getIdSport());
        System.out.println("Teste de WinnnerOlympic passou");

        System.out.println("A remover dados inseridos na BD...");
        //Remover WinnerOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removido");

        //Remover os resultados
        for (Result res : resultFound) {
            ResultDao.removeResult(res.getIdResult());
        }
        System.out.println("Resultados removidos");

        //Remover os registos
        for (Registration reg : registrationsAdicionados) {
            RegistrationDao.removeRegistration(reg.getIdRegistration());
        }
        System.out.println("Registos removidos");

        //Remover medals
        for (Medal m : medalsFound) {
            MedalDao.removeMedal(m.getIdMedal());
        }
        System.out.println("Medalhas removidas");

        //Remover winnersOlympic
        WinnerOlympicDao.removeWinnerOlympic(sportAdd.getIdSport(), year);
        System.out.println("WinnersOlympic removidos");

        //Remover os atletas
        for (Athlete at : athletesAdicionados) {
            AthleteDao.removeAthlete(at.getIdAthlete());
        }
        System.out.println("Atletas removidos");

        //Remover o Sport
        SportDao.removeSport(sportAdd.getIdSport());
        System.out.println("Sport removido");
    }
}