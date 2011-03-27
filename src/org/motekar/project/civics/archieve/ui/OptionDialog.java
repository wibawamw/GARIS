package org.motekar.project.civics.archieve.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import net.n3.nanoxml.XMLException;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.misc.ArchievePropertiesBuilder;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class OptionDialog implements ActionListener {

    private ArchieveMainframe frame;
    private MasterBusinessLogic logic;
    private JXTextField fieldName = new JXTextField();
    private JXTextArea fieldAddress = new JXTextArea();
    private JXComboBox comboStateType = new JXComboBox();
    private JXTextField fieldCapital = new JXTextField();
    private JXTextField fieldState = new JXTextField();
    private JXTextField fieldProvince = new JXTextField();
    private JXComboBox comboDivision = new JXComboBox();
    private JCheckBox checkDivision = new JCheckBox("Bagian");
    private JXPanel imagePanel = new JXPanel();
    private JXPanel imagePanel2 = new JXPanel();
    private JXPanel imagePanel3 = new JXPanel();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/disk.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private ArchieveProperties properties;
    private ImagePainter painter = null;
    private ImagePainter painter2 = null;
    private ImagePainter painter3 = null;
    private static String IMAGE_FOLDER = "./images/";
    private String fileToCopy = "";
    private String fileToCopy2 = "";
    private String fileToCopy3 = "";
    private File defFile = null;
    private File defFile2 = null;
    private File defFile3 = null;

    private int response = JOptionPane.NO_OPTION;

    public OptionDialog(ArchieveMainframe frame, ArchieveProperties properties) {
        this.frame = frame;
        logic = new MasterBusinessLogic(frame.getConnection());
        this.properties = properties;
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(550, 550));
        dlg.setModal(true);
        dlg.setResizable(false);

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private JXHeader createHeader() {
        JXHeader headerPane = new JXHeader();
        headerPane.setTitle("<html><b>Konfigurasi Umum</b>");
        headerPane.setDescription("<html><i>Konfigurasi Umum merupakan konfigurasi standar aplikasi</i>");
        headerPane.setIcon(Mainframe.getResizableIconFromSource("resource/process_accept.png", new Dimension(48, 48)));
        headerPane.setIconPosition(JXHeader.IconPosition.LEFT);
        return headerPane;
    }

    private JXPanel construct() {

        loadComboStateType();
        loadComboDivision();
        setOptions();

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        imagePanel.addMouseListener(new MouseHandler());
        imagePanel2.addMouseListener(new MouseHandler());
        imagePanel3.addMouseListener(new MouseHandler());

        imagePanel.setBorder(BorderFactory.createEtchedBorder());
        imagePanel2.setBorder(BorderFactory.createEtchedBorder());
        imagePanel3.setBorder(BorderFactory.createEtchedBorder());

        panel.add(createHeader(), BorderLayout.NORTH);
        panel.add(createContentPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;

    }

    private JPanel createContentPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,pref,fill:default:grow,5px,pref,fill:default:grow,5px,pref,fill:default:grow,5px",
                "pref,5px,pref,fill:default:grow,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,fill:default:grow,5px,5px,pref,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        checkDivision.addActionListener(this);

        comboDivision.setEnabled(checkDivision.isSelected());

        lm.setRowGroups(new int[][]{{1, 3, 6, 8, 10, 12, 14, 18}});

        CellConstraints cc = new CellConstraints();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldAddress);

        builder.addLabel("Dinas/Badan/Kantor", cc.xy(1, 1));
        builder.add(fieldName, cc.xyw(3, 1,8));

        builder.addLabel("Alamat", cc.xy(1, 3));
        builder.add(scPane, cc.xywh(3, 3, 8, 2));

        builder.addLabel("Jenis Pemerintahan", cc.xy(1, 6));
        builder.add(comboStateType, cc.xyw(3, 6,8));

        builder.addLabel("Kabupaten / Kota", cc.xy(1, 8));
        builder.add(fieldState, cc.xyw(3, 8,8));

        builder.addLabel("Ibukota", cc.xy(1, 10));
        builder.add(fieldCapital, cc.xyw(3, 10,8));

        builder.addLabel("Propinsi", cc.xy(1, 12));
        builder.add(fieldProvince, cc.xyw(3, 12,8));

        builder.addLabel("Logo Daerah", cc.xy(1, 14));
        builder.add(imagePanel, cc.xywh(3, 14, 2, 3));

        builder.add(imagePanel2, cc.xywh(6, 14, 2, 3));

        builder.add(imagePanel3, cc.xywh(9, 14, 2, 3));

        builder.add(checkDivision, cc.xy(1, 18));
        builder.add(comboDivision, cc.xyw(3, 18,8));

        return builder.getPanel();
    }

    private JXPanel createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();

        okTooltip.setTitle("Simpan");
        btOK.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");


        btCancel.setActionRichTooltip(cancelTooltip);

        btOK.addActionListener(this);
        btCancel.addActionListener(this);

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    private void setOptions() {
        fieldName.setText(properties.getCompany());
        fieldAddress.setText(properties.getAddress());
        comboStateType.setSelectedItem(properties.getStateType());
        fieldState.setText(properties.getState());
        fieldCapital.setText(properties.getCapital());
        fieldProvince.setText(properties.getProvince());
        imagePanel.setBackgroundPainter(getPainter());
        imagePanel2.setBackgroundPainter(getPainter2());
        imagePanel3.setBackgroundPainter(getPainter3());

        if (!properties.getDivisionCode().equals("")) {
            checkDivision.setSelected(true);

            Division div = new Division();
            div.setCode(properties.getDivisionCode());
            div.setName(properties.getDivisionName());

            comboDivision.setSelectedItem(div);
        } else {
            checkDivision.setSelected(false);
            comboDivision.setSelectedIndex(0);
        }
    }

    private void resetProperties() {
        properties.setCompany(fieldName.getText());
        properties.setAddress(fieldAddress.getText());
        properties.setStateType((String) comboStateType.getSelectedItem());
        properties.setState(fieldState.getText());
        properties.setCapital(fieldCapital.getText());
        properties.setProvince(fieldProvince.getText());

        if (checkDivision.isSelected()) {
            Object obj = comboDivision.getSelectedItem();
            Division div = null;
            if (obj instanceof Division) {
                div = (Division) obj;
            }

            if (div != null) {
                properties.setDivisionCode(div.getCode());
                properties.setDivisionName(div.getName());
            }
        } else {
            properties.setDivisionCode("");
            properties.setDivisionName("");
        }

        File originalFile = new File(fileToCopy);
        File fileCopied = new File(IMAGE_FOLDER + originalFile.getName());

        File originalFile2 = new File(fileToCopy2);
        File fileCopied2 = new File(IMAGE_FOLDER + originalFile2.getName());

        File originalFile3 = new File(fileToCopy3);
        File fileCopied3 = new File(IMAGE_FOLDER + originalFile3.getName());

        if (originalFile.exists()) {
            copyfile(originalFile, fileCopied);
        } else {
            fileCopied = defFile;
        }

        if (originalFile2.exists()) {
            copyfile(originalFile2, fileCopied2);
        } else {
            fileCopied2 = defFile2;
        }

        if (originalFile3.exists()) {
            copyfile(originalFile3, fileCopied3);
        } else {
            fileCopied3 = defFile3;
        }


        properties.setLogo(fileCopied);
        properties.setLogo2(fileCopied2);
        properties.setLogo3(fileCopied3);

    }

    private Painter getPainter() {
        try {

            File file = properties.getLogo();
            defFile = properties.getLogo();

            if (!file.exists() || file.isDirectory()) {
                file = new File("./images/logo_daerah.jpg");
                defFile = new File("./images/logo_daerah.jpg");
            }

            BufferedImage img = ImageIO.read(file);

            painter = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
            painter.setScaleToFit(true);
            painter.setFillHorizontal(true);
            painter.setFillVertical(true);
            painter.setAntialiasing(true);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter;
    }

    private Painter getPainter2() {
        try {

            File file = properties.getLogo2();
            defFile2 = properties.getLogo2();

            if (!file.exists() || file.isDirectory()) {
                file = new File("./images/logo_daerah.jpg");
                defFile2 = new File("./images/logo_daerah.jpg");
            }

            BufferedImage img = ImageIO.read(file);

            painter2 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
            painter2.setScaleToFit(true);
            painter2.setFillHorizontal(true);
            painter2.setFillVertical(true);
            painter2.setAntialiasing(true);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter2;
    }

    private Painter getPainter3() {
        try {

            File file = properties.getLogo3();
            defFile3 = properties.getLogo3();

            if (!file.exists() || file.isDirectory()) {
                file = new File("./images/logo_daerah.jpg");
                defFile3 = new File("./images/logo_daerah.jpg");
            }

            BufferedImage img = ImageIO.read(file);

            painter3 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
            painter3.setScaleToFit(true);
            painter3.setFillHorizontal(true);
            painter3.setFillVertical(true);
            painter3.setAntialiasing(true);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter3;
    }

    private void loadComboStateType() {
        comboStateType.removeAllItems();
        comboStateType.addItem("");
        comboStateType.addItem("Kabupaten");
        comboStateType.addItem("Kotamadya");
    }

    private void loadComboDivision() {
        comboDivision.removeAllItems();
        try {

            ArrayList<Division> divisions = logic.getDivision(frame.getSession(), false);
            if (!divisions.isEmpty()) {
                for (Division division : divisions) {
                    comboDivision.addItem(division);
                }

            }
            comboDivision.insertItemAt(new Division(), 0);
            comboDivision.setSelectedIndex(0);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            dlg.dispose();
        } else if (source == checkDivision) {
            comboDivision.setSelectedIndex(0);
            comboDivision.setEnabled(checkDivision.isSelected());
        }
    }

    private void onOK() {
        try {
            resetProperties();

            ArchievePropertiesBuilder builder = new ArchievePropertiesBuilder(properties);
            builder.createXML();

            frame.setProperties(properties);

            response = JOptionPane.YES_OPTION;

            dlg.dispose();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (XMLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidKeyException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalBlockSizeException ex) {
            Exceptions.printStackTrace(ex);
        } catch (BadPaddingException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public int getResponse() {
        return response;
    }

    public ArchieveProperties getProperties() {
        return properties;
    }

    private static void copyfile(File f1, File f2) {
        try {
            InputStream in = new FileInputStream(f1);

            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles click events on the component
     */
    private class MouseHandler extends MouseAdapter {

        private Cursor oldCursor;
        private JFileChooser chooser;

        @Override
        public void mouseClicked(MouseEvent evt) {
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

            int retVal = chooser.showOpenDialog(OptionDialog.this.dlg);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    if (!file.exists()) {
                        file = new File("./images/logo_daerah.jpg");
                    }

                    if (evt.getSource() == imagePanel) {
                        fileToCopy = file.getPath();

                        BufferedImage img = ImageIO.read(file);
                        painter.setImage(img);
                        painter.setScaleToFit(true);
                        painter.setFillHorizontal(true);
                        painter.setFillVertical(true);
                        painter.setAntialiasing(true);

                    } else if (evt.getSource() == imagePanel2) {
                        fileToCopy2 = file.getPath();

                        BufferedImage img = ImageIO.read(file);
                        painter2.setImage(img);
                        painter2.setScaleToFit(true);
                        painter2.setFillHorizontal(true);
                        painter2.setFillVertical(true);
                        painter2.setAntialiasing(true);
                    } else if (evt.getSource() == imagePanel3) {
                        fileToCopy3 = file.getPath();

                        BufferedImage img = ImageIO.read(file);
                        painter3.setImage(img);
                        painter3.setScaleToFit(true);
                        painter3.setFillHorizontal(true);
                        painter3.setFillVertical(true);
                        painter3.setAntialiasing(true);
                    }
                } catch (Exception ex) {
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
            if (oldCursor == null) {
                oldCursor = dlg.getCursor();
                dlg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            if (oldCursor != null) {
                dlg.setCursor(oldCursor);
                oldCursor = null;
            }
        }
    }
}
