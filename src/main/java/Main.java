import DBUtils.DBConnector;
import Domain.Director;
import Domain.Movie;
import Service.DirectorService;
import Service.MovieService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBConnector.getConnection();
        DBConnector.executeUpdate(DBConnector.getPrepraredStatement("DROP TABLE IF EXISTS MOVIE;"));
        DBConnector.executeUpdate(DBConnector.getPrepraredStatement("DROP TABLE IF EXISTS DIRECTOR;"));
        
        DirectorService directorService = new DirectorService();
        MovieService movieService = new MovieService();
        
        Director director = new Director(1, "Adam", "Dyrek", "Warszawa", Date.valueOf("1980-01-10"));
        Director director1 = new Director(2, "Jan", "Kowalski", "Gdansk", Date.valueOf("1985-04-17"));
        Director director2 = new Director(3, "Anna", "Nowak", "Zakopane", Date.valueOf("1979-02-22"));
        
        directorService.create(director);
        directorService.create(director1);
        directorService.create(director2);
        
        System.out.println(directorService.read(1));
        
        director.setFirstName("Kasia");
        directorService.update(director);
        
        System.out.println(directorService.read(1));
        
        directorService.delete(3);
        
        List<Director> directors = directorService.read_all();
        for ( Director dir : directors ) {
            System.out.println(dir);
        }
        
        System.out.println(directorService.countDirectorMovies(2));
        
        System.out.println(directorService.readYoungestDirector());
        
        System.out.println(directorService.getDirectorAge(1));
        
        Movie movie = new Movie(1, "Transformers", 210, Date.valueOf("2001-11-15"), 1);
        Movie movie1 = new Movie(2, "Harry Potter", 120, Date.valueOf("2000-05-14"), 2);
        Movie movie2 = new Movie(3, "Smierc w Wenecji", 250, Date.valueOf("2005-10-06"), 1);
        
        movieService.create(movie);
        movieService.create(movie1);
        movieService.create(movie2);
        
        System.out.println(movieService.read(1));
        
        movie2.setTitle("Batman");
        movieService.update(movie2);
        
        movieService.delete(2);
        
        List<Movie> movies = movieService.read_all();
        for ( Movie mov : movies ) {
            System.out.println(mov);
        }
        
        System.out.println(movieService.countMovies());
        
        System.out.println(movieService.countMoviesWhereDirectorFrom("Warszawa"));
        
        System.out.println(movieService.readOldestMovie());
        
        System.out.println(movieService.longestMovie());
        
        System.out.println(movieService.sumAllMovieLength());
        
        DBConnector.closeConnection();
    }
    
}
