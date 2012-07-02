package org.motekar.project.civics.archieve.expedition.ui;

import anw.pattern.common.utils.SpellableNumber;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
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
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionCheque;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionFollower;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.master.ui.StandardPricePickDlg;
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

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionChequePanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private ExpeditionBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private ExpeditionChequeList chequeList = new ExpeditionChequeList();
    /**
     *
     */
    private JTabbedPane tabPane = new JTabbedPane();
    private JXComboBox comboExpedition = new JXComboBox();
    private JXComboBox comboAccount = new JXComboBox();
    private JXComboBox comboProgram = new JXComboBox();
    private JXComboBox comboActivity = new JXComboBox();
    private JXTextField fieldPayer = new JXTextField();
    private JXTextField fieldBankAccount = new JXTextField();
    private JXTextField fieldTaxNumber = new JXTextField();
    private JXTextField fieldReceivedFrom = new JXTextField();
    private JXTextArea fieldNotes = new JXTextArea();
    private JXFormattedTextField fieldAmount;
    private JXButton btPickStandardPrice = new JXButton(Mainframe.getResizableIconFromSource("resource/file_download.png", new Dimension(14, 14)));
    private JXTextField fieldReceivedPlace = new JXTextField();
    private JXComboBox comboHeadEmployee = new JXComboBox();
    private JXComboBox comboPPTK = new JXComboBox();
    private JXComboBox comboClerk = new JXComboBox();
    private JXComboBox comboPaidTo = new JXComboBox();
    private JXComboBox comboBudgetType = new JXComboBox();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private LoadExpeditionCheque worker;
    private ExpeditionChequesProgressListener progressListener;
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private StringBuilder errorString = new StringBuilder();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;
    private ArchieveProperties properties;
    private StandardPrice amount = null;
    private ExpeditionCheque selectedCheque = null;

    public ExpeditionChequePanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.properties = mainframe.getProperties();
        logic = new ExpeditionBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        chequeList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Kwitansi");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(chequeList), BorderLayout.CENTER);
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
                + "Fasilitas Kwitansi Pembayaran merupakan fasilitas untuk mengisi dan membuat "
                + "Surat untuk perjalanan dinas pegawai negeri sipil\n\n"
                + "Tambah Kwitansi Pembayaran\n"
                + "Untuk menambah Pegawai klik tombol paling kiri "
                + "kemudian isi data Kwitansi Pembayaran baru yang akan ditambah "
                + "sebanyak tiga halaman tampilan aplikasi, setelah selesai mengisi"
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Kwitansi Pembayaran\n"
                + "Untuk merubah Kwitansi Pembayaran klik tombol kedua dari kiri "
                + "kemudian ubah data Kwitansi Pembayaran yang telah ada, "
                + "kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Kwitansi Pembayaran\n"
                + "Untuk menghapus Kwitansi Pembayaran klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Kwitansi Pembayaran "
                + "yang tepilih, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Kwitansi Pembayaran");
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

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,pref,10px",
                "pref,3px,pref,3px,pref,3px,pref,3px,pref,3px,"
                + "pref,3px,pref,3px,pref,3px,pref,3px,pref,3px,"
                + "pref,fill:default:grow,3px,20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17, 19}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldNotes);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SPPD", cc.xy(1, 1));
        builder.add(comboExpedition, cc.xyw(3, 1, 2));

        builder.addLabel("    Program", cc.xy(1, 3));
        builder.add(comboProgram, cc.xyw(3, 3, 2));

        builder.addLabel("    Kegiatan", cc.xy(1, 5));
        builder.add(comboActivity, cc.xyw(3, 5, 2));

        builder.addLabel("    Kode Rekening", cc.xy(1, 7));
        builder.add(comboAccount, cc.xyw(3, 7, 2));

        builder.addLabel("Untuk Dinas", cc.xy(1, 9));
        builder.add(fieldPayer, cc.xyw(3, 9, 2));

        builder.addLabel("Nomor Rekening", cc.xy(1, 11));
        builder.add(fieldBankAccount, cc.xyw(3, 11, 2));

        builder.addLabel("NPWP", cc.xy(1, 13));
        builder.add(fieldTaxNumber, cc.xyw(3, 13, 2));

        builder.addLabel("Sudah Terima Dari", cc.xy(1, 15));
        builder.add(fieldReceivedFrom, cc.xyw(3, 15, 2));

        builder.addLabel("Sumber Dana", cc.xy(1, 17));
        builder.add(comboBudgetType, cc.xyw(3, 17, 2));

        builder.addLabel("Uang Sejumlah", cc.xy(1, 19));
        builder.add(fieldAmount, cc.xyw(3, 19, 1));
        builder.add(btPickStandardPrice, cc.xy(4, 19));

        builder.addLabel("Untuk Keperluan", cc.xy(1, 21));
        builder.add(scPane, cc.xywh(3, 21, 2, 3));

        tabPane.addTab("Input Hal. 1", builder.getPanel());
        tabPane.addTab("Input Hal. 2", createMainPanelPage2());
        tabPane.addTab("Cetak", createPrintPanel());

        return tabPane;
    }

    private Component createMainPanelPage2() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,pref,10px",
                "pref,3px,pref,3px,pref,3px,pref,3px,pref,3px,20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tempat Pembayaran", cc.xy(1, 1));
        builder.add(fieldReceivedPlace, cc.xyw(3, 1, 2));

        builder.addLabel("Pengguna Anggaran", cc.xy(1, 3));
        builder.add(comboHeadEmployee, cc.xyw(3, 3, 2));

        builder.addLabel("PPTK", cc.xy(1, 5));
        builder.add(comboPPTK, cc.xyw(3, 5, 2));

        builder.addLabel("Bendahara Pengeluaran", cc.xy(1, 7));
        builder.add(comboClerk, cc.xyw(3, 7, 2));

        builder.addLabel("Penerima", cc.xy(1, 9));
        builder.add(comboPaidTo, cc.xyw(3, 9, 2));

        return builder.getPanel();
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,5px, fill:default:grow,5px,fill:default:grow", "pref,4px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1, 3));

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

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldAmount = new JXFormattedTextField();
        fieldAmount.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));
    }

    private void construct() {

        loadComboExpedition();
        loadComboEmployee();
        loadComboAccount();
        loadComboProgram();
        loadComboActivity();
        loadBudgetType();
        contructNumberField();

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

        chequeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chequeList.setAutoCreateRowSorter(true);
        chequeList.addListSelectionListener(this);

        fieldNotes.setLineWrap(true);


        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        comboExpedition.setAction(new ExpeditionChequeAction());

        btPickStandardPrice.addActionListener(this);

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
        chequeList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void loadComboAccount() {
        try {
            ArrayList<Account> accounts = mLogic.getAccount(mainframe.getSession());

            if (!accounts.isEmpty()) {

                accounts.add(0, new Account());
                comboAccount.setModel(new ListComboBoxModel<Account>(accounts));
                AutoCompleteDecorator.decorate(comboAccount);
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboProgram() {
        try {
            ArrayList<Program> programs = mLogic.getProgram(mainframe.getSession());

            if (!programs.isEmpty()) {

                programs.add(0, new Program());
                comboProgram.setModel(new ListComboBoxModel<Program>(programs));
                AutoCompleteDecorator.decorate(comboProgram);
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboActivity() {
        try {
            ArrayList<Activity> activitys = mLogic.getActivity(mainframe.getSession());

            activitys.add(0, new Activity());
            comboActivity.setModel(new ListComboBoxModel<Activity>(activitys));
            AutoCompleteDecorator.decorate(comboActivity);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadBudgetType() {
        comboBudgetType.removeAllItems();

        ArrayList<String> budgetTypes = new ArrayList<String>();

        budgetTypes.add(0, " ");
        budgetTypes.add("APBD");
        budgetTypes.add("APBD Perubahan");

        comboBudgetType.setModel(new ListComboBoxModel<String>(budgetTypes));
        AutoCompleteDecorator.decorate(comboBudgetType);
    }

    private void loadComboEmployee() {
        try {
            ArrayList<Employee> commanderEmployee = mLogic.getCommanderEmployee(mainframe.getSession());
            ArrayList<Employee> assignedEmployee = mLogic.getAssignedEmployee(mainframe.getSession());

            if (!commanderEmployee.isEmpty()) {
                for (Employee e : commanderEmployee) {
                    e.setStyled(false);
                }
                for (Employee e : assignedEmployee) {
                    e.setStyled(false);
                }

                commanderEmployee.add(0, new Employee());
                assignedEmployee.add(0, new Employee());
                comboHeadEmployee.setModel(new ListComboBoxModel<Employee>(commanderEmployee));
                comboPPTK.setModel(new ListComboBoxModel<Employee>(assignedEmployee));
                comboClerk.setModel(new ListComboBoxModel<Employee>(assignedEmployee));
                comboPaidTo.setModel(new ListComboBoxModel<Employee>(assignedEmployee));

                AutoCompleteDecorator.decorate(comboHeadEmployee, new EmployeeConverter());
                AutoCompleteDecorator.decorate(comboPPTK, new EmployeeConverter());
                AutoCompleteDecorator.decorate(comboClerk, new EmployeeConverter());
                AutoCompleteDecorator.decorate(comboPaidTo, new EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboExpedition() {
        try {
            ArrayList<Expedition> expeditions = logic.getExpedition(mainframe.getSession());

            if (!expeditions.isEmpty()) {
                for (Expedition e : expeditions) {
                    e = logic.getCompleteExpedition(mainframe.getSession(), e);
                    e.setStyled(false);
                }
            }

            expeditions.add(0, new Expedition());
            comboExpedition.setModel(new ListComboBoxModel<Expedition>(expeditions));
            AutoCompleteDecorator.decorate(comboExpedition);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void setFormState() {

        comboExpedition.setEnabled(iseditable);
        comboAccount.setEnabled(false);
        comboProgram.setEnabled(false);
        comboActivity.setEnabled(false);
        fieldPayer.setEnabled(iseditable);
        fieldBankAccount.setEnabled(iseditable);
        fieldTaxNumber.setEnabled(iseditable);
        fieldReceivedFrom.setEnabled(iseditable);
        fieldNotes.setEnabled(iseditable);
        fieldAmount.setEnabled(false);
        btPickStandardPrice.setEnabled(iseditable);
        fieldReceivedPlace.setEnabled(iseditable);
        comboHeadEmployee.setEnabled(iseditable);
        comboPPTK.setEnabled(iseditable);
        comboClerk.setEnabled(iseditable);
        comboPaidTo.setEnabled(iseditable);
        comboBudgetType.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        chequeList.setEnabled(!iseditable);

    }

    private void clearForm() {

        if (comboExpedition.getItemCount() > 0) {
            comboExpedition.setSelectedIndex(0);
        }

        if (comboAccount.getItemCount() > 0) {
            comboAccount.setSelectedIndex(0);
        }

        if (comboProgram.getItemCount() > 0) {
            comboProgram.setSelectedIndex(0);
        }

        if (comboActivity.getItemCount() > 0) {
            comboActivity.setSelectedIndex(0);
        }

        fieldPayer.setText("");
        fieldBankAccount.setText("");
        fieldTaxNumber.setText("");
        fieldReceivedFrom.setText("");
        fieldNotes.setText("");
        fieldAmount.setValue(BigDecimal.ZERO);
        amount = null;
        fieldReceivedPlace.setText("");

        if (comboHeadEmployee.getItemCount() > 0) {
            comboHeadEmployee.setSelectedIndex(0);
        }

        if (comboPPTK.getItemCount() > 0) {
            comboPPTK.setSelectedIndex(0);
        }

        if (comboClerk.getItemCount() > 0) {
            comboClerk.setSelectedIndex(0);
        }

        if (comboPaidTo.getItemCount() > 0) {
            comboPaidTo.setSelectedIndex(0);
        }

        comboBudgetType.setSelectedIndex(0);

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

    private void setFormValues() {
        if (selectedCheque != null) {
            try {
                selectedCheque = logic.getCompleteExpeditionCheque(mainframe.getSession(), selectedCheque);

                if (selectedCheque.getExpedition() == null) {
                    comboExpedition.setSelectedIndex(0);
                } else {
                    comboExpedition.setSelectedItem(selectedCheque.getExpedition());
                }

                if (selectedCheque.getHeadEmployee() == null) {
                    comboHeadEmployee.setSelectedIndex(0);
                } else {
                    comboHeadEmployee.setSelectedItem(selectedCheque.getHeadEmployee());
                }

                if (selectedCheque.getPptk() == null) {
                    comboPPTK.setSelectedIndex(0);
                } else {
                    comboPPTK.setSelectedItem(selectedCheque.getPptk());
                }

                if (selectedCheque.getClerk() == null) {
                    comboClerk.setSelectedIndex(0);
                } else {
                    comboClerk.setSelectedItem(selectedCheque.getClerk());
                }

                if (selectedCheque.getPaidTo() == null) {
                    comboPaidTo.setSelectedIndex(0);
                } else {
                    comboPaidTo.setSelectedItem(selectedCheque.getPaidTo());
                }

                comboBudgetType.setSelectedIndex(selectedCheque.getBudgetType());

                fieldPayer.setText(selectedCheque.getPayer());
                fieldBankAccount.setText(selectedCheque.getBankAccount());
                fieldTaxNumber.setText(selectedCheque.getTaxNumber());
                fieldReceivedFrom.setText(selectedCheque.getReceivedFrom());
                fieldNotes.setText(selectedCheque.getNotes());
                fieldReceivedPlace.setText(selectedCheque.getReceivedPlace());

                if (selectedCheque.getAmount() == null) {
                    fieldAmount.setValue(BigDecimal.ZERO);
                } else {
                    amount = selectedCheque.getAmount();
                    fieldAmount.setValue(selectedCheque.getAmount().getPrice());
                }

                reloadPrintPanel();

                setButtonState("Save");

            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            setButtonState("");
            jasperPrint = null;
            panelViewer.reload(jasperPrint);
        }
    }

    private ExpeditionCheque getExpeditionCheque() throws MotekarException {
        errorString = new StringBuilder();

        Expedition expedition = null;
        Object letterObj = comboExpedition.getSelectedItem();

        if (letterObj instanceof Expedition) {
            expedition = (Expedition) letterObj;
        }

        if (expedition == null) {
            errorString.append("<br>- SPPD</br>");
        }

        String payer = fieldPayer.getText();

        if (payer.equals("")) {
            errorString.append("<br>- Untuk Dinas</br>");
        }

        String bankaccount = fieldBankAccount.getText();
        String taxNumber = fieldTaxNumber.getText();

        if (taxNumber.equals("")) {
            errorString.append("<br>- NPWP</br>");
        }

        String receivedFrom = fieldReceivedFrom.getText();

        if (receivedFrom.equals("")) {
            errorString.append("<br>- Sudah Terima Dari</br>");
        }

        if (amount == null) {
            errorString.append("<br>- Uang Sejumlah</br>");
        }

        String notes = fieldNotes.getText();

        if (notes.equals("")) {
            errorString.append("<br>- Untuk Keperluan</br>");
        }

        String receivedPlace = fieldReceivedPlace.getText();

        if (receivedPlace.equals("")) {
            errorString.append("<br>- Tempat Pembayaran</br>");
        }

        Employee headEmployee = null;
        Object headObj = comboHeadEmployee.getSelectedItem();

        if (headObj instanceof Employee) {
            headEmployee = (Employee) headObj;
        }

        if (headEmployee == null) {
            errorString.append("<br>- Pengguna Anggaran</br>");
        }


        Employee pptk = null;
        Object pptkObj = comboPPTK.getSelectedItem();

        if (pptkObj instanceof Employee) {
            pptk = (Employee) pptkObj;
        }

        if (pptk == null) {
            errorString.append("<br>- PPTK</br>");
        }

        Employee clerk = null;
        Object clerkObj = comboClerk.getSelectedItem();

        if (clerkObj instanceof Employee) {
            clerk = (Employee) clerkObj;
        }

        if (clerk == null) {
            errorString.append("<br>- Bendahara Pengeluaran</br>");
        }


        Employee paidTo = null;
        Object paidToObj = comboPaidTo.getSelectedItem();

        if (paidToObj instanceof Employee) {
            paidTo = (Employee) paidToObj;
        }

        if (paidTo == null) {
            errorString.append("<br>- Penerima</br>");
        }

        Integer bugdetType = Integer.valueOf(comboBudgetType.getSelectedIndex());

        if (bugdetType.equals(Integer.valueOf(0))) {
            errorString.append("<br>- Sumber Dana</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ExpeditionCheque cheque = new ExpeditionCheque();
        cheque.setExpedition(expedition);
        cheque.setPayer(payer);
        cheque.setBankAccount(bankaccount);
        cheque.setTaxNumber(taxNumber);
        cheque.setReceivedFrom(receivedFrom);
        cheque.setAmount(amount);
        cheque.setNotes(notes);
        cheque.setReceivedPlace(receivedPlace);
        cheque.setHeadEmployee(headEmployee);
        cheque.setPptk(pptk);
        cheque.setClerk(clerk);
        cheque.setPaidTo(paidTo);
        cheque.setBudgetType(bugdetType);

        return cheque;

    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Kwitansi Pembayaran");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        statusLabel.setText("Ubah Kwitansi Pembayaran");
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
                logic.deleteExpeditionCheque(mainframe.getSession(), selectedCheque.getIndex());
                chequeList.removeSelected(selectedCheque);
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
            ExpeditionCheque newCheque = getExpeditionCheque();

            if (isnew) {
                newCheque = logic.insertExpeditionCheque(mainframe.getSession(), newCheque);
                isnew = false;
                iseditable = false;
                newCheque.setStyled(true);
                chequeList.addExpeditionCheque(newCheque);
                selectedCheque = newCheque;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newCheque = logic.updateExpeditionCheque(mainframe.getSession(), selectedCheque, newCheque);
                isedit = false;
                iseditable = false;
                newCheque.setStyled(true);
                chequeList.updateExpeditionCheque(newCheque);
                selectedCheque = newCheque;
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
        if (chequeList.getElementCount() > 0) {
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
        } else if (obj == btPickStandardPrice) {

            BigDecimal budget = calculateBudget();

            Activity activity = null;

            Object objExp = comboExpedition.getSelectedItem();
            if (objExp instanceof Expedition) {
                Expedition exp = (Expedition) objExp;
                if (exp != null) {
                    activity = exp.getActivity();
                }
            }

            StandardPricePickDlg dlg = new StandardPricePickDlg(mainframe, mainframe.getSession(),
                    mainframe.getConnection(), budget,activity, ListSelectionModel.SINGLE_SELECTION);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                ArrayList<StandardPrice> prices = dlg.getSelectedStandardPrice();
                if (!prices.isEmpty()) {
                    amount = prices.get(0);
                    fieldAmount.setValue(amount.getPrice());
                }
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedCheque = chequeList.getSelectedExpeditionCheque();
            setFormValues();
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
        printWorker = new LoadPrintPanel(jasperPrint, selectedCheque, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();

        comp.add(comboExpedition.getEditor().getEditorComponent());
        comp.add(fieldPayer);
        comp.add(fieldBankAccount);
        comp.add(fieldTaxNumber);
        comp.add(fieldReceivedFrom);
        comp.add(comboBudgetType.getEditor().getEditorComponent());
        comp.add(fieldAmount);
        comp.add(fieldNotes);
        comp.add(fieldReceivedPlace);
        comp.add(comboHeadEmployee.getEditor().getEditorComponent());
        comp.add(comboPPTK.getEditor().getEditorComponent());
        comp.add(comboClerk.getEditor().getEditorComponent());
        comp.add(comboPaidTo.getEditor().getEditorComponent());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private BigDecimal calculateBudget() {
        BigDecimal budget = BigDecimal.ZERO;

        Integer years = Integer.valueOf(0);

        Activity activity = null;

        Object obj = comboExpedition.getSelectedItem();
        if (obj instanceof Expedition) {
            Expedition exp = (Expedition) obj;
            if (exp != null) {
                activity = exp.getActivity();
                if (exp.getStartDate() != null) {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(exp.getStartDate());
                    years = cal.get(Calendar.YEAR);
                }
            } else {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                years = cal.get(Calendar.YEAR);
            }
        }

        Integer budgetType = Integer.valueOf(comboBudgetType.getSelectedIndex());

        if (activity != null) {
            try {
                budget = mLogic.getBudgetAmount(mainframe.getSession(), activity, years, budgetType);

                BigDecimal used = BigDecimal.ZERO;

                if (isnew) {
                    used = logic.getBudgetUsed(mainframe.getSession(), years, budgetType);
                } else {
                    used = logic.getBudgetUsed(mainframe.getSession(),
                            selectedCheque.getIndex(), years, budgetType);
                }

                budget = budget.subtract(used);
                
                if (amount != null) {
                    budget = budget.add(amount.getPrice());
                }

            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return budget;
    }

    private class ExpeditionChequeAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == comboExpedition) {
                Object obj = comboExpedition.getSelectedItem();
                if (obj != null) {
                    if (obj instanceof Expedition) {
                        try {
                            Expedition expedition = (Expedition) obj;

                            if (!expedition.equals(new Expedition())) {
                                expedition = logic.getCompleteExpedition(mainframe.getSession(), expedition);
                                expedition.setStyled(false);

                                Program program = expedition.getProgram();
                                Activity activity = expedition.getActivity();
                                Account account = expedition.getAccount();

                                if (program == null) {
                                    comboProgram.setSelectedIndex(0);
                                } else {
                                    comboProgram.setSelectedItem(program);
                                }

                                if (activity == null) {
                                    comboActivity.setSelectedIndex(0);
                                } else {
                                    comboActivity.setSelectedItem(activity);
                                }

                                if (account == null) {
                                    comboAccount.setSelectedIndex(0);
                                } else {
                                    comboAccount.setSelectedItem(account);
                                }
                            }
                        } catch (SQLException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                    }
                }
            }
        }
    }

    private class ExpeditionChequeList extends JXList {

        private Icon CHEQUE_ICON = Mainframe.getResizableIconFromSource("resource/payment.png", new Dimension(40, 40));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(40, 40));

        public ExpeditionChequeList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadExpeditionCheque((DefaultListModel) getModel());
            progressListener = new ExpeditionChequesProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public ExpeditionCheque getSelectedExpeditionCheque() {
            ExpeditionCheque cheque = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof ExpeditionCheque) {
                cheque = (ExpeditionCheque) obj;
            }
            return cheque;
        }

        public void addExpeditionCheque(ExpeditionCheque cheque) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(cheque);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateExpeditionCheque(ExpeditionCheque cheque) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(cheque, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<ExpeditionCheque> cheques) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(cheques);
            filter();
        }

        public void removeSelected(ExpeditionCheque cheque) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(cheque);
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

                    ExpeditionCheque cheque = null;

                    if (o instanceof ExpeditionCheque) {
                        cheque = (ExpeditionCheque) o;
                    }

                    if (cheque != null) {
                        return ExpeditionChequeList.this.CHEQUE_ICON;
                    }

                    return ExpeditionChequeList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadExpeditionCheque extends SwingWorker<DefaultListModel, ExpeditionCheque> {

        private DefaultListModel model;
        private Exception exception;

        public LoadExpeditionCheque(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ExpeditionCheque> chunks) {
            mainframe.stopInActiveListener();
            for (ExpeditionCheque cheque : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kwitansi Pembayaran Kepada " + cheque.toString());
                model.addElement(cheque);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<ExpeditionCheque> cheques = logic.getExpeditionCheque(mainframe.getSession());

                double progress = 0.0;
                if (!cheques.isEmpty()) {
                    for (int i = 0; i < cheques.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / cheques.size();
                        setProgress((int) progress);
                        publish(cheques.get(i));
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
                JXErrorPane.showDialog(ExpeditionChequePanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ExpeditionChequesProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ExpeditionChequesProgressListener() {
        }

        ExpeditionChequesProgressListener(JProgressBar progressBar) {
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

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private ExpeditionCheque cheque = null;

        public LoadPrintPanel(JasperPrint jasperPrint, ExpeditionCheque cheque, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.cheque = cheque;
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

        private JasperReport loadReportFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "ExpeditionCheque.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel() throws InterruptedException {
            DefaultTableModel model = null;
            return model;
        }

        private Map constructParameter() throws InterruptedException {
            Map param = new HashMap();

            StringBuilder stateName = new StringBuilder();

            if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                stateName.append(ArchieveProperties.KABUPATEN.toUpperCase()).append(" ").
                        append(properties.getState().toUpperCase());
            }

            File file = properties.getLogo();

            if (!file.exists()) {
                file = new File("./images/logo_daerah.jpg");
            }

            ImageIcon ico = new ImageIcon(file.getPath());

            param.put("statename", stateName.toString());
            param.put("governname", properties.getCompany().toUpperCase());
            param.put("governaddress", properties.getAddress().toUpperCase());
            param.put("capital", properties.getCapital().toUpperCase());

            param.put("logo", ico.getImage());

            param.put("dinasname", properties.getCompany().toUpperCase());
            param.put("dinascode", "");

            Expedition expedition = cheque.getExpedition();

            if (expedition.getProgram() == null) {
                param.put("programname", "");
                param.put("programcode", "");
            } else {
                param.put("programname", expedition.getProgram().getProgramName());
                param.put("programcode", "(" + expedition.getProgram().getProgramCode() + ")");
            }

            StringBuilder accountcode = new StringBuilder();

            if (expedition.getActivity() == null) {
                param.put("activityname", "");
                param.put("activitycode", "");
            } else {
                param.put("activityname", expedition.getActivity().getActivityName());
                param.put("activitycode", "(" + expedition.getActivity().getActivityCode() + ")");
                accountcode.append(expedition.getActivity().getActivityCode());
            }

            if (expedition.getAccount() == null) {
                param.put("accountcode", "");
            } else {
                accountcode.append(".").append(expedition.getAccount().getAccountCode());
                param.put("accountcode", "(" + accountcode.toString() + ")");
            }

            param.put("bankaccount", cheque.getBankAccount());
            param.put("amount", cheque.getAmount().getPrice());
            param.put("notes", cheque.getNotes());

            param.put("spellablenumber", SpellableNumber.format(cheque.getAmount().getPrice()));

            param.put("receivedfrom", cheque.getReceivedFrom().toUpperCase());
            param.put("receivedplace", cheque.getReceivedPlace().toUpperCase());
            param.put("taxnumber", cheque.getTaxNumber());

            param.put("pptkname", cheque.getPptk().getName());
            param.put("pptknip", cheque.getPptk().getNip());

            param.put("clerkname", cheque.getClerk().getName());
            param.put("clerknip", cheque.getClerk().getNip());

            param.put("paidtoname", cheque.getPaidTo().getName());
            if (!cheque.getPaidTo().getNip().equals("")) {
                param.put("paidtonip", "NIP. "+cheque.getPaidTo().getNip());
            } else {
                param.put("paidtonip", "");
            }
            

            param.put("headname", cheque.getHeadEmployee().getName());
            param.put("headnip", cheque.getHeadEmployee().getNip());

            setProgress(90);
            Thread.sleep(100L);

            return param;
        }

        private synchronized JasperPrint createJasperPrint(JasperReport jasperReport, DefaultTableModel model, Map param) throws InterruptedException {
            JasperPrint jrPrint = null;
            try {
                if (model != null) {
                    JRTableModelDataSource ds = new JRTableModelDataSource(model);
                    jrPrint = JasperFillManager.fillReport(jasperReport, param, ds);
                } else {
                    jrPrint = JasperFillManager.fillReport(jasperReport, param);
                }

            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
            return jrPrint;
        }

        @Override
        protected void process(List<Void> chunks) {
            mainframe.stopInActiveListener();
            viewerPanel.reload(jasperPrint);
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {
                JasperReport jrReport = loadReportFile();
                DefaultTableModel model = constructModel();
                Map param = constructParameter();
                jasperPrint = createJasperPrint(jrReport, model, param);

                publish();

                setProgress(100);

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
                JXErrorPane.showDialog(ExpeditionChequePanel.this, info);
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
