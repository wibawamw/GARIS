package org.motekar.project.civics.archieve.assets.inventory.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
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
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryBook;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemStatus;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.report.ItemStatusJasper;
import org.motekar.project.civics.archieve.assets.procurement.ui.SignerDlg;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsStatusPanel extends JXPanel implements PrintButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
//    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(false, false, false, false, false, FlowLayout.LEFT);
    //
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.LEFT);
    //
    private ItemsStatusTable table = new ItemsStatusTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private LoadInventoryBook worker;
    private ProgressListener progressListener;
    private TableRowSorter<TableModel> sorter;
    //
    private JXHyperlink linkGorvernor = new JXHyperlink();
    private JXTextField fieldNumber = new JXTextField();
    private JXDatePicker fieldDate = new JXDatePicker();
    //
    private Signer governorSigner = null;
    //
    private JFileChooser xlsChooser;

    public ItemsStatusPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("", "");
        } else {
            Unit unit = mainframe.getUnit();
            String modifier = generateUnitModifier(unit);
            table.loadData(modifier, modifier);
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

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);


        btPrintPanel.addListener(this);

        linkGorvernor.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                chooseGorvernorSigner();
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

    private void chooseGorvernorSigner() {
        SignerDlg dlg = new SignerDlg(mainframe, mainframe.getConnection(), mainframe.getSession(), governorSigner, Signer.TYPE_GOVERNOR);
        dlg.showDialog();
        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
            governorSigner = dlg.getSigner();
            linkGorvernor.setText(governorSigner.getSignerName());
        }
    }

    private void fillDefaultSignerLink() {
        linkGorvernor.setText("(..........................................)");
    }

    private Component createSignerPanel() {
        FormLayout lm = new FormLayout(
                "5px,pref,50px,fill:default:grow,50px,pref,5px",
                "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(createGorvernorSignerPanel(), cc.xy(6, 1));

        return builder.getPanel();
    }

    private Component createGorvernorSignerPanel() {
        FormLayout lm = new FormLayout(
                "center:default:grow,5px",
                "pref,5px,pref,5px,pref,2px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        ProfileAccount profileAccount = mainframe.getProfileAccount();
        StringBuilder gorvernorname = new StringBuilder();


        if (profileAccount != null) {
            if (profileAccount.getStateType().equals(ProfileAccount.KABUPATEN)) {
                gorvernorname.append("BUPATI ").
                        append(profileAccount.getState().toUpperCase());
            } else {
                gorvernorname.append("WALIKOTA ").
                        append(profileAccount.getState().toUpperCase());
            }
        }

        CellConstraints cc = new CellConstraints();

        builder.addLabel(gorvernorname.toString(), cc.xy(1, 1));

        builder.add(linkGorvernor, cc.xy(1, 7));

        return builder.getPanel();
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private JXPanel createTablePanel() {

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JXTitledPanel titledPanel = new JXTitledPanel("Data View");

        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.add(createSearchPanel(), BorderLayout.NORTH);
        panelNorth.add(btPrintPanel, BorderLayout.CENTER);


        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panelNorth, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        collapasepanel.add(createSignerPanel(), BorderLayout.SOUTH);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,10px, fill:default:grow,30px,pref,10px,pref,10px,fill:default:grow,350px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        CellConstraints cc = new CellConstraints();

        builder.addLabel("NOMOR", cc.xy(1, 1));
        builder.addLabel(":", cc.xy(3, 1));
        builder.add(fieldNumber, cc.xy(5, 1));

        builder.addLabel("TANGGAL", cc.xy(1, 3));
        builder.addLabel(":", cc.xy(3, 3));
        builder.add(fieldDate, cc.xy(5, 3));


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

            String number = fieldNumber.getText();
            Date date = fieldDate.getDate();

            StringBuilder errorString = new StringBuilder();

            if (governorSigner == null) {
                errorString.append("<br>- Pilih Penandatangan</br>");
            }

            if (number.equals("")) {
                errorString.append("<br>- Isi Nomor</br>");
            }

            if (date == null) {
                errorString.append("<br>- Isi Tanggal</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }


            ItemStatusJasper report = new ItemStatusJasper("Laporan Status Barang", table.getItemStatuses(),
                    governorSigner, mainframe.getProfileAccount(), number, date);

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

            String number = fieldNumber.getText();
            Date date = fieldDate.getDate();

            StringBuilder errorString = new StringBuilder();

            if (governorSigner == null) {
                errorString.append("<br>- Pilih Penandatangan</br>");
            }

            if (number.equals("")) {
                errorString.append("<br>- Isi Nomor</br>");
            }

            if (date == null) {
                errorString.append("<br>- Isi Tanggal</br>");
            }

            if (errorString.length() > 0) {
                throw new MotekarException("<html>Harap diperhatikan hal berikut : " + errorString.toString() + "</html>");
            }

            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";


                ItemStatusJasper report = new ItemStatusJasper("Laporan Status Barang", table.getItemStatuses(),
                        governorSigner, mainframe.getProfileAccount(), number, date);

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
        //
    }

    private class ItemsStatusTable extends JXTable {

        private ItemsStatusTableModel model;

        public ItemsStatusTable() {
            model = new ItemsStatusTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Keadaan Barang</b></center>");
            group.add(colModel.getColumn(10));
            group.add(colModel.getColumn(11));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void loadData(String modifier, String customModifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadInventoryBook(modifier, customModifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public Object getSelectedObject() {
            ArrayList<Object> selectedObjects = getSelectedObjects();
            return selectedObjects.get(0);
        }

        public ArrayList<Object> getInventoryBooks() {
            return model.getObjects();
        }

        public ArrayList<ItemStatus> getItemStatuses() {

            ArrayList<Object> objects = model.getObjects();

            ArrayList<ItemStatus> itemStatuses = new ArrayList<ItemStatus>();

            if (!objects.isEmpty()) {
                for (Object book : objects) {

                    Condition condition = null;

                    ItemStatus itemStatus = new ItemStatus();

                    if (book instanceof InventoryBook) {
                        itemStatus.setItemCode(((InventoryBook) book).getItemCode());
                        itemStatus.setItemName(((InventoryBook) book).getItemName());
                    }

                    itemStatus.setAmount(Integer.valueOf(1));

                    if (book instanceof ItemsLand) {
                        itemStatus.setMachineNumber(((ItemsLand) book).getLandCertificateNumber());
                        itemStatus.setMeasure(((ItemsLand) book).getWide().intValue());
                        itemStatus.setAcquisitionYear(((ItemsLand) book).getAcquisitionYear());
                        itemStatus.setPrice(((ItemsLand) book).getLandPrice());

                    } else if (book instanceof ItemsMachine) {

                        itemStatus.setMachineType(((ItemsMachine) book).getMachineType());
                        itemStatus.setMachineNumber(((ItemsMachine) book).getMachineNumber());
                        itemStatus.setMeasure(((ItemsMachine) book).getVolume());
                        itemStatus.setMaterial(((ItemsMachine) book).getMaterial());
                        itemStatus.setAcquisitionYear(((ItemsMachine) book).getAcquisitionYear());
                        itemStatus.setPrice(((ItemsMachine) book).getPrice());

                        condition = ((ItemsMachine) book).getCondition();

                        itemStatus.setDescription(((ItemsMachine) book).getDescription());

                    } else if (book instanceof ItemsBuilding) {
                        itemStatus.setMachineNumber(((ItemsBuilding) book).getDocumentNumber());
                        itemStatus.setMeasure(((ItemsBuilding) book).getWide().intValue());
                        itemStatus.setAcquisitionYear(((ItemsBuilding) book).getAcquisitionYear());
                        itemStatus.setPrice(((ItemsBuilding) book).getPrice());

                        condition = ((ItemsBuilding) book).getCondition();

                        itemStatus.setDescription(((ItemsBuilding) book).getDescription());
                    } else if (book instanceof ItemsNetwork) {
                        itemStatus.setMachineNumber(((ItemsNetwork) book).getDocumentNumber());
                        itemStatus.setMeasure(((ItemsNetwork) book).getWide().intValue());
                        itemStatus.setAcquisitionYear(null);
                        itemStatus.setPrice(((ItemsNetwork) book).getPrice());

                        condition = ((ItemsNetwork) book).getCondition();

                        itemStatus.setDescription(((ItemsNetwork) book).getDescription());
                    } else if (book instanceof ItemsFixedAsset) {
                        itemStatus.setMachineNumber("");
                        itemStatus.setMaterial(((ItemsFixedAsset) book).getArtMaterial());
                        itemStatus.setAcquisitionYear(((ItemsFixedAsset) book).getAcquisitionYear());
                        itemStatus.setPrice(((ItemsFixedAsset) book).getPrice());

                        condition = ((ItemsFixedAsset) book).getCondition();

                        itemStatus.setDescription(((ItemsFixedAsset) book).getDescription());
                    } else if (book instanceof ItemsConstruction) {
                        itemStatus.setMachineNumber(((ItemsConstruction) book).getDocumentNumber());
                        itemStatus.setMeasure(((ItemsConstruction) book).getBuildingWide().intValue());
                        itemStatus.setAcquisitionYear(null);
                        itemStatus.setPrice(((ItemsConstruction) book).getPrice());

                        itemStatus.setDescription(((ItemsConstruction) book).getDescription());

                    }

                    if (condition == null) {
                        itemStatus.setGood("");
                        itemStatus.setNotGood("");
                    } else {
                        if (condition.getConditionCode().equals("B") || condition.getConditionCode().equals("SB")) {
                            itemStatus.setGood("\u2713");
                            itemStatus.setNotGood("");
                        } else if (condition.getConditionCode().equals("KB") || condition.getConditionCode().equals("RB")
                                || condition.getConditionCode().equals("R")) {
                            itemStatus.setNotGood("\u2713");
                            itemStatus.setGood("");
                        } else {
                            itemStatus.setGood("");
                            itemStatus.setNotGood("");
                        }
                    }

                    itemStatuses.add(itemStatus);
                }
            }

            return itemStatuses;
        }

        public ArrayList<Object> getSelectedObjects() {

            ArrayList<Object> objects = new ArrayList<Object>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                Object object = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof InventoryBook) {
                            object = (InventoryBook) obj;
                            objects.add(object);
                        }
                    }
                }
            }

            return objects;
        }

        public void updateSelectedObject(Object object) {
            model.updateRow(getSelectedObject(), object);
        }

        public void removeObject(ArrayList<Object> objects) {
            if (!objects.isEmpty()) {
                for (Object object : objects) {
                    model.remove(object);
                }
            }

        }

        public void add(ArrayList<Object> objects) {
            if (!objects.isEmpty()) {
                for (Object object : objects) {
                    model.add(object);
                }
            }
        }

        public void add(Object object) {
            model.add(object);
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

    private static class ItemsStatusTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 13;
        private static final String[] COLUMNS = {"<html><b>No Urut</b>",
            "<html><center><b>Nama Barang /<br>Jenis Barang</br></b></center>",
            "<html><b>Merk / Model</b>",
            "<html><center><b>No.Seri<br>Pabrik</br></b></center>",
            "<html><b>Ukuran</b>",
            "<html><b>Bahan</b>",
            "<html><center><b>Tahun Pembuatan /<br>Pembelian</br></b></center>",
            "<html><center><b>No.Kode<br>Barang</br></b></center>",
            "<html><center><b>Jumlah Barang / <br>Register</br></b></center>",
            "<html><center><b>Harga<br>Perolehan</br></b></center>",
            "<html><b>Baik(B)</b>",
            "<html><center><b>Kurang Baik<br>(KB)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<Object> books = new ArrayList<Object>();

        public ItemsStatusTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 10 || columnIndex == 11) {
                return Boolean.class;
            } else if (columnIndex == 9) {
                return BigDecimal.class;
            } else if (columnIndex == 0 || columnIndex == 4 || columnIndex == 6 || columnIndex == 8) {
                return Integer.class;
            } else if (columnIndex == 2 || columnIndex == 3 || columnIndex == 7) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<? extends InventoryBook> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            books.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(Object object) {
            insertRow(getRowCount(), object);
        }

        public void insertRow(int row, Object object) {
            books.add(row, object);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(Object oldObject, Object newObject) {
            int index = books.indexOf(oldObject);
            books.set(index, newObject);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Object object) {
            int row = books.indexOf(object);
            books.remove(object);
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
                books.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                books.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            books.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (books.get(i) == null) {
                    books.set(i, new InventoryBook());
                }
            }
        }

        public ArrayList<Object> getObjects() {
            return books;
        }

        @Override
        public int getRowCount() {
            return books.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Object getObject(int row) {
            if (!books.isEmpty()) {
                return books.get(row);
            }
            return new InventoryBook();
        }

        private NumberFormat createNumberFormat() {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(0);
            format.setGroupingUsed(false);
            format.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
            return format;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Object object = getObject(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        ((InventoryBook) object).setSelected((Boolean) aValue);
                    }

                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Object book = getObject(row);
            switch (column) {
                case 0:
                    toBeDisplayed = Integer.valueOf(row + 1);

                    break;
                case 1:
                    toBeDisplayed = book;
                    break;
                case 2:
                    if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getMachineType();
                    } else {
                        toBeDisplayed = "";
                    }
                    break;
                case 3:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = ((ItemsLand) book).getLandCertificateNumber();
                    } else if (book instanceof ItemsMachine) {

                        StringBuilder builder = new StringBuilder();

                        ItemsMachine machine = (ItemsMachine) book;

                        if (machine.getMachineNumber() != null) {
                            if (!machine.getMachineNumber().equals("")) {
                                builder.append(machine.getMachineNumber());
                            }
                        }

                        if (machine.getFabricNumber() != null) {
                            if (!machine.getFabricNumber().equals("")) {
                                if (builder.length() > 0) {
                                    builder.append(" / ").append(machine.getFabricNumber());
                                } else {
                                    builder.append(machine.getFabricNumber());
                                }
                            }
                        }

                        toBeDisplayed = builder.toString();

                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = ((ItemsBuilding) book).getDocumentNumber();
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = ((ItemsNetwork) book).getDocumentNumber();
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = ((ItemsConstruction) book).getDocumentNumber();
                    }
                    break;
                case 4:
                    NumberFormat format = createNumberFormat();

                    if (book instanceof ItemsLand) {
                        toBeDisplayed = format.format(((ItemsLand) book).getWide());
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = format.format(((ItemsMachine) book).getVolume());
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = format.format(((ItemsBuilding) book).getWide());
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = format.format(((ItemsNetwork) book).getWide());

                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getCattleSize();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = format.format(((ItemsConstruction) book).getBuildingWide());
                    }
                    break;
                case 5:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getMaterial();
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getArtMaterial();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = "";
                    }

                    break;
                case 6:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = ((ItemsLand) book).getAcquisitionYear();
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getAcquisitionYear();
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = ((ItemsBuilding) book).getAcquisitionYear();
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getAcquisitionYear();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = "";
                    }
                    break;
                case 7:
                    if (book instanceof InventoryBook) {
                        toBeDisplayed = ((InventoryBook) book).getItemCode();
                    }

                    break;
                case 8:
                    toBeDisplayed = Integer.valueOf(1);
                    break;
                case 9:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = ((ItemsLand) book).getLandPrice();
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getPrice();
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = ((ItemsBuilding) book).getPrice();
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = ((ItemsNetwork) book).getPrice();
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getPrice();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = ((ItemsConstruction) book).getPrice();
                    }
                    break;
                case 10:

                    Condition condition = null;

                    if (book instanceof ItemsLand) {
                    } else if (book instanceof ItemsMachine) {
                        condition = ((ItemsMachine) book).getCondition();
                    } else if (book instanceof ItemsBuilding) {
                        condition = ((ItemsBuilding) book).getCondition();
                    } else if (book instanceof ItemsNetwork) {
                        condition = ((ItemsNetwork) book).getCondition();
                    } else if (book instanceof ItemsFixedAsset) {
                        condition = ((ItemsFixedAsset) book).getCondition();
                    } else if (book instanceof ItemsConstruction) {
                    }

                    if (condition == null) {
                        toBeDisplayed = Boolean.FALSE;
                    } else {
                        if (condition.getConditionCode().equals("B") || condition.getConditionCode().equals("SB")) {
                            toBeDisplayed = Boolean.TRUE;
                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    }

                    break;
                case 11:

                    Condition condition2 = null;

                    if (book instanceof ItemsLand) {
                    } else if (book instanceof ItemsMachine) {
                        condition2 = ((ItemsMachine) book).getCondition();
                    } else if (book instanceof ItemsBuilding) {
                        condition2 = ((ItemsBuilding) book).getCondition();
                    } else if (book instanceof ItemsNetwork) {
                        condition2 = ((ItemsNetwork) book).getCondition();
                    } else if (book instanceof ItemsFixedAsset) {
                        condition2 = ((ItemsFixedAsset) book).getCondition();
                    } else if (book instanceof ItemsConstruction) {
                    }

                    if (condition2 == null) {
                        toBeDisplayed = Boolean.FALSE;
                    } else {
                        if (condition2.getConditionCode().equals("KB") || condition2.getConditionCode().equals("RB")
                                || condition2.getConditionCode().equals("R")) {
                            toBeDisplayed = Boolean.TRUE;
                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    }

                    break;
                case 12:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getDescription();
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = ((ItemsBuilding) book).getDescription();
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = ((ItemsNetwork) book).getDescription();
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getDescription();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = ((ItemsConstruction) book).getDescription();
                    }

                    break;

            }
            return toBeDisplayed;
        }
    }

    private class LoadInventoryBook extends SwingWorker<ItemsStatusTableModel, Object> {

        private ItemsStatusTableModel model;
        private Exception exception;
        private String modifier = "";
        private String customModifier = "";

        public LoadInventoryBook(String modifier, String customModifier) {
            this.model = (ItemsStatusTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
            this.customModifier = customModifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Object> chunks) {
            mainframe.stopInActiveListener();
            for (Object object : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Status Penggunaan Barang " + object.toString());
                model.add(object);
            }
        }

        private String getCompleteItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = null;

            if (!iLookUp.isEmpty()) {
                int size = iLookUp.size();
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < size; i++) {
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

        @Override
        protected ItemsStatusTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsLand> lands = logic.getItemsLand(mainframe.getSession(), modifier);
                ArrayList<ItemsMachine> machines = logic.getItemsMachine(mainframe.getSession(), customModifier);
                ArrayList<ItemsBuilding> buildings = logic.getItemsBuilding(mainframe.getSession(), modifier);
                ArrayList<ItemsNetwork> networks = logic.getItemsNetwork(mainframe.getSession(), modifier);
                ArrayList<ItemsFixedAsset> fixedAssets = logic.getItemsFixedAsset(mainframe.getSession(), customModifier);
                ArrayList<ItemsConstruction> constructions = logic.getItemsConstruction(mainframe.getSession(), modifier);

                ArrayList<Object> objects = new ArrayList<Object>();
                objects.addAll(lands);
                objects.addAll(machines);
                objects.addAll(buildings);
                objects.addAll(networks);
                objects.addAll(fixedAssets);
                objects.addAll(constructions);

                double progress = 0.0;
                if (!objects.isEmpty()) {
                    for (int i = 0; i < objects.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / objects.size();

                        Object book = objects.get(i);

                        Condition condition = null;
                        Unit unit = null;
                        Region urban = null;
                        Region subUrban = null;

                        ItemsCategory category = new ItemsCategory();


                        if (book instanceof ItemsLand) {
                            unit = ((ItemsLand) book).getUnit();
                            urban = ((ItemsLand) book).getUrban();
                            subUrban = ((ItemsLand) book).getSubUrban();

                            if (unit != null) {
                                ((ItemsLand) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (urban != null) {
                                ((ItemsLand) book).setUrban(mLogic.getRegion(mainframe.getSession(), urban));
                            }

                            if (subUrban != null) {
                                ((ItemsLand) book).setSubUrban(mLogic.getRegion(mainframe.getSession(), subUrban));
                            }

                            category.setCategoryCode(((ItemsLand) book).getItemCode());
                            category.setCategoryName(((ItemsLand) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_LAND);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsLand) book).setItemName(getCompleteItemsName(iLookUp));


                        } else if (book instanceof ItemsMachine) {
                            condition = ((ItemsMachine) book).getCondition();
                            unit = ((ItemsMachine) book).getUnit();

                            if (unit != null) {
                                ((ItemsMachine) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (condition != null) {
                                ((ItemsMachine) book).setCondition(mLogic.getCondition(mainframe.getSession(), condition));
                            }

                            category.setCategoryCode(((ItemsMachine) book).getItemCode());
                            category.setCategoryName(((ItemsMachine) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_MACHINE);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsMachine) book).setItemName(getCompleteItemsName(iLookUp));

                        } else if (book instanceof ItemsBuilding) {
                            condition = ((ItemsBuilding) book).getCondition();
                            unit = ((ItemsBuilding) book).getUnit();
                            urban = ((ItemsBuilding) book).getUrban();
                            subUrban = ((ItemsBuilding) book).getSubUrban();

                            if (unit != null) {
                                ((ItemsBuilding) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (condition != null) {
                                ((ItemsBuilding) book).setCondition(mLogic.getCondition(mainframe.getSession(), condition));
                            }

                            if (urban != null) {
                                ((ItemsBuilding) book).setUrban(mLogic.getRegion(mainframe.getSession(), urban));
                            }

                            if (subUrban != null) {
                                ((ItemsBuilding) book).setSubUrban(mLogic.getRegion(mainframe.getSession(), subUrban));
                            }

                            category.setCategoryCode(((ItemsBuilding) book).getItemCode());
                            category.setCategoryName(((ItemsBuilding) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_BUILDING);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsBuilding) book).setItemName(getCompleteItemsName(iLookUp));

                        } else if (book instanceof ItemsNetwork) {
                            condition = ((ItemsNetwork) book).getCondition();
                            unit = ((ItemsNetwork) book).getUnit();
                            urban = ((ItemsNetwork) book).getUrban();
                            subUrban = ((ItemsNetwork) book).getSubUrban();

                            if (unit != null) {
                                ((ItemsNetwork) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (condition != null) {
                                ((ItemsNetwork) book).setCondition(mLogic.getCondition(mainframe.getSession(), condition));
                            }

                            if (urban != null) {
                                ((ItemsNetwork) book).setUrban(mLogic.getRegion(mainframe.getSession(), urban));
                            }

                            if (subUrban != null) {
                                ((ItemsNetwork) book).setSubUrban(mLogic.getRegion(mainframe.getSession(), subUrban));
                            }

                            category.setCategoryCode(((ItemsNetwork) book).getItemCode());
                            category.setCategoryName(((ItemsNetwork) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_NETWORK);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsNetwork) book).setItemName(getCompleteItemsName(iLookUp));

                        } else if (book instanceof ItemsFixedAsset) {
                            condition = ((ItemsFixedAsset) book).getCondition();
                            unit = ((ItemsFixedAsset) book).getUnit();
                            if (unit != null) {
                                ((ItemsFixedAsset) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (condition != null) {
                                ((ItemsFixedAsset) book).setCondition(mLogic.getCondition(mainframe.getSession(), condition));
                            }

                            category.setCategoryCode(((ItemsFixedAsset) book).getItemCode());
                            category.setCategoryName(((ItemsFixedAsset) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsFixedAsset) book).setItemName(getCompleteItemsName(iLookUp));

                        } else if (book instanceof ItemsConstruction) {
                            unit = ((ItemsConstruction) book).getUnit();
                            urban = ((ItemsConstruction) book).getUrban();
                            subUrban = ((ItemsConstruction) book).getSubUrban();
                            if (unit != null) {
                                ((ItemsConstruction) book).setUnit(mLogic.getUnitByIndex(mainframe.getSession(), unit));
                            }

                            if (urban != null) {
                                ((ItemsConstruction) book).setUrban(mLogic.getRegion(mainframe.getSession(), urban));
                            }

                            if (subUrban != null) {
                                ((ItemsConstruction) book).setSubUrban(mLogic.getRegion(mainframe.getSession(), subUrban));
                            }

                            category.setCategoryCode(((ItemsConstruction) book).getItemCode());
                            category.setCategoryName(((ItemsConstruction) book).getItemName());
                            category.setTypes(ItemsCategory.ITEMS_CONSTRUCTION);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            ((ItemsConstruction) book).setItemName(getCompleteItemsName(iLookUp));
                        }


                        setProgress((int) progress);
                        publish(book);
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
                JXErrorPane.showDialog(ItemsStatusPanel.this, info);
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
