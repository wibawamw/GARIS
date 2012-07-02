package org.motekar.project.civics.archieve.master.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeCoursesTablePanel extends JXPanel implements ActionListener {

    private MasterBusinessLogic logic;
    private EmployeeCoursesTable table = new EmployeeCoursesTable();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));

    public EmployeeCoursesTablePanel(Connection conn) {
        this.logic = new MasterBusinessLogic(conn);
        construct();
    }

    private void construct() {
        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(table, ComponentState.DEFAULT);

        table.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        table.setShowGrid(false, false);


        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<EmployeeCourses> courseses = table.getSelectedCoursesess();
                    if (!courseses.isEmpty()) {
                        if (courseses.size() == 1) {
                            setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            setButtonState(ManipulationButtonPanel.UNEDIT);
                        }
                    } else {
                        setButtonState(ManipulationButtonPanel.ONLY_NEW);
                    }
                }
            }
        });

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelButton.add(createStrip(1.0, 1.0), null);

        this.setLayout(new BorderLayout());
        this.add(panelButton, BorderLayout.EAST);
        this.add(scPane, BorderLayout.CENTER);

    }

    private void setButtonState(String state) {
        if (state.equals(ManipulationButtonPanel.ONLY_NEW)) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
        } else if (state.equals(ManipulationButtonPanel.ENABLEALL)) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(true);
        }
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Diklat");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Diklat");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Diklat");

        btDelete.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip(JCommandButtonStrip.StripOrientation.VERTICAL);
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btAdd);
        buttonStrip.add(btEdit);
        buttonStrip.add(btDelete);


        return buttonStrip;
    }

    public void clear() {
        table.clear();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        table.setEnabled(enabled);
    }

    public void setButtonEnable(boolean enabled) {
        btAdd.setEnabled(enabled);
        btEdit.setEnabled(false);
        btDelete.setEnabled(false);
    }

    public ArrayList<EmployeeCourses> getCoursesess() {
        return table.getCoursesess();
    }

    public void addEmployeeCourses(ArrayList<EmployeeCourses> coursess) {
        table.addEmployeeCourses(coursess);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btAdd) {
            EmployeeCoursesDlg dlg = new EmployeeCoursesDlg();
            dlg.showDialog();
            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                table.addEmployeeCourses(dlg.getCourses());
            }
        } else if (obj == btEdit) {
            EmployeeCoursesDlg dlg = new EmployeeCoursesDlg(table.getSelectedCourseses());
            dlg.showDialog();
            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                table.updateSelectedCourseses(dlg.getCourses());
            }
        } else if (obj == btDelete) {
            Object[] options = {"Ya", "Tidak"};
            int choise = JOptionPane.showOptionDialog(this,
                    " Anda yakin menghapus data ini ? (Y/T)",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null,
                    options, options[1]);
            if (choise == JOptionPane.YES_OPTION) {
                ArrayList<EmployeeCourses> selectedEmployeeCoursess = table.getSelectedCoursesess();
                if (!selectedEmployeeCoursess.isEmpty()) {
                    table.removeCourseses(selectedEmployeeCoursess);
                }
            }
        }
    }

    private class EmployeeCoursesTable extends JXTable {

        private EmployeeCoursesTableModel model = new EmployeeCoursesTableModel();

        public EmployeeCoursesTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);

            getColumnModel().getColumn(0).setMinWidth(50);
            getColumnModel().getColumn(0).setMaxWidth(50);

            getColumnModel().getColumn(1).setMinWidth(50);
            getColumnModel().getColumn(1).setMaxWidth(50);
        }

        public void clear() {
            model.clear();
        }

        public EmployeeCourses getSelectedCourseses() {
            EmployeeCourses courses = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 3);

            if (obj instanceof EmployeeCourses) {
                courses = (EmployeeCourses) obj;
            }

            return courses;
        }

        public ArrayList<EmployeeCourses> getCoursesess() {
            return model.getCoursesess();
        }

        public ArrayList<EmployeeCourses> getSelectedCoursesess() {

            ArrayList<EmployeeCourses> coursess = new ArrayList<EmployeeCourses>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                EmployeeCourses courses = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 3);
                        if (obj instanceof EmployeeCourses) {
                            courses = (EmployeeCourses) obj;
                            coursess.add(courses);
                        }
                    }
                }
            }

            return coursess;
        }

        public void updateSelectedCourseses(EmployeeCourses courses) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedCourseses(), courses);
        }

        public void removeCourseses(ArrayList<EmployeeCourses> coursess) {
            if (!coursess.isEmpty()) {
                for (EmployeeCourses courses : coursess) {
                    model.remove(courses);
                }
            }

        }

        public void addEmployeeCourses(EmployeeCourses courses) {
            model.add(courses);
        }

        public void addEmployeeCourses(ArrayList<EmployeeCourses> coursess) {
            if (!coursess.isEmpty()) {
                for (EmployeeCourses courses : coursess) {
                    model.add(courses);
                }
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else if (columnIndex == 1 || columnIndex > 3) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
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
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class EmployeeCoursesTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 6;
        private static final String[] columnIds = {"", "No", "Jenis", "Nama Diklat", "Tahun", "Jumlah Jam"};
        private ArrayList<EmployeeCourses> coursess = new ArrayList<EmployeeCourses>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return true;
            }
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<EmployeeCourses> coursess) {
            int first = coursess.size();
            int last = first + coursess.size() - 1;
            coursess.addAll(coursess);
            fireTableRowsInserted(first, last);
        }

        public void add(EmployeeCourses courses) {
            if (!coursess.contains(courses)) {
                insertRow(getRowCount(), courses);
            }
        }

        public void insertRow(int row, EmployeeCourses courses) {
            coursess.add(row, courses);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, EmployeeCourses oldCourseses, EmployeeCourses newCourseses) {
            int index = coursess.indexOf(oldCourseses);
            coursess.set(index, newCourseses);
            fireTableRowsUpdated(index, index);
        }

        public void remove(EmployeeCourses courses) {
            int row = coursess.indexOf(courses);
            coursess.remove(courses);
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
                coursess.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                coursess.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            coursess.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (coursess.get(i) == null) {
                    coursess.set(i, new EmployeeCourses());
                }
            }
        }

        public ArrayList<EmployeeCourses> getCoursesess() {
            return coursess;
        }

        @Override
        public int getRowCount() {
            return coursess.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public EmployeeCourses getCourseses(int row) {
            if (!coursess.isEmpty()) {
                return coursess.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            EmployeeCourses courses = getCourseses(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        courses.setSelected((Boolean) aValue);
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    if (aValue instanceof EmployeeCourses) {
                        courses = (EmployeeCourses) aValue;
                    }
                    break;
                case 4:
                    if (aValue instanceof Integer) {
                        courses.setYearAttended((Integer) aValue);
                    }
                    break;
                case 5:
                    if (aValue instanceof Integer) {
                        courses.setTotalHour((Integer) aValue);
                    }
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            EmployeeCourses courses = getCourseses(row);
            switch (column) {
                case 0:
                    toBeDisplayed = courses.isSelected();
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    toBeDisplayed = courses.getCoursesTypeAsString();
                    break;
                case 3:
                    toBeDisplayed = courses;
                    break;
                case 4:
                    toBeDisplayed = courses.getYearAttended();
                    break;
                case 5:
                    toBeDisplayed = courses.getTotalHour();
                    break;
            }
            return toBeDisplayed;
        }
    }
}
