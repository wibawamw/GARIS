package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeCellPanel extends JXPanel {

    private Long session;
    private MasterBusinessLogic logic;
    private ArrayList<Employee> employeeData = new ArrayList<Employee>();
    private JXComboBox comboEmployee = new JXComboBox();
    private JXTextField fieldGrade = new JXTextField();
    private Unit unit = null;
    private UserGroup userGroup = null;

    public EmployeeCellPanel(Connection conn, Long session, UserGroup userGroup, Unit unit) {
        this.employeeData = new ArrayList<Employee>();
        this.session = session;
        this.userGroup = userGroup;
        logic = new MasterBusinessLogic(conn);
        construct();
    }

    public EmployeeCellPanel(ArrayList<Employee> employeeData, UserGroup userGroup, Unit unit) {
        this.employeeData = employeeData;
        this.userGroup = userGroup;
        construct();
    }

    private String generateUnitModifier() {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    private void construct() {
        loadComboEmployee();
        fieldGrade.setEnabled(false);
        comboEmployee.setAction(new EmployeeAction());

        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama Pegawai", cc.xy(1, 1));
        builder.add(comboEmployee, cc.xyw(3, 1, 2));

        builder.addLabel("Jabatan", cc.xy(1, 3));
        builder.add(fieldGrade, cc.xyw(3, 3, 2));

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        add(builder.getPanel(), BorderLayout.CENTER);

    }

    private void loadComboEmployee() {
        comboEmployee.removeAllItems();
        try {

            if (!employeeData.isEmpty()) {
                for (Employee e : employeeData) {
                    e.setStyled(false);
                }

                employeeData.add(0, new Employee());
                comboEmployee.setModel(new ListComboBoxModel<Employee>(employeeData));

                AutoCompleteDecorator.decorate(comboEmployee, new EmployeeConverter());
            } else {
                employeeData = logic.getAssignedEmployee(session,generateUnitModifier());
                for (Employee e : employeeData) {
                    e.setStyled(false);
                }

                employeeData.add(0, new Employee());
                comboEmployee.setModel(new ListComboBoxModel<Employee>(employeeData));

                AutoCompleteDecorator.decorate(comboEmployee, new EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void setSelectedObject(Object obj) {
        if (obj == null) {
            comboEmployee.setSelectedIndex(0);
        } else {
            if (obj instanceof Employee) {
                comboEmployee.setSelectedItem((Employee) obj);
            } else {
                comboEmployee.setSelectedIndex(0);
            }
        }
    }

    public Object getSelectedObject() {
        Object obj = comboEmployee.getSelectedItem();
        Employee selectedEmployee = null;
        if (obj instanceof Employee) {
            selectedEmployee = (Employee) obj;
        }
        return selectedEmployee;
    }

    private class EmployeeAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == comboEmployee) {

                Object obj = comboEmployee.getSelectedItem();

                if (obj != null) {
                    if (obj instanceof Employee) {
                        Employee selectedEmployee = (Employee) obj;
                        if (selectedEmployee.getStrukturalAsString().equals("")) {
                            fieldGrade.setText(selectedEmployee.getFungsionalAsString());
                        } else {
                            fieldGrade.setText(selectedEmployee.getStrukturalAsString());
                        }
                    }
                }
            }
        }
    }

    public static class EmployeeConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof Employee)) {
                return new String[0];
            }
            Employee employee = (Employee) item;
            return new String[]{
                        employee.toString(), employee.getName(), employee.getNip()
                    };
        }

        public String getPreferredStringForItem(Object item) {
            String[] possible = getPossibleStringsForItem(item);
            String preferred = null;
            if (possible != null && possible.length > 0) {
                preferred = possible[0];
            }
            return preferred;
        }
    }
}