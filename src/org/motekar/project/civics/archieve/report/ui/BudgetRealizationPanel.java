package org.motekar.project.civics.archieve.report.ui;

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
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.report.object.BudgetRealization;
import org.motekar.project.civics.archieve.report.sqlapi.ReportBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetRealizationPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private ReportBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private JYearChooser yearChooser = new JYearChooser();
    private JXComboBox comboBudgetType = new JXComboBox();
    //
    private JXComboBox comboActivity = new JXComboBox();
    private JXComboBox comboEselon = new JXComboBox();
    //
    private JCheckBox checkBox = new JCheckBox("Kegiatan");
    private JCheckBox checkBox2 = new JCheckBox("Eselon");
    //
    private JXDatePicker startDate = new JXDatePicker();
    private JXDatePicker endDate = new JXDatePicker();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JYearChooser yearChooser2 = new JYearChooser();
    private JCheckBox checkBox3 = new JCheckBox("Tanggal");
    private JCheckBox checkBox4 = new JCheckBox("Bulan");
    //
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private BudgetRealizationPanel.LoadPrintPanel printWorker;
    private BudgetRealizationPanel.JasperProgressListener jpListener;

    public BudgetRealizationPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ReportBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Realisasi Anggaran");

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
        task.setTitle("Cetak Realisasi Anggaran");
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

        loadBudgetType();
        loadComboActivity();
        loadEselon();

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });

        comboBudgetType.setAction(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                reloadPrintPanel();
            }
        });

        comboActivity.setEnabled(checkBox.isSelected());
        comboEselon.setEnabled(checkBox2.isSelected());
        startDate.setEnabled(checkBox3.isSelected());
        endDate.setEnabled(checkBox3.isSelected());
        monthChooser.setEnabled(checkBox4.isSelected());
        yearChooser2.setEnabled(checkBox4.isSelected());


        startDate.setFormats("dd/MM/yyyy");
        endDate.setFormats("dd/MM/yyyy");

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });

        yearChooser2.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reloadPrintPanel();
            }
        });

        checkBox.addActionListener(this);
        checkBox2.addActionListener(this);
        checkBox3.addActionListener(this);
        checkBox4.addActionListener(this);
        startDate.addActionListener(this);
        endDate.addActionListener(this);

        comboActivity.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadPrintPanel();
                }
            }
        });

        comboEselon.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadPrintPanel();
                }
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
                "pref,5px,fill:default:grow,30px,pref,5px,pref,5px,pref,5px,fill:default:grow,70dlu",
                "pref,1px,pref,1px,pref,1px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tahun Anggaran ", cc.xy(1, 1));
        builder.add(yearChooser, cc.xyw(3, 1, 1));

        builder.addLabel("Tipe Anggaran ", cc.xy(1, 3));
        builder.add(comboBudgetType, cc.xyw(3, 3, 1));

        builder.add(checkBox, cc.xy(5, 1));
        builder.add(comboActivity, cc.xyw(7, 1, 5));

        builder.add(checkBox2, cc.xy(5, 3));
        builder.add(comboEselon, cc.xyw(7, 3, 5));

        builder.add(checkBox3, cc.xy(5, 5));
        builder.add(startDate, cc.xyw(7, 5, 1));
        builder.addLabel("s.d. ", cc.xyw(9, 5, 1));
        builder.add(endDate, cc.xyw(11, 5, 1));

        builder.add(checkBox4, cc.xy(5, 7));
        builder.add(monthChooser, cc.xyw(7, 7, 1));
        builder.addLabel("Tahun", cc.xyw(9, 7, 1));
        builder.add(yearChooser2, cc.xyw(11, 7, 1));

        return builder.getPanel();
    }

    private void loadBudgetType() {

        ArrayList<String> budgetTypes = new ArrayList<String>();

        budgetTypes.add(0, " ");
        budgetTypes.add("APBD");
        budgetTypes.add("APBD Perubahan");

        comboBudgetType.setModel(new ListComboBoxModel<String>(budgetTypes));
        AutoCompleteDecorator.decorate(comboBudgetType);

        comboBudgetType.setSelectedIndex(1);
    }

    private void loadComboActivity() {
        try {
            ArrayList<Activity> activitys = mLogic.getActivity(mainframe.getSession());

            activitys.add(0, new Activity());
            comboActivity.setModel(new ListComboBoxModel<Activity>(activitys));
            AutoCompleteDecorator.decorate(comboActivity);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadEselon() {
        comboEselon.removeAllItems();
        comboEselon.setModel(new ListComboBoxModel<String>(StandardPrice.eselonAsList()));
        AutoCompleteDecorator.decorate(comboEselon);
    }

    public void reloadPrintPanel() {
        printWorker = new BudgetRealizationPanel.LoadPrintPanel(jasperPrint, panelViewer, yearChooser.getYear(),
                comboBudgetType.getSelectedIndex());

        jpListener = new BudgetRealizationPanel.JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkBox) {
            checkBox2.setSelected(false);
            comboActivity.setEnabled(checkBox.isSelected());
            comboEselon.setEnabled(checkBox2.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox2) {
            checkBox.setSelected(false);
            comboActivity.setEnabled(checkBox.isSelected());
            comboEselon.setEnabled(checkBox2.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox3) {
            checkBox4.setSelected(false);
            startDate.setEnabled(checkBox3.isSelected());
            endDate.setEnabled(checkBox3.isSelected());
            monthChooser.setEnabled(checkBox4.isSelected());
            yearChooser2.setEnabled(checkBox4.isSelected());
            reloadPrintPanel();
        } else if (source == checkBox4) {
            checkBox3.setSelected(false);
            startDate.setEnabled(checkBox3.isSelected());
            endDate.setEnabled(checkBox3.isSelected());
            monthChooser.setEnabled(checkBox4.isSelected());
            yearChooser2.setEnabled(checkBox4.isSelected());
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
        private Integer year = Integer.valueOf(0);
        private Integer budgetType = Integer.valueOf(0);

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel, Integer year, Integer budgetType) {
            this.jasperPrint = jasperPrint;
            this.viewerPanel = viewerPanel;
            this.year = year;
            this.budgetType = budgetType;
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

        private JasperReport loadReportFileAll() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "BudgetRealizationAll.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModelAll(ArrayList<BudgetRealization> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Eselon");
            model.addColumn("Budget");
            model.addColumn("Credit");
            model.addColumn("Closing");
            model.addColumn("Description");
            model.addColumn("Activity");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    BudgetRealization br = data.get(i);

                    model.addRow(new Object[]{br.getEselon(),
                                br.getBudget(), br.getCredit(),
                                br.getClosing(), br.getDescription(),
                                br.getActivity()});



                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private Map constructParameterAll() throws InterruptedException {
            Map param = new HashMap();

            StringBuilder builder = new StringBuilder();

            if (budgetType.equals(Integer.valueOf(1))) {
                builder.append("APBD Tahun ").append(year);
            } else {
                builder.append("APBD Perubahan Tahun ").append(year);
            }

            SimpleDateFormat sdf;

            Date sDate = startDate.getDate();
            Date eDate = endDate.getDate();

            Integer month = monthChooser.getMonth() + 1;
            Integer year2 = yearChooser2.getYear();

            if (sDate != null && eDate != null && checkBox3.isSelected()) {
                sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("in", "id", "id"));
                builder.append(" (").
                        append(sdf.format(sDate)).
                        append(" - ").
                        append(sdf.format(eDate)).
                        append(") ");
            } else if (month != 0 && checkBox4.isSelected()) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year2, month - 1, 1);
                builder.append(" (").
                        append(sdf.format(cal.getTime())).
                        append(") ");
            }

            param.put("periode", builder.toString());
            setProgress(80);
            Thread.sleep(100L);

            return param;
        }

        private JasperReport loadReportFileActivity() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "BudgetRealization.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModelActivity(ArrayList<BudgetRealization> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Eselon");
            model.addColumn("Budget");
            model.addColumn("Credit");
            model.addColumn("Closing");
            model.addColumn("Description");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    BudgetRealization br = data.get(i);

                    model.addRow(new Object[]{Integer.valueOf(i + 1), br.getEselon(),
                                br.getBudget(), br.getCredit(),
                                br.getClosing(), br.getDescription()});



                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private Map constructParameterActivity(Activity activity) throws InterruptedException {
            Map param = new HashMap();

            StringBuilder builder = new StringBuilder();

            if (budgetType.equals(Integer.valueOf(1))) {
                builder.append("APBD Tahun ").append(year);
            } else {
                builder.append("APBD Perubahan Tahun ").append(year);
            }

            SimpleDateFormat sdf;

            Date sDate = startDate.getDate();
            Date eDate = endDate.getDate();

            Integer month = monthChooser.getMonth() + 1;
            Integer year2 = yearChooser2.getYear();

            if (sDate != null && eDate != null && checkBox3.isSelected()) {
                sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("in", "id", "id"));
                builder.append(" (").
                        append(sdf.format(sDate)).
                        append(" - ").
                        append(sdf.format(eDate)).
                        append(") ");
            } else if (month != 0 && checkBox4.isSelected()) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year2, month - 1, 1);
                builder.append(" (").
                        append(sdf.format(cal.getTime())).
                        append(") ");
            }

            param.put("periode", builder.toString());

            param.put("actname", activity.getActivityCode() + " " + activity.getActivityName());

            setProgress(80);
            Thread.sleep(100L);

            return param;
        }

        private JasperReport loadReportFileEselon() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "BudgetRealization2.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModelEselon(ArrayList<BudgetRealization> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("Activity");
            model.addColumn("Budget");
            model.addColumn("Credit");
            model.addColumn("Closing");
            model.addColumn("Description");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    BudgetRealization br = data.get(i);

                    model.addRow(new Object[]{Integer.valueOf(i + 1), br.getActivity(),
                                br.getBudget(), br.getCredit(),
                                br.getClosing(), br.getDescription()});



                    progress = 50 * (i + 1) / data.size();
                    setProgress((int) progress);
                    Thread.sleep(100L);
                }
            } else {
                setProgress(75);
            }

            return model;
        }

        private Map constructParameterEselon(Integer eselon) throws InterruptedException {
            Map param = new HashMap();

            StringBuilder builder = new StringBuilder();

            if (budgetType.equals(Integer.valueOf(1))) {
                builder.append("APBD Tahun ").append(year);
            } else {
                builder.append("APBD Perubahan Tahun ").append(year);
            }

            SimpleDateFormat sdf;

            Date sDate = startDate.getDate();
            Date eDate = endDate.getDate();

            Integer month = monthChooser.getMonth() + 1;
            Integer year2 = yearChooser2.getYear();

            if (sDate != null && eDate != null && checkBox3.isSelected()) {
                sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("in", "id", "id"));
                builder.append(" (").
                        append(sdf.format(sDate)).
                        append(" - ").
                        append(sdf.format(eDate)).
                        append(") ");
            } else if (month != 0 && checkBox4.isSelected()) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year2, month - 1, 1);
                builder.append(" (").
                        append(sdf.format(cal.getTime())).
                        append(") ");
            }

            param.put("periode", builder.toString());

            param.put("EselonName", StandardPrice.ESELON[eselon]);

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

        private String generateDateModifier() {
            StringBuilder builder = new StringBuilder();

            Date sDate = startDate.getDate();
            Date eDate = endDate.getDate();

            if (sDate != null && eDate != null) {
                builder.append(" and exp.startdate between '").
                        append(new java.sql.Date(sDate.getTime())).
                        append("' and '").
                        append(new java.sql.Date(eDate.getTime())).append("' ");
            }

            return builder.toString();
        }

        private String generateMonthModifier() {
            StringBuilder builder = new StringBuilder();

            Integer month = monthChooser.getMonth() + 1;
            Integer year2 = yearChooser2.getYear();

            if (!month.equals(Integer.valueOf(0)) && !year2.equals(Integer.valueOf(0))) {
                builder.append(" and date_part('month',exp.startdate) = ").
                        append(month).
                        append(" and date_part('year',exp.startdate) = ").
                        append(year2).append(" ");
            }

            return builder.toString();
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<BudgetRealization> realization = new ArrayList<BudgetRealization>();
                Activity activity = null;
                Integer eselon = Integer.valueOf(0);
                String dateModifier = generateDateModifier();
                String monthModifier = generateMonthModifier();

                if (checkBox.isSelected()) {
                    Object obj = comboActivity.getSelectedItem();

                    if (obj instanceof Activity) {
                        activity = (Activity) obj;
                    }

                    if (checkBox3.isSelected()) {
                        if (activity != null) {
                            if (!dateModifier.equals("")) {
                                realization = logic.getBudgetRealizationByActivity(mainframe.getSession(), year, budgetType, activity, dateModifier);
                            }
                        }
                    } else if (checkBox4.isSelected()) {
                        if (activity != null) {
                            if (!monthModifier.equals("")) {
                                realization = logic.getBudgetRealizationByActivity(mainframe.getSession(), year, budgetType, activity, monthModifier);
                            }
                        }
                    } else {
                        if (activity != null) {
                            realization = logic.getBudgetRealizationByActivity(mainframe.getSession(), year, budgetType, activity, "");
                        }
                    }

                } else if (checkBox2.isSelected()) {
                    eselon = comboEselon.getSelectedIndex();

                    if (checkBox3.isSelected()) {
                        if (!eselon.equals(Integer.valueOf(0))) {
                            if (!dateModifier.equals("")) {
                                realization = logic.getBudgetRealizationByEselon(mainframe.getSession(), year, budgetType, eselon, dateModifier);
                            }
                        }
                    } else if (checkBox4.isSelected()) {
                        if (!eselon.equals(Integer.valueOf(0))) {
                            if (!monthModifier.equals("")) {
                                realization = logic.getBudgetRealizationByEselon(mainframe.getSession(), year, budgetType, eselon, monthModifier);
                            }
                        }
                    } else {
                        if (!eselon.equals(Integer.valueOf(0))) {
                            realization = logic.getBudgetRealizationByEselon(mainframe.getSession(), year, budgetType, eselon, "");
                        }
                    }
                } else {
                    if (checkBox3.isSelected()) {
                        if (!dateModifier.equals("")) {
                            realization = logic.getBudgetRealizationAll(mainframe.getSession(), year, budgetType, dateModifier);
                        }
                    } else if (checkBox4.isSelected()) {
                        if (!monthModifier.equals("")) {
                            realization = logic.getBudgetRealizationAll(mainframe.getSession(), year, budgetType, monthModifier);
                        }
                    } else {
                        realization = logic.getBudgetRealizationAll(mainframe.getSession(), year, budgetType, "");
                    }
                }

                if (!realization.isEmpty()) {
                    if (checkBox.isSelected()) {
                        JasperReport jrReport = loadReportFileActivity();
                        DefaultTableModel model = constructModelActivity(realization);
                        Map param = constructParameterActivity(activity);
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else if (checkBox2.isSelected()) {
                        JasperReport jrReport = loadReportFileEselon();
                        DefaultTableModel model = constructModelEselon(realization);
                        Map param = constructParameterEselon(eselon);
                        jasperPrint = createJasperPrint(jrReport, model, param);
                    } else {
                        JasperReport jrReport = loadReportFileAll();
                        DefaultTableModel model = constructModelAll(realization);
                        Map param = constructParameterAll();
                        jasperPrint = createJasperPrint(jrReport, model, param);
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
                JXErrorPane.showDialog(BudgetRealizationPanel.this, info);
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
