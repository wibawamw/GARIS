package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.NodeIndex;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ProgramActivityPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private ProgramActivityTree paTree = new ProgramActivityTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldProgramCode = new JXTextField();
    private JXTextField fieldProgramName = new JXTextField();
    private JXLabel labelProgramTitle = new JXLabel("Program");
    private JXLabel labelProgramName = new JXLabel();
    private JXLabel labelSubCode = new JXLabel("");
    private JXTextField fieldActivityCode = new JXTextField();
    private JXTextField fieldActivityName = new JXTextField();
    private JPanel cardPanel = new JPanel();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Program selectedProgram = null;
    private Activity selectedActivity = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadProgram worker;
    private ProgramProgressListener progressListener;

    public ProgramActivityPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        paTree.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Program & Kegiatan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(new JScrollPane(paTree), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JXPanel createCenterComponent() {

        constructCardPanel();

        JXTitledPanel titledPanel = new JXTitledPanel("Rincian Data");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createButtonsPanel(), BorderLayout.NORTH);
        collapasepanel.add(cardPanel, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Fasilitas Program / Kegiatan untuk pengisian data-data Program "
                + "dan Kegiatan yang ada di suatu dinas\n\n"
                + "Tambah Rekening\n"
                + "Untuk menambah Program / Kegiatan klik tombol paling kiri "
                + "kemudian isi data Program / Kegiatan baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Program / Kegiatan\n"
                + "Untuk merubah Program / Kegiatan klik tombol kedua dari kiri "
                + "kemudian ubah data Program / Kegiatan, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Program / Kegiatan\n"
                + "Untuk menghapus Program / Kegiatan klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Rekening "
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
        task.setTitle("Tambah / Edit / Hapus Program / Kegiatan");
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

    protected JPanel createProgramPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,20px,right:pref,5px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Input Program", cc.xyw(1, 1, 5));

        builder.addLabel("Kode Program", cc.xy(1, 3));
        builder.add(fieldProgramCode, cc.xyw(3, 3, 3));

        builder.addLabel("Nama Program", cc.xy(1, 5));
        builder.add(fieldProgramName, cc.xyw(3, 5, 3));

        return builder.getPanel();
    }

    protected JPanel createActivityPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,20px,right:pref,5px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelProgramTitle.setFont(new Font(labelProgramTitle.getFont().getName(), Font.BOLD, labelProgramTitle.getFont().getSize()));
        labelProgramName.setFont(new Font(labelProgramName.getFont().getName(), Font.BOLD, labelProgramName.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7}});

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Input Kegiatan", cc.xyw(1, 1, 5));

        builder.add(labelProgramTitle, cc.xy(1, 3));
        builder.add(labelProgramName, cc.xyw(3, 3, 3));

        builder.addLabel("Kode Kegiatan", cc.xy(1, 5));
        builder.add(labelSubCode, cc.xy(3, 5));
        builder.add(fieldActivityCode, cc.xy(5, 5));

        builder.addLabel("Nama Kegiatan", cc.xy(1, 7));
        builder.add(fieldActivityName, cc.xyw(3, 7, 3));

        return builder.getPanel();
    }

    private void constructCardPanel() {
        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createProgramPanel(), "0");
        cardPanel.add(createActivityPanel(), "1");
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
        addTooltip.setTitle("Tambah Program / Kegiatan");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Program / Kegiatan");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Program / Kegiatan");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Program / Kegiatan");

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

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        paTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        paTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = paTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof Program) {
                        selectedProgram = (Program) obj;
                        selectedActivity = null;
                    } else if (obj instanceof Activity) {
                        selectedProgram = null;
                        selectedActivity = (Activity) obj;
                    } else {
                        selectedProgram = null;
                        selectedActivity = null;
                    }
                    setFormValues();
                }
            }
        });

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.36)"
                + "(LEAF name=editor2 weight=0.39) (LEAF name=editor3 weight=0.25))";
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
        setButtonState("disableAll");
    }

    private void setFormState() {
        fieldProgramCode.setEnabled(iseditable);
        fieldProgramName.setEnabled(iseditable);
        fieldActivityCode.setEnabled(iseditable);
        fieldActivityName.setEnabled(iseditable);
        paTree.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldProgramCode.setText("");
        fieldProgramName.setText("");
        fieldActivityCode.setText("");
        fieldActivityName.setText("");

        DefaultMutableTreeNode node = paTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                labelProgramName.setText("-");
                labelSubCode.setText("");
                ((CardLayout) cardPanel.getLayout()).first(cardPanel);
                if (fieldProgramCode.isEnabled()) {
                    fieldProgramCode.requestFocus();
                }
            } else {
                if (selectedProgram != null) {
                    labelProgramName.setText(selectedProgram.toString());
                    StringBuilder builder = new StringBuilder();
                    builder.append(selectedProgram.getProgramCode()).append(".");
                    labelSubCode.setText(builder.toString());
                    ((CardLayout) cardPanel.getLayout()).last(cardPanel);
                    if (fieldActivityCode.isEnabled()) {
                        fieldActivityCode.requestFocus();
                    }
                }
            }
        } else {
            labelProgramName.setText("-");
            labelSubCode.setText("");
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

        } else if (state.equals("disableAdd")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
        } else if (state.equals("disableAll")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
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

    private Program getProgram() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldProgramName.getText();
        String code = fieldProgramCode.getText();

        if (code.equals("")) {
            errorString.append("<br>- Kode Program</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama Program</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Program program = new Program();
        program.setProgramCode(code);
        program.setProgramName(name);

        return program;
    }

    private Activity getActivity() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldActivityName.getText();
        String code = fieldActivityCode.getText();

        if (code.equals("")) {
            errorString.append("<br>- Kode Kegiatan</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama Kegiatan</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Activity activity = new Activity();
        activity.setActivityCode(labelSubCode.getText().trim() + code);
        activity.setActivityName(name);
        if (selectedProgram != null) {
            activity.setProgramIndex(selectedProgram.getIndex());
        } else {
            DefaultMutableTreeNode selectedNode = paTree.getSelectedNode();
            if (selectedNode != null) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
                Object object = parent.getUserObject();
                if (object instanceof Program) {
                    activity.setProgramIndex(((Program) object).getIndex());
                }
            }
        }


        return activity;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Program / Kegiatan");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        if (selectedProgram != null) {
            statusLabel.setText("Ubah Program");
            fieldProgramCode.requestFocus();
            fieldProgramCode.selectAll();
        } else {
            statusLabel.setText("Ubah Kegiatan");
            fieldActivityCode.requestFocus();
            fieldActivityCode.selectAll();
        }
        
        defineCustomFocusTraversalPolicy();
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
                if (selectedProgram != null) {
                    boolean inexp = logic.getProgramInExpedition(mainframe.getSession(), selectedProgram.getProgramCode());

                    if (!inexp) {
                        logic.deleteProgram(mainframe.getSession(), selectedProgram.getIndex());
                        paTree.removeSelected();
                        clearForm();
                        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("<html>Program ").
                                append("<b>").
                                append(selectedProgram.toString()).
                                append("</b>").
                                append(" tidak boleh dihapus karena sudah dipakai di SPPD");
                        throw new MotekarException(builder.toString());
                    }
                } else if (selectedActivity != null) {
                    boolean inexp = logic.getActivityInExpedition(mainframe.getSession(), selectedActivity.getActivityCode());

                    if (!inexp) {
                        logic.deleteActivity(mainframe.getSession(), selectedActivity.getIndex());
                        paTree.removeSelected();
                        clearForm();
                        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("<html>Kegiatan ").
                                append("<b>").
                                append(selectedActivity.toString()).
                                append("</b>").
                                append(" tidak boleh dihapus karena sudah dipakai di SPPD");
                        throw new MotekarException(builder.toString());
                    }
                }

            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            } catch (MotekarException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat menghapus data ",
                        ex.getMessage(), "Error", null, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    private void onSave() {
        try {
            Program newProgram;
            Activity newActivity;
            DefaultMutableTreeNode selectedNode = paTree.getSelectedNode();
            Object selectedObject = paTree.getSelectedObject();

            if (isnew) {
                if (selectedNode.isRoot()) {
                    newProgram = getProgram();
                    newProgram = logic.insertProgram(mainframe.getSession(), newProgram);
                    paTree.addProgram(selectedNode, newProgram);
                    selectedProgram = newProgram;
                    selectedActivity = null;
                    setButtonState("Save");
                } else {
                    if (selectedObject instanceof Program) {
                        newActivity = getActivity();
                        newActivity = logic.insertActivity(mainframe.getSession(), newActivity);
                        paTree.addActivity(selectedNode, newActivity);
                        selectedActivity = newActivity;
                        selectedProgram = null;
                        setButtonState("disableAdd");
                    }
                }
                isnew = false;
                iseditable = false;
                setFormState();

            } else if (isedit) {
                if (selectedObject instanceof Program) {
                    newProgram = getProgram();
                    newProgram = logic.updateProgram(mainframe.getSession(), selectedProgram, newProgram);
                    paTree.updateProgram(newProgram);
                    selectedProgram = newProgram;
                    selectedActivity = null;
                    setButtonState("Save");
                } else if (selectedObject instanceof Activity) {
                    newActivity = getActivity();
                    newActivity = logic.updateActivity(mainframe.getSession(), selectedActivity, newActivity);
                    paTree.updateActivity(newActivity);
                    selectedActivity = newActivity;
                    selectedProgram = null;
                    setButtonState("disableAdd");
                }
                isedit = false;
                iseditable = false;

                setFormState();
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
        if (paTree.getRoot().getChildCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
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

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldProgramCode);
        comp.add(fieldProgramName);
        comp.add(fieldActivityCode);
        comp.add(fieldActivityName);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedActivity != null) {
            DefaultMutableTreeNode node = paTree.getSelectedNode();
            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent.isRoot()) {
                    labelProgramName.setText("-");
                    labelSubCode.setText("");
                } else {
                    Object obj = parent.getUserObject();
                    Program program = null;
                    if (obj instanceof Program) {
                        program = (Program) obj;
                        labelProgramName.setText(program.toString());
                        StringBuilder builder = new StringBuilder();
                        builder.append(program.getProgramCode()).append(".");
                        labelSubCode.setText(builder.toString());
                    } else {
                        labelProgramName.setText("-");
                        labelSubCode.setText("");
                    }
                }

                fieldActivityCode.setText(selectedActivity.getChildCode());
                fieldActivityName.setText(selectedActivity.getActivityName());
                setButtonState("disableAdd");

            } else {
                clearForm();
                setButtonState("");
            }
            ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        } else if (selectedProgram != null) {
            fieldProgramCode.setText(selectedProgram.getProgramCode());
            fieldProgramName.setText(selectedProgram.getProgramName());
            setButtonState("Save");
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        } else {
            clearForm();
            setButtonState("");
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        }
    }

    private class ProgramActivityTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/activity.png", new Dimension(16, 16));

        public ProgramActivityTree() {
            root = new DefaultMutableTreeNode("Program / Kegiatan");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData() {
            worker = new LoadProgram(this);
            progressListener = new ProgramProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("Program / Kegiatan");
            model = new DefaultTreeModel(root);
        }

        public DefaultMutableTreeNode getRoot() {
            return (DefaultMutableTreeNode) model.getRoot();
        }

        public DefaultMutableTreeNode getSelectedNode() {
            TreePath treePath = getSelectionPath();

            if (treePath == null) {
                return null;
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            return treeNode;
        }

        public Object getSelectedObject() {
            TreePath treePath = getSelectionPath();

            if (treePath == null) {
                return null;
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            Object obj = treeNode.getUserObject();
            return obj;
        }

        private void goToPath() {
            int row = 0;

            TreePath selPath = getSelectionPath();
            if (selPath != null) {
                row = getRowForPath(selPath);
            }
            setSelectionRow(row + 1);
        }

        public void addProgram(DefaultMutableTreeNode parent, Program program) {
            if (program != null) {
                DefaultMutableTreeNode programNode = new DefaultMutableTreeNode(program);
                model.insertNodeInto(programNode, parent, parent.getChildCount());
                expandAll();
                goToPath();
            }
        }

        public void addActivity(DefaultMutableTreeNode parent, Activity activity) {
            if (activity != null) {
                DefaultMutableTreeNode activityNode = new DefaultMutableTreeNode(activity);
                model.insertNodeInto(activityNode, parent, parent.getChildCount());
                expandAll();
                goToPath();
            }
        }

        public void removeSelected() {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                model.removeNodeFromParent(selNode);
            }
        }

        public void updateProgram(Program program) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(program);
                model.nodeChanged(selNode);
            }
        }

        public void updateActivity(Activity activity) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(activity);
                model.nodeChanged(selNode);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof Program) {
                        return ProgramActivityTree.this.GROUP_ICON;
                    } else if (o instanceof Activity) {
                        return ProgramActivityTree.this.CHILD_ICON;
                    }
                    return null;
                }
            }, new StringValue() {

                public String getString(Object o) {
                    return o.toString();
                }
            });
        }
    }

    private class LoadProgram extends SwingWorker<DefaultTreeModel, Object> {

        private ProgramActivityTree programActivityTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeIndex> nodeIndexs = new ArrayList<NodeIndex>();

        public LoadProgram(ProgramActivityTree programActivityTree) {
            this.programActivityTree = programActivityTree;
            this.model = (DefaultTreeModel) programActivityTree.getModel();
            root = new DefaultMutableTreeNode("Program / Kegiatan");
            model.setRoot(root);
            nodeIndexs = new ArrayList<NodeIndex>();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Object> chunks) {
            mainframe.stopInActiveListener();
            for (Object object : chunks) {
                if (isCancelled()) {
                    break;
                }

                if (object instanceof Program) {
                    Program program = (Program) object;

                    statusLabel.setText("Memuat Program = " + program.toString());

                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(object);
                    model.insertNodeInto(child, root, root.getChildCount());
                    NodeIndex nodeIndex = new NodeIndex(program.getIndex(), child);
                    nodeIndexs.add(nodeIndex);



                } else if (object instanceof Activity) {
                    Activity activity = (Activity) object;

                    statusLabel.setText("Memuat Kegiatan = " + activity.toString());

                    NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, activity.getProgramIndex());
                    DefaultMutableTreeNode parent = idx.getNode();
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(object);
                    model.insertNodeInto(child, parent, parent.getChildCount());
                }
                programActivityTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                ArrayList<Program> programs = logic.getProgram(mainframe.getSession());
                ArrayList<Activity> activitys = logic.getActivity(mainframe.getSession());
                int size = programs.size();

                double progress = 0.0;
                if (!programs.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        Program program = programs.get(i);
                        publish(program);
                        setProgress((int) progress);
                        Thread.sleep(100L);
                    }
                }

                setProgress(0);
                size = activitys.size();

                if (!activitys.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        Activity activity = activitys.get(i);
                        publish(activity);
                        setProgress((int) progress);
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
            mainframe.startInActiveListener();

        }
    }

    private class ProgramProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ProgramProgressListener() {
        }

        ProgramProgressListener(JProgressBar progressBar) {
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
