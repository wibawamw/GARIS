package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
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
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.mail.objects.Regulation;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
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
public class RegulationPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private RegulationList regulationList = new RegulationList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXTextField fieldDocNumber = new JXTextField();
    private JXDatePicker fieldDate = new JXDatePicker();
    private JXTextArea fieldDescription = new JXTextArea();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Regulation selectedRegulation = null;
    private LoadRegulation worker;
    private RegulationProgressListener progressListener;
    private StringBuilder errorString = new StringBuilder();

    public RegulationPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
        regulationList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Perda");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(regulationList), BorderLayout.CENTER);
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
                + "Fasilitas Peraturan Daerah merupakan fasilitas untuk mencatat semua Peraturan-peraturan "
                + "yang dikeluarkan di suatu daerah.\n\n"
                + "Tambah Peraturan Daerah\n"
                + "Untuk menambah Peraturan Daerah klik tombol paling kiri "
                + "kemudian isi data Peraturan Daerah baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Peraturan Daerah\n"
                + "Untuk merubah Peraturan Daerah klik tombol kedua dari kiri "
                + "kemudian ubah data Peraturan Daerah, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Peraturan Daerah\n"
                + "Untuk menghapus Peraturan Daerah klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Peraturan Daerah "
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
        task.setTitle("Tambah / Edit / Hapus Peraturan Daerah");
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
                "right:pref,10px,fill:default:grow,30px",
                "pref,5px,pref,5px,pref,fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nomor Perda", cc.xy(1, 1));
        builder.add(fieldDocNumber, cc.xy(3, 1));

        builder.addLabel("Tanggal Perda", cc.xy(1, 3));
        builder.add(fieldDate, cc.xy(3, 3));

        builder.addLabel("Perihal", cc.xy(1, 5));
        builder.add(scPane, cc.xywh(3, 5,1,2));

        return builder.getPanel();
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,5px, fill:default:grow,5px,fill:default:grow", "pref,4px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        monthChooser.setYearChooser(yearChooser);
        monthChooser.setEnabled(checkBox.isSelected());
        yearChooser.setEnabled(checkBox.isSelected());

        checkBox.addActionListener(this);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1, 3));

        builder.add(checkBox, cc.xy(1, 3));
        builder.add(monthChooser, cc.xyw(3, 3, 2));
        builder.add(yearChooser, cc.xy(5, 3));

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
        addTooltip.setTitle("Tambah Perda");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Perda");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Perda");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Perda");

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

        regulationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        regulationList.setAutoCreateRowSorter(true);
        regulationList.addListSelectionListener(this);

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                regulationList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                regulationList.loadData();
            }
        });

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        fieldDescription.setWrapStyleWord(true);
        fieldDescription.setLineWrap(true);

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

    public void filter() {
        regulationList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldDocNumber.setEnabled(iseditable);
        fieldDate.setEnabled(iseditable);
        fieldDescription.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        regulationList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldDocNumber.setText("");
        fieldDate.setDate(null);
        fieldDescription.setText("");
        if (fieldDocNumber.isEnabled()) {
            fieldDocNumber.requestFocus();
            fieldDocNumber.selectAll();
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
        } else if (obj == checkBox) {
            monthChooser.setEnabled(checkBox.isSelected());
            yearChooser.setEnabled(checkBox.isSelected());
            regulationList.loadData();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedRegulation = regulationList.getSelectedRegulation();
            setFormValues();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Peraturan Daerah");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldDocNumber.requestFocus();
        statusLabel.setText("Ubah Peraturan Daerah");
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
                logic.deleteRegulation(mainframe.getSession(), selectedRegulation.getIndex());
                regulationList.removeSelected(selectedRegulation);
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
            Regulation newRegulation = getRegulation();

            if (isnew) {
                newRegulation = logic.insertRegulation(mainframe.getSession(), newRegulation);
                isnew = false;
                iseditable = false;
                regulationList.addRegulation(newRegulation);
                selectedRegulation = newRegulation;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newRegulation = logic.updateRegulation(mainframe.getSession(), selectedRegulation, newRegulation);
                isedit = false;
                iseditable = false;
                regulationList.updateRegulation(newRegulation);
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
        if (regulationList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
    }

    private void setFormValues() {
        if (selectedRegulation != null) {
            fieldDocNumber.setText(selectedRegulation.getRegulationNumber());
            fieldDate.setDate(selectedRegulation.getRegulationDate());
            fieldDescription.setText(selectedRegulation.getDescription());
            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private Regulation getRegulation() throws MotekarException {

        String docNumber = fieldDocNumber.getText();

        if (docNumber.equals("")) {
            errorString.append("<br>- Nomor Perda</br>");
        }

        Date RegulationDate = fieldDate.getDate();

        if (RegulationDate == null) {
            errorString.append("<br>- Tanggal Perda</br>");
        }

        String description = fieldDescription.getText();

        if (description.equals("")) {
            errorString.append("<br>- Perihal</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Regulation Regulation = new Regulation();

        Regulation.setRegulationNumber(docNumber);
        Regulation.setRegulationDate(RegulationDate);
        Regulation.setDescription(description);

        return Regulation;
    }

    private class RegulationList extends JXList {

        private Icon Regulation_ICON = Mainframe.getResizableIconFromSource("resource/Regulation.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public RegulationList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadRegulation((DefaultListModel) getModel());
            progressListener = new RegulationProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Regulation getSelectedRegulation() {
            Regulation Regulation = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Regulation) {
                Regulation = (Regulation) obj;
            }
            return Regulation;
        }

        public void addRegulation(Regulation Regulation) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(Regulation);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateRegulation(Regulation Regulation) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(Regulation, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Regulation> Regulations) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(Regulations);
            filter();
        }

        public void removeSelected(Regulation Regulation) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(Regulation);
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

                    Regulation Regulation = null;

                    if (o instanceof Regulation) {
                        Regulation = (Regulation) o;
                    }

                    if (Regulation != null) {
                        return RegulationList.this.Regulation_ICON;
                    }

                    return RegulationList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadRegulation extends SwingWorker<DefaultListModel, Regulation> {

        private DefaultListModel model;
        private Exception exception;

        public LoadRegulation(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Regulation> chunks) {
            mainframe.stopInActiveListener();
            for (Regulation Regulation : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Peraturan Daerah Nomor " + Regulation.getRegulationNumber());
                model.addElement(Regulation);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Regulation> regulations = new ArrayList<Regulation>();

                if (checkBox.isSelected()) {
                    regulations = logic.getRegulation(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    regulations = logic.getRegulation(mainframe.getSession());
                }

                double progress = 0.0;
                if (!regulations.isEmpty()) {
                    for (int i = 0; i < regulations.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / regulations.size();
                        setProgress((int) progress);
                        publish(regulations.get(i));
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
                JXErrorPane.showDialog(RegulationPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class RegulationProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private RegulationProgressListener() {
        }

        RegulationProgressListener(JProgressBar progressBar) {
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
