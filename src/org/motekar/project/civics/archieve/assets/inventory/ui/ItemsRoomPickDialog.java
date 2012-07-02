package org.motekar.project.civics.archieve.assets.inventory.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.CustomFilterPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterCardPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterValue;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsRoomPickDialog {

    private JFrame frame;
    private JDialog dlg;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private Long session = null;
    //
    private ItemsMachineTable mTable = new ItemsMachineTable();
    private ItemsFixedAssetTable faTable = new ItemsFixedAssetTable();
    private JXComboBox comboItemsType = new JXComboBox();
    //
    private JXPanel cardPanel = new JXPanel();
    private JXButton btOK = new JXButton("Pilih");
    private JXButton btCancel = new JXButton("Batal");
    private CustomFilterPanel filterPanel;
    private JXButton btFilter = new JXButton("Filter");
    //
    private LoadItemsMachine mWorker;
    private LoadItemsFixedAsset faWorker;
    private ProgressListener progressListener;
    private JProgressBar pbar = new JProgressBar();
    //
    private int response = JOptionPane.NO_OPTION;
    private Object selectedItemObject;

    public ItemsRoomPickDialog(JFrame frame, Connection conn, Long session,ArrayList<Unit> unit) {
        this.frame = frame;
        this.session = session;
        this.logic = new InventoryBusinessLogic(conn);
        this.mLogic = new AssetMasterBusinessLogic(conn);
        this.filterPanel = new CustomFilterPanel((ArchieveMainframe) frame, createFilterValues(), false, false);
        this.filterPanel.loadComboUnit(unit);
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Barang");
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

    public Object getSelectedItemObject() {
        return selectedItemObject;
    }
    
    private void loadComboType() {
        ArrayList<String> types = new ArrayList<String>();
        types.add(ItemsCategory.TYPE_ITEMS_MACHINE);
        types.add(ItemsCategory.TYPE_ITEMS_FIXED_ASSET);

        comboItemsType.setModel(new ListComboBoxModel<String>(types));
        AutoCompleteDecorator.decorate(comboItemsType);

    }

    private Component construct() {

        loadComboType();

        comboItemsType.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object object = comboItemsType.getSelectedItem();
                    if (object instanceof String) {
                        btFilter.setEnabled(filterPanel.isFiltered());
                        if (filterPanel.isFiltered()) {
                            try {
                                showCardPanel((String) object,filterPanel.getModifier());
                            } catch (MotekarException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        } else {
                            showCardPanel((String) object,"");
                        }

                    }
                }
            }
        });

        btOK.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                response = JOptionPane.YES_OPTION;
                ItemsRoomPickDialog.this.dlg.dispose();
            }
        });

        mTable.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ItemsMachine> machines = mTable.getSelectedItemsMachines();
                    if (!machines.isEmpty()) {
                        if (machines.size() == 1) {
                            btOK.setEnabled(true);
                            selectedItemObject = machines.get(0);
                        } else {
                            btOK.setEnabled(false);
                            selectedItemObject = null;
                        }
                    } else {
                        btOK.setEnabled(false);
                        selectedItemObject = null;
                    }
                }
            }
        });

        faTable.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<ItemsFixedAsset> fixedAssets = faTable.getSelectedItemsFixedAssets();
                    if (!fixedAssets.isEmpty()) {
                        if (fixedAssets.size() == 1) {
                            btOK.setEnabled(true);
                            selectedItemObject = fixedAssets.get(0);
                        } else {
                            btOK.setEnabled(false);
                            selectedItemObject = null;
                        }
                    } else {
                        btOK.setEnabled(false);
                        selectedItemObject = null;
                    }
                }
            }
        });

        btCancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsRoomPickDialog.this.dlg.dispose();
            }
        });

        filterPanel.addPropertyChangeListener("filtered", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                btFilter.setEnabled(filterPanel.isFiltered());
                if (!filterPanel.isFiltered()) {
                    Object object = comboItemsType.getSelectedItem();
                    if (object instanceof String) {
                        String str = (String) object;
                        if (str.equals(ItemsCategory.TYPE_ITEMS_MACHINE)) {
                            mTable.loadData("");
                        } else if (str.equals(ItemsCategory.TYPE_ITEMS_FIXED_ASSET)) {
                            faTable.loadData("");
                        }
                    }
                }
            }
        });

        btFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String modifier = filterPanel.getModifier();
                    Object object = comboItemsType.getSelectedItem();
                    if (object instanceof String) {
                        String str = (String) object;
                        if (str.equals(ItemsCategory.TYPE_ITEMS_MACHINE)) {
                            mTable.loadData(modifier);
                        } else if (str.equals(ItemsCategory.TYPE_ITEMS_FIXED_ASSET)) {
                            faTable.loadData(modifier);
                        }
                    }

                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(frame, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mTable.addHighlighter(HighlighterFactory.createAlternateStriping());
        mTable.setShowGrid(false, false);

        faTable.addHighlighter(HighlighterFactory.createAlternateStriping());
        faTable.setShowGrid(false, false);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createMachineTablePanel(), "0");
        cardPanel.add(createFixedAssetTablePanel(), "1");

        JXPanel panelCenter = new JXPanel();
        panelCenter.setLayout(new BorderLayout());
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCenter.add(cardPanel, BorderLayout.CENTER);
        panelCenter.add(pbar, BorderLayout.SOUTH);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createFilterPanel(), BorderLayout.NORTH);
        panel.add(panelCenter, BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        pbar.setVisible(false);
        checkLogin();

        btOK.setEnabled(false);
        btFilter.setEnabled(filterPanel.isFiltered());
        
        return panel;

    }
    
    private void checkLogin() {
        UserGroup userGroup = ((ArchieveMainframe)frame).getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            mTable.loadData("");
        } else {
            try {
                Unit unit = ((ArchieveMainframe)frame).getUnit();
                filterPanel.setUnitEnable(false);
                filterPanel.setComboUnit(unit);
                String modifier = filterPanel.getModifier();
                mTable.loadData(modifier);
            } catch (MotekarException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void showCardPanel(String types, String modifier) {
        if (types.equals(ItemsCategory.TYPE_ITEMS_MACHINE)) {
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
            mTable.loadData(modifier);
        } else {
            ((CardLayout) cardPanel.getLayout()).last(cardPanel);
            faTable.loadData(modifier);
        }
        btOK.setEnabled(false);
    }

    private JPanel createFilterPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,50px",
                "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Bidang Barang", cc.xy(1, 1));
        builder.add(comboItemsType, cc.xyw(3, 1, 2));

        builder.add(filterPanel, cc.xyw(1, 3, 4));
        builder.add(btFilter, cc.xyw(1, 5, 1));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return builder.getPanel();
    }

    private JXPanel createMachineTablePanel() {

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(mTable);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scPane, BorderLayout.CENTER);

        return panel;
    }

    private JXPanel createFixedAssetTablePanel() {

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(faTable);

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

    private class ItemsFixedAssetTable extends JXTable {

        private ItemsFixedAssetTableModel model;

        public ItemsFixedAssetTable() {
            model = new ItemsFixedAssetTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            ColumnGroup group2 = new ColumnGroup("<html><b>Buku/Perpustakaan</b>");
            group2.add(colModel.getColumn(5));
            group2.add(colModel.getColumn(6));

            ColumnGroup group3 = new ColumnGroup("<html><center><b>Barang Bercorak Kesenian /<br>Kebudayaan</br></b></center>");
            group3.add(colModel.getColumn(7));
            group3.add(colModel.getColumn(8));
            group3.add(colModel.getColumn(9));

            ColumnGroup group4 = new ColumnGroup("<html><b>Hewan /<br>Ternak</br></b>");
            group4.add(colModel.getColumn(10));
            group4.add(colModel.getColumn(11));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group2);
            header.addColumnGroup(group3);
            header.addColumnGroup(group4);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void loadData(String modifier) {
            if (faWorker != null) {
                faWorker.cancel(true);
            }

            if (mWorker != null) {
                mWorker.cancel(true);
            }

            faWorker = new LoadItemsFixedAsset(modifier);
            progressListener = new ProgressListener(pbar, faWorker, false);
            faWorker.addPropertyChangeListener(progressListener);
            faWorker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsFixedAsset getSelectedItemsFixedAsset() {
            ArrayList<ItemsFixedAsset> selectedItemsFixedAssets = getSelectedItemsFixedAssets();
            return selectedItemsFixedAssets.get(0);
        }

        public ArrayList<ItemsFixedAsset> getItemsFixedAssets() {
            return model.getItemsFixedAssets();
        }

        public ArrayList<ItemsFixedAsset> getSelectedItemsFixedAssets() {

            ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsFixedAsset fixedAsset = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof ItemsFixedAsset) {
                            fixedAsset = (ItemsFixedAsset) obj;
                            fixedAssets.add(fixedAsset);
                        }
                    }
                }
            }

            return fixedAssets;
        }

        public void updateSelectedItemsFixedAsset(ItemsFixedAsset fixedAsset) {
            model.updateRow(getSelectedItemsFixedAsset(), fixedAsset);
        }

        public void removeItemsFixedAsset(ArrayList<ItemsFixedAsset> fixedAssets) {
            if (!fixedAssets.isEmpty()) {
                for (ItemsFixedAsset fixedAsset : fixedAssets) {
                    model.remove(fixedAsset);
                }
            }

        }

        public void addItemsFixedAsset(ArrayList<ItemsFixedAsset> fixedAssets) {
            if (!fixedAssets.isEmpty()) {
                for (ItemsFixedAsset fixedAsset : fixedAssets) {
                    model.add(fixedAsset);
                }
            }
        }

        public void addItemsFixedAsset(ItemsFixedAsset fixedAsset) {
            model.add(fixedAsset);
        }

        public void insertEmptyItemsFixedAsset() {
            addItemsFixedAsset(new ItemsFixedAsset());
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
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setGroupingUsed(false);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsFixedAssetTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 16;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Judul /<br>Pencipta</br></b></center>", "<html><b>Spresifikasi</b>",
            "<html><b>Asal Derah</b>", "<html><b>Pencipta</b>", "<html><b>Bahan</b>",
            "<html><b>Jenis</b>", "<html><b>Ukuran</b>",
            "<html><center><b>Tahun Cetak /<br>Pembelian</br></b></center>",
            "<html><center><b>Asal-Usul /<br>Cara Perolehan</br></b></center>",
            "<html><center><b>Harga<br>(Rp.)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        public ItemsFixedAssetTableModel() {
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
            } else if (columnIndex == 14) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 12) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<ItemsFixedAsset> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            fixedAssets.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsFixedAsset fixedAsset) {
            insertRow(getRowCount(), fixedAsset);
        }

        public void insertRow(int row, ItemsFixedAsset fixedAsset) {
            fixedAssets.add(row, fixedAsset);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsFixedAsset oldItemsFixedAsset, ItemsFixedAsset newItemsFixedAsset) {
            int index = fixedAssets.indexOf(oldItemsFixedAsset);
            fixedAssets.set(index, newItemsFixedAsset);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsFixedAsset fixedAsset) {
            int row = fixedAssets.indexOf(fixedAsset);
            fixedAssets.remove(fixedAsset);
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
                fixedAssets.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                fixedAssets.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            fixedAssets.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (fixedAssets.get(i) == null) {
                    fixedAssets.set(i, new ItemsFixedAsset());
                }
            }
        }

        public ArrayList<ItemsFixedAsset> getItemsFixedAssets() {
            return fixedAssets;
        }

        @Override
        public int getRowCount() {
            return fixedAssets.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsFixedAsset getItemsFixedAsset(int row) {
            if (!fixedAssets.isEmpty()) {
                return fixedAssets.get(row);
            }
            return new ItemsFixedAsset();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsFixedAsset fixedAsset = getItemsFixedAsset(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        fixedAsset.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    if (aValue instanceof ItemsFixedAsset) {
                        fixedAsset = (ItemsFixedAsset) aValue;
                    }
                    break;
                case 3:
                    fixedAsset.setItemCode((String) aValue);
                    break;
                case 4:
                    fixedAsset.setRegNumber((String) aValue);
                    break;
                case 5:
                    fixedAsset.setBookAuthor((String) aValue);
                    break;
                case 6:
                    fixedAsset.setBookSpec((String) aValue);
                    break;
                case 7:
                    fixedAsset.setArtRegion((String) aValue);
                    break;
                case 8:
                    fixedAsset.setArtAuthor((String) aValue);
                    break;
                case 9:
                    fixedAsset.setArtMaterial((String) aValue);
                    break;
                case 10:
                    fixedAsset.setCattleType((String) aValue);
                    break;
                case 11:
                    fixedAsset.setCattleSize((String) aValue);
                    break;
                case 12:
                    if (aValue instanceof Integer) {
                        fixedAsset.setAcquisitionYear((Integer) aValue);
                    }
                    break;
                case 13:
                    fixedAsset.setAcquisitionWay((String) aValue);
                    break;
                case 14:
                    if (aValue instanceof BigDecimal) {
                        fixedAsset.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 15:
                    fixedAsset.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsFixedAsset fixedAsset = getItemsFixedAsset(row);
            switch (column) {
                case 0:
                    toBeDisplayed = fixedAsset.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = fixedAsset;
                    break;
                case 3:
                    toBeDisplayed = fixedAsset.getItemCode();
                    break;
                case 4:
                    toBeDisplayed = fixedAsset.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = fixedAsset.getBookAuthor();
                    break;
                case 6:
                    toBeDisplayed = fixedAsset.getBookSpec();
                    break;
                case 7:
                    toBeDisplayed = fixedAsset.getArtRegion();
                    break;
                case 8:
                    toBeDisplayed = fixedAsset.getArtAuthor();
                    break;
                case 9:
                    toBeDisplayed = fixedAsset.getArtMaterial();
                    break;
                case 10:
                    toBeDisplayed = fixedAsset.getCattleType();
                    break;
                case 11:
                    toBeDisplayed = fixedAsset.getCattleSize();
                    break;
                case 12:
                    toBeDisplayed = fixedAsset.getAcquisitionYear();
                    break;
                case 13:
                    toBeDisplayed = fixedAsset.getAcquisitionWay();
                    break;
                case 14:
                    toBeDisplayed = fixedAsset.getPrice();
                    break;
                case 15:
                    toBeDisplayed = fixedAsset.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class ItemsMachineTable extends JXTable {

        private ItemsMachineTableModel model;

        public ItemsMachineTable() {
            model = new ItemsMachineTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(9));
            group.add(colModel.getColumn(10));
            group.add(colModel.getColumn(11));
            group.add(colModel.getColumn(12));
            group.add(colModel.getColumn(13));

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

        public void loadData(String modifier) {

            if (faWorker != null) {
                faWorker.cancel(true);
            }

            if (mWorker != null) {
                mWorker.cancel(true);
            }

            mWorker = new LoadItemsMachine(modifier);
            progressListener = new ProgressListener(pbar, mWorker, false);
            mWorker.addPropertyChangeListener(progressListener);
            mWorker.execute();
        }

        public void clear() {
            model.clear();
        }

        public ItemsMachine getSelectedItemsMachine() {
            ArrayList<ItemsMachine> selectedItemsMachines = getSelectedItemsMachines();
            return selectedItemsMachines.get(0);
        }

        public ArrayList<ItemsMachine> getItemsMachines() {
            return model.getItemsMachines();
        }

        public ArrayList<ItemsMachine> getSelectedItemsMachines() {

            ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                ItemsMachine machine = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof ItemsMachine) {
                            machine = (ItemsMachine) obj;
                            machines.add(machine);
                        }
                    }
                }
            }

            return machines;
        }

        public void updateSelectedItemsMachine(ItemsMachine machine) {
            model.updateRow(getSelectedItemsMachine(), machine);
        }

        public void removeItemsMachine(ArrayList<ItemsMachine> machines) {
            if (!machines.isEmpty()) {
                for (ItemsMachine machine : machines) {
                    model.remove(machine);
                }
            }

        }

        public void addItemsMachine(ArrayList<ItemsMachine> machines) {
            if (!machines.isEmpty()) {
                for (ItemsMachine machine : machines) {
                    model.add(machine);
                }
            }
        }

        public void addItemsMachine(ItemsMachine machine) {
            model.add(machine);
        }

        public void insertEmptyItemsMachine() {
            addItemsMachine(new ItemsMachine());
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
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class ItemsMachineTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 17;
        private static final String[] COLUMN_RELIGION = {"", "<html><b>No Urut</b>",
            "<html><b>Kode Barang</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><center><b>Nomor<br>Register</br></b></center>", "<html><b>Merk/Tipe</b>",
            "<html><center><b>Ukuran<br>(CC)</br></b></center>",
            "<html><b>Bahan</b>",
            "<html><center><b>Tahun<br>Beli</br></b></center>",
            "<html><b>Pabrik</b>", "<html><b>Rangka</b>",
            "<html><b>Mesin</b>", "<html><b>Polisi</b>",
            "<html><b>BPKB</b>", "<html><b>Asal-Usul</b>",
            "<html><center><b>Harga Satuan<br>(Rp.)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        public ItemsMachineTableModel() {
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
            } else if (columnIndex == 15) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 6 || columnIndex == 8) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_RELIGION[column];
        }

        public void add(ArrayList<ItemsMachine> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            machines.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(ItemsMachine machine) {
            insertRow(getRowCount(), machine);
        }

        public void insertRow(int row, ItemsMachine machine) {
            machines.add(row, machine);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(ItemsMachine oldItemsMachine, ItemsMachine newItemsMachine) {
            int index = machines.indexOf(oldItemsMachine);
            machines.set(index, newItemsMachine);
            fireTableRowsUpdated(index, index);
        }

        public void remove(ItemsMachine machine) {
            int row = machines.indexOf(machine);
            machines.remove(machine);
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
                machines.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                machines.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            machines.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (machines.get(i) == null) {
                    machines.set(i, new ItemsMachine());
                }
            }
        }

        public ArrayList<ItemsMachine> getItemsMachines() {
            return machines;
        }

        @Override
        public int getRowCount() {
            return machines.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public ItemsMachine getItemsMachine(int row) {
            if (!machines.isEmpty()) {
                return machines.get(row);
            }
            return new ItemsMachine();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemsMachine machine = getItemsMachine(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        machine.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;
                case 2:
                    machine.setItemCode((String) aValue);
                    break;
                case 3:
                    if (aValue instanceof ItemsMachine) {
                        machine = (ItemsMachine) aValue;
                    }
                    break;
                case 4:
                    machine.setRegNumber((String) aValue);
                    break;
                case 5:
                    machine.setMachineType((String) aValue);
                    break;
                case 6:
                    machine.setVolume((Integer) aValue);
                    break;
                case 7:
                    machine.setMaterial((String) aValue);
                    break;
                case 8:
                    machine.setAcquisitionYear((Integer) aValue);
                    break;
                case 9:
                    machine.setFactoryNumber((String) aValue);
                    break;
                case 10:
                    machine.setFabricNumber((String) aValue);
                    break;
                case 11:
                    machine.setMachineNumber((String) aValue);
                    break;
                case 12:
                    machine.setPoliceNumber((String) aValue);
                    break;
                case 13:
                    machine.setBpkbNumber((String) aValue);
                    break;
                case 14:
                    machine.setAcquisitionWay((String) aValue);
                    break;
                case 15:
                    if (aValue instanceof BigDecimal) {
                        machine.setPrice((BigDecimal) aValue);
                    }
                    break;
                case 16:
                    machine.setDescription((String) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            ItemsMachine machine = getItemsMachine(row);
            switch (column) {
                case 0:
                    toBeDisplayed = machine.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = machine.getItemCode();
                    break;
                case 3:
                    toBeDisplayed = machine;
                    break;
                case 4:
                    toBeDisplayed = machine.getRegNumber();
                    break;
                case 5:
                    toBeDisplayed = machine.getMachineType();
                    break;
                case 6:
                    toBeDisplayed = machine.getVolume();
                    break;
                case 7:
                    toBeDisplayed = machine.getMaterial();
                    break;
                case 8:
                    toBeDisplayed = machine.getAcquisitionYear();
                    break;
                case 9:
                    toBeDisplayed = machine.getFactoryNumber();
                    break;
                case 10:
                    toBeDisplayed = machine.getFabricNumber();
                    break;
                case 11:
                    toBeDisplayed = machine.getMachineNumber();
                    break;
                case 12:
                    toBeDisplayed = machine.getPoliceNumber();
                    break;
                case 13:
                    toBeDisplayed = machine.getBpkbNumber();
                    break;
                case 14:
                    toBeDisplayed = machine.getAcquisitionWay();
                    break;
                case 15:
                    toBeDisplayed = machine.getPrice();
                    break;
                case 16:
                    toBeDisplayed = machine.getDescription();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadItemsMachine extends SwingWorker<ItemsMachineTableModel, ItemsMachine> {

        private ItemsMachineTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsMachine(String modifier) {
            this.model = (ItemsMachineTableModel) mTable.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsMachine> chunks) {
            for (ItemsMachine machine : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.add(machine);
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
        protected ItemsMachineTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsMachine> machines = logic.getItemsMachineNotInRoomInventory(session, modifier);

                double progress = 0.0;
                if (!machines.isEmpty()) {
                    for (int i = 0; i < machines.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / machines.size();

                        ItemsMachine machine = machines.get(i);

                        if (machine.getUnit() != null) {
                            machine.setUnit(mLogic.getUnitByIndex(session, machine.getUnit()));
                        }

                        if (machine.getCondition() != null) {
                            machine.setCondition(mLogic.getCondition(session, machine.getCondition()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(machine.getItemCode());
                        category.setCategoryName(machine.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_MACHINE);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(session, category);
                        machine.setItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(machine);
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

            mTable.packAll();

            setProgress(100);
        }
    }

    private class LoadItemsFixedAsset extends SwingWorker<ItemsFixedAssetTableModel, ItemsFixedAsset> {

        private ItemsFixedAssetTableModel model;
        private Exception exception;
        private String modifier;

        public LoadItemsFixedAsset(String modifier) {
            this.model = (ItemsFixedAssetTableModel) faTable.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsFixedAsset> chunks) {
            for (ItemsFixedAsset fixedAsset : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.add(fixedAsset);
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
        protected ItemsFixedAssetTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsFixedAsset> fixedAssets = logic.getItemsFixedAssetNotInRoomInventory(session, modifier);

                double progress = 0.0;
                if (!fixedAssets.isEmpty()) {
                    for (int i = 0; i < fixedAssets.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / fixedAssets.size();

                        ItemsFixedAsset fixedAsset = fixedAssets.get(i);

                        if (fixedAsset.getUnit() != null) {
                            fixedAsset.setUnit(mLogic.getUnitByIndex(session, fixedAsset.getUnit()));
                        }

                        if (fixedAsset.getCondition() != null) {
                            fixedAsset.setCondition(mLogic.getCondition(session, fixedAsset.getCondition()));
                        }

                        ItemsCategory category = new ItemsCategory();
                        category.setCategoryCode(fixedAsset.getItemCode());
                        category.setCategoryName(fixedAsset.getItemName());
                        category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                        category.setStyled(true);

                        ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(session, category);
                        fixedAsset.setItemName(getCompleteItemsName(iLookUp));

                        setProgress((int) progress);
                        publish(fixedAsset);
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

            faTable.packAll();

            setProgress(100);
        }
    }
}
