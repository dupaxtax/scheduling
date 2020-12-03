package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    //JDBC URL
    private static final String jdbcURL = "jdbc:mysql://wgudb.ucertify.com:3306/U05ZMd";

    //Driver and Connection interface refrences
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U05ZMd";
    private static final String password = "53688649873";

    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful");
        }
        catch(ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection()
    {
        try {
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println("Connection closed");
    }

    public static Connection getConnection(){
        return conn;
    }
}
