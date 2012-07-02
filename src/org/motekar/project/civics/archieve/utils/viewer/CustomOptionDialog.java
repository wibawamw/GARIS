package org.motekar.project.civics.archieve.utils.viewer;

import java.awt.Component;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.JXLabel;

/**
 *
 * @author Muhamad Wibawa
 */
public class CustomOptionDialog {

    private CustomOptionDialog() {
        //
    }

    public static void showDialog(Component parent,String message) {
        JXLabel label = new JXLabel();
        label.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);
        label.setLineWrap(true);
        label.setMaxLineSpan(400);
        label.setText(message);
        JOptionPane.showMessageDialog(parent, label, "Perhatian", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showDialog(Component parentComponent,String message,String title) {
        JXLabel label = new JXLabel();
        label.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);
        label.setLineWrap(true);
        label.setMaxLineSpan(400);
        label.setText(message);
        JOptionPane.showMessageDialog(parentComponent, label, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showDialog(Component parentComponent,String message,String title, int optionType) {
        JXLabel label = new JXLabel();
        label.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);
        label.setLineWrap(true);
        label.setMaxLineSpan(400);
        label.setText(message);
        JOptionPane.showMessageDialog(parentComponent, label, title, optionType);
    }
}
