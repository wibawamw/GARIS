package org.motekar.project.civics.archieve.utils.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ImageViewerDlg implements ActionListener {

    private JDialog dlg;
    private JXPanel viewerPanel = new JXPanel();
    private CompoundPainter cmpPainter = null;
    private JFileChooser chooser;
    private JFileChooser saveChooser;
    private JXButton btOpen = new JXButton("Buka File");
    private JXButton btSave = new JXButton("Simpan Ke File");
    private JXButton btDelete = new JXButton("Hapus Gambar");
    private JXButton btOK = new JXButton("OK");
    private JXButton btCancel = new JXButton("Batal");
    private ArchieveMainframe mainframe;
    private byte[] imageStream = null;
    private File imageFile = null;
    private String imageFileExt = "";
    private int response = JOptionPane.CANCEL_OPTION;
    private boolean viewOnly = false;

    public ImageViewerDlg(ArchieveMainframe mainframe, byte[] imageStream, String imageFileExt) {
        this.mainframe = mainframe;
        this.imageStream = imageStream;
        this.imageFileExt = imageFileExt;
    }

    public ImageViewerDlg(ArchieveMainframe mainframe, byte[] imageStream, String imageFileExt, boolean viewOnly) {
        this.mainframe = mainframe;
        this.imageStream = imageStream;
        this.imageFileExt = imageFileExt;
        this.viewOnly = viewOnly;
    }

    public void showDialog() {
        dlg = new JDialog(mainframe);

        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(800, 600));
        dlg.setModal(true);
        dlg.setResizable(false);
        dlg.setTitle("Preview Gambar");

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private JXPanel construct() {

        constructSaveFileChooser();

        cmpPainter = new CompoundPainter();

        drawImage();

        setButtonState();

        btOpen.addActionListener(this);
        btSave.addActionListener(this);
        btDelete.addActionListener(this);

        btOK.addActionListener(this);
        btCancel.addActionListener(this);

        JXTitledPanel titledPanel = new JXTitledPanel(imageFileExt);

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(viewerPanel, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        JXPanel panelButton = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        if (viewOnly) {
            btCancel.setText("Tutup");
            panelButton.add(btCancel);
        } else {
            panelButton.add(btOpen);
            panelButton.add(btSave);
            panelButton.add(btDelete);
            panelButton.add(new JSeparator());
            panelButton.add(btOK);
            panelButton.add(btCancel);
        }


        JXPanel mainPanel = new JXPanel();

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titledPanel, BorderLayout.CENTER);
        mainPanel.add(panelButton, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void constructSaveFileChooser() {
        if (saveChooser == null) {
            saveChooser = new JFileChooser();
        }

        saveChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg")
                        || fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".bmp"));
            }

            @Override
            public String getDescription() {
                return "Image File";
            }
        });

    }

    private void setButtonState() {
        if (imageStream == null) {
            btSave.setEnabled(false);
            btDelete.setEnabled(false);
        } else {
            btSave.setEnabled(true);
            btDelete.setEnabled(true);
        }
    }

    private void drawImage() {
        if (imageStream != null) {
            try {
                InputStream inputStream = new ByteArrayInputStream(imageStream);
                BufferedImage img = ImageIO.read(inputStream);

                ImagePainter painter = new ImagePainter(img);
                painter.setScaleToFit(true);
                painter.setFillHorizontal(true);
                painter.setFillVertical(true);
                painter.setAntialiasing(true);
                cmpPainter.setPainters(painter);

                viewerPanel.setBackgroundPainter(cmpPainter);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOpen) {
            onOpen();
        } else if (source == btSave) {
            onSave();
        } else if (source == btCancel) {
            dlg.dispose();
        } else if (source == btOK) {
            onOK();
        } else if (source == btDelete) {
            onDelete();
        }
    }

    private void onSave() {
        if (imageStream != null) {
            int retVal = saveChooser.showSaveDialog(this.dlg);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = saveChooser.getSelectedFile();
                try {

                    File fileToCopy = new File(file.getParent() + File.separator + file.getName() + "." + imageFileExt);

                    OutputStream out = new FileOutputStream(fileToCopy);
                    out.write(imageStream);

                    out.close();

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private void onOpen() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg")
                        || fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".bmp"));
            }

            @Override
            public String getDescription() {
                return "Image File";
            }
        });

        int retVal = chooser.showOpenDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            imageFile = chooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(imageFile);

                imageFileExt = extractFileExtension(imageFile.getCanonicalPath());

                int width = img.getWidth();
                int height = img.getHeight();

                if (width <= 1024 && height <= 1024) {
                    FileInputStream fileInputStream = new FileInputStream(imageFile);

                    imageStream = new byte[(int) imageFile.length()];
                    fileInputStream.read(imageStream);

                    ImagePainter painter = new ImagePainter(img);
                    painter.setScaleToFit(true);
                    painter.setFillHorizontal(true);
                    painter.setFillVertical(true);
                    painter.setAntialiasing(true);
                    cmpPainter.setPainters(painter);

                    viewerPanel.setBackgroundPainter(cmpPainter);
                } else {
                    CustomOptionDialog.showDialog(mainframe, "Ukuran Gambar Tidak boleh lebih dari 1024x1024", "Perhatian", JOptionPane.WARNING_MESSAGE);
                }

                setButtonState();

            } catch (Exception ex) {
            }
        }
    }

    private void onOK() {
        response = JOptionPane.OK_OPTION;
        dlg.dispose();
    }

    private void onDelete() {
        viewerPanel.setBackgroundPainter(null);
        imageStream = null;
        imageFile = null;
        setButtonState();
    }

    private String extractFileExtension(String fileFullPath) {
        String extension = "";

        StringTokenizer token = new StringTokenizer(fileFullPath, ".");

        while (token.hasMoreTokens()) {
            extension = token.nextToken();
        }

        return extension;
    }

    public byte[] getImageStream() {
        return imageStream;
    }

    public String getImageFileExt() {
        return imageFileExt;
    }

    public void setImageFileExt(String imageFileExt) {
        this.imageFileExt = imageFileExt;
    }

    public int getResponse() {
        return response;
    }
}
