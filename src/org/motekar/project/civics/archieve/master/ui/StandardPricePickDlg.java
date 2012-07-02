package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class StandardPricePickDlg implements ActionListener, ListSelectionListener {

    private JFrame frame;
    private JProgressBar pBar = new JProgressBar();
    private MasterBusinessLogic logic;
    private Long session = null;
    private JXTextField fieldSearch = new JXTextField();
    private LoadStandardPrice worker;
    private PriceProgressListener progressListener;
    private StandardPriceList priceList = new StandardPriceList();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private ArrayList<StandardPrice> selectedStandardPrice = new ArrayList<StandardPrice>();
    private int response = JOptionPane.NO_OPTION;
    private BigDecimal budget = BigDecimal.ZERO;
    private JXLabel labelBudget = new JXLabel("");
    private JXLabel labelSelected = new JXLabel("");
    private Color foreColor = labelBudget.getForeground();
    private Activity activity = null;

    public StandardPricePickDlg(JFrame frame, Long session, Connection conn, BigDecimal budget,Activity activity, int selectionType) {
        this.frame = frame;
        this.session = session;
        this.budget = budget;
        this.activity = activity;
        logic = new MasterBusinessLogic(conn);
        priceList.getSelectionModel().setSelectionMode(selectionType);
        priceList.loadData();
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Standar Harga");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 500));
        dlg.setModal(true);
        dlg.setResizable(false);

        setBudget();

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    public void filter() {
        priceList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,5px,pref,5px,fill:default:grow,10px",
                "pref,5px,pref,5px,pref,5px,fill:default:grow,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelBudget.setFont(new Font(labelBudget.getFont().getName(), Font.BOLD, labelBudget.getFont().getSize()));
        labelSelected.setFont(new Font(labelSelected.getFont().getName(), Font.BOLD, labelSelected.getFont().getSize()));

        JXLabel label1 = new JXLabel("Sisa Anggaran");
        JXLabel labelSp1 = new JXLabel(":");

        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, label1.getFont().getSize()));
        labelSp1.setFont(new Font(labelSp1.getFont().getName(), Font.BOLD, labelSp1.getFont().getSize()));

        JXLabel label2 = new JXLabel("Terpilih");
        JXLabel labelSp2 = new JXLabel(":");

        label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, label2.getFont().getSize()));
        labelSp2.setFont(new Font(labelSp2.getFont().getName(), Font.BOLD, labelSp2.getFont().getSize()));

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(priceList);

        CellConstraints cc = new CellConstraints();

        builder.add(label1, cc.xy(1, 1));
        builder.add(labelSp1, cc.xy(3, 1));
        builder.add(labelBudget, cc.xyw(5, 1, 1));

        builder.add(label2, cc.xy(1, 3));
        builder.add(labelSp2, cc.xy(3, 3));
        builder.add(labelSelected, cc.xyw(5, 3, 1));

        builder.addLabel("Cari", cc.xy(1, 5));
        builder.addLabel(":", cc.xy(3, 5));
        builder.add(fieldSearch, cc.xyw(5, 5, 1));

        builder.add(scPane, cc.xywh(1, 7, 5, 2));

        builder.add(pBar, cc.xyw(1, 9, 4));

        return builder.getPanel();
    }

    private void setBudget() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        labelBudget.setText(amountDisplayFormat.format(budget.doubleValue()));
        labelSelected.setText(amountDisplayFormat.format(BigDecimal.ZERO.doubleValue()));
    }

    private Component construct() {

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        priceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        priceList.setAutoCreateRowSorter(true);

        priceList.addListSelectionListener(this);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JXPanel createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();
        okTooltip.setTitle("Pilih");
        btOK.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");


        btCancel.setActionRichTooltip(cancelTooltip);

        btOK.addActionListener(this);
        btCancel.addActionListener(this);

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    public int getResponse() {
        return response;
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }

    public ArrayList<StandardPrice> getSelectedStandardPrice() {
        return selectedStandardPrice;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            if (worker != null) {
                worker.cancel(true);
            }
            dlg.dispose();
        }
    }

    private void onOK() {
        response = JOptionPane.YES_OPTION;
        if (worker != null) {
            worker.cancel(true);
        }
        selectedStandardPrice = priceList.getSelectedStandardPrices();
        dlg.dispose();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedStandardPrice = priceList.getSelectedStandardPrices();
            if (selectedStandardPrice != null) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                BigDecimal used = selectedStandardPrice.get(0).getPrice();
                labelSelected.setText(amountDisplayFormat.format(used.doubleValue()));

                if (budget.compareTo(used) < 0 ) {
                    labelBudget.setForeground(Color.RED);
                    btOK.setEnabled(false);
                    JOptionPane.showMessageDialog(dlg, "Dana melebihi Sisa Anggaran", "Perhatian", JOptionPane.WARNING_MESSAGE);
                } else {
                    btOK.setEnabled(true);
                    labelBudget.setForeground(foreColor);
                }

            }
        }
    }

    private class StandardPriceList extends JXList {

        private Icon CAR_ICON = Mainframe.getResizableIconFromSource("resource/Cars.png", new Dimension(36, 36));
        private Icon PLANE_ICON = Mainframe.getResizableIconFromSource("resource/Mini_Plane.png", new Dimension(36, 36));
        private Icon BUS_ICON = Mainframe.getResizableIconFromSource("resource/Travel_Bus.png", new Dimension(36, 36));
        private Icon TRAIN_ICON = Mainframe.getResizableIconFromSource("resource/Train.png", new Dimension(36, 36));
        private Icon SAIL_ICON = Mainframe.getResizableIconFromSource("resource/Sailing_Ship.png", new Dimension(36, 36));
        private Icon OTHER_ICON = Mainframe.getResizableIconFromSource("resource/OtherTrans.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public StandardPriceList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadStandardPrice((DefaultListModel) getModel());
            progressListener = new PriceProgressListener(pBar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public StandardPrice getSelectedStandardPrice() {
            StandardPrice price = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof StandardPrice) {
                price = (StandardPrice) obj;
            }
            return price;
        }

        public ArrayList<StandardPrice> getSelectedStandardPrices() {

            ArrayList<StandardPrice> standardPrices = new ArrayList<StandardPrice>();

            Object[] objects = this.getSelectedValues();

            if (objects != null) {
                StandardPrice standardPrice = null;
                for (Object obj : objects) {
                    if (obj instanceof StandardPrice) {
                        standardPrice = (StandardPrice) obj;
                        standardPrices.add(standardPrice);
                    }
                }
            }


            return standardPrices;
        }

        public void addStandardPrice(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(price);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateStandardPrice(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(price, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<StandardPrice> prices) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(prices);
            filter();
        }

        public void removeSelected(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(price);
            filter();
        }

        @Override
        public ListCellRenderer getCellRenderer() {
            return new DefaultListRenderer(new StringValue() {

                public String getString(Object o) {
                    return o.toString();
                }
            }, new IconValue() {

                public Icon getIcon(Object o) {
                    StandardPrice price = null;
                    if (o instanceof StandardPrice) {
                        price = (StandardPrice) o;
                    }

                    if (price != null) {
                        if (price.getTransportType() == StandardPrice.TYPE_CAR) {
                            return StandardPriceList.this.CAR_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_BUS || 
                                price.getTransportType() == StandardPrice.TYPE_LAND) {
                            return StandardPriceList.this.BUS_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_PLANE || 
                                price.getTransportType() == StandardPrice.TYPE_AIR) {
                            return StandardPriceList.this.PLANE_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_SAILING || 
                                price.getTransportType() == StandardPrice.TYPE_SEA) {
                            return StandardPriceList.this.SAIL_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_TRAIN) {
                            return StandardPriceList.this.TRAIN_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_LAND_AIR || 
                                price.getTransportType() == StandardPrice.TYPE_LAND_SEA || 
                                price.getTransportType() == StandardPrice.TYPE_LAND_SEA_AIR) {
                            return StandardPriceList.this.OTHER_ICON;
                        }
                    }

                    return StandardPriceList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadStandardPrice extends SwingWorker<DefaultListModel, StandardPrice> {

        private DefaultListModel model;
        private Exception exception;

        public LoadStandardPrice(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<StandardPrice> chunks) {
            for (StandardPrice price : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.addElement(price);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<StandardPrice> price = logic.getStandardPrice(session, activity);

                double progress = 0.0;
                if (!price.isEmpty()) {
                    for (int i = 0; i < price.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / price.size();
                        setProgress((int) progress);
                        publish(price.get(i));
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
            if (isCancelled()) {
                return;
            }
            setProgress(100);
        }
    }

    private class PriceProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private PriceProgressListener() {
        }

        PriceProgressListener(JProgressBar progressBar) {
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
            } else if ("state".equals(strPropertyName) && worker.getState() == SwingWorker.StateValue.DONE) {
                this.progressBar.setVisible(false);
                this.progressBar.setValue(0);
            }
        }
    }
}
