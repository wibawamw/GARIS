package org.motekar.project.civics.archieve.assets.procurement.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.TableColumnExt;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.assets.procurement.objects.InventoryCard;
import org.motekar.project.civics.archieve.assets.procurement.objects.ItemCardMap;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.ItemCardJasper;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
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
public class ItemCardPanel extends JXPanel implements PrintButtonListener {

    private ArchieveMainframe mainframe;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.LEFT);
    //
    private JXComboBox comboSearchUnit = new JXComboBox();
    private JXTextField fieldItems = new JXTextField();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItemsUnit = new JXTextField();
    private JXTextField fieldCardNumber = new JXTextField();
    private JXTextField fieldSpecification = new JXTextField();
    private JXButton btRefresh = new JXButton("Refresh");
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private ItemCardTable table = new ItemCardTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private LoadItemCard worker;
    private ProgressListener progressListener;
    private TableRowSorter<TableModel> sorter;
    //
    private ItemsCategory selectedItemsCategory = null;
    private ItemsCategory rootItemsCategory = null;
    private ItemsCategory parentCategory = null;
    //
    private Signer commanderSigner = null;
    private Signer employeeSigner = null;
    private Approval approval;
    private InventoryCard currentCard = null;
    //
    private JFileChooser xlsChooser;

    public ItemCardPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ProcurementBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
    }

    private void construct() {
        
        constructFileChooser();

        loadParentCategory();
        loadComboUnit();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPrintPanel.addListener(this);

        btChooseItems.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg dlg = new ItemsCategoryChooserDlg(mainframe, mainframe.getSession(), mainframe.getConnection(),
                        null, parentCategory, false);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    try {
                        selectedItemsCategory = dlg.getSelectedItemsCategory();
                        rootItemsCategory = dlg.getRootCategory();
                        fieldItems.setText(selectedItemsCategory.getCategoryName());
                        setOldCard();
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });

        comboSearchUnit.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                try {
                    setOldCard();
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
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


        btRefresh.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    InventoryCard oldCard = getOldInventoryCard(getWarehouseFromComboBox());
                    InventoryCard newCard = getNewInventoryCard();
                    
                    if (oldCard != null) {
                        newCard = logic.updateInventoryCard(mainframe.getSession(), oldCard, newCard);
                    } else {
                        newCard = logic.insertInventoryCard(mainframe.getSession(), newCard);
                    }
                    
                    currentCard = newCard;
                    
                    if (newCard != null) {
                        table.loadData(generateModifier(newCard.getWarehouse(), newCard.getItemCode()));
                    }
                    
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        fillDefaultSignerLink();

        setLayout(new BorderLayout());
        add(createDataViewPanel(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        
        checkLogin();
        
        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
    }
    
    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            comboSearchUnit.setEnabled(true);
        } else {
            Unit unit = mainframe.getUnit();
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

    private Unit getWarehouseFromComboBox() {
        Unit warehouse = null;
        Object obj = comboSearchUnit.getSelectedItem();
        if (obj instanceof Unit) {
            warehouse = (Unit) obj;
        }

        return warehouse;
    }

    private void setOldCard() throws SQLException {
        Unit warehouse = getWarehouseFromComboBox();
        InventoryCard oldCard = getOldInventoryCard(warehouse);
        if (oldCard != null) {
            fieldItemsUnit.setText(oldCard.getItemsUnit());
            fieldCardNumber.setText(oldCard.getCardNumber());
            fieldSpecification.setText(oldCard.getSpecification());
        } else {
            if (rootItemsCategory != null) {
                fieldCardNumber.setText(logic.generatedInventoryCardCode(mainframe.getSession()));
                fieldSpecification.setText(rootItemsCategory.getCategoryName());

                String itemsUnit = "";

                if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_LAND)) {
                    itemsUnit = "m2";
                } else if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_MACHINE)) {
                    itemsUnit = "unit/buah";
                } else if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_BUILDING)) {
                    itemsUnit = "unit";
                } else if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_NETWORK)) {
                    itemsUnit = "unit";
                } else if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_FIXED_ASSET)) {
                    itemsUnit = "unit/buah";
                } else if (rootItemsCategory.getTypes().equals(ItemsCategory.ITEMS_CONSTRUCTION)) {
                    itemsUnit = "unit";
                }


                fieldItemsUnit.setText(itemsUnit);
            } else {
                fieldSpecification.setText("");
                fieldItemsUnit.setText("");
            }
        }
    }

    private InventoryCard getOldInventoryCard(Unit warehouse) throws SQLException {
        InventoryCard oldCard = null;

        if (warehouse != null && selectedItemsCategory != null) {
            oldCard = logic.getInventoryCard(mainframe.getSession(), warehouse, selectedItemsCategory.getCategoryCode());
        }

        return oldCard;
    }

    private InventoryCard getNewInventoryCard() {
        Unit warehouse = null;
        Object obj = comboSearchUnit.getSelectedItem();
        if (obj instanceof Unit) {
            warehouse = (Unit) obj;
        }

        InventoryCard newCard = null;

        if (warehouse != null && selectedItemsCategory != null) {
            newCard = new InventoryCard();
            newCard.setWarehouse(warehouse);
            newCard.setItemCode(selectedItemsCategory.getCategoryCode());
            newCard.setItemName(selectedItemsCategory.getCategoryName());
            newCard.setItemsUnit(fieldItemsUnit.getText());
            newCard.setCardNumber(fieldCardNumber.getText());
            newCard.setSpecification(fieldSpecification.getText());
        }

        return newCard;
    }

    private String generateModifier(Unit warehouse, String code) {
        StringBuilder query = new StringBuilder();
        
        if (warehouse != null && !code.equals("")) {
            query.append("where warehouse = ").
                    append(warehouse.getIndex()).
                    append(" and itemscode like '").
                    append(code).append("%' ");
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
                    return mLogic.getUnit(mainframe.getSession());
                }
            };
            lw.execute();
            final EventList<Unit> units = GlazedLists.eventList(lw.get());

            units.add(0, new Unit());

            if (!units.isEmpty()) {
                for (Unit un : units) {
                    un.setStyled(true);
                }
            }

            units.add(0, new Unit());

            AutoCompleteSupport support = AutoCompleteSupport.install(comboSearchUnit, units);
            support.setFilterMode(TextMatcherEditor.CONTAINS);


        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
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

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);


//        JPanel panelSearch = new JPanel();
//        panelSearch.setLayout(new FlowLayout(FlowLayout.LEFT));
//        panelSearch.add(createSearchPanel(), null);
//        panelSearch.add(createSearchPanel2(), null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(btPrintPanel, BorderLayout.CENTER);

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
                "left:pref,10px,left:pref,10px, pref,30px,"
                + "left:pref,10px,left:pref,10px, fill:default:grow,pref,30px,"
                + "left:pref,10px,left:pref,10px, fill:default:grow,100px",
                "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setColumnGroups(new int[][]{{6, 13}});

        ProfileAccount profile = mainframe.getProfileAccount();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SKPD", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.addLabel(profile.getCompany().toUpperCase().trim(), cc.xy(5, 1));

        builder.addLabel("KAB / KOTA", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.addLabel(profile.getState().toUpperCase().trim(), cc.xy(5, 3));

        builder.addLabel("PROPINSI", cc.xy(1, 5));
        builder.addLabel(":", cc.xy(3, 5));
        builder.addLabel(profile.getProvince().toUpperCase().trim(), cc.xy(5, 5));

        builder.addLabel("GUDANG/UNIT", cc.xy(7, 1));
        builder.addLabel(":", cc.xy(9, 1));
        builder.add(comboSearchUnit, cc.xyw(11, 1, 2));

        builder.addLabel("NAMA BARANG", cc.xy(7, 3));
        builder.addLabel(":", cc.xy(9, 3));
        builder.add(fieldItems, cc.xy(11, 3));
        builder.add(createStrip(1.0, 1.0), cc.xy(12, 3));

        builder.addLabel("SATUAN", cc.xy(7, 5));
        builder.addLabel(":", cc.xy(9, 5));
        builder.add(fieldItemsUnit, cc.xyw(11, 5, 2));

        builder.addLabel("Kartu No.", cc.xy(14, 1));
        builder.addLabel(":", cc.xy(16, 1));
        builder.add(fieldCardNumber, cc.xy(18, 1));

        builder.addLabel("Spesifikasi", cc.xy(14, 3));
        builder.addLabel(":", cc.xy(16, 3));
        builder.add(fieldSpecification, cc.xy(18, 3));

        builder.add(btRefresh, cc.xy(14, 5));

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
            

            ItemCardJasper report = new ItemCardJasper("Kartu Persediaan Barang", table.getItemCardMaps(), 
                    currentCard, mainframe.getProfileAccount(), approval, employeeSigner, commanderSigner);
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
            
            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";


                ItemCardJasper report = new ItemCardJasper("Kartu Persediaan Barang", table.getItemCardMaps(), 
                    currentCard, mainframe.getProfileAccount(), approval, employeeSigner, commanderSigner);

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

    private class ItemCardTable extends JXTable {

        private ItemCardTableModel model;

        public ItemCardTable() {
            model = new ItemCardTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Barang-barang</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));
            group.add(colModel.getColumn(5));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><center><b>Jumlah Barang yang diterima/<br>Yang dikeluarkan/Sisa</br></b></center>");
            group2.add(colModel.getColumn(7));
            group2.add(colModel.getColumn(8));
            group2.add(colModel.getColumn(9));


            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group2);
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

            worker = new LoadItemCard(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemCardMap getSelectedItemCardMap() {
            ArrayList<ItemCardMap> selectedItemCardMaps = getSelectedItemCardMaps();
            return selectedItemCardMaps.get(0);
        }

        public ArrayList<ItemCardMap> getItemCardMaps() {
            return model.getItemCardMaps();
        }

        public ArrayList<ItemCardMap> getSelectedItemCardMaps() {

            ArrayList<ItemCardMap> itemCardMaps = new ArrayList<ItemCardMap>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemCardMap itemCardMap = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof ItemCardMap) {
                            itemCardMap = (ItemCardMap) obj;
                            itemCardMaps.add(itemCardMap);
                        }
                    }
                }
            }

            return itemCardMaps;
        }

        public void updateSelectedItemCardMap(ItemCardMap itemCardMap) {
            model.updateRow(getSelectedItemCardMap(), itemCardMap);
        }

        public void removeItemCardMap(ArrayList<ItemCardMap> itemCardMaps) {
            if (!itemCardMaps.isEmpty()) {
                for (ItemCardMap itemCardMap : itemCardMaps) {
                    model.remove(itemCardMap);
                }
            }

        }

        public void addItemCardMap(ArrayList<ItemCardMap> itemCardMaps) {
            if (!itemCardMaps.isEmpty()) {
                for (ItemCardMap itemCardMap : itemCardMaps) {
                    model.add(itemCardMap);
                }
            }
        }

        public void addItemCardMap(ItemCardMap itemCardMap) {
            model.add(itemCardMap);
        }

        public void insertEmptyItemCardMap() {
            addItemCardMap(new ItemCardMap());
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

    private static class ItemCardTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 11;
        private static final String[] COLUMNS = {"<html><b>Tanggal</b>",
            "<html><center><b>No./Tgl.Surat Dasar<br>Penerimaan/Pengeluaran</br></b></center>",
            "<html><center><b>Uraian</b></center>",
            "<html><center><b>Masuk</b></center>",
            "<html><center><b>Keluar</b></center>",
            "<html><center><b>Sisa</b></center>",
            "<html><center><b>Harga<br>Satuan</br></b></center>",
            "<html><center><b>Bertambah</b></center>",
            "<html><center><b>Berkurang</b></center>",
            "<html><center><b>Sisa</b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<ItemCardMap> itemCardMaps = new ArrayList<ItemCardMap>();

        public ItemCardTableModel() {
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
                return Date.class;
            } else if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
                return Integer.class;
            } else if (columnIndex == 1) {
                return JLabel.class;
            } else if (columnIndex == 6 || columnIndex == 7 || columnIndex == 8 || columnIndex == 9) {
                return BigDecimal.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ItemCardMap> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            itemCardMaps.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemCardMap itemCardMap) {
            insertRow(getRowCount(), itemCardMap);
        }

        public void insertRow(int row, ItemCardMap itemCardMap) {
            itemCardMaps.add(row, itemCardMap);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemCardMap oldItemCardMap, ItemCardMap newItemCardMap) {
            int index = itemCardMaps.indexOf(oldItemCardMap);
            itemCardMaps.set(index, newItemCardMap);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemCardMap itemCardMap) {
            int row = itemCardMaps.indexOf(itemCardMap);
            itemCardMaps.remove(itemCardMap);
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
                itemCardMaps.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                itemCardMaps.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            itemCardMaps.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (itemCardMaps.get(i) == null) {
                    itemCardMaps.set(i, new ItemCardMap());
                }
            }
        }

        public ArrayList<ItemCardMap> getItemCardMaps() {
            return itemCardMaps;
        }

        @Override
        public int getRowCount() {
            return itemCardMaps.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemCardMap getItemCardMap(int row) {
            if (!itemCardMaps.isEmpty()) {
                return itemCardMaps.get(row);
            }
            return new ItemCardMap();
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemCardMap itemCardMap = getItemCardMap(row);
            switch (column) {
                case 0:
                    toBeDisplayed = itemCardMap;
                    break;
                case 1:

                    StringBuilder str = new StringBuilder();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                            new Locale("in", "id", "id"));

                    if (!itemCardMap.getDocumentNumber().equals("")) {
                        str.append(itemCardMap.getDocumentNumber());
                    }

                    if (itemCardMap.getDocumentDate() != null) {
                        if (!itemCardMap.getDocumentNumber().equals("")) {
                            str.append(" -- ");
                            str.append(sdf.format(itemCardMap.getDocumentDate()));
                        } else {
                            str.append(sdf.format(itemCardMap.getDocumentDate()));
                        }

                    }

                    toBeDisplayed = str.toString();
                    break;
                case 2:
                    toBeDisplayed = itemCardMap.getArgument();
                    break;
                case 3:
                    if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) > 0) {
                        toBeDisplayed = itemCardMap.getAmount();
                    } else {
                        toBeDisplayed = Integer.valueOf(0);
                    }
                    break;
                case 4:
                    if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) < 0) {
                        toBeDisplayed = Math.abs(itemCardMap.getAmount());
                    } else {
                        toBeDisplayed = Integer.valueOf(0);
                    }
                    break;
                case 5:
                    toBeDisplayed = itemCardMap.getResidual();
                    break;
                case 6:
                    toBeDisplayed = itemCardMap.getPrice();
                    break;
                case 7:
                    BigDecimal added = BigDecimal.ZERO;
                    added = added.add(itemCardMap.getPrice());

                    if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) > 0) {
                        added = added.multiply(BigDecimal.valueOf(itemCardMap.getAmount()));
                        toBeDisplayed = added;
                    } else {
                        toBeDisplayed = BigDecimal.ZERO;
                    }
                    break;
                case 8:
                    BigDecimal subs = BigDecimal.ZERO;
                    subs = subs.add(itemCardMap.getPrice());

                    if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) < 0) {
                        subs = subs.multiply(BigDecimal.valueOf(Math.abs(itemCardMap.getAmount())));
                        toBeDisplayed = subs;
                    } else {
                        toBeDisplayed = BigDecimal.ZERO;
                    }
                    break;
                case 9:
                    BigDecimal rsd = BigDecimal.ZERO;
                    rsd = rsd.add(itemCardMap.getPrice());
                    rsd = rsd.multiply(BigDecimal.valueOf(itemCardMap.getResidual()));

                    toBeDisplayed = rsd;
                    break;
                case 10:
                    toBeDisplayed = itemCardMap.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemCard extends SwingWorker<ItemCardTableModel, ItemCardMap> {

        private ItemCardTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemCard(String modifier) {
            this.model = (ItemCardTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemCardMap> chunks) {
            mainframe.stopInActiveListener();
            for (ItemCardMap itemCardMap : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kartu Persediaan Barang = " + itemCardMap.getDocumentNumber());
                model.add(itemCardMap);
            }
        }

        @Override
        protected ItemCardTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemCardMap> itemCardMaps = logic.getItemCardMap(mainframe.getSession(), modifier);

                double progress = 0.0;
                Integer residual = Integer.valueOf(0);
                if (!itemCardMaps.isEmpty()) {
                    for (int i = 0; i < itemCardMaps.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / itemCardMaps.size();

                        ItemCardMap itemCardMap = itemCardMaps.get(i);

                        residual += itemCardMap.getAmount();

                        itemCardMap.setResidual(residual);

                        setProgress((int) progress);
                        publish(itemCardMap);
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
                JXErrorPane.showDialog(ItemCardPanel.this, info);
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
