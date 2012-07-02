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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsConstructionJasper extends SimpleAbstractJasper {

    private ArrayList<ItemsConstruction> lands = new ArrayList<ItemsConstruction>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private Unit unit = null;

    public ItemsConstructionJasper(String title, ArrayList<ItemsConstruction> lands, Unit unit) {
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
                        String filename = "ItemsConstruction.jrxml";
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
                    model.addColumn("BuildingCategory");
                    model.addColumn("Raised");
                    model.addColumn("Framework");
                    model.addColumn("Wide");
                    model.addColumn("AddressLocation");
                    model.addColumn("DocumentDate");
                    model.addColumn("DocumentNumber");
                    model.addColumn("StartDate");
                    model.addColumn("LandStatus");
                    model.addColumn("LandCode");
                    model.addColumn("AcquisitionWay");
                    model.addColumn("Price");
                    model.addColumn("Description");

                    int i = 0;

                    if (!lands.isEmpty()) {
                        for (ItemsConstruction construction : lands) {

                            model.addRow(new Object[]{Integer.valueOf(++i), construction.getItemName(),
                                        construction.getItemCode(),construction.getRegNumber(), 
                                        construction.getBuildingCategory(), construction.getRaised(),
                                        construction.getFrameWork(), construction.getBuildingWide(),
                                        construction.getAddressLocation(), construction.getDocumentDate(),
                                        construction.getDocumentNumber(), construction.getStartDate(),
                                        construction.getLandStatus(), construction.getLandCode(),
                                        construction.getFundingSource(), construction.getPrice(),
                                        construction.getDescription()});
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
