package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que consulta una API para obtener información sobre series de televisión, enfocándose
 * en obtener la URL del póster de la primera serie registrada del año 1995.
 */
public class Consulta2_Poster {

    private static final String API_KEY = APIConfig.API_KEY; // Asumimos que APIConfig contiene la clave de la API.
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            String idSerie = obtenerIdSerie();
            if (!idSerie.isEmpty()) {
                imprimirURLPoster(idSerie);
            } else {
                System.out.println("No se encontró la serie en el año especificado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una consulta para obtener el ID de la primera serie "Dragon Ball" registrada en el año 1995.
     *
     * @return El ID de la serie "Dragon Ball" del año 1995, o una cadena vacía si no se encuentra.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    private static String obtenerIdSerie() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/series/byYear/1995/"))
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
     * Realiza una consulta para obtener y mostrar la URL del póster de la serie utilizando su ID.
     *
     * @param idSerie El ID de la serie de la cual se desea obtener la URL del póster.
     * @throws Exception Si ocurre un error durante la solicitud HTTP.
     */
    private static void imprimirURLPoster(String idSerie) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/series/id/" + idSerie + "/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        String bannerUrl = jsonResponse.getJSONObject("results").getString("banner");
        System.out.println("URL del poster: " + bannerUrl);
    }
}