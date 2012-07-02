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
import org.motekar.project.civics.archieve.mail.objects.SicknessMail;
import org.motekar.project.civics.archieve.mail.report.SicknessMailJasper;
import org.motekar.project.civics.archieve.mail.report.SicknessMailJasper2;
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
public class SicknessMailPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private SicknessMailList sicknessMailList = new SicknessMailList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXTextField fieldPatienceName = new JXTextField();
    private JSpinField fieldPatienceAge = new JSpinField();
    private JXTextField fieldJobs = new JXTextField();
    private JXTextArea fieldAddress = new JXTextArea();
    private JXDatePicker fieldStartDate = new JXDatePicker();
    private JXDatePicker fieldEndDate = new JXDatePicker();
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
    private SicknessMail selectedSicknessMail = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadSicknessMail worker;
    private SicknessMailProgressListener progressListener;
    //
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    //
    private Approval approval = null;
    private Employee selectedApproval = null;
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public SicknessMailPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        sicknessMailList.loadData("");
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Keterangan Sakit");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(sicknessMailList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Keterangan Sakit untuk pengisian data-data surat keterangan sakit yang ada di suatu dinas\n\n"
                + "Tambah Surat Keterangan Sakit\n"
                + "Untuk menambah Surat Keterangan Sakit klik tombol paling kiri "
                + "kemudian isi data Surat Keterangan Sakit baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Keterangan Sakit\n"
                + "Untuk merubah Surat Keterangan Sakit klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Keterangan Sakit, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Keterangan Sakit\n"
                + "Untuk menghapus Surat Keterangan Sakit klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Keterangan Sakit "
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
        task.setTitle("Tambah / Edit / Hapus Surat Keterangan Sakit");
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
                "pref");

        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApprovalDatePlace, cc.xyw(1, 1, 5));


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
                "pref,10px,fill:default:grow,center:pref,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,fill:default:grow,5px,"
                + "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 12}});

        JScrollPane addressPane = new JScrollPane();
        addressPane.setViewportView(fieldAddress);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nomor Surat", cc.xy(1, 1));
        builder.add(fieldDocumentNumber, cc.xyw(3, 1, 3));

        builder.addLabel("Nama", cc.xy(1, 3));
        builder.add(fieldPatienceName, cc.xyw(3, 3, 3));

        builder.addLabel("Umur", cc.xy(1, 5));
        builder.add(fieldPatienceAge, cc.xyw(3, 5, 3));

        builder.addLabel("Pekerjaan", cc.xy(1, 7));
        builder.add(fieldJobs, cc.xyw(3, 7, 3));

        builder.addLabel("Tempat Tinggal", cc.xy(1, 9));
        builder.add(addressPane, cc.xywh(3, 9, 3, 2));

        builder.addLabel("Tanggal Mulai", cc.xy(1, 12));
        builder.add(fieldStartDate, cc.xyw(3, 12, 3));

        builder.addLabel("Sampai Dengan", cc.xy(1, 14));
        builder.add(fieldEndDate, cc.xyw(3, 14, 3));

        builder.add(createApprovalPanel(), cc.xy(4, 16));
        builder.add(createApprovalPanel2(), cc.xy(4, 18));
        
        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(builder.getPanel());
        
        
        scPane.getVerticalScrollBar().setUnitIncrement(20);

        tabbedPane.addTab("Input Data", scPane);
        tabbedPane.addTab("Cetak", createPrintPanel());

        return tabbedPane;
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
        addTooltip.setTitle("Tambah Surat Keterangan Sakit");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Keterangan Sakit");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Keterangan Sakit");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Keterangan Sakit");

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
                sicknessMailList.loadData("");
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                sicknessMailList.loadData("");
            }
        });

        fieldAddress.setWrapStyleWord(true);
        fieldAddress.setLineWrap(true);

        fieldStartDate.setFormats("dd/MM/yyyy");
        fieldEndDate.setFormats("dd/MM/yyyy");

        sicknessMailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sicknessMailList.setAutoCreateRowSorter(true);
        sicknessMailList.addListSelectionListener(this);

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
        sicknessMailList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {

        fieldDocumentNumber.setEnabled(iseditable);
        fieldPatienceName.setEnabled(iseditable);
        fieldJobs.setEnabled(iseditable);
        fieldAddress.setEnabled(iseditable);
        fieldStartDate.setEnabled(iseditable);
        fieldEndDate.setEnabled(iseditable);
        linkApprovalDatePlace.setEnabled(iseditable);
        linkApprover.setEnabled(iseditable);
        linkApproverNIP.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        sicknessMailList.setEnabled(!iseditable);

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
        fieldJobs.setText("");
        fieldAddress.setText("");
        fieldStartDate.setDate(null);
        fieldEndDate.setDate(null);

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
            sicknessMailList.loadData("");
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
        statusLabel.setText("Tambah Surat Keterangan Sakit");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocumentNumber.requestFocus();
        statusLabel.setText("Ubah Surat Keterangan Sakit");
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
                logic.deleteSicknessMail(mainframe.getSession(), selectedSicknessMail.getIndex());
                sicknessMailList.removeSelected(selectedSicknessMail);
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
            SicknessMail newSicknessMail = getSicknessMail();

            if (isnew) {
                newSicknessMail = logic.insertSicknessMail(mainframe.getSession(), newSicknessMail);
                isnew = false;
                iseditable = false;
                sicknessMailList.addSicknessMail(newSicknessMail);
                selectedSicknessMail = newSicknessMail;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newSicknessMail = logic.updateSicknessMail(mainframe.getSession(), selectedSicknessMail, newSicknessMail);
                isedit = false;
                iseditable = false;
                sicknessMailList.updateSicknessMail(newSicknessMail);
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
        if (sicknessMailList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private SicknessMail getSicknessMail() throws MotekarException, ParseException {
        errorString = new StringBuilder();

        String documentNumber = fieldDocumentNumber.getText();
        String patienceName = fieldPatienceName.getText();

        Integer patienceAge = fieldPatienceAge.getValue();
        String address = fieldAddress.getText();
        String jobs = fieldJobs.getText();

        Date startDate = fieldStartDate.getDate();
        Date endDate = fieldEndDate.getDate();

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

        SicknessMail sicknessMail = new SicknessMail();
        sicknessMail.setDocumentNumber(documentNumber);
        sicknessMail.setPatienceName(patienceName);
        sicknessMail.setPatienceAge(patienceAge);
        sicknessMail.setJobs(jobs);
        sicknessMail.setAddress(address);
        sicknessMail.setStartDate(startDate);
        sicknessMail.setEndDate(endDate);

        sicknessMail.setApprovalPlace(approval.getPlace());
        sicknessMail.setApprovalDate(approval.getDate());

        sicknessMail.setApproval(selectedApproval);


        return sicknessMail;
    }

    private void jasperRemoveAll() {
        int size = jasperPrint.getPages().size();
        for (int i = 0; i < size; i++) {
            jasperPrint.removePage(i);
        }
        panelViewer.reload(jasperPrint);
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, selectedSicknessMail, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldDocumentNumber);
        comp.add(fieldPatienceName);
        comp.add(fieldPatienceAge);
        comp.add(fieldAddress);
        comp.add(fieldPatienceAge);
        comp.add(fieldAddress);
        comp.add(fieldJobs);
        comp.add(fieldStartDate.getEditor());
        comp.add(fieldEndDate.getEditor());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedSicknessMail != null) {
            fieldDocumentNumber.setText(selectedSicknessMail.getDocumentNumber());
            fieldPatienceName.setText(selectedSicknessMail.getPatienceName());
            fieldJobs.setText(selectedSicknessMail.getJobs());
            fieldPatienceAge.setValue(selectedSicknessMail.getPatienceAge());
            fieldAddress.setText(selectedSicknessMail.getAddress());
            fieldStartDate.setDate(selectedSicknessMail.getStartDate());
            fieldEndDate.setDate(selectedSicknessMail.getEndDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

            linkApprovalDatePlace.setText(selectedSicknessMail.getApprovalPlace() + "," + sdf.format(selectedSicknessMail.getApprovalDate()));

            selectedApproval = selectedSicknessMail.getApproval();

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
            selectedSicknessMail = sicknessMailList.getSelectedSicknessMail();
            setFormValues();
        }
    }

    private class SicknessMailList extends JXList {

        private Icon MAIL_ICON = Mainframe.getResizableIconFromSource("resource/new_mail.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public SicknessMailList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadSicknessMail((DefaultListModel) getModel(), modifier);
            progressListener = new SicknessMailProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public SicknessMail getSelectedSicknessMail() {
            SicknessMail sicknessMail = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof SicknessMail) {
                sicknessMail = (SicknessMail) obj;
            }
            return sicknessMail;
        }

        public void addSicknessMail(SicknessMail sicknessMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(sicknessMail);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateSicknessMail(SicknessMail sicknessMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(sicknessMail, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<SicknessMail> sicknessMails) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(sicknessMails);
            filter();
        }

        public void removeSelected(SicknessMail sicknessMail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(sicknessMail);
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

                    SicknessMail sicknessMail = null;

                    if (o instanceof SicknessMail) {
                        sicknessMail = (SicknessMail) o;
                    }

                    if (sicknessMail != null) {
                        return SicknessMailList.this.MAIL_ICON;
                    }

                    return SicknessMailList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadSicknessMail extends SwingWorker<DefaultListModel, SicknessMail> {

        private DefaultListModel model;
        private Exception exception;
        private String modifier = "";

        public LoadSicknessMail(DefaultListModel model, String modifier) {
            this.model = model;
            this.modifier = modifier;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<SicknessMail> chunks) {
            mainframe.stopInActiveListener();
            for (SicknessMail sicknessMail : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Keterangan Sakit Nomor " + sicknessMail.getDocumentNumber());
                model.addElement(sicknessMail);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<SicknessMail> sicknessMails = new ArrayList<SicknessMail>();

                if (checkBox.isSelected()) {
                    sicknessMails = logic.getSicknessMail(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear(), modifier);
                } else {
                    sicknessMails = logic.getSicknessMail(mainframe.getSession(), modifier);
                }

                double progress = 0.0;
                if (!sicknessMails.isEmpty()) {
                    for (int i = 0; i < sicknessMails.size() && !isCancelled(); i++) {

                        SicknessMail mail = sicknessMails.get(i);

                        if (mail.getApproval() != null) {
                            mail.setApproval(mLogic.getEmployeeByIndex(mainframe.getSession(), mail.getApproval().getIndex()));
                        }


                        progress = 100 * (i + 1) / sicknessMails.size();
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
                JXErrorPane.showDialog(SicknessMailPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class SicknessMailProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private SicknessMailProgressListener() {
        }

        SicknessMailProgressListener(JProgressBar progressBar) {
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
        private SicknessMail sicknessMail = null;

        public LoadPrintPanel(JasperPrint jasperPrint, SicknessMail sicknessMail, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.sicknessMail = sicknessMail;
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
                    SicknessMailJasper2 mailJasper = new SicknessMailJasper2(sicknessMail, mainframe.getProfileAccount(),unit);
                    jasperPrint = mailJasper.getJasperPrint();
                } else {
                    SicknessMailJasper mailJasper = new SicknessMailJasper(sicknessMail, mainframe.getProfileAccount());
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
                JXErrorPane.showDialog(SicknessMailPanel.this, info);
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
