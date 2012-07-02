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
import javax.swing.JTabbedPane;
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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
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
public class ItemsNetworkViewPanel extends JXPanel implements CommonButtonListener {

    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    //
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    //
    //
    private JXComboBox comboUnit = new JXComboBox();
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
    // Irrigation Main Building
    private JYearChooser fieldIrrigationYearBuild = new JYearChooser();
    private RadioButtonPanel radioIrrigationBuildingType = new RadioButtonPanel(new String[]{"Bangunan Tetap", "Waduk",
                "Bendung Gerak", "Stasiun Pompa"}, 0, 4);
    private JXFormattedTextField fieldIrrigationLength;
    private JXFormattedTextField fieldIrrigationHeight;
    private JXFormattedTextField fieldIrrigationWidth;
    private RadioButtonPanel radioIrrigationBuildingMaterial = new RadioButtonPanel(new String[]{"Beton", "Batu", "Baja", "Kayu"}, 0, 4);
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
    // Protection Building
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
    // Support Building
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
    //
    private JXPanel panelParent;
    private JScrollPane mainSCPane = new JScrollPane();
    private JScrollPane irrSCPane = new JScrollPane();
    private ArrayList<JXTaskPane> taskArrays = new ArrayList<JXTaskPane>();
    private ArrayList<JXTaskPane> taskArrays2 = new ArrayList<JXTaskPane>();

    public ItemsNetworkViewPanel(ArchieveMainframe mainframe, JXPanel panelParent) {
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

            AutoCompleteSupport support2 = AutoCompleteSupport.install(comboIrrigationCondition, conditions);
            support2.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support3 = AutoCompleteSupport.install(comboIrrigationCarrierCondition, conditions);
            support3.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support4 = AutoCompleteSupport.install(comboIrrigationDebitCondition, conditions);
            support4.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support5 = AutoCompleteSupport.install(comboIrrigationPoolCondition, conditions);
            support5.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support6 = AutoCompleteSupport.install(comboIrrigationHWFCondition, conditions);
            support6.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support7 = AutoCompleteSupport.install(comboIrrigationProtectCondition, conditions);
            support7.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support8 = AutoCompleteSupport.install(comboIrrigationTappedCondition, conditions);
            support8.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support9 = AutoCompleteSupport.install(comboIrrigationSupportCondition, conditions);
            support9.setFilterMode(TextMatcherEditor.CONTAINS);

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

    private void construct() {
        contructNumberField();
        loadComboUnit();
        loadComboCondition();

        btSavePanelBottom.addListener(this);
        btSavePanelUp.addListener(this);

        disableAll();

        setLayout(new BorderLayout());
        add(createViewPanel(), BorderLayout.CENTER);
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
        builder.add(fieldItems, cc.xyw(4, 4, 5));

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
        builder.add(fieldLandCode, cc.xyw(4, 28, 5));

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
        builder.add(radioIrrigationBuildingType, cc.xyw(4, 4, 3));

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
        builder.add(radioIrrigationBuildingMaterial, cc.xyw(4, 14, 3));

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

    private Component createViewPanel() {

        FormLayout lm = new FormLayout(
                "150px,right:pref,10px, 100px,5px,fill:default:grow,150px",
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
        container.setAlpha(0.95f);

        builder.add(container, cc.xyw(2, 6, 5));

        builder.addSeparator("", cc.xyw(1, 10, 7));
        builder.add(btSavePanelBottom, cc.xyw(1, 11, 7));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Data Barang Jalan, Irigasi, Jaringan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private void disableAll() {
        comboUnit.setEnabled(false);
        comboUrban.setEnabled(false);
        comboSubUrban.setEnabled(false);
        fieldItems.setEditable(false);
        fieldWidth.setEditable(false);
        fieldWide.setEditable(false);
        fieldAddressLocation.setEditable(false);
        fieldRegNumber.setEditable(false);
        fieldDocumentNumber.setEditable(false);
        fieldDocumentDate.setEditable(false);
        fieldConstructionType.setEditable(false);
        fieldLength.setEditable(false);
        fieldLandStatus.setEditable(false);
        fieldLandCode.setEditable(false);
        comboCondition.setEnabled(false);
        fieldPrice.setEditable(false);
        fieldLength.setEditable(false);
        fieldFundingSource.setEditable(false);
        fieldAcquisitionWay.setEditable(false);
        fieldDescription.setEditable(false);
        //
        fieldStreetYearBuild.setEnabled(false);
        fieldBridgeYearBuild.setEnabled(false);
        fieldIrrigationYearBuild.setEnabled(false);
        radioStreetLocation.setAllDisabled();
        radioStreetFlatness.setAllDisabled();
        fieldStreetStartKM.setEditable(false);
        fieldStreetEndKM.setEditable(false);
        radioStreetWidth.setAllDisabled();
        radioStreetSurface.setAllDisabled();
        radioStreetSide.setAllDisabled();
        radioStreetTrotoire.setAllDisabled();
        radioStreetChannel.setAllDisabled();
        radioStreetSafetyZone.setAllDisabled();
        //
        radioBridgeStandarType.setAllDisabled();
        fieldBridgeLength.setEditable(false);
        fieldBridgeWidth.setEditable(false);
        radioBridgePurpose.setAllDisabled();
        radioBridgeMainMaterial.setAllDisabled();
        radioBridgeTopShape.setAllDisabled();
        radioBridgeOtherShape.setAllDisabled();
        radioBridgeHeadShape.setAllDisabled();
        radioBridgeHeadMaterial.setAllDisabled();
        radioBridgePillar.setAllDisabled();
        radioBridgePillarMaterial.setAllDisabled();
        //
        radioIrrigationBuildingType.setAllDisabled();
        fieldIrrigationLength.setEditable(false);
        fieldIrrigationHeight.setEditable(false);
        fieldIrrigationWidth.setEditable(false);
        radioIrrigationBuildingMaterial.setAllDisabled();
        comboIrrigationCondition.setEnabled(false);
        //
        radioIrrigationCarrierType.setAllDisabled();
        fieldIrrigationCarrierLength.setEditable(false);
        fieldIrrigationCarrierHeight.setEditable(false);
        fieldIrrigationCarrierWidth.setEditable(false);
        radioIrrigationCarrierMaterial.setAllDisabled();
        comboIrrigationCarrierCondition.setEnabled(false);
        //
        fieldIrrigationDebitThresholdWidth.setEditable(false);
        fieldIrrigationDebitCDG.setEditable(false);
        fieldIrrigationDebitCipolleti.setEditable(false);
        fieldIrrigationDebitLength.setEditable(false);
        fieldIrrigationDebitHeight.setEditable(false);
        fieldIrrigationDebitWidth.setEditable(false);
        radioIrrigationDebitMaterial.setAllDisabled();
        comboIrrigationDebitCondition.setEnabled(false);
        //
        fieldIrrigationPoolUSBR3.setEditable(false);
        fieldIrrigationPoolBlock.setEditable(false);
        fieldIrrigationPoolUSBR4.setEditable(false);
        fieldIrrigationPoolVlogtor.setEditable(false);
        fieldIrrigationPoolLength.setEditable(false);
        fieldIrrigationPoolHeight.setEditable(false);
        fieldIrrigationPoolWidth.setEditable(false);
        radioIrrigationPoolMaterial.setAllDisabled();
        comboIrrigationPoolCondition.setEnabled(false);
        //
        fieldIrrigationHWFLotBlock.setEditable(false);
        fieldIrrigationHWFTrapesium.setEditable(false);
        fieldIrrigationHWFSlide.setEditable(false);
        fieldIrrigationHWFTreeTop.setEditable(false);
        fieldIrrigationHWFLength.setEditable(false);
        fieldIrrigationHWFHeight.setEditable(false);
        fieldIrrigationHWFWidth.setEditable(false);
        radioIrrigationHWFMaterial.setAllDisabled();
        comboIrrigationHWFCondition.setEnabled(false);
        //
        fieldIrrigationProtectTransfer.setEditable(false);
        fieldIrrigationProtectDisposal.setEditable(false);
        fieldIrrigationProtectDrain.setEditable(false);
        fieldIrrigationProtectLength.setEditable(false);
        fieldIrrigationProtectHeight.setEditable(false);
        fieldIrrigationProtectWidth.setEditable(false);
        radioIrrigationProtectMaterial.setAllDisabled();
        comboIrrigationProtectCondition.setEnabled(false);
        //
        fieldIrrigationTappedFor.setEditable(false);
        fieldIrrigationTappedSecond.setEditable(false);
        fieldIrrigationTappedRegulator.setEditable(false);
        fieldIrrigationTappedThird.setEditable(false);
        fieldIrrigationTappedLength.setEditable(false);
        fieldIrrigationTappedHeight.setEditable(false);
        fieldIrrigationTappedWidth.setEditable(false);
        radioIrrigationTappedMaterial.setAllDisabled();
        comboIrrigationTappedCondition.setEnabled(false);
        //
        fieldIrrigationSupportLevee.setEditable(false);
        fieldIrrigationSupportWash.setEditable(false);
        fieldIrrigationSupportBridge.setEditable(false);
        fieldIrrigationSupportKrib.setEditable(false);
        fieldIrrigationSupportLength.setEditable(false);
        fieldIrrigationSupportHeight.setEditable(false);
        fieldIrrigationSupportWidth.setEditable(false);
        radioIrrigationSupportMaterial.setAllDisabled();
        comboIrrigationSupportCondition.setEnabled(false);
        //
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
        comboUnit.getEditor().setItem(null);
        fieldItems.setText("");
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
        radioIrrigationBuildingType.setAllDeselected();
        fieldIrrigationLength.setValue(Integer.valueOf(0));
        fieldIrrigationHeight.setValue(Integer.valueOf(0));
        fieldIrrigationWidth.setValue(Integer.valueOf(0));
        radioIrrigationBuildingMaterial.setAllDeselected();
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

    public void setFormValues(ItemsNetwork network) {
        if (network != null) {

            if (network.getUnit() == null) {
                comboUnit.getEditor().setItem(null);
            } else {
                comboUnit.setSelectedItem(network.getUnit());
            }

            fieldItems.setText(network.getItemCode() + " " + network.getItemName());

            fieldRegNumber.setText(network.getRegNumber());
            fieldDocumentNumber.setText(network.getDocumentNumber());
            fieldDocumentDate.setDate(network.getDocumentDate());
            fieldConstructionType.setText(network.getConstructionType());

            fieldLength.setValue(network.getLength());
            fieldWidth.setValue(network.getWidth());
            fieldWide.setValue(network.getWide());
            fieldPrice.setValue(network.getPrice());

            fieldLandStatus.setText(network.getLandStatus());
            fieldLandCode.setText(network.getLandCode());

            if (network.getCondition() == null) {
                comboCondition.getEditor().setItem(null);
            } else {
                comboCondition.setSelectedItem(network.getCondition());
            }

            fieldAddressLocation.setText(network.getAddressLocation());
            if (network.getUrban() == null) {
                comboUrban.getEditor().setItem(null);
            } else {
                comboUrban.setSelectedItem(network.getUrban());
            }

            if (network.getSubUrban() == null) {
                comboSubUrban.getEditor().setItem(null);
            } else {
                comboSubUrban.setSelectedItem(network.getSubUrban());
            }

            fieldLength.setValue(network.getLength());
            fieldFundingSource.setText(network.getFundingSource());
            fieldAcquisitionWay.setText(network.getAcquisitionWay());
            fieldDescription.setText(network.getDescription());

            //
            fieldStreetYearBuild.setYear(network.getStreetYearBuild());
            radioStreetLocation.setSelectedData(network.getStreetLocation());
            radioStreetFlatness.setSelectedData(network.getStreetFlatness());
            fieldStreetStartKM.setValue(network.getStreetStartKM());
            fieldStreetEndKM.setValue(network.getStreetEndKM());
            radioStreetWidth.setSelectedData(network.getStreetWidth());
            radioStreetSurface.setSelectedData(network.getStreetSurface());
            radioStreetSide.setSelectedData(network.getStreetSide());
            radioStreetTrotoire.setSelectedData(network.getStreetTrotoire());
            radioStreetChannel.setSelectedData(network.getStreetChannel());

            if (network.isStreetSafetyZone()) {
                radioStreetSafetyZone.setSelectedData("Ada");
            } else {
                radioStreetSafetyZone.setSelectedData("Tidak Ada");
            }

            //
            radioBridgeStandarType.setSelectedData(network.getBridgeStandarType());
            fieldBridgeYearBuild.setYear(network.getBridgeYearBuild());
            fieldBridgeLength.setValue(network.getBridgeLength());
            fieldBridgeWidth.setValue(network.getBridgeWidth());
            radioBridgePurpose.setSelectedData(network.getBridgePurpose());
            radioBridgeMainMaterial.setSelectedData(network.getBridgeMainMaterial());
            radioBridgeTopShape.setSelectedData(network.getBridgeTopShape());
            radioBridgeOtherShape.setSelectedData(network.getBridgeOtherShape());
            radioBridgeHeadShape.setSelectedData(network.getBridgeHeadShape());
            radioBridgeHeadMaterial.setSelectedData(network.getBridgeHeadMaterial());
            radioBridgePillar.setSelectedData(network.getBridgePillar());
            radioBridgePillarMaterial.setSelectedData(network.getBridgePillarMaterial());
            //
            fieldIrrigationYearBuild.setYear(network.getIrrigationYearBuild());
            radioIrrigationBuildingType.setSelectedData(network.getIrrigationBuildingType());
            fieldIrrigationLength.setValue(network.getIrrigationLength());
            fieldIrrigationHeight.setValue(network.getIrrigationHeight());
            fieldIrrigationWidth.setValue(network.getIrrigationWidth());
            radioIrrigationBuildingMaterial.setSelectedData(network.getIrrigationBuildingMaterial());

            if (network.getIrrigationCondition() != null) {
                try {
                    network.setIrrigationCondition(mLogic.getCondition(mainframe.getSession(), network.getIrrigationCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationCondition() == null) {
                comboIrrigationCondition.getEditor().setItem(null);
            } else {
                comboIrrigationCondition.setSelectedItem(network.getIrrigationCondition());
            }

            //
            radioIrrigationCarrierType.setSelectedData(network.getIrrigationCarrierType());
            fieldIrrigationCarrierLength.setValue(network.getIrrigationCarrierLength());
            fieldIrrigationCarrierHeight.setValue(network.getIrrigationCarrierHeight());
            fieldIrrigationCarrierWidth.setValue(network.getIrrigationCarrierWidth());
            radioIrrigationCarrierMaterial.setSelectedData(network.getIrrigationCarrierMaterial());

            if (network.getIrrigationCarrierCondition() != null) {
                try {
                    network.setIrrigationCarrierCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationCarrierCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationCarrierCondition() == null) {
                comboIrrigationCarrierCondition.getEditor().setItem(null);
            } else {
                comboIrrigationCarrierCondition.setSelectedItem(network.getIrrigationCarrierCondition());
            }

            //
            fieldIrrigationDebitThresholdWidth.setValue(network.getIrrigationDebitThresholdWidth());
            fieldIrrigationDebitCDG.setValue(network.getIrrigationDebitCDG());
            fieldIrrigationDebitCipolleti.setValue(network.getIrrigationDebitCipolleti());
            fieldIrrigationDebitLength.setValue(network.getIrrigationDebitLength());
            fieldIrrigationDebitHeight.setValue(network.getIrrigationDebitHeight());
            fieldIrrigationDebitWidth.setValue(network.getIrrigationDebitWidth());
            radioIrrigationDebitMaterial.setSelectedData(network.getIrrigationDebitMaterial());

            if (network.getIrrigationDebitCondition() != null) {
                try {
                    network.setIrrigationDebitCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationDebitCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationDebitCondition() == null) {
                comboIrrigationDebitCondition.getEditor().setItem(null);
            } else {
                comboIrrigationDebitCondition.setSelectedItem(network.getIrrigationDebitCondition());
            }

            //

            fieldIrrigationPoolUSBR3.setValue(network.getIrrigationPoolUSBR3());
            fieldIrrigationPoolBlock.setValue(network.getIrrigationPoolBlock());
            fieldIrrigationPoolUSBR4.setValue(network.getIrrigationPoolUSBR4());
            fieldIrrigationPoolVlogtor.setValue(network.getIrrigationPoolVlogtor());
            fieldIrrigationPoolLength.setValue(network.getIrrigationPoolLength());
            fieldIrrigationPoolHeight.setValue(network.getIrrigationPoolHeight());
            fieldIrrigationPoolWidth.setValue(network.getIrrigationPoolWidth());
            radioIrrigationPoolMaterial.setSelectedData(network.getIrrigationPoolMaterial());

            if (network.getIrrigationPoolCondition() != null) {
                try {
                    network.setIrrigationPoolCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationPoolCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationPoolCondition() == null) {
                comboIrrigationPoolCondition.getEditor().setItem(null);
            } else {
                comboIrrigationPoolCondition.setSelectedItem(network.getIrrigationPoolCondition());
            }

            //

            fieldIrrigationHWFLotBlock.setValue(network.getIrrigationHWFLotBlock());
            fieldIrrigationHWFTrapesium.setValue(network.getIrrigationHWFTrapesium());
            fieldIrrigationHWFSlide.setValue(network.getIrrigationHWFSlide());
            fieldIrrigationHWFTreeTop.setValue(network.getIrrigationHWFTreeTop());
            fieldIrrigationHWFLength.setValue(network.getIrrigationHWFLength());
            fieldIrrigationHWFHeight.setValue(network.getIrrigationHWFHeight());
            fieldIrrigationHWFWidth.setValue(network.getIrrigationHWFWidth());
            radioIrrigationHWFMaterial.setSelectedData(network.getIrrigationHWFMaterial());

            if (network.getIrrigationHWFCondition() != null) {
                try {
                    network.setIrrigationHWFCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationHWFCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationHWFCondition() == null) {
                comboIrrigationHWFCondition.getEditor().setItem(null);
            } else {
                comboIrrigationHWFCondition.setSelectedItem(network.getIrrigationHWFCondition());
            }

            //

            fieldIrrigationProtectTransfer.setValue(network.getIrrigationProtectTransfer());
            fieldIrrigationProtectDisposal.setValue(network.getIrrigationProtectDisposal());
            fieldIrrigationProtectDrain.setValue(network.getIrrigationProtectDrain());
            fieldIrrigationProtectLength.setValue(network.getIrrigationProtectLength());
            fieldIrrigationProtectHeight.setValue(network.getIrrigationProtectHeight());
            fieldIrrigationProtectWidth.setValue(network.getIrrigationProtectWidth());
            radioIrrigationProtectMaterial.setSelectedData(network.getIrrigationProtectMaterial());

            if (network.getIrrigationProtectCondition() != null) {
                try {
                    network.setIrrigationProtectCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationProtectCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationProtectCondition() == null) {
                comboIrrigationProtectCondition.getEditor().setItem(null);
            } else {
                comboIrrigationProtectCondition.setSelectedItem(network.getIrrigationProtectCondition());
            }

            //

            fieldIrrigationTappedFor.setValue(network.getIrrigationTappedFor());
            fieldIrrigationTappedSecond.setValue(network.getIrrigationTappedSecond());
            fieldIrrigationTappedRegulator.setValue(network.getIrrigationTappedRegulator());
            fieldIrrigationTappedThird.setValue(network.getIrrigationTappedThird());
            fieldIrrigationTappedLength.setValue(network.getIrrigationTappedLength());
            fieldIrrigationTappedHeight.setValue(network.getIrrigationTappedHeight());
            fieldIrrigationTappedWidth.setValue(network.getIrrigationTappedWidth());
            radioIrrigationTappedMaterial.setSelectedData(network.getIrrigationTappedMaterial());

            if (network.getIrrigationTappedCondition() != null) {
                try {
                    network.setIrrigationTappedCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationTappedCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationTappedCondition() == null) {
                comboIrrigationTappedCondition.getEditor().setItem(null);
            } else {
                comboIrrigationTappedCondition.setSelectedItem(network.getIrrigationTappedCondition());
            }

            //

            fieldIrrigationSupportLevee.setValue(network.getIrrigationSupportLevee());
            fieldIrrigationSupportWash.setValue(network.getIrrigationSupportWash());
            fieldIrrigationSupportBridge.setValue(network.getIrrigationSupportBridge());
            fieldIrrigationSupportKrib.setValue(network.getIrrigationSupportKrib());
            fieldIrrigationSupportLength.setValue(network.getIrrigationSupportLength());
            fieldIrrigationSupportHeight.setValue(network.getIrrigationSupportHeight());
            fieldIrrigationSupportWidth.setValue(network.getIrrigationSupportWidth());
            radioIrrigationSupportMaterial.setSelectedData(network.getIrrigationSupportMaterial());

            if (network.getIrrigationSupportCondition() != null) {
                try {
                    network.setIrrigationSupportCondition(mLogic.getCondition(mainframe.getSession(),
                            network.getIrrigationSupportCondition()));
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (network.getIrrigationSupportCondition() == null) {
                comboIrrigationSupportCondition.getEditor().setItem(null);
            } else {
                comboIrrigationSupportCondition.setSelectedItem(network.getIrrigationSupportCondition());
            }

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
