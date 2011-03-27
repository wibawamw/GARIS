package org.motekar.project.civics.archieve.report.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
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
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private JYearChooser yearChooser = new JYearChooser();
    private JXComboBox comboBudgetType = new JXComboBox();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public BudgetRealizationPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new ReportBusinessLogic(mainframe.getConnection());
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
                "pref,5px,fill:default:grow,250dlu",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tahun Anggaran ", cc.xy(1, 1));
        builder.add(yearChooser, cc.xy(3, 1));

        builder.addLabel("Tipe Anggaran ", cc.xy(1, 3));
        builder.add(comboBudgetType, cc.xy(3, 3));

        return builder.getPanel();
    }

    private void loadBudgetType() {

        ArrayList<String> budgetTypes = new ArrayList<String>();

        budgetTypes.add(0," ");
        budgetTypes.add("APBD");
        budgetTypes.add("APBD Perubahan");

        comboBudgetType.setModel(new ListComboBoxModel<String>(budgetTypes));
        AutoCompleteDecorator.decorate(comboBudgetType);

        comboBudgetType.setSelectedIndex(1);
    }

    public void reloadPrintPanel() {
        printWorker = new LoadPrintPanel(jasperPrint, panelViewer, yearChooser.getYear(),
                comboBudgetType.getSelectedIndex());

        jpListener = new JasperProgressListener(viewerBar);
        printWorker.addPropertyChangeListener(jpListener);
        printWorker.execute();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

    }

    private class LoadPrintPanel extends SwingWorker<JasperPrint, Void> {

        private JasperPrint jasperPrint;
        private Exception exception;
        private ViewerPanel viewerPanel;
        private Integer year = Integer.valueOf(0);
        private Integer budgetType = Integer.valueOf(0);

        public LoadPrintPanel(JasperPrint jasperPrint, ViewerPanel viewerPanel, Integer year,Integer budgetType) {
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

        private JasperReport loadReportFile() throws InterruptedException, JRException {
            if (isCancelled()) {
                return null;
            }

            String filename = "BudgetRealization.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel(ArrayList<BudgetRealization> data) throws InterruptedException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Code");
            model.addColumn("Description");
            model.addColumn("Opening");
            model.addColumn("Debet");
            model.addColumn("Credit");
            model.addColumn("Closing");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    BudgetRealization br = data.get(i);

                    if (br.getMarker().equals("Detail")) {
                        model.addRow(new Object[]{"", br.getDescription(),
                                    br.getOpening(),br.getDebet(),br.getCredit(),
                                    br.getClosing()});
                    } else {
                        model.addRow(new Object[]{br.getCode(), br.getDescription(),
                                    br.getOpening(),br.getDebet(),br.getCredit(),
                                    br.getClosing()});
                    }



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

            StringBuilder builder = new StringBuilder();

            if (budgetType.equals(Integer.valueOf(1))) {
                builder.append("APBD Tahun ").append(year);
            } else {
                builder.append("APBD Perubahan Tahun ").append(year);
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

        @Override
        protected void process(List<Void> chunks) {
            mainframe.stopInActiveListener();
            viewerPanel.reload(jasperPrint);
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<BudgetRealization> realization = logic.getBudgetRealization(mainframe.getSession(), year, budgetType);

                if (!realization.isEmpty()) {
                    JasperReport jrReport = loadReportFile();
                    DefaultTableModel model = constructModel(realization);
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
