package org.motekar.project.civics.archieve.expedition.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.swing.table.AbstractTableModel;
import net.sf.jasperreports.engine.JasperPrint;
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
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionJournal;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionResult;
import org.motekar.project.civics.archieve.expedition.reports.ExpeditionJournalJasper;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
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
public class ExpeditionJournalPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe;
    private ExpeditionBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private JXTextField fieldDocNumber = new JXTextField();
    private JXComboBox comboLetter = new JXComboBox();
    private JXTextArea fieldPurpose = new JXTextArea();
    private JXTextField fieldRepPlace = new JXTextField();
    private JXDatePicker fieldRepDate = new JXDatePicker();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private ExpeditionResultTable resultTable = new ExpeditionResultTable();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private JCommandButton btInsResult = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelResult = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private LoadExpeditionJournal worker;
    private ExpeditionJournalProgressListener progressListener;
    private ExpeditionJournalList journalList = new ExpeditionJournalList();
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private ExpeditionJournal selectedJournal = null;
    private StringBuilder errorString = new StringBuilder();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JProgressBar viewerBar = new JProgressBar();
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;
    /**
     *
     */
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    private ArchieveProperties properties;

    public ExpeditionJournalPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.properties = mainframe.getProperties();
        logic = new ExpeditionBusinessLogic(mainframe.getConnection());
        construct();
        journalList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Laporan Pertanggungjawaban");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(journalList), BorderLayout.CENTER);
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
                + "Fasilitas Laporan Pertanggung Jawaban untuk pengisian data-data Laporan Perjalanan Dinas yang ada di suatu dinas\n\n"
                + "Tambah Laporan Pertanggungjawaban\n"
                + "Untuk menambah Laporan Pertanggung Jawaban klik tombol paling kiri "
                + "kemudian isi data Laporan Perjalanan Dinas baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Laporan Pertanggungjawaban\n"
                + "Untuk merubah Laporan Pertanggung Jawaban Perjalanan Dinas klik tombol kedua dari kiri "
                + "kemudian ubah data Laporan Perjalanan Dinas, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Laporan Pertanggungjawaban\n"
                + "Untuk menghapus Laporan Pertanggung Jawaban klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Laporan Perjalanan Dinas "
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
        task.setTitle("Manipulasi Laporan Pertanggungjawaban");
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
                "pref,10px,fill:default:grow,5px,pref,20px",
                "pref,5px,pref,5px,fill:default,"
                + "fill:default:grow,5px,5px,pref,5px,pref,5px,"
                + "pref,fill:default:grow,fill:default:grow,10px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        fieldRepDate.setFormats("dd/MM/yyyy");

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(resultTable);

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11}});

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(fieldPurpose);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nomor Dokumen", cc.xy(1, 1));
        builder.add(fieldDocNumber, cc.xyw(3, 1, 3));

        builder.addLabel("Dasar Dokumen", cc.xy(1, 3));
        builder.add(comboLetter, cc.xyw(3, 3, 3));

        builder.addLabel("Maksud dan Tujuan", cc.xy(1, 5));
        builder.add(scrollPane, cc.xywh(3, 5, 3, 3));

        builder.addLabel("Tempat Pelaporan", cc.xy(1, 9));
        builder.add(fieldRepPlace, cc.xyw(3, 9, 3));

        builder.addLabel("Tanggal Pelaporan", cc.xy(1, 11));
        builder.add(fieldRepDate, cc.xyw(3, 11, 3));

        builder.addLabel("Hasil", cc.xy(1, 13));
        builder.add(scPane, cc.xywh(3, 13, 2, 3));
        builder.add(createStrip2(1.0, 1.0), cc.xy(5, 13));

        tabbedPane.addTab("Input Data", builder.getPanel());
        tabbedPane.addTab("Cetak", createPrintPanel());

        return tabbedPane;
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
        addTooltip.setTitle("Tambah Laporan Perjalanan Dinas");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Laporan Perjalanan Dinas");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Laporan Perjalanan Dinas");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Laporan Perjalanan Dinas");

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
        addTooltip.setTitle("Tambah Hasil");

        btInsResult.setActionRichTooltip(addTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Hasil");

        btDelResult.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip(StripOrientation.VERTICAL);
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsResult);
        buttonStrip.add(btDelResult);


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

        loadComboLetter();

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
                journalList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                journalList.loadData();
            }
        });

        fieldPurpose.setWrapStyleWord(true);
        fieldPurpose.setLineWrap(true);

        journalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        journalList.setAutoCreateRowSorter(true);
        journalList.addListSelectionListener(this);


        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(resultTable, ComponentState.DEFAULT);

        resultTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        resultTable.setShowGrid(false, false);

        comboLetter.setEditable(true);

        comboLetter.setAction(new JournalAction());

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsResult.addActionListener(this);
        btDelResult.addActionListener(this);

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
        journalList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {

        fieldDocNumber.setEnabled(iseditable);
        fieldRepPlace.setEnabled(iseditable);
        fieldRepDate.setEnabled(iseditable);
        fieldPurpose.setEnabled(false);

        comboLetter.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        journalList.setEnabled(!iseditable);

        btInsResult.setEnabled(iseditable);
        btDelResult.setEnabled(iseditable);

        resultTable.setEditable(iseditable);
    }

    private void clearForm() {

        fieldDocNumber.setText("");
        fieldRepPlace.setText("");
        fieldRepDate.setDate(null);
        fieldPurpose.setText("");

        comboLetter.setSelectedIndex(0);

        resultTable.clear();

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

    private void setFormValues() {
        if (selectedJournal != null) {
            try {
                selectedJournal = logic.getCompleteExpeditionJournal(mainframe.getSession(), selectedJournal);

                fieldDocNumber.setText(selectedJournal.getReportNumber());
                fieldRepPlace.setText(selectedJournal.getReportPlace());
                fieldRepDate.setDate(selectedJournal.getReportDate());

                AssignmentLetter letter = selectedJournal.getLetter();

                comboLetter.setSelectedItem(letter);

                fieldPurpose.setText(letter.getPurpose());

                resultTable.clear();

                resultTable.addResults(selectedJournal.getResult());

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

    private void loadComboLetter() {
        comboLetter.removeAllItems();
        try {
            ArrayList<AssignmentLetter> letter = logic.getAssignmentLetterInExpedition(mainframe.getSession());

            if (!letter.isEmpty()) {
                for (AssignmentLetter al : letter) {
                    al = logic.getCompleteAssignmentLetter(mainframe.getSession(), al);
                    al.setStyled(false);
                }

                letter.add(0, new AssignmentLetter());
                comboLetter.setModel(new ListComboBoxModel<AssignmentLetter>(letter));

                AutoCompleteDecorator.decorate(comboLetter, new AssignmentLetterConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private ExpeditionJournal getExpeditionJournal() throws MotekarException {
        errorString = new StringBuilder();

        String reportNumber = fieldDocNumber.getText();

        if (reportNumber.equals("")) {
            errorString.append("<br>- Nomor Dokumen</br>");
        }

        AssignmentLetter letter = null;

        Object objExp = comboLetter.getSelectedItem();

        if (objExp instanceof AssignmentLetter) {
            letter = (AssignmentLetter) objExp;
        }

        if (letter == null) {
            errorString.append("<br>- Dasar Dokumen</br>");
        }

        String reportPlace = fieldRepPlace.getText();

        if (reportPlace.equals("")) {
            errorString.append("<br>- Tempat Pelaporan</br>");
        }

        Date reportDate = fieldRepDate.getDate();

        if (reportDate == null) {
            errorString.append("<br>- Tanggal Pelaporan</br>");
        }

        ArrayList<ExpeditionResult> results = resultTable.getResults();

        if (results.isEmpty()) {
            errorString.append("<br>- Hasil Perjalanan Dinas</br>");
        }

        ExpeditionJournal journal = new ExpeditionJournal();
        journal.setReportNumber(reportNumber);
        journal.setReportPlace(reportPlace);
        journal.setReportDate(reportDate);
        journal.setResult(results);
        journal.setLetter(letter);

        return journal;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Laporan Perjalanan Dinas");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocNumber.requestFocus();
        fieldDocNumber.selectAll();
        statusLabel.setText("Ubah Laporan Perjalanan Dinas");
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
                logic.deleteExpeditionJournal(mainframe.getSession(), selectedJournal.getJournalIndex());
                journalList.removeSelected(selectedJournal);
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
            ExpeditionJournal newJournal = getExpeditionJournal();

            if (isnew) {
                newJournal = logic.insertExpeditionJournal(mainframe.getSession(), newJournal);
                isnew = false;
                iseditable = false;
                newJournal.setStyled(true);
                journalList.addExpeditionJournal(newJournal);
                selectedJournal = newJournal;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newJournal = logic.updateExpeditionJournal(mainframe.getSession(), selectedJournal, newJournal);
                isedit = false;
                iseditable = false;
                newJournal.setStyled(true);
                journalList.updateExpeditionJournal(newJournal);
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
        if (journalList.getElementCount() > 0) {
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
        } else if (obj == btInsResult) {
            resultTable.insertEmptyResult();
        } else if (obj == btDelResult) {
            ArrayList<ExpeditionResult> result = resultTable.getSelectedResults();

            if (!result.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    resultTable.removeResult(result);
                }
            }
        } else if (obj == checkBox) {
            monthChooser.setEnabled(checkBox.isSelected());
            yearChooser.setEnabled(checkBox.isSelected());
            journalList.loadData();
        }
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldDocNumber);
        comp.add(comboLetter.getEditor().getEditorComponent());
        comp.add(fieldRepPlace);
        comp.add(fieldRepDate.getEditor());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, selectedJournal, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    private void jasperRemoveAll() {
        if (jasperPrint != null) {
            int size = jasperPrint.getPages().size();
            for (int i = 0; i < size; i++) {
                jasperPrint.removePage(i);
            }
            panelViewer.reload(jasperPrint);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedJournal = journalList.getSelectedExpeditionJournal();
            setFormValues();
        }
    }

    private class ExpeditionJournalList extends JXList {

        private Icon JOURNAL_ICON = Mainframe.getResizableIconFromSource("resource/Journal.png", new Dimension(48, 48));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(48, 48));

        public ExpeditionJournalList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadExpeditionJournal((DefaultListModel) getModel());
            progressListener = new ExpeditionJournalProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public ExpeditionJournal getSelectedExpeditionJournal() {
            ExpeditionJournal ExpeditionJournal = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof ExpeditionJournal) {
                ExpeditionJournal = (ExpeditionJournal) obj;
            }
            return ExpeditionJournal;
        }

        public void addExpeditionJournal(ExpeditionJournal journal) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(journal);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateExpeditionJournal(ExpeditionJournal journal) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(journal, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<ExpeditionJournal> journals) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(journals);
            filter();
        }

        public void removeSelected(ExpeditionJournal journal) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(journal);
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

                    ExpeditionJournal journal = null;

                    if (o instanceof ExpeditionJournal) {
                        journal = (ExpeditionJournal) o;
                    }

                    if (journal != null) {
                        return ExpeditionJournalList.this.JOURNAL_ICON;
                    }

                    return ExpeditionJournalList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadExpeditionJournal extends SwingWorker<DefaultListModel, ExpeditionJournal> {

        private DefaultListModel model;
        private Exception exception;

        public LoadExpeditionJournal(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ExpeditionJournal> chunks) {
            mainframe.stopInActiveListener();
            for (ExpeditionJournal journal : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Laporan Perjalanan Dinas Nomor " + journal.getReportNumber());
                model.addElement(journal);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<ExpeditionJournal> journals = new ArrayList<ExpeditionJournal>();

                if (checkBox.isSelected()) {
                    journals = logic.getExpeditionJournal(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    journals = logic.getExpeditionJournal(mainframe.getSession());
                }

                double progress = 0.0;
                if (!journals.isEmpty()) {
                    for (int i = 0; i < journals.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / journals.size();
                        setProgress((int) progress);
                        publish(journals.get(i));
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
                JXErrorPane.showDialog(ExpeditionJournalPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ExpeditionJournalProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ExpeditionJournalProgressListener() {
        }

        ExpeditionJournalProgressListener(JProgressBar progressBar) {
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

    private class ExpeditionResultTable extends JXTable {

        private ExpeditionResultTableModel model;

        public ExpeditionResultTable() {
            model = new ExpeditionResultTableModel();
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public ExpeditionResult getSelectedResult() {
            ExpeditionResult result = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof ExpeditionResult) {
                result = (ExpeditionResult) obj;
            }

            return result;
        }

        public ArrayList<ExpeditionResult> getResults() {
            return model.getResults();
        }

        public ArrayList<ExpeditionResult> getSelectedResults() {

            ArrayList<ExpeditionResult> results = new ArrayList<ExpeditionResult>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    ExpeditionResult result = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof ExpeditionResult) {
                        result = (ExpeditionResult) obj;
                        results.add(result);
                    }
                }
            }

            return results;
        }

        public void updateSelectedResult(ExpeditionResult result) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedResult(), result);
        }

        public void removeResult(ArrayList<ExpeditionResult> results) {
            if (!results.isEmpty()) {
                for (ExpeditionResult result : results) {
                    model.remove(result);
                }
            }

        }

        public void addResults(ArrayList<ExpeditionResult> results) {
            if (!results.isEmpty()) {
                for (ExpeditionResult result : results) {
                    model.add(result);
                }
            }
        }

        public void addResult(ExpeditionResult result) {
            model.add(result);
        }

        public void insertEmptyResult() {
            addResult(new ExpeditionResult());
        }
    }

    private static class ExpeditionResultTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 1;
        private static final String[] columnResult = {"Hasil"};
        private ArrayList<ExpeditionResult> results = new ArrayList<ExpeditionResult>();

        public ExpeditionResultTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public String getColumnName(int column) {
            return columnResult[column];
        }

        public void add(ArrayList<ExpeditionResult> results) {
            int first = results.size();
            int last = first + results.size() - 1;
            results.addAll(results);
            fireTableRowsInserted(first, last);
        }

        public void add(ExpeditionResult result) {
            if (!results.contains(result)) {
                insertRow(getRowCount(), result);
            }
        }

        public void insertRow(int row, ExpeditionResult result) {
            results.add(row, result);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, ExpeditionResult oldResult, ExpeditionResult newResult) {
            int index = results.indexOf(oldResult);
            results.set(index, newResult);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ExpeditionResult result) {
            int row = results.indexOf(result);
            results.remove(result);
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
                results.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                results.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            results.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (results.get(i) == null) {
                    results.set(i, new ExpeditionResult());
                }
            }
        }

        public ArrayList<ExpeditionResult> getResults() {
            return results;
        }

        @Override
        public int getRowCount() {
            return results.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ExpeditionResult getResult(int row) {
            if (!results.isEmpty()) {
                return results.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ExpeditionResult result = getResult(row);
            switch (column) {
                case 0:
                    if (aValue instanceof ExpeditionResult) {
                        result = (ExpeditionResult) aValue;
                    } else if (aValue instanceof String) {
                        String notes = (String) aValue;
                        result.setNotes(notes);
                    }

                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ExpeditionResult result = getResult(row);
            switch (column) {
                case 0:
                    toBeDisplayed = result;
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class AssignmentLetterConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof AssignmentLetter)) {
                return new String[0];
            }
            AssignmentLetter letter = (AssignmentLetter) item;
            return new String[]{
                        letter.toString(), letter.getDocumentNumber()
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

    private class JournalAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == comboLetter) {
                Object obj = comboLetter.getSelectedItem();
                if (obj != null) {
                    AssignmentLetter letter = null;
                    if (obj instanceof AssignmentLetter) {
                        letter = (AssignmentLetter) obj;
                    }

                    if (letter != null) {
                        fieldPurpose.setText(letter.getPurpose());
                    }
                }
            }
        }
    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private ExpeditionJournal journal = null;

        public LoadPrintPanel(JasperPrint jasperPrint, ExpeditionJournal journal, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.journal = journal;
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
        protected void process(List<Void> chunks) {
            viewerPanel.reload(jasperPrint);
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                if (journal != null) {
                    ExpeditionJournalJasper ejj = new ExpeditionJournalJasper(journal, properties);
                    jasperPrint = ejj.getJasperPrint();
                }

                setProgress(100);
                publish();

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
                JXErrorPane.showDialog(ExpeditionJournalPanel.this, info);
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
