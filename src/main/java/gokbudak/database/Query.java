package gokbudak.database;

import java.sql.*;

public class Query {

    private static Query instance;

    public enum DataType {
        STRING, INTEGER, FLOAT, DATE, TIME
    }

    public static Query getInstance(){
        if (instance == null){
            instance = new Query();
        }
        return instance;
    }

    //insert for users table
    public void insert(String firstName, String lastName, String gender, String TCNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO users (firstName, lastName, gender, TCNumber) " +
                        "VALUES (?,?,?,?)");

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, gender);
            ps.setString(4, TCNumber);

            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    public void insert(int address_id, int user_id, String district, String full_address) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO addresses (address_id, user_id, district, full_address) " +
                            "VALUES (?,?,?,?)");

            ps.setInt(1, address_id);
            ps.setInt(2, user_id);
            ps.setString(3, district);
            ps.setString(4, full_address);

            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    //insert for loginInfos table
    public void insert(String username, String password, String TCNumber) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;

        String txtUser_id = select("user_id", "users", "TCNumber", TCNumber, true);
        int user_id = Integer.parseInt(txtUser_id);

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("INSERT INTO loginInfos (user_id, username, password) " +
                    "VALUES (?,?,?)");
            ps.setInt(1, user_id);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    public void update(String update, String set, String setValue, String whereKey, String whereValue, DataType dt) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("UPDATE " + update +
                                                " SET " + set + " = ?" +
                                                " WHERE " + whereKey + " = ?");
            if(dt == DataType.STRING){
                ps.setString(1, setValue);
            }
            else if (dt == DataType.INTEGER){
                ps.setInt(1, Integer.parseInt(setValue));
            }
            else if (dt.equals(DataType.FLOAT)){
                ps.setFloat(1, Float.parseFloat(setValue));
            }
            else if (dt.equals(DataType.DATE)){
                ps.setString(1, getDate());
            }
            else if (dt.equals(DataType.TIME)){
                ps.setString(1, getTime());
            }

            ps.setString(2, whereValue);
            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    public void delete() throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(" ");
            //TODO
            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    public String select(String from, String whereKey, String whereValue) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String password;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("SELECT * FROM " + from + " WHERE " +
                    whereKey + " = ?");
            ps.setString(1, whereValue);
            rs = ps.executeQuery();
            rs.next();
            password = rs.getString("password");
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps, rs);
        }

        return password;
    }

    public boolean isHave(String select, String from, String value, boolean isInt) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean have = false;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("SELECT " + select + " FROM " + from);
            rs = ps.executeQuery();

            while(rs.next()){
                if (!isInt) {
                    if (rs.getString(1).equals(value)) {
                        have = true;
                    }
                }
                else {
                    if (rs.getInt(1) == Integer.parseInt(value)){
                        have = true;
                    }
                }
            }
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps, rs);
        }

        return have;
    }

    public String select(String select, String from, String whereKey, String whereValue, boolean isInt) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String text = null;
        int numeric = 0;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select + " FROM " + from + " WHERE " +
                    whereKey + " = ?");
            ps.setString(1, whereValue);
            rs = ps.executeQuery();
            rs.next();

            if (isInt){
                numeric = rs.getInt(1);
            }
            else {
                text = rs.getString(1);
            }
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }

        if (isInt){
            return Integer.toString(numeric);
        }
        else {
            return text;
        }
    }

    public String select(String select, String from, String where, DataType dt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String txt = "", date = "", time = "";
        int iNumber = 0;
        float fNumber = 0;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select + " FROM " + from +
                    " WHERE user_id = ?");
            ps.setString(1, where);
            rs = ps.executeQuery();
            rs.next();

            if(dt == DataType.STRING){
                txt = rs.getString(1);
            }
            else if (dt == DataType.INTEGER){
                iNumber = rs.getInt(1);
            }
            else if (dt.equals(DataType.FLOAT)){
                fNumber = rs.getFloat(1);
            }
            else if (dt.equals(DataType.DATE)){
                date = String.valueOf(rs.getDate(1));
            }
            else if (dt.equals(DataType.TIME)){
                time = String.valueOf(rs.getTime(1));
            }
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }

        if(dt == DataType.STRING){
            return txt;
        }
        else if (dt == DataType.INTEGER){
            return Integer.toString(iNumber);
        }
        else if (dt.equals(DataType.FLOAT)){
            return Float.toString(fNumber);
        }
        else if (dt.equals(DataType.DATE)){
            return date;
        }
        else if (dt.equals(DataType.TIME)){
            return time;
        }
        else {
            return null;
        }
    }

    public String getDate() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String date;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT SWITCHOFFSET(SYSDATETIMEOFFSET(),'+03:00')");
            rs = ps.executeQuery();
            rs.next();
            date = rs.getString(1);
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }
        return date;
    }

    public String getTime() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String time;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT CONVERT(VARCHAR(5),SWITCHOFFSET(SYSDATETIMEOFFSET(),'+03:00'),108)");
            rs = ps.executeQuery();
            rs.next();
            time = rs.getString(1);
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }
        return time;
    }

}
