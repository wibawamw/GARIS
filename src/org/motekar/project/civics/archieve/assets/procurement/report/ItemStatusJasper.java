package org.motekar.project.civics.archieve.assets.procurement.report;

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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemStatus;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemStatusJasper extends SimpleAbstractJasper {

    private ArrayList<ItemStatus> itemStatuses = new ArrayList<ItemStatus>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("d MMMM yyyy", new Locale("in", "id", "id"));
    private Signer governorSigner;
    private ProfileAccount profileAccount;
    private String number = "";
    private Date date = null;

    public ItemStatusJasper(String title,  ArrayList<ItemStatus> approvalBooks, Signer governorSigner,ProfileAccount profileAccount,
            String number, Date date) {
        super(title);
        this.itemStatuses = approvalBooks;
        this.governorSigner = governorSigner;
        this.profileAccount = profileAccount;
        this.number = number;
        this.date = date;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "ItemStatus.jrxml";
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
                    model.addColumn("Specification");
                    model.addColumn("SpecificationNumber");
                    model.addColumn("Measure");
                    model.addColumn("Material");
                    model.addColumn("AcquisitionYear");
                    model.addColumn("ItemsCode");
                    model.addColumn("Amount");
                    model.addColumn("Price");
                    model.addColumn("Good");
                    model.addColumn("NotGood");
                    model.addColumn("Description");

                    int i = 0;

                    if (!itemStatuses.isEmpty()) {
                        for (ItemStatus itemStatus : itemStatuses) {

                            model.addRow(new Object[]{Integer.valueOf(++i), itemStatus.getItemName(),
                                        itemStatus.getMachineType(), itemStatus.getMachineNumber(),
                                        itemStatus.getMeasure(), itemStatus.getMaterial(),
                                        itemStatus.getAcquisitionYear(), itemStatus.getItemCode(),
                                        itemStatus.getAmount(),itemStatus.getPrice(),
                                        itemStatus.getGood(),itemStatus.getNotGood(),
                                        itemStatus.getDescription()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null,null, null,null, null,
                                        null, null, null,null,null,null,null});
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

        StringBuilder gorvernor = new StringBuilder();


        if (profileAccount != null) {
            if (profileAccount.getStateType().equals(ProfileAccount.KABUPATEN)) {
                gorvernor.append("BUPATI ").
                        append(profileAccount.getState().toUpperCase());
            } else {
                gorvernor.append("WALIKOTA ").
                        append(profileAccount.getState().toUpperCase());
            }
        }
        
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",new Locale("in", "id", "id"));
        
        map.put("governor", gorvernor.toString());
        map.put("instantionname", profileAccount.getCompany().toUpperCase());
        map.put("documentnumber", number);
        map.put("documentdate", sdf.format(date));
        
        if (governorSigner != null) {
            map.put("governorname", governorSigner.getSignerName());
        } else {
            map.put("governorname", "......................");
        }

        return map;
    }
}
