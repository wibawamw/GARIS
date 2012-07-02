package org.motekar.project.civics.archieve.report.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
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
import javax.swing.ButtonGroup;
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
import org.jdesktop.swingx.JXCollapsiblePane;
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
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.project.civics.archieve.master.objects.EmployeeFacility;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.report.sqlapi.ReportBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic mLogic;
    private ReportBusinessLogic rLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private JCheckBox checkFacility = new JCheckBox("Cetak Fasilitas Dinas");
    private JCheckBox checkCourses = new JCheckBox("Cetak Diklat Kepegawaian");
    private JCheckBox checkPayroll = new JCheckBox("Cetak Kenaikan Gaji Berkala");
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;
    private ArchieveProperties properties;

    public EmployeeReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.properties = mainframe.getProperties();
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
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
                + "Realisasi Anggaran merupakan daftar realisasi anggaran berdasarkan program dan kegiatan\n"
                + "Untuk melihat data Realisasi Anggaran pilih periode tahun anggaran serta tipe anggarannya.\n"
                + "Untuk melakukan cetak daftar Realisasi Anggaran, dari data yang telah difilter sebelumnya (panel dibawah) "
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

        ButtonGroup bg = new ButtonGroup();

        bg.add(checkCourses);
        bg.add(checkFacility);
        bg.add(checkPayroll);

        checkCourses.addActionListener(this);
        checkFacility.addActionListener(this);
        checkPayroll.addActionListener(this);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

    }

    private JPanel createSearchPanel() {

        FormLayout lm = new FormLayout(
                "pref,5px,fill:default:grow,250dlu",
                "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        CellConstraints cc = new CellConstraints();

        builder.add(checkCourses, cc.xyw(1, 1, 3));
        builder.add(checkFacility, cc.xyw(1, 3, 3));
        builder.add(checkPayroll, cc.xyw(1, 5, 3));

        return builder.getPanel();
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, panelViewer);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkCourses) {
            reloadPrintPanel();
        } else if (source == checkFacility) {
            reloadPrintPanel();
        } else if (source == checkPayroll) {
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

        private JasperReport loadReportCoursesFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "EmployeeCourses.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructCoursesModel(ArrayList<Employee> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Name");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("NIP");
            model.addColumn("CpnsTMT");
            model.addColumn("PnsTMT");
            model.addColumn("Rank");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("StageCourses");
            model.addColumn("FungCourses");
            model.addColumn("TechCourses");
            model.addColumn("Education");
            model.addColumn("Department");

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

                    StringBuilder title = new StringBuilder();

                    if (emp.isGorvernor()) {
                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            title.append("BUPATI ").append(properties.getState().toUpperCase());
                        } else {
                            title.append("WALIKOTA ").append(properties.getState().toUpperCase());
                        }
                    } else if (!emp.getName().equals("")) {
                        if (emp.getStrukturalAsString().equals("")) {
                            title.append(emp.getFungsionalAsString()).
                                    append(" ").append(emp.getPositionNotes());
                        } else {
                            title.append(emp.getStrukturalAsString());
                        }
                    }

                    ArrayList<EmployeeCourses> courseses = mLogic.getEmployeeCourseses(mainframe.getSession(), emp.getIndex());

                    String crs = "";
                    String crs2 = "";
                    String crs3 = "";


                    if (!courseses.isEmpty()) {
                        for (EmployeeCourses courses : courseses) {
                            if (courses.getCourses().equals(EmployeeCourses.PERJENJANG)) {
                                if (courses.isAttending()) {
                                    crs = "\u2713";
                                } else {
                                    crs = "";
                                }
                            } else if (courses.getCourses().equals(EmployeeCourses.FUNGSIONAL)) {
                                if (courses.isAttending()) {
                                    crs2 = "\u2713";
                                } else {
                                    crs2 = "";
                                }
                            } else if (courses.getCourses().equals(EmployeeCourses.TEKNIS)) {
                                if (courses.isAttending()) {
                                    crs3 = "\u2713";
                                } else {
                                    crs3 = "";
                                }
                            }
                        }
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getNip(), emp.getCpnsTMT(), emp.getPnsTMT(), emp.getPangkatAsString(),
                                emp.getGradeAsString(), emp.getGradeTMT(), title.toString(),
                                emp.getEselonTMT(), emp.getEselonAsString(), crs, crs2, crs3,
                                emp.getEducationAsString(), emp.getDepartment()});


                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private JasperReport loadReportFacilityFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "EmployeeFacility.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructFacilityModel(ArrayList<Employee> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Name");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("NIP");
            model.addColumn("CpnsTMT");
            model.addColumn("PnsTMT");
            model.addColumn("Rank");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("Houses");
            model.addColumn("Motorcycle");
            model.addColumn("Car");
            model.addColumn("Education");
            model.addColumn("Department");

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

                    StringBuilder title = new StringBuilder();

                    if (emp.isGorvernor()) {
                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            title.append("BUPATI ").append(properties.getState().toUpperCase());
                        } else {
                            title.append("WALIKOTA ").append(properties.getState().toUpperCase());
                        }
                    } else if (!emp.getName().equals("")) {
                        if (emp.getStrukturalAsString().equals("")) {
                            title.append(emp.getFungsionalAsString()).
                                    append(" ").append(emp.getPositionNotes());
                        } else {
                            title.append(emp.getStrukturalAsString());
                        }
                    }

                    ArrayList<EmployeeFacility> facilitys = mLogic.getEmployeeFacilitys(mainframe.getSession(), emp.getIndex());

                    String fcl = "";
                    String fcl2 = "";
                    String fcl3 = "";


                    if (!facilitys.isEmpty()) {
                        for (EmployeeFacility facility : facilitys) {
                            if (facility.getFacility().equals(EmployeeFacility.HOUSE)) {
                                if (facility.isOwned()) {
                                    fcl = "\u2713";
                                } else {
                                    fcl = "";
                                }
                            } else if (facility.getFacility().equals(EmployeeFacility.MOTORCYCLE)) {
                                if (facility.isOwned()) {
                                    fcl2 = "\u2713";
                                } else {
                                    fcl2 = "";
                                }
                            } else if (facility.getFacility().equals(EmployeeFacility.CAR)) {
                                if (facility.isOwned()) {
                                    fcl3 = "\u2713";
                                } else {
                                    fcl3 = "";
                                }
                            }
                        }
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getNip(), emp.getCpnsTMT(), emp.getPnsTMT(), emp.getPangkatAsString(),
                                emp.getGradeAsString(), emp.getGradeTMT(), title.toString(),
                                emp.getEselonTMT(), emp.getEselonAsString(), fcl, fcl2, fcl3,
                                emp.getEducationAsString(), emp.getDepartment()});


                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private JasperReport loadReportPayrollFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "EmployeePayroll.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructPayrollModel(ArrayList<Employee> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Name");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("NIP");
            model.addColumn("CpnsTMT");
            model.addColumn("PnsTMT");
            model.addColumn("Rank");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("Last");
            model.addColumn("nowtmt");
            model.addColumn("nexttmt");
            model.addColumn("year");
            model.addColumn("month");
            model.addColumn("year2");
            model.addColumn("month2");
            model.addColumn("Education");
            model.addColumn("Department");

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

                    StringBuilder title = new StringBuilder();

                    if (emp.isGorvernor()) {
                        if (properties.getStateType().equals(ArchieveProperties.KABUPATEN)) {
                            title.append("BUPATI ").append(properties.getState().toUpperCase());
                        } else {
                            title.append("WALIKOTA ").append(properties.getState().toUpperCase());
                        }
                    } else if (!emp.getName().equals("")) {
                        if (emp.getStrukturalAsString().equals("")) {
                            title.append(emp.getFungsionalAsString()).
                                    append(" ").append(emp.getPositionNotes());
                        } else {
                            title.append(emp.getStrukturalAsString());
                        }
                    }

                    Date now = new Date();

                    DateTime nowDate = new DateTime(now);
                    DateTime tmtCpns = new DateTime(emp.getCpnsTMT());
//                    DateTime tmtPns = new DateTime(emp.getPnsTMT());
                    DateTime tmtGrade = new DateTime(emp.getGradeTMT());

                    Period gradePeriod = new Period(tmtGrade, nowDate);

                    Integer lastYear = Integer.valueOf(0);

                    Date lastDate = null;
                    Date nextDate = null;

                    Integer year = gradePeriod.getYears();
                    Integer month = gradePeriod.getMonths();

                    Integer year2 = emp.getMkYear();
                    Integer month2 = emp.getMkMonth();

                    if (!year2.equals(Integer.valueOf(0))) {
                        if (emp.getGrade().equals(Employee.GRADE_IA) || emp.getGrade().equals(Employee.GRADE_IB)
                                || emp.getGrade().equals(Employee.GRADE_IC) || emp.getGrade().equals(Employee.GRADE_ID)
                                || emp.getGrade().equals(Employee.GRADE_IIIA) || emp.getGrade().equals(Employee.GRADE_IIIB)
                                || emp.getGrade().equals(Employee.GRADE_IIIC) || emp.getGrade().equals(Employee.GRADE_IIID)) {

                            // Genap
                            if ((year2 % 2) == 0) {
                                if (month2.equals(Integer.valueOf(0))) {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                } else {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusMonths(12 - month2);
                                    ld = ld.plusYears(1);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                }

                            } else {
                                if (month2.equals(Integer.valueOf(0))) {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusYears(1);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                } else {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusMonths(12 - month2);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                }
                            }
                        } else {
                            // Ganjil
                            if ((year2 % 2) == 0) {
                                if (month2.equals(Integer.valueOf(0))) {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusYears(1);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                } else {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusMonths(12 - month2);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                }

                            } else {
                                if (month2.equals(Integer.valueOf(0))) {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                } else {
                                    DateTime ld = tmtCpns.plusYears(year2);
                                    ld = ld.plusMonths(12 - month2);
                                    ld = ld.plusYears(1);
                                    lastYear = ld.getYear();
                                    lastDate = ld.toDate();
                                    ld = ld.plusYears(2);
                                    nextDate = ld.toDate();
                                }
                            }
                        }
                    }

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getNip(), emp.getCpnsTMT(), emp.getPnsTMT(), emp.getPangkatAsString(),
                                emp.getGradeAsString(), emp.getGradeTMT(), title.toString(),
                                emp.getEselonTMT(), emp.getEselonAsString(),
                                lastYear, lastDate, nextDate, year, month,
                                year2, month2, emp.getEducationAsString(), emp.getDepartment()});


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

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<Employee> employees = rLogic.getEmployeeReport(mainframe.getSession());

                if (!employees.isEmpty()) {
                    if (checkCourses.isSelected()) {
                        JasperReport jrReport = loadReportCoursesFile();
                        DefaultTableModel model = constructCoursesModel(employees);
                        Map param = constructParameter();
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else if (checkFacility.isSelected()) {
                        JasperReport jrReport = loadReportFacilityFile();
                        DefaultTableModel model = constructFacilityModel(employees);
                        Map param = constructParameter();
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else if (checkPayroll.isSelected()) {
                        JasperReport jrReport = loadReportPayrollFile();
                        DefaultTableModel model = constructPayrollModel(employees);
                        Map param = constructParameter();
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else {
                        jasperPrint = null;
                    }

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
                JXErrorPane.showDialog(EmployeeReportPanel.this, info);
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
