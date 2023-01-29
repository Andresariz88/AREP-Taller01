import org.arep.HttpConnection;
import org.arep.HttpServer;
import org.junit.*;

import java.io.IOException;

public class HttpConnectionTest {

    public HttpConnectionTest() {

    }

    @Test
    public void deberiaRetornarPelicula() {
        try {
            String busqueda = HttpServer.getMovie("Guardians of the Galaxy Vol. 2");
            String guardians = "{\"Search\":[{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\"},{\"Title\":\"Bonus Round: The Making of 'Guardians of the Galaxy Vol. 2'\",\"Year\":\"2017\",\"imdbID\":\"tt7312152\",\"Type\":\"movie\",\"Poster\":\"N/A\"},{\"Title\":\"Guardians of the Galaxy Vol. 2 (2017)\",\"Year\":\"2017\",\"imdbID\":\"tt17977702\",\"Type\":\"movie\",\"Poster\":\"N/A\"},{\"Title\":\"Chris Pratt Shows You Around the Set of Guardians of the Galaxy Vol. 2\",\"Year\":\"2016\",\"imdbID\":\"tt5630492\",\"Type\":\"movie\",\"Poster\":\"N/A\"}],\"totalResults\":\"4\",\"Response\":\"True\"}";
            Assert.assertEquals(guardians, busqueda);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
