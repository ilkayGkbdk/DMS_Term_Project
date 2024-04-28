package gokbudak.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import gokbudak.database.MSSQLConnection;
import gokbudak.database.Query;
import gokbudak.login.Login;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

public class Message {

    private static String title;
    private static JLabel lbSize, lbStock, lbPrice;

    public Message(String title, JLabel lbSize, JLabel lbStock, JLabel lbPrice){
        Message.title = title;
        Message.lbSize = lbSize;
        Message.lbStock = lbStock;
        Message.lbPrice = lbPrice;
        init();
    }

    private void init(){
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setLayout(new MigLayout("wrap, fillx", "[center]", "[center]"));

        JLabel label1 = new JLabel("Boyut");
        JLabel label2 = new JLabel("Kalan Adet");
        JLabel label3 = new JLabel("Fiyat");
        JLabel lbQ = new JLabel("İşlemi onaylıyor musun?");

        label1.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        label2.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        label3.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        lbQ.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#F0F3F4");

        lbSize.putClientProperty(FlatClientProperties.STYLE, "font:italic +4; foreground:#F0F3F4");
        lbStock.putClientProperty(FlatClientProperties.STYLE, "font:italic +4; foreground:#F0F3F4");
        lbPrice.putClientProperty(FlatClientProperties.STYLE, "font:italic +4; foreground:#F0F3F4");

        JPanel buttonPanel = getPanel(dialog);

        dialog.add(label1, "split 3");
        dialog.add(label2, "gapx 40");
        dialog.add(label3, "gapx 40");
        dialog.add(lbSize, "split 3, gapx 20");
        dialog.add(lbStock, "gapx 50");
        dialog.add(lbPrice, "gapx 40");
        dialog.add(new JSeparator());
        dialog.add(lbQ);
        dialog.add(buttonPanel);

        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static JPanel getPanel(JDialog dialog) {
        JButton yesButton = new JButton("Evet");
        yesButton.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "font:bold +3; foreground:#27AE60");
        yesButton.addActionListener(e -> {
            dialog.dispose();
            JLabel label = new JLabel();
            label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
            boolean isError = true;
            float balance = 0;

            try {
                balance = Float.parseFloat(Query.getInstance().select("current_balance", "balances", "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if(Integer.parseInt(lbStock.getText()) == 0){
                label.setText("Tüm " + title + " Depolar Kullanımda");
            }
            else if(balance < Float.parseFloat(lbPrice.getText().trim().replace("₺", ""))){
                label.setText("Bakiye Yetersiz");
            }
            else {
                int order_id;
                int product_id;
                try {
                    product_id = Integer.parseInt(Query.getInstance().select("product_id", "products", "name", title, Query.DataType.STRING));
                    int stock = Integer.parseInt(lbStock.getText());
                    String stc = String.valueOf((stock - 1));
                    float exBlc = balance;
                    balance -= Float.parseFloat(lbPrice.getText().trim().replace("₺", ""));
                    if(!Query.getInstance().isHave("order_id", "orders", "1", Query.DataType.INTEGER)){
                        order_id = 1;
                    }
                    else{
                        order_id = Integer.parseInt(Query.getInstance().select("order_id", "orders", "order_id", Query.OrderType.DESC, Query.DataType.INTEGER));
                        order_id++;
                    }
                    Query.getInstance().insert(order_id, Integer.parseInt(Login.getCurrentUserId()), product_id);
                    Query.getInstance().update("products", "stock", stc, "product_id", String.valueOf(product_id), Query.DataType.INTEGER);
                    Query.getInstance().update("balances", "current_balance", String.valueOf(balance), "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "old_balance", String.valueOf(exBlc), "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "change_date", "date", "user_id", Login.getCurrentUserId(), Query.DataType.DATE);
                    Query.getInstance().update("balances", "change_time", "time", "user_id", Login.getCurrentUserId(), Query.DataType.TIME);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                label.setText("Talebiniz Başarıyla Oluştu! Yönetici Onaylayınca 'Depolarım/Depoları Görüntüle' Kısmından Kontrol Edebilirsiniz.");
                JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(Message.class.getResource("/gokbudak/images/success.png"))));
                isError = false;
            }

            if (isError) {
                JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(Message.class.getResource("/gokbudak/images/error.png"))));
            }
        });

        JButton noButton = new JButton("Hayır");
        noButton.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "font:bold +3; foreground:#E74C3C");
        noButton.addActionListener(e -> {
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        return buttonPanel;
    }

}
