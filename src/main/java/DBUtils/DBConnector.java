package DBUtils;

import java.sql.*;

public class DBConnector {
    
    private final static String DBDRIVER = "org.hsqldb.jdbc.JDBCDriver";
    public static Connection connection=null;
    
    public static void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DBDRIVER);
        connection = DriverManager.getConnection( "jdbc:hsqldb:hsql://localhost/workdb", "SA", "");
    }
    
    public static void closeConnection() throws SQLException {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
    }
    
    public static PreparedStatement getPrepraredStatement(String query) throws SQLException, ClassNotFoundException {
       
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        
        return preparedStatement;
    }
    
    public static ResultSet executeQuery(String query) throws SQLException, ClassNotFoundException {
        
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        ResultSet rs=preparedStatement.executeQuery();
      
        return rs;
    }
    
    public static int executeUpdate(PreparedStatement preparedStatement) throws SQLException, ClassNotFoundException {
     
        int result=preparedStatement.executeUpdate();
        
        return result;
    }
}
