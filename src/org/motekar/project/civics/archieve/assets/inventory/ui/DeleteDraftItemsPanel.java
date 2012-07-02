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
import org.motekar.project.civics.archieve.assets.inventory.objects.DeleteDraftItems;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.DeleteDraftItemsJasper;
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
public class DeleteDraftItemsPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

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
    private JXTextField fieldSpecificationType = new JXTextField();
    private JXTextField fieldOwnedDocument = new JXTextField();
    private JYearChooser fieldAcquitionYear = new JYearChooser();
    private JXFormattedTextField fieldPrice;
    private RadioButtonPanel radioCondition = new RadioButtonPanel(new String[]{"RB", "R", "KB", "B", "SB"}, 0, 5);
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private DeleteDraftItemsTable table = new DeleteDraftItemsTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadDeleteDraftItems worker;
    private ProgressListener progressListener;
    private DeleteDraftItems selectedDeleteDraftItems = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportDeleteDraftItems importWorker;
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

    public DeleteDraftItemsPanel(ArchieveMainframe mainframe) {
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
                    ArrayList<DeleteDraftItems> deleteDraftItemss = table.getSelectedDeleteDraftItemss();
                    if (!deleteDraftItemss.isEmpty()) {
                        if (deleteDraftItemss.size() == 1) {
                            selectedDeleteDraftItems = deleteDraftItemss.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedDeleteDraftItems = null;
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

                ItemsPickDialog dlg = new ItemsPickDialog(mainframe, mainframe.getConnection(), mainframe.getSession());
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
            fieldSpecificationType.setText(selectedItemsMachine.getMachineType());
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
                + "pref,5px,pref,5px,pref,5px,"
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

        builder.addLabel("Merk/Type", cc.xy(2, 12));
        builder.add(fieldSpecificationType, cc.xyw(4, 12, 5));

        builder.addLabel("Dokumen Kepemilikan", cc.xy(2, 14));
        builder.add(fieldOwnedDocument, cc.xyw(4, 14, 5));

        builder.addLabel("Tahun Pembelian", cc.xy(2, 16));
        builder.add(fieldAcquitionYear, cc.xyw(4, 16, 3));

        builder.addLabel("Harga Perolehan", cc.xy(2, 18));
        builder.add(fieldPrice, cc.xyw(4, 18, 3));

        builder.addLabel("Keadaan Barang", cc.xy(2, 20));
        builder.add(radioCondition, cc.xyw(4, 20, 5));

        builder.addLabel("Keterangan", cc.xy(2, 22));
        builder.add(scPane, cc.xywh(4, 22, 5, 2));

        builder.addSeparator("", cc.xyw(1, 25, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 26, 9));

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

        builder.add(createCommanderSignerPanel(), cc.xy(2, 3));
        builder.add(createEmployeeSignerPanel(), cc.xy(6, 3));

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
                "pref,5px,pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("MENGETAHUI", cc.xy(1, 1));
        builder.addLabel("KEPALA SKPD", cc.xy(1, 3));

        builder.add(linkCommander, cc.xy(1, 7));
        builder.addSeparator("", cc.xyw(1, 9, 2));
        builder.add(linkCommanderNIP, cc.xy(1, 11));

        return builder.getPanel();
    }

    private Component createEmployeeSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("PENYIMPAN BARANG", cc.xy(1, 1));

        builder.add(linkEmployee, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkEmployeeNIP, cc.xy(1, 9));

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
        fieldSpecificationType.setText("");
        fieldOwnedDocument.setText("");
        clearYear();
        fieldPrice.setValue(BigDecimal.ZERO);
        radioCondition.setAllDeselected();
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedDeleteDraftItems != null) {

            fieldItemsName.setText(selectedDeleteDraftItems.getItemName());
            fieldItemsCode.setText(selectedDeleteDraftItems.getItemCode());

            fieldLocationCode.setText(selectedDeleteDraftItems.getLocationCode());
            fieldSpecificationType.setText(selectedDeleteDraftItems.getSpecificationType());
            fieldOwnedDocument.setText(selectedDeleteDraftItems.getOwnedDocument());
            fieldAcquitionYear.setYear(selectedDeleteDraftItems.getAcquisitionYear());
            fieldPrice.setValue(selectedDeleteDraftItems.getPrice());
            radioCondition.setSelectedData(selectedDeleteDraftItems.getCondition());
            fieldDescription.setText(selectedDeleteDraftItems.getDescription());

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private DeleteDraftItems getDeleteDraftItems() throws MotekarException {
        StringBuilder errorString = new StringBuilder();


        String itemName = fieldItemsName.getText();
        String itemCode = fieldItemsCode.getText();
        String locationCode = fieldLocationCode.getText();
        String specificationType = fieldSpecificationType.getText();
        String ownedDocument = fieldOwnedDocument.getText();
        Integer acquisitionYear = fieldAcquitionYear.getYear();

        Object objPrice = fieldPrice.getValue();
        BigDecimal price = BigDecimal.ZERO;

        if (objPrice instanceof Long) {
            Long value = (Long) objPrice;
            price = BigDecimal.valueOf(value.intValue());
        } else if (objPrice instanceof BigDecimal) {
            price = (BigDecimal) objPrice;
        }

        String condition = radioCondition.getSelectedData();

        String description = fieldDescription.getText();


        if (itemName.equals("")) {
            errorString.append("<br>- Nama / Jenis Barang</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        DeleteDraftItems deleteDraftItems = new DeleteDraftItems();
        deleteDraftItems.setItemCode(itemCode);
        deleteDraftItems.setItemName(itemName);
        deleteDraftItems.setLocationCode(locationCode);
        deleteDraftItems.setSpecificationType(specificationType);
        deleteDraftItems.setOwnedDocument(ownedDocument);
        deleteDraftItems.setAcquisitionYear(acquisitionYear);
        deleteDraftItems.setPrice(price);
        deleteDraftItems.setCondition(condition);
        deleteDraftItems.setDescription(description);

        if (selectedItemsLand != null) {
            deleteDraftItems.setItemIndex(selectedItemsLand.getIndex());
            deleteDraftItems.setItemType(ItemsCategory.ITEMS_LAND);
        } else if (selectedItemsMachine != null) {
            deleteDraftItems.setItemIndex(selectedItemsMachine.getIndex());
            deleteDraftItems.setItemType(ItemsCategory.ITEMS_MACHINE);
        } else if (selectedItemsBuilding != null) {
            deleteDraftItems.setItemIndex(selectedItemsBuilding.getIndex());
            deleteDraftItems.setItemType(ItemsCategory.ITEMS_BUILDING);
        } else if (selectedItemsNetwork != null) {
            deleteDraftItems.setItemIndex(selectedItemsNetwork.getIndex());
            deleteDraftItems.setItemType(ItemsCategory.ITEMS_NETWORK);
        } else if (selectedItemsFixedAsset != null) {
            deleteDraftItems.setItemIndex(selectedItemsFixedAsset.getIndex());
            deleteDraftItems.setItemType(ItemsCategory.ITEMS_FIXED_ASSET);
        } else {
            if (selectedDeleteDraftItems != null) {
                deleteDraftItems.setItemIndex(selectedDeleteDraftItems.getItemIndex());
                deleteDraftItems.setItemType(selectedDeleteDraftItems.getItemType());
            }
        }


        return deleteDraftItems;
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
                ArrayList<DeleteDraftItems> selectedDeleteDraftItemss = table.getSelectedDeleteDraftItemss();
                if (!selectedDeleteDraftItemss.isEmpty()) {
                    logic.deleteDeleteDraftItems(mainframe.getSession(), selectedDeleteDraftItemss);
                    table.removeDeleteDraftItems(selectedDeleteDraftItemss);
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
            DeleteDraftItems newDeleteDraftItems = getDeleteDraftItems();
            if (btPanel.isNewstate()) {
                newDeleteDraftItems = logic.insertDeleteDraftItems(mainframe.getSession(), newDeleteDraftItems);
                table.addDeleteDraftItems(newDeleteDraftItems);
                selectedDeleteDraftItems = newDeleteDraftItems;
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newDeleteDraftItems = logic.updateDeleteDraftItems(mainframe.getSession(), selectedDeleteDraftItems, newDeleteDraftItems);
                table.updateSelectedDeleteDraftItems(newDeleteDraftItems);
                selectedDeleteDraftItems = newDeleteDraftItems;
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


            DeleteDraftItemsJasper report = new DeleteDraftItemsJasper("Daftar Usulan Barang yang Akan dihapus", table.getDeleteDraftItemss(),
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


                DeleteDraftItemsJasper report = new DeleteDraftItemsJasper("Daftar Usulan Barang yang Akan dihapus", table.getDeleteDraftItemss(),
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

    private class DeleteDraftItemsTable extends JXTable {

        private DeleteDraftItemsTableModel model;

        public DeleteDraftItemsTable() {
            model = new DeleteDraftItemsTableModel();
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

            worker = new LoadDeleteDraftItems(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public DeleteDraftItems getSelectedDeleteDraftItems() {
            ArrayList<DeleteDraftItems> selectedDeleteDraftItemss = getSelectedDeleteDraftItemss();
            return selectedDeleteDraftItemss.get(0);
        }

        public ArrayList<DeleteDraftItems> getDeleteDraftItemss() {
            return model.getDeleteDraftItemss();
        }

        public ArrayList<DeleteDraftItems> getSelectedDeleteDraftItemss() {

            ArrayList<DeleteDraftItems> deleteDraftItemss = new ArrayList<DeleteDraftItems>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                DeleteDraftItems deleteDraftItems = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof DeleteDraftItems) {
                            deleteDraftItems = (DeleteDraftItems) obj;
                            deleteDraftItemss.add(deleteDraftItems);
                        }
                    }
                }
            }

            return deleteDraftItemss;
        }

        public void updateSelectedDeleteDraftItems(DeleteDraftItems deleteDraftItems) {
            model.updateRow(getSelectedDeleteDraftItems(), deleteDraftItems);
        }

        public void removeDeleteDraftItems(ArrayList<DeleteDraftItems> deleteDraftItemss) {
            if (!deleteDraftItemss.isEmpty()) {
                for (DeleteDraftItems deleteDraftItems : deleteDraftItemss) {
                    model.remove(deleteDraftItems);
                }
            }

        }

        public void addDeleteDraftItems(ArrayList<DeleteDraftItems> deleteDraftItemss) {
            if (!deleteDraftItemss.isEmpty()) {
                for (DeleteDraftItems deleteDraftItems : deleteDraftItemss) {
                    model.add(deleteDraftItems);
                }
            }
        }

        public void addDeleteDraftItems(DeleteDraftItems deleteDraftItems) {
            model.add(deleteDraftItems);
        }

        public void insertEmptyDeleteDraftItems() {
            addDeleteDraftItems(new DeleteDraftItems());
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

    private static class DeleteDraftItemsTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 11;
        private static final String[] COLUMNS = {"",
            "<html><center><b>No</b></center>",
            "<html><center><b>Nama Barang</b></center>",
            "<html><center><b>No.Kode<br>Barang</br></b></center>",
            "<html><center><b>No.Kode<br>Lokasi</br></b></center>",
            "<html><center><b>Merk/<br>Type</br></b></center>",
            "<html><center><b>Dokumen<br>Kepemilikan</br></b></center>",
            "<html><center><b>Tahun Beli/<br>Pembelian</br></b></center>",
            "<html><center><b>Harga<br>Perolehan</br></b></center>",
            "<html><center><b>Keadaan Barang<br>(RB,R,KB,B,SB)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<DeleteDraftItems> deleteDraftItemss = new ArrayList<DeleteDraftItems>();

        public DeleteDraftItemsTableModel() {
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
            } else if (columnIndex == 8) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 7) {
                return Integer.class;
            } else if (columnIndex == 3 || columnIndex == 4
                    || columnIndex == 9) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<DeleteDraftItems> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            deleteDraftItemss.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(DeleteDraftItems deleteDraftItems) {
            insertRow(getRowCount(), deleteDraftItems);
        }

        public void insertRow(int row, DeleteDraftItems deleteDraftItems) {
            deleteDraftItemss.add(row, deleteDraftItems);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(DeleteDraftItems oldDeleteDraftItems, DeleteDraftItems newDeleteDraftItems) {
            int index = deleteDraftItemss.indexOf(oldDeleteDraftItems);
            deleteDraftItemss.set(index, newDeleteDraftItems);
            fireTableRowsUpdated(index, index);
        }

        public void remove(DeleteDraftItems deleteDraftItems) {
            int row = deleteDraftItemss.indexOf(deleteDraftItems);
            deleteDraftItemss.remove(deleteDraftItems);
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
                deleteDraftItemss.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                deleteDraftItemss.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            deleteDraftItemss.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (deleteDraftItemss.get(i) == null) {
                    deleteDraftItemss.set(i, new DeleteDraftItems());
                }
            }
        }

        public ArrayList<DeleteDraftItems> getDeleteDraftItemss() {
            return deleteDraftItemss;
        }

        @Override
        public int getRowCount() {
            return deleteDraftItemss.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public DeleteDraftItems getDeleteDraftItems(int row) {
            if (!deleteDraftItemss.isEmpty()) {
                return deleteDraftItemss.get(row);
            }
            return new DeleteDraftItems();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            DeleteDraftItems deleteDraftItems = getDeleteDraftItems(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        deleteDraftItems.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    if (aValue instanceof DeleteDraftItems) {
                        deleteDraftItems = (DeleteDraftItems) aValue;
                    }
                    break;
                case 3:
                    deleteDraftItems.setItemCode((String) aValue);
                    break;
                case 4:
                    deleteDraftItems.setLocationCode((String) aValue);
                    break;
                case 5:
                    deleteDraftItems.setSpecificationType((String) aValue);
                    break;
                case 6:
                    deleteDraftItems.setOwnedDocument((String) aValue);
                    break;
                case 7:
                    if (aValue instanceof Integer) {
                        deleteDraftItems.setAcquisitionYear((Integer) aValue);
                    }
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        deleteDraftItems.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    deleteDraftItems.setCondition((String) aValue);
                    break;
                case 10:
                    deleteDraftItems.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            DeleteDraftItems deleteDraftItems = getDeleteDraftItems(row);
            switch (column) {
                case 0:
                    toBeDisplayed = deleteDraftItems.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = deleteDraftItems;
                    break;
                case 3:
                    toBeDisplayed = deleteDraftItems.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = deleteDraftItems.getLocationCode();
                    break;
                case 5:
                    toBeDisplayed = deleteDraftItems.getSpecificationType();
                    break;
                case 6:
                    toBeDisplayed = deleteDraftItems.getOwnedDocument();
                    break;
                case 7:
                    toBeDisplayed = deleteDraftItems.getAcquisitionYear();
                    break;
                case 8:
                    toBeDisplayed = deleteDraftItems.getPrice();
                    break;
                case 9:
                    toBeDisplayed = deleteDraftItems.getCondition();
                    break;
                case 10:
                    toBeDisplayed = deleteDraftItems.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadDeleteDraftItems extends SwingWorker<DeleteDraftItemsTableModel, DeleteDraftItems> {

        private DeleteDraftItemsTableModel model;
        private Exception exception;
        private String modifier;

        public LoadDeleteDraftItems(String modifier) {
            this.model = (DeleteDraftItemsTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<DeleteDraftItems> chunks) {
            mainframe.stopInActiveListener();
            for (DeleteDraftItems deleteDraftItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Usulan Barang Yang Akan Dihapus " + deleteDraftItems.getItemName());
                model.add(deleteDraftItems);
            }
        }

        @Override
        protected DeleteDraftItemsTableModel doInBackground() throws Exception {
            try {
                ArrayList<DeleteDraftItems> deleteDraftItemss = logic.getDeleteDraftItems(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!deleteDraftItemss.isEmpty()) {
                    for (int i = 0; i < deleteDraftItemss.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / deleteDraftItemss.size();

                        DeleteDraftItems deleteDraftItems = deleteDraftItemss.get(i);

                        setProgress((int) progress);
                        publish(deleteDraftItems);
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
                JXErrorPane.showDialog(DeleteDraftItemsPanel.this, info);
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

    private class ImportDeleteDraftItems extends SwingWorker<DeleteDraftItemsTableModel, DeleteDraftItems> {

        private DeleteDraftItemsTableModel model;
        private Exception exception;
        private List<DeleteDraftItems> data;

        public ImportDeleteDraftItems(List<DeleteDraftItems> data) {
            this.model = (DeleteDraftItemsTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<DeleteDraftItems> chunks) {
            mainframe.stopInActiveListener();
            for (DeleteDraftItems deleteDraftItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload Penerimaan Barang Dari Pihak Ketiga Barang " + deleteDraftItems.getItemName());
                model.add(deleteDraftItems);
            }
        }

        @Override
        protected DeleteDraftItemsTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        DeleteDraftItems deleteDraftItems = data.get(i);

                        synchronized (deleteDraftItems) {
                            deleteDraftItems = logic.insertDeleteDraftItems(mainframe.getSession(), deleteDraftItems);
                        }


                        setProgress((int) progress);
                        publish(deleteDraftItems);
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
                JXErrorPane.showDialog(DeleteDraftItemsPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
