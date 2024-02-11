package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase encargada de consultar información específica de una serie de televisión desde una API.
 * Se enfoca en obtener el título y la descripción del primer episodio de la tercera temporada de
 * "Dragon Ball", conocida como la Saga del Ejército de la Patrulla Roja.
 */
public class Consulta3_InfoCap1Temp3 {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_KEY = APIConfig.API_KEY; // Asumimos que APIConfig es una clase con la clave API.
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";

    public static void main(String[] args) {
        try {
            String seriesId = obtenerIdSerie();
            if (!seriesId.isEmpty()) {
                String episodioInfo = obtenerDatosSerie(seriesId);
                System.out.println(episodioInfo);
            } else {
                System.out.println("No se encontró el ID de la serie.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID de la serie "Dragon Ball" a través de una solicitud HTTP.
     *
     * @return El ID de IMDb de la serie "Dragon Ball" o una cadena vacía si no se encuentra.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    private static String obtenerIdSerie() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/series/idbyTitle/Dragon%20Ball/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray jsonArray = jsonResponse.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject serie = jsonArray.getJSONObject(i);
            if ("Dragon Ball".equalsIgnoreCase(serie.getString("title"))) {
                return serie.getString("imdb_id");
            }
        }

        return "";
    }

    /**
     * Realiza una consulta para obtener información sobre el primer episodio de la tercera temporada de la serie.
     *
     * @param seriesId El ID de IMDb de la serie.
     * @return Una descripción detallada del primer episodio de la tercera temporada o un mensaje de error.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    private static String obtenerDatosSerie(String seriesId) throws Exception {
        String temporada = "3";
        String episodio = "1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/series/id/" + seriesId +
                        "/season/" + temporada + "/episode/" + episodio + "/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Aquí se asume que la respuesta contiene la información deseada.
        // En un caso real, deberías procesar esta respuesta para extraer y devolver los detalles específicos del episodio.
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse.toString(2); // Devuelve la respuesta JSON formateada para facilitar la lectura.
    }
}