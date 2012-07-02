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
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsBuildingViewPanel extends JXPanel implements CommonButtonListener {

    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    //
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    // Main Data
    private JXComboBox comboUnit = new JXComboBox();
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXFormattedTextField fieldWide;
    private RadioButtonPanel radioRaised = new RadioButtonPanel(new String[]{"Ya", "Tidak"}, 0, 2);
    private JXComboBox comboCondition = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JXTextField fieldAddress = new JXTextField();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXDatePicker fieldDocumentDate = new JXDatePicker();
    private JXFormattedTextField fieldLandWide;
    private JXTextField fieldLandCode = new JXTextField();
    private JXTextField fieldLandState = new JXTextField();
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
    private JXTextField fieldLandTaxNumber = new JXTextField();
    private JXDatePicker fieldLandTaxDate = new JXDatePicker();
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
    private RadioButtonPanel radioFrameworks = new RadioButtonPanel(new String[]{"Ya", "Tidak"}, 0, 2);
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
    private JXPanel panelParent;
    private JScrollPane mainSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    //
    private EventList<Region> urbanRegions;

    public ItemsBuildingViewPanel(ArchieveMainframe mainframe, JXPanel panelParent) {
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

            AutoCompleteSupport support2 = AutoCompleteSupport.install(comboBuildingCondition, conditions);
            support2.setFilterMode(TextMatcherEditor.CONTAINS);

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

        fieldLandWide = new JXFormattedTextField();
        fieldLandWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldLandWide.setHorizontalAlignment(JLabel.LEFT);

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);
    }

    private void construct() {
        contructNumberField();
        loadComboUnit();
        loadComboCondition();
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

    private Component createMainDataPanel() {

        FormLayout lm = new FormLayout(
                "50px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,50px",
                "30px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
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
        builder.add(fieldItems, cc.xyw(4, 4, 5));

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

        builder.addLabel("Luas Tanah (M2)", cc.xy(2, 28));
        builder.add(fieldLandWide, cc.xyw(4, 28, 3));

        builder.addLabel("Status Tanah", cc.xy(2, 30));
        builder.add(fieldLandState, cc.xyw(4, 30, 5));

        builder.addLabel("Nomor Kode Tanah", cc.xy(2, 32));
        builder.add(fieldLandCode, cc.xyw(4, 32, 5));

        builder.addLabel("Asal-Usul", cc.xy(2, 34));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 34, 5));

        builder.addLabel("Harga Perolehan (Rp.)", cc.xy(2, 36));
        builder.add(fieldPrice, cc.xyw(4, 36, 5));

        builder.addLabel("Sumber Dana", cc.xy(2, 38));
        builder.add(fieldFundingSource, cc.xyw(4, 38, 5));

        builder.addLabel("Keterangan", cc.xy(2, 40));
        builder.add(scPane, cc.xywh(4, 40, 5, 3));

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

        JXTaskPaneContainer container = createTaskPaneContainer();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 7));
        builder.addSeparator("", cc.xyw(1, 2, 7));

        builder.add(container, cc.xyw(2, 6, 5));

        builder.addSeparator("", cc.xyw(1, 10, 7));
        builder.add(btSavePanelBottom, cc.xyw(1, 11, 7));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        container.setAlpha(0.95f);

        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Data Barang Bangunan dan Gedung");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private void disableAll() {
        comboUnit.setEnabled(false);
        fieldItems.setEditable(false);
        comboUrban.setEnabled(false);
        comboSubUrban.setEnabled(false);
        fieldDocumentNumber.setEditable(false);
        fieldDocumentDate.setEditable(false);
        fieldRegNumber.setEditable(false);
        fieldWide.setEditable(false);
        radioRaised.setAllDisabled();
        comboCondition.setEnabled(false);
        fieldPrice.setEditable(false);
        fieldAcquisitionYear.setEnabled(false);
        fieldLandWide.setEditable(false);
        fieldAddress.setEditable(false);
        fieldLandCode.setEditable(false);
        fieldLandState.setEditable(false);
        fieldFundingSource.setEditable(false);
        fieldAcquisitionWay.setEditable(false);
        fieldDescription.setEditable(false);
        //
        fieldEastLongitudeCoord.setEditable(false);
        fieldSouthLatitudeCoord.setEditable(false);
        //
        radioOwnerShipStateName.setAllDisabled();
        radioOwnerShipRelation.setAllDisabled();
        radioOwnerShipOccupancy.setAllDisabled();
        radioOwnerShipOccupaying.setAllDisabled();
        //
        fieldLandTaxNumber.setEditable(false);
        fieldLandTaxDate.setEditable(false);
        fieldBuildPermitNumber.setEditable(false);
        fieldBuildPermitDate.setEditable(false);
        fieldAdvisPlanningNumber.setEditable(false);
        fieldAdvisPlanningDate.setEditable(false);
        //
        radioType.setAllDisabled();
        radioShape.setAllDisabled();
        radioUtilization.setAllDisabled();
        radioAges.setAllDisabled();
        radioLevels.setAllDisabled();
        radioFrameworks.setAllDisabled();
        radioFoundation.setAllDisabled();
        radioFloorType.setAllDisabled();
        radioWallType.setAllDisabled();
        radioCeillingType.setAllDisabled();
        radioRoofType.setAllDisabled();
        radioFrameType.setAllDisabled();
        radioDoorType.setAllDisabled();
        radioWindowType.setAllDisabled();
        radioRoomType.setAllDisabled();
        radioMaterialQuality.setAllDisabled();
        radioFacilities.setAllDisabled();
        comboBuildingCondition.setEditable(false);
        radioNursing.setAllDisabled();
        radioBuildingClass.setAllDisabled();
        //
        radioAllotmentPoint.setAllDisabled();
        radioUtilizationPoint.setAllDisabled();
        radioLocationPoint.setAllDisabled();
        radioAccessionPoint.setAllDisabled();
        radioQualityPoint.setAllDisabled();
        radioConditionPoint.setAllDisabled();
        radioLayoutPoint.setAllDisabled();
        radioNursingPoint.setAllDisabled();
        radioMarketInterestPoint.setAllDisabled();
    }

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldAcquisitionYear.setYear(year);
    }

    private void clearForm() {
        comboUnit.getEditor().setItem(null);
        fieldItems.setText("");
        fieldRegNumber.setText("");
        fieldWide.setValue(BigDecimal.ZERO);
        radioRaised.setAllDeselected();
        comboCondition.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        clearYear();
        fieldLandWide.setValue(BigDecimal.ZERO);
        fieldAddress.setText("");
        comboUrban.getEditor().setItem(null);
        comboSubUrban.getEditor().setItem(null);
        fieldDocumentDate.setDate(null);
        fieldDocumentNumber.setText("");
        fieldLandCode.setText("");
        fieldLandState.setText("");
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
        fieldLandTaxNumber.setText("");
        fieldLandTaxDate.setDate(null);
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

    public void setFormValues(ItemsBuilding building) {
        if (building != null) {

            if (building.getUnit() == null) {
                comboUnit.getEditor().setItem(null);
            } else {
                comboUnit.setSelectedItem(building.getUnit());
            }

            fieldItems.setText(building.getItemCode() + " " + building.getItemName());

            fieldRegNumber.setText(building.getRegNumber());

            fieldWide.setValue(building.getWide());
            radioRaised.setSelectedData(building.getRaised());

            fieldPrice.setValue(building.getPrice());
            fieldAcquisitionYear.setYear(building.getAcquisitionYear());
            fieldAddress.setText(building.getAddress());

            if (building.getUrban() == null) {
                comboUrban.getEditor().setItem(null);
            } else {
                comboUrban.setSelectedItem(building.getUrban());
            }

            if (building.getSubUrban() == null) {
                comboSubUrban.getEditor().setItem(null);
            } else {
                comboSubUrban.setSelectedItem(building.getSubUrban());
            }

            fieldDocumentDate.setDate(building.getDocumentDate());
            fieldDocumentNumber.setText(building.getDocumentNumber());

            fieldLandWide.setValue(building.getLandWide());
            fieldLandCode.setText(building.getLandCode());
            fieldLandState.setText(building.getLandState());

            if (building.getCondition() == null) {
                comboCondition.getEditor().setItem(null);
            } else {
                comboCondition.setSelectedItem(building.getCondition());
            }

            fieldFundingSource.setText(building.getFundingSource());
            fieldAcquisitionWay.setText(building.getAcquitionWay());
            fieldDescription.setText(building.getDescription());
            //
            fieldEastLongitudeCoord.setText(building.getEastLongitudeCoord());
            fieldSouthLatitudeCoord.setText(building.getSouthLatitudeCoord());
            //
            radioOwnerShipStateName.setSelectedData(building.getOwnerShipStateName());
            radioOwnerShipRelation.setSelectedData(building.getOwnerShipRelation());
            radioOwnerShipOccupancy.setSelectedData(building.getOwnerShipOccupancy());
            radioOwnerShipOccupaying.setSelectedData(building.getOwnerShipOccupaying());
            //
            fieldLandTaxNumber.setText(building.getLandTaxNumber());
            fieldLandTaxDate.setDate(building.getLandTaxDate());
            fieldBuildPermitNumber.setText(building.getBuildPermitNumber());
            fieldBuildPermitDate.setDate(building.getBuildPermitDate());
            fieldAdvisPlanningNumber.setText(building.getAdvisPlanningNumber());
            fieldAdvisPlanningDate.setDate(building.getAdvisPlanningDate());
            //
            radioType.setSelectedData(building.getType());
            radioShape.setSelectedData(building.getShape());
            radioUtilization.setSelectedData(building.getUtilization());
            radioAges.setSelectedData(building.getAges());
            radioLevels.setSelectedData(building.getLevels());
            radioFrameworks.setSelectedData(building.getFrameworks());
            radioFoundation.setSelectedData(building.getFoundation());
            radioFloorType.setSelectedData(building.getFloorType());
            radioWallType.setSelectedData(building.getWallType());
            radioCeillingType.setSelectedData(building.getCeillingType());
            radioRoofType.setSelectedData(building.getRoofType());
            radioFrameType.setSelectedData(building.getFrameType());
            radioDoorType.setSelectedData(building.getDoorType());
            radioWindowType.setSelectedData(building.getWindowType());
            radioRoomType.setSelectedData(building.getRoomType());
            radioMaterialQuality.setSelectedData(building.getMaterialQuality());
            radioFacilities.setSelectedData(building.getFacilities());

            if (building.getBuildingCondition() != null) {
                try {
                    building.setBuildingCondition(mLogic.getCondition(mainframe.getSession(), building.getCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (building.getBuildingCondition() == null) {
                comboBuildingCondition.getEditor().setItem(null);
            } else {
                comboBuildingCondition.setSelectedItem(building.getBuildingCondition());
            }

            radioNursing.setSelectedData(building.getNursing());
            radioBuildingClass.setSelectedData(building.getBuildingClass());
            //
            radioAllotmentPoint.setSelectedData(building.getAllotmentPoint());
            radioUtilizationPoint.setSelectedData(building.getUtilizationPoint());
            radioLocationPoint.setSelectedData(building.getLocationPoint());
            radioAccessionPoint.setSelectedData(building.getAccesionPoint());
            radioQualityPoint.setSelectedData(building.getQualityPoint());
            radioConditionPoint.setSelectedData(building.getConditionPoint());
            radioLayoutPoint.setSelectedData(building.getLayoutPoint());
            radioNursingPoint.setSelectedData(building.getNursingPoint());
            radioMarketInterestPoint.setSelectedData(building.getMarketInterestPoint());
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
