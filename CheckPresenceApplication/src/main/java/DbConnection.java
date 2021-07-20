
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 41MCR955
 */

public class DbConnection {
    private static String servername = "localhost";
    private static String username = "root";
    private static String databaseName = "users_db";
    //private static String url = "jdbc:mysql://" + servername + "/:" + portnumber + "/" + databaseName;
    private static Integer portnumber = 3306;
    private static String password = "DbPass123!";
        
    public static Connection getConnection(){
        Connection connection = null;
        
        MysqlDataSource datasource = new MysqlDataSource();
        
        datasource.setServerName(servername);
        datasource.setUser(username);
        datasource.setPassword(password);
        datasource.setDatabaseName(databaseName);
        datasource.setPortNumber(portnumber);
        
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            //connection = DriverManager.getConnection(url,username, password);
            connection = datasource.getConnection();
        } catch (Exception ex) {
            //System.out.println("Ooops, error!");
            //ex.printStackTrace();
            Logger.getLogger(" Get connection -> " + DbConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return connection;
    }
}
