package gokbudak.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import gokbudak.database.Query;
import gokbudak.login.Login;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class Panels {

    private JLabel lbFullName, fullName, lbGender, gender, lbId, id, lbPhone, phone;
    private JLabel lbDistrict, district, lbAddress, address;

    public enum SystemPanel {
        PERSONAL_INFO, LOGIN_INFO, BALANCE, SHOW_WAREHOUSE, BUY_WAREHOUSE, SHOW_ITEMS,
        ADD_ITEMS, QUESTIONS
    }

    private static Panels instance;

    public Panels(){}

    public static Panels getInstance(){
        if (instance == null){
            instance = new Panels();
        }
        return instance;
    }

    public JPanel getPanel(SystemPanel systemPanel) throws SQLException {
        switch (systemPanel){
            case PERSONAL_INFO -> {
                return personalPanel();
            }
            case LOGIN_INFO -> {
                return loginPanel();

            }
            case BALANCE -> {
                return balancePanel();
            }
            case SHOW_WAREHOUSE -> {
                return showPanel();
            }
            case BUY_WAREHOUSE -> {
                return buyPanel();
            }
            case SHOW_ITEMS -> {
                return itemsPanel();
            }
            case ADD_ITEMS -> {
                return addItemPanel();
            }
            case QUESTIONS -> {
                return questionPanel();
            }
        }
        return null;
    }

    private JPanel questionPanel() {
        //TODO
        return null;
    }

    private JPanel addItemPanel() {
        //TODO
        return null;
    }

    private JPanel itemsPanel() {
        //TODO
        return null;
    }

    private JPanel buyPanel() {
        //TODO
        return null;
    }

    private JPanel showPanel() {
        //TODO
        return null;
    }

    private JPanel balancePanel() {
        //TODO
        return null;
    }

    private JPanel loginPanel() {
        //TODO
        return null;
    }

    public JPanel personalPanel() throws SQLException {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JPanel nestPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        nestPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        JPanel nestPanel2 = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        nestPanel2.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        lbFullName = new JLabel("İsim Soyisim");
        fullName = new JLabel(Query.getInstance().select("firstName", "users", "user_id", Login.getCurrentUserId(), false) +
                " " + Query.getInstance().select("lastName", "users", "user_id", Login.getCurrentUserId(), false));
        lbGender = new JLabel("Cinsiyet");
        gender = new JLabel(Query.getInstance().select("gender", "users", "user_id", Login.getCurrentUserId(), false));
        lbId = new JLabel("TC");
        id = new JLabel(Query.getInstance().select("TCNumber", "users", "user_id", Login.getCurrentUserId(), false));
        lbPhone = new JLabel("Telefon");
        phone = new JLabel();

        lbFullName.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        fullName.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
        lbGender.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        gender.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
        lbId.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        id.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
        lbPhone.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        phone.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");

        nestPanel.add(lbFullName, "gapy 8");
        nestPanel.add(fullName);
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(lbGender, "gapy 8");
        nestPanel.add(gender);
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(lbId, "gapy 8");
        nestPanel.add(id);
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(lbPhone, "gapy 8");
        if(Query.getInstance().select("phoneNumber", "loginInfos", "user_id", Login.getCurrentUserId(), false) == null){
            nestPanel.add(createAddLabel("Telefon Ekle"));
        }
        else {
            phone.setText(Query.getInstance().select("phoneNumber", "loginInfos", "user_id", Login.getCurrentUserId(), false));
            nestPanel.add(phone);
        }

        if (!Query.getInstance().isHave("user_id", "addresses", Login.getCurrentUserId(), true)){
            nestPanel2.add(createAddLabel("Adres Ekle"));
        }
        else {
            JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            title.putClientProperty(FlatClientProperties.STYLE, "background:null");
            JLabel tit = new JLabel("Adress Bilgisi");
            tit.putClientProperty(FlatClientProperties.STYLE, "font:bold +5; foreground:#23904AFF");
            title.add(tit);

            lbDistrict = new JLabel("İlçe");
            district = new JLabel(Query.getInstance().select("district", "addresses", "user_id", Login.getCurrentUserId(), false));
            lbAddress = new JLabel("Konum");
            address = new JLabel(Query.getInstance().select("full_address", "addresses", "user_id", Login.getCurrentUserId(), false));

            lbDistrict.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
            district.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
            lbAddress.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
            address.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");

            nestPanel2.add(title);
            nestPanel2.add(new JSeparator(), "gapy 5 5");
            nestPanel2.add(lbDistrict, "gapy 8");
            nestPanel2.add(district);
            nestPanel2.add(new JSeparator(), "gapy 5 5");
            nestPanel2.add(lbAddress, "gapy 8");
            nestPanel2.add(address);
        }

        panel.add(nestPanel);
        panel.add(nestPanel2);

        return panel;
    }

    private Component createAddLabel(String str) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        JButton cmdAddPhone = new JButton("<html><a href = \"#\">" + str + "</a></html>");
        cmdAddPhone.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        cmdAddPhone.putClientProperty(FlatClientProperties.STYLE, "font:bold +5; foreground:#23904AFF");
        cmdAddPhone.setContentAreaFilled(false);
        cmdAddPhone.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdAddPhone.addActionListener(e -> {

            if (str.equals("Telefon Ekle")){
                JLabel label = new JLabel();
                label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
                boolean isError = true;

                label.setText("Telefon Numaranı Gir");
                String phoneNumberWithZero = (String) JOptionPane.showInputDialog(null, label, "Telefon Ekle", JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);
                String phoneNumber = phoneNumberWithZero.startsWith("0") ? phoneNumberWithZero.substring(1):phoneNumberWithZero;

                if (!isLengthOK(phoneNumber)){
                    label.setText("Telefon 10 Haneli Olmalıdır (0'ı saymadan)");
                }
                else if (!isNumeric(phoneNumber)){
                    label.setText("Telefon Sadece Sayılardan Oluşabilir");
                }
                else {
                    try {
                        Query.getInstance().update("loginInfos", "phoneNumber", phoneNumber, "user_id", Login.getCurrentUserId());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    label.setText("Telefon Başarıyla Eklendi! Sayfa Kapanınca Güncellenir.");
                    JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                            new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                    System.out.println(phoneNumber);
                    isError = false;
                }

                if (isError){
                    JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                            new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
                }

            }
        });

        panel.add(cmdAddPhone);

        return panel;
    }

    private boolean isNumeric(String phone){
        for (int i = 0; i < 10; i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    private boolean isLengthOK(String phone){
        return phone.length() == 10;
    }
}
