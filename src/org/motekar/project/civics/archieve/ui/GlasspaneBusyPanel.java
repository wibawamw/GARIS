package org.motekar.project.civics.archieve.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;

/**
 *
 * @author Muhamad Wibawa
 */

public class GlasspaneBusyPanel extends JXPanel {
    
    private JXBusyLabel busyLabel;
    
    public GlasspaneBusyPanel() {
        setListener();
        paintPanel();
    }
    
    private void paintPanel() {
        createBusyPanel();
        
    }
    
    private void createBusyPanel() {
        
        busyLabel = new JXBusyLabel(new Dimension(100, 100));
        busyLabel.setName("busyLabel");
        busyLabel.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
        busyLabel.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
        
        busyLabel.getBusyPainter().setTrailLength(5);
        busyLabel.getBusyPainter().setPoints(20);
        
        busyLabel.setBusy(true);
        
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int x2 = (r.width - 100) / 2;
        int y2 = (r.height - 100) / 2;
        setLayout(null);
        add(busyLabel);
        busyLabel.setBounds(x2, y2, 100, 100);
    }
    
    private void setListener() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });
        setFocusTraversalKeysEnabled(false);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });
    }
}
