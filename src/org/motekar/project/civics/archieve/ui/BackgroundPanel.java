package org.motekar.project.civics.archieve.ui;

import com.jhlabs.image.ShadowFilter;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.TextPainter;
import org.jdesktop.swingx.painter.effects.AreaEffect;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.openide.util.Exceptions;
import org.pushingpixels.lafwidget.contrib.blogofbug.utility.ImageUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

/**
 *
 * @author Muhamad Wibawa
 */
public class BackgroundPanel extends JXPanel {

    private ArchieveProperties properties;
    private JXPanel topLeftPanel = new JXPanel();
    private JXPanel rightBottomPanel = new JXPanel();

    public BackgroundPanel(ArchieveProperties properties) {
        this.properties = properties;
        construct();
    }

    private void construct() {
        try {
            paintMainPanel();
            repaintTopLeftPanel();
            repaintRightBottomPanel();

            Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

            int x = 20;
            int y = 20;

            int x2 = (int) (r.getWidth() - (r.getWidth() * 0.31));
            int y2 = (int) (r.getHeight() - (r.getHeight() * 0.4));

            setDoubleBuffered(true);
            removeAll();
            setLayout(null);
            add(topLeftPanel);
            add(rightBottomPanel);
            topLeftPanel.setBounds(x, y, 500, 120);
            rightBottomPanel.setBounds(x2, y2, 400, 256);

            repaint();
            revalidate();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void paintMainPanel() {
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getEnabledColorScheme(DecorationAreaType.HEADER);

        GradientPaint paint = new GradientPaint(new Point(0, 0), color.getBackgroundFillColor(),
                new Point(0, (int) (r.getHeight())), color.getWatermarkDarkColor());

        MattePainter matte = new MattePainter(paint);
        matte.setAntialiasing(true);
        setBackgroundPainter(matte);
    }

    private void repaintTopLeftPanel() throws IOException {

        BufferedImage buff = ImageIO.read(properties.getLogo3());
        BufferedImage scaledImage = ImageUtilities.scaledImage(buff, 80, 80);

        int imageWidth = scaledImage.getWidth();

        ImagePainter painter = new ImagePainter(scaledImage, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        painter.setAntialiasing(true);
        painter.setScaleType(ImagePainter.ScaleType.InsideFit);

        ShadowFilter shadow = new ShadowFilter();
        painter.setFilters(shadow);
        topLeftPanel.setAlpha(0.65f);

        Object value = javax.swing.UIManager.get("CommandButtonPanel.font");

        Font defFont = new Font("Times New Roman", Font.BOLD, 16);

        if (value instanceof Font) {
            defFont = (Font) value;
        }

        TextPainter txtPainter = new TextPainter(getTextStateName());
        txtPainter.setFont(new Font(defFont.getName(), Font.BOLD, 16));
        txtPainter.setHorizontalAlignment(HorizontalAlignment.LEFT);
        txtPainter.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter.setFilters(shadow);

        TextPainter txtPainter2 = new TextPainter(getTextDinasName());
        txtPainter2.setFont(new Font(defFont.getName(), Font.BOLD, 14));
        txtPainter2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        txtPainter2.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter2.setFilters(shadow);

        TextPainter txtPainter3 = new TextPainter(getTextAddress());
        txtPainter3.setFont(new Font(defFont.getName(), Font.PLAIN, 12));
        txtPainter3.setHorizontalAlignment(HorizontalAlignment.LEFT);
        txtPainter3.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter3.setFilters(shadow);

        txtPainter.setInsets(new Insets(5, imageWidth + 10, 0, 0));
        txtPainter2.setInsets(new Insets(25, imageWidth + 10, 0, 0));
        txtPainter3.setInsets(new Insets(45, imageWidth + 10, 0, 0));

        CompoundPainter cmpPainter = new CompoundPainter<Object>(painter, txtPainter, txtPainter2, txtPainter3);

        topLeftPanel.setBackgroundPainter(cmpPainter);
    }

    private String getTextStateName() {
        StringBuilder builder = new StringBuilder();

        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
            builder.append("PEMERINTAH KABUPATEN ").
                    append(properties.getState().toUpperCase());
        } else {
            builder.append("PEMERINTAH KOTA ").
                    append(properties.getState().toUpperCase());
        }

        return builder.toString().toUpperCase();
    }

    private String getTextDinasName() {
        StringBuilder builder = new StringBuilder();

        builder.append(properties.getCompany());

        return builder.toString();
    }

    private String getTextAddress() {
        StringBuilder builder = new StringBuilder();

        builder.append(properties.getAddress());

        return builder.toString();
    }

    private void repaintRightBottomPanel() throws IOException {
        BufferedImage buff = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Magnolia.png"));
        BufferedImage scaledImage = ImageUtilities.scaledImage(buff, 100, 100);

        int imageWidth = scaledImage.getWidth();

        AreaEffect effect = new GlowPathEffect();

        ImagePainter painter = new ImagePainter(scaledImage, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
        painter.setAntialiasing(true);
        painter.setScaleType(ImagePainter.ScaleType.InsideFit);

        ShadowFilter shadow = new ShadowFilter();
        painter.setFilters(shadow);


        Object value = javax.swing.UIManager.get("CommandButtonPanel.font");

        Font defFont = new Font("Times New Roman", Font.BOLD, 16);

        if (value instanceof Font) {
            defFont = (Font) value;
        }

        TextPainter txtPainter = new TextPainter("G    A    R    I    S");
        txtPainter.setFont(new Font(defFont.getName(), Font.BOLD, 28));
        txtPainter.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        txtPainter.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter.setFilters(shadow);
        txtPainter.setAreaEffects(effect);

        TextPainter txtPainter2 = new TextPainter("Government Archieves Information System");
        txtPainter2.setFont(new Font(defFont.getName(), Font.BOLD, 14));
        txtPainter2.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        txtPainter2.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter2.setFilters(shadow);
        txtPainter2.setAreaEffects(effect);

        TextPainter txtPainter3 = new TextPainter("Versi 1.0.5");
        txtPainter3.setFont(new Font(defFont.getName(), Font.BOLD, 16));
        txtPainter3.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        txtPainter3.setVerticalAlignment(VerticalAlignment.TOP);
        txtPainter3.setFilters(shadow);
        txtPainter3.setAreaEffects(effect);

        txtPainter.setInsets(new Insets(0, 0, 0, imageWidth + 10));
        txtPainter2.setInsets(new Insets(40, 0, 0, imageWidth + 10));
        txtPainter3.setInsets(new Insets(60, 0, 0, imageWidth + 10));

        CompoundPainter cmpPainter = new CompoundPainter<Object>(painter, txtPainter, txtPainter2, txtPainter3);

        rightBottomPanel.setAlpha(0.65f);
        rightBottomPanel.setBackgroundPainter(cmpPainter);
    }

    public void repaintAll() {
        construct();
    }

    public void setProperties(ArchieveProperties properties) {
        this.properties = properties;
    }
}
