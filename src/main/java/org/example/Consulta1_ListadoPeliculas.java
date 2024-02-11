package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase encargada de consultar una API para obtener información específica sobre las series
 * relacionadas con "Dragon Ball". Se enfoca en mostrar la cantidad total de series de Dragon Ball registradas.
 */
public class Consulta1_ListadoPeliculas {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_KEY = APIConfig.API_KEY; // Asumiendo que APIConfig contiene la clave de la API.
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";
    private static final String BASE_URL = "https://moviesminidatabase.p.rapidapi.com/series/idbyTitle/Dragon%20ball/";

    /**
     * Ejecuta la consulta a la API y muestra la información sobre las series de Dragon Ball.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     * @throws IOException Si ocurre un error de entrada/salida durante la solicitud HTTP.
     * @throws InterruptedException Si la ejecución es interrumpida durante la solicitud HTTP.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET() // Simplificación del método de solicitud
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Convertir la respuesta en un objeto JSON y obtener el arreglo de resultados
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultsArray = jsonResponse.getJSONArray("results");

        // Mostrar la cantidad de series de Dragon Ball registradas
        System.out.println("Cantidad de series de Dragon Ball registradas: " + resultsArray.length());
    }
}