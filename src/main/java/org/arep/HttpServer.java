package org.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HttpServer {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://omdbapi.com/?t=%S&apikey=1d53bda9";
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String request = "/form";
            String method = "GET";
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    request = inputLine.split(" ")[1];
                    method = inputLine.split(" ")[0];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            String requestedMovie = null;
            //System.out.println("FILE, METHOD: " + request + " " + method);
            if (request.startsWith("/form?") && method.equals("POST")) {
                requestedMovie = request.replace("/form?s=", "");
                outputLine = "HTTP/1.1 200 OK\r\n" +
                        "Content-type: application/json\r\n"+
                        "\r\n"
                        + getMovie(requestedMovie.toLowerCase());
            } else {
                outputLine = htmlGetForm();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Método que obtiene la película ingresada por el usuario, ya sea del caché o del API.
     * @param movie película a buscar
     * @return String correspondiente al la información de la película en formato JSON
     * @throws IOException
     */
    public static String getMovie(String movie) throws IOException {
        String reqMovie = "";
        if (cache.containsKey(movie)) {
            reqMovie = cache.get(movie);
            //System.out.println("Película " + movie + " recuperada.");
            return reqMovie;
        }
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
            cache.put(movie, reqMovie);
            //System.out.println("Película " + movie + " guardada.");
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return reqMovie;
    }

    public static String jsonSimple() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: application/json\r\n"+
                "\r\n" +
                "{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"Rated\":\"PG-13\",\"Released\":\"05 May 2017\",\"Runtime\":\"136 min\",\"Genre\":\"Action, Adventure, Comedy\",\"Director\":\"James Gunn\",\"Writer\":\"James Gunn, Dan Abnett, Andy Lanning\",\"Actors\":\"Chris Pratt, Zoe Saldana, Dave Bautista\",\"Plot\":\"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.\",\"Language\":\"English\",\"Country\":\"United States\",\"Awards\":\"Nominated for 1 Oscar. 15 wins & 60 nominations total\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"85%\"},{\"Source\":\"Metacritic\",\"Value\":\"67/100\"}],\"Metascore\":\"67\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"687,044\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"DVD\":\"22 Aug 2017\",\"BoxOffice\":\"$389,813,101\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
    }

    public static String htmlGetForm() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Búsqueda</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "      @import url(\"https://fonts.googleapis.com/css?family=Roboto&display=swap\");\n" +
                "\n" +
                "      * {\n" +
                "        font-family: \"Roboto\", sans-serif;\n" +
                "        background-color: #f5f6fa;\n" +
                "      }\n" +
                "\n" +
                "      h1 {\n" +
                "        padding: 5px;\n" +
                "        margin: 15px 0px;\n" +
                "      }\n" +
                "\n" +
                "      .form {\n" +
                "        padding: 5px;\n" +
                "      }\n" +
                "\n" +
                "      .form label {\n" +
                "        margin: 5px 2px;\n" +
                "      }\n" +
                "\n" +
                "      .form input {\n" +
                "        margin: 5px 2px;\n" +
                "        padding: 8px;\n" +
                "        font-size: 15px;\n" +
                "        border-radius: 3px;\n" +
                "        border: 1px solid rgba(0, 0, 0, 0);\n" +
                "        box-shadow: 0 6px 10px 0 rgba(0, 0, 0 , .15);\n" +
                "        transition: all 200ms ease;\n" +
                "      }\n" +
                "\n" +
                "      .form input:hover {\n" +
                "        border: 1px solid rgba(0, 0, 0, 0.281);\n" +
                "        box-shadow: 0 6px 10px 0 rgba(0, 0, 0 , .22);\n" +
                "      }\n" +
                "\n" +
                "      .form input:focus {\n" +
                "        outline: none !important;\n" +
                "        border: 1px solid #6c7ff2;\n" +
                "      }\n" +
                "\n" +
                "      .btn {\n" +
                "        color: white;\n" +
                "        background-color: #7f8ff4;\n" +
                "        transition: all 200ms ease;\n" +
                "        cursor: pointer;\n" +
                "      }\n" +
                "\n" +
                "      .btn:hover {\n" +
                "        background-color: #6c7ff2;\n" +
                "      }\n" +
                "\n" +
                "      .container {\n" +
                "        margin: 5px 2px;\n" +
                "        padding: 8px;\n" +
                "      }\n" +
                "\n" +
                "    </style>\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "    <h1>Busca una Película</h1>\n" +
                "    <form class=\"form\" action=\"/form\">\n" +
                "      <label for=\"postname\">Nombre:</label><br>\n" +
                "      <input type=\"text\" id=\"postname\" name=\"s\" value=\"John\" placeholder=\"Ingresa el nombre\" required><br>\n" +
                "      <input class=\"btn\" type=\"button\" value=\"Buscar\" onclick=\"loadPostMsg(postname)\">\n" +
                "    </form>\n" +
                "      <div class=\"container\" id=\"postrespmsg\"></div>\n" +
                "      <script>\n" +
                "        function displayJson(json, div) {\n" +
                "            for (const key of Object.keys(json)) {\n" +
                "                if (key == \"Ratings\") {\n" +
                "                    div.innerHTML += \"Ratings: \"\n" +
                "                    for (const ratingKey of Object.keys(json[key])) {\n" +
                "                        div.innerHTML += json[key][ratingKey][\"Source\"] + \": \" + json[key][ratingKey][\"Value\"] + \", \";\n" +
                "                    }\n" +
                "                    div.innerHTML += \"<br/>\"\n" +
                "                } else {\n" +
                "                    div.innerHTML += key + \": \" + json[key] + \"<br/>\";\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function loadPostMsg(name){\n" +
                "            let movie;\n" +
                "            let url = \"/form?s=\" + name.value;\n" +
                "            fetch (url, {method: 'POST'})\n" +
                "                .then(response => response.json())\n" +
                "                .then(data => {\n" +
                "                    let div = document.getElementById(\"postrespmsg\");\n" +
                "                    div.innerHTML = \"\";\n" +
                "                    console.log(data);\n" +
                "                    displayJson(data, div);\n" +
                "                } /*document.getElementById(\"postrespmsg\").innerHTML = data*/);\n" +
                "          }\n" +
                "      </script>\n" +
                "  </body>\n" +
                "</html>";
    }
}
