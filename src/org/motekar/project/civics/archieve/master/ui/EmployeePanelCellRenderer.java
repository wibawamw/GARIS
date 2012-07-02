package org.motekar.project.civics.archieve.master.ui;

import java.awt.Component;
import java.sql.Connection;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeePanelCellRenderer extends EmployeeCellPanel implements TableCellRenderer {

    public EmployeePanelCellRenderer(Connection conn, ArchieveMainframe mainframe) {
        super(conn, mainframe.getSession(),mainframe.getUserGroup(),mainframe.getUnit());
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {
            return this;
        }

        if (value instanceof Employee) {
            setSelectedObject((Employee)value);
        }

        setBackground(isSelected ? table.getSelectionBackground()
                : table.getBackground());

        setForeground(isSelected ? table.getSelectionForeground()
                : table.getForeground());


        return this;
    }

}
