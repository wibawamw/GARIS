package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionJournal;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
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
    private ProfileAccount profileAccount;

    public ExpeditionJournalJasper(ExpeditionJournal journal, ProfileAccount profileAccount) {
        this.journal = journal;
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
                        String filename = System.getProperty("user.dir") + File.separator + File.separator + "printing" + File.separator + "ExpeditionJournal.jrxml";
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

                    try {
                        if (journal != null) {

                            AssignmentLetter letter = journal.getLetter();
                            ArrayList<Expedition> expeditions = journal.getExpeditions();

                            ExpeditionResultJasper erj = new ExpeditionResultJasper(journal.getResult());
                            ExpeditionReporterJasper ej = new ExpeditionReporterJasper(expeditions);

                            param.put("subreport", erj.loadReportFile());
                            param.put("datasource", erj.getDataSource());

                            param.put("subreport2", ej.loadReportFile());
                            param.put("datasource2", ej.getDataSource());

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


                            ImageIcon ico = null;

                            if (profileAccount == null) {
                                File file = new File("./images/logo_daerah.jpg");
                                ico = new ImageIcon(file.getPath());
                            } else {
                                byte[] imageStream = profileAccount.getByteLogo();
                                ico = new ImageIcon(imageStream);
                            }

                            param.put("logo", ico.getImage());

                            param.put("docnumber", journal.getReportNumber());
                            param.put("purpose", letter.getPurpose());

                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

                            StringBuilder reportplace = new StringBuilder();
                            reportplace.append(journal.getReportPlace()).
                                    append(", ").
                                    append(sdf.format(journal.getReportDate()));

                            param.put("reportplace", reportplace.toString());

                            StringBuilder letterNumber = new StringBuilder();

                            letterNumber.append("Surat Tugas Nomor : ").
                                    append(letter.getDocumentNumber()).
                                    append(", Tanggal ").
                                    append(sdf.format(letter.getApprovalDate()));

                            param.put("letternumber", letterNumber.toString());

                            if (!expeditions.isEmpty()) {

                                int size = expeditions.size();

                                Expedition firstExp = expeditions.get(0);

                                StringBuilder expNumber = new StringBuilder();
                                for (int i = 0; i < size; i++) {
                                    if (i == 0) {
                                        if (size > 1) {
                                            expNumber.append("SPPD Nomor : ").
                                                    append(expeditions.get(i).getDocumentNumber()).append(";");
                                        } else {
                                            expNumber.append("SPPD Nomor : ").
                                                    append(expeditions.get(i).getDocumentNumber());
                                        }

                                    } else if (i == expeditions.size() - 1) {
                                        expNumber.append(expeditions.get(i).getDocumentNumber());
                                    } else {
                                        expNumber.append(expeditions.get(i).getDocumentNumber()).append(";");
                                    }

                                }

                                expNumber.append(", Tanggal ").
                                        append(sdf.format(firstExp.getApprovalDate()));

                                param.put("expnumber", expNumber.toString());
                            }
                        }
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
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
