package org.motekar.project.civics.archieve.assets.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ConditionPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private AssetMasterBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private ConditionList conditionList = new ConditionList();
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    private JXFormattedTextField fieldStartValue;
    private JXFormattedTextField fieldEndValue;
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Condition selectedCondition = null;
    private LoadCondition worker;
    private ConditionProgressListener progressListener;
    private StringBuilder errorString = new StringBuilder();

    public ConditionPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        conditionList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Kondisi");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(conditionList), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Rincian Data");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createButtonsPanel(), BorderLayout.NORTH);
        collapasepanel.add(createMainPanel(), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Fasilitas Kondisi merupakan kondisi / keadaan terakhir dari barang.\n\n"
                + "Tambah Kondisi\n"
                + "Untuk menambah Kondisi klik tombol paling kiri "
                + "kemudian isi data Kondisi baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Kondisi\n"
                + "Untuk merubah Kondisi klik tombol kedua dari kiri "
                + "kemudian ubah data Kondisi, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Kondisi\n"
                + "Untuk menghapus Kondisi klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Kondisi "
                + "tersebut, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Kondisi");
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
        bar.add(pbar, c2);

        return bar;
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,30px",
                "pref,5px,pref,5px,pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Kode Kondisi", cc.xy(1, 1));
        builder.add(fieldCode, cc.xy(3, 1));

        builder.addLabel("Nama Kondisi", cc.xy(1, 3));
        builder.add(fieldName, cc.xy(3, 3));

        builder.addLabel("Nilai Batas Awal", cc.xy(1, 5));
        builder.add(fieldStartValue, cc.xy(3, 5));

        builder.addLabel("Nilai Batas Akhir", cc.xy(1, 7));
        builder.add(fieldEndValue, cc.xy(3, 7));

        return builder.getPanel();
    }

    protected JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,10px, fill:default:grow", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(new JXLabel("Cari"), fieldSearch);

        return builder.getPanel();
    }

    protected JPanel createButtonsPanel() {
        FormLayout lm = new FormLayout(
                "right:pref, 4dlu, left:pref, 4dlu, left:pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(createStrip(3.0, 3.0));
        return builder.getPanel();
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Pegawai");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Pegawai");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Pegawai");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Pegawai");

        btDelete.setActionRichTooltip(deleteTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batalkan Perubahan");

        btCancel.setActionRichTooltip(cancelTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btAdd);
        buttonStrip.add(btEdit);
        buttonStrip.add(btSave);
        buttonStrip.add(btDelete);
        buttonStrip.add(btCancel);


        return buttonStrip;
    }

    private void construct() {

        contructNumberField();

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

        conditionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conditionList.setAutoCreateRowSorter(true);
        conditionList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.25)"
                + "(LEAF name=editor2 weight=0.5) (LEAF name=editor3 weight=0.25))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createLeftComponent(), "editor1");
        splitPane.add(createCenterComponent(), "editor2");
        splitPane.add(panel, "editor3");

        panel.setVisible(true);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setFormState();
        setButtonState("");
    }

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setGroupingUsed(false);
        amountDisplayFormat.setMinimumIntegerDigits(0);
        amountDisplayFormat.setMaximumIntegerDigits(3);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        amountEditFormat.setMinimumIntegerDigits(0);
        amountEditFormat.setMaximumIntegerDigits(3);

        fieldStartValue = new JXFormattedTextField();
        fieldStartValue.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldStartValue.setHorizontalAlignment(JLabel.LEFT);

        fieldEndValue = new JXFormattedTextField();
        fieldEndValue.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldEndValue.setHorizontalAlignment(JLabel.LEFT);
    }

    public void filter() {
        conditionList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldCode.setEnabled(iseditable);
        fieldName.setEnabled(iseditable);
        fieldStartValue.setEnabled(iseditable);
        fieldEndValue.setEnabled(iseditable);


        fieldSearch.setEnabled(!iseditable);
        conditionList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldCode.setText("");
        fieldName.setText("");
        fieldStartValue.setValue(Integer.valueOf(0));
        fieldEndValue.setValue(Integer.valueOf(0));
        if (fieldCode.isEnabled()) {
            fieldCode.requestFocus();
            fieldCode.selectAll();
        }
    }

    private void setButtonState(String state) {
        if (state.equals("New")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(true);
            btCancel.setEnabled(true);
        } else if (state.equals("Save")) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);

        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btAdd) {
            onNew();
        } else if (obj == btEdit) {
            onEdit();
        } else if (obj == btDelete) {
            onDelete();
        } else if (obj == btSave) {
            onSave();
        } else if (obj == btCancel) {
            onCancel();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedCondition = conditionList.getSelectedCondition();
            setFormValues();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Kondisi");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldCode.requestFocus();
        fieldCode.selectAll();
        statusLabel.setText("Ubah Kondisi");
    }

    private void onDelete() {
        Object[] options = {"Ya", "Tidak"};
        int choise = JOptionPane.showOptionDialog(this,
                " Anda yakin menghapus data ini ? (Y/T)",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[1]);
        if (choise == JOptionPane.YES_OPTION) {
            try {
                logic.deleteCondition(mainframe.getSession(), selectedCondition.getIndex());
                conditionList.removeSelected(selectedCondition);
                clearForm();
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    private void onSave() {
        try {
            Condition newCondition = getCondition();

            if (isnew) {
                newCondition = logic.insertCondition(mainframe.getSession(), newCondition);
                newCondition.setStyled(true);
                isnew = false;
                iseditable = false;
                conditionList.addCondition(newCondition);
                selectedCondition = newCondition;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newCondition = logic.updateCondition(mainframe.getSession(), selectedCondition, newCondition);
                newCondition.setStyled(true);
                isedit = false;
                iseditable = false;
                conditionList.updateCondition(newCondition);
                setFormState();
                setButtonState("Save");
            }
            statusLabel.setText("Ready");
            mainframe.setFocusTraversalPolicy(null);
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        } catch (Exception ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    null, "Error", ex, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        }
    }

    private void onCancel() {
        iseditable = false;
        isedit = false;
        isnew = false;
        setFormState();
        if (conditionList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
    }

    private void setFormValues() {
        if (selectedCondition != null) {
            fieldCode.setText(selectedCondition.getConditionCode());
            fieldName.setText(selectedCondition.getConditionName());
            fieldStartValue.setValue(selectedCondition.getStartValue());
            fieldEndValue.setValue(selectedCondition.getEndValue());
            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private Condition getCondition() throws MotekarException {
        String code = fieldCode.getText();
        String name = fieldName.getText();

        Object objSVal = fieldStartValue.getValue();
        Integer startValue = Integer.valueOf(0);

        if (objSVal instanceof Long) {
            Long value = (Long) objSVal;
            startValue = Integer.valueOf(value.intValue());
        } else if (objSVal instanceof Integer) {
            startValue = (Integer) objSVal;
        }

        Object objEVal = fieldEndValue.getValue();
        Integer endValue = Integer.valueOf(0);

        if (objEVal instanceof Long) {
            Long value = (Long) objEVal;
            endValue = Integer.valueOf(value.intValue());
        } else if (objEVal instanceof Integer) {
            endValue = (Integer) objEVal;
        }

        if (code.equals("")) {
            errorString.append("<br>- Kode Kondisi</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama Kondisi</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Condition condition = new Condition();

        condition.setConditionCode(code);
        condition.setConditionName(name);
        condition.setStartValue(startValue);
        condition.setEndValue(endValue);

        return condition;
    }

    private class ConditionList extends JXList {

        private Icon DIVISION_ICON = Mainframe.getResizableIconFromSource("resource/Condition.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public ConditionList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadCondition((DefaultListModel) getModel());
            progressListener = new ConditionProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Condition getSelectedCondition() {
            Condition condition = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Condition) {
                condition = (Condition) obj;
            }
            return condition;
        }

        public void addCondition(Condition condition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(condition);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateCondition(Condition condition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(condition, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Condition> conditions) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(conditions);
            filter();
        }

        public void removeSelected(Condition condition) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(condition);
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

                    Condition condition = null;

                    if (o instanceof Condition) {
                        condition = (Condition) o;
                    }

                    if (condition != null) {
                        return ConditionList.this.DIVISION_ICON;
                    }

                    return ConditionList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadCondition extends SwingWorker<DefaultListModel, Condition> {

        private DefaultListModel model;
        private Exception exception;

        public LoadCondition(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Condition> chunks) {
            mainframe.stopInActiveListener();
            for (Condition condition : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kondisi " + condition.getConditionName());
                model.addElement(condition);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Condition> conditions = logic.getCondition(mainframe.getSession());

                double progress = 0.0;
                if (!conditions.isEmpty()) {
                    for (int i = 0; i < conditions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / conditions.size();
                        Condition condition = conditions.get(i);
                        condition.setStyled(true);
                        setProgress((int) progress);
                        publish(condition);
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
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(ConditionPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ConditionProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ConditionProgressListener() {
        }

        ConditionProgressListener(JProgressBar progressBar) {
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
                statusLabel.setText("Ready");
            }
        }
    }
}
