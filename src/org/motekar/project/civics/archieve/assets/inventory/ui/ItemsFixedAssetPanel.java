package org.motekar.project.civics.archieve.assets.inventory.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXGroupableTableHeader;
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
import org.motekar.lib.common.listener.ButtonLoadListener;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.LoadButtonPanel;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsFixedAssetJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsFixedAssetWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsFixedAssetWorkbookReader;
import org.motekar.project.civics.archieve.utils.misc.CustomFilterPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterCardPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterValue;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserAccount;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsFixedAssetPanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private LoadButtonPanel btLoadPanel = new LoadButtonPanel("Upload Template", "Download Template", FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    //
    private CustomFilterPanel filterPanel;
    private JXButton btFilter = new JXButton("Reload");
    //
    private JXComboBox comboUnit = new JXComboBox();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXTextField fieldBookAuthor = new JXTextField();
    private JXTextField fieldBookSpec = new JXTextField();
    private JXTextField fieldArtRegion = new JXTextField();
    private JXTextField fieldArtAuthor = new JXTextField();
    private JXTextField fieldArtMaterial = new JXTextField();
    private JXTextField fieldCattleType = new JXTextField();
    private JXTextField fieldCattleSize = new JXTextField();
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JCheckBox checkUnknown = new JCheckBox("Tidak Diketahui");
    private JXComboBox comboCondition = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private ItemsFixedAssetTable table = new ItemsFixedAssetTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsFixedAsset worker;
    private ProgressListener progressListener;
    private ItemsFixedAsset selectedItemsFixedAsset = null;
    private ItemsCategory selectedItemsCategory = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportItemsFixedAsset importWorker;
    //
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    private ItemsCategory parentCategory = null;
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor;

    public ItemsFixedAssetPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.userAccount = mainframe.getUserAccount();
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        this.filterPanel = new CustomFilterPanel(mainframe, createFilterValues(), false, false);
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (!userGroup.getGroupName().equals("Administrator")) {
            Unit unit = mainframe.getUnit();
            unit.setHirarchiecal(true);
            filterPanel.setUnitEnable(false);
            filterPanel.setComboUnit(unit);
            comboUnit.setEnabled(false);
            comboUnit.setSelectedItem(unit);
            btFilter.setEnabled(true);
        }
    }

    private void construct() {

        contructNumberField();
        loadComboUnit();
        loadComboCondition();
        loadParentCategory();

        pbar.setVisible(false);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPanel.addListener(this);
        btSavePanelUp.addListener(this);
        btSavePanelBottom.addListener(this);
        btPrintPanel.addListener(this);

        btLoadPanel.addListener(this);

        btPanel.mergeWith(btLoadPanel);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(createInputPanel(), "1");

        btFilter.setEnabled(filterPanel.isFiltered());

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ItemsFixedAsset> fixedAssets = table.getSelectedItemsFixedAssets();
                    if (!fixedAssets.isEmpty()) {
                        if (fixedAssets.size() == 1) {
                            selectedItemsFixedAsset = fixedAssets.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsFixedAsset.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsFixedAsset.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsFixedAsset = null;
                            selectedItemsCategory = null;
                            btPanel.setButtonState(ManipulationButtonPanel.UNEDIT);
                        }
                    } else {
                        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                    }
                }
            }
        });

        filterPanel.addPropertyChangeListener("filtered", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                btFilter.setEnabled(filterPanel.isFiltered());
                if (!filterPanel.isFiltered()) {
                    if (worker != null) {
                        worker.cancel(true);
                    }
                    table.clear();
                    btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
                }
            }
        });

        btChooseItems.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg dlg = new ItemsCategoryChooserDlg(mainframe, mainframe.getSession(), mainframe.getConnection(),
                        ItemsCategory.ITEMS_FIXED_ASSET, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.toString());
                }
            }
        });

        btFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String modifier = filterPanel.getModifier();
                    table.loadData(modifier);
                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(ItemsFixedAssetPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        checkUnknown.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
            }
        });

        checkUnknown.setFocusPainted(false);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);


        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        fieldItems.setEditable(false);
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

            filterPanel.loadComboUnit(units);

            unitMaster.addAll(units);

            units.add(0, new Unit());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUnit, units);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboCondition() {
        try {
            SwingWorker<ArrayList<Condition>, Void> lw = new SwingWorker<ArrayList<Condition>, Void>() {

                @Override
                protected ArrayList<Condition> doInBackground() throws Exception {
                    return mLogic.getCondition(mainframe.getSession());
                }
            };
            lw.execute();
            final EventList<Condition> conditions = GlazedLists.eventList(lw.get());

            conditions.add(0, new Condition());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboCondition, conditions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadParentCategory() {
        try {
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_FIXED_ASSET);
        } catch (SQLException ex) {
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
                "300px,right:pref,10px, pref,5px,100px,5px,fill:default:grow,300px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,20px,pref,5px,"
                + "pref,5px,pref,20px,pref,5px,pref,5px,"
                + "pref,5px,pref,20px,pref,5px,pref,5px,"
                + "pref,20px,pref,20px,pref,5px,pref,5px,"
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

        builder.addLabel("UPB / Sub UPB", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 5));

        builder.add(itemLabel, cc.xy(2, 8));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 8, 1));
        builder.add(fieldItems, cc.xyw(6, 8, 3));

        builder.addLabel("No. Register", cc.xy(2, 10));
        builder.add(fieldRegNumber, cc.xyw(4, 10, 5));

        builder.addSeparator("Buku / Perpustakaan", cc.xyw(2, 12, 7));

        builder.addLabel("Judul/Pencipta", cc.xy(2, 14));
        builder.add(fieldBookAuthor, cc.xyw(4, 14, 5));

        builder.addLabel("Spesifikasi", cc.xy(2, 16));
        builder.add(fieldBookSpec, cc.xyw(4, 16, 5));

        builder.addSeparator("Barang Bercorak Kesenian / Kebudayaan", cc.xyw(2, 18, 7));

        builder.addLabel("Asal Daerah", cc.xy(2, 20));
        builder.add(fieldArtRegion, cc.xyw(4, 20, 5));

        builder.addLabel("Pencipta", cc.xy(2, 22));
        builder.add(fieldArtAuthor, cc.xyw(4, 22, 5));

        builder.addLabel("Bahan", cc.xy(2, 24));
        builder.add(fieldArtMaterial, cc.xyw(4, 24, 5));

        builder.addSeparator("Hewan / Ternak dan Tumbuhan", cc.xyw(2, 26, 7));

        builder.addLabel("Jenis", cc.xy(2, 28));
        builder.add(fieldCattleType, cc.xyw(4, 28, 5));

        builder.addLabel("Ukuran", cc.xy(2, 30));
        builder.add(fieldCattleSize, cc.xyw(4, 30, 5));

        builder.addSeparator("", cc.xyw(2, 32, 7));

        builder.addLabel("Harga  (Rp.)", cc.xy(2, 34));
        builder.add(fieldPrice, cc.xyw(4, 34, 5));

        builder.addLabel("Tahun Cetak / Pembelian", cc.xy(2, 36));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 36, 3));
        builder.add(checkUnknown, cc.xyw(8, 36, 1));

        builder.addLabel("Kondisi", cc.xy(2, 38));
        builder.add(comboCondition, cc.xyw(4, 38, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 40));
        builder.add(fieldFundingSource, cc.xyw(4, 40, 5));

        builder.addLabel("Asal-Usul / Cara Perolehan", cc.xy(2, 42));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 42, 5));

        builder.addLabel("Keterangan", cc.xy(2, 44));
        builder.add(scPane, cc.xywh(4, 44, 5, 2));

        builder.addSeparator("", cc.xyw(1, 47, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 48, 9));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-E");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
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
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,50px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(filterPanel, cc.xyw(1, 1, 3));
        builder.add(btFilter, cc.xyw(1, 3, 1));

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
        fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
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

    private void clearForm() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            comboUnit.getEditor().setItem(null);
        }
        if (parentCategory != null) {
            fieldItems.setText(parentCategory.toString());
        } else {
            fieldItems.setText("");
        }
        iLookUp.clear();
        fieldRegNumber.setText("");
        fieldBookAuthor.setText("");
        fieldBookSpec.setText("");
        fieldArtRegion.setText("");
        fieldArtAuthor.setText("");
        fieldArtMaterial.setText("");
        fieldCattleType.setText("");
        fieldCattleSize.setText("");
        checkUnknown.setSelected(false);
        clearYear();
        comboCondition.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedItemsFixedAsset != null) {
            try {
                if (selectedItemsFixedAsset.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsFixedAsset.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsFixedAsset.getItemCode());
                category.setCategoryName(selectedItemsFixedAsset.getItemName());
                category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsFixedAsset.getRegNumber());
                fieldBookAuthor.setText(selectedItemsFixedAsset.getBookAuthor());
                fieldBookSpec.setText(selectedItemsFixedAsset.getBookSpec());
                fieldArtRegion.setText(selectedItemsFixedAsset.getArtRegion());
                fieldArtAuthor.setText(selectedItemsFixedAsset.getArtAuthor());
                fieldArtMaterial.setText(selectedItemsFixedAsset.getArtMaterial());
                fieldCattleType.setText(selectedItemsFixedAsset.getCattleType());
                fieldCattleSize.setText(selectedItemsFixedAsset.getCattleSize());

                if (selectedItemsFixedAsset.getCondition() == null) {
                    comboCondition.getEditor().setItem(null);
                } else {
                    comboCondition.setSelectedItem(selectedItemsFixedAsset.getCondition());
                }

                if (selectedItemsFixedAsset.getAcquisitionYear() == null) {
                    checkUnknown.setSelected(true);
                    clearYear();
                } else {
                    checkUnknown.setSelected(false);

                    fieldAcquisitionYear.setYear(selectedItemsFixedAsset.getAcquisitionYear());
                }

                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
                fieldPrice.setValue(selectedItemsFixedAsset.getPrice());
                fieldFundingSource.setText(selectedItemsFixedAsset.getFundingSource());
                fieldAcquisitionWay.setText(selectedItemsFixedAsset.getAcquisitionWay());
                fieldDescription.setText(selectedItemsFixedAsset.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsFixedAsset getItemsFixedAsset() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Unit unit = null;
        Object unitObj = comboUnit.getSelectedItem();
        if (unitObj instanceof Unit) {
            unit = (Unit) unitObj;
        }

        Condition condition = null;
        Object conObj = comboCondition.getSelectedItem();
        if (conObj instanceof Condition) {
            condition = (Condition) conObj;
        }

        if (condition != null) {
            if (condition.getIndex().equals(Long.valueOf(0))) {
                condition = null;
            }
        }

        ItemsCategory category = null;
        
        if (!iLookUp.isEmpty() && iLookUp.size() > 0) {
            category = iLookUp.get(iLookUp.size() - 1);
        } 

        String itemCode = "";
        String itemName = "";

        if (category != null) {
            itemCode = category.getCategoryCode();
            itemName = category.getCategoryName();
        }

        String regNumber = fieldRegNumber.getText();
        String bookAuthor = fieldBookAuthor.getText();
        String bookSpec = fieldBookSpec.getText();


        String artRegion = fieldArtRegion.getText();
        String artAuthor = fieldArtAuthor.getText();
        String artMaterial = fieldArtMaterial.getText();

        String cattleType = fieldCattleType.getText();
        String cattleSize = fieldCattleSize.getText();

        Integer acquisitionYear = fieldAcquisitionYear.getYear();

        if (checkUnknown.isSelected()) {
            acquisitionYear = null;
        }

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        String fundingSource = fieldFundingSource.getText();
        String acquisitionWay = fieldAcquisitionWay.getText();
        String description = fieldDescription.getText();


        if (itemCode.equals("") || itemName.equals("")) {
            errorString.append("<br>- Kode / Nama Barang Harus Lengkap</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan : " + errorString.toString() + "</html>");
        }

        ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
        fixedAsset.setUnit(unit);
        fixedAsset.setItemCode(itemCode);
        fixedAsset.setItemName(itemName);
        fixedAsset.setRegNumber(regNumber);
        fixedAsset.setBookAuthor(bookAuthor);
        fixedAsset.setBookSpec(bookSpec);
        fixedAsset.setArtRegion(artRegion);
        fixedAsset.setArtAuthor(artAuthor);
        fixedAsset.setArtMaterial(artMaterial);
        fixedAsset.setCattleType(cattleType);
        fixedAsset.setCattleSize(cattleSize);

        fixedAsset.setAcquisitionYear(acquisitionYear);
        fixedAsset.setCondition(condition);
        fixedAsset.setPrice(price);
        fixedAsset.setFundingSource(fundingSource);
        fixedAsset.setAcquisitionWay(acquisitionWay);
        fixedAsset.setDescription(description);

        if (selectedItemsFixedAsset != null && btPanel.isEditstate()) {
            fixedAsset.setUserName(selectedItemsFixedAsset.getUserName());
        } else {
            fixedAsset.setUserName(userAccount.getUserName());
        }
        fixedAsset.setLastUserName(userAccount.getUserName());

        return fixedAsset;
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
                ArrayList<ItemsFixedAsset> selectedItemsFixedAssets = table.getSelectedItemsFixedAssets();
                if (!selectedItemsFixedAssets.isEmpty()) {
                    logic.deleteItemsFixedAsset(mainframe.getSession(), selectedItemsFixedAssets);
                    table.removeItemsFixedAsset(selectedItemsFixedAssets);
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
            ItemsFixedAsset newItemsFixedAsset = getItemsFixedAsset();
            if (btPanel.isNewstate()) {
                newItemsFixedAsset = logic.insertItemsFixedAsset(mainframe.getSession(), newItemsFixedAsset);
                table.addItemsFixedAsset(newItemsFixedAsset);
                selectedItemsFixedAsset = newItemsFixedAsset;
                selectedItemsFixedAsset.setItemName(getLastItemsName());
                selectedItemsFixedAsset.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newItemsFixedAsset = logic.updateItemsFixedAsset(mainframe.getSession(), selectedItemsFixedAsset, newItemsFixedAsset);
                table.updateSelectedItemsFixedAsset(newItemsFixedAsset);
                selectedItemsFixedAsset = newItemsFixedAsset;
                selectedItemsFixedAsset.setItemName(getLastItemsName());
                selectedItemsFixedAsset.setFullItemName(getCompleteItemsName());
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
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Unit unit = filterPanel.getUnit();

        if (unit != null) {
            ItemsFixedAssetJasper report = new ItemsFixedAssetJasper("Buku Inventaris Barang Aset Tetap Lainnya (KIB-E)", table.getItemsFixedAssets(), unit);
            report.showReport();
        }

        mainframe.setCursor(old);
    }

    public void onPrintExcel() throws Exception, CommonException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"));
            }

            @Override
            public String getDescription() {
                return "Excel File";
            }
        });

        chooser.setSelectedFile(new File(ItemsFixedAssetWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsFixedAsset> fixedAssets = table.getItemsFixedAssets();

            ItemsFixedAssetWorkbookCreator wb = new ItemsFixedAssetWorkbookCreator(mainframe, file.getPath(), fixedAssets, unitMaster);
            wb.createXLSFile();
        }
    }

    public void onPrintPDF() throws Exception, CommonException {
        //
    }

    public void onPrintGraph() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onUpload() throws Exception, CommonException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"));
            }

            @Override
            public String getDescription() {
                return "Excel File";
            }
        });

        int retVal = chooser.showOpenDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                ItemsFixedAssetWorkbookReader wbr = new ItemsFixedAssetWorkbookReader();
                List<ItemsFixedAsset> items = wbr.loadXLSFile(file.getPath());

                Comparator<Unit> comparator = new Comparator<Unit>() {

                    public int compare(Unit o1, Unit o2) {
                        return o1.getUnitCode().compareTo(o2.getUnitCode());
                    }
                };

                Comparator<ItemsCategory> comparator2 = new Comparator<ItemsCategory>() {

                    public int compare(ItemsCategory o1, ItemsCategory o2) {
                        return o1.getCategoryCode().compareTo(o2.getCategoryCode());
                    }
                };

                if (!unitMaster.isEmpty()) {
                    Collections.sort(unitMaster, comparator);
                }

                if (!items.isEmpty()) {
                    for (ItemsFixedAsset item : items) {
                        Unit unit = item.getUnit();

                        if (unit != null && !unitMaster.isEmpty()) {
                            int index = Collections.binarySearch(unitMaster, new Unit(unit.getUnitCode()), comparator);
                            if (index >= 0) {
                                item.setUnit(unitMaster.get(index));
                            }
                        }
                        item.setItemCode(item.getItemCode().replaceAll(" ", "."));

                        ItemsCategory category = mLogic.getItemsCategoryByCode(mainframe.getSession(), item.getItemCode());

                        if (category != null) {
                            item.setItemName(category.getCategoryName());
                        }
                    }

                    if (importWorker != null) {
                        importWorker.cancel(true);
                    }

                    oldCursor = mainframe.getCursor();
                    mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    importWorker = new ImportItemsFixedAsset(items);
                    progressListener = new ProgressListener(pbar, statusLabel, importWorker, false);
                    importWorker.addPropertyChangeListener(progressListener);
                    importWorker.execute();
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvalidFormatException ex) {
                Exceptions.printStackTrace(ex);
            } catch (MotekarException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            }

        }
    }

    public void onDownload() throws Exception, CommonException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"));
            }

            @Override
            public String getDescription() {
                return "Excel File";
            }
        });

        chooser.setSelectedFile(new File(ItemsFixedAssetWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            
            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            File file = chooser.getSelectedFile();
            ItemsFixedAssetWorkbookCreator wb = new ItemsFixedAssetWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsFixedAsset>(), unitMaster);
            wb.createXLSFile();
            
            mainframe.setCursor(oldCursor);
        }
    }

    private ArrayList<FilterValue> createFilterValues() {
        ArrayList<FilterValue> filterValues = new ArrayList<FilterValue>();

        filterValues.add(new FilterValue("Nama Barang", "itemname", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Kode Barang", "itemcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Register", "regnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Judul/Pencipta", "bookauthor", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Spesifikasi", "bookspec", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal Daerah", "artregion", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Pencipta", "artauthor", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Bahan", "artmaterial", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Jenis", "cattletype", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Ukuran", "cattlesize", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Jumlah", "total", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Tahun Cetak/Pembelian", "acquisitionyear", FilterCardPanel.YEAR_PANEL));
        filterValues.add(new FilterValue("Asal-Usul / Cara Perolehan", "acquisitionway", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Harga", "price", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ItemsFixedAssetTable extends JXTable {

        private ItemsFixedAssetTableModel model;

        public ItemsFixedAssetTable() {
            model = new ItemsFixedAssetTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            ColumnGroup group2 = new ColumnGroup("<html><b>Buku/Perpustakaan</b>");
            group2.add(colModel.getColumn(5));
            group2.add(colModel.getColumn(6));

            ColumnGroup group3 = new ColumnGroup("<html><center><b>Barang Bercorak Kesenian /<br>Kebudayaan</br></b></center>");
            group3.add(colModel.getColumn(7));
            group3.add(colModel.getColumn(8));
            group3.add(colModel.getColumn(9));

            ColumnGroup group4 = new ColumnGroup("<html><b>Hewan /<br>Ternak</br></b>");
            group4.add(colModel.getColumn(10));
            group4.add(colModel.getColumn(11));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group2);
            header.addColumnGroup(group3);
            header.addColumnGroup(group4);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadItemsFixedAsset(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsFixedAsset getSelectedItemsFixedAsset() {
            ArrayList<ItemsFixedAsset> selectedItemsFixedAssets = getSelectedItemsFixedAssets();
            return selectedItemsFixedAssets.get(0);
        }

        public ArrayList<ItemsFixedAsset> getItemsFixedAssets() {
            return model.getItemsFixedAssets();
        }

        public ArrayList<ItemsFixedAsset> getSelectedItemsFixedAssets() {

            ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsFixedAsset fixedAsset = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsFixedAsset) {
                            fixedAsset = (ItemsFixedAsset) obj;
                            fixedAssets.add(fixedAsset);
                        }
                    }
                }
            }

            return fixedAssets;
        }

        public void updateSelectedItemsFixedAsset(ItemsFixedAsset fixedAsset) {
            model.updateRow(getSelectedItemsFixedAsset(), fixedAsset);
        }

        public void removeItemsFixedAsset(ArrayList<ItemsFixedAsset> fixedAssets) {
            if (!fixedAssets.isEmpty()) {
                for (ItemsFixedAsset fixedAsset : fixedAssets) {
                    model.remove(fixedAsset);
                }
            }

        }

        public void addItemsFixedAsset(ArrayList<ItemsFixedAsset> fixedAssets) {
            if (!fixedAssets.isEmpty()) {
                for (ItemsFixedAsset fixedAsset : fixedAssets) {
                    model.add(fixedAsset);
                }
            }
        }

        public void addItemsFixedAsset(ItemsFixedAsset fixedAsset) {
            model.add(fixedAsset);
        }

        public void insertEmptyItemsFixedAsset() {
            addItemsFixedAsset(new ItemsFixedAsset());
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
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setGroupingUsed(false);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsFixedAssetTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 18;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Judul /<br>Pencipta</br></b></center>", "<html><b>Spresifikasi</b>",
            "<html><b>Asal Derah</b>", "<html><b>Pencipta</b>", "<html><b>Bahan</b>",
            "<html><b>Jenis</b>", "<html><b>Ukuran</b>",
            "<html><center><b>Tahun Cetak /<br>Pembelian</br></b></center>",
            "<html><center><b>Asal-Usul /<br>Cara Perolehan</br></b></center>",
            "<html><center><b>Harga<br>(Rp.)</br></b></center>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        public ItemsFixedAssetTableModel() {
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
            } else if (columnIndex == 14) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 12) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ItemsFixedAsset> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            fixedAssets.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsFixedAsset fixedAsset) {
            insertRow(getRowCount(), fixedAsset);
        }

        public void insertRow(int row, ItemsFixedAsset fixedAsset) {
            fixedAssets.add(row, fixedAsset);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsFixedAsset oldItemsFixedAsset, ItemsFixedAsset newItemsFixedAsset) {
            int index = fixedAssets.indexOf(oldItemsFixedAsset);
            fixedAssets.set(index, newItemsFixedAsset);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsFixedAsset fixedAsset) {
            int row = fixedAssets.indexOf(fixedAsset);
            fixedAssets.remove(fixedAsset);
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
                fixedAssets.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                fixedAssets.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            fixedAssets.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (fixedAssets.get(i) == null) {
                    fixedAssets.set(i, new ItemsFixedAsset());
                }
            }
        }

        public ArrayList<ItemsFixedAsset> getItemsFixedAssets() {
            return fixedAssets;
        }

        @Override
        public int getRowCount() {
            return fixedAssets.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsFixedAsset getItemsFixedAsset(int row) {
            if (!fixedAssets.isEmpty()) {
                return fixedAssets.get(row);
            }
            return new ItemsFixedAsset();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsFixedAsset fixedAsset = getItemsFixedAsset(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        fixedAsset.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsFixedAsset) {
                        fixedAsset = (ItemsFixedAsset) aValue;
                    }
                    break;
                case 3:
                    fixedAsset.setItemCode((String) aValue);
                    break;
                case 4:
                    fixedAsset.setRegNumber((String) aValue);
                    break;
                case 5:
                    fixedAsset.setBookAuthor((String) aValue);
                    break;
                case 6:
                    fixedAsset.setBookSpec((String) aValue);
                    break;
                case 7:
                    fixedAsset.setArtRegion((String) aValue);
                    break;
                case 8:
                    fixedAsset.setArtAuthor((String) aValue);
                    break;
                case 9:
                    fixedAsset.setArtMaterial((String) aValue);
                    break;
                case 10:
                    fixedAsset.setCattleType((String) aValue);
                    break;
                case 11:
                    fixedAsset.setCattleSize((String) aValue);
                    break;
                case 12:
                    if (aValue instanceof Integer) {
                        fixedAsset.setAcquisitionYear((Integer) aValue);
                    }
                    break;
                case 13:
                    fixedAsset.setAcquisitionWay((String) aValue);
                    break;
                case 14:
                    if (aValue instanceof BigDecimal) {
                        fixedAsset.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 15:
                    fixedAsset.setDescription((String) aValue);
                    break;
                case 16:
                    fixedAsset.setUserName((String) aValue);
                    break;
                case 17:
                    fixedAsset.setLastUserName((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsFixedAsset fixedAsset = getItemsFixedAsset(row);
            switch (column) {
                case 0:
                    toBeDisplayed = fixedAsset.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = fixedAsset;
                    break;
                case 3:
                    toBeDisplayed = fixedAsset.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = fixedAsset.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = fixedAsset.getBookAuthor();
                    break;
                case 6:
                    toBeDisplayed = fixedAsset.getBookSpec();
                    break;
                case 7:
                    toBeDisplayed = fixedAsset.getArtRegion();
                    break;
                case 8:
                    toBeDisplayed = fixedAsset.getArtAuthor();
                    break;
                case 9:
                    toBeDisplayed = fixedAsset.getArtMaterial();
                    break;
                case 10:
                    toBeDisplayed = fixedAsset.getCattleType();
                    break;
                case 11:
                    toBeDisplayed = fixedAsset.getCattleSize();
                    break;
                case 12:
                    toBeDisplayed = fixedAsset.getAcquisitionYear();
                    break;
                case 13:
                    toBeDisplayed = fixedAsset.getAcquisitionWay();
                    break;
                case 14:
                    toBeDisplayed = fixedAsset.getPrice();
                    break;
                case 15:
                    toBeDisplayed = fixedAsset.getDescription();
                    break;
                case 16:
                    toBeDisplayed = fixedAsset.getUserName();
                    break;
                case 17:
                    toBeDisplayed = fixedAsset.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsFixedAsset extends SwingWorker<ItemsFixedAssetTableModel, ItemsFixedAsset> {

        private ItemsFixedAssetTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsFixedAsset(String modifier) {
            this.model = (ItemsFixedAssetTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsFixedAsset> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsFixedAsset fixedAsset : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-E " + fixedAsset.getItemName());
                model.add(fixedAsset);
            }
        }

        private String getCompleteItemsName(ArrayList<ItemsCategory> iLookUp) {
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

        private String getLastItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = "";

            if (!iLookUp.isEmpty()) {
                itemsName = iLookUp.get(iLookUp.size() - 1).getCategoryName();
            }

            return itemsName;
        }

        @Override
        protected ItemsFixedAssetTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsFixedAsset> fixedAssets = logic.getItemsFixedAsset(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!fixedAssets.isEmpty()) {
                    for (int i = 0; i < fixedAssets.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / fixedAssets.size();

                        ItemsFixedAsset fixedAsset = fixedAssets.get(i);

                        if (fixedAsset.getUnit() != null) {
                            fixedAsset.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), fixedAsset.getUnit()));
                        }

                        if (fixedAsset.getCondition() != null) {
                            fixedAsset.setCondition(mLogic.getCondition(mainframe.getSession(), fixedAsset.getCondition()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(fixedAsset.getItemCode());
                        category.setCategoryName(fixedAsset.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        fixedAsset.setItemName(getLastItemsName(iLookUp));
                        fixedAsset.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(fixedAsset);
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
                JXErrorPane.showDialog(ItemsFixedAssetPanel.this, info);
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

    private class ImportItemsFixedAsset extends SwingWorker<ItemsFixedAssetTableModel, ItemsFixedAsset> {

        private ItemsFixedAssetTableModel model;
        private Exception exception;
        private List<ItemsFixedAsset> data;

        public ImportItemsFixedAsset(List<ItemsFixedAsset> data) {
            this.model = (ItemsFixedAssetTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsFixedAsset> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsFixedAsset land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-E " + land.getItemName());
                model.add(land);
            }
        }

        private String getCompleteItemsName(ArrayList<ItemsCategory> iLookUp) {
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

        private String getLastItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = "";

            if (!iLookUp.isEmpty()) {
                itemsName = iLookUp.get(iLookUp.size() - 1).getCategoryName();
            }

            return itemsName;
        }

        @Override
        protected ItemsFixedAssetTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {

                    ItemsFixedAsset ifa = data.get(0);
                    Unit ifaUnit = ifa.getUnit();

                    if (ifaUnit != null) {
                        table.clear();
                        logic.deleteItemsFixedAsset(mainframe.getSession(), ifaUnit);
                    }

                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsFixedAsset fixedAsset = data.get(i);

                        fixedAsset.setUserName(userAccount.getUserName());
                        fixedAsset.setLastUserName(userAccount.getUserName());

                        synchronized (fixedAsset) {
                            fixedAsset = logic.insertItemsFixedAsset(mainframe.getSession(), fixedAsset);
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(fixedAsset.getItemCode());
                        category.setCategoryName(fixedAsset.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        fixedAsset.setItemName(getLastItemsName(iLookUp));
                        fixedAsset.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(fixedAsset);
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
                JXErrorPane.showDialog(ItemsFixedAssetPanel.this, info);
            }

            table.packAll();
            
            mainframe.setCursor(oldCursor);

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
