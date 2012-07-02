package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
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
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
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
    private ProfileAccount profileAccount;

    public AssignedEmployeeJasper(ArrayList<Employee> employees, ProfileAccount profileAccount) {
        this.employees = employees;
        this.profileAccount = profileAccount;
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = System.getProperty("user.dir") + File.separator + File.separator + "printing" + File.separator + "AssignedEmployee.jrxml";
                        jasperReport = JasperCompileManager.compileReport(filename);
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
                    model.addColumn("Title");
                    try {

                        int i = 0;

                        if (!employees.isEmpty()) {
                            for (Employee emp : employees) {

                                StringBuilder pos = new StringBuilder();
                                if (emp.getStrukturalAsString().equals("")) {
                                    pos.append(emp.getFungsionalAsString()).
                                            append(" ").append(emp.getPositionNotes());
                                } else {
                                    pos.append(initCaps(emp.getStrukturalAsString()));
                                    if (pos.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                        pos.append(" ").append(profileAccount.getStateType()).
                                                append(" ").append(profileAccount.getState());
                                    } else if (pos.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                            || pos.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                        pos.append(" ").append(profileAccount.getCompany());
                                    } else {
                                        pos.append(" ").append(emp.getPositionNotes());
                                    }
                                }

                                model.addRow(new Object[]{Integer.valueOf(++i), emp.getName() + " / "+emp.getNip(),
                                            emp.getGradeAsString(),initCaps(pos.toString())});
                            }
                        } else {
                            model.addRow(new Object[]{null, null,
                                        null, null});
                        }
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
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

        String buff = str.toLowerCase();

        StringTokenizer token = new StringTokenizer(buff, " ");

        while (token.hasMoreElements()) {
            String s = token.nextToken();
            caps.append(s.substring(0, 1).toUpperCase());
            caps.append(s.substring(1));
            caps.append(" ");
        }

        if (caps.length() > 0) {
            caps.deleteCharAt(caps.length() - 1);
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