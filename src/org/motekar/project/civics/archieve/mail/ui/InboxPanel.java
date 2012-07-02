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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.mail.objects.Inbox;
import org.motekar.project.civics.archieve.mail.objects.InboxFile;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Division;
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
public class InboxPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private MasterBusinessLogic mLogic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private InboxList inboxList = new InboxList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXDatePicker fieldMailDate = new JXDatePicker();
    private JXTextField fieldMailNumber = new JXTextField();
    private JXTextField fieldSubject = new JXTextField();
    private JXTextField fieldSender = new JXTextField();
    private JXTextArea fieldSenderAddress = new JXTextArea();
    private JXDatePicker fieldDispositionDate = new JXDatePicker();
    private JXComboBox comboReceiver = new JXComboBox();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private InboxFileFileGrid ifList = new InboxFileFileGrid();
    private JSlider slider;
    /**
     *
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/new_mail_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private JCommandButton btInsFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    /**
     *
     */
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Inbox selectedInbox = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadInbox worker;
    private InboxProgressListener progressListener;
    private JFileChooser openChooser;
    private JFileChooser saveChooser;

    public InboxPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        inboxList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Masuk");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(inboxList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Masuk untuk pengisian surat masuk pada suatu dinas/kantor/badan\n\n"
                + "Tambah Surat Masuk\n"
                + "Untuk menambah Surat Masuk klik tombol paling kiri "
                + "kemudian isi data Surat Masuk baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Masuk\n"
                + "Untuk merubah Surat Masuk klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Masuk, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Masuk\n"
                + "Untuk menghapus Surat Masuk klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Masuk "
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
        task.setTitle("Tambah / Edit / Hapus Surat Masuk");
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
                "pref,5px,pref,5px,pref,5px,pref,5px,"
                + "fill:default,fill:default:grow,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 12}});

        JScrollPane addressPane = new JScrollPane();
        addressPane.setViewportView(fieldSenderAddress);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tanggal Surat", cc.xy(1, 1));
        builder.add(fieldMailDate, cc.xy(3, 1));

        builder.addLabel("Nomor Surat", cc.xy(1, 3));
        builder.add(fieldMailNumber, cc.xy(3, 3));

        builder.addLabel("Perihal", cc.xy(1, 5));
        builder.add(fieldSubject, cc.xy(3, 5));

        builder.addLabel("Pengirim", cc.xy(1, 7));
        builder.add(fieldSender, cc.xy(3, 7));

        builder.addLabel("Alamat Pengirim", cc.xy(1, 9));
        builder.add(addressPane, cc.xywh(3, 9, 1, 2));

        builder.addLabel("Tanggal Disposisi", cc.xy(1, 12));
        builder.add(fieldDispositionDate, cc.xy(3, 12));

        builder.addLabel("Penerima", cc.xy(1, 14));
        builder.add(comboReceiver, cc.xy(3, 14));

        tabbedPane.addTab("Input Data", builder.getPanel());
        tabbedPane.addTab("Lampiran Surat", createMainPanelPage2());

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
        scPane.setViewportView(ifList);


        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Scan Dokumen Surat", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(createSliderPanel(), cc.xy(3, 3));
        builder.add(scPane, cc.xywh(1, 5, 3, 2));

        JPanel panel = builder.getPanel();

        ifList.setBackground(panel.getBackground());

        return panel;
    }

    private JPanel createSliderPanel() {
        slider = new JSlider(128, 256, ifList.getFixedCellDimension());
        slider.setValue(ifList.getFixedCellDimension());
        slider.putClientProperty("JComponent.sizeVariant", "small");
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ifList.setFixedCellDimension(slider.getValue());
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
        addTooltip.setTitle("Tambah Surat Masuk");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Masuk");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Masuk");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Masuk");

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

        loadComboDivision();
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
                inboxList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                inboxList.loadData();
            }
        });

        ifList.addMouseListener(new MouseAdapter() {

            Cursor oldCursor = ifList.getCursor();

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = ifList.getCellAt(e.getPoint());
                if (index >= 0) {
                    Object o = ifList.getModel().getElementAt(index);
                    if (o instanceof InboxFile) {
                        InboxFile file = (InboxFile) o;
                        if (e.getClickCount() == 2) {
                            onViewDocument(file);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ifList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ifList.setCursor(oldCursor);
            }
        });

        comboReceiver.setEditable(true);

        fieldSenderAddress.setWrapStyleWord(true);
        fieldSenderAddress.setLineWrap(true);

        fieldDispositionDate.setFormats("dd/MM/yyyy");
        fieldMailDate.setFormats("dd/MM/yyyy");

        inboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inboxList.setAutoCreateRowSorter(true);
        inboxList.addListSelectionListener(this);

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

    public void filter() {
        inboxList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void loadComboDivision() {
        try {
            ArrayList<Division> divisions = mLogic.getDivision(mainframe.getSession(), false);

            divisions.add(0, new Division());
            comboReceiver.setModel(new ListComboBoxModel<Division>(divisions));
            AutoCompleteDecorator.decorate(comboReceiver);

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

        fieldDispositionDate.setEnabled(iseditable);
        fieldMailDate.setEnabled(iseditable);
        fieldMailNumber.setEnabled(iseditable);
        fieldSender.setEnabled(iseditable);
        fieldSenderAddress.setEnabled(iseditable);
        fieldSubject.setEnabled(iseditable);
        comboReceiver.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        inboxList.setEnabled(!iseditable);

        checkBox.setEnabled(!iseditable);
        monthChooser.setEnabled(!iseditable && checkBox.isSelected());
        yearChooser.setEnabled(!iseditable && checkBox.isSelected());

        btInsFile.setEnabled(iseditable);
        btDelFile.setEnabled(iseditable);
    }

    private void clearForm() {
        fieldDispositionDate.setDate(null);
        fieldMailDate.setDate(null);
        fieldMailNumber.setText("");
        fieldSender.setText("");
        fieldSenderAddress.setText("");
        fieldSubject.setText("");

        if (comboReceiver.getItemCount() > 0) {
            comboReceiver.setSelectedIndex(0);
        }

        if (fieldMailDate.isEnabled()) {
            fieldMailDate.requestFocus();
        }

        ifList.clear();

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
            inboxList.loadData();
        } else if (obj == btInsFile) {
            int retVal = openChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = openChooser.getSelectedFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    byte[] fileStream = new byte[(int) file.length()];
                    fileInputStream.read(fileStream);

                    InboxFile inboxFile = new InboxFile();
                    if (selectedInbox != null) {
                        inboxFile.setInboxIndex(selectedInbox.getIndex());
                    }
                    inboxFile.setFileName(file.getName());
                    inboxFile.setFileStream(fileStream);

                    ifList.addInboxFile(inboxFile);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (obj == btDelFile) {
            InboxFile inboxFile = ifList.getSelectedInboxFile();
            if (inboxFile != null) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    ifList.removeSelected(inboxFile);
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
        statusLabel.setText("Tambah Surat Masuk");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldMailDate.requestFocus();
        statusLabel.setText("Ubah Surat Masuk");
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
                logic.deleteInbox(mainframe.getSession(), selectedInbox.getIndex());
                inboxList.removeSelected(selectedInbox);
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
            Inbox newInbox = getInbox();

            if (isnew) {
                newInbox = logic.insertInbox(mainframe.getSession(), newInbox);
                isnew = false;
                iseditable = false;
                inboxList.addInbox(newInbox);
                selectedInbox = newInbox;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newInbox = logic.updateInbox(mainframe.getSession(), selectedInbox, newInbox);
                isedit = false;
                iseditable = false;
                inboxList.updateInbox(newInbox);
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
        if (inboxList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private Inbox getInbox() throws MotekarException {
        errorString = new StringBuilder();

        Date mailDate = fieldMailDate.getDate();

        if (mailDate == null) {
            errorString.append("<br>- Tanggal Surat</br>");
        }

        String mailNumber = fieldMailNumber.getText();

        if (mailNumber.equals("")) {
            errorString.append("<br>- Nomor Surat</br>");
        }

        String subject = fieldSubject.getText();

        if (subject.equals("")) {
            errorString.append("<br>- Perihal</br>");
        }

        String sender = fieldSender.getText();

        if (sender.equals("")) {
            errorString.append("<br>- Pengirim</br>");
        }

        String senderAddress = fieldSenderAddress.getText();

        if (senderAddress.equals("")) {
            errorString.append("<br>- Alamat Pengirim</br>");
        }

        Date dispositionDate = fieldDispositionDate.getDate();

        Division receiver = null;

        Object obj = comboReceiver.getSelectedItem();

        if (obj instanceof Division) {
            receiver = (Division) obj;
        }

        if (comboReceiver.getSelectedIndex() == 0) {
            receiver = null;
        }

        ArrayList<InboxFile> inboxFiles = ifList.getInboxFiles();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Inbox inbox = new Inbox();
        inbox.setMailDate(mailDate);
        inbox.setMailNumber(mailNumber);
        inbox.setSubject(subject);
        inbox.setSender(sender);
        inbox.setSenderAddress(senderAddress);
        inbox.setDispositionDate(dispositionDate);
        inbox.setReceipient(receiver);
        inbox.setInboxFiles(inboxFiles);

        return inbox;
    }

    private void onViewDocument(InboxFile file) {

        String fileExtension = file.getFileName();

        if (fileExtension.toLowerCase().endsWith(".png") || fileExtension.toLowerCase().endsWith(".jpg")
                || fileExtension.toLowerCase().endsWith(".gif") || fileExtension.toLowerCase().endsWith(".bmp")) {

            Cursor oldCursor = ifList.getCursor();
            ifList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ImageViewerDlg dlg = new ImageViewerDlg(mainframe, file.getFileStream(), fileExtension, true);
            dlg.showDialog();
            ifList.setCursor(oldCursor);

        } else if (fileExtension.toLowerCase().endsWith(".doc") || fileExtension.toLowerCase().endsWith(".docx")
                || fileExtension.toLowerCase().endsWith(".xls") || fileExtension.toLowerCase().endsWith(".xlsx")
                || fileExtension.toLowerCase().endsWith(".pdf")) {
            OutputStream out = null;
            try {
                Cursor oldCursor = ifList.getCursor();
                ifList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                File fileToCopy = new File(System.getProperty("user.dir") + "\\temp" + File.separator + fileExtension);
                out = new FileOutputStream(fileToCopy);
                out.write(file.getFileStream());
                out.close();
                String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                Runtime.getRuntime().exec(commands);
                ifList.setCursor(oldCursor);
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
        comp.add(fieldMailDate.getEditor());
        comp.add(fieldMailNumber);
        comp.add(fieldSubject);
        comp.add(fieldSender);
        comp.add(fieldSenderAddress);
        comp.add(fieldDispositionDate.getEditor());
        comp.add(comboReceiver.getEditor().getEditorComponent());

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedInbox != null) {
            try {
                selectedInbox = logic.getCompleteInbox(mainframe.getSession(), selectedInbox);
                fieldMailDate.setDate(selectedInbox.getMailDate());
                fieldMailNumber.setText(selectedInbox.getMailNumber());
                fieldSubject.setText(selectedInbox.getSubject());
                fieldSender.setText(selectedInbox.getSender());
                fieldSenderAddress.setText(selectedInbox.getSenderAddress());
                fieldDispositionDate.setDate(selectedInbox.getDispositionDate());
                if (selectedInbox.getReceipient() == null) {
                    comboReceiver.setSelectedIndex(0);
                } else {
                    comboReceiver.setSelectedItem(selectedInbox.getReceipient());
                }
                ifList.clear();
                ifList.addInboxFiles(selectedInbox.getInboxFiles());
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
            selectedInbox = inboxList.getSelectedInbox();
            setFormValues();
        }
    }

    private class InboxList extends JXList {

        private Icon MAIL_ICON = Mainframe.getResizableIconFromSource("resource/new_mail.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public InboxList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadInbox((DefaultListModel) getModel());
            progressListener = new InboxProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Inbox getSelectedInbox() {
            Inbox inbox = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Inbox) {
                inbox = (Inbox) obj;
            }
            return inbox;
        }

        public void addInbox(Inbox inbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(inbox);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateInbox(Inbox inbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(inbox, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Inbox> inboxs) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(inboxs);
            filter();
        }

        public void removeSelected(Inbox inbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(inbox);
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

                    Inbox inbox = null;

                    if (o instanceof Inbox) {
                        inbox = (Inbox) o;
                    }

                    if (inbox != null) {
                        return InboxList.this.MAIL_ICON;
                    }

                    return InboxList.this.NULL_ICON;
                }
            });
        }
    }

    private class InboxFileFileGrid extends JGrid {

        public InboxFileFileGrid() {
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

        public InboxFile getSelectedInboxFile() {
            InboxFile file = null;
            Object obj = this.getModel().getElementAt(this.getSelectedIndex());
            if (obj instanceof InboxFile) {
                file = (InboxFile) obj;
            }
            return file;
        }

        public ArrayList<InboxFile> getInboxFiles() {
            ArrayList<InboxFile> inboxFiles = new ArrayList<InboxFile>();

            int size = this.getModel().getSize();

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    InboxFile file = null;
                    Object obj = this.getModel().getElementAt(i);
                    if (obj instanceof InboxFile) {
                        file = (InboxFile) obj;
                    }

                    if (file != null) {
                        inboxFiles.add(file);
                    }
                }
            }

            return inboxFiles;
        }

        public void addInboxFile(InboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(file);
        }

        public void addInboxFiles(ArrayList<InboxFile> files) {
            DefaultListModel model = (DefaultListModel) getModel();

            if (!files.isEmpty()) {
                for (InboxFile file : files) {
                    model.addElement(file);
                }
            }

        }

        public void updateInboxFile(InboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(file, getSelectedIndex());
        }

        public void removeSelected(InboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(file);
        }
    }

    private class LoadInbox extends SwingWorker<DefaultListModel, Inbox> {

        private DefaultListModel model;
        private Exception exception;

        public LoadInbox(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Inbox> chunks) {
            mainframe.stopInActiveListener();
            for (Inbox inbox : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Masuk Nomor " + inbox.getMailNumber());
                model.addElement(inbox);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

                if (checkBox.isSelected()) {
                    inboxs = logic.getInbox(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    inboxs = logic.getInbox(mainframe.getSession());
                }

                double progress = 0.0;
                if (!inboxs.isEmpty()) {
                    for (int i = 0; i < inboxs.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / inboxs.size();
                        setProgress((int) progress);
                        publish(inboxs.get(i));
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
                JXErrorPane.showDialog(InboxPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class InboxProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private InboxProgressListener() {
        }

        InboxProgressListener(JProgressBar progressBar) {
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
