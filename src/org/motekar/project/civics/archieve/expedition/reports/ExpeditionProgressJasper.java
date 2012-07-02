package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionProgressJasper extends AbstractJasper {

    private Employee commander;
    private Employee employee;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ProfileAccount profileAccount;
    private String departure;

    public ExpeditionProgressJasper(Employee commander,Employee employee,String departure, ProfileAccount profileAccount) {
        this.commander = commander;
        this.employee = employee;
        this.profileAccount = profileAccount;
        this.departure = departure;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = System.getProperty("user.dir") + File.separator + File.separator + "printing" + File.separator + "ExpeditionProgress.jrxml";
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
        return null;
    }

    @Override
    public synchronized Map constructParameter() {
        try {
            SwingWorker<Map, Void> worker = new SwingWorker<Map, Void>() {

                @Override
                protected Map doInBackground() throws Exception {
                    Map param = new HashMap();
                    if (commander != null) {

                        StringBuilder pos = new StringBuilder();
                        if (commander.getStrukturalAsString().equals("")) {
                            pos.append(commander.getFungsionalAsString()).
                                    append(" ").append(commander.getPositionNotes());
                        } else {
                            pos.append(commander.getStrukturalAsString());
                            if (pos.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                pos.append(" ").append(profileAccount.getStateType()).
                                        append(" ").append(profileAccount.getState());
                            } else if (pos.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                    || pos.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                pos.append(" ").append(profileAccount.getCompany());
                            } else {
                                pos.append(" ").append(commander.getPositionNotes());
                            }
                        }
                        
                        
                        StringBuilder pos2 = new StringBuilder();
                        if (employee.getStrukturalAsString().equals("")) {
                            pos2.append(employee.getFungsionalAsString()).
                                    append(" ").append(employee.getPositionNotes());
                        } else {
                            pos2.append(employee.getStrukturalAsString());
                            if (pos2.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                pos2.append(" ").append(profileAccount.getStateType()).
                                        append(" ").append(profileAccount.getState());
                            } else if (pos2.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                    || pos2.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                pos2.append(" ").append(profileAccount.getCompany());
                            } else {
                                pos2.append(" ").append(employee.getPositionNotes());
                            }
                        }

                        param.put("ApproverName", commander.getName());
                        param.put("ApproverNIP", commander.getNip());
                        param.put("ApproverGrade", commander.getPangkatAsString());
                        param.put("ApproverTitle", pos.toString().toUpperCase());
                        
                        
                        param.put("EmployeeName", employee.getName());
                        param.put("EmployeeNIP", employee.getNip());
                        param.put("EmployeeTitle", pos2.toString().toUpperCase());
                        
                        param.put("Departure", departure);
                        
                        
                        StringBuilder stateName = new StringBuilder();

                        if (profileAccount.getStateType().equals(ProfileAccount.KABUPATEN)) {
                            stateName.append(ProfileAccount.KABUPATEN.toUpperCase()).append(" ").
                                    append(profileAccount.getState().toUpperCase());
                        } else {
                            stateName.append(ProfileAccount.KOTAMADYA.toUpperCase()).append(" ").
                                    append(profileAccount.getState().toUpperCase());
                        }
                        
                        param.put("StateName", stateName.toString());
                    }
                    return param;
                }
            };
            worker.execute();
            params = worker.get();

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return params;
    }
}
