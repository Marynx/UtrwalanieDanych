import DBUtils.DBConnector;
import Domain.Director;
import Domain.Movie;
import Service.DirectorService;
import Service.MovieService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class MovieServiceTest {
    
    MovieService movieService = null;
    
    DirectorService directorService = null;
    
    @Before
    public void setUpDB() throws SQLException, ClassNotFoundException {
        DBConnector.getConnection();
        DBConnector.executeUpdate(DBConnector.getPrepraredStatement("DROP TABLE IF EXISTS MOVIE;"));
        DBConnector.executeUpdate(DBConnector.getPrepraredStatement("DROP TABLE IF EXISTS DIRECTOR;"));
    
        directorService = new DirectorService();
        movieService = new MovieService();
     
        
        Director director = new Director(1, "Adam", "Dyrek", "Warszawa", Date.valueOf("1980-01-10"));
        Director director1 = new Director(2, "Jan", "Kowalski", "Gdansk", Date.valueOf("1985-04-17"));
        Director director2 = new Director(3, "Anna", "Nowak", "Zakopane", Date.valueOf("1979-02-22"));
      
        directorService.create(director);
        directorService.create(director1);
        directorService.create(director2);
        
        Movie movie = new Movie(1, "Transformers", 210, Date.valueOf("2001-11-15"), 1);
        Movie movie1 = new Movie(2, "Harry Potter", 120, Date.valueOf("2000-05-14"), 2);
        Movie movie2 = new Movie(3, "Smierc w Wenecji", 250, Date.valueOf("2005-10-06"), 1);
        
        movieService.create(movie);
        movieService.create(movie1);
        movieService.create(movie2);
    }
    
    @After
    public void cloneDB() throws SQLException {
        DBConnector.closeConnection();
    }
    
    @Test
    public void testCreateValidMovie() throws SQLException {
        Movie movie = new Movie(4, "Transformers", 210, Date.valueOf("2001-11-15"), 1);
        movieService.create(movie);
        Movie resultMovie=movieService.read(4);
        Assert.assertEquals(movie,resultMovie);
    }
    
    @Test(expected=NullPointerException.class)
    public void testCreateNotValidMovie() throws SQLException {
        Movie movie=null;
        fail(String.valueOf(movieService.create(movie)));
    }
    
    @Test(expected = AssertionError.class)
    public void testCreateNotValidMovieWithIdAlreadyInDb() throws SQLException {
        Movie movie=new Movie(1, "Transformers", 210, Date.valueOf("2001-11-15"), 1);
        fail(String.valueOf(movieService.create(movie)));
    }
    
    @Test
    public void testReadValidMovie() throws SQLException {
        Movie resultMovie=movieService.read(1);
        Assert.assertNotNull(resultMovie.getTitle());
    }
    
    @Test
    public void testReadNotValidMovie() throws SQLException {
        Movie resultMovie=movieService.read(-1);
        Assert.assertNull(resultMovie.getTitle());
    }
    
    @Test
    public void testReadAllMovies() throws SQLException {
        List<Movie> movies=movieService.read_all();
        Movie movie = new Movie(1, "Transformers", 210, Date.valueOf("2001-11-15"), 1);
        Movie movie1 = new Movie(2, "Harry Potter", 120, Date.valueOf("2000-05-14"), 2);
        Movie movie2 = new Movie(3, "Smierc w Wenecji", 250, Date.valueOf("2005-10-06"), 1);
    
        List<Movie> moviesToCheck=new ArrayList<>();
        moviesToCheck.add(movie);
        moviesToCheck.add(movie1);
        moviesToCheck.add(movie2);
        Assert.assertEquals(moviesToCheck,movies);
    }
    
    @Test
    public void testUpdateMovie() throws SQLException {
        Movie movieToUpdate=movieService.read(1);
        String newTitle="New value";
        movieToUpdate.setTitle(newTitle);
        movieService.update(movieToUpdate);
        Assert.assertEquals(newTitle,movieService.read(1).getTitle());
    }
    
    @Test
    public void testUpdateNotValidMovie() throws SQLException {
        Movie movieToUpdate=movieService.read(1);
        String titleBefore=movieToUpdate.getTitle();
        String newTitle="New value";
        movieToUpdate.setTitle(newTitle);
        movieService.update(movieToUpdate);
        Assert.assertNotEquals(titleBefore,movieService.read(1).getTitle());
    }
    
    @Test
    public void testUpdateMovieButNotId() throws SQLException {
        Movie movieToUpdate=movieService.read(1);
        int idBefore=movieToUpdate.getId();
        movieToUpdate.setId(-1);
        movieService.update(movieToUpdate);
        Assert.assertEquals(idBefore,movieService.read(1).getId());
    }
    
    @Test
    public void testDeleteMovie() throws SQLException {
        int sizeBefore=movieService.read_all().size();
        Movie movieToDelete=movieService.read(1);
        movieService.delete(movieToDelete.getId());
        int sizeAfter=movieService.read_all().size();
        Assert.assertNotEquals(sizeBefore,sizeAfter);
        Assert.assertEquals(sizeBefore,sizeAfter+1);
    }
    
    @Test
    public void testDeleteNotValidMovie() throws SQLException {
        int sizeBefore=movieService.read_all().size();
        movieService.delete(-1);
        int sizeAfter=movieService.read_all().size();
        Assert.assertTrue(sizeBefore==sizeAfter);
    }
    
    
    @Test
    public void testCountMovies() throws SQLException {
        int size=movieService.countMovies();
        Assert.assertTrue(movieService.read_all().size()==size);
    }
    
    @Test
    public void testCountMoviesAfterCreatingMovie() throws SQLException {
        int sizeBefore=movieService.countMovies();
        Movie movie = new Movie(4, "Smierc w Wenecji", 250, Date.valueOf("2005-10-06"), 1);
        movieService.create(movie);
        int sizeAfter=movieService.countMovies();
        Assert.assertEquals(sizeBefore+1,sizeAfter);
    }
    
    @Test
    public void testCountMoviesWhereDirectorFromValidCity() throws SQLException {
        String city="Warszawa";
        Assert.assertEquals(2,movieService.countMoviesWhereDirectorFrom(city));
        Movie movie = new Movie(4, "Smierc w Wenecji", 250, Date.valueOf("2005-10-06"), 1);
        movieService.create(movie);
        Assert.assertEquals(3,movieService.countMoviesWhereDirectorFrom(city));
    }
    
    @Test
    public void testCountMoviesWhereDirectorFromNotValidCity() throws SQLException {
        String city = "asadasd";
        Assert.assertEquals(0, movieService.countMoviesWhereDirectorFrom(city));
    }
    
    @Test
    public void testLongestMovie() throws SQLException {
        Movie longestMovie = new Movie(4, "Smierc w Wenecji", 9999999, Date.valueOf("2005-10-06"), 1);
        movieService.create(longestMovie);
        Assert.assertEquals(longestMovie, movieService.longestMovie());
    }
    
    @Test
    public void testLongestMovieAfterAdding() throws SQLException {
        Movie longestMovieBefore=movieService.longestMovie();
        Movie longestMovie = new Movie(4, "Smierc w Wenecji", 9999999, Date.valueOf("2005-10-06"), 1);
        movieService.create(longestMovie);
        Assert.assertNotEquals(longestMovieBefore, movieService.longestMovie());
    }
    
    
    @Test
    public void testOldestMovie() throws SQLException {
        Movie oldestMovie = new Movie(4, "Smierc w Wenecji", 9999999, Date.valueOf("1985-10-06"), 1);
        movieService.create(oldestMovie);
        Assert.assertEquals(oldestMovie, movieService.readOldestMovie());
    }
    
    @Test
    public void testAllMovieLength() throws SQLException {
        List<Movie> movies=movieService.read_all();
        int sum=0;
        for(Movie movie :movies){
            sum+=movie.getLengthInMinutes();
        }
        Assert.assertEquals(sum, movieService.sumAllMovieLength());
    }
    
    @Test
    public void testAllMovieLengthAfterDeleting() throws SQLException {
        List<Movie> movies=movieService.read_all();
        int sumBefore=0;
        for(Movie movie :movies){
            sumBefore+=movie.getLengthInMinutes();
        }
        movieService.delete(1);
        movies=movieService.read_all();
        int sumAfter=0;
        for(Movie movie :movies){
            sumAfter+=movie.getLengthInMinutes();
        }
        Assert.assertNotEquals(sumBefore, sumAfter);
    }
    
}
