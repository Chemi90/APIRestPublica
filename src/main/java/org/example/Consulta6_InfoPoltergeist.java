package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

/**
 * Clase encargada de realizar consultas a una base de datos de películas para obtener información
 * específica sobre la película "Poltergeist" del año 1982 y sus premios asociados.
 */
public class Consulta6_InfoPoltergeist {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://moviesminidatabase.p.rapidapi.com";
    private static final String API_KEY = APIConfig.API_KEY;
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";

    /**
     * Método principal que inicia la consulta de información sobre la película "Poltergeist".
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            String movieId = fetchMovieId("Poltergeist", 1982);
            System.out.println(movieId);
            if (!movieId.isEmpty()) {
                String awards = fetchMovieAwards(movieId);
                System.out.println(awards);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una consulta HTTP para obtener el ID de una película por título y año.
     *
     * @param title Título de la película.
     * @param year Año de lanzamiento de la película.
     * @return El ID de la película en IMDb o una cadena vacía si no se encuentra.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws InterruptedException Si la operación es interrumpida.
     */
    public static String fetchMovieId(String title, int year) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/movie/byYear/" + year + "/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray resultsArray = new JSONObject(response.body()).getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movie = resultsArray.getJSONObject(i);
            if (title.equals(movie.getString("title"))) {
                String movieId = movie.getString("imdb_id");
                System.out.println("ID de la película '" + title + "': " + movieId);
                return movieId;
            }
        }
        return "";
    }

    /**
     * Obtiene información sobre los premios recibidos por una película dado su ID.
     *
     * @param movieId El ID de la película.
     * @return Detalles sobre los premios recibidos por la película o una cadena vacía si no se encuentra información.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws InterruptedException Si la operación es interrumpida.
     */
    public static String fetchMovieAwards(String movieId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/movie/id/" + movieId + "/awards/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Este ejemplo simplemente imprime la respuesta JSON con formato,
        // podrías procesar esta información para devolver una cadena más descriptiva.
        System.out.println(new JSONObject(response.body()).toString(2));
        return response.body();
    }
}
