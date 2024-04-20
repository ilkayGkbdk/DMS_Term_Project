package gokbudak.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.util.SystemInfo;
import gokbudak.database.MSSQLConnection;
import gokbudak.login.Login;
import gokbudak.manager.FormManager;
import gokbudak.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Application extends JFrame{

    public Application() throws SQLException {
        init();
    }

    private void init() throws SQLException {
        setTitle(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        System.setProperty( "apple.awt.application.appearance", "system" );
        System.setProperty( "apple.awt.application.name", "Depo Uygulaması" );
        if (SystemInfo.isMacFullWindowContentSupported) {
            getRootPane().putClientProperty( "apple.awt.fullWindowContent", true );
            getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true );
        }

        setLocationRelativeTo(null);
        setContentPane(new Login());
        FormManager.getInstance().initApplication(this);
    }

    public static void main(String[] args) {

        try {
            MSSQLConnection.getInstance().connectDatabase();
            System.out.println("Bağlantı Başarılı..");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("gokbudak.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();

        EventQueue.invokeLater(() -> {
            try {
                new Application().setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
