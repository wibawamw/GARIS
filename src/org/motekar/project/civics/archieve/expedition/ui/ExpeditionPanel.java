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
import java.util.Date;
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
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
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
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionFollower;
import org.motekar.project.civics.archieve.expedition.reports.ExpeditionCommJasper;
import org.motekar.project.civics.archieve.expedition.reports.ExpeditionJasper;
import org.motekar.project.civics.archieve.expedition.reports.ExpeditionProgressCommJasper;
import org.motekar.project.civics.archieve.expedition.reports.ExpeditionProgressJasper;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.master.ui.EmployeePickDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
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
public class ExpeditionPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private ExpeditionBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private ExpeditionList expeditionList = new ExpeditionList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     * 
     */
    private JTabbedPane tabPane = new JTabbedPane();
    private JXComboBox comboLetter = new JXComboBox();
    private JXTextField fieldDocNumber = new JXTextField();
    private JXComboBox comboCommander = new JXComboBox();
    private JXComboBox comboAssinedEmp = new JXComboBox();
    private JXTextField fieldEmpGrade = new JXTextField();
    private JXTextField fieldPosition = new JXTextField();
    private JXTextField fieldDeparture = new JXTextField();
    private JXTextField fieldDestination = new JXTextField();
    private JXComboBox comboTransport = new JXComboBox();
    private JXDatePicker fieldStartDate = new JXDatePicker();
    private JXDatePicker fieldEndDate = new JXDatePicker();
    private JXTextArea fieldPurpose = new JXTextArea();
    private JXTextArea fieldNotes = new JXTextArea();
    private JXTextField fieldCharge = new JXTextField();
    private JXComboBox comboChargeBudget = new JXComboBox();
    private JXComboBox comboProgram = new JXComboBox();
    private JXComboBox comboActivity = new JXComboBox();
    private JXHyperlink linkApprovalDate = new JXHyperlink();
    private JXHyperlink linkApprovalPlace = new JXHyperlink();
    private JLabel linkApproverTitle = new JLabel();
    private JLabel linkApprover = new JLabel();
    private JLabel linkApproverGrade = new JLabel();
    private JLabel linkApproverNIP = new JLabel();
    private ExpeditionFollowerTable followerTable = new ExpeditionFollowerTable();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private JCommandButton btInsEmployee = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelEmployee = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private LoadExpedition worker;
    private ExpeditionProgressListener progressListener;
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Expedition selectedExpedition = null;
    private StringBuilder errorString = new StringBuilder();
    private Approval approval = null;
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;
    private ArchieveProperties properties;

    public ExpeditionPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.properties = mainframe.getProperties();
        logic = new ExpeditionBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        expeditionList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar SPPD");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(expeditionList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Perjalanan Dinas merupakan fasilitas untuk mengisi dan membuat "
                + "Surat untuk perjalanan dinas pegawai negeri sipil\n\n"
                + "Tambah Surat Perjalanan Dinas\n"
                + "Untuk menambah Pegawai klik tombol paling kiri "
                + "kemudian isi data Surat Perjalanan Dinas baru yang akan ditambah "
                + "sebanyak tiga halaman tampilan aplikasi, setelah selesai mengisi"
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Perjalanan Dinas\n"
                + "Untuk merubah Surat Perjalanan Dinas klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Perjalanan Dinas yang telah ada, "
                + "kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Perjalanan Dinas\n"
                + "Untuk menghapus Surat Perjalanan Dinas klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Perjalanan Dinas "
                + "yang tepilih, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        String text2 = "Penjelasan Singkat\n"
                + "Untuk form input data Surat Perjalanan Dinas terdapat 3 halaman "
                + "input pada aplikasi / program, yang akan dijelaskan berikut ini :\n\n"
                + "Halaman Pertama\n"
                + "Halaman ini berisi data-data yang harus diisi sebagai berikut :\n\n"
                + "Nomor dokumen\n"
                + "Nomor dokumen adalah penomoran surat perjalanan dinas yang baku, "
                + "terdapat dimasing-masing dinas dapat berbeda-beda maupun sama, "
                + "penomoran ini diperlukan untuk pengarsipan.\n\n"
                + "Pejabat pemberi perintah\n"
                + "Pejabat yang menugaskan seorang pegawai untuk melakukan perjalanan "
                + "dinas tentunya untuk keperluan dinas tersebut maupun untuk keperluan "
                + "pegawai yang ditugaskan.\n\n"
                + "Pegawai yang diberi perintah\n"
                + "Pegawai yang diberi tugas melakukan perjalanan dinas. Dibawah pegawai "
                + "ini terdapat isian, yaitu Pangkat dan Golongan serta Jabatan / Instansi, "
                + "yang akan terisi secara otomatis setelah pegawai yang diberi perintah "
                + "diisi.\n\n"
                + "Maksud Perjalanan Dinas\n"
                + "Diisi dengan maksud dari dinas tersebut melakukan perjalanan dinas, "
                + "misalkan untuk menghadiri seminar keuangan nasional ataupun yang "
                + "lainnya\n\n"
                + "Transportasi\n"
                + "Transportasi yang digunakan dalam rangka melaksanakan perjalanan dinas, "
                + "bisa menggunakan bis, kereta api, perahu, maupun pesawat terbang\n\n"
                + "Halaman Kedua\n"
                + "Halaman ini berisi data-data yang harus diisi sebagai berikut :\n\n"
                + "Tempat Berangkat\n"
                + "Diisi dengan nama tempat keberangkatan pegawai yang ditugaskan\n\n"
                + "Tempat Tujuan\n"
                + "Diisi dengan nama tempat tujuan pegawai tersebut ditugaskan\n\n"
                + "Tanggal Berangkat\n"
                + "Tanggal mulai perjalanan dinas dilaksanakan\n\n"
                + "Tanggal selesai\n"
                + "Tanggal selesai perjalanan dinas dilaksanakan\n\n"
                + "Keterangan Lain-lain\n"
                + "Diisi keterangan lainnya jika memang ada, tidak usah diisi"
                + "jika tidak ada keterangan tambahan\n\n"
                + "Pengesahan\n"
                + "Untuk mengisi tempat dan tanggal pengesahan klik di tulisan .... pada aplikasi "
                + "untuk memunculkan dialog tempat dan tanggal pengesahan. Begitu juga untuk mengisi "
                + "pejabat yang menandatangai pengesahan dokumen surat perjalana dinas ini klik pada "
                + "tulisan .... untuk memunculkan dialog pilih pegawai.\n\n"
                + "Halaman Ketiga\n"
                + "Halaman ini berisi data-data yang harus diisi sebagai berikut :\n\n"
                + "Tabel Pengikut (Posisi Atas)\n"
                + "Tabel pengikut ini diisi jika pada saat melakukan perjalanan dinas "
                + "pegawai yang ditugas lebih dari satu. Untuk melakukan penambahan pegawai "
                + "yang ikut serta dalam perjalanan dinas ini, klik tombol '+' (diatasnya) "
                + "kemudian akan muncul dialog pilih pegawai, pilih pegawai yang akan diikut "
                + "sertakan, bisa cuma satu ataupun banyak. Untuk memilih banyak klik pegawai sambil "
                + "menekan tombol SHIFT atau CTRL pada keyboard.\n\n"
                + "Halaman Cetak\n"
                + "Halaman ini untuk mencetak dokumen surat perjalanan dinas yang telah disimpan. "
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
        task.setTitle("Tambah / Edit / Hapus Surat Perjalanan Dinas");
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
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 15}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPurpose);

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(fieldNotes);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Surat Tugas", cc.xy(1, 1));
        builder.add(comboLetter, cc.xyw(3, 1, 2));

        builder.addLabel("Nomor Dokumen", cc.xy(1, 3));
        builder.add(fieldDocNumber, cc.xyw(3, 3, 2));

        builder.addLabel("Pejabat pemberi perintah", cc.xy(1, 5));
        builder.add(comboCommander, cc.xyw(3, 5, 2));

        builder.addLabel("Pegawai yang diperintahkan", cc.xy(1, 7));
        builder.add(comboAssinedEmp, cc.xyw(3, 7, 2));

        builder.addLabel("      Pangkat dan Golongan", cc.xy(1, 9));
        builder.add(fieldEmpGrade, cc.xyw(3, 9, 2));

        builder.addLabel("      Jabatan", cc.xy(1, 11));
        builder.add(fieldPosition, cc.xyw(3, 11, 2));

        builder.addLabel("Maksud Perjalanan Dinas", cc.xy(1, 13));
        builder.add(scPane, cc.xywh(3, 13, 2, 3));

        builder.addLabel("Transportasi", cc.xy(1, 17));
        builder.add(comboTransport, cc.xyw(3, 17, 2));

        builder.addLabel("Tempat Berangkat", cc.xy(1, 19));
        builder.add(fieldDeparture, cc.xyw(3, 19, 2));

        builder.addLabel("Tempat Tujuan", cc.xy(1, 21));
        builder.add(fieldDestination, cc.xyw(3, 21, 2));

        builder.addLabel("Tanggal Berangkat", cc.xy(1, 23));
        builder.add(fieldStartDate, cc.xyw(3, 23, 2));

        builder.addLabel("Tanggal Selesai", cc.xy(1, 25));
        builder.add(fieldEndDate, cc.xyw(3, 25, 2));

        tabPane.addTab("Input Data Hal. 1", builder.getPanel());
        tabPane.addTab("Input Data Hal. 2", createMainPanelPage2());
        tabPane.addTab("Input Data Hal. 3", createMainPanelPage3());
        tabPane.addTab("Cetak", createPrintPanel());

        return tabPane;
    }

    private Component createMainPanelPage2() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,center:pref,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,fill:default:grow,5px,"
                + "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldNotes);

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Pembebanan Anggaran", cc.xyw(1, 1, 5));

        builder.addLabel("      Instansi", cc.xy(1, 3));
        builder.add(fieldCharge, cc.xyw(3, 3, 3));

        builder.addSeparator("      Mata Anggaran", cc.xyw(1, 5, 5));

        builder.addLabel("            Program", cc.xy(1, 7));
        builder.add(comboProgram, cc.xyw(3, 7, 3));

        builder.addLabel("            Kegiatan", cc.xy(1, 9));
        builder.add(comboActivity, cc.xyw(3, 9, 3));

        builder.addLabel("            Akun", cc.xy(1, 11));
        builder.add(comboChargeBudget, cc.xyw(3, 11, 3));

        builder.addLabel("Keterangan Lain-lain", cc.xy(1, 13));
        builder.add(scPane, cc.xywh(3, 13, 3, 2));

        builder.add(createApprovalPanel(), cc.xy(4, 15));
        builder.add(createApprovalPanel2(), cc.xy(4, 17));


        return builder.getPanel();
    }

    private Component createMainPanelPage3() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,10px,pref,fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 2, 5}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(followerTable);

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Pengikut", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(scPane, cc.xywh(1, 4, 3, 2));

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
                "pref,30px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApproverTitle, cc.xy(1, 1));

        builder.add(linkApprover, cc.xy(1, 3));
        builder.addSeparator("", cc.xyw(1, 5, 2));
        builder.add(linkApproverGrade, cc.xy(1, 7));
        builder.add(linkApproverNIP, cc.xy(1, 9));

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
        addTooltip.setTitle("Tambah SPPD");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah SPPD");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan SPPD");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus SPPD");

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


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsEmployee);
        buttonStrip.add(btDelEmployee);


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
        loadComboTransport();
        loadComboLetter();
        loadComboAccount();
        loadComboProgram();

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
                expeditionList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                expeditionList.loadData();
            }
        });

        fieldStartDate.addActionListener(this);
        fieldEndDate.addActionListener(this);

        fieldStartDate.setFormats("dd/MM/yyyy");
        fieldEndDate.setFormats("dd/MM/yyyy");

        fieldPurpose.setWrapStyleWord(true);
        fieldPurpose.setLineWrap(true);

        fieldNotes.setWrapStyleWord(true);
        fieldNotes.setLineWrap(true);

        comboCommander.setEditable(true);
        comboAssinedEmp.setEditable(true);
        comboTransport.setEditable(true);
        comboLetter.setEditable(true);
        comboChargeBudget.setEditable(true);
        comboProgram.setEditable(true);
        comboActivity.setEditable(true);

        comboCommander.setAction(new ExpeditionAction());
        comboAssinedEmp.setAction(new ExpeditionAction());
        comboTransport.setAction(new ExpeditionAction());
        comboLetter.setAction(new ExpeditionAction());
        comboProgram.setAction(new ExpeditionAction());

        expeditionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expeditionList.setAutoCreateRowSorter(true);
        expeditionList.addListSelectionListener(this);

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(followerTable, ComponentState.DEFAULT);

        followerTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        followerTable.setShowGrid(false, false);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsEmployee.addActionListener(this);
        btDelEmployee.addActionListener(this);

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

    public void filter() {
        expeditionList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        comboLetter.setEnabled(iseditable);
        fieldDocNumber.setEnabled(iseditable);
        fieldDeparture.setEnabled(iseditable);
        fieldDestination.setEnabled(iseditable);
        fieldEmpGrade.setEnabled(false);
        fieldEndDate.setEnabled(iseditable);
        fieldNotes.setEnabled(iseditable);
        fieldPurpose.setEnabled(false);
        fieldStartDate.setEnabled(iseditable);
        fieldPosition.setEnabled(false);

        comboCommander.setEnabled(false);
        comboAssinedEmp.setEnabled(iseditable);
        comboTransport.setEnabled(iseditable);

        linkApprovalDate.setEnabled(iseditable);
        linkApprovalPlace.setEnabled(iseditable);
        linkApproverTitle.setEnabled(iseditable);
        linkApprover.setEnabled(iseditable);
        linkApproverGrade.setEnabled(iseditable);
        linkApproverNIP.setEnabled(iseditable);

        fieldCharge.setEnabled(iseditable);
        comboChargeBudget.setEnabled(iseditable);
        comboProgram.setEnabled(iseditable);
        comboActivity.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        expeditionList.setEnabled(!iseditable);

        btInsEmployee.setEnabled(iseditable);
        btDelEmployee.setEnabled(iseditable);

        followerTable.setEditable(iseditable);
    }

    private void clearForm() {

        fieldDocNumber.setText("");
        fieldDeparture.setText("");
        fieldDestination.setText("");
        fieldEmpGrade.setText("");
        fieldEndDate.setDate(null);
        fieldNotes.setText("");
        fieldPurpose.setText("");
        fieldStartDate.setDate(null);
        fieldPosition.setText("");

        fieldCharge.setText("");
        if (comboLetter.getItemCount() > 0) {
            comboLetter.setSelectedIndex(0);
        }
        if (comboChargeBudget.getItemCount() > 0) {
            comboChargeBudget.setSelectedIndex(0);
        }

        if (comboProgram.getItemCount() > 0) {
            comboProgram.setSelectedIndex(0);
        }

        if (comboActivity.getItemCount() > 0) {
            comboActivity.setSelectedIndex(0);
        }

        approval = null;

        linkApprovalPlace.setText("..........................................");
        linkApprovalDate.setText("..........................................");
        linkApproverTitle.setText("............................................................................................");
        linkApprover.setText("..........................................");
        linkApproverGrade.setText("..........................................");
        linkApproverNIP.setText("NIP. ..........................................");

        if (comboCommander.getItemCount() > 0) {
            comboCommander.setSelectedIndex(0);
        }

        if (comboAssinedEmp.getItemCount() > 0) {
            comboAssinedEmp.setSelectedIndex(0);
        }

        comboTransport.setSelectedIndex(0);

        followerTable.clear();

        if (comboLetter.isEnabled()) {
            comboLetter.requestFocus();
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

    private void loadComboEmployee() {
        comboCommander.removeAllItems();
        comboAssinedEmp.removeAllItems();
        try {
            ArrayList<Employee> commanderEmployee = mLogic.getCommanderEmployee(mainframe.getSession());
            ArrayList<Employee> assignedEmployee = mLogic.getEmployee(mainframe.getSession());

            if (!commanderEmployee.isEmpty()) {
                for (Employee e : commanderEmployee) {
                    e.setStyled(false);
                }
                for (Employee e : assignedEmployee) {
                    e.setStyled(false);
                }

                commanderEmployee.add(0, new Employee());
                assignedEmployee.add(0, new Employee());
                comboCommander.setModel(new ListComboBoxModel<Employee>(commanderEmployee));
                comboAssinedEmp.setModel(new ListComboBoxModel<Employee>(assignedEmployee));

                AutoCompleteDecorator.decorate(comboCommander, new EmployeeConverter());
                AutoCompleteDecorator.decorate(comboAssinedEmp, new EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboTransport() {
        comboTransport.removeAllItems();
        comboTransport.setModel(new ListComboBoxModel<String>(StandardPrice.transportTypeAsList()));
        AutoCompleteDecorator.decorate(comboTransport);
    }

    private void loadComboLetter() {
        comboLetter.removeAllItems();
        try {
            ArrayList<AssignmentLetter> letter = logic.getAssignmentLetter(mainframe.getSession());

            if (!letter.isEmpty()) {

                for (AssignmentLetter e : letter) {
                    e = logic.getCompleteAssignmentLetter(mainframe.getSession(), e);
                    e.setStyled(false);
                }

                letter.add(0, new AssignmentLetter());
                comboLetter.setModel(new ListComboBoxModel<AssignmentLetter>(letter));
                AutoCompleteDecorator.decorate(comboLetter);
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboAccount() {
        try {
            ArrayList<Account> accounts = mLogic.getAccount(mainframe.getSession());

            accounts.add(0, new Account());
            comboChargeBudget.setModel(new ListComboBoxModel<Account>(accounts));
            AutoCompleteDecorator.decorate(comboChargeBudget);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboProgram() {
        try {
            ArrayList<Program> programs = mLogic.getProgram(mainframe.getSession());

            programs.add(0, new Program());
            comboProgram.setModel(new ListComboBoxModel<Program>(programs));
            AutoCompleteDecorator.decorate(comboProgram);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboActivity(Program program) {
        try {
            ArrayList<Activity> activitys = mLogic.getActivity(mainframe.getSession(), program.getIndex());

            activitys.add(0, new Activity());
            comboActivity.setModel(new ListComboBoxModel<Activity>(activitys));
            AutoCompleteDecorator.decorate(comboActivity);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void setFormValues() {
        if (selectedExpedition != null) {
            try {
                selectedExpedition = logic.getCompleteExpedition(mainframe.getSession(), selectedExpedition);

                fieldDocNumber.setText(selectedExpedition.getDocumentNumber());
                fieldDeparture.setText(selectedExpedition.getDeparture());
                fieldDestination.setText(selectedExpedition.getDestination());
                fieldStartDate.setDate(selectedExpedition.getStartDate());
                fieldEndDate.setDate(selectedExpedition.getEndDate());
                fieldNotes.setText(selectedExpedition.getNotes());

                fieldEmpGrade.setText("");
                fieldPosition.setText("");

                fieldCharge.setText(selectedExpedition.getCharge());

                AssignmentLetter letter = selectedExpedition.getLetter();
                Employee commander = letter.getCommander();

                comboLetter.setSelectedItem(letter);
                fieldPurpose.setText(letter.getPurpose());

                if (selectedExpedition.getAccount() == null) {
                    comboChargeBudget.setSelectedIndex(0);
                } else {
                    comboChargeBudget.setSelectedItem(selectedExpedition.getAccount());
                }

                if (selectedExpedition.getProgram() == null) {
                    comboProgram.setSelectedIndex(0);
                } else {
                    comboProgram.setSelectedItem(selectedExpedition.getProgram());
                }

                if (selectedExpedition.getActivity() == null) {
                    comboActivity.setSelectedIndex(0);
                } else {
                    comboActivity.setSelectedItem(selectedExpedition.getActivity());
                }

                if (commander.isGorvernor()) {

                    int count = comboCommander.getItemCount();
                    int index = -1;
                    for (int i = 0; i < count; i++) {
                        Object obj = comboCommander.getItemAt(i);
                        Employee comm = null;
                        if (obj instanceof Employee) {
                            comm = (Employee) obj;
                            if (comm.isGorvernor() && comm.getName().equals(selectedExpedition.getLetter().getCommander().getName())) {
                                index = i;
                                break;
                            }
                        }
                    }
                    comboCommander.setSelectedIndex(index);
                } else {
                    comboCommander.setSelectedItem(commander);
                }

                comboAssinedEmp.setSelectedItem(selectedExpedition.getAssignedEmployee());

                fieldEmpGrade.setText(selectedExpedition.getAssignedEmployee().getGradeAsString());

                StringBuilder position = new StringBuilder();

                if (selectedExpedition.getAssignedEmployee().getStrukturalAsString().equals("")) {
                    position.append(selectedExpedition.getAssignedEmployee().getFungsionalAsString()).
                            append(" ").append(selectedExpedition.getAssignedEmployee().getPositionNotes());
                } else {
                    position.append(selectedExpedition.getAssignedEmployee().getStrukturalAsString());
                }

                fieldPosition.setText(position.toString());

                comboTransport.setSelectedIndex(selectedExpedition.getTransportation());

                approval = new Approval();
                approval.setPlace(selectedExpedition.getApprovalPlace());
                approval.setDate(selectedExpedition.getApprovalDate());

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

                linkApprovalPlace.setText(approval.getPlace());
                linkApprovalDate.setText(sdf.format(approval.getDate()));


                if (commander.isGorvernor()) {
                    linkApprover.setText(commander.getName());
                    linkApproverGrade.setVisible(false);
                    linkApproverNIP.setVisible(false);
                    if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                        linkApproverTitle.setText("BUPATI " + properties.getState().toUpperCase());
                    } else {
                        linkApproverTitle.setText("WALIKOTA " + properties.getState().toUpperCase());
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
                    linkApproverGrade.setVisible(true);
                    linkApproverNIP.setVisible(true);
                    linkApprover.setText(commander.getName());
                    linkApproverGrade.setText(commander.getPangkatAsString());
                    linkApproverNIP.setText("Nip. " + commander.getNip());
                }

                followerTable.clear();

                followerTable.addExpeditionFollower(selectedExpedition.getFollower());
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

    private Expedition getExpedition() throws MotekarException {
        errorString = new StringBuilder();

        AssignmentLetter letter = null;
        Object letterObj = comboLetter.getSelectedItem();

        if (letterObj instanceof AssignmentLetter) {
            letter = (AssignmentLetter) letterObj;
        }

        if (letter == null) {
            errorString.append("<br>- Surat Tugas</br>");
        }

        String documentNumber = fieldDocNumber.getText();

        if (documentNumber.equals("")) {
            errorString.append("<br>- Nomor Dokumen</br>");
        }

        Employee assinedEmployee = null;

        Object objAss = comboAssinedEmp.getSelectedItem();

        if (objAss instanceof Employee) {
            assinedEmployee = (Employee) objAss;
        }

        if (assinedEmployee == null) {
            errorString.append("<br>- Pegawai yang diperintahkan</br>");
        }

        Integer transportation = comboTransport.getSelectedIndex();

        if (transportation <= 0) {
            errorString.append("<br>- Transportasi</br>");
        }

        String departure = fieldDeparture.getText();

        if (departure.equals("")) {
            errorString.append("<br>- Tempat Berangkat</br>");
        }

        String destination = fieldDestination.getText();

        if (destination.equals("")) {
            errorString.append("<br>- Tempat Tujuan</br>");
        }

        Date startDate = fieldStartDate.getDate();

        if (startDate == null) {
            errorString.append("<br>- Tanggal Berangkat</br>");
        }

        Date endDate = fieldEndDate.getDate();

        if (endDate == null) {
            errorString.append("<br>- Tanggal Selesai</br>");
        }

        if (approval == null) {
            errorString.append("<br>- Tempat Pengesahan</br>");
            errorString.append("<br>- Tanggal Pengesahan</br>");
        }

        Program program = null;
        Object progObj = comboProgram.getSelectedItem();

        if (progObj instanceof Program) {
            program = (Program) progObj;
        }

        if (comboProgram.getSelectedIndex() == 0) {
            program = null;
        }

        Activity activity = null;
        Object actObj = comboActivity.getSelectedItem();

        if (actObj instanceof Activity) {
            activity = (Activity) actObj;
        }

        if (comboActivity.getSelectedIndex() == 0) {
            activity = null;
        }

        Account account = null;
        Object accObj = comboChargeBudget.getSelectedItem();

        if (accObj instanceof Account) {
            account = (Account) accObj;
        }

        if (comboChargeBudget.getSelectedIndex() == 0) {
            account = null;
        }

        if (program != null && activity == null && account == null) {
            errorString.append("<br>- Kegiatan</br>");
            errorString.append("<br>- Akun</br>");
        } else if (program == null && activity != null && account == null) {
            errorString.append("<br>- Program</br>");
            errorString.append("<br>- Akun</br>");
        } else if (program == null && activity == null && account != null) {
            errorString.append("<br>- Program</br>");
            errorString.append("<br>- Kegiatan</br>");
        } else if (program != null && activity != null && account == null) {
            errorString.append("<br>- Akun</br>");
        } else if (program != null && activity == null && account != null) {
            errorString.append("<br>- Kegiatan</br>");
        } else if (program == null && activity != null && account != null) {
            errorString.append("<br>- Program</br>");
        }

        ArrayList<ExpeditionFollower> followers = followerTable.getFollowers();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }


        Expedition expedition = new Expedition();
        expedition.setLetter(letter);
        expedition.setDocumentNumber(documentNumber);
        expedition.setAssignedEmployee(assinedEmployee);
        expedition.setTransportation(transportation);
        expedition.setDeparture(departure);
        expedition.setDestination(destination);
        expedition.setStartDate(startDate);
        expedition.setEndDate(endDate);

        expedition.setNotes(fieldNotes.getText());
        expedition.setApprovalPlace(approval.getPlace());
        expedition.setApprovalDate(approval.getDate());

        expedition.setFollower(followers);
        expedition.setCharge(fieldCharge.getText());
        expedition.setAccount(account);
        expedition.setActivity(activity);
        expedition.setProgram(program);

        return expedition;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Surat Perjalanan Dinas");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocNumber.requestFocus();
        fieldDocNumber.selectAll();
        statusLabel.setText("Ubah Surat Perjalanan Dinas");
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
                logic.deleteExpedition(mainframe.getSession(), selectedExpedition.getIndex());
                expeditionList.removeSelected(selectedExpedition);
                clearForm();
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    private void onSave() {
        try {
            Expedition newExpedition = getExpedition();

            if (isnew) {
                newExpedition = logic.insertExpedition(mainframe.getSession(), newExpedition);
                isnew = false;
                iseditable = false;
                newExpedition.setStyled(true);
                expeditionList.addExpedition(newExpedition);
                selectedExpedition = newExpedition;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newExpedition = logic.updateExpedition(mainframe.getSession(), selectedExpedition, newExpedition);
                isedit = false;
                iseditable = false;
                newExpedition.setStyled(true);
                expeditionList.updateExpedition(newExpedition);
                selectedExpedition = newExpedition;
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
        if (expeditionList.getElementCount() > 0) {
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
            EmployeePickDlg dlg = new EmployeePickDlg(mainframe, mainframe.getSession(), mainframe.getConnection(), true);
            dlg.showDialog();
            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                ArrayList<Employee> employees = dlg.getSelectedEmployees();
                if (!employees.isEmpty()) {
                    followerTable.addFollowers(employees);
                }
            }

        } else if (obj == btDelEmployee) {
            ArrayList<ExpeditionFollower> follower = followerTable.getSelectedFollowers();

            if (!follower.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    followerTable.removeFollower(follower);
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
            expeditionList.loadData();
        } else if (obj == fieldStartDate) {
            checkExpeditionDate();
        } else if (obj == fieldEndDate) {
            checkExpeditionDate();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedExpedition = expeditionList.getSelectedExpedition();
            setFormValues();
        }
    }

    private void checkExpeditionDate() {
        if (iseditable && isnew) {

            Date startDate = fieldStartDate.getDate();
            Date enddate = fieldEndDate.getDate();
            Employee assinedEmployee = null;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Object objAss = comboAssinedEmp.getSelectedItem();

            if (objAss instanceof Employee) {
                assinedEmployee = (Employee) objAss;
            }

            if (assinedEmployee != null && !assinedEmployee.equals(new Employee()) && startDate != null && enddate != null) {
                try {
                    boolean found = logic.getExpeditionRangeCheck(mainframe.getSession(), startDate, enddate, assinedEmployee.getIndex());
                    if (found) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Pegawai dengan Nama ").
                                append(assinedEmployee.getName()).
                                append(" sedang melakukan perjalanan dinas pada rentang tanggal ").
                                append(sdf.format(startDate)).append(" s/d ").append(sdf.format(enddate)).
                                append(", silahkan ganti Tanggal Berangkat atau ").
                                append("Tanggal Selesai");

                        JXLabel label = new JXLabel();
                        label.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);
                        label.setLineWrap(true);
                        label.setMaxLineSpan(400);
                        label.setText(builder.toString());

                        JOptionPane.showMessageDialog(this, label, "Perhatian", JOptionPane.WARNING_MESSAGE);
                        fieldStartDate.setDate(null);
                        fieldEndDate.setDate(null);
                    }

                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
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
        printWorker = new LoadPrintPanel(jasperPrint, selectedExpedition, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(comboLetter.getEditor().getEditorComponent());
        comp.add(fieldDocNumber);
        comp.add(comboAssinedEmp.getEditor().getEditorComponent());
        comp.add(comboTransport.getEditor().getEditorComponent());
        comp.add(fieldDeparture);
        comp.add(fieldDestination);
        comp.add(fieldStartDate.getEditor());
        comp.add(fieldEndDate.getEditor());
        comp.add(fieldCharge);
        comp.add(comboProgram.getEditor().getEditorComponent());
        comp.add(comboActivity.getEditor().getEditorComponent());
        comp.add(comboChargeBudget.getEditor().getEditorComponent());
        comp.add(fieldNotes);
        comp.add(linkApprovalPlace);
        comp.add(linkApprovalDate);
        comp.add(linkApprover);
        comp.add(linkApproverNIP);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class ExpeditionList extends JXList {

        private Icon EXPEDITION_ICON = Mainframe.getResizableIconFromSource("resource/Spd.png", new Dimension(40, 40));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(40, 40));

        public ExpeditionList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadExpedition((DefaultListModel) getModel());
            progressListener = new ExpeditionProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Expedition getSelectedExpedition() {
            Expedition expedition = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Expedition) {
                expedition = (Expedition) obj;
            }
            return expedition;
        }

        public void addExpedition(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(expedition);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateExpedition(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(expedition, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Expedition> expeditions) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(expeditions);
            filter();
        }

        public void removeSelected(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(expedition);
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

                    Expedition expedition = null;

                    if (o instanceof Expedition) {
                        expedition = (Expedition) o;
                    }

                    if (expedition != null) {
                        return ExpeditionList.this.EXPEDITION_ICON;
                    }

                    return ExpeditionList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadExpedition extends SwingWorker<DefaultListModel, Expedition> {

        private DefaultListModel model;
        private Exception exception;

        public LoadExpedition(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Expedition> chunks) {
            mainframe.stopInActiveListener();
            for (Expedition expedition : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Perjalanan Dinas Nomor " + expedition.getDocumentNumber());
                model.addElement(expedition);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

                if (checkBox.isSelected()) {
                    expeditions = logic.getExpedition(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    expeditions = logic.getExpedition(mainframe.getSession());
                }

                double progress = 0.0;
                if (!expeditions.isEmpty()) {
                    for (int i = 0; i < expeditions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / expeditions.size();
                        setProgress((int) progress);
                        publish(expeditions.get(i));
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
                JXErrorPane.showDialog(ExpeditionPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ExpeditionProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ExpeditionProgressListener() {
        }

        ExpeditionProgressListener(JProgressBar progressBar) {
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

    private class ExpeditionFollowerTable extends JXTable {

        private ExpeditionFollowerTableModel model = new ExpeditionFollowerTableModel();

        public ExpeditionFollowerTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public ExpeditionFollower getSelectedFollower() {
            ExpeditionFollower follower = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof ExpeditionFollower) {
                follower = (ExpeditionFollower) obj;
            }

            return follower;
        }

        public ArrayList<ExpeditionFollower> getFollowers() {
            return model.getFollowers();
        }

        public ArrayList<ExpeditionFollower> getSelectedFollowers() {

            ArrayList<ExpeditionFollower> followers = new ArrayList<ExpeditionFollower>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    ExpeditionFollower follower = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof ExpeditionFollower) {
                        follower = (ExpeditionFollower) obj;
                        followers.add(follower);
                    }
                }
            }

            return followers;
        }

        public void updateSelectedFollower(ExpeditionFollower follower) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedFollower(), follower);
        }

        public void removeFollower(ArrayList<ExpeditionFollower> followers) {
            if (!followers.isEmpty()) {
                for (ExpeditionFollower follower : followers) {
                    model.remove(follower);
                }
            }

        }

        public void addExpeditionFollower(ArrayList<ExpeditionFollower> followers) {
            if (!followers.isEmpty()) {
                for (ExpeditionFollower follower : followers) {
                    model.add(follower);
                }
            }
        }

        public void addFollowers(ArrayList<Employee> employees) {
            ArrayList<ExpeditionFollower> followers = new ArrayList<ExpeditionFollower>();
            for (Employee employee : employees) {
                ExpeditionFollower follower = new ExpeditionFollower();
                follower.setFollower(employee);
                followers.add(follower);
            }

            if (!followers.isEmpty()) {
                for (ExpeditionFollower follower : followers) {
                    model.add(follower);
                }
            }
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 1) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy")), JLabel.CENTER);
            }
            return super.getCellRenderer(row, column);
        }
    }

    private static class ExpeditionFollowerTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 3;
        private static final String[] columnIds = {"Nama", "Tanggal Lahir", "Keterangan"};
        private ArrayList<ExpeditionFollower> followers = new ArrayList<ExpeditionFollower>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                return true;
            }
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<ExpeditionFollower> followers) {
            int first = followers.size();
            int last = first + followers.size() - 1;
            followers.addAll(followers);
            fireTableRowsInserted(first, last);
        }

        public void add(ExpeditionFollower follower) {
            if (!followers.contains(follower)) {
                insertRow(getRowCount(), follower);
            }
        }

        public void insertRow(int row, ExpeditionFollower follower) {
            followers.add(row, follower);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, ExpeditionFollower oldFollower, ExpeditionFollower newFollower) {
            int index = followers.indexOf(oldFollower);
            followers.set(index, newFollower);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ExpeditionFollower follower) {
            int row = followers.indexOf(follower);
            followers.remove(follower);
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
                followers.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                followers.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            followers.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (followers.get(i) == null) {
                    followers.set(i, new ExpeditionFollower());
                }
            }
        }

        public ArrayList<ExpeditionFollower> getFollowers() {
            return followers;
        }

        @Override
        public int getRowCount() {
            return followers.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ExpeditionFollower getFollower(int row) {
            if (!followers.isEmpty()) {
                return followers.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ExpeditionFollower follower = getFollower(row);
            switch (column) {
                case 0:
                    follower = (ExpeditionFollower) aValue;
                    break;
                case 1:
                    follower.getFollower().setBirthDate(((Employee) aValue).getBirthDate());
                    break;
                case 2:
                    follower.setNotes((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ExpeditionFollower follower = getFollower(row);
            switch (column) {
                case 0:
                    toBeDisplayed = follower;
                    break;
                case 1:
                    toBeDisplayed = follower.getFollower().getBirthDate();
                    break;
                case 2:
                    toBeDisplayed = follower.getNotes();
                    break;
            }
            return toBeDisplayed;
        }
    }

    public static class EmployeeConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof Employee)) {
                return new String[0];
            }
            Employee employee = (Employee) item;
            return new String[]{
                        employee.toString(), employee.getName(), employee.getNip()
                    };
        }

        public String getPreferredStringForItem(Object item) {
            String[] possible = getPossibleStringsForItem(item);
            String preferred = null;
            if (possible != null && possible.length > 0) {
                preferred = possible[0];
            }
            return preferred;
        }
    }

    private class ExpeditionAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == comboAssinedEmp) {
                checkExpeditionDate();
                Object obj = comboAssinedEmp.getSelectedItem();
                if (obj != null) {
                    if (obj instanceof Employee) {
                        Employee employee = (Employee) obj;
                        fieldEmpGrade.setText(employee.getGradeAsString());
                        if (employee.getStrukturalAsString().equals("")) {
                            fieldPosition.setText(employee.getFungsionalAsString());
                        } else {
                            fieldPosition.setText(employee.getStrukturalAsString());
                        }
                    }

                }
            } else if (source == comboCommander) {

                Object obj = comboCommander.getSelectedItem();

                if (obj != null) {
                    if (obj instanceof Employee) {
                        Employee commander = (Employee) obj;
                        if (commander.isGorvernor()) {
                            linkApprover.setText(commander.getName());
                            linkApproverGrade.setVisible(false);
                            linkApproverNIP.setVisible(false);
                            if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                                linkApproverTitle.setText("BUPATI " + properties.getState().toUpperCase());
                            } else {
                                linkApproverTitle.setText("WALIKOTA " + properties.getState().toUpperCase());
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
                            linkApproverGrade.setVisible(true);
                            linkApproverNIP.setVisible(true);
                            linkApprover.setText(commander.getName());
                            linkApproverGrade.setText(commander.getPangkatAsString());
                            linkApproverNIP.setText("Nip. " + commander.getNip());
                        } else {
                            linkApproverTitle.setText("............................................................................................");
                            linkApprover.setText("..........................................");
                            linkApproverGrade.setText("..........................................");
                            linkApproverNIP.setText("NIP. ..........................................");
                        }
                    }
                }
            } else if (source == comboLetter) {
                Object obj = comboLetter.getSelectedItem();
                if (obj != null) {
                    if (obj instanceof AssignmentLetter) {
                        try {
                            AssignmentLetter letter = (AssignmentLetter) obj;
                            Employee commander = letter.getCommander();
                            if (commander != null) {
                                commander = mLogic.getEmployeeByIndex(mainframe.getSession(), commander.getIndex());
                                comboCommander.setSelectedItem(commander);
                            }

                            fieldPurpose.setText(letter.getPurpose());

                        } catch (SQLException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }

                }
            } else if (source == comboProgram) {
                Object obj = comboProgram.getSelectedItem();
                if (obj != null) {
                    if (obj instanceof Program) {
                        Program program = (Program) obj;
                        loadComboActivity(program);
                    }
                }
            }
        }
    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, JRPrintPage> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private Expedition expedition = null;

        public LoadPrintPanel(JasperPrint jasperPrint, Expedition expedition, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.expedition = expedition;
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
        protected void process(List<JRPrintPage> chunks) {
            mainframe.stopInActiveListener();
            for (JRPrintPage page : chunks) {
                if (isCancelled()) {
                    break;
                }

                jasperPrint.addPage(page);
                viewerPanel.reload(jasperPrint);
            }
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                if (expedition.getLetter().getCommander().isGorvernor()) {
                    ExpeditionCommJasper expJasper = new ExpeditionCommJasper(expedition, properties);
                    jasperPrint = expJasper.getJasperPrint();
                } else {
                    ExpeditionJasper expJasper = new ExpeditionJasper(expedition, properties);
                    jasperPrint = expJasper.getJasperPrint();
                }

                setProgress(50);

                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        viewerPanel.reload(jasperPrint);
                    }
                });

                ArrayList<JRPrintPage> pages = new ArrayList<JRPrintPage>();

                if (expedition.getLetter().getCommander().isGorvernor()) {
                    ExpeditionProgressCommJasper epcj = new ExpeditionProgressCommJasper(expedition.getLetter().getCommander(), properties);
                    pages.addAll(epcj.getPages());
                } else {
                    ExpeditionProgressJasper epj = new ExpeditionProgressJasper(expedition.getLetter().getCommander(), properties);
                    pages.addAll(epj.getPages());
                }

                if (!pages.isEmpty()) {
                    for (int i = 0; i < pages.size() && !isCancelled(); i++) {
                        publish(pages.get(i));
                        Thread.sleep(100L);
                    }
                    setProgress(100);
                }
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
                JXErrorPane.showDialog(ExpeditionPanel.this, info);
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
