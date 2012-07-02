package org.motekar.project.civics.archieve.assets.procurement.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
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
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
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
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.TableColumnExt;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.ui.ItemsCategoryChooserDlg;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.objects.UnrecycledItems;
import org.motekar.project.civics.archieve.assets.procurement.report.UnrecycledItemsJasper;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
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
public class UnrecycledItemsPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    //
    private JXDatePicker fieldReceiveDate = new JXDatePicker();
    private JXTextField fieldItems = new JXTextField();
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldSpecification = new JXTextField();
    private JYearChooser fieldProductionYear = new JYearChooser();
    private JSpinField fieldReceiveAmount = new JSpinField();
    private JXDatePicker fieldContractDate = new JXDatePicker();
    private JXTextField fieldContractNumber = new JXTextField();
    private JXDatePicker fieldDocumentDate = new JXDatePicker();
    private JXTextField fieldDocumentNumber = new JXTextField();
    private JXDatePicker fieldReleaseDate = new JXDatePicker();
    private JXTextField fieldSubmitted = new JXTextField();
    private JSpinField fieldReleaseAmount = new JSpinField();
    private JXDatePicker fieldReleaseDocumentDate = new JXDatePicker();
    private JXTextField fieldReleseDocumentNumber = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private UnrecycledItemsTable table = new UnrecycledItemsTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadUnrecycledItems worker;
    private ProgressListener progressListener;
    private UnrecycledItems selectedUnrecycledItems = null;
    private TableRowSorter<TableModel> sorter;
    //
    private ImportUnrecycledItems importWorker;
    private ItemsCategory selectedItemsCategory = null;
    private ItemsCategory parentCategory = null;
    //
    private Signer commanderSigner = null;
    private Signer employeeSigner = null;
    private Approval approval;
    private Unit unit = null;
    //
    private JFileChooser xlsChooser;
    //
    private ArrayList<ItemsCategory> iLookUp = new ArrayList<ItemsCategory>();

    public UnrecycledItemsPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ProcurementBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("");
        } else {
            unit = mainframe.getUnit();
            String modifier = generateUnitModifier(unit);
            table.loadData(modifier);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    private void construct() {

        constructFileChooser();
        loadParentCategory();

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

        fieldReceiveDate.setFormats("dd/MM/yyyy");
        fieldContractDate.setFormats("dd/MM/yyyy");
        fieldDocumentDate.setFormats("dd/MM/yyyy");
        fieldReleaseDate.setFormats("dd/MM/yyyy");
        fieldReleaseDocumentDate.setFormats("dd/MM/yyyy");

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<UnrecycledItems> unrecycledItemss = table.getSelectedUnrecycledItemss();
                    if (!unrecycledItemss.isEmpty()) {
                        if (unrecycledItemss.size() == 1) {
                            selectedUnrecycledItems = unrecycledItemss.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedUnrecycledItems = null;
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
                        null, parentCategory, true);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemsCategory = dlg.getSelectedItemsCategory();
                    iLookUp = dlg.getSelectedItemsCategorys();
                    fieldItems.setText(selectedItemsCategory.getCategoryName());
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
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
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

        JXLabel itemLabel = new JXLabel("Nama / Jenis Barang");
        itemLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        JXLabel receviceDateLabel = new JXLabel("Tanggal Diterima");
        receviceDateLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        receviceDateLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);


        builder.add(receviceDateLabel, cc.xy(2, 6));
        builder.add(fieldReceiveDate, cc.xyw(4, 6, 3));

        builder.add(itemLabel, cc.xy(2, 8));
        builder.add(createStrip(1.0, 1.0), cc.xyw(4, 8, 1));
        builder.add(fieldItems, cc.xyw(6, 8, 3));

        builder.addLabel("Merk/Ukuran", cc.xy(2, 10));
        builder.add(fieldSpecification, cc.xyw(4, 10, 5));

        builder.addLabel("Tahun Pembuatan", cc.xy(2, 12));
        builder.add(fieldProductionYear, cc.xyw(4, 12, 3));

        builder.addLabel("Jumlah / Satuan", cc.xy(2, 14));
        builder.add(fieldReceiveAmount, cc.xyw(4, 14, 3));

        builder.addSeparator("SPK/Perjanjian/Kontrak", cc.xyw(2, 16, 7));

        builder.addLabel("Tanggal", cc.xy(2, 18));
        builder.add(fieldContractDate, cc.xyw(4, 18, 3));

        builder.addLabel("Nomor", cc.xy(2, 20));
        builder.add(fieldContractNumber, cc.xyw(4, 20, 5));

        builder.addSeparator("Berita Acara Pemeriksaan", cc.xyw(2, 22, 7));

        builder.addLabel("Tanggal", cc.xy(2, 24));
        builder.add(fieldDocumentDate, cc.xyw(4, 24, 3));

        builder.addLabel("Nomor", cc.xy(2, 26));
        builder.add(fieldDocumentNumber, cc.xyw(4, 26, 5));

        builder.addLabel("Tanggal Dikeluarkan", cc.xy(2, 28));
        builder.add(fieldReleaseDate, cc.xyw(4, 28, 3));

        builder.addLabel("Diserahkan Kepada", cc.xy(2, 30));
        builder.add(fieldSubmitted, cc.xyw(4, 30, 5));

        builder.addLabel("Jumlah Satuan / Barang", cc.xy(2, 32));
        builder.add(fieldReleaseAmount, cc.xyw(4, 32, 3));

        builder.addSeparator("Surat Penyerahan", cc.xyw(2, 34, 7));

        builder.addLabel("Tanggal", cc.xy(2, 36));
        builder.add(fieldReleaseDocumentDate, cc.xyw(4, 36, 3));

        builder.addLabel("Nomor", cc.xy(2, 38));
        builder.add(fieldReleseDocumentNumber, cc.xyw(4, 38, 5));

        builder.addLabel("Keterangan", cc.xy(2, 40));
        builder.add(scPane, cc.xywh(4, 40, 5, 2));

        builder.addSeparator("", cc.xyw(1, 43, 9));
        builder.add(btSavePanelBottom, cc.xyw(1, 44, 9));

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
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("ATASAN LANGSUNG", cc.xy(1, 1));

        builder.add(linkCommander, cc.xy(1, 5));
        builder.addSeparator("", cc.xyw(1, 7, 2));
        builder.add(linkCommanderNIP, cc.xy(1, 9));

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
        fieldProductionYear.setYear(year);
    }

    private void clearForm() {
        fieldReceiveDate.setDate(null);
        fieldItems.setText("");
        fieldSpecification.setText("");
        clearYear();
        fieldReceiveAmount.setValue(Integer.valueOf(0));
        fieldContractDate.setDate(null);
        fieldContractNumber.setText("");
        fieldDocumentDate.setDate(null);
        fieldDocumentNumber.setText("");
        fieldReleaseDate.setDate(null);
        fieldSubmitted.setText("");
        fieldReleaseAmount.setValue(Integer.valueOf(0));
        fieldReleaseDocumentDate.setDate(null);
        fieldReleseDocumentNumber.setText("");
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedUnrecycledItems != null) {
            try {
                fieldReceiveDate.setDate(selectedUnrecycledItems.getReceiveDate());

                selectedItemsCategory = new ItemsCategory();
                selectedItemsCategory.setCategoryCode(selectedUnrecycledItems.getItemCode());
                selectedItemsCategory.setCategoryName(selectedUnrecycledItems.getItemName());
                if (selectedUnrecycledItems.getItemCode().startsWith("01")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_LAND);
                } else if (selectedUnrecycledItems.getItemCode().startsWith("02")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_MACHINE);
                } else if (selectedUnrecycledItems.getItemCode().startsWith("03")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_BUILDING);
                } else if (selectedUnrecycledItems.getItemCode().startsWith("04")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_NETWORK);
                } else if (selectedUnrecycledItems.getItemCode().startsWith("05")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                } else if (selectedUnrecycledItems.getItemCode().startsWith("06")) {
                    selectedItemsCategory.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                }

                iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), selectedItemsCategory);

                fieldItems.setText(getCompleteItemsName());

                fieldSpecification.setText(selectedUnrecycledItems.getSpecification());
                fieldProductionYear.setYear(selectedUnrecycledItems.getProductionYear());
                fieldReceiveAmount.setValue(selectedUnrecycledItems.getReceiveAmount());

                fieldSubmitted.setText(selectedUnrecycledItems.getSubmitted());

                fieldContractDate.setDate(selectedUnrecycledItems.getContractDate());
                fieldContractNumber.setText(selectedUnrecycledItems.getContractNumber());
                fieldDocumentDate.setDate(selectedUnrecycledItems.getDocumentDate());
                fieldDocumentNumber.setText(selectedUnrecycledItems.getDocumentNumber());
                fieldReleaseDate.setDate(selectedUnrecycledItems.getReleaseDate());
                fieldReleaseAmount.setValue(selectedUnrecycledItems.getReleaseAmount());
                fieldReleaseDocumentDate.setDate(selectedUnrecycledItems.getReleaseDocumentDate());
                fieldReleseDocumentNumber.setText(selectedUnrecycledItems.getReleaseDocumentNumber());

                fieldDescription.setText(selectedUnrecycledItems.getDescription());
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private UnrecycledItems getUnrecycledItems() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Date receiveDate = fieldReceiveDate.getDate();

        String itemName = getLastItemsName();
        String specification = fieldSpecification.getText();
        Integer productionYear = fieldProductionYear.getYear();
        Integer receiveAmount = fieldReceiveAmount.getValue();
        String contractNumber = fieldContractNumber.getText();
        Date contractDate = fieldContractDate.getDate();
        String documentNumber = fieldDocumentNumber.getText();
        Date documentDate = fieldDocumentDate.getDate();

        Date releaseDate = fieldReleaseDate.getDate();
        String submitted = fieldSubmitted.getText();
        Integer releaseAmount = fieldReleaseAmount.getValue();

        String releaseDocumentNumber = fieldReleseDocumentNumber.getText();
        Date releaseDocumentDate = fieldReleaseDocumentDate.getDate();

        String description = fieldDescription.getText();


        if (receiveDate == null) {
            errorString.append("<br>- Tanggal Diterima</br>");
        }

        if (itemName.equals("")) {
            errorString.append("<br>- Nama / Jenis Barang</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        UnrecycledItems unrecycledItems = new UnrecycledItems();
        unrecycledItems.setReceiveDate(receiveDate);
        unrecycledItems.setItemCode(selectedItemsCategory.getCategoryCode());
        unrecycledItems.setItemName(itemName);
        unrecycledItems.setSpecification(specification);
        unrecycledItems.setProductionYear(productionYear);
        unrecycledItems.setReceiveAmount(receiveAmount);
        unrecycledItems.setContractDate(contractDate);
        unrecycledItems.setContractNumber(contractNumber);
        unrecycledItems.setDocumentDate(documentDate);
        unrecycledItems.setDocumentNumber(documentNumber);
        unrecycledItems.setReleaseDate(releaseDate);
        unrecycledItems.setSubmitted(submitted);
        unrecycledItems.setReleaseAmount(releaseAmount);
        unrecycledItems.setReleaseDocumentDate(releaseDocumentDate);
        unrecycledItems.setReleaseDocumentNumber(releaseDocumentNumber);
        unrecycledItems.setDescription(description);

        if (selectedUnrecycledItems != null) {
            unrecycledItems.setUnit(selectedUnrecycledItems.getUnit());
        } else {
            unrecycledItems.setUnit(unit);
        }


        return unrecycledItems;
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldReceiveDate.getEditor().requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        fieldReceiveDate.getEditor().requestFocus();
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
                ArrayList<UnrecycledItems> selectedUnrecycledItemss = table.getSelectedUnrecycledItemss();
                if (!selectedUnrecycledItemss.isEmpty()) {
                    logic.deleteUnrecycledItems(mainframe.getSession(), selectedUnrecycledItemss);
                    table.removeUnrecycledItems(selectedUnrecycledItemss);
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
            UnrecycledItems newUnrecycledItems = getUnrecycledItems();
            if (btPanel.isNewstate()) {
                newUnrecycledItems = logic.insertUnrecycledItems(mainframe.getSession(), newUnrecycledItems);
                table.addUnrecycledItems(newUnrecycledItems);
                selectedUnrecycledItems = newUnrecycledItems;
                selectedUnrecycledItems.setItemName(getLastItemsName());
                selectedUnrecycledItems.setFullItemName(getCompleteItemsName());
                table.packAll();
            } else if (btPanel.isEditstate()) {
                newUnrecycledItems = logic.updateUnrecycledItems(mainframe.getSession(), selectedUnrecycledItems, newUnrecycledItems);
                table.updateSelectedUnrecycledItems(newUnrecycledItems);
                selectedUnrecycledItems = newUnrecycledItems;
                selectedUnrecycledItems.setItemName(getLastItemsName());
                selectedUnrecycledItems.setFullItemName(getCompleteItemsName());
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
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
            }

            if (approval == null) {
                errorString.append("<br>- Pilih Tempat dan Tanggal Pengesahan</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }


            UnrecycledItemsJasper report = new UnrecycledItemsJasper("Buku Barang Habis Pakai", table.getUnrecycledItemss(),
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
                errorString.append("<br>- Pilih Penyimpan Barang</br>");
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


                UnrecycledItemsJasper report = new UnrecycledItemsJasper("Buku Barang Habis Pakai", table.getUnrecycledItemss(),
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

    private class UnrecycledItemsTable extends JXTable {

        private UnrecycledItemsTableModel model;

        public UnrecycledItemsTable() {
            model = new UnrecycledItemsTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Berita Acara<br>Pemeriksaan</br></b></center>");
            group.add(colModel.getColumn(8));
            group.add(colModel.getColumn(9));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><center><b>PENERIMAAN</b></center>");
            group2.add(colModel.getColumn(2));
            group2.add(colModel.getColumn(3));
            group2.add(colModel.getColumn(4));
            group2.add(colModel.getColumn(5));
            group2.add(colModel.getColumn(6));
            group2.add(colModel.getColumn(7));
            group2.add(group);

            CustomColumnGroup group3 = new CustomColumnGroup("<html><center><b>PENGELUARAN</b></center>");
            group3.add(colModel.getColumn(10));
            group3.add(colModel.getColumn(11));
            group3.add(colModel.getColumn(12));
            group3.add(colModel.getColumn(13));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group2);
            header.addColumnGroup(group3);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
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

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadUnrecycledItems(modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public UnrecycledItems getSelectedUnrecycledItems() {
            ArrayList<UnrecycledItems> selectedUnrecycledItemss = getSelectedUnrecycledItemss();
            return selectedUnrecycledItemss.get(0);
        }

        public ArrayList<UnrecycledItems> getUnrecycledItemss() {
            return model.getUnrecycledItemss();
        }

        public ArrayList<UnrecycledItems> getSelectedUnrecycledItemss() {

            ArrayList<UnrecycledItems> unrecycledItemss = new ArrayList<UnrecycledItems>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                UnrecycledItems unrecycledItems = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof UnrecycledItems) {
                            unrecycledItems = (UnrecycledItems) obj;
                            unrecycledItemss.add(unrecycledItems);
                        }
                    }
                }
            }

            return unrecycledItemss;
        }

        public void updateSelectedUnrecycledItems(UnrecycledItems unrecycledItems) {
            model.updateRow(getSelectedUnrecycledItems(), unrecycledItems);
        }

        public void removeUnrecycledItems(ArrayList<UnrecycledItems> unrecycledItemss) {
            if (!unrecycledItemss.isEmpty()) {
                for (UnrecycledItems unrecycledItems : unrecycledItemss) {
                    model.remove(unrecycledItems);
                }
            }

        }

        public void addUnrecycledItems(ArrayList<UnrecycledItems> unrecycledItemss) {
            if (!unrecycledItemss.isEmpty()) {
                for (UnrecycledItems unrecycledItems : unrecycledItemss) {
                    model.add(unrecycledItems);
                }
            }
        }

        public void addUnrecycledItems(UnrecycledItems unrecycledItems) {
            model.add(unrecycledItems);
        }

        public void insertEmptyUnrecycledItems() {
            addUnrecycledItems(new UnrecycledItems());
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

    private static class UnrecycledItemsTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 15;
        private static final String[] COLUMNS = {"",
            "<html><center><b>No</b></center>",
            "<html><center><b>Tanggal<br>Diterima</br></b></center>",
            "<html><center><b>Nama/Jenis<br>Barang</br></b></center>",
            "<html><center><b>Merk/<br>Ukuran</br></b></center>",
            "<html><center><b>Tahun<br>Pembuatan</br></b></center>",
            "<html><center><b>Jumlah/<br>Satuan</br></b></center>",
            "<html><center><b>Tgl/No.Kontrak/<br>SP/SPK</br></b></center>",
            "<html><center><b>Tanggal</b></center>",
            "<html><center><b>Nomor</b></center>",
            "<html><center><b>Tanggal<br>Dikeluarkan</br></b></center>",
            "<html><center><b>Diserahkan<br>Kepada</br></b></center>",
            "<html><center><b>Jumlah<br>Satuan/Barang</br></b></center>",
            "<html><center><b>Tgl/No.<br>Surat Penyerahan</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<UnrecycledItems> unrecycledItemss = new ArrayList<UnrecycledItems>();

        public UnrecycledItemsTableModel() {
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
            } else if (columnIndex == 1 || columnIndex == 5 || columnIndex == 6
                    || columnIndex == 12) {
                return Integer.class;
            } else if (columnIndex == 4 || columnIndex == 7
                    || columnIndex == 9 || columnIndex == 11
                    || columnIndex == 13) {
                return JLabel.class;
            } else if (columnIndex == 2 || columnIndex == 8 || columnIndex == 10) {
                return Date.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<UnrecycledItems> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            unrecycledItemss.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(UnrecycledItems unrecycledItems) {
            insertRow(getRowCount(), unrecycledItems);
        }

        public void insertRow(int row, UnrecycledItems unrecycledItems) {
            unrecycledItemss.add(row, unrecycledItems);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(UnrecycledItems oldUnrecycledItems, UnrecycledItems newUnrecycledItems) {
            int index = unrecycledItemss.indexOf(oldUnrecycledItems);
            unrecycledItemss.set(index, newUnrecycledItems);
            fireTableRowsUpdated(index, index);
        }

        public void remove(UnrecycledItems unrecycledItems) {
            int row = unrecycledItemss.indexOf(unrecycledItems);
            unrecycledItemss.remove(unrecycledItems);
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
                unrecycledItemss.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                unrecycledItemss.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            unrecycledItemss.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (unrecycledItemss.get(i) == null) {
                    unrecycledItemss.set(i, new UnrecycledItems());
                }
            }
        }

        public ArrayList<UnrecycledItems> getUnrecycledItemss() {
            return unrecycledItemss;
        }

        @Override
        public int getRowCount() {
            return unrecycledItemss.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public UnrecycledItems getUnrecycledItems(int row) {
            if (!unrecycledItemss.isEmpty()) {
                return unrecycledItemss.get(row);
            }
            return new UnrecycledItems();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            UnrecycledItems unrecycledItems = getUnrecycledItems(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        unrecycledItems.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    if (aValue instanceof Date) {
                        unrecycledItems.setReceiveDate((Date) aValue);
                    }
                    break;
                case 3:
                    if (aValue instanceof UnrecycledItems) {
                        unrecycledItems = (UnrecycledItems) aValue;
                    }
                    break;
                case 4:
                    unrecycledItems.setSpecification((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof Integer) {
                        unrecycledItems.setProductionYear((Integer) aValue);
                    }
                    break;
                case 6:
                    if (aValue instanceof Integer) {
                        unrecycledItems.setReceiveAmount((Integer) aValue);
                    }
                    break;
                case 7:
                    break;
                case 8:
                    if (aValue instanceof Date) {
                        unrecycledItems.setDocumentDate((Date) aValue);
                    }
                    break;
                case 9:
                    unrecycledItems.setDocumentNumber((String) aValue);
                    break;
                case 10:
                    if (aValue instanceof Date) {
                        unrecycledItems.setReleaseDate((Date) aValue);
                    }
                    break;
                case 11:
                    unrecycledItems.setSubmitted((String) aValue);
                    break;
                case 12:
                    if (aValue instanceof Integer) {
                        unrecycledItems.setReleaseAmount((Integer) aValue);
                    }
                    break;
                case 13:
                    break;
                case 14:
                    unrecycledItems.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            UnrecycledItems unrecycledItems = getUnrecycledItems(row);
            switch (column) {
                case 0:
                    toBeDisplayed = unrecycledItems.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = unrecycledItems.getReceiveDate();
                    break;
                case 3:
                    toBeDisplayed = unrecycledItems;
                    break;
                case 4:
                    toBeDisplayed = unrecycledItems.getSpecification();
                    break;
                case 5:
                    toBeDisplayed = unrecycledItems.getProductionYear();
                    break;
                case 6:
                    toBeDisplayed = unrecycledItems.getReceiveAmount();
                    break;
                case 7:
                    StringBuilder str = new StringBuilder();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                            new Locale("in", "id", "id"));

                    if (unrecycledItems.getContractDate() != null) {
                        str.append(sdf.format(unrecycledItems.getContractDate()));

                    }

                    if (!unrecycledItems.getContractNumber().equals("")) {
                        if (unrecycledItems.getContractDate() != null) {
                            str.append(" -- ");
                            str.append(unrecycledItems.getContractNumber());
                        } else {
                            str.append(unrecycledItems.getContractNumber());
                        }
                    }

                    toBeDisplayed = str.toString();
                    break;
                case 8:
                    toBeDisplayed = unrecycledItems.getDocumentDate();
                    break;
                case 9:
                    toBeDisplayed = unrecycledItems.getDocumentNumber();
                    break;
                case 10:
                    toBeDisplayed = unrecycledItems.getReleaseDate();
                    break;
                case 11:
                    toBeDisplayed = unrecycledItems.getSubmitted();
                    break;
                case 12:
                    toBeDisplayed = unrecycledItems.getReleaseAmount();
                    break;
                case 13:
                    StringBuilder str2 = new StringBuilder();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy",
                            new Locale("in", "id", "id"));

                    if (unrecycledItems.getReleaseDocumentDate() != null) {
                        str2.append(sdf2.format(unrecycledItems.getReleaseDocumentDate()));

                    }

                    if (!unrecycledItems.getReleaseDocumentNumber().equals("")) {
                        if (unrecycledItems.getReleaseDocumentDate() != null) {
                            str2.append(" -- ");
                            str2.append(unrecycledItems.getReleaseDocumentNumber());
                        } else {
                            str2.append(unrecycledItems.getReleaseDocumentNumber());
                        }
                    }

                    toBeDisplayed = str2.toString();
                    break;
                case 14:
                    toBeDisplayed = unrecycledItems.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadUnrecycledItems extends SwingWorker<UnrecycledItemsTableModel, UnrecycledItems> {

        private UnrecycledItemsTableModel model;
        private Exception exception;
        private String modifier;

        public LoadUnrecycledItems(String modifier) {
            this.model = (UnrecycledItemsTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<UnrecycledItems> chunks) {
            mainframe.stopInActiveListener();
            for (UnrecycledItems unrecycledItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Buku Barang Pakai Habis " + unrecycledItems.getItemName());
                model.add(unrecycledItems);
            }
        }

        @Override
        protected UnrecycledItemsTableModel doInBackground() throws Exception {
            try {
                ArrayList<UnrecycledItems> unrecycledItemss = logic.getUnrecycledItems(mainframe.getSession(), modifier);

                double progress = 0.0;
                if (!unrecycledItemss.isEmpty()) {
                    for (int i = 0; i < unrecycledItemss.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / unrecycledItemss.size();

                        UnrecycledItems unrecycledItems = unrecycledItemss.get(i);

                        setProgress((int) progress);
                        publish(unrecycledItems);
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
                JXErrorPane.showDialog(UnrecycledItemsPanel.this, info);
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

    private class ImportUnrecycledItems extends SwingWorker<UnrecycledItemsTableModel, UnrecycledItems> {

        private UnrecycledItemsTableModel model;
        private Exception exception;
        private List<UnrecycledItems> data;

        public ImportUnrecycledItems(List<UnrecycledItems> data) {
            this.model = (UnrecycledItemsTableModel) table.getModel();
            this.data = data;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<UnrecycledItems> chunks) {
            mainframe.stopInActiveListener();
            for (UnrecycledItems unrecycledItems : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Upload Penerimaan Barang Dari Pihak Ketiga Barang " + unrecycledItems.getItemName());
                model.add(unrecycledItems);
            }
        }

        @Override
        protected UnrecycledItemsTableModel doInBackground() throws Exception {
            try {
                double progress = 0.0;
                if (!data.isEmpty()) {
                    for (int i = 0; i < data.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / data.size();

                        UnrecycledItems unrecycledItems = data.get(i);

                        synchronized (unrecycledItems) {
                            unrecycledItems = logic.insertUnrecycledItems(mainframe.getSession(), unrecycledItems);
                        }


                        setProgress((int) progress);
                        publish(unrecycledItems);
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
                JXErrorPane.showDialog(UnrecycledItemsPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
