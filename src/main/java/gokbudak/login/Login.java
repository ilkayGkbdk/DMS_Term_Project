package gokbudak.login;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import gokbudak.database.Query;
import gokbudak.database.MSSQLConnection;
import gokbudak.manager.FormManager;
import gokbudak.menu.Menu;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Objects;

public class Login extends JPanel{

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox cbRememberMe;
    private JButton cmdLogin;
    private MSSQLConnection connection;
    private String username, password;

    private static String currentUserId;

    public Login(){
        init();
    }

    private void init(){
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cbRememberMe = new JCheckBox("Beni Hatırla");
        cmdLogin = new JButton("Giriş");

        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "fill, 250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 3%);" +
                "[dark]background:lighten(@background, 3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Kullanıcı adı veya e-posta");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Şifre");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        JLabel lbTitle = new JLabel("Hoşgeldiniz!");
        JLabel lbDescription = new JLabel("Hesabınıza erişmek için lütfen giriş yapın.");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground, 30%);" +
                "[dark]foreground:darken(@foreground, 30%)");

        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        cmdLogin.addActionListener(e -> {
            setCmdLoginAction(panel);
        });

        txtPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    setCmdLoginAction(panel);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });


        panel.add(lbTitle);
        panel.add(lbDescription);
        panel.add(new JLabel("Kullanıcı Adı"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Şifre"), "gapy 8");
        panel.add(txtPassword);
        panel.add(cbRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignUpLabel(), "gapy 10");

        add(panel);
    }

    private Component createSignUpLabel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        JButton cmdRegister = new JButton("<html><a href = \"#\">Kaydol</a></html>");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
            FormManager.getInstance().showForm(new Register());
        });

        JLabel label = new JLabel("Hesabınız yok mu?");
        label.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground, 30%);" +
                "[dark]foreground:darken(@foreground, 30%)");

        panel.add(label);
        panel.add(cmdRegister);

        return panel;
    }

    private void setCmdLoginAction(JPanel panel){
        JLabel label = new JLabel();
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));

        boolean isError = true;
        username = txtUsername.getText();
        password = new String(txtPassword.getPassword());

        if (username.isEmpty() && password.isEmpty()){
            label.setText("Giriş bilgilerini gir");
        }
        else if (username.isEmpty()){
            label.setText("Kullanıcı adı gir");
        }
        else if (password.isEmpty()){
            label.setText("Şifre gir");
        }
        else {
            try {
                if (!Query.getInstance().isHave("username", "loginInfos", username, Query.DataType.STRING)){
                    label.setText("Kullanıcı adı yanlış");
                }
                else if (!Query.getInstance().select("*", "loginInfos", "username", username, Query.DataType.STRING).equals(password)){
                    label.setText("Şifre yanlış");
                }
                else {
                    currentUserId = Query.getInstance().select("user_id", "loginInfos", "username", username, Query.DataType.INTEGER);
                    FormManager.getInstance().showForm(new Menu(username));
                    isError = false;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (isError) {
            JOptionPane.showMessageDialog(panel, label, "HATA", JOptionPane.ERROR_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/error.png"))));
        }
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }
}
