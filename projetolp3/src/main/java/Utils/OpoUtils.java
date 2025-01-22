package Utils;

import Dao.SportDao;
import Dao.SportEventDao;
import Models.Sport;
import com.google.gson.JsonSyntaxException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class OpoUtils {
    private static Dotenv dotenv = Dotenv.load();
    private static String authUsername = dotenv.get("API_USERNAME");
    private static String authPassword = dotenv.get("API_PASSWORD");
    private static String authHeader = "Basic " + Base64.getEncoder().encodeToString((authUsername + ":" + authPassword).getBytes());

    /**
     * Get all spectators from the API
     * @return ErrorHandler
     */
    public ErrorHandler getEspectadores() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/client/"))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) return new ErrorHandler(false, "Erro: " + response.body());
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseJson = gson.fromJson(response.body(), type);

            List<Map<String, Object>> clients = (List<Map<String, Object>>) responseJson.get("Clients");
            return new ErrorHandler(true, "Espectadores obtidos com sucesso.", clients);
        } catch (IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }

    /**
     * Ban a spectator from the API
     * @param idEspectador {String} Spectator ID
     * @return ErrorHandler
     */
    public ErrorHandler banEspectador(String idEspectador) {
        if(idEspectador == null || idEspectador.isEmpty()) return new ErrorHandler(false, "ID do espectador não pode ser vazio.");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/client/" + idEspectador))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"Active\": false}"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 202) return new ErrorHandler(false, "Erro: " + response.body());
            return new ErrorHandler(true, "Espectador banido com sucesso.");
        } catch (IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }

    /**
     * Get all tickets from a game
     * @param idGame {String} Game ID
     * @return ErrorHandler
     */
    public ErrorHandler getTickets(String idGame) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/ticket/game/" + idGame))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) return new ErrorHandler(false, "Erro: " + response.body());
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseJson = gson.fromJson(response.body(), type);

            List<Map<String, Object>> tickets = (List<Map<String, Object>>) responseJson.get("TicketInfo");
            return new ErrorHandler(true, "Tickets obtidos com sucesso.", tickets);
        } catch (IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }

    /**
     * Get all games from the API
     * @return ErrorHandler
     */
    public ErrorHandler getGames() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/game/"))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) return new ErrorHandler(false, "Erro: " + response.body());
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseJson = gson.fromJson(response.body(), type);

            List<Map<String, Object>> games = (List<Map<String, Object>>) responseJson.get("Games");
            return new ErrorHandler(true, "Games obtidos com sucesso.", games);
        } catch (IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }

    /**
     * Add a new game to the API
     * @param idSport {int} Sport ID
     * @param eventId {int} Event ID
     * @return ErrorHandler
     */
    public ErrorHandler addNovaProva(int idSport, int eventId) {
        try {
            SportDao sportDao = new SportDao();
            String sportInfo = sportDao.getSportForAPI(idSport);
            System.out.println("> " + sportInfo + ", " + eventId);
            if(sportInfo == null || sportInfo.isEmpty()) return new ErrorHandler(false, "Erro: Modalidade não encontrada.");

            String[] sportInfoArray = sportInfo.split(",");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://services.inapa.com/opo/api/game/"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"StartDate\": \"" + sportInfoArray[0] + "\", \"EndDate\": \"" + sportInfoArray[1] + "\", \"Location\": \"" + sportInfoArray[2] + "\", \"Sport\": \"" + sportInfoArray[3] + "\", \"Capacity\": \"" + sportInfoArray[4] + "\", \"EventId\": " + eventId +  "}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 201) return new ErrorHandler(false, "Erro: " + response.body());
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseJson = gson.fromJson(response.body(), type);

            String gameApiId = (String) responseJson.get("Game");

            SportEventDao sportEventDao = new SportEventDao();
            boolean guidUpdated = sportEventDao.updateGuidApi(gameApiId, eventId);
            if(!guidUpdated) return new ErrorHandler(false, "Erro: Não foi possível atualizar o GUID da prova.");

            return new ErrorHandler(true, "Jogo registado com sucesso.");
        } catch (SQLException | IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }
}
