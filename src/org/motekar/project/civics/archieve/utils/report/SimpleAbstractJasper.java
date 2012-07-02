package org.motekar.project.civics.archieve.utils.report;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public abstract class SimpleAbstractJasper {

    private JasperPrint jasperPrint = null;
    
    protected String title = "";
    
    public SimpleAbstractJasper() {
    }
    
    public SimpleAbstractJasper(String title) {
        this.title = title;
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
    
    public void showReport() {
        ReportRibbonViewer viewer = new ReportRibbonViewer(title, jasperPrint);
        Point centerPoint = WindowUtils.getPointForCentering(viewer);
        viewer.setLocation(centerPoint.getLocation());
        viewer.setVisible(true);
    }
    
    public void exportToExcel(String path) {
         try {
            ReportExporter.exportReportXls(jasperPrint, path);
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            CustomOptionDialog.showDialog(null, "File '"+path+"' tidak ditemukan", "Perhatian", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void exportToPDF(String path) {
        try {
            ReportExporter.exportReport(jasperPrint, path);
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            CustomOptionDialog.showDialog(null, "File '"+path+"' tidak ditemukan", "Perhatian", JOptionPane.ERROR_MESSAGE);
        }
    }
}
