package org.motekar.project.civics.archieve.expedition.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private ExpeditionBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private JXDatePicker startDate = new JXDatePicker();
    private JXDatePicker endDate = new JXDatePicker();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JYearChooser yearChooser = new JYearChooser();
    private JYearChooser yearChooser2 = new JYearChooser();
    private JXComboBox comboEmployee = new JXComboBox();
    private JCheckBox checkBox = new JCheckBox();
    private JCheckBox checkBox2 = new JCheckBox();
    private JCheckBox checkBox3 = new JCheckBox();
    private JCheckBox checkBox4 = new JCheckBox("Pegawai yg Ditugaskan");
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public ExpeditionReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ExpeditionBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Perjalanan Dinas");

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
                + "Daftar Perjalanan Dinas merupakan daftar semua surat Perjalanan Dinas dan kondisi perjalanan dinas "
                + "pada dinas tertentu\n"
                + "Untuk melihat data perjalanan dinas pilih periode berdasarkan tanggal, berdasarkan bulan,"
                + "maupun berdasarkan bulan. Untuk memilih berdasarkan tanggal pilih ceklist paling atas kemudian "
                + "masukan tanggal misalkan dari tanggal 01/01/2010 s.d. 31/12/2010. Data akan langsung ditampilkan "
                + "pada panel dibawahnya. Kemudian jika ingin menampilkan data per bulan pilih ceklist kedua dengan "
                + "cara yang kurang lebih sama dengan sebelumnya. Begitu juga jika ingin menampilkan data per tahun "
                + "pilih ceklist paling bawah.\n"
                + "Untuk melakukan cetak daftar perjalanan dinas, dari data yang telah difilter sebelumnya (panel dibawah) "
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
        task.setTitle("Cetak Daftar Perjalanan Dinas");
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
    
    private void loadComboEmployee() {
        comboEmployee.removeAllItems();
        try {
            ArrayList<Employee> employees = mLogic.getAssignedEmployee(mainframe.getSession());

            if (!employees.isEmpty()) {
                for (Employee e : employees) {
                    e.setStyled(false);
                }

                employees.add(0, new Employee());
                comboEmployee.setModel(new ListComboBoxModel<Employee>(employees));

                AutoCompleteDecorator.decorate(comboEmployee, new ExpeditionPanel.EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void construct() {
        
        loadComboEmployee();

        startDate.addActionListener(this);
        endDate.addActionListener(this);

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });

        yearChooser2.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });


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

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,pref,5px, pref,5px,pref,5px,fill:default:grow,5px,250dlu", 
                "pref,5px,pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        startDate.setEnabled(checkBox.isSelected());
        endDate.setEnabled(checkBox.isSelected());

        monthChooser.setYearChooser(yearChooser);
        monthChooser.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        monthChooser.setEnabled(checkBox2.isSelected());
        yearChooser.setEnabled(checkBox2.isSelected());
        yearChooser2.setEnabled(checkBox3.isSelected());
        comboEmployee.setEnabled(checkBox4.isSelected());

        checkBox.addActionListener(this);
        checkBox2.addActionListener(this);
        checkBox3.addActionListener(this);
        checkBox4.addActionListener(this);
        
        comboEmployee.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadPrintPanel();
                }
            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(checkBox);
        bg.add(checkBox2);
        bg.add(checkBox3);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Berdasarkan Tanggal", cc.xy(1, 1));
        builder.add(checkBox, cc.xy(3, 1));
        builder.add(startDate, cc.xy(5, 1));
        builder.addLabel("s.d.", cc.xy(7, 1));
        builder.add(endDate, cc.xyw(9, 1, 2));

        builder.addLabel("Berdasarkan Bulan", cc.xy(1, 3));
        builder.add(checkBox2, cc.xy(3, 3));
        builder.add(monthChooser, cc.xyw(5, 3, 4));
        builder.add(yearChooser, cc.xyw(9, 3, 2));

        builder.addLabel("Berdasarkan Tahun", cc.xy(1, 5));
        builder.add(checkBox3, cc.xy(3, 5));
        builder.add(yearChooser2, cc.xyw(5, 5, 5));
        
        builder.add(checkBox4, cc.xy(1, 7));
        builder.add(comboEmployee, cc.xyw(3, 7, 7));
        
        return builder.getPanel();
    }

    public void reloadPrintPanel() {
        if (checkBox.isSelected()) {
            printWorker = new LoadPrintPanel(jasperPrint, panelViewer, startDate.getDate(), endDate.getDate());
        } else if (checkBox2.isSelected()) {
            printWorker = new LoadPrintPanel(jasperPrint, panelViewer, monthChooser.getMonth()+1, yearChooser.getYear());
        } else {
            printWorker = new LoadPrintPanel(jasperPrint, panelViewer, yearChooser2.getYear());
        }

        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkBox) {
            startDate.setEnabled(checkBox.isSelected());
            endDate.setEnabled(checkBox.isSelected());
            monthChooser.setEnabled(checkBox2.isSelected());
            yearChooser.setEnabled(checkBox2.isSelected());
            yearChooser2.setEnabled(checkBox3.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox2) {
            startDate.setEnabled(checkBox.isSelected());
            endDate.setEnabled(checkBox.isSelected());
            monthChooser.setEnabled(checkBox2.isSelected());
            yearChooser.setEnabled(checkBox2.isSelected());
            yearChooser2.setEnabled(checkBox3.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox3) {
            startDate.setEnabled(checkBox.isSelected());
            endDate.setEnabled(checkBox.isSelected());
            monthChooser.setEnabled(checkBox2.isSelected());
            yearChooser.setEnabled(checkBox2.isSelected());
            yearChooser2.setEnabled(checkBox3.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox4) {
            comboEmployee.setEnabled(checkBox4.isSelected());
            reloadPrintPanel();
        } else if (source == startDate) {
            reloadPrintPanel();
        } else if (source == endDate) {
            reloadPrintPanel();
        } 
    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private Integer month = Integer.valueOf(0);
        private Integer year = Integer.valueOf(0);
        private Date startDate = null;
        private Date endDate = null;

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel, Integer month, Integer year) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.month = month;
            this.year = year;
            clearAll();
        }

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel, Integer year) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.year = year;
            clearAll();
        }

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel, Date startDate, Date endDate) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.startDate = startDate;
            this.endDate = endDate;
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

            String filename = "ExpeditionReport.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel(ArrayList<Expedition> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("DocNumber");
            model.addColumn("Nip");
            model.addColumn("name");
            model.addColumn("startdate");
            model.addColumn("enddate");
            model.addColumn("destination");
            model.addColumn("purpose");
            model.addColumn("status");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Expedition exp = data.get(i);

                    Employee assEmp = exp.getAssignedEmployee();
                    AssignmentLetter letter = exp.getLetter();

                    String status = "Belum\nSelesai";

                    if (exp.isDone()) {
                        status = "Selesai";
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), exp.getDocumentNumber(),
                                assEmp.getNip(), assEmp.getName(), exp.getStartDate(), exp.getEndDate(),
                                exp.getDestination(),letter.getPurpose(), status});

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

            SimpleDateFormat sdf;

            StringBuilder builder = new StringBuilder();

            if (startDate != null && endDate != null) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                builder.append(sdf.format(startDate)).
                        append(" s.d. ").
                        append(sdf.format(endDate));
            } else if (month != 0) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year, month-1, 1);
                builder.append(sdf.format(cal.getTime()));
            } else {
                builder.append(year);
            }

            param.put("periode", builder.toString());
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
        
        private JasperReport loadReportFile2() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "ExpeditionReport2.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel2(ArrayList<Expedition> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("DocNumber");
            model.addColumn("startdate");
            model.addColumn("enddate");
            model.addColumn("destination");
            model.addColumn("purpose");
            model.addColumn("status");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Expedition exp = data.get(i);

                    AssignmentLetter letter = exp.getLetter();

                    String status = "Belum\nSelesai";

                    if (exp.isDone()) {
                        status = "Selesai";
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), exp.getDocumentNumber(),
                                exp.getStartDate(), exp.getEndDate(),
                                exp.getDestination(),letter.getPurpose(), status});

                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private Map constructParameter2(Employee assinedEmployee) throws InterruptedException {
            Map param = new HashMap();

            SimpleDateFormat sdf;

            StringBuilder builder = new StringBuilder();

            if (startDate != null && endDate != null) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                builder.append(sdf.format(startDate)).
                        append(" s.d. ").
                        append(sdf.format(endDate));
            } else if (month != 0) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year, month-1, 1);
                builder.append(sdf.format(cal.getTime()));
            } else {
                builder.append(year);
            }

            param.put("periode", builder.toString());
            param.put("employee", assinedEmployee.getNip()+" "+assinedEmployee.getName());
            
            setProgress(80);
            Thread.sleep(100L);

            return param;
        }

        @Override
        protected void process(List<Void> chunks) {
            mainframe.stopInActiveListener();
            viewerPanel.reload(jasperPrint);
        }
        
        private String generateModifier(Employee employee) {
            StringBuilder builder = new StringBuilder();
            
            if (employee != null) {
                builder.append(" and assignedemployee = ").
                        append(employee.getIndex());
            }
            
            return builder.toString();
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

                Employee employee = null;
                Object selEmployee = comboEmployee.getSelectedItem();
                if (selEmployee instanceof Employee) {
                    employee = (Employee) selEmployee;
                }

                if (!checkBox4.isSelected()) {
                    employee = null;
                }

                if (employee != null) {
                    if (!employee.getIndex().equals(Long.valueOf(0))) {
                        if (startDate != null && endDate != null) {
                            expeditions = logic.getExpedition(mainframe.getSession(), startDate, endDate, generateModifier(employee));
                        } else if (month != 0 || year != 0) {
                            expeditions = logic.getExpedition(mainframe.getSession(), month, year, generateModifier(employee));
                        }
                    } else {
                        employee = null;
                        if (startDate != null && endDate != null) {
                            expeditions = logic.getExpedition(mainframe.getSession(), startDate, endDate, "");
                        } else if (month != 0 || year != 0) {
                            expeditions = logic.getExpedition(mainframe.getSession(), month, year, "");
                        }
                    }

                } else {
                    if (startDate != null && endDate != null) {
                        expeditions = logic.getExpedition(mainframe.getSession(), startDate, endDate, "");
                    } else if (month != 0 || year != 0) {
                        expeditions = logic.getExpedition(mainframe.getSession(), month, year, "");
                    }
                }


                if (!expeditions.isEmpty()) {

                    if (checkBox4.isSelected() && employee != null) {
                        JasperReport jrReport = loadReportFile2();
                        DefaultTableModel model = constructModel2(expeditions);
                        Map param = constructParameter2(employee);
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else {
                        JasperReport jrReport = loadReportFile();
                        DefaultTableModel model = constructModel(expeditions);
                        Map param = constructParameter();
                        jasperPrint = createJasperPrint(jrReport, model, param);
                        
                    }


                } else {
                    jasperPrint = null;
                }

                publish();

                setProgress(100);

                return jasperPrint;
            } catch (Exception anyException) {
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
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(ExpeditionReportPanel.this, info);
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
