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
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        JLabel label = new JLabel("questionPanel");
        panel.add(label);
        return panel;
    }

    private JPanel addItemPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        JLabel label = new JLabel("addItemPanel");
        panel.add(label);
        return panel;
    }

    private JPanel itemsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        JLabel label = new JLabel("itemsPanel");
        panel.add(label);
        return panel;
    }

    private JPanel buyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        JLabel label = new JLabel("buyPanel");
        panel.add(label);
        return panel;
    }

    private JPanel showPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JPanel nestPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        nestPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");




        panel.add(nestPanel);
        return panel;
    }

    private JPanel balancePanel() throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JPanel nestPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        nestPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "[light]background:darken(@background,3%);" +
                        "[dark]background:lighten(@background,3%)");

        JLabel lbBalance = new JLabel("Bakiye");
        String exBalance = Query.getInstance().select("current_balance", "balances", "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
        JLabel balance = new JLabel(exBalance + " ₺");
        JButton cmdAddBalance = new JButton("Bakiye Ekle");
        JButton cmdWithdrawBalance = new JButton("Bakiye Çek");

        cmdAddBalance.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                        "[dark]background:lighten(@background, 10%);" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0");
        cmdWithdrawBalance.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                        "[dark]background:lighten(@background, 10%);" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0");

        lbBalance.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        balance.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");

        cmdAddBalance.addActionListener(e -> {
            JLabel label = new JLabel();
            label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
            boolean isError = true;

            label.setText("Eklenecek Bakiyeyi Gir");
            String strBalance = (String) JOptionPane.showInputDialog(null, label, "Bakiye Ekle", JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);

            if(isNumericF(strBalance)){
                label.setText("Sadece Sayı Girişi Yazın");
            }
            else if (checkFloat(strBalance)){
                label.setText("En Fazla 99,999.9 ₺ Eklenebilir");
            }
            else {

                float newBalance = Float.parseFloat(strBalance);
                float exBlc = Float.parseFloat(exBalance);
                newBalance += exBlc;

                String execBalance = String.valueOf(newBalance);
                String oldBalance = String.valueOf(exBlc);


                try {
                    Query.getInstance().update("balances", "current_balance", execBalance, "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "old_balance", oldBalance, "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "change_date", "date", "user_id", Login.getCurrentUserId(), Query.DataType.DATE);
                    Query.getInstance().update("balances", "change_time", "time", "user_id", Login.getCurrentUserId(), Query.DataType.TIME);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                label.setText(strBalance + " ₺ Başarıyla Eklendi! Sayfa Kapanınca Güncellenir.");
                JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                isError = false;
            }

            if(isError){
                JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
            }

        });

        cmdWithdrawBalance.addActionListener(e -> {
            JLabel label = new JLabel();
            label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
            boolean isError = true;

            label.setText("Çekilecek Bakiyeyi Gir");
            String strBalance = (String) JOptionPane.showInputDialog(null, label, "Bakiye Çek", JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);

            if(isNumericF(strBalance)){
                label.setText("Sadece Sayı Girişi Yazın");
            }
            else if (checkFloat(strBalance)){
                label.setText("En Fazla 99,999.9 ₺ Çekilebilir");
            }
            else if(Float.parseFloat(strBalance) > Float.parseFloat(exBalance)){
                label.setText("Mevcut Bakiyeden Fazla Çekilemez");
            }
            else {
                float newBalance = Float.parseFloat(strBalance);
                float exBlc = Float.parseFloat(exBalance);
                newBalance = exBlc - newBalance;

                String execBalance = String.valueOf(newBalance);
                String oldBalance = String.valueOf(exBlc);


                try {
                    Query.getInstance().update("balances", "current_balance", execBalance, "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "old_balance", oldBalance, "user_id", Login.getCurrentUserId(), Query.DataType.FLOAT);
                    Query.getInstance().update("balances", "change_date", "date", "user_id", Login.getCurrentUserId(), Query.DataType.DATE);
                    Query.getInstance().update("balances", "change_time", "time", "user_id", Login.getCurrentUserId(), Query.DataType.TIME);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                label.setText(strBalance + " ₺ Başarıyla Çekildi! Sayfa Kapanınca Güncellenir.");
                JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                isError = false;
            }

            if(isError){
                JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
            }
        });

        nestPanel.add(lbBalance, "gapy 8");
        nestPanel.add(balance);
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(cmdAddBalance, "split 2");
        nestPanel.add(cmdWithdrawBalance);

        panel.add(nestPanel);
        return panel;
    }

    private boolean checkFloat(String str) {
        int dotIndex = str.indexOf('.');
        if(dotIndex == -1){
            str += ".0";
        }
        dotIndex = str.indexOf('.');

        String beforeDot = str.substring(0, dotIndex);

        return beforeDot.length() > 5;
    }

    private JPanel loginPanel() throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JPanel nestPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        nestPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "[light]background:darken(@background,3%);" +
                        "[dark]background:lighten(@background,3%)");

        JLabel lbUsername, username, lbEmail, email, lbPassword;
        JPasswordField txtPassword;

        lbUsername = new JLabel("Kullanıcı Adı");
        username = new JLabel(Query.getInstance().select("username", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
        lbEmail = new JLabel("E-Mail");
        email = new JLabel();
        lbPassword = new JLabel("Şifre");

        lbUsername.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        username.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");
        email.putClientProperty(FlatClientProperties.STYLE, "font:italic +4");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold +3; foreground:#23904AFF");

        txtPassword = new JPasswordField(Query.getInstance().select("password", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        txtPassword.setEditable(false);
        txtPassword.setFocusable(false);

        nestPanel.add(lbUsername, "gapy 8");
        nestPanel.add(username);
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(lbEmail, "gapy 8");
        if(Query.getInstance().select("email", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING) == null){
            nestPanel.add(createAddLabel("Email Ekle"));
        }
        else {
            email.setText(Query.getInstance().select("email", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
            nestPanel.add(email);
        }
        nestPanel.add(new JSeparator(), "gapy 5 5");
        nestPanel.add(lbPassword, "gapy 8");
        nestPanel.add(txtPassword);

        panel.add(nestPanel);
        return panel;
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

        JLabel lbFullName, fullName, lbGender, gender, lbId, id, lbPhone, phone;
        JLabel lbDistrict, district, lbAddress, address;

        lbFullName = new JLabel("İsim Soyisim");
        fullName = new JLabel(Query.getInstance().select("firstName", "users", "user_id", Login.getCurrentUserId(), Query.DataType.STRING) +
                " " + Query.getInstance().select("lastName", "users", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
        lbGender = new JLabel("Cinsiyet");
        gender = new JLabel(Query.getInstance().select("gender", "users", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
        lbId = new JLabel("TC");
        id = new JLabel(Query.getInstance().select("TCNumber", "users", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
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
        if(Query.getInstance().select("phoneNumber", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING) == null){
            nestPanel.add(createAddLabel("Telefon Ekle"));
        }
        else {
            phone.setText(Query.getInstance().select("phoneNumber", "loginInfos", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
            nestPanel.add(phone);
        }

        if (Query.getInstance().select("district", "addresses", "user_id", Login.getCurrentUserId(), Query.DataType.STRING) == null){
            nestPanel2.add(createAddLabel("Adres Ekle"));
        }
        else {
            JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            title.putClientProperty(FlatClientProperties.STYLE, "background:null");
            JLabel tit = new JLabel("Adress Bilgisi");
            tit.putClientProperty(FlatClientProperties.STYLE, "font:bold +5; foreground:#23904AFF");
            title.add(tit);

            lbDistrict = new JLabel("İlçe");
            district = new JLabel(Query.getInstance().select("district", "addresses", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));
            lbAddress = new JLabel("Konum");
            address = new JLabel(Query.getInstance().select("full_address", "addresses", "user_id", Login.getCurrentUserId(), Query.DataType.STRING));

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

            switch (str) {
                case "Telefon Ekle" -> {
                    JLabel label = new JLabel();
                    label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
                    boolean isError = true;

                    label.setText("Telefon Numaranı Gir");
                    String phoneNumberWithZero = (String) JOptionPane.showInputDialog(null, label, "Telefon Ekle", JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);
                    String phoneNumber = phoneNumberWithZero.startsWith("0") ? phoneNumberWithZero.substring(1) : phoneNumberWithZero;

                    if (!isLength10(phoneNumber)) {
                        label.setText("Telefon 10 Haneli Olmalıdır (0'ı saymadan)");
                    } else if (!isNumeric(phoneNumber)) {
                        label.setText("Telefon Sadece Sayılardan Oluşabilir");
                    } else {
                        try {
                            Query.getInstance().update("loginInfos", "phoneNumber", phoneNumber, "user_id", Login.getCurrentUserId(), Query.DataType.STRING);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        label.setText("Telefon Başarıyla Eklendi! Sayfa Kapanınca Güncellenir.");
                        JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                        isError = false;
                    }

                    if (isError) {
                        JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
                    }
                }
                case "Adres Ekle" -> {
                    JLabel label = new JLabel();
                    label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
                    boolean isError = true;

                    label.setText("İlçe Gir");
                    String txtDistrict = (String) JOptionPane.showInputDialog(null, label, "Adres Ekle", JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);

                    if (!isLetter(txtDistrict)) {
                        label.setText("İlçe Adı Sadece Harflerden Oluşabilir");
                    } else {
                        label.setText("Konum Gir ('Tahralı Sokak 7A' gibi, MAKS 50 Karakter)");
                        String location = (String) JOptionPane.showInputDialog(null, label, "Adres Ekle", JOptionPane.QUESTION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);

                        if (!isLength50(location)) {
                            label.setText("Maksimum 25 Karaktere İzin Verilir");
                        } else {
                            try {
                                Query.getInstance().update("addresses", "district", txtDistrict, "user_id", Login.getCurrentUserId(), Query.DataType.STRING);
                                Query.getInstance().update("addresses", "full_address", location, "user_id", Login.getCurrentUserId(), Query.DataType.STRING);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            label.setText("Adres Başarıyla Eklendi! Sayfa Kapanınca Güncellenir.");
                            JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                                    new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                            isError = false;
                        }
                    }

                    if (isError) {
                        JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
                    }
                }
                case "Email Ekle" -> {
                    JLabel label = new JLabel();
                    label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
                    boolean isError = true;

                    label.setText("E-Mail Gir (Sadece @ Kısmından Öncesi)");
                    String mail = (String) JOptionPane.showInputDialog(null, label, "E-Mail Ekle", JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/enter.png"))), null, null);

                    if(!checkMail(mail)){
                        label.setText("Geçersiz E-Mail ('@', '.', yada Mail İmzası Kullanma)");
                    }
                    else if(mail.length() < 3){
                        label.setText("Geçersiz E-Mail (En Az 3 Karakter)");
                    }
                    else {
                        label.setText("Mail İmzası Seç");
                        int choose = JOptionPane.showOptionDialog(
                                null,
                                label,
                                "İmza Seç",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/choose.png"))),
                                new Object[]{"gmail.com", "@hotmail.com"},
                                null);

                        if (choose == JOptionPane.YES_OPTION){
                            mail += "@gmail.com";
                            isError = false;
                        }
                        else if (choose == JOptionPane.NO_OPTION){
                            mail += "@hotmail.com";
                            isError = false;
                        }

                    }

                    if (isError){
                        JOptionPane.showMessageDialog(null, label, "HATA", JOptionPane.INFORMATION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
                    }
                    else {
                        try {
                            Query.getInstance().update("loginInfos", "email", mail, "user_id", Login.getCurrentUserId(), Query.DataType.STRING);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        label.setText("E-Mail Başarıyla Eklendi! Sayfa Kapanınca Güncellenir.");
                        JOptionPane.showMessageDialog(null, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                    }
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

    private boolean isNumericF(String text){
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }
        return false;
    }

    private boolean isLength10(String phone){
        return phone.length() == 10;
    }

    private boolean isLength50(String str){
        return str.length() <= 50;
    }

    private boolean isLetter(String str){
        return str.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ ]+");
    }

    private boolean checkMail(String email) {
        return !email.contains("@") && !email.contains("hotmail") && !email.contains("gmail") &&
                !email.contains(".");
    }
}
