package gokbudak.database;

import java.sql.*;

public class MSSQLConnection {

    private static MSSQLConnection instance;

    private Statement stat;
    //private ResultSet resultSetTRY = null;
    private String dburl;
    private String port = "localhost:1433;";
    private String databaseName = "TERMPROJECT;";
    private String user = "sa;";
    private String password = "reallyStrongPwd123;";

    public static MSSQLConnection getInstance(){
        if (instance == null){
            instance = new MSSQLConnection();
        }
        return instance;
    }

    private MSSQLConnection() {

    }

    public void connectDatabase() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dburl =  "jdbc:sqlserver://" + port +
                    "databaseName=" + databaseName +
                    "integratedSecurity = false;" +
                    "encrypt = true;" +
                    "trustServerCertificate = true;" +
                    "user = " + user + "password = " + password;
            Connection connection = DriverManager.getConnection(dburl);
            stat = connection.createStatement();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dburl);
    }

    public void close(AutoCloseable ...closeables) throws SQLException {
        try {
            for (AutoCloseable c : closeables){
                if (c != null){
                    c.close();
                }
            }
        }
        catch (Exception e){
            throw new SQLException("Kapatilirken bir hata olustu");
        }
    }

}
