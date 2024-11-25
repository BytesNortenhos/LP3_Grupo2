package controllers;

import Dao.*;
import Models.*;
import controllers.admin.StartSportsController;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IniciarModalidadesTest {
    @Test
    void testIniciarModalidadesIndividual() throws SQLException {
        SportDao spd = new SportDao();
        List<Sport> sports = spd.getSports();
        Sport sportEncontrado = null;
        int idSport = 1;
        int year = 2024;

        //Atualizar o número mínimo de participantes para 4
        int numeroMinimoAntesDoUpdate = 0;
        for (Sport sp : sports) {
            if (sp.getIdSport() == idSport) {
                sportEncontrado = sp;
                numeroMinimoAntesDoUpdate = sp.getMinParticipants();
            }
        }
        sportEncontrado.setMinParticipants(4);
        SportDao.updateSport(sportEncontrado);

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

        List<Athlete> athetesAdicionados = new ArrayList<>();
        athetesAdicionados.add(atleta1);
        athetesAdicionados.add(atleta2);
        athetesAdicionados.add(atleta3);
        athetesAdicionados.add(atleta4);

        for (Athlete at : athetesAdicionados) {
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        Registration registration1 = new Registration(0, atleta1, null, sportEncontrado, status, year);
        Registration registration2 = new Registration(0, atleta2, null, sportEncontrado, status, year);
        Registration registration3 = new Registration(0, atleta3, null, sportEncontrado, status, year);
        Registration registration4 = new Registration(0, atleta4, null, sportEncontrado, status, year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);

        for (Registration reg : registrationsAdicionados) {
            reg.setIdRegistration(RegistrationDao.addRegistrationSolo(reg));
        }

        System.out.println("A gerar ids...");

        //Recolher o número de medalhas antes de iniciar a modalidade
        MedalDao md = new MedalDao();
        List<Medal> medalsAntes = md.getMedals();
        int countMedalsAntes = 0;
        for (Medal mdl : medalsAntes) {
            if (mdl.getYear() == year) {
                for (Athlete at : athetesAdicionados) {
                    if (mdl.getAthlete().getIdAthlete() == at.getIdAthlete()) {
                        countMedalsAntes++;
                    }
                }
            }
        }


        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.iniciarModalidades(sportEncontrado.getIdSport(), year);
        assertTrue(result);


        if (result) {
            System.out.println("Modalidade Iniciada");
            //Verificar se os resultados foram adicionados
            List<Result> results = ResultDao.getResults();
            List<Result> resultsEncontrados = new ArrayList<>();
            int countRes = 0;

            for (Result res : results) {
                if (res.getSport().getIdSport() == idSport && (res.getDate().getYear() + 1900) == year) {
                    for (Athlete at : athetesAdicionados) {
                        if (res.getAthlete().getIdAthlete() == at.getIdAthlete()) {
                            resultsEncontrados.add(res);
                            countRes++;
                        }
                    }
                }
            }

            int countResEsperado = resultsEncontrados.size();
            System.out.println("Esperado: " + countResEsperado + "vs" + countRes);
            assertEquals(countResEsperado, countRes);
            System.out.println("Teste dos resultados passou");

            //Verificar se as medalhas foram atribuídas
            List<Medal> medalsDepois = md.getMedals();
            List<Medal> medalsEncontradas = new ArrayList<>();
            int countMedalsDepois = 0;
            for (Medal mdlD : medalsDepois) {
                if (mdlD.getYear() == year) {
                    for (Athlete at : athetesAdicionados) {
                        if (mdlD.getAthlete().getIdAthlete() == at.getIdAthlete()) {
                            countMedalsDepois++;
                            medalsEncontradas.add(mdlD);
                        }
                    }
                }
            }
            int expectedCount = countMedalsAntes + (countMedalsDepois - countMedalsAntes);
            System.out.println("Esperado: " + expectedCount + "vs" + countMedalsDepois);
            assertEquals(expectedCount, countMedalsDepois);
            System.out.println("Teste das medalhas passou");

            //Remover os resultados
            for (Result res : resultsEncontrados) {
                ResultDao.removeResult(res.getIdResult());
            }

            //Remover os registos
            for (Registration reg : registrationsAdicionados) {
                RegistrationDao.removeRegistration(reg.getIdRegistration());
            }

            //Remover medals
            for (Medal m : medalsEncontradas) {
                MedalDao.removeMedal(m.getIdMedal());
            }

            //Remover os atletas
            for (Athlete at : athetesAdicionados) {
                AthleteDao.removeAthlete(at.getIdAthlete());
            }

            sportEncontrado.setMinParticipants(numeroMinimoAntesDoUpdate);
            SportDao.updateSport(sportEncontrado);
        }
    }

    @Test
    void testIniciarModalidadesCollective() throws SQLException {
        SportDao spd = new SportDao();
        List<Sport> sports = spd.getSports();
        Sport sportEncontrado = null;
        int idSport = 6;
        int year = 2024;

        //Atualizar o número mínimo de participantes para 4
        int numeroMinimoAntesDoUpdate = 0;
        for (Sport sp : sports) {
            if (sp.getIdSport() == idSport) {
                sportEncontrado = sp;
                numeroMinimoAntesDoUpdate = sp.getMinParticipants();
            }
        }
        sportEncontrado.setMinParticipants(4);
        SportDao.updateSport(sportEncontrado);

        //Adicionar equipas
        String nameAus = "Australia Men's 4x100m Freestyle Relay Team";
        String nameBra = "Brazil Men's 4x100m Freestyle Relay Team";
        String namePrt = "Portugal Men's 4x100m Freestyle Relay Team";
        String nameUsa = "Usa men's 4x100m Freestyle Relay Team";

        Gender gender = GenderDao.getGenders().stream()
                .filter(g -> g.getDesc().equals("Male"))
                .findFirst().orElse(null);

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



        Team teamAus = new Team(0,nameAus, countryAus, gender, sportEncontrado, 2000,2,8);
        Team teamBra = new Team(0,nameBra, countryBra, gender, sportEncontrado, 2000,2,8);
        Team teamPrt = new Team(0,namePrt, countryPrt, gender, sportEncontrado, 2000,2,8);
        Team teamUsa = new Team(0,nameUsa, countryUsa, gender, sportEncontrado, 2000,2,8);

        List<Team> teamsAdicionadas = new ArrayList<>();
        teamsAdicionadas.add(teamAus);
        teamsAdicionadas.add(teamBra);
        teamsAdicionadas.add(teamPrt);
        teamsAdicionadas.add(teamUsa);

        TeamDao td = new TeamDao();
        for(Team t: teamsAdicionadas){
            t.setIdTeam(td.addTeam(t));
        }

        //Registar atletas para as equipas

        Athlete atletaAus1 = new Athlete(0, "Teste123", "João Teste", countryAus, gender, 195, 90, java.sql.Date.valueOf("2000-08-22"));
        Athlete atletaAus2 = new Athlete(0, "Teste123", "Dinis Teste", countryAus, gender, 175, 65, java.sql.Date.valueOf("2004-04-30"));
        Athlete atletaBra1 = new Athlete(0, "Teste123", "Samuel Teste", countryBra, gender, 170, 70, java.sql.Date.valueOf("2005-06-24"));
        Athlete atletaBra2 = new Athlete(0, "Teste123", "Roberto Teste", countryBra, gender, 180, 75, java.sql.Date.valueOf("2004-02-16"));
        Athlete atletaPrt1 = new Athlete(0, "Teste123", "Gonçalo Teste", countryPrt, gender, 155, 59, java.sql.Date.valueOf("2000-03-22"));
        Athlete atletaPrt2 = new Athlete(0, "Teste123", "Pedro Teste", countryPrt, gender, 179, 80, java.sql.Date.valueOf("2004-09-30"));
        Athlete atletaUsa1 = new Athlete(0, "Teste123", "Tiago Teste", countryUsa, gender, 183, 86, java.sql.Date.valueOf("2005-10-24"));
        Athlete atletaUsa2 = new Athlete(0, "Teste123", "Rui Teste", countryUsa, gender, 198, 102, java.sql.Date.valueOf("2004-01-16"));

        List<Athlete> athletesAdicionados = new ArrayList<>();
        athletesAdicionados.add(atletaAus1);
        athletesAdicionados.add(atletaAus2);
        athletesAdicionados.add(atletaBra1);
        athletesAdicionados.add(atletaBra2);
        athletesAdicionados.add(atletaPrt1);
        athletesAdicionados.add(atletaPrt2);
        athletesAdicionados.add(atletaUsa1);
        athletesAdicionados.add(atletaUsa2);

        for(Athlete at: athletesAdicionados){
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        Registration registrationAus1 = new Registration(0, atletaAus1, teamAus, sportEncontrado, status , year);
        Registration registrationAus2 = new Registration(0, atletaAus2, teamAus, sportEncontrado, status , year);
        Registration registrationBra1 = new Registration(0, atletaBra1, teamBra, sportEncontrado, status , year);
        Registration registrationBra2 = new Registration(0, atletaBra2, teamBra, sportEncontrado, status , year);
        Registration registrationPrt1 = new Registration(0, atletaPrt1, teamPrt, sportEncontrado, status , year);
        Registration registrationPrt2 = new Registration(0, atletaPrt2, teamPrt, sportEncontrado, status , year);
        Registration registrationUsa1 = new Registration(0, atletaUsa1, teamUsa, sportEncontrado, status , year);
        Registration registrationUsa2 = new Registration(0, atletaUsa2, teamUsa, sportEncontrado, status , year);

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
        for (Registration reg: registrationsAdicionadosTeam){
            reg.setIdRegistration(rgDao.addRegistrationTeam(reg));
        }

        //Recolher o número de medalhas atletas/equipas antes de iniciar a modalidade
        MedalDao md = new MedalDao();
        List<Medal> medalsAntes = md.getMedals();
        int countMedalsAntes = 0;
        for(Medal mdl: medalsAntes){
            if(mdl.getYear() == year){
                for(Athlete at: athletesAdicionados){
                    if(mdl.getAthlete().getIdAthlete() == at.getIdAthlete()){
                        countMedalsAntes++;
                    }
                }
            }
        }

        //Verificar se a modalidade foi iniciada
        StartSportsController startSportsController = new StartSportsController();
        boolean result = startSportsController.iniciarModalidades(sportEncontrado.getIdSport(), year);
        assertTrue(result);

        if(result){
            System.out.println("Modalidade Iniciada");
            //Verificar se os resultados foram adicionados
            List<Result> results = ResultDao.getResults();
            List<Result> resultsEncontrados = new ArrayList<>();
            List<Result> resultsIdEncontrados = new ArrayList<>();

            for(Result res: results){
                if(res.getSport().getIdSport() == idSport && (res.getDate().getYear()+1900) == year){
                    for (Athlete at: athletesAdicionados){
                        if(res.getAthlete().getIdAthlete() == at.getIdAthlete()){
                            resultsEncontrados.add(res);
                        }
                    }
                }
            }

            ResultDao rsDao = new ResultDao();
            for(Result res: results){
               resultsIdEncontrados.add(rsDao.getResultById(res.getIdResult())) ;
            }

            int countResEncontradoEsperado = resultsEncontrados.size();
            int countResIdEncontradoEsperado = resultsIdEncontrados.size();
            System.out.println("Esperado: " + countResEncontradoEsperado + "vs" + countResIdEncontradoEsperado);
            assertEquals(countResEncontradoEsperado, countResIdEncontradoEsperado);
            System.out.println("Teste dos resultados passou");

            //Verificar se as medalhas foram atribuídas
            List<Medal> medalsDepois = md.getMedals();
            List<Medal> medalsEncontradas = new ArrayList<>();
            int countMedalsDepois = 0;
            for(Medal mdlD: medalsDepois){
                if(mdlD.getYear() == year){
                    for(Athlete at: athletesAdicionados){
                        if(mdlD.getAthlete().getIdAthlete() == at.getIdAthlete()){
                            countMedalsDepois++;
                            medalsEncontradas.add(mdlD);
                        }
                    }
                }
            }
            int expectedCount = countMedalsAntes + (countMedalsDepois - countMedalsAntes);
            System.out.println("Esperado: " + expectedCount + "vs" + countMedalsDepois);
            assertEquals(expectedCount, countMedalsDepois);
            System.out.println("Teste das medalhas passou" );



            //Remover os resultados
            for(Result res: resultsEncontrados){
                ResultDao.removeResult(res.getIdResult());
            }

            //Remover os registos
            for(Registration reg: registrationsAdicionadosTeam){
                RegistrationDao.removeRegistration(reg.getIdRegistration());
            }

            //Remover medals
            for(Medal m: medalsEncontradas){
                MedalDao.removeMedal(m.getIdMedal());
            }

            //Remover as equipas
            for(Team t: teamsAdicionadas){
                TeamDao.removeTeam(t.getIdTeam());
            }

            //Remover os atletas
            for(Athlete at: athletesAdicionados){
                AthleteDao.removeAthlete(at.getIdAthlete());
            }

            sportEncontrado.setMinParticipants(numeroMinimoAntesDoUpdate);
            SportDao.updateSport(sportEncontrado);

    }

    }
}
