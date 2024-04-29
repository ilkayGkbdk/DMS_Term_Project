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

        TableColumn clmName = table.getColumnModel().getColumn(0);
        TableColumn clmSize = table.getColumnModel().getColumn(1);
        TableColumn clmSituation = table.getColumnModel().getColumn(2);
        TableColumn clmSaleDt = table.getColumnModel().getColumn(3);
        TableColumn clmDeliveryDt = table.getColumnModel().getColumn(4);

        clmName.setResizable(false);
        clmSize.setResizable(false);
        clmSituation.setResizable(false);
        clmSaleDt.setResizable(false);
        clmSaleDt.setPreferredWidth(150);
        clmDeliveryDt.setResizable(false);
        clmDeliveryDt.setPreferredWidth(150);

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
        else if (i==2){
            model = getDefaultTableModel2(columnNames);
        }
        else{
            model = getDefaultTableModel3(columnNames);
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

        ArrayList<Object[]> dataList = Query.getInstance().getDataForWRInfo("p.name, p.size, o.saleDate, o.deliveryDate, o.situation",
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

        ArrayList<Object[]> dataList = Query.getInstance().getDataForUserInfo("o.order_id, u.firstName, u.lastName, u.TCNumber,  p.name, o.saleDate",
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

        ArrayList<Object[]> dataList = Query.getInstance().getDataForAdminWR_Show("p.name, u.firstName, u.lastName, u.TCNumber, o.situation, o.saleDate",
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
}
