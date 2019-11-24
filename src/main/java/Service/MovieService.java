package Service;

import DBUtils.DBConnector;
import Domain.Movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    
    private final String CREATE_MOVIE_TABLE = "CREATE TABLE movie (\n" +
                                              "   id INT NOT NULL,\n" +
                                              "   title VARCHAR(50) NOT NULL,\n" +
                                              "   length_in_minutes int NOT NULL,\n" +
                                              "   release_date DATE,\n" +
                                              "   director_id INT, \n" +
                                              "   FOREIGN KEY (director_id) REFERENCES director(id) ON DELETE CASCADE, \n" +
                                              "   PRIMARY KEY (id) \n" +
                                              ");";
    
    private final String DROP_TABLE = "DROP TABLE IF EXISTS MOVIE";
    
    private final String CREATE_MOVIE = "INSERT INTO MOVIE(ID,TITLE,LENGTH_IN_MINUTES,RELEASE_DATE,DIRECTOR_ID) VALUES (?,?,?,?,?)";
    
    private final String UPDATE_MOVIE = "UPDATE MOVIE SET TITLE=(?),LENGTH_IN_MINUTES=(?),RELEASE_DATE=(?),DIRECTOR_ID=(?) WHERE ID=(?)";
    
    private final String READ_MOVIES = "SELECT * FROM MOVIE";
    
    private final String READ_MOVIE_WITH_ID = "SELECT * FROM MOVIE where ID=(?)";
    
    private final String DELETE_MOVIE = "DELETE FROM MOVIE WHERE ID=(?)";
    
    private final String COUNT_MOVIES = "SELECT COUNT(*) FROM MOVIE";
    
    private final String COUNT_MOVIES_WHERE_DIRECTOR_FROM = "SELECT COUNT(*) FROM MOVIE JOIN DIRECTOR ON MOVIE.DIRECTOR_ID=DIRECTOR.ID" +
                                                            " WHERE DIRECTOR.CITY=(?)";
    
    private final String LONGEST_MOVIE = "SELECT * FROM MOVIE WHERE LENGTH_IN_MINUTES=(SELECT MAX(LENGTH_IN_MINUTES) FROM MOVIE)";
    
    private final String READ_OLDEST_MOVIE = "SELECT * FROM MOVIE WHERE RELEASE_DATE=(SELECT MIN(RELEASE_DATE) FROM MOVIE)";
    
    private final String SUM_ALL_MOVIE_LENGTH = "SELECT SUM(LENGTH_IN_MINUTES) FROM MOVIE";
    
    PreparedStatement preparedStatement = null;
    
    public MovieService() throws SQLException {
        
        try {
            DBConnector.connection.setAutoCommit(false);
            preparedStatement = DBConnector.getPrepraredStatement(CREATE_MOVIE_TABLE);
            DBConnector.executeUpdate(preparedStatement);
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        
    }
    
    public int create(Movie movie) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(CREATE_MOVIE);
            preparedStatement.setInt(1, movie.getId());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setInt(3, movie.getLengthInMinutes());
            preparedStatement.setDate(4, movie.getReleaseDate());
            preparedStatement.setInt(5, movie.getDirector());
            result = DBConnector.executeUpdate(preparedStatement);
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Movie read(int id) throws SQLException {
        Movie movie = new Movie();
        try {
            preparedStatement = DBConnector.getPrepraredStatement(READ_MOVIE_WITH_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            DBConnector.connection.commit();
            if ( rs == null ) {
                System.out.println("Movie with id " + id + " not found");
                return null;
            }
            while ( rs.next() ) {
                movie.setId(rs.getInt("MOVIE.ID"));
                movie.setTitle(rs.getString("TITLE"));
                movie.setLengthInMinutes(rs.getInt("LENGTH_IN_MINUTES"));
                movie.setReleaseDate(rs.getDate("RELEASE_DATE"));
                movie.setDirector(rs.getInt("DIRECTOR_ID"));
            }
            return movie;
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Movie> read_all() throws SQLException {
        List<Movie> movies = new ArrayList<Movie>();
        try {
            ResultSet rs = DBConnector.executeQuery(READ_MOVIES);
            DBConnector.connection.commit();
            
            if ( rs == null ) {
                System.out.println("No movies in Database");
                return movies;
            }
            
            while ( rs.next() ) {
                movies.add(new Movie(
                                rs.getInt("ID"),
                                rs.getString("TITLE"),
                                rs.getInt("LENGTH_IN_MINUTES"),
                                rs.getDate("RELEASE_DATE"),
                                rs.getInt("DIRECTOR_ID")
                        )
                );
            }
            
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return movies;
    }
    
    public int delete(int id) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(DELETE_MOVIE);
            preparedStatement.setInt(1, id);
            result = DBConnector.executeUpdate(preparedStatement);
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            System.out.println("Something went wrong!");
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int update(Movie movie) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(UPDATE_MOVIE);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getLengthInMinutes());
            preparedStatement.setDate(3, movie.getReleaseDate());
            preparedStatement.setInt(4, movie.getDirector());
            preparedStatement.setInt(5, movie.getId());
            result = DBConnector.executeUpdate(preparedStatement);
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            System.out.println("Something went wrong!");
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int countMovies() throws SQLException {
        int result = 0;
        try {
            ResultSet rs = DBConnector.executeQuery(COUNT_MOVIES);
            while ( rs.next() ) {
                result = rs.getInt(1);
            }
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int countMoviesWhereDirectorFrom(String city) throws SQLException {
        int result = 0;
        try {
            PreparedStatement preparedStatement = DBConnector.getPrepraredStatement(COUNT_MOVIES_WHERE_DIRECTOR_FROM);
            preparedStatement.setString(1, city);
            ResultSet rs = preparedStatement.executeQuery();
            while ( rs.next() ) {
                result = rs.getInt(1);
            }
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Movie longestMovie() throws SQLException {
        Movie movie = new Movie();
        try {
            ResultSet rs = DBConnector.executeQuery(LONGEST_MOVIE);
            DBConnector.connection.commit();
            while ( rs.next() ) {
                movie.setId(rs.getInt("ID"));
                movie.setTitle(rs.getString("TITLE"));
                movie.setLengthInMinutes(rs.getInt("LENGTH_IN_MINUTES"));
                movie.setReleaseDate(rs.getDate("RELEASE_DATE"));
                movie.setDirector(rs.getInt("DIRECTOR_ID"));
            }
            return movie;
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Movie readOldestMovie() throws SQLException {
        Movie movie = new Movie();
        try {
            ResultSet rs = DBConnector.executeQuery(READ_OLDEST_MOVIE);
            DBConnector.connection.commit();
            while ( rs.next() ) {
                movie.setId(rs.getInt("ID"));
                movie.setTitle(rs.getString("TITLE"));
                movie.setLengthInMinutes(rs.getInt("LENGTH_IN_MINUTES"));
                movie.setReleaseDate(rs.getDate("RELEASE_DATE"));
                movie.setDirector(rs.getInt("DIRECTOR_ID"));
            }
            return movie;
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int sumAllMovieLength() throws SQLException {
        int result = 0;
        try {
            ResultSet rs = DBConnector.executeQuery(SUM_ALL_MOVIE_LENGTH);
            while ( rs.next() ) {
                result = rs.getInt(1);
            }
            DBConnector.connection.commit();
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }
    
    public void dropTable() throws SQLException, ClassNotFoundException {
        preparedStatement = DBConnector.getPrepraredStatement(DROP_TABLE);
        DBConnector.executeUpdate(preparedStatement);
    }
    
}
