package gokbudak.menu;

import gokbudak.manager.FormManager;
import net.miginfocom.swing.MigLayout;
import raven.swing.blur.BlurBackground;
import raven.swing.blur.style.StyleOverlay;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class Menu extends BlurBackground {

    private SystemMenu systemMenu;
    private Title title;
    private JDesktopPane desktopPane;

    public Menu(String username) throws SQLException {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/gokbudak/images/green.jpeg")));
        setOverlay(new StyleOverlay(new Color(20, 20, 20), 0.5f));
        setImage(icon.getImage());
        init(username);
    }

    private void init(String username) throws SQLException {
        setLayout(new MigLayout("fill, insets 0 0 6 6", "[fill]", "[fill]"));
        systemMenu = new SystemMenu(username);
        title = new Title();
        desktopPane = new JDesktopPane();

        desktopPane.setLayout(null);
        desktopPane.setOpaque(false);
        FormManager.getInstance().setDesktopPane(desktopPane);

        add(systemMenu, "dock west, gap 6 6 32 6, width 280!");
        add(title, "dock north, gap 0 6 32 6, height 50!");
        add(desktopPane);
    }
}
