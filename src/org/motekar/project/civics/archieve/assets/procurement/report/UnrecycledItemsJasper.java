package org.motekar.project.civics.archieve.assets.procurement.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.objects.UnrecycledItems;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class UnrecycledItemsJasper extends SimpleAbstractJasper {

    private ArrayList<UnrecycledItems> unrecycledItemss = new ArrayList<UnrecycledItems>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private ProfileAccount profileAccount;
    private Approval approval;
    private Signer commSigner;
    private Signer signer;

    public UnrecycledItemsJasper(String title, ArrayList<UnrecycledItems> unrecycledItemss, ProfileAccount profileAccount, Approval approval, Signer signer, Signer commSigner) {
        super(title);
        this.unrecycledItemss = unrecycledItemss;
        this.approval = approval;
        this.signer = signer;
        this.commSigner = commSigner;
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
                        String filename = "UnrecycledItems.jrxml";
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
                    model.addColumn("ReceiveDate");
                    model.addColumn("ItemsName");
                    model.addColumn("Specification");
                    model.addColumn("ProductionYear");
                    model.addColumn("ReceiveAmount");
                    model.addColumn("Contract");
                    model.addColumn("DocumentDate");
                    model.addColumn("DocumentNumber");
                    model.addColumn("ReleaseDate");
                    model.addColumn("Submitted");
                    model.addColumn("ReleaseAmount");
                    model.addColumn("Delegated");
                    model.addColumn("Description");

                    int i = 0;

                    if (!unrecycledItemss.isEmpty()) {
                        for (UnrecycledItems unrecycledItems : unrecycledItemss) {
                            
                            StringBuilder str = new StringBuilder();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                                    new Locale("in", "id", "id"));

                            if (unrecycledItems.getContractDate() != null) {
                                str.append(sdf.format(unrecycledItems.getContractDate()));

                            }

                            if (!unrecycledItems.getContractNumber().equals("")) {
                                if (unrecycledItems.getContractDate() != null) {
                                    str.append("\n");
                                    str.append(unrecycledItems.getContractNumber());
                                } else {
                                    str.append(unrecycledItems.getContractNumber());
                                }
                            }

                            StringBuilder str2 = new StringBuilder();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy",
                                    new Locale("in", "id", "id"));

                            if (unrecycledItems.getReleaseDocumentDate() != null) {
                                str2.append(sdf2.format(unrecycledItems.getReleaseDocumentDate()));

                            }

                            if (!unrecycledItems.getReleaseDocumentNumber().equals("")) {
                                if (unrecycledItems.getReleaseDocumentDate() != null) {
                                    str2.append("\n");
                                    str2.append(unrecycledItems.getReleaseDocumentNumber());
                                } else {
                                    str2.append(unrecycledItems.getReleaseDocumentNumber());
                                }
                            }

                            
                            model.addRow(new Object[]{Integer.valueOf(++i), unrecycledItems.getReceiveDate(),
                                        unrecycledItems.getItemName(), unrecycledItems.getSpecification(),
                                        unrecycledItems.getProductionYear(), unrecycledItems.getReceiveAmount(),
                                        str.toString(), unrecycledItems.getDocumentDate(), 
                                        unrecycledItems.getDocumentNumber(),unrecycledItems.getReleaseDate(), 
                                        unrecycledItems.getSubmitted(),unrecycledItems.getReleaseAmount(),
                                        str.toString(),unrecycledItems.getDescription()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null, null, null, null, null, null, null, null});
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

    public JRTableModelDataSource getDataSource() {
        JRTableModelDataSource ds = new JRTableModelDataSource(constructModel());
        return ds;
    }

    @Override
    public synchronized Map constructParameter() {

        Map map = new HashMap();

        if (approval != null) {
            map.put("approvalplace", approval.getPlace());
            map.put("approvaldate", dateFormat.format(approval.getDate()));
        } else {
            map.put("approvalplace", "...........");
            map.put("approvaldate", "..................");
        }

        if (signer != null) {
            map.put("signername", signer.getSignerName());
            map.put("signernip", signer.getSignerNIP());
        } else {
            map.put("signername", "......................");
            map.put("signernip", "......................");
        }

        if (commSigner != null) {
            map.put("commsignername", commSigner.getSignerName());
            map.put("commsignernip", commSigner.getSignerNIP());
        } else {
            map.put("commsignername", "......................");
            map.put("commsignernip", "......................");
        }

        map.put("skpd", profileAccount.getCompany().toUpperCase());
        map.put("state", profileAccount.getState().toUpperCase());
        map.put("province", profileAccount.getProvince().toUpperCase());

        return map;
    }
}
