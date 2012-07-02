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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.sqlapi.ConfigBusinessLogic;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
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
    private JXTextField fieldName = new JXTextField();
    private JXTextArea fieldAddress = new JXTextArea();
    private JXComboBox comboStateType = new JXComboBox();
    private JXTextField fieldCapital = new JXTextField();
    private JXTextField fieldState = new JXTextField();
    private JXTextField fieldProvince = new JXTextField();
    private JXPanel imagePanel = new JXPanel();
    private JXPanel imagePanel2 = new JXPanel();
    private JXPanel imagePanel3 = new JXPanel();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/disk.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private ProfileAccount profileAccount;
    private ImagePainter painter = null;
    private ImagePainter painter2 = null;
    private ImagePainter painter3 = null;
    private byte[] logoStream = null;
    private byte[] logoStream2 = null;
    private byte[] logoStream3 = null;
    private int response = JOptionPane.NO_OPTION;
    //
    private ConfigBusinessLogic cLogic;

    public OptionDialog(ArchieveMainframe frame, ProfileAccount profileAccount) {
        this.frame = frame;
        this.profileAccount = profileAccount;
        this.cLogic = new ConfigBusinessLogic(frame.getConnection());
    }

    public void showDialog() {

        init();

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

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createHeader(), BorderLayout.NORTH);
        panel.add(createContentPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private void init() {
        loadComboStateType();
        setOptions();

        imagePanel.setBorder(BorderFactory.createEtchedBorder());
        imagePanel2.setBorder(BorderFactory.createEtchedBorder());
        imagePanel3.setBorder(BorderFactory.createEtchedBorder());
        
        try {
            imagePanel.addMouseListener(new MouseHandler());
            imagePanel2.addMouseListener(new MouseHandler());
            imagePanel3.addMouseListener(new MouseHandler());
        } catch (ExceptionInInitializerError ex) {
            Exceptions.printStackTrace(ex.getException());
        }

        
    }

    private JPanel createContentPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,pref,fill:default:grow,5px,pref,fill:default:grow,5px,pref,fill:default:grow,5px",
                "pref,5px,pref,fill:default:grow,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,fill:default:grow,5px,5px,pref,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 6, 8, 10, 12, 14, 18}});

        CellConstraints cc = new CellConstraints();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldAddress);

        builder.addLabel("Dinas/Badan/Kantor", cc.xy(1, 1));
        builder.add(fieldName, cc.xyw(3, 1, 8));

        builder.addLabel("Alamat", cc.xy(1, 3));
        builder.add(scPane, cc.xywh(3, 3, 8, 2));

        builder.addLabel("Jenis Pemerintahan", cc.xy(1, 6));
        builder.add(comboStateType, cc.xyw(3, 6, 8));

        builder.addLabel("Kabupaten / Kota", cc.xy(1, 8));
        builder.add(fieldState, cc.xyw(3, 8, 8));

        builder.addLabel("Ibukota", cc.xy(1, 10));
        builder.add(fieldCapital, cc.xyw(3, 10, 8));

        builder.addLabel("Propinsi", cc.xy(1, 12));
        builder.add(fieldProvince, cc.xyw(3, 12, 8));

        builder.addLabel("Logo Daerah", cc.xy(1, 14));
        builder.add(imagePanel, cc.xywh(3, 14, 2, 3));

        builder.add(imagePanel2, cc.xywh(6, 14, 2, 3));

        builder.add(imagePanel3, cc.xywh(9, 14, 2, 3));

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
        if (profileAccount != null) {
            fieldName.setText(profileAccount.getCompany());
            fieldAddress.setText(profileAccount.getAddress());
            comboStateType.setSelectedItem(profileAccount.getStateType());
            fieldState.setText(profileAccount.getState());
            fieldCapital.setText(profileAccount.getCapital());
            fieldProvince.setText(profileAccount.getProvince());
            imagePanel.setBackgroundPainter(getPainter());
            imagePanel2.setBackgroundPainter(getPainter2());
            imagePanel3.setBackgroundPainter(getPainter3());

        } else {
            fieldName.setText("");
            fieldAddress.setText("");
            comboStateType.setSelectedIndex(0);
            fieldState.setText("");
            fieldCapital.setText("");
            fieldProvince.setText("");
            imagePanel.setBackgroundPainter(getPainter());
            imagePanel2.setBackgroundPainter(getPainter2());
            imagePanel3.setBackgroundPainter(getPainter3());
        }
    }

    private void resetProperties() {

        if (profileAccount == null) {
            profileAccount = new ProfileAccount();
        }

        profileAccount.setCompany(fieldName.getText());
        profileAccount.setAddress(fieldAddress.getText());
        profileAccount.setStateType((String) comboStateType.getSelectedItem());
        profileAccount.setState(fieldState.getText());
        profileAccount.setCapital(fieldCapital.getText());
        profileAccount.setProvince(fieldProvince.getText());

        profileAccount.setByteLogo(logoStream);
        profileAccount.setByteLogo2(logoStream2);
        profileAccount.setByteLogo3(logoStream3);

    }

    private Painter getPainter() {
        try {
            if (profileAccount != null) {
                logoStream = profileAccount.getByteLogo();

                if (logoStream == null) {
                    File file = new File("./images/logo_daerah.jpg");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    logoStream = new byte[(int) file.length()];
                    fileInputStream.read(logoStream);
                }

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(logoStream));

                painter = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter.setScaleToFit(true);
                painter.setFillHorizontal(true);
                painter.setFillVertical(true);
                painter.setAntialiasing(true);

            } else {
                File file = new File("./images/logo_daerah.jpg");

                FileInputStream fileInputStream = new FileInputStream(file);

                logoStream = new byte[(int) file.length()];
                fileInputStream.read(logoStream);

                BufferedImage img = ImageIO.read(file);

                painter = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter.setScaleToFit(true);
                painter.setFillHorizontal(true);
                painter.setFillVertical(true);
                painter.setAntialiasing(true);
            }


        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter;
    }

    private Painter getPainter2() {
        try {
            if (profileAccount != null) {
                logoStream2 = profileAccount.getByteLogo2();

                if (logoStream2 == null) {
                    File file = new File("./images/logo_daerah.jpg");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    logoStream2 = new byte[(int) file.length()];
                    fileInputStream.read(logoStream2);
                }

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(logoStream2));

                painter2 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter2.setScaleToFit(true);
                painter2.setFillHorizontal(true);
                painter2.setFillVertical(true);
                painter2.setAntialiasing(true);

            } else {
                File file = new File("./images/logo_daerah.jpg");

                FileInputStream fileInputStream = new FileInputStream(file);

                logoStream2 = new byte[(int) file.length()];
                fileInputStream.read(logoStream2);

                BufferedImage img = ImageIO.read(file);

                painter2 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter2.setScaleToFit(true);
                painter2.setFillHorizontal(true);
                painter2.setFillVertical(true);
                painter2.setAntialiasing(true);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter2;
    }

    private Painter getPainter3() {
        try {
            if (profileAccount != null) {
                logoStream3 = profileAccount.getByteLogo3();

                if (logoStream3 == null) {
                    File file = new File("./images/logo_daerah.jpg");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    logoStream3 = new byte[(int) file.length()];
                    fileInputStream.read(logoStream3);
                }

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(logoStream3));

                painter3 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter3.setScaleToFit(true);
                painter3.setFillHorizontal(true);
                painter3.setFillVertical(true);
                painter3.setAntialiasing(true);

            } else {
                File file = new File("./images/logo_daerah.jpg");

                FileInputStream fileInputStream = new FileInputStream(file);

                logoStream3 = new byte[(int) file.length()];
                fileInputStream.read(logoStream3);

                BufferedImage img = ImageIO.read(file);

                painter3 = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
                painter3.setScaleToFit(true);
                painter3.setFillHorizontal(true);
                painter3.setFillVertical(true);
                painter3.setAntialiasing(true);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter3;
    }

    private void loadComboStateType() {
        comboStateType.removeAllItems();
        comboStateType.addItem("");
        comboStateType.addItem("Kabupaten");
        comboStateType.addItem("Kota");
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            dlg.dispose();
        } 
    }

    private DataConfiguration createDataConfiguration() {
        DataConfiguration config = new DataConfiguration(new PropertiesConfiguration());

        config.addProperty("Company", profileAccount.getCompany());
        config.addProperty("Address", profileAccount.getAddress());
        config.addProperty("StateType", profileAccount.getStateType());
        config.addProperty("State", profileAccount.getState());
        config.addProperty("Capital", profileAccount.getCapital());
        config.addProperty("Province", profileAccount.getProvince());

        config.addProperty("Logo", profileAccount.getByteLogo());
        config.addProperty("Logo2", profileAccount.getByteLogo2());
        config.addProperty("Logo3", profileAccount.getByteLogo3());

        return config;
    }

    private void onOK() {
        resetProperties();

        DataConfiguration config = createDataConfiguration();

        if (config != null) {
            try {
                cLogic.insertConfigurationSetting(frame.getSession(), config);
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        frame.setProfileAccount(profileAccount);
        response = JOptionPane.YES_OPTION;
        dlg.dispose();
    }

    public int getResponse() {
        return response;
    }

    public ProfileAccount getProfileAccount() {
        return profileAccount;
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
                        FileInputStream fileInputStream = new FileInputStream(file);

                        logoStream = new byte[(int) file.length()];
                        fileInputStream.read(logoStream);

                        BufferedImage img = ImageIO.read(file);
                        painter.setImage(img);
                        painter.setScaleToFit(true);
                        painter.setFillHorizontal(true);
                        painter.setFillVertical(true);
                        painter.setAntialiasing(true);

                    } else if (evt.getSource() == imagePanel2) {
                        FileInputStream fileInputStream = new FileInputStream(file);

                        logoStream2 = new byte[(int) file.length()];
                        fileInputStream.read(logoStream2);

                        BufferedImage img = ImageIO.read(file);
                        painter2.setImage(img);
                        painter2.setScaleToFit(true);
                        painter2.setFillHorizontal(true);
                        painter2.setFillVertical(true);
                        painter2.setAntialiasing(true);
                    } else if (evt.getSource() == imagePanel3) {
                        FileInputStream fileInputStream = new FileInputStream(file);

                        logoStream3 = new byte[(int) file.length()];
                        fileInputStream.read(logoStream3);

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
