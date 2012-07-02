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
public abstract class AbstractJasper {

    private JasperPrint jasperPrint = null;

    public AbstractJasper() {
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    protected void createJasperPrint() {
        try {
            DefaultTableModel model = constructModel();

            if (model != null) {
                JRTableModelDataSource ds = new JRTableModelDataSource(model);
                jasperPrint = JasperFillManager.fillReport(loadReportFile(), constructParameter(), ds);
            } else {
                jasperPrint = JasperFillManager.fillReport(loadReportFile(), constructParameter());
            }

        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void insertPages(ArrayList<JRPrintPage> pages) {
        if (!pages.isEmpty()) {
            if (jasperPrint != null) {
                for (JRPrintPage page : pages) {
                    jasperPrint.addPage(page);
                }
            }
        }
    }

    public void insertPages(ArrayList<JRPrintPage> pages, int index) {
        int i = index;
        if (!pages.isEmpty()) {
            if (jasperPrint != null) {
                for (JRPrintPage page : pages) {
                    jasperPrint.addPage(i++, page);
                }
            }
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

    public abstract JasperReport loadReportFile();

    public abstract DefaultTableModel constructModel();

    public abstract Map constructParameter();
}
