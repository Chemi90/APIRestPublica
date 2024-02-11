package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase dedicada a realizar consultas sobre la película "Dragonball Evolution" y obtener información específica
 * sobre el actor que interpreta a Goku. Incluye métodos para buscar la película por nombre, obtener el actor
 * que interpreta a Goku y finalmente recuperar detalles sobre dicho actor.
 */
public class Consulta4_InfoActorGoku {

    private static final String API_KEY = APIConfig.API_KEY; // Supongamos que APIConfig es una clase con la clave API
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            String movieId = obtenerPeliculaPorNombre();
            if (!movieId.isEmpty()) {
                String actorId = obtenerActorGoku(movieId);
                if (!actorId.isEmpty()) {
                    obtenerDatosActor(actorId);
                } else {
                    System.out.println("No se pudo encontrar el actor de Goku.");
                }
            } else {
                System.out.println("No se encontraron resultados para la película.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID de la película "Dragonball Evolution" por su nombre.
     *
     * @return El ID de la película, o una cadena vacía si no se encuentra.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    public static String obtenerPeliculaPorNombre() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/movie/imdb_id/byTitle/Dragonball%20Evolution/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        JSONArray resultsArray = jsonResponse.getJSONArray("results");
        if (resultsArray.length() > 0) {
            JSONObject movieInfo = resultsArray.getJSONObject(0);
            String movieId = movieInfo.getString("imdb_id");
            System.out.println("ID de la película: " + movieId);
            return movieId;
        }
        return "";
    }

    /**
     * Obtiene el ID del actor que interpreta a Goku en la película especificada por su ID.
     *
     * @param movieId El ID de la película.
     * @return El ID del actor de Goku, o una cadena vacía si no se encuentra.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    public static String obtenerActorGoku(String movieId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/movie/id/" + movieId + "/cast/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        JSONArray rolesArray = jsonResponse.getJSONObject("results").getJSONArray("roles");
        for (int i = 0; i < rolesArray.length(); i++) {
            JSONObject role = rolesArray.getJSONObject(i);
            if ("Goku".equalsIgnoreCase(role.getString("role"))) {
                String actorId = role.getJSONObject("actor").getString("imdb_id");
                System.out.println("ID del actor de Goku: " + actorId);
                return actorId;
            }
        }
        return "";
    }

    /**
     * Obtiene y muestra información sobre el actor especificado por su ID.
     *
     * @param actorId El ID del actor.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    public static void obtenerDatosActor(String actorId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/actor/id/" + actorId + "/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        String birthPlace = jsonResponse.getJSONObject("results").getString("birth_place");
        String birthDate = jsonResponse.getJSONObject("results").getString("birth_date");
        String starSign = jsonResponse.getJSONObject("results").getString("star_sign");

        System.out.println("Lugar de nacimiento: " + birthPlace);
        System.out.println("Fecha de nacimiento: " + birthDate);
        System.out.println("Signo del zodiaco: " + starSign);
    }
}