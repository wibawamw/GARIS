package org.motekar.project.civics.archieve.expedition.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionFollower;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.utils.report.SimpleAbstractJasper;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionFollowerJasper extends SimpleAbstractJasper {

    private ArrayList<ExpeditionFollower> followers = new ArrayList<ExpeditionFollower>();
    private JasperReport jasperReport;
    private DefaultTableModel models;

    private Date startDate;

    public ExpeditionFollowerJasper(ArrayList<ExpeditionFollower> followers,Date startDate) {
        this.followers = followers;
        this.startDate = startDate;
    }

    @Override
    public synchronized JasperReport loadReportFile() {
        try {
            SwingWorker<JasperReport, Void> worker = new SwingWorker<JasperReport, Void>() {

                @Override
                protected JasperReport doInBackground() throws Exception {
                    try {
                        String filename = System.getProperty("user.dir")+File.separator+File.separator+"printing"+File.separator+"ExpeditionFollower.jrxml";
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
                    model.addColumn("EmployeeName");
                    model.addColumn("Age");
                    model.addColumn("Notes");

                    int i = 0;

                    if (!followers.isEmpty()) {
                        for (ExpeditionFollower foll : followers) {

                            Employee emp = foll.getFollower();

                            DateTime date = new DateTime(emp.getBirthDate());
                            DateTime date2 = new DateTime(startDate);

                            Years yb = Years.yearsBetween(date, date2);

                            model.addRow(new Object[]{Integer.valueOf(++i), emp.getName(),
                                        String.valueOf(yb.getYears()),foll.getNotes()});
                        }
                    } else {
                        model.addRow(new Object[]{null, null,
                                        null,null});
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
        return new HashMap();
    }
}
