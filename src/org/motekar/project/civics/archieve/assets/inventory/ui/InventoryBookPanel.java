package org.motekar.project.civics.archieve.assets.inventory.ui;

import ca.odell.glazedlists.GlazedLists;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
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
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryBook;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsBuildingViewPanel;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsConstructionViewPanel;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsFixedAssetViewPanel;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsLandViewPanel;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsMachineViewPanel;
import org.motekar.project.civics.archieve.assets.inventory.ui.view.ItemsNetworkViewPanel;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
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
public class InventoryBookPanel extends JXPanel implements CommonButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(false, false, false, false, false, FlowLayout.LEFT);
    //
    private CustomFilterPanel filterPanel;
    private JXButton btFilter = new JXButton("Reload");
    //
    private JXButton btView = new JXButton("Lihat");
    //
    private ItemsLandViewPanel landView;
    private ItemsMachineViewPanel machineView;
    private ItemsBuildingViewPanel buildingView;
    private ItemsNetworkViewPanel netwokView;
    private ItemsFixedAssetViewPanel fixedAsetView;
    private ItemsConstructionViewPanel constructionView;
    //
    private InventoryBookTable table = new InventoryBookTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadInventoryBook worker;
    private ProgressListener progressListener;
    private TableRowSorter<TableModel> sorter;
    private Object selectedData = null;

    public InventoryBookPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        landView = new ItemsLandViewPanel(mainframe, cardPanel);
        machineView = new ItemsMachineViewPanel(mainframe, cardPanel);
        buildingView = new ItemsBuildingViewPanel(mainframe, cardPanel);
        netwokView = new ItemsNetworkViewPanel(mainframe, cardPanel);
        fixedAsetView = new ItemsFixedAssetViewPanel(mainframe, cardPanel);
        constructionView = new ItemsConstructionViewPanel(mainframe, cardPanel);
        this.filterPanel = new CustomFilterPanel(mainframe, createFilterValues(), true, true);
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (!userGroup.getGroupName().equals("Administrator")) {
            Unit unit = mainframe.getUnit();
            filterPanel.setUnitEnable(false);
            filterPanel.setComboUnit(unit);
            btFilter.setEnabled(true);
        }
    }

    private void construct() {

        loadComboUnit();
        loadComboUrban();
        loadComboSubUrban();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);


        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPanel.addListener(this);
        btPanel.mergeWith(btView);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(landView, "1");
        cardPanel.add(machineView, "2");
        cardPanel.add(buildingView, "3");
        cardPanel.add(netwokView, "4");
        cardPanel.add(fixedAsetView, "5");
        cardPanel.add(constructionView, "6");

        btView.setEnabled(false);

        btFilter.setEnabled(filterPanel.isFiltered());

        btView.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                onView();
            }
        });

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<Object> objects = table.getSelectedObjects();
                    if (!objects.isEmpty()) {
                        if (objects.size() == 1) {
                            selectedData = objects.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                            btView.setEnabled(true);
                        } else {
                            selectedData = null;
                            btPanel.setButtonState(ManipulationButtonPanel.UNEDIT);
                            btView.setEnabled(false);
                        }
                    } else {
                        selectedData = null;
                        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                        btView.setEnabled(false);
                    }
                }
            }
        });

        filterPanel.addPropertyChangeListener("filtered", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                btFilter.setEnabled(filterPanel.isFiltered());
                if (!filterPanel.isFiltered()) {
                    if (worker != null) {
                        worker.cancel(true);
                    }
                    table.clear();
                }
            }
        });

        btFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String modifier = filterPanel.getModifier();
                    String customModifier = filterPanel.getCustomModifier();
                    table.loadData(modifier, customModifier);
                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(InventoryBookPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
    }

    private void loadComboUnit() {
        try {
            SwingWorker<ArrayList<Unit>, Void> lw = new SwingWorker<ArrayList<Unit>, Void>() {

                @Override
                protected ArrayList<Unit> doInBackground() throws Exception {
                    ArrayList<Unit> units = mLogic.getUnit(mainframe.getSession());
                    if (!units.isEmpty()) {
                        for (Unit unit : units) {
                            unit.setHirarchiecal(true);
                        }
                    }
                    return units;
                }
            };
            lw.execute();
            filterPanel.loadComboUnit(lw.get());

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
            filterPanel.loadComboUrban(GlazedLists.eventList(lw.get()));

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboSubUrban() {
        filterPanel.loadComboSubUrban(GlazedLists.eventList(new ArrayList<Region>()));
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private JXPanel createTablePanel() {

        btPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(btPanel, BorderLayout.CENTER);

        JXTitledPanel titledPanel = new JXTitledPanel("Data View");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panel, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,50px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(filterPanel, cc.xyw(1, 1, 3));
        builder.add(btFilter, cc.xyw(1, 3, 1));

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

    @Override
    public void onInsert() throws SQLException, CommonException {
        //
    }

    @Override
    public void onEdit() {
        //
    }

    @Override
    public void onDelete() {
        //
    }

    @Override
    public void onSave() {
        //
    }

    @Override
    public void onCancel() throws SQLException, CommonException {
        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        btPanel.setStateFalse();
    }

    private void onView() {
        if (selectedData != null) {
            if (selectedData instanceof ItemsLand) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "1");
                landView.setFormValues((ItemsLand) selectedData);
            } else if (selectedData instanceof ItemsMachine) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "2");
                machineView.setFormValues((ItemsMachine) selectedData);
            } else if (selectedData instanceof ItemsBuilding) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "3");
                buildingView.setFormValues((ItemsBuilding) selectedData);
            } else if (selectedData instanceof ItemsNetwork) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "4");
                netwokView.setFormValues((ItemsNetwork) selectedData);
            } else if (selectedData instanceof ItemsFixedAsset) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "5");
                fixedAsetView.setFormValues((ItemsFixedAsset) selectedData);
            } else if (selectedData instanceof ItemsConstruction) {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "6");
                constructionView.setFormValues((ItemsConstruction) selectedData);
            }
        }
    }

    private ArrayList<FilterValue> createFilterValues() {
        ArrayList<FilterValue> filterValues = new ArrayList<FilterValue>();

        filterValues.add(new FilterValue("Nama Barang", "itemname", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Kode Barang", "itemcode", FilterCardPanel.STRING_PANEL));
        filterValues.add(new FilterValue("Nomor Register", "regnumber", FilterCardPanel.STRING_PANEL));

        return filterValues;
    }

    private class InventoryBookTable extends JXTable {

        private InventoryBookTableModel model;

        public InventoryBookTable() {
            model = new InventoryBookTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Nomor</b></center>");
            group.add(colModel.getColumn(4));
            group.add(colModel.getColumn(5));
            group.add(colModel.getColumn(6));

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
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class InventoryBookTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 15;
        private static final String[] COLUMNS = {"", "<html><b>No Urut</b>",
            "<html><center><b>Jenis Barang /<br>Nama Barang</br></b></center>",
            "<html><b>Merk / Type</b>",
            "<html><b>Kode Barang</b>", "<html><b>Register</b>",
            "<html><center><b>Sertifikat / Dokumen /<br>Mesin / Rangka</br></b></center>",
            "<html><b>Bahan</b>",
            "<html><center><b>Ukuran / Panjang /<br>Lebar / Luas</br></b></center>",
            "<html><center><b>Letak / Lokasi /<br>Alamat</br></b></center>",
            "<html><center><b>Tahun Perolehan /<br>Cetak / Pembelian</br></b></center>",
            "<html><center><b>Asal-Usul / <br>Cara Perolehan</br></b></center>",
            "<html><center><b>Harga /<br>Nilai Kontrak</br><br>(Rp.)</br></b></center>",
            "<html><b>Kondisi</b>", "<html><b>Keterangan</b>"};
        private ArrayList<Object> books = new ArrayList<Object>();

        public InventoryBookTableModel() {
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
            } else if (columnIndex == 13) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 10 || columnIndex == 12) {
                return Integer.class;
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
                    if (book instanceof InventoryBook) {
                        toBeDisplayed = ((InventoryBook) book).isSelected();
                    }

                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = book;
                    break;
                case 3:
                    if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getMachineType();
                    } else {
                        toBeDisplayed = "";
                    }
                    break;
                case 4:
                    if (book instanceof InventoryBook) {
                        toBeDisplayed = ((InventoryBook) book).getItemCode();
                    }
                    break;
                case 5:
                    if (book instanceof InventoryBook) {
                        toBeDisplayed = ((InventoryBook) book).getRegNumber();
                    }

                    break;
                case 6:
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
                case 7:
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
                case 8:

                    NumberFormat format = createNumberFormat();

                    if (book instanceof ItemsLand) {
                        toBeDisplayed = format.format(((ItemsLand) book).getWide());
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = format.format(((ItemsMachine) book).getVolume());
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = format.format(((ItemsBuilding) book).getWide());
                    } else if (book instanceof ItemsNetwork) {

                        StringBuilder builder = new StringBuilder();

                        ItemsNetwork network = (ItemsNetwork) book;

                        if (network.getLength().compareTo(Integer.valueOf(0)) > 0) {
                            builder.append(format.format(network.getLength()));
                        }

                        if (network.getWidth().compareTo(Integer.valueOf(0)) > 0) {
                            if (builder.length() > 0) {
                                builder.append(" / ").append(format.format(network.getWidth()));
                            } else {
                                builder.append(format.format(network.getWidth()));
                            }
                        }

                        if (network.getWide().compareTo(BigDecimal.ZERO) > 0) {
                            if (builder.length() > 0) {
                                builder.append(" / ").append(format.format(network.getWide()));
                            } else {
                                builder.append(format.format(network.getWide()));
                            }
                        }

                        toBeDisplayed = builder.toString();

                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getCattleSize();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = format.format(((ItemsConstruction) book).getBuildingWide());
                    }

                    break;
                case 9:
                    if (book instanceof ItemsLand) {

                        ItemsLand land = (ItemsLand) book;

                        StringBuilder builder = new StringBuilder();
                        builder.append(land.getAddressLocation());

                        if (land.getUrban() != null) {
                            Region urban = land.getUrban();
                            builder.append(" Kecamatan ").append(urban.getRegionName());
                        }

                        if (land.getSubUrban() != null) {
                            Region subUrban = land.getSubUrban();
                            builder.append(" Kelurahan ").append(subUrban.getRegionName());
                        }

                        toBeDisplayed = builder.toString();

                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsBuilding) {
                        ItemsBuilding building = (ItemsBuilding) book;

                        StringBuilder builder = new StringBuilder();
                        builder.append(building.getAddress());

                        if (building.getUrban() != null) {
                            Region urban = building.getUrban();
                            builder.append(" Kecamatan ").append(urban.getRegionName());
                        }

                        if (building.getSubUrban() != null) {
                            Region subUrban = building.getSubUrban();
                            builder.append(" Kelurahan ").append(subUrban.getRegionName());
                        }

                        toBeDisplayed = builder.toString();

                    } else if (book instanceof ItemsNetwork) {
                        ItemsNetwork network = (ItemsNetwork) book;

                        StringBuilder builder = new StringBuilder();
                        builder.append(network.getAddressLocation());

                        if (network.getUrban() != null) {
                            Region urban = network.getUrban();
                            builder.append(" Kecamatan ").append(urban.getRegionName());
                        }

                        if (network.getSubUrban() != null) {
                            Region subUrban = network.getSubUrban();
                            builder.append(" Kelurahan ").append(subUrban.getRegionName());
                        }

                        toBeDisplayed = builder.toString();

                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsConstruction) {
                        ItemsConstruction construction = (ItemsConstruction) book;

                        StringBuilder builder = new StringBuilder();
                        builder.append(construction.getAddressLocation());

                        if (construction.getUrban() != null) {
                            Region urban = construction.getUrban();
                            builder.append(" Kecamatan ").append(urban.getRegionName());
                        }

                        if (construction.getSubUrban() != null) {
                            Region subUrban = construction.getSubUrban();
                            builder.append(" Kelurahan ").append(subUrban.getRegionName());
                        }

                        toBeDisplayed = builder.toString();
                    }
                    break;
                case 10:

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
                case 11:

                    if (book instanceof ItemsLand) {
                        toBeDisplayed = ((ItemsLand) book).getAcquisitionWay();
                    } else if (book instanceof ItemsMachine) {
                        toBeDisplayed = ((ItemsMachine) book).getAcquisitionWay();
                    } else if (book instanceof ItemsBuilding) {
                        toBeDisplayed = ((ItemsBuilding) book).getAcquitionWay();
                    } else if (book instanceof ItemsNetwork) {
                        toBeDisplayed = ((ItemsNetwork) book).getAcquisitionWay();
                    } else if (book instanceof ItemsFixedAsset) {
                        toBeDisplayed = ((ItemsFixedAsset) book).getAcquisitionWay();
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = "";
                    }

                    break;
                case 12:

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
                case 13:
                    if (book instanceof ItemsLand) {
                        toBeDisplayed = "";
                    } else if (book instanceof ItemsMachine) {
                        Condition condition = ((ItemsMachine) book).getCondition();
                        if (condition != null) {
                            toBeDisplayed = condition.getConditionName();
                        } else {
                            toBeDisplayed = null;
                        }
                    } else if (book instanceof ItemsBuilding) {
                        Condition condition = ((ItemsBuilding) book).getCondition();
                        if (condition != null) {
                            toBeDisplayed = condition.getConditionName();
                        } else {
                            toBeDisplayed = null;
                        }
                    } else if (book instanceof ItemsNetwork) {
                        Condition condition = ((ItemsNetwork) book).getCondition();
                        if (condition != null) {
                            toBeDisplayed = condition.getConditionName();
                        } else {
                            toBeDisplayed = null;
                        }
                    } else if (book instanceof ItemsFixedAsset) {
                        Condition condition = ((ItemsFixedAsset) book).getCondition();
                        if (condition != null) {
                            toBeDisplayed = condition.getConditionName();
                        } else {
                            toBeDisplayed = null;
                        }
                    } else if (book instanceof ItemsConstruction) {
                        toBeDisplayed = "";
                    }
                    break;
                case 14:
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

    private class LoadInventoryBook extends SwingWorker<InventoryBookTableModel, Object> {

        private InventoryBookTableModel model;
        private Exception exception;
        private String modifier = "";
        private String customModifier = "";

        public LoadInventoryBook(String modifier, String customModifier) {
            this.model = (InventoryBookTableModel) table.getModel();
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
                statusLabel.setText("Memuat Buku Inventaris " + object.toString());
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

        private String getLastItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = "";

            if (!iLookUp.isEmpty()) {
                itemsName = iLookUp.get(iLookUp.size() - 1).getCategoryName();
            }

            return itemsName;
        }

        @Override
        protected InventoryBookTableModel doInBackground() throws Exception {
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
                            ((ItemsLand) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsLand) book).setFullItemName(getCompleteItemsName(iLookUp));


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
                            ((ItemsMachine) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsMachine) book).setFullItemName(getCompleteItemsName(iLookUp));

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
                            ((ItemsBuilding) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsBuilding) book).setFullItemName(getCompleteItemsName(iLookUp));

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
                            ((ItemsNetwork) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsNetwork) book).setFullItemName(getCompleteItemsName(iLookUp));

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
                            ((ItemsFixedAsset) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsFixedAsset) book).setFullItemName(getCompleteItemsName(iLookUp));

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
                            ((ItemsConstruction) book).setItemName(getLastItemsName(iLookUp));
                            ((ItemsConstruction) book).setFullItemName(getCompleteItemsName(iLookUp));
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
                JXErrorPane.showDialog(InventoryBookPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
