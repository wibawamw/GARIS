package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class DivisionChooserDlg implements ActionListener {

    private JFrame frame;
    private JProgressBar pBar = new JProgressBar();
    private MasterBusinessLogic logic;
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private Division selectedDivision = null;
    private int response = JOptionPane.NO_OPTION;
    private DivisionTable table = new DivisionTable();
    private LoadDivision worker;
    private ProgressListener progressListener;

    public DivisionChooserDlg(JFrame frame, Connection conn) {
        this.frame = frame;
        logic = new MasterBusinessLogic(conn);
        checkLogin();
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

    public void showDialog() {

        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Bagian / Bidang / Seksi");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(500, 450));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,5px,fill:default:grow,10px",
                "fill:default:grow,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        CellConstraints cc = new CellConstraints();

        builder.add(scPane, cc.xywh(1, 1, 5, 2));

        builder.add(pBar, cc.xyw(1, 3, 5));

        return builder.getPanel();
    }

    private Component construct() {

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<Division> divisions = table.getSelectedDivisions();
                    if (!divisions.isEmpty()) {
                        if (divisions.size() == 1) {
                            btOK.setEnabled(true);
                        } else {
                            btOK.setEnabled(false);
                        }
                    } else {
                        btOK.setEnabled(false);
                    }
                }
            }
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        btOK.setEnabled(false);

        return panel;
    }

    private JXPanel createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();
        okTooltip.setTitle("Pilih");
        btOK.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");

        btCancel.setActionRichTooltip(cancelTooltip);

        btOK.addActionListener(this);
        btCancel.addActionListener(this);

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            if (worker != null) {
                worker.cancel(true);
            }
            dlg.dispose();
        }
    }

    public int getResponse() {
        return response;
    }

    private void onOK() {
        if (worker != null) {
            worker.cancel(true);
        }
        response = JOptionPane.YES_OPTION;
        selectedDivision = table.getSelectedDivision();
        dlg.dispose();
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (worker != null) {
                    worker.cancel(true);
                }
                dlg.dispose();
            }
        });
    }

    public Division getSelectedDivision() {
        return selectedDivision;
    }

    private class DivisionTable extends JXTable {

        private DivisionTableModel model;

        public DivisionTable() {
            model = new DivisionTableModel();
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
            getColumnModel().getColumn(0).setMinWidth(50);
            getColumnModel().getColumn(0).setMaxWidth(50);

            setColumnMargin(5);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadDivision(modifier);
            progressListener = new ProgressListener(pBar, worker, true);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public Division getSelectedDivision() {
            ArrayList<Division> selectedDivisions = getSelectedDivisions();
            return selectedDivisions.get(0);
        }

        public ArrayList<Division> getDivisions() {
            return model.getDivisions();
        }

        public ArrayList<Division> getSelectedDivisions() {

            ArrayList<Division> divisions = new ArrayList<Division>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                Division division = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 1);
                        if (obj instanceof Division) {
                            division = (Division) obj;
                            divisions.add(division);
                        }
                    }
                }
            }

            return divisions;
        }

        public Division getClickedDivision() {

            Division division = null;

            int rowSel = getSelectedRow();

            Object object = model.getValueAt(rowSel, 1);

            if (object instanceof Division) {
                division = (Division) object;
            }
            return division;
        }

        public void updateSelectedDivision(Division division) {
            model.updateRow(getSelectedDivision(), division);
        }

        public void removeDivision(ArrayList<Division> divisions) {
            if (!divisions.isEmpty()) {
                for (Division division : divisions) {
                    model.remove(division);
                }
            }

        }

        public void addDivision(ArrayList<Division> divisions) {
            if (!divisions.isEmpty()) {
                for (Division division : divisions) {
                    model.add(division);
                }
            }
        }

        public void addDivision(Division division) {
            model.add(division);
        }

        public void insertEmptyDivision() {
            addDivision(new Division());
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Boolean.class)) {
                return new DefaultTableRenderer(new CheckBoxProvider());
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class DivisionTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 2;
        private static final String[] COLUMNS = {"", "<html><b>No. Medrec</b>"};
        private ArrayList<Division> divisions = new ArrayList<Division>();

        public DivisionTableModel() {
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
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<Division> divisions) {
            int first = divisions.size();
            int last = first + divisions.size() - 1;
            divisions.addAll(divisions);
            fireTableRowsInserted(first, last);
        }

        public void add(Division division) {
            insertRow(getRowCount(), division);
        }

        public void insertRow(int row, Division division) {
            divisions.add(row, division);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(Division oldDivision, Division newDivision) {
            int index = divisions.indexOf(oldDivision);
            divisions.set(index, newDivision);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Division division) {
            int row = divisions.indexOf(division);
            divisions.remove(division);
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
                divisions.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                divisions.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            divisions.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (divisions.get(i) == null) {
                    divisions.set(i, new Division());
                }
            }
        }

        public ArrayList<Division> getDivisions() {
            return divisions;
        }

        @Override
        public int getRowCount() {
            return divisions.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Division getDivision(int row) {
            if (!divisions.isEmpty()) {
                return divisions.get(row);
            }
            return new Division();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Division division = getDivision(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        division.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    if (aValue instanceof Division) {
                        division = (Division) aValue;
                    }
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Division division = getDivision(row);
            switch (column) {
                case 0:
                    toBeDisplayed = division.isSelected();
                    break;
                case 1:
                    toBeDisplayed = division;
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadDivision extends SwingWorker<DivisionTableModel, Division> {

        private DivisionTableModel model;
        private Exception exception;
        private String modifier = "";

        public LoadDivision(String modifier) {
            this.model = (DivisionTableModel) table.getModel();
            this.modifier = modifier;
            this.model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Division> chunks) {
            for (Division division : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.add(division);
            }
        }

        @Override
        protected DivisionTableModel doInBackground() throws Exception {
            try {
                ArrayList<Division> divisions = logic.getDivision(((ArchieveMainframe) frame).getSession(), false, modifier);

                double progress = 0.0;
                if (!divisions.isEmpty()) {
                    for (int i = 0; i < divisions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / divisions.size();
                        setProgress((int) progress);

                        Division division = divisions.get(i);

                        publish(division);
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
