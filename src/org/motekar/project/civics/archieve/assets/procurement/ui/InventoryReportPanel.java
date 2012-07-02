package org.motekar.project.civics.archieve.assets.procurement.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.TableColumnExt;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.objects.ApprovalBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.InventoryReport;
import org.motekar.project.civics.archieve.assets.procurement.objects.ReleaseBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.InventoryReportJasper;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.expedition.ui.ApprovalDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryReportPanel extends JXPanel implements PrintButtonListener {

    private ArchieveMainframe mainframe;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.LEFT);
    //
    private JXHyperlink linkCommander = new JXHyperlink();
    private JXHyperlink linkCommanderNIP = new JXHyperlink();
    //
    private JXHyperlink linkApprovalDatePlace = new JXHyperlink();
    private JXHyperlink linkEmployee = new JXHyperlink();
    private JXHyperlink linkEmployeeNIP = new JXHyperlink();
    //
    private InventoryReportTable table = new InventoryReportTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private LoadInventoryReport worker;
    private ProgressListener progressListener;
    private TableRowSorter<TableModel> sorter;
    //
    private Signer commanderSigner = null;
    private Signer employeeSigner = null;
    private Approval approval;
    //
    private JSpinField fieldSemester = new JSpinField(1, 2);
    private JYearChooser fieldYear = new JYearChooser();
    private JFileChooser xlsChooser;

    public InventoryReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ProcurementBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        table.loadData();
    }

    private void construct() {

        constructFileChooser();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPrintPanel.addListener(this);

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

        fieldSemester.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadData();
            }
        });

        fieldYear.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadData();
            }
        });


        fillDefaultSignerLink();

        setLayout(new BorderLayout());
        add(createDataViewPanel(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        
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

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
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

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);


//        JPanel panelSearch = new JPanel();
//        panelSearch.setLayout(new FlowLayout(FlowLayout.LEFT));
//        panelSearch.add(createSearchPanel(), null);
//        panelSearch.add(createSearchPanel2(), null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(btPrintPanel, BorderLayout.CENTER);

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
                "left:pref,10px,left:pref,10px, pref,30px,"
                + "left:pref,10px,left:pref,10px, pref,pref,30px,"
                + "left:pref,10px,left:pref,10px, fill:default:grow,100px",
                "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setColumnGroups(new int[][]{{6, 13}});

        ProfileAccount profile = mainframe.getProfileAccount();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SKPD", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.addLabel(profile.getCompany().toUpperCase().trim(), cc.xy(5, 1));

        builder.addLabel("KAB / KOTA", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.addLabel(profile.getState().toUpperCase().trim(), cc.xy(5, 3));

        builder.addLabel("PROPINSI", cc.xy(7, 1));
        builder.addLabel(":", cc.xy(9, 1));
        builder.addLabel(profile.getProvince().toUpperCase().trim(), cc.xy(11, 1));

        builder.addLabel("SEMESTER", cc.xy(7, 3));
        builder.addLabel(":", cc.xy(9, 3));
        builder.add(fieldSemester, cc.xy(11, 3));
        builder.add(fieldYear, cc.xy(12, 3));

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


            InventoryReportJasper report = new InventoryReportJasper("Laporan Penerimaan dan Pengeluaran Barang", table.getInventoryReports(),
                    mainframe.getProfileAccount(), fieldSemester.getValue(), fieldYear.getYear(),
                    approval, employeeSigner, commanderSigner);

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


                InventoryReportJasper report = new InventoryReportJasper("Laporan Penerimaan dan Pengeluaran Barang", table.getInventoryReports(),
                        mainframe.getProfileAccount(), fieldSemester.getValue(), fieldYear.getYear(),
                        approval, employeeSigner, commanderSigner);

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

    private class InventoryReportTable extends JXTable {

        private InventoryReportTableModel model;

        public InventoryReportTable() {
            model = new InventoryReportTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Dokumen Faktur</b></center>");
            group.add(colModel.getColumn(4));
            group.add(colModel.getColumn(5));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><center><b>Buku Penerimaan B.A/<br>Surat Penerimaan</br></b></center>");
            group2.add(colModel.getColumn(9));
            group2.add(colModel.getColumn(10));

            CustomColumnGroup group3 = new CustomColumnGroup("<html><center><b>Surat/<br>Bon</br></b></center>");
            group3.add(colModel.getColumn(14));
            group3.add(colModel.getColumn(15));


            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
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
                Dimension size = new Dimension(0, 40);

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

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadInventoryReport();
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public InventoryReport getSelectedInventoryReport() {
            ArrayList<InventoryReport> selectedInventoryReports = getSelectedInventoryReports();
            return selectedInventoryReports.get(0);
        }

        public ArrayList<InventoryReport> getInventoryReports() {
            return model.getInventoryReports();
        }

        public ArrayList<InventoryReport> getSelectedInventoryReports() {

            ArrayList<InventoryReport> inventoryReports = new ArrayList<InventoryReport>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                InventoryReport inventoryReport = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof InventoryReport) {
                            inventoryReport = (InventoryReport) obj;
                            inventoryReports.add(inventoryReport);
                        }
                    }
                }
            }

            return inventoryReports;
        }

        public void updateSelectedInventoryReport(InventoryReport inventoryReport) {
            model.updateRow(getSelectedInventoryReport(), inventoryReport);
        }

        public void removeInventoryReport(ArrayList<InventoryReport> inventoryReports) {
            if (!inventoryReports.isEmpty()) {
                for (InventoryReport inventoryReport : inventoryReports) {
                    model.remove(inventoryReport);
                }
            }

        }

        public void addInventoryReport(ArrayList<InventoryReport> inventoryReports) {
            if (!inventoryReports.isEmpty()) {
                for (InventoryReport inventoryReport : inventoryReports) {
                    model.add(inventoryReport);
                }
            }
        }

        public void addInventoryReport(InventoryReport inventoryReport) {
            model.add(inventoryReport);
        }

        public void insertEmptyInventoryReport() {
            addInventoryReport(new InventoryReport());
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

    private static class InventoryReportTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 23;
        private static final String[] COLUMNS = {"<html><b>No</b>",
            "<html><center><b>Terima<br>Tgl</br></b></center>",
            "<html><b>Dari</b>",
            "<html><center><b>Penerimaan SPK/<br>Perjanjian</br><center></b>",
            "<html><b>No</b>", "<html><b>Tgl</b>",
            "<html><center><b>Banyaknya</b></center>",
            "<html><center><b>Nama Barang</b></center>",
            "<html><center><b>Harga<br>Satuan</br></b></center>",
            "<html><b>No</b>", "<html><b>Tgl</b>",
            "<html><b>Ket.</b>",
            //
            "<html><center><b>Nomor<br>Urut</br></b></center>", "<html><center><b>Pengeluaran<br>Tgl</br></b></center>",
            "<html><b>No</b>", "<html><b>Tgl</b>",
            "<html><b>Untuk</b>",
            "<html><center><b>Banyaknya</b></center>",
            "<html><center><b>Nama Barang</b></center>",
            "<html><center><b>Harga<br>Satuan</br></b></center>",
            "<html><center><b>Jumlah<br>Harga</br></b></center>",
            "<html><b>Tanggal Penyerahan</b>",
            "<html><b>Ket.</b>"};
        private ArrayList<InventoryReport> inventoryReports = new ArrayList<InventoryReport>();

        public InventoryReportTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1 || columnIndex == 5 || columnIndex == 10
                    || columnIndex == 13 || columnIndex == 15 || columnIndex == 21) {
                return Date.class;
            } else if (columnIndex == 0 || columnIndex == 6 || columnIndex == 12
                    || columnIndex == 17) {
                return Integer.class;
            } else if (columnIndex == 3 || columnIndex == 4 || columnIndex == 9
                    || columnIndex == 14) {
                return JLabel.class;
            } else if (columnIndex == 8 || columnIndex == 19 || columnIndex == 20) {
                return BigDecimal.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<InventoryReport> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            inventoryReports.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(InventoryReport inventoryReport) {
            insertRow(getRowCount(), inventoryReport);
        }

        public void insertRow(int row, InventoryReport inventoryReport) {
            inventoryReports.add(row, inventoryReport);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(InventoryReport oldInventoryReport, InventoryReport newInventoryReport) {
            int index = inventoryReports.indexOf(oldInventoryReport);
            inventoryReports.set(index, newInventoryReport);
            fireTableRowsUpdated(index, index);
        }

        public void remove(InventoryReport inventoryReport) {
            int row = inventoryReports.indexOf(inventoryReport);
            inventoryReports.remove(inventoryReport);
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
                inventoryReports.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                inventoryReports.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            inventoryReports.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (inventoryReports.get(i) == null) {
                    inventoryReports.set(i, new InventoryReport());
                }
            }
        }

        public ArrayList<InventoryReport> getInventoryReports() {
            return inventoryReports;
        }

        @Override
        public int getRowCount() {
            return inventoryReports.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public InventoryReport getInventoryReport(int row) {
            if (!inventoryReports.isEmpty()) {
                return inventoryReports.get(row);
            }
            return new InventoryReport();
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            InventoryReport inventoryReport = getInventoryReport(row);
            ApprovalBook approvalBook = inventoryReport.getApprovalBook();
            ReleaseBook releaseBook = inventoryReport.getReleaseBook();
            switch (column) {
                case 0:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 1:
                    if (approvalBook.getReceiveDate() == null) {
                        inventoryReport.setInventoryDate(null);
                    }

                    toBeDisplayed = inventoryReport;

                    break;
                case 2:
                    toBeDisplayed = approvalBook.getReceiveFrom();
                    break;
                case 3:
                    toBeDisplayed = approvalBook.getContractNumber();
                    break;
                case 4:
                    toBeDisplayed = approvalBook.getInvoiceNumber();
                    break;
                case 5:
                    toBeDisplayed = approvalBook.getInvoiceDate();
                    break;
                case 6:
                    if (approvalBook.getAmount() != null) {
                        if (approvalBook.getAmount().equals(Integer.valueOf(0))) {
                            Integer val = null;
                            toBeDisplayed = val;
                        } else {
                            toBeDisplayed = approvalBook.getAmount();
                        }
                    } else {
                        toBeDisplayed = approvalBook.getAmount();
                    }
                    break;
                case 7:
                    toBeDisplayed = approvalBook.getItemName();
                    break;
                case 8:
                    if (approvalBook.getPrice() != null) {
                        if (approvalBook.getPrice().equals(BigDecimal.ZERO)) {
                            BigDecimal val = null;
                            toBeDisplayed = val;
                        } else {
                            toBeDisplayed = approvalBook.getPrice();
                        }
                    } else {
                        toBeDisplayed = approvalBook.getPrice();
                    }
                    break;
                case 9:
                    toBeDisplayed = approvalBook.getDocumentNumber();
                    break;
                case 10:
                    toBeDisplayed = approvalBook.getDocumentDate();
                    break;
                case 11:
                    toBeDisplayed = approvalBook.getDescription();
                    break;
                case 12:
                    toBeDisplayed = releaseBook.getSortingNumber();
                    break;
                case 13:
                    toBeDisplayed = releaseBook.getReleaseDate();
                    break;
                case 14:
                    toBeDisplayed = releaseBook.getBillNumber();
                    break;
                case 15:
                    toBeDisplayed = releaseBook.getBillDate();
                    break;
                case 16:
                    toBeDisplayed = releaseBook.getAllotment();
                    break;
                case 17:
                    if (releaseBook.getAmount() != null) {
                        if (releaseBook.getAmount().equals(Integer.valueOf(0))) {
                            Integer val = null;
                            toBeDisplayed = val;
                        } else {
                            toBeDisplayed = releaseBook.getAmount();
                        }
                    } else {
                        toBeDisplayed = releaseBook.getAmount();
                    }
                    break;
                case 18:
                    toBeDisplayed = releaseBook.getItemName();
                    break;
                case 19:
                    if (releaseBook.getPrice() != null) {
                        if (releaseBook.getPrice().equals(BigDecimal.ZERO)) {
                            BigDecimal val = null;
                            toBeDisplayed = val;
                        } else {
                            toBeDisplayed = releaseBook.getPrice();
                        }
                    } else {
                        toBeDisplayed = releaseBook.getPrice();
                    }
                    break;
                case 20:
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    if (releaseBook.getPrice() != null) {
                        totalPrice = totalPrice.add(releaseBook.getPrice());
                    }
                    if (releaseBook.getAmount() != null) {
                        totalPrice = totalPrice.multiply(BigDecimal.valueOf(releaseBook.getAmount().doubleValue()));
                    }


                    if (totalPrice.equals(BigDecimal.valueOf(0.0))) {
                        BigDecimal val = null;
                        toBeDisplayed = val;
                    } else {
                        toBeDisplayed = totalPrice;
                    }

                    break;
                case 21:
                    toBeDisplayed = releaseBook.getDelegateDate();
                    break;
                case 22:
                    toBeDisplayed = releaseBook.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadInventoryReport extends SwingWorker<InventoryReportTableModel, InventoryReport> {

        private InventoryReportTableModel model;
        private Exception exception;

        public LoadInventoryReport() {
            this.model = (InventoryReportTableModel) table.getModel();
            this.model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<InventoryReport> chunks) {
            mainframe.stopInActiveListener();
            for (InventoryReport inventoryReport : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kartu Persediaan Barang");
                model.add(inventoryReport);
            }
        }

        private String generateModifier() {
            StringBuilder query = new StringBuilder();

            int semester = fieldSemester.getValue();
            int year = fieldYear.getYear();


            UserGroup userGroup = mainframe.getUserGroup();

            if (userGroup.getGroupName().equals("Administrator")) {
                if (semester == 1) {
                    query.append(" where (date_part('month',receivedate) between 1 and 6) ").
                            append(" and date_part('year',receivedate) = ").append(year);
                } else {
                    query.append(" where (date_part('month',receivedate) between 7 and 12) ").
                            append(" and date_part('year',receivedate) = ").append(year);
                }
            } else {

                Unit unit = mainframe.getUnit();

                if (semester == 1) {
                    query.append(" where (date_part('month',receivedate) between 1 and 6) ").
                            append(" and date_part('year',receivedate) = ").append(year).
                            append(" and warehouse = ").append(unit.getIndex());
                } else {
                    query.append(" where (date_part('month',receivedate) between 7 and 12) ").
                            append(" and date_part('year',receivedate) = ").append(year).
                            append(" and warehouse = ").append(unit.getIndex());
                }
            }



            return query.toString();
        }

        private String generateModifier2() {
            StringBuilder query = new StringBuilder();

            int semester = fieldSemester.getValue();
            int year = fieldYear.getYear();

            UserGroup userGroup = mainframe.getUserGroup();

            if (userGroup.getGroupName().equals("Administrator")) {
                if (semester == 1) {
                    query.append(" where (date_part('month',releasedate) between 1 and 6) ").
                            append(" and date_part('year',releasedate) = ").append(year);
                } else {
                    query.append(" where (date_part('month',releasedate) between 7 and 12) ").
                            append(" and date_part('year',releasedate) = ").append(year);
                }
            } else {
                
                Unit unit = mainframe.getUnit();
                
                if (semester == 1) {
                    query.append(" where (date_part('month',releasedate) between 1 and 6) ").
                            append(" and date_part('year',releasedate) = ").append(year).
                            append(" and warehouse = ").append(unit.getIndex());
                } else {
                    query.append(" where (date_part('month',releasedate) between 7 and 12) ").
                            append(" and date_part('year',releasedate) = ").append(year).
                            append(" and warehouse = ").append(unit.getIndex());
                }
            }



            return query.toString();
        }

        @Override
        protected InventoryReportTableModel doInBackground() throws Exception {
            try {
                ArrayList<ApprovalBook> approvalBooks = logic.getApprovalBook(mainframe.getSession(), generateModifier());
                ArrayList<ReleaseBook> releaseBooks = logic.getReleaseBook(mainframe.getSession(), generateModifier2());

                ArrayList<InventoryReport> inventoryReports = InventoryReport.createInventoryReport(approvalBooks, releaseBooks);

                double progress = 0.0;
                if (!inventoryReports.isEmpty()) {
                    for (int i = 0; i < inventoryReports.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / inventoryReports.size();

                        InventoryReport inventoryReport = inventoryReports.get(i);


                        setProgress((int) progress);
                        publish(inventoryReport);
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
                JXErrorPane.showDialog(InventoryReportPanel.this, info);
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
}
