package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
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
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.mail.objects.DoctorMail;
import org.motekar.project.civics.archieve.mail.report.DoctorMailJasper;
import org.motekar.project.civics.archieve.mail.report.DoctorMailJasper2;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.master.ui.EmployeePickDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
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
public class DoctorMailPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private DoctorMailList doctorMailList = new DoctorMailList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXTextField fieldPatienceName = new JXTextField();
    private JXTextField fieldBirthPlace = new JXTextField();
    private JXDatePicker fieldBirthDate = new JXDatePicker();
    private JXTextField fieldJobs = new JXTextField();
    private JXTextArea fieldAddress = new JXTextArea();
    private JXTextField fieldRequested = new JXTextField();
    private JXDatePicker fieldMailDate = new JXDatePicker();
    private JXTextField fieldMailNumber = new JXTextField();
    private JXTextField fieldChecked = new JXTextField();
    private JXTextField fieldTerm = new JXTextField();
    private JSpinField fieldHeight = new JSpinField();
    private JSpinField fieldWeight = new JSpinField();
    private JSpinField fieldBloodPreasure = new JSpinField();
    private JSpinField fieldBloodPreasure2 = new JSpinField();
    private JXDatePicker fieldExpiredDate = new JXDatePicker();
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkApprover = new JXHyperlink();
    private JXHyperlink linkApproverNIP = new JXHyperlink();
    private JTabbedPane tabbedPane = new JTabbedPane();
    /**
     *
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    /**
     *
     */
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private DoctorMail selectedDoctorMail = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadDoctorMail worker;
    private DoctorMailProgressListener progressListener;
    //
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    //
    private Approval approval = null;
    private Employee selectedApproval = null;
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public DoctorMailPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        doctorMailList.loadData("");
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Keterangan Dokter");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(doctorMailList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Keterangan Dokter untuk pengisian data-data surat keterangan dokter yang ada di suatu dinas\n\n"
                + "Tambah Surat Keterangan Dokter\n"
                + "Untuk menambah Surat Keterangan Dokter klik tombol paling kiri "
                + "kemudian isi data Surat Keterangan Dokter baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Keterangan Dokter\n"
                + "Untuk merubah Surat Keterangan Dokter klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Keterangan Dokter, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Keterangan Dokter\n"
                + "Untuk menghapus Surat Keterangan Dokter klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Keterangan Dokter "
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
        task.setTitle("Tambah / Edit / Hapus Surat Keterangan Dokter");
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

    private Component createApprovalPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,10px,fill:default:grow,10px",
                "pref,2px,pref");

        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApprovalDatePlace, cc.xyw(1, 1, 5));
        builder.addLabel("Dokter Penguji Tersendiri", cc.xyw(1, 3, 5));


        return builder.getPanel();
    }

    private Component createApprovalPanel2() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,30px,pref,5px,pref,2px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApprover, cc.xy(1, 3));
        builder.addSeparator("", cc.xyw(1, 5, 2));
        builder.add(linkApproverNIP, cc.xy(1, 7));

        return builder.getPanel();
    }

    protected Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,70px,5px,fill:default:grow,center:pref,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,5px,pref,5px,pref,5px,"
                + "pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 12}});

        JScrollPane addressPane = new JScrollPane();
        addressPane.setViewportView(fieldAddress);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nomor Surat", cc.xy(1, 1));
        builder.add(fieldDocumentNumber, cc.xyw(3, 1, 5));

        builder.addLabel("Nama", cc.xy(1, 3));
        builder.add(fieldPatienceName, cc.xyw(3, 3, 5));

        builder.addLabel("Tempat Lahir", cc.xy(1, 5));
        builder.add(fieldBirthPlace, cc.xyw(3, 5, 5));

        builder.addLabel("Tanggal Lahir", cc.xy(1, 7));
        builder.add(fieldBirthDate, cc.xyw(3, 7, 5));

        builder.addLabel("Pekerjaan", cc.xy(1, 9));
        builder.add(fieldJobs, cc.xyw(3, 9, 5));

        builder.addLabel("Tempat Tinggal", cc.xy(1, 11));
        builder.add(addressPane, cc.xywh(3, 11, 5, 2));

        builder.addLabel("Atas Permintaan", cc.xy(1, 14));
        builder.add(fieldRequested, cc.xyw(3, 14, 5));

        builder.addLabel("Dengan surat Tanggal", cc.xy(1, 16));
        builder.add(fieldMailDate, cc.xyw(3, 16, 5));

        builder.addLabel("Nomor Surat", cc.xy(1, 18));
        builder.add(fieldMailNumber, cc.xyw(3, 18, 5));
        
        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(builder.getPanel());
        
        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(createMainPanel2());
        
        scPane.getVerticalScrollBar().setUnitIncrement(20);
        scPane2.getVerticalScrollBar().setUnitIncrement(20);


        tabbedPane.addTab("Input Data Hal.1", scPane);
        tabbedPane.addTab("Input Data Hal.2", scPane2);
        tabbedPane.addTab("Cetak", createPrintPanel());

        return tabbedPane;
    }

    protected Component createMainPanel2() {
        FormLayout lm = new FormLayout(
                "pref,10px,70px,5px,pref,5px,70px,5px,fill:default:grow,center:pref,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,fill:default:grow,5px,"
                + "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 12}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Yang  diperiksa", cc.xy(1, 1));
        builder.add(fieldChecked, cc.xyw(3, 1, 9));

        builder.addLabel("Untuk melengkapi persyaratan", cc.xy(1, 3));
        builder.add(fieldTerm, cc.xyw(3, 3, 9));

        builder.addLabel("Tinggi Badan", cc.xy(1, 5));
        builder.add(fieldHeight, cc.xyw(3, 5, 1));
        builder.addLabel("cm", cc.xyw(5, 5, 1));

        builder.addLabel("Berat Badan", cc.xy(1, 7));
        builder.add(fieldWeight, cc.xyw(3, 7, 1));
        builder.addLabel("kg", cc.xyw(5, 7, 1));

        builder.addLabel("Tekanan Darah", cc.xy(1, 9));
        builder.add(fieldBloodPreasure, cc.xyw(3, 9, 1));
        builder.addLabel("/", cc.xy(5, 9));
        builder.add(fieldBloodPreasure2, cc.xyw(7, 9, 1));
        builder.addLabel("mmHg", cc.xyw(9, 9, 1));

        builder.addLabel("Berlaku sampai", cc.xy(1, 11));
        builder.add(fieldExpiredDate, cc.xyw(3, 11, 9));

        builder.add(createApprovalPanel(), cc.xy(10, 13));
        builder.add(createApprovalPanel2(), cc.xy(10, 15));

        return builder.getPanel();
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

    protected JPanel createSearchPanel() {
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
        addTooltip.setTitle("Tambah Surat Keterangan Dokter");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Keterangan Dokter");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Keterangan Dokter");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Keterangan Dokter");

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
                doctorMailList.loadData("");
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                doctorMailList.loadData("");
            }
        });

        fieldAddress.setWrapStyleWord(true);
        fieldAddress.setLineWrap(true);

        fieldBirthDate.setFormats("dd/MM/yyyy");
        fieldMailDate.setFormats("dd/MM/yyyy");
        fieldExpiredDate.setFormats("dd/MM/yyyy");

        doctorMailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorMailList.setAutoCreateRowSorter(true);
        doctorMailList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);


        linkApprovalDatePlace.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                approvalPlaceAndDateAction();
            }
        });

        linkApprover.addActionListener(this);
        linkApproverNIP.addActionListener(this);

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

        fillDefaultApprovalText();
    }

    private void approvalPlaceAndDateAction() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

            approval = getApprovalFromLabel();

            ApprovalDlg dlg = new ApprovalDlg(mainframe, approval);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                approval = dlg.getApproval();
                linkApprovalDatePlace.setText(approval.getPlace() + "," + sdf.format(approval.getDate()));
            }
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private Approval getApprovalFromLabel() throws ParseException {
        Approval approvals = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

        ArrayList<String> str = new ArrayList<String>();

        if (!linkApprovalDatePlace.getText().contains("........")) {

            StringTokenizer token = new StringTokenizer(linkApprovalDatePlace.getText(), ",");
            while (token.hasMoreElements()) {
                str.add(token.nextToken());
            }

            if (!str.isEmpty()) {
                approvals = new Approval();
                approvals.setPlace(str.get(0));
                approvals.setDate(sdf.parse(str.get(1)));
            }
        }


        return approvals;
    }

    public void filter() {
        doctorMailList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {

        fieldDocumentNumber.setEnabled(iseditable);
        fieldPatienceName.setEnabled(iseditable);
        fieldBirthPlace.setEnabled(iseditable);
        fieldBirthDate.setEnabled(iseditable);
        fieldJobs.setEnabled(iseditable);
        fieldAddress.setEnabled(iseditable);
        fieldRequested.setEnabled(iseditable);
        fieldMailDate.setEnabled(iseditable);
        fieldMailNumber.setEnabled(iseditable);
        fieldChecked.setEnabled(iseditable);
        fieldTerm.setEnabled(iseditable);
        fieldHeight.setEnabled(iseditable);
        fieldWeight.setEnabled(iseditable);
        fieldBloodPreasure.setEnabled(iseditable);
        fieldBloodPreasure2.setEnabled(iseditable);
        fieldExpiredDate.setEnabled(iseditable);


        linkApprovalDatePlace.setEnabled(iseditable);
        linkApprover.setEnabled(iseditable);
        linkApproverNIP.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        doctorMailList.setEnabled(!iseditable);

        checkBox.setEnabled(!iseditable);
        monthChooser.setEnabled(!iseditable && checkBox.isSelected());
        yearChooser.setEnabled(!iseditable && checkBox.isSelected());

    }

    private void fillDefaultApprovalText() {
        linkApprovalDatePlace.setText(".......... , ................................");
        linkApprover.setText("..........................................");
        linkApproverNIP.setText("NIP. ..........................................");
    }

    private void clearForm() {
        fieldDocumentNumber.setText("");
        fieldPatienceName.setText("");
        fieldBirthPlace.setText("");
        fieldBirthDate.setDate(null);
        fieldJobs.setText("");
        fieldAddress.setText("");
        fieldRequested.setText("");
        fieldMailDate.setDate(null);
        fieldMailNumber.setText("");
        fieldChecked.setText("");
        fieldTerm.setText("");
        fieldHeight.setValue(0);
        fieldWeight.setValue(0);
        fieldBloodPreasure.setValue(0);
        fieldBloodPreasure2.setValue(0);
        fieldExpiredDate.setDate(null);

        fillDefaultApprovalText();

        fieldDocumentNumber.requestFocus();
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
        } else if (obj == checkBox) {
            monthChooser.setEnabled(checkBox.isSelected());
            yearChooser.setEnabled(checkBox.isSelected());
            doctorMailList.loadData("");
        } else if (obj == linkApprover) {
            loadApprovalPanel();
        } else if (obj == linkApproverNIP) {
            loadApprovalPanel();
        }
    }

    private void loadApprovalPanel() {
        EmployeePickDlg dlg = new EmployeePickDlg(mainframe, mainframe.getSession(), mainframe.getConnection(), false);
        dlg.showDialog();

        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            selectedApproval = dlg.getSelectedEmployee();

            if (selectedApproval != null) {
                if (selectedApproval.isGorvernor()) {
                    linkApprover.setText(selectedApproval.getName());
                    linkApproverNIP.setVisible(false);
                } else {
                    linkApproverNIP.setVisible(true);
                    linkApprover.setText(selectedApproval.getName());
                    linkApproverNIP.setText("Nip. " + selectedApproval.getNip());
                }
            }
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Surat Keterangan Dokter");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocumentNumber.requestFocus();
        statusLabel.setText("Ubah Surat Keterangan Dokter");
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
                logic.deleteDoctorMail(mainframe.getSession(), selectedDoctorMail.getIndex());
                doctorMailList.removeSelected(selectedDoctorMail);
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
            DoctorMail newDoctorMail = getDoctorMail();

            if (isnew) {
                newDoctorMail = logic.insertDoctorMail(mainframe.getSession(), newDoctorMail);
                isnew = false;
                iseditable = false;
                doctorMailList.addDoctorMail(newDoctorMail);
                selectedDoctorMail = newDoctorMail;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newDoctorMail = logic.updateDoctorMail(mainframe.getSession(), selectedDoctorMail, newDoctorMail);
                isedit = false;
                iseditable = false;
                doctorMailList.updateDoctorMail(newDoctorMail);
                setFormState();
                setButtonState("Save");
            }
            statusLabel.setText("Ready");
            mainframe.setFocusTraversalPolicy(null);
            reloadPrintPanel();
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
        if (doctorMailList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private DoctorMail getDoctorMail() throws MotekarException, ParseException {
        errorString = new StringBuilder();

        String documentNumber = fieldDocumentNumber.getText();
        String patienceName = fieldPatienceName.getText();
        String birthPlace = fieldBirthPlace.getText();
        Date birthDate = fieldBirthDate.getDate();
        String address = fieldAddress.getText();
        String jobs = fieldJobs.getText();
        String requested = fieldRequested.getText();
        Date mailDate = fieldMailDate.getDate();
        String mailNumber = fieldMailNumber.getText();
        String checked = fieldChecked.getText();
        String term = fieldTerm.getText();
        Integer height = fieldHeight.getValue();
        Integer weight = fieldWeight.getValue();
        Integer bloodPreasure = fieldBloodPreasure.getValue();
        Integer bloodPreasure2 = fieldBloodPreasure2.getValue();
        Date expiredDate = fieldExpiredDate.getDate();

        if (approval == null) {
            approval = getApprovalFromLabel();
        }

        if (approval == null) {
            errorString.append("<br>- Pilih Tempat dan Tanggal dikeluarkan</br>");
        }

        if (documentNumber.equals("")) {
            errorString.append("<br>- Nomor Surat</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        DoctorMail doctorMail = new DoctorMail();
        doctorMail.setDocumentNumber(documentNumber);
        doctorMail.setPatienceName(patienceName);
        doctorMail.setBirthPlace(birthPlace);
        doctorMail.setBirthDate(birthDate);
        doctorMail.setJobs(jobs);
        doctorMail.setAddress(address);
        doctorMail.setRequested(requested);
        doctorMail.setMailDate(mailDate);
        doctorMail.setMailNumber(mailNumber);
        doctorMail.setChecked(checked);
        doctorMail.setTerm(term);
        doctorMail.setHeight(height);
        doctorMail.setWeight(weight);
        doctorMail.setBloodPreasure(bloodPreasure);
        doctorMail.setBloodPreasure2(bloodPreasure2);
        doctorMail.setExpiredDate(expiredDate);

        doctorMail.setApprovalPlace(approval.getPlace());
        doctorMail.setApprovalDate(approval.getDate());

        doctorMail.setApproval(selectedApproval);


        return doctorMail;
    }

    private void jasperRemoveAll() {
        int size = jasperPrint.getPages().size();
        for (int i = 0; i < size; i++) {
            jasperPrint.removePage(i);
        }
        panelViewer.reload(jasperPrint);
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, selectedDoctorMail, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldDocumentNumber);
        comp.add(fieldPatienceName);
        comp.add(fieldBirthPlace);
        comp.add(fieldBirthDate.getEditor());
        comp.add(fieldJobs);
        comp.add(fieldAddress);
        comp.add(fieldRequested);
        comp.add(fieldMailDate.getEditor());
        comp.add(fieldMailNumber);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedDoctorMail != null) {
            fieldDocumentNumber.setText(selectedDoctorMail.getDocumentNumber());
            fieldPatienceName.setText(selectedDoctorMail.getPatienceName());
            fieldBirthPlace.setText(selectedDoctorMail.getBirthPlace());
            fieldBirthDate.setDate(selectedDoctorMail.getBirthDate());
            fieldJobs.setText(selectedDoctorMail.getJobs());
            fieldAddress.setText(selectedDoctorMail.getAddress());
            fieldRequested.setText(selectedDoctorMail.getRequested());
            fieldMailDate.setDate(selectedDoctorMail.getMailDate());
            fieldMailNumber.setText(selectedDoctorMail.getMailNumber());
            fieldChecked.setText(selectedDoctorMail.getChecked());
            fieldTerm.setText(selectedDoctorMail.getTerm());
            fieldHeight.setValue(selectedDoctorMail.getHeight());
            fieldWeight.setValue(selectedDoctorMail.getWeight());
            fieldBloodPreasure.setValue(selectedDoctorMail.getBloodPreasure());
            fieldBloodPreasure2.setValue(selectedDoctorMail.getBloodPreasure2());
            fieldExpiredDate.setDate(selectedDoctorMail.getExpiredDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

            linkApprovalDatePlace.setText(selectedDoctorMail.getApprovalPlace() + "," + sdf.format(selectedDoctorMail.getApprovalDate()));

            selectedApproval = selectedDoctorMail.getApproval();

            if (selectedApproval != null) {
                if (selectedApproval.isGorvernor()) {
                    linkApprover.setText(selectedApproval.getName());
                    linkApproverNIP.setVisible(false);
                } else {
                    linkApproverNIP.setVisible(true);
                    linkApprover.setText(selectedApproval.getName());
                    linkApproverNIP.setText("Nip. " + selectedApproval.getNip());
                }
            }

            reloadPrintPanel();

            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
            jasperPrint = null;
            panelViewer.reload(jasperPrint);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedDoctorMail = doctorMailList.getSelectedDoctorMail();
            setFormValues();
        }
    }

    private class DoctorMailList extends JXList {

        private Icon MAIL_ICON = Mainframe.getResizableIconFromSource("resource/new_mail.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public DoctorMailList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadDoctorMail((DefaultListModel) getModel(), modifier);
            progressListener = new DoctorMailProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public DoctorMail getSelectedDoctorMail() {
            DoctorMail doctorMail = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof DoctorMail) {
                doctorMail = (DoctorMail) obj;
            }
            return doctorMail;
        }

        public void addDoctorMail(DoctorMail doctorMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(doctorMail);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateDoctorMail(DoctorMail doctorMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(doctorMail, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<DoctorMail> doctorMails) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(doctorMails);
            filter();
        }

        public void removeSelected(DoctorMail doctorMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(doctorMail);
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

                    DoctorMail doctorMail = null;

                    if (o instanceof DoctorMail) {
                        doctorMail = (DoctorMail) o;
                    }

                    if (doctorMail != null) {
                        return DoctorMailList.this.MAIL_ICON;
                    }

                    return DoctorMailList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadDoctorMail extends SwingWorker<DefaultListModel, DoctorMail> {

        private DefaultListModel model;
        private Exception exception;
        private String modifier = "";

        public LoadDoctorMail(DefaultListModel model, String modifier) {
            this.model = model;
            this.modifier = modifier;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<DoctorMail> chunks) {
            mainframe.stopInActiveListener();
            for (DoctorMail doctorMail : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Keterangan Dokter Nomor " + doctorMail.getDocumentNumber());
                model.addElement(doctorMail);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<DoctorMail> doctorMails = new ArrayList<DoctorMail>();

                if (checkBox.isSelected()) {
                    doctorMails = logic.getDoctorMail(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear(), modifier);
                } else {
                    doctorMails = logic.getDoctorMail(mainframe.getSession(), modifier);
                }

                double progress = 0.0;
                if (!doctorMails.isEmpty()) {
                    for (int i = 0; i < doctorMails.size() && !isCancelled(); i++) {

                        DoctorMail mail = doctorMails.get(i);

                        if (mail.getApproval() != null) {
                            mail.setApproval(mLogic.getEmployeeByIndex(mainframe.getSession(), mail.getApproval().getIndex()));
                        }


                        progress = 100 * (i + 1) / doctorMails.size();
                        setProgress((int) progress);
                        publish(mail);
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
                JXErrorPane.showDialog(DoctorMailPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class DoctorMailProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private DoctorMailProgressListener() {
        }

        DoctorMailProgressListener(JProgressBar progressBar) {
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

    private class LoadPrintPanel extends SwingWorker<JasperPrint, JRPrintPage> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private DoctorMail doctorMail = null;

        public LoadPrintPanel(JasperPrint jasperPrint, DoctorMail doctorMail, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.doctorMail = doctorMail;
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

                Unit unit = mainframe.getUnit();

                if (unit != null) {
                    DoctorMailJasper mailJasper = new DoctorMailJasper(doctorMail, mainframe.getProfileAccount(), mainframe.getUnit());
                    jasperPrint = mailJasper.getJasperPrint();
                } else {
                    DoctorMailJasper2 mailJasper = new DoctorMailJasper2(doctorMail, mainframe.getProfileAccount());
                    jasperPrint = mailJasper.getJasperPrint();
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
                JXErrorPane.showDialog(DoctorMailPanel.this, info);
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
