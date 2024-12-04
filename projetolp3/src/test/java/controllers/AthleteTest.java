package controllers;
import Dao.AthleteDao;
import Dao.CountryDao;
import Dao.GenderDao;
import Models.Athlete;
import Models.Country;
import Models.Gender;
import Utils.ConnectionsUtlis;
import Utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {


    @Test
    void testCrudAthlete() throws SQLException {
        try (Connection connection = ConnectionsUtlis.dbConnect()) {
            // Configuração dos dados
            String password = "Teste123";
            String name = "Joao Kobi";
            int height = 195;
            float weight = 90;
            Date dateOfBirth = java.sql.Date.valueOf("2023-11-13");
            String img = "imagem1";

            Gender gender = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Male"))
                    .findFirst().orElse(null);

            Country country = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Portugal"))
                    .findFirst().orElse(null);

            Athlete athlete = new Athlete(0, password, name, country, gender, height, weight, dateOfBirth, img);

            athlete.setIdAthlete(AthleteDao.addAthlete(athlete));

            //Colocar a Password encriptada para poder comparar objetos
            PasswordUtils passwordUtils = new PasswordUtils();
            String passwordEncriptadaAdd = passwordUtils.encriptarPassword(athlete.getPassword());
            athlete.setPassword(passwordEncriptadaAdd);


            // Verificar se foi inserido
            AthleteDao athleteDao = new AthleteDao();
            Athlete athleteAdded = athleteDao.getAthleteById(athlete.getIdAthlete());

            assertEqualsAthlete(athleteAdded, athlete);
            System.out.println("Atleta inserido com sucesso");

            //Dados para atualizar
            String passwordUpdate = "123Teste";
            String nameUpdate = "Daniela Kobi";
            int heightUpdate = 100;
            float weightUpdate = 33;
            Date dateOfBirthUpdate = java.sql.Date.valueOf("2023-02-12");
            String imgUpdate = "imagem2";

            Gender genderUpdate = GenderDao.getGenders().stream()
                    .filter(g -> g.getDesc().equals("Female"))
                    .findFirst().orElse(null);

            Country countryUpdate = CountryDao.getCountries().stream()
                    .filter(c -> c.getName().equals("Angola"))
                    .findFirst().orElse(null);

            Athlete athleteUp = new Athlete(athlete.getIdAthlete(), passwordUpdate, nameUpdate, countryUpdate, genderUpdate, heightUpdate, weightUpdate, dateOfBirthUpdate, imgUpdate);

            AthleteDao.updateAthlete(athleteUp);
            String passwordEncriptadaUp = passwordUtils.encriptarPassword(athleteUp.getPassword());
            athleteUp.setPassword(passwordEncriptadaUp);


            Athlete athleteUpdated = athleteDao.getAthleteById(athleteUp.getIdAthlete());

            assertEqualsAthlete(athleteUpdated, athleteUp);
            System.out.println("Atleta atualizado com sucesso");

            AthleteDao.removeAthlete(athlete.getIdAthlete());
            System.out.println("Atleta removido com sucesso");
        }
    }

    public static void assertEqualsAthlete(Athlete expected, Athlete actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        // Compara os atributos primitivos e strings diretamente
        assertEquals(expected.getIdAthlete(), actual.getIdAthlete(), "ID do atleta não é igual");
        assertEquals(expected.getName(), actual.getName(), "Nome do atleta não é igual");
        assertEquals(expected.getHeight(), actual.getHeight(), "Altura do atleta não é igual");
        assertEquals(expected.getWeight(), actual.getWeight(), "Peso do atleta não é igual");
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth(), "Data de nascimento do atleta não é igual");
        assertEquals(expected.getImage(), actual.getImage(), "Imagem do atleta não é igual");

        // Compara os objetos Country
        assertNotNull(expected.getCountry(), "O país do atleta não pode ser nulo");
        assertNotNull(actual.getCountry(), "O país do atleta não pode ser nulo");
        assertEquals(expected.getCountry().getName(), actual.getCountry().getName(), "Nome do país não é igual");

        // Compara os objetos Gender
        assertNotNull(expected.getGenre(), "O gênero do atleta não pode ser nulo");
        assertNotNull(actual.getGenre(), "O gênero do atleta não pode ser nulo");
        assertEquals(expected.getGenre().getDesc(), actual.getGenre().getDesc(), "Descrição do gênero não é igual");
    }
}