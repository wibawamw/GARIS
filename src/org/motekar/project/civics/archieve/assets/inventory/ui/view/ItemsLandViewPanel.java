package org.motekar.project.civics.archieve.assets.inventory.ui.view;

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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsLandViewPanel extends JXPanel implements CommonButtonListener {

    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    // Main Data
    private JXComboBox comboUnit = new JXComboBox();
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
    private JXPanel panelParent;
    private JScrollPane mainSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    //
    private EventList<Region> urbanRegions;

    public ItemsLandViewPanel(ArchieveMainframe mainframe, JXPanel panelParent) {
        this.mainframe = mainframe;
        this.panelParent = panelParent;
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
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

    private void construct() {
        contructNumberField();
        loadComboUnit();
        loadComboUrban();
        loadComboSubUrban();

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

        btSavePanelBottom.addListener(this);
        btSavePanelUp.addListener(this);

        disableAll();

        setLayout(new BorderLayout());
        add(createViewPanel(), BorderLayout.CENTER);
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

        builder.addLabel("UPB / Sub UPB", cc.xy(2, 2));
        builder.add(comboUnit, cc.xyw(4, 2, 5));

        builder.addLabel("Kode / Nama Barang", cc.xy(2, 4));
        builder.add(fieldItems, cc.xyw(4, 4, 5));

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

    private Component createViewPanel() {

        FormLayout lm = new FormLayout(
                "200px,right:pref,10px, 100px,5px,fill:default:grow,200px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,"
                + "20px,pref,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        btSavePanelUp.setBorder(new EmptyBorder(5, 0, 5, 5));
        btSavePanelBottom.setBorder(new EmptyBorder(5, 0, 5, 5));

        btSavePanelUp.setVisibleButtonSave(false);
        btSavePanelBottom.setVisibleButtonSave(false);

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 7));
        builder.addSeparator("", cc.xyw(1, 2, 7));

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

        JXTitledPanel titledPanel = new JXTitledPanel("Data Barang Tanah");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private void disableAll() {
        comboUnit.setEnabled(false);

        fieldItems.setEditable(false);
        fieldAddressLocation.setEditable(false);
        comboUrban.setEnabled(false);
        comboSubUrban.setEnabled(false);
        checkUnknown.setEnabled(false);

        fieldAllotment.setEditable(false);
        fieldDescription.setEditable(false);

        fieldRegNumber.setEditable(false);
        fieldWide.setEditable(false);
        fieldPrice.setEditable(false);
        fieldAcquisitionYear.setEnabled(false);
        fieldFundingSource.setEditable(false);
        fieldAcquisitionWay.setEditable(false);
        //
        radioOwnerShipStateName.setAllDisabled();
        fieldOwnerShipStateNumber.setEditable(false);
        fieldOwnerShipDate.setEditable(false);
        fieldOwnerShipIssued.setEditable(false);
        fieldOwner.setEditable(false);
        radioOwnerShipStatus.setAllDisabled();
        radioOwnerShipRelation.setAllDisabled();
        radioOwnerShipOccupancy.setAllDisabled();
        radioOwnerShipOccupying.setAllDisabled();
        //
        fieldLandCertificateNumber.setEditable(false);
        fieldLandCertificateDate.setEditable(false);
        fieldLandTaxNumber.setEditable(false);
        fieldLandTaxDate.setEditable(false);
        fieldBuildPermitNumber.setEditable(false);
        fieldBuildPermitDate.setEditable(false);
        fieldAdvisPlanningNumber.setEditable(false);
        fieldAdvisPlanningDate.setEditable(false);
        //
        radioLandCondition.setAllDisabled();
        radioShape.setAllDisabled();
        radioTopographCondition.setAllDisabled();
        radioCurrentUse.setAllDisabled();
        radioCloserRoad.setAllDisabled();
        radioRoadSurface.setAllDisabled();
        radioHeightAbove.setAllDisabled();
        radioHeightUnder.setAllDisabled();
        radioElevation.setAllDisabled();

        fieldPriceBasedChief.setEditable(false);
        fieldPriceBasedNJOP.setEditable(false);
        fieldPriceBasedExam.setEditable(false);
        fieldPriceDescription.setEditable(false);
        //
        radioLocation.setAllDisabled();
        radioPosition.setAllDisabled();
        radioRoadAccess.setAllDisabled();
        radioRoadWidth.setAllDisabled();
        radioPavement.setAllDisabled();
        radioTraffic.setAllDisabled();
        radioTopographLocation.setAllDisabled();
        radioEnvironment.setAllDisabled();
        radioDensity.setAllDisabled();
        radioSocialLevels.setAllDisabled();
        radioFacilities.setAllDisabled();
        radioDrainage.setAllDisabled();
        radioPublicTransportation.setAllDisabled();
        radioSecurity.setAllDisabled();
        //
        radioSecurityDisturbance.setAllDisabled();
        radioFloodDangers.setAllDisabled();
        radioLocationEffect.setAllDisabled();
        radioEnvironmentalInfluences.setAllDisabled();
        //
        radioAllotmentPoint.setAllDisabled();
        radioUtilizationPoint.setAllDisabled();
        radioLocationPoint.setAllDisabled();
        radioAccessionPoint.setAllDisabled();
        radioNursingPoint.setAllDisabled();
        radioSoilConditionsPoint.setAllDisabled();
        radioMarketInterestPoint.setAllDisabled();
        //
        fieldComparativePrice.setEditable(false);
        fieldComparativeWide.setEditable(false);
        fieldComparativeSource.setEditable(false);
        fieldComparativePrice2.setEditable(false);
        fieldComparativeWide2.setEditable(false);
        fieldComparativeSource2.setEditable(false);
        fieldComparativePrice3.setEditable(false);
        fieldComparativeWide3.setEditable(false);
        fieldComparativeSource3.setEditable(false);
        fieldComparativePrice4.setEditable(false);
        fieldComparativeWide4.setEditable(false);
        fieldComparativeSource4.setEditable(false);
        fieldComparativePrice5.setEditable(false);
        fieldComparativeWide5.setEditable(false);
        fieldComparativeSource5.setEditable(false);
        fieldLandAddress.setEditable(false);
        fieldNJOPYear.setEnabled(false);
        fieldNJOPLandwide.setEditable(false);
        fieldNJOPLandClass.setEditable(false);
        fieldNJOPLandValue.setEditable(false);
    }

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldAcquisitionYear.setYear(year);
        fieldNJOPYear.setYear(year);
    }

    private void clearForm() {
        comboUnit.getEditor().setItem(null);
        fieldItems.setText("");
        fieldRegNumber.setText("");
        fieldWide.setValue(BigDecimal.ZERO);
        fieldAddressLocation.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        clearYear();
        checkUnknown.setSelected(false);
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
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

    public void setFormValues(ItemsLand land) {
        if (land != null) {

            if (land.getUnit() == null) {
                comboUnit.getEditor().setItem(null);
            } else {
                comboUnit.setSelectedItem(land.getUnit());
            }

            fieldItems.setText(land.getItemCode() + " " + land.getItemName());

            fieldRegNumber.setText(land.getRegNumber());
            fieldWide.setValue(land.getWide());
            fieldAddressLocation.setText(land.getAddressLocation());

            if (land.getUrban() == null) {
                comboUrban.getEditor().setItem(null);
            } else {
                comboUrban.setSelectedItem(land.getUrban());
            }

            if (land.getSubUrban() == null) {
                comboSubUrban.getEditor().setItem(null);
            } else {
                comboSubUrban.setSelectedItem(land.getSubUrban());
            }


            fieldPrice.setValue(land.getLandPrice());

            if (land.getAcquisitionYear() == null) {
                clearYear();
                checkUnknown.setSelected(true);
            } else {
                fieldAcquisitionYear.setYear(land.getAcquisitionYear());
                checkUnknown.setSelected(false);
            }

            fieldFundingSource.setText(land.getFundingSource());
            fieldAcquisitionWay.setText(land.getAcquisitionWay());
            fieldDescription.setText(land.getDescription());
            //
            radioOwnerShipStateName.setSelectedData(land.getOwnerShipStateName());
            fieldOwnerShipStateNumber.setText(land.getOwnerShipNumberState());
            fieldOwnerShipDate.setDate(land.getOwnerShipDate());
            fieldOwnerShipIssued.setText(land.getOwnerShipIssuedBy());
            fieldOwner.setText(land.getOwner());
            radioOwnerShipStatus.setSelectedData(land.getOwnerShipStatus());
            radioOwnerShipRelation.setSelectedData(land.getOwnerShipRelation());
            radioOwnerShipOccupancy.setSelectedData(land.getOwnerShipOccupancy());
            radioOwnerShipOccupying.setSelectedData(land.getOwnerShipOccupying());
            //
            fieldLandCertificateNumber.setText(land.getLandCertificateNumber());
            fieldLandCertificateDate.setDate(land.getLandCertificateDate());
            fieldLandTaxNumber.setText(land.getLandTaxNumber());
            fieldLandTaxDate.setDate(land.getLandTaxDate());
            fieldBuildPermitNumber.setText(land.getBuildPermitNumber());
            fieldBuildPermitDate.setDate(land.getBuildPermitDate());
            fieldAdvisPlanningNumber.setText(land.getAdvisPlanningNumber());
            fieldAdvisPlanningDate.setDate(land.getAdvisPlanningDate());
            //
            radioLandCondition.setSelectedData(land.getLandCondition());
            radioShape.setSelectedData(land.getShape());
            radioTopographCondition.setSelectedData(land.getTopographCondition());
            radioCurrentUse.setSelectedData(land.getCurrentUse());
            radioCloserRoad.setSelectedData(land.getCloserRoad());
            radioRoadSurface.setSelectedData(land.getRoadSurface());
            radioHeightAbove.setSelectedData(land.getHeightAbove());
            radioHeightUnder.setSelectedData(land.getHeightUnder());
            radioElevation.setSelectedData(land.getElevation());
            fieldAllotment.setText(land.getAllotment());
            fieldPriceBasedChief.setValue(land.getPriceBasedChief());
            fieldPriceBasedNJOP.setValue(land.getPriceBasedNJOP());
            fieldPriceBasedExam.setValue(land.getPriceBasedExam());
            fieldPriceDescription.setText(land.getPriceDescription());
            //
            radioLocation.setSelectedData(land.getLocation());
            radioPosition.setSelectedData(land.getPosition());
            radioRoadAccess.setSelectedData(land.getRoadAccess());
            radioRoadWidth.setSelectedData(land.getRoadWidth());
            radioPavement.setSelectedData(land.getPavement());
            radioTraffic.setSelectedData(land.getTraffic());
            radioTopographLocation.setSelectedData(land.getTopographLocation());
            radioEnvironment.setSelectedData(land.getEnvironment());
            radioDensity.setSelectedData(land.getDensity());
            radioSocialLevels.setSelectedData(land.getSocialLevels());
            radioFacilities.setSelectedData(land.getFacilities());
            radioDrainage.setSelectedData(land.getDrainage());
            radioPublicTransportation.setSelectedData(land.getPublicTransportation());
            radioSecurity.setSelectedData(land.getSecurity());
            //
            radioSecurityDisturbance.setSelectedData(land.getSecurityDisturbance());
            radioFloodDangers.setSelectedData(land.getFloodDangers());
            radioLocationEffect.setSelectedData(land.getLocationEffect());
            radioEnvironmentalInfluences.setSelectedData(land.getEnvironmentalInfluences());
            //
            radioAllotmentPoint.setSelectedData(land.getAllotmentPoint());
            radioUtilizationPoint.setSelectedData(land.getUtilizationPoint());
            radioLocationPoint.setSelectedData(land.getLocationPoint());
            radioAccessionPoint.setSelectedData(land.getAccessionPoint());
            radioNursingPoint.setSelectedData(land.getNursingPoint());
            radioSoilConditionsPoint.setSelectedData(land.getSoilConditionsPoint());
            radioMarketInterestPoint.setSelectedData(land.getMarketInterestPoint());
            //
            fieldComparativePrice.setValue(land.getComparativePrice());
            fieldComparativeWide.setValue(land.getComparativeWide());
            fieldComparativeSource.setText(land.getComparativeSource());
            fieldComparativePrice2.setValue(land.getComparativePrice2());
            fieldComparativeWide2.setValue(land.getComparativeWide2());
            fieldComparativeSource2.setText(land.getComparativeSource2());
            fieldComparativePrice3.setValue(land.getComparativePrice3());
            fieldComparativeWide3.setValue(land.getComparativeWide3());
            fieldComparativeSource3.setText(land.getComparativeSource3());
            fieldComparativePrice4.setValue(land.getComparativePrice4());
            fieldComparativeWide4.setValue(land.getComparativeWide4());
            fieldComparativeSource4.setText(land.getComparativeSource4());
            fieldComparativePrice5.setValue(land.getComparativePrice5());
            fieldComparativeWide5.setValue(land.getComparativeWide5());
            fieldComparativeSource5.setText(land.getComparativeSource5());
            fieldLandAddress.setText(land.getLandAddress());
            fieldNJOPYear.setYear(land.getNjopYear());
            fieldNJOPLandwide.setValue(land.getNjopLandwide());
            fieldNJOPLandClass.setText(land.getNjopLandClass());
            fieldNJOPLandValue.setValue(land.getNjopLandValue());
            setTaskPaneState();
        } else {
            clearForm();
        }
    }

    public void onInsert() throws SQLException, CommonException {
    }

    public void onEdit() throws SQLException, CommonException {
    }

    public void onDelete() throws SQLException, CommonException {
    }

    public void onSave() throws SQLException, CommonException {
    }

    public void onCancel() throws SQLException, CommonException {
        ((CardLayout) panelParent.getLayout()).first(panelParent);
    }
}
