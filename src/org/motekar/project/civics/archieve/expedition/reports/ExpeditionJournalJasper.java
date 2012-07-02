package org.motekar.project.civics.archieve.expedition.reports;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionJournal;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionJournalJasper extends SimpleAbstractJasper {

    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ExpeditionJournal journal;
    private ArchieveProperties properties;

    public ExpeditionJournalJasper(ExpeditionJournal journal, ArchieveProperties properties) {
        this.journal = journal;
        this.properties = properties;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "ExpeditionJournal.jrxml";
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
        DefaultTableModel model = new DefaultTableModel();
        return model;
    }

    @Override
    public synchronized Map constructParameter() {
        try {
            SwingWorker<Map, Void> worker = new SwingWorker<Map, Void>() {

                @Override
                protected Map doInBackground() throws Exception {
                    Map param = new HashMap();
                    if (journal != null) {

                        Expedition expedition = journal.getExpedition();
                        Employee assignedEmployee = expedition.getAssignedEmployee();
                        AssignmentLetter letter = expedition.getLetter();

                        ExpeditionResultJasper erj = new ExpeditionResultJasper(journal.getResult());

                        param.put("subreport", erj.loadReportFile());
                        param.put("datasource", erj.getDataSource());


                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

                        param.put("destination", expedition.getDestination());
                        param.put("reportdate", sdf.format(journal.getReportDate()));
                        param.put("expeditionnumber", expedition.getDocumentNumber());
                        param.put("employeename", assignedEmployee.getName());
                        param.put("employeenip", assignedEmployee.getNip());

                        if (!assignedEmployee.isNonEmployee()) {
                            param.put("employeenip2", "NIP. " + assignedEmployee.getNip());
                        } else {
                            param.put("employeenip2", "");
                        }

                        StringBuilder position = new StringBuilder();

                        if (!assignedEmployee.isNonEmployee()) {
                            if (assignedEmployee.getStrukturalAsString().equals("")) {
                                position.append(assignedEmployee.getFungsionalAsString());
                                position.append(" ");
                                position.append(assignedEmployee.getPositionNotes());
                            } else {
                                position.append(assignedEmployee.getStrukturalAsString());
                                if (position.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                    position.append(" ").append(properties.getStateType()).
                                            append(" ").append(properties.getState());
                                } else if (position.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                        || position.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                    position.append(" ").append(properties.getCompany());
                                }
                            }
                        }

                        if (!assignedEmployee.isNonEmployee()) {
                            param.put("employeegrade", assignedEmployee.getGradeAsString());
                            param.put("position", position.toString());
                        } else {
                            param.put("employeegrade", "");
                            param.put("position", "");
                        }
                        
                        param.put("fundingsource", journal.getFundingSource());
                        param.put("purpose", letter.getPurpose());

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
