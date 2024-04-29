package gokbudak.manager;

import com.formdev.flatlaf.FlatClientProperties;
import gokbudak.database.Query;
import gokbudak.login.Login;
import gokbudak.menu.TableGradientCell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableManager {

    public static JScrollPane getjScrollPane(String[] columnNames, int i) throws SQLException {
        JTable table = getjTable(columnNames, i);

        for (int j = 0; j < columnNames.length; j++) {
            TableColumn column = table.getColumnModel().getColumn(j);
            column.setResizable(false);
            if (columnNames[j].equals("Satın Alma Tarihi") || columnNames[j].equals("Teslim Tarihi")) {
                column.setPreferredWidth(150);
            }
        }

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "hoverTrackColor:null");

        return scrollPane;
    }

    private static JTable getjTable(String[] columnNames, int i) throws SQLException {
        DefaultTableModel model;
        if(i == 1){
            model = getDefaultTableModel1(columnNames);
        }
        else if (i == 2){
            model = getDefaultTableModel2(columnNames);
        }
        else if (i == 3){
            model = getDefaultTableModel3(columnNames);
        }
        else if (i == 4){
            model = getDefaultTableModel4(columnNames);
        }
        else if (i == 5){
            model = getDefaultTableModel5(columnNames);
        }
        else if (i == 6){
            model = getDefaultTableModel6(columnNames);
        }
        else if (i == 7){
            model = getDefaultTableModel7(columnNames);
        }
        else{
            model = null;
        }
        JTable table = new JTable(model);
        table.setDefaultRenderer(Object.class, new TableGradientCell());
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "hoverBackground:null;"
                        + "pressedBackground:null;"
                        + "separatorColor:$TableHeader.background");

        return table;
    }

    private static DefaultTableModel getDefaultTableModel1(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfWRInfo_ForUser("p.name, p.size, o.saleDate, o.deliveryDate, o.situation",
                "products p", "orders o", "p.product_id", "o.product_id",
                "user_id", Login.getCurrentUserId(),
                "AND o.is_delivered = 1");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel2(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfUserRequests_ForAdmin("o.order_id, u.firstName, u.lastName, u.TCNumber,  p.name, o.saleDate",
                "orders o", "users u", "o.user_id", "u.user_id",
                "products p", "o.product_id", "p.product_id",
                "o.is_delivered", "0");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel3(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfInUseWRs_ForAdmin("p.name, u.firstName, u.lastName, u.TCNumber, o.situation, o.saleDate",
                "orders o", "users u", "o.user_id", "u.user_id",
                "products p", "o.product_id", "p.product_id",
                "o.is_delivered", "1");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel4(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfAllWRInfo_ForAdmin("*", "products");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel5(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfAllUsersInfo_ForAdmin("*", "users");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel6(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfAllLoginInfo_ForAdmin("user_id, username, email, phoneNumber", "loginInfos");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static DefaultTableModel getDefaultTableModel7(String[] columnNames) throws SQLException {

        ArrayList<Object[]> dataList = Query.getInstance().getDataOfAllAddressesInfo_ForAdmin("*", "addresses");

        Object[][] data = dataList.toArray(new Object[dataList.size()][]);

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
