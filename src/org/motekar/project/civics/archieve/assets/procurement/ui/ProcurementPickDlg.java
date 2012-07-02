package org.motekar.project.civics.archieve.assets.procurement.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.objects.Procurement;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.FilterCardPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterValue;
import org.motekar.util.user.objects.UserGroup;

/**
 *
 * @author Muhamad Wibawa
 */
public class ProcurementPickDlg {

    private JFrame frame;
    private JDialog dlg;
    private ProcurementBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private Long session = null;
    //
    private ProcurementTable table = new ProcurementTable();
    //
    private JXButton btOK = new JXButton("Pilih");
    private JXButton btCancel = new JXButton("Batal");
    //
    private LoadProcurement worker;
    private ProgressListener progressListener;
    private JProgressBar pbar = new JProgressBar();
    //
    private int response = JOptionPane.NO_OPTION;
    private Procurement selectedProcurement;
    //
    private TableRowSorter<TableModel> sorter;
    private JTextField fieldSearch = new JTextField();

    public ProcurementPickDlg(JFrame frame, Connection conn, Long session) {
        this.frame = frame;
        this.session = session;
        this.logic = new ProcurementBusinessLogic(conn);
        this.mLogic = new AssetMasterBusinessLogic(conn);
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Pengadaan Barang");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(900, 650));
        dlg.setModal(true);
        dlg.setResizable(false);


        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    public int getResponse() {
        return response;
    }

    public Procurement getSelectedProcurement() {
        return selectedProcurement;
    }

    private Component construct() {

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        btOK.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                response = JOptionPane.YES_OPTION;
                ProcurementPickDlg.this.dlg.dispose();
            }
        });

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<Procurement> procurements = table.getSelectedProcurements();
                    if (!procurements.isEmpty()) {
                        if (procurements.size() == 1) {
                            btOK.setEnabled(true);
                            selectedProcurement = procurements.get(0);
                        } else {
                            btOK.setEnabled(false);
                            selectedProcurement = null;
                        }
                    } else {
                        btOK.setEnabled(false);
                        selectedProcurement = null;
                    }
                }
            }
        });

        btCancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ProcurementPickDlg.this.dlg.dispose();
            }
        });

        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        JXPanel panelCenter = new JXPanel();
        panelCenter.setLayout(new BorderLayout());
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCenter.add(createTablePanel(), BorderLayout.CENTER);
        panelCenter.add(pbar, BorderLayout.SOUTH);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createFilterPanel(), BorderLayout.NORTH);
        panel.add(panelCenter, BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        pbar.setVisible(false);
        checkLogin();

        btOK.setEnabled(false);

        return panel;

    }

    private void checkLogin() {
        UserGroup userGroup = ((ArchieveMainframe) frame).getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("");
        } else {
            Unit unit = ((ArchieveMainframe) frame).getUnit();
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

    public void filter() {
        String text = fieldSearch.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(Pattern.compile("(?i).*" + text + ".*",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL).toString()));
        }
    }

    private JPanel createFilterPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,50px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1, 2));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return builder.getPanel();
    }

    private JXPanel createTablePanel() {

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scPane, BorderLayout.CENTER);

        return panel;
    }

    private Component createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    private ArrayList<FilterValue> createFilterValues() {
        ArrayList<FilterValue> filterValues = new ArrayList<FilterValue>();

        filterValues.add(new FilterValue("Nama Barang", "itemname", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Kode Barang", "itemcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Register", "regnumber", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class ProcurementTable extends JXTable {

        private ProcurementTableModel model;

        public ProcurementTable() {
            model = new ProcurementTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>SPK/Perjanjian/Kontrak</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            ColumnGroup group2 = new ColumnGroup("<html><center><b>DPA/SPM/Kwitansi</br></b></center>");
            group2.add(colModel.getColumn(5));
            group2.add(colModel.getColumn(6));

            ColumnGroup group3 = new ColumnGroup("<html><center><b>Jumlah</b></center>");
            group3.add(colModel.getColumn(7));
            group3.add(colModel.getColumn(8));
            group3.add(colModel.getColumn(9));

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

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadProcurement(modifier);
            progressListener = new ProgressListener(pbar, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public Procurement getSelectedProcurement() {
            ArrayList<Procurement> selectedProcurements = getSelectedProcurements();
            return selectedProcurements.get(0);
        }

        public ArrayList<Procurement> getProcurements() {
            return model.getProcurements();
        }

        public ArrayList<Procurement> getSelectedProcurements() {

            ArrayList<Procurement> procurements = new ArrayList<Procurement>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                Procurement procurement = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof Procurement) {
                            procurement = (Procurement) obj;
                            procurements.add(procurement);
                        }
                    }
                }
            }

            return procurements;
        }

        public void updateSelectedProcurement(Procurement procurement) {
            model.updateRow(getSelectedProcurement(), procurement);
        }

        public void removeProcurement(ArrayList<Procurement> procurements) {
            if (!procurements.isEmpty()) {
                for (Procurement procurement : procurements) {
                    model.remove(procurement);
                }
            }

        }

        public void addProcurement(ArrayList<Procurement> procurements) {
            if (!procurements.isEmpty()) {
                for (Procurement procurement : procurements) {
                    model.add(procurement);
                }
            }
        }

        public void addProcurement(Procurement procurement) {
            model.add(procurement);
        }

        public void insertEmptyProcurement() {
            addProcurement(new Procurement());
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

    private static class ProcurementTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 12;
        private static final String[] COLUMNS = {"", "<html><b>No</b>",
            "<html><b>Jenis Barang<br>Yang Dibeli</br></b>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><b>Tanggal</b>", "<html><b>Nomor</b>",
            "<html><center><b>Banyaknya<br>Barang</br></b></center>",
            "<html><center><b>Harga<br>Satuan</br></b></center>",
            "<html><center><b>Jumlah<br>Harga</br></b></center>",
            "<html><center><b>Dipergunakan<br>Pada Unit</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<Procurement> procurements = new ArrayList<Procurement>();

        public ProcurementTableModel() {
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
            } else if (columnIndex == 8 || columnIndex == 9) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 7) {
                return Integer.class;
            } else if (columnIndex == 3 || columnIndex == 5) {
                return Date.class;
            } else if (columnIndex == 4 || columnIndex == 6) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<Procurement> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            procurements.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(Procurement procurement) {
            insertRow(getRowCount(), procurement);
        }

        public void insertRow(int row, Procurement procurement) {
            procurements.add(row, procurement);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(Procurement oldProcurement, Procurement newProcurement) {
            int index = procurements.indexOf(oldProcurement);
            procurements.set(index, newProcurement);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Procurement procurement) {
            int row = procurements.indexOf(procurement);
            procurements.remove(procurement);
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
                procurements.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                procurements.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            procurements.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (procurements.get(i) == null) {
                    procurements.set(i, new Procurement());
                }
            }
        }

        public ArrayList<Procurement> getProcurements() {
            return procurements;
        }

        @Override
        public int getRowCount() {
            return procurements.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Procurement getProcurement(int row) {
            if (!procurements.isEmpty()) {
                return procurements.get(row);
            }
            return new Procurement();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Procurement procurement = getProcurement(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        procurement.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    if (aValue instanceof Procurement) {
                        procurement = (Procurement) aValue;
                    }
                    break;
                case 3:
                    if (aValue instanceof Date) {
                        procurement.setContractDate((Date) aValue);
                    }
                    break;
                case 4:
                    procurement.setContractNumber((String) aValue);
                    break;
                case 5:
                    if (aValue instanceof Date) {
                        procurement.setDocumentDate((Date) aValue);
                    }
                    break;
                case 6:
                    procurement.setDocumentNumber((String) aValue);
                    break;
                case 7:
                    if (aValue instanceof Integer) {
                        procurement.setAmount((Integer) aValue);
                    }
                    break;
                case 8:
                    if (aValue instanceof BigDecimal) {
                        procurement.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 9:
                    break;
                case 10:
                    if (aValue instanceof Unit) {
                        procurement.setUnit((Unit) aValue);
                    }
                    break;
                case 11:
                    procurement.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Procurement procurement = getProcurement(row);
            switch (column) {
                case 0:
                    toBeDisplayed = procurement.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = procurement;
                    break;
                case 3:
                    toBeDisplayed = procurement.getContractDate();
                    break;
                case 4:
                    toBeDisplayed = procurement.getContractNumber();
                    break;
                case 5:
                    toBeDisplayed = procurement.getDocumentDate();
                    break;
                case 6:
                    toBeDisplayed = procurement.getDocumentNumber();
                    break;
                case 7:
                    toBeDisplayed = procurement.getAmount();
                    break;
                case 8:
                    toBeDisplayed = procurement.getPrice();
                    break;
                case 9:
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    if (procurement.getPrice() != null) {
                        totalPrice = totalPrice.add(procurement.getPrice());
                    }
                    if (procurement.getAmount() != null) {
                        totalPrice = totalPrice.multiply(BigDecimal.valueOf(procurement.getAmount().doubleValue()));
                    }
                    toBeDisplayed = totalPrice;
                    break;
                case 10:
                    toBeDisplayed = procurement.getUnit();
                    break;
                case 11:
                    toBeDisplayed = procurement.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadProcurement extends SwingWorker<ProcurementTableModel, Procurement> {

        private ProcurementTableModel model;
        private Exception exception;
        private String modifier;

        public LoadProcurement(String modifier) {
            this.model = (ProcurementTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Procurement> chunks) {
            for (Procurement procurement : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.add(procurement);
            }
        }

        @Override
        protected ProcurementTableModel doInBackground() throws Exception {
            try {
                ArrayList<Procurement> procurements = logic.getProcurement(session, modifier);

                double progress = 0.0;
                if (!procurements.isEmpty()) {
                    for (int i = 0; i < procurements.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / procurements.size();

                        Procurement procurement = procurements.get(i);

                        if (procurement.getUnit() != null) {
                            procurement.setUnit(mLogic.getUnitByIndex(session, procurement.getUnit()));
                        }

                        setProgress((int) progress);
                        publish(procurement);
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
                JXErrorPane.showDialog(frame, info);
            }

            table.packAll();
            
            setProgress(100);
        }
    }
}
