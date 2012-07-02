package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
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
import javax.swing.table.AbstractTableModel;
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
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.project.civics.archieve.master.objects.EmployeeFacility;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
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
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
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
    private JSpinField fieldMkYear = new JSpinField(0, 56);
    private JSpinField fieldMkMonth = new JSpinField(0, 11);
    private JXTextArea fieldPosNotes = new JXTextArea();
    private JCheckBox checkGovernor = new JCheckBox();
    /**
     * 
     */
    private JXComboBox comboMarital = new JXComboBox();
    private JXTextField fieldSoulMate = new JXTextField();
    private JXComboBox comboChildren = new JXComboBox();
    private JXComboBox comboEducation = new JXComboBox();
    private JXTextField fieldDepartment = new JXTextField();
    private JXComboBox comboEselon = new JXComboBox();
    private JXDatePicker fieldCpnsTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldPnsTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldGradeTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldEselonTMT = new JXDatePicker(new Locale("in", "ID", "ID"));
    private EmployeeCoursesTable courseTable = new EmployeeCoursesTable();
    private EmployeeFacilityTable facilityTable = new EmployeeFacilityTable();
    /**
     * 
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Employee selectedEmployee = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadEmployee worker;
    private EmployeeProgressListener progressListener;
    private JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

    public EmployeePanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        employeeList.loadData();
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
                "right:pref,10px,fill:default:grow,50px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        checkGovernor.addActionListener(this);

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Kepala Daerah ?", cc.xy(1, 1));
        builder.add(checkGovernor, cc.xy(3, 1));

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
        builder.add(comboChildren, cc.xy(3, 19));

        tabPane.addTab("Pegawai", builder.getPanel());
        tabPane.addTab("Kepegawaian", createMainPanel2());
        tabPane.addTab("Diklat & Fasilitas", createMainPanel3());

        return tabPane;
    }

    protected Component createMainPanel2() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,5px,pref,5px,fill:default:grow,5px,pref,50px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,"
                + "fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPosNotes);

        fieldPosNotes.setToolTipText("<html>Diisi dengan keterangan Jabatan jika ada"
                + "<br>Misalkan 'Staf Bidang Perbendaharaan dan Verifikasi',</br>"
                + "<br>maka diisi dengan Bidang Perbendaharaan dan Verifikasi</br>");

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Masa Kerja", cc.xy(1, 1));
        builder.add(fieldMkYear, cc.xy(3, 1));
        builder.addLabel("Tahun", cc.xy(5, 1));
        builder.add(fieldMkMonth, cc.xy(7, 1));
        builder.addLabel("Bulan", cc.xy(9, 1));

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

        builder.addLabel("Jab. Fungsional", cc.xy(1, 15));
        builder.add(comboFungsional, cc.xyw(3, 15, 7));

        builder.addLabel("TMT Jabatan", cc.xy(1, 17));
        builder.add(fieldEselonTMT, cc.xyw(3, 17, 7));

        builder.addLabel("Keterangan Jabatan", cc.xy(1, 19));
        builder.add(scPane, cc.xywh(3, 19, 7, 2));

        return builder.getPanel();
    }

    protected Component createMainPanel3() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,50px",
                "pref,2px,pref,10px,pref,5px,pref,fill:default:grow,20px,"
                + "pref,5px,pref,fill:default:grow,5px,30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(courseTable);

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(facilityTable);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Pendidikan Terakhir", cc.xy(1, 1));
        builder.add(comboEducation, cc.xy(3, 1));

        builder.addLabel("Jurusan", cc.xy(1, 3));
        builder.add(fieldDepartment, cc.xy(3, 3));

        builder.addSeparator("Diklat", cc.xyw(1, 5, 3));

        builder.add(scPane, cc.xywh(1, 7, 3, 3));

        builder.addSeparator("Fasilitas", cc.xyw(1, 10, 3));

        builder.add(scPane2, cc.xywh(1, 12, 3, 3));


        return builder.getPanel();
    }

    protected JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,10px, fill:default:grow", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(new JXLabel("Cari"), fieldSearch);

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

    private void construct() {

        loadSex();
        loadReligion();
        loadGrade();
        loadFungsional();
        loadStruktural();
        loadMarital();
        loadChildren();
        loadEducation();
        loadEselon();

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

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(courseTable, ComponentState.DEFAULT);

        courseTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        courseTable.setShowGrid(false, false);

        color = skin.getColorScheme(facilityTable, ComponentState.DEFAULT);

        facilityTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        facilityTable.setShowGrid(false, false);

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

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.25)"
                + "(LEAF name=editor2 weight=0.5) (LEAF name=editor3 weight=0.25))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createLeftComponent(), "editor1");
        splitPane.add(createCenterComponent(), "editor2");
        splitPane.add(panel, "editor3");

        panel.setVisible(true);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setFormState();
        setButtonState("");
    }

    public void filter() {
        employeeList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
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
            fieldMkYear.setEnabled(iseditable);
            fieldMkMonth.setEnabled(iseditable);
            fieldPosNotes.setEnabled(iseditable);
            fieldSearch.setEnabled(!iseditable);
            employeeList.setEnabled(!iseditable);

            comboMarital.setEnabled(iseditable);
            fieldSoulMate.setEnabled(iseditable);
            comboChildren.setEnabled(iseditable);
            comboEducation.setEnabled(iseditable);
            fieldDepartment.setEnabled(iseditable);
            comboEselon.setEnabled(iseditable);
            fieldCpnsTMT.setEnabled(iseditable);
            fieldPnsTMT.setEnabled(iseditable);
            fieldGradeTMT.setEnabled(iseditable);
            fieldEselonTMT.setEnabled(iseditable);

            facilityTable.setEnabled(iseditable);
            courseTable.setEnabled(iseditable);

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
                fieldMkYear.setEnabled(false);
                fieldMkMonth.setEnabled(false);
                fieldPosNotes.setEnabled(false);
                fieldSearch.setEnabled(!iseditable);
                employeeList.setEnabled(!iseditable);

                comboMarital.setEnabled(iseditable);
                fieldSoulMate.setEnabled(iseditable);
                comboChildren.setEnabled(iseditable);
                comboEducation.setEnabled(iseditable);
                fieldDepartment.setEnabled(iseditable);
                comboEselon.setEnabled(false);
                fieldCpnsTMT.setEnabled(false);
                fieldPnsTMT.setEnabled(false);
                fieldGradeTMT.setEnabled(false);
                fieldEselonTMT.setEnabled(false);

                facilityTable.setEnabled(iseditable);
                courseTable.setEnabled(iseditable);

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
                fieldMkYear.setEnabled(iseditable);
                fieldMkMonth.setEnabled(iseditable);
                fieldPosNotes.setEnabled(iseditable);
                fieldSearch.setEnabled(!iseditable);
                employeeList.setEnabled(!iseditable);

                comboMarital.setEnabled(iseditable);
                fieldSoulMate.setEnabled(iseditable);
                comboChildren.setEnabled(iseditable);
                comboEducation.setEnabled(iseditable);
                fieldDepartment.setEnabled(iseditable);
                comboEselon.setEnabled(iseditable);
                fieldCpnsTMT.setEnabled(iseditable);
                fieldPnsTMT.setEnabled(iseditable);
                fieldGradeTMT.setEnabled(iseditable);
                fieldEselonTMT.setEnabled(iseditable);
                facilityTable.setEnabled(iseditable);
                courseTable.setEnabled(iseditable);
            }
        }

    }

    private void setFormChecked() {
        fieldNIP.setEnabled(!checkGovernor.isSelected());
        comboGrade.setEnabled(!checkGovernor.isSelected());
        comboFungsional.setEnabled(!checkGovernor.isSelected());
        comboStruktural.setEnabled(!checkGovernor.isSelected());
        fieldMkYear.setEnabled(!checkGovernor.isSelected());
        fieldMkMonth.setEnabled(!checkGovernor.isSelected());
        fieldPosNotes.setEnabled(!checkGovernor.isSelected());

        comboEselon.setEnabled(!checkGovernor.isSelected());
        fieldCpnsTMT.setEnabled(!checkGovernor.isSelected());
        fieldPnsTMT.setEnabled(!checkGovernor.isSelected());
        fieldGradeTMT.setEnabled(!checkGovernor.isSelected());
        fieldEselonTMT.setEnabled(!checkGovernor.isSelected());

        facilityTable.setEnabled(true);
        courseTable.setEnabled(true);

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
        fieldMkYear.setValue(Integer.valueOf(0));
        fieldMkMonth.setValue(Integer.valueOf(0));
        fieldPosNotes.setText("");

        comboMarital.setSelectedIndex(0);
        fieldSoulMate.setText("");
        comboChildren.setSelectedIndex(0);
        comboEducation.setSelectedIndex(0);
        fieldDepartment.setText("");
        comboEselon.setSelectedIndex(0);
        fieldCpnsTMT.setDate(null);
        fieldPnsTMT.setDate(null);
        fieldGradeTMT.setDate(null);
        fieldEselonTMT.setDate(null);

        courseTable.clear();
        facilityTable.clear();

        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
        }
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
        comboFungsional.setModel(new ListComboBoxModel<String>(Employee.fungsionalAsList()));
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

    private void loadChildren() {
        comboChildren.removeAllItems();
        comboChildren.setModel(new ListComboBoxModel<Integer>(Employee.childrenAsList()));
        AutoCompleteDecorator.decorate(comboChildren);
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

    private void createCourse() {
        if (isnew) {
            ArrayList<EmployeeCourses> courseses = EmployeeCourses.createCourses();
            courseTable.clear();
            courseTable.addEmployeeCourses(courseses);
        } else if (isedit) {

            if (selectedEmployee != null) {
                if (selectedEmployee.getCourseses().isEmpty()) {
                    ArrayList<EmployeeCourses> courseses = EmployeeCourses.createCourses();
                    courseTable.clear();
                    courseTable.addEmployeeCourses(courseses);
                }
            }
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
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        createCourse();
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
        createCourse();
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
                    fieldMkYear.setValue(Integer.valueOf(0));
                    fieldMkMonth.setValue(Integer.valueOf(0));
                    fieldPosNotes.setText(selectedEmployee.getPositionNotes());

                    comboMarital.setSelectedIndex(selectedEmployee.getMarital());
                    fieldSoulMate.setText(selectedEmployee.getSoulmate());
                    comboChildren.setSelectedIndex(selectedEmployee.getChildren());
                    comboEducation.setSelectedIndex(selectedEmployee.getEducation());
                    fieldDepartment.setText(selectedEmployee.getDepartment());
                    comboEselon.setSelectedIndex(0);
                    fieldCpnsTMT.setDate(null);
                    fieldPnsTMT.setDate(null);
                    fieldGradeTMT.setDate(null);
                    fieldEselonTMT.setDate(null);
                    courseTable.clear();
                    ArrayList<EmployeeCourses> courseses = selectedEmployee.getCourseses();
                    courseTable.addEmployeeCourses(courseses);

                    facilityTable.clear();
                    ArrayList<EmployeeFacility> facilitys = selectedEmployee.getFacilitys();
                    facilityTable.addEmployeeFacilitys(facilitys);

                    if (fieldName.isEnabled()) {
                        fieldName.requestFocus();
                        fieldName.selectAll();
                    }
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
                    fieldMkYear.setValue(selectedEmployee.getMkYear());
                    fieldMkMonth.setValue(selectedEmployee.getMkMonth());
                    fieldPosNotes.setText(selectedEmployee.getPositionNotes());

                    comboMarital.setSelectedIndex(selectedEmployee.getMarital());
                    fieldSoulMate.setText(selectedEmployee.getSoulmate());
                    comboChildren.setSelectedIndex(selectedEmployee.getChildren());
                    comboEducation.setSelectedIndex(selectedEmployee.getEducation());
                    fieldDepartment.setText(selectedEmployee.getDepartment());
                    comboEselon.setSelectedIndex(selectedEmployee.getEselon());
                    fieldCpnsTMT.setDate(selectedEmployee.getCpnsTMT());
                    fieldPnsTMT.setDate(selectedEmployee.getPnsTMT());
                    fieldGradeTMT.setDate(selectedEmployee.getGradeTMT());
                    fieldEselonTMT.setDate(selectedEmployee.getEselonTMT());
                    courseTable.clear();
                    ArrayList<EmployeeCourses> courseses = selectedEmployee.getCourseses();
                    courseTable.addEmployeeCourses(courseses);
                    facilityTable.clear();
                    ArrayList<EmployeeFacility> facilitys = selectedEmployee.getFacilitys();
                    facilityTable.addEmployeeFacilitys(facilitys);
                }

                setButtonState("Save");
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat memuat data ",
                        null, "Error", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
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
        
        Integer mkYear = fieldMkYear.getValue();
        Integer mkMonth = fieldMkMonth.getValue();

        if (name.equals("")) {
            errorString.append("<br>- Nama</br>");
        }

        if (sex <= 0) {
            errorString.append("<br>- Jenis Kelamin</br>");
        }

        if (!isGorvernor) {

            if (nip.equals("")) {
                errorString.append("<br>- NIP</br>");
            }

            if (grade <= 0) {
                errorString.append("<br>- Golongan</br>");
            }
        }

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
        employee.setNonEmployee(false);

        if (employee.getFungsional() == -1) {
            employee.setFungsional(Integer.valueOf(0));
        }

        if (employee.getStruktural() == -1) {
            employee.setStruktural(Integer.valueOf(0));
        }


        employee.setMarital(comboMarital.getSelectedIndex());
        employee.setSoulmate(fieldSoulMate.getText());
        employee.setChildren(comboChildren.getSelectedIndex());
        employee.setEselon(comboEselon.getSelectedIndex());
        employee.setEducation(comboEducation.getSelectedIndex());
        employee.setDepartment(fieldDepartment.getText());

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


        employee.setCpnsTMT(fieldCpnsTMT.getDate());
        employee.setPnsTMT(fieldPnsTMT.getDate());
        employee.setGradeTMT(fieldGradeTMT.getDate());
        employee.setEselonTMT(fieldEselonTMT.getDate());
        employee.setMkYear(mkYear);
        employee.setMkMonth(mkMonth);

        employee.setCourseses(courseses);
        employee.setFacilitys(facilitys);

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
        comp.add(comboChildren.getEditor().getEditorComponent());
        comp.add(fieldMkYear);
        comp.add(fieldMkMonth);
        comp.add(fieldCpnsTMT.getEditor());
        comp.add(fieldPnsTMT.getEditor());

        comp.add(comboGrade.getEditor().getEditorComponent());
        comp.add(fieldGradeTMT.getEditor());
        comp.add(comboEselon.getEditor().getEditorComponent());
        comp.add(comboStruktural.getEditor().getEditorComponent());
        comp.add(comboFungsional.getEditor().getEditorComponent());
        comp.add(fieldEselonTMT.getEditor());
        comp.add(fieldPosNotes);
        
        comp.add(comboEducation.getEditor().getEditorComponent());
        comp.add(fieldDepartment);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class EmployeeList extends JXList {

        private Icon MALE_ICON = Mainframe.getResizableIconFromSource("resource/user_male.png", new Dimension(36, 36));
        private Icon FEMALE_ICON = Mainframe.getResizableIconFromSource("resource/user_female.png", new Dimension(36, 36));
        //
        private Icon GOVERNOR_ICON = Mainframe.getResizableIconFromSource("resource/business_user.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public EmployeeList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadEmployee((DefaultListModel) getModel());
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

    private class EmployeeCoursesTable extends JXTable {

        private EmployeeCoursesTableModel model = new EmployeeCoursesTableModel();

        public EmployeeCoursesTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public EmployeeCourses getSelectedCourseses() {
            EmployeeCourses courses = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof EmployeeCourses) {
                courses = (EmployeeCourses) obj;
            }

            return courses;
        }

        public ArrayList<EmployeeCourses> getCoursesess() {
            return model.getCoursesess();
        }

        public ArrayList<EmployeeCourses> getSelectedCoursesess() {

            ArrayList<EmployeeCourses> coursess = new ArrayList<EmployeeCourses>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    EmployeeCourses courses = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof EmployeeCourses) {
                        courses = (EmployeeCourses) obj;
                        coursess.add(courses);
                    }
                }
            }

            return coursess;
        }

        public void updateSelectedCourseses(EmployeeCourses courses) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedCourseses(), courses);
        }

        public void removeCourseses(ArrayList<EmployeeCourses> coursess) {
            if (!coursess.isEmpty()) {
                for (EmployeeCourses courses : coursess) {
                    model.remove(courses);
                }
            }

        }

        public void addEmployeeCourses(ArrayList<EmployeeCourses> coursess) {
            if (!coursess.isEmpty()) {
                for (EmployeeCourses courses : coursess) {
                    model.add(courses);
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

    private static class EmployeeCoursesTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 2;
        private static final String[] columnIds = {"Nama Diklat", "Mengikuti"};
        private ArrayList<EmployeeCourses> coursess = new ArrayList<EmployeeCourses>();

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

        public void add(ArrayList<EmployeeCourses> coursess) {
            int first = coursess.size();
            int last = first + coursess.size() - 1;
            coursess.addAll(coursess);
            fireTableRowsInserted(first, last);
        }

        public void add(EmployeeCourses courses) {
            if (!coursess.contains(courses)) {
                insertRow(getRowCount(), courses);
            }
        }

        public void insertRow(int row, EmployeeCourses courses) {
            coursess.add(row, courses);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, EmployeeCourses oldCourseses, EmployeeCourses newCourseses) {
            int index = coursess.indexOf(oldCourseses);
            coursess.set(index, newCourseses);
            fireTableRowsUpdated(index, index);
        }

        public void remove(EmployeeCourses courses) {
            int row = coursess.indexOf(courses);
            coursess.remove(courses);
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
                coursess.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                coursess.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            coursess.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (coursess.get(i) == null) {
                    coursess.set(i, new EmployeeCourses());
                }
            }
        }

        public ArrayList<EmployeeCourses> getCoursesess() {
            return coursess;
        }

        @Override
        public int getRowCount() {
            return coursess.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public EmployeeCourses getCourseses(int row) {
            if (!coursess.isEmpty()) {
                return coursess.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            EmployeeCourses courses = getCourseses(row);
            switch (column) {
                case 0:
                    courses = (EmployeeCourses) aValue;
                    break;
                case 1:
                    courses.setAttending((Boolean) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            EmployeeCourses courses = getCourseses(row);
            switch (column) {
                case 0:
                    toBeDisplayed = courses;
                    break;
                case 1:
                    toBeDisplayed = courses.isAttending();
                    break;
            }
            return toBeDisplayed;
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

    private class LoadEmployee extends SwingWorker<DefaultListModel, Employee> {

        private DefaultListModel model;
        private Exception exception;

        public LoadEmployee(DefaultListModel model) {
            this.model = model;
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
                ArrayList<Employee> employees = logic.getEmployee(mainframe.getSession(), false);

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
}
