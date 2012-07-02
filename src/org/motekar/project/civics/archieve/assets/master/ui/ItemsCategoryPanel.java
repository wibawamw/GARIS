package org.motekar.project.civics.archieve.assets.master.ui;

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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.NodeIndex;
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
public class ItemsCategoryPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private AssetMasterBusinessLogic logic;
    private ItemsCategoryTree categoryTree = new ItemsCategoryTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXLabel labelSuperTitle = new JXLabel("Induk");
    private JXLabel labelSuperItemsCategory = new JXLabel();
    private JXLabel labelSubItemsCategory = new JXLabel("");
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    private JXComboBox comboTypes = new JXComboBox();
    //
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private ItemsCategory selectedItemsCategory = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadItemsCategory worker;
    private ItemsCategoryProgressListener progressListener;

    public ItemsCategoryPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new AssetMasterBusinessLogic(this.mainframe.getConnection());
        construct();
        categoryTree.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Struktur Bidang Barang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(new JScrollPane(categoryTree), BorderLayout.CENTER);
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
                + "Fasilitas Bidang Barang untuk pengisian data-data Bidang Barang yang ada di suatu dinas\n\n"
                + "Tambah Bidang Barang\n"
                + "Untuk menambah Bidang Barang klik tombol paling kiri "
                + "kemudian isi data Bidang Barang baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Bidang Barang\n"
                + "Untuk merubah Bidang Barang klik tombol kedua dari kiri "
                + "kemudian ubah data Bidang Barang, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Bidang Barang\n"
                + "Untuk menghapus Bidang Barang klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Bidang Barang "
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
        task.setTitle("Tambah / Edit / Hapus Struktur Bidang Barang");
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
                "right:pref,20px,right:pref,5px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelSuperTitle.setFont(new Font(labelSuperTitle.getFont().getName(), Font.BOLD, labelSuperTitle.getFont().getSize()));
        labelSuperItemsCategory.setFont(new Font(labelSuperItemsCategory.getFont().getName(), Font.BOLD, labelSuperItemsCategory.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        CellConstraints cc = new CellConstraints();

        builder.add(labelSuperTitle, cc.xy(1, 1));
        builder.add(labelSuperItemsCategory, cc.xyw(3, 1, 3));

        builder.addLabel("Kode Bidang Barang", cc.xy(1, 3));
        builder.add(labelSubItemsCategory, cc.xy(3, 3));
        builder.add(fieldCode, cc.xy(5, 3));

        builder.addLabel("Uraian", cc.xy(1, 5));
        builder.add(fieldName, cc.xyw(3, 5, 3));

        builder.addLabel("Tipe", cc.xy(1, 7));
        builder.add(comboTypes, cc.xyw(3, 7, 3));

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
        addTooltip.setTitle("Tambah Struktur Bidang Barang");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Struktur Bidang Barang");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Struktur Bidang Barang");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Struktur Bidang Barang");

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

        loadComboType();

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        categoryTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        categoryTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = categoryTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof ItemsCategory) {
                        selectedItemsCategory = (ItemsCategory) obj;
                    } else {
                        selectedItemsCategory = null;
                    }
                    setFormValues();
                }
            }
        });

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.30)"
                + "(LEAF name=editor2 weight=0.45) (LEAF name=editor3 weight=0.25))";
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

    private void loadComboType() {
        ArrayList<String> types = ItemsCategory.typeAsList();

        comboTypes.setModel(new ListComboBoxModel<String>(types));
        AutoCompleteDecorator.decorate(comboTypes);

    }

    private void setFormState() {
        fieldCode.setEnabled(iseditable);
        fieldName.setEnabled(iseditable);
        categoryTree.setEnabled(!iseditable);
        comboTypes.setEnabled(iseditable);
    }

    private void clearForm() {
        fieldCode.setText("");
        fieldName.setText("");

        comboTypes.setSelectedIndex(0);

        DefaultMutableTreeNode node = categoryTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                labelSuperItemsCategory.setText("-");
                labelSubItemsCategory.setText("");
            } else {
                if (selectedItemsCategory != null) {
                    labelSuperItemsCategory.setText(selectedItemsCategory.toString());

                    StringBuilder builder = new StringBuilder();

                    builder.append(selectedItemsCategory.getCategoryCode()).append(".");
                    labelSubItemsCategory.setText(builder.toString());
                    comboTypes.setSelectedIndex(selectedItemsCategory.getTypes());
                }
            }
        } else {
            labelSuperItemsCategory.setText("-");
            labelSubItemsCategory.setText("");
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

    private ItemsCategory getItemsCategory() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldName.getText();
        String code = fieldCode.getText();

        Integer types = comboTypes.getSelectedIndex();

        if (code.equals("")) {
            errorString.append("<br>- Kode Bidang Barang</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Uraian</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }


        DefaultMutableTreeNode node = categoryTree.getSelectedNode();

        ItemsCategory category = new ItemsCategory();
        category.setCategoryCode(labelSubItemsCategory.getText().trim() + code);
        category.setCategoryName(name);
        category.setTypes(types);
        if (node == null) {
            category.setGroup(false);
        } else {
            if (node.getChildCount() > 0) {
                category.setGroup(true);
            } else {
                category.setGroup(false);
            }
        }



        return category;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Bidang Barang");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Bidang Barang");
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
                DefaultMutableTreeNode node = categoryTree.getSelectedNode();
                DefaultMutableTreeNode parent = null;
                if (node != null) {
                    parent = (DefaultMutableTreeNode) node.getParent();
                }

                logic.deleteItemsCategory(mainframe.getSession(), selectedItemsCategory.getCategoryCode());
                categoryTree.removeSelected();

                if (parent != null) {
                    Object object = parent.getUserObject();
                    ItemsCategory category = null;

                    if (object instanceof ItemsCategory) {
                        category = (ItemsCategory) object;
                    }

                    if (parent.getChildCount() <= 0 && category != null) {
                        logic.updateItemsCategory(mainframe.getSession(), category, false);
                    }
                }

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
            ItemsCategory newItemsCategory = getItemsCategory();
            DefaultMutableTreeNode selectedNode = categoryTree.getSelectedNode();

            if (isnew) {
                if (selectedNode == null) {
                    newItemsCategory = logic.insertItemsCategory(mainframe.getSession(), newItemsCategory);
                } else {
                    if (selectedNode.isRoot()) {
                        newItemsCategory = logic.insertItemsCategory(mainframe.getSession(), newItemsCategory);
                    } else {
                        Object parentObj = selectedNode.getUserObject();
                        if (parentObj instanceof ItemsCategory) {
                            ItemsCategory parent = (ItemsCategory) parentObj;
                            newItemsCategory = logic.insertItemsCategory(mainframe.getSession(), parent, newItemsCategory);
                        } else {
                            newItemsCategory = logic.insertItemsCategory(mainframe.getSession(), newItemsCategory);
                        }
                    }
                }

                isnew = false;
                iseditable = false;
                newItemsCategory.setStyled(true);
                categoryTree.addItemsCategory(selectedNode, newItemsCategory);
                selectedItemsCategory = newItemsCategory;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newItemsCategory = logic.updateItemsCategory(mainframe.getSession(), selectedItemsCategory, newItemsCategory);
                isedit = false;
                iseditable = false;
                newItemsCategory.setStyled(true);
                categoryTree.updateItemsCategory(newItemsCategory);
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
        if (categoryTree.getRoot().getChildCount() > 0) {
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

    private void setFormValues() {
        if (selectedItemsCategory != null) {
            DefaultMutableTreeNode node = categoryTree.getSelectedNode();

            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent.isRoot()) {
                    labelSuperItemsCategory.setText("-");
                    labelSubItemsCategory.setText("");
                    fieldCode.setText(selectedItemsCategory.getCategoryCode());
                } else {
                    Object obj = parent.getUserObject();
                    ItemsCategory category = null;
                    if (obj instanceof ItemsCategory) {
                        category = (ItemsCategory) obj;
                        labelSuperItemsCategory.setText(category.toString());
                        StringBuilder builder = new StringBuilder();

                        if (parent.isRoot()) {
                            builder.append(category.getCategoryCode());
                            fieldCode.setText(selectedItemsCategory.getCategoryCode());
                        } else {
                            builder.append(category.getCategoryCode()).append(".");
                            fieldCode.setText(selectedItemsCategory.getChildCode());
                        }
                        labelSubItemsCategory.setText(builder.toString());
                    } else {
                        labelSuperItemsCategory.setText("-");
                        labelSubItemsCategory.setText("");
                        fieldCode.setText(selectedItemsCategory.getCategoryCode());
                    }
                }

                fieldName.setText(selectedItemsCategory.getCategoryName());
                comboTypes.setSelectedIndex(selectedItemsCategory.getTypes());
                setButtonState("Save");
            } else {
                clearForm();
                setButtonState("");
            }

        } else {
            clearForm();
            setButtonState("");
        }
    }

    private class ItemsCategoryTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/box2.png", new Dimension(16, 16));

        public ItemsCategoryTree() {
            root = new DefaultMutableTreeNode("Struktur Bidang Barang");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData() {
            worker = new LoadItemsCategory(this);
            progressListener = new ItemsCategoryProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("Struktur Bidang Barang");
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

        public void addItemsCategory(DefaultMutableTreeNode parent, ItemsCategory category) {
            if (category != null) {
                DefaultMutableTreeNode accNode = new DefaultMutableTreeNode(category);
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

        public void updateItemsCategory(ItemsCategory category) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(category);
                model.nodeChanged(selNode);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    ItemsCategory category = null;
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof ItemsCategory) {
                        category = (ItemsCategory) o;
                        if (category.isGroup()) {
                            return ItemsCategoryTree.this.GROUP_ICON;
                        } else {
                            return ItemsCategoryTree.this.CHILD_ICON;
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

    private class LoadItemsCategory extends SwingWorker<DefaultTreeModel, ItemsCategory> {

        private ItemsCategoryTree categoryTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeIndex> nodeIndexs = new ArrayList<NodeIndex>();

        public LoadItemsCategory(ItemsCategoryTree categoryTree) {
            this.categoryTree = categoryTree;
            this.model = (DefaultTreeModel) categoryTree.getModel();
            root = new DefaultMutableTreeNode("Struktur Bidang Barang");
            model.setRoot(root);
            nodeIndexs = new ArrayList<NodeIndex>();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemsCategory> chunks) {
            mainframe.stopInActiveListener();
            for (ItemsCategory category : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Bidang Barang = " + category.toString());

                if (category.isGroup()) {
                    if (category.getParentIndex() == 0) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category);
                        model.insertNodeInto(child, root, root.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(category.getIndex(), child);
                        nodeIndexs.add(nodeIndex);

                    } else {
                        NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, category.getParentIndex());
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(category.getIndex(), child);
                        nodeIndexs.add(nodeIndex);
                    }

                } else {
                    NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, category.getParentIndex());
                    if (idx != null) {
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                    } else {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category);
                        model.insertNodeInto(child, root, root.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(category.getIndex(), child);
                        nodeIndexs.add(nodeIndex);
                    }
                }
//                categoryTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsCategory> categorys = logic.getItemsCategory(mainframe.getSession());
                int size = categorys.size();

                double progress = 0.0;
                if (!categorys.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        ItemsCategory category = categorys.get(i);
                        category.setStyled(true);
                        publish(category);
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

    private class ItemsCategoryProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ItemsCategoryProgressListener() {
        }

        ItemsCategoryProgressListener(JProgressBar progressBar) {
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