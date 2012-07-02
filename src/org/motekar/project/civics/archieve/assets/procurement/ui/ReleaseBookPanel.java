package org.motekar.project.civics.archieve.assets.procurement.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.TableColumnExt;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.assets.procurement.objects.ReleaseBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.ReleaseBookJasper;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReleaseBookPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    //
    private JXDatePicker fieldSearchDate = new JXDatePicker();
    private JXDatePicker fieldSearchDate2 = new JXDatePicker();
    private JCheckBox checkFilter = new JCheckBox("Filter");
    //
    private JXComboBox comboUnit = new JXComboBox();
    private JXDatePicker fieldReleaseDate = new JXDatePicker();
    private JXTextField fieldSortingNumber = new JXTextField();
    private JXDatePicker fieldBillDate = new JXDatePicker();
    private JXTextField fieldBillNumber = new JXTextField();
    private JXTextField fieldItems = new JXTextField();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JSpinField fieldAmount = new JSpinField();
    private JXFormattedTextField fieldPrice;
    private JXFormattedTextField fieldTotalPrice;
    private JXTextField fieldAllotment = new JXTextField();
    private JXDatePicker fieldDelegateDate = new JXDatePicker();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkReleaseDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private ReleaseBookTable table = new ReleaseBookTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadReleaseBook worker;
    private ProgressListener progressListener;
    private ReleaseBook selectedReleaseBook = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportReleaseBook importWorker;
    private ItemsCategory selectedItemsCategory = null;
    private ItemsCategory parentCategory = null;
    //
    private Signer commanderSigner = null;
    private Signer employeeSigner = null;
    private Approval approval;
    //
    private JFileChooser xlsChooser;
    //
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();

    public ReleaseBookPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ProcurementBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("");
        } else {
            Unit unit = mainframe.getUnit();
            comboUnit.setEnabled(false);
            comboUnit.setSelectedItem(unit);
            String modifier = generateUnitModifier(unit);
            table.loadData(modifier);
        }
    }

    private void construct() {

        constructFileChooser();

        loadParentCategory();
        loadComboUnit();
        contructNumberField();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPanel.addListener(this);
        btSavePanelUp.addListener(this);
        btSavePanelBottom.addListener(this);
        btPrintPanel.addListener(this);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(createInputPanel(), "1");

        fieldReleaseDate.setFormats("dd/MM/yyyy");
        fieldBillDate.setFormats("dd/MM/yyyy");
        fieldDelegateDate.setFormats("dd/MM/yyyy");
        fieldSearchDate.setFormats("dd/MM/yyyy");
        fieldSearchDate2.setFormats("dd/MM/yyyy");

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ReleaseBook> releaseBooks = table.getSelectedReleaseBooks();
                    if (!releaseBooks.isEmpty()) {
                        if (releaseBooks.size() == 1) {
                            selectedReleaseBook = releaseBooks.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedReleaseBook = null;
                            btPanel.setButtonState(ManipulationButtonPanel.UNEDIT);
                        }
                    } else {
                        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                    }
                }
            }
        });

        fieldPrice.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        fieldAmount.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        fieldSearchDate.setEnabled(checkFilter.isSelected());
        fieldSearchDate2.setEnabled(checkFilter.isSelected());

        checkFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldSearchDate.setEnabled(checkFilter.isSelected());
                fieldSearchDate2.setEnabled(checkFilter.isSelected());
                table.loadData(generateModifier());
            }
        });

        fieldSearchDate.addPropertyChangeListener("date", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadData(generateModifier());
            }
        });

        fieldSearchDate2.addPropertyChangeListener("date", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadData(generateModifier());
            }
        });

        btChooseItems.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg dlg = new ItemsCategoryChooserDlg(mainframe, mainframe.getSession(), mainframe.getConnection(),
                        null, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.getCategoryName());
                }
            }
        });

        linkReleaseDatePlace.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                approvalPlaceAndDateAction();
            }
        });

        linkCommander.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseCommanderSigner();
            }
        });

        linkCommanderNIP.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseCommanderSigner();
            }
        });

        linkEmployee.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseEmployeeSigner();
            }
        });

        linkEmployeeNIP.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseEmployeeSigner();
            }
        });

        fillDefaultSignerLink();

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
    }

    public String getCompleteItemsName() {
        String itemsName = null;

        if (!iLookUp.isEmpty()) {
            int size = iLookUp.size();
            StringBuilder str = new StringBuilder();
            for (int i = 1; i < size; i++) {
                if (i == size - 1) {
                    str.append(iLookUp.get(i).getCategoryName());
                } else {
                    str.append(iLookUp.get(i).getCategoryName()).append(">");
                }
            }

            if (str.length() > 0) {
                itemsName = str.toString();
            }
        }

        return itemsName;
    }

    private String getLastItemsName() {
        String itemsName = "";

        if (!iLookUp.isEmpty()) {
            itemsName = iLookUp.get(iLookUp.size() - 1).getCategoryName();
        }

        return itemsName;
    }

    private void constructFileChooser() {
        if (xlsChooser == null) {
            xlsChooser = new JFileChooser();
        }

        xlsChooser.setDialogTitle("Simpan File ke Excel");
        xlsChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);

        xlsChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return fileName.toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Excel(*.xls)";
            }
        });

    }

    private void approvalPlaceAndDateAction() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

            approval = getApprovalFromLabel();

            ApprovalDlg dlg = new ApprovalDlg(mainframe, approval);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                approval = dlg.getApproval();
                linkReleaseDatePlace.setText(approval.getPlace() + "," + sdf.format(approval.getDate()));
            }
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void chooseCommanderSigner() {
        SignerDlg dlg = new SignerDlg(mainframe, mainframe.getConnection(), mainframe.getSession(), commanderSigner, Signer.TYPE_COMMANDER);
        dlg.showDialog();
        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            commanderSigner = dlg.getSigner();
            linkCommander.setText(commanderSigner.getSignerName());
            linkCommanderNIP.setText("NIP. " + commanderSigner.getSignerNIP());
        }
    }

    private void chooseEmployeeSigner() {
        SignerDlg dlg = new SignerDlg(mainframe, mainframe.getConnection(), mainframe.getSession(), employeeSigner, Signer.TYPE_SIGNER);
        dlg.showDialog();
        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            employeeSigner = dlg.getSigner();
            linkEmployee.setText(employeeSigner.getSignerName());
            linkEmployeeNIP.setText("NIP. " + employeeSigner.getSignerNIP());
        }
    }

    private Approval getApprovalFromLabel() throws ParseException {
        Approval approvals = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

        ArrayList<String> str = new ArrayList<String>();

        if (!linkReleaseDatePlace.getText().contains("........")) {

            StringTokenizer token = new StringTokenizer(linkReleaseDatePlace.getText(), ",");
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

    private void fillDefaultSignerLink() {
        linkReleaseDatePlace.setText(".......... , ................................");
        linkCommander.setText("(..........................................)");
        linkCommanderNIP.setText("NIP. ..........................................");
        linkEmployee.setText("(..........................................)");
        linkEmployeeNIP.setText("NIP. ..........................................");
    }

    private String generateModifier() {
        StringBuilder query = new StringBuilder();

        if (checkFilter.isSelected()) {
            Date date = fieldSearchDate.getDate();
            Date date2 = fieldSearchDate2.getDate();

            if (date != null && date2 != null) {
                query.append(" where releasedate between '").
                        append(new java.sql.Date(date.getTime())).append("' and '").
                        append(new java.sql.Date(date2.getTime())).append("'");
            }
        }

        return query.toString();
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (checkFilter.isSelected()) {
            Date date = fieldSearchDate.getDate();
            Date date2 = fieldSearchDate2.getDate();

            if (date != null && date2 != null) {
                if (unit != null) {
                    query.append(" where warehouse = ").append(unit.getIndex()).
                            append(" and receivedate between '").
                            append(new java.sql.Date(date.getTime())).append("' and '").
                            append(new java.sql.Date(date2.getTime())).append("'");
                } else {
                    query.append(" where receivedate between '").
                            append(new java.sql.Date(date.getTime())).append("' and '").
                            append(new java.sql.Date(date2.getTime())).append("'");
                }
            }
        }

        return query.toString();
    }

    private BigDecimal calculateTotalPrice() {

        Integer amount = fieldAmount.getValue();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            if (value != null) {
                price = BigDecimal.valueOf(value.intValue());
            }
        } else if (objPrice instanceof BigDecimal) {
            if (objPrice != null) {
                price = (BigDecimal) objPrice;
            }
        }
        BigDecimal total = BigDecimal.valueOf(0);
        total = total.add(price);
        total = total.multiply(BigDecimal.valueOf(amount.doubleValue()));
        return total;
    }

    private void loadParentCategory() {
        try {
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession());
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
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
            final EventList<Unit> units = GlazedLists.eventList(lw.get());

            units.add(0, new Unit());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUnit, units);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldTotalPrice = new JXFormattedTextField();
        fieldTotalPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldTotalPrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldTotalPrice.setEditable(false);
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Pilih Bidang / Sub Bidang Barang");

        btChooseItems.setActionRichTooltip(addTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btChooseItems);

        return buttonStrip;
    }

    private Component createInputPanel() {

        FormLayout lm = new FormLayout(
                "300px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,300px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "50px,fill:default:grow,5px,"
                + "20px,pref,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JXLabel requiredLabel = new JXLabel("Field tidak boleh kosong");
        requiredLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        requiredLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);
        requiredLabel.setFont(new Font(requiredLabel.getFont().getName(), Font.BOLD, requiredLabel.getFont().getSize()));

        JXPanel panelRequired = new JXPanel(new FlowLayout(FlowLayout.LEFT));
        panelRequired.add(requiredLabel, null);

        btSavePanelUp.setBorder(new EmptyBorder(5, 0, 5, 5));
        btSavePanelBottom.setBorder(new EmptyBorder(5, 0, 5, 5));

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 9));
        builder.addSeparator("", cc.xyw(1, 2, 9));

        builder.add(panelRequired, cc.xyw(1, 4, 9));

        JXLabel itemLabel = new JXLabel("Nama Barang");
        itemLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);


        builder.addLabel("Gudang / Unit", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 5));

        builder.addLabel("Tanggal", cc.xy(2, 8));
        builder.add(fieldReleaseDate, cc.xyw(4, 8, 3));

        builder.addLabel("Nomor Urut", cc.xy(2, 10));
        builder.add(fieldSortingNumber, cc.xyw(4, 10, 3));

        builder.addSeparator("Surat Bon", cc.xyw(2, 12, 7));

        builder.addLabel("Nomor", cc.xy(2, 14));
        builder.add(fieldBillNumber, cc.xyw(4, 14, 5));

        builder.addLabel("Tanggal", cc.xy(2, 16));
        builder.add(fieldBillDate, cc.xyw(4, 16, 3));

        builder.add(itemLabel, cc.xy(2, 18));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 18, 1));
        builder.add(fieldItems, cc.xyw(6, 18, 3));

        builder.addLabel("Banyaknya", cc.xy(2, 20));
        builder.add(fieldAmount, cc.xyw(4, 20, 3));

        builder.addLabel("Harga Satuan", cc.xy(2, 22));
        builder.add(fieldPrice, cc.xyw(4, 22, 3));

        builder.addLabel("Jumlah Harga", cc.xy(2, 24));
        builder.add(fieldTotalPrice, cc.xyw(4, 24, 3));

        builder.addLabel("Untuk", cc.xy(2, 26));
        builder.add(fieldAllotment, cc.xyw(4, 26, 5));

        builder.addLabel("Tanggal Penyerahan", cc.xy(2, 28));
        builder.add(fieldDelegateDate, cc.xyw(4, 28, 3));

        builder.addLabel("Keterangan", cc.xy(2, 30));
        builder.add(scPane, cc.xywh(4, 30, 5, 2));

        builder.addSeparator("", cc.xyw(1, 33, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 34, 9));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input Pengeluaran Barang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
//        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private Component createSignerPanel() {
        FormLayout lm = new FormLayout(
                "5px,pref,50px,fill:default:grow,50px,pref,5px",
                "pref,2px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(createReleasePlacePanel(), cc.xy(6, 1));

        builder.add(createCommanderSignerPanel(), cc.xy(2, 3));
        builder.add(createEmployeeSignerPanel(), cc.xy(6, 3));

        return builder.getPanel();
    }

    private Component createReleasePlacePanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkReleaseDatePlace, cc.xy(1, 1));

        return builder.getPanel();
    }

    private Component createCommanderSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("ATASAN LANGSUNG", cc.xy(1, 1));

        builder.add(linkCommander, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkCommanderNIP, cc.xy(1, 9));

        return builder.getPanel();
    }

    private Component createEmployeeSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("PENYIMPAN BARANG", cc.xy(1, 1));

        builder.add(linkEmployee, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkEmployeeNIP, cc.xy(1, 9));

        return builder.getPanel();
    }

    private JXPanel createTablePanel() {

        btPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BorderLayout());
        panelButton.add(btPanel, BorderLayout.WEST);
        panelButton.add(btPrintPanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(panelButton, BorderLayout.CENTER);

        JXTitledPanel titledPanel = new JXTitledPanel("Data View");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panel, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        collapasepanel.add(createSignerPanel(), BorderLayout.SOUTH);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,5px,pref,5px,fill:default:grow,550px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(checkFilter, cc.xy(1, 1));
        builder.add(fieldSearchDate, cc.xy(3, 1));
        builder.addLabel("s.d.", cc.xy(5, 1));
        builder.add(fieldSearchDate2, cc.xy(7, 1));

        return builder.getPanel();
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

    private void clearForm() {
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldAmount.setValue(Integer.valueOf(0));
        fieldTotalPrice.setValue(BigDecimal.ZERO);
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            comboUnit.getEditor().setItem(null);
        }
        fieldReleaseDate.setDate(null);
        fieldSortingNumber.setText("");
        fieldBillDate.setDate(null);
        fieldBillNumber.setText("");
        fieldDelegateDate.setDate(null);
        fieldDescription.setText("");
        fieldAllotment.setText("");
        fieldItems.setText("");
    }

    private void setFormValues() {
        if (selectedReleaseBook != null) {
            try {
                if (selectedReleaseBook.getWarehouse() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedReleaseBook.getWarehouse());
                }

                fieldReleaseDate.setDate(selectedReleaseBook.getReleaseDate());
                fieldSortingNumber.setText(selectedReleaseBook.getSortingNumber());

                fieldBillDate.setDate(selectedReleaseBook.getBillDate());
                fieldBillNumber.setText(selectedReleaseBook.getBillNumber());

                selectedItemsCategory = new ItemsCategory();
                selectedItemsCategory.setCategoryCode(selectedReleaseBook.getItemCode());
                selectedItemsCategory.setCategoryName(selectedReleaseBook.getItemName());
                if (selectedReleaseBook.getItemCode().startsWith("01")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_LAND);
                } else if (selectedReleaseBook.getItemCode().startsWith("02")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_MACHINE);
                } else if (selectedReleaseBook.getItemCode().startsWith("03")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_BUILDING);
                } else if (selectedReleaseBook.getItemCode().startsWith("04")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_NETWORK);
                } else if (selectedReleaseBook.getItemCode().startsWith("05")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                } else if (selectedReleaseBook.getItemCode().startsWith("06")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                }

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), selectedItemsCategory);

                fieldItems.setText(getCompleteItemsName());

                fieldAmount.setValue(selectedReleaseBook.getAmount());
                fieldPrice.setValue(selectedReleaseBook.getPrice());

                BigDecimal totalPrice = BigDecimal.ZERO;
                totalPrice = totalPrice.add(selectedReleaseBook.getPrice());
                totalPrice = totalPrice.multiply(BigDecimal.valueOf(selectedReleaseBook.getAmount().doubleValue()));

                fieldTotalPrice.setValue(totalPrice);

                fieldAllotment.setText(selectedReleaseBook.getAllotment());

                fieldDelegateDate.setDate(selectedReleaseBook.getDelegateDate());


                fieldDescription.setText(selectedReleaseBook.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ReleaseBook getReleaseBook() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Unit warehouse = null;
        Object objwhs = comboUnit.getSelectedItem();
        if (objwhs instanceof Unit) {
            warehouse = (Unit) objwhs;
        }

        Date releaseDate = fieldReleaseDate.getDate();
        String sortingNumber = fieldSortingNumber.getText();

        Date billDate = fieldBillDate.getDate();
        String billNumber = fieldBillNumber.getText();

        String itemName = getLastItemsName();
        Integer amount = fieldAmount.getValue();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        Date delegateDate = fieldDelegateDate.getDate();
        String allotment = fieldAllotment.getText();


        String description = fieldDescription.getText();


        if (itemName.equals("")) {
            errorString.append("<br>- Jenis Barang yang dibeli</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ReleaseBook releaseBook = new ReleaseBook();
        releaseBook.setReleaseDate(releaseDate);
        releaseBook.setSortingNumber(sortingNumber);
        releaseBook.setItemCode(selectedItemsCategory.getCategoryCode());
        releaseBook.setItemName(itemName);
        releaseBook.setAmount(amount);
        releaseBook.setPrice(price);
        releaseBook.setAllotment(allotment);
        releaseBook.setDelegateDate(delegateDate);
        releaseBook.setDescription(description);
        releaseBook.setWarehouse(warehouse);
        releaseBook.setBillDate(billDate);
        releaseBook.setBillNumber(billNumber);


        return releaseBook;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboUnit.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboUnit.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onDelete() {
        Object[] options = {"Ya", "Tidak"};
        int choise = JOptionPane.showOptionDialog(this,
                " Anda yakin menghapus data ini ? (Y/T)",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[1]);
        if (choise == JOptionPane.YES_OPTION) {
            try {
                ArrayList<ReleaseBook> selectedReleaseBooks = table.getSelectedReleaseBooks();
                if (!selectedReleaseBooks.isEmpty()) {
                    logic.deleteReleaseBook(mainframe.getSession(), selectedReleaseBooks);
                    table.removeReleaseBook(selectedReleaseBooks);
                    clearForm();
                    btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                }
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    @Override
    public void onSave() {
        try {
            ReleaseBook newReleaseBook = getReleaseBook();
            if (btPanel.isNewstate()) {
                newReleaseBook = logic.insertReleaseBook(mainframe.getSession(), newReleaseBook);
                table.addReleaseBook(newReleaseBook);
                selectedReleaseBook = newReleaseBook;
                selectedReleaseBook.setItemName(getLastItemsName());
                selectedReleaseBook.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newReleaseBook = logic.updateReleaseBook(mainframe.getSession(), selectedReleaseBook, newReleaseBook);
                table.updateSelectedReleaseBook(newReleaseBook);
                selectedReleaseBook = newReleaseBook;
                selectedReleaseBook.setItemName(getLastItemsName());
                selectedReleaseBook.setFullItemName(getCompleteItemsName());
                table.packAll();
            }
            btPanel.setStateFalse();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
            statusLabel.setText("Ready");
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        } catch (MotekarException ex) {
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    null, "Error", ex, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        }
    }

    @Override
    public void onCancel() throws SQLException, CommonException {
        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        btPanel.setStateFalse();
    }

    public void onPrint() throws Exception, CommonException {
        try {

            StringBuilder errorString = new StringBuilder();

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }

            Date date = null;
            Date date2 = null;

            if (checkFilter.isSelected()) {
                date = fieldSearchDate.getDate();
                date2 = fieldSearchDate2.getDate();
            }

            ReleaseBookJasper report = new ReleaseBookJasper("Buku Pengeluaran Barang", table.getReleaseBooks(), date, date2,
                    approval, employeeSigner, commanderSigner);

            report.showReport();

        } catch (MotekarException ex) {
            Exceptions.printStackTrace(ex);
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void onPrintExcel() throws Exception, CommonException {
        try {

            StringBuilder errorString = new StringBuilder();

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }

            Date date = null;
            Date date2 = null;

            if (checkFilter.isSelected()) {
                date = fieldSearchDate.getDate();
                date2 = fieldSearchDate2.getDate();
            }

            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";


                ReleaseBookJasper report = new ReleaseBookJasper("Buku Pengeluaran Barang", table.getReleaseBooks(), date, date2,
                        approval, employeeSigner, commanderSigner);

                report.exportToExcel(fileName);
            }
        } catch (MotekarException ex) {
            Exceptions.printStackTrace(ex);
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void onPrintPDF() throws Exception, CommonException {
        //
    }

    public void onPrintGraph() throws Exception, CommonException {
    }

    private class ReleaseBookTable extends JXTable {

        private ReleaseBookTableModel model;

        public ReleaseBookTable() {
            model = new ReleaseBookTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Surat Bon</b></center>");
            group.add(colModel.getColumn(4));
            group.add(colModel.getColumn(5));


            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        private class CustomColumnGroup extends ColumnGroup {

            public CustomColumnGroup(TableCellRenderer renderer, Object headerValue) {
                super(renderer, headerValue);
            }

            public CustomColumnGroup(Object headerValue) {
                super(headerValue);
            }

            @Override
            public Dimension getSize(JTable table) {
                Dimension size = new Dimension(0, 40);

                // Add the width for all visible TableColumns      
                if (columns != null) {
                    for (TableColumn column : columns) {
                        if (column instanceof TableColumnExt) {
                            TableColumnExt columnExt = (TableColumnExt) column;
                            if (columnExt.isVisible()) {
                                size.width += column.getWidth();
                            }
                        } else {
                            size.width += column.getWidth();
                        }
                    }
                }

                // Add the width for all ColumnGroups        
                if (groups != null) {
                    for (ColumnGroup group : groups) {
                        size.width += group.getSize(table).width;
                    }
                }
                return size;
            }
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadReleaseBook(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ReleaseBook getSelectedReleaseBook() {
            ArrayList<ReleaseBook> selectedReleaseBooks = getSelectedReleaseBooks();
            return selectedReleaseBooks.get(0);
        }

        public ArrayList<ReleaseBook> getReleaseBooks() {
            return model.getReleaseBooks();
        }

        public ArrayList<ReleaseBook> getSelectedReleaseBooks() {

            ArrayList<ReleaseBook> releaseBooks = new ArrayList<ReleaseBook>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ReleaseBook releaseBook = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 6);
                        if (obj instanceof ReleaseBook) {
                            releaseBook = (ReleaseBook) obj;
                            releaseBooks.add(releaseBook);
                        }
                    }
                }
            }

            return releaseBooks;
        }

        public void updateSelectedReleaseBook(ReleaseBook releaseBook) {
            model.updateRow(getSelectedReleaseBook(), releaseBook);
        }

        public void removeReleaseBook(ArrayList<ReleaseBook> releaseBooks) {
            if (!releaseBooks.isEmpty()) {
                for (ReleaseBook releaseBook : releaseBooks) {
                    model.remove(releaseBook);
                }
            }

        }

        public void addReleaseBook(ArrayList<ReleaseBook> releaseBooks) {
            if (!releaseBooks.isEmpty()) {
                for (ReleaseBook releaseBook : releaseBooks) {
                    model.add(releaseBook);
                }
            }
        }

        public void addReleaseBook(ReleaseBook releaseBook) {
            model.add(releaseBook);
        }

        public void insertEmptyReleaseBook() {
            addReleaseBook(new ReleaseBook());
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Boolean.class)) {
                return new DefaultTableRenderer(new CheckBoxProvider());
            } else if (columnClass.equals(BigDecimal.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.RIGHT);
            } else if (columnClass.equals(Integer.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(0);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
                amountDisplayFormat.setGroupingUsed(false);

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(JLabel.class)) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ReleaseBookTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 13;
        private static final String[] COLUMNS = {"", "<html><b>No</b>",
            "<html><b>Tanggal</b>", "<html><b>Nomor Urut</b>",
            "<html><b>Nomor</b>", "<html><b>Tanggal</b>",
            "<html><center><b>Nama Barang</b></center>",
            "<html><center><b>Banyaknya</b></center>",
            "<html><center><b>Harga<br>Satuan</br></b></center>",
            "<html><center><b>Jumlah<br>Harga</br></b></center>",
            "<html><b>Untuk</b>", "<html><b>Tanggal Penyerahan</b>",
            "<html><b>Keterangan</b>"};
        private ArrayList<ReleaseBook> releaseBooks = new ArrayList<ReleaseBook>();

        public ReleaseBookTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return true;
            }
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else if (columnIndex == 8 || columnIndex == 9) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 7) {
                return Integer.class;
            } else if (columnIndex == 2 || columnIndex == 5 || columnIndex == 11) {
                return Date.class;
            } else if (columnIndex == 3 || columnIndex == 4) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ReleaseBook> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            releaseBooks.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ReleaseBook releaseBook) {
            insertRow(getRowCount(), releaseBook);
        }

        public void insertRow(int row, ReleaseBook releaseBook) {
            releaseBooks.add(row, releaseBook);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ReleaseBook oldReleaseBook, ReleaseBook newReleaseBook) {
            int index = releaseBooks.indexOf(oldReleaseBook);
            releaseBooks.set(index, newReleaseBook);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ReleaseBook releaseBook) {
            int row = releaseBooks.indexOf(releaseBook);
            releaseBooks.remove(releaseBook);
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
                releaseBooks.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                releaseBooks.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            releaseBooks.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (releaseBooks.get(i) == null) {
                    releaseBooks.set(i, new ReleaseBook());
                }
            }
        }

        public ArrayList<ReleaseBook> getReleaseBooks() {
            return releaseBooks;
        }

        @Override
        public int getRowCount() {
            return releaseBooks.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ReleaseBook getReleaseBook(int row) {
            if (!releaseBooks.isEmpty()) {
                return releaseBooks.get(row);
            }
            return new ReleaseBook();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ReleaseBook releaseBook = getReleaseBook(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        releaseBook.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    if (aValue instanceof Date) {
                        releaseBook.setReleaseDate((Date) aValue);
                    }
                    break;
                case 3:
                    releaseBook.setSortingNumber((String) aValue);
                    break;
                case 4:
                    releaseBook.setBillNumber((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof Date) {
                        releaseBook.setBillDate((Date) aValue);
                    }
                    break;
                case 6:
                    if (aValue instanceof ReleaseBook) {
                        releaseBook = (ReleaseBook) aValue;
                    }
                    break;
                case 7:
                    if (aValue instanceof Integer) {
                        releaseBook.setAmount((Integer) aValue);
                    }
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        releaseBook.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    break;
                case 10:
                    releaseBook.setAllotment((String) aValue);
                    break;
                case 11:
                    if (aValue instanceof Date) {
                        releaseBook.setDelegateDate((Date) aValue);
                    }
                    break;
                case 12:
                    releaseBook.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ReleaseBook releaseBook = getReleaseBook(row);
            switch (column) {
                case 0:
                    toBeDisplayed = releaseBook.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = releaseBook.getReleaseDate();
                    break;
                case 3:
                    toBeDisplayed = releaseBook.getSortingNumber();
                    break;
                case 4:
                    toBeDisplayed = releaseBook.getBillNumber();
                    break;
                case 5:
                    toBeDisplayed = releaseBook.getBillDate();
                    break;
                case 6:
                    toBeDisplayed = releaseBook;
                    break;
                case 7:
                    toBeDisplayed = releaseBook.getAmount();
                    break;
                case 8:
                    toBeDisplayed = releaseBook.getPrice();
                    break;
                case 9:
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    if (releaseBook.getPrice() != null) {
                        totalPrice = totalPrice.add(releaseBook.getPrice());
                    }
                    if (releaseBook.getAmount() != null) {
                        totalPrice = totalPrice.multiply(BigDecimal.valueOf(releaseBook.getAmount().doubleValue()));
                    }
                    toBeDisplayed = totalPrice;
                    break;
                case 10:
                    toBeDisplayed = releaseBook.getAllotment();
                    break;
                case 11:
                    toBeDisplayed = releaseBook.getDelegateDate();
                    break;
                case 12:
                    toBeDisplayed = releaseBook.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadReleaseBook extends SwingWorker<ReleaseBookTableModel, ReleaseBook> {

        private ReleaseBookTableModel model;
        private Exception exception;
        private String modifier;

        public LoadReleaseBook(String modifier) {
            this.model = (ReleaseBookTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ReleaseBook> chunks) {
            mainframe.stopInActiveListener();
            for (ReleaseBook releaseBook : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Pengeluaran Barang " + releaseBook.getItemName());
                model.add(releaseBook);
            }
        }

        @Override
        protected ReleaseBookTableModel doInBackground() throws Exception {
            try {
                ArrayList<ReleaseBook> releaseBooks = logic.getReleaseBook(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!releaseBooks.isEmpty()) {
                    for (int i = 0; i < releaseBooks.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / releaseBooks.size();

                        ReleaseBook releaseBook = releaseBooks.get(i);

                        if (releaseBook.getWarehouse() != null) {
                            releaseBook.setWarehouse(mLogic.getUnitByIndex(mainframe.getSession(), releaseBook.getWarehouse()));
                        }

                        setProgress((int) progress);
                        publish(releaseBook);
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
                JXErrorPane.showDialog(ReleaseBookPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ImportReleaseBook extends SwingWorker<ReleaseBookTableModel, ReleaseBook> {

        private ReleaseBookTableModel model;
        private Exception exception;
        private List<ReleaseBook> data;

        public ImportReleaseBook(List<ReleaseBook> data) {
            this.model = (ReleaseBookTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ReleaseBook> chunks) {
            mainframe.stopInActiveListener();
            for (ReleaseBook releaseBook : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload Pengeluaran Barang " + releaseBook.getItemName());
                model.add(releaseBook);
            }
        }

        @Override
        protected ReleaseBookTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ReleaseBook releaseBook = data.get(i);

                        synchronized (releaseBook) {
                            releaseBook = logic.insertReleaseBook(mainframe.getSession(), releaseBook);
                        }


                        setProgress((int) progress);
                        publish(releaseBook);
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
                Exceptions.printStackTrace(e);
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(ReleaseBookPanel.this, info);
            }

            table.packAll();
            
            if (table.getRowCount() > 0) {
                btPrintPanel.setButtonState(PrintButtonPanel.ENABLEALL);
            } else {
                btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
            }

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
