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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsMachineJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsMachineWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsMachineWorkbookReader;
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
public class ItemsMachinePanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

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
    private JXTextField fieldBPKBNumber = new JXTextField();
    private JXTextField fieldMachineType = new JXTextField();
    private JXFormattedTextField fieldVolume;
    private JXTextField fieldFactoryNumber = new JXTextField();
    private JXTextField fieldFabricNumber = new JXTextField();
    private JXTextField fieldMachineNumber = new JXTextField();
    private JXTextField fieldPoliceNumber = new JXTextField();
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JCheckBox checkUnknown = new JCheckBox("Tidak Diketahui");
    private JYearChooser fieldYearBuilt = new JYearChooser();
    private JCheckBox checkUnknown2 = new JCheckBox("Tidak Diketahui");
    private JXTextField fieldLocation = new JXTextField();
    private JXTextField fieldPresentLocation = new JXTextField();
    private JXTextField fieldMaterial = new JXTextField();
    private JXComboBox comboCondition = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JXFormattedTextField fieldAdminCost;
    private JXFormattedTextField fieldTotalPrice;
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private ItemsMachineTable table = new ItemsMachineTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsMachine worker;
    private ProgressListener progressListener;
    private ItemsMachine selectedItemsMachine = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ItemsCategory selectedItemsCategory = null;
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    private ItemsCategory parentCategory = null;
    //
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    private ImportItemsMachine importWorker;
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor;

    public ItemsMachinePanel(ArchieveMainframe mainframe) {
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
        btLoadPanel.addListener(this);
        btPrintPanel.addListener(this);

        btPanel.mergeWith(btLoadPanel);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(createInputPanel(), "1");

        btFilter.setEnabled(filterPanel.isFiltered());

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ItemsMachine> machines = table.getSelectedItemsMachines();
                    if (!machines.isEmpty()) {
                        if (machines.size() == 1) {
                            selectedItemsMachine = machines.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsMachine.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsMachine.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_MACHINE);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsMachine = null;
                            selectedItemsCategory = null;
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
                        ItemsCategory.ITEMS_MACHINE, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.toString());
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

        btFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String modifier = filterPanel.getModifier();
                    table.loadData(modifier);
                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(ItemsMachinePanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fieldPrice.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        fieldPrice.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }

            @Override
            public void focusLost(FocusEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        fieldAdminCost.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        fieldAdminCost.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }

            @Override
            public void focusLost(FocusEvent e) {
                fieldTotalPrice.setValue(calculateTotalPrice());
            }
        });

        checkUnknown.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
            }
        });

        checkUnknown.setFocusPainted(false);

        checkUnknown2.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldYearBuilt.setEnabled(!checkUnknown2.isSelected());
            }
        });

        checkUnknown2.setFocusPainted(false);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        fieldItems.setEditable(false);
    }

    private BigDecimal calculateTotalPrice() {
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

        Object objAdminCost = fieldAdminCost.getValue();
        BigDecimal adminCost = BigDecimal.ZERO;

        if (objAdminCost instanceof Long) {
            Long value = (Long) objAdminCost;
            if (value != null) {
                adminCost = BigDecimal.valueOf(value.intValue());
            }
        } else if (objAdminCost instanceof BigDecimal) {
            if (objAdminCost != null) {
                adminCost = (BigDecimal) objAdminCost;
            }
        }

        BigDecimal total = BigDecimal.valueOf(0);
        total = total.add(price);
        total = total.add(adminCost);
        return total;
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
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_MACHINE);
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

        fieldVolume = new JXFormattedTextField();
        fieldVolume.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldVolume.setHorizontalAlignment(JLabel.LEFT);

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldAdminCost = new JXFormattedTextField();
        fieldAdminCost.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldAdminCost.setHorizontalAlignment(JLabel.RIGHT);

        fieldTotalPrice = new JXFormattedTextField();
        fieldTotalPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldTotalPrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldTotalPrice.setEditable(false);
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
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
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


        builder.addLabel("UPB / Sub UPB", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 5));

        builder.add(itemLabel, cc.xy(2, 8));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 8, 1));
        builder.add(fieldItems, cc.xyw(6, 8, 3));

        builder.addLabel("No. Register", cc.xy(2, 10));
        builder.add(fieldRegNumber, cc.xyw(4, 10, 5));

        builder.addLabel("Merk / Tipe", cc.xy(2, 12));
        builder.add(fieldMachineType, cc.xyw(4, 12, 5));

        builder.addLabel("Ukuran (CC)", cc.xy(2, 14));
        builder.add(fieldVolume, cc.xyw(4, 14, 3));

        builder.addLabel("Bahan", cc.xy(2, 16));
        builder.add(fieldMaterial, cc.xyw(4, 16, 5));

        builder.addLabel("Tahun Pembuatan", cc.xy(2, 18));
        builder.add(fieldYearBuilt, cc.xyw(4, 18, 3));
        builder.add(checkUnknown2, cc.xyw(8, 18, 1));

        builder.addLabel("Tahun Beli", cc.xy(2, 20));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 20, 3));
        builder.add(checkUnknown, cc.xyw(8, 20, 1));


        builder.addLabel("No. Pabrik", cc.xy(2, 22));
        builder.add(fieldFactoryNumber, cc.xyw(4, 22, 5));

        builder.addLabel("No. Rangka", cc.xy(2, 24));
        builder.add(fieldFabricNumber, cc.xyw(4, 24, 5));

        builder.addLabel("No. Mesin", cc.xy(2, 26));
        builder.add(fieldMachineNumber, cc.xyw(4, 26, 5));

        builder.addLabel("No. Polisi", cc.xy(2, 28));
        builder.add(fieldPoliceNumber, cc.xyw(4, 28, 5));

        builder.addLabel("No. BPKB", cc.xy(2, 30));
        builder.add(fieldBPKBNumber, cc.xyw(4, 30, 5));

        builder.addLabel("Lokasi Aset", cc.xy(2, 32));
        builder.add(fieldLocation, cc.xyw(4, 32, 5));

        builder.addLabel("Lokasi Saat Ini", cc.xy(2, 34));
        builder.add(fieldPresentLocation, cc.xyw(4, 34, 5));

        builder.addLabel("Kondisi", cc.xy(2, 36));
        builder.add(comboCondition, cc.xyw(4, 36, 5));

        builder.addLabel("Harga Satuan", cc.xy(2, 38));
        builder.add(fieldPrice, cc.xyw(4, 38, 5));

        builder.addLabel("Honor + Admin", cc.xy(2, 40));
        builder.add(fieldAdminCost, cc.xyw(4, 40, 5));

        builder.addLabel("Jumlah Harga", cc.xy(2, 42));
        builder.add(fieldTotalPrice, cc.xyw(4, 42, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 44));
        builder.add(fieldFundingSource, cc.xyw(4, 44, 5));

        builder.addLabel("Asal-Usul", cc.xy(2, 46));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 46, 5));

        builder.addLabel("Keterangan", cc.xy(2, 48));
        builder.add(scPane, cc.xywh(4, 48, 5, 2));

        builder.addSeparator("", cc.xyw(1, 51, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 52, 9));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-B");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
//        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
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

    private void clearYear2() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldYearBuilt.setYear(year);
        fieldYearBuilt.setEnabled(!checkUnknown2.isSelected());
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
        fieldBPKBNumber.setText("");
        fieldMachineType.setText("");
        fieldVolume.setValue(Integer.valueOf(0));
        fieldFactoryNumber.setText("");
        fieldFabricNumber.setText("");
        fieldMachineNumber.setText("");
        fieldPoliceNumber.setText("");
        checkUnknown.setSelected(false);
        checkUnknown2.setSelected(false);
        clearYear();
        clearYear2();
        fieldLocation.setText("");
        fieldPresentLocation.setText("");
        fieldMaterial.setText("");
        comboCondition.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldAdminCost.setValue(BigDecimal.ZERO);
        fieldTotalPrice.setValue(BigDecimal.ZERO);
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedItemsMachine != null) {
            try {
                if (selectedItemsMachine.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsMachine.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsMachine.getItemCode());
                category.setCategoryName(selectedItemsMachine.getItemName());
                category.setTypes(ItemsCategory.ITEMS_MACHINE);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsMachine.getRegNumber());
                fieldBPKBNumber.setText(selectedItemsMachine.getBpkbNumber());
                fieldMachineType.setText(selectedItemsMachine.getMachineType());
                fieldVolume.setValue(selectedItemsMachine.getVolume());
                fieldFactoryNumber.setText(selectedItemsMachine.getFactoryNumber());
                fieldFabricNumber.setText(selectedItemsMachine.getFabricNumber());
                fieldMachineNumber.setText(selectedItemsMachine.getMachineNumber());
                fieldPoliceNumber.setText(selectedItemsMachine.getPoliceNumber());

                if (selectedItemsMachine.getAcquisitionYear() == null) {
                    checkUnknown.setSelected(true);
                    clearYear();
                } else {
                    checkUnknown.setSelected(false);
                    fieldAcquisitionYear.setYear(selectedItemsMachine.getAcquisitionYear());
                }

                if (selectedItemsMachine.getYearBuilt() == null) {
                    checkUnknown2.setSelected(true);
                    clearYear2();
                } else {
                    checkUnknown2.setSelected(false);
                    fieldYearBuilt.setYear(selectedItemsMachine.getYearBuilt());
                }

                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
                fieldYearBuilt.setEnabled(!checkUnknown2.isSelected());

                fieldLocation.setText(selectedItemsMachine.getLocation());
                fieldPresentLocation.setText(selectedItemsMachine.getPresentLocation());
                fieldMaterial.setText(selectedItemsMachine.getMaterial());

                if (selectedItemsMachine.getCondition() == null) {
                    comboCondition.getEditor().setItem(null);
                } else {
                    comboCondition.setSelectedItem(selectedItemsMachine.getCondition());
                }

                fieldPrice.setValue(selectedItemsMachine.getPrice());
                fieldAdminCost.setValue(selectedItemsMachine.getAdminCost());
                fieldTotalPrice.setValue(selectedItemsMachine.getTotalPrice());
                fieldFundingSource.setText(selectedItemsMachine.getFundingSource());
                fieldAcquisitionWay.setText(selectedItemsMachine.getAcquisitionWay());
                fieldDescription.setText(selectedItemsMachine.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsMachine getItemsMachine() throws MotekarException {
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
        String bpkpNumber = fieldBPKBNumber.getText();
        String machineType = fieldMachineType.getText();

        Object objVol = fieldVolume.getValue();
        Integer volume = Integer.valueOf(0);

        if (objVol instanceof Long) {
            Long value = (Long) objVol;
            volume = Integer.valueOf(value.intValue());
        } else if (objVol instanceof Integer) {
            volume = (Integer) objVol;
        }

        String factoryNumber = fieldFactoryNumber.getText();
        String fabricNumber = fieldFabricNumber.getText();
        String machineNumber = fieldMachineNumber.getText();
        String policeNumber = fieldPoliceNumber.getText();

        Integer acquisitionYear = fieldAcquisitionYear.getYear();

        if (checkUnknown.isSelected()) {
            acquisitionYear = null;
        }

        Integer yearBuilt = fieldYearBuilt.getYear();

        if (checkUnknown2.isSelected()) {
            yearBuilt = null;
        }

        String location = fieldLocation.getText();
        String presentLocation = fieldPresentLocation.getText();
        String material = fieldMaterial.getText();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        Object objAdminCost = fieldAdminCost.getValue();
        BigDecimal adminCost = BigDecimal.ZERO;

        if (objAdminCost instanceof Long) {
            Long value = (Long) objAdminCost;
            adminCost = BigDecimal.valueOf(value.intValue());
        } else if (objAdminCost instanceof BigDecimal) {
            adminCost = (BigDecimal) objAdminCost;
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

        ItemsMachine machine = new ItemsMachine();
        machine.setUnit(unit);
        machine.setItemCode(itemCode);
        machine.setItemName(itemName);
        machine.setRegNumber(regNumber);
        machine.setBpkbNumber(bpkpNumber);
        machine.setMachineType(machineType);
        machine.setVolume(volume);
        machine.setFactoryNumber(factoryNumber);
        machine.setFabricNumber(fabricNumber);
        machine.setMachineNumber(machineNumber);
        machine.setPoliceNumber(policeNumber);
        machine.setAcquisitionYear(acquisitionYear);
        machine.setYearBuilt(yearBuilt);
        machine.setLocation(location);
        machine.setPresentLocation(presentLocation);
        machine.setMaterial(material);
        machine.setCondition(condition);
        machine.setPrice(price);
        machine.setAdminCost(adminCost);
        machine.setFundingSource(fundingSource);
        machine.setAcquisitionWay(acquisitionWay);
        machine.setDescription(description);

        if (selectedItemsMachine != null && btPanel.isEditstate()) {
            machine.setUserName(selectedItemsMachine.getUserName());
        } else {
            machine.setUserName(userAccount.getUserName());
        }
        machine.setLastUserName(userAccount.getUserName());

        return machine;
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
                ArrayList<ItemsMachine> selectedItemsMachines = table.getSelectedItemsMachines();
                if (!selectedItemsMachines.isEmpty()) {
                    logic.deleteItemsMachine(mainframe.getSession(), selectedItemsMachines);
                    table.removeItemsMachine(selectedItemsMachines);
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
            ItemsMachine newItemsMachine = getItemsMachine();
            if (btPanel.isNewstate()) {
                newItemsMachine = logic.insertItemsMachine(mainframe.getSession(), newItemsMachine);
                table.addItemsMachine(newItemsMachine);
                selectedItemsMachine = newItemsMachine;
                selectedItemsMachine.setItemName(getLastItemsName());
                selectedItemsMachine.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newItemsMachine = logic.updateItemsMachine(mainframe.getSession(), selectedItemsMachine, newItemsMachine);
                table.updateSelectedItemsMachine(newItemsMachine);
                selectedItemsMachine = newItemsMachine;
                selectedItemsMachine.setItemName(getLastItemsName());
                selectedItemsMachine.setFullItemName(getCompleteItemsName());
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
                ItemsMachineWorkbookReader wbr = new ItemsMachineWorkbookReader();
                List<ItemsMachine> items = wbr.loadXLSFile(file.getPath());

                Comparator<Unit> comparator = new Comparator<Unit>() {

                    public int compare(Unit o1, Unit o2) {
                        return o1.getUnitCode().compareTo(o2.getUnitCode());
                    }
                };

                if (!unitMaster.isEmpty()) {
                    Collections.sort(unitMaster, comparator);
                }

                if (!items.isEmpty()) {
                    for (ItemsMachine item : items) {
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


                        if (importWorker != null) {
                            importWorker.cancel(true);
                        }

                        oldCursor = mainframe.getCursor();
                        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                        importWorker = new ImportItemsMachine(items);
                        progressListener = new ProgressListener(pbar, statusLabel, importWorker, false);
                        importWorker.addPropertyChangeListener(progressListener);
                        importWorker.execute();
                    }
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

        chooser.setSelectedFile(new File(ItemsMachineWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {

            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            File file = chooser.getSelectedFile();
            ItemsMachineWorkbookCreator wb = new ItemsMachineWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsMachine>(), unitMaster);
            wb.createXLSFile();

            mainframe.setCursor(oldCursor);
        }
    }

    public void onPrint() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Unit unit = filterPanel.getUnit();

        if (unit != null) {
            ItemsMachineJasper report = new ItemsMachineJasper("Buku Inventaris Barang Peralatan dan Mesin (KIB-B)", table.getItemsMachines(), unit);
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

        chooser.setSelectedFile(new File(ItemsMachineWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsMachine> machines = table.getItemsMachines();

            ItemsMachineWorkbookCreator wb = new ItemsMachineWorkbookCreator(mainframe, file.getPath(), machines, unitMaster);
            wb.createXLSFile();
        }
    }

    public void onPrintPDF() throws Exception, CommonException {
        //
    }

    public void onPrintGraph() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ArrayList<FilterValue> createFilterValues() {
        ArrayList<FilterValue> filterValues = new ArrayList<FilterValue>();

        filterValues.add(new FilterValue("Nama Barang", "itemname", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Kode Barang", "itemcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Register", "regnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Merk / Tipe", "machinetype", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Ukuran (CC)", "volume", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Bahan", "material", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Tahun Beli", "acquisitionyear", FilterCardPanel.YEAR_PANEL));
        filterValues.add(new FilterValue("Nomor Pabrik", "factorynumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Rangka", "fabricnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Mesin", "machinenumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Polisi", "policenumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor BPKB", "bpkbnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal-Usul", "fundingsources", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Harga Satuan (Rp.)", "price", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ItemsMachineTable extends JXTable {

        private ItemsMachineTableModel model;

        public ItemsMachineTable() {
            model = new ItemsMachineTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(9));
            group.add(colModel.getColumn(10));
            group.add(colModel.getColumn(11));
            group.add(colModel.getColumn(12));
            group.add(colModel.getColumn(13));

            ColumnGroup group2 = new ColumnGroup("<html><center><b>Harga Perolehan<br>(Rp.)</br></b></center>");
            group2.add(colModel.getColumn(15));
            group2.add(colModel.getColumn(16));
            group2.add(colModel.getColumn(17));

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

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadItemsMachine(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsMachine getSelectedItemsMachine() {
            ArrayList<ItemsMachine> selectedItemsMachines = getSelectedItemsMachines();
            return selectedItemsMachines.get(0);
        }

        public ArrayList<ItemsMachine> getItemsMachines() {
            return model.getItemsMachines();
        }

        public ArrayList<ItemsMachine> getSelectedItemsMachines() {

            ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsMachine machine = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof ItemsMachine) {
                            machine = (ItemsMachine) obj;
                            machines.add(machine);
                        }
                    }
                }
            }

            return machines;
        }

        public void updateSelectedItemsMachine(ItemsMachine machine) {
            model.updateRow(getSelectedItemsMachine(), machine);
        }

        public void removeItemsMachine(ArrayList<ItemsMachine> machines) {
            if (!machines.isEmpty()) {
                for (ItemsMachine machine : machines) {
                    model.remove(machine);
                }
            }

        }

        public void addItemsMachine(ArrayList<ItemsMachine> machines) {
            if (!machines.isEmpty()) {
                for (ItemsMachine machine : machines) {
                    model.add(machine);
                }
            }
        }

        public void addItemsMachine(ItemsMachine machine) {
            model.add(machine);
        }

        public void insertEmptyItemsMachine() {
            addItemsMachine(new ItemsMachine());
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
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsMachineTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 21;
        private static final String[] COLUMN_RELIGION = {"", "<html><b>No Urut</b>",
            "<html><b>Kode Barang</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><center><b>Nomor<br>Register</br></b></center>", "<html><b>Merk/Tipe</b>",
            "<html><center><b>Ukuran<br>(CC)</br></b></center>",
            "<html><b>Bahan</b>",
            "<html><center><b>Tahun<br>Beli</br></b></center>",
            "<html><b>Pabrik</b>", "<html><b>Rangka</b>",
            "<html><b>Mesin</b>", "<html><b>Polisi</b>",
            "<html><b>BPKB</b>", "<html><b>Asal-Usul</b>",
            "<html><b>Satuan</b>", "<html><b>Honor + ADM</b>", "<html><b>Jumlah</b>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        public ItemsMachineTableModel() {
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
            } else if (columnIndex == 15 || columnIndex == 16 || columnIndex == 17) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 6 || columnIndex == 8) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_RELIGION[column];
        }

        public void add(ArrayList<ItemsMachine> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            machines.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsMachine machine) {
            insertRow(getRowCount(), machine);
        }

        public void insertRow(int row, ItemsMachine machine) {
            machines.add(row, machine);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsMachine oldItemsMachine, ItemsMachine newItemsMachine) {
            int index = machines.indexOf(oldItemsMachine);
            machines.set(index, newItemsMachine);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsMachine machine) {
            int row = machines.indexOf(machine);
            machines.remove(machine);
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
                machines.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                machines.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            machines.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (machines.get(i) == null) {
                    machines.set(i, new ItemsMachine());
                }
            }
        }

        public ArrayList<ItemsMachine> getItemsMachines() {
            return machines;
        }

        @Override
        public int getRowCount() {
            return machines.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsMachine getItemsMachine(int row) {
            if (!machines.isEmpty()) {
                return machines.get(row);
            }
            return new ItemsMachine();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsMachine machine = getItemsMachine(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        machine.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    machine.setItemCode((String) aValue);
                    break;
                case 3:
                    if (aValue instanceof ItemsMachine) {
                        machine = (ItemsMachine) aValue;
                    }
                    break;
                case 4:
                    machine.setRegNumber((String) aValue);
                    break;
                case 5:
                    machine.setMachineType((String) aValue);
                    break;
                case 6:
                    machine.setVolume((Integer) aValue);
                    break;
                case 7:
                    machine.setMaterial((String) aValue);
                    break;
                case 8:
                    machine.setAcquisitionYear((Integer) aValue);
                    break;
                case 9:
                    machine.setFactoryNumber((String) aValue);
                    break;
                case 10:
                    machine.setFabricNumber((String) aValue);
                    break;
                case 11:
                    machine.setMachineNumber((String) aValue);
                    break;
                case 12:
                    machine.setPoliceNumber((String) aValue);
                    break;
                case 13:
                    machine.setBpkbNumber((String) aValue);
                    break;
                case 14:
                    machine.setAcquisitionWay((String) aValue);
                    break;
                case 15:
                    if (aValue instanceof BigDecimal) {
                        machine.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 16:
                    if (aValue instanceof BigDecimal) {
                        machine.setAdminCost((BigDecimal) aValue);
                    }
                    break;
                case 17:
                    break;
                case 18:
                    machine.setDescription((String) aValue);
                    break;
                case 19:
                    machine.setUserName((String) aValue);
                    break;
                case 20:
                    machine.setLastUserName((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsMachine machine = getItemsMachine(row);
            switch (column) {
                case 0:
                    toBeDisplayed = machine.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = machine.getItemCode();
                    break;
                case 3:
                    toBeDisplayed = machine;
                    break;
                case 4:
                    toBeDisplayed = machine.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = machine.getMachineType();
                    break;
                case 6:
                    toBeDisplayed = machine.getVolume();
                    break;
                case 7:
                    toBeDisplayed = machine.getMaterial();
                    break;
                case 8:
                    toBeDisplayed = machine.getAcquisitionYear();
                    break;
                case 9:
                    toBeDisplayed = machine.getFactoryNumber();
                    break;
                case 10:
                    toBeDisplayed = machine.getFabricNumber();
                    break;
                case 11:
                    toBeDisplayed = machine.getMachineNumber();
                    break;
                case 12:
                    toBeDisplayed = machine.getPoliceNumber();
                    break;
                case 13:
                    toBeDisplayed = machine.getBpkbNumber();
                    break;
                case 14:
                    toBeDisplayed = machine.getAcquisitionWay();
                    break;
                case 15:
                    toBeDisplayed = machine.getPrice();
                    break;
                case 16:
                    toBeDisplayed = machine.getAdminCost();
                    break;
                case 17:
                    toBeDisplayed = machine.getTotalPrice();
                    break;
                case 18:
                    toBeDisplayed = machine.getDescription();
                    break;
                case 19:
                    toBeDisplayed = machine.getUserName();
                    break;
                case 20:
                    toBeDisplayed = machine.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsMachine extends SwingWorker<ItemsMachineTableModel, ItemsMachine> {

        private ItemsMachineTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsMachine(String modifier) {
            this.model = (ItemsMachineTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsMachine> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsMachine machine : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-B " + machine.getItemName());
                model.add(machine);
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
        protected ItemsMachineTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsMachine> machines = logic.getItemsMachine(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!machines.isEmpty()) {
                    for (int i = 0; i < machines.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / machines.size();

                        ItemsMachine machine = machines.get(i);

                        if (machine.getUnit() != null) {
                            machine.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), machine.getUnit()));
                        }

                        if (machine.getCondition() != null) {
                            machine.setCondition(mLogic.getCondition(mainframe.getSession(), machine.getCondition()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(machine.getItemCode());
                        category.setCategoryName(machine.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_MACHINE);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        machine.setItemName(getLastItemsName(iLookUp));
                        machine.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(machine);
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
                JXErrorPane.showDialog(ItemsMachinePanel.this, info);
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

    private class ImportItemsMachine extends SwingWorker<ItemsMachineTableModel, ItemsMachine> {

        private ItemsMachineTableModel model;
        private Exception exception;
        private List<ItemsMachine> data;

        public ImportItemsMachine(List<ItemsMachine> data) {
            this.model = (ItemsMachineTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsMachine> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsMachine machine : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-B " + machine.getItemName());
                model.add(machine);
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
        protected ItemsMachineTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {

                    ItemsMachine im = data.get(0);
                    Unit imUnit = im.getUnit();

                    if (imUnit != null) {
                        table.clear();
                        synchronized (imUnit) {
                            logic.deleteItemsMachine(mainframe.getSession(), imUnit);
                        }
                    }

                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsMachine machine = data.get(i);

                        machine.setUserName(userAccount.getUserName());
                        machine.setLastUserName(userAccount.getUserName());

                        synchronized (machine) {
                            machine = logic.insertItemsMachine(mainframe.getSession(), machine);
                        }


                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(machine.getItemCode());
                        category.setCategoryName(machine.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_MACHINE);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        machine.setItemName(getLastItemsName(iLookUp));
                        machine.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(machine);
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
                JXErrorPane.showDialog(ItemsMachinePanel.this, info);
            }

            table.packAll();

            mainframe.setCursor(oldCursor);

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
