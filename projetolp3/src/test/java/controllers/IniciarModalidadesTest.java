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
    void testIniciarModalidades() throws SQLException {
        SportDao spd = new SportDao();
        List<Sport> sports = spd.getSports();
        Sport sportEncontrado = null;
        int idSport = 1;
        int year = 2024;

        //Atualizar o número mínimo de participantes para 4
        for(Sport sp: sports){
            if(sp.getIdSport() == 1){
                sportEncontrado = sp;
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

        for(Athlete at: athetesAdicionados){
            at.setIdAthlete(AthleteDao.addAthlete(at));
        }

        //Guardar status de registo
        RegistrationStatus status = RegistrationStatusDao.getRegistrationStatuses().stream()
                .filter(s -> s.getIdStatus() == 3)
                .findFirst().orElse(null);

        //Adicionar registo
        Registration registration1 = new Registration(0, atleta1, null, sportEncontrado, status , year);
        Registration registration2 = new Registration(0, atleta2, null, sportEncontrado, status , year);
        Registration registration3 = new Registration(0, atleta3, null, sportEncontrado, status , year);
        Registration registration4 = new Registration(0, atleta4, null, sportEncontrado, status , year);

        List<Registration> registrationsAdicionados = new ArrayList<>();
        registrationsAdicionados.add(registration1);
        registrationsAdicionados.add(registration2);
        registrationsAdicionados.add(registration3);
        registrationsAdicionados.add(registration4);

        for (Registration reg: registrationsAdicionados){
            reg.setIdRegistration(RegistrationDao.addRegistrationSolo(reg));
        }

        //Recolher o número de medalhas antes de iniciar a modalidade
        MedalDao md = new MedalDao();
        List<Medal> medalsAntes = md.getMedals();
        int countMedalsAntes = 0;
        for(Medal mdl: medalsAntes){
            if(mdl.getYear() == year){
                for(Athlete at: athetesAdicionados){
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
            System.out.println("Entrou");
            //Verificar se os resultados foram adicionados
            List<Result> results = ResultDao.getResults();
            List<Result> resultsEncontrados = new ArrayList<>();
            int countRes = 0;

            for(Result res: results){
                if(res.getSport().getIdSport() == idSport && (res.getDate().getYear()+1900) == year){
                    for (Athlete at: athetesAdicionados){
                        if(res.getAthlete().getIdAthlete() == at.getIdAthlete()){
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
            for(Medal mdlD: medalsDepois){
                if(mdlD.getYear() == year){
                    for(Athlete at: athetesAdicionados){
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
            for(Registration reg: registrationsAdicionados){
                RegistrationDao.removeRegistration(reg.getIdRegistration());
            }

            //Remover medals
            for(Medal m: medalsEncontradas){
                MedalDao.removeMedal(m.getIdMedal());
            }

            //Remover os atletas
            for(Athlete at: athetesAdicionados){
                AthleteDao.removeAthlete(at.getIdAthlete());
            }
        }
    }
}
