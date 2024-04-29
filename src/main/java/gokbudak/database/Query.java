package gokbudak.database;

import java.sql.*;
import java.util.ArrayList;

public class Query {

    private static Query instance;

    public enum DataType {
        STRING, INTEGER, FLOAT, DATE, TIME, BYTE
    }

    public enum OrderType {
        ASC, DESC
    }

    public static Query getInstance(){
        if (instance == null){
            instance = new Query();
        }
        return instance;
    }

    //insert for users table
    public void insert(String user_id, String firstName, String lastName, String gender, String TCNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO users (user_id, firstName, lastName, gender, TCNumber) " +
                        "VALUES (?,?,?,?,?)");

            ps.setInt(1, Integer.parseInt(user_id));
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, gender);
            ps.setString(5, TCNumber);

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

        String txtUser_id = select("user_id", "users", "TCNumber", TCNumber, DataType.INTEGER);
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

    //insert for addresses table
    public void insert(String address_id, String user_id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO addresses (address_id, user_id) " +
                            "VALUES (?,?)");

            ps.setInt(1, Integer.parseInt(address_id));
            ps.setInt(2, Integer.parseInt(user_id));

            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    //insert for balances table
    public void insert(String balance_id, String user_id, float current_balance) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO balances (balance_id, user_id, current_balance) " +
                            "VALUES (?,?,?)");

            ps.setInt(1, Integer.parseInt(balance_id));
            ps.setInt(2, Integer.parseInt(user_id));
            ps.setFloat(3, current_balance);

            ps.execute();
        }
        finally {
            MSSQLConnection.getInstance().close(connection, ps);
        }
    }

    //insert for orders table
    public void insert(int order_id, int user_id, int product_id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO orders (order_id, user_id, product_id, is_delivered, saleDate) " +
                            "VALUES (?,?,?,?,?)");

            ps.setInt(1, order_id);
            ps.setInt(2, user_id);
            ps.setInt(3, product_id);
            ps.setByte(4, (byte) 0);
            ps.setString(5, getDate());

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

    public boolean isHave(String select, String from, String whereKey, String whereValue, String extra, String value, DataType dt) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean have = false;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("SELECT " + select + " FROM " + from + " WHERE " +
                    whereKey + " = ? " + extra);
            if(dt.equals(DataType.BYTE)){
                System.out.println(Byte.parseByte(whereValue));
                ps.setByte(1, Byte.parseByte(whereValue));
            }
            else{
                ps.setString(1, whereValue);
            }
            rs = ps.executeQuery();

            while(rs.next()){
                if (dt.equals(DataType.STRING)) {
                    if (rs.getString(1).equals(value)) {
                        have = true;
                    }
                }
                else if (dt.equals(DataType.INTEGER)) {
                    if (rs.getInt(1) == Integer.parseInt(value)){
                        have = true;
                    }
                }
                else if(dt.equals(DataType.FLOAT)) {
                    if (rs.getFloat(1) == Float.parseFloat(value)){
                        have = true;
                    }
                }
                else if(dt.equals(DataType.DATE) || dt.equals(DataType.TIME)){
                    if (rs.getString(1).equals(value)){
                        have = true;
                    }
                }
                else if(dt.equals(DataType.BYTE)){
                    if (rs.getByte(1) == Byte.parseByte(value)){
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

    public boolean isHave(String select, String from, String value, DataType dt) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean have = false;

        try {
            connection = MSSQLConnection.getInstance().createConnection();
            ps = connection.prepareStatement("SELECT " + select + " FROM " + from);
            rs = ps.executeQuery();

            while(rs.next()){
                if (dt.equals(DataType.STRING)) {
                    if (rs.getString(1).equals(value)) {
                        have = true;
                    }
                }
                else if (dt.equals(DataType.INTEGER)) {
                    if (rs.getInt(1) == Integer.parseInt(value)){
                        have = true;
                    }
                }
                else if(dt.equals(DataType.FLOAT)) {
                    if (rs.getFloat(1) == Float.parseFloat(value)){
                        have = true;
                    }
                }
                else if(dt.equals(DataType.DATE) || dt.equals(DataType.TIME)){
                    if (rs.getString(1).equals(value)){
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

    //select xx from xx where xx = xx
    public String select(String select, String from, String whereKey, String whereValue, DataType dt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String txt = "", date = "", time = "";
        int iNumber = 0;
        float fNumber = 0;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select + " FROM " + from +
                    " WHERE " + whereKey +" = ?");
            ps.setString(1, whereValue);
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

    //select xx from xx order by xx ASC|DESC
    public String select(String select, String from, String orderBy, OrderType oT, DataType dt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String txt = "", date = "", time = "", orderType = "";
        int iNumber = 0;
        float fNumber = 0;

        if(oT.equals(OrderType.ASC)){
            orderType = "ASC";
        }
        else if(oT.equals(OrderType.DESC)){
            orderType = "DESC";
        }

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select + " FROM " + from +
                    " ORDER BY " + orderBy + " " + orderType);
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

    public ArrayList<Object[]> getDataForWRInfo(String select, String from, String join, String onLeft, String onRight, String whereV, String whereK) throws SQLException {
        ArrayList<Object[]> dataList = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select +
                    " FROM " + from +
                    " JOIN " + join +
                    " ON "  + onLeft + " = " + onRight +
                    " WHERE " + whereV + " = ?");
            ps.setString(1, whereK);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                String situation = rs.getString("situation");
                String saleDate = rs.getString("saleDate");
                String deliveryDate = rs.getString("deliveryDate");

                Object[] row = {name, size, situation, saleDate, deliveryDate};
                dataList.add(row);
            }
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }

        return dataList;
    }

    public ArrayList<Object[]> getDataForUserInfo(String select, String from, String join, String onLeft, String onRight, String secondJoin, String secondOnL, String secondOnR) throws SQLException {
        ArrayList<Object[]> dataList = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MSSQLConnection.getInstance().createConnection();
            ps = con.prepareStatement("SELECT " + select +
                    " FROM " + from +
                    " JOIN " + join +
                    " ON "  + onLeft + " = " + onRight +
                    " JOIN " + secondJoin +
                    " ON " + secondOnL + " = " + secondOnR);
            rs = ps.executeQuery();

            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String TCNumber = rs.getString("TCNumber");
                String name = rs.getString("name");
                String saleDate = rs.getString("saleDate");

                Object[] row = {order_id, firstName, lastName, TCNumber, name, saleDate};
                dataList.add(row);
            }
        }
        finally {
            MSSQLConnection.getInstance().close(con, ps, rs);
        }

        return dataList;
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
