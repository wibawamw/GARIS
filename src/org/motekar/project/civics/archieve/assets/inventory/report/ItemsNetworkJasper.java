package org.motekar.project.civics.archieve.assets.inventory.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsNetworkJasper extends SimpleAbstractJasper {

    private ArrayList<ItemsNetwork> lands = new ArrayList<ItemsNetwork>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private Unit unit = null;

    public ItemsNetworkJasper(String title, ArrayList<ItemsNetwork> lands, Unit unit) {
        super(title);
        this.lands = lands;
        this.unit = unit;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "ItemsNetwork.jrxml";
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
                    model.addColumn("ItemsCode");
                    model.addColumn("RegNumber");
                    model.addColumn("Construction");
                    model.addColumn("Length");
                    model.addColumn("Width");
                    model.addColumn("Wide");
                    model.addColumn("AddressLocation");
                    model.addColumn("DocumentDate");
                    model.addColumn("DocumentNumber");
                    model.addColumn("LandStatus");
                    model.addColumn("LandCode");
                    model.addColumn("AcquisitionWay");
                    model.addColumn("Price");
                    model.addColumn("Condition");
                    model.addColumn("Description");

                    int i = 0;

                    if (!lands.isEmpty()) {
                        for (ItemsNetwork network : lands) {

                            String condition = "";

                            if (network.getCondition() != null) {
                                condition = network.getCondition().getConditionName();
                            }


                            model.addRow(new Object[]{Integer.valueOf(++i), network.getItemName(),
                                        network.getItemCode(),network.getRegNumber(),
                                        network.getConstructionType(), network.getLength(),
                                        network.getWidth(), network.getWide(),
                                        network.getAddressLocation(), network.getDocumentDate(),
                                        network.getDocumentNumber(), network.getLandStatus(),
                                        network.getLandCode(), network.getAcquisitionWay(),
                                        network.getPrice(), condition, network.getDescription()});
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

        map.put("locationcode", unit.getUnitCode());
        map.put("unitname", unit.getUnitName().toUpperCase());

        return map;
    }
}
