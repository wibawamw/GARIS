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
import org.motekar.project.civics.archieve.assets.procurement.objects.InventoryCard;
import org.motekar.project.civics.archieve.assets.procurement.objects.ItemCardMap;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemCardJasper extends SimpleAbstractJasper {

    private ArrayList<ItemCardMap> itemCardMaps = new ArrayList<ItemCardMap>();
    private InventoryCard inventoryCard = null;
    private JasperReport jasperReport;
    private DefaultTableModel models;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));
    private ProfileAccount profileAccount;
    private Approval approval;
    private Signer commSigner;
    private Signer signer;

    public ItemCardJasper(String title, ArrayList<ItemCardMap> itemCardMaps, InventoryCard inventoryCard, ProfileAccount profileAccount, Approval approval, Signer signer, Signer commSigner) {
        super(title);
        this.itemCardMaps = itemCardMaps;
        this.inventoryCard = inventoryCard;
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
                        String filename = "ItemCard.jrxml";
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
                    model.addColumn("TransDate");
                    model.addColumn("Document");
                    model.addColumn("Argument");
                    model.addColumn("Accepted");
                    model.addColumn("Released");
                    model.addColumn("Residual");
                    model.addColumn("Price");
                    model.addColumn("Increased");
                    model.addColumn("Decreased");
                    model.addColumn("Remains");
                    model.addColumn("Description");

                    int i = 0;

                    if (!itemCardMaps.isEmpty()) {
                        for (ItemCardMap itemCardMap : itemCardMaps) {

                            StringBuilder str = new StringBuilder();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                                    new Locale("in", "id", "id"));

                            if (!itemCardMap.getDocumentNumber().equals("")) {
                                str.append(itemCardMap.getDocumentNumber());
                            }

                            if (itemCardMap.getDocumentDate() != null) {
                                if (!itemCardMap.getDocumentNumber().equals("")) {
                                    str.append("\n");
                                    str.append(sdf.format(itemCardMap.getDocumentDate()));
                                } else {
                                    str.append(sdf.format(itemCardMap.getDocumentDate()));
                                }

                            }

                            Integer accepted = Integer.valueOf(0);
                            Integer released = Integer.valueOf(0);

                            if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) > 0) {
                                accepted = itemCardMap.getAmount();
                            } else if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) < 0) {
                                released = Math.abs(itemCardMap.getAmount());
                            }

                            BigDecimal added = BigDecimal.ZERO;
                            added = added.add(itemCardMap.getPrice());
                            BigDecimal subs = BigDecimal.ZERO;
                            subs = subs.add(itemCardMap.getPrice());

                            if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) > 0) {
                                added = added.multiply(BigDecimal.valueOf(itemCardMap.getAmount()));
                                subs = BigDecimal.ZERO;
                            } else if (itemCardMap.getAmount().compareTo(Integer.valueOf(0)) < 0) {
                                added = BigDecimal.ZERO;
                                subs = subs.multiply(BigDecimal.valueOf(Math.abs(itemCardMap.getAmount())));
                            }

                            BigDecimal rsd = BigDecimal.ZERO;
                            rsd = rsd.add(itemCardMap.getPrice());
                            rsd = rsd.multiply(BigDecimal.valueOf(itemCardMap.getResidual()));

                            model.addRow(new Object[]{itemCardMap.getItemsDate(),
                                        str.toString(), itemCardMap.getArgument(),
                                        accepted, released, itemCardMap.getResidual(),
                                        itemCardMap.getPrice(), added, subs, rsd, 
                                        itemCardMap.getDescription()});
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

        if (inventoryCard != null) {
            map.put("warehouse", inventoryCard.getWarehouse().getUnitName());
            map.put("itemsname", inventoryCard.getItemName());
            map.put("itemsunit", inventoryCard.getItemsUnit());
            map.put("cardnumber", inventoryCard.getCardNumber());
            map.put("specification", inventoryCard.getSpecification());
        }

        return map;
    }
}
