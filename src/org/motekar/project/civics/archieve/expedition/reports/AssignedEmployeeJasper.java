package org.motekar.project.civics.archieve.expedition.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssignedEmployeeJasper extends SimpleAbstractJasper {

    private ArrayList<Employee> employees = new ArrayList<Employee>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private ArchieveProperties properties;

    public AssignedEmployeeJasper(ArrayList<Employee> employees, ArchieveProperties properties) {
        this.employees = employees;
        this.properties = properties;
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "AssignedEmployee.jrxml";
                        jasperReport = JasperCompileManager.compileReport("printing/" + filename);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    return jasperReport;
                }
            };
            worker.execute();
            jasperReport = worker.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return jasperReport;
    }

    @Override
    public synchronized DefaultTableModel constructModel() {
        try {
            SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<DefaultTableModel, Void>() {

                @Override
                protected DefaultTableModel doInBackground() throws Exception {
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("No");
                    model.addColumn("EmployeeName");
                    model.addColumn("Grade");

                    int i = 0;

                    if (!employees.isEmpty()) {
                        for (Employee emp : employees) {

                            StringBuilder pos = new StringBuilder();
                            if (emp.getStrukturalAsString().equals("")) {
                                pos.append(emp.getFungsionalAsString()).
                                        append(" ").append(emp.getPositionNotes());
                            } else {
                                pos.append(emp.getStrukturalAsString());
                                if (pos.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                    pos.append(" ").append(properties.getStateType()).
                                            append(" ").append(properties.getState());
                                } else if (pos.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                        || pos.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                    pos.append(" ").append(properties.getCompany());
                                }
                            }

                            model.addRow(new Object[]{Integer.valueOf(++i), emp.getName(),
                                        initCaps(pos.toString())});
                        }
                    } else {
                        model.addRow(new Object[]{null, null,
                                    null, null});
                    }

                    return model;
                }
            };
            worker.execute();
            models = worker.get();

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return models;
    }

    private String initCaps(String str) {
        StringBuilder caps = new StringBuilder();

        if (str != null) {
            if (!str.equals("")) {
                String buff = str.toLowerCase();

                StringTokenizer token = new StringTokenizer(buff, " ");

                while (token.hasMoreElements()) {
                    String s = token.nextToken();
                    caps.append(s.substring(0, 1).toUpperCase());
                    caps.append(s.substring(1));
                    caps.append(" ");
                }

                caps.deleteCharAt(caps.length() - 1);
            }
        }

        return caps.toString();
    }

    public JRTableModelDataSource getDataSource() {
        JRTableModelDataSource ds = new JRTableModelDataSource(constructModel());
        return ds;
    }

    @Override
    public synchronized Map constructParameter() {
        return new HashMap();
    }
}
