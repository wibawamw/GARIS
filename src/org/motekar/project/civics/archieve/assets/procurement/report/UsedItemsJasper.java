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
import org.motekar.project.civics.archieve.assets.inventory.objects.UsedItems;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class UsedItemsJasper extends SimpleAbstractJasper {

    private ArrayList<UsedItems> usedItemss = new ArrayList<UsedItems>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("d MMMM yyyy", new Locale("in", "id", "id"));
    private Approval approval;
    private Signer commSigner;
    private Signer signer;
    private ProfileAccount profileAccount;

    public UsedItemsJasper(String title, ArrayList<UsedItems> usedItemss, ProfileAccount profileAccount,
            Approval approval, Signer signer, Signer commSigner) {
        super(title);
        this.usedItemss = usedItemss;
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
                        String filename = "UsedItems.jrxml";
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
                    model.addColumn("LocationCode");
                    model.addColumn("ItemsCode");
                    model.addColumn("RegNumber");
                    model.addColumn("ItemsName");
                    model.addColumn("Document");
                    model.addColumn("ItemAddress");
                    model.addColumn("AcquisitionWay");
                    model.addColumn("AcquisitionYear");
                    model.addColumn("Construction");
                    model.addColumn("Condition");
                    model.addColumn("Wide");
                    model.addColumn("Price");
                    model.addColumn("DecreeNumber");
                    model.addColumn("CooperationPeriod");
                    model.addColumn("ThirdPartyAddress");
                    model.addColumn("Description");

                    int i = 0;

                    if (!usedItemss.isEmpty()) {
                        for (UsedItems usedItems : usedItemss) {

                            model.addRow(new Object[]{Integer.valueOf(++i), usedItems.getLocationCode(),
                                        usedItems.getItemCode(), usedItems.getRegisterNumber(),
                                        usedItems.getItemName(), usedItems.getDocumentNumber(),
                                        usedItems.getItemAddress(), usedItems.getAcquisitionWay(),
                                        usedItems.getAcquisitionYear(), usedItems.getConstruction(),
                                        usedItems.getCondition(), usedItems.getWide(),
                                        usedItems.getPrice(), usedItems.getDecreeNumber(),
                                        usedItems.getCooperationPeriod(), usedItems.getThirdPartyAddress(),
                                        usedItems.getDescription()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null, null, null, null, null, null, null, null,
                                    null, null, null});
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

        map.put("skpd", profileAccount.getCompany().toUpperCase());
        map.put("state", profileAccount.getState().toUpperCase());
        map.put("province", profileAccount.getProvince().toUpperCase());

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

        return map;
    }
}
