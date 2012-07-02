package org.motekar.project.civics.archieve.utils.viewer;

import de.jgrid.JGrid;
import de.jgrid.renderer.GridCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.motekar.project.civics.archieve.mail.objects.InboxFile;
import org.motekar.project.civics.archieve.mail.objects.OutboxFile;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class PicViewerRenderer extends JComponent implements GridCellRenderer {

    private static final long serialVersionUID = 1L;
    private BufferedImage image;

    @Override
    public Component getGridCellRendererComponent(JGrid grid, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        image = null;
        if (value instanceof InboxFile) {
            InboxFile file = (InboxFile) value;
            image = getImageFromFileStream(file.getFileExtension(), file.getFileStream());
        } else if (value instanceof OutboxFile) {
            OutboxFile file = (OutboxFile) value;
            image = getImageFromFileStream(file.getFileExtension(), file.getFileStream());
        } 
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();

            float widthFactor = (float) getWidth() / (float) image.getWidth();
            float heightFactor = (float) getHeight() / (float) image.getHeight();
            if (widthFactor > heightFactor) {
                width = (int) ((float) image.getWidth() * widthFactor);
                height = (int) ((float) image.getHeight() * widthFactor);
            } else {
                width = (int) ((float) image.getWidth() * heightFactor);
                height = (int) ((float) image.getHeight() * heightFactor);
            }
            g2.drawImage(ImageUtilities.getOptimalScalingImage(image, width, height), (getWidth() - width) / 2, (getHeight() - height) / 2, null);
            g2.dispose();
        }
    }

    private BufferedImage getImageFromFileStream(String fileExtension, byte[] fileStream) {
        BufferedImage bufferedImage = null;

        if (fileExtension.toLowerCase().endsWith("png") || fileExtension.toLowerCase().endsWith("jpg")
                || fileExtension.toLowerCase().endsWith("gif") || fileExtension.toLowerCase().endsWith("bmp")) {
            try {
                InputStream inputStream = new ByteArrayInputStream(fileStream);
                bufferedImage = ImageIO.read(inputStream);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (fileExtension.toLowerCase().endsWith("doc") || fileExtension.toLowerCase().endsWith("docx")) {
            try {
                bufferedImage = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Word.png"));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (fileExtension.toLowerCase().endsWith("xls") || fileExtension.toLowerCase().endsWith("xlsx")) {
            try {
                bufferedImage = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Excel.png"));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (fileExtension.toLowerCase().endsWith("pdf")) {
            try {
                bufferedImage = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Adobe Acrobat.png"));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return bufferedImage;
    }

    @Override
    public void validate() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void repaint() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    @Override
    public void repaint(Rectangle r) {
    }

    @Override
    public void firePropertyChange(String propertyName, byte oldValue,
            byte newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, char oldValue,
            char newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, short oldValue,
            short newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, int oldValue,
            int newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, long oldValue,
            long newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, float oldValue,
            float newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, double oldValue,
            double newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue,
            boolean newValue) {
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue,
            Object newValue) {
    }
}
