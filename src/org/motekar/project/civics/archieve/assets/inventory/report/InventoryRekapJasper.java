package org.motekar.project.civics.archieve.assets.inventory.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryRekap;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryRekapJasper extends SimpleAbstractJasper {

    private ArrayList<InventoryRekap> rekaps = new ArrayList<InventoryRekap>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private Unit unit = null;
    private ProfileAccount profileAccount;

    public InventoryRekapJasper(String title, ArrayList<InventoryRekap> lands, Unit unit, ProfileAccount profileAccount) {
        super(title);
        this.rekaps = lands;
        this.unit = unit;
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
                        String filename = "InventoryRekap.jrxml";
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
                    model.addColumn("ParentCode");
                    model.addColumn("ItemsCode");
                    model.addColumn("ItemsName");
                    model.addColumn("Amount");
                    model.addColumn("Price");
                    model.addColumn("Description");

                    int i = 0;

                    if (!rekaps.isEmpty()) {
                        for (InventoryRekap rekap : rekaps) {

                            int length = rekap.getItemCode().length();

                            Integer number = null;
                            String parentCode = null;
                            String itemsCode = null;
                            String itemsName = null;
                            Integer amount = null;
                            BigDecimal price = null;
                            String description = null;


                            if (length == 2) {
                                if (!rekap.getItemCode().equals("99")) {
                                    number = Integer.valueOf(rekap.getItemCode().substring(1, 2));
                                    parentCode = rekap.getItemCode();
                                    itemsName = rekap.getItemName().toUpperCase();
                                } else {
                                    number = null;
                                    parentCode = null;
                                    itemsName = "TOTAL";
                                }
                                itemsCode = null;
                                amount = null;
                            } else {
                                number = null;
                                parentCode = null;
                                itemsCode = rekap.getChildCode();
                                itemsName = rekap.getItemName();
                                amount = rekap.getTotal();
                            }
                            
                            price = rekap.getPrice();

                            model.addRow(new Object[]{number, parentCode, itemsCode,
                                        itemsName, amount, price, description});
                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null});
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

        map.put("locationcode", unit.getUnitCode());
        map.put("unitname", unit.getUnitName().toUpperCase());

        map.put("skpd", profileAccount.getCompany().toUpperCase());
        map.put("state", profileAccount.getState().toUpperCase());
        map.put("province", profileAccount.getProvince().toUpperCase());

        return map;
    }
}
