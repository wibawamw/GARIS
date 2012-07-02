package org.motekar.project.civics.archieve.assets.inventory.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.FontHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.lib.common.utils.tree.TreeTableNodeCode;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryRekap;
import org.motekar.project.civics.archieve.assets.inventory.report.InventoryRekapJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.asset.creator.ItemsLandWorkbookCreator;
import org.motekar.project.civics.archieve.utils.misc.CustomFilterPanel;
import org.motekar.project.civics.archieve.utils.misc.FilterValue;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryRekapPanel extends JXPanel implements PrintButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    //
    private CustomFilterPanel filterPanel;
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    private JXButton btFilter = new JXButton("Reload");
    private InventoryRekapTreeTable table = new InventoryRekapTreeTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private LoadInventoryRekap worker;
    private ProgressListener progressListener;
    //
    private Unit unit = null;
    private JFileChooser xlsChooser;

    public InventoryRekapPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        this.filterPanel = new CustomFilterPanel(mainframe, new ArrayList<FilterValue>(), false, false, false);
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (!userGroup.getGroupName().equals("Administrator")) {
            unit = mainframe.getUnit();
            filterPanel.setUnitEnable(false);
            filterPanel.setComboUnit(unit);
            btFilter.setEnabled(true);
        }
    }

    private void construct() {

        loadComboUnit();
        constructFileChooser();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Highlighter highlighter = new FontHighlighter(HighlightPredicate.IS_FOLDER,
                new Font(table.getFont().getName(), Font.BOLD, table.getFont().getSize()));

//        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setHighlighters(HighlighterFactory.createAlternateStriping(), highlighter);
        table.setShowGrid(false, false);

        pbar.setVisible(false);

        btFilter.setEnabled(filterPanel.isFiltered());

        btPrintPanel.addListener(this);

        filterPanel.addPropertyChangeListener("filtered", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                btFilter.setEnabled(filterPanel.isFiltered());
                if (!filterPanel.isFiltered()) {
                    if (worker != null) {
                        worker.cancel(true);
                    }
                    table.clear();
                    btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
                }
            }
        });

        btFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String modifier = filterPanel.getModifier();
                    table.loadData(modifier);
                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(InventoryRekapPanel.this.mainframe, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());
        add(createTablePanel(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);

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

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,5px,right:pref",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(filterPanel, cc.xyw(1, 1, 5));
        builder.add(btFilter, cc.xyw(1, 3, 1));
        builder.add(btPrintPanel, cc.xyw(5, 3, 1));

        return builder.getPanel();
    }

    private JXPanel createTablePanel() {


        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JXTitledPanel titledPanel = new JXTitledPanel("Data View");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
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
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        unit = filterPanel.getUnit();

        if (unit != null) {
            InventoryRekapJasper report = new InventoryRekapJasper("Rekapitulasi Buku Inventaris",
                    table.getInventoryRekaps(), unit, mainframe.getProfileAccount());
            report.showReport();
        }

        mainframe.setCursor(old);
    }

    public void onPrintExcel() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        unit = filterPanel.getUnit();

        if (unit != null) {
            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";

                InventoryRekapJasper report = new InventoryRekapJasper("Rekapitulasi Buku Inventaris",
                    table.getInventoryRekaps(), unit, mainframe.getProfileAccount());
                report.exportToExcel(fileName);
            }
        }

        mainframe.setCursor(old);
    }

    public void onPrintPDF() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPrintGraph() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class InventoryRekapTreeTableModel extends DefaultTreeTableModel {

        private static final String[] columnNames = {"<html><center><b>No<br>Urut</br></b></center>", "<html><b>Golongan</b>",
            "<html><center><b>Kode <br>Bidang</br><br>Barang</br></b></center>",
            "<html><b>Nama Bidang Barang</b>",
            "<html><center><b>Jumlah<br>Barang</br></b></center>",
            "<html><center><b>Jumlah Harga<br>Dlm Ribuan</br><br>(Rp)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private static ArrayList<String> vectorNames = new ArrayList<String>(Arrays.asList(columnNames));

        public InventoryRekapTreeTableModel(TreeTableNode root) {
            super(root, vectorNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5 || columnIndex == 6) {
                return BigDecimal.class;
            } else if (columnIndex == 1 || columnIndex == 2) {
                return JLabel.class;
            } else if (columnIndex == 4) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }
    }

    private class InventoryRekapTreeTableNode extends DefaultMutableTreeTableNode {

        InventoryRekapTreeTableNode(Object userObject) {
            super(userObject);
        }

        @Override
        /**
         * Tells if a column can be edited.
         */
        public boolean isEditable(int column) {
            return false;
        }

        /**
         * must override this for setValue from {@link DefaultTreeTableModel}
         * to work properly!
         */
        @Override
        public int getColumnCount() {
            return 7;
        }

        /**
         * Called when done editing a cell from {@link DefaultTreeTableModel}.
         */
        @Override
        public Object getValueAt(int column) {
            Object toBeDisplayed = "";
            if (getUserObject() instanceof InventoryRekap) {
                InventoryRekap rekap = (InventoryRekap) getUserObject();
                int length = rekap.getItemCode().length();
                switch (column) {
                    case 0:
                        if (length == 2) {
                            if (!rekap.getItemCode().equals("99")) {
                                toBeDisplayed = rekap.getItemCode().substring(1, 2);
                            } else {
                                toBeDisplayed = "";
                            }
                        } else {
                            toBeDisplayed = "";
                        }
                        break;
                    case 1:
                        if (length == 2) {
                            if (!rekap.getItemCode().equals("99")) {
                                toBeDisplayed = rekap.getItemCode();
                            } else {
                                toBeDisplayed = "";
                            }
                        } else {
                            toBeDisplayed = "";
                        }
                        break;
                    case 2:
                        if (length == 2) {
                            toBeDisplayed = "";
                        } else {
                            toBeDisplayed = rekap.getChildCode();
                        }
                        break;
                    case 3:
                        if (length == 2) {
                            if (!rekap.getItemCode().equals("99")) {
                                toBeDisplayed = rekap.getItemName().toUpperCase();
                            } else {
                                toBeDisplayed = "TOTAL";
                            }
                        } else {
                            toBeDisplayed = rekap.getItemName();
                        }
                        break;
                    case 4:
                        if (length == 2) {
                            toBeDisplayed = null;
                        } else {
                            toBeDisplayed = rekap.getTotal();
                        }
                        break;
                    case 5:
                        toBeDisplayed = rekap.getPrice();
                        break;
                    case 6:
                        toBeDisplayed = "";
                        break;
                }
            }
            return toBeDisplayed;
        }
    }

    private class InventoryRekapTreeTable extends JXTreeTable {

        private InventoryRekapTreeTableModel model;
        private InventoryRekapTreeTableNode root = null;

        public InventoryRekapTreeTable() {
            root = new InventoryRekapTreeTableNode(new InventoryRekap());
            model = new InventoryRekapTreeTableModel(root);
            setTreeTableModel(model);
            getTableHeader().setReorderingAllowed(false);

            initializeColumnWidths();

            setAutoCreateColumnsFromModel(false);

            getTableHeader().setReorderingAllowed(false);
            getColumnModel().getColumn(0).setMinWidth(70);
            getColumnModel().getColumn(0).setMaxWidth(70);


            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void clear() {
            root = new InventoryRekapTreeTableNode(new InventoryRekap());
            model.setRoot(root);
        }

        public void loadData(String modifier) {
            worker = new LoadInventoryRekap(this, modifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, true);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void cancelLoad() {
        }

        private void addChildInventoryRekap(ArrayList<InventoryRekap> InventoryRekaps, InventoryRekapTreeTableNode parent) {
            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) parent.children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object obj = child.getUserObject();
                if (obj instanceof InventoryRekap) {
                    InventoryRekaps.add((InventoryRekap) obj);
                }

                if (child.getChildCount() > 0) {
                    addChildInventoryRekap(InventoryRekaps, child);
                }
            }
        }

        public ArrayList<InventoryRekap> getInventoryRekaps() {
            ArrayList<InventoryRekap> InventoryRekaps = new ArrayList<InventoryRekap>();

            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) model.getRoot().children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object obj = child.getUserObject();
                if (obj instanceof InventoryRekap) {
                    InventoryRekaps.add((InventoryRekap) obj);
                }

                if (child.getChildCount() > 0) {
                    addChildInventoryRekap(InventoryRekaps, child);
                }
            }

            return InventoryRekaps;
        }

        private void addSelectedChildInventoryRekap(ArrayList<InventoryRekap> InventoryRekaps, InventoryRekapTreeTableNode parent) {
            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) parent.children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object boolObj = model.getValueAt(child, 0);
                if (boolObj instanceof Boolean) {
                    if ((Boolean) boolObj) {
                        Object obj = child.getUserObject();
                        if (obj instanceof InventoryRekap) {
                            InventoryRekaps.add((InventoryRekap) obj);
                        }
                    }
                }

                if (child.getChildCount() > 0) {
                    addSelectedChildInventoryRekap(InventoryRekaps, child);
                }
            }
        }

        public ArrayList<InventoryRekap> getSelectedInventoryRekaps() {
            ArrayList<InventoryRekap> InventoryRekaps = new ArrayList<InventoryRekap>();

            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) model.getRoot().children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object boolObj = model.getValueAt(child, 0);
                if (boolObj instanceof Boolean) {
                    if ((Boolean) boolObj) {
                        Object obj = child.getUserObject();
                        if (obj instanceof InventoryRekap) {
                            InventoryRekaps.add((InventoryRekap) obj);
                        }
                    }
                }

                if (child.getChildCount() > 0) {
                    addSelectedChildInventoryRekap(InventoryRekaps, child);
                }
            }

            return InventoryRekaps;
        }

        public void addInventoryRekap(InventoryRekap rekap) {
            InventoryRekapTreeTableNode parent = getSelectedNode();
            if (parent != null) {
                InventoryRekapTreeTableNode child = new InventoryRekapTreeTableNode(rekap);
                model.insertNodeInto(child, parent, parent.getChildCount());
            } else {
                parent = (InventoryRekapTreeTableNode) model.getRoot();
                InventoryRekapTreeTableNode child = new InventoryRekapTreeTableNode(rekap);
                model.insertNodeInto(child, parent, parent.getChildCount());
            }
        }

        public void updateSelectedInventoryRekap(InventoryRekap rekap) {
            InventoryRekapTreeTableNode node = getSelectedNode();
            if (node != null) {
                model.setUserObject(node, rekap);
            }
        }

        public void removeSelectedInventoryRekaps() {
            ArrayList<InventoryRekapTreeTableNode> nodes = getSelectedNodes();
            if (!nodes.isEmpty()) {
                for (InventoryRekapTreeTableNode node : nodes) {
                    removeSelectedInventoryRekap(node);
                }
            }
        }

        public void removeSelectedInventoryRekap(InventoryRekapTreeTableNode node) {
            if (node != null) {
                if (node.getChildCount() > 0) {
                    removeSelectedChildInventoryRekap(node);
                }
                model.removeNodeFromParent(node);
            }
        }

        private void removeSelectedChildInventoryRekap(InventoryRekapTreeTableNode parent) {
            for (int i = parent.getChildCount() - 1; i >= 0; i--) {
                InventoryRekapTreeTableNode child = (InventoryRekapTreeTableNode) parent.getChildAt(i);
                if (child != null) {
                    model.removeNodeFromParent(child);
                }
            }
        }

        private InventoryRekapTreeTableNode getSelectedChildInventoryRekapNode(InventoryRekapTreeTableNode parent) {
            InventoryRekapTreeTableNode node = null;
            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) parent.children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object boolObj = model.getValueAt(child, 0);
                if (boolObj instanceof Boolean) {
                    if (((Boolean) boolObj).booleanValue()) {
                        node = child;
                        break;
                    } else if (child.getChildCount() > 0) {
                        node = getSelectedChildInventoryRekapNode(child);
                        if (node != null) {
                            break;
                        }
                    }
                } else if (child.getChildCount() > 0) {
                    node = getSelectedChildInventoryRekapNode(child);
                    if (node != null) {
                        break;
                    }
                }
            }

            return node;
        }

        private InventoryRekapTreeTableNode getSelectedNode() {
            InventoryRekapTreeTableNode node = null;
            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) model.getRoot().children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object boolObj = model.getValueAt(child, 0);
                if (boolObj instanceof Boolean) {
                    if (((Boolean) boolObj).booleanValue()) {
                        node = child;
                        break;
                    } else if (child.getChildCount() > 0) {
                        node = getSelectedChildInventoryRekapNode(child);
                        if (node != null) {
                            break;
                        }
                    }
                } else if (child.getChildCount() > 0) {
                    node = getSelectedChildInventoryRekapNode(child);
                    if (node != null) {
                        break;
                    }
                }

            }

            return node;
        }

        private ArrayList<InventoryRekapTreeTableNode> getSelectedNodes() {
            ArrayList<InventoryRekapTreeTableNode> nodes = new ArrayList<InventoryRekapTreeTableNode>();
            Enumeration<InventoryRekapTreeTableNode> rekapEnums = (Enumeration<InventoryRekapTreeTableNode>) model.getRoot().children();
            while (rekapEnums.hasMoreElements()) {
                InventoryRekapTreeTableNode child = rekapEnums.nextElement();
                Object boolObj = model.getValueAt(child, 0);
                if (boolObj instanceof Boolean) {
                    if (((Boolean) boolObj).booleanValue()) {
                        nodes.add(child);
                    } else if (child.getChildCount() > 0) {
                        nodes.add(getSelectedChildInventoryRekapNode(child));
                    }
                } else if (child.getChildCount() > 0) {
                    nodes.add(getSelectedChildInventoryRekapNode(child));
                    if (nodes != null) {
                        break;
                    }
                }

            }

            return nodes;
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
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(JLabel.class)) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private class LoadInventoryRekap extends SwingWorker<InventoryRekapTreeTableModel, InventoryRekap> {

        private InventoryRekapTreeTableModel model;
        private InventoryRekapTreeTableNode root;
        private Exception exception;
        private ArrayList<TreeTableNodeCode> nodeCodes = new ArrayList<TreeTableNodeCode>();
        //
        private String modifier = "";

        public LoadInventoryRekap(InventoryRekapTreeTable table, String modifier) {
            this.model = (InventoryRekapTreeTableModel) table.getTreeTableModel();
            root = new InventoryRekapTreeTableNode(new InventoryRekap());
            this.modifier = modifier;
            model.setRoot(root);
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<InventoryRekap> chunks) {
            mainframe.stopInActiveListener();
            for (InventoryRekap rekap : chunks) {
                if (isCancelled()) {
                    break;
                }

                statusLabel.setText("Memuat Data " + rekap.getItemName());

                if (rekap.getParentCode().equals("")) {
                    InventoryRekapTreeTableNode child = new InventoryRekapTreeTableNode(rekap);
                    model.insertNodeInto(child, root, root.getChildCount());
                    TreeTableNodeCode nodeCode = new TreeTableNodeCode(rekap.getItemCode(), child);
                    nodeCodes.add(nodeCode);
                } else {
                    TreeTableNodeCode code = TreeTableNodeCode.searchArrays(nodeCodes, rekap.getParentCode());
                    InventoryRekapTreeTableNode parent = (InventoryRekapTreeTableNode) code.getNode();
                    InventoryRekapTreeTableNode child = new InventoryRekapTreeTableNode(rekap);
                    model.insertNodeInto(child, parent, parent.getChildCount());
                    TreeTableNodeCode nodeCode = new TreeTableNodeCode(rekap.getItemCode(), child);
                    nodeCodes.add(nodeCode);
                }
                table.expandAll();
            }
        }

        private BigDecimal calculateTotal(ArrayList<InventoryRekap> rekaps) {
            BigDecimal total = BigDecimal.ZERO;

            if (!rekaps.isEmpty()) {
                for (InventoryRekap rekap : rekaps) {
                    if (rekap.getItemCode().length() == 2) {
                        total = total.add(rekap.getPrice());
                    }
                }
            }

            return total;
        }

        @Override
        protected InventoryRekapTreeTableModel doInBackground() throws Exception {
            try {
                ArrayList<InventoryRekap> rekap = logic.getInventoryRekap(mainframe.getSession(), modifier);

                rekap.add(new InventoryRekap("", null, null));
                rekap.add(new InventoryRekap("99", null, calculateTotal(rekap)));

                double progress = 0.0;
                if (!rekap.isEmpty()) {
                    for (int i = 0; i < rekap.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / rekap.size();
                        publish(rekap.get(i));
                        setProgress((int) progress);
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
            if (isCancelled()) {
                return;
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
