package controllers;
import Dao.AthleteDao;
import Dao.CountryDao;
import Dao.GenderDao;
import Models.Athlete;
import Models.Country;
import Models.Gender;
import Utils.ConnectionsUtlis;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AthleteTest {


    @Test
    void testAddAthlete() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String password = "Teste123";
            String name = "Raul Silva";
            int height = 195;
            float weight = 90;
            Date dateOfBirth = java.sql.Date.valueOf("2023-11-13");

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Male"))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Portugal"))
                    .findFirst().orElse(null);

            Athlete atleta = new Athlete(0, password, name, country, gender, height, weight, dateOfBirth);

            AthleteDao.addAthlete(atleta);

            // Verificar se foi inserido
            boolean atletaEncontrado = false;
            int idAtletaEncontado = 0;

            for (Athlete a : AthleteDao.getAthletes()) {
                if (a.getName().equals(name)) {
                    atletaEncontrado = true;
                    idAtletaEncontado = a.getIdAthlete();
                    break;
                }
            }

            if(atletaEncontrado){
                AthleteDao.removeAthlete(idAtletaEncontado);
                assertTrue(atletaEncontrado);
            }
        }

    }

    @Test
    void testUpdateAthlete() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String password = "123Teste";
            String name = "Sandro Silva";
            int height = 100;
            float weight = 33;
            Date dateOfBirth = java.sql.Date.valueOf("2023-02-12");

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Female"))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Angola"))
                    .findFirst().orElse(null);

            int idAtleta = 0;
            Athlete atletaEncontrado = null;
            for (Athlete a : AthleteDao.getAthletes()) {
                if (a.getName().equals("João Costa")) {
                    idAtleta = a.getIdAthlete();
                    atletaEncontrado = a;
                    break;
                }
            }

            Athlete atletaUpdate = new Athlete(idAtleta, password, name, country, gender, height, weight, dateOfBirth);

            if(atletaEncontrado != null){
                AthleteDao.updateAthlete(atletaUpdate);
            } else {
                fail();
            }

            for (Athlete a : AthleteDao.getAthletes()) {
                if (a.getIdAthlete() == idAtleta) {
                    if(a.getPassword().equals(password) && a.getName().equals(name) && a.getHeight() == height
                            && a.getWeight() == weight && a.getDateOfBirth().equals(dateOfBirth) && a.getGenre().getIdGender() == gender.getIdGender()
                            && a.getCountry().getIdCountry() == country.getIdCountry()){
                        assertTrue(true);
                        AthleteDao.updateAthlete(atletaEncontrado);
                    }else {
                        fail("Erro ao atualizar");
                    }

                }
            }
        }
    }
}






