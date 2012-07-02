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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAssetRoom;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachineRoom;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class RoomInventoryJasper extends SimpleAbstractJasper {

    private ArrayList<Object> objects = new ArrayList<Object>();
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private Unit unit = null;
    private ProfileAccount profileAccount;
    private Room room;

    public RoomInventoryJasper(String title, ArrayList<Object> objects, Unit unit,Room room,ProfileAccount profileAccount) {
        super(title);
        this.objects = objects;
        this.unit = unit;
        this.room = room;
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
                        String filename = "RoomCard.jrxml";
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
                    model.addColumn("Spesification");
                    model.addColumn("FactoryNumber");
                    model.addColumn("Volume");
                    model.addColumn("Material");
                    model.addColumn("AcqusitionYear");
                    model.addColumn("ItemsCode");
                    model.addColumn("Price");
                    model.addColumn("Good");
                    model.addColumn("NotGood");
                    model.addColumn("Bad");
                    model.addColumn("Description");

                    int i = 0;

                    if (!objects.isEmpty()) {
                        for (Object object : objects) {

                            String itemsName = "";
                            String itemsCode = "";
                            String specification = "";
                            String factoryNumber = "";
                            Integer volume = null;
                            String material = "";
                            Integer acquisitionYear = null;
                            BigDecimal price = null;

                            String good = "";
                            String notGood = "";
                            String bad = "";
                            String description = "";


                            if (object instanceof ItemsMachineRoom) {
                                System.out.println("1");
                                ItemsMachine machine = ((ItemsMachineRoom) object).getMachine();

                                itemsName = machine.getItemName();
                                itemsCode = machine.getItemCode();
                                specification = machine.getMachineType();
                                factoryNumber = machine.getFactoryNumber();
                                volume = machine.getVolume();
                                material = machine.getMaterial();
                                acquisitionYear = machine.getAcquisitionYear();
                                price = machine.getPrice();

                                Condition condition = machine.getCondition();


                                if (condition != null) {
                                    if (condition.getConditionCode().equals("B")) {
                                        good = "\u2713";
                                        notGood = "";
                                        bad = "";
                                    } else if (condition.getConditionCode().equals("KB")) {
                                        good = "";
                                        notGood = "\u2713";
                                        bad = "";
                                    } else if (condition.getConditionCode().equals("RB")) {
                                        good = "";
                                        notGood = "";
                                        bad = "\u2713";
                                    } else {
                                        good = "";
                                        notGood = "";
                                        bad = "";
                                    }
                                } else {
                                    good = "";
                                    notGood = "";
                                    bad = "";
                                }

                                description = machine.getDescription();

                            } else if (object instanceof ItemsFixedAssetRoom) {
                                System.out.println("2");
                                ItemsFixedAsset fixedAsset = ((ItemsFixedAssetRoom) object).getFixedAsset();

                                itemsName = fixedAsset.getItemName();
                                itemsCode = fixedAsset.getItemCode();

                                if (!fixedAsset.getBookAuthor().equals("")) {
                                    specification = fixedAsset.getBookAuthor();
                                } else if (!fixedAsset.getArtAuthor().equals("")) {
                                    specification = fixedAsset.getArtAuthor();
                                } else if (!fixedAsset.getCattleType().equals("")) {
                                    specification = fixedAsset.getCattleType();
                                } else {
                                    specification = "";
                                }

                                factoryNumber = "";
                                volume = Integer.valueOf(0);
                                material = fixedAsset.getArtMaterial();
                                acquisitionYear = fixedAsset.getAcquisitionYear();
                                price = fixedAsset.getPrice();

                                Condition condition = fixedAsset.getCondition();
                                
                                if (condition != null) {
                                    if (condition.getConditionCode().equals("B")) {
                                        good = "\u2713";
                                        notGood = "";
                                        bad = "";
                                    } else if (condition.getConditionCode().equals("KB")) {
                                        good = "";
                                        notGood = "\u2713";
                                        bad = "";
                                    } else if (condition.getConditionCode().equals("RB")) {
                                        good = "";
                                        notGood = "";
                                        bad = "\u2713";
                                    } else {
                                        good = "";
                                        notGood = "";
                                        bad = "";
                                    }
                                } else {
                                    good = "";
                                    notGood = "";
                                    bad = "";
                                }

                                description = fixedAsset.getDescription();
                            }

                            model.addRow(new Object[]{Integer.valueOf(++i), itemsName,
                                        specification, factoryNumber,
                                        volume, material, acquisitionYear,
                                        itemsCode, price, good,
                                        notGood, bad,
                                        description});
                        }
                    } else {
                        model.addRow(new Object[]{null, null, null, null, null, null,
                                    null, null, null, null, null, null, null});
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
        map.put("roomname", room.getRoomName());
        
        map.put("skpd", profileAccount.getCompany().toUpperCase());
        map.put("state", profileAccount.getState().toUpperCase());
        map.put("province", profileAccount.getProvince().toUpperCase());

        return map;
    }
}
