package org.motekar.project.civics.archieve.expedition.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
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
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.sqlapi.ExpeditionBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionPickDlg implements ActionListener {

    private JFrame frame;
    private JProgressBar pBar = new JProgressBar();
    private ExpeditionBusinessLogic logic;
    private Long session = null;
    private JXTextField fieldSearch = new JXTextField();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private Expedition selectedExpedition = null;
    private int response = JOptionPane.NO_OPTION;
    private ExpeditionList expeditionList = new ExpeditionList();
    private LoadExpedition worker;
    private ExpeditionProgressListener progressListener;

    public ExpeditionPickDlg(JFrame frame, Long session, Connection conn) {
        this.frame = frame;
        this.session = session;
        logic = new ExpeditionBusinessLogic(conn);
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = ((ArchieveMainframe) frame).getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            expeditionList.loadData("");
        } else {
            Unit unit = ((ArchieveMainframe) frame).getUnit();
            String modifier = generateUnitModifier(unit);
            expeditionList.loadData(modifier);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Surat Perjalanan Dinas");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 500));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    public void filter() {
        expeditionList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,fill:default:grow,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(expeditionList);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xy(3, 1));

        builder.add(scPane, cc.xywh(1, 3, 3, 2));

        builder.add(pBar, cc.xyw(1, 5, 3));

        return builder.getPanel();
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

        expeditionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expeditionList.setAutoCreateRowSorter(true);

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

    public int getResponse() {
        return response;
    }

    private void onOK() {
        if (worker != null) {
            worker.cancel(true);
        }
        response = JOptionPane.YES_OPTION;
        selectedExpedition = expeditionList.getSelectedExpedition();
        dlg.dispose();
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (worker != null) {
                    worker.cancel(true);
                }
                dlg.dispose();
            }
        });
    }

    public Expedition getSelectedExpedition() {
        return selectedExpedition;
    }

    private class ExpeditionList extends JXList {

        private Icon EXPEDITION_ICON = Mainframe.getResizableIconFromSource("resource/Spd.png", new Dimension(40, 40));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(40, 40));

        public ExpeditionList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData(String modifier) {
            worker = new LoadExpedition((DefaultListModel) getModel(), modifier);
            progressListener = new ExpeditionProgressListener(pBar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Expedition getSelectedExpedition() {
            Expedition expedition = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Expedition) {
                expedition = (Expedition) obj;
            }
            return expedition;
        }

        public void addExpedition(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(expedition);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateExpedition(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(expedition, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Expedition> expeditions) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(expeditions);
            filter();
        }

        public void removeSelected(Expedition expedition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(expedition);
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

                    Expedition expedition = null;

                    if (o instanceof Expedition) {
                        expedition = (Expedition) o;
                    }

                    if (expedition != null) {
                        return ExpeditionList.this.EXPEDITION_ICON;
                    }

                    return ExpeditionList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadExpedition extends SwingWorker<DefaultListModel, Expedition> {

        private DefaultListModel model;
        private Exception exception;
        private String modifier = "";

        public LoadExpedition(DefaultListModel model, String modifier) {
            this.model = model;
            this.modifier = modifier;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Expedition> chunks) {
            for (Expedition expedition : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.addElement(expedition);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Expedition> expeditions = logic.getExpedition(session, modifier);

                double progress = 0.0;
                if (!expeditions.isEmpty()) {
                    for (int i = 0; i < expeditions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / expeditions.size();
                        setProgress((int) progress);
                        publish(expeditions.get(i));
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

    private class ExpeditionProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ExpeditionProgressListener() {
        }

        ExpeditionProgressListener(JProgressBar progressBar) {
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
