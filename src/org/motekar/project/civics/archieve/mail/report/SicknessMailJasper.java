package org.motekar.project.civics.archieve.mail.report;

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
import org.motekar.project.civics.archieve.mail.objects.SicknessMail;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class SicknessMailJasper extends AbstractJasper {

    private SicknessMail sicknessMail;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ProfileAccount profileAccount;

    public SicknessMailJasper(SicknessMail sicknessMail, ProfileAccount profileAccount) {
        this.sicknessMail = sicknessMail;
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
                        String filename = System.getProperty("user.dir") + File.separator + File.separator + "printing" + File.separator + "SicknessMail.jrxml";
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
                    if (sicknessMail != null) {
                        Employee approvalEmployee = sicknessMail.getApproval();
                        approvalEmployee.setStyled(false);

                        StringBuilder pos = new StringBuilder();
                        if (approvalEmployee.getStrukturalAsString().equals("")) {
                            pos.append(approvalEmployee.getFungsionalAsString()).
                                    append(" ").append(approvalEmployee.getPositionNotes());
                        } else {
                            pos.append(approvalEmployee.getStrukturalAsString()).
                                    append(" ").append(approvalEmployee.getPositionNotes());
                        }


                        DateTime startDate = new DateTime(sicknessMail.getStartDate());
                        DateTime endDate = new DateTime(sicknessMail.getEndDate());
                        Days day = Days.daysBetween(startDate, endDate);


                        ImageIcon ico = null;

                        if (profileAccount == null) {
                            File file = new File("./images/logo_daerah.jpg");
                            ico = new ImageIcon(file.getPath());
                        } else {
                            byte[] imageStream = profileAccount.getByteLogo();
                            ico = new ImageIcon(imageStream);
                        }

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

                        StringBuilder struk = new StringBuilder();
                        struk.append(approvalEmployee.getStrukturalAsString()).append(" ").
                                append(approvalEmployee.getPositionNotes());

                        param.put("docnumber", sicknessMail.getDocumentNumber());
                        param.put("patiencename", sicknessMail.getPatienceName());
                        param.put("patienceage", sicknessMail.getPatienceAge());
                        param.put("jobs", sicknessMail.getJobs());
                        param.put("address", sicknessMail.getAddress());

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));


                        StringBuilder duration = new StringBuilder();
                        duration.append(String.valueOf(day.getDays() + 1)).
                                append(" (").
                                append(SpellableNumber.format(BigDecimal.valueOf(day.getDays() + 1))).
                                append(") hari");

                        String durationStr = duration.toString();
                        durationStr = durationStr.replaceAll("Rupiah", "");

                        param.put("duration", durationStr);
                        param.put("startdate", sdf.format(sicknessMail.getStartDate()));
                        param.put("enddate", sdf.format(sicknessMail.getEndDate()));
                        param.put("approvalplaceanddate", sicknessMail.getApprovalPlace() + ", " + sdf.format(sicknessMail.getApprovalDate()));
                        param.put("approvername", approvalEmployee.getName());
                        param.put("approverNIP", approvalEmployee.getNip());

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
