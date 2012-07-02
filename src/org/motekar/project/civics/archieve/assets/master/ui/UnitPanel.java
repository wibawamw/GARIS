package org.motekar.project.civics.archieve.assets.master.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.NodeIndex;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class UnitPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private AssetMasterBusinessLogic logic;
    private UnitTree unitTree = new UnitTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXLabel labelSuperTitle = new JXLabel("Unit");
    private JXLabel labelSuperUnit = new JXLabel();
    private JXLabel labelSubUnit = new JXLabel("");
    private JXLabel labelCode = new JXLabel("Kode Unit");
    private JXLabel labelName = new JXLabel("Nama Unit");
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    private JXTextArea fieldStreetName = new JXTextArea();
    private JXTextField fieldRT = new JXTextField();
    private JXTextField fieldRW = new JXTextField();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Unit selectedUnit = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadUnit worker;
    private UnitProgressListener progressListener;
    private EventList<Region> urbanRegions;

    public UnitPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new AssetMasterBusinessLogic(this.mainframe.getConnection());
        construct();
        unitTree.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Struktur Unit Pengguna Barang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(new JScrollPane(unitTree), BorderLayout.CENTER);
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

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,pref,1px,center:pref,1px,30px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,pref,fill:default:grow,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelSuperTitle.setFont(new Font(labelSuperTitle.getFont().getName(), Font.BOLD, labelSuperTitle.getFont().getSize()));
        labelSuperUnit.setFont(new Font(labelSuperUnit.getFont().getName(), Font.BOLD, labelSuperUnit.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldStreetName);

        CellConstraints cc = new CellConstraints();

        builder.add(labelSuperTitle, cc.xy(1, 1));
        builder.add(labelSuperUnit, cc.xyw(3, 1, 6));

        builder.add(labelCode, cc.xy(1, 3));
        builder.add(labelSubUnit, cc.xyw(3, 3, 1));
        builder.add(fieldCode, cc.xyw(4, 3, 5));

        builder.add(labelName, cc.xy(1, 5));
        builder.add(fieldName, cc.xyw(3, 5, 6));

        builder.addLabel("Kecamatan", cc.xy(1, 7));
        builder.add(comboUrban, cc.xyw(3, 7, 6));

        builder.addLabel("Kelurahan", cc.xy(1, 9));
        builder.add(comboSubUrban, cc.xyw(3, 9, 6));

        builder.addLabel("Nama Jalan/Kampung", cc.xy(1, 11));
        builder.add(scPane, cc.xywh(3, 11, 6, 3));

        builder.addLabel("RT / RW", cc.xy(1, 15));
        builder.add(fieldRT, cc.xyw(3, 15, 1));
        builder.addLabel("/", cc.xyw(5, 15, 1));
        builder.add(fieldRW, cc.xyw(7, 15, 1));

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
        addTooltip.setTitle("Tambah Unit/Sub Unit");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Unit/Sub Unit");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Unit/Sub Unit");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Unit/Sub Unit");

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

        loadComboUrban();
        loadComboSubUrban();

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        comboUrban.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadComboSubUrban();
                }
            }
        });

        unitTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        unitTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = unitTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof Unit) {
                        selectedUnit = (Unit) obj;
                    } else {
                        selectedUnit = null;
                    }
                    setFormValues();
                }
            }
        });

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.45)"
                + "(LEAF name=editor2 weight=0.55))";
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

    private void loadComboUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    return logic.getUrbanRegion(mainframe.getSession());
                }
            };
            lw.execute();
            final EventList<Region> regions = GlazedLists.eventList(lw.get());

            regions.add(0, new Region());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUrban, regions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboSubUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    Object obj = comboUrban.getSelectedItem();
                    Region pr = null;
                    if (obj instanceof Region) {
                        pr = (Region) obj;
                    }

                    ArrayList<Region> regions = new ArrayList<Region>();

                    if (pr != null) {
                        regions = logic.getSubUrbanRegion(mainframe.getSession(), pr);

                    }
                    return regions;
                }
            };
            lw.execute();
            urbanRegions = GlazedLists.eventList(lw.get());

            urbanRegions.add(0, new Region());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboSubUrban, urbanRegions);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void reloadComboSubUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    Object obj = comboUrban.getSelectedItem();
                    Region pr = null;
                    if (obj instanceof Region) {
                        pr = (Region) obj;
                    }

                    ArrayList<Region> regions = new ArrayList<Region>();

                    if (pr != null) {
                        regions = logic.getSubUrbanRegion(mainframe.getSession(), pr);

                    }
                    return regions;
                }
            };
            lw.execute();
            comboSubUrban.getEditor().setItem(null);
            urbanRegions.clear();
            EventList<Region> evtRegion = GlazedLists.eventList(lw.get());
            urbanRegions.addAll(evtRegion);
            urbanRegions.add(0, new Region());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void setFormState() {
        fieldCode.setEnabled(iseditable);
        fieldName.setEnabled(iseditable);
        unitTree.setEnabled(!iseditable);
        comboUrban.setEnabled(iseditable);
        comboSubUrban.setEnabled(iseditable);
        fieldStreetName.setEnabled(iseditable);
        fieldRT.setEnabled(iseditable);
        fieldRW.setEnabled(iseditable);
    }

    private void setFormValues() {
        if (selectedUnit != null) {
            DefaultMutableTreeNode node = unitTree.getSelectedNode();

            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent.isRoot()) {
                    labelSuperUnit.setText("");
                    labelSubUnit.setText("");
                    labelSuperTitle.setText("");
                    labelCode.setText("Kode Unit");
                    labelName.setText("Nama Unit");

                } else {
                    Object obj = parent.getUserObject();
                    Unit unit = null;
                    if (obj instanceof Unit) {
                        unit = (Unit) obj;
                        labelSuperUnit.setText(unit.toString());
                        StringBuilder builder = new StringBuilder();
                        if (!selectedUnit.getUnitType().equals(Unit.TYPE_UPB)) {
                            builder.append(unit.getUnitCode()).append(".");
                            labelCode.setText("Kode Sub Unit");
                            labelName.setText("Nama Sub Unit");
                            labelSuperTitle.setText("Unit");
                        } else {
                            builder.append(unit.getUnitCode());
                            labelSuperTitle.setText("");
                            labelCode.setText("Kode Unit");
                            labelName.setText("Nama Unit");
                        }
                        labelSubUnit.setText(builder.toString());
                    } else {
                        labelSuperUnit.setText("");
                        labelSubUnit.setText("");
                        labelSuperTitle.setText("");
                        labelCode.setText("Kode Unit");
                        labelName.setText("Nama Unit");
                    }
                }

                if (!selectedUnit.getUnitType().equals(Unit.TYPE_SUB_UPB)) {
                    fieldCode.setText(selectedUnit.getUnitCode());
                } else {
                    fieldCode.setText(selectedUnit.getChildCode());
                }

                fieldName.setText(selectedUnit.getUnitName());
//                if (!selectedUnit.getUnitType().equals(Unit.TYPE_SUB_UPB)) {
//                    setButtonState("Save");
//                } else {
//                    setButtonState("disableAdd");
//                }

                if (selectedUnit.getUrban() == null) {
                    comboUrban.getEditor().setItem(null);
                } else {
                    comboUrban.setSelectedItem(selectedUnit.getUrban());
                }

                if (selectedUnit.getSubUrban() == null) {
                    comboSubUrban.getEditor().setItem(null);
                } else {
                    comboSubUrban.setSelectedItem(selectedUnit.getSubUrban());
                }

                fieldStreetName.setText(selectedUnit.getStreetName());
                fieldRT.setText(selectedUnit.getRt());
                fieldRW.setText(selectedUnit.getRw());

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

    private void clearForm() {
        fieldCode.setText("");
        fieldName.setText("");
        comboSubUrban.getEditor().setItem(null);
        comboUrban.getEditor().setItem(null);
        fieldStreetName.setText("");
        fieldRT.setText("");
        fieldRW.setText("");

        DefaultMutableTreeNode node = unitTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                labelSuperUnit.setText("");
                labelSubUnit.setText("");
                labelSuperTitle.setText("");
                labelCode.setText("Kode Unit");
                labelName.setText("Nama Unit");
            } else {
                if (selectedUnit != null) {
                    labelSuperUnit.setText(selectedUnit.toString());
                    StringBuilder builder = new StringBuilder();
                    builder.append(selectedUnit.getUnitCode()).append(".");
                    labelCode.setText("Kode Sub Unit");
                    labelName.setText("Nama Sub Unit");
                    labelSuperTitle.setText("Unit");
                    labelSubUnit.setText(builder.toString());
                }
            }
        } else {
            labelSuperUnit.setText("");
            labelSubUnit.setText("");
            labelSuperTitle.setText("");
            labelCode.setText("Kode Unit");
            labelName.setText("Nama Unit");
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

    private Unit getUnit() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldName.getText();
        String code = fieldCode.getText();

        String streetName = fieldStreetName.getText();
        String rt = fieldRT.getText();
        String rw = fieldRW.getText();

        Region urban = null;
        Object urbanObj = comboUrban.getSelectedItem();
        if (urbanObj instanceof Region) {
            if (comboUrban.getSelectedIndex() > 0) {
                urban = (Region) urbanObj;
            }
        }

        Region subUrban = null;
        Object subUrbanObj = comboSubUrban.getSelectedItem();
        if (subUrbanObj instanceof Region) {
            if (comboSubUrban.getSelectedIndex() > 0) {
                subUrban = (Region) subUrbanObj;
            }
        }

        if (code.equals("")) {
            errorString.append("<br>- Kode UPB</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama UPB</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Unit unit = new Unit();
        unit.setUnitCode(labelSubUnit.getText().trim() + code);
        unit.setUnitName(name);
        unit.setStreetName(streetName);
        unit.setRt(rt);
        unit.setRw(rw);

        unit.setUrban(urban);
        unit.setSubUrban(subUrban);

        DefaultMutableTreeNode node = unitTree.getSelectedNode();
        if (node != null) {
            if (!node.isRoot()) {
                if (isnew) {
                    unit.setUnitType(Unit.TYPE_SUB_UPB);
                    unit.setGroup(false);
                } else {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                    if (parent.isRoot()) {
                        unit.setUnitType(Unit.TYPE_UPB);
                        unit.setGroup(true);
                    } else {
                        unit.setUnitType(Unit.TYPE_SUB_UPB);
                        unit.setGroup(false);
                    }
                }
            } else {
                unit.setUnitType(Unit.TYPE_UPB);
                unit.setGroup(true);
            }
        } else {
            unit.setUnitType(Unit.TYPE_UPB);
            unit.setGroup(true);
        }

        return unit;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Unit/Sub Unit");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Unit/Sub Unit");
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

                DefaultMutableTreeNode parentNode = unitTree.getSelectedParentNode();
                Unit parent = null;
                Object object = parentNode.getUserObject();
                if (object instanceof Unit) {
                    parent = (Unit) object;
                }
                if (parent != null) {
                    parent.setGroup(false);
                    logic.deleteUnit(mainframe.getSession(), selectedUnit.getUnitCode(), parent);
                    unitTree.removeSelected();
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
            Unit newUnit = getUnit();
            DefaultMutableTreeNode selectedNode = unitTree.getSelectedNode();
            Unit parent = null;
            if (isnew) {
                if (selectedNode == null) {
                    newUnit = logic.insertUnit(mainframe.getSession(), newUnit);
                } else {
                    if (selectedNode.isRoot()) {
                        newUnit = logic.insertUnit(mainframe.getSession(), newUnit);
                    } else {
                        Object parentObj = selectedNode.getUserObject();
                        if (parentObj instanceof Unit) {
                            parent = (Unit) parentObj;
                            parent.setGroup(true);
                            newUnit = logic.insertUnit(mainframe.getSession(), parent, newUnit);
                        } else {
                            newUnit = logic.insertUnit(mainframe.getSession(), newUnit);
                        }
                    }
                }

                isnew = false;
                iseditable = false;
                newUnit.setStyled(true);
                unitTree.addUnit(selectedNode, newUnit);
                unitTree.updateNode(selectedNode, parent);
                selectedUnit = newUnit;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                DefaultMutableTreeNode parentNode = ((DefaultMutableTreeNode) selectedNode.getParent());
                if (selectedNode != null) {
                    if (parentNode.isRoot()) {
                        newUnit = logic.updateUnit(mainframe.getSession(), selectedUnit, newUnit);
                    } else {
                        Object parentObj = parentNode.getUserObject();
                        if (parentObj instanceof Unit) {
                            parent = (Unit) parentObj;
                            newUnit = logic.updateUnit(mainframe.getSession(), selectedUnit, newUnit, parent);
                        }
                    }
                }

                newUnit = logic.updateUnit(mainframe.getSession(), selectedUnit, newUnit);
                isedit = false;
                iseditable = false;
                newUnit.setStyled(true);
                unitTree.updateUnit(newUnit);
                unitTree.updateNode(parentNode, parent);
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
        if (unitTree.getRoot().getChildCount() > 0) {
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
        comp.add(comboUrban.getEditor().getEditorComponent());
        comp.add(comboSubUrban.getEditor().getEditorComponent());
        comp.add(fieldStreetName);
        comp.add(fieldRT);
        comp.add(fieldRW);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class UnitTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/Museum.png", new Dimension(16, 16));

        public UnitTree() {
            root = new DefaultMutableTreeNode("Unit/Sub Unit");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData() {
            worker = new LoadUnit(this);
            progressListener = new UnitProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("Unit/Sub Unit");
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

        public void addUnit(DefaultMutableTreeNode parent, Unit unit) {
            if (unit != null) {
                DefaultMutableTreeNode accNode = new DefaultMutableTreeNode(unit);
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

        public void updateUnit(Unit unit) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(unit);
                model.nodeChanged(selNode);
            }
        }

        public void updateNode(DefaultMutableTreeNode node, Unit unit) {
            if (node != null) {
                node.setUserObject(unit);
                model.nodeChanged(node);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    Unit unit = null;
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof Unit) {
                        unit = (Unit) o;
                        if (unit.isGroup()) {
                            return UnitTree.this.GROUP_ICON;
                        } else {
                            return UnitTree.this.CHILD_ICON;
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

    private class LoadUnit extends SwingWorker<DefaultTreeModel, Unit> {

        private UnitTree unitTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeIndex> nodeIndexs = new ArrayList<NodeIndex>();

        public LoadUnit(UnitTree unitTree) {
            this.unitTree = unitTree;
            this.model = (DefaultTreeModel) unitTree.getModel();
            root = new DefaultMutableTreeNode("Unit/Sub Unit");
            model.setRoot(root);
            nodeIndexs = new ArrayList<NodeIndex>();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Unit> chunks) {
            mainframe.stopInActiveListener();
            for (Unit unit : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Unit/Sub Unit = " + unit.toString());

                if (unit.isGroup()) {
                    if (unit.getParentIndex() == 0) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(unit);
                        model.insertNodeInto(child, root, root.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(unit.getIndex(), child);
                        nodeIndexs.add(nodeIndex);

                    } else {
                        NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, unit.getParentIndex());
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(unit);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(unit.getIndex(), child);
                        nodeIndexs.add(nodeIndex);
                    }

                } else {
                    NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, unit.getParentIndex());
                    if (idx != null) {
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(unit);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                    } else {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(unit);
                        model.insertNodeInto(child, root, root.getChildCount());
                    }

                }
                unitTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                ArrayList<Unit> units = logic.getUnit(mainframe.getSession());
                int size = units.size();

                double progress = 0.0;
                if (!units.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        Unit unit = units.get(i);
                        unit.setStyled(true);
                        if (unit.getUrban() != null) {
                            unit.setUrban(logic.getRegion(mainframe.getSession(), unit.getUrban()));
                        }

                        if (unit.getSubUrban() != null) {
                            unit.setSubUrban(logic.getRegion(mainframe.getSession(), unit.getSubUrban()));
                        }

                        publish(unit);
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

    private class UnitProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private UnitProgressListener() {
        }

        UnitProgressListener(JProgressBar progressBar) {
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
