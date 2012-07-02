package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.*;
import org.motekar.project.civics.archieve.master.objects.*;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private BudgetTree bTree = new BudgetTree();
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JYearChooser yearChooser = new JYearChooser();
    private JXComboBox comboBudgetType = new JXComboBox();
    private JXLabel labelProgram = new JXLabel();
    private JXLabel labelAmountProgram = new JXLabel();
    private JXLabel labelProgramActivity = new JXLabel("Program");
    private JXLabel labelProgramActivityName = new JXLabel();
    private JXLabel labelActivity = new JXLabel();
    private JXFormattedTextField fieldAmountActivity;
    /**
     *
     */
    private JYearChooser fYearChooser = new JYearChooser();
    private JXComboBox fComboBudgetType = new JXComboBox();
    /**
     *
     */
    private JPanel cardPanel = new JPanel();
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    //
    private BudgetSubDetailChildTable table = new BudgetSubDetailChildTable();
    private JCommandButton btInsDetail = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btEditDetail = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Edit.png"));
    private JCommandButton btDelDetail = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    //
    private boolean iseditable = false;
    private Budget selectedBudget = null;
    private BudgetDetail selectedDetail = null;
    private BudgetSubDetail selectedSubDetail = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadBudget worker;
    private BudgetDetailProgressListener progressListener;
    private NumberFormat currFormat;

    public BudgetPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        bTree.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar BudgetDetail & Kegiatan");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createFilterPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(bTree), BorderLayout.CENTER);
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
                + "Fasilitas Anggaran untuk pengisian data Anggaran berdasarkan "
                + "Program dan Kegiatan yang ada di suatu dinas\n\n"
                + "Tambah Anggaran\n"
                + "Untuk menambah Anggaran klik tombol paling kiri "
                + "kemudian isi data Anggaran baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Anggaran\n"
                + "Untuk merubah Anggaran klik tombol kedua dari kiri "
                + "kemudian ubah data Anggaran, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Anggaran\n"
                + "Untuk menghapus Anggaran klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Anggaran "
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
        task.setTitle("Tambah / Edit / Hapus Anggaran / Kegiatan");
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

    protected JPanel createFilterPanel() {
        FormLayout lm = new FormLayout(
                "pref,5px,fill:default:grow",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tahun Anggaran ", cc.xy(1, 1));
        builder.add(fYearChooser, cc.xy(3, 1));

        builder.addLabel("Tipe Anggaran ", cc.xy(1, 3));
        builder.add(fComboBudgetType, cc.xy(3, 3));

        return builder.getPanel();
    }

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldAmountActivity = new JXFormattedTextField();
        fieldAmountActivity.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));
    }

    private void createCurrencyFormat() {
        currFormat = NumberFormat.getNumberInstance();
        currFormat.setMinimumFractionDigits(0);
        currFormat.setMaximumFractionDigits(2);
        currFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
    }

    protected JPanel createBudgetPanel() {
        FormLayout lm = new FormLayout(
                "pref,20px,pref,5px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Anggaran", cc.xyw(1, 1, 5));

        builder.addLabel("Tahun Anggaran", cc.xy(1, 3));
        builder.add(yearChooser, cc.xyw(3, 3, 3));

        builder.addLabel("Tipe Anggaran", cc.xy(1, 5));
        builder.add(comboBudgetType, cc.xyw(3, 5, 3));

        return builder.getPanel();
    }

    protected JPanel createBudgetDetailPanel() {
        FormLayout lm = new FormLayout(
                "pref,20px,pref,5px,fill:default:grow,20px",
                "pref,10px,pref,10px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Anggaran Berdasarkan Program", cc.xyw(1, 1, 5));

        builder.addLabel("Program", cc.xy(1, 3));
        builder.add(labelProgram, cc.xyw(3, 3, 3));

        builder.addLabel("Anggaran", cc.xy(1, 5));
        builder.add(labelAmountProgram, cc.xyw(3, 5, 3));

        return builder.getPanel();
    }

    protected JPanel createBudgetSubDetailPanel() {
        FormLayout lm = new FormLayout(
                "pref,20px,pref,5px,fill:default:grow,20px",
                "pref,5px,pref,5px,pref,5px,pref,20px,"
                + "pref,10px,pref,3px,fill:default:grow,20px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        labelProgramActivity.setFont(new Font(labelProgramActivity.getFont().getName(), Font.BOLD, labelProgramActivity.getFont().getSize()));
        labelProgramActivityName.setFont(new Font(labelProgramActivityName.getFont().getName(), Font.BOLD, labelProgramActivityName.getFont().getSize()));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7}});

        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Anggaran Berdasarkan Kegiatan", cc.xyw(1, 1, 5));

        builder.add(labelProgramActivity, cc.xy(1, 3));
        builder.add(labelProgramActivityName, cc.xyw(3, 3, 3));

        builder.addLabel("Kegiatan", cc.xy(1, 5));
        builder.add(labelActivity, cc.xyw(3, 5, 3));

        builder.addLabel("Anggaran", cc.xy(1, 7));
        builder.add(fieldAmountActivity, cc.xyw(3, 7, 3));

        builder.addSeparator("Rincian Kegiatan", cc.xyw(1, 9, 5));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 11));
        builder.add(createPanelTable(), cc.xywh(1, 13, 5, 1));

        return builder.getPanel();
    }

    private JXPanel createPanelTable() {
        JXPanel tablePanel = new JXPanel();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void constructCardPanel() {
        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createBudgetPanel(), "0");
        cardPanel.add(createBudgetDetailPanel(), "1");
        cardPanel.add(createBudgetSubDetailPanel(), "2");
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

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Anggaran / Kegiatan");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Anggaran / Kegiatan");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batalkan Perubahan");

        btCancel.setActionRichTooltip(cancelTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btEdit);
        buttonStrip.add(btSave);
        buttonStrip.add(btCancel);

        return buttonStrip;
    }

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Rincian");

        btInsDetail.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Rincian");

        btEditDetail.setActionRichTooltip(editTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Rincian");

        btDelDetail.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip(JCommandButtonStrip.StripOrientation.HORIZONTAL);
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsDetail);
        buttonStrip.add(btEditDetail);
        buttonStrip.add(btDelDetail);


        return buttonStrip;
    }

    private void construct() {

        loadBudgetType();
        createCurrencyFormat();
        contructNumberField();

        btEdit.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsDetail.addActionListener(this);
        btEditDetail.addActionListener(this);
        btDelDetail.addActionListener(this);



        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(table, ComponentState.DEFAULT);

        table.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkLightColor()));
        table.setShowGrid(false, false);

        bTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        bTree.addPropertyChangeListener("leadSelectionPath", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = bTree.getSelectedObject();
                if (obj != null) {
                    if (obj instanceof Budget) {
                        selectedBudget = (Budget) obj;
                        selectedDetail = null;
                        selectedSubDetail = null;
                    } else if (obj instanceof BudgetDetail) {
                        selectedBudget = null;
                        selectedDetail = (BudgetDetail) obj;
                        selectedSubDetail = null;
                    } else if (obj instanceof BudgetSubDetail) {
                        selectedBudget = null;
                        selectedDetail = null;
                        selectedSubDetail = (BudgetSubDetail) obj;
                    } else {
                        selectedBudget = null;
                        selectedDetail = null;
                        selectedSubDetail = null;
                    }
                    setFormValues();
                }
            }
        });

        fYearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                bTree.loadData();
            }
        });

        fComboBudgetType.addActionListener(this);

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
        setButtonState("disableAll");
    }

    private void loadBudgetType() {

        ArrayList<String> budgetTypes = new ArrayList<String>();

        budgetTypes.add(0, " ");
        budgetTypes.add("APBD");
        budgetTypes.add("APBD Perubahan");

        comboBudgetType.setModel(new ListComboBoxModel<String>(budgetTypes));
        AutoCompleteDecorator.decorate(comboBudgetType);

        fComboBudgetType.setModel(new ListComboBoxModel<String>(budgetTypes));
        AutoCompleteDecorator.decorate(fComboBudgetType);

        fComboBudgetType.setSelectedIndex(1);
    }

    private void setFormState() {
        yearChooser.setEnabled(false);
        comboBudgetType.setEnabled(false);
        fieldAmountActivity.setEnabled(iseditable);
        bTree.setEnabled(!iseditable);
        table.setEditable(iseditable);
    }

    private void clearForm() {
        fieldAmountActivity.setValue(BigDecimal.ZERO);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        yearChooser.setYear(cal.get(Calendar.YEAR));
        comboBudgetType.setSelectedIndex(1);
        labelActivity.setText("");
        labelAmountProgram.setText("");
        labelProgram.setText("");
        labelProgramActivityName.setText("");
    }

    private void setButtonState(String state) {
        if (state.equals("New")) {
            btEdit.setEnabled(false);
            btSave.setEnabled(true);
            btCancel.setEnabled(true);
            btInsDetail.setEnabled(true);
            btEditDetail.setEnabled(true);
            btDelDetail.setEnabled(true);
        } else if (state.equals("Save")) {
            btEdit.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            btInsDetail.setEnabled(false);
            btEditDetail.setEnabled(false);
            btDelDetail.setEnabled(false);
        } else if (state.equals("disableAdd")) {
            btEdit.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            btInsDetail.setEnabled(true);
            btEditDetail.setEnabled(true);
            btDelDetail.setEnabled(true);
        } else if (state.equals("disableAll")) {
            btEdit.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            btInsDetail.setEnabled(false);
            btEditDetail.setEnabled(false);
            btDelDetail.setEnabled(false);
        } else {
            btEdit.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
            btInsDetail.setEnabled(false);
            btEditDetail.setEnabled(false);
            btDelDetail.setEnabled(false);
        }
    }

    private BudgetSubDetail getBudgetSubDetail() throws MotekarException {
        errorString = new StringBuilder();

        BigDecimal amount = BigDecimal.ZERO;

        Object obj = fieldAmountActivity.getValue();

        if (obj instanceof BigDecimal) {
            amount = (BigDecimal) obj;
        } else if (obj instanceof Long) {
            amount = BigDecimal.valueOf(((Long) obj).longValue());
        } else if (obj instanceof Integer) {
            amount = BigDecimal.valueOf(((Integer) obj).longValue());
        }

        if (amount.equals(BigDecimal.ZERO)) {
            errorString.append("<br>- Anggaran</br>");
        }

        ArrayList<BudgetSubDetailChild> subDetailChilds = table.getBudgetSubDetailChilds();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        BudgetSubDetail subDetail = new BudgetSubDetail(selectedSubDetail);
        subDetail.setAmount(amount);
        subDetail.setSubDetailChilds(subDetailChilds);
        return subDetail;
    }

    private void onEdit() {
        iseditable = true;
        setFormState();
        setButtonState("New");
        statusLabel.setText("Ubah Anggaran");
    }

    private void onSave() {
        try {
            BudgetSubDetail newSubDetail = getBudgetSubDetail();
            iseditable = false;

            newSubDetail = logic.updateBudgetSubDetail(mainframe.getSession(), selectedSubDetail, newSubDetail);
            bTree.updateBudgetSubDetail(newSubDetail);
            selectedSubDetail = newSubDetail;
            selectedDetail = null;
            selectedBudget = null;
            setButtonState("Save");

            setFormState();
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
        setFormState();
        if (bTree.getRoot().getChildCount() > 0) {
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
        if (obj == btEdit) {
            onEdit();
        } else if (obj == btSave) {
            onSave();
        } else if (obj == btCancel) {
            onCancel();
        } else if (obj == fComboBudgetType) {
            bTree.loadData();
        } else if (obj == btInsDetail) {
            BudgetSubDetailChildDlg dlg = new BudgetSubDetailChildDlg(mainframe);
            dlg.showDialog();

            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                BudgetSubDetailChild subDetailChild = dlg.getSubDetailChild();
                table.addBudgetSubDetailChild(subDetailChild);

                fieldAmountActivity.setValue(table.getTotalAmount());
            }

        } else if (obj == btEditDetail) {
            BudgetSubDetailChild child = table.getSelectedBudgetSubDetailChild();

            if (child != null) {
                BudgetSubDetailChildDlg dlg = new BudgetSubDetailChildDlg(mainframe, child);
                dlg.showDialog();

                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    BudgetSubDetailChild subDetailChild = dlg.getSubDetailChild();
                    table.updateSelectedBudgetSubDetailChild(subDetailChild);

                    fieldAmountActivity.setValue(table.getTotalAmount());
                }
            }

        } else if (obj == btDelDetail) {
            ArrayList<BudgetSubDetailChild> childs = table.getSelectedBudgetSubDetailChilds();

            if (!childs.isEmpty()) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    table.removeBudgetSubDetailChild(childs);
                    fieldAmountActivity.setValue(table.getTotalAmount());
                }
            }
        }
    }

    private void setFormValues() {
        if (selectedSubDetail != null) {
            DefaultMutableTreeNode node = bTree.getSelectedNode();
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

            Object obj = parent.getUserObject();
            Program program = null;

            if (obj instanceof BudgetDetail) {
                BudgetDetail bd = (BudgetDetail) obj;
                program = bd.getProgram();
                labelProgramActivityName.setText(program.toString());
            } else {
                labelProgramActivityName.setText("-");
            }

            labelActivity.setText(selectedSubDetail.toString());
            fieldAmountActivity.setValue(selectedSubDetail.getAmount());

            table.clear();
            table.addBudgetSubDetailChild(selectedSubDetail.getSubDetailChilds());

            setButtonState("Save");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "2");
        } else if (selectedDetail != null) {

            labelProgram.setText(selectedDetail.toString());

            DefaultMutableTreeNode node = bTree.getSelectedNode();

            int childcount = node.getChildCount();

            BigDecimal amount = BigDecimal.ZERO;

            if (childcount > 0) {
                for (int i = 0; i < childcount; i++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                    if (child != null) {
                        Object object = child.getUserObject();
                        if (object instanceof BudgetSubDetail) {
                            BudgetSubDetail subDetail = (BudgetSubDetail) object;
                            amount = amount.add(subDetail.getAmount());
                        }
                    }
                }
            }

            labelAmountProgram.setText(currFormat.format(amount));

            setButtonState("disableAll");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "1");
        } else if (selectedBudget != null) {
            yearChooser.setYear(selectedBudget.getYears());
            comboBudgetType.setSelectedIndex(selectedBudget.getBudgetType());
            setButtonState("disableAll");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "0");
        } else {
            clearForm();
            setButtonState("");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "0");
        }
    }

    private class BudgetTree extends JXTree {

        private DefaultTreeModel model;
        private DefaultMutableTreeNode root = null;
        private Icon GROUP_ICON = Mainframe.getResizableIconFromSource("resource/folder.png", new Dimension(16, 16));
        private Icon CHILD_ICON = Mainframe.getResizableIconFromSource("resource/activity.png", new Dimension(16, 16));

        public BudgetTree() {
            root = new DefaultMutableTreeNode("BudgetDetail / Kegiatan");
            model = new DefaultTreeModel(root);
            setModel(model);
            this.setShowsRootHandles(true);
        }

        public void loadData() {
            clearForm();
            setButtonState("");
            ((CardLayout) cardPanel.getLayout()).show(cardPanel, "0");

            worker = new LoadBudget(this);
            progressListener = new BudgetDetailProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            root = new DefaultMutableTreeNode("BudgetDetail / Kegiatan");
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

        public void addBudgetDetail(DefaultMutableTreeNode parent, BudgetDetail detail) {
            if (detail != null) {
                DefaultMutableTreeNode detailNode = new DefaultMutableTreeNode(detail);
                model.insertNodeInto(detailNode, parent, parent.getChildCount());
                expandAll();
                goToPath();
            }
        }

        public void addBudgetSubDetail(DefaultMutableTreeNode parent, BudgetSubDetail subDetail) {
            if (subDetail != null) {
                DefaultMutableTreeNode subDetailNode = new DefaultMutableTreeNode(subDetail);
                model.insertNodeInto(subDetailNode, parent, parent.getChildCount());
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

        public void updateBudgetDetail(BudgetDetail detail) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(detail);
                model.nodeChanged(selNode);
            }
        }

        public void updateBudgetSubDetail(BudgetSubDetail subDetail) {
            DefaultMutableTreeNode selNode = getSelectedNode();
            if (selNode != null) {
                selNode.setUserObject(subDetail);
                model.nodeChanged(selNode);
            }
        }

        @Override
        public TreeCellRenderer getCellRenderer() {
            return new DefaultTreeRenderer(new IconValue() {

                public Icon getIcon(Object o) {
                    if (o instanceof String) {
                        return null;
                    } else if (o instanceof BudgetDetail) {
                        return BudgetTree.this.GROUP_ICON;
                    } else if (o instanceof BudgetSubDetail) {
                        return BudgetTree.this.CHILD_ICON;
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

    private class LoadBudget extends SwingWorker<DefaultTreeModel, Object> {

        private BudgetTree budgetTree;
        private DefaultTreeModel model;
        private DefaultMutableTreeNode root;
        private Exception exception;
        private ArrayList<NodeIndex> nodeIndexs = new ArrayList<NodeIndex>();

        public LoadBudget(BudgetTree budgetTree) {
            this.budgetTree = budgetTree;
            this.model = (DefaultTreeModel) budgetTree.getModel();
            root = new DefaultMutableTreeNode("Anggaran");
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

                if (object instanceof Budget) {
                    Budget budget = (Budget) object;
                    root = new DefaultMutableTreeNode(budget);
                    model.setRoot(root);
                    statusLabel.setText("Memuat " + budget.toString());

                } else if (object instanceof BudgetDetail) {
                    BudgetDetail detail = (BudgetDetail) object;

                    statusLabel.setText("Memuat Anggaran = " + detail.toString());

                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(object);
                    model.insertNodeInto(child, root, root.getChildCount());
                    NodeIndex nodeIndex = new NodeIndex(detail.getIndex(), child);
                    nodeIndexs.add(nodeIndex);



                } else if (object instanceof BudgetSubDetail) {
                    BudgetSubDetail subDetail = (BudgetSubDetail) object;

                    statusLabel.setText("Memuat Anggaran = " + subDetail.toString());

                    NodeIndex idx = NodeIndex.searchArrays(nodeIndexs, subDetail.getParentIndex());
                    DefaultMutableTreeNode parent = idx.getNode();
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(object);
                    model.insertNodeInto(child, parent, parent.getChildCount());
                }
                budgetTree.expandAll();
            }
        }

        @Override
        protected DefaultTreeModel doInBackground() throws Exception {
            try {
                Budget budget = logic.getBudget(mainframe.getSession(), fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());

                if (budget == null) {
                    budget = new Budget();
                    budget.setYears(fYearChooser.getYear());
                    budget.setBudgetType(fComboBudgetType.getSelectedIndex());

                    budget = logic.insertBudget(mainframe.getSession(), budget);
                }

                ArrayList<BudgetDetail> details = logic.getBudgetDetail(mainframe.getSession(),
                        fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());


                ArrayList<BudgetSubDetail> subDetails = logic.getBudgetSubDetail(mainframe.getSession(),
                        fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());

                if (details.isEmpty()) {
                    ArrayList<Program> programs = logic.getProgram(mainframe.getSession());

                    if (!programs.isEmpty()) {
                        details = new ArrayList<BudgetDetail>();
                        subDetails = new ArrayList<BudgetSubDetail>();
                        for (Program program : programs) {
                            BudgetDetail detail = new BudgetDetail();
                            detail.setParentIndex(budget.getIndex());
                            detail.setProgram(program);
                            detail = logic.insertBudgetDetail(mainframe.getSession(), detail);
                            if (subDetails.isEmpty()) {
                                ArrayList<Activity> activitys = logic.getActivity(mainframe.getSession(), program.getIndex());

                                if (!activitys.isEmpty()) {
                                    for (Activity activity : activitys) {
                                        BudgetSubDetail subDetail = new BudgetSubDetail();
                                        subDetail.setParentIndex(detail.getIndex());
                                        subDetail.setActivity(activity);
                                        subDetail = logic.insertBudgetSubDetail(mainframe.getSession(), subDetail);
                                        subDetails.add(subDetail);
                                    }
                                }


                            } else {
                                ArrayList<Activity> activitys = logic.getActivityNotInBudgetSubDetail(mainframe.getSession(),
                                        fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());

                                if (!activitys.isEmpty()) {
                                    for (Activity activity : activitys) {
                                        BudgetSubDetail subDetail = new BudgetSubDetail();
                                        subDetail.setParentIndex(detail.getIndex());
                                        subDetail.setActivity(activity);
                                        subDetail = logic.insertBudgetSubDetail(mainframe.getSession(), subDetail);
                                        subDetails.add(subDetail);
                                    }
                                }
                            }
                            details.add(detail);
                        }
                    }

                } else {
                    ArrayList<Program> programs = logic.getProgramNotInBudgetDetail(mainframe.getSession(), fYearChooser.getYear(),
                            fComboBudgetType.getSelectedIndex());

                    if (!programs.isEmpty()) {
                        for (Program program : programs) {
                            BudgetDetail detail = new BudgetDetail();
                            detail.setParentIndex(budget.getIndex());
                            detail.setProgram(program);
                            detail = logic.insertBudgetDetail(mainframe.getSession(), detail);
                            if (subDetails.isEmpty()) {
                                ArrayList<Activity> activitys = logic.getActivity(mainframe.getSession(), program.getIndex());

                                if (!activitys.isEmpty()) {
                                    for (Activity activity : activitys) {
                                        BudgetSubDetail subDetail = new BudgetSubDetail();
                                        subDetail.setParentIndex(detail.getIndex());
                                        subDetail.setActivity(activity);
                                        subDetail = logic.insertBudgetSubDetail(mainframe.getSession(), subDetail);
                                        subDetails.add(subDetail);
                                    }
                                }


                            } else {
                                ArrayList<Activity> activitys = logic.getActivityNotInBudgetSubDetail(mainframe.getSession(),
                                        fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());

                                if (!activitys.isEmpty()) {
                                    for (Activity activity : activitys) {
                                        BudgetSubDetail subDetail = new BudgetSubDetail();
                                        subDetail.setParentIndex(detail.getIndex());
                                        subDetail.setActivity(activity);
                                        subDetail = logic.insertBudgetSubDetail(mainframe.getSession(), subDetail);
                                        subDetails.add(subDetail);
                                    }
                                }
                            }
                            details.add(detail);
                        }
                    } else {
                        for (BudgetDetail detail : details) {
                            ArrayList<Activity> activitys = logic.getActivityNotInBudgetSubDetail(mainframe.getSession(),
                                    fYearChooser.getYear(), fComboBudgetType.getSelectedIndex());

                            if (!activitys.isEmpty()) {
                                for (Activity activity : activitys) {
                                    BudgetSubDetail subDetail = new BudgetSubDetail();
                                    subDetail.setParentIndex(detail.getIndex());
                                    subDetail.setActivity(activity);
                                    subDetail = logic.insertBudgetSubDetail(mainframe.getSession(), subDetail);
                                    subDetails.add(subDetail);
                                }
                            }
                        }
                    }
                }

                publish(budget);

                int size = details.size();

                double progress = 0.0;
                if (!details.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        BudgetDetail detail = details.get(i);
                        publish(detail);
                        setProgress((int) progress);
                        Thread.sleep(100L);
                    }
                }

                setProgress(0);
                size = subDetails.size();

                if (!subDetails.isEmpty()) {
                    for (int i = 0; i < size && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / size;
                        BudgetSubDetail subDetail = subDetails.get(i);

                        subDetail.setSubDetailChilds(logic.getBudgetSubDetailChild(mainframe.getSession(), subDetail));

                        publish(subDetail);
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
            try {
                if (isCancelled()) {
                    return;
                }
                get();
                setProgress(100);
                mainframe.startInActiveListener();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }

        }
    }

    private class BudgetDetailProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private BudgetDetailProgressListener() {
        }

        BudgetDetailProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
            this.progressBar.setVisible(true);
            this.progressBar.setIndeterminate(true);
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

    private class BudgetSubDetailChildTable extends JXTable {

        private BudgetSubDetailChildTableModel model;

        public BudgetSubDetailChildTable() {
            model = new BudgetSubDetailChildTableModel();
            setModel(model);
            getTableHeader().setReorderingAllowed(false);

            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);
        }

        public void clear() {
            model.clear();
        }

        public BudgetSubDetailChild getSelectedBudgetSubDetailChild() {
            ArrayList<BudgetSubDetailChild> selectedBudgetSubDetailChilds = getSelectedBudgetSubDetailChilds();
            return selectedBudgetSubDetailChilds.get(0);
        }

        public ArrayList<BudgetSubDetailChild> getBudgetSubDetailChilds() {
            return model.getBudgetSubDetailChilds();
        }

        public ArrayList<BudgetSubDetailChild> getSelectedBudgetSubDetailChilds() {

            ArrayList<BudgetSubDetailChild> childs = new ArrayList<BudgetSubDetailChild>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                BudgetSubDetailChild child = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        Object obj = model.getValueAt(i, 1);
                        if (obj instanceof BudgetSubDetailChild) {
                            child = (BudgetSubDetailChild) obj;
                            childs.add(child);
                        }
                    }
                }
            }

            return childs;
        }

        public void updateSelectedBudgetSubDetailChild(BudgetSubDetailChild child) {
            model.updateRow(getSelectedBudgetSubDetailChild(), child);
        }

        public void removeBudgetSubDetailChild(ArrayList<BudgetSubDetailChild> childs) {
            if (!childs.isEmpty()) {
                for (BudgetSubDetailChild child : childs) {
                    model.remove(child);
                }
            }

        }

        public void addBudgetSubDetailChild(ArrayList<BudgetSubDetailChild> childs) {
            if (!childs.isEmpty()) {
                for (BudgetSubDetailChild child : childs) {
                    model.add(child);
                }
            }
        }

        public void addBudgetSubDetailChild(BudgetSubDetailChild child) {
            model.add(child);
        }

        public void insertEmptyBudgetSubDetailChild() {
            addBudgetSubDetailChild(new BudgetSubDetailChild());
        }

        public BigDecimal getTotalAmount() {
            BigDecimal total = BigDecimal.ZERO;

            ArrayList<BudgetSubDetailChild> childs = model.getBudgetSubDetailChilds();

            if (!childs.isEmpty()) {
                for (BudgetSubDetailChild child : childs) {
                    BigDecimal tt = BigDecimal.ZERO;
                    tt = tt.add(child.getAmount());
                    tt = tt.multiply(BigDecimal.valueOf(child.getCounted()));

                    total = total.add(tt);
                }
            }

            return total;
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Boolean.class)) {
                return new DefaultTableRenderer(new CheckBoxProvider());
            } else if (columnClass.equals(BigDecimal.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.RIGHT);
            } else if (columnClass.equals(Integer.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(0);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
                amountDisplayFormat.setGroupingUsed(false);

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            } else if (columnClass.equals(JLabel.class)) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class BudgetSubDetailChildTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 7;
        private static final String[] COLUMN = {"", "Uraian", "Eselon", "Jumlah", "Satuan", "Sebesar (Rp.)", "Total"};
        private ArrayList<BudgetSubDetailChild> childs = new ArrayList<BudgetSubDetailChild>();

        public BudgetSubDetailChildTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return true;
            }
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN[column];
        }

        public void add(ArrayList<BudgetSubDetailChild> clins) {
            int first = clins.size();
            int last = first + clins.size() - 1;
            childs.addAll(clins);
            fireTableRowsInserted(first, last);
        }

        public void add(BudgetSubDetailChild child) {
            insertRow(getRowCount(), child);
        }

        public void insertRow(int row, BudgetSubDetailChild child) {
            childs.add(row, child);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(BudgetSubDetailChild oldBudgetSubDetailChild, BudgetSubDetailChild newBudgetSubDetailChild) {
            int index = childs.indexOf(oldBudgetSubDetailChild);
            childs.set(index, newBudgetSubDetailChild);
            fireTableRowsUpdated(index, index);
        }

        public void remove(BudgetSubDetailChild child) {
            int row = childs.indexOf(child);
            childs.remove(child);
            fireTableRowsDeleted(row, row);
        }

        public void clear() {
            setRowCount(0);
        }

        protected void setRowCount(int rowCount) {
            int old = getRowCount();
            if (old == rowCount) {
                return;
            }

            if (rowCount <= old) {
                childs.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                childs.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            childs.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (childs.get(i) == null) {
                    childs.set(i, new BudgetSubDetailChild());
                }
            }
        }

        public ArrayList<BudgetSubDetailChild> getBudgetSubDetailChilds() {
            return childs;
        }

        @Override
        public int getRowCount() {
            return childs.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else if (columnIndex == 4) {
                return JLabel.class;
            } else if (columnIndex == 3) {
                return Integer.class;
            } else if (columnIndex == 5 || columnIndex == 6) {
                return BigDecimal.class;
            }
            return super.getColumnClass(columnIndex);
        }

        public BudgetSubDetailChild getBudgetSubDetailChild(int row) {
            if (!childs.isEmpty()) {
                return childs.get(row);
            }
            return null;
        }

        public void setBudgetSubDetailChild(int row, BudgetSubDetailChild child) {
            if (!childs.isEmpty()) {
                childs.set(row, child);
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            BudgetSubDetailChild child = getBudgetSubDetailChild(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        child.setSelected((Boolean) aValue);
                    }

                    break;

                case 1:
                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            BudgetSubDetailChild child = getBudgetSubDetailChild(row);
            switch (column) {
                case 0:
                    toBeDisplayed = child.isSelected();
                    break;
                case 1:
                    toBeDisplayed = child;
                    break;
                case 2:
                    toBeDisplayed = child.getEselonAsString();
                    break;
                case 3:
                    toBeDisplayed = child.getCounted();
                    break;
                case 4:
                    toBeDisplayed = child.getUnits();
                    break;
                case 5:
                    toBeDisplayed = child.getAmount();
                    break;
                case 6:

                    BigDecimal total = BigDecimal.ZERO;
                    total = total.add(child.getAmount());
                    total = total.multiply(BigDecimal.valueOf(child.getCounted()));

                    toBeDisplayed = total;
                    break;
            }
            return toBeDisplayed;
        }
    }
}
