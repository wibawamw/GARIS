package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionJasper extends AbstractJasper {

    private Expedition expedition;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ArchieveProperties properties;

    public ExpeditionJasper(Expedition expedition, ArchieveProperties properties) {
        this.expedition = expedition;
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
                        String filename = "Expedition.jrxml";
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
        return null;
    }

    @Override
    public synchronized Map constructParameter() {
        try {
            SwingWorker<Map, Void> worker = new SwingWorker<Map, Void>() {

                @Override
                protected Map doInBackground() throws Exception {
                    Map param = new HashMap();
                    if (expedition != null) {
                        Employee commander = expedition.getLetter().getCommander();
                        commander.setStyled(false);
                        Employee assinedEmployee = expedition.getAssignedEmployee();
                        assinedEmployee.setStyled(false);

                        StringBuilder position = new StringBuilder();
                        if (assinedEmployee.getStrukturalAsString().equals("")) {
                            position.append(assinedEmployee.getFungsionalAsString());
                            position.append(" ");
                            position.append(assinedEmployee.getPositionNotes());
                        } else {
                            position.append(assinedEmployee.getStrukturalAsString());
                            if (position.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                position.append(" ").append(properties.getStateType()).
                                        append(" ").append(properties.getState());
                            } else if (position.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                    || position.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                position.append(" ").append(properties.getCompany());
                            }
                        }

                        StringBuilder pos = new StringBuilder();
                        if (commander.getStrukturalAsString().equals("")) {
                            pos.append(commander.getFungsionalAsString()).
                                    append(" ").append(commander.getPositionNotes());
                        } else {
                            pos.append(commander.getStrukturalAsString());
                        }


                        DateTime startDate = new DateTime(expedition.getStartDate());
                        DateTime endDate = new DateTime(expedition.getEndDate());
                        Days day = Days.daysBetween(startDate, endDate);

                        ExpeditionFollowerJasper fjp = new ExpeditionFollowerJasper(expedition.getFollower(), expedition.getStartDate());


                        File file = properties.getLogo();

                        if (!file.exists()) {
                            file = new File("./images/logo_daerah.jpg");
                        }

                        ImageIcon ico = new ImageIcon(file.getPath());

                        param.put("subreport", fjp.loadReportFile());
                        param.put("datasource", fjp.getDataSource());

                        StringBuilder stateName = new StringBuilder();

                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            stateName.append(ArchieveProperties.KABUPATEN.toUpperCase()).append(" ").
                                    append(properties.getState().toUpperCase());
                        }

                        param.put("statename", stateName.toString());
                        param.put("governname", properties.getCompany().toUpperCase());
                        param.put("governaddress", properties.getAddress().toUpperCase());
                        param.put("capital", properties.getCapital().toUpperCase());

                        param.put("logo", ico.getImage());

                        param.put("docnumber", expedition.getDocumentNumber());
                        param.put("commander", initCaps(commander.getStrukturalAsString()));
                        param.put("assignedemployee", assinedEmployee.getName());
                        param.put("grades", assinedEmployee.getGradeAsString());
                        param.put("position", initCaps(position.toString()));
                        param.put("departure", expedition.getDeparture());
                        param.put("destination", expedition.getDestination());
                        param.put("transportation", expedition.getTransportationAsString());
                        param.put("duration", String.valueOf(day.getDays()));
                        param.put("startdate", expedition.getStartDate());
                        param.put("enddate", expedition.getEndDate());
                        param.put("purpose", expedition.getLetter().getPurpose());
                        param.put("notes", expedition.getNotes());
                        param.put("approvalplace", expedition.getApprovalPlace());
                        param.put("approvaldate", expedition.getApprovalDate());
                        param.put("approvername", commander.getName());
                        param.put("approverGrade", commander.getPangkatAsString());
                        param.put("approverNIP", commander.getNip());
                        param.put("approverTitle", pos.toString());

                        param.put("charge", expedition.getCharge());
                        StringBuilder builder = new StringBuilder();

                        if (expedition.getProgram() != null) {
                            builder.append(expedition.getProgram().getProgramCode());
                        }

                        if (expedition.getActivity() != null) {
                            builder.append(".").
                                    append(expedition.getActivity().getChildCode());
                        }

                        if (expedition.getAccount() != null) {
                            builder.append(".").
                                    append(expedition.getAccount().getAccountCode());
                        }

                        param.put("chargebudget", builder.toString());
                        
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

        caps.deleteCharAt(caps.length() - 1);

        return caps.toString();
    }
}
