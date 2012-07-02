package org.motekar.project.civics.archieve.assets.procurement.report;

import java.math.BigDecimal;
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
import org.motekar.project.civics.archieve.assets.procurement.objects.ApprovalBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.InventoryReport;
import org.motekar.project.civics.archieve.assets.procurement.objects.ReleaseBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryReportJasper extends SimpleAbstractJasper {

    private ArrayList<InventoryReport> thirdPartyItemss = new ArrayList<InventoryReport>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("d MMMM yyyy", new Locale("in", "id", "id"));
    private Approval approval;
    private Signer commSigner;
    private Signer signer;
    private ProfileAccount profileAccount;
    private Integer semester = Integer.valueOf(0);
    private Integer year = Integer.valueOf(0);

    public InventoryReportJasper(String title, ArrayList<InventoryReport> thirdPartyItemss, ProfileAccount profileAccount,
             Integer semester,Integer year,Approval approval, Signer signer, Signer commSigner) {
        super(title);
        this.thirdPartyItemss = thirdPartyItemss;
        this.approval = approval;
        this.signer = signer;
        this.commSigner = commSigner;
        this.profileAccount = profileAccount;
        this.semester = semester;
        this.year = year;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "InventoryReport.jrxml";
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
                    model.addColumn("ReceiveFrom");
                    model.addColumn("Contract");
                    model.addColumn("InvoiceNumber");
                    model.addColumn("InvoiceDate");
                    model.addColumn("ReceiveAmount");
                    model.addColumn("ItemsName");
                    model.addColumn("ReceivePrice");
                    model.addColumn("DocumentNumber");
                    model.addColumn("DocumentDate");
                    model.addColumn("Description");
                    model.addColumn("SortingNumber");
                    model.addColumn("ReleaseDate");
                    model.addColumn("BillNumber");
                    model.addColumn("BillDate");
                    model.addColumn("ReleaseAmount");
                    model.addColumn("ItemsName2");
                    model.addColumn("ReleasePrice");
                    model.addColumn("TotalPrice");
                    model.addColumn("Allotment");
                    model.addColumn("DelegateDate");
                    model.addColumn("Description2");

                    int i = 0;

                    if (!thirdPartyItemss.isEmpty()) {
                        for (InventoryReport thirdPartyItems : thirdPartyItemss) {

                            ApprovalBook approvalBook = thirdPartyItems.getApprovalBook();
                            ReleaseBook releaseBook = thirdPartyItems.getReleaseBook();

                            BigDecimal totalPrice = BigDecimal.ZERO;
                            if (releaseBook.getPrice() != null) {
                                totalPrice = totalPrice.add(releaseBook.getPrice());
                            }
                            if (releaseBook.getAmount() != null) {
                                totalPrice = totalPrice.multiply(BigDecimal.valueOf(releaseBook.getAmount().doubleValue()));
                            }

                            model.addRow(new Object[]{Integer.valueOf(++i), approvalBook.getReceiveDate(),
                                        approvalBook.getReceiveFrom(), approvalBook.getContractNumber(),
                                        approvalBook.getInvoiceNumber(), approvalBook.getInvoiceDate(),
                                        approvalBook.getAmount(), approvalBook.getItemName(),
                                        approvalBook.getPrice(), approvalBook.getDocumentNumber(),
                                        approvalBook.getDocumentDate(), approvalBook.getDescription(),
                                        releaseBook.getSortingNumber(),
                                        releaseBook.getReleaseDate(), releaseBook.getBillNumber(),
                                        releaseBook.getBillDate(), releaseBook.getAmount(),
                                        releaseBook.getItemName(), releaseBook.getPrice(), totalPrice,
                                        releaseBook.getAllotment(), releaseBook.getDelegateDate(),
                                        releaseBook.getDescription()});


                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null, null, null, null, null, null, null,null, 
                                    null, null, null, null, null, null,null,null,null});
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

        map.put("semester", semester);
        map.put("year", year);

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
