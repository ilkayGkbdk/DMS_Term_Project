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
        FlatAnimatedLafChange.showSnapshot();
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (JInternalFrame frame : frames) {
            frame.dispose();
        }

        JInternalFrame internalFrame = new JInternalFrame(title, false, true, false, false);
        internalFrame.setSize(desktopPane.getSize());
        internalFrame.setMaximum(true);

        internalFrame.add(component);
        internalFrame.setVisible(true);

        desktopPane.add(internalFrame);
        desktopPane.moveToFront(internalFrame);
        internalFrame.setSelected(true);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public void setDesktopPane(JDesktopPane desktopPane){
        this.desktopPane = desktopPane;
    }
}
