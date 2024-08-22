package context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    protected Connection connection;

    public DBContext() {

    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        try {
            String user = "sa";
            String pass = "123";
              String url = "jdbc:sqlserver://LAPTOP-M6E94HAE\\SQLEXPRESS\\SQLEXPRESS:1433;databaseName=SWP391_SE1849_GROUP42";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
