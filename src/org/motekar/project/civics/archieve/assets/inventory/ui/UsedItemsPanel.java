package org.motekar.project.civics.archieve.assets.inventory.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
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
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXHyperlink;
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
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.objects.UsedItems;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.UsedItemsJasper;
import org.motekar.project.civics.archieve.assets.procurement.ui.SignerDlg;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class UsedItemsPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    //
    private JXTextField fieldItemsName = new JXTextField();
    private JXTextField fieldItemsCode = new JXTextField();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldLocationCode = new JXTextField();
    private JXTextField fieldRegNumber = new JXTextField();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXTextArea fieldItemAddress = new JXTextArea();
    private JXTextField fieldAcquitionWay = new JXTextField();
    private JYearChooser fieldAcquitionYear = new JYearChooser();
    private RadioButtonPanel radioConstruction = new RadioButtonPanel(new String[]{"P", "S", "D"}, 0, 3);
    private RadioButtonPanel radioCondition = new RadioButtonPanel(new String[]{"RB", "R", "KB", "B", "SB"}, 0, 5);
    private JXFormattedTextField fieldWide;
    private JXFormattedTextField fieldPrice;
    private JXTextField fieldDecreeNumber = new JXTextField();
    private JXTextField fieldCooperationPeriod = new JXTextField();
    private JXTextArea fieldThirdPartyAddress = new JXTextArea();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private UsedItemsTable table = new UsedItemsTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadUsedItems worker;
    private ProgressListener progressListener;
    private UsedItems selectedUsedItems = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportUsedItems importWorker;
    //
    private ItemsLand selectedItemsLand = null;
    private ItemsMachine selectedItemsMachine = null;
    private ItemsBuilding selectedItemsBuilding = null;
    private ItemsNetwork selectedItemsNetwork = null;
    private ItemsFixedAsset selectedItemsFixedAsset = null;
    private Object selectedItemObject = null;
    //
    private ItemsCategory parentCategory = null;
    //
    private Signer commanderSigner = null;
    private Signer employeeSigner = null;
    private Approval approval;
    //
    private JFileChooser xlsChooser;

    public UsedItemsPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("");
        } else {
            Unit unit = mainframe.getUnit();
            fieldLocationCode.setEnabled(false);
            fieldLocationCode.setText(unit.getUnitCode());
            String modifier = generateUnitModifier(unit);
            table.loadData(modifier);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where locationcode = '").append(unit.getUnitCode()).append("' ");
        }

        return query.toString();
    }

    private void construct() {

        constructFileChooser();

        loadParentCategory();
        contructNumberField();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPanel.addListener(this);
        btSavePanelUp.addListener(this);
        btSavePanelBottom.addListener(this);
        btPrintPanel.addListener(this);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(createInputPanel(), "1");

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<UsedItems> usedItemss = table.getSelectedUsedItemss();
                    if (!usedItemss.isEmpty()) {
                        if (usedItemss.size() == 1) {
                            selectedUsedItems = usedItemss.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedUsedItems = null;
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

                ItemsUsedPickDialog dlg = new ItemsUsedPickDialog(mainframe, mainframe.getConnection(), mainframe.getSession());
                dlg.showDialog();

                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemObject = dlg.getSelectedItemObject();
                    setItemsValues();
                }
            }
        });

        linkApprovalDatePlace.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                approvalPlaceAndDateAction();
            }
        });

        linkCommander.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseCommanderSigner();
            }
        });

        linkCommanderNIP.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseCommanderSigner();
            }
        });

        linkEmployee.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseEmployeeSigner();
            }
        });

        linkEmployeeNIP.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseEmployeeSigner();
            }
        });


        fillDefaultSignerLink();

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        
        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
    }

    private void constructFileChooser() {
        if (xlsChooser == null) {
            xlsChooser = new JFileChooser();
        }

        xlsChooser.setDialogTitle("Simpan File ke Excel");
        xlsChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);

        xlsChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return fileName.toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Excel(*.xls)";
            }
        });

    }

    private void setItemsValues() {
        if (selectedItemObject instanceof ItemsLand) {
            selectedItemsLand = (ItemsLand) selectedItemObject;
            fieldItemsName.setText(selectedItemsLand.getItemName());
            fieldItemsCode.setText(selectedItemsLand.getItemCode());

            fieldLocationCode.setText(selectedItemsLand.getUnit().getUnitCode());
            fieldRegNumber.setText(selectedItemsLand.getRegNumber());


            StringBuilder addr = new StringBuilder();
            addr.append(selectedItemsLand.getAddressLocation());

            if (selectedItemsLand.getUrban() != null) {
                Region urban = selectedItemsLand.getUrban();
                addr.append(" Kecamatan ").append(urban.getRegionName());
            }

            if (selectedItemsLand.getSubUrban() != null) {
                Region subUrban = selectedItemsLand.getSubUrban();
                addr.append(" Kelurahan ").append(subUrban.getRegionName());
            }

            fieldItemAddress.setText(addr.toString());

            fieldDocumentNumber.setText(selectedItemsLand.getLandCertificateNumber());

            if (selectedItemsLand.getAcquisitionYear() != null) {
                fieldAcquitionYear.setYear(selectedItemsLand.getAcquisitionYear());
            } else {
                clearYear();
            }
            fieldPrice.setValue(selectedItemsLand.getLandPrice());
            fieldDescription.setText(selectedItemsLand.getDescription());

            selectedItemsMachine = null;
            selectedItemsFixedAsset = null;
            selectedItemsBuilding = null;
            selectedItemsNetwork = null;

        } else if (selectedItemObject instanceof ItemsMachine) {
            selectedItemsMachine = (ItemsMachine) selectedItemObject;
            fieldItemsName.setText(selectedItemsMachine.getItemName());
            fieldItemsCode.setText(selectedItemsMachine.getItemCode());

            fieldLocationCode.setText(selectedItemsMachine.getUnit().getUnitCode());
            fieldRegNumber.setText(selectedItemsMachine.getRegNumber());

            fieldDocumentNumber.setText(selectedItemsMachine.getBpkbNumber());

            fieldItemAddress.setText(selectedItemsMachine.getLocation());

            if (selectedItemsMachine.getCondition() != null) {
                radioCondition.setSelectedData(selectedItemsMachine.getCondition().getConditionCode());
            } else {
                radioCondition.setAllDeselected();
            }
            if (selectedItemsMachine.getAcquisitionYear() != null) {
                fieldAcquitionYear.setYear(selectedItemsMachine.getAcquisitionYear());
            } else {
                clearYear();
            }
            fieldPrice.setValue(selectedItemsMachine.getPrice());
            fieldDescription.setText(selectedItemsMachine.getDescription());

            selectedItemsLand = null;
            selectedItemsFixedAsset = null;
            selectedItemsBuilding = null;
            selectedItemsNetwork = null;


        } else if (selectedItemObject instanceof ItemsFixedAsset) {
            selectedItemsFixedAsset = (ItemsFixedAsset) selectedItemObject;
            fieldItemsName.setText(selectedItemsFixedAsset.getItemName());
            fieldItemsCode.setText(selectedItemsFixedAsset.getItemCode());

            fieldLocationCode.setText(selectedItemsFixedAsset.getUnit().getUnitCode());
            fieldRegNumber.setText(selectedItemsFixedAsset.getRegNumber());

            if (selectedItemsFixedAsset.getCondition() != null) {
                radioCondition.setSelectedData(selectedItemsFixedAsset.getCondition().getConditionCode());
            } else {
                radioCondition.setAllDeselected();
            }
            if (selectedItemsFixedAsset.getAcquisitionYear() != null) {
                fieldAcquitionYear.setYear(selectedItemsFixedAsset.getAcquisitionYear());
            } else {
                clearYear();
            }
            fieldPrice.setValue(selectedItemsFixedAsset.getPrice());
            fieldDescription.setText(selectedItemsFixedAsset.getDescription());

            selectedItemsLand = null;
            selectedItemsMachine = null;
            selectedItemsBuilding = null;
            selectedItemsNetwork = null;
        } else if (selectedItemObject instanceof ItemsNetwork) {
            selectedItemsNetwork = (ItemsNetwork) selectedItemObject;
            fieldItemsName.setText(selectedItemsNetwork.getItemName());
            fieldItemsCode.setText(selectedItemsNetwork.getItemCode());

            fieldLocationCode.setText(selectedItemsNetwork.getUnit().getUnitCode());
            fieldRegNumber.setText(selectedItemsNetwork.getRegNumber());
            fieldDocumentNumber.setText(selectedItemsNetwork.getDocumentNumber());

            StringBuilder addr = new StringBuilder();
            addr.append(selectedItemsNetwork.getAddressLocation());

            if (selectedItemsNetwork.getUrban() != null) {
                Region urban = selectedItemsNetwork.getUrban();
                addr.append(" Kecamatan ").append(urban.getRegionName());
            }

            if (selectedItemsNetwork.getSubUrban() != null) {
                Region subUrban = selectedItemsNetwork.getSubUrban();
                addr.append(" Kelurahan ").append(subUrban.getRegionName());
            }

            fieldItemAddress.setText(addr.toString());

            if (selectedItemsNetwork.getCondition() != null) {
                radioCondition.setSelectedData(selectedItemsNetwork.getCondition().getConditionCode());
            } else {
                radioCondition.setAllDeselected();
            }
            fieldPrice.setValue(selectedItemsNetwork.getPrice());
            fieldDescription.setText(selectedItemsNetwork.getDescription());

            selectedItemsLand = null;
            selectedItemsMachine = null;
            selectedItemsBuilding = null;
            selectedItemsFixedAsset = null;

        } else if (selectedItemObject instanceof ItemsBuilding) {
            selectedItemsBuilding = (ItemsBuilding) selectedItemObject;
            fieldItemsName.setText(selectedItemsBuilding.getItemName());
            fieldItemsCode.setText(selectedItemsBuilding.getItemCode());

            fieldLocationCode.setText(selectedItemsBuilding.getUnit().getUnitCode());
            fieldRegNumber.setText(selectedItemsBuilding.getRegNumber());

            fieldDocumentNumber.setText(selectedItemsBuilding.getDocumentNumber());

            StringBuilder addr = new StringBuilder();
            addr.append(selectedItemsBuilding.getAddress());

            if (selectedItemsBuilding.getUrban() != null) {
                Region urban = selectedItemsBuilding.getUrban();
                addr.append(" Kecamatan ").append(urban.getRegionName());
            }

            if (selectedItemsBuilding.getSubUrban() != null) {
                Region subUrban = selectedItemsBuilding.getSubUrban();
                addr.append(" Kelurahan ").append(subUrban.getRegionName());
            }

            fieldItemAddress.setText(addr.toString());


            if (selectedItemsBuilding.getCondition() != null) {
                radioCondition.setSelectedData(selectedItemsBuilding.getCondition().getConditionCode());
            } else {
                radioCondition.setAllDeselected();
            }
            if (selectedItemsBuilding.getAcquisitionYear() != null) {
                fieldAcquitionYear.setYear(selectedItemsBuilding.getAcquisitionYear());
            } else {
                clearYear();
            }
            fieldPrice.setValue(selectedItemsBuilding.getPrice());
            fieldDescription.setText(selectedItemsBuilding.getDescription());

            selectedItemsLand = null;
            selectedItemsMachine = null;
            selectedItemsNetwork = null;
            selectedItemsFixedAsset = null;
        }
    }

    private void approvalPlaceAndDateAction() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

            approval = getApprovalFromLabel();

            ApprovalDlg dlg = new ApprovalDlg(mainframe, approval);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                approval = dlg.getApproval();
                linkApprovalDatePlace.setText(approval.getPlace() + "," + sdf.format(approval.getDate()));
            }
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void chooseCommanderSigner() {
        SignerDlg dlg = new SignerDlg(mainframe, mainframe.getConnection(), mainframe.getSession(), commanderSigner, Signer.TYPE_COMMANDER);
        dlg.showDialog();
        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            commanderSigner = dlg.getSigner();
            linkCommander.setText(commanderSigner.getSignerName());
            linkCommanderNIP.setText("NIP. " + commanderSigner.getSignerNIP());
        }
    }

    private void chooseEmployeeSigner() {
        SignerDlg dlg = new SignerDlg(mainframe, mainframe.getConnection(), mainframe.getSession(), employeeSigner, Signer.TYPE_SIGNER);
        dlg.showDialog();
        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            employeeSigner = dlg.getSigner();
            linkEmployee.setText(employeeSigner.getSignerName());
            linkEmployeeNIP.setText("NIP. " + employeeSigner.getSignerNIP());
        }
    }

    private Approval getApprovalFromLabel() throws ParseException {
        Approval approvals = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

        ArrayList<String> str = new ArrayList<String>();

        if (!linkApprovalDatePlace.getText().contains("........")) {

            StringTokenizer token = new StringTokenizer(linkApprovalDatePlace.getText(), ",");
            while (token.hasMoreElements()) {
                str.add(token.nextToken());
            }

            if (!str.isEmpty()) {
                approvals = new Approval();
                approvals.setPlace(str.get(0));
                approvals.setDate(sdf.parse(str.get(1)));
            }
        }


        return approvals;
    }

    private void fillDefaultSignerLink() {
        linkApprovalDatePlace.setText(".......... , ................................");
        linkCommander.setText("(..........................................)");
        linkCommanderNIP.setText("NIP. ..........................................");
        linkEmployee.setText("(..........................................)");
        linkEmployeeNIP.setText("NIP. ..........................................");
    }

    private void loadParentCategory() {
        try {
            parentCategory = mLogic.getParentItemsCategory(mainframe.getSession());
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

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);

        fieldWide = new JXFormattedTextField();
        fieldWide.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldWide.setHorizontalAlignment(JLabel.RIGHT);
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
                "200px,right:pref,10px,pref,5px, 100px,5px,fill:default:grow,200px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "50px,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,"
                + "50px,fill:default:grow,5px,"
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

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(fieldItemAddress);

        JScrollPane scPane3 = new JScrollPane();
        scPane3.setViewportView(fieldThirdPartyAddress);

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 9));
        builder.addSeparator("", cc.xyw(1, 2, 9));

        builder.add(panelRequired, cc.xyw(1, 4, 9));

        JXLabel itemLabel = new JXLabel("Nama Barang");
        itemLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        JXLabel receviceDateLabel = new JXLabel("Tanggal Diterima");
        receviceDateLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        receviceDateLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);


        builder.add(itemLabel, cc.xy(2, 6));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 6, 1));
        builder.add(fieldItemsName, cc.xyw(6, 6, 3));

        builder.addLabel("Kode Barang", cc.xy(2, 8));
        builder.add(fieldItemsCode, cc.xyw(4, 8, 5));

        builder.addLabel("No. Kode Lokasi", cc.xy(2, 10));
        builder.add(fieldLocationCode, cc.xyw(4, 10, 5));

        builder.addLabel("Nomor Register", cc.xy(2, 12));
        builder.add(fieldRegNumber, cc.xyw(4, 12, 5));

        builder.addLabel("Dokumen Barang", cc.xy(2, 14));
        builder.add(fieldDocumentNumber, cc.xyw(4, 14, 5));

        builder.addLabel("Alamat Barang", cc.xy(2, 16));
        builder.add(scPane2, cc.xywh(4, 16, 5, 2));

        builder.addLabel("Asal-usul Barang", cc.xy(2, 19));
        builder.add(fieldAcquitionWay, cc.xyw(4, 19, 5));

        builder.addLabel("Tahun Pembelian / Pengadaan", cc.xy(2, 21));
        builder.add(fieldAcquitionYear, cc.xyw(4, 21, 3));

        builder.addLabel("Konstruksi", cc.xy(2, 23));
        builder.add(radioConstruction, cc.xyw(4, 23, 5));

        builder.addLabel("Keadaan Barang", cc.xy(2, 25));
        builder.add(radioCondition, cc.xyw(4, 25, 5));

        builder.addLabel("Luas (M2)", cc.xy(2, 27));
        builder.add(fieldWide, cc.xyw(4, 27, 3));

        builder.addLabel("Nilai Barang", cc.xy(2, 29));
        builder.add(fieldPrice, cc.xyw(4, 29, 3));

        builder.addLabel("SK KDH", cc.xy(2, 31));
        builder.add(fieldDecreeNumber, cc.xyw(4, 31, 5));

        builder.addLabel("Jangka Waktu Kerjasama", cc.xy(2, 33));
        builder.add(fieldCooperationPeriod, cc.xyw(4, 33, 3));

        builder.addLabel("Alamat Pihak Ketiga", cc.xy(2, 35));
        builder.add(scPane3, cc.xywh(4, 35, 5, 2));

        builder.addLabel("Keterangan", cc.xy(2, 38));
        builder.add(scPane, cc.xywh(4, 38, 5, 2));

        builder.addSeparator("", cc.xyw(1, 41, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 42, 9));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input Penerimaan Barang Dari Pihak Ketiga Barang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
//        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private Component createSignerPanel() {
        FormLayout lm = new FormLayout(
                "5px,pref,50px,fill:default:grow,50px,pref,5px",
                "pref,2px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(createApprovalPlacePanel(), cc.xy(6, 1));

        builder.add(createEmployeeSignerPanel(), cc.xy(2, 3));
        builder.add(createCommanderSignerPanel(), cc.xy(6, 3));

        return builder.getPanel();
    }

    private Component createApprovalPlacePanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(linkApprovalDatePlace, cc.xy(1, 1));

        return builder.getPanel();
    }

    private Component createCommanderSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("KEPALA SKPD", cc.xy(1, 1));

        builder.add(linkCommander, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkCommanderNIP, cc.xy(1, 9));

        return builder.getPanel();
    }

    private Component createEmployeeSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("MENGETAHUI", cc.xy(1, 1));
        builder.addLabel("PENGELOLA BARANG", cc.xy(1, 3));

        builder.add(linkEmployee, cc.xy(1, 7));
        builder.addSeparator("", cc.xyw(1, 9, 2));
        builder.add(linkEmployeeNIP, cc.xy(1, 11));

        return builder.getPanel();
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
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panel, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        collapasepanel.add(createSignerPanel(), BorderLayout.SOUTH);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,10px, pref,30px,pref,10px,pref,10px,fill:default:grow,350px",
                "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        ProfileAccount profile = mainframe.getProfileAccount();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SKPD", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.addLabel(profile.getCompany().toUpperCase(), cc.xy(5, 1));

        builder.addLabel("KAB / KOTA", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.addLabel(profile.getState().toUpperCase(), cc.xy(5, 3));

        builder.addLabel("PROPINSI", cc.xy(7, 1));
        builder.addLabel(":", cc.xy(9, 1));
        builder.addLabel(profile.getProvince().toUpperCase(), cc.xy(11, 1));

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
        fieldAcquitionYear.setYear(year);
    }

    private void clearForm() {
        fieldItemsName.setText("");
        fieldItemsCode.setText("");
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            fieldLocationCode.setText("");
        }

        fieldRegNumber.setText("");
        fieldDocumentNumber.setText("");
        fieldItemAddress.setText("");
        fieldAcquitionWay.setText("");
        clearYear();
        radioConstruction.setAllDeselected();
        radioCondition.setAllDeselected();
        fieldWide.setValue(BigDecimal.ZERO);
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedUsedItems != null) {

            fieldItemsName.setText(selectedUsedItems.getItemName());
            fieldItemsCode.setText(selectedUsedItems.getItemCode());
            fieldLocationCode.setText(selectedUsedItems.getLocationCode());
            fieldRegNumber.setText(selectedUsedItems.getRegisterNumber());
            fieldDocumentNumber.setText(selectedUsedItems.getDocumentNumber());
            fieldItemAddress.setText(selectedUsedItems.getItemAddress());
            fieldAcquitionWay.setText(selectedUsedItems.getAcquisitionWay());
            fieldAcquitionYear.setYear(selectedUsedItems.getAcquisitionYear());
            radioConstruction.setSelectedData(selectedUsedItems.getConstruction());
            radioCondition.setSelectedData(selectedUsedItems.getCondition());

            fieldWide.setValue(selectedUsedItems.getWide());
            fieldPrice.setValue(selectedUsedItems.getPrice());
            fieldDecreeNumber.setText(selectedUsedItems.getDecreeNumber());
            fieldCooperationPeriod.setText(selectedUsedItems.getCooperationPeriod());
            fieldThirdPartyAddress.setText(selectedUsedItems.getThirdPartyAddress());
            fieldDescription.setText(selectedUsedItems.getDescription());

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private UsedItems getUsedItems() throws MotekarException {
        StringBuilder errorString = new StringBuilder();


        String itemName = fieldItemsName.getText();
        String itemCode = fieldItemsCode.getText();
        String locationCode = fieldLocationCode.getText();
        String registerNumber = fieldRegNumber.getText();
        String documentNumber = fieldDocumentNumber.getText();
        String itemAddress = fieldItemAddress.getText();
        String acquisitionWay = fieldAcquitionWay.getText();
        Integer acquisitionYear = fieldAcquitionYear.getYear();
        String construction = radioConstruction.getSelectedData();
        String condition = radioCondition.getSelectedData();

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


        String decreeNumber = fieldDecreeNumber.getText();
        String cooperationPeriod = fieldCooperationPeriod.getText();
        String thirdPartyAddress = fieldThirdPartyAddress.getText();
        String description = fieldDescription.getText();


        if (itemName.equals("")) {
            errorString.append("<br>- Nama / Jenis Barang</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        UsedItems usedItems = new UsedItems();
        usedItems.setItemCode(itemCode);
        usedItems.setItemName(itemName);
        usedItems.setLocationCode(locationCode);
        usedItems.setRegisterNumber(registerNumber);
        usedItems.setDocumentNumber(documentNumber);
        usedItems.setItemAddress(itemAddress);
        usedItems.setAcquisitionWay(acquisitionWay);
        usedItems.setAcquisitionYear(acquisitionYear);
        usedItems.setConstruction(construction);
        usedItems.setCondition(condition);
        usedItems.setWide(wide);
        usedItems.setPrice(price);
        usedItems.setDecreeNumber(decreeNumber);
        usedItems.setCooperationPeriod(cooperationPeriod);
        usedItems.setThirdPartyAddress(thirdPartyAddress);
        usedItems.setDescription(description);

        if (selectedItemsLand != null) {
            usedItems.setItemIndex(selectedItemsLand.getIndex());
            usedItems.setItemType(ItemsCategory.ITEMS_LAND);
        } else if (selectedItemsMachine != null) {
            usedItems.setItemIndex(selectedItemsMachine.getIndex());
            usedItems.setItemType(ItemsCategory.ITEMS_MACHINE);
        } else if (selectedItemsBuilding != null) {
            usedItems.setItemIndex(selectedItemsBuilding.getIndex());
            usedItems.setItemType(ItemsCategory.ITEMS_BUILDING);
        } else if (selectedItemsNetwork != null) {
            usedItems.setItemIndex(selectedItemsNetwork.getIndex());
            usedItems.setItemType(ItemsCategory.ITEMS_NETWORK);
        } else if (selectedItemsFixedAsset != null) {
            usedItems.setItemIndex(selectedItemsFixedAsset.getIndex());
            usedItems.setItemType(ItemsCategory.ITEMS_FIXED_ASSET);
        } else {
            if (selectedUsedItems != null) {
                usedItems.setItemIndex(selectedUsedItems.getItemIndex());
                usedItems.setItemType(selectedUsedItems.getItemType());
            }
        }


        return usedItems;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldItemsName.requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldItemsName.requestFocus();
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
                ArrayList<UsedItems> selectedUsedItemss = table.getSelectedUsedItemss();
                if (!selectedUsedItemss.isEmpty()) {
                    logic.deleteUsedItems(mainframe.getSession(), selectedUsedItemss);
                    table.removeUsedItems(selectedUsedItemss);
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
            UsedItems newUsedItems = getUsedItems();
            if (btPanel.isNewstate()) {
                newUsedItems = logic.insertUsedItems(mainframe.getSession(), newUsedItems);
                table.addUsedItems(newUsedItems);
                selectedUsedItems = newUsedItems;
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newUsedItems = logic.updateUsedItems(mainframe.getSession(), selectedUsedItems, newUsedItems);
                table.updateSelectedUsedItems(newUsedItems);
                selectedUsedItems = newUsedItems;
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

    public void onPrint() throws Exception, CommonException {
        try {

            StringBuilder errorString = new StringBuilder();

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Pengurus Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }


            UsedItemsJasper report = new UsedItemsJasper("Daftar Barang Milik Daerah yang Digunausahakan", table.getUsedItemss(),
                    mainframe.getProfileAccount(), approval, employeeSigner, commanderSigner);

            report.showReport();

        } catch (MotekarException ex) {
            Exceptions.printStackTrace(ex);
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void onPrintExcel() throws Exception, CommonException {
        try {

            StringBuilder errorString = new StringBuilder();

            if (commanderSigner == null) {
                errorString.append("<br>- Pilih Atasan Langsung</br>");
            }

            if (employeeSigner == null) {
                errorString.append("<br>- Pilih Pengurus Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }


            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";


                UsedItemsJasper report = new UsedItemsJasper("Daftar Barang Milik Daerah yang Digunausahakan", table.getUsedItemss(),
                        mainframe.getProfileAccount(), approval, employeeSigner, commanderSigner);

                report.exportToExcel(fileName);
            }

        } catch (MotekarException ex) {
            Exceptions.printStackTrace(ex);
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void onPrintPDF() throws Exception, CommonException {
        //
    }

    public void onPrintGraph() throws Exception, CommonException {
    }

    private class UsedItemsTable extends JXTable {

        private UsedItemsTableModel model;

        public UsedItemsTable() {
            model = new UsedItemsTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

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

            worker = new LoadUsedItems(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public UsedItems getSelectedUsedItems() {
            ArrayList<UsedItems> selectedUsedItemss = getSelectedUsedItemss();
            return selectedUsedItemss.get(0);
        }

        public ArrayList<UsedItems> getUsedItemss() {
            return model.getUsedItemss();
        }

        public ArrayList<UsedItems> getSelectedUsedItemss() {

            ArrayList<UsedItems> usedItemss = new ArrayList<UsedItems>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                UsedItems usedItems = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 5);
                        if (obj instanceof UsedItems) {
                            usedItems = (UsedItems) obj;
                            usedItemss.add(usedItems);
                        }
                    }
                }
            }

            return usedItemss;
        }

        public void updateSelectedUsedItems(UsedItems usedItems) {
            model.updateRow(getSelectedUsedItems(), usedItems);
        }

        public void removeUsedItems(ArrayList<UsedItems> usedItemss) {
            if (!usedItemss.isEmpty()) {
                for (UsedItems usedItems : usedItemss) {
                    model.remove(usedItems);
                }
            }

        }

        public void addUsedItems(ArrayList<UsedItems> usedItemss) {
            if (!usedItemss.isEmpty()) {
                for (UsedItems usedItems : usedItemss) {
                    model.add(usedItems);
                }
            }
        }

        public void addUsedItems(UsedItems usedItems) {
            model.add(usedItems);
        }

        public void insertEmptyUsedItems() {
            addUsedItems(new UsedItems());
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
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(JLabel.class)) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class UsedItemsTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 18;
        private static final String[] COLUMNS = {"",
            "<html><center><b>No</b></center>",
            "<html><center><b>No.Kode<br>Lokasi</br></b></center>",
            "<html><center><b>No.Kode<br>Barang</br></b></center>",
            "<html><center><b>Nomor<br>Register</br></b></center>",
            "<html><center><b>Nama/Jenis<br>Barang</br></b></center>",
            "<html><center><b>Dokumen<br>Barang</br></b></center>",
            "<html><center><b>Alamat<br>Barang</br></b></center>",
            "<html><center><b>Asal-usul<br>Barang</br></b></center>",
            "<html><center><b>Tahun Pembelian/<br>Pengadaan</br></b></center>",
            "<html><center><b>Konstruksi<br>(P,S,D)</br></b></center>",
            "<html><center><b>Keadaan Barang<br>(RB,R,KB,B,SB)</br></b></center>",
            "<html><center><b>Luas<br>(M2)</br></b></center>",
            "<html><center><b>Nilai<br>Barang</br></b></center>",
            "<html><center><b>SK KDH</b></center>",
            "<html><center><b>Jangka Waktu<br>Kerjasama</br></b></center>",
            "<html><center><b>Alamat<br>Pihak Ketiga</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<UsedItems> usedItemss = new ArrayList<UsedItems>();

        public UsedItemsTableModel() {
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
            } else if (columnIndex == 12 || columnIndex == 13) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 9) {
                return Integer.class;
            } else if (columnIndex == 2 || columnIndex == 3
                    || columnIndex == 4 || columnIndex == 6
                    || columnIndex == 10 || columnIndex == 11
                    || columnIndex == 14 || columnIndex == 15) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<UsedItems> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            usedItemss.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(UsedItems usedItems) {
            insertRow(getRowCount(), usedItems);
        }

        public void insertRow(int row, UsedItems usedItems) {
            usedItemss.add(row, usedItems);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(UsedItems oldUsedItems, UsedItems newUsedItems) {
            int index = usedItemss.indexOf(oldUsedItems);
            usedItemss.set(index, newUsedItems);
            fireTableRowsUpdated(index, index);
        }

        public void remove(UsedItems usedItems) {
            int row = usedItemss.indexOf(usedItems);
            usedItemss.remove(usedItems);
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
                usedItemss.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                usedItemss.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            usedItemss.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (usedItemss.get(i) == null) {
                    usedItemss.set(i, new UsedItems());
                }
            }
        }

        public ArrayList<UsedItems> getUsedItemss() {
            return usedItemss;
        }

        @Override
        public int getRowCount() {
            return usedItemss.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public UsedItems getUsedItems(int row) {
            if (!usedItemss.isEmpty()) {
                return usedItemss.get(row);
            }
            return new UsedItems();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            UsedItems usedItems = getUsedItems(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        usedItems.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    usedItems.setLocationCode((String) aValue);
                    break;
                case 3:
                    usedItems.setItemCode((String) aValue);
                    break;
                case 4:
                    usedItems.setRegisterNumber((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof UsedItems) {
                        usedItems = (UsedItems) aValue;
                    }
                    break;
                case 6:
                    usedItems.setDocumentNumber((String) aValue);
                    break;
                case 7:
                    usedItems.setItemAddress((String) aValue);
                    break;
                case 8:
                    usedItems.setAcquisitionWay((String) aValue);
                    break;
                case 9:
                    if (aValue instanceof Integer) {
                        usedItems.setAcquisitionYear((Integer) aValue);
                    }
                    break;
                case 10:
                    usedItems.setConstruction((String) aValue);
                    break;
                case 11:
                    usedItems.setCondition((String) aValue);
                    break;
                case 12:
                    if (aValue instanceof BigDecimal) {
                        usedItems.setWide((BigDecimal) aValue);
                    }
                    break;
                case 13:
                    if (aValue instanceof BigDecimal) {
                        usedItems.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 14:
                    usedItems.setDecreeNumber((String) aValue);
                    break;
                case 15:
                    usedItems.setCooperationPeriod((String) aValue);
                    break;
                case 16:
                    usedItems.setThirdPartyAddress((String) aValue);
                    break;
                case 17:
                    usedItems.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            UsedItems usedItems = getUsedItems(row);
            switch (column) {
                case 0:
                    toBeDisplayed = usedItems.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = usedItems.getLocationCode();
                    break;
                case 3:
                    toBeDisplayed = usedItems.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = usedItems.getRegisterNumber();
                    break;
                case 5:
                    toBeDisplayed = usedItems;
                    break;
                case 6:
                    toBeDisplayed = usedItems.getDocumentNumber();
                    break;
                case 7:
                    toBeDisplayed = usedItems.getItemAddress();
                    break;
                case 8:
                    toBeDisplayed = usedItems.getAcquisitionWay();
                    break;
                case 9:
                    toBeDisplayed = usedItems.getAcquisitionYear();
                    break;
                case 10:
                    toBeDisplayed = usedItems.getConstruction();
                    break;
                case 11:
                    toBeDisplayed = usedItems.getCondition();
                    break;
                case 12:
                    toBeDisplayed = usedItems.getWide();
                    break;
                case 13:
                    toBeDisplayed = usedItems.getPrice();
                    break;
                case 14:
                    toBeDisplayed = usedItems.getDecreeNumber();
                    break;
                case 15:
                    toBeDisplayed = usedItems.getCooperationPeriod();
                    break;
                case 16:
                    toBeDisplayed = usedItems.getThirdPartyAddress();
                    break;
                case 17:
                    toBeDisplayed = usedItems.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadUsedItems extends SwingWorker<UsedItemsTableModel, UsedItems> {

        private UsedItemsTableModel model;
        private Exception exception;
        private String modifier;

        public LoadUsedItems(String modifier) {
            this.model = (UsedItemsTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<UsedItems> chunks) {
            mainframe.stopInActiveListener();
            for (UsedItems usedItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat BMD yang digunausahakan " + usedItems.getItemName());
                model.add(usedItems);
            }
        }

        @Override
        protected UsedItemsTableModel doInBackground() throws Exception {
            try {
                ArrayList<UsedItems> usedItemss = logic.getUsedItems(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!usedItemss.isEmpty()) {
                    for (int i = 0; i < usedItemss.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / usedItemss.size();

                        UsedItems usedItems = usedItemss.get(i);

                        setProgress((int) progress);
                        publish(usedItems);
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
                JXErrorPane.showDialog(UsedItemsPanel.this, info);
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

    private class ImportUsedItems extends SwingWorker<UsedItemsTableModel, UsedItems> {

        private UsedItemsTableModel model;
        private Exception exception;
        private List<UsedItems> data;

        public ImportUsedItems(List<UsedItems> data) {
            this.model = (UsedItemsTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<UsedItems> chunks) {
            mainframe.stopInActiveListener();
            for (UsedItems usedItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload Penerimaan Barang Dari Pihak Ketiga Barang " + usedItems.getItemName());
                model.add(usedItems);
            }
        }

        @Override
        protected UsedItemsTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        UsedItems usedItems = data.get(i);

                        synchronized (usedItems) {
                            usedItems = logic.insertUsedItems(mainframe.getSession(), usedItems);
                        }


                        setProgress((int) progress);
                        publish(usedItems);
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
                JXErrorPane.showDialog(UsedItemsPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
