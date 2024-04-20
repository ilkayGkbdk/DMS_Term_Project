package gokbudak.login;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import gokbudak.database.Query;
import gokbudak.manager.FormManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class Register extends JPanel {

    private JTextField txtFirstName, txtSurName, txtUsername, txtID;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JRadioButton jrMale, jrFemale;
    private JButton cmdRegister;
    private ButtonGroup groupGender;


    public Register(){
        init();
    }

    private void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtFirstName = new JTextField();
        txtSurName = new JTextField();
        txtID = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Kaydol");

        cmdRegister.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        cmdRegister.addActionListener(e -> {
            setRegisterAction(panel);
        });

        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "İsim");
        txtSurName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Soyisim");
        txtID.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "TC kimlik numarası");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Kullanıcı adı veya e-posta");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Şifre");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tekrar şifre");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        JLabel lbTitle = new JLabel("Kaydol");
        JLabel description = new JLabel("Depolar için bize katılın. Hemen kaydolun!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);

        panel.add(new JLabel("İsim Soyisim"), "gapy 8");
        panel.add(txtFirstName, "split 2");
        panel.add(txtSurName);
        panel.add(new JLabel("TC Kimlik No"), "gapy 8");
        panel.add(txtID);

        panel.add(new JLabel("Cinsiyet"), "gapy 8");
        panel.add(createGenderPanel());
        panel.add(new JSeparator(), "gapy 5 5");

        panel.add(new JLabel("Kullanıcı Adı"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Şifre"), "gapy 8");
        panel.add(txtPassword);
        panel.add(new JLabel("Tekrar Şifre"), "gapy 8");
        panel.add(txtConfirmPassword);

        panel.add(cmdRegister, "gapy 10");
        panel.add(createSignInLabel(), "gapy 10");

        add(panel);
    }

    private Component createGenderPanel(){
        JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        jrMale = new JRadioButton("Bay");
        jrFemale = new JRadioButton("Bayan");

        groupGender = new ButtonGroup();
        groupGender.add(jrFemale);
        groupGender.add(jrMale);

        jrMale.setSelected(true);

        panel.add(jrMale);
        panel.add(jrFemale);

        return panel;
    }

    private Component createSignInLabel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "background:null");

        JButton cmdLogin = new JButton("<html><a href = \"#\">Giriş Yap</a></html>");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "border:3,3,3,3");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(e -> {
            FormManager.getInstance().showForm(new Login());
        });

        JLabel label = new JLabel("Hesabınız var mı?");
        label.putClientProperty(FlatClientProperties.STYLE,
                "[light]foreground:lighten(@foreground, 30%);" +
                "[dark]foreground:darken(@foreground, 30%)");

        panel.add(label);
        panel.add(cmdLogin);

        return panel;
    }

    private void setRegisterAction(JPanel panel){
        JLabel label = new JLabel();
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));

        boolean isError = true;

        String firstName = txtFirstName.getText();
        String lastName = txtSurName.getText();
        String id = txtID.getText();
        String gender = getGender();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String rePassword = new String(txtConfirmPassword.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty()){
            label.setText("İsim veya Soyisim Eksik");
        }
        else if (id.isEmpty()){
            label.setText("TC Kimlik Numarası Eksik");
        }
        else if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
            label.setText("Kullanıcı Adı veya Şifre Eksik");
        }
        else if (!isIdNumeric()){
            label.setText("TCKN Sadece Rakamlardan Oluşabilir");
        }
        else if (!isIdLengthOK()){
            label.setText("TCKN 11 Haneli Olmalıdır");
        }
        else if (!isPasswordMatched()){
            label.setText("Şifreler Uyuşmuyor");
        }
        else {
            try {
                Query.getInstance().insert(firstName, lastName, gender, id);
                Query.getInstance().insert(username, password, id);

                label.setText("Giriş Yapabilirsiniz");
                JOptionPane.showMessageDialog(panel, label, "BAŞARILI", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/success.png"))));
                FormManager.getInstance().showForm(new Login());
                isError = false;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (isError) {
            JOptionPane.showMessageDialog(panel, label, "HATA", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
        }
    }

    private boolean isPasswordMatched(){
        return new String(txtPassword.getPassword()).equals(new String(txtConfirmPassword.getPassword()));
    }

    private boolean isIdNumeric(){
        for (int i = 0; i < txtID.getText().length(); i++) {
            if (!Character.isDigit(txtID.getText().charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isIdLengthOK(){
        return txtID.getText().length() == 11;
    }

    private String getGender(){
        String gender;

        if (jrMale.isSelected()){
            gender = "Bay";
        }
        else {
            gender = "Bayan";
        }

        return gender;
    }
}
