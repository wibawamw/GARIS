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
import org.jdesktop.swingx.table.TableColumnExt;
import org.motekar.lib.common.listener.ButtonLoadListener;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.LoadButtonPanel;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsLandJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsLandWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsLandWorkbookReader;
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
public class ItemsLandPanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

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
    // Main Data
    private JXComboBox comboUnit = new JXComboBox();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXFormattedTextField fieldWide;
    private JXTextField fieldAddressLocation = new JXTextField();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JCheckBox checkUnknown = new JCheckBox("Tidak Diketahui");
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    // Data Kepemilikan
    private RadioButtonPanel radioOwnerShipStateName = new RadioButtonPanel(new String[]{"SHM", "HGB", "HGU", "Girik", "HP",
                "AJB", "SPH", "SPPT", "Hak Ulayat", "Hibah", "Negara", "Perorangan", "Sengketa", "Tidak Ada", "Lainnya"}, 3, 5);
    private JXTextField fieldOwnerShipStateNumber = new JXTextField();
    private JXDatePicker fieldOwnerShipDate = new JXDatePicker();
    private JXTextField fieldOwnerShipIssued = new JXTextField();
    private JXTextField fieldOwner = new JXTextField();
    private RadioButtonPanel radioOwnerShipStatus = new RadioButtonPanel(new String[]{"Milik", "Pakai", "Kelola"}, 0, 3);
    private RadioButtonPanel radioOwnerShipRelation = new RadioButtonPanel(new String[]{"Pemda", "Pusat", "Departemen", "Lainnya"}, 0, 4);
    private RadioButtonPanel radioOwnerShipOccupancy = new RadioButtonPanel(new String[]{"Dihuni", "Kosong"}, 0, 2);
    private RadioButtonPanel radioOwnerShipOccupying = new RadioButtonPanel(new String[]{"Struktural", "Pemilik", "Sewa", "Lainnya"}, 0, 4);
    // Data Dokumen
    private JXTextField fieldLandCertificateNumber = new JXTextField();
    private JXDatePicker fieldLandCertificateDate = new JXDatePicker();
    private JXTextField fieldLandTaxNumber = new JXTextField();
    private JXDatePicker fieldLandTaxDate = new JXDatePicker();
    private JXTextField fieldBuildPermitNumber = new JXTextField();
    private JXDatePicker fieldBuildPermitDate = new JXDatePicker();
    private JXTextField fieldAdvisPlanningNumber = new JXTextField();
    private JXDatePicker fieldAdvisPlanningDate = new JXDatePicker();
    // Data KOndisi
    private RadioButtonPanel radioLandCondition = new RadioButtonPanel(new String[]{"Matang", "Kebun", "Sawah", "Tambak", "Rawa", "Hutan"}, 0, 6);
    private RadioButtonPanel radioShape = new RadioButtonPanel(new String[]{"Segi Empat", "Tidak Beraturan", "Segi Empat Tidak Beraturan"}, 0, 3);
    private RadioButtonPanel radioTopographCondition = new RadioButtonPanel(new String[]{"Datar", "Landai", "Terjal", "Bergelombang"}, 0, 4);
    private RadioButtonPanel radioCurrentUse = new RadioButtonPanel(new String[]{"Bangunan", "Rumah Tinggal",
                "Kebun", "Jembatan", "Hutan", "Jalan", "Saluran/Irigasi", "Bangunan Air", "Fasum", "Industri",
                "Komersil", "Tidak Ada"}, 3, 4);
    private RadioButtonPanel radioCloserRoad = new RadioButtonPanel(new String[]{"Provinsi", "Kabupaten", "Desa"}, 0, 3);
    private RadioButtonPanel radioRoadSurface = new RadioButtonPanel(new String[]{"Aspal", "Beton", "Perkerasan Tanah"}, 0, 3);
    private RadioButtonPanel radioHeightAbove = new RadioButtonPanel(new String[]{"< 30 cm", "30 - 100 cm", "> 100 cm"}, 0, 3);
    private RadioButtonPanel radioHeightUnder = new RadioButtonPanel(new String[]{"< 30 cm", "30 - 100 cm", "> 100 cm"}, 0, 3);
    private RadioButtonPanel radioElevation = new RadioButtonPanel(new String[]{"Lebih Tinggi dari Jalan", "Lebih Rendah dari Jalan"}, 0, 2);
//    private RadioButtonPanel radioAllotment = new RadioButtonPanel(new String[]{"Rumah Tinggal", "Industri", "Komersil", "Fasum", "Lainnya"}, 0, 5);
    private JXTextField fieldAllotment = new JXTextField();
    private JXFormattedTextField fieldPriceBasedChief;
    private JXFormattedTextField fieldPriceBasedNJOP;
    private JXFormattedTextField fieldPriceBasedExam;
    private JXTextArea fieldPriceDescription = new JXTextArea();
    // Data Lokasi dan Lingkungan
    private RadioButtonPanel radioLocation = new RadioButtonPanel(new String[]{"Didalam Real Estate", "Diluar Real Estate"}, 0, 2);
    private RadioButtonPanel radioPosition = new RadioButtonPanel(new String[]{"Antara", "Sudut/Hoek", "Tusuk Sate"}, 0, 3);
    private RadioButtonPanel radioRoadAccess = new RadioButtonPanel(new String[]{"Jalan Raya", "Jalan Penghubung",
                "Jalan Boulevard", "Jalan Lingkungan", "Gang", "Setapak"}, 2, 3);
    private RadioButtonPanel radioRoadWidth = new RadioButtonPanel(new String[]{"1 - 3 m", "3 - 5 m", "5 - 10 m", "> 10 m"}, 0, 4);
    private RadioButtonPanel radioPavement = new RadioButtonPanel(new String[]{"Aspal", "Beton", "Paving/Batu", "Tanah"}, 0, 4);
    private RadioButtonPanel radioTraffic = new RadioButtonPanel(new String[]{"Sangat Padat", "Padat", "Sedang", "Kurang"}, 0, 4);
    private RadioButtonPanel radioTopographLocation = new RadioButtonPanel(new String[]{"> Tinggi", "Tinggi", "Sejajar", "Rendah"}, 0, 4);
    private RadioButtonPanel radioEnvironment = new RadioButtonPanel(new String[]{"Perumahan", "Industri", "Komersial"}, 0, 4);
    private RadioButtonPanel radioDensity = new RadioButtonPanel(new String[]{"> Rapat", "Rapat", "Cukup", "Kurang"}, 0, 4);
    private RadioButtonPanel radioSocialLevels = new RadioButtonPanel(new String[]{"Atas", "Menengah", "Bawah", "Campuran"}, 0, 4);
    private RadioButtonPanel radioFacilities = new RadioButtonPanel(new String[]{"> Lengkap", "Lengkap", "Memadai", "Kurang"}, 0, 4);
    private RadioButtonPanel radioDrainage = new RadioButtonPanel(new String[]{"Sangat Baik", "Baik", "Kurang", "< Kurang"}, 0, 4);
    private RadioButtonPanel radioPublicTransportation = new RadioButtonPanel(new String[]{"Mudah", "Cukup", "Sulit"}, 0, 3);
    private RadioButtonPanel radioSecurity = new RadioButtonPanel(new String[]{"Lengkap", "Memadai", "< Memadai", "Tidak Memadai"}, 0, 4);
    // Data Pengaruh Lokasi dan Lingkungan
    private RadioButtonPanel radioSecurityDisturbance = new RadioButtonPanel(new String[]{"Sangat Rawan", "Rawan", "Aman", "Sangat Aman"}, 0, 4);
    private RadioButtonPanel radioFloodDangers = new RadioButtonPanel(new String[]{"Sangat Rawan", "Rawan Limpasan", "Bebas Banjir"}, 0, 3);
    private RadioButtonPanel radioLocationEffect = new RadioButtonPanel(new String[]{"Positif", "Negatif"}, 0, 2);
    private RadioButtonPanel radioEnvironmentalInfluences = new RadioButtonPanel(new String[]{"Positif", "Negatif"}, 0, 2);
    // Data Faktor yang Berpengaruh dalam Penilaian
    private RadioButtonPanel radioAllotmentPoint = new RadioButtonPanel(new String[]{"Sesuai", "Tidak Sesuai"}, 0, 2);
    private RadioButtonPanel radioUtilizationPoint = new RadioButtonPanel(new String[]{"Sesuai", "Tidak Sesuai"}, 0, 2);
    private RadioButtonPanel radioLocationPoint = new RadioButtonPanel(new String[]{"Strategis", "Kurang Strategis"}, 0, 2);
    private RadioButtonPanel radioAccessionPoint = new RadioButtonPanel(new String[]{"Mudah", "Sulit"}, 0, 2);
    private RadioButtonPanel radioNursingPoint = new RadioButtonPanel(new String[]{"Baik", "Kurang Baik"}, 0, 2);
    private RadioButtonPanel radioSoilConditionsPoint = new RadioButtonPanel(new String[]{"Siap Bangung", "Belum Siap Bangun"}, 0, 2);
    private RadioButtonPanel radioMarketInterestPoint = new RadioButtonPanel(new String[]{"Baik", "Kurang Baik"}, 0, 2);
    // Data Pembanding
    private JXFormattedTextField fieldComparativePrice;
    private JXFormattedTextField fieldComparativeWide;
    private JXTextField fieldComparativeSource = new JXTextField();
    private JXFormattedTextField fieldComparativePrice2;
    private JXFormattedTextField fieldComparativeWide2;
    private JXTextField fieldComparativeSource2 = new JXTextField();
    private JXFormattedTextField fieldComparativePrice3;
    private JXFormattedTextField fieldComparativeWide3;
    private JXTextField fieldComparativeSource3 = new JXTextField();
    private JXFormattedTextField fieldComparativePrice4;
    private JXFormattedTextField fieldComparativeWide4;
    private JXTextField fieldComparativeSource4 = new JXTextField();
    private JXFormattedTextField fieldComparativePrice5;
    private JXFormattedTextField fieldComparativeWide5;
    private JXTextField fieldComparativeSource5 = new JXTextField();
    private JXTextField fieldLandAddress = new JXTextField();
    private JYearChooser fieldNJOPYear = new JYearChooser();
    private JXFormattedTextField fieldNJOPLandwide;
    private JXTextField fieldNJOPLandClass = new JXTextField();
    private JXFormattedTextField fieldNJOPLandValue;
    //
    private ItemsLandTable table = new ItemsLandTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsLand worker;
    private ProgressListener progressListener;
    private ItemsLand selectedItemsLand = null;
    private ItemsCategory selectedItemsCategory = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportItemsLand importWorker;
    //
    private JScrollPane mainSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    //
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    //
    private EventList<Region> urbanRegions;
    private ItemsCategory parentCategory = null;
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    private List<Region> regionMaster = new ArrayList<Region>();
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor ;

    public ItemsLandPanel(ArchieveMainframe mainframe) {
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

        fieldOwnerShipDate.setFormats("dd/MM/yyyy");
        fieldLandCertificateDate.setFormats("dd/MM/yyyy");
        fieldLandTaxDate.setFormats("dd/MM/yyyy");
        fieldBuildPermitDate.setFormats("dd/MM/yyyy");
        fieldAdvisPlanningDate.setFormats("dd/MM/yyyy");

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
                    ArrayList<ItemsLand> lands = table.getSelectedItemsLands();
                    if (!lands.isEmpty()) {
                        if (lands.size() == 1) {
                            selectedItemsLand = lands.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsLand.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsLand.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_LAND);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsLand = null;
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
                        ItemsCategory.ITEMS_LAND, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.toString());
                }
            }
        });

        checkUnknown.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());
            }
        });

        checkUnknown.setFocusPainted(false);

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
                    CustomOptionDialog.showDialog(ItemsLandPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
            ArrayList<Unit> units = lw.get();

            unitMaster.addAll(units);

            filterPanel.loadComboUnit(units);

            units.add(0, new Unit());
            comboUnit.setModel(new ListComboBoxModel<Unit>(units));
            AutoCompleteDecorator.decorate(comboUnit);

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

            urbanRegions.add(0, new Region());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboSubUrban, urbanRegions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

            filterPanel.loadComboSubUrban(urbanRegions);

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
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_LAND);
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

        fieldPriceBasedChief = new JXFormattedTextField();
        fieldPriceBasedChief.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPriceBasedChief.setHorizontalAlignment(JLabel.RIGHT);

        fieldPriceBasedNJOP = new JXFormattedTextField();
        fieldPriceBasedNJOP.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPriceBasedNJOP.setHorizontalAlignment(JLabel.RIGHT);

        fieldPriceBasedExam = new JXFormattedTextField();
        fieldPriceBasedExam.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPriceBasedExam.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativePrice = new JXFormattedTextField();
        fieldComparativePrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativePrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativePrice2 = new JXFormattedTextField();
        fieldComparativePrice2.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativePrice2.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativePrice3 = new JXFormattedTextField();
        fieldComparativePrice3.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativePrice3.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativePrice4 = new JXFormattedTextField();
        fieldComparativePrice4.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativePrice4.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativePrice5 = new JXFormattedTextField();
        fieldComparativePrice5.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativePrice5.setHorizontalAlignment(JLabel.RIGHT);

        fieldComparativeWide = new JXFormattedTextField();
        fieldComparativeWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativeWide.setHorizontalAlignment(JLabel.LEFT);

        fieldComparativeWide2 = new JXFormattedTextField();
        fieldComparativeWide2.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativeWide2.setHorizontalAlignment(JLabel.LEFT);

        fieldComparativeWide3 = new JXFormattedTextField();
        fieldComparativeWide3.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativeWide3.setHorizontalAlignment(JLabel.LEFT);

        fieldComparativeWide4 = new JXFormattedTextField();
        fieldComparativeWide4.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativeWide4.setHorizontalAlignment(JLabel.LEFT);

        fieldComparativeWide5 = new JXFormattedTextField();
        fieldComparativeWide5.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldComparativeWide5.setHorizontalAlignment(JLabel.LEFT);

        fieldNJOPLandwide = new JXFormattedTextField();
        fieldNJOPLandwide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldNJOPLandwide.setHorizontalAlignment(JLabel.LEFT);

        fieldNJOPLandValue = new JXFormattedTextField();
        fieldNJOPLandValue.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldNJOPLandValue.setHorizontalAlignment(JLabel.RIGHT);

    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private JXTaskPaneContainer createTaskPaneContainer() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Data Utama");
        task.add(createMainDataPanel());
        task.setAnimated(false);

        JXTaskPane task2 = new JXTaskPane();
        task2.setTitle("Data Kepemilikan");
        task2.add(createOwnerShipPanel());
        task2.setAnimated(false);

        JXTaskPane task3 = new JXTaskPane();
        task3.setTitle("Dokumen");
        task3.add(createDocumentPanel());
        task3.setAnimated(false);

        JXTaskPane task4 = new JXTaskPane();
        task4.setTitle("Kondisi");
        task4.add(createConditionPanel());
        task4.setAnimated(false);

        JXTaskPane task5 = new JXTaskPane();
        task5.setTitle("Lokasi dan Lingkungan");
        task5.add(createEnvironmentPanel());
        task5.setAnimated(false);

        JXTaskPane task6 = new JXTaskPane();
        task6.setTitle("Pengaruh Lokasi dan Lingkungan");
        task6.add(createInfluencePanel());
        task6.setAnimated(false);

        JXTaskPane task7 = new JXTaskPane();
        task7.setTitle("Faktor Yang Berpengaruh Dalam Penilaian");
        task7.add(createPointPanel());
        task7.setAnimated(false);

        JXTaskPane task8 = new JXTaskPane();
        task8.setTitle("Sumber Harga (Pembanding)");
        task8.add(createMarketPanel());
        task8.setAnimated(false);

        container.add(task);
        container.add(task2);
        container.add(task3);
        container.add(task4);
        container.add(task5);
        container.add(task6);
        container.add(task7);
        container.add(task8);

        taskArrays.add(task);
        taskArrays.add(task2);
        taskArrays.add(task3);
        taskArrays.add(task4);
        taskArrays.add(task5);
        taskArrays.add(task6);
        taskArrays.add(task7);
        taskArrays.add(task8);

        setTaskPaneState();

        addTaskPaneListener();

        return container;
    }

    private void setAllTaskPaneCollapsed(JXTaskPane paneExcept) {

        if (!taskArrays.isEmpty()) {
            for (JXTaskPane taskPane : taskArrays) {
                if (taskPane.getTitle().equals(paneExcept.getTitle())) {
                    taskPane.setCollapsed(false);
                } else {
                    taskPane.setCollapsed(true);
                }
            }
        }
    }

    private void setTaskPaneState() {

        if (!taskArrays.isEmpty()) {
            for (JXTaskPane taskPane : taskArrays) {
                if (taskPane.getTitle().equals("Data Utama")) {
                    taskPane.setCollapsed(false);
                } else {
                    taskPane.setCollapsed(true);
                }
            }
        }
    }

    private void addTaskPaneListener() {
        if (!taskArrays.isEmpty()) {
            for (JXTaskPane taskPane : taskArrays) {
                taskPane.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Object source = e.getSource();
                        if (source instanceof JXTaskPane) {
                            JXTaskPane pane = (JXTaskPane) source;
                            if (!pane.isCollapsed()) {
                                setAllTaskPaneCollapsed(pane);
                            } else {
                                setTaskPaneState();
                            }

                            JScrollBar sb = mainSCPane.getVerticalScrollBar();

                            int index = taskArrays.indexOf(pane);
                            int size = taskArrays.size();
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

    private Component createMainDataPanel() {
        FormLayout lm = new FormLayout(
                "50px,right:pref,10px, pref,5px,100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "30px");
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

        builder.addLabel("Luas (m2)", cc.xy(2, 10));
        builder.add(fieldWide, cc.xyw(4, 10, 3));

        builder.addLabel("Tahun Pengadaan", cc.xy(2, 12));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 12, 3));
        builder.add(checkUnknown, cc.xyw(8, 12, 1));

        builder.addLabel("Letak / Alamat", cc.xy(2, 14));
        builder.add(fieldAddressLocation, cc.xyw(4, 14, 5));

        builder.addLabel("Kecamatan", cc.xy(2, 16));
        builder.add(comboUrban, cc.xyw(4, 16, 5));

        builder.addLabel("Kelurahan", cc.xy(2, 18));
        builder.add(comboSubUrban, cc.xyw(4, 18, 5));

        builder.addLabel("Hak", cc.xy(2, 20));
        builder.add(radioOwnerShipStatus, cc.xyw(4, 20, 5));

        builder.addLabel("Nomor Sertifikat", cc.xy(2, 22));
        builder.add(fieldLandCertificateNumber, cc.xyw(4, 22, 5));

        builder.addLabel("Tanggal Sertifikat", cc.xy(2, 24));
        builder.add(fieldLandCertificateDate, cc.xyw(4, 24, 3));

        builder.addLabel("Penggunaan", cc.xy(2, 26));
        builder.add(fieldAllotment, cc.xyw(4, 26, 5));

        builder.addLabel("Asal-Usul", cc.xy(2, 28));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 28, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 30));
        builder.add(fieldFundingSource, cc.xyw(4, 30, 5));

        builder.addLabel("Harga Satuan", cc.xy(2, 32));
        builder.add(fieldPrice, cc.xyw(4, 32, 5));

        builder.addLabel("Keterangan", cc.xy(2, 34));
        builder.add(scPane, cc.xywh(4, 34, 5, 8));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        return panel;
    }

    private Component createOwnerShipPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Status", cc.xy(2, 2));
        builder.add(radioOwnerShipStateName, cc.xywh(4, 2, 3, 3));

        builder.addLabel("Nomor Status", cc.xy(2, 6));
        builder.add(fieldOwnerShipStateNumber, cc.xyw(4, 6, 3));

        builder.addLabel("Tanggal Dikeluarkan", cc.xy(2, 8));
        builder.add(fieldOwnerShipDate, cc.xyw(4, 8, 1));

        builder.addLabel("Dikeluarkan Oleh", cc.xy(2, 10));
        builder.add(fieldOwnerShipIssued, cc.xyw(4, 10, 3));

        builder.addLabel("Tercatat Atas Nama", cc.xy(2, 12));
        builder.add(fieldOwner, cc.xyw(4, 12, 3));

        builder.addLabel("Hubungan Kepemilikan", cc.xy(2, 14));
        builder.add(radioOwnerShipRelation, cc.xyw(4, 14, 3));

        builder.addLabel("Okupansi", cc.xy(2, 16));
        builder.add(radioOwnerShipOccupancy, cc.xyw(4, 16, 3));

        builder.addLabel("Dasar Menempati", cc.xy(2, 18));
        builder.add(radioOwnerShipOccupying, cc.xyw(4, 18, 3));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createDocumentPanel() {
        FormLayout lm = new FormLayout(
                "100px,pref,20px, 100px,5px,fill:default:grow,100px",
                "30px,"
                + "pref,5px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("PBB", cc.xyw(2, 2, 5));

        builder.addLabel("     Nomor", cc.xy(2, 4));
        builder.add(fieldLandTaxNumber, cc.xyw(4, 4, 3));

        builder.addLabel("     Tanggal", cc.xy(2, 6));
        builder.add(fieldLandTaxDate, cc.xyw(4, 6, 1));

        builder.addSeparator("IMB", cc.xyw(2, 8, 5));

        builder.addLabel("     Nomor", cc.xy(2, 10));
        builder.add(fieldBuildPermitNumber, cc.xyw(4, 10, 3));

        builder.addLabel("     Tanggal", cc.xy(2, 12));
        builder.add(fieldBuildPermitDate, cc.xyw(4, 12, 1));

        builder.addSeparator("Advis Planning", cc.xyw(2, 14, 5));

        builder.addLabel("     Nomor", cc.xy(2, 16));
        builder.add(fieldAdvisPlanningNumber, cc.xyw(4, 16, 3));

        builder.addLabel("     Tanggal", cc.xy(2, 18));
        builder.add(fieldAdvisPlanningDate, cc.xyw(4, 18, 1));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createConditionPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,20px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,20px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPriceDescription);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Kondisi", cc.xy(2, 2));
        builder.add(radioLandCondition, cc.xyw(4, 2, 3));

        builder.addLabel("Bentuk", cc.xy(2, 4));
        builder.add(radioShape, cc.xyw(4, 4, 3));

        builder.addLabel("Topografi", cc.xy(2, 6));
        builder.add(radioTopographCondition, cc.xyw(4, 6, 3));

        builder.addLabel("Penggunaan Saat Ini", cc.xy(2, 8));
        builder.add(radioCurrentUse, cc.xywh(4, 8, 3, 3));

        builder.addLabel("Jenis Jalan Terdekat", cc.xy(2, 12));
        builder.add(radioCloserRoad, cc.xyw(4, 12, 3));

        builder.addLabel("Permukaan Jalan Terdekat", cc.xy(2, 14));
        builder.add(radioRoadSurface, cc.xyw(4, 14, 3));

        builder.addSeparator("Tinggi Permukaan Jalan", cc.xyw(2, 16, 5));

        builder.addLabel("      A.Diatas lokasi yang ditinjau", cc.xy(2, 18));
        builder.add(radioHeightAbove, cc.xyw(4, 18, 3));

        builder.addLabel("      B.Dibawah lokasi yang ditinjau", cc.xy(2, 20));
        builder.add(radioHeightUnder, cc.xyw(4, 20, 3));

        builder.addLabel("Elevasi Tanah", cc.xy(2, 22));
        builder.add(radioElevation, cc.xyw(4, 22, 3));

        builder.addSeparator("Harga Pasar Menurut (Rp / M2)", cc.xyw(2, 24, 5));

        builder.addLabel("      A.Keterangan Lurah", cc.xy(2, 24));
        builder.add(fieldPriceBasedChief, cc.xyw(4, 24, 3));

        builder.addLabel("      B.NJOP", cc.xy(2, 28));
        builder.add(fieldPriceBasedNJOP, cc.xyw(4, 28, 3));

        builder.addLabel("      C.BA Pemeriksaan", cc.xy(2, 30));
        builder.add(fieldPriceBasedExam, cc.xyw(4, 30, 3));

        builder.addLabel("Keterangan", cc.xy(2, 32));
        builder.add(scPane, cc.xywh(4, 32, 3, 3));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createEnvironmentPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Lokasi", cc.xy(2, 2));
        builder.add(radioLocation, cc.xyw(4, 2, 3));

        builder.addLabel("Posisi", cc.xy(2, 4));
        builder.add(radioPosition, cc.xyw(4, 4, 3));

        builder.addLabel("Akses Jalan", cc.xy(2, 6));
        builder.add(radioRoadAccess, cc.xywh(4, 6, 3, 3));

        builder.addLabel("Lebar Jalan", cc.xy(2, 10));
        builder.add(radioRoadWidth, cc.xyw(4, 10, 3));

        builder.addLabel("Perkerasan", cc.xy(2, 12));
        builder.add(radioPavement, cc.xyw(4, 12, 3));

        builder.addLabel("Lalu Lintas", cc.xy(2, 14));
        builder.add(radioTraffic, cc.xyw(4, 14, 3));

        builder.addLabel("Topografi", cc.xy(2, 16));
        builder.add(radioTopographLocation, cc.xyw(4, 16, 3));

        builder.addLabel("Lingkungan", cc.xy(2, 18));
        builder.add(radioEnvironment, cc.xyw(4, 18, 3));

        builder.addLabel("Kerapatan", cc.xy(2, 20));
        builder.add(radioDensity, cc.xyw(4, 20, 3));

        builder.addLabel("Tingkat Sosial", cc.xy(2, 22));
        builder.add(radioSocialLevels, cc.xyw(4, 22, 3));

        builder.addLabel("Fasilitas Umum", cc.xy(2, 24));
        builder.add(radioFacilities, cc.xyw(4, 24, 3));

        builder.addLabel("Drainase", cc.xy(2, 26));
        builder.add(radioDrainage, cc.xyw(4, 26, 3));

        builder.addLabel("Transportasi Umum", cc.xy(2, 28));
        builder.add(radioPublicTransportation, cc.xyw(4, 28, 3));

        builder.addLabel("Keamanan", cc.xy(2, 30));
        builder.add(radioSecurity, cc.xyw(4, 30, 3));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createInfluencePanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Gangguan Keamanan", cc.xy(2, 2));
        builder.add(radioSecurityDisturbance, cc.xyw(4, 2, 3));

        builder.addLabel("Bahaya Banjir", cc.xy(2, 4));
        builder.add(radioFloodDangers, cc.xyw(4, 4, 3));

        builder.addLabel("Pengaruh Lokasi", cc.xy(2, 6));
        builder.add(radioLocationEffect, cc.xyw(4, 6, 3));

        builder.addLabel("Pengaruh Lingkungan", cc.xy(2, 8));
        builder.add(radioEnvironmentalInfluences, cc.xyw(4, 8, 3));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createPointPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Peruntukan", cc.xy(2, 2));
        builder.add(radioAllotmentPoint, cc.xyw(4, 2, 3));

        builder.addLabel("Pemanfaatan", cc.xy(2, 4));
        builder.add(radioUtilizationPoint, cc.xyw(4, 4, 3));

        builder.addLabel("Letak Objek", cc.xy(2, 6));
        builder.add(radioLocationPoint, cc.xyw(4, 6, 3));

        builder.addLabel("Pencapaian", cc.xy(2, 8));
        builder.add(radioAccessionPoint, cc.xyw(4, 8, 3));

        builder.addLabel("Perawatan", cc.xy(2, 10));
        builder.add(radioNursingPoint, cc.xyw(4, 10, 3));

        builder.addLabel("Kondisi Tanah", cc.xy(2, 12));
        builder.add(radioSoilConditionsPoint, cc.xyw(4, 12, 3));

        builder.addLabel("Minat Pasar", cc.xy(2, 14));
        builder.add(radioMarketInterestPoint, cc.xyw(4, 14, 3));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createMarketPanel() {
        FormLayout lm = new FormLayout(
                "20px,"
                + "pref,20px, "
                + "pref,2px,fill:default:grow,20px,"
                + "fill:default:grow,5px,pref,20px,"
                + "pref,5px,fill:default:grow,5px,"
                + "20px",
                "30px,"
                + "pref,20px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,20px,pref,20px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JXLabel titleLabel = new JXLabel("DATA PASAR PEMBANDING TANAH");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, titleLabel.getFont().getSize()));

        JXLabel titleLabel2 = new JXLabel("DATA NJOP");
        titleLabel2.setFont(new Font(titleLabel2.getFont().getName(), Font.BOLD, titleLabel2.getFont().getSize()));

        CellConstraints cc = new CellConstraints();

        builder.add(titleLabel, cc.xyw(2, 2, 14));

        builder.addLabel("Data", cc.xy(2, 4));
        builder.addLabel("Harga / m2", cc.xyw(4, 4, 3));
        builder.addLabel("Luas", cc.xyw(8, 4, 3));
        builder.addLabel("Sumber Informasi", cc.xyw(12, 4, 3));

        builder.addLabel("1.", cc.xy(2, 6));
        builder.addLabel("Rp.", cc.xyw(4, 6, 1));
        builder.add(fieldComparativePrice, cc.xyw(6, 6, 1));
        builder.add(fieldComparativeWide, cc.xyw(8, 6, 1));
        builder.addLabel("m2", cc.xyw(10, 6, 1));
        builder.add(fieldComparativeSource, cc.xyw(12, 6, 3));

        builder.addLabel("2.", cc.xy(2, 8));
        builder.addLabel("Rp.", cc.xyw(4, 8, 1));
        builder.add(fieldComparativePrice2, cc.xyw(6, 8, 1));
        builder.add(fieldComparativeWide2, cc.xyw(8, 8, 1));
        builder.addLabel("m2", cc.xyw(10, 8, 1));
        builder.add(fieldComparativeSource2, cc.xyw(12, 8, 3));

        builder.addLabel("3.", cc.xy(2, 10));
        builder.addLabel("Rp.", cc.xyw(4, 10, 1));
        builder.add(fieldComparativePrice3, cc.xyw(6, 10, 1));
        builder.add(fieldComparativeWide3, cc.xyw(8, 10, 1));
        builder.addLabel("m2", cc.xyw(10, 10, 1));
        builder.add(fieldComparativeSource3, cc.xyw(12, 10, 3));

        builder.addLabel("4.", cc.xy(2, 12));
        builder.addLabel("Rp.", cc.xyw(4, 12, 1));
        builder.add(fieldComparativePrice4, cc.xyw(6, 12, 1));
        builder.add(fieldComparativeWide4, cc.xyw(8, 12, 1));
        builder.addLabel("m2", cc.xyw(10, 12, 1));
        builder.add(fieldComparativeSource4, cc.xyw(12, 12, 3));

        builder.addLabel("5.", cc.xy(2, 14));
        builder.addLabel("Rp.", cc.xyw(4, 14, 1));
        builder.add(fieldComparativePrice5, cc.xyw(6, 14, 1));
        builder.add(fieldComparativeWide5, cc.xyw(8, 14, 1));
        builder.addLabel("m2", cc.xyw(10, 14, 1));
        builder.add(fieldComparativeSource5, cc.xyw(12, 14, 3));

        builder.add(titleLabel2, cc.xyw(2, 16, 14));

        builder.addLabel("Alamat Objek", cc.xyw(2, 18, 6));
        builder.add(fieldLandAddress, cc.xyw(8, 18, 8));

        builder.addLabel("Dasar NJOP Tahun", cc.xyw(2, 20, 6));
        builder.add(fieldNJOPYear, cc.xyw(8, 20, 2));
        builder.addLabel("Luas Tanah", cc.xyw(12, 20, 1));
        builder.add(fieldNJOPLandwide, cc.xyw(14, 20, 1));

        builder.addLabel("Kelas Tanah", cc.xyw(2, 22, 6));
        builder.add(fieldNJOPLandClass, cc.xyw(8, 22, 2));
        builder.addLabel("Nilai NJOP", cc.xyw(12, 22, 1));
        builder.add(fieldNJOPLandValue, cc.xyw(14, 22, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createInputPanel() {

        FormLayout lm = new FormLayout(
                "200px,right:pref,10px, 100px,5px,fill:default:grow,200px",
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

        builder.add(container, cc.xyw(2, 6, 5));

        builder.addSeparator("", cc.xyw(1, 10, 7));
        builder.add(btSavePanelBottom, cc.xyw(1, 11, 7));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        container.setAlpha(0.95f);

        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-A");

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

    private void clearYear2() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldNJOPYear.setYear(year);
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
        fieldWide.setValue(BigDecimal.ZERO);
        fieldAddressLocation.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldDescription.setText("");
        fieldPrice.setValue(BigDecimal.ZERO);
        checkUnknown.setSelected(false);
        clearYear();
        clearYear2();
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        //
        radioOwnerShipStateName.setAllDeselected();
        fieldOwnerShipStateNumber.setText("");
        fieldOwnerShipDate.setDate(null);
        fieldOwnerShipIssued.setText("");
        fieldOwner.setText("");
        radioOwnerShipStatus.setAllDeselected();
        radioOwnerShipRelation.setAllDeselected();
        radioOwnerShipOccupancy.setAllDeselected();
        radioOwnerShipOccupying.setAllDeselected();
        //
        fieldLandCertificateNumber.setText("");
        fieldLandCertificateDate.setDate(null);
        fieldLandTaxNumber.setText("");
        fieldLandTaxDate.setDate(null);
        fieldBuildPermitNumber.setText("");
        fieldBuildPermitDate.setDate(null);
        fieldAdvisPlanningNumber.setText("");
        fieldAdvisPlanningDate.setDate(null);
        //
        radioLandCondition.setAllDeselected();
        radioShape.setAllDeselected();
        radioTopographCondition.setAllDeselected();
        radioCurrentUse.setAllDeselected();
        radioCloserRoad.setAllDeselected();
        radioRoadSurface.setAllDeselected();
        radioHeightAbove.setAllDeselected();
        radioHeightUnder.setAllDeselected();
        radioElevation.setAllDeselected();
//        radioAllotment.setAllDeselected();
        fieldAllotment.setText("");
        fieldPriceBasedChief.setValue(BigDecimal.ZERO);
        fieldPriceBasedNJOP.setValue(BigDecimal.ZERO);
        fieldPriceBasedExam.setValue(BigDecimal.ZERO);
        fieldPriceDescription.setText("");
        //
        radioLocation.setAllDeselected();
        radioPosition.setAllDeselected();
        radioRoadAccess.setAllDeselected();
        radioRoadWidth.setAllDeselected();
        radioPavement.setAllDeselected();
        radioTraffic.setAllDeselected();
        radioTopographLocation.setAllDeselected();
        radioEnvironment.setAllDeselected();
        radioDensity.setAllDeselected();
        radioSocialLevels.setAllDeselected();
        radioFacilities.setAllDeselected();
        radioDrainage.setAllDeselected();
        radioPublicTransportation.setAllDeselected();
        radioSecurity.setAllDeselected();
        //
        radioSecurityDisturbance.setAllDeselected();
        radioFloodDangers.setAllDeselected();
        radioLocationEffect.setAllDeselected();
        radioEnvironmentalInfluences.setAllDeselected();
        //
        radioAllotmentPoint.setAllDeselected();
        radioUtilizationPoint.setAllDeselected();
        radioLocationPoint.setAllDeselected();
        radioAccessionPoint.setAllDeselected();
        radioNursingPoint.setAllDeselected();
        radioSoilConditionsPoint.setAllDeselected();
        radioMarketInterestPoint.setAllDeselected();
        //
        fieldComparativePrice.setValue(BigDecimal.ZERO);
        fieldComparativeWide.setValue(BigDecimal.ZERO);
        fieldComparativeSource.setText("");
        fieldComparativePrice2.setValue(BigDecimal.ZERO);
        fieldComparativeWide2.setValue(BigDecimal.ZERO);
        fieldComparativeSource2.setText("");
        fieldComparativePrice3.setValue(BigDecimal.ZERO);
        fieldComparativeWide3.setValue(BigDecimal.ZERO);
        fieldComparativeSource3.setText("");
        fieldComparativePrice4.setValue(BigDecimal.ZERO);
        fieldComparativeWide4.setValue(BigDecimal.ZERO);
        fieldComparativeSource4.setText("");
        fieldComparativePrice5.setValue(BigDecimal.ZERO);
        fieldComparativeWide5.setValue(BigDecimal.ZERO);
        fieldComparativeSource5.setText("");
        fieldLandAddress.setText("");
        fieldNJOPLandwide.setValue(BigDecimal.ZERO);
        fieldNJOPLandClass.setText("");
        fieldNJOPLandValue.setValue(BigDecimal.ZERO);
    }

    private void setFormValues() {
        if (selectedItemsLand != null) {
            try {
                if (selectedItemsLand.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsLand.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsLand.getItemCode());
                category.setCategoryName(selectedItemsLand.getItemName());
                category.setTypes(ItemsCategory.ITEMS_LAND);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsLand.getRegNumber());
                fieldWide.setValue(selectedItemsLand.getWide());
                fieldAddressLocation.setText(selectedItemsLand.getAddressLocation());

                if (selectedItemsLand.getUrban() == null) {
                    comboUrban.getEditor().setItem(null);
                } else {
                    comboUrban.setSelectedItem(selectedItemsLand.getUrban());
                }

                if (selectedItemsLand.getSubUrban() == null) {
                    comboSubUrban.getEditor().setItem(null);
                } else {
                    comboSubUrban.setSelectedItem(selectedItemsLand.getSubUrban());
                }


                fieldPrice.setValue(selectedItemsLand.getLandPrice());

                if (selectedItemsLand.getAcquisitionYear() == null) {
                    checkUnknown.setSelected(true);
                    clearYear();
                } else {
                    checkUnknown.setSelected(false);

                    fieldAcquisitionYear.setYear(selectedItemsLand.getAcquisitionYear());
                }

                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());

                fieldDescription.setText(selectedItemsLand.getDescription());

                fieldFundingSource.setText(selectedItemsLand.getFundingSource());
                fieldAcquisitionWay.setText(selectedItemsLand.getAcquisitionWay());
                //
                radioOwnerShipStateName.setSelectedData(selectedItemsLand.getOwnerShipStateName());
                fieldOwnerShipStateNumber.setText(selectedItemsLand.getOwnerShipNumberState());
                fieldOwnerShipDate.setDate(selectedItemsLand.getOwnerShipDate());
                fieldOwnerShipIssued.setText(selectedItemsLand.getOwnerShipIssuedBy());
                fieldOwner.setText(selectedItemsLand.getOwner());
                radioOwnerShipStatus.setSelectedData(selectedItemsLand.getOwnerShipStatus());
                radioOwnerShipRelation.setSelectedData(selectedItemsLand.getOwnerShipRelation());
                radioOwnerShipOccupancy.setSelectedData(selectedItemsLand.getOwnerShipOccupancy());
                radioOwnerShipOccupying.setSelectedData(selectedItemsLand.getOwnerShipOccupying());
                //
                fieldLandCertificateNumber.setText(selectedItemsLand.getLandCertificateNumber());
                fieldLandCertificateDate.setDate(selectedItemsLand.getLandCertificateDate());
                fieldLandTaxNumber.setText(selectedItemsLand.getLandTaxNumber());
                fieldLandTaxDate.setDate(selectedItemsLand.getLandTaxDate());
                fieldBuildPermitNumber.setText(selectedItemsLand.getBuildPermitNumber());
                fieldBuildPermitDate.setDate(selectedItemsLand.getBuildPermitDate());
                fieldAdvisPlanningNumber.setText(selectedItemsLand.getAdvisPlanningNumber());
                fieldAdvisPlanningDate.setDate(selectedItemsLand.getAdvisPlanningDate());
                //
                radioLandCondition.setSelectedData(selectedItemsLand.getLandCondition());
                radioShape.setSelectedData(selectedItemsLand.getShape());
                radioTopographCondition.setSelectedData(selectedItemsLand.getTopographCondition());
                radioCurrentUse.setSelectedData(selectedItemsLand.getCurrentUse());
                radioCloserRoad.setSelectedData(selectedItemsLand.getCloserRoad());
                radioRoadSurface.setSelectedData(selectedItemsLand.getRoadSurface());
                radioHeightAbove.setSelectedData(selectedItemsLand.getHeightAbove());
                radioHeightUnder.setSelectedData(selectedItemsLand.getHeightUnder());
                radioElevation.setSelectedData(selectedItemsLand.getElevation());
//                radioAllotment.setSelectedData(selectedItemsLand.getAllotment());
                fieldAllotment.setText(selectedItemsLand.getAllotment());
                fieldPriceBasedChief.setValue(selectedItemsLand.getPriceBasedChief());
                fieldPriceBasedNJOP.setValue(selectedItemsLand.getPriceBasedNJOP());
                fieldPriceBasedExam.setValue(selectedItemsLand.getPriceBasedExam());
                fieldPriceDescription.setText(selectedItemsLand.getPriceDescription());
                //
                radioLocation.setSelectedData(selectedItemsLand.getLocation());
                radioPosition.setSelectedData(selectedItemsLand.getPosition());
                radioRoadAccess.setSelectedData(selectedItemsLand.getRoadAccess());
                radioRoadWidth.setSelectedData(selectedItemsLand.getRoadWidth());
                radioPavement.setSelectedData(selectedItemsLand.getPavement());
                radioTraffic.setSelectedData(selectedItemsLand.getTraffic());
                radioTopographLocation.setSelectedData(selectedItemsLand.getTopographLocation());
                radioEnvironment.setSelectedData(selectedItemsLand.getEnvironment());
                radioDensity.setSelectedData(selectedItemsLand.getDensity());
                radioSocialLevels.setSelectedData(selectedItemsLand.getSocialLevels());
                radioFacilities.setSelectedData(selectedItemsLand.getFacilities());
                radioDrainage.setSelectedData(selectedItemsLand.getDrainage());
                radioPublicTransportation.setSelectedData(selectedItemsLand.getPublicTransportation());
                radioSecurity.setSelectedData(selectedItemsLand.getSecurity());
                //
                radioSecurityDisturbance.setSelectedData(selectedItemsLand.getSecurityDisturbance());
                radioFloodDangers.setSelectedData(selectedItemsLand.getFloodDangers());
                radioLocationEffect.setSelectedData(selectedItemsLand.getLocationEffect());
                radioEnvironmentalInfluences.setSelectedData(selectedItemsLand.getEnvironmentalInfluences());
                //
                radioAllotmentPoint.setSelectedData(selectedItemsLand.getAllotmentPoint());
                radioUtilizationPoint.setSelectedData(selectedItemsLand.getUtilizationPoint());
                radioLocationPoint.setSelectedData(selectedItemsLand.getLocationPoint());
                radioAccessionPoint.setSelectedData(selectedItemsLand.getAccessionPoint());
                radioNursingPoint.setSelectedData(selectedItemsLand.getNursingPoint());
                radioSoilConditionsPoint.setSelectedData(selectedItemsLand.getSoilConditionsPoint());
                radioMarketInterestPoint.setSelectedData(selectedItemsLand.getMarketInterestPoint());
                //
                fieldComparativePrice.setValue(selectedItemsLand.getComparativePrice());
                fieldComparativeWide.setValue(selectedItemsLand.getComparativeWide());
                fieldComparativeSource.setText(selectedItemsLand.getComparativeSource());
                fieldComparativePrice2.setValue(selectedItemsLand.getComparativePrice2());
                fieldComparativeWide2.setValue(selectedItemsLand.getComparativeWide2());
                fieldComparativeSource2.setText(selectedItemsLand.getComparativeSource2());
                fieldComparativePrice3.setValue(selectedItemsLand.getComparativePrice3());
                fieldComparativeWide3.setValue(selectedItemsLand.getComparativeWide3());
                fieldComparativeSource3.setText(selectedItemsLand.getComparativeSource3());
                fieldComparativePrice4.setValue(selectedItemsLand.getComparativePrice4());
                fieldComparativeWide4.setValue(selectedItemsLand.getComparativeWide4());
                fieldComparativeSource4.setText(selectedItemsLand.getComparativeSource4());
                fieldComparativePrice5.setValue(selectedItemsLand.getComparativePrice5());
                fieldComparativeWide5.setValue(selectedItemsLand.getComparativeWide5());
                fieldComparativeSource5.setText(selectedItemsLand.getComparativeSource5());
                fieldLandAddress.setText(selectedItemsLand.getLandAddress());
                fieldNJOPYear.setYear(selectedItemsLand.getNjopYear());
                fieldNJOPLandwide.setValue(selectedItemsLand.getNjopLandwide());
                fieldNJOPLandClass.setText(selectedItemsLand.getNjopLandClass());
                fieldNJOPLandValue.setValue(selectedItemsLand.getNjopLandValue());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsLand getItemsLand() throws MotekarException {
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

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }


        Object objWide = fieldWide.getValue();
        BigDecimal wide = BigDecimal.ZERO;

        if (objWide instanceof Long) {
            Long value = (Long) objWide;
            wide = BigDecimal.valueOf(value.intValue());
        } else if (objWide instanceof BigDecimal) {
            wide = (BigDecimal) objWide;
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

        Integer acquisitionYear = fieldAcquisitionYear.getYear();

        if (checkUnknown.isSelected()) {
            acquisitionYear = null;
        }

        String fundingSource = fieldFundingSource.getText();
        String acquisitionWay = fieldAcquisitionWay.getText();

        String description = fieldDescription.getText();

        String ownerShipStateName = radioOwnerShipStateName.getSelectedData();
        String ownerShipNumberState = fieldOwnerShipStateNumber.getText();
        Date ownerShipDate = fieldOwnerShipDate.getDate();
        String ownerShipStateIssuedBy = fieldOwnerShipIssued.getText();
        String owner = fieldOwner.getText();
        String ownerShipStatus = radioOwnerShipStatus.getSelectedData();
        String ownerShipRelation = radioOwnerShipRelation.getSelectedData();
        String ownerShipOccupancy = radioOwnerShipOccupancy.getSelectedData();
        String ownerShipOccupying = radioOwnerShipOccupying.getSelectedData();

        //

        String landCertificateNumber = fieldLandCertificateNumber.getText();
        Date landCertificateDate = fieldLandCertificateDate.getDate();
        String landTaxNumber = fieldLandTaxNumber.getText();
        Date landTaxDate = fieldLandTaxDate.getDate();
        String buildPermitNumber = fieldBuildPermitNumber.getText();
        Date buildPermitDate = fieldBuildPermitDate.getDate();
        String advisPlanningNumber = fieldAdvisPlanningNumber.getText();
        Date advisPlanningDate = fieldAdvisPlanningDate.getDate();
        //
        String landCondition = radioLandCondition.getSelectedData();
        String shape = radioShape.getSelectedData();
        String topographCondition = radioTopographCondition.getSelectedData();
        String currentUse = radioCurrentUse.getSelectedData();
        String closerRoad = radioCloserRoad.getSelectedData();
        String roadSurface = radioRoadSurface.getSelectedData();
        String heightAbove = radioHeightAbove.getSelectedData();
        String heightUnder = radioHeightUnder.getSelectedData();
        String elevation = radioElevation.getSelectedData();
//        String allotment = radioAllotment.getSelectedData();
        String allotment = fieldAllotment.getText();

        BigDecimal priceBasedChief = BigDecimal.ZERO;

        Object objPBC = fieldPriceBasedChief.getValue();

        if (objPBC instanceof Long) {
            Long value = (Long) objPBC;
            priceBasedChief = BigDecimal.valueOf(value.intValue());
        } else if (objPBC instanceof BigDecimal) {
            priceBasedChief = (BigDecimal) objPBC;
        }

        BigDecimal priceBasedNJOP = BigDecimal.ZERO;

        Object objNJOP = fieldPriceBasedNJOP.getValue();

        if (objNJOP instanceof Long) {
            Long value = (Long) objNJOP;
            priceBasedNJOP = BigDecimal.valueOf(value.intValue());
        } else if (objNJOP instanceof BigDecimal) {
            priceBasedNJOP = (BigDecimal) objNJOP;
        }

        BigDecimal priceBasedExam = BigDecimal.ZERO;

        Object objExam = fieldPriceBasedExam.getValue();

        if (objExam instanceof Long) {
            Long value = (Long) objExam;
            priceBasedExam = BigDecimal.valueOf(value.intValue());
        } else if (objExam instanceof BigDecimal) {
            priceBasedExam = (BigDecimal) objExam;
        }

        String priceDescription = fieldPriceDescription.getText();

        //

        String location = radioLocation.getSelectedData();
        String position = radioPosition.getSelectedData();
        String roadAccess = radioRoadAccess.getSelectedData();
        String roadWidth = radioRoadWidth.getSelectedData();
        String pavement = radioPavement.getSelectedData();
        String traffic = radioTraffic.getSelectedData();
        String topographLocation = radioTopographLocation.getSelectedData();
        String environment = radioEnvironment.getSelectedData();
        String density = radioDensity.getSelectedData();
        String socialLevels = radioSocialLevels.getSelectedData();
        String facilities = radioFacilities.getSelectedData();
        String drainage = radioDrainage.getSelectedData();
        String publicTransportation = radioPublicTransportation.getSelectedData();
        String security = radioSecurity.getSelectedData();

        //

        String securityDisturbance = radioSecurityDisturbance.getSelectedData();
        String floodDangers = radioFloodDangers.getSelectedData();
        String locationEffect = radioLocationEffect.getSelectedData();
        String environmentalInfluences = radioEnvironmentalInfluences.getSelectedData();

        //
        String allotmentPoint = radioAllotmentPoint.getSelectedData();
        String utilizationPoint = radioUtilizationPoint.getSelectedData();
        String locationPoint = radioLocationPoint.getSelectedData();
        String accessionPoint = radioAccessionPoint.getSelectedData();
        String nursingPoint = radioNursingPoint.getSelectedData();
        String soilConditionsPoint = radioSoilConditionsPoint.getSelectedData();
        String marketInterestPoint = radioMarketInterestPoint.getSelectedData();

        //

        BigDecimal comparativePrice = BigDecimal.ZERO;

        Object objCompPrice = fieldComparativePrice.getValue();

        if (objCompPrice instanceof Long) {
            Long value = (Long) objCompPrice;
            comparativePrice = BigDecimal.valueOf(value.intValue());
        } else if (objCompPrice instanceof BigDecimal) {
            comparativePrice = (BigDecimal) objCompPrice;
        }

        BigDecimal comparativeWide = BigDecimal.ZERO;

        Object objCompWide = fieldComparativeWide.getValue();

        if (objCompWide instanceof Long) {
            Long value = (Long) objCompWide;
            comparativeWide = BigDecimal.valueOf(value.intValue());
        } else if (objCompWide instanceof BigDecimal) {
            comparativeWide = (BigDecimal) objCompWide;
        }

        String comparativeSource = fieldComparativeSource.getText();

        BigDecimal comparativePrice2 = BigDecimal.ZERO;

        Object objCompPrice2 = fieldComparativePrice2.getValue();

        if (objCompPrice2 instanceof Long) {
            Long value = (Long) objCompPrice2;
            comparativePrice2 = BigDecimal.valueOf(value.intValue());
        } else if (objCompPrice2 instanceof BigDecimal) {
            comparativePrice2 = (BigDecimal) objCompPrice2;
        }

        BigDecimal comparativeWide2 = BigDecimal.ZERO;

        Object objCompWide2 = fieldComparativeWide2.getValue();

        if (objCompWide2 instanceof Long) {
            Long value = (Long) objCompWide2;
            comparativeWide2 = BigDecimal.valueOf(value.intValue());
        } else if (objCompWide2 instanceof BigDecimal) {
            comparativeWide2 = (BigDecimal) objCompWide2;
        }

        String comparativeSource2 = fieldComparativeSource2.getText();

        BigDecimal comparativePrice3 = BigDecimal.ZERO;

        Object objCompPrice3 = fieldComparativePrice3.getValue();

        if (objCompPrice3 instanceof Long) {
            Long value = (Long) objCompPrice3;
            comparativePrice3 = BigDecimal.valueOf(value.intValue());
        } else if (objCompPrice3 instanceof BigDecimal) {
            comparativePrice3 = (BigDecimal) objCompPrice3;
        }

        BigDecimal comparativeWide3 = BigDecimal.ZERO;

        Object objCompWide3 = fieldComparativeWide3.getValue();

        if (objCompWide3 instanceof Long) {
            Long value = (Long) objCompWide3;
            comparativeWide3 = BigDecimal.valueOf(value.intValue());
        } else if (objCompWide3 instanceof BigDecimal) {
            comparativeWide3 = (BigDecimal) objCompWide3;
        }

        String comparativeSource3 = fieldComparativeSource3.getText();

        BigDecimal comparativePrice4 = BigDecimal.ZERO;

        Object objCompPrice4 = fieldComparativePrice4.getValue();

        if (objCompPrice4 instanceof Long) {
            Long value = (Long) objCompPrice4;
            comparativePrice4 = BigDecimal.valueOf(value.intValue());
        } else if (objCompPrice4 instanceof BigDecimal) {
            comparativePrice4 = (BigDecimal) objCompPrice4;
        }

        BigDecimal comparativeWide4 = BigDecimal.ZERO;

        Object objCompWide4 = fieldComparativeWide4.getValue();

        if (objCompWide4 instanceof Long) {
            Long value = (Long) objCompWide4;
            comparativeWide4 = BigDecimal.valueOf(value.intValue());
        } else if (objCompWide4 instanceof BigDecimal) {
            comparativeWide4 = (BigDecimal) objCompWide4;
        }

        String comparativeSource4 = fieldComparativeSource4.getText();

        BigDecimal comparativePrice5 = BigDecimal.ZERO;

        Object objCompPrice5 = fieldComparativePrice5.getValue();

        if (objCompPrice5 instanceof Long) {
            Long value = (Long) objCompPrice5;
            comparativePrice5 = BigDecimal.valueOf(value.intValue());
        } else if (objCompPrice5 instanceof BigDecimal) {
            comparativePrice5 = (BigDecimal) objCompPrice5;
        }

        BigDecimal comparativeWide5 = BigDecimal.ZERO;

        Object objCompWide5 = fieldComparativeWide5.getValue();

        if (objCompWide5 instanceof Long) {
            Long value = (Long) objCompWide5;
            comparativeWide5 = BigDecimal.valueOf(value.intValue());
        } else if (objCompWide5 instanceof BigDecimal) {
            comparativeWide5 = (BigDecimal) objCompWide5;
        }

        String comparativeSource5 = fieldComparativeSource5.getText();

        String landAddress = fieldLandAddress.getText();
        Integer njopYear = fieldNJOPYear.getYear();

        BigDecimal njopLandwide = BigDecimal.ZERO;

        Object objNJOPWide = fieldNJOPLandwide.getValue();

        if (objNJOPWide instanceof Long) {
            Long value = (Long) objNJOPWide;
            njopLandwide = BigDecimal.valueOf(value.intValue());
        } else if (objNJOPWide instanceof BigDecimal) {
            njopLandwide = (BigDecimal) objNJOPWide;
        }

        String njopLandClass = fieldNJOPLandClass.getText();

        BigDecimal njopLandValue = BigDecimal.ZERO;

        Object objNJOPValue = fieldNJOPLandValue.getValue();

        if (objNJOPValue instanceof Long) {
            Long value = (Long) objNJOPValue;
            njopLandValue = BigDecimal.valueOf(value.intValue());
        } else if (objNJOPValue instanceof BigDecimal) {
            njopLandValue = (BigDecimal) objNJOPValue;
        }

        if (itemCode.equals("") || itemName.equals("")) {
            errorString.append("<br>- Kode / Nama Barang Harus Lengkap</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan : " + errorString.toString() + "</html>");
        }

        ItemsLand land = new ItemsLand();
        land.setUnit(unit);
        land.setItemCode(itemCode);
        land.setItemName(itemName);
        land.setRegNumber(regNumber);
        land.setWide(wide);
        land.setAddressLocation(addressLocation);
        land.setUrban(urban);
        land.setSubUrban(subUrban);
        land.setLandPrice(price);
        land.setAcquisitionYear(acquisitionYear);
        land.setFundingSource(fundingSource);
        land.setAcquisitionWay(acquisitionWay);
        land.setDescription(description);
        //
        land.setOwnerShipStateName(ownerShipStateName);
        land.setOwnerShipNumberState(ownerShipNumberState);
        land.setOwnerShipDate(ownerShipDate);
        land.setOwnerShipIssuedBy(ownerShipStateIssuedBy);
        land.setOwner(owner);
        land.setOwnerShipStatus(ownerShipStatus);
        land.setOwnerShipRelation(ownerShipRelation);
        land.setOwnerShipOccupancy(ownerShipOccupancy);
        land.setOwnerShipOccupying(ownerShipOccupying);
        //
        land.setLandCertificateNumber(landCertificateNumber);
        land.setLandCertificateDate(landCertificateDate);
        land.setLandTaxNumber(landTaxNumber);
        land.setLandTaxDate(landTaxDate);
        land.setBuildPermitNumber(buildPermitNumber);
        land.setBuildPermitDate(buildPermitDate);
        land.setAdvisPlanningNumber(advisPlanningNumber);
        land.setAdvisPlanningDate(advisPlanningDate);
        //
        land.setLandCondition(landCondition);
        land.setShape(shape);
        land.setTopographCondition(topographCondition);
        land.setCurrentUse(currentUse);
        land.setCloserRoad(closerRoad);
        land.setRoadSurface(roadSurface);
        land.setHeightAbove(heightAbove);
        land.setHeightUnder(heightUnder);
        land.setElevation(elevation);
        land.setAllotment(allotment);
        land.setPriceBasedChief(priceBasedChief);
        land.setPriceBasedNJOP(priceBasedNJOP);
        land.setPriceBasedExam(priceBasedExam);
        land.setPriceDescription(priceDescription);
        //
        land.setLocation(location);
        land.setPosition(position);
        land.setRoadAccess(roadAccess);
        land.setRoadWidth(roadWidth);
        land.setPavement(pavement);
        land.setTraffic(traffic);
        land.setTopographLocation(topographLocation);
        land.setEnvironment(environment);
        land.setDensity(density);
        land.setSocialLevels(socialLevels);
        land.setFacilities(facilities);
        land.setDrainage(drainage);
        land.setPublicTransportation(publicTransportation);
        land.setSecurity(security);
        //
        land.setSecurityDisturbance(securityDisturbance);
        land.setFloodDangers(floodDangers);
        land.setLocationEffect(locationEffect);
        land.setEnvironmentalInfluences(environmentalInfluences);
        //
        land.setAllotmentPoint(allotmentPoint);
        land.setUtilizationPoint(utilizationPoint);
        land.setLocationPoint(locationPoint);
        land.setAccessionPoint(accessionPoint);
        land.setNursingPoint(nursingPoint);
        land.setSoilConditionsPoint(soilConditionsPoint);
        land.setMarketInterestPoint(marketInterestPoint);
        //
        land.setComparativePrice(comparativePrice);
        land.setComparativePrice2(comparativePrice2);
        land.setComparativePrice3(comparativePrice3);
        land.setComparativePrice4(comparativePrice4);
        land.setComparativePrice5(comparativePrice5);
        land.setComparativeWide(comparativeWide);
        land.setComparativeWide2(comparativeWide2);
        land.setComparativeWide3(comparativeWide3);
        land.setComparativeWide4(comparativeWide4);
        land.setComparativeWide5(comparativeWide5);
        land.setComparativeSource(comparativeSource);
        land.setComparativeSource2(comparativeSource2);
        land.setComparativeSource3(comparativeSource3);
        land.setComparativeSource4(comparativeSource4);
        land.setComparativeSource5(comparativeSource5);
        land.setLandAddress(landAddress);
        land.setNjopYear(njopYear);
        land.setNjopLandClass(njopLandClass);
        land.setNjopLandwide(njopLandwide);
        land.setNjopLandValue(njopLandValue);

        if (selectedItemsLand != null && btPanel.isEditstate()) {
            land.setUserName(selectedItemsLand.getUserName());
        } else {
            land.setUserName(userAccount.getUserName());
        }
        land.setLastUserName(userAccount.getUserName());

        return land;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        setTaskPaneState();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldItems.requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        setTaskPaneState();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldItems.requestFocus();
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
                ArrayList<ItemsLand> selectedItemsLands = table.getSelectedItemsLands();
                if (!selectedItemsLands.isEmpty()) {
                    logic.deleteItemsLand(mainframe.getSession(), selectedItemsLands);
                    table.removeItemsLand(selectedItemsLands);
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
            ItemsLand newItemsLand = getItemsLand();
            if (btPanel.isNewstate()) {
                newItemsLand = logic.insertItemsLand(mainframe.getSession(), newItemsLand);
                table.addItemsLand(newItemsLand);
                selectedItemsLand = newItemsLand;
                selectedItemsLand.setItemName(getLastItemsName());
                selectedItemsLand.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newItemsLand = logic.updateItemsLand(mainframe.getSession(), selectedItemsLand, newItemsLand);
                table.updateSelectedItemsLand(newItemsLand);
                selectedItemsLand = newItemsLand;
                selectedItemsLand.setItemName(getLastItemsName());
                selectedItemsLand.setFullItemName(getCompleteItemsName());
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
                ItemsLandWorkbookReader wbr = new ItemsLandWorkbookReader();
                List<ItemsLand> items = wbr.loadXLSFile(file.getPath());

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

                if (!unitMaster.isEmpty()) {
                    Collections.sort(unitMaster, comparator);
                }

                if (!regionMaster.isEmpty()) {
                    Collections.sort(regionMaster, comparator2);
                }

                if (!items.isEmpty()) {
                    for (ItemsLand item : items) {
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

                        String addressLocation = item.getAddressLocation();
                        if (addressLocation != null) {
                            if (!addressLocation.equals("")) {
                                int index = Collections.binarySearch(regionMaster, new Region(addressLocation), comparator2);
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
                    }

                    if (importWorker != null) {
                        importWorker.cancel(true);
                    }
                    
                    oldCursor = mainframe.getCursor();
                    mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    importWorker = new ImportItemsLand(items);
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

        chooser.setSelectedFile(new File(ItemsLandWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            
            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            File file = chooser.getSelectedFile();
            ItemsLandWorkbookCreator wb = new ItemsLandWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsLand>(), unitMaster);
            wb.createXLSFile();
            
            mainframe.setCursor(oldCursor);
        }
    }

    public void onPrint() throws Exception, CommonException {

        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Unit unit = filterPanel.getUnit();

        if (unit != null) {
            ItemsLandJasper report = new ItemsLandJasper("Buku Inventaris Barang Tanah (KIB-A)", table.getItemsLands(), unit);
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

        chooser.setSelectedFile(new File(ItemsLandWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsLand> lands = table.getItemsLands();

            ItemsLandWorkbookCreator wb = new ItemsLandWorkbookCreator(mainframe, file.getPath(), lands, unitMaster);
            wb.createXLSFile();
        }
    }

    public void onPrintPDF() throws Exception, CommonException {
        //
    }

    public void onPrintGraph() throws Exception, CommonException {
        //
    }

    private ArrayList<FilterValue> createFilterValues() {
        ArrayList<FilterValue> filterValues = new ArrayList<FilterValue>();

        filterValues.add(new FilterValue("Nama Barang", "itemname", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Kode Barang", "itemcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Register", "regnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Luas", "wide", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Tahun Pengadaan", "acquisitionyear", FilterCardPanel.YEAR_PANEL));
        filterValues.add(new FilterValue("Letak / Alamat", "addresslocation", FilterCardPanel.STRING_PANEL));

        ArrayList<String> comboValues = new ArrayList<String>();
        comboValues.add("Milik");
        comboValues.add("Pakai");
        comboValues.add("Kelola");

        filterValues.add(new FilterValue("Hak", "ownershipstatus", FilterCardPanel.COMBO_PANEL, comboValues, null));
        filterValues.add(new FilterValue("Tanggal Sertifikat", "landcertificatedate", FilterCardPanel.DATE_PANEL));
        filterValues.add(new FilterValue("Nomor Sertifikat", "landcertificatenumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Penggunaan", "allotment", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal-Usul", "acquisitionway", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Harga Perolehan (Rp.)", "landprice", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ItemsLandTable extends JXTable {

        private ItemsLandTableModel model;

        public ItemsLandTable() {
            model = new ItemsLandTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><b>Sertifikat</b>");
            group2.add(colModel.getColumn(9));
            group2.add(colModel.getColumn(10));

            CustomColumnGroup group3 = new CustomColumnGroup("<html><center><b>Status Tanah</b></center>");
            group3.add(colModel.getColumn(8));
            group3.add(group2);

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group3);
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

            worker = new LoadItemsLand(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsLand getSelectedItemsLand() {
            ArrayList<ItemsLand> selectedItemsLands = getSelectedItemsLands();
            return selectedItemsLands.get(0);
        }

        public ArrayList<ItemsLand> getItemsLands() {
            return model.getItemsLands();
        }

        public ArrayList<ItemsLand> getSelectedItemsLands() {

            ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsLand land = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsLand) {
                            land = (ItemsLand) obj;
                            lands.add(land);
                        }
                    }
                }
            }

            return lands;
        }

        public void updateSelectedItemsLand(ItemsLand land) {
            model.updateRow(getSelectedItemsLand(), land);
        }

        public void removeItemsLand(ArrayList<ItemsLand> lands) {
            if (!lands.isEmpty()) {
                for (ItemsLand land : lands) {
                    model.remove(land);
                }
            }

        }

        public void addItemsLand(ArrayList<ItemsLand> lands) {
            if (!lands.isEmpty()) {
                for (ItemsLand land : lands) {
                    model.add(land);
                }
            }
        }

        public void addItemsLand(ItemsLand land) {
            model.add(land);
        }

        public void insertEmptyItemsLand() {
            addItemsLand(new ItemsLand());
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
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsLandTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 17;
        private static final String[] COLUMN_RELIGION = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Luas<br>(M<sup>2</sup>)</br></b></center>",
            "<html><center><b>Tahun<br>Pengadaan</br></b></center>",
            "<html><b>Letak / Alamat</b>", "<html><b>Hak</b>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><b>Penggunaan</b>", "<html><b>Asal-Usul</b>",
            "<html><center><b>Harga Perolehan<br>(Rp.)</br></b></center>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

        public ItemsLandTableModel() {
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
            } else if (columnIndex == 1 || columnIndex == 6) {
                return Integer.class;
            } else if (columnIndex == 5 || columnIndex == 13) {
                return BigDecimal.class;
            } else if (columnIndex == 9) {
                return Date.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_RELIGION[column];
        }

        public void add(ArrayList<ItemsLand> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            lands.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsLand land) {
            insertRow(getRowCount(), land);
        }

        public void insertRow(int row, ItemsLand land) {
            lands.add(row, land);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsLand oldItemsLand, ItemsLand newItemsLand) {
            int index = lands.indexOf(oldItemsLand);
            lands.set(index, newItemsLand);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsLand land) {
            int row = lands.indexOf(land);
            lands.remove(land);
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
                lands.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                lands.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            lands.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (lands.get(i) == null) {
                    lands.set(i, new ItemsLand());
                }
            }
        }

        public ArrayList<ItemsLand> getItemsLands() {
            return lands;
        }

        @Override
        public int getRowCount() {
            return lands.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsLand getItemsLand(int row) {
            if (!lands.isEmpty()) {
                return lands.get(row);
            }
            return new ItemsLand();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsLand land = getItemsLand(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        land.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsLand) {
                        land = (ItemsLand) aValue;
                    }
                    break;
                case 3:
                    land.setItemCode((String) aValue);
                    break;
                case 4:
                    land.setRegNumber((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof BigDecimal) {
                        land.setWide((BigDecimal) aValue);
                    }
                    break;
                case 6:
                    land.setAcquisitionYear((Integer) aValue);
                    break;
                case 7:
                    land.setAddressLocation((String) aValue);
                    break;
                case 8:
                    land.setOwnerShipStatus((String) aValue);
                    break;
                case 9:
                    land.setLandCertificateDate((Date) aValue);
                    break;
                case 10:
                    land.setLandCertificateNumber((String) aValue);
                    break;
                case 11:
                    land.setAllotment((String) aValue);
                    break;
                case 12:
                    land.setAcquisitionWay((String) aValue);
                    break;
                case 13:
                    if (aValue instanceof BigDecimal) {
                        land.setLandPrice((BigDecimal) aValue);
                    }
                    break;
                case 14:
                    land.setDescription((String) aValue);
                    break;
                case 15:
                    land.setUserName((String) aValue);
                    break;
                case 16:
                    land.setLastUserName((String) aValue);
                    break;

            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsLand land = getItemsLand(row);
            switch (column) {
                case 0:
                    toBeDisplayed = land.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = land;
                    break;
                case 3:
                    toBeDisplayed = land.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = land.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = land.getWide();
                    break;
                case 6:
                    toBeDisplayed = land.getAcquisitionYear();
                    break;
                case 7:
                    StringBuilder builder = new StringBuilder();
                    builder.append(land.getAddressLocation());

                    if (land.getUrban() != null) {
                        Region urban = land.getUrban();
                        builder.append(" Kecamatan ").append(urban.getRegionName());
                    }

                    if (land.getSubUrban() != null) {
                        Region subUrban = land.getSubUrban();
                        if (subUrban.getRegionType().equals(Region.TYPE_SUB_URBAN)) {
                            builder.append(" Kelurahan ").append(subUrban.getRegionName());
                        } else {
                            builder.append(" Desa ").append(subUrban.getRegionName());
                        }
                    }

                    toBeDisplayed = builder.toString();
                    break;
                case 8:
                    toBeDisplayed = land.getOwnerShipStatus();

                    break;
                case 9:
                    toBeDisplayed = land.getLandCertificateDate();
                    break;
                case 10:
                    toBeDisplayed = land.getLandCertificateNumber();
                    break;
                case 11:
                    toBeDisplayed = land.getAllotment();
                    break;
                case 12:
                    toBeDisplayed = land.getAcquisitionWay();
                    break;
                case 13:
                    toBeDisplayed = land.getLandPrice();
                    break;
                case 14:
                    toBeDisplayed = land.getDescription();
                    break;
                case 15:
                    toBeDisplayed = land.getUserName();
                    break;
                case 16:
                    toBeDisplayed = land.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
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
            Dimension size = new Dimension(0, 30);

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

    private class LoadItemsLand extends SwingWorker<ItemsLandTableModel, ItemsLand> {

        private ItemsLandTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsLand(String modifier) {
            this.model = (ItemsLandTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsLand> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsLand land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-A " + land.getItemName());
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
        protected ItemsLandTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsLand> lands = logic.getItemsLand(mainframe.getSession(), modifier);
                double progress = 0.0;
                if (!lands.isEmpty()) {
                    for (int i = 0; i < lands.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / lands.size();

                        ItemsLand land = lands.get(i);

                        if (land.getUnit() != null) {
                            land.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), land.getUnit()));
                        }

                        if (land.getUrban() != null) {
                            land.setUrban(mLogic.getRegion(mainframe.getSession(), land.getUrban()));
                        }

                        if (land.getSubUrban() != null) {
                            land.setSubUrban(mLogic.getRegion(mainframe.getSession(), land.getSubUrban()));
                        }


                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(land.getItemCode());
                        category.setCategoryName(land.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_LAND);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        land.setItemName(getLastItemsName(iLookUp));
                        land.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(land);
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
                JXErrorPane.showDialog(ItemsLandPanel.this, info);
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

    private class ImportItemsLand extends SwingWorker<ItemsLandTableModel, ItemsLand> {

        private ItemsLandTableModel model;
        private Exception exception;
        private List<ItemsLand> data;

        public ImportItemsLand(List<ItemsLand> data) {
            this.model = (ItemsLandTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsLand> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsLand land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-A " + land.getItemName());
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
        protected ItemsLandTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {

                    ItemsLand il = data.get(0);
                    Unit ilUnit = il.getUnit();

                    if (ilUnit != null) {
                        table.clear();
                        logic.deleteItemsLand(mainframe.getSession(), ilUnit);
                    }

                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsLand land = data.get(i);

                        land.setUserName(userAccount.getUserName());
                        land.setLastUserName(userAccount.getUserName());

                        synchronized (land) {
                            land = logic.insertItemsLand(mainframe.getSession(), land);
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(land.getItemCode());
                        category.setCategoryName(land.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_LAND);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        land.setItemName(getLastItemsName(iLookUp));
                        land.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(land);
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
                JXErrorPane.showDialog(ItemsLandPanel.this, info);
            }

            table.packAll();
            
            mainframe.setCursor(oldCursor);
            
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
