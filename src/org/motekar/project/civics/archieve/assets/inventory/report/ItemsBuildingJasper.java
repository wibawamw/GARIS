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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsBuildingJasper extends SimpleAbstractJasper {

    private ArrayList<ItemsBuilding> lands = new ArrayList<ItemsBuilding>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private Unit unit = null;

    public ItemsBuildingJasper(String title, ArrayList<ItemsBuilding> lands, Unit unit) {
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
                        String filename = "ItemsBuilding.jrxml";
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
                    model.addColumn("LocationCode");
                    model.addColumn("ItemsCode");
                    model.addColumn("RegNumber");
                    model.addColumn("Condition");
                    model.addColumn("Raised");
                    model.addColumn("Framework");
                    model.addColumn("BuildingWide");
                    model.addColumn("AddressLocation");
                    model.addColumn("DocumentDate");
                    model.addColumn("DocumentNumber");
                    model.addColumn("LandWide");
                    model.addColumn("LandStatus");
                    model.addColumn("LandCode");
                    model.addColumn("AcquisitionWay");
                    model.addColumn("Price");
                    model.addColumn("AdminCost");
                    model.addColumn("TotalPrice");
                    model.addColumn("Description");

                    int i = 0;

                    if (!lands.isEmpty()) {
                        for (ItemsBuilding building : lands) {

                            String condition = "";

                            if (building.getCondition() != null) {
                                condition = building.getCondition().getConditionName();
                            }

                            Unit unit = building.getUnit();
                            String locationCode = "";

                            if (unit != null) {
                                locationCode = unit.getUnitCode();
                            }


                            model.addRow(new Object[]{Integer.valueOf(++i), building.getItemName(),
                                        locationCode, building.getItemCode(),
                                        building.getRegNumber(), condition,
                                        building.getRaised(), building.getFrameworks(),
                                        building.getWide(), building.getAddress(),
                                        building.getDocumentDate(), building.getDocumentNumber(),
                                        building.getLandWide(), building.getLandState(),
                                        building.getLandCode(), building.getAcquitionWay(),
                                        building.getPrice(), building.getAdminCost(),
                                        building.getTotalPrice(), building.getDescription()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null, null, null, null, null, null, null, null,
                                    null, null, null, null,null,null});
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
