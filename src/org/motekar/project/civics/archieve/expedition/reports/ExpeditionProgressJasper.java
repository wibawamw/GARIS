package org.motekar.project.civics.archieve.expedition.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionProgressJasper extends AbstractJasper {

    private Employee commander;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ArchieveProperties properties;
    private Expedition expedition;

    public ExpeditionProgressJasper(Expedition expedition,Employee commander, ArchieveProperties properties) {
        this.commander = commander;
        this.properties = properties;
        this.expedition = expedition;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "ExpeditionProgress.jrxml";
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
        return null;
    }

    @Override
    public synchronized Map constructParameter() {
        try {
            SwingWorker<Map, Void> worker = new SwingWorker<Map, Void>() {

                @Override
                protected Map doInBackground() throws Exception {
                    Map param = new HashMap();
                    if (commander != null) {

                        StringBuilder pos = new StringBuilder();
                        if (commander.getStrukturalAsString().equals("")) {
                            pos.append(commander.getFungsionalAsString()).
                                    append(" ").append(commander.getPositionNotes());
                        } else {
                            pos.append(commander.getStrukturalAsString());
                            if (pos.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                pos.append(" ").append(properties.getStateType()).
                                        append(" ").append(properties.getState());
                            } else if (pos.toString().equalsIgnoreCase(Employee.SEKRETARIS_DAERAH)) {
                                pos.append(" ").append(properties.getStateType()).
                                        append(" ").append(properties.getState());
                            } else if (pos.toString().equalsIgnoreCase(Employee.KEPALA_DINAS)
                                    || pos.toString().equalsIgnoreCase(Employee.KEPALA_BADAN)) {
                                pos.append(" ").append(properties.getCompany());
                            }
                        }

                        param.put("ApproverName", commander.getName());
                        param.put("ApproverNIP", commander.getNip());
                        param.put("ApproverTitle", pos.toString().toUpperCase());

                        param.put("departure", expedition.getDeparture());
                        param.put("destination", expedition.getDestination());
                        param.put("startdate", expedition.getStartDate());

                        StringBuilder stateName = new StringBuilder();

                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            stateName.append(ArchieveProperties.KABUPATEN.toUpperCase()).append(" ").
                                    append(properties.getState().toUpperCase());
                        }

                        param.put("statename", stateName.toString());
                    }
                    return param;
                }
            };
            worker.execute();
            params = worker.get();

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return params;
    }
}
