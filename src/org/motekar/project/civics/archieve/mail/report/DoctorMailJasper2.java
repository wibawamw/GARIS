package org.motekar.project.civics.archieve.mail.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.mail.objects.DoctorMail;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class DoctorMailJasper2 extends AbstractJasper {

    private DoctorMail doctorMail;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ProfileAccount profileAccount;

    public DoctorMailJasper2(DoctorMail doctorMail, ProfileAccount profileAccount) {
        this.doctorMail = doctorMail;
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
                        String filename = System.getProperty("user.dir") + File.separator + File.separator + "printing" + File.separator + "DoctorMail2.jrxml";
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
                    if (doctorMail != null) {
                        Employee approvalEmployee = doctorMail.getApproval();
                        approvalEmployee.setStyled(false);

                        StringBuilder pos = new StringBuilder();
                        if (approvalEmployee.getStrukturalAsString().equals("")) {
                            pos.append(approvalEmployee.getFungsionalAsString()).
                                    append(" ").append(approvalEmployee.getPositionNotes());
                        } else {
                            pos.append(approvalEmployee.getStrukturalAsString()).
                                    append(" ").append(approvalEmployee.getPositionNotes());
                        }


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

                        param.put("docnumber", doctorMail.getDocumentNumber());
                        param.put("patiencename", doctorMail.getPatienceName());

                        String birthPlace = doctorMail.getBirthPlace();
                        Date birthDate = doctorMail.getBirthDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

                        String birthPlaceAndDate = "";
                        
                        if (birthDate != null) {
                            birthPlaceAndDate = birthPlace + ", " + sdf.format(birthDate);
                        } else if (birthPlace != null && !birthPlace.equals("")) {
                            birthPlaceAndDate = birthPlace;
                        } else {
                            birthPlaceAndDate = "";
                        }

                        param.put("birthdateandplace", birthPlaceAndDate);
                        param.put("jobs", doctorMail.getJobs());
                        param.put("address", doctorMail.getAddress());
                        param.put("requested", doctorMail.getRequested());
                        param.put("mailddate", doctorMail.getMailDate());
                        param.put("mailnumber", doctorMail.getMailNumber());
                        param.put("checked", doctorMail.getChecked());
                        param.put("term", doctorMail.getTerm());
                        param.put("height", doctorMail.getHeight());
                        param.put("weight", doctorMail.getWeight());
                        
                        StringBuilder bloodPreasure = new StringBuilder();
                        bloodPreasure.append(doctorMail.getBloodPreasure()).append("/").
                                append(doctorMail.getBloodPreasure2());
                        
                        
                        param.put("bloodpreasure", bloodPreasure.toString());
                        param.put("expireddate", sdf.format(doctorMail.getExpiredDate()));
                        
                        param.put("unitname", profileAccount.getCompany());
                        

                        param.put("approvalplaceanddate", doctorMail.getApprovalPlace() + ", " + sdf.format(doctorMail.getApprovalDate()));
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
