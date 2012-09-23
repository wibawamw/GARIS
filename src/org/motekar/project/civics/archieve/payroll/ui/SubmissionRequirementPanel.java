package org.motekar.project.civics.archieve.payroll.ui;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.payroll.objects.SubmissionRequirement;
import org.motekar.project.civics.archieve.payroll.sql.PayrollSubmissionBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */

public class SubmissionRequirementPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private PayrollSubmissionBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private SubmissionRequirementList submissionRequirementList = new SubmissionRequirementList();
    private JXTextField fieldName = new JXTextField();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private SubmissionRequirement selectedSubmissionRequirement = null;
    private LoadSubmissionRequirement worker;
    private SubmissionRequirementProgressListener progressListener;
    private StringBuilder errorString = new StringBuilder();

    public SubmissionRequirementPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new PayrollSubmissionBusinessLogic(mainframe.getConnection());
        construct();
        submissionRequirementList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Syarat-syarat Pengajuan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(submissionRequirementList), BorderLayout.CENTER);
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
                + "Fasilitas Syarat-syarat Pengajuan merupakan dokumen yang diperlukan "
                + "untuk pengajuan gaji pegawai\n\n"
                + "Tambah Syarat-syarat Pengajuan\n"
                + "Untuk menambah Syarat-syarat Pengajuan klik tombol paling kiri "
                + "kemudian isi data Syarat-syarat Pengajuan baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Syarat-syarat Pengajuan\n"
                + "Untuk merubah Syarat-syarat Pengajuan klik tombol kedua dari kiri "
                + "kemudian ubah data Syarat-syarat Pengajuan, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Syarat-syarat Pengajuan\n"
                + "Untuk menghapus Syarat-syarat Pengajuan klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Syarat-syarat Pengajuan "
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
        task.setTitle("Tambah / Edit / Hapus Syarat-syarat Pengajuan");
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
                "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama Syarat-syarat Pengajuan", cc.xy(1, 1));
        builder.add(fieldName, cc.xy(3, 1));

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
        addTooltip.setTitle("Tambah Syarat-syarat Pengajuan");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Syarat-syarat Pengajuan");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Syarat-syarat Pengajuan");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Syarat-syarat Pengajuan");

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

        submissionRequirementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        submissionRequirementList.setAutoCreateRowSorter(true);
        submissionRequirementList.addListSelectionListener(this);

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

    public void filter() {
        submissionRequirementList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldName.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        submissionRequirementList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldName.setText("");
        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
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
            selectedSubmissionRequirement = submissionRequirementList.getSelectedSubmissionRequirement();
            setFormValues();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Syarat-syarat Pengajuan");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Syarat-syarat Pengajuan");
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
                logic.deleteSubmissionRequirement(mainframe.getSession(), selectedSubmissionRequirement.getIndex());
                submissionRequirementList.removeSelected(selectedSubmissionRequirement);
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
            SubmissionRequirement newSubmissionRequirement = getSubmissionRequirement();

            if (isnew) {
                newSubmissionRequirement = logic.insertSubmissionRequirement(mainframe.getSession(), newSubmissionRequirement);
                isnew = false;
                iseditable = false;
                submissionRequirementList.addSubmissionRequirement(newSubmissionRequirement);
                selectedSubmissionRequirement = newSubmissionRequirement;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newSubmissionRequirement = logic.updateSubmissionRequirement(mainframe.getSession(), selectedSubmissionRequirement, newSubmissionRequirement);
                isedit = false;
                iseditable = false;
                submissionRequirementList.updateSubmissionRequirement(newSubmissionRequirement);
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
        if (submissionRequirementList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
    }

    private void setFormValues() {
        if (selectedSubmissionRequirement != null) {
            fieldName.setText(selectedSubmissionRequirement.getRequirementName());
            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private SubmissionRequirement getSubmissionRequirement() throws MotekarException {
        String name = fieldName.getText();

        if (name.equals("")) {
            errorString.append("<br>- Nama Syarat-syarat Pengajuan</br>");
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        SubmissionRequirement submissionRequirement = new SubmissionRequirement();

        submissionRequirement.setRequirementName(name);

        return submissionRequirement;
    }

    private class SubmissionRequirementList extends JXList {

        private Icon DIVISION_ICON = Mainframe.getResizableIconFromSource("resource/SubmissionRequirement.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public SubmissionRequirementList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadSubmissionRequirement((DefaultListModel) getModel());
            progressListener = new SubmissionRequirementProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public SubmissionRequirement getSelectedSubmissionRequirement() {
            SubmissionRequirement submissionRequirement = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof SubmissionRequirement) {
                submissionRequirement = (SubmissionRequirement) obj;
            }
            return submissionRequirement;
        }

        public void addSubmissionRequirement(SubmissionRequirement submissionRequirement) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(submissionRequirement);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateSubmissionRequirement(SubmissionRequirement submissionRequirement) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(submissionRequirement, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<SubmissionRequirement> submissionRequirements) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(submissionRequirements);
            filter();
        }

        public void removeSelected(SubmissionRequirement submissionRequirement) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(submissionRequirement);
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

                    SubmissionRequirement submissionRequirement = null;

                    if (o instanceof SubmissionRequirement) {
                        submissionRequirement = (SubmissionRequirement) o;
                    }

                    if (submissionRequirement != null) {
                        return SubmissionRequirementList.this.DIVISION_ICON;
                    }

                    return SubmissionRequirementList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadSubmissionRequirement extends SwingWorker<DefaultListModel, SubmissionRequirement> {

        private DefaultListModel model;
        private Exception exception;

        public LoadSubmissionRequirement(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<SubmissionRequirement> chunks) {
            mainframe.stopInActiveListener();
            for (SubmissionRequirement submissionRequirement : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Syarat-syarat Pengajuan " + submissionRequirement.getRequirementName());
                model.addElement(submissionRequirement);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<SubmissionRequirement> submissionRequirements = logic.getSubmissionRequirement(mainframe.getSession());

                double progress = 0.0;
                if (!submissionRequirements.isEmpty()) {
                    for (int i = 0; i < submissionRequirements.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / submissionRequirements.size();
                        setProgress((int) progress);
                        publish(submissionRequirements.get(i));
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
                JXErrorPane.showDialog(SubmissionRequirementPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class SubmissionRequirementProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private SubmissionRequirementProgressListener() {
        }

        SubmissionRequirementProgressListener(JProgressBar progressBar) {
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

