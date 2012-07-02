package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.hyperlink.EditorPaneLinkVisitor;
import org.jdesktop.swingx.hyperlink.LinkModel;
import org.jdesktop.swingx.hyperlink.LinkModelAction;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.HyperlinkProvider;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.CurriculumVitae;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.project.civics.archieve.master.objects.EmployeeFacility;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip.StripOrientation;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeePanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private JXComboBox comboUnit = new JXComboBox();
    //
    private EmployeeList employeeList = new EmployeeList();
    private JXTextField fieldName = new JXTextField();
    private JXTextField fieldNIP = new JXTextField();
    private JXTextField fieldBirthPlace = new JXTextField();
    private JXDatePicker fieldBirthDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXComboBox comboSex = new JXComboBox();
    private JXComboBox comboReligion = new JXComboBox();
    private JXComboBox comboGrade = new JXComboBox();
    private JXComboBox comboFungsional = new JXComboBox();
    private JXComboBox comboStruktural = new JXComboBox();
    private JXTextArea fieldPosNotes = new JXTextArea();
    private JCheckBox checkGovernor = new JCheckBox();
    private JSpinField fieldMKYear = new JSpinField();
    private JSpinField fieldMKMonth = new JSpinField(0, 11);
    private JXTextField fieldMutation = new JXTextField();
    /**
     *
     */
    private JXComboBox comboMarital = new JXComboBox();
    private JXTextField fieldSoulMate = new JXTextField();
    private JSpinField fieldChildren = new JSpinField();
    private JXComboBox comboEducation = new JXComboBox();
    private JXComboBox comboEselon = new JXComboBox();
    private JXComboBox comboEmployeeStatus = new JXComboBox();
    private JXDatePicker fieldCpnsTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldPnsTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldGradeTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldEselonTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private EmployeeCoursesTablePanel courseTable;
    private JXTextField fieldDepartment = new JXTextField();
    private JYearChooser fieldGraduateYear = new JYearChooser();
    private EmployeeFacilityTable facilityTable = new EmployeeFacilityTable();
    private JXPanel imagePanel = new JXPanel();
    private ImagePainter painter = null;
    private byte[] pictureStream = null;
    /**
     *
     */
    private CurriculumVitaeTable cvTable = new CurriculumVitaeTable();
    private JTabbedPane tabPane = new JTabbedPane();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private JCommandButton btInsCV = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelCV = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    //
    private JCommandButton btChoose = new JCommandButton(Mainframe.getResizableIconFromSource("resource/file_upload.png"));
    //
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Employee selectedEmployee = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadEmployee worker;
    private EmployeeProgressListener progressListener;
    private JFileChooser openChooser;
    private JFileChooser saveChooser;
    //
    private Unit unit = null;

    public EmployeePanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        this.mLogic = new AssetMasterBusinessLogic(this.mainframe.getConnection());
        this.courseTable = new EmployeeCoursesTablePanel(this.mainframe.getConnection());
        construct();
        checkLogin();
    }

    private Painter getPainter() {
        try {

            if (selectedEmployee != null) {
                pictureStream = selectedEmployee.getPicture();

                if (pictureStream == null) {

                    if (selectedEmployee.getSex().equals(Employee.MALE)) {
                        File file = new File("./images/male.jpg");
                        FileInputStream fileInputStream = new FileInputStream(file);
                        pictureStream = new byte[(int) file.length()];
                        fileInputStream.read(pictureStream);
                    } else {
                        File file = new File("./images/female.jpg");
                        FileInputStream fileInputStream = new FileInputStream(file);
                        pictureStream = new byte[(int) file.length()];
                        fileInputStream.read(pictureStream);
                    }


                }

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(pictureStream));

                painter = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
//                painter.setScaleToFit(true);
                painter.setFillHorizontal(true);
                painter.setFillVertical(true);
                painter.setAntialiasing(true);
            } else {
                File file = new File("./images/male.jpg");
                FileInputStream fileInputStream = new FileInputStream(file);
                pictureStream = new byte[(int) file.length()];
                fileInputStream.read(pictureStream);

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(pictureStream));

                painter = new ImagePainter(img, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
//                painter.setScaleToFit(true);
                painter.setFillHorizontal(true);
                painter.setFillVertical(true);
                painter.setAntialiasing(true);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return painter;
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            employeeList.loadData("");
        } else {
            unit = mainframe.getUnit();
            comboUnit.setSelectedItem(unit);
            String modifier = generateUnitModifier(unit);
            employeeList.loadData(modifier);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Pegawai");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Rincian Data");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createButtonsPanel(), BorderLayout.NORTH);
        collapasepanel.add(createMainPanel(), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Fasilitas Pegawai untuk pengisian data-data pegawai yang ada di suatu dinas\n\n"
                + "Tambah Pegawai\n"
                + "Untuk menambah Pegawai klik tombol paling kiri "
                + "kemudian isi data Pegawai baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Pegawai\n"
                + "Untuk merubah Pegawai klik tombol kedua dari kiri "
                + "kemudian ubah data Pegawai, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Pegawai\n"
                + "Untuk menghapus Pegawai klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Pegawai "
                + "tersebut, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Pegawai");
        task.getContentPane().add(helpLabel);
        task.setAnimated(true);

        container.add(task);

        helpLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titledPanel.setContentContainer(scPane);

        return titledPanel;
    }

    private JXStatusBar createStatusBar() {
        JXStatusBar bar = new JXStatusBar();
        JXStatusBar.Constraint c1 = new JXStatusBar.Constraint(
                JXStatusBar.Constraint.ResizeBehavior.FILL);
        bar.add(statusLabel, c1);
        JXStatusBar.Constraint c2 = new JXStatusBar.Constraint();
        c2.setFixedWidth(300);
        bar.add(pbar, c2);

        return bar;
    }

    protected Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,10px,130px,20px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        checkGovernor.addActionListener(this);

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Kepala Daerah ?", cc.xy(1, 1));
        builder.add(checkGovernor, cc.xy(3, 1));

        builder.add(imagePanel, cc.xywh(5, 3, 1, 9));

        builder.addLabel("Nama", cc.xy(1, 3));
        builder.add(fieldName, cc.xy(3, 3));

        builder.addLabel("NIP", cc.xy(1, 5));
        builder.add(fieldNIP, cc.xy(3, 5));

        builder.addLabel("Jenis Kelamin", cc.xy(1, 7));
        builder.add(comboSex, cc.xy(3, 7));

        builder.addLabel("Agama", cc.xy(1, 9));
        builder.add(comboReligion, cc.xy(3, 9));

        builder.addLabel("Tempat Lahir", cc.xy(1, 11));
        builder.add(fieldBirthPlace, cc.xy(3, 11));

        builder.addLabel("Tanggal Lahir", cc.xy(1, 13));
        builder.add(fieldBirthDate, cc.xy(3, 13));

        builder.addLabel("Status Kawin", cc.xy(1, 15));
        builder.add(comboMarital, cc.xy(3, 15));

        builder.addLabel("Nama Pasangan", cc.xy(1, 17));
        builder.add(fieldSoulMate, cc.xy(3, 17));

        builder.addLabel("Jumlah Anak", cc.xy(1, 19));
        builder.add(fieldChildren, cc.xy(3, 19));


        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(builder.getPanel());

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(createMainPanel2());

        JScrollPane scPane3 = new JScrollPane();
        scPane3.setViewportView(createMainPanel3());

        JScrollPane scPane4 = new JScrollPane();
        scPane4.setViewportView(createMainPanel4());

        scPane.getVerticalScrollBar().setUnitIncrement(20);
        scPane2.getVerticalScrollBar().setUnitIncrement(20);
        scPane3.getVerticalScrollBar().setUnitIncrement(20);
        scPane4.getVerticalScrollBar().setUnitIncrement(20);


        tabPane.addTab("Pegawai", scPane);
        tabPane.addTab("Kepegawaian", scPane2);
        tabPane.addTab("Diklat & Fasilitas", scPane3);
//        tabPane.addTab("Cetak CV", new JPanel());
        tabPane.addTab("Upload Dokumen", scPane4);

        return tabPane;
    }

    protected Component createMainPanel2() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,2px,pref,2px,fill:default:grow,2px,pref,50px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,"
                + "fill:default:grow,10px,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPosNotes);

        fieldPosNotes.setToolTipText("<html>Diisi dengan keterangan Jabatan jika ada"
                + "<br>Misalkan 'Staf Bidang Perbendaharaan dan Verifikasi',</br>"
                + "<br>maka diisi dengan Bidang Perbendaharaan dan Verifikasi</br>");

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Status Kepegawaian", cc.xy(1, 1));
        builder.add(comboEmployeeStatus, cc.xyw(3, 1, 7));

        builder.addLabel("TMT SK CPNS", cc.xy(1, 3));
        builder.add(fieldCpnsTMT, cc.xyw(3, 3, 7));

        builder.addLabel("TMT SK PNS", cc.xy(1, 5));
        builder.add(fieldPnsTMT, cc.xyw(3, 5, 7));

        builder.addLabel("Golongan", cc.xy(1, 7));
        builder.add(comboGrade, cc.xyw(3, 7, 7));

        builder.addLabel("TMT Golongan", cc.xy(1, 9));
        builder.add(fieldGradeTMT, cc.xyw(3, 9, 7));

        builder.addLabel("Eselon", cc.xy(1, 11));
        builder.add(comboEselon, cc.xyw(3, 11, 7));

        builder.addLabel("Jab. Struktural", cc.xy(1, 13));
        builder.add(comboStruktural, cc.xyw(3, 13, 7));

        builder.addLabel("Jab. Fungsional / Umum", cc.xy(1, 15));
        builder.add(comboFungsional, cc.xyw(3, 15, 7));

        builder.addLabel("TMT Jabatan", cc.xy(1, 17));
        builder.add(fieldEselonTMT, cc.xyw(3, 17, 7));

        builder.addLabel("Keterangan Jabatan", cc.xy(1, 19));
        builder.add(scPane, cc.xywh(3, 19, 5, 3));
        builder.add(createStrip3(1.0, 1.0), cc.xy(9, 19));

        builder.addLabel("Masa Kerja", cc.xy(1, 23));
        builder.add(fieldMKYear, cc.xyw(3, 23, 1));
        builder.addLabel("Tahun", cc.xyw(5, 23, 1));
        builder.add(fieldMKMonth, cc.xyw(7, 23, 1));
        builder.addLabel("Bulan", cc.xyw(9, 23, 1));

        builder.addLabel("Mutasi Dari", cc.xy(1, 25));
        builder.add(fieldMutation, cc.xyw(3, 25, 7));

        return builder.getPanel();
    }

    protected Component createMainPanel3() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,50px",
                "pref,5px,pref,2px,pref,2px,pref,5px,pref,5px,pref,fill:default:grow,20px,2px,"
                + "pref,5px,pref,fill:default:grow,5px,15px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(facilityTable);

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Pendidikan", cc.xyw(1, 1, 3));

        builder.addLabel("Tingkat", cc.xy(1, 3));
        builder.add(comboEducation, cc.xy(3, 3));

        builder.addLabel("Nama", cc.xy(1, 5));
        builder.add(fieldDepartment, cc.xy(3, 5));

        builder.addLabel("Tahun Lulus", cc.xy(1, 7));
        builder.add(fieldGraduateYear, cc.xy(3, 7));

        builder.addSeparator("Diklat / Pelatihan", cc.xyw(1, 9, 3));

        builder.add(courseTable, cc.xywh(1, 11, 3, 3));

        builder.addSeparator("Fasilitas", cc.xyw(1, 15, 3));

        builder.add(scPane2, cc.xywh(1, 17, 3, 3));


        return builder.getPanel();
    }

    private Component createMainPanel4() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,10px,pref,fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 2, 5}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(cvTable);

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Curriculum Vitae", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(scPane, cc.xywh(1, 4, 3, 2));

        return builder.getPanel();
    }

    protected JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,5px, pref,20px", "pref,4px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1,1));

        builder.addLabel("Unit", cc.xy(1, 3));
        builder.add(comboUnit, cc.xyw(3, 3,1));

        return builder.getPanel();
    }

    protected JPanel createButtonsPanel() {
        FormLayout lm = new FormLayout(
                "right:pref, 4dlu, left:pref, 4dlu, left:pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(createStrip(3.0, 3.0));
        return builder.getPanel();
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Pegawai");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Pegawai");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Pegawai");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Pegawai");

        btDelete.setActionRichTooltip(deleteTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batalkan Perubahan");

        btCancel.setActionRichTooltip(cancelTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btAdd);
        buttonStrip.add(btEdit);
        buttonStrip.add(btSave);
        buttonStrip.add(btDelete);
        buttonStrip.add(btCancel);


        return buttonStrip;
    }

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah CV");

        btInsCV.setActionRichTooltip(addTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus CV");

        btDelCV.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsCV);
        buttonStrip.add(btDelCV);


        return buttonStrip;
    }

    private JCommandButtonStrip createStrip3(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip searchTooltip = new RichTooltip();
        searchTooltip.setTitle("Pilih Bagian / Bidang / Seksi");

        btChoose.setActionRichTooltip(searchTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip(StripOrientation.VERTICAL);
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btChoose);

        return buttonStrip;
    }

    private void construct() {

        loadSex();
        loadReligion();
        loadGrade();
        loadFungsional();
        loadStruktural();
        loadMarital();
        loadEducation();
        loadEselon();
        loadEmployeeStatus();
        loadComboUnit();

        constructOpenFileChooser();
        constructSaveFileChooser();

        imagePanel.setBorder(BorderFactory.createEtchedBorder());
        imagePanel.setBackgroundPainter(getPainter());
        imagePanel.addMouseListener(new MouseHandler());

        comboSex.setEditable(true);
        comboReligion.setEditable(true);
        comboGrade.setEditable(true);
        comboFungsional.setEditable(true);
        comboStruktural.setEditable(true);

        fieldBirthDate.setFormats("dd/MM/yyyy");
        fieldCpnsTMT.setFormats("dd/MM/yyyy");
        fieldPnsTMT.setFormats("dd/MM/yyyy");
        fieldGradeTMT.setFormats("dd/MM/yyyy");
        fieldEselonTMT.setFormats("dd/MM/yyyy");

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        comboUnit.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object object = comboUnit.getSelectedItem();
                if (object instanceof Unit) {
                    Unit units = (Unit) object;
                    String modifier = generateUnitModifier(units);
                    employeeList.loadData(modifier);
                }
            }
        });

        btChoose.addActionListener(this);

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(cvTable, ComponentState.DEFAULT);

        cvTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        cvTable.setShowGrid(false, false);

        color = skin.getColorScheme(facilityTable, ComponentState.DEFAULT);

        facilityTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        facilityTable.setShowGrid(false, false);

        fieldPosNotes.setWrapStyleWord(true);
        fieldPosNotes.setLineWrap(true);

        employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeList.setAutoCreateRowSorter(true);
        employeeList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsCV.addActionListener(this);
        btDelCV.addActionListener(this);

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.45)"
                + "(LEAF name=editor2 weight=0.55))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createLeftComponent(), "editor1");
        splitPane.add(createCenterComponent(), "editor2");
//        splitPane.add(panel, "editor3");

        panel.setVisible(true);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setFormState();
        setButtonState("");
        clearYear();
    }

    private void downloadFile() {
        CurriculumVitae vitae = cvTable.getSelectedVitae();
        if (vitae != null) {
            int retVal = saveChooser.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = saveChooser.getSelectedFile();
                try {

                    File fileToCopy = new File(file.getParent() + File.separator + file.getName() + "." + vitae.getFileExtension());

                    OutputStream out = new FileOutputStream(fileToCopy);

                    out.write(vitae.getFileStream());

                    out.close();

                    String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                    Runtime.getRuntime().exec(commands);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public void filter() {
        employeeList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void constructOpenFileChooser() {
        if (openChooser == null) {
            openChooser = new JFileChooser();
        }

        openChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".doc"));
            }

            @Override
            public String getDescription() {
                return "File CV (*.doc)";
            }
        });
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
                return (fileName.toLowerCase().endsWith(".doc"));
            }

            @Override
            public String getDescription() {
                return "File CV (*.doc)";
            }
        });
    }

    private void setFormState() {
        checkGovernor.setEnabled(iseditable);
        if (isnew) {
            fieldName.setEnabled(iseditable);
            fieldNIP.setEnabled(iseditable);
            fieldBirthPlace.setEnabled(iseditable);
            fieldBirthDate.setEnabled(iseditable);
            comboSex.setEnabled(iseditable);
            comboReligion.setEnabled(iseditable);
            comboGrade.setEnabled(iseditable);
            comboFungsional.setEnabled(iseditable);
            comboStruktural.setEnabled(iseditable);
            fieldPosNotes.setEnabled(iseditable);
            btChoose.setEnabled(iseditable);
            fieldSearch.setEnabled(!iseditable);
            employeeList.setEnabled(!iseditable);
            btInsCV.setEnabled(iseditable);
            btDelCV.setEnabled(iseditable);

            comboMarital.setEnabled(iseditable);
            fieldSoulMate.setEnabled(iseditable);
            fieldChildren.setEnabled(iseditable);
            comboEducation.setEnabled(iseditable);
            fieldDepartment.setEnabled(iseditable);
            fieldGraduateYear.setEnabled(iseditable);
            comboEselon.setEnabled(iseditable);
            comboEmployeeStatus.setEnabled(iseditable);
            fieldCpnsTMT.setEnabled(iseditable);
            fieldPnsTMT.setEnabled(iseditable);
            fieldGradeTMT.setEnabled(iseditable);
            fieldEselonTMT.setEnabled(iseditable);
            fieldMKYear.setEnabled(iseditable);
            fieldMKMonth.setEnabled(iseditable);
            fieldMutation.setEnabled(iseditable);

            facilityTable.setEnabled(iseditable);
            courseTable.setEnabled(iseditable);
            courseTable.setButtonEnable(iseditable);
        } else {
            if (checkGovernor.isSelected()) {
                fieldName.setEnabled(iseditable);
                fieldNIP.setEnabled(false);
                fieldBirthPlace.setEnabled(iseditable);
                fieldBirthDate.setEnabled(iseditable);
                comboSex.setEnabled(iseditable);
                comboReligion.setEnabled(iseditable);
                comboGrade.setEnabled(false);
                comboFungsional.setEnabled(false);
                comboStruktural.setEnabled(false);
                fieldPosNotes.setEnabled(false);
                btChoose.setEnabled(false);
                fieldSearch.setEnabled(!iseditable);
                employeeList.setEnabled(!iseditable);
                btInsCV.setEnabled(iseditable);
                btDelCV.setEnabled(iseditable);

                comboMarital.setEnabled(iseditable);
                fieldSoulMate.setEnabled(iseditable);
                fieldChildren.setEnabled(iseditable);
                comboEducation.setEnabled(iseditable);
                fieldDepartment.setEnabled(iseditable);
                fieldGraduateYear.setEnabled(iseditable);
                comboEselon.setEnabled(false);
                comboEmployeeStatus.setEnabled(false);
                fieldCpnsTMT.setEnabled(false);
                fieldPnsTMT.setEnabled(false);
                fieldGradeTMT.setEnabled(false);
                fieldEselonTMT.setEnabled(false);
                fieldMKYear.setEnabled(iseditable);
                fieldMKMonth.setEnabled(iseditable);
                fieldMutation.setEnabled(iseditable);

                facilityTable.setEnabled(iseditable);
                courseTable.setEnabled(iseditable);
                courseTable.setButtonEnable(iseditable);
            } else {
                fieldName.setEnabled(iseditable);
                fieldNIP.setEnabled(iseditable);
                fieldBirthPlace.setEnabled(iseditable);
                fieldBirthDate.setEnabled(iseditable);
                comboSex.setEnabled(iseditable);
                comboReligion.setEnabled(iseditable);
                comboGrade.setEnabled(iseditable);
                comboFungsional.setEnabled(iseditable);
                comboStruktural.setEnabled(iseditable);
                fieldPosNotes.setEnabled(iseditable);
                btChoose.setEnabled(iseditable);
                fieldSearch.setEnabled(!iseditable);
                employeeList.setEnabled(!iseditable);
                btInsCV.setEnabled(iseditable);
                btDelCV.setEnabled(iseditable);

                comboMarital.setEnabled(iseditable);
                fieldSoulMate.setEnabled(iseditable);
                fieldChildren.setEnabled(iseditable);
                comboEducation.setEnabled(iseditable);
                fieldDepartment.setEnabled(iseditable);
                fieldGraduateYear.setEnabled(iseditable);
                comboEselon.setEnabled(iseditable);
                comboEmployeeStatus.setEnabled(iseditable);
                fieldCpnsTMT.setEnabled(iseditable);
                fieldPnsTMT.setEnabled(iseditable);
                fieldGradeTMT.setEnabled(iseditable);
                fieldEselonTMT.setEnabled(iseditable);
                fieldMKYear.setEnabled(iseditable);
                fieldMKMonth.setEnabled(iseditable);
                fieldMutation.setEnabled(iseditable);
                //
                facilityTable.setEnabled(iseditable);
                courseTable.setEnabled(iseditable);
                courseTable.setButtonEnable(iseditable);
            }
        }

    }

    private void setFormChecked() {
        fieldNIP.setEnabled(!checkGovernor.isSelected());
        comboGrade.setEnabled(!checkGovernor.isSelected());
        comboFungsional.setEnabled(!checkGovernor.isSelected());
        comboStruktural.setEnabled(!checkGovernor.isSelected());
        fieldPosNotes.setEnabled(!checkGovernor.isSelected());
        btChoose.setEnabled(!checkGovernor.isSelected());

        comboEselon.setEnabled(!checkGovernor.isSelected());
        comboEmployeeStatus.setEnabled(!checkGovernor.isSelected());
        fieldCpnsTMT.setEnabled(!checkGovernor.isSelected());
        fieldPnsTMT.setEnabled(!checkGovernor.isSelected());
        fieldGradeTMT.setEnabled(!checkGovernor.isSelected());
        fieldEselonTMT.setEnabled(!checkGovernor.isSelected());
        fieldMKYear.setEnabled(!checkGovernor.isSelected());
        fieldMKMonth.setEnabled(!checkGovernor.isSelected());
        fieldMutation.setEnabled(!checkGovernor.isSelected());
    }

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldGraduateYear.setYear(year);
    }

    private void clearForm() {
        checkGovernor.setSelected(false);
        fieldName.setText("");
        fieldNIP.setText("");
        fieldBirthPlace.setText("");
        fieldBirthDate.setDate(null);
        comboSex.setSelectedIndex(0);
        comboReligion.setSelectedIndex(0);
        comboGrade.setSelectedIndex(0);
        comboStruktural.setSelectedIndex(0);
        comboFungsional.setSelectedIndex(0);
        fieldPosNotes.setText("");

        comboMarital.setSelectedIndex(0);
        fieldSoulMate.setText("");
        fieldChildren.setValue(0);
        comboEducation.setSelectedIndex(0);
        fieldDepartment.setText("");
        clearYear();
        comboEselon.setSelectedIndex(0);
        comboEmployeeStatus.setSelectedIndex(0);
        fieldCpnsTMT.setDate(null);
        fieldPnsTMT.setDate(null);
        fieldGradeTMT.setDate(null);
        fieldEselonTMT.setDate(null);

        fieldMKYear.setValue(0);
        fieldMKMonth.setValue(0);
        fieldMutation.setText("");

        courseTable.clear();
        facilityTable.clear();

        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
        }
        cvTable.clear();
        pictureStream = null;
        imagePanel.setBackgroundPainter(getPainter());
    }

    private void setButtonState(String state) {
        if (state.equals("New")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(true);
            btCancel.setEnabled(true);
        } else if (state.equals("Save")) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);

        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
        }
    }

    private void loadSex() {
        comboSex.removeAllItems();
        comboSex.setModel(new ListComboBoxModel<String>(Employee.sexAsList()));
        AutoCompleteDecorator.decorate(comboSex);
    }

    private void loadReligion() {
        comboReligion.removeAllItems();
        comboReligion.setModel(new ListComboBoxModel<String>(Employee.religionAsList()));
        AutoCompleteDecorator.decorate(comboReligion);
    }

    private void loadGrade() {
        comboGrade.removeAllItems();
        comboGrade.setModel(new ListComboBoxModel<String>(Employee.gradeAsList()));
        AutoCompleteDecorator.decorate(comboGrade);
    }

    private void loadFungsional() {
        comboFungsional.removeAllItems();
        comboFungsional.setModel(new ListComboBoxModel<String>(Employee.fungsionalGradeAsList()));
        AutoCompleteDecorator.decorate(comboFungsional);
    }

    private void loadStruktural() {
        comboStruktural.removeAllItems();
        comboStruktural.setModel(new ListComboBoxModel<String>(Employee.strukturalAsList()));
        AutoCompleteDecorator.decorate(comboStruktural);
    }

    private void loadMarital() {
        comboMarital.removeAllItems();
        comboMarital.setModel(new ListComboBoxModel<String>(Employee.maritalAsList()));
        AutoCompleteDecorator.decorate(comboMarital);
    }

    private void loadEselon() {
        comboEselon.removeAllItems();
        comboEselon.setModel(new ListComboBoxModel<String>(Employee.eselonAsList()));
        AutoCompleteDecorator.decorate(comboEselon);
    }

    private void loadEducation() {
        comboEducation.removeAllItems();
        comboEducation.setModel(new ListComboBoxModel<String>(Employee.educationAsList()));
        AutoCompleteDecorator.decorate(comboEducation);
    }

    private void loadEmployeeStatus() {
        comboEmployeeStatus.removeAllItems();
        comboEmployeeStatus.setModel(new ListComboBoxModel<String>(Employee.employeeStatusAsList()));
        AutoCompleteDecorator.decorate(comboEmployeeStatus);
    }

    private void loadComboUnit() {
        try {
            SwingWorker<ArrayList<Unit>, Void> lw = new SwingWorker<ArrayList<Unit>, Void>() {

                @Override
                protected ArrayList<Unit> doInBackground() throws Exception {
                    ArrayList<Unit> units = mLogic.getUnit(mainframe.getSession());
                    if (!units.isEmpty()) {
                        for (Unit unit : units) {
                            unit.setHirarchiecal(true);
                        }
                    }
                    return units;
                }
            };
            lw.execute();
            ArrayList<Unit> units = lw.get();

            units.add(0, new Unit());
            comboUnit.setModel(new ListComboBoxModel<Unit>(units));
            AutoCompleteDecorator.decorate(comboUnit);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void createFacility() {
        if (isnew) {
            ArrayList<EmployeeFacility> facilitys = EmployeeFacility.createFacility();
            facilityTable.clear();
            facilityTable.addEmployeeFacilitys(facilitys);
        } else if (isedit) {
            if (selectedEmployee != null) {
                if (selectedEmployee.getFacilitys().isEmpty()) {
                    ArrayList<EmployeeFacility> facilitys = EmployeeFacility.createFacility();
                    facilityTable.clear();
                    facilityTable.addEmployeeFacilitys(facilitys);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btAdd) {
            onNew();
        } else if (obj == btEdit) {
            onEdit();
        } else if (obj == btDelete) {
            onDelete();
        } else if (obj == btSave) {
            onSave();
        } else if (obj == btCancel) {
            onCancel();
        } else if (obj == checkGovernor) {
            setFormChecked();
        } else if (obj == btInsCV) {
            int retVal = openChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = openChooser.getSelectedFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    byte[] fileStream = new byte[(int) file.length()];
                    fileInputStream.read(fileStream);

                    CurriculumVitae vitae = new CurriculumVitae();
                    if (selectedEmployee != null) {
                        vitae.setEmployeeIndex(selectedEmployee.getIndex());
                    }
                    vitae.setFileName(file.getName());
                    vitae.setFileStream(fileStream);

                    cvTable.addVitae(vitae);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (obj == btDelCV) {
            ArrayList<CurriculumVitae> vitaes = cvTable.getSelectedVitaes();
            if (!vitaes.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    cvTable.removeVitaes(vitaes);
                }
            }
        } else if (obj == btChoose) {
            DivisionChooserDlg dlg = new DivisionChooserDlg(mainframe, mainframe.getConnection());
            dlg.showDialog();
            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                Division division = dlg.getSelectedDivision();
                fieldPosNotes.setText(division.getName());
            }
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        createFacility();
        statusLabel.setText("Tambah Pegawai");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Pegawai");
        createFacility();
        defineCustomFocusTraversalPolicy();
    }

    private void onDelete() {
        Object[] options = {"Ya", "Tidak"};
        int choise = JOptionPane.showOptionDialog(this,
                " Anda yakin menghapus data ini ? (Y/T)",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[1]);
        if (choise == JOptionPane.YES_OPTION) {
            try {

                boolean inexp = logic.getEmployeeInExpeditionCheque(mainframe.getSession(), selectedEmployee.getIndex());

                if (!inexp) {
                    logic.deleteEmployee(mainframe.getSession(), selectedEmployee.getIndex());
                    employeeList.removeSelected(selectedEmployee);
                    clearForm();
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<html>Pegawai dengan NIP ").
                            append("<b>").
                            append(selectedEmployee.getNip()).
                            append("</b>").
                            append(" tidak boleh dihapus karena sudah dipakai di Kwitansi Pembayaran");
                    throw new MotekarException(builder.toString());
                }
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            } catch (MotekarException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat menghapus data ",
                        ex.getMessage(), "Error", null, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    private void onSave() {
        try {
            Employee newEmployee = getEmployee();

            if (isnew) {
                newEmployee = logic.insertEmployee(mainframe.getSession(), newEmployee);
                isnew = false;
                iseditable = false;
                employeeList.addEmployee(newEmployee);
                selectedEmployee = newEmployee;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newEmployee = logic.updateEmployee(mainframe.getSession(), selectedEmployee, newEmployee);
                isedit = false;
                iseditable = false;
                employeeList.updateEmployee(newEmployee);
                setFormState();
                setButtonState("Save");
            }
            statusLabel.setText("Ready");
            mainframe.setFocusTraversalPolicy(null);
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        } catch (Exception ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    null, "Error", ex, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        }
    }

    private void onCancel() {
        iseditable = false;
        isedit = false;
        isnew = false;
        setFormState();
        if (employeeList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private void setFormValues() {
        if (selectedEmployee != null) {
            try {
                selectedEmployee = logic.getCompleteEmployee(mainframe.getSession(), selectedEmployee);

                ArrayList<CurriculumVitae> curriculumVitaes = selectedEmployee.getCurriculumVitae();

                checkGovernor.setSelected(selectedEmployee.isGorvernor());
                if (checkGovernor.isSelected()) {
                    fieldName.setText(selectedEmployee.getName());
                    fieldNIP.setText("");
                    fieldBirthPlace.setText(selectedEmployee.getBirthPlace());
                    fieldBirthDate.setDate(selectedEmployee.getBirthDate());
                    comboSex.setSelectedIndex(selectedEmployee.getSex());
                    comboReligion.setSelectedIndex(selectedEmployee.getReligion());
                    comboGrade.setSelectedIndex(0);
                    comboStruktural.setSelectedIndex(0);
                    comboFungsional.setSelectedIndex(0);
                    fieldPosNotes.setText("");
                    if (fieldName.isEnabled()) {
                        fieldName.requestFocus();
                        fieldName.selectAll();
                    }
                    cvTable.clear();
                    cvTable.addVitae(curriculumVitaes);

                    comboMarital.setSelectedIndex(selectedEmployee.getMarital());
                    fieldSoulMate.setText(selectedEmployee.getSoulmate());
                    fieldChildren.setValue(selectedEmployee.getChildren());
                    comboEducation.setSelectedIndex(selectedEmployee.getEducation());
                    fieldDepartment.setText(selectedEmployee.getDepartment());
                    fieldGraduateYear.setYear(selectedEmployee.getGraduatedYear());
                    comboEselon.setSelectedIndex(0);
                    comboEmployeeStatus.setSelectedIndex(0);
                    fieldCpnsTMT.setDate(null);
                    fieldPnsTMT.setDate(null);
                    fieldGradeTMT.setDate(null);
                    fieldEselonTMT.setDate(null);
                    fieldMKYear.setValue(0);
                    fieldMKMonth.setValue(0);
                    fieldMutation.setText("");
                    courseTable.clear();
                    ArrayList<EmployeeCourses> courseses = selectedEmployee.getCourseses();
                    courseTable.addEmployeeCourses(courseses);

                    facilityTable.clear();
                    ArrayList<EmployeeFacility> facilitys = selectedEmployee.getFacilitys();
                    facilityTable.addEmployeeFacilitys(facilitys);

                    imagePanel.setBackgroundPainter(getPainter());

                } else {
                    fieldName.setText(selectedEmployee.getName());
                    fieldNIP.setText(selectedEmployee.getNip());
                    fieldBirthPlace.setText(selectedEmployee.getBirthPlace());
                    fieldBirthDate.setDate(selectedEmployee.getBirthDate());
                    comboSex.setSelectedIndex(selectedEmployee.getSex());
                    comboReligion.setSelectedIndex(selectedEmployee.getReligion());
                    comboGrade.setSelectedIndex(selectedEmployee.getGrade());
                    comboFungsional.setSelectedIndex(selectedEmployee.getFungsional());
                    comboStruktural.setSelectedIndex(selectedEmployee.getStruktural());
                    fieldPosNotes.setText(selectedEmployee.getPositionNotes());

                    cvTable.clear();
                    cvTable.addVitae(curriculumVitaes);

                    comboMarital.setSelectedIndex(selectedEmployee.getMarital());
                    fieldSoulMate.setText(selectedEmployee.getSoulmate());
                    fieldChildren.setValue(selectedEmployee.getChildren());
                    comboEducation.setSelectedIndex(selectedEmployee.getEducation());
                    fieldDepartment.setText(selectedEmployee.getDepartment());
                    fieldGraduateYear.setYear(selectedEmployee.getGraduatedYear());
                    comboEselon.setSelectedIndex(selectedEmployee.getEselon());
                    comboEmployeeStatus.setSelectedIndex(selectedEmployee.getEmployeeStatus());
                    fieldCpnsTMT.setDate(selectedEmployee.getCpnsTMT());
                    fieldPnsTMT.setDate(selectedEmployee.getPnsTMT());
                    fieldGradeTMT.setDate(selectedEmployee.getGradeTMT());
                    fieldEselonTMT.setDate(selectedEmployee.getEselonTMT());
                    fieldMKYear.setValue(selectedEmployee.getMkYear());
                    fieldMKMonth.setValue(selectedEmployee.getMkMonth());
                    fieldMutation.setText(selectedEmployee.getMutation());
                    courseTable.clear();
                    ArrayList<EmployeeCourses> courseses = selectedEmployee.getCourseses();
                    courseTable.addEmployeeCourses(courseses);
                    facilityTable.clear();
                    ArrayList<EmployeeFacility> facilitys = selectedEmployee.getFacilitys();
                    facilityTable.addEmployeeFacilitys(facilitys);

                    imagePanel.setBackgroundPainter(getPainter());
                }
                setButtonState("Save");
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private Employee getEmployee() throws MotekarException {
        errorString = new StringBuilder();

        boolean isGorvernor = checkGovernor.isSelected();


        String name = fieldName.getText();
        String nip = fieldNIP.getText();
        Integer sex = comboSex.getSelectedIndex();
        Integer religion = comboReligion.getSelectedIndex();
        Integer grade = comboGrade.getSelectedIndex();
        String department = fieldDepartment.getText();
        Integer graduatedYear = fieldGraduateYear.getYear();

        if (name.equals("")) {
            errorString.append("<br>- Nama</br>");
        }

        if (sex < 0) {
            errorString.append("<br>- Jenis Kelamin</br>");
        }

        if (!isGorvernor) {

            if (nip.equals("")) {
                errorString.append("<br>- NIP</br>");
            }

            if (grade < 0) {
                errorString.append("<br>- Golongan</br>");
            }
        }

        ArrayList<CurriculumVitae> curriculumVitae = cvTable.getVitaes();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ArrayList<EmployeeCourses> courseses = courseTable.getCoursesess();
        ArrayList<EmployeeFacility> facilitys = facilityTable.getFacilitys();

        Employee employee = new Employee();
        employee.setNip(nip);
        employee.setName(name);
        employee.setBirthPlace(fieldBirthPlace.getText());
        employee.setBirthDate(fieldBirthDate.getDate());
        employee.setSex(sex);
        employee.setReligion(religion);
        employee.setGrade(grade);
        employee.setFungsional(comboFungsional.getSelectedIndex());
        employee.setStruktural(comboStruktural.getSelectedIndex());
        employee.setPositionNotes(fieldPosNotes.getText());
        employee.setGorvernor(isGorvernor);

        if (employee.getFungsional() == -1) {
            employee.setFungsional(Integer.valueOf(0));
        }

        if (employee.getStruktural() == -1) {
            employee.setStruktural(Integer.valueOf(0));
        }

        employee.setCurriculumVitae(curriculumVitae);

        employee.setMarital(comboMarital.getSelectedIndex());
        employee.setSoulmate(fieldSoulMate.getText());
        employee.setChildren(fieldChildren.getValue());
        employee.setEselon(comboEselon.getSelectedIndex());
        employee.setEducation(comboEducation.getSelectedIndex());
        employee.setDepartment(department);
        employee.setGraduatedYear(graduatedYear);

        if (employee.getEselon() == -1) {
            employee.setEselon(Integer.valueOf(0));
        }

        if (employee.getMarital() == -1) {
            employee.setMarital(Integer.valueOf(0));
        }

        if (employee.getChildren() == -1) {
            employee.setChildren(Integer.valueOf(0));
        }

        if (employee.getEducation() == -1) {
            employee.setEducation(Integer.valueOf(0));
        }

        employee.setEmployeeStatus(comboEmployeeStatus.getSelectedIndex());
        employee.setCpnsTMT(fieldCpnsTMT.getDate());
        employee.setPnsTMT(fieldPnsTMT.getDate());
        employee.setGradeTMT(fieldGradeTMT.getDate());
        employee.setEselonTMT(fieldEselonTMT.getDate());
        employee.setMkYear(fieldMKYear.getValue());
        employee.setMkMonth(fieldMKMonth.getValue());
        employee.setMutation(fieldMutation.getText());

        employee.setCourseses(courseses);
        employee.setFacilitys(facilitys);

        if (selectedEmployee != null) {
            employee.setUnit(selectedEmployee.getUnit());
        } else {
            employee.setUnit(unit);
        }

        employee.setPicture(pictureStream);

        return employee;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedEmployee = employeeList.getSelectedEmployee();
            setFormValues();
        }
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldName);
        comp.add(fieldNIP);
        comp.add(comboSex.getEditor().getEditorComponent());
        comp.add(comboReligion.getEditor().getEditorComponent());
        comp.add(fieldBirthPlace);
        comp.add(fieldBirthDate.getEditor());

        comp.add(comboMarital.getEditor().getEditorComponent());
        comp.add(fieldSoulMate);
        comp.add(fieldChildren);
        comp.add(comboEmployeeStatus.getEditor().getEditorComponent());
        comp.add(fieldCpnsTMT.getEditor());
        comp.add(fieldPnsTMT.getEditor());

        comp.add(comboGrade.getEditor().getEditorComponent());
        comp.add(fieldGradeTMT.getEditor());
        comp.add(comboEselon.getEditor().getEditorComponent());
        comp.add(comboStruktural.getEditor().getEditorComponent());
        comp.add(comboFungsional.getEditor().getEditorComponent());
        comp.add(fieldEselonTMT.getEditor());
        comp.add(fieldPosNotes);
        comp.add(fieldMKYear);
        comp.add(fieldMKMonth);
        comp.add(fieldMutation);
        comp.add(comboEducation.getEditor().getEditorComponent());
        comp.add(fieldDepartment);
        comp.add(fieldGraduateYear);


        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class EmployeeList extends JXList {

        private Icon MALE_ICON = Mainframe.getResizableIconFromSource("resource/user_male.png", new Dimension(36, 36));
        private Icon FEMALE_ICON = Mainframe.getResizableIconFromSource("resource/user_female.png", new Dimension(36, 36));
        private Icon GOVERNOR_ICON = Mainframe.getResizableIconFromSource("resource/business_user.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public EmployeeList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData(String modifier) {
            worker = new LoadEmployee((DefaultListModel) getModel(), modifier);
            progressListener = new EmployeeProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Employee getSelectedEmployee() {
            Employee employee = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Employee) {
                employee = (Employee) obj;
            }
            return employee;
        }

        public void addEmployee(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(employee);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateEmployee(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(employee, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Employee> employees) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(employees);
            filter();
        }

        public void removeSelected(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(employee);
            filter();
        }

        @Override
        public ListCellRenderer getCellRenderer() {
            return new DefaultListRenderer(new StringValue() {

                public String getString(Object o) {
                    return o.toString();
                }
            }, new IconValue() {

                public Icon getIcon(Object o) {

                    Employee employee = null;

                    if (o instanceof Employee) {
                        employee = (Employee) o;
                    }

                    if (employee != null) {
                        if (employee.isGorvernor()) {
                            return EmployeeList.this.GOVERNOR_ICON;
                        } else {
                            if (employee.getSex() == Employee.MALE) {
                                return EmployeeList.this.MALE_ICON;
                            } else {
                                return EmployeeList.this.FEMALE_ICON;
                            }
                        }
                    }

                    return EmployeeList.this.NULL_ICON;
                }
            });
        }
    }

    private class EmployeeFacilityTable extends JXTable {

        private EmployeeFacilityTableModel model = new EmployeeFacilityTableModel();

        public EmployeeFacilityTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public EmployeeFacility getSelectedFacility() {
            EmployeeFacility facility = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof EmployeeFacility) {
                facility = (EmployeeFacility) obj;
            }

            return facility;
        }

        public ArrayList<EmployeeFacility> getFacilitys() {
            return model.getFacilitys();
        }

        public ArrayList<EmployeeFacility> getSelectedFacilitys() {

            ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    EmployeeFacility facility = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof EmployeeFacility) {
                        facility = (EmployeeFacility) obj;
                        facilitys.add(facility);
                    }
                }
            }

            return facilitys;
        }

        public void updateSelectedFacility(EmployeeFacility facility) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedFacility(), facility);
        }

        public void removeFacility(ArrayList<EmployeeFacility> facilitys) {
            if (!facilitys.isEmpty()) {
                for (EmployeeFacility facility : facilitys) {
                    model.remove(facility);
                }
            }

        }

        public void addEmployeeFacilitys(ArrayList<EmployeeFacility> facilitys) {
            if (!facilitys.isEmpty()) {
                for (EmployeeFacility facility : facilitys) {
                    model.add(facility);
                }
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }
    }

    private static class EmployeeFacilityTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 2;
        private static final String[] columnIds = {"Nama Fasilitas", "Mempunyai"};
        private ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return true;
            }
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<EmployeeFacility> facility) {
            int first = facility.size();
            int last = first + facility.size() - 1;
            facility.addAll(facility);
            fireTableRowsInserted(first, last);
        }

        public void add(EmployeeFacility facility) {
            if (!facilitys.contains(facility)) {
                insertRow(getRowCount(), facility);
            }
        }

        public void insertRow(int row, EmployeeFacility facility) {
            facilitys.add(row, facility);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, EmployeeFacility oldFacility, EmployeeFacility newFacility) {
            int index = facilitys.indexOf(oldFacility);
            facilitys.set(index, newFacility);
            fireTableRowsUpdated(index, index);
        }

        public void remove(EmployeeFacility facility) {
            int row = facilitys.indexOf(facility);
            facilitys.remove(facility);
            fireTableRowsDeleted(row, row);
        }

        public void clear() {
            setRowCount(0);
        }

        protected void setRowCount(int rowCount) {
            int old = getRowCount();
            if (old == rowCount) {
                return;
            }

            if (rowCount <= old) {
                facilitys.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                facilitys.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            facilitys.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (facilitys.get(i) == null) {
                    facilitys.set(i, new EmployeeFacility());
                }
            }
        }

        public ArrayList<EmployeeFacility> getFacilitys() {
            return facilitys;
        }

        @Override
        public int getRowCount() {
            return facilitys.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public EmployeeFacility getFacility(int row) {
            if (!facilitys.isEmpty()) {
                return facilitys.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            EmployeeFacility facility = getFacility(row);
            switch (column) {
                case 0:
                    facility = (EmployeeFacility) aValue;
                    break;
                case 1:
                    facility.setOwned((Boolean) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            EmployeeFacility facility = getFacility(row);
            switch (column) {
                case 0:
                    toBeDisplayed = facility;
                    break;
                case 1:
                    toBeDisplayed = facility.isOwned();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class CurriculumVitaeTable extends JXTable {

        private CurriculumVitaeTableModel model = new CurriculumVitaeTableModel();

        public CurriculumVitaeTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
            getColumnModel().getColumn(0).setMinWidth(100);
            getColumnModel().getColumn(0).setMaxWidth(100);
        }

        public void clear() {
            model.clear();
        }

        public CurriculumVitae getSelectedVitae() {
            CurriculumVitae vitae = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 1);

            if (obj instanceof CurriculumVitae) {
                vitae = (CurriculumVitae) obj;
            }

            return vitae;
        }

        public ArrayList<CurriculumVitae> getVitaes() {
            return model.getVitaes();
        }

        public ArrayList<CurriculumVitae> getSelectedVitaes() {

            ArrayList<CurriculumVitae> vitaes = new ArrayList<CurriculumVitae>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    CurriculumVitae vitae = null;
                    Object obj = model.getValueAt(rows[i], 1);
                    if (obj instanceof CurriculumVitae) {
                        vitae = (CurriculumVitae) obj;
                        vitaes.add(vitae);
                    }
                }
            }

            return vitaes;
        }

        public void updateSelectedVitae(CurriculumVitae vitae) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedVitae(), vitae);
        }

        public void removeVitaes(ArrayList<CurriculumVitae> vitaes) {
            if (!vitaes.isEmpty()) {
                for (CurriculumVitae vitae : vitaes) {
                    model.remove(vitae);
                }
            }

        }

        public void addVitae(CurriculumVitae vitae) {
            model.add(vitae);
        }

        public void addVitae(ArrayList<CurriculumVitae> vitaes) {
            if (!vitaes.isEmpty()) {
                for (CurriculumVitae vitae : vitaes) {
                    model.add(vitae);
                }
            }
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 0) {
                EditorPaneLinkVisitor visitor = new EditorPaneLinkVisitor();
                LinkModelAction<?> action = new LinkModelAction<LinkModel>(visitor) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        downloadFile();
                    }

                    @Override
                    protected void updateFromTarget() {
                        super.updateFromTarget();
                        putValue(Action.SHORT_DESCRIPTION, null);
                    }
                };
                return new DefaultTableRenderer(new HyperlinkProvider(action, LinkModel.class));
            }
            return super.getCellRenderer(row, column);
        }
    }

    private static class CurriculumVitaeTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 2;
        private static final String[] columnIds = {"Download File", "Nama File"};
        private ArrayList<CurriculumVitae> curriculumVitaes = new ArrayList<CurriculumVitae>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<CurriculumVitae> vitaes) {
            int first = vitaes.size();
            int last = first + vitaes.size() - 1;
            vitaes.addAll(vitaes);
            fireTableRowsInserted(first, last);
        }

        public void add(CurriculumVitae vitae) {
            if (!curriculumVitaes.contains(vitae)) {
                insertRow(getRowCount(), vitae);
            }
        }

        public void insertRow(int row, CurriculumVitae vitae) {
            curriculumVitaes.add(row, vitae);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, CurriculumVitae oldVitae, CurriculumVitae newVitae) {
            int index = curriculumVitaes.indexOf(oldVitae);
            curriculumVitaes.set(index, newVitae);
            fireTableRowsUpdated(index, index);
        }

        public void remove(CurriculumVitae vitae) {
            int row = curriculumVitaes.indexOf(vitae);
            curriculumVitaes.remove(vitae);
            fireTableRowsDeleted(row, row);
        }

        public void clear() {
            setRowCount(0);
        }

        protected void setRowCount(int rowCount) {
            int old = getRowCount();
            if (old == rowCount) {
                return;
            }

            if (rowCount <= old) {
                curriculumVitaes.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                curriculumVitaes.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            curriculumVitaes.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (curriculumVitaes.get(i) == null) {
                    curriculumVitaes.set(i, new CurriculumVitae());
                }
            }
        }

        public ArrayList<CurriculumVitae> getVitaes() {
            return curriculumVitaes;
        }

        @Override
        public int getRowCount() {
            return curriculumVitaes.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public CurriculumVitae getVitae(int row) {
            if (!curriculumVitaes.isEmpty()) {
                return curriculumVitaes.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            CurriculumVitae vitae = getVitae(row);
            switch (column) {
                case 0:
                    break;

                case 1:
                    vitae = (CurriculumVitae) aValue;
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            CurriculumVitae vitae = getVitae(row);
            switch (column) {
                case 0:
                    if (vitae != null) {
                        LinkModel link = new LinkModel("Download File", null, null);
                        toBeDisplayed = link;
                    }

                    break;

                case 1:
                    toBeDisplayed = vitae;
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadEmployee extends SwingWorker<DefaultListModel, Employee> {

        private DefaultListModel model;
        private Exception exception;
        private String modifier = "";

        public LoadEmployee(DefaultListModel model, String modifier) {
            this.model = model;
            this.modifier = modifier;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Employee> chunks) {
            mainframe.stopInActiveListener();
            for (Employee employee : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Pegawai " + employee.getName());
                model.addElement(employee);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Employee> employees = logic.getEmployee(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!employees.isEmpty()) {
                    for (int i = 0; i < employees.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / employees.size();
                        setProgress((int) progress);
                        publish(employees.get(i));
                        Thread.sleep(100L);
                    }
                }
                return model;
            } catch (Exception anyException) {
                exception = anyException;
                throw exception;
            }
        }

        @Override
        protected void done() {
            try {
                if (isCancelled()) {
                    return;
                }
                get();
            } catch (InterruptedException e) {
                //ignore
            } catch (ExecutionException e) {
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(EmployeePanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class EmployeeProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private EmployeeProgressListener() {
        }

        EmployeeProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
            this.progressBar.setVisible(true);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String strPropertyName = evt.getPropertyName();
            if ("progress".equals(strPropertyName)) {
                progressBar.setIndeterminate(false);
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            } else if ("state".equals(strPropertyName) && worker.getState() == SwingWorker.StateValue.DONE) {
                this.progressBar.setVisible(false);
                this.progressBar.setValue(0);
                statusLabel.setText("Ready");
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        private Cursor oldCursor;
        private JFileChooser chooser;

        @Override
        public void mouseClicked(MouseEvent evt) {

            if (iseditable) {
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
                    File file = chooser.getSelectedFile();
                    try {
                        if (!file.exists()) {
                            file = new File("./images/male.jpg");
                        }

                        if (evt.getSource() == imagePanel) {
                            FileInputStream fileInputStream = new FileInputStream(file);

                            pictureStream = new byte[(int) file.length()];
                            fileInputStream.read(pictureStream);

                            BufferedImage img = ImageIO.read(file);
                            painter.setImage(img);
//                        painter.setScaleToFit(true);
                            painter.setFillHorizontal(true);
                            painter.setFillVertical(true);
                            painter.setAntialiasing(true);

                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
            if (oldCursor == null) {
                oldCursor = imagePanel.getCursor();
                imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            if (oldCursor != null) {
                imagePanel.setCursor(oldCursor);
                oldCursor = null;
            }
        }
    }
}
