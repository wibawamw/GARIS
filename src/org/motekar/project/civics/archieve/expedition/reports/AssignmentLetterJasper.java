package org.motekar.project.civics.archieve.expedition.reports;

import anw.pattern.common.utils.SpellableNumber;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
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
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssignmentLetterJasper extends AbstractJasper {

    private AssignmentLetter letter;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ProfileAccount profileAccount;

    public AssignmentLetterJasper(AssignmentLetter letter, ProfileAccount profileAccount) {
        this.letter = letter;
        this.profileAccount = profileAccount;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = System.getProperty("user.dir")+File.separator+File.separator+"printing"+File.separator+"AssignmentLetter.jrxml";
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
                    if (letter != null) {
                        Employee commander = letter.getCommander();
                        commander.setStyled(false);

                        StringBuilder pos = new StringBuilder();
                        if (commander.getStrukturalAsString().equals("")) {
                            pos.append(commander.getFungsionalAsString()).
                                    append(" ").append(commander.getPositionNotes());
                        } else {
                            pos.append(commander.getStrukturalAsString()).
                                    append(" ").append(commander.getPositionNotes().toUpperCase());
                        }


                        AssignedEmployeeJasper aej = new AssignedEmployeeJasper(letter.getAssignedEmployee(),profileAccount);

                        ImageIcon ico = null;

                        if (profileAccount == null) {
                            File file = new File("./images/logo_daerah.jpg");
                            ico = new ImageIcon(file.getPath());
                        } else {
                            if (profileAccount.getByteLogo() == null) {
                                File file = new File("./images/logo_daerah.jpg");
                                ico = new ImageIcon(file.getPath());
                            } else {
                                byte[] imageStream = profileAccount.getByteLogo();
                                ico = new ImageIcon(imageStream);
                            }
                        }

                        param.put("subreport", aej.loadReportFile());
                        param.put("datasource", aej.getDataSource());

                        StringBuilder stateName = new StringBuilder();

                        if (profileAccount.getStateType().equals(ProfileAccount.KABUPATEN)) {
                            stateName.append(ProfileAccount.KABUPATEN.toUpperCase()).append(" ").
                                    append(profileAccount.getState().toUpperCase());
                        } else {
                            stateName.append(ProfileAccount.KOTAMADYA.toUpperCase()).append(" ").
                                    append(profileAccount.getState().toUpperCase());
                        }

                        param.put("statename", stateName.toString());
                        param.put("governname", profileAccount.getCompany().toUpperCase());
                        param.put("governaddress", profileAccount.getAddress().toUpperCase());
                        param.put("capital", profileAccount.getCapital().toUpperCase());

                        param.put("logo", ico.getImage());

                        param.put("docnumber", letter.getDocumentNumber());
                        param.put("purpose", letter.getPurpose());
                        param.put("goals", letter.getGoals());
                        param.put("notes", letter.getNotes());
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
                        
                        DateTime startDate = new DateTime(letter.getStartDate());
                        DateTime endDate = new DateTime(letter.getEndDate());
                        Days day = Days.daysBetween(startDate, endDate);
                        
                        
                        StringBuilder duration = new StringBuilder();
                        duration.append(String.valueOf(day.getDays()+1)).
                                append(" (").
                                append(SpellableNumber.format(BigDecimal.valueOf(day.getDays()+1))).
                                append(") hari");
                        
                        String durationStr = duration.toString();
                        durationStr = durationStr.replaceAll("Rupiah", "");
                        
                        param.put("duration", durationStr);
                        param.put("startdate", sdf.format(letter.getStartDate()));
                        param.put("enddate", sdf.format(letter.getEndDate()));

                        param.put("approvalplace", letter.getApprovalPlace());
                        param.put("approvaldate", letter.getApprovalDate());
                        param.put("approvername", commander.getName());
                        param.put("approverGrade", commander.getPangkatAsString());
                        param.put("approverNIP", commander.getNip());
                        param.put("approverTitle", pos.toString());
                        param.put("approverTitle2", initCaps(pos.toString()));

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

        StringTokenizer token = new StringTokenizer(buff," ");

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
}
