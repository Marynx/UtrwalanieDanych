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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class DirectorServiceTest {
    
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
    public void testCreateValidDirector() throws SQLException {
        Director director = new Director(4, "Anna", "Nowak", "Zakopane", Date.valueOf("1979-02-22"));
        directorService.create(director);
        Director addedDirector=directorService.read(4);
        Assert.assertEquals(director,addedDirector);
    }
    
    @Test(expected=NullPointerException.class)
    public void testCreateNotValidDirector() throws SQLException {
        Director director=null;
        fail(String.valueOf(directorService.create(director)));
    }
    
    @Test(expected = AssertionError.class)
    public void testCreateNotValidDirectorWithIdAlreadyInDb() throws SQLException {
        Director director = new Director(1, "Adam", "Dyrek", "Warszawa", Date.valueOf("1980-01-10"));
        fail(String.valueOf(directorService.create(director)));
    }
    
    @Test
    public void testReadValidDirector() throws SQLException {
        Director resultDirector=directorService.read(1);
        Assert.assertNotNull(resultDirector.getFirstName());
    }
    
    @Test
    public void testReadNotValidDirector() throws SQLException {
        Director resultDirector=directorService.read(-1);
        Assert.assertNull(resultDirector.getFirstName());
    }
    
    @Test
    public void testReadAllDirectors() throws SQLException {
        List<Director> directors=directorService.read_all();
        Director director = new Director(1, "Adam", "Dyrek", "Warszawa", Date.valueOf("1980-01-10"));
        Director director1 = new Director(2, "Jan", "Kowalski", "Gdansk", Date.valueOf("1985-04-17"));
        Director director2 = new Director(3, "Anna", "Nowak", "Zakopane", Date.valueOf("1979-02-22"));
        List<Director> directorsToCheck=new ArrayList<>();
        directorsToCheck.add(director);
        directorsToCheck.add(director1);
        directorsToCheck.add(director2);
        Assert.assertEquals(directorsToCheck,directors);
    }
    
    @Test
    public void testUpdateDirector() throws SQLException {
        Director directorToUpdate=directorService.read(1);
        String newFirstName="Andrzej";
        directorToUpdate.setFirstName(newFirstName);
        directorService.update(directorToUpdate);
        Assert.assertEquals(newFirstName,directorService.read(1).getFirstName());
    }
    
    @Test
    public void testUpdateNotValidDirector() throws SQLException {
        Director directorToUpdate=directorService.read(1);
        String firstNameBefore=directorToUpdate.getFirstName();
        String newFirstName="Andrzej";
        directorToUpdate.setFirstName(newFirstName);
        directorService.update(directorToUpdate);
        Assert.assertNotEquals(firstNameBefore,directorService.read(1).getFirstName());
    }
    
    @Test
    public void testUpdateDirectorButNotId() throws SQLException {
        Director directorToUpdate=directorService.read(1);
        int idBefore=directorToUpdate.getId();
        directorToUpdate.setId(-1);
        directorService.update(directorToUpdate);
        Assert.assertEquals(idBefore,directorService.read(1).getId());
    }
    
    @Test
    public void testDeleteDirector() throws SQLException {
        int sizeBefore=directorService.read_all().size();
        Director directorToDelete=directorService.read(1);
        directorService.delete(directorToDelete.getId());
        int sizeAfter=directorService.read_all().size();
        Assert.assertNotEquals(sizeBefore,sizeAfter);
        Assert.assertEquals(sizeBefore,sizeAfter+1);
    }
    
    @Test
    public void testDeleteNotValidDirector() throws SQLException {
        int sizeBefore=directorService.read_all().size();
        directorService.delete(-1);
        int sizeAfter=directorService.read_all().size();
        Assert.assertTrue(sizeBefore==sizeAfter);
    }
    
    @Test
    public void testReadYoungestDirector() throws SQLException {
        Director youngest=directorService.read(2);
        Assert.assertEquals(youngest,directorService.readYoungestDirector());
    }
    
    @Test
    public void testReadYoungestDirectorFail() throws SQLException {
        Director youngest=directorService.read(1);
        Assert.assertNotEquals(youngest,directorService.readYoungestDirector());
    }
    
    @Test
    public void testCountDirectorMovies() throws SQLException {
        Assert.assertEquals(2,directorService.countDirectorMovies(1));
    }
    
    
    @Test
    public void testCountNotValidDirectorMovies() throws SQLException {
        Assert.assertEquals(0,directorService.countDirectorMovies(-1));
    }
    
    
    @Test
    public void testGetDirectorAge() throws SQLException {
        Director director=directorService.read(1);
        LocalDate ld=LocalDate.now();
        Period age= Period.between(director.getYob().toLocalDate(),ld);
        Assert.assertEquals(age.getYears(),directorService.getDirectorAge(1));
    }
    
    @Test
    public void testGetDirectorAgeAfterUpdate() throws SQLException {
        Director director=directorService.read(1);
        LocalDate ld=LocalDate.now();
        Period ageBefore= Period.between(director.getYob().toLocalDate(),ld);
        director.setYob(Date.valueOf("2017-03-14"));
        directorService.update(director);
        Assert.assertTrue(ageBefore.getYears()>directorService.getDirectorAge(1));
    }
}
