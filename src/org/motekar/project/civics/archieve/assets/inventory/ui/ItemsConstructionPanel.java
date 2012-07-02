package org.motekar.project.civics.archieve.assets.inventory.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import org.jdesktop.swingx.JXDatePicker;
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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsConstructionJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsConstructionWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsConstructionWorkbookReader;
import org.motekar.project.civics.archieve.utils.misc.CustomFilterPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterCardPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterValue;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
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
public class ItemsConstructionPanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

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
    private JCommandButton btChooseItemsLand = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXDatePicker fieldDocumentDate = new JXDatePicker();
    private RadioButtonPanel radioBuildCategory = new RadioButtonPanel(new String[]{"Permanen", "Semi Permanen", "Darurat"}, 0, 5);
    private RadioButtonPanel radioRaised = new RadioButtonPanel(new String[]{"Bertingkat", "Tidak"}, 0, 5);
    private RadioButtonPanel radioFrameworks = new RadioButtonPanel(new String[]{"Beton", "Tidak"}, 0, 5);
    private JXFormattedTextField fieldBuildingWide;
    private RadioButtonPanel radioWorkType = new RadioButtonPanel(new String[]{"Swakelola", "Pihak Ke III"}, 0, 5);
    private JXTextField fieldAddressLocation = new JXTextField();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXDatePicker fieldStartDate = new JXDatePicker();
    private JXTextField fieldLandStatus = new JXTextField();
    private JXTextField fieldLandCode = new JXTextField();
    private JXFormattedTextField fieldPrice;
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private ItemsConstructionTable table = new ItemsConstructionTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsConstruction worker;
    private ProgressListener progressListener;
    private ItemsConstruction selectedItemsConstruction = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportItemsConstruction importWorker;
    //
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    private ItemsCategory selectedItemsCategory = null;
    private EventList<Region> urbanRegions;
    //
    private ItemsCategory parentCategory = null;
    private ItemsCategory landParentCategory = null;
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    private List<Region> regionMaster = new ArrayList<Region>();
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor;

    public ItemsConstructionPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.userAccount = mainframe.getUserAccount();
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        this.filterPanel = new CustomFilterPanel(mainframe, createFilterValues(), true, true);
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
        loadComboUrban();
        loadComboSubUrban();
        loadMasterRegion();
        loadParentCategory();

        pbar.setVisible(false);

        comboUrban.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadComboSubUrban();
                }
            }
        });

        fieldStartDate.setFormats("dd/MM/yyyy");
        fieldDocumentDate.setFormats("dd/MM/yyyy");

        fieldDescription.setLineWrap(true);
        fieldDescription.setWrapStyleWord(true);

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
                    ArrayList<ItemsConstruction> constructions = table.getSelectedItemsConstructions();
                    if (!constructions.isEmpty()) {
                        if (constructions.size() == 1) {
                            selectedItemsConstruction = constructions.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsConstruction.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsConstruction.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsConstruction = null;
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
                        ItemsCategory.ITEMS_CONSTRUCTION, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.toString());
                }
            }
        });
        btChooseItemsLand.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg dlg = new ItemsCategoryChooserDlg(mainframe, mainframe.getSession(), mainframe.getConnection(),
                        ItemsCategory.ITEMS_LAND, landParentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    ItemsCategory category = dlg.getSelectedItemsCategory();
                    fieldLandCode.setText(category.getCategoryCode());
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
                    CustomOptionDialog.showDialog(ItemsConstructionPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        fieldItems.setEditable(false);
        fieldLandCode.setEditable(false);

        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
    }

    private void loadMasterRegion() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    return mLogic.getRegion(mainframe.getSession());
                }
            };
            lw.execute();
            regionMaster = lw.get();

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadParentCategory() {
        try {
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_CONSTRUCTION);
            landParentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_LAND);
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

    private void loadComboUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    return mLogic.getUrbanRegion(mainframe.getSession());
                }
            };
            lw.execute();
            final EventList<Region> regions = GlazedLists.eventList(lw.get());

            filterPanel.loadComboUrban(regions);

            regions.add(0, new Region());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUrban, regions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboSubUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    Object obj = comboUrban.getSelectedItem();
                    Region pr = null;
                    if (obj instanceof Region) {
                        pr = (Region) obj;
                    }

                    ArrayList<Region> regions = new ArrayList<Region>();

                    if (pr != null) {
                        regions = mLogic.getSubUrbanRegion(mainframe.getSession(), pr);

                    }
                    return regions;
                }
            };
            lw.execute();
            urbanRegions = GlazedLists.eventList(lw.get());

            filterPanel.loadComboSubUrban(urbanRegions);

            urbanRegions.add(0, new Region());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboSubUrban, urbanRegions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void reloadComboSubUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    Object obj = comboUrban.getSelectedItem();
                    Region pr = null;
                    if (obj instanceof Region) {
                        pr = (Region) obj;
                    }

                    ArrayList<Region> regions = new ArrayList<Region>();

                    if (pr != null) {
                        regions = mLogic.getSubUrbanRegion(mainframe.getSession(), pr);

                    }
                    return regions;
                }
            };
            lw.execute();
            comboSubUrban.getEditor().setItem(null);
            urbanRegions.clear();
            EventList<Region> evtRegion = GlazedLists.eventList(lw.get());
            urbanRegions.addAll(evtRegion);
            urbanRegions.add(0, new Region());
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
        amountDisplayFormat.setMinimumIntegerDigits(1);
        amountDisplayFormat.setMaximumIntegerDigits(100000);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldBuildingWide = new JXFormattedTextField();
        fieldBuildingWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldBuildingWide.setHorizontalAlignment(JLabel.LEFT);

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);
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

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Pilih Bidang / Sub Bidang Barang");

        btChooseItemsLand.setActionRichTooltip(addTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btChooseItemsLand);

        return buttonStrip;
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private Component createInputPanel() {

        FormLayout lm = new FormLayout(
                "300px,right:pref,10px,pref,5px,100px,5px,fill:default:grow,300px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
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

        builder.addLabel("No. Dokumen", cc.xy(2, 12));
        builder.add(fieldDocumentNumber, cc.xyw(4, 12, 5));

        builder.addLabel("Tgl. Dokumen", cc.xy(2, 14));
        builder.add(fieldDocumentDate, cc.xyw(4, 14, 3));

        builder.addLabel("Kategori Bangunan", cc.xy(2, 16));
        builder.add(radioBuildCategory, cc.xyw(4, 16, 5));

        builder.addLabel("Bangunan Tingkat", cc.xy(2, 18));
        builder.add(radioRaised, cc.xyw(4, 18, 5));

        builder.addLabel("Beton", cc.xy(2, 20));
        builder.add(radioFrameworks, cc.xyw(4, 20, 5));

        builder.addLabel("Luas Lantai (m2)", cc.xy(2, 22));
        builder.add(fieldBuildingWide, cc.xyw(4, 22, 5));

        builder.addLabel("Jenis Pengerjaan", cc.xy(2, 24));
        builder.add(radioWorkType, cc.xyw(4, 24, 5));

        builder.addLabel("Nilai Kontrak (Rp.)", cc.xy(2, 26));
        builder.add(fieldPrice, cc.xyw(4, 26, 5));

        builder.addLabel("Letak/Lokasi/Alamat", cc.xy(2, 28));
        builder.add(fieldAddressLocation, cc.xyw(4, 28, 5));

        builder.addLabel("Kecamatan", cc.xy(2, 30));
        builder.add(comboUrban, cc.xyw(4, 30, 5));

        builder.addLabel("Kelurahan", cc.xy(2, 32));
        builder.add(comboSubUrban, cc.xyw(4, 32, 5));

        builder.addLabel("Tgl. Mulai Pengerjaan", cc.xy(2, 34));
        builder.add(fieldStartDate, cc.xyw(4, 34, 3));

        builder.addLabel("Status Tanah", cc.xy(2, 36));
        builder.add(fieldLandStatus, cc.xyw(4, 36, 5));

        builder.addLabel("Nomor Kode Tanah", cc.xy(2, 38));
        builder.add(createStrip2(1.0, 1.0), cc.xyw(4, 38, 1));
        builder.add(fieldLandCode, cc.xyw(6, 38, 3));

        builder.addLabel("Asal-Usul Pembiayaan", cc.xy(2, 40));
        builder.add(fieldFundingSource, cc.xyw(4, 40, 5));

        builder.addLabel("Keterangan", cc.xy(2, 42));
        builder.add(scPane, cc.xywh(4, 42, 5, 2));

        builder.addSeparator("", cc.xyw(1, 46, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 47, 9));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-F");

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
        fieldDocumentNumber.setText("");
        fieldDocumentDate.setDate(null);
        radioBuildCategory.setAllDeselected();
        radioRaised.setAllDeselected();
        radioFrameworks.setAllDeselected();
        radioWorkType.setAllDeselected();
        fieldBuildingWide.setValue(BigDecimal.ZERO);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldAddressLocation.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldStartDate.setDate(null);
        fieldLandStatus.setText("");
        fieldLandCode.setText("");
        fieldFundingSource.setText("");
        fieldDescription.setText("");

    }

    private void setFormValues() {
        if (selectedItemsConstruction != null) {
            try {
                if (selectedItemsConstruction.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsConstruction.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsConstruction.getItemCode());
                category.setCategoryName(selectedItemsConstruction.getItemName());
                category.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsConstruction.getRegNumber());

                fieldDocumentNumber.setText(selectedItemsConstruction.getDocumentNumber());
                fieldDocumentDate.setDate(selectedItemsConstruction.getDocumentDate());
                radioBuildCategory.setSelectedData(selectedItemsConstruction.getBuildingCategory());

                radioRaised.setSelectedData(selectedItemsConstruction.getRaised());

                radioWorkType.setSelectedData(selectedItemsConstruction.getWorkType());
                radioFrameworks.setSelectedData(selectedItemsConstruction.getFrameWork());

                fieldBuildingWide.setValue(selectedItemsConstruction.getBuildingWide());
                fieldPrice.setValue(selectedItemsConstruction.getPrice());
                fieldAddressLocation.setText(selectedItemsConstruction.getAddressLocation());

                if (selectedItemsConstruction.getUrban() == null) {
                    comboUrban.getEditor().setItem(null);
                } else {
                    comboUrban.setSelectedItem(selectedItemsConstruction.getUrban());
                }

                if (selectedItemsConstruction.getSubUrban() == null) {
                    comboSubUrban.getEditor().setItem(null);
                } else {
                    comboSubUrban.setSelectedItem(selectedItemsConstruction.getSubUrban());
                }

                fieldStartDate.setDate(selectedItemsConstruction.getStartDate());
                fieldLandStatus.setText(selectedItemsConstruction.getLandStatus());
                fieldLandCode.setText(selectedItemsConstruction.getLandCode());

                fieldFundingSource.setText(selectedItemsConstruction.getFundingSource());
                fieldDescription.setText(selectedItemsConstruction.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsConstruction getItemsConstruction() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Unit unit = null;
        Object unitObj = comboUnit.getSelectedItem();
        if (unitObj instanceof Unit) {
            unit = (Unit) unitObj;
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
        String documentNumber = fieldDocumentNumber.getText();
        Date documentDate = fieldDocumentDate.getDate();

        String buildingCategory = radioBuildCategory.getSelectedData();
        String raised = radioRaised.getSelectedData();

        String frameWork = radioFrameworks.getSelectedData();

        Object objWide = fieldBuildingWide.getValue();
        BigDecimal buildingWide = BigDecimal.ZERO;

        if (objWide instanceof Long) {
            Long value = (Long) objWide;
            buildingWide = BigDecimal.valueOf(value.intValue());
        } else if (objWide instanceof BigDecimal) {
            buildingWide = (BigDecimal) objWide;
        }

        String workType = radioWorkType.getSelectedData();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        String addressLocation = fieldAddressLocation.getText();

        Region urban = null;
        Object objUrban = comboUrban.getSelectedItem();
        if (objUrban instanceof Region) {
            urban = (Region) objUrban;
        }

        if (urban != null) {
            if (urban.getIndex().equals(Long.valueOf(0))) {
                urban = null;
            }
        }

        Region subUrban = null;
        Object objSuburban = comboSubUrban.getSelectedItem();
        if (objSuburban instanceof Region) {
            subUrban = (Region) objSuburban;
        }

        if (subUrban != null) {
            if (subUrban.getIndex().equals(Long.valueOf(0))) {
                subUrban = null;
            }
        }

        Date startDate = fieldStartDate.getDate();

        String landStatus = fieldLandStatus.getText();
        String landCode = fieldLandCode.getText();

        String fundingSource = fieldFundingSource.getText();
        String description = fieldDescription.getText();


        if (itemCode.equals("") || itemName.equals("")) {
            errorString.append("<br>- Kode / Nama Barang Harus Lengkap</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan : " + errorString.toString() + "</html>");
        }

        ItemsConstruction construction = new ItemsConstruction();
        construction.setUnit(unit);
        construction.setItemCode(itemCode);
        construction.setItemName(itemName);
        construction.setRegNumber(regNumber);
        construction.setDocumentNumber(documentNumber);
        construction.setDocumentDate(documentDate);
        construction.setBuildingCategory(buildingCategory);
        construction.setBuildingWide(buildingWide);
        construction.setRaised(raised);
        construction.setFrameWork(frameWork);
        construction.setWorkType(workType);
        construction.setPrice(price);
        construction.setAddressLocation(addressLocation);
        construction.setUrban(urban);
        construction.setSubUrban(subUrban);
        construction.setStartDate(startDate);
        construction.setLandStatus(landStatus);
        construction.setLandCode(landCode);
        construction.setFundingSource(fundingSource);
        construction.setDescription(description);

        if (selectedItemsConstruction != null && btPanel.isEditstate()) {
            construction.setUserName(selectedItemsConstruction.getUserName());
        } else {
            construction.setUserName(userAccount.getUserName());
        }
        construction.setLastUserName(userAccount.getUserName());

        return construction;
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
                ArrayList<ItemsConstruction> selectedItemsConstructions = table.getSelectedItemsConstructions();
                if (!selectedItemsConstructions.isEmpty()) {
                    logic.deleteItemsConstruction(mainframe.getSession(), selectedItemsConstructions);
                    table.removeItemsConstruction(selectedItemsConstructions);
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
            ItemsConstruction newItemsConstruction = getItemsConstruction();
            if (btPanel.isNewstate()) {
                newItemsConstruction = logic.insertItemsConstruction(mainframe.getSession(), newItemsConstruction);
                table.addItemsConstruction(newItemsConstruction);
                selectedItemsConstruction = newItemsConstruction;
                selectedItemsConstruction.setItemName(getLastItemsName());
                selectedItemsConstruction.setFullItemName(getCompleteItemsName());
            } else if (btPanel.isEditstate()) {
                newItemsConstruction = logic.updateItemsConstruction(mainframe.getSession(), selectedItemsConstruction, newItemsConstruction);
                table.updateSelectedItemsConstruction(newItemsConstruction);
                selectedItemsConstruction = newItemsConstruction;
                selectedItemsConstruction.setItemName(getLastItemsName());
                selectedItemsConstruction.setFullItemName(getCompleteItemsName());
            }
            btPanel.setStateFalse();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
            statusLabel.setText("Ready");
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
            table.packAll();
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
            ItemsConstructionJasper report = new ItemsConstructionJasper("Buku Inventaris Barang Konstruksi dalam Pengerjaan (KIB-F)", table.getItemsConstructions(), unit);
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

        chooser.setSelectedFile(new File(ItemsConstructionWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsConstruction> constructions = table.getItemsConstructions();

            ItemsConstructionWorkbookCreator wb = new ItemsConstructionWorkbookCreator(mainframe, file.getPath(), constructions, unitMaster);
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
        filterValues.add(new FilterValue("Nomor Dokumen", "documentnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Tanggal Dokumen", "documentdate", FilterCardPanel.DATE_PANEL));

        ArrayList<String> comboCategoryVal = new ArrayList<String>();
        comboCategoryVal.add("Permanen");
        comboCategoryVal.add("Semi Permanen");
        comboCategoryVal.add("Darurat");

        ArrayList<String> twoState = new ArrayList<String>();
        twoState.add("Ya");
        twoState.add("Tidak");

        filterValues.add(new FilterValue("Kategori Bangunan", "buildingcategory", FilterCardPanel.COMBO_PANEL, comboCategoryVal, null));
        filterValues.add(new FilterValue("Bertingkat", "israised", FilterCardPanel.COMBO_PANEL, twoState, null));
        filterValues.add(new FilterValue("Beton", "framework", FilterCardPanel.COMBO_PANEL, twoState, null));
        filterValues.add(new FilterValue("Luas Lantai", "buildingwide", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Letak/Lokasi/Alamat", "addresslocation", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Tanggal Mulai", "startdate", FilterCardPanel.DATE_PANEL));
        filterValues.add(new FilterValue("Status Tanah", "landstatus", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Kode Tanah", "landcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal-Usul Pembiayaan", "fundingsources", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nilai Kontrak", "price", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
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
                ItemsConstructionWorkbookReader wbr = new ItemsConstructionWorkbookReader();
                List<ItemsConstruction> items = wbr.loadXLSFile(file.getPath());

                Comparator<Unit> comparator = new Comparator<Unit>() {

                    public int compare(Unit o1, Unit o2) {
                        return o1.getUnitCode().compareTo(o2.getUnitCode());
                    }
                };

                Comparator<Region> comparator2 = new Comparator<Region>() {

                    public int compare(Region o1, Region o2) {
                        return o1.getRegionName().compareTo(o2.getRegionName());
                    }
                };

                Comparator<ItemsCategory> comparator3 = new Comparator<ItemsCategory>() {

                    public int compare(ItemsCategory o1, ItemsCategory o2) {
                        return o1.getCategoryCode().compareTo(o2.getCategoryCode());
                    }
                };

                if (!unitMaster.isEmpty()) {
                    Collections.sort(unitMaster, comparator);
                }

                if (!regionMaster.isEmpty()) {
                    Collections.sort(regionMaster, comparator2);
                }

                if (!items.isEmpty()) {
                    for (ItemsConstruction item : items) {
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

                        String address = item.getAddressLocation();
                        if (address != null) {
                            if (!address.equals("")) {
                                int index = Collections.binarySearch(regionMaster, new Region(address), comparator2);
                                if (index >= 0) {
                                    Region r = regionMaster.get(index);
                                    if (r.getRegionType() == Region.TYPE_SUB_URBAN
                                            || r.getRegionType() == Region.TYPE_SUB_URBAN_2) {
                                        item.setSubUrban(r);
                                        item.setUrban(null);
                                    } else {
                                        item.setUrban(r);
                                        item.setSubUrban(null);
                                    }
                                    item.setAddressLocation("");
                                }
                            }
                        }

                        String buildingCategory = item.getBuildingCategory();

                        if (buildingCategory.equalsIgnoreCase("P")) {
                            item.setBuildingCategory("Semi Permanen");
                        } else if (buildingCategory.equalsIgnoreCase("SP")) {
                            item.setBuildingCategory("Permanen");
                        } else if (buildingCategory.equalsIgnoreCase("D")) {
                            item.setBuildingCategory("Darurat");
                        }
                    }

                    if (importWorker != null) {
                        importWorker.cancel(true);
                    }

                    oldCursor = mainframe.getCursor();
                    mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    importWorker = new ImportItemsConstruction(items);
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

        chooser.setSelectedFile(new File(ItemsConstructionWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            
            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            File file = chooser.getSelectedFile();
            ItemsConstructionWorkbookCreator wb = new ItemsConstructionWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsConstruction>(), unitMaster);
            wb.createXLSFile();
            
            mainframe.setCursor(oldCursor);
        }
    }

    private class ItemsConstructionTable extends JXTable {

        private ItemsConstructionTableModel model;

        public ItemsConstructionTable() {
            model = new ItemsConstructionTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            ColumnGroup group2 = new ColumnGroup("<html><center><b>Konstruksi<br>Bangunan</br></b></center>");
            group2.add(colModel.getColumn(6));
            group2.add(colModel.getColumn(7));

            ColumnGroup group3 = new ColumnGroup("<html><center><b>Dokumen</b></center>");
            group3.add(colModel.getColumn(10));
            group3.add(colModel.getColumn(11));

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

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadItemsConstruction(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsConstruction getSelectedItemsConstruction() {
            ArrayList<ItemsConstruction> selectedItemsConstructions = getSelectedItemsConstructions();
            return selectedItemsConstructions.get(0);
        }

        public ArrayList<ItemsConstruction> getItemsConstructions() {
            return model.getItemsConstructions();
        }

        public ArrayList<ItemsConstruction> getSelectedItemsConstructions() {

            ArrayList<ItemsConstruction> constructions = new ArrayList<ItemsConstruction>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsConstruction construction = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsConstruction) {
                            construction = (ItemsConstruction) obj;
                            constructions.add(construction);
                        }
                    }
                }
            }

            return constructions;
        }

        public void updateSelectedItemsConstruction(ItemsConstruction construction) {
            model.updateRow(getSelectedItemsConstruction(), construction);
        }

        public void removeItemsConstruction(ArrayList<ItemsConstruction> constructions) {
            if (!constructions.isEmpty()) {
                for (ItemsConstruction construction : constructions) {
                    model.remove(construction);
                }
            }

        }

        public void addItemsConstruction(ArrayList<ItemsConstruction> constructions) {
            if (!constructions.isEmpty()) {
                for (ItemsConstruction construction : constructions) {
                    model.add(construction);
                }
            }
        }

        public void addItemsConstruction(ItemsConstruction construction) {
            model.add(construction);
        }

        public void insertEmptyItemsConstruction() {
            addItemsConstruction(new ItemsConstruction());
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
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(Integer.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(0);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsConstructionTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 20;
        private static final String[] COLUMN = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Kategori Bangunan<br>(P,SP,D)</br></b></center>",
            "<html><b>Bertingkat</b>", "<html><b>Beton</b>",
            "<html><center><b>Luas Lantai<br>(M<sup>2</sup>)</br></b></center>",
            "<html><center><b>Letak/Lokasi/<br>Alamat</br></b></center>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><center><b>Tgl,Bln,<br>Tahun Mulai</br></b></center>",
            "<html><center><b>Status<br>Tanah</br></b></center>",
            "<html><center><b>Nomor Kode<br>Tanah</br></b></center>",
            "<html><center><b>Asal-Usul<br>Pembiayaan</br></b></center>",
            "<html><center><b>Nilai Kontrak<br>(Rp.)</br></b></center>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsConstruction> constructions = new ArrayList<ItemsConstruction>();

        public ItemsConstructionTableModel() {
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
            } else if (columnIndex == 8 || columnIndex == 16) {
                return BigDecimal.class;
            } else if (columnIndex == 10 || columnIndex == 12) {
                return Date.class;
            } else if (columnIndex == 1) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN[column];
        }

        public void add(ArrayList<ItemsConstruction> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            constructions.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsConstruction construction) {
            insertRow(getRowCount(), construction);
        }

        public void insertRow(int row, ItemsConstruction construction) {
            constructions.add(row, construction);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsConstruction oldItemsConstruction, ItemsConstruction newItemsConstruction) {
            int index = constructions.indexOf(oldItemsConstruction);
            constructions.set(index, newItemsConstruction);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsConstruction construction) {
            int row = constructions.indexOf(construction);
            constructions.remove(construction);
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
                constructions.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                constructions.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            constructions.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (constructions.get(i) == null) {
                    constructions.set(i, new ItemsConstruction());
                }
            }
        }

        public ArrayList<ItemsConstruction> getItemsConstructions() {
            return constructions;
        }

        @Override
        public int getRowCount() {
            return constructions.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsConstruction getItemsConstruction(int row) {
            if (!constructions.isEmpty()) {
                return constructions.get(row);
            }
            return new ItemsConstruction();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsConstruction construction = getItemsConstruction(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        construction.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsConstruction) {
                        construction = (ItemsConstruction) aValue;
                    }
                    break;
                case 3:
                    construction.setItemCode((String) aValue);
                    break;
                case 4:
                    construction.setRegNumber((String) aValue);
                    break;
                case 5:
                    construction.setBuildingCategory((String) aValue);
                    break;
                case 6:
                    construction.setRaised((String) aValue);
                    break;
                case 7:
                    construction.setFrameWork((String) aValue);
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        construction.setBuildingWide((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    construction.setAddressLocation((String) aValue);
                    break;
                case 10:
                    construction.setDocumentDate((Date) aValue);
                    break;
                case 11:
                    construction.setDocumentNumber((String) aValue);
                    break;
                case 12:
                    construction.setStartDate((Date) aValue);
                    break;
                case 13:
                    construction.setLandStatus((String) aValue);
                    break;
                case 14:
                    construction.setLandCode((String) aValue);
                    break;
                case 15:
                    construction.setFundingSource((String) aValue);
                    break;
                case 16:
                    if (aValue instanceof BigDecimal) {
                        construction.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 17:
                    construction.setDescription((String) aValue);
                    break;
                case 18:
                    construction.setUserName((String) aValue);
                    break;
                case 19:
                    construction.setLastUserName((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsConstruction construction = getItemsConstruction(row);
            switch (column) {
                case 0:
                    toBeDisplayed = construction.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = construction;
                    break;
                case 3:
                    toBeDisplayed = construction.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = construction.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = construction.getBuildingCategory();
                    break;
                case 6:
                    toBeDisplayed = construction.getRaised();
                    break;
                case 7:
                    toBeDisplayed = construction.getFrameWork();
                    break;
                case 8:
                    toBeDisplayed = construction.getBuildingWide();
                    break;
                case 9:
                    StringBuilder builder = new StringBuilder();
                    builder.append(construction.getAddressLocation());

                    if (construction.getUrban() != null) {
                        Region urban = construction.getUrban();
                        builder.append(" Kecamatan ").append(urban.getRegionName());
                    }

                    if (construction.getSubUrban() != null) {
                        Region subUrban = construction.getSubUrban();
                        builder.append(" Kelurahan ").append(subUrban.getRegionName());
                    }

                    toBeDisplayed = builder.toString();
                    break;
                case 10:
                    toBeDisplayed = construction.getDocumentDate();
                    break;
                case 11:
                    toBeDisplayed = construction.getDocumentNumber();
                    break;
                case 12:
                    toBeDisplayed = construction.getStartDate();
                    break;
                case 13:
                    toBeDisplayed = construction.getLandStatus();
                    break;
                case 14:
                    toBeDisplayed = construction.getLandCode();
                    break;
                case 15:
                    toBeDisplayed = construction.getFundingSource();
                    break;
                case 16:
                    toBeDisplayed = construction.getPrice();
                    break;
                case 17:
                    toBeDisplayed = construction.getDescription();
                    break;
                case 18:
                    toBeDisplayed = construction.getUserName();
                    break;
                case 19:
                    toBeDisplayed = construction.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsConstruction extends SwingWorker<ItemsConstructionTableModel, ItemsConstruction> {

        private ItemsConstructionTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsConstruction(String modifier) {
            this.model = (ItemsConstructionTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsConstruction> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsConstruction construction : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-F " + construction.getItemName());
                model.add(construction);
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
        protected ItemsConstructionTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsConstruction> constructions = logic.getItemsConstruction(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!constructions.isEmpty()) {
                    for (int i = 0; i < constructions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / constructions.size();

                        ItemsConstruction construction = constructions.get(i);

                        construction.setUserName(userAccount.getUserName());
                        construction.setLastUserName(userAccount.getUserName());

                        if (construction.getUnit() != null) {
                            construction.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), construction.getUnit()));
                        }

                        if (construction.getUrban() != null) {
                            construction.setUrban(mLogic.getRegion(mainframe.getSession(), construction.getUrban()));
                        }

                        if (construction.getSubUrban() != null) {
                            construction.setSubUrban(mLogic.getRegion(mainframe.getSession(), construction.getSubUrban()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(construction.getItemCode());
                        category.setCategoryName(construction.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        construction.setItemName(getLastItemsName(iLookUp));
                        construction.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(construction);
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
                JXErrorPane.showDialog(ItemsConstructionPanel.this, info);
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

    private class ImportItemsConstruction extends SwingWorker<ItemsConstructionTableModel, ItemsConstruction> {

        private ItemsConstructionTableModel model;
        private Exception exception;
        private List<ItemsConstruction> data;

        public ImportItemsConstruction(List<ItemsConstruction> data) {
            this.model = (ItemsConstructionTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsConstruction> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsConstruction land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-F " + land.getItemName());
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
        protected ItemsConstructionTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {

                    ItemsConstruction ic = data.get(0);
                    Unit icUnit = ic.getUnit();

                    if (icUnit != null) {
                        table.clear();
                        logic.deleteItemsConstruction(mainframe.getSession(), icUnit);
                    }

                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsConstruction construction = data.get(i);

                        synchronized (construction) {
                            construction = logic.insertItemsConstruction(mainframe.getSession(), construction);
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(construction.getItemCode());
                        category.setCategoryName(construction.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        construction.setItemName(getLastItemsName(iLookUp));
                        construction.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(construction);
                        Thread.sleep(100L);
                    }
                }
                return model;
            } catch (Exception anyException) {
                Exceptions.printStackTrace(anyException);
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
                JXErrorPane.showDialog(ItemsConstructionPanel.this, info);
            }
            table.packAll();
            mainframe.setCursor(oldCursor);
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
