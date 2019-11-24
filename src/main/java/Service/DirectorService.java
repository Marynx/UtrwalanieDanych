package Service;

import DBUtils.DBConnector;
import Domain.Director;
import Domain.Movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DirectorService {
    
    private final String CREATE_DIRECTOR_TABLE = "CREATE TABLE director (\n" +
                                                 "   id INT NOT NULL,\n" +
                                                 "   firstname VARCHAR(50) NOT NULL,\n" +
                                                 "   lastname VARCHAR(50) NOT NULL,\n" +
                                                 "   city VARCHAR(50) NOT NULL,\n" +
                                                 "   yob DATE,\n" +
                                                 "   PRIMARY KEY (id) \n" +
                                                 ");";
    
    private final String DROP_TABLE = "DROP TABLE IF EXISTS DIRECTOR";
    
    private final String CREATE_DIRECTOR="INSERT INTO DIRECTOR(ID,FIRSTNAME,LASTNAME,CITY,YOB) VALUES (?,?,?,?,?)";
    private final String READ_DIRECTORS="SELECT * FROM DIRECTOR";
    private final String READ_DIRECTOR_WITH_ID="SELECT * FROM DIRECTOR WHERE ID=(?)";
    private final String UPDATE_DIRECTOR="UPDATE DIRECTOR SET FIRSTNAME=(?),LASTNAME=(?),CITY=(?),YOB=(?) WHERE ID=(?)";
    private final String DELETE_DIRECTOR="DELETE FROM DIRECTOR WHERE ID=(?)";
    private final String READ_YOUNGEST_DIRECTOR="SELECT MAX(YOB) FROM DIRECTOR";
    private final String COUNT_DIRECTOR_MOVIES="SELECT COUNT(*) FROM MOVIE JOIN DIRECTOR " +
                                               "ON MOVIE.DIRECTOR.ID = DIRECTOR.ID WHERE DIRECTOR.ID=(?)";
    private final String DIRECTOR_AGE="SELECT DATEDIFF(YEAR,YOB, CURRENT_DATE) AS AGE FROM DIRECTOR WHERE ID=(?)";
    
    PreparedStatement preparedStatement = null;
    
    public DirectorService() throws SQLException {
        try {
            DBConnector.connection.setAutoCommit(false);
            preparedStatement = DBConnector.getPrepraredStatement(CREATE_DIRECTOR_TABLE);
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
    
    public int create(Director director) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(CREATE_DIRECTOR);
            preparedStatement.setInt(1, director.getId());
            preparedStatement.setString(2, director.getFirstName());
            preparedStatement.setString(3, director.getLastName());
            preparedStatement.setString(4, director.getCity());
            preparedStatement.setDate(5, director.getYob());
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
    
    public Director read(int id) throws SQLException {
       Director director=new Director();
        try {
            preparedStatement = DBConnector.getPrepraredStatement(READ_DIRECTOR_WITH_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            DBConnector.connection.commit();
            if ( rs == null ) {
                System.out.println("Director with id " + id + " not found");
                return null;
            }
            while ( rs.next() ) {
                director.setId(rs.getInt("ID"));
               director.setFirstName(rs.getString("FIRSTNAME"));
               director.setLastName(rs.getString("LASTNAME"));
               director.setCity(rs.getString("CITY"));
               director.setYob(rs.getDate("YOB"));
            }
            return director;
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Director> read_all() throws SQLException {
        List<Director> directors = new ArrayList<Director>();
        try {
            ResultSet rs = DBConnector.executeQuery(READ_DIRECTORS);
            DBConnector.connection.commit();
            
            if ( rs == null ) {
                System.out.println("No movies in Database");
                return directors;
            }
            
            while ( rs.next() ) {
                directors.add(new Director(
                                rs.getInt("ID"),
                                rs.getString("FIRSTNAME"),
                                rs.getString("LASTNAME"),
                                rs.getString("CITY"),
                                rs.getDate("YOB")
                        )
                );
            }
            
        } catch ( SQLException e ) {
            DBConnector.connection.rollback();
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return directors;
    }
    
    public int delete(int id) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(DELETE_DIRECTOR);
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
    
    public int update(Director director, int id) throws SQLException {
        int result = 0;
        try {
            preparedStatement = DBConnector.getPrepraredStatement(UPDATE_DIRECTOR);
            preparedStatement.setString(1, director.getFirstName());
            preparedStatement.setString(2, director.getLastName());
            preparedStatement.setString(3, director.getCity());
            preparedStatement.setDate(4, director.getYob());
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
    
    public Director readYoungestDirector() throws SQLException {
        Director director = new Director();
        try {
            ResultSet rs = DBConnector.executeQuery(READ_YOUNGEST_DIRECTOR);
            DBConnector.connection.commit();
            while ( rs.next() ) {
                director.setId(rs.getInt("ID"));
                director.setFirstName(rs.getString("FIRSTNAME"));
                director.setLastName(rs.getString("LASTNAME"));
                director.setCity(rs.getString("CITY"));
                director.setYob(rs.getDate("YOB"));
            }
            return director;
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
            DBConnector.connection.rollback();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int countDirectorMovies(int id) throws SQLException {
        int result = 0;
        try {
            PreparedStatement preparedStatement = DBConnector.getPrepraredStatement(COUNT_DIRECTOR_MOVIES);
            preparedStatement.setInt(1, id);
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
    
    public int getDirectorAge(int id) throws SQLException {
        int result = 0;
        try {
            PreparedStatement preparedStatement = DBConnector.getPrepraredStatement(DIRECTOR_AGE);
            preparedStatement.setInt(1, id);
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
    
    
    
    
}
