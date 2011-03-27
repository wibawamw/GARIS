package org.motekar.project.civics.archieve.utils.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public abstract class SimpleAbstractJasper {

    private JasperPrint jasperPrint = null;

    public SimpleAbstractJasper() {
    }

    protected void createJasperPrint() {

        try {
            DefaultTableModel model = constructModel();

            if (model != null) {
                JRTableModelDataSource ds = new JRTableModelDataSource(constructModel());
                jasperPrint = JasperFillManager.fillReport(loadReportFile(), constructParameter(), ds);
            } else {
                jasperPrint = JasperFillManager.fillReport(loadReportFile(), constructParameter());
            }

        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public JRPrintPage getPage() {
        if (jasperPrint == null) {
            return null;
        }

        List list = jasperPrint.getPages();

        return (JRPrintPage) list.get(0);
    }

    public ArrayList<JRPrintPage> getPages() {
        if (jasperPrint == null) {
            return null;
        }

        List pageList = jasperPrint.getPages();
        ArrayList<JRPrintPage> aList = new ArrayList<JRPrintPage>();

        if (!pageList.isEmpty()) {
            for (int i = 0; i < pageList.size(); i++) {
                aList.add((JRPrintPage) pageList.get(i));
            }
        }


        return aList;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public abstract JasperReport loadReportFile();

    public abstract DefaultTableModel constructModel();

    public abstract Map constructParameter();
}
