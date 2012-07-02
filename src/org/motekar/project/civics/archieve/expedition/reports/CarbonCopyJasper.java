package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class CarbonCopyJasper extends SimpleAbstractJasper {

    private ArrayList<String> copys = new ArrayList<String>();
    private JasperReport jasperReport;
    private DefaultTableModel models;

    public CarbonCopyJasper(ArrayList<String> copys) {
        this.copys = copys;
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = System.getProperty("user.dir")+File.separator+File.separator+"printing"+File.separator+"CarbonCopy.jrxml";
                        jasperReport = JasperCompileManager.compileReport(filename);
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
                    model.addColumn("CopyTo");

                    int i = 0;

                    if (!copys.isEmpty()) {
                        for (String copy : copys) {
                            model.addRow(new Object[]{Integer.valueOf(++i), copy});
                        }
                    } else {
                        model.addRow(new Object[]{null, null,
                                    null, null});
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

    private String initCaps(String str) {
        StringBuilder caps = new StringBuilder();

        String buff = str.toLowerCase();

        StringTokenizer token = new StringTokenizer(buff," ");

        while (token.hasMoreElements()) {
            String s = token.nextToken();
            caps.append(s.substring(0, 1).toUpperCase());
            caps.append(s.substring(1));
            caps.append(" ");
        }

        caps.deleteCharAt(caps.length()-1);

        return caps.toString();
    }

    public JRTableModelDataSource getDataSource() {
        JRTableModelDataSource ds = new JRTableModelDataSource(constructModel());
        return ds;
    }

    @Override
    public synchronized Map constructParameter() {
        return new HashMap();
    }
}
