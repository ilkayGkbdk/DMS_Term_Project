package gokbudak.menu;

import com.formdev.flatlaf.FlatClientProperties;
import gokbudak.login.Login;
import gokbudak.manager.FormManager;
import net.miginfocom.swing.MigLayout;
import raven.drawer.component.header.SimpleHeader;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.SimpleMenu;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.drawer.component.menu.SimpleMenuStyle;
import raven.drawer.component.menu.data.Item;
import raven.swing.AvatarIcon;
import raven.swing.blur.BlurChild;
import raven.swing.blur.style.GradientColor;
import raven.swing.blur.style.Style;
import raven.swing.blur.style.StyleBorder;
import raven.swing.blur.style.StyleOverlay;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

public class AdminMenu extends BlurChild {

    public AdminMenu(String username) throws SQLException {
        super(new Style()
                .setBlur(30)
                .setBorder(new StyleBorder(10)
                        .setOpacity(0.15f)
                        .setBorderWidth(1.2f)
                        .setBorderColor(new GradientColor(new Color(200, 200, 200),
                                new Color(150, 150, 150), new Point2D.Float(0, 0), new Point2D.Float(1f, 0))))
                .setOverlay(new StyleOverlay(new Color(0, 0, 0), 0.2f))
        );

        init(username);
    }

    public void init(String username) throws SQLException {
        setLayout(new MigLayout("wrap,fill", "[fill]", "[grow 0][fill]"));

        SimpleMenu simpleMenu = new SimpleMenu(getMenuOption());
        simpleMenu.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(simpleMenu);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "trackArc:999;" +
                        "width:5;" +
                        "thumbInsets:0,0,0,0");

        SimpleHeader header = new SimpleHeader(getHeaderData(username));
        header.setOpaque(false);

        add(header);
        add(scrollPane);
    }

    private SimpleHeaderData getHeaderData(String username) {

        return new SimpleHeaderData()
                .setTitle(username)
                .setDescription("yönetici")
                .setIcon(new AvatarIcon(getClass().getResource("/gokbudak/images/admin.png"), 65, 65, 999));
    }

    private SimpleMenuOption getMenuOption(){
        raven.drawer.component.menu.data.MenuItem[] items = new raven.drawer.component.menu.data.MenuItem[]{
                new Item.Label("YÖNETİCİ"),
                new Item("Üyeler", "email.svg")
                        .subMenu("Üyeleri Gör")
                        .subMenu("Üyeleri Sil"),
                new Item("Ciro", "logout.svg"),
                new Item.Label("ÜRÜNLER"),
                new Item("Depolar", "ui.svg")
                        .subMenu("Depoları Görüntüle")
                        .subMenu("Depo Onayları"),
                new Item.Label("DİĞER"),
                new Item("ÇIKIŞ", "logout.svg")
        };
        return new SimpleMenuOption()
                .setBaseIconPath("gokbudak/icons")
                .setIconScale(0.5f)
                .setMenus(items)
                .setMenuStyle(new SimpleMenuStyle() {
                    @Override
                    public void styleMenuPanel(JPanel panel, int[] index) {
                        panel.setOpaque(false);
                    }

                    @Override
                    public void styleMenuItem(JButton menu, int[] index) {
                        menu.setContentAreaFilled(false);
                    }
                })
                .addMenuEvent((menuAction, ints) -> {

                    if (ints.length == 1){
                        int index = ints[0];

                        if (index == 1){
                            try {
                                FormManager.getInstance().showForm("", Panels.getInstance().getPanel(Panels.SystemPanel.BALANCE));
                            } catch (PropertyVetoException | SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (index == 3){
                            FormManager.getInstance().showForm(new Login());
                        }

                    }
                    else if (ints.length == 2){
                        int index = ints[0];
                        int subIndex = ints[1];

                        if (index == 0){
                            if(subIndex == 0){
                                try {
                                    FormManager.getInstance().showForm("", Panels.getInstance().getPanel(Panels.SystemPanel.PERSONAL_INFO));
                                } catch (PropertyVetoException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if(subIndex == 1){
                                try {
                                    FormManager.getInstance().showForm("", Panels.getInstance().getPanel(Panels.SystemPanel.LOGIN_INFO));
                                } catch (PropertyVetoException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        else if (index == 2){
                            if(subIndex == 0){
                                try {
                                    FormManager.getInstance().showForm("", Panels.getInstance().getPanel(Panels.SystemPanel.SHOW_WAREHOUSE));
                                } catch (PropertyVetoException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if(subIndex == 1){
                                try {
                                    FormManager.getInstance().showForm("", Panels.getInstance().getPanel(Panels.SystemPanel.ADMIN_REQUESTS));
                                } catch (PropertyVetoException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                });
    }

}
