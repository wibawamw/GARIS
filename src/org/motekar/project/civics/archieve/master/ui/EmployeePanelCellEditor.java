package org.motekar.project.civics.archieve.master.ui;

import java.awt.Component;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeePanelCellEditor extends EmployeeCellPanel implements TableCellEditor {

    private Object originalValue;
    protected transient ArrayList listeners;
    protected transient boolean editing;

    public EmployeePanelCellEditor(Connection conn, Long session) {
        super(conn, session);
        listeners = new ArrayList();
    }

    public Object getCellEditorValue() {
        return getSelectedObject();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (value == null) {
            return this;
        }

        if (value instanceof Employee) {
            setSelectedObject((Employee)value);
        }

        originalValue = getSelectedObject();

        editing = true;

        setRequestFocusEnabled(true);
        
        setBackground(table.getSelectionBackground());
        setForeground(table.getSelectionForeground());

        setOpaque(true);

        return this;
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
        editing = false;
    }

    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }

    protected void fireEditingCanceled() {
        setSelectedObject(originalValue);
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CellEditorListener) listeners.get(i)).editingCanceled(ce);
        }
    }

    protected void fireEditingStopped() {
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CellEditorListener) listeners.get(i)).editingStopped(ce);
        }
    }

}
