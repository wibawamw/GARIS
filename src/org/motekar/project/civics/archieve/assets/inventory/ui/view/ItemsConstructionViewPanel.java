package org.motekar.project.civics.archieve.assets.inventory.ui.view;

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
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
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
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
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
public class ItemsConstructionViewPanel extends JXPanel implements CommonButtonListener {

    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    //
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    //
    private JXComboBox comboUnit = new JXComboBox();
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXDatePicker fieldDocumentDate = new JXDatePicker();
    private RadioButtonPanel radioBuildCategory = new RadioButtonPanel(new String[]{"Permanen", "Semi Permanen", "Darurat"}, 0, 5);
    private RadioButtonPanel radioRaised = new RadioButtonPanel(new String[]{"Ya", "Tidak"}, 0, 5);
    private RadioButtonPanel radioFrameworks = new RadioButtonPanel(new String[]{"Ya", "Tidak"}, 0, 5);
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
    private JXPanel panelParent;
    //
    private EventList<Region> urbanRegions;

    public ItemsConstructionViewPanel(ArchieveMainframe mainframe, JXPanel panelParent) {
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

        btSavePanelBottom.addListener(this);
        btSavePanelUp.addListener(this);

        setAllDisable();

        setLayout(new BorderLayout());
        add(createViewPanel(), BorderLayout.CENTER);
    }

    private Component createViewPanel() {

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

        btSavePanelUp.setBorder(new EmptyBorder(5, 0, 5, 5));
        btSavePanelBottom.setBorder(new EmptyBorder(5, 0, 5, 5));

        btSavePanelUp.setVisibleButtonSave(false);
        btSavePanelBottom.setVisibleButtonSave(false);

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 9));
        builder.addSeparator("", cc.xyw(1, 2, 9));

        builder.addLabel("UPB / Sub UPB", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 5));

        builder.addLabel("Kode / Nama Barang", cc.xy(2, 8));
        builder.add(fieldItems, cc.xyw(4, 8, 5));

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
        builder.add(fieldLandCode, cc.xyw(4, 38, 5));

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

        JXTitledPanel titledPanel = new JXTitledPanel("Data Barang Konstruksi dalam Pengerjaan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private void setAllDisable() {
        comboUnit.setEnabled(false);
        fieldItems.setEditable(false);
        radioFrameworks.setAllDisabled();
        comboUrban.setEnabled(false);
        comboSubUrban.setEnabled(false);
        fieldRegNumber.setEditable(false);
        fieldDocumentNumber.setEditable(false);
        fieldDocumentDate.setEditable(false);
        radioBuildCategory.setAllDisabled();
        radioRaised.setAllDisabled();
        radioWorkType.setAllDisabled();
        fieldBuildingWide.setEditable(false);
        fieldPrice.setEditable(false);
        fieldAddressLocation.setEditable(false);
        fieldStartDate.setEditable(false);
        fieldLandStatus.setEditable(false);
        fieldLandCode.setEditable(false);
        fieldFundingSource.setEditable(false);
        fieldDescription.setEditable(false);
    }

    private void clearForm() {
        comboUnit.getEditor().setItem(null);
        fieldItems.setText("");
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

    public void setFormValues(ItemsConstruction construction) {
        if (construction != null) {

            if (construction.getUnit() == null) {
                comboUnit.getEditor().setItem(null);
            } else {
                comboUnit.setSelectedItem(construction.getUnit());
            }


            fieldItems.setText(construction.getItemCode() + " " + construction.getItemName());

            fieldRegNumber.setText(construction.getRegNumber());

            fieldDocumentNumber.setText(construction.getDocumentNumber());
            fieldDocumentDate.setDate(construction.getDocumentDate());
            radioBuildCategory.setSelectedData(construction.getBuildingCategory());

            radioRaised.setSelectedData(construction.getRaised());

            radioWorkType.setSelectedData(construction.getWorkType());
            radioFrameworks.setSelectedData(construction.getFrameWork());

            fieldBuildingWide.setValue(construction.getBuildingWide());
            fieldPrice.setValue(construction.getPrice());
            fieldAddressLocation.setText(construction.getAddressLocation());

            if (construction.getUrban() == null) {
                comboUrban.getEditor().setItem(null);
            } else {
                comboUrban.setSelectedItem(construction.getUrban());
            }

            if (construction.getSubUrban() == null) {
                comboSubUrban.getEditor().setItem(null);
            } else {
                comboSubUrban.setSelectedItem(construction.getSubUrban());
            }

            fieldStartDate.setDate(construction.getStartDate());
            fieldLandStatus.setText(construction.getLandStatus());
            fieldLandCode.setText(construction.getLandCode());

            fieldFundingSource.setText(construction.getFundingSource());
            fieldDescription.setText(construction.getDescription());

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
