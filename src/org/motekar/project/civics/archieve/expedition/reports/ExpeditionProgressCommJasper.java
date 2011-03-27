package org.motekar.project.civics.archieve.expedition.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.AbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ExpeditionProgressCommJasper extends AbstractJasper {

    private Employee commander;
    private JasperReport jasperReport;
    private Map params = new HashMap();
    private ArchieveProperties properties;

    public ExpeditionProgressCommJasper(Employee commander, ArchieveProperties properties) {
        this.commander = commander;
        this.properties = properties;
        createJasperPrint();
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = "ExpeditionProgressComm.jrxml";
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

                        StringBuilder gorvernorname = new StringBuilder();

                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            gorvernorname.append("BUPATI ").
                                    append(properties.getState().toUpperCase());
                        } else {
                            gorvernorname.append("WALIKOTA ").
                                    append(properties.getState().toUpperCase());
                        }

                        param.put("ApproverName", commander.getName());
                        param.put("ApproverTitle", gorvernorname.toString());
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
