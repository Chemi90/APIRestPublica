package org.example;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase que realiza una consulta HTTP para obtener el número de películas registradas
 * en una base de datos de películas para un año específico, asociado al nacimiento de un actor.
 */
public class Consulta5_InfoActorGokuEdad {

    private static final String API_KEY = APIConfig.API_KEY; // Suponiendo que APIConfig es una clase que contiene la API_KEY
    private static final String API_HOST = "moviesminidatabase.p.rapidapi.com";
    private static final String BASE_URL = "https://moviesminidatabase.p.rapidapi.com/movie/byYear/";

    /**
     * Punto de entrada principal del programa. Realiza una solicitud HTTP para obtener la
     * cantidad de películas registradas en un año específico.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     * @throws IOException Si ocurre un error de entrada/salida durante la solicitud HTTP.
     * @throws InterruptedException Si el hilo es interrumpido mientras espera la respuesta.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int year = 1982; // Ejemplo de año, este valor debería ser dinámico según el actor.
        int movieCount = fetchMovieCountByYear(year);

        System.out.println("Cantidad de películas registradas en el año " + year + ": " + movieCount);
    }

    /**
     * Realiza una solicitud HTTP para obtener la cantidad de películas registradas en un año específico.
     *
     * @param year El año para el cual se desea obtener el conteo de películas.
     * @return El número de películas registradas en el año especificado.
     * @throws IOException Si ocurre un error de entrada/salida durante la solicitud HTTP.
     * @throws InterruptedException Si el hilo es interrumpido mientras espera la respuesta.
     */
    private static int fetchMovieCountByYear(int year) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + year + "/"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET() // Usa el método GET para simplificar la llamada
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse.getJSONArray("results").length();
    }
}
