import DBUtils.DBConnector;
import Domain.Movie;
import Service.MovieService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBConnector.getConnection();
       MovieService movieService=new MovieService();
       Movie movie =new Movie(1,"Transformers",210,Date.valueOf("2001-11-15"),1);
       movieService.create(movie);
       Movie movie1=movieService.read(1);
        System.out.println(movie1);
        movieService.update(new Movie("dddd",212,Date.valueOf("1999-01-15"),4),1);
        movie1=movieService.read(1);
        System.out.println(movie1);
        movie=new Movie(2,"Transformers",210,Date.valueOf("2001-11-15"),1);
        movieService.create(movie);
        List<Movie>movies=movieService.read_all();
        for(Movie mov:movies){
            System.out.println(mov);
        }
        System.out.println(movieService.countMovies());
        movieService.delete(1);
        
       movieService.drop_table();
       DBConnector.closeConnection();
    }
}
