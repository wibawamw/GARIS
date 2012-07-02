package org.motekar.project.civics.archieve.report.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.TableColumnExt;
import org.joda.time.DateTime;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.project.civics.archieve.master.objects.EmployeeFacility;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.report.sqlapi.ReportBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.motekar.util.user.objects.UserGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic mLogic;
    private ReportBusinessLogic rLogic;
    private AssetMasterBusinessLogic amLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private JXComboBox comboUnit = new JXComboBox();
    private JCheckBox checkFacility = new JCheckBox("Cetak Fasilitas Dinas");
    private JCheckBox checkCourses = new JCheckBox("Cetak Diklat Kepegawaian");
    private JCheckBox checkPayroll = new JCheckBox("Cetak Kenaikan Gaji Berkala");
    private LoadPrintPanel printWorker;
    private LoadEmployee worker;
    private ProgressListener progressListener;
    private JasperProgressListener jpListener;
    private ProfileAccount profileAccount;
    //
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private EmployeeTable table = new EmployeeTable();
    //
    private Unit unit = null;

    public EmployeeReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.profileAccount = mainframe.getProfileAccount();
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        rLogic = new ReportBusinessLogic(mainframe.getConnection());
        amLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }
    
    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            table.loadData("");
        } else {
            unit = mainframe.getUnit();
            String modifier = generateUnitModifier(unit);
            table.loadData(modifier);
            comboUnit.setSelectedItem(unit);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();
        
        if (unit != null) {
            query.append(" and unit = ").append(unit.getIndex());
        }

        return query.toString();
    }
    
    private void loadComboUnit() {
        try {
            SwingWorker<ArrayList<Unit>, Void> lw = new SwingWorker<ArrayList<Unit>, Void>() {

                @Override
                protected ArrayList<Unit> doInBackground() throws Exception {
                    ArrayList<Unit> units = amLogic.getUnit(mainframe.getSession());
                    if (!units.isEmpty()) {
                        for (Unit unit : units) {
                            unit.setHirarchiecal(true);
                        }
                    }
                    return units;
                }
            };
            lw.execute();
            ArrayList<Unit> units = lw.get();

            units.add(0, new Unit());
            comboUnit.setModel(new ListComboBoxModel<Unit>(units));
            AutoCompleteDecorator.decorate(comboUnit);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Data Urutan Kepangkatan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);

        collapasepanel.add(panelViewer, BorderLayout.CENTER);

        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Data", createDataViewPanel());
        tabbedPane.addTab("Cetak", collapasepanel);

        titledPanel.setContentContainer(tabbedPane);



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
        
        loadComboUnit();

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.7)"
                + "(LEAF name=editor2 weight=0.3))";

        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);


        splitPane.add(createCenterComponent(), "editor1");
        splitPane.add(panel, "editor2");
        
        comboUnit.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object object = comboUnit.getSelectedItem();
                if (object instanceof Unit) {
                    Unit units = (Unit) object;
                    String modifier = generateUnitModifier(units);
                    table.loadData(modifier);
                }
            }
        });

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
    
    protected JPanel createSearchPanel2() {
        FormLayout lm = new FormLayout(
                "pref,5px, fill:default:grow,20px", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Unit", cc.xy(1, 1));
        builder.add(comboUnit, cc.xyw(3, 1,1));

        return builder.getPanel();
    }

    private JXPanel createDataViewPanel() {
        JXPanel panel = new JXPanel();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createSearchPanel2(), BorderLayout.NORTH);
        panel.add(scPane, BorderLayout.CENTER);

        return panel;
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

    public void reloadPrintPanel(String modifier) {
        printWorker = new LoadPrintPanel(jasperPrint, panelViewer,modifier);
        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkCourses) {
            reloadPrintPanel(generateUnitModifier(unit));
        } else if (source == checkFacility) {
            reloadPrintPanel(generateUnitModifier(unit));
        } else if (source == checkPayroll) {
            reloadPrintPanel(generateUnitModifier(unit));
        }

    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private String modifier = "";

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel,String modifier) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.modifier = modifier;
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
            model.addColumn("NIP");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("Religion");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("MKYear");
            model.addColumn("MKMonth");
            model.addColumn("Department");
            model.addColumn("GraduateYear");
            model.addColumn("Education");
            model.addColumn("Mutation");
            model.addColumn("EmployeeStatus");
            model.addColumn("BUP");
            model.addColumn("CourseName");
            model.addColumn("YearAttended");
            model.addColumn("TotalHour");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Employee emp = data.get(i);
                    
                    emp.setCourseses(mLogic.getEmployeeCourseses(mainframe.getSession(), emp.getIndex()));

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

                    if (emp.getStrukturalAsString().equals("")) {
                        title.append(emp.getFungsionalAsString()).
                                append("\n").append(emp.getPositionNotes());
                    } else {
                        title.append(EmployeeTableModel.initCaps(emp.getStrukturalAsString())).
                                append(" ").append(emp.getPositionNotes());
                    }
                    
                    DateTime bup = new DateTime(emp.getBirthDate().getTime());
                    bup = bup.plusYears(56);
                    
                    EmployeeCourses diklat = EmployeeTableModel.getDiklat(emp);
                    String courseName = "";
                    Integer yearattended = null;
                    Integer totalHour = null;
                    
                    
                    if (diklat != null) {
                        courseName = diklat.getCoursesName();
                        yearattended = diklat.getYearAttended();
                        totalHour = diklat.getTotalHour();
                    } 

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(), emp.getNip(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getReligionAsString(), emp.getRuangAsString(),
                                emp.getGradeTMT(), title.toString(), emp.getEselonTMT(),
                                emp.getEselonAsString(), emp.getMkYear(), emp.getMkMonth(),
                                emp.getDepartment(), emp.getGraduatedYear(), emp.getEducationAsString(),
                                emp.getMutation(), emp.getEmployeeStatusAsString(),bup.getYear(),
                                courseName, yearattended, totalHour});


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
            model.addColumn("NIP");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("Religion");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("MKYear");
            model.addColumn("MKMonth");
            model.addColumn("Department");
            model.addColumn("GraduateYear");
            model.addColumn("Education");
            model.addColumn("Mutation");
            model.addColumn("EmployeeStatus");
            model.addColumn("BUP");
            model.addColumn("Houses");
            model.addColumn("Motorcycle");
            model.addColumn("Car");

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

                    if (emp.getStrukturalAsString().equals("")) {
                        title.append(emp.getFungsionalAsString()).
                                append("\n").append(emp.getPositionNotes());
                    } else {
                        title.append(EmployeeTableModel.initCaps(emp.getStrukturalAsString())).
                                append(" ").append(emp.getPositionNotes());
                    }
                    
                    DateTime bup = new DateTime(emp.getBirthDate().getTime());
                    bup = bup.plusYears(56);
                    
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

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(), emp.getNip(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getReligionAsString(), emp.getRuangAsString(),
                                emp.getGradeTMT(), title.toString(), emp.getEselonTMT(),
                                emp.getEselonAsString(), emp.getMkYear(), emp.getMkMonth(),
                                emp.getDepartment(), emp.getGraduatedYear(), emp.getEducationAsString(),
                                emp.getMutation(), emp.getEmployeeStatusAsString(),bup.getYear(),
                                fcl, fcl2, fcl3});


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
            model.addColumn("NIP");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("BirthPlace");
            model.addColumn("BirthDate");
            model.addColumn("Religion");
            model.addColumn("Grade");
            model.addColumn("GradeTMT");
            model.addColumn("Title");
            model.addColumn("EselonTMT");
            model.addColumn("Eselon");
            model.addColumn("MKYear");
            model.addColumn("MKMonth");
            model.addColumn("Department");
            model.addColumn("GraduateYear");
            model.addColumn("Education");
            model.addColumn("Mutation");
            model.addColumn("EmployeeStatus");
            model.addColumn("BUP");
            model.addColumn("Last");
            model.addColumn("nowtmt");
            model.addColumn("nexttmt");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Employee emp = data.get(i);
                    
                    emp.setCourseses(mLogic.getEmployeeCourseses(mainframe.getSession(), emp.getIndex()));

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

                    if (emp.getStrukturalAsString().equals("")) {
                        title.append(emp.getFungsionalAsString()).
                                append("\n").append(emp.getPositionNotes());
                    } else {
                        title.append(EmployeeTableModel.initCaps(emp.getStrukturalAsString())).
                                append(" ").append(emp.getPositionNotes());
                    }
                    
                    DateTime bup = new DateTime(emp.getBirthDate().getTime());
                    bup = bup.plusYears(56);
                    
                    DateTime tmtCpns = new DateTime(emp.getCpnsTMT());

                    Integer lastYear = Integer.valueOf(0);

                    Date lastDate = null;
                    Date nextDate = null;


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

                    model.addRow(new Object[]{Integer.valueOf(i + 1), emp.getName(), emp.getNip(),
                                male, female, emp.getBirthPlace(), emp.getBirthDate(),
                                emp.getReligionAsString(), emp.getRuangAsString(),
                                emp.getGradeTMT(), title.toString(), emp.getEselonTMT(),
                                emp.getEselonAsString(), emp.getMkYear(), emp.getMkMonth(),
                                emp.getDepartment(), emp.getGraduatedYear(), emp.getEducationAsString(),
                                emp.getMutation(), emp.getEmployeeStatusAsString(),bup.getYear(),
                                lastYear, lastDate, nextDate});


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

                ArrayList<Employee> employees = rLogic.getEmployeeReport(mainframe.getSession(),modifier);

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
            progressBar.setIndeterminate(true);
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

    private class EmployeeTable extends JXTable {

        private EmployeeTableModel model;

        public EmployeeTable() {
            model = new EmployeeTableModel();
            setModel(model);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            CustomColumnGroup group = new CustomColumnGroup("<html><center><b>Jenis<br>Kelamin</br></b></center>");
            group.add(colModel.getColumn(3));
            group.add(colModel.getColumn(4));

            CustomColumnGroup group2 = new CustomColumnGroup("<html><b>Pangkat</b>");
            group2.add(colModel.getColumn(7));
            group2.add(colModel.getColumn(8));

            CustomColumnGroup group3 = new CustomColumnGroup("<html><center><b>Jabatan</b></center>");
            group3.add(colModel.getColumn(9));
            group3.add(colModel.getColumn(10));

            CustomColumnGroup group4 = new CustomColumnGroup("<html><center><b>Masa<br>Kerja<br></b></center>");
            group4.add(colModel.getColumn(11));
            group4.add(colModel.getColumn(12));

            CustomColumnGroup group5 = new CustomColumnGroup("<html><center><b>Pelatihan Jabatan</b></center>");
            group5.add(colModel.getColumn(13));
            group5.add(colModel.getColumn(14));
            group5.add(colModel.getColumn(15));

            CustomColumnGroup group6 = new CustomColumnGroup("<html><center><b>Pendidikan</b></center>");
            group6.add(colModel.getColumn(17));
            group6.add(colModel.getColumn(18));

            CustomColumnGroup group7a = new CustomColumnGroup("<html><center><b>Kendaraan</b></center>");
            group7a.add(colModel.getColumn(23));
            group7a.add(colModel.getColumn(24));

            CustomColumnGroup group7 = new CustomColumnGroup("<html><center><b>Fasilitas Dinas</b></center>");
            group7.add(colModel.getColumn(22));
            group7.add(group7a);

            CustomColumnGroup group8 = new CustomColumnGroup("<html><center><b>Kenaikan Gaji Berkala</b></center>");
            group8.add(colModel.getColumn(25));
            group8.add(colModel.getColumn(26));
            group8.add(colModel.getColumn(27));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.addColumnGroup(group2);
            header.addColumnGroup(group3);
            header.addColumnGroup(group4);
            header.addColumnGroup(group5);
            header.addColumnGroup(group6);
            header.addColumnGroup(group7);
            header.addColumnGroup(group8);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void loadData(String modifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadEmployee(modifier);
            progressListener = new ProgressListener(viewerBar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public Employee getSelectedEmployee() {
            ArrayList<Employee> selectedEmployees = getSelectedEmployees();
            return selectedEmployees.get(0);
        }

        public ArrayList<Employee> getEmployees() {
            return model.getEmployees();
        }

        public ArrayList<Employee> getSelectedEmployees() {

            ArrayList<Employee> employees = new ArrayList<Employee>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                Employee employee = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 2);
                        if (obj instanceof Employee) {
                            employee = (Employee) obj;
                            employees.add(employee);
                        }
                    }
                }
            }

            return employees;
        }

        public void updateSelectedEmployee(Employee employee) {
            model.updateRow(getSelectedEmployee(), employee);
        }

        public void removeEmployee(ArrayList<Employee> employees) {
            if (!employees.isEmpty()) {
                for (Employee employee : employees) {
                    model.remove(employee);
                }
            }

        }

        public void addEmployee(ArrayList<Employee> employees) {
            if (!employees.isEmpty()) {
                for (Employee employee : employees) {
                    model.add(employee);
                }
            }
        }

        public void addEmployee(Employee employee) {
            model.add(employee);
        }

        public void insertEmptyEmployee() {
            addEmployee(new Employee());
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Boolean.class)) {
                return new DefaultTableRenderer(new CheckBoxProvider());
            } else if (columnClass.equals(BigDecimal.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.RIGHT);
            } else if (columnClass.equals(Integer.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(0);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
                amountDisplayFormat.setGroupingUsed(false);

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(JLabel.class)) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class EmployeeTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 28;
        private static final String[] COLUMNS = {"<html><b>No</b>",
            "<html><center><b>Nama</b></center>", "<html><center><b>NIP </b></center>",
            "<html><b>L</b>", "<html><b>P</b>", "<html><center><b>Tempat<br>Tanggal Lahir</br></b></center>",
            "<html><center><b>Agama</b></center>",
            "<html><center><b>Gol<br>Ruang</br></b></center>",
            "<html><b>TMT</b>", "<html><b>Nama</b>",
            "<html><b>TMT</b>", "<html><b>Thn</b>",
            "<html><b>Bln</b>", "<html><b>Nama</b>",
            "<html><b>Thn</b>", "<html><b>Jml Jam</b>",
            "<html><b>Eselon</b>",
            "<html><b>Nama</b>", "<html><center><b>Lulus Thn<br>Tingkat</br></b></center>",
            "<html><center><b>Catatan Mutasi<br>Kepegawaian</br></b></center>",
            "<html><center><b>Status<br>Kepegawaian</br></b></center>",
            "<html><b>BUP</b>", "<html><center><b>Rumah<br>Dinas</br></b></center>",
            "<html><b>Roda 2</b>", "<html><b>Roda 4</b>",
            "<html><center><b>Akhir</b></center>",
            "<html><center><b>TMT</b></center>",
            "<html><center><b>Berikut</b></center>"};
        private ArrayList<Employee> employees = new ArrayList<Employee>();

        public EmployeeTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3 || columnIndex == 4
                    || columnIndex == 22 || columnIndex == 23 || columnIndex == 24) {
                return Boolean.class;
            } else if (columnIndex == 0 || columnIndex == 11 || columnIndex == 12
                    || columnIndex == 14 || columnIndex == 15 || columnIndex == 21
                    || columnIndex == 25) {
                return Integer.class;
            } else if (columnIndex == 8 || columnIndex == 10 || columnIndex == 8
                    || columnIndex == 26 || columnIndex == 27) {
                return Date.class;
            } else if (columnIndex == 7 || columnIndex == 16 || columnIndex == 17
                    || columnIndex == 18 || columnIndex == 19 || columnIndex == 20) {
                return JLabel.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(ArrayList<Employee> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            employees.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(Employee employee) {
            insertRow(getRowCount(), employee);
        }

        public void insertRow(int row, Employee employee) {
            employees.add(row, employee);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(Employee oldEmployee, Employee newEmployee) {
            int index = employees.indexOf(oldEmployee);
            employees.set(index, newEmployee);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Employee employee) {
            int row = employees.indexOf(employee);
            employees.remove(employee);
            fireTableRowsDeleted(row, row);
        }

        public void clear() {
            setRowCount(0);
        }

        protected void setRowCount(int rowCount) {
            int old = getRowCount();
            if (old == rowCount) {
                return;
            }

            if (rowCount <= old) {
                employees.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                employees.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            employees.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (employees.get(i) == null) {
                    employees.set(i, new Employee());
                }
            }
        }

        public ArrayList<Employee> getEmployees() {
            return employees;
        }

        @Override
        public int getRowCount() {
            return employees.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Employee getEmployee(int row) {
            if (!employees.isEmpty()) {
                return employees.get(row);
            }
            return new Employee();
        }

        private static ArrayList<EmployeeCourses> getEmployeeCourseses(ArrayList<EmployeeCourses> master, Integer courseType) {
            ArrayList<EmployeeCourses> courseses = new ArrayList<EmployeeCourses>();

            if (!master.isEmpty()) {
                for (EmployeeCourses ec : master) {
                    if (ec.getCoursesType().equals(courseType)) {
                        courseses.add(ec);
                    }
                }
            }

            return courseses;
        }

        public static EmployeeCourses getDiklat(Employee employee) {
            ArrayList<EmployeeCourses> jabatan = new ArrayList<EmployeeCourses>();

            if (!employee.getStrukturalAsString().equals("")) {
                jabatan = getEmployeeCourseses(employee.getCourseses(), EmployeeCourses.JABATAN);
            } else {
                jabatan = getEmployeeCourseses(employee.getCourseses(), EmployeeCourses.FUNGSIONAL);
            }

            Comparator<EmployeeCourses> comparator = new Comparator<EmployeeCourses>() {

                public int compare(EmployeeCourses o1, EmployeeCourses o2) {
                    return o1.getYearAttended().compareTo(o2.getYearAttended());
                }
            };


            if (!jabatan.isEmpty()) {
                Collections.sort(jabatan, comparator);
                return jabatan.get(jabatan.size() - 1);
            }

            return null;
        }

        private Date getGajiBerkala(Employee emp) {
            Integer year2 = emp.getMkYear();
            Integer month2 = emp.getMkMonth();

            Date lastDate = null;
            DateTime tmtCpns = new DateTime(emp.getCpnsTMT());

            if (!year2.equals(Integer.valueOf(0))) {
                if (emp.getGrade().equals(Employee.GRADE_IA) || emp.getGrade().equals(Employee.GRADE_IB)
                        || emp.getGrade().equals(Employee.GRADE_IC) || emp.getGrade().equals(Employee.GRADE_ID)
                        || emp.getGrade().equals(Employee.GRADE_IIIA) || emp.getGrade().equals(Employee.GRADE_IIIB)
                        || emp.getGrade().equals(Employee.GRADE_IIIC) || emp.getGrade().equals(Employee.GRADE_IIID)) {

                    // Genap
                    if ((year2 % 2) == 0) {
                        if (month2.equals(Integer.valueOf(0))) {
                            DateTime ld = tmtCpns.plusYears(year2);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        } else {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusMonths(12 - month2);
                            ld = ld.plusYears(1);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        }

                    } else {
                        if (month2.equals(Integer.valueOf(0))) {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusYears(1);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        } else {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusMonths(12 - month2);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        }
                    }
                } else {
                    // Ganjil
                    if ((year2 % 2) == 0) {
                        if (month2.equals(Integer.valueOf(0))) {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusYears(1);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        } else {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusMonths(12 - month2);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        }

                    } else {
                        if (month2.equals(Integer.valueOf(0))) {
                            DateTime ld = tmtCpns.plusYears(year2);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        } else {
                            DateTime ld = tmtCpns.plusYears(year2);
                            ld = ld.plusMonths(12 - month2);
                            ld = ld.plusYears(1);
                            lastDate = ld.toDate();
                            ld = ld.plusYears(2);
                        }
                    }
                }
            }
            return lastDate;
        }

        public static String initCaps(String str) {
            StringBuilder caps = new StringBuilder();

            String buff = str.toLowerCase();

            StringTokenizer token = new StringTokenizer(buff, " ");

            while (token.hasMoreElements()) {
                String s = token.nextToken();
                caps.append(s.substring(0, 1).toUpperCase());
                caps.append(s.substring(1));
                caps.append(" ");
            }

            caps.deleteCharAt(caps.length() - 1);

            return caps.toString();
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Employee employee = getEmployee(row);
            EmployeeCourses diklat = getDiklat(employee);
            ArrayList<EmployeeFacility> facilitys = employee.getFacilitys();
            Date lastDate = getGajiBerkala(employee);
            switch (column) {
                case 0:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 1:
                    toBeDisplayed = employee;
                    break;
                case 2:
                    toBeDisplayed = employee.getNip();
                    break;
                case 3:
                    if (employee.getSex().equals(Employee.MALE)) {
                        toBeDisplayed = Boolean.TRUE;
                    } else {
                        toBeDisplayed = Boolean.FALSE;
                    }
                    break;
                case 4:
                    if (employee.getSex().equals(Employee.FEMALE)) {
                        toBeDisplayed = Boolean.TRUE;
                    } else {
                        toBeDisplayed = Boolean.FALSE;
                    }
                    break;
                case 5:
                    String birthPlace = employee.getBirthPlace();
                    Date birthDate = employee.getBirthDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "id", "id"));

                    if (birthDate != null) {
                        toBeDisplayed = birthPlace + ", " + sdf.format(birthDate);
                    } else if (!birthPlace.equals("")) {
                        toBeDisplayed = birthPlace;
                    } else {
                        toBeDisplayed = "";
                    }
                    break;
                case 6:
                    toBeDisplayed = employee.getReligionAsString();
                    break;
                case 7:
                    toBeDisplayed = employee.getRuangAsString();
                    break;
                case 8:
                    toBeDisplayed = employee.getGradeTMT();
                    break;
                case 9:
                    StringBuilder position = new StringBuilder();

                    if (employee.getStrukturalAsString().equals("")) {
                        position.append(employee.getFungsionalAsString()).
                                append(" ").append(employee.getPositionNotes());
                    } else {
                        position.append(initCaps(employee.getStrukturalAsString())).
                                append(" ").append(employee.getPositionNotes());
                    }
                    toBeDisplayed = position.toString();
                    break;
                case 10:
                    toBeDisplayed = employee.getEselonTMT();
                    break;
                case 11:
                    toBeDisplayed = employee.getMkYear();
                    break;
                case 12:
                    toBeDisplayed = employee.getMkMonth();
                    break;
                case 13:
                    if (diklat != null) {
                        toBeDisplayed = diklat.getCoursesName();
                    } else {
                        toBeDisplayed = "";
                    }
                    break;
                case 14:
                    if (diklat != null) {
                        toBeDisplayed = diklat.getYearAttended();
                    } else {
                        toBeDisplayed = null;
                    }
                    break;
                case 15:
                    if (diklat != null) {
                        toBeDisplayed = diklat.getTotalHour();
                    } else {
                        toBeDisplayed = null;
                    }
                    break;
                case 16:
                    toBeDisplayed = employee.getEselonAsString();
                    break;
                case 17:
                    toBeDisplayed = employee.getDepartment();
                    break;
                case 18:
                    Integer graduateYear = employee.getGraduatedYear();
                    String education = employee.getEducationAsString();

                    StringBuilder str = new StringBuilder();

                    if (graduateYear != null && !graduateYear.equals(Integer.valueOf(0))) {
                        str.append(graduateYear);
                    }

                    if (education != null && !education.equals("")) {
                        str.append(", ").append(education);
                    }

                    toBeDisplayed = str.toString();
                    break;
                case 19:
                    toBeDisplayed = employee.getMutation();
                    break;
                case 20:
                    toBeDisplayed = employee.getEmployeeStatusAsString();
                    break;
                case 21:
                    DateTime bd = new DateTime(employee.getBirthDate().getTime());
                    bd = bd.plusYears(56);
                    toBeDisplayed = bd.getYear();
                    break;
                case 22:
                    if (!facilitys.isEmpty()) {
                        for (EmployeeFacility ef : facilitys) {
                            if (ef.getFacility().equals(EmployeeFacility.HOUSE)) {
                                if (ef.isOwned()) {
                                    toBeDisplayed = Boolean.TRUE;
                                } else {
                                    toBeDisplayed = Boolean.FALSE;
                                }
                                break;
                            }
                        }
                    }
                    break;
                case 23:
                    if (!facilitys.isEmpty()) {
                        for (EmployeeFacility ef : facilitys) {
                            if (ef.getFacility().equals(EmployeeFacility.MOTORCYCLE)) {
                                if (ef.isOwned()) {
                                    toBeDisplayed = Boolean.TRUE;
                                } else {
                                    toBeDisplayed = Boolean.FALSE;
                                }
                                break;
                            }
                        }
                    }
                    break;
                case 24:
                    if (!facilitys.isEmpty()) {
                        for (EmployeeFacility ef : facilitys) {
                            if (ef.getFacility().equals(EmployeeFacility.CAR)) {
                                if (ef.isOwned()) {
                                    toBeDisplayed = Boolean.TRUE;
                                } else {
                                    toBeDisplayed = Boolean.FALSE;
                                }
                                break;
                            }
                        }
                    }
                    break;
                case 25:
                    DateTime ld = new DateTime(lastDate.getTime());
                    toBeDisplayed = ld.getYear();
                    break;
                case 26:
                    toBeDisplayed = lastDate;
                    break;
                case 27:
                    DateTime nd = new DateTime(lastDate.getTime());
                    nd = nd.plusYears(2);
                    toBeDisplayed = nd.toDate();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class CustomColumnGroup extends ColumnGroup {

        public CustomColumnGroup(TableCellRenderer renderer, Object headerValue) {
            super(renderer, headerValue);
        }

        public CustomColumnGroup(Object headerValue) {
            super(headerValue);
        }

        @Override
        public Dimension getSize(JTable table) {
            Dimension size = new Dimension(0, 35);

            // Add the width for all visible TableColumns      
            if (columns != null) {
                for (TableColumn column : columns) {
                    if (column instanceof TableColumnExt) {
                        TableColumnExt columnExt = (TableColumnExt) column;
                        if (columnExt.isVisible()) {
                            size.width += column.getWidth();
                        }
                    } else {
                        size.width += column.getWidth();
                    }
                }
            }

            // Add the width for all ColumnGroups        
            if (groups != null) {
                for (ColumnGroup group : groups) {
                    size.width += group.getSize(table).width;
                }
            }
            return size;
        }
    }

    private class LoadEmployee extends SwingWorker<EmployeeTableModel, Employee> {

        private EmployeeTableModel model;
        private Exception exception;
        private String modifier = "";

        public LoadEmployee(String modifier) {
            this.model = (EmployeeTableModel) table.getModel();
            this.model.clear();
            this.modifier = modifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Employee> chunks) {
            mainframe.stopInActiveListener();
            for (Employee employee : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Pegawai " + employee.toString());
                model.add(employee);
            }
        }

        @Override
        protected EmployeeTableModel doInBackground() throws Exception {
            try {
                ArrayList<Employee> employees = rLogic.getEmployeeReport(mainframe.getSession(),modifier);
                double progress = 0.0;
                if (!employees.isEmpty()) {
                    for (int i = 0; i < employees.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / employees.size();

                        Employee employee = employees.get(i);

                        employee.setJustName(true);

                        employee.setCourseses(mLogic.getEmployeeCourseses(mainframe.getSession(), employee.getIndex()));
                        employee.setFacilitys(mLogic.getEmployeeFacilitys(mainframe.getSession(), employee.getIndex()));


                        setProgress((int) progress);
                        publish(employee);
                        Thread.sleep(100L);
                    }
                }
                return model;
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
                Exceptions.printStackTrace(e);
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(EmployeeReportPanel.this, info);
            }

            table.packAll();

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}
