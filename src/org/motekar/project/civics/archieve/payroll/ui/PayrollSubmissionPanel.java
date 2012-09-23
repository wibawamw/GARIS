package org.motekar.project.civics.archieve.payroll.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.jgrid.JGrid;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.SKPD;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.payroll.objects.*;
import org.motekar.project.civics.archieve.payroll.sql.PayrollSubmissionBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.viewer.ImageViewerDlg;
import org.motekar.project.civics.archieve.utils.viewer.PicViewerRenderer;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
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
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmissionPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private PayrollSubmissionBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private PayrollSubmissionList payrollSubmissionList = new PayrollSubmissionList();
    private JXComboBox comboEmployee = new JXComboBox();
    private JXComboBox comboSKPD = new JXComboBox();
    private JXDatePicker fieldSubmissionDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JCheckBox checkProccessed = new JCheckBox();
    private JXComboBox comboType = new JXComboBox();
    /**
     *
     */
    private PayrollSubmissionRequirementTable requirementTable = new PayrollSubmissionRequirementTable();
    private PayrollSubmissionFileGrid fileGrid = new PayrollSubmissionFileGrid();
    private JFileChooser openChooser;
    private JFileChooser saveChooser;
    private JSlider slider;
    /**
     *
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private JCommandButton btInsFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private PayrollSubmission selectedPayrollSubmission = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadPayrollSubmission worker;
    private PayrollSubmissionProgressListener progressListener;
    private JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

    public PayrollSubmissionPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new PayrollSubmissionBusinessLogic(this.mainframe.getConnection());
        this.mLogic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        payrollSubmissionList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Pengajuan Gaji");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(payrollSubmissionList), BorderLayout.CENTER);
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
                + "Fasilitas Pengajuan Gaji untuk pengisian data-data pegawai yang ada di suatu dinas\n\n"
                + "Tambah Pengajuan Gaji\n"
                + "Untuk menambah Pengajuan Gaji klik tombol paling kiri "
                + "kemudian isi data Pengajuan Gaji baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Pengajuan Gaji\n"
                + "Untuk merubah Pengajuan Gaji klik tombol kedua dari kiri "
                + "kemudian ubah data Pengajuan Gaji, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Pengajuan Gaji\n"
                + "Untuk menghapus Pengajuan Gaji klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Pengajuan Gaji "
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
        task.setTitle("Tambah / Edit / Hapus Pengajuan Gaji");
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

    protected Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,50px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,"
                + "pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        checkProccessed.addActionListener(this);

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11}});

        CellConstraints cc = new CellConstraints();


        builder.addLabel("Nama Pegawai", cc.xy(1, 1));
        builder.add(comboEmployee, cc.xy(3, 1));

        builder.addLabel("SKPD", cc.xy(1, 3));
        builder.add(comboSKPD, cc.xy(3, 3));

        builder.addLabel("Jenis Pengajuan", cc.xy(1, 5));
        builder.add(comboType, cc.xy(3, 5));

        builder.addLabel("Tanggal Pengajuan", cc.xy(1, 7));
        builder.add(fieldSubmissionDate, cc.xy(3, 7));

        builder.addLabel("Telah Diproses", cc.xy(1, 9));
        builder.add(checkProccessed, cc.xy(3, 9));

        tabPane.addTab("Pengajuan Gaji", builder.getPanel());
        tabPane.addTab("Syarat-syarat", createMainPanel2());
        tabPane.addTab("Lampiran", createMainPanel3());

        return tabPane;
    }

    protected Component createMainPanel2() {
        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow",
                "pref,5px,fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(requirementTable);


        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Syarat-syarat", cc.xyw(1, 1, 3));

        builder.add(scPane, cc.xywh(1, 3, 3, 3));

        return builder.getPanel();
    }

    protected Component createMainPanel3() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,8px,pref,2px,fill:default:grow,5px,10px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

//        lm.setRowGroups(new int[][]{{1, 2, 5}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fileGrid);


        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Lampiran", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(createSliderPanel(), cc.xy(3, 3));
        builder.add(scPane, cc.xywh(1, 5, 3, 2));

        JPanel panel = builder.getPanel();

        fileGrid.setBackground(panel.getBackground());

        return panel;
    }

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah File");

        btInsFile.setActionRichTooltip(addTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus File");

        btDelFile.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsFile);
        buttonStrip.add(btDelFile);


        return buttonStrip;
    }

    private JPanel createSliderPanel() {
        slider = new JSlider(128, 256, fileGrid.getFixedCellDimension());
        slider.setValue(fileGrid.getFixedCellDimension());
        slider.putClientProperty("JComponent.sizeVariant", "small");
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                fileGrid.setFixedCellDimension(slider.getValue());
            }
        });

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new FlowLayout());
        try {
            sliderPanel.add(new JLabel(new ImageIcon(ImageIO.read(ArchieveMainframe.class.getResource(
                    "/resource/dim_small.png")))));
        } catch (IOException e1) {
            Exceptions.printStackTrace(e1);
        }
        sliderPanel.add(slider);
        try {
            sliderPanel.add(new JLabel(new ImageIcon(ImageIO.read(ArchieveMainframe.class.getResource(
                    "/resource/dim_large.png")))));
        } catch (IOException e1) {
            Exceptions.printStackTrace(e1);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(sliderPanel, BorderLayout.EAST);

        return panel;
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
        addTooltip.setTitle("Tambah Pengajuan Gaji");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Pengajuan Gaji");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Pengajuan Gaji");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Pengajuan Gaji");

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

        loadComboEmployee();
        loadComboSKPD();
        loadComboType();
        constructOpenFileChooser();
        constructSaveFileChooser();

        comboEmployee.setEditable(true);
        comboSKPD.setEditable(true);
        comboType.setEditable(true);

        btInsFile.addActionListener(this);
        btDelFile.addActionListener(this);

        fieldSubmissionDate.setFormats("dd/MM/yyyy");

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(requirementTable, ComponentState.DEFAULT);

        requirementTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        requirementTable.setShowGrid(false, false);

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

        fileGrid.addMouseListener(new MouseAdapter() {

            Cursor oldCursor = fileGrid.getCursor();

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = fileGrid.getCellAt(e.getPoint());
                if (index >= 0) {
                    Object o = fileGrid.getModel().getElementAt(index);
                    if (o instanceof PayrollSubmissionFile) {
                        PayrollSubmissionFile file = (PayrollSubmissionFile) o;
                        if (e.getClickCount() == 2) {
                            onViewDocument(file);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fileGrid.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileGrid.setCursor(oldCursor);
            }
        });

        payrollSubmissionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payrollSubmissionList.setAutoCreateRowSorter(true);
        payrollSubmissionList.addListSelectionListener(this);

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

    private void onViewDocument(PayrollSubmissionFile file) {

        String fileExtension = file.getFileName();

        if (fileExtension.toLowerCase().endsWith(".png") || fileExtension.toLowerCase().endsWith(".jpg")
                || fileExtension.toLowerCase().endsWith(".gif") || fileExtension.toLowerCase().endsWith(".bmp")) {

            Cursor oldCursor = fileGrid.getCursor();
            fileGrid.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ImageViewerDlg dlg = new ImageViewerDlg(mainframe, file.getFileStream(), fileExtension, true);
            dlg.showDialog();
            fileGrid.setCursor(oldCursor);

        } else if (fileExtension.toLowerCase().endsWith(".doc") || fileExtension.toLowerCase().endsWith(".docx")
                || fileExtension.toLowerCase().endsWith(".xls") || fileExtension.toLowerCase().endsWith(".xlsx")
                || fileExtension.toLowerCase().endsWith(".pdf")) {
            OutputStream out = null;
            try {
                Cursor oldCursor = fileGrid.getCursor();
                fileGrid.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                File fileToCopy = new File(System.getProperty("user.dir") + "\\temp" + File.separator + fileExtension);
                out = new FileOutputStream(fileToCopy);
                out.write(file.getFileStream());
                out.close();
                String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                Runtime.getRuntime().exec(commands);
                fileGrid.setCursor(oldCursor);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private void loadComboEmployee() {
        comboEmployee.removeAllItems();
        try {
            ArrayList<Employee> employee = mLogic.getEmployee(mainframe.getSession(), false);

            if (!employee.isEmpty()) {
                for (Employee e : employee) {
                    e.setStyled(false);
                }
                employee.add(0, new Employee());
                comboEmployee.setModel(new ListComboBoxModel<Employee>(employee));

                AutoCompleteDecorator.decorate(comboEmployee, new EmployeeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboSKPD() {
        comboSKPD.removeAllItems();
        try {
            ArrayList<SKPD> skpd = mLogic.getSKPD(mainframe.getSession());

            if (!skpd.isEmpty()) {
                for (SKPD s : skpd) {
                    s.setStyled(false);
                }
                skpd.add(0, new SKPD());
                comboSKPD.setModel(new ListComboBoxModel<SKPD>(skpd));

                AutoCompleteDecorator.decorate(comboSKPD, new SKPDConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboType() {
        comboType.removeAllItems();
        try {
            ArrayList<SubmissionType> types = logic.getSubmissionType(mainframe.getSession());

            if (!types.isEmpty()) {
                for (SubmissionType type : types) {
                    type.setStyled(false);
                }
                types.add(0, new SubmissionType());
                comboType.setModel(new ListComboBoxModel<SubmissionType>(types));

                AutoCompleteDecorator.decorate(comboType, new TypeConverter());
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void filter() {
        payrollSubmissionList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void constructOpenFileChooser() {
        if (openChooser == null) {
            openChooser = new JFileChooser();
        }

        openChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg")
                        || fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".bmp")
                        || fileName.toLowerCase().endsWith(".doc") || fileName.toLowerCase().endsWith(".docx")
                        || fileName.toLowerCase().endsWith(".pdf") || fileName.toLowerCase().endsWith(".xls")
                        || fileName.toLowerCase().endsWith(".xlsx"));
            }

            @Override
            public String getDescription() {
                return "Scan Dokumen (*.png;*.jpg;*.gif;*.bmp;*.doc;*.docx;*.pdf;*.xls;*.xlsx)";
            }
        });
    }

    private void constructSaveFileChooser() {
        if (saveChooser == null) {
            saveChooser = new JFileChooser();
        }

        saveChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg")
                        || fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(".bmp")
                        || fileName.toLowerCase().endsWith(".doc") || fileName.toLowerCase().endsWith(".docx")
                        || fileName.toLowerCase().endsWith(".pdf") || fileName.toLowerCase().endsWith(".xls")
                        || fileName.toLowerCase().endsWith(".xlsx"));
            }

            @Override
            public String getDescription() {
                return "Scan Dokumen (*.png;*.jpg;*.gif;*.bmp;*.doc;*.docx;*.pdf;*.xls;*.xlsx)";
            }
        });
    }

    private void setFormState() {
        checkProccessed.setEnabled(iseditable);
        comboEmployee.setEnabled(iseditable);
        comboSKPD.setEnabled(iseditable);
        comboType.setEnabled(iseditable);
        fieldSubmissionDate.setEnabled(iseditable);
        fieldSearch.setEnabled(!iseditable);
        payrollSubmissionList.setEnabled(!iseditable);

        requirementTable.setEnabled(iseditable);

        btInsFile.setEnabled(iseditable);
        btDelFile.setEnabled(iseditable);

    }

    private void clearForm() {
        checkProccessed.setSelected(false);
        comboEmployee.setSelectedItem(null);
        comboSKPD.setSelectedItem(null);
        comboType.setSelectedItem(null);
        fieldSubmissionDate.setDate(null);

        requirementTable.clear();
        fileGrid.clear();

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

    private void createRequirements() {
        try {
            ArrayList<SubmissionRequirement> sRequirements = logic.getSubmissionRequirement(mainframe.getSession());
            ArrayList<PayrollSubmissionRequirement> requirementes = PayrollSubmissionRequirement.createRequirement(sRequirements);
            if (isnew) {
                requirementTable.clear();
                requirementTable.addPayrollSubmissionRequirement(requirementes);
            } else if (isedit) {

                if (selectedPayrollSubmission != null) {
                    if (!selectedPayrollSubmission.getRequirements().isEmpty()) {
                        requirementes = selectedPayrollSubmission.getRequirements();
                    }
                    requirementTable.clear();
                    requirementTable.addPayrollSubmissionRequirement(requirementes);
                }
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
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
        } else if (obj == btInsFile) {
            int retVal = openChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = openChooser.getSelectedFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    byte[] fileStream = new byte[(int) file.length()];
                    fileInputStream.read(fileStream);

                    PayrollSubmissionFile submissionFile = new PayrollSubmissionFile();
                    if (selectedPayrollSubmission != null) {
                        submissionFile.setSubmissionIndex(selectedPayrollSubmission.getIndex());
                    }
                    submissionFile.setFileName(file.getName());
                    submissionFile.setFileStream(fileStream);

                    fileGrid.addPayrollSubmissionFile(submissionFile);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (obj == btDelFile) {
            PayrollSubmissionFile submissionFile = fileGrid.getSelectedPayrollSubmissionFile();
            if (submissionFile != null) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    fileGrid.removeSelected(submissionFile);
                }
            }
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        createRequirements();
        statusLabel.setText("Tambah Pengajuan Gaji");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        statusLabel.setText("Ubah Pengajuan Gaji");
        createRequirements();
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
                logic.deletePayrollSubmission(mainframe.getSession(), selectedPayrollSubmission.getIndex());
                payrollSubmissionList.removeSelected(selectedPayrollSubmission);
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
            PayrollSubmission newPayrollSubmission = getPayrollSubmission();

            if (isnew) {
                newPayrollSubmission = logic.insertPayrollSubmission(mainframe.getSession(), newPayrollSubmission);
                isnew = false;
                iseditable = false;
                payrollSubmissionList.addPayrollSubmission(newPayrollSubmission);
                selectedPayrollSubmission = newPayrollSubmission;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newPayrollSubmission = logic.updatePayrollSubmission(mainframe.getSession(), selectedPayrollSubmission, newPayrollSubmission);
                isedit = false;
                iseditable = false;
                payrollSubmissionList.updatePayrollSubmission(newPayrollSubmission);
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
        if (payrollSubmissionList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private void setFormValues() {
        if (selectedPayrollSubmission != null) {

            if (selectedPayrollSubmission.getEmployee() == null) {
                comboEmployee.setSelectedItem(null);
            } else {
                comboEmployee.setSelectedItem(selectedPayrollSubmission.getEmployee());
            }

            if (selectedPayrollSubmission.getSkpd() == null) {
                comboSKPD.setSelectedItem(null);
            } else {
                comboSKPD.setSelectedItem(selectedPayrollSubmission.getSkpd());
            }

            if (selectedPayrollSubmission.getType() == null) {
                comboType.setSelectedItem(null);
            } else {
                comboType.setSelectedItem(selectedPayrollSubmission.getType());
            }


            fieldSubmissionDate.setDate(selectedPayrollSubmission.getSubmissionDate());
            checkProccessed.setSelected(selectedPayrollSubmission.isProccesed());

            fileGrid.clear();
            fileGrid.addPayrollSubmissionFiles(selectedPayrollSubmission.getFiles());

            requirementTable.clear();
            requirementTable.addPayrollSubmissionRequirement(selectedPayrollSubmission.getRequirements());

            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private PayrollSubmission getPayrollSubmission() throws MotekarException {
        errorString = new StringBuilder();

        Employee employee = null;
        SKPD skpd = null;
        SubmissionType type = null;

        Object objEmp = comboEmployee.getSelectedItem();

        if (objEmp instanceof Employee) {
            employee = (Employee) objEmp;
        }

        Object objSkpd = comboSKPD.getSelectedItem();

        if (objSkpd instanceof SKPD) {
            skpd = (SKPD) objSkpd;
        }

        Object objType = comboType.getSelectedItem();

        if (objType instanceof SubmissionType) {
            type = (SubmissionType) objType;
        }

        Date submissionDate = fieldSubmissionDate.getDate();
        boolean proccesed = checkProccessed.isSelected();

        if (employee == null) {
            errorString.append("<br>- Nama Pegawai</br>");
        }
        if (skpd == null) {
            errorString.append("<br>- Nama SKPD</br>");
        }

        if (type == null) {
            errorString.append("<br>- Jenis Pengajuan</br>");
        }

        if (submissionDate == null) {
            errorString.append("<br>- Tanggal Pengajuan</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ArrayList<PayrollSubmissionRequirement> requirementes = requirementTable.getRequirementess();
        ArrayList<PayrollSubmissionFile> files = fileGrid.getPayrollSubmissionFiles();
//
        PayrollSubmission payrollSubmission = new PayrollSubmission();
        payrollSubmission.setEmployee(employee);
        payrollSubmission.setSkpd(skpd);
        payrollSubmission.setType(type);
        payrollSubmission.setSubmissionDate(submissionDate);
        payrollSubmission.setProccesed(proccesed);

        payrollSubmission.setRequirements(requirementes);
        payrollSubmission.setFiles(files);

        return payrollSubmission;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedPayrollSubmission = payrollSubmissionList.getSelectedPayrollSubmission();
            setFormValues();
        }
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldSubmissionDate.getEditor());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class PayrollSubmissionList extends JXList {

        private Icon SUBMISSION_ICON = Mainframe.getResizableIconFromSource("resource/PayrollSubmission.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public PayrollSubmissionList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadPayrollSubmission((DefaultListModel) getModel());
            progressListener = new PayrollSubmissionProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public PayrollSubmission getSelectedPayrollSubmission() {
            PayrollSubmission payrollSubmission = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof PayrollSubmission) {
                payrollSubmission = (PayrollSubmission) obj;
            }
            return payrollSubmission;
        }

        public void addPayrollSubmission(PayrollSubmission payrollSubmission) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(payrollSubmission);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updatePayrollSubmission(PayrollSubmission payrollSubmission) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(payrollSubmission, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<PayrollSubmission> payrollSubmissions) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(payrollSubmissions);
            filter();
        }

        public void removeSelected(PayrollSubmission payrollSubmission) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(payrollSubmission);
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

                    PayrollSubmission payrollSubmission = null;

                    if (o instanceof PayrollSubmission) {
                        payrollSubmission = (PayrollSubmission) o;
                    }

                    if (payrollSubmission != null) {
                        return PayrollSubmissionList.this.SUBMISSION_ICON;
                    }

                    return PayrollSubmissionList.this.NULL_ICON;
                }
            });
        }
    }

    private class PayrollSubmissionRequirementTable extends JXTable {

        private PayrollSubmissionRequirementTableModel model = new PayrollSubmissionRequirementTableModel();

        public PayrollSubmissionRequirementTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public PayrollSubmissionRequirement getSelectedRequirementes() {
            PayrollSubmissionRequirement requirement = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);

            if (obj instanceof PayrollSubmissionRequirement) {
                requirement = (PayrollSubmissionRequirement) obj;
            }

            return requirement;
        }

        public ArrayList<PayrollSubmissionRequirement> getRequirementess() {
            return model.getRequirementess();
        }

        public ArrayList<PayrollSubmissionRequirement> getSelectedRequirementess() {

            ArrayList<PayrollSubmissionRequirement> requirements = new ArrayList<PayrollSubmissionRequirement>();

            int[] rows = getSelectedRows();

            if (rows != null) {
                for (int i = 0; i < rows.length; i++) {
                    PayrollSubmissionRequirement requirement = null;
                    Object obj = model.getValueAt(rows[i], 0);
                    if (obj instanceof PayrollSubmissionRequirement) {
                        requirement = (PayrollSubmissionRequirement) obj;
                        requirements.add(requirement);
                    }
                }
            }

            return requirements;
        }

        public void updateSelectedRequirementes(PayrollSubmissionRequirement requirement) {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedRequirementes(), requirement);
        }

        public void removeRequirementes(ArrayList<PayrollSubmissionRequirement> requirements) {
            if (!requirements.isEmpty()) {
                for (PayrollSubmissionRequirement requirement : requirements) {
                    model.remove(requirement);
                }
            }

        }

        public void addPayrollSubmissionRequirement(ArrayList<PayrollSubmissionRequirement> requirements) {
            if (!requirements.isEmpty()) {
                for (PayrollSubmissionRequirement requirement : requirements) {
                    model.add(requirement);
                }
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }
    }

    private static class PayrollSubmissionRequirementTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 2;
        private static final String[] columnIds = {"Dokumen", "Terlampir"};
        private ArrayList<PayrollSubmissionRequirement> requirements = new ArrayList<PayrollSubmissionRequirement>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return true;
            }
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<PayrollSubmissionRequirement> requirements) {
            int first = requirements.size();
            int last = first + requirements.size() - 1;
            requirements.addAll(requirements);
            fireTableRowsInserted(first, last);
        }

        public void add(PayrollSubmissionRequirement requirement) {
            if (!requirements.contains(requirement)) {
                insertRow(getRowCount(), requirement);
            }
        }

        public void insertRow(int row, PayrollSubmissionRequirement requirement) {
            requirements.add(row, requirement);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, PayrollSubmissionRequirement oldRequirementes, PayrollSubmissionRequirement newRequirementes) {
            int index = requirements.indexOf(oldRequirementes);
            requirements.set(index, newRequirementes);
            fireTableRowsUpdated(index, index);
        }

        public void remove(PayrollSubmissionRequirement requirement) {
            int row = requirements.indexOf(requirement);
            requirements.remove(requirement);
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
                requirements.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                requirements.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            requirements.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (requirements.get(i) == null) {
                    requirements.set(i, new PayrollSubmissionRequirement());
                }
            }
        }

        public ArrayList<PayrollSubmissionRequirement> getRequirementess() {
            return requirements;
        }

        @Override
        public int getRowCount() {
            return requirements.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public PayrollSubmissionRequirement getRequirementes(int row) {
            if (!requirements.isEmpty()) {
                return requirements.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            PayrollSubmissionRequirement requirement = getRequirementes(row);
            switch (column) {
                case 0:
                    requirement = (PayrollSubmissionRequirement) aValue;
                    break;
                case 1:
                    requirement.setChecked((Boolean) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            PayrollSubmissionRequirement requirement = getRequirementes(row);
            switch (column) {
                case 0:
                    toBeDisplayed = requirement;
                    break;
                case 1:
                    toBeDisplayed = requirement.isChecked();
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class PayrollSubmissionFileGrid extends JGrid {

        public PayrollSubmissionFileGrid() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);

            getCellRendererManager().setDefaultRenderer(new PicViewerRenderer());
            setFixedCellDimension(160);
            setHorizonztalMargin(15);
            setVerticalMargin(15);
        }

        public void clear() {
            DefaultListModel model = (DefaultListModel) getModel();
            model.clear();
        }

        public PayrollSubmissionFile getSelectedPayrollSubmissionFile() {
            PayrollSubmissionFile file = null;
            Object obj = this.getModel().getElementAt(this.getSelectedIndex());
            if (obj instanceof PayrollSubmissionFile) {
                file = (PayrollSubmissionFile) obj;
            }
            return file;
        }

        public ArrayList<PayrollSubmissionFile> getPayrollSubmissionFiles() {
            ArrayList<PayrollSubmissionFile> submissionFiles = new ArrayList<PayrollSubmissionFile>();

            int size = this.getModel().getSize();

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    PayrollSubmissionFile file = null;
                    Object obj = this.getModel().getElementAt(i);
                    if (obj instanceof PayrollSubmissionFile) {
                        file = (PayrollSubmissionFile) obj;
                    }

                    if (file != null) {
                        submissionFiles.add(file);
                    }
                }
            }

            return submissionFiles;
        }

        public void addPayrollSubmissionFile(PayrollSubmissionFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(file);
        }

        public void addPayrollSubmissionFiles(ArrayList<PayrollSubmissionFile> files) {
            DefaultListModel model = (DefaultListModel) getModel();

            if (!files.isEmpty()) {
                for (PayrollSubmissionFile file : files) {
                    model.addElement(file);
                }
            }

        }

        public void updatePayrollSubmissionFile(PayrollSubmissionFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(file, getSelectedIndex());
        }

        public void removeSelected(PayrollSubmissionFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(file);
        }
    }

    private class LoadPayrollSubmission extends SwingWorker<DefaultListModel, PayrollSubmission> {

        private DefaultListModel model;
        private Exception exception;

        public LoadPayrollSubmission(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<PayrollSubmission> chunks) {
            mainframe.stopInActiveListener();
            for (PayrollSubmission payrollSubmission : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Pengajuan Gaji " + payrollSubmission.getEmployee().toString());
                model.addElement(payrollSubmission);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<PayrollSubmission> payrollSubmissions = logic.getPayrollSubmission(mainframe.getSession());

                double progress = 0.0;
                if (!payrollSubmissions.isEmpty()) {
                    for (int i = 0; i < payrollSubmissions.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / payrollSubmissions.size();
                        setProgress((int) progress);

                        PayrollSubmission submission = payrollSubmissions.get(i);

                        if (submission.getEmployee() != null) {
                            submission.setEmployee(mLogic.getEmployeeByIndex(mainframe.getSession(), submission.getEmployee().getIndex()));
                        }

                        if (submission.getSkpd() != null) {
                            submission.setSkpd(mLogic.getSKPDByIndex(mainframe.getSession(), submission.getSkpd()));
                        }

                        if (submission.getType() != null) {
                            submission.setType(logic.getSubmissionType(mainframe.getSession(), submission.getType().getIndex()));
                        }

                        ArrayList<PayrollSubmissionRequirement> requirements = logic.getPayrollSubmissionRequirement(mainframe.getSession(), submission.getIndex());
                        ArrayList<PayrollSubmissionFile> files = logic.getPayrollSubmissionFile(mainframe.getSession(), submission.getIndex());

                        if (!requirements.isEmpty()) {
                            for (PayrollSubmissionRequirement requirement : requirements) {
                                requirement.setRequirement(logic.getSubmissionRequirement(mainframe.getSession(), requirement.getRequirement().getIndex()));
                            }
                        }

                        submission.setRequirements(requirements);
                        submission.setFiles(files);

                        publish(submission);
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
            } catch (InterruptedException ex) {
                //ignore
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
                ErrorInfo info = new ErrorInfo("Kesalahan", ex.getMessage(),
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(PayrollSubmissionPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class PayrollSubmissionProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private PayrollSubmissionProgressListener() {
        }

        PayrollSubmissionProgressListener(JProgressBar progressBar) {
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

    public static class EmployeeConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof Employee)) {
                return new String[0];
            }
            Employee employee = (Employee) item;
            return new String[]{
                        employee.toString(), employee.getName()
                    };
        }

        public String getPreferredStringForItem(Object item) {
            String[] possible = getPossibleStringsForItem(item);
            String preferred = null;
            if (possible != null && possible.length > 0) {
                preferred = possible[0];
            }
            return preferred;
        }
    }

    public static class SKPDConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof SKPD)) {
                return new String[0];
            }
            SKPD skpd = (SKPD) item;
            return new String[]{
                        skpd.toString(), skpd.getName()
                    };
        }

        public String getPreferredStringForItem(Object item) {
            String[] possible = getPossibleStringsForItem(item);
            String preferred = null;
            if (possible != null && possible.length > 0) {
                preferred = possible[0];
            }
            return preferred;
        }
    }

    public static class TypeConverter extends ObjectToStringConverter {

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null) {
                return null;
            }
            if (!(item instanceof SubmissionType)) {
                return new String[0];
            }
            SubmissionType type = (SubmissionType) item;
            return new String[]{
                        type.toString(), type.getTypeName()
                    };
        }

        public String getPreferredStringForItem(Object item) {
            String[] possible = getPossibleStringsForItem(item);
            String preferred = null;
            if (possible != null && possible.length > 0) {
                preferred = possible[0];
            }
            return preferred;
        }
    }
}
