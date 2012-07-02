package org.motekar.project.civics.archieve.report.ui;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;
import ca.odell.glazedlists.SortedList;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.error.ErrorInfo;
import org.motekar.project.civics.archieve.master.objects.ItemStandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.report.ViewerPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemStandardPriceReportPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar viewerBar = new JProgressBar();
    private JXButton btReload = new JXButton("Reload");
    private ViewerPanel panelViewer = new ViewerPanel(null);
    private JasperPrint jasperPrint = new JasperPrint();
    private LoadPrintPanel printWorker;
    private JasperProgressListener jpListener;

    public ItemStandardPriceReportPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MasterBusinessLogic(mainframe.getConnection());
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
                "pref,10px,pref,5px, pref,5px,pref,5px,fill:default:grow,5px,250dlu", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        btReload.addActionListener(this);

        builder.add(btReload, cc.xy(1, 1));

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
        if (source == btReload) {
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

            String filename = "ItemStandardPrice.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport("printing/" + filename);
            setProgress(25);
            Thread.sleep(100L);

            return jasperReport;
        }

        private DefaultTableModel constructModel(ArrayList<ItemStandardPrice> data) throws InterruptedException, SQLException {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("No");
            model.addColumn("ItemName");
            model.addColumn("Specification");
            model.addColumn("ItemsUnit");
            model.addColumn("Price");
            model.addColumn("Description");

            double progress = 0.0;

            Comparator<ItemStandardPrice> comp = new Comparator<ItemStandardPrice>() {

                public int compare(ItemStandardPrice o1, ItemStandardPrice o2) {
                    return o1.getItemName().toLowerCase().compareTo(o2.getItemName().toLowerCase());
                }
            };
            
            Comparator<ItemStandardPrice> comp2 = new Comparator<ItemStandardPrice>() {

                public int compare(ItemStandardPrice o1, ItemStandardPrice o2) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
            };

            int i = 0;
            int j = 0;

            boolean wi;

            if (!data.isEmpty()) {
                final GroupingList<ItemStandardPrice> gpl = new GroupingList<ItemStandardPrice>(GlazedLists.eventList(data), comp);

                for (Iterator<List<ItemStandardPrice>> iter = gpl.iterator(); iter.hasNext();) {
                    List<ItemStandardPrice> ispList = iter.next();

                    String itemName = ispList.get(0).getItemName();
                    wi = true;
                    
                    final SortedList<ItemStandardPrice> sorted = new SortedList<ItemStandardPrice>(GlazedLists.eventList(ispList), comp2);

                    for (Iterator<ItemStandardPrice> ispItter = sorted.iterator(); ispItter.hasNext();) {
                        ItemStandardPrice isprice = ispItter.next();

                        if (wi) {
                            model.addRow(new Object[]{Integer.valueOf(j+1), itemName,
                                        isprice.getSpecification(), isprice.getItemsUnit(), isprice.getPrice(),
                                        isprice.getDescription()});
                            wi = false;
                        } else {
                            model.addRow(new Object[]{null, null,
                                isprice.getSpecification(), isprice.getItemsUnit(), isprice.getPrice(),
                                isprice.getDescription()});
                        }

                        progress = 50 * (i + 1) / data.size();
                        setProgress((int) progress);
                        Thread.sleep(100L);
                        i++;
                    }
                    j++;
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

                ArrayList<ItemStandardPrice> itemStandardPrice = logic.getItemStandardPrice(mainframe.getSession());

                if (!itemStandardPrice.isEmpty()) {
                    JasperReport jrReport = loadReportFile();
                    DefaultTableModel model = constructModel(itemStandardPrice);
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
                JXErrorPane.showDialog(ItemStandardPriceReportPanel.this, info);
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
