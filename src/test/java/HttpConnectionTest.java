import org.arep.HttpServer;
import org.junit.*;

import java.io.IOException;

public class HttpConnectionTest {

    public HttpConnectionTest() {

    }

    @Test
    public void shouldSearchMovie() {
        try {
            String search = HttpServer.getMovie("Guardians of the Galaxy Vol 2");
            String guardians = "{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"Rated\":\"PG-13\",\"Released\":\"05 May 2017\",\"Runtime\":\"136 min\",\"Genre\":\"Action, Adventure, Comedy\",\"Director\":\"James Gunn\",\"Writer\":\"James Gunn, Dan Abnett, Andy Lanning\",\"Actors\":\"Chris Pratt, Zoe Saldana, Dave Bautista\",\"Plot\":\"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.\",\"Language\":\"English\",\"Country\":\"United States\",\"Awards\":\"Nominated for 1 Oscar. 15 wins & 60 nominations total\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"85%\"},{\"Source\":\"Metacritic\",\"Value\":\"67/100\"}],\"Metascore\":\"67\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"687,044\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"DVD\":\"22 Aug 2017\",\"BoxOffice\":\"$389,813,101\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(guardians, search);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldSaveInCache() {
        try {
            String search = HttpServer.getMovie("avatar");
            HttpServer.getMovie("guardians");
            HttpServer.getMovie("iron man");
            HttpServer.getMovie("batman");
            HttpServer.getMovie("elite");
            String avatar = "{\"Title\":\"Avatar\",\"Year\":\"2009\",\"Rated\":\"PG-13\",\"Released\":\"18 Dec 2009\",\"Runtime\":\"162 min\",\"Genre\":\"Action, Adventure, Fantasy\",\"Director\":\"James Cameron\",\"Writer\":\"James Cameron\",\"Actors\":\"Sam Worthington, Zoe Saldana, Sigourney Weaver\",\"Plot\":\"A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.\",\"Language\":\"English, Spanish\",\"Country\":\"United States\",\"Awards\":\"Won 3 Oscars. 89 wins & 131 nominations total\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.9/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"82%\"},{\"Source\":\"Metacritic\",\"Value\":\"83/100\"}],\"Metascore\":\"83\",\"imdbRating\":\"7.9\",\"imdbVotes\":\"1,313,610\",\"imdbID\":\"tt0499549\",\"Type\":\"movie\",\"DVD\":\"22 Apr 2010\",\"BoxOffice\":\"$785,221,649\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertTrue(HttpServer.cache.containsKey("avatar"));
            Assert.assertEquals(search, avatar);
            Assert.assertEquals(5, HttpServer.cache.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
