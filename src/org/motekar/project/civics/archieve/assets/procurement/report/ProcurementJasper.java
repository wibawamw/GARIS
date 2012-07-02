package org.motekar.project.civics.archieve.assets.procurement.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.procurement.objects.Procurement;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ProcurementJasper extends SimpleAbstractJasper {

    private ArrayList<Procurement> procurements = new ArrayList<Procurement>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("d MMMM yyyy", new Locale("in", "id", "id"));
    private Date date = null;
    private Date date2 = null;
    private ProfileAccount profileAccount;
    private Approval approval;
    private Signer signer;

    public ProcurementJasper(String title,  ArrayList<Procurement> procurements, Date date, Date date2,ProfileAccount profileAccount,Approval approval,Signer signer) {
        super(title);
        this.procurements = procurements;
        this.date = date;
        this.date2 = date2;
        this.profileAccount = profileAccount;
        this.approval = approval;
        this.signer = signer;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "Procurement.jrxml";
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
                    model.addColumn("ItemsName");
                    model.addColumn("ContractDate");
                    model.addColumn("ContractNumber");
                    model.addColumn("DocumentDate");
                    model.addColumn("DocumentNumber");
                    model.addColumn("Amount");
                    model.addColumn("Price");
                    model.addColumn("TotalPrice");
                    model.addColumn("UnitName");
                    model.addColumn("Description");

                    int i = 0;

                    if (!procurements.isEmpty()) {
                        for (Procurement procurement : procurements) {

                            BigDecimal totalPrice = BigDecimal.ZERO;
                            if (procurement.getPrice() != null) {
                                totalPrice = totalPrice.add(procurement.getPrice());
                            }
                            if (procurement.getAmount() != null) {
                                totalPrice = totalPrice.multiply(BigDecimal.valueOf(procurement.getAmount().doubleValue()));
                            }
                            
                            Unit unit = procurement.getUnit();
                            
                            String unitName = "";
                            
                            if (unit != null) {
                                unitName = unit.getUnitName();
                            }

                            model.addRow(new Object[]{Integer.valueOf(++i), procurement.getItemName(),
                                        procurement.getContractDate(), procurement.getContractNumber(),
                                        procurement.getDocumentDate(), procurement.getDocumentNumber(),
                                        procurement.getAmount(), procurement.getPrice(), totalPrice,
                                        unitName,procurement.getDescription()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null,null, null,null, null,
                                        null, null, null,null,null});
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

        StringBuilder builder = new StringBuilder();

        if (date != null && date2 != null) {
            builder.append("Dari Tgl. ").append(dateFormat2.format(date)).
                    append(" s.d. ").append(dateFormat2.format(date2));
        }

        if (builder.toString().equals("")) {
            map.put("periode", null);
        } else {
            map.put("periode", builder.toString());
        }
        
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

        return map;
    }
}
