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
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.NodeIndex;
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
public class AccountPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private AccountTree accountTree = new AccountTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXLabel labelSuperTitle = new JXLabel("Kode Rekening (Induk)");
    private JXLabel labelSuperAccount = new JXLabel();
    private JXLabel labelSubAccount = new JXLabel("");
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    private JXComboBox comboCategory = new JXComboBox();
    private JXComboBox comboType = new JXComboBox();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Account selectedAccount = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadAccount worker;
    private AccountProgressListener progressListener;

    public AccountPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        accountTree.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Struktur Rekening");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(new JScrollPane(accountTree), BorderLayout.CENTER);
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
                + "Fasilitas Struktur Rekening untuk pengisian data-data Rekening yang ada di suatu dinas\n\n"
                + "Tambah Rekening\n"
                + "Untuk menambah Rekening klik tombol paling kiri "
                + "kemudian isi data Rekening baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Struktur Rekening\n"
                + "Untuk merubah Rekening klik tombol kedua dari kiri "
                + "kemudian ubah data Rekening, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Rekening\n"
                + "Untuk menghapus Rekening klik tombol paling kanan "
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
        task.setTitle("Tambah / Edit / Hapus Struktur Rekening");
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
        labelSuperAccount.setFont(new Font(labelSuperAccount.getFont().getName(), Font.BOLD, labelSuperAccount.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        CellConstraints cc = new CellConstraints();

        builder.add(labelSuperTitle, cc.xy(1, 1));
        builder.add(labelSuperAccount, cc.xyw(3, 1, 3));

        builder.addLabel("Kode Rekening", cc.xy(1, 3));
        builder.add(labelSubAccount, cc.xy(3, 3));
        builder.add(fieldCode, cc.xy(5, 3));

        builder.addLabel("Uraian", cc.xy(1, 5));
        builder.add(fieldName, cc.xyw(3, 5, 3));

        builder.addLabel("Kategori", cc.xy(1, 7));
        builder.add(comboCategory, cc.xyw(3, 7, 3));

        builder.addLabel("Tipe", cc.xy(1, 9));
        builder.add(comboType, cc.xyw(3, 9, 3));

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
        addTooltip.setTitle("Tambah Struktur Rekening");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Struktur Rekening");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Struktur Rekening");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Struktur Rekening");

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

        loadAccountCategory();
        loadAccountType();

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        accountTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        accountTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = accountTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof Account) {
                        selectedAccount = (Account) obj;
                    } else {
                        selectedAccount = null;
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

    private void setFormState() {
        fieldCode.setEnabled(iseditable);
        fieldName.setEnabled(iseditable);
        comboCategory.setEnabled(iseditable);
        comboType.setEnabled(iseditable);
        accountTree.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldCode.setText("");
        fieldName.setText("");
        comboCategory.setSelectedIndex(0);
        comboType.setSelectedIndex(0);

        DefaultMutableTreeNode node = accountTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                labelSuperAccount.setText("-");
                labelSubAccount.setText("");
            } else {
                if (selectedAccount != null) {
                    labelSuperAccount.setText(selectedAccount.getAccountCode());
                    StringBuilder builder = new StringBuilder();
                    if (selectedAccount.getAccountType() != Account.TYPE_RINCIAN) {
                        builder.append(selectedAccount.getAccountCode()).append(".");
                    } else {
                        builder.append(selectedAccount.getAccountCode());
                    }
                    labelSubAccount.setText(builder.toString());
                }
            }
        } else {
            labelSuperAccount.setText("-");
            labelSubAccount.setText("");
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

    private void loadAccountType() {
        comboType.removeAllItems();
        comboType.setModel(new ListComboBoxModel<String>(Account.accountTypeAsList()));
        AutoCompleteDecorator.decorate(comboType);
    }

    private void loadAccountCategory() {
        comboCategory.removeAllItems();
        comboCategory.setModel(new ListComboBoxModel<String>(Account.accountCategoryAsList()));
        AutoCompleteDecorator.decorate(comboCategory);
    }

    private void setDefaultCategoryAndType() {
        comboCategory.setSelectedItem(Account.ACCOUNT_CATEGORY[Account.CAT_BELANJA]);
        DefaultMutableTreeNode node = accountTree.getSelectedNode();
        if (node != null) {
            if (node.isRoot()) {
                comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_AKUN_UTAMA]);
            } else {
                if (selectedAccount != null) {
                    if (selectedAccount.getAccountType() == Account.TYPE_AKUN_UTAMA) {
                        comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_KELOMPOK]);
                    } else if (selectedAccount.getAccountType() == Account.TYPE_KELOMPOK) {
                        comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_JENIS]);
                    } else if (selectedAccount.getAccountType() == Account.TYPE_JENIS) {
                        comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_OBJEK]);
                    } else if (selectedAccount.getAccountType() == Account.TYPE_OBJEK) {
                        comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_RINCIAN]);
                    }
                } else {
                    comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_AKUN_UTAMA]);
                }
            }


        } else {
            comboType.setSelectedItem(Account.ACCOUNT_TYPE[Account.TYPE_AKUN_UTAMA]);
        }
    }

    private Account getAccount() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldName.getText();
        String code = fieldCode.getText();

        if (code.equals("")) {
            errorString.append("<br>- Kode Rekening</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Uraian</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Account account = new Account();
        account.setAccountCode(labelSubAccount.getText().trim() + code);
        account.setAccountName(name);
        account.setAccountCategory(comboCategory.getSelectedIndex());
        account.setAccountType(comboType.getSelectedIndex());

        if (account.getAccountType() == Account.TYPE_RINCIAN) {
            account.setAccountGroup(false);
        } else {
            account.setAccountGroup(true);
        }

        return account;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Rekening");
        setDefaultCategoryAndType();
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Rekening");
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

                boolean inexp = logic.getAccountInExpedition(mainframe.getSession(), selectedAccount.getAccountCode());

                if (!inexp) {
                    logic.deleteAccount(mainframe.getSession(), selectedAccount.getAccountCode());
                    accountTree.removeSelected();
                    clearForm();
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<html>Kode Rekening ").
                            append("<b>").
                            append(selectedAccount.toString()).
                            append("</b>").
                            append(" tidak boleh dihapus karena sudah dipakai di SPPD");
                    throw new MotekarException(builder.toString());
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
            Account newAccount = getAccount();
            DefaultMutableTreeNode selectedNode = accountTree.getSelectedNode();

            if (isnew) {
                if (selectedNode == null) {
                    newAccount = logic.insertAccount(mainframe.getSession(), newAccount);
                } else {
                    if (selectedNode.isRoot()) {
                        newAccount = logic.insertAccount(mainframe.getSession(), newAccount);
                    } else {
                        Object parentObj = selectedNode.getUserObject();
                        if (parentObj instanceof Account) {
                            Account parent = (Account) parentObj;
                            newAccount = logic.insertAccount(mainframe.getSession(), parent, newAccount);
                        } else {
                            newAccount = logic.insertAccount(mainframe.getSession(), newAccount);
                        }
                    }
                }

                isnew = false;
                iseditable = false;
                accountTree.addAccount(selectedNode, newAccount);
                selectedAccount = newAccount;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newAccount = logic.updateAccount(mainframe.getSession(), selectedAccount, newAccount);
                isedit = false;
                iseditable = false;
                accountTree.updateAccount(newAccount);
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
        if (accountTree.getRoot().getChildCount() > 0) {
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
        comp.add(comboCategory.getEditor().getEditorComponent());
        comp.add(comboType.getEditor().getEditorComponent());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedAccount != null) {
            DefaultMutableTreeNode node = accountTree.getSelectedNode();

            if (!node.isRoot()) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent.isRoot()) {
                    labelSuperAccount.setText("-");
                    labelSubAccount.setText("");
                } else {

                    Object obj = parent.getUserObject();
                    Account account = null;
                    if (obj instanceof Account) {
                        account = (Account) obj;
                        labelSuperAccount.setText(account.getAccountCode());
                        StringBuilder builder = new StringBuilder();
                        if (selectedAccount.getAccountType() != Account.TYPE_RINCIAN) {
                            builder.append(account.getAccountCode()).append(".");
                        } else {
                            builder.append(account.getAccountCode());
                        }
                        labelSubAccount.setText(builder.toString());
                    } else {
                        labelSuperAccount.setText("-");
                        labelSubAccount.setText("");
                    }
                }

                if (selectedAccount.getAccountType() == Account.TYPE_AKUN_UTAMA) {
                    fieldCode.setText(selectedAccount.getAccountCode());
                } else {
                    fieldCode.setText(selectedAccount.getChildCode());
                }

                fieldName.setText(selectedAccount.getAccountName());
                comboCategory.setSelectedIndex(selectedAccount.getAccountCategory());
                comboType.setSelectedIndex(selectedAccount.getAccountType());
                if (selectedAccount.getAccountType() != Account.TYPE_RINCIAN) {
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

    private class AccountTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/Account.png", new Dimension(16, 16));

        public AccountTree() {
            root = new DefaultMutableTreeNode("Struktur Kode Rekening");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData() {
            worker = new LoadAccount(this);
            progressListener = new AccountProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("Struktur Kode Rekening");
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

        public void addAccount(DefaultMutableTreeNode parent, Account account) {
            if (account != null) {
                DefaultMutableTreeNode accNode = new DefaultMutableTreeNode(account);
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

        public void updateAccount(Account account) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(account);
                model.nodeChanged(selNode);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    Account account = null;
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof Account) {
                        account = (Account) o;
                        if (account.isAccountGroup()) {
                            return AccountTree.this.GROUP_ICON;
                        } else {
                            return AccountTree.this.CHILD_ICON;
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

    private class LoadAccount extends SwingWorker<DefaultTreeModel, Account> {

        private AccountTree accountTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeIndex> nodeIndexs = new ArrayList<NodeIndex>();

        public LoadAccount(AccountTree accountTree) {
            this.accountTree = accountTree;
            this.model = (DefaultTreeModel) accountTree.getModel();
            root = new DefaultMutableTreeNode("Struktur Kode Rekening");
            model.setRoot(root);
            nodeIndexs = new ArrayList<NodeIndex>();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Account> chunks) {
            mainframe.stopInActiveListener();
            for (Account account : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kode Rekening = " + account.toString());

                if (account.isAccountGroup()) {
                    if (account.getParentIndex() == 0) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(account);
                        model.insertNodeInto(child, root, root.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(account.getIndex(), child);
                        nodeIndexs.add(nodeIndex);

                    } else {
                        NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, account.getParentIndex());
                        DefaultMutableTreeNode parent = idx.getNode();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(account);
                        model.insertNodeInto(child, parent, parent.getChildCount());
                        NodeIndex nodeIndex = new NodeIndex(account.getIndex(), child);
                        nodeIndexs.add(nodeIndex);
                    }

                } else {
                    NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, account.getParentIndex());
                    DefaultMutableTreeNode parent = idx.getNode();
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(account);
                    model.insertNodeInto(child, parent, parent.getChildCount());
                }
                accountTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                ArrayList<Account> accounts = logic.getAccount(mainframe.getSession());
                int size = accounts.size();

                double progress = 0.0;
                if (!accounts.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        Account acc = accounts.get(i);
                        publish(acc);
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

    private class AccountProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private AccountProgressListener() {
        }

        AccountProgressListener(JProgressBar progressBar) {
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
