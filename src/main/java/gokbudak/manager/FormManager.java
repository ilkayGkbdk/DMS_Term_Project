package gokbudak.manager;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import gokbudak.main.Application;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.beans.PropertyVetoException;

public class FormManager {

    private Application application;
    private JDesktopPane desktopPane;
    private static FormManager instance;

    public static FormManager getInstance() {
        if (instance == null){
            instance = new FormManager();
        }
        return instance;
    }

    private FormManager(){

    }

    public void initApplication(Application application){
        this.application = application;
    }

    public void showForm(JComponent form){
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            application.setContentPane(form);
            application.revalidate();
            application.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public void showForm(String title, Component component) throws PropertyVetoException {
        JInternalFrame internalFrame = new JInternalFrame(title, true, true, true, true);
        internalFrame.setSize(desktopPane.getSize());
        internalFrame.setMaximum(true);
        internalFrame.add(component);
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame, 0);
    }

    public void setDesktopPane(JDesktopPane desktopPane){
        this.desktopPane = desktopPane;
    }
}
