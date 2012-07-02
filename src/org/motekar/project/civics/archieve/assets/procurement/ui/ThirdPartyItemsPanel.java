package org.motekar.project.civics.archieve.assets.procurement.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.objects.ThirdPartyItems;
import org.motekar.project.civics.archieve.assets.procurement.report.ThirdPartyItemsJasper;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
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
public class ThirdPartyItemsPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    //
    private JXComboBox comboSearchUnit = new JXComboBox();
    //
    private JXComboBox comboUnit = new JXComboBox();
    private JXTextField fieldItems = new JXTextField();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldSpecificationType = new JXTextField();
    private JXTextField fieldSpecificationNumber = new JXTextField();
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextField fieldThirdPartyName = new JXTextField();
    private JXTextField fieldItemsUnit = new JXTextField();
    private RadioButtonPanel radioCondition = new RadioButtonPanel(new String[]{"B", "KB"}, 0, 2);
    private JSpinField fieldAmount = new JSpinField();
    private JXFormattedTextField fieldPrice;
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private ThirdPartyItemsTable table = new ThirdPartyItemsTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadThirdPartyItems worker;
    private ProgressListener progressListener;
    private ThirdPartyItems selectedThirdPartyItems = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportThirdPartyItems importWorker;
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

    public ThirdPartyItemsPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ProcurementBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
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

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ThirdPartyItems> thirdPartyItemss = table.getSelectedThirdPartyItemss();
                    if (!thirdPartyItemss.isEmpty()) {
                        if (thirdPartyItemss.size() == 1) {
                            selectedThirdPartyItems = thirdPartyItemss.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedThirdPartyItems = null;
                            btPanel.setButtonState(ManipulationButtonPanel.UNEDIT);
                        }
                    } else {
                        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                    }
                }
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

        linkApprovalDatePlace.addActionListener(new AbstractAction() {

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

        comboSearchUnit.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                table.loadData(generateModifier());
            }
        });

        fillDefaultSignerLink();

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
        checkLogin();
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

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            comboSearchUnit.setEnabled(true);
        } else {
            Unit unit = mainframe.getUnit();
            unit.setStyled(true);
            comboSearchUnit.setEnabled(false);
            comboSearchUnit.setSelectedItem(unit);
        }
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

    private String generateModifier() {
        StringBuilder query = new StringBuilder();

        Unit unit = null;
        Object unitObj = comboSearchUnit.getSelectedItem();
        if (unitObj instanceof Unit) {
            unit = (Unit) unitObj;
        }

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
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

    private void fillDefaultSignerLink() {
        linkApprovalDatePlace.setText(".......... , ................................");
        linkCommander.setText("(..........................................)");
        linkCommanderNIP.setText("NIP. ..........................................");
        linkEmployee.setText("(..........................................)");
        linkEmployeeNIP.setText("NIP. ..........................................");
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

            units.add(0, new Unit());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUnit, units);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support2 = AutoCompleteSupport.install(comboSearchUnit, units);
            support2.setFilterMode(TextMatcherEditor.CONTAINS);


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
                "200px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,200px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
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

        JXLabel itemLabel = new JXLabel("Kode / Nama Barang");
        itemLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);


        builder.addLabel("Lokasi", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 5));

        builder.add(itemLabel, cc.xy(2, 8));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 8, 1));
        builder.add(fieldItems, cc.xyw(6, 8, 3));

        builder.addLabel("Merk/Type", cc.xy(2, 10));
        builder.add(fieldSpecificationType, cc.xyw(4, 10, 5));

        builder.addLabel("No. Sertifikat / No. Pabrik / No. Chasis / No. Mesin", cc.xy(2, 12));
        builder.add(fieldSpecificationNumber, cc.xyw(4, 12, 5));

        builder.addLabel("Tahun Pembelian / Pengadaan", cc.xy(2, 14));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 14, 3));

        builder.addLabel("Asal-usul / Cara Perolehan", cc.xy(2, 16));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 16, 5));

        builder.addLabel("Nama Penyumbang / Pihak Ketiga", cc.xy(2, 18));
        builder.add(fieldThirdPartyName, cc.xyw(4, 18, 5));

        builder.addLabel("Satuan", cc.xy(2, 20));
        builder.add(fieldItemsUnit, cc.xyw(4, 20, 3));

        builder.addLabel("Keadaan Barang (B/KB)", cc.xy(2, 22));
        builder.add(radioCondition, cc.xyw(4, 22, 5));

        builder.addLabel("Jumlah Barang", cc.xy(2, 24));
        builder.add(fieldAmount, cc.xyw(4, 24, 3));

        builder.addLabel("Harga Barang", cc.xy(2, 26));
        builder.add(fieldPrice, cc.xyw(4, 26, 3));

        builder.addLabel("Keterangan", cc.xy(2, 28));
        builder.add(scPane, cc.xywh(4, 28, 5, 2));

        builder.addSeparator("", cc.xyw(1, 31, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 32, 9));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input Penerimaan Barang Dari Pihak Ketiga Barang");

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

        builder.add(createApprovalPlacePanel(), cc.xy(6, 1));

        builder.add(createCommanderSignerPanel(), cc.xy(2, 3));
        builder.add(createEmployeeSignerPanel(), cc.xy(6, 3));

        return builder.getPanel();
    }

    private Component createApprovalPlacePanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApprovalDatePlace, cc.xy(1, 1));

        return builder.getPanel();
    }

    private Component createCommanderSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("MENGETAHUI", cc.xy(1, 1));
        builder.addLabel("KEPALA SKPD/PENGELOLA", cc.xy(1, 3));

        builder.add(linkCommander, cc.xy(1, 7));
        builder.addSeparator("", cc.xyw(1, 9, 2));
        builder.add(linkCommanderNIP, cc.xy(1, 11));

        return builder.getPanel();
    }

    private Component createEmployeeSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("PENGURUS BARANG", cc.xy(1, 1));

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
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panel, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        collapasepanel.add(createSignerPanel(), BorderLayout.SOUTH);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,10px, pref,30px,pref,10px,pref,10px,fill:default:grow,350px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        ProfileAccount profile = mainframe.getProfileAccount();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SKPD", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.addLabel(profile.getCompany().toUpperCase(), cc.xy(5, 1));

        builder.addLabel("KAB / KOTA", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.addLabel(profile.getState().toUpperCase(), cc.xy(5, 3));

        builder.addLabel("PROPINSI", cc.xy(7, 1));
        builder.addLabel(":", cc.xy(9, 1));
        builder.addLabel(profile.getProvince().toUpperCase(), cc.xy(11, 1));

        builder.addLabel("KODE LOKASI", cc.xy(7, 3));
        builder.addLabel(":", cc.xy(9, 3));
        builder.add(comboSearchUnit, cc.xy(11, 3));

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

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldAcquisitionYear.setYear(year);
    }

    private void clearForm() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            comboUnit.getEditor().setItem(null);
        }
        fieldItems.setText("");
        fieldSpecificationType.setText("");
        fieldSpecificationNumber.setText("");
        clearYear();
        fieldAcquisitionWay.setText("");
        fieldThirdPartyName.setText("");
        fieldItemsUnit.setText("");
        radioCondition.setAllDeselected();
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldAmount.setValue(Integer.valueOf(0));
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedThirdPartyItems != null) {
            try {
                if (selectedThirdPartyItems.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedThirdPartyItems.getUnit());
                }

                selectedItemsCategory = new ItemsCategory();
                selectedItemsCategory.setCategoryCode(selectedThirdPartyItems.getItemCode());
                selectedItemsCategory.setCategoryName(selectedThirdPartyItems.getItemName());
                if (selectedThirdPartyItems.getItemCode().startsWith("01")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_LAND);
                } else if (selectedThirdPartyItems.getItemCode().startsWith("02")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_MACHINE);
                } else if (selectedThirdPartyItems.getItemCode().startsWith("03")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_BUILDING);
                } else if (selectedThirdPartyItems.getItemCode().startsWith("04")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_NETWORK);
                } else if (selectedThirdPartyItems.getItemCode().startsWith("05")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                } else if (selectedThirdPartyItems.getItemCode().startsWith("06")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                }

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), selectedItemsCategory);

                fieldItems.setText(getCompleteItemsName());

                fieldSpecificationType.setText(selectedThirdPartyItems.getSpecificationType());
                fieldSpecificationNumber.setText(selectedThirdPartyItems.getSpecificationNumber());

                fieldAcquisitionYear.setYear(selectedThirdPartyItems.getAcquisitionYear());
                fieldAcquisitionWay.setText(selectedThirdPartyItems.getAcquisitionWay());
                fieldThirdPartyName.setText(selectedThirdPartyItems.getThirdPartyName());
                fieldItemsUnit.setText(selectedThirdPartyItems.getItemsUnit());

                radioCondition.setSelectedData(selectedThirdPartyItems.getCondition());

                fieldAmount.setValue(selectedThirdPartyItems.getAmount());
                fieldPrice.setValue(selectedThirdPartyItems.getPrice());

                fieldDescription.setText(selectedThirdPartyItems.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ThirdPartyItems getThirdPartyItems() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Unit unit = null;
        Object unitObj = comboUnit.getSelectedItem();
        if (unitObj instanceof Unit) {
            unit = (Unit) unitObj;
        }

        String itemName = getLastItemsName();

        String specificationType = fieldSpecificationType.getText();
        String specificationNumber = fieldSpecificationNumber.getText();
        Integer acquisitionYear = fieldAcquisitionYear.getYear();
        String acquisitionWay = fieldAcquisitionWay.getText();
        String thirdPartyName = fieldThirdPartyName.getText();
        String itemsUnit = fieldItemsUnit.getText();

        String condition = radioCondition.getSelectedData();

        Integer amount = fieldAmount.getValue();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        String description = fieldDescription.getText();


        if (itemName.equals("")) {
            errorString.append("<br>- Nama Barang</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ThirdPartyItems thirdPartyItems = new ThirdPartyItems();
        thirdPartyItems.setItemCode(selectedItemsCategory.getCategoryCode());
        thirdPartyItems.setItemName(itemName);
        thirdPartyItems.setSpecificationType(specificationType);
        thirdPartyItems.setSpecificationNumber(specificationNumber);
        thirdPartyItems.setAcquisitionYear(acquisitionYear);
        thirdPartyItems.setAcquisitionWay(acquisitionWay);
        thirdPartyItems.setThirdPartyName(thirdPartyName);
        thirdPartyItems.setItemsUnit(itemsUnit);
        thirdPartyItems.setCondition(condition);
        thirdPartyItems.setAmount(amount);
        thirdPartyItems.setPrice(price);
        thirdPartyItems.setDescription(description);
        thirdPartyItems.setUnit(unit);


        return thirdPartyItems;
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
                ArrayList<ThirdPartyItems> selectedThirdPartyItemss = table.getSelectedThirdPartyItemss();
                if (!selectedThirdPartyItemss.isEmpty()) {
                    logic.deleteThirdPartyItems(mainframe.getSession(), selectedThirdPartyItemss);
                    table.removeThirdPartyItems(selectedThirdPartyItemss);
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
            ThirdPartyItems newThirdPartyItems = getThirdPartyItems();
            if (btPanel.isNewstate()) {
                newThirdPartyItems = logic.insertThirdPartyItems(mainframe.getSession(), newThirdPartyItems);
                table.addThirdPartyItems(newThirdPartyItems);
                selectedThirdPartyItems = newThirdPartyItems;
                selectedThirdPartyItems.setItemName(getLastItemsName());
                selectedThirdPartyItems.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newThirdPartyItems = logic.updateThirdPartyItems(mainframe.getSession(), selectedThirdPartyItems, newThirdPartyItems);
                table.updateSelectedThirdPartyItems(newThirdPartyItems);
                selectedThirdPartyItems.setItemName(getLastItemsName());
                selectedThirdPartyItems.setFullItemName(getCompleteItemsName());
                selectedThirdPartyItems = newThirdPartyItems;
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

            Unit unit = null;
            Object unitObj = comboSearchUnit.getSelectedItem();
            if (unitObj instanceof Unit) {
                unit = (Unit) unitObj;
            }

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (unit == null) {
                errorString.append("<br>- Pilih Kode Lokasi</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }


            ThirdPartyItemsJasper report = new ThirdPartyItemsJasper("Daftar Penerimaan Barang Dari Pihak Ketiga", table.getThirdPartyItemss(), mainframe.getProfileAccount(),
                    unit.getUnitCode(), approval, employeeSigner, commanderSigner);

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

            Unit unit = null;
            Object unitObj = comboSearchUnit.getSelectedItem();
            if (unitObj instanceof Unit) {
                unit = (Unit) unitObj;
            }

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (unit == null) {
                errorString.append("<br>- Pilih Kode Lokasi</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }

            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";


                ThirdPartyItemsJasper report = new ThirdPartyItemsJasper("Daftar Penerimaan Barang Dari Pihak Ketiga", table.getThirdPartyItemss(), mainframe.getProfileAccount(),
                        unit.getUnitCode(), approval, employeeSigner, commanderSigner);

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

    private class ThirdPartyItemsTable extends JXTable {

        private ThirdPartyItemsTableModel model;

        public ThirdPartyItemsTable() {
            model = new ThirdPartyItemsTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(1));
            group.add(colModel.getColumn(2));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><center><b>Spesifikasi Barang</b></center>");
            group2.add(colModel.getColumn(3));
            group2.add(colModel.getColumn(4));

            CustomColumnGroup group3 = new CustomColumnGroup("<html><center><b>Jumlah</b></center>");
            group3.add(colModel.getColumn(11));
            group3.add(colModel.getColumn(12));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group2);
            header.addColumnGroup(group3);
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

            worker = new LoadThirdPartyItems(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ThirdPartyItems getSelectedThirdPartyItems() {
            ArrayList<ThirdPartyItems> selectedThirdPartyItemss = getSelectedThirdPartyItemss();
            return selectedThirdPartyItemss.get(0);
        }

        public ArrayList<ThirdPartyItems> getThirdPartyItemss() {
            return model.getThirdPartyItemss();
        }

        public ArrayList<ThirdPartyItems> getSelectedThirdPartyItemss() {

            ArrayList<ThirdPartyItems> thirdPartyItemss = new ArrayList<ThirdPartyItems>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ThirdPartyItems thirdPartyItems = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof ThirdPartyItems) {
                            thirdPartyItems = (ThirdPartyItems) obj;
                            thirdPartyItemss.add(thirdPartyItems);
                        }
                    }
                }
            }

            return thirdPartyItemss;
        }

        public void updateSelectedThirdPartyItems(ThirdPartyItems thirdPartyItems) {
            model.updateRow(getSelectedThirdPartyItems(), thirdPartyItems);
        }

        public void removeThirdPartyItems(ArrayList<ThirdPartyItems> thirdPartyItemss) {
            if (!thirdPartyItemss.isEmpty()) {
                for (ThirdPartyItems thirdPartyItems : thirdPartyItemss) {
                    model.remove(thirdPartyItems);
                }
            }

        }

        public void addThirdPartyItems(ArrayList<ThirdPartyItems> thirdPartyItemss) {
            if (!thirdPartyItemss.isEmpty()) {
                for (ThirdPartyItems thirdPartyItems : thirdPartyItemss) {
                    model.add(thirdPartyItems);
                }
            }
        }

        public void addThirdPartyItems(ThirdPartyItems thirdPartyItems) {
            model.add(thirdPartyItems);
        }

        public void insertEmptyThirdPartyItems() {
            addThirdPartyItems(new ThirdPartyItems());
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

    private static class ThirdPartyItemsTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 14;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Kode<br>Barang</br></b></center>", "<html><center><b>Nama/Jenis<br>Barang</br></b></center>",
            "<html><center><b>Merk/<br>Type</br></b></center>",
            "<html><center><b>No.Sertifikat/No.Pabrik/<br>No.Chasis/No.Mesin</br></b></center>",
            "<html><center><b>Tahun Pembelian/<br>Pengadaan</br></b></center>",
            "<html><center><b>Asal-usul/<br>Cara Perolehan</br></b></center>",
            "<html><center><b>Nama Penyumbang/<br>Pihak Ketiga</br></b></center>",
            "<html><center><b>Satuan</b></center>",
            "<html><center><b>Keadaan Barang/<br>(B/KB)</br></b></center>",
            "<html><b>Barang</b>", "<html><b>Harga</b>",
            "<html><b>Keterangan</b>"};
        private ArrayList<ThirdPartyItems> thirdPartyItemss = new ArrayList<ThirdPartyItems>();

        public ThirdPartyItemsTableModel() {
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
            } else if (columnIndex == 11 || columnIndex == 12) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 6) {
                return Integer.class;
            } else if (columnIndex == 2 || columnIndex == 4 || columnIndex == 5
                    || columnIndex == 9 || columnIndex == 10) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ThirdPartyItems> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            thirdPartyItemss.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ThirdPartyItems thirdPartyItems) {
            insertRow(getRowCount(), thirdPartyItems);
        }

        public void insertRow(int row, ThirdPartyItems thirdPartyItems) {
            thirdPartyItemss.add(row, thirdPartyItems);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ThirdPartyItems oldThirdPartyItems, ThirdPartyItems newThirdPartyItems) {
            int index = thirdPartyItemss.indexOf(oldThirdPartyItems);
            thirdPartyItemss.set(index, newThirdPartyItems);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ThirdPartyItems thirdPartyItems) {
            int row = thirdPartyItemss.indexOf(thirdPartyItems);
            thirdPartyItemss.remove(thirdPartyItems);
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
                thirdPartyItemss.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                thirdPartyItemss.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            thirdPartyItemss.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (thirdPartyItemss.get(i) == null) {
                    thirdPartyItemss.set(i, new ThirdPartyItems());
                }
            }
        }

        public ArrayList<ThirdPartyItems> getThirdPartyItemss() {
            return thirdPartyItemss;
        }

        @Override
        public int getRowCount() {
            return thirdPartyItemss.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ThirdPartyItems getThirdPartyItems(int row) {
            if (!thirdPartyItemss.isEmpty()) {
                return thirdPartyItemss.get(row);
            }
            return new ThirdPartyItems();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ThirdPartyItems thirdPartyItems = getThirdPartyItems(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        thirdPartyItems.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    thirdPartyItems.setItemCode((String) aValue);
                    break;
                case 3:
                    if (aValue instanceof ThirdPartyItems) {
                        thirdPartyItems = (ThirdPartyItems) aValue;
                    }
                    break;
                case 4:
                    thirdPartyItems.setSpecificationType((String) aValue);
                    break;
                case 5:
                    thirdPartyItems.setSpecificationNumber((String) aValue);
                    break;
                case 6:
                    if (aValue instanceof Integer) {
                        thirdPartyItems.setAcquisitionYear((Integer) aValue);
                    }
                    break;
                case 7:
                    thirdPartyItems.setAcquisitionWay((String) aValue);
                    break;
                case 8:
                    thirdPartyItems.setThirdPartyName((String) aValue);
                    break;
                case 9:
                    thirdPartyItems.setItemsUnit((String) aValue);
                    break;
                case 10:
                    thirdPartyItems.setCondition((String) aValue);
                    break;
                case 11:
                    if (aValue instanceof Integer) {
                        thirdPartyItems.setAmount((Integer) aValue);
                    }
                    break;
                case 12:
                    if (aValue instanceof BigDecimal) {
                        thirdPartyItems.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 13:
                    thirdPartyItems.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ThirdPartyItems thirdPartyItems = getThirdPartyItems(row);
            switch (column) {
                case 0:
                    toBeDisplayed = thirdPartyItems.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = thirdPartyItems.getItemCode();
                    break;
                case 3:
                    toBeDisplayed = thirdPartyItems;
                    break;
                case 4:
                    toBeDisplayed = thirdPartyItems.getSpecificationType();
                    break;
                case 5:
                    toBeDisplayed = thirdPartyItems.getSpecificationNumber();
                    break;
                case 6:
                    toBeDisplayed = thirdPartyItems.getAcquisitionYear();
                    break;
                case 7:
                    toBeDisplayed = thirdPartyItems.getAcquisitionWay();
                    break;
                case 8:
                    toBeDisplayed = thirdPartyItems.getThirdPartyName();
                    break;
                case 9:
                    toBeDisplayed = thirdPartyItems.getItemsUnit();
                    break;
                case 10:
                    toBeDisplayed = thirdPartyItems.getCondition();
                    break;
                case 11:
                    toBeDisplayed = thirdPartyItems.getAmount();
                    break;
                case 12:
                    toBeDisplayed = thirdPartyItems.getPrice();
                    break;
                case 13:
                    toBeDisplayed = thirdPartyItems.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadThirdPartyItems extends SwingWorker<ThirdPartyItemsTableModel, ThirdPartyItems> {

        private ThirdPartyItemsTableModel model;
        private Exception exception;
        private String modifier;

        public LoadThirdPartyItems(String modifier) {
            this.model = (ThirdPartyItemsTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ThirdPartyItems> chunks) {
            mainframe.stopInActiveListener();
            for (ThirdPartyItems thirdPartyItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Penerimaan Barang Dari Pihak Ketiga Barang " + thirdPartyItems.getItemName());
                model.add(thirdPartyItems);
            }
        }

        @Override
        protected ThirdPartyItemsTableModel doInBackground() throws Exception {
            try {
                ArrayList<ThirdPartyItems> thirdPartyItemss = logic.getThirdPartyItems(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!thirdPartyItemss.isEmpty()) {
                    for (int i = 0; i < thirdPartyItemss.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / thirdPartyItemss.size();

                        ThirdPartyItems thirdPartyItems = thirdPartyItemss.get(i);
                        
                        if (thirdPartyItems.getUnit() != null) {
                            thirdPartyItems.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), thirdPartyItems.getUnit()));
                        }

                        setProgress((int) progress);
                        publish(thirdPartyItems);
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
                JXErrorPane.showDialog(ThirdPartyItemsPanel.this, info);
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

    private class ImportThirdPartyItems extends SwingWorker<ThirdPartyItemsTableModel, ThirdPartyItems> {

        private ThirdPartyItemsTableModel model;
        private Exception exception;
        private List<ThirdPartyItems> data;

        public ImportThirdPartyItems(List<ThirdPartyItems> data) {
            this.model = (ThirdPartyItemsTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ThirdPartyItems> chunks) {
            mainframe.stopInActiveListener();
            for (ThirdPartyItems thirdPartyItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload Penerimaan Barang Dari Pihak Ketiga Barang " + thirdPartyItems.getItemName());
                model.add(thirdPartyItems);
            }
        }

        @Override
        protected ThirdPartyItemsTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ThirdPartyItems thirdPartyItems = data.get(i);

                        synchronized (thirdPartyItems) {
                            thirdPartyItems = logic.insertThirdPartyItems(mainframe.getSession(), thirdPartyItems);
                        }


                        setProgress((int) progress);
                        publish(thirdPartyItems);
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
                JXErrorPane.showDialog(ThirdPartyItemsPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
