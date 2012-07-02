package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import de.jgrid.JGrid;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.*;
import org.motekar.project.civics.archieve.mail.objects.SP2DFile;
import org.motekar.project.civics.archieve.mail.objects.SP2D;
import org.motekar.project.civics.archieve.mail.objects.SP2DFile;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.SKPD;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
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

/**
 *
 * @author Muhamad Wibawa
 */
public class SP2DPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private SP2DList sp2dList = new SP2DList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXComboBox comboSKPD = new JXComboBox();
    private JXTextField fieldSP2DNumber = new JXTextField();
    private JXDatePicker fieldSP2DDate = new JXDatePicker();
    private JXTextField fieldReceiver = new JXTextField();
    private JXTextArea fieldPurpose = new JXTextArea();
    private JXFormattedTextField fieldAmount;
    private JXTextArea fieldDescription = new JXTextArea();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private SP2DFileFileGrid sp2dfList = new SP2DFileFileGrid();
    private JSlider slider;
    /**
     *
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private JCommandButton btInsFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    /**
     *
     */
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private SP2D selectedSP2D = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadSP2D worker;
    private SP2DProgressListener progressListener;
    private JFileChooser openChooser;
    private JFileChooser saveChooser;

    public SP2DPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        sp2dList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar SP2D");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(sp2dList), BorderLayout.CENTER);
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
                + "Fasilitas SP2D untuk pengisian surat perintah pencairan dana\n\n"
                + "Tambah SP2D\n"
                + "Untuk menambah SP2D klik tombol paling kiri "
                + "kemudian isi data SP2D baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit SP2D\n"
                + "Untuk merubah SP2D klik tombol kedua dari kiri "
                + "kemudian ubah data SP2D, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus SP2D\n"
                + "Untuk menghapus SP2D klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus SP2D "
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
        task.setTitle("Tambah / Edit / Hapus SP2D");
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
                "right:pref,10px,fill:default:grow,30px",
                "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,fill:default:grow,5px,pref,5px,pref,fill:default:grow,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

//        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 12}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldPurpose);

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("SKPD", cc.xy(1, 1));
        builder.add(comboSKPD, cc.xy(3, 1));

        builder.addLabel("Nomor SP2D", cc.xy(1, 3));
        builder.add(fieldSP2DNumber, cc.xy(3, 3));

        builder.addLabel("Tanggal Pencairan", cc.xy(1, 5));
        builder.add(fieldSP2DDate, cc.xy(3, 5));

        builder.addLabel("Penerima Dana", cc.xy(1, 7));
        builder.add(fieldReceiver, cc.xy(3, 7));

        builder.addLabel("Untuk Keperluan", cc.xy(1, 9));
        builder.add(scPane, cc.xywh(3, 9, 1, 2));

        builder.addLabel("Jumlah Yang Dibayarkan", cc.xy(1, 12));
        builder.add(fieldAmount, cc.xy(3, 12));

        builder.addLabel("Keterangan Lainnya", cc.xy(1, 14));
        builder.add(scPane2, cc.xywh(3, 14, 1, 2));

        tabbedPane.addTab("Input Data", builder.getPanel());
        tabbedPane.addTab("Lampiran SP2D", createMainPanelPage2());

        return tabbedPane;
    }

    private Component createMainPanelPage2() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,8px,pref,2px,fill:default:grow,5px,10px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

//        lm.setRowGroups(new int[][]{{1, 2, 5}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(sp2dfList);


        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Scan Dokumen Surat", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(createSliderPanel(), cc.xy(3, 3));
        builder.add(scPane, cc.xywh(1, 5, 3, 2));

        JPanel panel = builder.getPanel();

        sp2dfList.setBackground(panel.getBackground());

        return panel;
    }
    
    private JPanel createSliderPanel() {
        slider = new JSlider(128, 256, sp2dfList.getFixedCellDimension());
        slider.setValue(sp2dfList.getFixedCellDimension());
        slider.putClientProperty("JComponent.sizeVariant", "small");
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                sp2dfList.setFixedCellDimension(slider.getValue());
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
        addTooltip.setTitle("Tambah SP2D");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah SP2D");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan SP2D");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus SP2D");

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

    private void construct() {

        contructNumberField();
        loadComboSKPD();
        constructOpenFileChooser();
        constructSaveFileChooser();

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

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                sp2dList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                sp2dList.loadData();
            }
        });

        comboSKPD.setEditable(true);

        fieldPurpose.setWrapStyleWord(true);
        fieldPurpose.setLineWrap(true);

        fieldDescription.setWrapStyleWord(true);
        fieldDescription.setLineWrap(true);

        fieldSP2DDate.setFormats("dd/MM/yyyy");

        sp2dfList.addMouseListener(new MouseAdapter() {

            Cursor oldCursor = sp2dfList.getCursor();

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = sp2dfList.getCellAt(e.getPoint());
                if (index >= 0) {
                    Object o = sp2dfList.getModel().getElementAt(index);
                    if (o instanceof SP2DFile) {
                        SP2DFile file = (SP2DFile) o;
                        if (e.getClickCount() == 2) {
                            onViewDocument(file);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sp2dfList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sp2dfList.setCursor(oldCursor);
            }
        });

        sp2dList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sp2dList.setAutoCreateRowSorter(true);
        sp2dList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        btInsFile.addActionListener(this);
        btDelFile.addActionListener(this);

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
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldAmount = new JXFormattedTextField();
        fieldAmount.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));
    }

    public void filter() {
        sp2dList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void loadComboSKPD() {
        try {
            ArrayList<SKPD> skpds = mLogic.getSKPD(mainframe.getSession());

            if (!skpds.isEmpty()) {
                skpds.add(0, new SKPD());
                comboSKPD.setModel(new ListComboBoxModel<SKPD>(skpds));
                AutoCompleteDecorator.decorate(comboSKPD);
            }

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
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

        comboSKPD.setEnabled(iseditable);
        fieldSP2DNumber.setEnabled(iseditable);
        fieldSP2DDate.setEnabled(iseditable);
        fieldPurpose.setEnabled(iseditable);
        fieldReceiver.setEnabled(iseditable);
        fieldAmount.setEnabled(iseditable);
        fieldDescription.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        sp2dList.setEnabled(!iseditable);

        checkBox.setEnabled(!iseditable);
        monthChooser.setEnabled(!iseditable && checkBox.isSelected());
        yearChooser.setEnabled(!iseditable && checkBox.isSelected());

        btInsFile.setEnabled(iseditable);
        btDelFile.setEnabled(iseditable);
    }

    private void clearForm() {

        comboSKPD.getEditor().setItem(null);
        fieldSP2DNumber.setText("");
        fieldSP2DDate.setDate(null);
        fieldPurpose.setText("");
        fieldReceiver.setText("");
        fieldAmount.setValue(BigDecimal.ZERO);
        fieldDescription.setText("");

        if (comboSKPD.isEnabled()) {
            comboSKPD.getEditor().getEditorComponent().requestFocus();
        }

        sp2dfList.clear();

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
            sp2dList.loadData();
        } else if (obj == btInsFile) {
            int retVal = openChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = openChooser.getSelectedFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    byte[] fileStream = new byte[(int) file.length()];
                    fileInputStream.read(fileStream);

                    SP2DFile sp2dFile = new SP2DFile();
                    if (selectedSP2D != null) {
                        sp2dFile.setSp2dIndex(selectedSP2D.getIndex());
                    }
                    sp2dFile.setFileName(file.getName());
                    sp2dFile.setFileStream(fileStream);

                    sp2dfList.addSP2DFile(sp2dFile);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (obj == btDelFile) {
            SP2DFile outboxFile = sp2dfList.getSelectedSP2DFile();
            if (outboxFile != null) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    sp2dfList.removeSelected(outboxFile);
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
        statusLabel.setText("Tambah SP2D");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldSP2DDate.requestFocus();
        statusLabel.setText("Ubah SP2D");
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
                logic.deleteSP2D(mainframe.getSession(), selectedSP2D);
                sp2dList.removeSelected(selectedSP2D);
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
            SP2D newSP2D = getSP2D();

            if (isnew) {
                newSP2D = logic.insertSP2D(mainframe.getSession(), newSP2D);
                isnew = false;
                iseditable = false;
                sp2dList.addSP2D(newSP2D);
                selectedSP2D = newSP2D;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newSP2D = logic.updateSP2D(mainframe.getSession(), selectedSP2D, newSP2D);
                isedit = false;
                iseditable = false;
                sp2dList.updateSP2D(newSP2D);
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
        if (sp2dList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private SP2D getSP2D() throws MotekarException {
        errorString = new StringBuilder();

        SKPD skpd = null;
        Object obj = comboSKPD.getSelectedItem();

        if (obj instanceof SKPD) {
            skpd = (SKPD) obj;
        }

        String sp2dNumber = fieldSP2DNumber.getText();
        Date sp2dDate = fieldSP2DDate.getDate();
        String receiver = fieldReceiver.getText();
        String purpose = fieldPurpose.getText();

        BigDecimal amount = BigDecimal.ZERO;
        Object prObj = fieldAmount.getValue();

        if (prObj instanceof Long) {
            amount = BigDecimal.valueOf((Long) prObj);
        } else if (prObj instanceof Double) {
            amount = BigDecimal.valueOf((Double) prObj);
        } else if (prObj instanceof BigDecimal) {
            amount = (BigDecimal) prObj;
        }

        String description = fieldDescription.getText();

        if (skpd == null) {
            errorString.append("<br>- SKPD</br>");
        }

        if (sp2dNumber.equals("")) {
            errorString.append("<br>- Nomor SP2D</br>");
        }

        if (sp2dDate == null) {
            errorString.append("<br>- Tanggal Pencairan SP2D</br>");
        }

        ArrayList<SP2DFile> sp2dFiles = sp2dfList.getSP2DFiles();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        SP2D sp2d = new SP2D();
        sp2d.setSkpd(skpd);
        sp2d.setSp2dNumber(sp2dNumber);
        sp2d.setSp2dDate(sp2dDate);
        sp2d.setReceiver(receiver);
        sp2d.setPurpose(purpose);
        sp2d.setAmount(amount);
        sp2d.setDescription(description);
        sp2d.setsP2DFiles(sp2dFiles);

        return sp2d;
    }
    
    private void onViewDocument(SP2DFile file) {

        String fileExtension = file.getFileName();

        if (fileExtension.toLowerCase().endsWith(".png") || fileExtension.toLowerCase().endsWith(".jpg")
                || fileExtension.toLowerCase().endsWith(".gif") || fileExtension.toLowerCase().endsWith(".bmp")) {

            Cursor oldCursor = sp2dfList.getCursor();
            sp2dfList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ImageViewerDlg dlg = new ImageViewerDlg(mainframe, file.getFileStream(), fileExtension, true);
            dlg.showDialog();
            sp2dfList.setCursor(oldCursor);

        } else if (fileExtension.toLowerCase().endsWith(".doc") || fileExtension.toLowerCase().endsWith(".docx")
                || fileExtension.toLowerCase().endsWith(".xls") || fileExtension.toLowerCase().endsWith(".xlsx")
                || fileExtension.toLowerCase().endsWith(".pdf")) {
            OutputStream out = null;
            try {
                Cursor oldCursor = sp2dfList.getCursor();
                sp2dfList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                File fileToCopy = new File(System.getProperty("user.dir") + "\\temp" + File.separator + fileExtension);
                out = new FileOutputStream(fileToCopy);
                out.write(file.getFileStream());
                out.close();
                String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                Runtime.getRuntime().exec(commands);
                sp2dfList.setCursor(oldCursor);
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

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(comboSKPD.getEditor().getEditorComponent());
        comp.add(fieldSP2DNumber);
        comp.add(fieldSP2DDate.getEditor());
        comp.add(fieldReceiver);
        comp.add(fieldPurpose);
        comp.add(fieldAmount);
        comp.add(fieldDescription);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedSP2D != null) {
            try {
                selectedSP2D = logic.getCompleteSP2D(mainframe.getSession(), selectedSP2D);


                if (selectedSP2D.getSkpd() == null) {
                    comboSKPD.getEditor().setItem(null);
                } else {
                    comboSKPD.setSelectedItem(selectedSP2D.getSkpd());
                }

                fieldSP2DNumber.setText(selectedSP2D.getSp2dNumber());
                fieldSP2DDate.setDate(selectedSP2D.getSp2dDate());
                fieldPurpose.setText(selectedSP2D.getPurpose());
                fieldReceiver.setText(selectedSP2D.getReceiver());
                fieldAmount.setValue(selectedSP2D.getAmount());
                fieldDescription.setText(selectedSP2D.getDescription());

                sp2dfList.clear();
                sp2dfList.addSP2DFiles(selectedSP2D.getsP2DFiles());
                setButtonState("Save");
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            clearForm();
            setButtonState("");
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedSP2D = sp2dList.getSelectedSP2D();
            setFormValues();
        }
    }

    private class SP2DList extends JXList {

        private Icon SP2D_ICON = Mainframe.getResizableIconFromSource("resource/SP2D.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public SP2DList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadSP2D((DefaultListModel) getModel());
            progressListener = new SP2DProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public SP2D getSelectedSP2D() {
            SP2D sp2d = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof SP2D) {
                sp2d = (SP2D) obj;
            }
            return sp2d;
        }

        public void addSP2D(SP2D sp2d) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(sp2d);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateSP2D(SP2D sp2d) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(sp2d, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<SP2D> sp2ds) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(sp2ds);
            filter();
        }

        public void removeSelected(SP2D sp2d) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(sp2d);
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

                    SP2D sp2d = null;

                    if (o instanceof SP2D) {
                        sp2d = (SP2D) o;
                    }

                    if (sp2d != null) {
                        return SP2DList.this.SP2D_ICON;
                    }

                    return SP2DList.this.NULL_ICON;
                }
            });
        }
    }

    private class SP2DFileFileGrid extends JGrid {

        public SP2DFileFileGrid() {
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

        public SP2DFile getSelectedSP2DFile() {
            SP2DFile file = null;
            Object obj = this.getModel().getElementAt(this.getSelectedIndex());
            if (obj instanceof SP2DFile) {
                file = (SP2DFile) obj;
            }
            return file;
        }

        public ArrayList<SP2DFile> getSP2DFiles() {
            ArrayList<SP2DFile> sp2dFiles = new ArrayList<SP2DFile>();

            int size = this.getModel().getSize();

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    SP2DFile file = null;
                    Object obj = this.getModel().getElementAt(i);
                    if (obj instanceof SP2DFile) {
                        file = (SP2DFile) obj;
                    }

                    if (file != null) {
                        sp2dFiles.add(file);
                    }
                }
            }

            return sp2dFiles;
        }

        public void addSP2DFile(SP2DFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(file);
        }

        public void addSP2DFiles(ArrayList<SP2DFile> files) {
            DefaultListModel model = (DefaultListModel) getModel();

            if (!files.isEmpty()) {
                for (SP2DFile file : files) {
                    model.addElement(file);
                }
            }

        }

        public void updateSP2DFile(SP2DFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(file, getSelectedIndex());
        }

        public void removeSelected(SP2DFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(file);
        }
    }

    private class LoadSP2D extends SwingWorker<DefaultListModel, SP2D> {

        private DefaultListModel model;
        private Exception exception;

        public LoadSP2D(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<SP2D> chunks) {
            mainframe.stopInActiveListener();
            for (SP2D sp2d : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat SP2D Nomor " + sp2d.getSp2dNumber());
                model.addElement(sp2d);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<SP2D> sp2ds = new ArrayList<SP2D>();

                if (checkBox.isSelected()) {
                    sp2ds = logic.getSP2D(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear(),"");
                } else {
                    sp2ds = logic.getSP2D(mainframe.getSession());
                }

                double progress;
                if (!sp2ds.isEmpty()) {
                    for (int i = 0; i < sp2ds.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / sp2ds.size();
                        
                        SP2D sp2d = sp2ds.get(i);
                        sp2d.setStyled(true);
                        
                        if (sp2d.getSkpd() != null) {
                            sp2d.setSkpd(mLogic.getSKPDByIndex(mainframe.getSession(), sp2d.getSkpd()));
                        }
                        
                        setProgress((int) progress);
                        publish(sp2d);
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
                JXErrorPane.showDialog(SP2DPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class SP2DProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private SP2DProgressListener() {
        }

        SP2DProgressListener(JProgressBar progressBar) {
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
