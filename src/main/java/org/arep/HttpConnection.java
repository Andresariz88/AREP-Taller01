package org.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase encargada de conectarse con la API de OMDb
 */
public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://omdbapi.com/?s=%S&apikey=1d53bda9";

    /**
     * Método que obtiene la película ingresada por el usuario
     * @param movie película a buscar
     * @return String correspondiente al la información de la película en formato JSON
     * @throws IOException
     */
    public static String getMovie(String movie) throws IOException {
        String reqMovie = "";
        String formatted = String.format(GET_URL, movie);
        URL obj = new URL(formatted);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            reqMovie = response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return reqMovie;
    }
}
