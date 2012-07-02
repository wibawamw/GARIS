package org.motekar.project.civics.archieve.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author Muhamad Wibawa
 */
public class SplashScreenPanel extends JWindow {
//Get transparent image that will be use as splash screen image. 

    Image bi = Toolkit.getDefaultToolkit().getImage("splash-screen.png");
    ImageIcon ii = new ImageIcon(bi);
    JPanel panel;
    JLabel label = new JLabel(new ImageIcon("splash-screen.png"));

    public SplashScreenPanel() {

        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                setAlwaysOnTop(false);
                SplashScreenPanel.this.dispose();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setAlwaysOnTop(false);
                SplashScreenPanel.this.dispose();
            }
        };
//        addMouseListener(ml);

        panel = new JPanel() {

            @Override
            public void paintComponent(Graphics grh) {
                Paint p = new Color(0, 255, 0, 0);
                Graphics2D g2d = (Graphics2D) grh;
                g2d.setPaint(p);
            }
        };

        panel.add(label);
        add(panel);
        setSize(ii.getIconWidth(), ii.getIconHeight());
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        SplashScreenPanel tss = new SplashScreenPanel();
        com.sun.awt.AWTUtilities.setWindowOpaque(tss, false);
    }
}
