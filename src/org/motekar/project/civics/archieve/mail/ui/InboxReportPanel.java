package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.motekar.project.civics.archieve.mail.objects.Inbox;
import org.motekar.project.civics.archieve.mail.objects.InboxDisposition;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InboxReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private JXDatePicker startDate = new JXDatePicker();
    private JXDatePicker endDate = new JXDatePicker();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JYearChooser yearChooser = new JYearChooser();
    private JYearChooser yearChooser2 = new JYearChooser();
    private JCheckBox checkBox = new JCheckBox();
    private JCheckBox checkBox2 = new JCheckBox();
    private JCheckBox checkBox3 = new JCheckBox();
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public InboxReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Masuk");

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
                + "Daftar Surat Masuk merupakan daftar semua surat yang masuk ke dinas beserta statusnya "
                + "pada dinas tertentu\n"
                + "Untuk melihat data Surat Masuk pilih periode berdasarkan tanggal, berdasarkan bulan,"
                + "maupun berdasarkan bulan. Untuk memilih berdasarkan tanggal pilih ceklist paling atas kemudian "
                + "masukan tanggal misalkan dari tanggal 01/01/2010 s.d. 31/12/2010. Data akan langsung ditampilkan "
                + "pada panel dibawahnya. Kemudian jika ingin menampilkan data per bulan pilih ceklist kedua dengan "
                + "cara yang kurang lebih sama dengan sebelumnya. Begitu juga jika ingin menampilkan data per tahun "
                + "pilih ceklist paling bawah.\n"
                + "Untuk melakukan cetak daftar Surat Masuk, dari data yang telah difilter sebelumnya (panel dibawah) "
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
        task.setTitle("Cetak Daftar Surat Masuk");
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
                "pref,10px,pref,5px, pref,5px,pref,5px,fill:default:grow,5px,250dlu", "pref,5px,pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        startDate.setEnabled(checkBox.isSelected());
        endDate.setEnabled(checkBox.isSelected());

        monthChooser.setYearChooser(yearChooser);
        monthChooser.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        monthChooser.setEnabled(checkBox2.isSelected());
        yearChooser.setEnabled(checkBox2.isSelected());
        yearChooser2.setEnabled(checkBox3.isSelected());

        checkBox.addActionListener(this);
        checkBox2.addActionListener(this);
        checkBox3.addActionListener(this);

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
        return builder.getPanel();
    }

    public void reloadPrintPanel() {
        if (checkBox.isSelected()) {
            printWorker = new LoadPrintPanel(jasperPrint, panelViewer, startDate.getDate(), endDate.getDate());
        } else if (checkBox2.isSelected()) {
            printWorker = new LoadPrintPanel(jasperPrint, panelViewer, monthChooser.getMonth() + 1, yearChooser.getYear());
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

            String filename = "InboxReport.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel(ArrayList<Inbox> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("MailDate");
            model.addColumn("MailNumber");
            model.addColumn("Subject");
            model.addColumn("Sender");
            model.addColumn("DispositionDate");
            model.addColumn("Receiver");
            model.addColumn("Receiver2");
            model.addColumn("ReceiveDate");
            model.addColumn("Status");
            model.addColumn("Sep");

            double progress = 0.0;

            if (!data.isEmpty()) {
                for (int i = 0; i < data.size() && !isCancelled(); i++) {

                    Inbox inbox = data.get(i);
                    Division division = inbox.getReceipient();

                    String receiver = "";

                    if (division != null) {
                        receiver = division.getName();
                    }

                    ArrayList<InboxDisposition> dispositions = logic.getInboxDisposition(mainframe.getSession(),inbox.getIndex());

                    if (!dispositions.isEmpty()) {
                        for (InboxDisposition dispo : dispositions) {
                                model.addRow(new Object[]{Integer.valueOf(i + 1), inbox.getMailDate(),
                                            inbox.getMailNumber(), inbox.getSubject(), inbox.getSender(),
                                            inbox.getDispositionDate(), receiver,
                                            dispo.getDivision().toString(),
                                            dispo.getReceiveDate(),
                                            dispo.getStatusAsString(),"\u2022"});
                        }

                    } else {
                        model.addRow(new Object[]{Integer.valueOf(i + 1), inbox.getMailDate(),
                                inbox.getMailNumber(), inbox.getSubject(), inbox.getSender(),
                                inbox.getDispositionDate(), receiver,null,null,null,null});
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

            SimpleDateFormat sdf;

            StringBuilder builder = new StringBuilder();

            if (startDate != null && endDate != null) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                builder.append(sdf.format(startDate)).
                        append(" s.d. ").
                        append(sdf.format(endDate));
            } else if (month != 0) {
                sdf = new SimpleDateFormat("MMMM yyyy", new Locale("in", "id", "id"));
                GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
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

        @Override
        protected void process(List<Void> chunks) {
            mainframe.stopInActiveListener();
            viewerPanel.reload(jasperPrint);
        }

        @Override
        protected JasperPrint doInBackground() throws Exception {
            try {

                ArrayList<Inbox> inbox = new ArrayList<Inbox>();

                if (startDate != null && endDate != null) {
                    inbox = logic.getInbox(mainframe.getSession(), startDate, endDate);
                } else if (month != 0 || year != 0) {
                    inbox = logic.getInbox(mainframe.getSession(), month, year);
                }

                if (!inbox.isEmpty()) {
                    JasperReport jrReport = loadReportFile();
                    DefaultTableModel model = constructModel(inbox);
                    Map param = constructParameter();
                    jasperPrint = createJasperPrint(jrReport, model, param);
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
                JXErrorPane.showDialog(InboxReportPanel.this, info);
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
