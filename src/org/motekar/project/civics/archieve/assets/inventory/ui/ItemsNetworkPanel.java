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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsNetworkJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsNetworkWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsNetworkWorkbookReader;
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
public class ItemsNetworkPanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

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
    private JXTextField fieldConstructionType = new JXTextField();
    private JXFormattedTextField fieldLength;
    private JXFormattedTextField fieldWidth;
    private JXFormattedTextField fieldWide;
    private JXFormattedTextField fieldPrice;
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXComboBox comboCondition = new JXComboBox();
    private JXTextField fieldAddressLocation = new JXTextField();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXTextField fieldLandCode = new JXTextField();
    private JXTextField fieldLandStatus = new JXTextField();
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JTabbedPane tabPane = new JTabbedPane();
    // Jalan
    private JYearChooser fieldStreetYearBuild = new JYearChooser();
    private RadioButtonPanel radioStreetLocation = new RadioButtonPanel(new String[]{"Desa", "Kota"}, 0, 2);
    private RadioButtonPanel radioStreetFlatness = new RadioButtonPanel(new String[]{"Datar", "Perbukitan", "Pegunungan"}, 0, 3);
    private JXFormattedTextField fieldStreetStartKM;
    private JXFormattedTextField fieldStreetEndKM;
    private RadioButtonPanel radioStreetWidth = new RadioButtonPanel(new String[]{"< 6 m", "6 - 8,25 m", "8.25 - 11 m", "> 11 m"}, 0, 4);
    private RadioButtonPanel radioStreetSurface = new RadioButtonPanel(new String[]{"Aspal", "Paving Blok", "Tanah", "Beton Bertulang", "Kerikil"}, 0, 5);
    private RadioButtonPanel radioStreetSide = new RadioButtonPanel(new String[]{"Aspal", "Kerikil", "Tanah"}, 0, 3);
    private RadioButtonPanel radioStreetTrotoire = new RadioButtonPanel(new String[]{"Tanah", "Beton Rabat", "Paving Blok"}, 0, 3);
    private RadioButtonPanel radioStreetChannel = new RadioButtonPanel(new String[]{"Dengan Pasangan Batu", "Tidak Ada"}, 0, 2);
    private RadioButtonPanel radioStreetSafetyZone = new RadioButtonPanel(new String[]{"Ada", "Tidak Ada"}, 0, 2);
    // Jembatan
    private RadioButtonPanel radioBridgeStandarType = new RadioButtonPanel(new String[]{"GKI (Kayu)", "PTI 1 (Pelat)",
                "PTI 2 (Pelat Beton Berongga)", "GTI (Beton Bertulang)", "GPI 1 (Beton Pra Tekan)",
                "GPI 2 (Concrete Box)", "GBI (Gelagar/Komposit Indonesia)", "GBA (Gelagar Baja Australia)",
                "GBJ (Gelagar Baja Jepang)", "RBU (Rangka Baja Callender-Hamillton)", "RBB (Rangka Baja Belanda)",
                "RBR/RBS (Rangka Baja Australia)", "RBA (Rangka Baja Australia)", "RBT (Transpanel Australia)",
                "RBW (Balley)", "Tidak Standar"}, 8, 2);
    private JYearChooser fieldBridgeYearBuild = new JYearChooser();
    private JXFormattedTextField fieldBridgeLength;
    private JXFormattedTextField fieldBridgeWidth;
    private RadioButtonPanel radioBridgePurpose = new RadioButtonPanel(new String[]{"Jalan Raya", "Jalan Pipa", "Jalan Air", "Penyebrangan"}, 0, 4);
    private RadioButtonPanel radioBridgeMainMaterial = new RadioButtonPanel(new String[]{"Kayu", "Baja", "Batu", "Beton Bertulang"}, 0, 4);
    private RadioButtonPanel radioBridgeTopShape = new RadioButtonPanel(new String[]{"Balok(Gelagar)", "Pelat", "Rangka",
                "Busur", "Gantung", "Kaku", "Gorong-gorong", "Komposit"}, 2, 4);
    private RadioButtonPanel radioBridgeOtherShape = new RadioButtonPanel(new String[]{"Railing", "Tiang Listrik",
                "Kerb/Trotoar", "Penunjang Saluran"}, 0, 4);
    private RadioButtonPanel radioBridgeHeadShape = new RadioButtonPanel(new String[]{"Cap", "Dinding Penuh"}, 0, 2);
    private RadioButtonPanel radioBridgeHeadMaterial = new RadioButtonPanel(new String[]{"Beton Bertulang", "Pasangan Batu"}, 0, 2);
    private RadioButtonPanel radioBridgePillar = new RadioButtonPanel(new String[]{"Cap", "Dinding Penuh",
                "Satu Kolom", "Dua Kolom", "Tiga Kolom", "Tidak Ada"}, 2, 3);
    private RadioButtonPanel radioBridgePillarMaterial = new RadioButtonPanel(new String[]{"Beton Bertulang", "Pasangan Batu", "Baja"}, 0, 3);
    // Irrigation Main Network
    private JYearChooser fieldIrrigationYearBuild = new JYearChooser();
    private RadioButtonPanel radioIrrigationNetworkType = new RadioButtonPanel(new String[]{"Bangunan Tetap", "Waduk",
                "Bendung Gerak", "Stasiun Pompa"}, 0, 4);
    private JXFormattedTextField fieldIrrigationLength;
    private JXFormattedTextField fieldIrrigationHeight;
    private JXFormattedTextField fieldIrrigationWidth;
    private RadioButtonPanel radioIrrigationNetworkMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationCondition = new JXComboBox();
    // Irrigation Carrier
    private RadioButtonPanel radioIrrigationCarrierType = new RadioButtonPanel(new String[]{"Terjun Tegak", "Terjun Miring",
                "Bendung Gerak", "Stasiun Pompa"}, 0, 4);
    private JXFormattedTextField fieldIrrigationCarrierLength;
    private JXFormattedTextField fieldIrrigationCarrierHeight;
    private JXFormattedTextField fieldIrrigationCarrierWidth;
    private RadioButtonPanel radioIrrigationCarrierMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationCarrierCondition = new JXComboBox();
    // Irrigation Debit
    private JXFormattedTextField fieldIrrigationDebitThresholdWidth;
    private JXFormattedTextField fieldIrrigationDebitCDG;
    private JXFormattedTextField fieldIrrigationDebitCipolleti;
    private JXFormattedTextField fieldIrrigationDebitLength;
    private JXFormattedTextField fieldIrrigationDebitHeight;
    private JXFormattedTextField fieldIrrigationDebitWidth;
    private RadioButtonPanel radioIrrigationDebitMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationDebitCondition = new JXComboBox();
    // Kolam Olak
    private JXFormattedTextField fieldIrrigationPoolUSBR3;
    private JXFormattedTextField fieldIrrigationPoolBlock;
    private JXFormattedTextField fieldIrrigationPoolUSBR4;
    private JXFormattedTextField fieldIrrigationPoolVlogtor;
    private JXFormattedTextField fieldIrrigationPoolLength;
    private JXFormattedTextField fieldIrrigationPoolHeight;
    private JXFormattedTextField fieldIrrigationPoolWidth;
    private RadioButtonPanel radioIrrigationPoolMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationPoolCondition = new JXComboBox();
    // Height Water Front
    private JXFormattedTextField fieldIrrigationHWFLotBlock;
    private JXFormattedTextField fieldIrrigationHWFTrapesium;
    private JXFormattedTextField fieldIrrigationHWFSlide;
    private JXFormattedTextField fieldIrrigationHWFTreeTop;
    private JXFormattedTextField fieldIrrigationHWFLength;
    private JXFormattedTextField fieldIrrigationHWFHeight;
    private JXFormattedTextField fieldIrrigationHWFWidth;
    private RadioButtonPanel radioIrrigationHWFMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationHWFCondition = new JXComboBox();
    // Protection Network
    private JXFormattedTextField fieldIrrigationProtectTransfer;
    private JXFormattedTextField fieldIrrigationProtectDisposal;
    private JXFormattedTextField fieldIrrigationProtectDrain;
    private JXFormattedTextField fieldIrrigationProtectLength;
    private JXFormattedTextField fieldIrrigationProtectHeight;
    private JXFormattedTextField fieldIrrigationProtectWidth;
    private RadioButtonPanel radioIrrigationProtectMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationProtectCondition = new JXComboBox();
    // Bagi dan Sadap
    private JXFormattedTextField fieldIrrigationTappedFor;
    private JXFormattedTextField fieldIrrigationTappedSecond;
    private JXFormattedTextField fieldIrrigationTappedRegulator;
    private JXFormattedTextField fieldIrrigationTappedThird;
    private JXFormattedTextField fieldIrrigationTappedLength;
    private JXFormattedTextField fieldIrrigationTappedHeight;
    private JXFormattedTextField fieldIrrigationTappedWidth;
    private RadioButtonPanel radioIrrigationTappedMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationTappedCondition = new JXComboBox();
    // Support Network
    private JXFormattedTextField fieldIrrigationSupportLevee;
    private JXFormattedTextField fieldIrrigationSupportWash;
    private JXFormattedTextField fieldIrrigationSupportBridge;
    private JXFormattedTextField fieldIrrigationSupportKrib;
    private JXFormattedTextField fieldIrrigationSupportLength;
    private JXFormattedTextField fieldIrrigationSupportHeight;
    private JXFormattedTextField fieldIrrigationSupportWidth;
    private RadioButtonPanel radioIrrigationSupportMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
    private JXComboBox comboIrrigationSupportCondition = new JXComboBox();
    // 
    private ItemsNetworkTable table = new ItemsNetworkTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsNetwork worker;
    private ProgressListener progressListener;
    private ItemsNetwork selectedItemsNetwork = null;
    private ItemsCategory selectedItemsCategory = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportItemsNetwork importWorker;
    //
    private JScrollPane mainSCPane = new JScrollPane();
    private JScrollPane irrSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    private ArrayList<JXTaskPane> taskArrays2 = new ArrayList<JXTaskPane>();
    //
    private EventList<Region> urbanRegions;
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    private ArrayList<Condition> comboCategoryVal = new ArrayList<Condition>();
    //
    private ItemsCategory parentCategory = null;
    private ItemsCategory landParentCategory = null;
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    private List<Region> regionMaster = new ArrayList<Region>();
    private ArrayList<Condition> conditionMaster = new ArrayList<Condition>();
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor;

    public ItemsNetworkPanel(ArchieveMainframe mainframe) {
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
        loadComboCondition();
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

        fieldDocumentDate.setFormats("dd/MM/yyyy");

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
                    ArrayList<ItemsNetwork> networks = table.getSelectedItemsNetworks();
                    if (!networks.isEmpty()) {
                        if (networks.size() == 1) {
                            selectedItemsNetwork = networks.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsNetwork.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsNetwork.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_NETWORK);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsNetwork = null;
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
                        ItemsCategory.ITEMS_NETWORK, parentCategory, true);
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
                    CustomOptionDialog.showDialog(ItemsNetworkPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
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
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_NETWORK);
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
            ArrayList<Unit> units = lw.get();
            
            filterPanel.loadComboUnit(units);
            
            unitMaster.addAll(units);

            units.add(0, new Unit());
            comboUnit.setModel(new ListComboBoxModel<Unit>(units));
            AutoCompleteDecorator.decorate(comboUnit);

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
            ArrayList<Condition> conditions = lw.get();

            conditionMaster.addAll(conditions);
            comboCategoryVal.addAll(conditions);

            conditions.add(0, new Condition());
            comboCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationCarrierCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationDebitCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationPoolCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationHWFCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationProtectCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationTappedCondition.setModel(new ListComboBoxModel<Condition>(conditions));
            comboIrrigationSupportCondition.setModel(new ListComboBoxModel<Condition>(conditions));

            AutoCompleteDecorator.decorate(comboCondition);
            AutoCompleteDecorator.decorate(comboIrrigationCondition);
            AutoCompleteDecorator.decorate(comboIrrigationCarrierCondition);
            AutoCompleteDecorator.decorate(comboIrrigationDebitCondition);
            AutoCompleteDecorator.decorate(comboIrrigationPoolCondition);
            AutoCompleteDecorator.decorate(comboIrrigationHWFCondition);
            AutoCompleteDecorator.decorate(comboIrrigationProtectCondition);
            AutoCompleteDecorator.decorate(comboIrrigationTappedCondition);
            AutoCompleteDecorator.decorate(comboIrrigationSupportCondition);

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

        fieldLength = new JXFormattedTextField();
        fieldLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldLength.setHorizontalAlignment(JLabel.LEFT);

        fieldWidth = new JXFormattedTextField();
        fieldWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldWidth.setHorizontalAlignment(JLabel.LEFT);

        fieldWide = new JXFormattedTextField();
        fieldWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldWide.setHorizontalAlignment(JLabel.LEFT);

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);

        //

        fieldStreetStartKM = new JXFormattedTextField();
        fieldStreetStartKM.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldStreetStartKM.setHorizontalAlignment(JLabel.LEFT);

        fieldStreetEndKM = new JXFormattedTextField();
        fieldStreetEndKM.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldStreetEndKM.setHorizontalAlignment(JLabel.LEFT);

        fieldBridgeLength = new JXFormattedTextField();
        fieldBridgeLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldBridgeLength.setHorizontalAlignment(JLabel.LEFT);

        fieldBridgeWidth = new JXFormattedTextField();
        fieldBridgeWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldBridgeWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationLength = new JXFormattedTextField();
        fieldIrrigationLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationWidth = new JXFormattedTextField();
        fieldIrrigationWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationWidth.setHorizontalAlignment(JLabel.LEFT);


        fieldIrrigationHeight = new JXFormattedTextField();
        fieldIrrigationHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHeight.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationCarrierLength = new JXFormattedTextField();
        fieldIrrigationCarrierLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationCarrierLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationCarrierHeight = new JXFormattedTextField();
        fieldIrrigationCarrierHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationCarrierHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationCarrierWidth = new JXFormattedTextField();
        fieldIrrigationCarrierWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationCarrierWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationDebitThresholdWidth = new JXFormattedTextField();
        fieldIrrigationDebitThresholdWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitThresholdWidth.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationDebitCDG = new JXFormattedTextField();
        fieldIrrigationDebitCDG.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitCDG.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationDebitCipolleti = new JXFormattedTextField();
        fieldIrrigationDebitCipolleti.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitCipolleti.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationDebitLength = new JXFormattedTextField();
        fieldIrrigationDebitLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationDebitHeight = new JXFormattedTextField();
        fieldIrrigationDebitHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationDebitWidth = new JXFormattedTextField();
        fieldIrrigationDebitWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationDebitWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationPoolUSBR3 = new JXFormattedTextField();
        fieldIrrigationPoolUSBR3.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolUSBR3.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolBlock = new JXFormattedTextField();
        fieldIrrigationPoolBlock.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolBlock.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolUSBR4 = new JXFormattedTextField();
        fieldIrrigationPoolUSBR4.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolUSBR4.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolVlogtor = new JXFormattedTextField();
        fieldIrrigationPoolVlogtor.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolVlogtor.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolLength = new JXFormattedTextField();
        fieldIrrigationPoolLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolHeight = new JXFormattedTextField();
        fieldIrrigationPoolHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationPoolWidth = new JXFormattedTextField();
        fieldIrrigationPoolWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationPoolWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationHWFLotBlock = new JXFormattedTextField();
        fieldIrrigationHWFLotBlock.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFLotBlock.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFTrapesium = new JXFormattedTextField();
        fieldIrrigationHWFTrapesium.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFTrapesium.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFSlide = new JXFormattedTextField();
        fieldIrrigationHWFSlide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFSlide.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFTreeTop = new JXFormattedTextField();
        fieldIrrigationHWFTreeTop.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFTreeTop.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFLength = new JXFormattedTextField();
        fieldIrrigationHWFLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFHeight = new JXFormattedTextField();
        fieldIrrigationHWFHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationHWFWidth = new JXFormattedTextField();
        fieldIrrigationHWFWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationHWFWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationProtectTransfer = new JXFormattedTextField();
        fieldIrrigationProtectTransfer.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectTransfer.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationProtectDisposal = new JXFormattedTextField();
        fieldIrrigationProtectDisposal.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectDisposal.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationProtectDrain = new JXFormattedTextField();
        fieldIrrigationProtectDrain.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectDrain.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationProtectLength = new JXFormattedTextField();
        fieldIrrigationProtectLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationProtectHeight = new JXFormattedTextField();
        fieldIrrigationProtectHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationProtectWidth = new JXFormattedTextField();
        fieldIrrigationProtectWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationProtectWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationTappedFor = new JXFormattedTextField();
        fieldIrrigationTappedFor.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedFor.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedSecond = new JXFormattedTextField();
        fieldIrrigationTappedSecond.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedSecond.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedRegulator = new JXFormattedTextField();
        fieldIrrigationTappedRegulator.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedRegulator.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedThird = new JXFormattedTextField();
        fieldIrrigationTappedThird.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedThird.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedLength = new JXFormattedTextField();
        fieldIrrigationTappedLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedHeight = new JXFormattedTextField();
        fieldIrrigationTappedHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationTappedWidth = new JXFormattedTextField();
        fieldIrrigationTappedWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationTappedWidth.setHorizontalAlignment(JLabel.LEFT);

        //

        fieldIrrigationSupportLevee = new JXFormattedTextField();
        fieldIrrigationSupportLevee.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportLevee.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportWash = new JXFormattedTextField();
        fieldIrrigationSupportWash.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportWash.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportBridge = new JXFormattedTextField();
        fieldIrrigationSupportBridge.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportBridge.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportKrib = new JXFormattedTextField();
        fieldIrrigationSupportKrib.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportKrib.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportLength = new JXFormattedTextField();
        fieldIrrigationSupportLength.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportLength.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportHeight = new JXFormattedTextField();
        fieldIrrigationSupportHeight.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportHeight.setHorizontalAlignment(JLabel.LEFT);

        fieldIrrigationSupportWidth = new JXFormattedTextField();
        fieldIrrigationSupportWidth.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldIrrigationSupportWidth.setHorizontalAlignment(JLabel.LEFT);
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private JXTaskPaneContainer createTaskPaneContainer() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();

        irrSCPane.setViewportView(createIrrigationTaskPaneContainer());

        JScrollBar sb = irrSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        tabPane.addTab("Jalan", createStreetPanel());
        tabPane.addTab("Jembatan", createBridgePanel());
        tabPane.addTab("Irigasi", irrSCPane);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Data Utama");
        task.add(createMainDataPanel());
        task.setAnimated(false);

        JXTaskPane task2 = new JXTaskPane();
        task2.setTitle("Rincian Data");
        task2.add(tabPane);
        task2.setAnimated(false);

        container.add(task);
        container.add(task2);

        taskArrays.add(task);
        taskArrays.add(task2);

        setTaskPaneState(taskArrays);
        addTaskPaneListener(taskArrays);

        return container;
    }

    private JXTaskPaneContainer createIrrigationTaskPaneContainer() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Bangunan Utama");
        task.add(createIrrigationMainPanel());
        task.setAnimated(false);

        JXTaskPane task2 = new JXTaskPane();
        task2.setTitle("Bangunan Pembawa");
        task2.add(createIrrigationCarrierPanel());
        task2.setAnimated(false);

        JXTaskPane task3 = new JXTaskPane();
        task3.setTitle("Bangunan Pengukur Debit");
        task3.add(createIrrigationDebitPanel());
        task3.setAnimated(false);

        JXTaskPane task4 = new JXTaskPane();
        task4.setTitle("Bangunan Kolam Olak");
        task4.add(createIrrigationPoolPanel());
        task4.setAnimated(false);

        JXTaskPane task5 = new JXTaskPane();
        task5.setTitle("Bangunan Tinggi Muka Air");
        task5.add(createIrrigationHWFPanel());
        task5.setAnimated(false);

        JXTaskPane task6 = new JXTaskPane();
        task6.setTitle("Bangunan Lindung");
        task6.add(createIrrigationProtectPanel());
        task6.setAnimated(false);

        JXTaskPane task7 = new JXTaskPane();
        task7.setTitle("Bangunan Bagi dan Sadap");
        task7.add(createIrrigationTappedPanel());
        task7.setAnimated(false);

        JXTaskPane task8 = new JXTaskPane();
        task8.setTitle("Bangunan Pelengkap");
        task8.add(createIrrigationSupportPanel());
        task8.setAnimated(false);

        container.add(task);
        container.add(task2);
        container.add(task3);
        container.add(task4);
        container.add(task5);
        container.add(task6);
        container.add(task7);
        container.add(task8);

        taskArrays2.add(task);
        taskArrays2.add(task2);
        taskArrays2.add(task3);
        taskArrays2.add(task4);
        taskArrays2.add(task5);
        taskArrays2.add(task6);
        taskArrays2.add(task7);
        taskArrays2.add(task8);

        setTaskPaneState(taskArrays2);
        addTaskPaneListener(taskArrays2);

        return container;
    }

    private void setAllTaskPaneCollapsed(JXTaskPane paneExcept, ArrayList<JXTaskPane> taskPanes) {

        if (!taskPanes.isEmpty()) {
            for (JXTaskPane taskPane : taskPanes) {
                if (taskPane.getTitle().equals(paneExcept.getTitle())) {
                    taskPane.setCollapsed(false);
                } else {
                    taskPane.setCollapsed(true);
                }
            }
        }
    }

    private void setTaskPaneState(ArrayList<JXTaskPane> taskPanes) {

        if (!taskPanes.isEmpty()) {
            for (JXTaskPane taskPane : taskPanes) {
                if (taskPane.getTitle().equals("Data Utama") || taskPane.getTitle().equals("Bangunan Utama")) {
                    taskPane.setCollapsed(false);
                } else {
                    taskPane.setCollapsed(true);
                }
            }
        }
    }

    private void addTaskPaneListener(final ArrayList<JXTaskPane> taskPanes) {
        if (!taskPanes.isEmpty()) {
            for (JXTaskPane taskPane : taskPanes) {
                taskPane.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Object source = e.getSource();
                        if (source instanceof JXTaskPane) {
                            JXTaskPane pane = (JXTaskPane) source;
                            if (!pane.isCollapsed()) {
                                setAllTaskPaneCollapsed(pane, taskPanes);
                            } else {
                                setTaskPaneState(taskPanes);
                            }

                            JScrollBar sb = mainSCPane.getVerticalScrollBar();

                            int index = taskPanes.indexOf(pane);
                            int size = taskPanes.size();
                            int max = sb.getMaximum();

                            int value = (index * max) / size;

                            sb.getModel().setValue(value);
                        }
                    }
                });
            }
        }
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

    private Component createMainDataPanel() {

        FormLayout lm = new FormLayout(
                "75px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,75px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "75px,fill:default:grow,20px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        JXLabel itemLabel = new JXLabel("Kode / Nama Barang");
        itemLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        builder.addLabel("UPB / Sub UPB", cc.xy(2, 2));
        builder.add(comboUnit, cc.xyw(4, 2, 5));

        builder.add(itemLabel, cc.xy(2, 4));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 4, 1));
        builder.add(fieldItems, cc.xyw(6, 4, 3));

        builder.addLabel("No. Register", cc.xy(2, 6));
        builder.add(fieldRegNumber, cc.xyw(4, 6, 5));

        builder.addLabel("Konstruksi", cc.xy(2, 8));
        builder.add(fieldConstructionType, cc.xyw(4, 8, 5));

        builder.addLabel("Panjang (Km)", cc.xy(2, 10));
        builder.add(fieldLength, cc.xyw(4, 10, 3));

        builder.addLabel("Lebar (M)", cc.xy(2, 12));
        builder.add(fieldWidth, cc.xyw(4, 12, 3));

        builder.addLabel("Luas (M2)", cc.xy(2, 14));
        builder.add(fieldWide, cc.xyw(4, 14, 3));

        builder.addLabel("Letak / Lokasi / Alamat", cc.xy(2, 16));
        builder.add(fieldAddressLocation, cc.xyw(4, 16, 5));

        builder.addLabel("Kecamatan", cc.xy(2, 18));
        builder.add(comboUrban, cc.xyw(4, 18, 5));

        builder.addLabel("Kelurahan", cc.xy(2, 20));
        builder.add(comboSubUrban, cc.xyw(4, 20, 5));

        builder.addLabel("No. Dokumen", cc.xy(2, 22));
        builder.add(fieldDocumentNumber, cc.xyw(4, 22, 5));

        builder.addLabel("Tgl. Dokumen", cc.xy(2, 24));
        builder.add(fieldDocumentDate, cc.xyw(4, 24, 3));

        builder.addLabel("Status Tanah", cc.xy(2, 26));
        builder.add(fieldLandStatus, cc.xyw(4, 26, 5));

        builder.addLabel("Nomor Kode Tanah", cc.xy(2, 28));
        builder.add(createStrip2(1.0, 1.0), cc.xyw(4, 28, 1));
        builder.add(fieldLandCode, cc.xyw(6, 28, 3));

        builder.addLabel("Asal-Usul", cc.xy(2, 30));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 30, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 32));
        builder.add(fieldFundingSource, cc.xyw(4, 32, 5));

        builder.addLabel("Harga Perolehan", cc.xy(2, 34));
        builder.add(fieldPrice, cc.xyw(4, 34, 5));

        builder.addLabel("Kondisi", cc.xy(2, 36));
        builder.add(comboCondition, cc.xyw(4, 36, 3));

        builder.addLabel("Keterangan", cc.xy(2, 38));
        builder.add(scPane, cc.xywh(4, 38, 5, 2));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createStreetPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tahun Bangun", cc.xy(2, 2));
        builder.add(fieldStreetYearBuild, cc.xyw(4, 2, 1));

        builder.addLabel("Daerah/Lokasi", cc.xy(2, 4));
        builder.add(radioStreetLocation, cc.xyw(4, 4, 3));

        builder.addLabel("Kelandaian Jalan", cc.xy(2, 6));
        builder.add(radioStreetFlatness, cc.xyw(4, 6, 3));

        builder.addLabel("KM Awal STA", cc.xy(2, 8));
        builder.add(fieldStreetStartKM, cc.xyw(4, 8, 1));

        builder.addLabel("KM Akhir STA", cc.xy(2, 10));
        builder.add(fieldStreetEndKM, cc.xyw(4, 10, 1));

        builder.addLabel("Lebar", cc.xy(2, 12));
        builder.add(radioStreetWidth, cc.xyw(4, 12, 3));

        builder.addLabel("Permukaan", cc.xy(2, 14));
        builder.add(radioStreetSurface, cc.xyw(4, 14, 3));

        builder.addLabel("Bahu", cc.xy(2, 16));
        builder.add(radioStreetSide, cc.xyw(4, 16, 3));

        builder.addLabel("Trotoar", cc.xy(2, 18));
        builder.add(radioStreetTrotoire, cc.xyw(4, 18, 3));

        builder.addLabel("Saluran Tepi Jalan", cc.xy(2, 20));
        builder.add(radioStreetChannel, cc.xyw(4, 20, 3));

        builder.addLabel("Pengaman Jalan Pada Tikungan", cc.xy(2, 22));
        builder.add(radioStreetSafetyZone, cc.xyw(4, 22, 3));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createBridgePanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,fill:default:grow,5px,220px,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,50px,fill:default:grow,5px,"
                + "pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tipe Standar", cc.xy(2, 2));
        builder.add(radioBridgeStandarType, cc.xywh(4, 2, 3, 5));

        builder.addLabel("Dibangun Tahun", cc.xy(2, 8));
        builder.add(fieldBridgeYearBuild, cc.xyw(4, 8, 1));

        builder.addLabel("Panjang", cc.xy(2, 10));
        builder.add(fieldBridgeLength, cc.xyw(4, 10, 1));
        builder.addLabel("(m)", cc.xyw(6, 10, 1));

        builder.addLabel("Lebar", cc.xy(2, 12));
        builder.add(fieldBridgeWidth, cc.xyw(4, 12, 1));
        builder.addLabel("(m)", cc.xyw(6, 12, 1));

        builder.addLabel("Kegunaan", cc.xy(2, 14));
        builder.add(radioBridgePurpose, cc.xyw(4, 14, 3));

        builder.addLabel("Material Utama", cc.xy(2, 16));
        builder.add(radioBridgeMainMaterial, cc.xyw(4, 16, 3));

        builder.addLabel("Bentuk Bangunan Atas", cc.xy(2, 18));
        builder.add(radioBridgeTopShape, cc.xywh(4, 18, 3, 3));

        builder.addLabel("Non Struktur Bangunan Atas", cc.xy(2, 22));
        builder.add(radioBridgeOtherShape, cc.xyw(4, 22, 3));

        builder.addLabel("Abulment (Kepala Jembatan)", cc.xy(2, 24));
        builder.add(radioBridgeHeadShape, cc.xyw(4, 24, 3));

        builder.addLabel("Material Abulment", cc.xy(2, 26));
        builder.add(radioBridgeHeadMaterial, cc.xyw(4, 26, 3));

        builder.addLabel("Pler (Pilar)", cc.xy(2, 28));
        builder.add(radioBridgePillar, cc.xywh(4, 28, 3, 3));

        builder.addLabel("Material Pler", cc.xy(2, 32));
        builder.add(radioBridgePillarMaterial, cc.xyw(4, 32, 3));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationMainPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tahun Bangun", cc.xy(2, 2));
        builder.add(fieldIrrigationYearBuild, cc.xyw(4, 2, 1));

        builder.addLabel("Jenis Bangunan", cc.xy(2, 4));
        builder.add(radioIrrigationNetworkType, cc.xyw(4, 4, 3));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 6, 5));

        builder.addLabel("   Panjang", cc.xy(2, 8));
        builder.add(fieldIrrigationLength, cc.xyw(4, 8, 1));
        builder.addLabel("(m)", cc.xyw(6, 8, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 10));
        builder.add(fieldIrrigationHeight, cc.xyw(4, 10, 1));
        builder.addLabel("(m)", cc.xyw(6, 10, 1));

        builder.addLabel("   Lebar", cc.xy(2, 12));
        builder.add(fieldIrrigationWidth, cc.xyw(4, 12, 1));
        builder.addLabel("(m)", cc.xyw(6, 12, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 14));
        builder.add(radioIrrigationNetworkMaterial, cc.xyw(4, 14, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 16));
        builder.add(comboIrrigationCondition, cc.xyw(4, 16, 1));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationCarrierPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xy(2, 2));
        builder.add(radioIrrigationCarrierType, cc.xyw(4, 2, 3));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 4, 5));

        builder.addLabel("   Panjang", cc.xy(2, 6));
        builder.add(fieldIrrigationCarrierLength, cc.xyw(4, 6, 1));
        builder.addLabel("(m)", cc.xyw(6, 6, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 8));
        builder.add(fieldIrrigationCarrierHeight, cc.xyw(4, 8, 1));
        builder.addLabel("(m)", cc.xyw(6, 8, 1));

        builder.addLabel("   Lebar", cc.xy(2, 10));
        builder.add(fieldIrrigationCarrierWidth, cc.xyw(4, 10, 1));
        builder.addLabel("(m)", cc.xyw(6, 10, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 12));
        builder.add(radioIrrigationCarrierMaterial, cc.xyw(4, 12, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 14));
        builder.add(comboIrrigationCarrierCondition, cc.xyw(4, 14, 1));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationDebitPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   Irigasi Ambang Lebar", cc.xy(2, 4));
        builder.add(fieldIrrigationDebitThresholdWidth, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Ambang Crump de Gruyler", cc.xy(2, 6));
        builder.add(fieldIrrigationDebitCDG, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   Cipolleti", cc.xy(2, 8));
        builder.add(fieldIrrigationDebitCipolleti, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 10, 5));

        builder.addLabel("   Panjang", cc.xy(2, 12));
        builder.add(fieldIrrigationDebitLength, cc.xyw(4, 12, 1));
        builder.addLabel("(m)", cc.xyw(6, 12, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 14));
        builder.add(fieldIrrigationDebitHeight, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Lebar", cc.xy(2, 16));
        builder.add(fieldIrrigationDebitWidth, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 18));
        builder.add(radioIrrigationDebitMaterial, cc.xyw(4, 18, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 20));
        builder.add(comboIrrigationDebitCondition, cc.xyw(4, 20, 1));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationPoolPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   U.S.B.R Tipe III", cc.xy(2, 4));
        builder.add(fieldIrrigationPoolUSBR3, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Blok Halang", cc.xy(2, 6));
        builder.add(fieldIrrigationPoolBlock, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   U.S.B.R Tipe IV", cc.xy(2, 8));
        builder.add(fieldIrrigationPoolUSBR4, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("   Vlogtor", cc.xy(2, 10));
        builder.add(fieldIrrigationPoolVlogtor, cc.xyw(4, 10, 1));
        builder.addLabel("Buah", cc.xyw(6, 10, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 12, 5));

        builder.addLabel("   Panjang", cc.xy(2, 14));
        builder.add(fieldIrrigationPoolLength, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 16));
        builder.add(fieldIrrigationPoolHeight, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("   Lebar", cc.xy(2, 18));
        builder.add(fieldIrrigationPoolWidth, cc.xyw(4, 18, 1));
        builder.addLabel("(m)", cc.xyw(6, 18, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 20));
        builder.add(radioIrrigationPoolMaterial, cc.xyw(4, 20, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 22));
        builder.add(comboIrrigationPoolCondition, cc.xyw(4, 22, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationHWFPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   Pintu Slot Balok", cc.xy(2, 4));
        builder.add(fieldIrrigationHWFLotBlock, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Kontrol Trapesium", cc.xy(2, 6));
        builder.add(fieldIrrigationHWFTrapesium, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   Pintu Sorong", cc.xy(2, 8));
        builder.add(fieldIrrigationHWFSlide, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("   Mercu Tetap", cc.xy(2, 10));
        builder.add(fieldIrrigationHWFTreeTop, cc.xyw(4, 10, 1));
        builder.addLabel("Buah", cc.xyw(6, 10, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 12, 5));

        builder.addLabel("   Panjang", cc.xy(2, 14));
        builder.add(fieldIrrigationHWFLength, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 16));
        builder.add(fieldIrrigationHWFHeight, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("   Lebar", cc.xy(2, 18));
        builder.add(fieldIrrigationHWFWidth, cc.xyw(4, 18, 1));
        builder.addLabel("(m)", cc.xyw(6, 18, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 20));
        builder.add(radioIrrigationHWFMaterial, cc.xyw(4, 20, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 22));
        builder.add(comboIrrigationHWFCondition, cc.xyw(4, 22, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationProtectPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   Pelimpah", cc.xy(2, 4));
        builder.add(fieldIrrigationProtectTransfer, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Saluran Pembuang Samping", cc.xy(2, 6));
        builder.add(fieldIrrigationProtectDisposal, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   Penguras", cc.xy(2, 8));
        builder.add(fieldIrrigationProtectDrain, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 10, 5));

        builder.addLabel("   Panjang", cc.xy(2, 12));
        builder.add(fieldIrrigationProtectLength, cc.xyw(4, 12, 1));
        builder.addLabel("(m)", cc.xyw(6, 12, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 14));
        builder.add(fieldIrrigationProtectHeight, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Lebar", cc.xy(2, 16));
        builder.add(fieldIrrigationProtectWidth, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 18));
        builder.add(radioIrrigationProtectMaterial, cc.xyw(4, 18, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 20));
        builder.add(comboIrrigationProtectCondition, cc.xyw(4, 20, 1));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationTappedPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   Bagi", cc.xy(2, 4));
        builder.add(fieldIrrigationTappedFor, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Sadap Sekunder", cc.xy(2, 6));
        builder.add(fieldIrrigationTappedSecond, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   Pengatur", cc.xy(2, 8));
        builder.add(fieldIrrigationTappedRegulator, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("   Sadap Tersier", cc.xy(2, 10));
        builder.add(fieldIrrigationTappedThird, cc.xyw(4, 10, 1));
        builder.addLabel("Buah", cc.xyw(6, 10, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 12, 5));

        builder.addLabel("   Panjang", cc.xy(2, 14));
        builder.add(fieldIrrigationTappedLength, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 16));
        builder.add(fieldIrrigationTappedHeight, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("   Lebar", cc.xy(2, 18));
        builder.add(fieldIrrigationTappedWidth, cc.xyw(4, 18, 1));
        builder.addLabel("(m)", cc.xyw(6, 18, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 20));
        builder.add(radioIrrigationTappedMaterial, cc.xyw(4, 20, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 22));
        builder.add(comboIrrigationTappedCondition, cc.xyw(4, 22, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createIrrigationSupportPanel() {
        FormLayout lm = new FormLayout(
                "20px,pref,20px, 100px,5px,fill:default:grow,20px",
                "20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Bangunan", cc.xyw(2, 2, 5));

        builder.addLabel("   Tanggul", cc.xy(2, 4));
        builder.add(fieldIrrigationSupportLevee, cc.xyw(4, 4, 1));
        builder.addLabel("Buah", cc.xyw(6, 4, 1));

        builder.addLabel("   Tangga Cuci", cc.xy(2, 6));
        builder.add(fieldIrrigationSupportWash, cc.xyw(4, 6, 1));
        builder.addLabel("Buah", cc.xyw(6, 6, 1));

        builder.addLabel("   Jembatan Untuk Orang dan Hewan", cc.xy(2, 8));
        builder.add(fieldIrrigationSupportBridge, cc.xyw(4, 8, 1));
        builder.addLabel("Buah", cc.xyw(6, 8, 1));

        builder.addLabel("   Krib", cc.xy(2, 10));
        builder.add(fieldIrrigationSupportKrib, cc.xyw(4, 10, 1));
        builder.addLabel("Buah", cc.xyw(6, 10, 1));

        builder.addLabel("Dimensi (Motor)", cc.xyw(2, 12, 5));

        builder.addLabel("   Panjang", cc.xy(2, 14));
        builder.add(fieldIrrigationSupportLength, cc.xyw(4, 14, 1));
        builder.addLabel("(m)", cc.xyw(6, 14, 1));

        builder.addLabel("   Tinggi", cc.xy(2, 16));
        builder.add(fieldIrrigationSupportHeight, cc.xyw(4, 16, 1));
        builder.addLabel("(m)", cc.xyw(6, 16, 1));

        builder.addLabel("   Lebar", cc.xy(2, 18));
        builder.add(fieldIrrigationSupportWidth, cc.xyw(4, 18, 1));
        builder.addLabel("(m)", cc.xyw(6, 18, 1));

        builder.addLabel("Material Bangunan", cc.xy(2, 20));
        builder.add(radioIrrigationSupportMaterial, cc.xyw(4, 20, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 22));
        builder.add(comboIrrigationSupportCondition, cc.xyw(4, 22, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createInputPanel() {

        FormLayout lm = new FormLayout(
                "150px,right:pref,10px, 100px,5px,fill:default:grow,150px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,"
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

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 7));
        builder.addSeparator("", cc.xyw(1, 2, 7));

        builder.add(panelRequired, cc.xyw(1, 4, 7));

        JXTaskPaneContainer container = createTaskPaneContainer();
        container.setAlpha(0.95f);

        builder.add(container, cc.xyw(2, 6, 5));

        builder.addSeparator("", cc.xyw(1, 10, 7));
        builder.add(btSavePanelBottom, cc.xyw(1, 11, 7));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-D");

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

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldStreetYearBuild.setYear(year);
        fieldBridgeYearBuild.setYear(year);
        fieldIrrigationYearBuild.setYear(year);
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
        fieldConstructionType.setText("");
        fieldLength.setValue(Integer.valueOf(0));
        fieldWidth.setValue(Integer.valueOf(0));
        fieldWide.setValue(BigDecimal.ZERO);
        fieldLandStatus.setText("");
        fieldLandCode.setText("");
        comboCondition.getEditor().setItem(null);
        fieldAddressLocation.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldLength.setValue(Integer.valueOf(0));
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
        //
        clearYear();
        radioStreetLocation.setAllDeselected();
        radioStreetFlatness.setAllDeselected();
        fieldStreetStartKM.setValue(Integer.valueOf(0));
        fieldStreetEndKM.setValue(Integer.valueOf(0));
        radioStreetWidth.setAllDeselected();
        radioStreetSurface.setAllDeselected();
        radioStreetSide.setAllDeselected();
        radioStreetTrotoire.setAllDeselected();
        radioStreetChannel.setAllDeselected();
        radioStreetSafetyZone.setAllDeselected();
        //
        radioBridgeStandarType.setAllDeselected();
        fieldBridgeLength.setValue(Integer.valueOf(0));
        fieldBridgeWidth.setValue(Integer.valueOf(0));
        radioBridgePurpose.setAllDeselected();
        radioBridgeMainMaterial.setAllDeselected();
        radioBridgeTopShape.setAllDeselected();
        radioBridgeOtherShape.setAllDeselected();
        radioBridgeHeadShape.setAllDeselected();
        radioBridgeHeadMaterial.setAllDeselected();
        radioBridgePillar.setAllDeselected();
        radioBridgePillarMaterial.setAllDeselected();
        //
        radioIrrigationNetworkType.setAllDeselected();
        fieldIrrigationLength.setValue(Integer.valueOf(0));
        fieldIrrigationHeight.setValue(Integer.valueOf(0));
        fieldIrrigationWidth.setValue(Integer.valueOf(0));
        radioIrrigationNetworkMaterial.setAllDeselected();
        comboIrrigationCondition.getEditor().setItem(null);
        //
        radioIrrigationCarrierType.setAllDeselected();
        fieldIrrigationCarrierLength.setValue(Integer.valueOf(0));
        fieldIrrigationCarrierHeight.setValue(Integer.valueOf(0));
        fieldIrrigationCarrierWidth.setValue(Integer.valueOf(0));
        radioIrrigationCarrierMaterial.setAllDeselected();
        comboIrrigationCarrierCondition.getEditor().setItem(null);
        //
        fieldIrrigationDebitThresholdWidth.setValue(Integer.valueOf(0));
        fieldIrrigationDebitCDG.setValue(Integer.valueOf(0));
        fieldIrrigationDebitCipolleti.setValue(Integer.valueOf(0));
        fieldIrrigationDebitLength.setValue(Integer.valueOf(0));
        fieldIrrigationDebitHeight.setValue(Integer.valueOf(0));
        fieldIrrigationDebitWidth.setValue(Integer.valueOf(0));
        radioIrrigationDebitMaterial.setAllDeselected();
        comboIrrigationDebitCondition.getEditor().setItem(null);
        //
        fieldIrrigationPoolUSBR3.setValue(Integer.valueOf(0));
        fieldIrrigationPoolBlock.setValue(Integer.valueOf(0));
        fieldIrrigationPoolUSBR4.setValue(Integer.valueOf(0));
        fieldIrrigationPoolVlogtor.setValue(Integer.valueOf(0));
        fieldIrrigationPoolLength.setValue(Integer.valueOf(0));
        fieldIrrigationPoolHeight.setValue(Integer.valueOf(0));
        fieldIrrigationPoolWidth.setValue(Integer.valueOf(0));
        radioIrrigationPoolMaterial.setAllDeselected();
        comboIrrigationPoolCondition.getEditor().setItem(null);
        //
        fieldIrrigationHWFLotBlock.setValue(Integer.valueOf(0));
        fieldIrrigationHWFTrapesium.setValue(Integer.valueOf(0));
        fieldIrrigationHWFSlide.setValue(Integer.valueOf(0));
        fieldIrrigationHWFTreeTop.setValue(Integer.valueOf(0));
        fieldIrrigationHWFLength.setValue(Integer.valueOf(0));
        fieldIrrigationHWFHeight.setValue(Integer.valueOf(0));
        fieldIrrigationHWFWidth.setValue(Integer.valueOf(0));
        radioIrrigationHWFMaterial.setAllDeselected();
        comboIrrigationHWFCondition.getEditor().setItem(null);
        //
        fieldIrrigationProtectTransfer.setValue(Integer.valueOf(0));
        fieldIrrigationProtectDisposal.setValue(Integer.valueOf(0));
        fieldIrrigationProtectDrain.setValue(Integer.valueOf(0));
        fieldIrrigationProtectLength.setValue(Integer.valueOf(0));
        fieldIrrigationProtectHeight.setValue(Integer.valueOf(0));
        fieldIrrigationProtectWidth.setValue(Integer.valueOf(0));
        radioIrrigationProtectMaterial.setAllDeselected();
        comboIrrigationProtectCondition.getEditor().setItem(null);
        //
        fieldIrrigationTappedFor.setValue(Integer.valueOf(0));
        fieldIrrigationTappedSecond.setValue(Integer.valueOf(0));
        fieldIrrigationTappedRegulator.setValue(Integer.valueOf(0));
        fieldIrrigationTappedThird.setValue(Integer.valueOf(0));
        fieldIrrigationTappedLength.setValue(Integer.valueOf(0));
        fieldIrrigationTappedHeight.setValue(Integer.valueOf(0));
        fieldIrrigationTappedWidth.setValue(Integer.valueOf(0));
        radioIrrigationTappedMaterial.setAllDeselected();
        comboIrrigationTappedCondition.getEditor().setItem(null);
        //
        fieldIrrigationSupportLevee.setValue(Integer.valueOf(0));
        fieldIrrigationSupportWash.setValue(Integer.valueOf(0));
        fieldIrrigationSupportBridge.setValue(Integer.valueOf(0));
        fieldIrrigationSupportKrib.setValue(Integer.valueOf(0));
        fieldIrrigationSupportLength.setValue(Integer.valueOf(0));
        fieldIrrigationSupportHeight.setValue(Integer.valueOf(0));
        fieldIrrigationSupportWidth.setValue(Integer.valueOf(0));
        radioIrrigationSupportMaterial.setAllDeselected();
        comboIrrigationSupportCondition.getEditor().setItem(null);
        //
    }

    private void setFormValues() {
        if (selectedItemsNetwork != null) {
            try {
                if (selectedItemsNetwork.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsNetwork.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsNetwork.getItemCode());
                category.setCategoryName(selectedItemsNetwork.getItemName());
                category.setTypes(ItemsCategory.ITEMS_NETWORK);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsNetwork.getRegNumber());
                fieldDocumentNumber.setText(selectedItemsNetwork.getDocumentNumber());
                fieldDocumentDate.setDate(selectedItemsNetwork.getDocumentDate());
                fieldConstructionType.setText(selectedItemsNetwork.getConstructionType());

                fieldLength.setValue(selectedItemsNetwork.getLength());
                fieldWidth.setValue(selectedItemsNetwork.getWidth());
                fieldWide.setValue(selectedItemsNetwork.getWide());
                fieldPrice.setValue(selectedItemsNetwork.getPrice());

                fieldLandStatus.setText(selectedItemsNetwork.getLandStatus());
                fieldLandCode.setText(selectedItemsNetwork.getLandCode());

                if (selectedItemsNetwork.getCondition() == null) {
                    comboCondition.getEditor().setItem(null);
                } else {
                    comboCondition.setSelectedItem(selectedItemsNetwork.getCondition());
                }

                fieldAddressLocation.setText(selectedItemsNetwork.getAddressLocation());
                if (selectedItemsNetwork.getUrban() == null) {
                    comboUrban.getEditor().setItem(null);
                } else {
                    comboUrban.setSelectedItem(selectedItemsNetwork.getUrban());
                }

                if (selectedItemsNetwork.getSubUrban() == null) {
                    comboSubUrban.getEditor().setItem(null);
                } else {
                    comboSubUrban.setSelectedItem(selectedItemsNetwork.getSubUrban());
                }

                fieldLength.setValue(selectedItemsNetwork.getLength());
                fieldFundingSource.setText(selectedItemsNetwork.getFundingSource());
                fieldAcquisitionWay.setText(selectedItemsNetwork.getAcquisitionWay());
                fieldDescription.setText(selectedItemsNetwork.getDescription());

                //
                fieldStreetYearBuild.setYear(selectedItemsNetwork.getStreetYearBuild());
                radioStreetLocation.setSelectedData(selectedItemsNetwork.getStreetLocation());
                radioStreetFlatness.setSelectedData(selectedItemsNetwork.getStreetFlatness());
                fieldStreetStartKM.setValue(selectedItemsNetwork.getStreetStartKM());
                fieldStreetEndKM.setValue(selectedItemsNetwork.getStreetEndKM());
                radioStreetWidth.setSelectedData(selectedItemsNetwork.getStreetWidth());
                radioStreetSurface.setSelectedData(selectedItemsNetwork.getStreetSurface());
                radioStreetSide.setSelectedData(selectedItemsNetwork.getStreetSide());
                radioStreetTrotoire.setSelectedData(selectedItemsNetwork.getStreetTrotoire());
                radioStreetChannel.setSelectedData(selectedItemsNetwork.getStreetChannel());

                if (selectedItemsNetwork.isStreetSafetyZone()) {
                    radioStreetSafetyZone.setSelectedData("Ada");
                } else {
                    radioStreetSafetyZone.setSelectedData("Tidak Ada");
                }

                //
                radioBridgeStandarType.setSelectedData(selectedItemsNetwork.getBridgeStandarType());
                fieldBridgeYearBuild.setYear(selectedItemsNetwork.getBridgeYearBuild());
                fieldBridgeLength.setValue(selectedItemsNetwork.getBridgeLength());
                fieldBridgeWidth.setValue(selectedItemsNetwork.getBridgeWidth());
                radioBridgePurpose.setSelectedData(selectedItemsNetwork.getBridgePurpose());
                radioBridgeMainMaterial.setSelectedData(selectedItemsNetwork.getBridgeMainMaterial());
                radioBridgeTopShape.setSelectedData(selectedItemsNetwork.getBridgeTopShape());
                radioBridgeOtherShape.setSelectedData(selectedItemsNetwork.getBridgeOtherShape());
                radioBridgeHeadShape.setSelectedData(selectedItemsNetwork.getBridgeHeadShape());
                radioBridgeHeadMaterial.setSelectedData(selectedItemsNetwork.getBridgeHeadMaterial());
                radioBridgePillar.setSelectedData(selectedItemsNetwork.getBridgePillar());
                radioBridgePillarMaterial.setSelectedData(selectedItemsNetwork.getBridgePillarMaterial());
                //
                fieldIrrigationYearBuild.setYear(selectedItemsNetwork.getIrrigationYearBuild());
                radioIrrigationNetworkType.setSelectedData(selectedItemsNetwork.getIrrigationBuildingType());
                fieldIrrigationLength.setValue(selectedItemsNetwork.getIrrigationLength());
                fieldIrrigationHeight.setValue(selectedItemsNetwork.getIrrigationHeight());
                fieldIrrigationWidth.setValue(selectedItemsNetwork.getIrrigationWidth());
                radioIrrigationNetworkMaterial.setSelectedData(selectedItemsNetwork.getIrrigationBuildingMaterial());

                if (selectedItemsNetwork.getIrrigationCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationCondition(mLogic.getCondition(mainframe.getSession(), selectedItemsNetwork.getIrrigationCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationCondition() == null) {
                    comboIrrigationCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationCondition.setSelectedItem(selectedItemsNetwork.getIrrigationCondition());
                }

                //
                radioIrrigationCarrierType.setSelectedData(selectedItemsNetwork.getIrrigationCarrierType());
                fieldIrrigationCarrierLength.setValue(selectedItemsNetwork.getIrrigationCarrierLength());
                fieldIrrigationCarrierHeight.setValue(selectedItemsNetwork.getIrrigationCarrierHeight());
                fieldIrrigationCarrierWidth.setValue(selectedItemsNetwork.getIrrigationCarrierWidth());
                radioIrrigationCarrierMaterial.setSelectedData(selectedItemsNetwork.getIrrigationCarrierMaterial());

                if (selectedItemsNetwork.getIrrigationCarrierCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationCarrierCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationCarrierCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationCarrierCondition() == null) {
                    comboIrrigationCarrierCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationCarrierCondition.setSelectedItem(selectedItemsNetwork.getIrrigationCarrierCondition());
                }

                //
                fieldIrrigationDebitThresholdWidth.setValue(selectedItemsNetwork.getIrrigationDebitThresholdWidth());
                fieldIrrigationDebitCDG.setValue(selectedItemsNetwork.getIrrigationDebitCDG());
                fieldIrrigationDebitCipolleti.setValue(selectedItemsNetwork.getIrrigationDebitCipolleti());
                fieldIrrigationDebitLength.setValue(selectedItemsNetwork.getIrrigationDebitLength());
                fieldIrrigationDebitHeight.setValue(selectedItemsNetwork.getIrrigationDebitHeight());
                fieldIrrigationDebitWidth.setValue(selectedItemsNetwork.getIrrigationDebitWidth());
                radioIrrigationDebitMaterial.setSelectedData(selectedItemsNetwork.getIrrigationDebitMaterial());

                if (selectedItemsNetwork.getIrrigationDebitCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationDebitCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationDebitCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationDebitCondition() == null) {
                    comboIrrigationDebitCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationDebitCondition.setSelectedItem(selectedItemsNetwork.getIrrigationDebitCondition());
                }

                //

                fieldIrrigationPoolUSBR3.setValue(selectedItemsNetwork.getIrrigationPoolUSBR3());
                fieldIrrigationPoolBlock.setValue(selectedItemsNetwork.getIrrigationPoolBlock());
                fieldIrrigationPoolUSBR4.setValue(selectedItemsNetwork.getIrrigationPoolUSBR4());
                fieldIrrigationPoolVlogtor.setValue(selectedItemsNetwork.getIrrigationPoolVlogtor());
                fieldIrrigationPoolLength.setValue(selectedItemsNetwork.getIrrigationPoolLength());
                fieldIrrigationPoolHeight.setValue(selectedItemsNetwork.getIrrigationPoolHeight());
                fieldIrrigationPoolWidth.setValue(selectedItemsNetwork.getIrrigationPoolWidth());
                radioIrrigationPoolMaterial.setSelectedData(selectedItemsNetwork.getIrrigationPoolMaterial());

                if (selectedItemsNetwork.getIrrigationPoolCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationPoolCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationPoolCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationPoolCondition() == null) {
                    comboIrrigationPoolCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationPoolCondition.setSelectedItem(selectedItemsNetwork.getIrrigationPoolCondition());
                }

                //

                fieldIrrigationHWFLotBlock.setValue(selectedItemsNetwork.getIrrigationHWFLotBlock());
                fieldIrrigationHWFTrapesium.setValue(selectedItemsNetwork.getIrrigationHWFTrapesium());
                fieldIrrigationHWFSlide.setValue(selectedItemsNetwork.getIrrigationHWFSlide());
                fieldIrrigationHWFTreeTop.setValue(selectedItemsNetwork.getIrrigationHWFTreeTop());
                fieldIrrigationHWFLength.setValue(selectedItemsNetwork.getIrrigationHWFLength());
                fieldIrrigationHWFHeight.setValue(selectedItemsNetwork.getIrrigationHWFHeight());
                fieldIrrigationHWFWidth.setValue(selectedItemsNetwork.getIrrigationHWFWidth());
                radioIrrigationHWFMaterial.setSelectedData(selectedItemsNetwork.getIrrigationHWFMaterial());

                if (selectedItemsNetwork.getIrrigationHWFCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationHWFCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationHWFCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationHWFCondition() == null) {
                    comboIrrigationHWFCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationHWFCondition.setSelectedItem(selectedItemsNetwork.getIrrigationHWFCondition());
                }

                //

                fieldIrrigationProtectTransfer.setValue(selectedItemsNetwork.getIrrigationProtectTransfer());
                fieldIrrigationProtectDisposal.setValue(selectedItemsNetwork.getIrrigationProtectDisposal());
                fieldIrrigationProtectDrain.setValue(selectedItemsNetwork.getIrrigationProtectDrain());
                fieldIrrigationProtectLength.setValue(selectedItemsNetwork.getIrrigationProtectLength());
                fieldIrrigationProtectHeight.setValue(selectedItemsNetwork.getIrrigationProtectHeight());
                fieldIrrigationProtectWidth.setValue(selectedItemsNetwork.getIrrigationProtectWidth());
                radioIrrigationProtectMaterial.setSelectedData(selectedItemsNetwork.getIrrigationProtectMaterial());

                if (selectedItemsNetwork.getIrrigationProtectCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationProtectCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationProtectCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationProtectCondition() == null) {
                    comboIrrigationProtectCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationProtectCondition.setSelectedItem(selectedItemsNetwork.getIrrigationProtectCondition());
                }

                //

                fieldIrrigationTappedFor.setValue(selectedItemsNetwork.getIrrigationTappedFor());
                fieldIrrigationTappedSecond.setValue(selectedItemsNetwork.getIrrigationTappedSecond());
                fieldIrrigationTappedRegulator.setValue(selectedItemsNetwork.getIrrigationTappedRegulator());
                fieldIrrigationTappedThird.setValue(selectedItemsNetwork.getIrrigationTappedThird());
                fieldIrrigationTappedLength.setValue(selectedItemsNetwork.getIrrigationTappedLength());
                fieldIrrigationTappedHeight.setValue(selectedItemsNetwork.getIrrigationTappedHeight());
                fieldIrrigationTappedWidth.setValue(selectedItemsNetwork.getIrrigationTappedWidth());
                radioIrrigationTappedMaterial.setSelectedData(selectedItemsNetwork.getIrrigationTappedMaterial());

                if (selectedItemsNetwork.getIrrigationTappedCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationTappedCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationTappedCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationTappedCondition() == null) {
                    comboIrrigationTappedCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationTappedCondition.setSelectedItem(selectedItemsNetwork.getIrrigationTappedCondition());
                }

                //

                fieldIrrigationSupportLevee.setValue(selectedItemsNetwork.getIrrigationSupportLevee());
                fieldIrrigationSupportWash.setValue(selectedItemsNetwork.getIrrigationSupportWash());
                fieldIrrigationSupportBridge.setValue(selectedItemsNetwork.getIrrigationSupportBridge());
                fieldIrrigationSupportKrib.setValue(selectedItemsNetwork.getIrrigationSupportKrib());
                fieldIrrigationSupportLength.setValue(selectedItemsNetwork.getIrrigationSupportLength());
                fieldIrrigationSupportHeight.setValue(selectedItemsNetwork.getIrrigationSupportHeight());
                fieldIrrigationSupportWidth.setValue(selectedItemsNetwork.getIrrigationSupportWidth());
                radioIrrigationSupportMaterial.setSelectedData(selectedItemsNetwork.getIrrigationSupportMaterial());

                if (selectedItemsNetwork.getIrrigationSupportCondition() != null) {
                    try {
                        selectedItemsNetwork.setIrrigationSupportCondition(mLogic.getCondition(mainframe.getSession(),
                                selectedItemsNetwork.getIrrigationSupportCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsNetwork.getIrrigationSupportCondition() == null) {
                    comboIrrigationSupportCondition.getEditor().setItem(null);
                } else {
                    comboIrrigationSupportCondition.setSelectedItem(selectedItemsNetwork.getIrrigationSupportCondition());
                }
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsNetwork getItemsNetwork() throws MotekarException {
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

        String addreasLocation = fieldAddressLocation.getText();

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
        String constructionType = fieldConstructionType.getText();
        String landStatus = fieldLandStatus.getText();
        String landCode = fieldLandCode.getText();

        Object objLength = fieldLength.getValue();
        Integer length = Integer.valueOf(0);

        if (objLength instanceof Long) {
            Long value = (Long) objLength;
            length = Integer.valueOf(value.intValue());
        } else if (objLength instanceof Integer) {
            length = (Integer) objLength;
        }

        Object objWidth = fieldWidth.getValue();
        Integer width = Integer.valueOf(0);

        if (objWidth instanceof Long) {
            Long value = (Long) objWidth;
            width = Integer.valueOf(value.intValue());
        } else if (objWidth instanceof Integer) {
            width = (Integer) objWidth;
        }

        Object objWide = fieldWide.getValue();
        BigDecimal wide = BigDecimal.ZERO;

        if (objWide instanceof Long) {
            Long value = (Long) objWide;
            wide = BigDecimal.valueOf(value.intValue());
        } else if (objWide instanceof BigDecimal) {
            wide = (BigDecimal) objWide;
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
        //
        Integer streetYearBuild = fieldStreetYearBuild.getYear();
        String streetLocation = radioStreetLocation.getSelectedData();
        String streetFlatness = radioStreetFlatness.getSelectedData();

        Integer streetStartKM = Integer.valueOf(0);
        Object objStart = fieldStreetStartKM.getValue();

        if (objStart instanceof Long) {
            Long value = (Long) objStart;
            streetStartKM = Integer.valueOf(value.intValue());
        } else if (objStart instanceof Integer) {
            streetStartKM = (Integer) objStart;
        }

        Integer streetEndKM = Integer.valueOf(0);
        Object objEnd = fieldStreetEndKM.getValue();

        if (objEnd instanceof Long) {
            Long value = (Long) objEnd;
            streetEndKM = Integer.valueOf(value.intValue());
        } else if (objEnd instanceof Integer) {
            streetEndKM = (Integer) objEnd;
        }

        String streetWidth = radioStreetWidth.getSelectedData();
        String streetSurface = radioStreetSurface.getSelectedData();
        String streetSide = radioStreetSide.getSelectedData();
        String streetTrotoire = radioStreetTrotoire.getSelectedData();
        String streetChannel = radioStreetChannel.getSelectedData();
        boolean streetSafetyZone = false;

        String safetyText = radioStreetSafetyZone.getSelectedData();

        if (safetyText.equals("Ada")) {
            streetSafetyZone = true;
        }

        //
        String bridgeStandarType = radioBridgeStandarType.getSelectedData();
        Integer bridgeYearBuild = fieldBridgeYearBuild.getYear();

        Integer bridgeLength = Integer.valueOf(0);
        Object objBLength = fieldBridgeLength.getValue();

        if (objBLength instanceof Long) {
            Long value = (Long) objBLength;
            bridgeLength = Integer.valueOf(value.intValue());
        } else if (objBLength instanceof Integer) {
            bridgeLength = (Integer) objBLength;
        }

        Integer bridgeWidth = Integer.valueOf(0);

        Object objBWidth = fieldBridgeWidth.getValue();

        if (objBWidth instanceof Long) {
            Long value = (Long) objBWidth;
            bridgeWidth = Integer.valueOf(value.intValue());
        } else if (objBWidth instanceof Integer) {
            bridgeWidth = (Integer) objBWidth;
        }


        String bridgePurpose = radioBridgePurpose.getSelectedData();
        String bridgeMainMaterial = radioBridgeMainMaterial.getSelectedData();
        String bridgeTopShape = radioBridgeTopShape.getSelectedData();
        String bridgeOtherShape = radioBridgeOtherShape.getSelectedData();
        String bridgeHeadShape = radioBridgeHeadShape.getSelectedData();
        String bridgeHeadMaterial = radioBridgeHeadMaterial.getSelectedData();
        String bridgePillar = radioBridgePillar.getSelectedData();
        String bridgePillarMaterial = radioBridgePillarMaterial.getSelectedData();

        //

        Integer irrigationYearBuild = fieldIrrigationYearBuild.getYear();
        String irrigationNetworkType = radioIrrigationNetworkType.getSelectedData();

        Integer irrigationLength = Integer.valueOf(0);
        Object objIrrLength = fieldIrrigationLength.getValue();

        if (objIrrLength instanceof Long) {
            Long value = (Long) objIrrLength;
            irrigationLength = Integer.valueOf(value.intValue());
        } else if (objIrrLength instanceof Integer) {
            irrigationLength = (Integer) objIrrLength;
        }

        Integer irrigationHeight = Integer.valueOf(0);
        Object objIrrHeight = fieldIrrigationHeight.getValue();

        if (objIrrHeight instanceof Long) {
            Long value = (Long) objIrrHeight;
            irrigationHeight = Integer.valueOf(value.intValue());
        } else if (objIrrHeight instanceof Integer) {
            irrigationHeight = (Integer) objIrrHeight;
        }


        Integer irrigationWidth = Integer.valueOf(0);
        Object objIrrWidth = fieldIrrigationWidth.getValue();

        if (objIrrWidth instanceof Long) {
            Long value = (Long) objIrrWidth;
            irrigationWidth = Integer.valueOf(value.intValue());
        } else if (objIrrWidth instanceof Integer) {
            irrigationWidth = (Integer) objIrrWidth;
        }

        String irrigationNetworkMaterial = radioIrrigationNetworkMaterial.getSelectedData();

        Condition irrigationCondition = null;
        Object irrConObj = comboIrrigationCondition.getSelectedItem();
        if (irrConObj instanceof Condition) {
            irrigationCondition = (Condition) irrConObj;
        }

        if (irrigationCondition != null) {
            if (irrigationCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationCondition = null;
            }
        }

        //

        String irrigationCarrierType = radioIrrigationCarrierType.getSelectedData();

        Integer irrigationCarrierLength = Integer.valueOf(0);
        Object objIrrCarrLength = fieldIrrigationCarrierLength.getValue();

        if (objIrrCarrLength instanceof Long) {
            Long value = (Long) objIrrCarrLength;
            irrigationCarrierLength = Integer.valueOf(value.intValue());
        } else if (objIrrCarrLength instanceof Integer) {
            irrigationCarrierLength = (Integer) objIrrCarrLength;
        }

        Integer irrigationCarrierHeight = Integer.valueOf(0);
        Object objIrrCarrHeight = fieldIrrigationCarrierHeight.getValue();

        if (objIrrCarrHeight instanceof Long) {
            Long value = (Long) objIrrCarrHeight;
            irrigationCarrierHeight = Integer.valueOf(value.intValue());
        } else if (objIrrCarrHeight instanceof Integer) {
            irrigationCarrierHeight = (Integer) objIrrCarrHeight;
        }

        Integer irrigationCarrierWidth = Integer.valueOf(0);
        Object objIrrCarrWidth = fieldIrrigationCarrierWidth.getValue();

        if (objIrrCarrWidth instanceof Long) {
            Long value = (Long) objIrrCarrWidth;
            irrigationCarrierWidth = Integer.valueOf(value.intValue());
        } else if (objIrrCarrWidth instanceof Integer) {
            irrigationCarrierWidth = (Integer) objIrrCarrWidth;
        }

        String irrigationCarrierMaterial = radioIrrigationCarrierMaterial.getSelectedData();

        Condition irrigationCarrierCondition = null;
        Object irrCarrConObj = comboIrrigationCarrierCondition.getSelectedItem();
        if (irrCarrConObj instanceof Condition) {
            irrigationCarrierCondition = (Condition) irrCarrConObj;
        }

        if (irrigationCarrierCondition != null) {
            if (irrigationCarrierCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationCarrierCondition = null;
            }
        }

        //

        Integer irrigationDebitThresholdWidth = Integer.valueOf(0);
        Object objIrrDTW = fieldIrrigationDebitThresholdWidth.getValue();

        if (objIrrDTW instanceof Long) {
            Long value = (Long) objIrrDTW;
            irrigationDebitThresholdWidth = Integer.valueOf(value.intValue());
        } else if (objIrrDTW instanceof Integer) {
            irrigationDebitThresholdWidth = (Integer) objIrrDTW;
        }

        Integer irrigationDebitCDG = Integer.valueOf(0);
        Object objIrrDCDG = fieldIrrigationDebitCDG.getValue();

        if (objIrrDCDG instanceof Long) {
            Long value = (Long) objIrrDCDG;
            irrigationDebitCDG = Integer.valueOf(value.intValue());
        } else if (objIrrDCDG instanceof Integer) {
            irrigationDebitCDG = (Integer) objIrrDCDG;
        }

        Integer irrigationDebitCipolleti = Integer.valueOf(0);
        Object objIrrDCipolleti = fieldIrrigationDebitCipolleti.getValue();

        if (objIrrDCipolleti instanceof Long) {
            Long value = (Long) objIrrDCipolleti;
            irrigationDebitCipolleti = Integer.valueOf(value.intValue());
        } else if (objIrrDCipolleti instanceof Integer) {
            irrigationDebitCipolleti = (Integer) objIrrDCipolleti;
        }

        Integer irrigationDebitLength = Integer.valueOf(0);
        Object objIrrDLength = fieldIrrigationDebitLength.getValue();

        if (objIrrDLength instanceof Long) {
            Long value = (Long) objIrrDLength;
            irrigationDebitLength = Integer.valueOf(value.intValue());
        } else if (objIrrDLength instanceof Integer) {
            irrigationDebitLength = (Integer) objIrrDLength;
        }

        Integer irrigationDebitHeight = Integer.valueOf(0);
        Object objIrrDHeight = fieldIrrigationDebitHeight.getValue();

        if (objIrrDHeight instanceof Long) {
            Long value = (Long) objIrrDHeight;
            irrigationDebitHeight = Integer.valueOf(value.intValue());
        } else if (objIrrDHeight instanceof Integer) {
            irrigationDebitHeight = (Integer) objIrrDHeight;
        }

        Integer irrigationDebitWidth = Integer.valueOf(0);
        Object objIrrDWidth = fieldIrrigationDebitWidth.getValue();

        if (objIrrDWidth instanceof Long) {
            Long value = (Long) objIrrDWidth;
            irrigationDebitWidth = Integer.valueOf(value.intValue());
        } else if (objIrrDWidth instanceof Integer) {
            irrigationDebitWidth = (Integer) objIrrDWidth;
        }

        String irrigationDebitMaterial = radioIrrigationDebitMaterial.getSelectedData();

        Condition irrigationDebitCondition = null;

        Object irrDebConObj = comboIrrigationDebitCondition.getSelectedItem();
        if (irrDebConObj instanceof Condition) {
            irrigationDebitCondition = (Condition) irrDebConObj;
        }

        if (irrigationDebitCondition != null) {
            if (irrigationDebitCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationDebitCondition = null;
            }
        }

        //

        Integer irrigationPoolUSBR3 = Integer.valueOf(0);
        Object objIrrPoolUSBR3 = fieldIrrigationPoolUSBR3.getValue();

        if (objIrrPoolUSBR3 instanceof Long) {
            Long value = (Long) objIrrPoolUSBR3;
            irrigationPoolUSBR3 = Integer.valueOf(value.intValue());
        } else if (objIrrPoolUSBR3 instanceof Integer) {
            irrigationPoolUSBR3 = (Integer) objIrrPoolUSBR3;
        }

        Integer irrigationPoolBlock = Integer.valueOf(0);
        Object objIrrPoolBlock = fieldIrrigationPoolBlock.getValue();

        if (objIrrPoolBlock instanceof Long) {
            Long value = (Long) objIrrPoolBlock;
            irrigationPoolBlock = Integer.valueOf(value.intValue());
        } else if (objIrrPoolBlock instanceof Integer) {
            irrigationPoolBlock = (Integer) objIrrPoolBlock;
        }

        Integer irrigationPoolUSBR4 = Integer.valueOf(0);
        Object objIrrPoolUSBR4 = fieldIrrigationPoolUSBR4.getValue();

        if (objIrrPoolUSBR4 instanceof Long) {
            Long value = (Long) objIrrPoolUSBR4;
            irrigationPoolUSBR4 = Integer.valueOf(value.intValue());
        } else if (objIrrPoolUSBR4 instanceof Integer) {
            irrigationPoolUSBR4 = (Integer) objIrrPoolUSBR4;
        }

        Integer irrigationPoolVlogtor = Integer.valueOf(0);
        Object objIrrPoolVlogtor = fieldIrrigationPoolVlogtor.getValue();

        if (objIrrPoolVlogtor instanceof Long) {
            Long value = (Long) objIrrPoolVlogtor;
            irrigationPoolVlogtor = Integer.valueOf(value.intValue());
        } else if (objIrrPoolVlogtor instanceof Integer) {
            irrigationPoolVlogtor = (Integer) objIrrPoolVlogtor;
        }

        Integer irrigationPoolLength = Integer.valueOf(0);
        Object objIrrPoolLength = fieldIrrigationPoolLength.getValue();

        if (objIrrPoolLength instanceof Long) {
            Long value = (Long) objIrrPoolLength;
            irrigationPoolLength = Integer.valueOf(value.intValue());
        } else if (objIrrPoolLength instanceof Integer) {
            irrigationPoolLength = (Integer) objIrrPoolLength;
        }

        Integer irrigationPoolHeight = Integer.valueOf(0);
        Object objIrrPoolHeight = fieldIrrigationPoolHeight.getValue();

        if (objIrrPoolHeight instanceof Long) {
            Long value = (Long) objIrrPoolHeight;
            irrigationPoolHeight = Integer.valueOf(value.intValue());
        } else if (objIrrPoolHeight instanceof Integer) {
            irrigationPoolHeight = (Integer) objIrrPoolHeight;
        }

        Integer irrigationPoolWidth = Integer.valueOf(0);
        Object objIrrPoolWidth = fieldIrrigationPoolWidth.getValue();

        if (objIrrPoolWidth instanceof Long) {
            Long value = (Long) objIrrPoolWidth;
            irrigationPoolWidth = Integer.valueOf(value.intValue());
        } else if (objIrrPoolWidth instanceof Integer) {
            irrigationPoolWidth = (Integer) objIrrPoolWidth;
        }

        String irrigationPoolMaterial = radioIrrigationPoolMaterial.getSelectedData();

        Condition irrigationPoolCondition = null;

        Object irrPoolConObj = comboIrrigationPoolCondition.getSelectedItem();
        if (irrPoolConObj instanceof Condition) {
            irrigationPoolCondition = (Condition) irrPoolConObj;
        }

        if (irrigationPoolCondition != null) {
            if (irrigationPoolCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationPoolCondition = null;
            }
        }

        //

        Integer irrigationHWFLotBlock = Integer.valueOf(0);
        Object objIrrHWFLotBlock = fieldIrrigationHWFLotBlock.getValue();

        if (objIrrHWFLotBlock instanceof Long) {
            Long value = (Long) objIrrHWFLotBlock;
            irrigationHWFLotBlock = Integer.valueOf(value.intValue());
        } else if (objIrrHWFLotBlock instanceof Integer) {
            irrigationHWFLotBlock = (Integer) objIrrHWFLotBlock;
        }

        Integer irrigationHWFTrapesium = Integer.valueOf(0);
        Object objIrrHWFTrapesium = fieldIrrigationHWFTrapesium.getValue();

        if (objIrrHWFTrapesium instanceof Long) {
            Long value = (Long) objIrrHWFTrapesium;
            irrigationHWFTrapesium = Integer.valueOf(value.intValue());
        } else if (objIrrHWFTrapesium instanceof Integer) {
            irrigationHWFTrapesium = (Integer) objIrrHWFTrapesium;
        }

        Integer irrigationHWFSlide = Integer.valueOf(0);
        Object objIrrHWFSlide = fieldIrrigationHWFSlide.getValue();

        if (objIrrHWFSlide instanceof Long) {
            Long value = (Long) objIrrHWFSlide;
            irrigationHWFSlide = Integer.valueOf(value.intValue());
        } else if (objIrrHWFSlide instanceof Integer) {
            irrigationHWFSlide = (Integer) objIrrHWFSlide;
        }

        Integer irrigationHWFTreeTop = Integer.valueOf(0);
        Object objIrrHWFTreeTop = fieldIrrigationHWFTreeTop.getValue();

        if (objIrrHWFTreeTop instanceof Long) {
            Long value = (Long) objIrrHWFTreeTop;
            irrigationHWFTreeTop = Integer.valueOf(value.intValue());
        } else if (objIrrHWFTreeTop instanceof Integer) {
            irrigationHWFTreeTop = (Integer) objIrrHWFTreeTop;
        }

        Integer irrigationHWFLength = Integer.valueOf(0);
        Object objIrrHWFLength = fieldIrrigationHWFLength.getValue();

        if (objIrrHWFLength instanceof Long) {
            Long value = (Long) objIrrHWFLength;
            irrigationHWFLength = Integer.valueOf(value.intValue());
        } else if (objIrrHWFLength instanceof Integer) {
            irrigationHWFLength = (Integer) objIrrHWFLength;
        }

        Integer irrigationHWFHeight = Integer.valueOf(0);
        Object objIrrHWFHeight = fieldIrrigationHWFHeight.getValue();

        if (objIrrHWFHeight instanceof Long) {
            Long value = (Long) objIrrHWFHeight;
            irrigationHWFHeight = Integer.valueOf(value.intValue());
        } else if (objIrrHWFHeight instanceof Integer) {
            irrigationHWFHeight = (Integer) objIrrHWFHeight;
        }

        Integer irrigationHWFWidth = Integer.valueOf(0);
        Object objIrrHWFWidth = fieldIrrigationHWFWidth.getValue();

        if (objIrrHWFWidth instanceof Long) {
            Long value = (Long) objIrrHWFWidth;
            irrigationHWFWidth = Integer.valueOf(value.intValue());
        } else if (objIrrHWFWidth instanceof Integer) {
            irrigationHWFWidth = (Integer) objIrrHWFWidth;
        }

        String irrigationHWFMaterial = radioIrrigationHWFMaterial.getSelectedData();

        Condition irrigationHWFCondition = null;

        Object irrHWFConObj = comboIrrigationHWFCondition.getSelectedItem();
        if (irrHWFConObj instanceof Condition) {
            irrigationHWFCondition = (Condition) irrHWFConObj;
        }

        if (irrigationHWFCondition != null) {
            if (irrigationHWFCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationHWFCondition = null;
            }
        }

        Integer irrigationProtectTransfer = Integer.valueOf(0);
        Object objIrrProtTransfer = fieldIrrigationProtectTransfer.getValue();

        if (objIrrProtTransfer instanceof Long) {
            Long value = (Long) objIrrProtTransfer;
            irrigationProtectTransfer = Integer.valueOf(value.intValue());
        } else if (objIrrProtTransfer instanceof Integer) {
            irrigationProtectTransfer = (Integer) objIrrProtTransfer;
        }

        Integer irrigationProtectDisposal = Integer.valueOf(0);
        Object objIrrProtDisposal = fieldIrrigationProtectDisposal.getValue();

        if (objIrrProtDisposal instanceof Long) {
            Long value = (Long) objIrrProtDisposal;
            irrigationProtectDisposal = Integer.valueOf(value.intValue());
        } else if (objIrrProtDisposal instanceof Integer) {
            irrigationProtectDisposal = (Integer) objIrrProtDisposal;
        }

        Integer irrigationProtectDrain = Integer.valueOf(0);
        Object objIrrProtDrain = fieldIrrigationProtectDrain.getValue();

        if (objIrrProtDrain instanceof Long) {
            Long value = (Long) objIrrProtDrain;
            irrigationProtectDrain = Integer.valueOf(value.intValue());
        } else if (objIrrProtDrain instanceof Integer) {
            irrigationProtectDrain = (Integer) objIrrProtDrain;
        }

        Integer irrigationProtectLength = Integer.valueOf(0);
        Object objIrrProtLength = fieldIrrigationProtectLength.getValue();

        if (objIrrProtLength instanceof Long) {
            Long value = (Long) objIrrProtLength;
            irrigationProtectLength = Integer.valueOf(value.intValue());
        } else if (objIrrProtLength instanceof Integer) {
            irrigationProtectLength = (Integer) objIrrProtLength;
        }

        Integer irrigationProtectHeight = Integer.valueOf(0);
        Object objIrrProtHeight = fieldIrrigationProtectHeight.getValue();

        if (objIrrProtHeight instanceof Long) {
            Long value = (Long) objIrrProtHeight;
            irrigationProtectHeight = Integer.valueOf(value.intValue());
        } else if (objIrrProtHeight instanceof Integer) {
            irrigationProtectHeight = (Integer) objIrrProtHeight;
        }

        Integer irrigationProtectWidth = Integer.valueOf(0);
        Object objIrrProtWidth = fieldIrrigationProtectWidth.getValue();

        if (objIrrProtWidth instanceof Long) {
            Long value = (Long) objIrrProtWidth;
            irrigationProtectWidth = Integer.valueOf(value.intValue());
        } else if (objIrrProtWidth instanceof Integer) {
            irrigationProtectWidth = (Integer) objIrrProtWidth;
        }

        String irrigationProtectMaterial = radioIrrigationProtectMaterial.getSelectedData();

        Condition irrigationProtectCondition = null;

        Object irrProtConObj = comboIrrigationProtectCondition.getSelectedItem();
        if (irrProtConObj instanceof Condition) {
            irrigationProtectCondition = (Condition) irrProtConObj;
        }

        if (irrigationProtectCondition != null) {
            if (irrigationProtectCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationProtectCondition = null;
            }
        }

        //

        Integer irrigationTappedFor = Integer.valueOf(0);
        Object objIrrTappedFor = fieldIrrigationTappedFor.getValue();

        if (objIrrTappedFor instanceof Long) {
            Long value = (Long) objIrrTappedFor;
            irrigationTappedFor = Integer.valueOf(value.intValue());
        } else if (objIrrTappedFor instanceof Integer) {
            irrigationTappedFor = (Integer) objIrrTappedFor;
        }

        Integer irrigationTappedSecond = Integer.valueOf(0);
        Object objIrrTappedSecond = fieldIrrigationTappedSecond.getValue();

        if (objIrrTappedSecond instanceof Long) {
            Long value = (Long) objIrrTappedSecond;
            irrigationTappedSecond = Integer.valueOf(value.intValue());
        } else if (objIrrTappedSecond instanceof Integer) {
            irrigationTappedSecond = (Integer) objIrrTappedSecond;
        }

        Integer irrigationTappedRegulator = Integer.valueOf(0);
        Object objIrrTappedRegulator = fieldIrrigationTappedRegulator.getValue();

        if (objIrrTappedRegulator instanceof Long) {
            Long value = (Long) objIrrTappedRegulator;
            irrigationTappedRegulator = Integer.valueOf(value.intValue());
        } else if (objIrrTappedRegulator instanceof Integer) {
            irrigationTappedRegulator = (Integer) objIrrTappedRegulator;
        }

        Integer irrigationTappedThird = Integer.valueOf(0);
        Object objIrrTappedThird = fieldIrrigationTappedThird.getValue();

        if (objIrrTappedThird instanceof Long) {
            Long value = (Long) objIrrTappedThird;
            irrigationTappedThird = Integer.valueOf(value.intValue());
        } else if (objIrrTappedThird instanceof Integer) {
            irrigationTappedThird = (Integer) objIrrTappedThird;
        }

        Integer irrigationTappedLength = Integer.valueOf(0);
        Object objIrrTappedLength = fieldIrrigationTappedLength.getValue();

        if (objIrrTappedLength instanceof Long) {
            Long value = (Long) objIrrTappedLength;
            irrigationTappedLength = Integer.valueOf(value.intValue());
        } else if (objIrrTappedLength instanceof Integer) {
            irrigationTappedLength = (Integer) objIrrTappedLength;
        }

        Integer irrigationTappedHeight = Integer.valueOf(0);
        Object objIrrTappedHeight = fieldIrrigationTappedHeight.getValue();

        if (objIrrTappedHeight instanceof Long) {
            Long value = (Long) objIrrTappedHeight;
            irrigationTappedHeight = Integer.valueOf(value.intValue());
        } else if (objIrrTappedHeight instanceof Integer) {
            irrigationTappedHeight = (Integer) objIrrTappedHeight;
        }

        Integer irrigationTappedWidth = Integer.valueOf(0);
        Object objIrrTappedWidth = fieldIrrigationTappedWidth.getValue();

        if (objIrrTappedWidth instanceof Long) {
            Long value = (Long) objIrrTappedWidth;
            irrigationTappedWidth = Integer.valueOf(value.intValue());
        } else if (objIrrTappedWidth instanceof Integer) {
            irrigationTappedWidth = (Integer) objIrrTappedWidth;
        }

        String irrigationTappedMaterial = radioIrrigationTappedMaterial.getSelectedData();

        Condition irrigationTappedCondition = null;

        Object irrTappedConObj = comboIrrigationTappedCondition.getSelectedItem();
        if (irrTappedConObj instanceof Condition) {
            irrigationTappedCondition = (Condition) irrTappedConObj;
        }

        if (irrigationTappedCondition != null) {
            if (irrigationTappedCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationTappedCondition = null;
            }
        }

        //

        Integer irrigationSupportLevee = Integer.valueOf(0);
        Object objIrrSuppLevee = fieldIrrigationSupportLevee.getValue();

        if (objIrrSuppLevee instanceof Long) {
            Long value = (Long) objIrrSuppLevee;
            irrigationSupportLevee = Integer.valueOf(value.intValue());
        } else if (objIrrSuppLevee instanceof Integer) {
            irrigationSupportLevee = (Integer) objIrrSuppLevee;
        }

        Integer irrigationSupportWash = Integer.valueOf(0);
        Object objIrrSuppWash = fieldIrrigationSupportWash.getValue();

        if (objIrrSuppWash instanceof Long) {
            Long value = (Long) objIrrSuppWash;
            irrigationSupportWash = Integer.valueOf(value.intValue());
        } else if (objIrrSuppWash instanceof Integer) {
            irrigationSupportWash = (Integer) objIrrSuppWash;
        }

        Integer irrigationSupportBridge = Integer.valueOf(0);
        Object objIrrSuppBridge = fieldIrrigationSupportBridge.getValue();

        if (objIrrSuppBridge instanceof Long) {
            Long value = (Long) objIrrSuppBridge;
            irrigationSupportBridge = Integer.valueOf(value.intValue());
        } else if (objIrrSuppBridge instanceof Integer) {
            irrigationSupportBridge = (Integer) objIrrSuppBridge;
        }

        Integer irrigationSupportKrib = Integer.valueOf(0);
        Object objIrrSuppKrib = fieldIrrigationSupportKrib.getValue();

        if (objIrrSuppKrib instanceof Long) {
            Long value = (Long) objIrrSuppKrib;
            irrigationSupportKrib = Integer.valueOf(value.intValue());
        } else if (objIrrSuppKrib instanceof Integer) {
            irrigationSupportKrib = (Integer) objIrrSuppKrib;
        }

        Integer irrigationSupportLength = Integer.valueOf(0);
        Object objIrrSuppLength = fieldIrrigationSupportLength.getValue();

        if (objIrrSuppLength instanceof Long) {
            Long value = (Long) objIrrSuppLength;
            irrigationSupportLength = Integer.valueOf(value.intValue());
        } else if (objIrrSuppLength instanceof Integer) {
            irrigationSupportLength = (Integer) objIrrSuppLength;
        }

        Integer irrigationSupportHeight = Integer.valueOf(0);
        Object objIrrSuppHeight = fieldIrrigationSupportHeight.getValue();

        if (objIrrSuppHeight instanceof Long) {
            Long value = (Long) objIrrSuppHeight;
            irrigationSupportHeight = Integer.valueOf(value.intValue());
        } else if (objIrrSuppHeight instanceof Integer) {
            irrigationSupportHeight = (Integer) objIrrSuppHeight;
        }

        Integer irrigationSupportWidth = Integer.valueOf(0);
        Object objIrrSuppWidth = fieldIrrigationSupportWidth.getValue();

        if (objIrrSuppWidth instanceof Long) {
            Long value = (Long) objIrrSuppWidth;
            irrigationSupportWidth = Integer.valueOf(value.intValue());
        } else if (objIrrSuppWidth instanceof Integer) {
            irrigationSupportWidth = (Integer) objIrrSuppWidth;
        }

        String irrigationSupportMaterial = radioIrrigationSupportMaterial.getSelectedData();

        Condition irrigationSupportCondition = null;

        Object irrSupportConObj = comboIrrigationSupportCondition.getSelectedItem();
        if (irrSupportConObj instanceof Condition) {
            irrigationSupportCondition = (Condition) irrSupportConObj;
        }

        if (irrigationSupportCondition != null) {
            if (irrigationSupportCondition.getIndex().equals(Long.valueOf(0))) {
                irrigationSupportCondition = null;
            }
        }

        if (itemCode.equals("") || itemName.equals("")) {
            errorString.append("<br>- Kode / Nama Barang Harus Lengkap</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan : " + errorString.toString() + "</html>");
        }

        ItemsNetwork network = new ItemsNetwork();
        network.setUnit(unit);
        network.setItemCode(itemCode);
        network.setItemName(itemName);
        network.setRegNumber(regNumber);
        network.setDocumentNumber(documentNumber);
        network.setDocumentDate(documentDate);
        network.setConstructionType(constructionType);
        network.setLength(length);
        network.setWidth(width);
        network.setWide(wide);
        network.setCondition(condition);
        network.setAddressLocation(addreasLocation);
        network.setUrban(urban);
        network.setSubUrban(subUrban);
        network.setPrice(price);
        network.setLandStatus(landStatus);
        network.setLandCode(landCode);
        network.setFundingSource(fundingSource);
        network.setAcquisitionWay(acquisitionWay);
        network.setDescription(description);
        //
        network.setStreetYearBuild(streetYearBuild);
        network.setStreetLocation(streetLocation);
        network.setStreetFlatness(streetFlatness);
        network.setStreetStartKM(streetStartKM);
        network.setStreetEndKM(streetEndKM);
        network.setStreetWidth(streetWidth);
        network.setStreetSurface(streetSurface);
        network.setStreetSide(streetSide);
        network.setStreetTrotoire(streetTrotoire);
        network.setStreetChannel(streetChannel);
        network.setStreetSafetyZone(streetSafetyZone);
        //
        network.setBridgeStandarType(bridgeStandarType);
        network.setBridgeYearBuild(bridgeYearBuild);
        network.setBridgeLength(bridgeLength);
        network.setBridgeWidth(bridgeWidth);
        network.setBridgePurpose(bridgePurpose);
        network.setBridgeMainMaterial(bridgeMainMaterial);
        network.setBridgeTopShape(bridgeTopShape);
        network.setBridgeOtherShape(bridgeOtherShape);
        network.setBridgeHeadShape(bridgeHeadShape);
        network.setBridgeHeadMaterial(bridgeHeadMaterial);
        network.setBridgePillar(bridgePillar);
        network.setBridgePillarMaterial(bridgePillarMaterial);
        //
        network.setIrrigationYearBuild(irrigationYearBuild);
        network.setIrrigationBuildingType(irrigationNetworkType);
        network.setIrrigationLength(irrigationLength);
        network.setIrrigationHeight(irrigationHeight);
        network.setIrrigationWidth(irrigationWidth);
        network.setIrrigationBuildingMaterial(irrigationNetworkMaterial);
        network.setIrrigationCondition(irrigationCondition);
        //
        network.setIrrigationCarrierType(irrigationCarrierType);
        network.setIrrigationCarrierLength(irrigationCarrierLength);
        network.setIrrigationCarrierHeight(irrigationCarrierHeight);
        network.setIrrigationCarrierWidth(irrigationCarrierWidth);
        network.setIrrigationCarrierMaterial(irrigationCarrierMaterial);
        network.setIrrigationCarrierCondition(irrigationCarrierCondition);
        //
        network.setIrrigationDebitThresholdWidth(irrigationDebitThresholdWidth);
        network.setIrrigationDebitCDG(irrigationDebitCDG);
        network.setIrrigationDebitCipolleti(irrigationDebitCipolleti);
        network.setIrrigationDebitLength(irrigationDebitLength);
        network.setIrrigationDebitHeight(irrigationDebitHeight);
        network.setIrrigationDebitWidth(irrigationDebitWidth);
        network.setIrrigationDebitMaterial(irrigationDebitMaterial);
        network.setIrrigationDebitCondition(irrigationDebitCondition);
        //
        network.setIrrigationPoolUSBR3(irrigationPoolUSBR3);
        network.setIrrigationPoolBlock(irrigationPoolBlock);
        network.setIrrigationPoolUSBR4(irrigationPoolUSBR4);
        network.setIrrigationPoolVlogtor(irrigationPoolVlogtor);
        network.setIrrigationPoolLength(irrigationPoolLength);
        network.setIrrigationPoolHeight(irrigationPoolHeight);
        network.setIrrigationPoolWidth(irrigationPoolWidth);
        network.setIrrigationPoolMaterial(irrigationPoolMaterial);
        network.setIrrigationPoolCondition(irrigationPoolCondition);
        //
        network.setIrrigationHWFLotBlock(irrigationHWFLotBlock);
        network.setIrrigationHWFTrapesium(irrigationHWFTrapesium);
        network.setIrrigationHWFSlide(irrigationHWFSlide);
        network.setIrrigationHWFTreeTop(irrigationHWFTreeTop);
        network.setIrrigationHWFLength(irrigationHWFLength);
        network.setIrrigationHWFHeight(irrigationHWFHeight);
        network.setIrrigationHWFWidth(irrigationHWFWidth);
        network.setIrrigationHWFMaterial(irrigationHWFMaterial);
        network.setIrrigationHWFCondition(irrigationHWFCondition);
        //
        network.setIrrigationProtectTransfer(irrigationProtectTransfer);
        network.setIrrigationProtectDisposal(irrigationProtectDisposal);
        network.setIrrigationProtectDrain(irrigationProtectDrain);
        network.setIrrigationProtectLength(irrigationProtectLength);
        network.setIrrigationProtectHeight(irrigationProtectHeight);
        network.setIrrigationProtectWidth(irrigationProtectWidth);
        network.setIrrigationProtectMaterial(irrigationProtectMaterial);
        network.setIrrigationProtectCondition(irrigationProtectCondition);
        //
        network.setIrrigationTappedFor(irrigationTappedFor);
        network.setIrrigationTappedSecond(irrigationTappedSecond);
        network.setIrrigationTappedRegulator(irrigationTappedRegulator);
        network.setIrrigationTappedThird(irrigationTappedThird);
        network.setIrrigationTappedLength(irrigationTappedLength);
        network.setIrrigationTappedHeight(irrigationTappedHeight);
        network.setIrrigationTappedWidth(irrigationTappedWidth);
        network.setIrrigationTappedMaterial(irrigationTappedMaterial);
        network.setIrrigationTappedCondition(irrigationTappedCondition);
        //
        network.setIrrigationSupportLevee(irrigationSupportLevee);
        network.setIrrigationSupportWash(irrigationSupportWash);
        network.setIrrigationSupportBridge(irrigationSupportBridge);
        network.setIrrigationSupportKrib(irrigationSupportKrib);
        network.setIrrigationSupportLength(irrigationSupportLength);
        network.setIrrigationSupportHeight(irrigationSupportHeight);
        network.setIrrigationSupportWidth(irrigationSupportWidth);
        network.setIrrigationSupportMaterial(irrigationSupportMaterial);
        network.setIrrigationSupportCondition(irrigationSupportCondition);
        //

        if (selectedItemsNetwork != null && btPanel.isEditstate()) {
            network.setUserName(selectedItemsNetwork.getUserName());
        } else {
            network.setUserName(userAccount.getUserName());
        }
        network.setLastUserName(userAccount.getUserName());

        return network;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        setTaskPaneState(taskArrays);
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboUnit.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        setTaskPaneState(taskArrays);
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
                ArrayList<ItemsNetwork> selectedItemsNetworks = table.getSelectedItemsNetworks();
                if (!selectedItemsNetworks.isEmpty()) {
                    logic.deleteItemsNetwork(mainframe.getSession(), selectedItemsNetworks);
                    table.removeItemsNetwork(selectedItemsNetworks);
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
            ItemsNetwork newItemsNetwork = getItemsNetwork();
            if (btPanel.isNewstate()) {
                newItemsNetwork = logic.insertItemsNetwork(mainframe.getSession(), newItemsNetwork);
                table.addItemsNetwork(newItemsNetwork);
                selectedItemsNetwork = newItemsNetwork;
                selectedItemsNetwork.setItemName(getLastItemsName());
                selectedItemsNetwork.setFullItemName(getCompleteItemsName());
            } else if (btPanel.isEditstate()) {
                newItemsNetwork = logic.updateItemsNetwork(mainframe.getSession(), selectedItemsNetwork, newItemsNetwork);
                table.updateSelectedItemsNetwork(newItemsNetwork);
                selectedItemsNetwork = newItemsNetwork;
                selectedItemsNetwork.setItemName(getLastItemsName());
                selectedItemsNetwork.setFullItemName(getCompleteItemsName());
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
                ItemsNetworkWorkbookReader wbr = new ItemsNetworkWorkbookReader();
                List<ItemsNetwork> items = wbr.loadXLSFile(file.getPath());

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
                    for (ItemsNetwork item : items) {
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

                        String conditonText = item.getConditionText();
                        if (conditonText != null) {
                            if (!conditonText.equals("")) {
                                if (conditonText.length() <= 2) {
                                    Comparator<Condition> conditionComp = new Comparator<Condition>() {

                                        public int compare(Condition o1, Condition o2) {
                                            return o1.getConditionCode().compareTo(o2.getConditionCode());
                                        }
                                    };

                                    if (!conditionMaster.isEmpty()) {
                                        Collections.sort(conditionMaster, conditionComp);
                                    }

                                    int index = Collections.binarySearch(conditionMaster, new Condition(conditonText, true), conditionComp);
                                    if (index >= 0) {
                                        item.setCondition(conditionMaster.get(index));
                                    }

                                } else {
                                    Comparator<Condition> conditionComp = new Comparator<Condition>() {

                                        public int compare(Condition o1, Condition o2) {
                                            return o1.getConditionName().compareTo(o2.getConditionName());
                                        }
                                    };

                                    if (!conditionMaster.isEmpty()) {
                                        Collections.sort(conditionMaster, conditionComp);
                                    }

                                    int index = Collections.binarySearch(conditionMaster, new Condition(conditonText, false), conditionComp);
                                    if (index >= 0) {
                                        item.setCondition(conditionMaster.get(index));
                                    }
                                }
                            }
                        }
                    }

                    if (importWorker != null) {
                        importWorker.cancel(true);
                    }
                    
                    oldCursor = mainframe.getCursor();
                    mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    importWorker = new ImportItemsNetwork(items);
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

        chooser.setSelectedFile(new File(ItemsNetworkWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            
            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            File file = chooser.getSelectedFile();
            ItemsNetworkWorkbookCreator wb = new ItemsNetworkWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsNetwork>(), unitMaster);
            wb.createXLSFile();
            
            mainframe.setCursor(oldCursor);
        }
    }

    public void onPrint() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        Unit unit = filterPanel.getUnit();

        if (unit != null) {
            ItemsNetworkJasper report = new ItemsNetworkJasper("Buku Inventaris Barang Jalan.Irigasi, dan Jaringan (KIB-D)", table.getItemsNetworks(), unit);
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

        chooser.setSelectedFile(new File(ItemsNetworkWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsNetwork> networks = table.getItemsNetworks();

            ItemsNetworkWorkbookCreator wb = new ItemsNetworkWorkbookCreator(mainframe, file.getPath(), networks, unitMaster);
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


        filterValues.add(new FilterValue("Konstruksi", "constructiontype", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Panjang", "length", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Lebar", "width", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Luas", "wide", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Letak/Lokasi/Alamat", "addresslocation", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Dokumen", "documentnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Tanggal Dokumen", "documentdate", FilterCardPanel.DATE_PANEL));
        filterValues.add(new FilterValue("Status Tanah", "landstatus", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Kode Tanah", "landcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal-Usul", "fundingsources", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Harga Perolehan", "buildingprice", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Kondisi Bangunan", "condition", FilterCardPanel.COMBO_LONG_PANEL, null, comboCategoryVal));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ItemsNetworkTable extends JXTable {

        private ItemsNetworkTableModel model;

        public ItemsNetworkTable() {
            model = new ItemsNetworkTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            ColumnGroup group2 = new ColumnGroup("<html><center><b>Dokumen</b></center>");
            group2.add(colModel.getColumn(10));
            group2.add(colModel.getColumn(11));

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

            worker = new LoadItemsNetwork(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsNetwork getSelectedItemsNetwork() {
            ArrayList<ItemsNetwork> selectedItemsNetworks = getSelectedItemsNetworks();
            return selectedItemsNetworks.get(0);
        }

        public ArrayList<ItemsNetwork> getItemsNetworks() {
            return model.getItemsNetworks();
        }

        public ArrayList<ItemsNetwork> getSelectedItemsNetworks() {

            ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsNetwork network = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsNetwork) {
                            network = (ItemsNetwork) obj;
                            networks.add(network);
                        }
                    }
                }
            }

            return networks;
        }

        public void updateSelectedItemsNetwork(ItemsNetwork network) {
            model.updateRow(getSelectedItemsNetwork(), network);
        }

        public void removeItemsNetwork(ArrayList<ItemsNetwork> networks) {
            if (!networks.isEmpty()) {
                for (ItemsNetwork network : networks) {
                    model.remove(network);
                }
            }

        }

        public void addItemsNetwork(ArrayList<ItemsNetwork> networks) {
            if (!networks.isEmpty()) {
                for (ItemsNetwork network : networks) {
                    model.add(network);
                }
            }
        }

        public void addItemsNetwork(ItemsNetwork network) {
            model.add(network);
        }

        public void insertEmptyItemsNetwork() {
            addItemsNetwork(new ItemsNetwork());
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

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsNetworkTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 20;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Konstruksi</b></center>",
            "<html><center><b>Panjang<br>(Km)</br></b></center>",
            "<html><center><b>Lebar<br>(M)</br></b></center>",
            "<html><center><b>Luas<br>(M<sup>2</sup>)</br></b></center>",
            "<html><center><b>Letak/Lokasi/<br>Alamat</br></b></center>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><center><b>Status<br>Tanah</br></b></center>",
            "<html><center><b>Nomor Kode<br>Tanah</br></b></center>",
            "<html><center><b>Asal-Usul<br>Pembiayaan</br></b></center>",
            "<html><center><b>Harga Perolehan<br>(Rp.)</br></b></center>",
            "<html><center><b>Kondisi<br>Bangunan</br><br>(RB,R,KB,B,SB)</br></b></center>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

        public ItemsNetworkTableModel() {
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
            } else if (columnIndex == 7 || columnIndex == 8 || columnIndex == 15) {
                return BigDecimal.class;
            } else if (columnIndex == 10) {
                return Date.class;
            } else if (columnIndex == 1) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ItemsNetwork> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            networks.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsNetwork network) {
            insertRow(getRowCount(), network);
        }

        public void insertRow(int row, ItemsNetwork network) {
            networks.add(row, network);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsNetwork oldItemsNetwork, ItemsNetwork newItemsNetwork) {
            int index = networks.indexOf(oldItemsNetwork);
            networks.set(index, newItemsNetwork);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsNetwork network) {
            int row = networks.indexOf(network);
            networks.remove(network);
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
                networks.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                networks.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            networks.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (networks.get(i) == null) {
                    networks.set(i, new ItemsNetwork());
                }
            }
        }

        public ArrayList<ItemsNetwork> getItemsNetworks() {
            return networks;
        }

        @Override
        public int getRowCount() {
            return networks.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsNetwork getItemsNetwork(int row) {
            if (!networks.isEmpty()) {
                return networks.get(row);
            }
            return new ItemsNetwork();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsNetwork network = getItemsNetwork(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        network.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsNetwork) {
                        network = (ItemsNetwork) aValue;
                    }
                    break;
                case 3:
                    network.setItemCode((String) aValue);
                    break;
                case 4:
                    network.setRegNumber((String) aValue);
                    break;
                case 5:
                    network.setConstructionType((String) aValue);
                    break;
                case 6:
                    network.setLength((Integer) aValue);
                    break;
                case 7:
                    network.setWidth((Integer) aValue);
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        network.setWide((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    network.setAddressLocation((String) aValue);
                    break;
                case 10:
                    network.setDocumentDate((Date) aValue);
                    break;
                case 11:
                    network.setDocumentNumber((String) aValue);
                    break;
                case 12:
                    network.setLandStatus((String) aValue);
                    break;
                case 13:
                    network.setLandCode((String) aValue);
                    break;
                case 14:
                    network.setAcquisitionWay((String) aValue);
                    break;
                case 15:
                    if (aValue instanceof BigDecimal) {
                        network.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 16:
                    if (aValue instanceof Condition) {
                        network.setCondition((Condition) aValue);
                    }
                    break;
                case 17:
                    network.setDescription((String) aValue);
                    break;
                case 18:
                    network.setUserName((String) aValue);
                    break;
                case 19:
                    network.setLastUserName((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsNetwork network = getItemsNetwork(row);
            switch (column) {
                case 0:
                    toBeDisplayed = network.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = network;
                    break;
                case 3:
                    toBeDisplayed = network.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = network.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = network.getConstructionType();
                    break;
                case 6:
                    toBeDisplayed = network.getLength();
                    break;
                case 7:
                    toBeDisplayed = network.getWidth();
                    break;
                case 8:
                    toBeDisplayed = network.getWide();
                    break;
                case 9:
                    StringBuilder builder = new StringBuilder();
                    builder.append(network.getAddressLocation());

                    if (network.getUrban() != null) {
                        Region urban = network.getUrban();
                        builder.append(" Kecamatan ").append(urban.getRegionName());
                    }

                    if (network.getSubUrban() != null) {
                        Region subUrban = network.getSubUrban();
                        builder.append(" Kelurahan ").append(subUrban.getRegionName());
                    }

                    toBeDisplayed = builder.toString();
                    break;
                case 10:
                    toBeDisplayed = network.getDocumentDate();
                    break;
                case 11:
                    toBeDisplayed = network.getDocumentNumber();
                    break;
                case 12:
                    toBeDisplayed = network.getLandStatus();
                    break;
                case 13:
                    toBeDisplayed = network.getLandCode();
                    break;
                case 14:
                    toBeDisplayed = network.getAcquisitionWay();
                    break;
                case 15:
                    toBeDisplayed = network.getPrice();
                    break;
                case 16:
                    toBeDisplayed = network.getCondition();
                    break;
                case 17:
                    toBeDisplayed = network.getDescription();
                    break;
                case 18:
                    toBeDisplayed = network.getUserName();
                    break;
                case 19:
                    toBeDisplayed = network.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsNetwork extends SwingWorker<ItemsNetworkTableModel, ItemsNetwork> {

        private ItemsNetworkTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsNetwork(String modifier) {
            this.model = (ItemsNetworkTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsNetwork> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsNetwork network : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-D " + network.getItemName());
                model.add(network);
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
        protected ItemsNetworkTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsNetwork> networks = logic.getItemsNetwork(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!networks.isEmpty()) {
                    for (int i = 0; i < networks.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / networks.size();

                        ItemsNetwork network = networks.get(i);

                        if (network.getUnit() != null) {
                            network.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), network.getUnit()));
                        }

                        if (network.getCondition() != null) {
                            network.setCondition(mLogic.getCondition(mainframe.getSession(), network.getCondition()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(network.getItemCode());
                        category.setCategoryName(network.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_NETWORK);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        network.setItemName(getLastItemsName(iLookUp));
                        network.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(network);
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
                JXErrorPane.showDialog(ItemsNetworkPanel.this, info);
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

    private class ImportItemsNetwork extends SwingWorker<ItemsNetworkTableModel, ItemsNetwork> {

        private ItemsNetworkTableModel model;
        private Exception exception;
        private List<ItemsNetwork> data;

        public ImportItemsNetwork(List<ItemsNetwork> data) {
            this.model = (ItemsNetworkTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsNetwork> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsNetwork land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-D " + land.getItemName());
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
        protected ItemsNetworkTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    ItemsNetwork inw = data.get(0);
                    Unit inwUnit = inw.getUnit();

                    if (inwUnit != null) {
                        table.clear();
                        logic.deleteItemsNetwork(mainframe.getSession(), inwUnit);
                    }
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsNetwork network = data.get(i);
                        
                        network.setUserName(userAccount.getUserName());
                        network.setLastUserName(userAccount.getUserName());

                        synchronized (network) {
                            network = logic.insertItemsNetwork(mainframe.getSession(), network);
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(network.getItemCode());
                        category.setCategoryName(network.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_NETWORK);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        network.setItemName(getLastItemsName(iLookUp));
                        network.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(network);
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
                JXErrorPane.showDialog(ItemsNetworkPanel.this, info);
            }

            table.packAll();
            
            mainframe.setCursor(oldCursor);

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
