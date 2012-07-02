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
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.mail.objects.Outbox;
import org.motekar.project.civics.archieve.mail.objects.OutboxFile;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
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
public class OutboxPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private OutboxList outboxList = new OutboxList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     *
     */
    private JXDatePicker fieldMailDate = new JXDatePicker();
    private JXTextField fieldMailNumber = new JXTextField();
    private JXTextField fieldSubject = new JXTextField();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private OutboxFileFileGrid ofList = new OutboxFileFileGrid();
    private JSlider slider;
    //
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private JCommandButton btInsFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btDelFile = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    /**
     *
     */
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Outbox selectedOutbox = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadOutbox worker;
    private OutboxProgressListener progressListener;
    private JFileChooser openChooser;
    private JFileChooser saveChooser;

    public OutboxPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
        outboxList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Keluar");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(outboxList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Keluar untuk pengisian data-data pegawai yang ada di suatu dinas\n\n"
                + "Tambah Surat Keluar\n"
                + "Untuk menambah Surat Keluar klik tombol paling kiri "
                + "kemudian isi data Surat Keluar baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Keluar\n"
                + "Untuk merubah Surat Keluar klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Keluar, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Keluar\n"
                + "Untuk menghapus Surat Keluar klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Keluar "
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
        task.setTitle("Tambah / Edit / Hapus Surat Keluar");
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
                "pref,5px,pref,5px,pref,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tanggal Surat", cc.xy(1, 1));
        builder.add(fieldMailDate, cc.xy(3, 1));

        builder.addLabel("Nomor Surat", cc.xy(1, 3));
        builder.add(fieldMailNumber, cc.xy(3, 3));

        builder.addLabel("Perihal", cc.xy(1, 5));
        builder.add(fieldSubject, cc.xy(3, 5));

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
        scPane.setViewportView(ofList);


        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Scan Dokumen Surat", cc.xyw(1, 1, 4));

        builder.add(createStrip2(1.0, 1.0), cc.xy(1, 3));
        builder.add(createSliderPanel(), cc.xy(3, 3));
        builder.add(scPane, cc.xywh(1, 5, 3, 2));

        JPanel panel = builder.getPanel();

        ofList.setBackground(panel.getBackground());

        return panel;
    }

    private JPanel createSliderPanel() {
        slider = new JSlider(128, 256, ofList.getFixedCellDimension());
        slider.setValue(ofList.getFixedCellDimension());
        slider.putClientProperty("JComponent.sizeVariant", "small");
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ofList.setFixedCellDimension(slider.getValue());
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
        addTooltip.setTitle("Tambah Surat Keluar");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Keluar");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Keluar");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Keluar");

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
                outboxList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                outboxList.loadData();
            }
        });

        ofList.addMouseListener(new MouseAdapter() {

            Cursor oldCursor = ofList.getCursor();

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = ofList.getCellAt(e.getPoint());
                if (index >= 0) {
                    Object o = ofList.getModel().getElementAt(index);
                    if (o instanceof OutboxFile) {
                        OutboxFile file = (OutboxFile) o;
                        if (e.getClickCount() == 2) {
                            onViewDocument(file);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ofList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ofList.setCursor(oldCursor);
            }
        });

        fieldMailDate.setFormats("dd/MM/yyyy");

        outboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outboxList.setAutoCreateRowSorter(true);
        outboxList.addListSelectionListener(this);

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
        outboxList.setRowFilter(new RowFilter<ListModel, Integer>() {

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
        fieldMailNumber.setEnabled(iseditable);
        fieldMailDate.setEnabled(iseditable);
        fieldSubject.setEnabled(iseditable);
        fieldSearch.setEnabled(!iseditable);
        outboxList.setEnabled(!iseditable);

        checkBox.setEnabled(!iseditable);
        monthChooser.setEnabled(!iseditable && checkBox.isSelected());
        yearChooser.setEnabled(!iseditable && checkBox.isSelected());

        btInsFile.setEnabled(iseditable);
        btDelFile.setEnabled(iseditable);
    }

    private void clearForm() {
        fieldMailNumber.setText("");
        fieldMailDate.setDate(null);
        fieldSubject.setText("");
        if (fieldMailDate.isEnabled()) {
            fieldMailDate.requestFocus();
        }

        ofList.clear();
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
            outboxList.loadData();
        } else if (obj == btInsFile) {
            int retVal = openChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = openChooser.getSelectedFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    byte[] fileStream = new byte[(int) file.length()];
                    fileInputStream.read(fileStream);

                    OutboxFile outboxFile = new OutboxFile();
                    if (selectedOutbox != null) {
                        outboxFile.setOutboxIndex(selectedOutbox.getIndex());
                    }
                    outboxFile.setFileName(file.getName());
                    outboxFile.setFileStream(fileStream);

                    ofList.addOutboxFile(outboxFile);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (obj == btDelFile) {
            OutboxFile outboxFile = ofList.getSelectedOutboxFile();
            if (outboxFile != null) {
                Object[] options = {"Ya", "Tidak"};
                int choise = JOptionPane.showOptionDialog(this,
                        " Anda yakin menghapus semua data ini ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    ofList.removeSelected(outboxFile);
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
        statusLabel.setText("Tambah Surat Keluar");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldMailDate.requestFocus();
        statusLabel.setText("Ubah Surat Keluar");
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
                logic.deleteOutbox(mainframe.getSession(), selectedOutbox.getIndex());
                outboxList.removeSelected(selectedOutbox);
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
            Outbox newOutbox = getOutbox();

            if (isnew) {
                newOutbox = logic.insertOutbox(mainframe.getSession(), newOutbox);
                isnew = false;
                iseditable = false;
                outboxList.addOutbox(newOutbox);
                selectedOutbox = newOutbox;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newOutbox = logic.updateOutbox(mainframe.getSession(), selectedOutbox, newOutbox);
                isedit = false;
                iseditable = false;
                outboxList.updateOutbox(newOutbox);
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
        if (outboxList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private Outbox getOutbox() throws MotekarException {
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

        ArrayList<OutboxFile> outboxFiles = ofList.getOutboxFiles();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Outbox outbox = new Outbox();
        outbox.setMailDate(mailDate);
        outbox.setMailNumber(mailNumber);
        outbox.setSubject(subject);
        outbox.setOutboxFiles(outboxFiles);

        return outbox;
    }
    
    private void onViewDocument(OutboxFile file) {

        String fileExtension = file.getFileName();

        if (fileExtension.toLowerCase().endsWith(".png") || fileExtension.toLowerCase().endsWith(".jpg")
                || fileExtension.toLowerCase().endsWith(".gif") || fileExtension.toLowerCase().endsWith(".bmp")) {

            Cursor oldCursor = ofList.getCursor();
            ofList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ImageViewerDlg dlg = new ImageViewerDlg(mainframe, file.getFileStream(), fileExtension, true);
            dlg.showDialog();
            ofList.setCursor(oldCursor);

        } else if (fileExtension.toLowerCase().endsWith(".doc") || fileExtension.toLowerCase().endsWith(".docx")
                || fileExtension.toLowerCase().endsWith(".xls") || fileExtension.toLowerCase().endsWith(".xlsx")
                || fileExtension.toLowerCase().endsWith(".pdf")) {
            OutputStream out = null;
            try {
                Cursor oldCursor = ofList.getCursor();
                ofList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                File fileToCopy = new File(System.getProperty("user.dir") + "\\temp" + File.separator + fileExtension);
                out = new FileOutputStream(fileToCopy);
                out.write(file.getFileStream());
                out.close();
                String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                Runtime.getRuntime().exec(commands);
                ofList.setCursor(oldCursor);
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
        comp.add(fieldMailDate);
        comp.add(fieldMailNumber);
        comp.add(fieldSubject);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedOutbox != null) {
            try {
                selectedOutbox = logic.getCompleteOutbox(mainframe.getSession(), selectedOutbox);
                fieldMailDate.setDate(selectedOutbox.getMailDate());
                fieldMailNumber.setText(selectedOutbox.getMailNumber());
                fieldSubject.setText(selectedOutbox.getSubject());
                ofList.clear();
                ofList.addOutboxFiles(selectedOutbox.getOutboxFiles());
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
            selectedOutbox = outboxList.getSelectedOutbox();
            setFormValues();
        }
    }

    private class OutboxList extends JXList {

        private Icon MAIL_ICON = Mainframe.getResizableIconFromSource("resource/mail.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public OutboxList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadOutbox((DefaultListModel) getModel());
            progressListener = new OutboxProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Outbox getSelectedOutbox() {
            Outbox outbox = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Outbox) {
                outbox = (Outbox) obj;
            }
            return outbox;
        }

        public void addOutbox(Outbox outbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(outbox);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateOutbox(Outbox outbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(outbox, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Outbox> outboxs) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(outboxs);
            filter();
        }

        public void removeSelected(Outbox outbox) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(outbox);
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

                    Outbox outbox = null;

                    if (o instanceof Outbox) {
                        outbox = (Outbox) o;
                    }

                    if (outbox != null) {
                        return OutboxList.this.MAIL_ICON;
                    }

                    return OutboxList.this.NULL_ICON;
                }
            });
        }
    }

    private class OutboxFileFileGrid extends JGrid {

        public OutboxFileFileGrid() {
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

        public OutboxFile getSelectedOutboxFile() {
            OutboxFile file = null;
            Object obj = this.getModel().getElementAt(this.getSelectedIndex());
            if (obj instanceof OutboxFile) {
                file = (OutboxFile) obj;
            }
            return file;
        }

        public ArrayList<OutboxFile> getOutboxFiles() {
            ArrayList<OutboxFile> outboxFiles = new ArrayList<OutboxFile>();

            int size = this.getModel().getSize();

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    OutboxFile file = null;
                    Object obj = this.getModel().getElementAt(i);
                    if (obj instanceof OutboxFile) {
                        file = (OutboxFile) obj;
                    }

                    if (file != null) {
                        outboxFiles.add(file);
                    }
                }
            }

            return outboxFiles;
        }

        public void addOutboxFile(OutboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(file);
        }

        public void addOutboxFiles(ArrayList<OutboxFile> files) {
            DefaultListModel model = (DefaultListModel) getModel();

            if (!files.isEmpty()) {
                for (OutboxFile file : files) {
                    model.addElement(file);
                }
            }

        }

        public void updateOutboxFile(OutboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(file, getSelectedIndex());
        }

        public void removeSelected(OutboxFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(file);
        }
    }

    private class LoadOutbox extends SwingWorker<DefaultListModel, Outbox> {

        private DefaultListModel model;
        private Exception exception;

        public LoadOutbox(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Outbox> chunks) {
            mainframe.stopInActiveListener();
            for (Outbox outbox : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Keluar Nomor " + outbox.getMailNumber());
                model.addElement(outbox);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {

                ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

                if (checkBox.isSelected()) {
                    outboxs = logic.getOutbox(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    outboxs = logic.getOutbox(mainframe.getSession());
                }

                double progress = 0.0;
                if (!outboxs.isEmpty()) {
                    for (int i = 0; i < outboxs.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / outboxs.size();
                        setProgress((int) progress);
                        publish(outboxs.get(i));
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
                JXErrorPane.showDialog(OutboxPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class OutboxProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private OutboxProgressListener() {
        }

        OutboxProgressListener(JProgressBar progressBar) {
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
