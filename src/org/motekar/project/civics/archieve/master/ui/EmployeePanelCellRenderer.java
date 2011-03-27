package org.motekar.project.civics.archieve.master.ui;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeePanelCellRenderer extends EmployeeCellPanel implements TableCellRenderer {

    public EmployeePanelCellRenderer(Connection conn, Long session) {
        super(conn, session);
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
