package org.motekar.project.civics.archieve.expedition.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHyperlink;
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
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.reports.AssignmentLetterCommJasper;
import org.motekar.project.civics.archieve.expedition.reports.AssignmentLetterJasper;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.expedition.ui.ExpeditionJournalPanel.EmployeeConverter;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.master.ui.EmployeePanelCellEditor;
import org.motekar.project.civics.archieve.master.ui.EmployeePanelCellRenderer;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.motekar.util.user.misc.MotekarException;
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
public class AssignmentLetterPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private ExpeditionBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private ArchieveProperties properties;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    private JTabbedPane tabPane = new JTabbedPane();
    private JXTextField fieldDocNumber = new JXTextField();
    private JXComboBox comboCommander = new JXComboBox();
    private JXTextArea fieldPurpose = new JXTextArea();
    private JXHyperlink linkApprovalDate = new JXHyperlink();
    private JXHyperlink linkApprovalPlace = new JXHyperlink();
    private JLabel linkApproverTitle = new JLabel();
    private JLabel linkApproverTitle2 = new JLabel();
    private JLabel linkApprover = new JLabel();
    private JLabel linkApproverGrade = new JLabel();
    private JLabel linkApproverNIP = new JLabel();
    private CarbonCopyTable copyTable = new CarbonCopyTable();
    private AssignmentList assignmentList = new AssignmentList();
    private AssignedEmployeeTable employeetable;
    private LoadAssignment worker;
    private AssignmentProgressListener progressListener;
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private JCommandButton btInsEmployee = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelEmployee = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private JCommandButton btInsCopy = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelCopy = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    private Approval approval = null;
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private StringBuilder errorString = new StringBuilder();
    private AssignmentLetter selectedLetter = null;

    public AssignmentLetterPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.properties = mainframe.getProperties();
        logic = new ExpeditionBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        employeetable = new AssignedEmployeeTable();
        construct();
        assignmentList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Tugas");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(assignmentList), BorderLayout.CENTER);
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

        JXLabel helpLabel2 = new JXLabel();
        helpLabel2.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Fasilitas Surat Tugas merupakan fasilitas untuk mengisi dan membuat "
                + "Surat untuk perjalanan dinas pegawai negeri sipil\n\n"
                + "Tambah Surat Tugas\n"
                + "Untuk menambah Pegawai klik tombol paling kiri "
                + "kemudian isi data Surat Tugas baru yang akan ditambah "
                + "sebanyak tiga halaman tampilan aplikasi, setelah selesai mengisi"
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Tugas\n"
                + "Untuk merubah Surat Tugas klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Tugas yang telah ada, "
                + "kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Tugas\n"
                + "Untuk menghapus Surat Tugas klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Tugas "
                + "yang tepilih, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        String text2 = "Penjelasan Singkat\n"
                + "Untuk form input data Surat Tugas terdapat 3 halaman "
                + "input pada aplikasi / program, yang akan dijelaskan berikut ini :\n\n"
                + "Halaman Pertama\n"
                + "Halaman ini berisi data-data yang harus diisi sebagai berikut :\n\n"
                + "Nomor dokumen\n"
                + "Nomor dokumen adalah penomoran Surat Tugas yang baku, "
                + "terdapat dimasing-masing dinas dapat berbeda-beda maupun sama, "
                + "penomoran ini diperlukan untuk pengarsipan.\n\n"
                + "Pejabat pemberi perintah\n"
                + "Pejabat yang menugaskan seorang pegawai untuk melakukan perjalanan "
                + "dinas tentunya untuk keperluan dinas tersebut maupun untuk keperluan "
                + "pegawai yang ditugaskan.\n\n"
                + "Pegawai yang dtugaskan\n"
                + "Pegawai yang diberi tugas melakukan perjalanan dinas. "
                + "Klik Tombol plus disebelahkan untuk memasukan pegawai yang ditugaskan\n\n"
                + "Maksud Penugasan\n"
                + "Diisi dengan maksud dari dinas tersebut melakukan perjalanan dinas, "
                + "misalkan untuk menghadiri seminar keuangan nasional ataupun yang "
                + "lainnya\n\n"
                + "Halaman Kedua\n"
                + "Halaman ini berisi data-data yang harus diisi sebagai berikut :\n\n"
                + "Tembusan\n"
                + "Isi tembusan surat ini ditujukan jika ada.\n\n"
                + "Halaman Cetak\n"
                + "Halaman ini untuk mencetak dokumen Surat Tugas yang telah disimpan. "
                + "Pada halaman ini berisi preview cetak sebelum dicetak untuk melakukan pencetakan "
                + "pada printer klik tombol bergambar printer.";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        helpLabel2.setLineWrap(true);
        helpLabel2.setMaxLineSpan(300);
        helpLabel2.setText(text2);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Surat Tugas");
        task.getContentPane().add(helpLabel);
        task.setAnimated(true);

        JXTaskPane task2 = new JXTaskPane();
        task2.setTitle("Penjelasan Masing-masing Halaman");
        task2.getContentPane().add(helpLabel2);
        task2.setAnimated(true);

        container.add(task);
        container.add(task2);

        helpLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        helpLabel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,pref,pref",
                "pref,5px,pref,5px,pref,fill:default:grow,fill:default:grow,100px, "
                + "5px,pref,fill:default:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 10}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPurpose);

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(employeetable);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nomor Dokumen", cc.xy(1, 1));
        builder.add(fieldDocNumber, cc.xyw(3, 1, 3));

        builder.addLabel("Pejabat pemberi perintah", cc.xy(1, 3));
        builder.add(comboCommander, cc.xyw(3, 3, 3));


        builder.addLabel("Pegawai yang ditugaskan", cc.xy(1, 5));
        builder.add(scPane2, cc.xywh(3, 5, 2, 4));
        builder.add(createStrip2(1.0, 1.0), cc.xy(5, 5));

        builder.addLabel("Untuk melaksanakan tugas", cc.xy(1, 10));
        builder.add(scPane, cc.xywh(3, 10, 3, 2));

        tabPane.addTab("Input Data Hal.1", builder.getPanel());
        tabPane.addTab("Input Data Hal.2", createMainPanelPage2());
        tabPane.addTab("Cetak", createPrintPanel());

        return tabPane;
    }

    private Component createMainPanelPage2() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,center:pref,10px",
                "pref,fill:default:grow,fill:default:grow,fill:default:grow,fill:default:grow,"
                + "10px,fill:default,fill:default:grow,pref,"
                + "fill:default:grow,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 2}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(copyTable);

        CellConstraints cc = new CellConstraints();

        builder.add(createStrip3(1.0, 1.0), cc.xy(1, 1));
        builder.add(scPane, cc.xywh(1, 2, 5, 5));

        builder.add(createApprovalPanel(), cc.xy(4, 7));
        builder.add(createApprovalPanel2(), cc.xy(4, 9));

        return builder.getPanel();
    }

    private Component createApprovalPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,10px,fill:default:grow,10px",
                "pref,2px,pref");

        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Dikeluarkan di", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.add(linkApprovalPlace, cc.xy(5, 1));

        builder.addLabel("Pada Tanggal", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.add(linkApprovalDate, cc.xy(5, 3));


        return builder.getPanel();
    }

    private Component createApprovalPanel2() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,30px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApproverTitle, cc.xy(1, 1));
        builder.add(linkApproverTitle2, cc.xy(1, 3));

        builder.add(linkApprover, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkApproverGrade, cc.xy(1, 9));
        builder.add(linkApproverNIP, cc.xy(1, 11));

        return builder.getPanel();
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,5px, fill:default:grow,5px,fill:default:grow", "pref,4px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        monthChooser.setYearChooser(yearChooser);
        monthChooser.setEnabled(checkBox.isSelected());
        yearChooser.setEnabled(checkBox.isSelected());

        checkBox.addActionListener(this);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1, 3));

        builder.add(checkBox, cc.xy(1, 3));
        builder.add(monthChooser, cc.xyw(3, 3, 2));
        builder.add(yearChooser, cc.xy(5, 3));

        return builder.getPanel();
    }

    private JPanel createButtonsPanel() {
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
        addTooltip.setTitle("Tambah Surat Tugas");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Tugas");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Tugas");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Tugas");

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
        addTooltip.setTitle("Tambah Pengikut");

        btInsEmployee.setActionRichTooltip(addTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Pengikut");

        btDelEmployee.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip(StripOrientation.VERTICAL);
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsEmployee);
        buttonStrip.add(btDelEmployee);


        return buttonStrip;
    }

    private JCommandButtonStrip createStrip3(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Tembusan");

        btInsCopy.setActionRichTooltip(addTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Tembusan");

        btDelCopy.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsCopy);
        buttonStrip.add(btDelCopy);


        return buttonStrip;
    }

    private JPanel createPrintPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "fill:default:grow,10px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(panelViewer, cc.xywh(1, 1, 3, 2));
        builder.add(viewerBar, cc.xyw(1, 3, 3));

        return builder.getPanel();
    }

    private void construct() {

        loadComboEmployee();

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

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                assignmentList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                assignmentList.loadData();
            }
        });

        fieldPurpose.setWrapStyleWord(true);
        fieldPurpose.setLineWrap(true);

        comboCommander.setEditable(true);

        comboCommander.setAction(new AssignmentAction());

        assignmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentList.setAutoCreateRowSorter(true);
        assignmentList.addListSelectionListener(this);

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(copyTable, ComponentState.DEFAULT);

        copyTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        copyTable.setShowGrid(false, false);

        color = skin.getColorScheme(employeetable, ComponentState.DEFAULT);
        employeetable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkLightColor()));
        employeetable.setShowGrid(false, false);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsEmployee.addActionListener(this);
        btDelEmployee.addActionListener(this);
        btInsCopy.addActionListener(this);
        btDelCopy.addActionListener(this);

        linkApprovalPlace.addActionListener(this);
        linkApprovalDate.addActionListener(this);

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
        clearForm();
        setButtonState("");
    }

    private void loadComboEmployee() {
        comboCommander.removeAllItems();
        try {
            ArrayList<Employee> commanderEmployee = mLogic.getCommanderEmployee(mainframe.getSession());

            if (!commanderEmployee.isEmpty()) {
                for (Employee e : commanderEmployee) {
                    e.setStyled(false);
                }

                commanderEmployee.add(0, new Employee());
                comboCommander.setModel(new ListComboBoxModel<Employee>(commanderEmployee));

                AutoCompleteDecorator.decorate(comboCommander, new EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void filter() {
        assignmentList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldDocNumber.setEnabled(iseditable);
        fieldPurpose.setEnabled(iseditable);

        comboCommander.setEnabled(iseditable);

        linkApprovalDate.setEnabled(iseditable);
        linkApprovalPlace.setEnabled(iseditable);
        linkApproverTitle.setEnabled(iseditable);
        linkApproverTitle2.setEnabled(iseditable);
        linkApprover.setEnabled(iseditable);
        linkApproverGrade.setEnabled(iseditable);
        linkApproverNIP.setEnabled(iseditable);


        fieldSearch.setEnabled(!iseditable);
        assignmentList.setEnabled(!iseditable);

        btInsEmployee.setEnabled(iseditable);
        btDelEmployee.setEnabled(iseditable);
        btInsCopy.setEnabled(iseditable);
        btDelCopy.setEnabled(iseditable);

        copyTable.setEditable(iseditable);
        employeetable.setEditable(iseditable);
    }

    private void clearForm() {

        fieldDocNumber.setText("");
        fieldPurpose.setText("");

        approval = null;

        linkApprovalPlace.setText("..........................................");
        linkApprovalDate.setText("..........................................");
        linkApproverTitle.setText("............................................................................................");
        linkApproverTitle2.setText("............................................................................................");
        linkApprover.setText("..........................................");
        linkApproverGrade.setText("..........................................");
        linkApproverNIP.setText("NIP. ..........................................");

        if (comboCommander.getItemCount() > 0) {
            comboCommander.setSelectedIndex(0);
        }

        copyTable.clear();
        employeetable.clear();

        if (fieldDocNumber.isEnabled()) {
            fieldDocNumber.requestFocus();
            fieldDocNumber.selectAll();
        }
        jasperRemoveAll();
    }

    private void setButtonState(String state) {
        if (state.equals("New")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(true);
            btCancel.setEnabled(true);
            panelViewer.setButtonEnable(false);
        } else if (state.equals("Save")) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            panelViewer.setButtonEnable(true);

        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            panelViewer.setButtonEnable(true);
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        generateCode();
        statusLabel.setText("Tambah Surat Tugas");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocNumber.requestFocus();
        fieldDocNumber.selectAll();
        statusLabel.setText("Ubah Surat Tugas");
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
                boolean inexp = logic.getAssignmentLetterInExpedition(mainframe.getSession(), selectedLetter.getIndex());

                if (!inexp) {
                    logic.deleteAssignmentLetter(mainframe.getSession(), selectedLetter.getIndex());
                    assignmentList.removeSelected(selectedLetter);
                    clearForm();
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<html>Surat Tugas dengan Nomor ").
                            append("<b>").
                            append(selectedLetter.getDocumentNumber()).
                            append("</b>").
                            append(" tidak boleh dihapus karena sudah dipakai di SPPD");
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
            AssignmentLetter newLetter = getAssignmentLetter();

            if (isnew) {
                newLetter = logic.insertAssignmentLetter(mainframe.getSession(), newLetter);
                isnew = false;
                iseditable = false;
                newLetter.setStyled(true);
                assignmentList.addAssignmentLetter(newLetter);
                selectedLetter = newLetter;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newLetter = logic.updateAssignmentLetter(mainframe.getSession(), selectedLetter, newLetter);
                isedit = false;
                iseditable = false;
                newLetter.setStyled(true);
                assignmentList.updateAssignmentLetter(newLetter);
                selectedLetter = newLetter;
                setFormState();
                setButtonState("Save");
            }
            statusLabel.setText("Ready");
            reloadPrintPanel();
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
        if (assignmentList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
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
        } else if (obj == btInsEmployee) {
            employeetable.insertEmptyEmployee();
        } else if (obj == btDelEmployee) {
            ArrayList<Employee> employees = employeetable.getSelectedEmployees();

            if (!employees.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    employeetable.removeEmployees(employees);
                }
            }
        } else if (obj == linkApprovalPlace) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
            ApprovalDlg dlg = new ApprovalDlg(mainframe, approval);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                approval = dlg.getApproval();
                linkApprovalPlace.setText(approval.getPlace());
                linkApprovalDate.setText(sdf.format(approval.getDate()));
            }
        } else if (obj == linkApprovalDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
            ApprovalDlg dlg = new ApprovalDlg(mainframe, approval);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                approval = dlg.getApproval();
                linkApprovalPlace.setText(approval.getPlace());
                linkApprovalDate.setText(sdf.format(approval.getDate()));
            }
        } else if (obj == checkBox) {
            monthChooser.setEnabled(checkBox.isSelected());
            yearChooser.setEnabled(checkBox.isSelected());
            assignmentList.loadData();
        } else if (obj == btInsCopy) {
            copyTable.insertEmptyCopy();
        } else if (obj == btDelCopy) {
            ArrayList<String> copy = copyTable.getSelectedCopys();

            if (!copy.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    copyTable.removeCopys(copy);
                }
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedLetter = assignmentList.getSelectedAssignmentLetter();
            setFormValues();
        }
    }

    private void generateCode() {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            Integer year = cal.get(Calendar.YEAR);
            String code = logic.generatedAssignmentLetterCode(mainframe.getSession(), year);
            fieldDocNumber.setText(code);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void setFormValues() {
        if (selectedLetter != null) {
            try {
                selectedLetter = logic.getCompleteAssignmentLetter(mainframe.getSession(), selectedLetter);

                fieldDocNumber.setText(selectedLetter.getDocumentNumber());
                fieldPurpose.setText(selectedLetter.getPurpose());

                Employee commander = selectedLetter.getCommander();

                if (commander.isGorvernor()) {

                    int count = comboCommander.getItemCount();
                    int index = -1;
                    for (int i = 0; i < count; i++) {
                        Object obj = comboCommander.getItemAt(i);
                        Employee comm = null;
                        if (obj instanceof Employee) {
                            comm = (Employee) obj;
                            if (comm.isGorvernor() && comm.getName().equals(selectedLetter.getCommander().getName())) {
                                index = i;
                                break;
                            }
                        }
                    }
                    comboCommander.setSelectedIndex(index);
                } else {
                    comboCommander.setSelectedItem(commander);
                }

                approval = new Approval();
                approval.setPlace(selectedLetter.getApprovalPlace());
                approval.setDate(selectedLetter.getApprovalDate());

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

                linkApprovalPlace.setText(approval.getPlace());
                linkApprovalDate.setText(sdf.format(approval.getDate()));


                if (commander.isGorvernor()) {
                    linkApprover.setText(commander.getName());
                    linkApproverGrade.setVisible(false);
                    linkApproverNIP.setVisible(false);
                    linkApproverTitle.setText(commander.getPositionNotes().toUpperCase());
                    if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                        linkApproverTitle2.setText(ArchieveProperties.KABUPATEN.toUpperCase() + " " + properties.getState().toUpperCase());
                    } else {
                        linkApproverTitle2.setText(ArchieveProperties.KOTAMADYA.toUpperCase() + " " + properties.getState().toUpperCase());
                    }
                } else {
                    StringBuilder pos = new StringBuilder();
                    if (commander.getStrukturalAsString().equals("")) {
                        pos.append(commander.getFungsionalAsString()).
                                append(" ").append(commander.getPositionNotes());
                    } else {
                        pos.append(commander.getStrukturalAsString());
                    }
                    linkApproverTitle.setText(pos.toString());
                    if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                        linkApproverTitle2.setText(ArchieveProperties.KABUPATEN.toUpperCase() + " " + properties.getState().toUpperCase());
                    } else {
                        linkApproverTitle2.setText(ArchieveProperties.KOTAMADYA.toUpperCase() + " " + properties.getState().toUpperCase());
                    }
                    linkApproverGrade.setVisible(true);
                    linkApproverNIP.setVisible(true);
                    linkApprover.setText(commander.getName());
                    linkApproverGrade.setText(commander.getPangkatAsString());
                    linkApproverNIP.setText("Nip. " + commander.getNip());
                }

                copyTable.clear();
                employeetable.clear();

                copyTable.addCopys(selectedLetter.getCarbonCopy());
                employeetable.addEmployees(selectedLetter.getAssignedEmployee());

                reloadPrintPanel();

                setButtonState("Save");

            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat pengambilan data ",
                        null, "Error", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        } else {
            clearForm();
            setButtonState("");
            jasperPrint = null;
            panelViewer.reload(jasperPrint);
        }
    }

    private void jasperRemoveAll() {
        int size = jasperPrint.getPages().size();
        for (int i = 0; i < size; i++) {
            jasperPrint.removePage(i);
        }
        panelViewer.reload(jasperPrint);
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, selectedLetter, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private AssignmentLetter getAssignmentLetter() throws MotekarException {
        errorString = new StringBuilder();

        String documentNumber = fieldDocNumber.getText();

        if (documentNumber.equals("")) {
            errorString.append("<br>- Nomor Dokumen</br>");
        }

        Employee commander = null;

        Object objComm = comboCommander.getSelectedItem();

        if (objComm instanceof Employee) {
            commander = (Employee) objComm;
        }

        if (commander == null) {
            errorString.append("<br>- Pejabat pemberi perintah</br>");
        }


        String pupose = fieldPurpose.getText();

        if (pupose.equals("")) {
            errorString.append("<br>- Untuk melaksanakan tugas</br>");
        }

        if (approval == null) {
            errorString.append("<br>- Tempat Pengesahan</br>");
            errorString.append("<br>- Tanggal Pengesahan</br>");
        }


        ArrayList<String> carbonCopy = copyTable.getCopys();
        ArrayList<Employee> assignedEmployees = employeetable.getEmployees();

        if (assignedEmployees.isEmpty()) {
            errorString.append("<br>- Pegawai Yang Diperintahkan</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        AssignmentLetter letter = new AssignmentLetter();
        letter.setDocumentNumber(documentNumber);
        letter.setCommander(commander);
        letter.setPurpose(pupose);
        letter.setApprovalPlace(approval.getPlace());
        letter.setApprovalDate(approval.getDate());

        letter.setCarbonCopy(carbonCopy);
        letter.setAssignedEmployee(assignedEmployees);

        return letter;
    }

    private class AssignmentList extends JXList {

        private Icon LETTER_ICON = Mainframe.getResizableIconFromSource("resource/paper_text.png", new Dimension(40, 40));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(40, 40));

        public AssignmentList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadAssignment((DefaultListModel) getModel());
            progressListener = new AssignmentProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public AssignmentLetter getSelectedAssignmentLetter() {
            AssignmentLetter letter = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof AssignmentLetter) {
                letter = (AssignmentLetter) obj;
            }
            return letter;
        }

        public void addAssignmentLetter(AssignmentLetter letter) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(letter);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateAssignmentLetter(AssignmentLetter letter) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(letter, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<AssignmentLetter> letters) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(letters);
            filter();
        }

        public void removeSelected(AssignmentLetter letter) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(letter);
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

                    AssignmentLetter letter = null;

                    if (o instanceof AssignmentLetter) {
                        letter = (AssignmentLetter) o;
                    }

                    if (letter != null) {
                        return AssignmentList.this.LETTER_ICON;
                    }

                    return AssignmentList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadAssignment extends SwingWorker<DefaultListModel, AssignmentLetter> {

        private DefaultListModel model;
        private Exception exception;

        public LoadAssignment(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<AssignmentLetter> chunks) {
            mainframe.stopInActiveListener();
            for (AssignmentLetter letter : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Tugas Nomor " + letter.getDocumentNumber());
                model.addElement(letter);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<AssignmentLetter> letters = new ArrayList<AssignmentLetter>();

                if (checkBox.isSelected()) {
                    letters = logic.getAssignmentLetter(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    letters = logic.getAssignmentLetter(mainframe.getSession());
                }

                double progress = 0.0;
                if (!letters.isEmpty()) {
                    for (int i = 0; i < letters.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / letters.size();
                        setProgress((int) progress);
                        publish(letters.get(i));
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
                JXErrorPane.showDialog(AssignmentLetterPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class AssignmentProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private AssignmentProgressListener() {
        }

        AssignmentProgressListener(JProgressBar progressBar) {
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

    private class CarbonCopyTable extends JXTable {

        private CarbonCopyTableModel model;

        public CarbonCopyTable() {
            model = new CarbonCopyTableModel();
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public String getSelectedCopy() {
            String copy = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof String) {
                copy = (String) obj;
            }

            return copy;
        }

        public ArrayList<String> getCopys() {
            return model.getCopys();
        }

        public ArrayList<String> getSelectedCopys() {

            ArrayList<String> copys = new ArrayList<String>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    String copy = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof String) {
                        copy = (String) obj;
                        copys.add(copy);
                    }
                }
            }

            return copys;
        }

        public void updateSelectedCopy(String copy) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedCopy(), copy);
        }

        public void removeCopys(ArrayList<String> copys) {
            if (!copys.isEmpty()) {
                for (String copy : copys) {
                    model.remove(copy);
                }
            }

        }

        public void addCopys(ArrayList<String> copys) {
            if (!copys.isEmpty()) {
                for (String copy : copys) {
                    model.add(copy);
                }
            }
        }

        public void addCopy(String copy) {
            model.add(copy);
        }

        public void insertEmptyCopy() {
            addCopy("");
        }
    }

    private static class CarbonCopyTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 1;
        private static final String[] COLUMN_COPY = {"Tembusan"};
        private ArrayList<String> copys = new ArrayList<String>();

        public CarbonCopyTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_COPY[column];
        }

        public void add(ArrayList<String> copy) {
            int first = copys.size();
            int last = first + copys.size() - 1;
            copys.addAll(copy);
            fireTableRowsInserted(first, last);
        }

        public void add(String copy) {
            insertRow(getRowCount(), copy);
        }

        public void insertRow(int row, String copy) {
            copys.add(row, copy);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, String oldCopy, String newCopy) {
            int index = copys.indexOf(oldCopy);
            copys.set(index, newCopy);
            fireTableRowsUpdated(index, index);
        }

        public void remove(String copy) {
            int row = copys.indexOf(copy);
            copys.remove(copy);
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
                copys.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                copys.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            copys.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (copys.get(i) == null) {
                    copys.set(i, "");
                }
            }
        }

        public ArrayList<String> getCopys() {
            return copys;
        }

        @Override
        public int getRowCount() {
            return copys.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public String getCopy(int row) {
            if (!copys.isEmpty()) {
                return copys.get(row);
            }
            return "";
        }

        public void setCopy(int row, String copy) {
            copys.set(row, copy);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            switch (column) {
                case 0:
                    if (aValue instanceof String) {
                        String copy = (String) aValue;
                        setCopy(row, copy);
                    }
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            String copy = getCopy(row);
            switch (column) {
                case 0:
                    toBeDisplayed = copy;
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class AssignedEmployeeTable extends JXTable {

        private AssignedEmployeeTableModel model;

        public AssignedEmployeeTable() {
            model = new AssignedEmployeeTableModel();
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
            setRowHeight(100);
        }

        public void clear() {
            model.clear();
        }

        public Employee getSelectedEmployee() {
            Employee employee = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof Employee) {
                employee = (Employee) obj;
            }

            return employee;
        }

        public ArrayList<Employee> getEmployees() {
            return model.getEmployees();
        }

        public ArrayList<Employee> getSelectedEmployees() {

            ArrayList<Employee> employees = new ArrayList<Employee>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    Employee employee = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof Employee) {
                        employee = (Employee) obj;
                        employees.add(employee);
                    }
                }
            }

            return employees;
        }

        public void updateSelectedEmployee(Employee employee) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedEmployee(), employee);
        }

        public void removeEmployees(ArrayList<Employee> employees) {
            if (!employees.isEmpty()) {
                for (Employee emp : employees) {
                    model.remove(emp);
                }
            }

        }

        public void addEmployees(ArrayList<Employee> employees) {
            if (!employees.isEmpty()) {
                for (Employee emp : employees) {
                    model.add(emp);
                }
            }
        }

        public void addEmployee(Employee employee) {
            model.add(employee);
        }

        public void insertEmptyEmployee() {
            addEmployee(new Employee());
        }

        @Override
        public TableCellEditor getDefaultEditor(Class<?> columnClass) {
            if (columnClass.equals(Employee.class)) {
                return new EmployeePanelCellEditor(mainframe.getConnection(), mainframe.getSession());
            }
            return super.getDefaultEditor(columnClass);
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Employee.class)) {
                return new EmployeePanelCellRenderer(mainframe.getConnection(), mainframe.getSession());
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class AssignedEmployeeTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 1;
        private static final String[] COLUMN = {"Daftar Pegawai"};
        private ArrayList<Employee> employees = new ArrayList<Employee>();

        public AssignedEmployeeTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN[column];
        }

        public void add(ArrayList<Employee> employee) {
            int first = employees.size();
            int last = first + employees.size() - 1;
            employees.addAll(employee);
            fireTableRowsInserted(first, last);
        }

        public void add(Employee employee) {
            if (!employees.contains(employee)) {
                insertRow(getRowCount(), employee);
            }
        }

        public void insertRow(int row, Employee employee) {
            employees.add(row, employee);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, Employee oldEmp, Employee newEmp) {
            int index = employees.indexOf(oldEmp);
            employees.set(index, newEmp);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Employee employee) {
            int row = employees.indexOf(employee);
            employees.remove(employee);
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
                employees.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                employees.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            employees.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (employees.get(i) == null) {
                    employees.set(i, new Employee());
                }
            }
        }

        public ArrayList<Employee> getEmployees() {
            return employees;
        }

        @Override
        public int getRowCount() {
            return employees.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Employee.class;
            }
            return super.getColumnClass(columnIndex);
        }

        public Employee getEmployee(int row) {
            if (!employees.isEmpty()) {
                return employees.get(row);
            }
            return null;
        }

        public void setEmployee(int row, Employee employee) {
            if (!employees.isEmpty()) {
                employees.set(row, employee);
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            switch (column) {
                case 0:
                    if (aValue instanceof Employee) {
                        Employee employee = (Employee) aValue;
                        setEmployee(row, employee);
                    }
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Employee employee = getEmployee(row);
            toBeDisplayed = employee;

            return toBeDisplayed;
        }
    }

    private class AssignmentAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == comboCommander) {

                Object obj = comboCommander.getSelectedItem();

                if (obj != null) {
                    if (obj instanceof Employee) {
                        Employee commander = (Employee) obj;
                        if (commander.isGorvernor()) {
                            linkApprover.setText(commander.getName());
                            linkApproverGrade.setVisible(false);
                            linkApproverNIP.setVisible(false);
                            linkApproverTitle.setText(commander.getPositionNotes());
                            if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                                linkApproverTitle2.setText(ArchieveProperties.KABUPATEN.toUpperCase() + " " + properties.getState().toUpperCase());
                            } else {
                                linkApproverTitle2.setText(ArchieveProperties.KOTAMADYA.toUpperCase() + " " + properties.getState().toUpperCase());
                            }
                        } else if (!commander.getName().equals("")) {
                            StringBuilder pos = new StringBuilder();
                            if (commander.getStrukturalAsString().equals("")) {
                                pos.append(commander.getFungsionalAsString()).
                                        append(" ").append(commander.getPositionNotes());
                            } else {
                                pos.append(commander.getStrukturalAsString());
                            }
                            linkApproverTitle.setText(pos.toString());
                            if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                                linkApproverTitle2.setText(ArchieveProperties.KABUPATEN.toUpperCase() + " " + properties.getState().toUpperCase());
                            } else {
                                linkApproverTitle2.setText(ArchieveProperties.KOTAMADYA.toUpperCase() + " " + properties.getState().toUpperCase());
                            }
                            linkApproverGrade.setVisible(true);
                            linkApproverNIP.setVisible(true);
                            linkApprover.setText(commander.getName());
                            linkApproverGrade.setText(commander.getPangkatAsString());
                            linkApproverNIP.setText("Nip. " + commander.getNip());
                        } else {
                            linkApproverTitle.setText("............................................................................................");
                            linkApproverTitle2.setText("............................................................................................");
                            linkApprover.setText("..........................................");
                            linkApproverGrade.setText("..........................................");
                            linkApproverNIP.setText("NIP. ..........................................");
                        }
                    }
                }
            }
        }
    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, JRPrintPage> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private AssignmentLetter letter = null;

        public LoadPrintPanel(JasperPrint jasperPrint, AssignmentLetter letter, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.letter = letter;
            clearAll();
        }

        private void clearAll() {
            if (jasperPrint != null) {
                int size = jasperPrint.getPages().size();
                for (int i = 0; i < size; i++) {
                    jasperPrint.removePage(i);
                }
            }
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {
                if (letter.getCommander().isGorvernor()) {
                    AssignmentLetterCommJasper letterJasper = new AssignmentLetterCommJasper(letter, properties);
                    jasperPrint = letterJasper.getJasperPrint();
                } else {
                    AssignmentLetterJasper letterJasper = new AssignmentLetterJasper(letter, properties);
                    jasperPrint = letterJasper.getJasperPrint();
                }

                setProgress(100);

                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        viewerPanel.reload(jasperPrint);
                    }
                });
                return jasperPrint;
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
                JXErrorPane.showDialog(AssignmentLetterPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class JasperProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private JasperProgressListener() {
        }

        JasperProgressListener(JProgressBar progressBar) {
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
            } else if ("state".equals(strPropertyName) && printWorker.getState() == SwingWorker.StateValue.DONE) {
                this.progressBar.setVisible(false);
                this.progressBar.setValue(0);
            }
        }
    }
}
