package Utils;

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
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class OpoUtils {
    private static Dotenv dotenv = Dotenv.load();
    private static String authUsername = dotenv.get("API_USERNAME");
    private static String authPassword = dotenv.get("API_PASSWORD");
    private static String authHeader = "Basic " + Base64.getEncoder().encodeToString((authUsername + ":" + authPassword).getBytes());

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

            List<Map<String, Object>> games = (List<Map<String, Object>>) responseJson.get("Clients");
            return new ErrorHandler(true, "Games obtidos com sucesso.", games);
        } catch (IOException | InterruptedException | JsonSyntaxException | IllegalStateException e) {
            return new ErrorHandler(false, "Erro: " + e.getMessage());
        }
    }
}
