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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.report.ItemsBuildingJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsBuildingWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.asset.reader.ItemsBuildingWorkbookReader;
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
public class ItemsBuildingPanel extends JXPanel implements CommonButtonListener, ButtonLoadListener, PrintButtonListener {

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
    private JCommandButton btChooseItemsBuilding = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXFormattedTextField fieldWide;
    private RadioButtonPanel radioRaised = new RadioButtonPanel(new String[]{"Bertingkat", "Tidak"}, 0, 2);
    private JXComboBox comboCondition = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JXFormattedTextField fieldAdminCost;
    private JXFormattedTextField fieldTotalPrice;
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JCheckBox checkUnknown = new JCheckBox("Tidak Diketahui");
    private JXTextField fieldAddress = new JXTextField();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXDatePicker fieldDocumentDate = new JXDatePicker();
    private JXFormattedTextField fieldBuildingWide;
    private JXTextField fieldBuildingCode = new JXTextField();
    private JXTextField fieldBuildingState = new JXTextField();
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    // Koordinat 
    private JXTextField fieldEastLongitudeCoord = new JXTextField();
    private JXTextField fieldSouthLatitudeCoord = new JXTextField();
    // Legalitas
    private RadioButtonPanel radioOwnerShipStateName = new RadioButtonPanel(new String[]{"Milik", "Pakai", "Kelola"}, 0, 3);
    private RadioButtonPanel radioOwnerShipRelation = new RadioButtonPanel(new String[]{"Pemda", "Pusat", "Departemen", "Lainnya"}, 0, 4);
    private RadioButtonPanel radioOwnerShipOccupancy = new RadioButtonPanel(new String[]{"Dihuni", "Kosong"}, 0, 2);
    private RadioButtonPanel radioOwnerShipOccupaying = new RadioButtonPanel(new String[]{"Struktural", "Pemilik", "Sewa", "Lainnya"}, 0, 4);
    // Data Dokumen
    private JXTextField fieldBuildingTaxNumber = new JXTextField();
    private JXDatePicker fieldBuildingTaxDate = new JXDatePicker();
    private JXTextField fieldBuildPermitNumber = new JXTextField();
    private JXDatePicker fieldBuildPermitDate = new JXDatePicker();
    private JXTextField fieldAdvisPlanningNumber = new JXTextField();
    private JXDatePicker fieldAdvisPlanningDate = new JXDatePicker();
    // Deskripsi Bangunan
    private RadioButtonPanel radioType = new RadioButtonPanel(new String[]{"Permanen", "Semi Permanen", "Sementara", "Gubuk"}, 0, 4);
    private RadioButtonPanel radioShape = new RadioButtonPanel(new String[]{"Kantor", "Sekolah",
                "Pabrik/Gedung", "Rumah Tinggal", "Rumah Sakit", "Puskesmas", "Monumen", "Lainnya"}, 2, 4);
    private RadioButtonPanel radioUtilization = new RadioButtonPanel(new String[]{"Kantor", "Sekolah",
                "Pabrik/Gedung", "Rumah Tinggal", "Rumah Sakit", "Puskesmas", "Monumen", "Lainnya"}, 2, 4);
    private RadioButtonPanel radioAges = new RadioButtonPanel(new String[]{"1-3 Tahun", "4-6 Tahun", "7-10 Tahun", "> 10 Tahun"}, 0, 4);
    private RadioButtonPanel radioLevels = new RadioButtonPanel(new String[]{"1 Lantai", "2 Lantai", "3 Lantai", "4 Lantai",
                "5 Lantai", "> 5 Lantai"}, 0, 6);
    private RadioButtonPanel radioFrameworks = new RadioButtonPanel(new String[]{"Beton", "Tidak"}, 0, 2);
    private RadioButtonPanel radioFoundation = new RadioButtonPanel(new String[]{"Batu Kali", "Beton", "Pancang", "Sumuran"}, 0, 4);
    private RadioButtonPanel radioFloorType = new RadioButtonPanel(new String[]{"Marmer", "Keramik", "Tegel", "Semen"}, 0, 4);
    private RadioButtonPanel radioWallType = new RadioButtonPanel(new String[]{"Beton", "Batu Bata", "Batako", "Marmer/Keramik"}, 0, 4);
    private RadioButtonPanel radioCeillingType = new RadioButtonPanel(new String[]{"Akustik", "Asbes/Gypsum", "Triplek", "Tidak Ada", "Lainnya"}, 0, 5);
    private RadioButtonPanel radioRoofType = new RadioButtonPanel(new String[]{"Beton", "Keramik",
                "Seng/Alumunium", "Asbes", "Slab Beton", "Genteng Kodok", "Tidak Ada", "Lainnya"}, 2, 4);
    private RadioButtonPanel radioFrameType = new RadioButtonPanel(new String[]{"Jati", "Kamper", "Borneo", "Alumunium", "Lain-lain"}, 0, 5);
    private RadioButtonPanel radioDoorType = new RadioButtonPanel(new String[]{"Papan Kayu", "Double Triplek",
                "Alumunium", "Lainnya"}, 0, 4);
    private RadioButtonPanel radioWindowType = new RadioButtonPanel(new String[]{"Kaca+Kayu", "Nako", "Kaca+Alumunium", "Lainnya"}, 0, 4);
    private RadioButtonPanel radioRoomType = new RadioButtonPanel(new String[]{"Lengkap", "Memadai", "< Memadai", "Tidak Memadai"}, 0, 4);
    private RadioButtonPanel radioMaterialQuality = new RadioButtonPanel(new String[]{"Mewah", "Semi", "Sedang", "Kurang"}, 0, 4);
    private RadioButtonPanel radioFacilities = new RadioButtonPanel(new String[]{"Lengkap", "Memadai", "Minim", "< Minim"}, 0, 4);
    private JXComboBox comboBuildingCondition = new JXComboBox();
    private RadioButtonPanel radioNursing = new RadioButtonPanel(new String[]{"Baik", "Kurang Baik"}, 0, 2);
    private RadioButtonPanel radioBuildingClass = new RadioButtonPanel(new String[]{"I", "II", "III"}, 0, 3);
    // Data Faktor yang Berpengaruh dalam Penilaian
    private RadioButtonPanel radioAllotmentPoint = new RadioButtonPanel(new String[]{"Sesuai", "Tidak Sesuai"}, 0, 2);
    private RadioButtonPanel radioUtilizationPoint = new RadioButtonPanel(new String[]{"Sesuai", "Tidak Sesuai"}, 0, 2);
    private RadioButtonPanel radioLocationPoint = new RadioButtonPanel(new String[]{"Strategis", "Kurang Strategis"}, 0, 2);
    private RadioButtonPanel radioAccessionPoint = new RadioButtonPanel(new String[]{"Mudah", "Sulit"}, 0, 2);
    private RadioButtonPanel radioQualityPoint = new RadioButtonPanel(new String[]{"Bagus", "Kurang Bagus"}, 0, 2);
    private RadioButtonPanel radioConditionPoint = new RadioButtonPanel(new String[]{"Bagus", "Kurang Bagus"}, 0, 2);
    private RadioButtonPanel radioLayoutPoint = new RadioButtonPanel(new String[]{"Bagus", "Kurang Bagus"}, 0, 2);
    private RadioButtonPanel radioNursingPoint = new RadioButtonPanel(new String[]{"Baik", "Kurang Baik"}, 0, 2);
    private RadioButtonPanel radioMarketInterestPoint = new RadioButtonPanel(new String[]{"Baik", "Kurang Baik"}, 0, 2);
    //
    private ItemsBuildingTable table = new ItemsBuildingTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadItemsBuilding worker;
    private ProgressListener progressListener;
    private ItemsBuilding selectedItemsBuilding = null;
    private ItemsCategory selectedItemsCategory = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportItemsBuilding importWorker;
    //
    private JScrollPane mainSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    //
    private EventList<Region> urbanRegions;
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();
    private ArrayList<Condition> comboCategoryVal = new ArrayList<Condition>();
    private ItemsCategory parentCategory = null;
    private ItemsCategory landParentCategory = null;
    //
    //
    private List<Unit> unitMaster = new ArrayList<Unit>();
    private List<Region> regionMaster = new ArrayList<Region>();
    private ArrayList<Condition> conditionMaster = new ArrayList<Condition>();
    //
    private UserAccount userAccount = null;
    //
    private Cursor oldCursor;

    public ItemsBuildingPanel(ArchieveMainframe mainframe) {
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
        fieldBuildingTaxDate.setFormats("dd/MM/yyyy");
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
                    ArrayList<ItemsBuilding> buildings = table.getSelectedItemsBuildings();
                    if (!buildings.isEmpty()) {
                        if (buildings.size() == 1) {
                            selectedItemsBuilding = buildings.get(0);
                            selectedItemsCategory = new ItemsCategory();
                            selectedItemsCategory.setCategoryCode(selectedItemsBuilding.getItemCode());
                            selectedItemsCategory.setCategoryName(selectedItemsBuilding.getItemName());
                            selectedItemsCategory.setTypes(ItemsCategory.ITEMS_BUILDING);
                            selectedItemsCategory.setStyled(true);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedItemsBuilding = null;
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
                        ItemsCategory.ITEMS_BUILDING, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.toString());
                }
            }
        });

        btChooseItemsBuilding.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg dlg = new ItemsCategoryChooserDlg(mainframe, mainframe.getSession(), mainframe.getConnection(),
                        ItemsCategory.ITEMS_LAND, landParentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    ItemsCategory category = dlg.getSelectedItemsCategory();
                    fieldBuildingCode.setText(category.getCategoryCode());
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
                    CustomOptionDialog.showDialog(ItemsBuildingPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
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

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        fieldItems.setEditable(false);
        fieldBuildingCode.setEditable(false);
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
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession(), ItemsCategory.ITEMS_BUILDING);
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
            comboBuildingCondition.setModel(new ListComboBoxModel<Condition>(conditions));

            AutoCompleteDecorator.decorate(comboCondition);
            AutoCompleteDecorator.decorate(comboBuildingCondition);

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
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldWide = new JXFormattedTextField();
        fieldWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldWide.setHorizontalAlignment(JLabel.LEFT);

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
        task2.setTitle("Koordinat");
        task2.add(createCoordinatPanel());
        task2.setAnimated(false);

        JXTaskPane task3 = new JXTaskPane();
        task3.setTitle("Legalitas");
        task3.add(createLegalityPanel());
        task3.setAnimated(false);

        JXTaskPane task4 = new JXTaskPane();
        task4.setTitle("Dokumen");
        task4.add(createDocumentPanel());
        task4.setAnimated(false);

        JXTaskPane task5 = new JXTaskPane();
        task5.setTitle("Deskripsi Bangunan");
        task5.add(createBuildingDescriptionPanel());
        task5.setAnimated(false);

        JXTaskPane task6 = new JXTaskPane();
        task6.setTitle("Faktor Yang Berpengaruh Dalam Penilaian");
        task6.add(createPointPanel());
        task6.setAnimated(false);

        container.add(task);
        container.add(task2);
        container.add(task3);
        container.add(task4);
        container.add(task5);
        container.add(task6);

        taskArrays.add(task);
        taskArrays.add(task2);
        taskArrays.add(task3);
        taskArrays.add(task4);
        taskArrays.add(task5);
        taskArrays.add(task6);

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

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Pilih Bidang / Sub Bidang Barang");

        btChooseItemsBuilding.setActionRichTooltip(addTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btChooseItemsBuilding);

        return buttonStrip;
    }

    private Component createMainDataPanel() {

        FormLayout lm = new FormLayout(
                "50px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "50px,fill:default:grow,5px,"
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

        builder.addLabel("Kondisi", cc.xy(2, 8));
        builder.add(comboCondition, cc.xyw(4, 8, 5));

        builder.addLabel("Bangunan Tingkat", cc.xy(2, 10));
        builder.add(radioRaised, cc.xyw(4, 10, 5));

        builder.addLabel("Beton", cc.xy(2, 12));
        builder.add(radioFrameworks, cc.xyw(4, 12, 5));

        builder.addLabel("Luas Lantai (M2)", cc.xy(2, 14));
        builder.add(fieldWide, cc.xyw(4, 14, 3));

        builder.addLabel("Alamat", cc.xy(2, 16));
        builder.add(fieldAddress, cc.xyw(4, 16, 5));

        builder.addLabel("Kecamatan", cc.xy(2, 18));
        builder.add(comboUrban, cc.xyw(4, 18, 5));

        builder.addLabel("Kelurahan", cc.xy(2, 20));
        builder.add(comboSubUrban, cc.xyw(4, 20, 5));

        builder.addLabel("Tanggal Dokumen", cc.xy(2, 22));
        builder.add(fieldDocumentDate, cc.xyw(4, 22, 3));

        builder.addLabel("Nomor Dokumen", cc.xy(2, 24));
        builder.add(fieldDocumentNumber, cc.xyw(4, 24, 5));

        builder.addLabel("Tahun Perolehan", cc.xy(2, 26));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 26, 3));
        builder.add(checkUnknown, cc.xyw(8, 26, 1));

        builder.addLabel("Luas Tanah (M2)", cc.xy(2, 28));
        builder.add(fieldBuildingWide, cc.xyw(4, 28, 3));

        builder.addLabel("Status Tanah", cc.xy(2, 30));
        builder.add(fieldBuildingState, cc.xyw(4, 30, 5));

        builder.addLabel("Nomor Kode Tanah", cc.xy(2, 32));
        builder.add(createStrip2(1.0, 1.0), cc.xyw(4, 32, 1));
        builder.add(fieldBuildingCode, cc.xyw(6, 32, 3));

        builder.addLabel("Asal-Usul", cc.xy(2, 34));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 34, 5));

        builder.addLabel("Harga Satuan", cc.xy(2, 36));
        builder.add(fieldPrice, cc.xyw(4, 36, 5));

        builder.addLabel("Honor + Admin", cc.xy(2, 38));
        builder.add(fieldAdminCost, cc.xyw(4, 38, 5));

        builder.addLabel("Jumlah Harga", cc.xy(2, 40));
        builder.add(fieldTotalPrice, cc.xyw(4, 40, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 42));
        builder.add(fieldFundingSource, cc.xyw(4, 42, 5));

        builder.addLabel("Keterangan", cc.xy(2, 44));
        builder.add(scPane, cc.xywh(4, 44, 5, 3));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createCoordinatPanel() {

        FormLayout lm = new FormLayout(
                "50px,right:pref,10px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Bujur Timur", cc.xy(2, 2));
        builder.add(fieldEastLongitudeCoord, cc.xyw(4, 2, 3));

        builder.addLabel("Lintang Selatan", cc.xy(2, 4));
        builder.add(fieldSouthLatitudeCoord, cc.xyw(4, 4, 3));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        return panel;
    }

    private Component createLegalityPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Status", cc.xy(2, 2));
        builder.add(radioOwnerShipStateName, cc.xyw(4, 2, 3));

        builder.addLabel("Hubungan Kepemilikan", cc.xy(2, 4));
        builder.add(radioOwnerShipRelation, cc.xyw(4, 4, 3));

        builder.addLabel("Okupansi", cc.xy(2, 6));
        builder.add(radioOwnerShipOccupancy, cc.xyw(4, 6, 3));

        builder.addLabel("Dasar Menempati", cc.xy(2, 8));
        builder.add(radioOwnerShipOccupaying, cc.xyw(4, 8, 3));


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
                + "pref,5px,pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("PBB", cc.xyw(2, 2, 5));

        builder.addLabel("     Nomor", cc.xy(2, 4));
        builder.add(fieldBuildingTaxNumber, cc.xyw(4, 4, 3));

        builder.addLabel("     Tanggal", cc.xy(2, 6));
        builder.add(fieldBuildingTaxDate, cc.xyw(4, 6, 1));

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

    private Component createBuildingDescriptionPanel() {
        FormLayout lm = new FormLayout(
                "50px,pref,20px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "pref,5px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "30px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis", cc.xy(2, 2));
        builder.add(radioType, cc.xyw(4, 2, 3));

        builder.addLabel("Bentuk", cc.xy(2, 4));
        builder.add(radioShape, cc.xywh(4, 4, 3, 3));

        builder.addLabel("Pemanfaatan", cc.xy(2, 8));
        builder.add(radioUtilization, cc.xywh(4, 8, 3, 3));

        builder.addLabel("Usia", cc.xy(2, 12));
        builder.add(radioAges, cc.xyw(4, 12, 3));

        builder.addLabel("Tingkat", cc.xy(2, 14));
        builder.add(radioLevels, cc.xyw(4, 14, 3));

        builder.addLabel("Pondasi", cc.xy(2, 16));
        builder.add(radioFoundation, cc.xyw(4, 16, 3));

        builder.addLabel("Lantai", cc.xy(2, 18));
        builder.add(radioFloorType, cc.xyw(4, 18, 3));

        builder.addLabel("Dinding", cc.xy(2, 20));
        builder.add(radioWallType, cc.xyw(4, 20, 3));

        builder.addLabel("Plafond", cc.xy(2, 22));
        builder.add(radioCeillingType, cc.xyw(4, 22, 3));

        builder.addLabel("Atap", cc.xy(2, 24));
        builder.add(radioRoofType, cc.xywh(4, 24, 3, 3));

        builder.addLabel("Kusen", cc.xy(2, 26));
        builder.add(radioFrameType, cc.xyw(4, 26, 3));

        builder.addLabel("Pintu", cc.xy(2, 28));
        builder.add(radioDoorType, cc.xyw(4, 28, 3));

        builder.addLabel("Jendela", cc.xy(2, 30));
        builder.add(radioWindowType, cc.xyw(4, 30, 3));

        builder.addLabel("Ruangan", cc.xy(2, 32));
        builder.add(radioRoomType, cc.xyw(4, 32, 3));

        builder.addLabel("Kualitas Bahan", cc.xy(2, 36));
        builder.add(radioMaterialQuality, cc.xyw(4, 36, 3));

        builder.addLabel("Fasilitas/Sarana", cc.xy(2, 38));
        builder.add(radioFacilities, cc.xyw(4, 38, 3));

        builder.addLabel("Kondisi", cc.xy(2, 40));
        builder.add(comboBuildingCondition, cc.xyw(4, 40, 3));

        builder.addLabel("Perawatan", cc.xy(2, 42));
        builder.add(radioNursing, cc.xyw(4, 42, 3));

        builder.addLabel("Kelas Bangunan", cc.xy(2, 44));
        builder.add(radioBuildingClass, cc.xyw(4, 44, 3));


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
                + "pref,5px,pref,5px,pref,5px,"
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

        builder.addLabel("Kualitas Bangunan", cc.xy(2, 10));
        builder.add(radioQualityPoint, cc.xyw(4, 10, 3));

        builder.addLabel("Kondisi Bangunan", cc.xy(2, 12));
        builder.add(radioConditionPoint, cc.xyw(4, 12, 3));

        builder.addLabel("Tata Ruang", cc.xy(2, 14));
        builder.add(radioLayoutPoint, cc.xyw(4, 14, 3));

        builder.addLabel("Perawatan", cc.xy(2, 16));
        builder.add(radioNursingPoint, cc.xyw(4, 16, 3));

        builder.addLabel("Minat Pasar", cc.xy(2, 18));
        builder.add(radioMarketInterestPoint, cc.xyw(4, 18, 3));

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

        JXTitledPanel titledPanel = new JXTitledPanel("Input KIB-C");

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
        fieldWide.setValue(BigDecimal.ZERO);
        radioRaised.setAllDeselected();
        comboCondition.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldAdminCost.setValue(BigDecimal.ZERO);
        fieldTotalPrice.setValue(BigDecimal.ZERO);
        checkUnknown.setSelected(false);
        clearYear();
        fieldBuildingWide.setValue(BigDecimal.ZERO);
        fieldAddress.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldDocumentDate.setDate(null);
        fieldDocumentNumber.setText("");
        fieldBuildingCode.setText("");
        fieldBuildingState.setText("");
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
        //
        fieldEastLongitudeCoord.setText("");
        fieldSouthLatitudeCoord.setText("");
        //
        radioOwnerShipStateName.setAllDeselected();
        radioOwnerShipRelation.setAllDeselected();
        radioOwnerShipOccupancy.setAllDeselected();
        radioOwnerShipOccupaying.setAllDeselected();
        //
        fieldBuildingTaxNumber.setText("");
        fieldBuildingTaxDate.setDate(null);
        fieldBuildPermitNumber.setText("");
        fieldBuildPermitDate.setDate(null);
        fieldAdvisPlanningNumber.setText("");
        fieldAdvisPlanningDate.setDate(null);
        //
        radioType.setAllDeselected();
        radioShape.setAllDeselected();
        radioUtilization.setAllDeselected();
        radioAges.setAllDeselected();
        radioLevels.setAllDeselected();
        radioFrameworks.setAllDeselected();
        radioFoundation.setAllDeselected();
        radioFloorType.setAllDeselected();
        radioWallType.setAllDeselected();
        radioCeillingType.setAllDeselected();
        radioRoofType.setAllDeselected();
        radioFrameType.setAllDeselected();
        radioDoorType.setAllDeselected();
        radioWindowType.setAllDeselected();
        radioRoomType.setAllDeselected();
        radioMaterialQuality.setAllDeselected();
        radioFacilities.setAllDeselected();
        comboBuildingCondition.getEditor().setItem(null);
        radioNursing.setAllDeselected();
        radioBuildingClass.setAllDeselected();
        //
        radioAllotmentPoint.setAllDeselected();
        radioUtilizationPoint.setAllDeselected();
        radioLocationPoint.setAllDeselected();
        radioAccessionPoint.setAllDeselected();
        radioQualityPoint.setAllDeselected();
        radioConditionPoint.setAllDeselected();
        radioLayoutPoint.setAllDeselected();
        radioNursingPoint.setAllDeselected();
        radioMarketInterestPoint.setAllDeselected();

    }

    private void setFormValues() {
        if (selectedItemsBuilding != null) {
            try {
                if (selectedItemsBuilding.getUnit() == null) {
                    comboUnit.getEditor().setItem(null);
                } else {
                    comboUnit.setSelectedItem(selectedItemsBuilding.getUnit());
                }

                ItemsCategory category = new ItemsCategory();
                category.setCategoryCode(selectedItemsBuilding.getItemCode());
                category.setCategoryName(selectedItemsBuilding.getItemName());
                category.setTypes(ItemsCategory.ITEMS_BUILDING);
                category.setStyled(true);

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);

                fieldItems.setText(category.getCategoryCode() + " " + getCompleteItemsName());

                fieldRegNumber.setText(selectedItemsBuilding.getRegNumber());

                fieldWide.setValue(selectedItemsBuilding.getWide());
                radioRaised.setSelectedData(selectedItemsBuilding.getRaised());

                fieldPrice.setValue(selectedItemsBuilding.getPrice());
                fieldAdminCost.setValue(selectedItemsBuilding.getAdminCost());
                fieldTotalPrice.setValue(selectedItemsBuilding.getTotalPrice());

                if (selectedItemsBuilding.getAcquisitionYear() == null) {
                    checkUnknown.setSelected(true);
                    clearYear();
                } else {
                    checkUnknown.setSelected(false);
                    fieldAcquisitionYear.setYear(selectedItemsBuilding.getAcquisitionYear());
                }

                fieldAcquisitionYear.setEnabled(!checkUnknown.isSelected());

                fieldAddress.setText(selectedItemsBuilding.getAddress());

                if (selectedItemsBuilding.getUrban() == null) {
                    comboUrban.getEditor().setItem(null);
                } else {
                    comboUrban.setSelectedItem(selectedItemsBuilding.getUrban());
                }

                if (selectedItemsBuilding.getSubUrban() == null) {
                    comboSubUrban.getEditor().setItem(null);
                } else {
                    comboSubUrban.setSelectedItem(selectedItemsBuilding.getSubUrban());
                }

                fieldDocumentDate.setDate(selectedItemsBuilding.getDocumentDate());
                fieldDocumentNumber.setText(selectedItemsBuilding.getDocumentNumber());

                fieldBuildingWide.setValue(selectedItemsBuilding.getLandWide());
                fieldBuildingCode.setText(selectedItemsBuilding.getLandCode());
                fieldBuildingState.setText(selectedItemsBuilding.getLandState());

                if (selectedItemsBuilding.getCondition() == null) {
                    comboCondition.getEditor().setItem(null);
                } else {
                    comboCondition.setSelectedItem(selectedItemsBuilding.getCondition());
                }

                fieldFundingSource.setText(selectedItemsBuilding.getFundingSource());
                fieldAcquisitionWay.setText(selectedItemsBuilding.getAcquitionWay());
                fieldDescription.setText(selectedItemsBuilding.getDescription());
                //
                fieldEastLongitudeCoord.setText(selectedItemsBuilding.getEastLongitudeCoord());
                fieldSouthLatitudeCoord.setText(selectedItemsBuilding.getSouthLatitudeCoord());
                //
                radioOwnerShipStateName.setSelectedData(selectedItemsBuilding.getOwnerShipStateName());
                radioOwnerShipRelation.setSelectedData(selectedItemsBuilding.getOwnerShipRelation());
                radioOwnerShipOccupancy.setSelectedData(selectedItemsBuilding.getOwnerShipOccupancy());
                radioOwnerShipOccupaying.setSelectedData(selectedItemsBuilding.getOwnerShipOccupaying());
                //
                fieldBuildingTaxNumber.setText(selectedItemsBuilding.getLandTaxNumber());
                fieldBuildingTaxDate.setDate(selectedItemsBuilding.getLandTaxDate());
                fieldBuildPermitNumber.setText(selectedItemsBuilding.getBuildPermitNumber());
                fieldBuildPermitDate.setDate(selectedItemsBuilding.getBuildPermitDate());
                fieldAdvisPlanningNumber.setText(selectedItemsBuilding.getAdvisPlanningNumber());
                fieldAdvisPlanningDate.setDate(selectedItemsBuilding.getAdvisPlanningDate());
                //
                radioType.setSelectedData(selectedItemsBuilding.getType());
                radioShape.setSelectedData(selectedItemsBuilding.getShape());
                radioUtilization.setSelectedData(selectedItemsBuilding.getUtilization());
                radioAges.setSelectedData(selectedItemsBuilding.getAges());
                radioLevels.setSelectedData(selectedItemsBuilding.getLevels());
                radioFrameworks.setSelectedData(selectedItemsBuilding.getFrameworks());
                radioFoundation.setSelectedData(selectedItemsBuilding.getFoundation());
                radioFloorType.setSelectedData(selectedItemsBuilding.getFloorType());
                radioWallType.setSelectedData(selectedItemsBuilding.getWallType());
                radioCeillingType.setSelectedData(selectedItemsBuilding.getCeillingType());
                radioRoofType.setSelectedData(selectedItemsBuilding.getRoofType());
                radioFrameType.setSelectedData(selectedItemsBuilding.getFrameType());
                radioDoorType.setSelectedData(selectedItemsBuilding.getDoorType());
                radioWindowType.setSelectedData(selectedItemsBuilding.getWindowType());
                radioRoomType.setSelectedData(selectedItemsBuilding.getRoomType());
                radioMaterialQuality.setSelectedData(selectedItemsBuilding.getMaterialQuality());
                radioFacilities.setSelectedData(selectedItemsBuilding.getFacilities());

                if (selectedItemsBuilding.getBuildingCondition() != null) {
                    try {
                        selectedItemsBuilding.setBuildingCondition(mLogic.getCondition(mainframe.getSession(), selectedItemsBuilding.getCondition()));
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                if (selectedItemsBuilding.getBuildingCondition() == null) {
                    comboBuildingCondition.getEditor().setItem(null);
                } else {
                    comboBuildingCondition.setSelectedItem(selectedItemsBuilding.getBuildingCondition());
                }

                radioNursing.setSelectedData(selectedItemsBuilding.getNursing());
                radioBuildingClass.setSelectedData(selectedItemsBuilding.getBuildingClass());
                //
                radioAllotmentPoint.setSelectedData(selectedItemsBuilding.getAllotmentPoint());
                radioUtilizationPoint.setSelectedData(selectedItemsBuilding.getUtilizationPoint());
                radioLocationPoint.setSelectedData(selectedItemsBuilding.getLocationPoint());
                radioAccessionPoint.setSelectedData(selectedItemsBuilding.getAccesionPoint());
                radioQualityPoint.setSelectedData(selectedItemsBuilding.getQualityPoint());
                radioConditionPoint.setSelectedData(selectedItemsBuilding.getConditionPoint());
                radioLayoutPoint.setSelectedData(selectedItemsBuilding.getLayoutPoint());
                radioNursingPoint.setSelectedData(selectedItemsBuilding.getNursingPoint());
                radioMarketInterestPoint.setSelectedData(selectedItemsBuilding.getMarketInterestPoint());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private ItemsBuilding getItemsBuilding() throws MotekarException {
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
        String raised = radioRaised.getSelectedData();

        Integer acquisitionYear = fieldAcquisitionYear.getYear();

        if (checkUnknown.isSelected()) {
            acquisitionYear = null;
        }

        String address = fieldAddress.getText();

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

        Date documentDate = fieldDocumentDate.getDate();
        String documentNumber = fieldDocumentNumber.getText();



        String landCode = fieldBuildingCode.getText();
        String landState = fieldBuildingState.getText();

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

        Object objWide = fieldWide.getValue();
        BigDecimal wide = BigDecimal.ZERO;

        if (objWide instanceof Long) {
            Long value = (Long) objWide;
            wide = BigDecimal.valueOf(value.intValue());
        } else if (objWide instanceof BigDecimal) {
            wide = (BigDecimal) objWide;
        }

        Object objLWide = fieldBuildingWide.getValue();
        BigDecimal landWide = BigDecimal.ZERO;

        if (objLWide instanceof Long) {
            Long value = (Long) objLWide;
            landWide = BigDecimal.valueOf(value.intValue());
        } else if (objLWide instanceof BigDecimal) {
            landWide = (BigDecimal) objLWide;
        }

        String fundingSource = fieldFundingSource.getText();
        String acquisitionWay = fieldAcquisitionWay.getText();
        String description = fieldDescription.getText();
        //
        String eastLongitudeCoord = fieldEastLongitudeCoord.getText();
        String southLatitudeCoord = fieldSouthLatitudeCoord.getText();
        //
        String ownerShipStateName = radioOwnerShipStateName.getSelectedData();
        String ownerShipRelation = radioOwnerShipRelation.getSelectedData();
        String ownerShipOccupancy = radioOwnerShipOccupancy.getSelectedData();
        String ownerShipOccupaying = radioOwnerShipOccupaying.getSelectedData();
        //
        String landTaxNumber = fieldBuildingTaxNumber.getText();
        Date landTaxDate = fieldBuildingTaxDate.getDate();
        String buildPermitNumber = fieldBuildPermitNumber.getText();
        Date buildPermitDate = fieldBuildPermitDate.getDate();
        String advisPlanningNumber = fieldAdvisPlanningNumber.getText();
        Date advisPlanningDate = fieldAdvisPlanningDate.getDate();
        //
        String type = radioType.getSelectedData();
        String shape = radioShape.getSelectedData();
        String utilization = radioUtilization.getSelectedData();
        String ages = radioAges.getSelectedData();
        String levels = radioLevels.getSelectedData();
        String frameworks = radioFrameworks.getSelectedData();
        String foundation = radioFoundation.getSelectedData();
        String floorType = radioFloorType.getSelectedData();
        String wallType = radioWallType.getSelectedData();
        String ceillingType = radioCeillingType.getSelectedData();
        String roofType = radioRoofType.getSelectedData();
        String frameType = radioFrameType.getSelectedData();
        String doorType = radioDoorType.getSelectedData();
        String windowType = radioWindowType.getSelectedData();
        String roomType = radioRoomType.getSelectedData();
        String materialQuality = radioMaterialQuality.getSelectedData();
        String facilities = radioFacilities.getSelectedData();
        Condition buildingCondition = null;

        Object bConObj = comboBuildingCondition.getSelectedItem();
        if (bConObj instanceof Condition) {
            buildingCondition = (Condition) bConObj;
        }

        String nursing = radioNursing.getSelectedData();
        String buildingClass = radioBuildingClass.getSelectedData();
        //
        String allotmentPoint = radioAllotmentPoint.getSelectedData();
        String utilizationPoint = radioUtilizationPoint.getSelectedData();
        String locationPoint = radioLocationPoint.getSelectedData();
        String accessionPoint = radioAccessionPoint.getSelectedData();
        String qualityPoint = radioQualityPoint.getSelectedData();
        String conditionPoint = radioConditionPoint.getSelectedData();
        String layoutPoint = radioLayoutPoint.getSelectedData();
        String nursingPoint = radioNursingPoint.getSelectedData();
        String marketInterestPoint = radioMarketInterestPoint.getSelectedData();

        if (itemCode.equals("") || itemName.equals("")) {
            errorString.append("<br>- Kode / Nama Barang Harus Lengkap</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan : " + errorString.toString() + "</html>");
        }

        ItemsBuilding building = new ItemsBuilding();
        building.setUnit(unit);
        building.setItemCode(itemCode);
        building.setItemName(itemName);
        building.setRegNumber(regNumber);
        building.setWide(wide);
        building.setLandWide(landWide);
        building.setRaised(raised);
        building.setAddress(address);
        building.setUrban(urban);
        building.setSubUrban(subUrban);
        building.setDocumentDate(documentDate);
        building.setDocumentNumber(documentNumber);
        building.setLandCode(landCode);
        building.setLandState(landState);
        building.setAcquitionWay(acquisitionWay);
        building.setAcquisitionYear(acquisitionYear);
        building.setCondition(condition);
        building.setPrice(price);
        building.setAdminCost(adminCost);
        building.setFundingSource(fundingSource);
        building.setDescription(description);
        //
        building.setEastLongitudeCoord(eastLongitudeCoord);
        building.setSouthLatitudeCoord(southLatitudeCoord);
        //
        building.setOwnerShipStateName(ownerShipStateName);
        building.setOwnerShipRelation(ownerShipRelation);
        building.setOwnerShipOccupancy(ownerShipOccupancy);
        building.setOwnerShipOccupaying(ownerShipOccupaying);
        //
        building.setLandTaxNumber(landTaxNumber);
        building.setLandTaxDate(landTaxDate);
        building.setBuildPermitNumber(buildPermitNumber);
        building.setBuildPermitDate(buildPermitDate);
        building.setAdvisPlanningNumber(advisPlanningNumber);
        building.setAdvisPlanningDate(advisPlanningDate);
        //
        building.setType(type);
        building.setShape(shape);
        building.setUtilization(utilization);
        building.setAges(ages);
        building.setLevels(levels);
        building.setFrameworks(frameworks);
        building.setFoundation(foundation);
        building.setFloorType(floorType);
        building.setWallType(wallType);
        building.setCeillingType(ceillingType);
        building.setRoofType(roofType);
        building.setFrameType(frameType);
        building.setDoorType(doorType);
        building.setWindowType(windowType);
        building.setRoomType(roomType);
        building.setMaterialQuality(materialQuality);
        building.setFacilities(facilities);
        building.setBuildingCondition(buildingCondition);
        building.setNursing(nursing);
        building.setBuildingClass(buildingClass);
        //
        building.setAllotmentPoint(allotmentPoint);
        building.setUtilizationPoint(utilizationPoint);
        building.setLocationPoint(locationPoint);
        building.setAccesionPoint(accessionPoint);
        building.setQualityPoint(qualityPoint);
        building.setConditionPoint(conditionPoint);
        building.setLayoutPoint(layoutPoint);
        building.setNursingPoint(nursingPoint);
        building.setMarketInterestPoint(marketInterestPoint);

        if (selectedItemsBuilding != null && btPanel.isEditstate()) {
            building.setUserName(selectedItemsBuilding.getUserName());
        } else {
            building.setUserName(userAccount.getUserName());
        }
        building.setLastUserName(userAccount.getUserName());

        return building;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        setTaskPaneState();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboUnit.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        setTaskPaneState();
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
                ArrayList<ItemsBuilding> selectedItemsBuildings = table.getSelectedItemsBuildings();
                if (!selectedItemsBuildings.isEmpty()) {
                    logic.deleteItemsBuilding(mainframe.getSession(), selectedItemsBuildings);
                    table.removeItemsBuilding(selectedItemsBuildings);
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
            ItemsBuilding newItemsBuilding = getItemsBuilding();
            if (btPanel.isNewstate()) {
                newItemsBuilding = logic.insertItemsBuilding(mainframe.getSession(), newItemsBuilding);
                table.addItemsBuilding(newItemsBuilding);
                selectedItemsBuilding = newItemsBuilding;
                selectedItemsBuilding.setItemName(getLastItemsName());
                selectedItemsBuilding.setFullItemName(getCompleteItemsName());
            } else if (btPanel.isEditstate()) {
                newItemsBuilding = logic.updateItemsBuilding(mainframe.getSession(), selectedItemsBuilding, newItemsBuilding);
                table.updateSelectedItemsBuilding(newItemsBuilding);
                selectedItemsBuilding = newItemsBuilding;
                selectedItemsBuilding.setItemName(getLastItemsName());
                selectedItemsBuilding.setFullItemName(getCompleteItemsName());
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
                ItemsBuildingWorkbookReader wbr = new ItemsBuildingWorkbookReader();
                List<ItemsBuilding> items = wbr.loadXLSFile(file.getPath());

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
                    for (ItemsBuilding item : items) {
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

                        String address = item.getAddress();
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
                                    item.setAddress("");
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

                    importWorker = new ImportItemsBuilding(items);
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

        chooser.setSelectedFile(new File(ItemsBuildingWorkbookCreator.TEMPLATE_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            
            oldCursor = mainframe.getCursor();
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            File file = chooser.getSelectedFile();
            ItemsBuildingWorkbookCreator wb = new ItemsBuildingWorkbookCreator(mainframe, file.getPath(), new ArrayList<ItemsBuilding>(), unitMaster);
            wb.createXLSFile();
            
            mainframe.setCursor(oldCursor);
        }
    }

    public void onPrint() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Unit unit = filterPanel.getUnit();

        if (unit != null) {
            ItemsBuildingJasper report = new ItemsBuildingJasper("Buku Inventaris Barang Gedung dan Bangunan (KIB-C)", table.getItemsBuildings(), unit);
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

        chooser.setSelectedFile(new File(ItemsBuildingWorkbookCreator.DATA_FILE_NAME));
        int retVal = chooser.showSaveDialog(mainframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            ArrayList<ItemsBuilding> buildings = table.getItemsBuildings();

            ItemsBuildingWorkbookCreator wb = new ItemsBuildingWorkbookCreator(mainframe, file.getPath(), buildings, unitMaster);
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

        ArrayList<String> twoState = new ArrayList<String>();
        twoState.add("Ya");
        twoState.add("Tidak");

        filterValues.add(new FilterValue("Kondisi Bangunan", "condition", FilterCardPanel.COMBO_LONG_PANEL, null, comboCategoryVal));
        filterValues.add(new FilterValue("Bertingkat", "israised", FilterCardPanel.COMBO_PANEL, twoState, null));
        filterValues.add(new FilterValue("Beton", "framework", FilterCardPanel.COMBO_PANEL, twoState, null));
        filterValues.add(new FilterValue("Luas Lantai", "wide", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Letak/Lokasi/Alamat", "addresslocation", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Dokumen", "documentnumber", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Tanggal Dokumen", "documentdate", FilterCardPanel.DATE_PANEL));
        filterValues.add(new FilterValue("Luas Tanah", "landwide", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Status Tanah", "landstatus", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Kode Tanah", "landcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Asal-Usul", "fundingsources", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Harga Perolehan", "buildingprice", FilterCardPanel.NUMBER_PANEL));
        filterValues.add(new FilterValue("Keterangan", "description", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ItemsBuildingTable extends JXTable {

        private ItemsBuildingTableModel model;

        public ItemsBuildingTable() {
            model = new ItemsBuildingTableModel();
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

            ColumnGroup group3 = new ColumnGroup("<html><center><b>Dokumen<br>Gedung</br></b></center>");
            group3.add(colModel.getColumn(10));
            group3.add(colModel.getColumn(11));

            ColumnGroup group4 = new ColumnGroup("<html><center><b>Harga Perolehan<br>(Rp.)</br></b></center>");
            group4.add(colModel.getColumn(16));
            group4.add(colModel.getColumn(17));
            group4.add(colModel.getColumn(18));

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

            worker = new LoadItemsBuilding(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsBuilding getSelectedItemsBuilding() {
            ArrayList<ItemsBuilding> selectedItemsBuildings = getSelectedItemsBuildings();
            return selectedItemsBuildings.get(0);
        }

        public ArrayList<ItemsBuilding> getItemsBuildings() {
            return model.getItemsBuildings();
        }

        public ArrayList<ItemsBuilding> getSelectedItemsBuildings() {

            ArrayList<ItemsBuilding> buildings = new ArrayList<ItemsBuilding>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsBuilding building = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsBuilding) {
                            building = (ItemsBuilding) obj;
                            buildings.add(building);
                        }
                    }
                }
            }

            return buildings;
        }

        public void updateSelectedItemsBuilding(ItemsBuilding building) {
            model.updateRow(getSelectedItemsBuilding(), building);
        }

        public void removeItemsBuilding(ArrayList<ItemsBuilding> buildings) {
            if (!buildings.isEmpty()) {
                for (ItemsBuilding building : buildings) {
                    model.remove(building);
                }
            }

        }

        public void addItemsBuilding(ArrayList<ItemsBuilding> buildings) {
            if (!buildings.isEmpty()) {
                for (ItemsBuilding building : buildings) {
                    model.add(building);
                }
            }
        }

        public void addItemsBuilding(ItemsBuilding building) {
            model.add(building);
        }

        public void insertEmptyItemsBuilding() {
            addItemsBuilding(new ItemsBuilding());
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

    private static class ItemsBuildingTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 22;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Kondisi<br>Bangunan</br><br>(RB,R,KB,B,SB)</br></b></center>",
            "<html><b>Bertingkat</b>", "<html><b>Beton</b>",
            "<html><center><b>Luas Lantai<br>(M<sup>2</sup>)</br></b></center>",
            "<html><center><b>Letak/Lokasi/<br>Alamat</br></b></center>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><center><b>Luas Tanah<br>(M<sup>2</sup>)</br></b></center>",
            "<html><center><b>Status<br>Tanah</br></b></center>",
            "<html><center><b>Nomor Kode<br>Tanah</br></b></center>",
            "<html><center><b>Asal-Usul<br>Pembiayaan</br></b></center>",
            "<html><b>Satuan</b>", "<html><b>Honor + ADM</b>", "<html><b>Jumlah</b>",
            "<html><b>Keterangan</b>", "<html><b>Di Input Oleh</b>", "<html><b>Diubah Oleh</b>"};
        private ArrayList<ItemsBuilding> buildings = new ArrayList<ItemsBuilding>();

        public ItemsBuildingTableModel() {
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
            } else if (columnIndex == 8 || columnIndex == 12 || columnIndex == 16
                    || columnIndex == 17 || columnIndex == 18) {
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

        public void add(ArrayList<ItemsBuilding> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            buildings.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsBuilding building) {
            insertRow(getRowCount(), building);
        }

        public void insertRow(int row, ItemsBuilding building) {
            buildings.add(row, building);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsBuilding oldItemsBuilding, ItemsBuilding newItemsBuilding) {
            int index = buildings.indexOf(oldItemsBuilding);
            buildings.set(index, newItemsBuilding);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsBuilding building) {
            int row = buildings.indexOf(building);
            buildings.remove(building);
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
                buildings.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                buildings.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            buildings.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (buildings.get(i) == null) {
                    buildings.set(i, new ItemsBuilding());
                }
            }
        }

        public ArrayList<ItemsBuilding> getItemsBuildings() {
            return buildings;
        }

        @Override
        public int getRowCount() {
            return buildings.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsBuilding getItemsBuilding(int row) {
            if (!buildings.isEmpty()) {
                return buildings.get(row);
            }
            return new ItemsBuilding();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsBuilding building = getItemsBuilding(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        building.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsBuilding) {
                        building = (ItemsBuilding) aValue;
                    }
                    break;
                case 3:
                    building.setItemCode((String) aValue);
                    break;
                case 4:
                    building.setRegNumber((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof Condition) {
                        building.setCondition((Condition) aValue);
                    }
                    break;
                case 6:
                    building.setRaised((String) aValue);
                    break;
                case 7:
                    building.setFrameworks((String) aValue);
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        building.setWide((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    building.setAddress((String) aValue);
                    break;
                case 10:
                    building.setDocumentDate((Date) aValue);
                    break;
                case 11:
                    building.setDocumentNumber((String) aValue);
                    break;
                case 12:
                    if (aValue instanceof BigDecimal) {
                        building.setLandWide((BigDecimal) aValue);
                    }
                    break;
                case 13:
                    building.setLandState((String) aValue);
                    break;
                case 14:
                    building.setLandCode((String) aValue);
                    break;
                case 15:
                    building.setAcquitionWay((String) aValue);
                    break;
                case 16:
                    if (aValue instanceof BigDecimal) {
                        building.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 17:
                    if (aValue instanceof BigDecimal) {
                        building.setAdminCost((BigDecimal) aValue);
                    }
                    break;
                case 18:
                    break;
                case 19:
                    building.setDescription((String) aValue);
                    break;
                case 20:
                    building.setUserName((String) aValue);
                    break;
                case 21:
                    building.setLastUserName((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsBuilding building = getItemsBuilding(row);
            switch (column) {
                case 0:
                    toBeDisplayed = building.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = building;
                    break;
                case 3:
                    toBeDisplayed = building.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = building.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = building.getCondition();
                    break;
                case 6:
                    toBeDisplayed = building.getRaised();
                    break;
                case 7:
                    toBeDisplayed = building.getFrameworks();
                    break;
                case 8:
                    toBeDisplayed = building.getWide();
                    break;
                case 9:
                    StringBuilder builder = new StringBuilder();
                    builder.append(building.getAddress());

                    if (building.getUrban() != null) {
                        Region urban = building.getUrban();
                        builder.append(" Kecamatan ").append(urban.getRegionName());
                    }

                    if (building.getSubUrban() != null) {
                        Region subUrban = building.getSubUrban();
                        builder.append(" Kelurahan ").append(subUrban.getRegionName());
                    }

                    toBeDisplayed = builder.toString();
                    break;
                case 10:
                    toBeDisplayed = building.getDocumentDate();
                    break;
                case 11:
                    toBeDisplayed = building.getDocumentNumber();
                    break;
                case 12:
                    toBeDisplayed = building.getLandWide();
                    break;
                case 13:
                    toBeDisplayed = building.getLandState();
                    break;
                case 14:
                    toBeDisplayed = building.getLandCode();
                    break;
                case 15:
                    toBeDisplayed = building.getAcquitionWay();
                    break;
                case 16:
                    toBeDisplayed = building.getPrice();
                    break;
                case 17:
                    toBeDisplayed = building.getAdminCost();
                    break;
                case 18:
                    toBeDisplayed = building.getTotalPrice();
                    break;
                case 19:
                    toBeDisplayed = building.getDescription();
                    break;
                case 20:
                    toBeDisplayed = building.getUserName();
                    break;
                case 21:
                    toBeDisplayed = building.getLastUserName();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsBuilding extends SwingWorker<ItemsBuildingTableModel, ItemsBuilding> {

        private ItemsBuildingTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsBuilding(String modifier) {
            this.model = (ItemsBuildingTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsBuilding> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsBuilding building : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat KIB-C " + building.getItemName());
                model.add(building);
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
        protected ItemsBuildingTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsBuilding> buildings = logic.getItemsBuilding(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!buildings.isEmpty()) {
                    for (int i = 0; i < buildings.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / buildings.size();

                        ItemsBuilding building = buildings.get(i);

                        if (building.getUnit() != null) {
                            building.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), building.getUnit()));
                        }

                        if (building.getCondition() != null) {
                            building.setCondition(mLogic.getCondition(mainframe.getSession(), building.getCondition()));
                        }

                        if (building.getUrban() != null) {
                            building.setUrban(mLogic.getRegion(mainframe.getSession(), building.getUrban()));
                        }

                        if (building.getSubUrban() != null) {
                            building.setSubUrban(mLogic.getRegion(mainframe.getSession(), building.getSubUrban()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(building.getItemCode());
                        category.setCategoryName(building.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_BUILDING);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        building.setItemName(getLastItemsName(iLookUp));
                        building.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(building);
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
                JXErrorPane.showDialog(ItemsBuildingPanel.this, info);
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

    private class ImportItemsBuilding extends SwingWorker<ItemsBuildingTableModel, ItemsBuilding> {

        private ItemsBuildingTableModel model;
        private Exception exception;
        private List<ItemsBuilding> data;

        public ImportItemsBuilding(List<ItemsBuilding> data) {
            this.model = (ItemsBuildingTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsBuilding> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsBuilding land : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload KIB-C " + land.getItemName());
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
        protected ItemsBuildingTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                ItemsBuilding ib = data.get(0);
                Unit ibUnit = ib.getUnit();

                if (ibUnit != null) {
                    table.clear();
                    logic.deleteItemsBuilding(mainframe.getSession(), ibUnit);
                }
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        ItemsBuilding building = data.get(i);

                        building.setUserName(userAccount.getUserName());
                        building.setLastUserName(userAccount.getUserName());

                        synchronized (building) {
                            building = logic.insertItemsBuilding(mainframe.getSession(), building);
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(building.getItemCode());
                        category.setCategoryName(building.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_BUILDING);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                        building.setItemName(getLastItemsName(iLookUp));
                        building.setFullItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(building);
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
                JXErrorPane.showDialog(ItemsBuildingPanel.this, info);
            }

            table.packAll();
            
            mainframe.setCursor(oldCursor);
            
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
