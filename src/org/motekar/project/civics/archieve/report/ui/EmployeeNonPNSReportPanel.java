package org.motekar.project.civics.archieve.report.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.report.sqlapi.ReportBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeNonPNSReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private ReportBusinessLogic rLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    //
    private JCheckBox checkEntryDate = new JCheckBox("TMT SK Masuk");
    private JCheckBox checkStartDate = new JCheckBox("TMT SK Awal");
    private JCheckBox checkEndDate = new JCheckBox("TMT SK Akhir");
    //
    private JXDatePicker startDate = new JXDatePicker();
    private JXDatePicker endDate = new JXDatePicker();
    private JXDatePicker startDate2 = new JXDatePicker();
    private JXDatePicker endDate2 = new JXDatePicker();
    private JXDatePicker startDate3 = new JXDatePicker();
    private JXDatePicker endDate3 = new JXDatePicker();
    //
    private JXButton btRefresh = new JXButton("Refresh");
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public EmployeeNonPNSReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        rLogic = new ReportBusinessLogic(mainframe.getConnection());
        construct();
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Data Urutan Kepangkatan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(panelViewer, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Pegawai non pns merupakan daftar pegawai non pns \n"
                + "Untuk melihat data Pegawai non pns pilih periode tahun anggaran serta tipe anggarannya.\n"
                + "Untuk melakukan cetak daftar Pegawai non pns, dari data yang telah difilter sebelumnya (panel dibawah) "
                + "pilih tombol bergambar printer. Sedang untuk menyimpannya dalam bentuk file seperti file excel(xls), "
                + "document(doc) maupun pdf klik tombol bergambar disket. Tombol tersebut tidak akan aktif jika data yang "
                + "difilter tidak terdapat di database.";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(350);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Cetak Data Urutan Kepangkatan");
        task.getContentPane().add(helpLabel);
        task.setAnimated(true);

        container.add(task);

        helpLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titledPanel.setContentContainer(scPane);

        return titledPanel;
    }

    private JXStatusBar createStatusBar() {
        JXStatusBar bar = new JXStatusBar();
        JXStatusBar.Constraint c1 = new JXStatusBar.Constraint(
                JXStatusBar.Constraint.ResizeBehavior.FILL);
        bar.add(statusLabel, c1);
        JXStatusBar.Constraint c2 = new JXStatusBar.Constraint();
        c2.setFixedWidth(300);
        bar.add(viewerBar, c2);

        viewerBar.setVisible(false);

        return bar;
    }

    private void construct() {

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.7)"
                + "(LEAF name=editor2 weight=0.3))";

        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createCenterComponent(), "editor1");
        splitPane.add(panel, "editor2");

        panel.setVisible(true);
        
        startDate.setFormats("dd/MM/yyyy");
        startDate2.setFormats("dd/MM/yyyy");
        startDate3.setFormats("dd/MM/yyyy");
        endDate.setFormats("dd/MM/yyyy");
        endDate2.setFormats("dd/MM/yyyy");
        endDate3.setFormats("dd/MM/yyyy");
        
        splitPane.setDividerSize(1);

        checkEntryDate.addActionListener(this);
        checkStartDate.addActionListener(this);
        checkEndDate.addActionListener(this);
        btRefresh.addActionListener(this);

        startDate.setEnabled(false);
        endDate.setEnabled(false);
        startDate2.setEnabled(false);
        endDate2.setEnabled(false);
        startDate3.setEnabled(false);
        endDate3.setEnabled(false);
        //
        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

    }

    private JPanel createSearchPanel() {

        FormLayout lm = new FormLayout(
                "pref,5px,fill:default:grow,5px,pref,5px,fill:default:grow,250dlu",
                "pref,5px,pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        JPanel btPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btPanel.add(btRefresh, null);

        CellConstraints cc = new CellConstraints();

        builder.add(checkEntryDate, cc.xy(1, 1));
        builder.add(startDate, cc.xy(3, 1));
        builder.addLabel("s.d.", cc.xy(5, 1));
        builder.add(endDate, cc.xy(7, 1));

        builder.add(checkStartDate, cc.xy(1, 3));
        builder.add(startDate2, cc.xy(3, 3));
        builder.addLabel("s.d.", cc.xy(5, 3));
        builder.add(endDate2, cc.xy(7, 3));

        builder.add(checkEndDate, cc.xy(1, 5));
        builder.add(startDate3, cc.xy(3, 5));
        builder.addLabel("s.d.", cc.xy(5, 5));
        builder.add(endDate3, cc.xy(7, 5));

        builder.add(btPanel, cc.xy(1, 7));

        return builder.getPanel();
    }

    private void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkEntryDate) {
            startDate.setEnabled(checkEntryDate.isSelected());
            endDate.setEnabled(checkEntryDate.isSelected());
        } else if (source == checkStartDate) {
            startDate2.setEnabled(checkStartDate.isSelected());
            endDate2.setEnabled(checkStartDate.isSelected());
        } else if (source == checkEndDate) {
            startDate3.setEnabled(checkEndDate.isSelected());
            endDate3.setEnabled(checkEndDate.isSelected());
        } else if (source == btRefresh) {
            reloadPrintPanel();
        }
    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            clearAll();
        }

        private void clearAll() {
            if (jasperPrint != null) {
                int size = jasperPrint.getPages().size();
                for (int i = 0; i < size; i++) {
                    jasperPrint.removePage(i);
                }
            }
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        private JasperReport loadReportFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "EmployeeNonPNS.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel(ArrayList<Employee> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Name");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("Education");
            model.addColumn("Department");
            model.addColumn("Workforce");
            model.addColumn("Religion");
            model.addColumn("EntryDate");
            model.addColumn("StartDate");
            model.addColumn("EndDate");
            model.addColumn("Description");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Employee emp = data.get(i);

                    String male = "";
                    String female = "";

                    if (emp.getSex().equals(Employee.MALE)) {
                        male = "\u2713";
                        female = "";
                    } else {
                        male = "";
                        female = "\u2713";
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getEducationAsString(), emp.getDepartment(),
                                emp.getWorkforceAsString(),
                                emp.getReligionAsString(), emp.getCpnsTMT(),
                                emp.getPnsTMT(), emp.getGradeTMT(), emp.getPositionNotes()});


                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private Map constructParameter() throws InterruptedException {
            Map param = new HashMap();

            setProgress(80);
            Thread.sleep(100L);

            return param;
        }

        private synchronized JasperPrint createJasperPrint(JasperReport jasperReport, DefaultTableModel model, Map param) throws InterruptedException {
            JasperPrint jrPrint = null;
            try {
                if (model != null) {
                    JRTableModelDataSource ds = new JRTableModelDataSource(model);
                    jrPrint = JasperFillManager.fillReport(jasperReport, param, ds);
                } else {
                    jrPrint = JasperFillManager.fillReport(jasperReport, param);
                }

            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
            return jrPrint;
        }

        @Override
        protected void process(List<Void> chunks) {
            mainframe.stopInActiveListener();
            viewerPanel.reload(jasperPrint);
        }

        private String createModifier() {
            StringBuilder query = new StringBuilder();

            if (checkEntryDate.isSelected()) {
                Date date = startDate.getDate();
                Date date2 = endDate.getDate();

                if (date != null && date2 != null) {
                    query.append("and ").append(" (cpnstmt between '").
                            append(new java.sql.Date(date.getTime())).append("' and '").
                            append(new java.sql.Date(date2.getTime())).
                            append("') ");

                    if (checkStartDate.isSelected() || checkEndDate.isSelected()) {
                        query.append("and ");
                    }
                }
            }

            if (checkStartDate.isSelected()) {
                Date date = startDate2.getDate();
                Date date2 = endDate2.getDate();
                if (date != null && date2 != null) {
                    
                    if (!checkEntryDate.isSelected()) {
                        query.append("and ");
                    }
                    
                    query.append(" (pnstmt between '").
                            append(new java.sql.Date(date.getTime())).append("' and '").
                            append(new java.sql.Date(date2.getTime())).
                            append("') ");

                    if (checkEndDate.isSelected()) {
                        query.append("and ");
                    }
                }
            }

            if (checkEndDate.isSelected()) {
                Date date = startDate3.getDate();
                Date date2 = endDate3.getDate();
                
                if (date != null && date2 != null) {
                    
                    if (!checkStartDate.isSelected()) {
                        query.append("and ");
                    }
                    
                    query.append(" (gradetmt between '").
                            append(new java.sql.Date(date.getTime())).append("' and '").
                            append(new java.sql.Date(date2.getTime())).
                            append("') ");

                }
            }

            return query.toString();
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<Employee> employees = rLogic.getEmployeeNonPNSReport(mainframe.getSession(), createModifier());

                if (!employees.isEmpty()) {
                    JasperReport jrReport = loadReportFile();
                    DefaultTableModel model = constructModel(employees);
                    Map param = constructParameter();
                    jasperPrint = createJasperPrint(jrReport, model, param);

                } else {
                    jasperPrint = null;
                }

                setProgress(100);
                publish();

                return jasperPrint;
            } catch (Exception anyException) {
                Exceptions.printStackTrace(anyException);
                exception = anyException;
                throw exception;
            }
        }

        @Override
        protected void done() {
            try {
                if (isCancelled()) {
                    return;
                }
                get();
            } catch (InterruptedException e) {
                //ignore
            } catch (ExecutionException e) {
                Exceptions.printStackTrace(e);
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(EmployeeNonPNSReportPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class JasperProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private JasperProgressListener() {
        }

        JasperProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
            this.progressBar.setVisible(true);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String strPropertyName = evt.getPropertyName();
            if ("progress".equals(strPropertyName)) {
                progressBar.setIndeterminate(false);
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            } else if ("state".equals(strPropertyName) && printWorker.getState() == SwingWorker.StateValue.DONE) {
                this.progressBar.setVisible(false);
                this.progressBar.setValue(0);
            }
        }
    }
}
