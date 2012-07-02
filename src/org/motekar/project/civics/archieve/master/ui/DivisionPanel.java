package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
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
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.objects.NodeCode;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class DivisionPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private DivisionTree divisionTree = new DivisionTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXLabel labelSuperTitle = new JXLabel("Bagian / Bidang");
    private JXLabel labelSuperDivision = new JXLabel();
    private JXLabel labelSubDivision = new JXLabel("");
    private JXLabel labelCode = new JXLabel("Kode Division");
    private JXLabel labelName = new JXLabel("Nama Division");
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Division selectedDivision = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadDivision worker;
    private DivisionProgressListener progressListener;
    private Unit unit;

    public DivisionPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (userGroup.getGroupName().equals("Administrator")) {
            System.out.println("1");
            divisionTree.loadData("");
        } else {
            System.out.println("2");
            unit = mainframe.getUnit();
            String modifier = generateUnitModifier(unit);
            divisionTree.loadData(modifier);
        }
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Struktur Bagian / Bidang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(new JScrollPane(divisionTree), BorderLayout.CENTER);
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

    protected Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,pref,1px,center:pref,1px,30px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,pref,fill:default:grow,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelSuperTitle.setFont(new Font(labelSuperTitle.getFont().getName(), Font.BOLD, labelSuperTitle.getFont().getSize()));
        labelSuperDivision.setFont(new Font(labelSuperDivision.getFont().getName(), Font.BOLD, labelSuperDivision.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        CellConstraints cc = new CellConstraints();

        builder.add(labelSuperTitle, cc.xy(1, 1));
        builder.add(labelSuperDivision, cc.xyw(3, 1, 6));

        builder.add(labelCode, cc.xy(1, 3));
        builder.add(labelSubDivision, cc.xyw(3, 3, 1));
        builder.add(fieldCode, cc.xyw(4, 3, 5));

        builder.add(labelName, cc.xy(1, 5));
        builder.add(fieldName, cc.xyw(3, 5, 6));
        
        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(builder.getPanel());
        
        scPane.getVerticalScrollBar().setUnitIncrement(20);
        
        return scPane;
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
        addTooltip.setTitle("Tambah Division/Sub Division");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Division/Sub Division");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Division/Sub Division");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Division/Sub Division");

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

        divisionTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        divisionTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = divisionTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof Division) {
                        selectedDivision = (Division) obj;
                    } else {
                        selectedDivision = null;
                    }
                    setFormValues();
                }
            }
        });

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.3)"
                + "(LEAF name=editor2 weight=0.7))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());


        splitPane.add(createLeftComponent(), "editor1");
        splitPane.add(createCenterComponent(), "editor2");

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        setFormState();
        setButtonState("");
    }

    private void setFormState() {
        fieldCode.setEnabled(iseditable);
        fieldName.setEnabled(iseditable);
        divisionTree.setEnabled(!iseditable);
    }

    private void setFormValues() {
        if (selectedDivision != null) {
            DefaultMutableTreeNode node = divisionTree.getSelectedNode();

            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent.isRoot()) {
                    labelSuperDivision.setText("");
                    labelSubDivision.setText("");
                    labelSuperTitle.setText("");
                    labelCode.setText("Kode Bagian / Bidang");
                    labelName.setText("Nama Bagian / Bidang");

                } else {
                    Object obj = parent.getUserObject();
                    Division division = null;
                    if (obj instanceof Division) {
                        division = (Division) obj;
                        labelSuperDivision.setText(division.toString());
                        StringBuilder builder = new StringBuilder();
                        if (!selectedDivision.getParentCode().equals("")) {
                            builder.append(division.getParentCode()).append(".");
                            labelCode.setText("Kode Sub Bagian / Bidang / Seksi");
                            labelName.setText("Nama Sub Bagian / Bidang / Seksi");
                            labelSuperTitle.setText("Bagian / Bidang");
                        } else {
                            builder.append(division.getParentCode());
                            labelSuperTitle.setText("");
                            labelCode.setText("Kode Bagian / Bidang");
                            labelName.setText("Nama Bagian / Bidang");
                        }
                        labelSubDivision.setText(builder.toString());
                    } else {
                        labelSuperDivision.setText("");
                        labelSubDivision.setText("");
                        labelSuperTitle.setText("");
                        labelCode.setText("Kode Bagian / Bidang");
                        labelName.setText("Nama Bagian / Bidang");
                    }
                }

                if (!selectedDivision.getParentCode().equals("")) {
                    fieldCode.setText(selectedDivision.getCode());
                } else {
                    fieldCode.setText(selectedDivision.getChildCode());
                }

                fieldName.setText(selectedDivision.getName());
                if (selectedDivision.getParentCode().equals("")) {
                    setButtonState("Save");
                } else {
                    setButtonState("disableAdd");
                }

            } else {
                clearForm();
                setButtonState("");
            }

        } else {
            clearForm();
            setButtonState("");
        }
    }

    private void clearForm() {
        fieldCode.setText("");
        fieldName.setText("");

        DefaultMutableTreeNode node = divisionTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                labelSuperDivision.setText("");
                labelSubDivision.setText("");
                labelSuperTitle.setText("");
                labelCode.setText("Kode Bagian / Bidang ");
                labelName.setText("Nama Bagian / Bidang");
            } else {
                if (selectedDivision != null) {
                    labelSuperDivision.setText(selectedDivision.toString());
                    StringBuilder builder = new StringBuilder();
                    builder.append(selectedDivision.getCode()).append(".");
                    labelCode.setText("Kode Sub Bagian / Bidang / Seksi");
                    labelName.setText("Nama Sub Bagian / Bidang / Seksi");
                    labelSuperTitle.setText("Bagian / Bidang ");
                    labelSubDivision.setText(builder.toString());
                }
            }
        } else {
            labelSuperDivision.setText("");
            labelSubDivision.setText("");
            labelSuperTitle.setText("");
            labelCode.setText("Kode Sub Bagian / Bidang / Seksi");
            labelName.setText("Nama Sub Bagian / Bidang / Seksi");
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
        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
        }
    }

    private Division getDivision() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldName.getText();
        String code = fieldCode.getText();
        String parentCode = labelSubDivision.getText().replace(".", "").trim();

        if (code.equals("")) {
            errorString.append("<br>- Kode Bagian / Bidang</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama Bagian / Bidang</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Division division = new Division();
        division.setCode(labelSubDivision.getText().trim() + code);
        division.setName(name);
        division.setParentCode(parentCode);

        if (selectedDivision != null) {
            division.setUnit(selectedDivision.getUnit());
        } else {
            division.setUnit(unit);
        }

        return division;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Division/Sub Division");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Division/Sub Division");
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

                logic.deleteDivision(mainframe.getSession(), selectedDivision.getCode());
                divisionTree.removeSelected();
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
            Division newDivision = getDivision();
            DefaultMutableTreeNode selectedNode = divisionTree.getSelectedNode();
            if (isnew) {
                newDivision = logic.insertDivision(mainframe.getSession(), newDivision);
                isnew = false;
                iseditable = false;
                newDivision.setStyled(true);
                divisionTree.addDivision(selectedNode, newDivision);
                selectedDivision = newDivision;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newDivision = logic.updateDivision(mainframe.getSession(), selectedDivision, newDivision);
                isedit = false;
                iseditable = false;
                newDivision.setStyled(true);
                divisionTree.updateDivision(newDivision);
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
        if (divisionTree.getRoot().getChildCount() > 0) {
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
        comp.add(fieldCode);
        comp.add(fieldName);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class DivisionTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/Division.png", new Dimension(16, 16));

        public DivisionTree() {
            root = new DefaultMutableTreeNode("Bidang / Sub Bidang");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData(String modifier) {
            worker = new LoadDivision(this, modifier);
            progressListener = new DivisionProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("Bidang / Sub Bidang");
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

        public DefaultMutableTreeNode getSelectedParentNode() {
            TreePath treePath = getSelectionPath();

            if (treePath == null) {
                return null;
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            return (DefaultMutableTreeNode) treeNode.getParent();
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

        public void addDivision(DefaultMutableTreeNode parent, Division division) {
            if (division != null) {
                DefaultMutableTreeNode accNode = new DefaultMutableTreeNode(division);
                if (parent != null) {
                    model.insertNodeInto(accNode, parent, parent.getChildCount());
                } else {
                    model.insertNodeInto(accNode, root, root.getChildCount());
                }
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

        public void updateDivision(Division division) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(division);
                model.nodeChanged(selNode);
            }
        }

        public void updateNode(DefaultMutableTreeNode node, Division division) {
            if (node != null) {
                node.setUserObject(division);
                model.nodeChanged(node);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    Division division = null;
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof Division) {
                        division = (Division) o;
                        if (division.getParentCode().equals("")) {
                            return DivisionTree.this.GROUP_ICON;
                        } else {
                            return DivisionTree.this.CHILD_ICON;
                        }
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

    private class LoadDivision extends SwingWorker<DefaultTreeModel, Division> {

        private DivisionTree divisionTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeCode> nodeCodes = new ArrayList<NodeCode>();
        //
        private String modifier = "";

        public LoadDivision(DivisionTree divisionTree, String modifier) {
            this.divisionTree = divisionTree;
            this.modifier = modifier;
            this.model = (DefaultTreeModel) divisionTree.getModel();
            root = new DefaultMutableTreeNode("Bagian / Sub Bagian");
            model.setRoot(root);
            nodeCodes = new ArrayList<NodeCode>();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Division> chunks) {
            mainframe.stopInActiveListener();
            for (Division division : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Bagian / Sub Bagian = " + division.toString());

                if (division.getParentCode().equals("")) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(division);
                    model.insertNodeInto(child, root, root.getChildCount());
                    NodeCode nodeCode = new NodeCode(division.getCode(), child);
                    nodeCodes.add(nodeCode);

                } else {
                    NodeCode idx = NodeCode.searchArrays(nodeCodes, division.getParentCode());
                    if (idx != null) {
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(division);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                    } else {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(division);
                        model.insertNodeInto(child, root, root.getChildCount());
                    }

                }
                divisionTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                ArrayList<Division> divisions = logic.getDivision(mainframe.getSession(), true, modifier);
                int size = divisions.size();

                double progress = 0.0;
                if (!divisions.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        Division division = divisions.get(i);
                        division.setStyled(true);

                        publish(division);
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

    private class DivisionProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private DivisionProgressListener() {
        }

        DivisionProgressListener(JProgressBar progressBar) {
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
