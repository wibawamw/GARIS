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
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsMachineViewPanel extends JXPanel implements CommonButtonListener {
    
    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    //
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    //
    private JXComboBox comboUnit = new JXComboBox();
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
    private JYearChooser fieldYearBuilt = new JYearChooser();
    private JXTextField fieldLocation = new JXTextField();
    private JXTextField fieldPresentLocation = new JXTextField();
    private JXTextField fieldMaterial = new JXTextField();
    private JXComboBox comboCondition = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JXTextField fieldFundingSource = new JXTextField();
    private JXTextField fieldAcquisitionWay = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    private JXPanel panelParent;

    public ItemsMachineViewPanel(ArchieveMainframe mainframe, JXPanel panelParent) {
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
    
    private Component createViewPanel() {

        FormLayout lm = new FormLayout(
                "300px,right:pref,10px, 100px,5px,fill:default:grow,300px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
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

        builder.add(btSavePanelUp, cc.xyw(1, 1, 7));
        builder.addSeparator("", cc.xyw(1, 2, 7));
        

        builder.addLabel("UPB / Sub UPB", cc.xy(2, 6));
        builder.add(comboUnit, cc.xyw(4, 6, 3));

        builder.addLabel("Kode / Nama Barang", cc.xy(2, 8));
        builder.add(fieldItems, cc.xyw(4, 8, 3));

        builder.addLabel("No. Register", cc.xy(2, 10));
        builder.add(fieldRegNumber, cc.xyw(4, 10, 3));
        
        builder.addLabel("Merk / Tipe", cc.xy(2, 12));
        builder.add(fieldMachineType, cc.xyw(4, 12, 3));
        
        builder.addLabel("Ukuran (CC)", cc.xy(2, 14));
        builder.add(fieldVolume, cc.xyw(4, 14, 1));
        
        builder.addLabel("Bahan", cc.xy(2, 16));
        builder.add(fieldMaterial, cc.xyw(4, 16, 3));
        
        builder.addLabel("Tahun Pembuatan", cc.xy(2, 18));
        builder.add(fieldYearBuilt, cc.xyw(4, 18, 1));
        
        builder.addLabel("Tahun Beli", cc.xy(2, 20));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 20, 1));

        builder.addLabel("No. Pabrik", cc.xy(2, 22));
        builder.add(fieldFactoryNumber, cc.xyw(4, 22, 3));

        builder.addLabel("No. Rangka", cc.xy(2, 24));
        builder.add(fieldFabricNumber, cc.xyw(4, 24, 3));

        builder.addLabel("No. Mesin", cc.xy(2, 26));
        builder.add(fieldMachineNumber, cc.xyw(4, 26, 3));

        builder.addLabel("No. Polisi", cc.xy(2, 28));
        builder.add(fieldPoliceNumber, cc.xyw(4, 28, 3));

        builder.addLabel("No. BPKB", cc.xy(2, 30));
        builder.add(fieldBPKBNumber, cc.xyw(4, 30, 3));

        builder.addLabel("Lokasi Aset", cc.xy(2, 32));
        builder.add(fieldLocation, cc.xyw(4, 32, 3));

        builder.addLabel("Lokasi Saat Ini", cc.xy(2, 34));
        builder.add(fieldPresentLocation, cc.xyw(4, 34, 3));

        builder.addLabel("Kondisi", cc.xy(2, 36));
        builder.add(comboCondition, cc.xyw(4, 36, 3));
        
        builder.addLabel("Harga Satuan (Rp.)", cc.xy(2, 38));
        builder.add(fieldPrice, cc.xyw(4, 38, 3));

        builder.addLabel("Sumber Dana", cc.xy(2, 40));
        builder.add(fieldFundingSource, cc.xyw(4, 40, 3));

        builder.addLabel("Asal-Usul", cc.xy(2, 42));
        builder.add(fieldAcquisitionWay, cc.xyw(4, 42, 3));

        builder.addLabel("Keterangan", cc.xy(2, 44));
        builder.add(scPane, cc.xywh(4, 44, 3, 2));

        builder.addSeparator("", cc.xyw(1, 47, 7));
        builder.add(btSavePanelBottom, cc.xyw(1, 49, 7));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Data Barang Peralatan Mesin");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }
    
    private void disableAll() {
        comboUnit.setEnabled(false);
        fieldItems.setEditable(false);
        fieldRegNumber.setEditable(false);
        fieldBPKBNumber.setEditable(false);
        fieldMachineType.setEditable(false);
        fieldVolume.setEditable(false);
        fieldFactoryNumber.setEditable(false);
        fieldFabricNumber.setEditable(false);
        fieldMachineNumber.setEditable(false);
        fieldPoliceNumber.setEditable(false);
        fieldAcquisitionYear.setEnabled(false);
        fieldYearBuilt.setEnabled(false);
        fieldLocation.setEditable(false);
        fieldPresentLocation.setEditable(false);
        fieldMaterial.setEditable(false);
        comboCondition.setEnabled(false);
        fieldPrice.setEditable(false);
        fieldFundingSource.setEditable(false);
        fieldAcquisitionWay.setEditable(false);
        fieldDescription.setEditable(false);
    }
    
    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldAcquisitionYear.setYear(year);
        fieldYearBuilt.setYear(year);
    }

    private void clearForm() {
        comboUnit.getEditor().setItem(null);
        fieldItems.setText("");
        fieldRegNumber.setText("");
        fieldBPKBNumber.setText("");
        fieldMachineType.setText("");
        fieldVolume.setValue(Integer.valueOf(0));
        fieldFactoryNumber.setText("");
        fieldFabricNumber.setText("");
        fieldMachineNumber.setText("");
        fieldPoliceNumber.setText("");
        clearYear();
        fieldLocation.setText("");
        fieldPresentLocation.setText("");
        fieldMaterial.setText("");
        comboCondition.getEditor().setItem(null);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldFundingSource.setText("");
        fieldAcquisitionWay.setText("");
        fieldDescription.setText("");
    }

    public void setFormValues(ItemsMachine machine) {
        if (machine != null) {

            if (machine.getUnit() == null) {
                comboUnit.getEditor().setItem(null);
            } else {
                comboUnit.setSelectedItem(machine.getUnit());
            }

            fieldItems.setText(machine.getItemCode() +" "+machine.getItemName());

            fieldRegNumber.setText(machine.getRegNumber());
            fieldBPKBNumber.setText(machine.getBpkbNumber());
            fieldMachineType.setText(machine.getMachineType());
            fieldVolume.setValue(machine.getVolume());
            fieldFactoryNumber.setText(machine.getFactoryNumber());
            fieldFabricNumber.setText(machine.getFabricNumber());
            fieldMachineNumber.setText(machine.getMachineNumber());
            fieldPoliceNumber.setText(machine.getPoliceNumber());
            fieldAcquisitionYear.setYear(machine.getAcquisitionYear());
            fieldYearBuilt.setYear(machine.getYearBuilt());
            fieldLocation.setText(machine.getLocation());
            fieldPresentLocation.setText(machine.getPresentLocation());
            fieldMaterial.setText(machine.getMaterial());

            if (machine.getCondition() == null) {
                comboCondition.getEditor().setItem(null);
            } else {
                comboCondition.setSelectedItem(machine.getCondition());
            }

            fieldPrice.setValue(machine.getPrice());
            fieldFundingSource.setText(machine.getFundingSource());
            fieldAcquisitionWay.setText(machine.getAcquisitionWay());
            fieldDescription.setText(machine.getDescription());

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
