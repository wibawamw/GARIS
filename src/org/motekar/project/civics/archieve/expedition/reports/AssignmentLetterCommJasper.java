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
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssignmentLetterCommJasper extends AbstractJasper {

    private AssignmentLetter letter;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ProfileAccount profileAccount;

    public AssignmentLetterCommJasper(AssignmentLetter letter, ProfileAccount profileAccount) {
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
                        String filename = "AssignmentLetterComm.jrxml";
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
                    if (letter != null) {
                        Employee commander = letter.getCommander();
                        commander.setStyled(false);

                        AssignedEmployeeJasper aej = new AssignedEmployeeJasper(letter.getAssignedEmployee(), profileAccount);
                        CarbonCopyJasper ccj = new CarbonCopyJasper(letter.getCarbonCopy());

                        ImageIcon ico = null;

                        if (profileAccount == null) {
                            File file = new File("./images/logo_daerah.jpg");
                            ico = new ImageIcon(file.getPath());
                        } else {
                            if (profileAccount.getByteLogo2() == null) {
                                File file = new File("./images/logo_daerah.jpg");
                                ico = new ImageIcon(file.getPath());
                            } else {
                                byte[] imageStream = profileAccount.getByteLogo2();
                                ico = new ImageIcon(imageStream);
                            }
                        }


                        param.put("subreport", aej.loadReportFile());
                        param.put("datasource", aej.getDataSource());

                        param.put("subreport2", ccj.loadReportFile());
                        param.put("datasource2", ccj.getDataSource());

                        StringBuilder gorvernorname = new StringBuilder();

                        if (profileAccount.getStateType().equals(ProfileAccount.KABUPATEN)) {
                            gorvernorname.append("BUPATI ").
                                    append(profileAccount.getState().toUpperCase());
                        } else {
                            gorvernorname.append("WALIKOTA ").
                                    append(profileAccount.getState().toUpperCase());
                        }

                        param.put("governorname", gorvernorname.toString());

                        param.put("logo", ico.getImage());

                        param.put("docnumber", letter.getDocumentNumber());
                        param.put("purpose", letter.getPurpose());
                        param.put("approvalplace", letter.getApprovalPlace());
                        param.put("approvaldate", letter.getApprovalDate());
                        param.put("approvername", commander.getName());
                        param.put("approverTitle", gorvernorname.toString());
                        param.put("approverTitle2", initCaps(gorvernorname.toString()));

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

       if (caps.length() > 0) {
            caps.deleteCharAt(caps.length() - 1);
        }

        return caps.toString();
    }
}
