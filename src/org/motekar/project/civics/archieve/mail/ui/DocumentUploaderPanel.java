package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.mail.objects.DocumentFile;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ImageViewerDlg;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class DocumentUploaderPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private DocumentFileList fileList = new DocumentFileList();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private DocumentFile selectedDocumentFile = null;
    private LoadDocumentFile worker;
    private DocumentFileProgressListener progressListener;
    private StringBuilder errorString = new StringBuilder();
    //
    private byte[] fileStream = null;
    private String fileExtension = "";
    private JXTextField fieldDocument = new JXTextField();
    private JXTextArea fieldDecription = new JXTextArea();
    private JCommandButton btFileAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/file_upload.png"));
    private JCommandButton btFileSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/file_download.png"));
    private JCommandButton btFileView = new JCommandButton(Mainframe.getResizableIconFromSource("resource/paper_text.png"));

    public DocumentUploaderPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
        fileList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Dokumen");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(fileList), BorderLayout.CENTER);
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
                + "Fasilitas Upload Dokumen merupakan fasilitas untuk upload document\n\n"
                + "Tambah Dokumen\n"
                + "Untuk menambah Dokumen klik tombol paling kiri "
                + "kemudian isi data Dokumen baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Dokumen\n"
                + "Untuk merubah Dokumen klik tombol kedua dari kiri "
                + "kemudian ubah data Dokumen, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Dokumen\n"
                + "Untuk menghapus Dokumen klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Dokumen "
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
        task.setTitle("Tambah / Edit / Hapus Dokumen");
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
                "pref,10px,fill:default:grow,1px,pref,30px",
                "pref,5px,pref,fill:default:grow,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3}});
        
        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDecription);
        
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Dokumen", cc.xy(1, 1));
        builder.add(fieldDocument, cc.xy(3, 1));
        builder.add(createFileStrip(1.0, 1.0), cc.xy(5, 1));
        
        builder.addLabel("Deskripsi", cc.xy(1, 3));
        builder.add(scPane, cc.xywh(3, 3,3,2));

        return builder.getPanel();
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
        addTooltip.setTitle("Tambah Dokumen");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Dokumen");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Dokumen");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Dokumen");

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

    private JCommandButtonStrip createFileStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Upload / Edit Dokumen");

        btFileAdd.setActionRichTooltip(addTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Download / Simpan Dokumen");

        btFileSave.setActionRichTooltip(saveTooltip);

        RichTooltip viewTooltip = new RichTooltip();
        viewTooltip.setTitle("Lihat Dokumen");

        btFileView.setActionRichTooltip(viewTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btFileAdd);
        buttonStrip.add(btFileSave);
        buttonStrip.add(btFileView);

        return buttonStrip;
    }

    private void construct() {

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

        btFileAdd.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onOpenDocument();
            }
        });
        
        btFileSave.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveDocument();
            }
        });

        btFileView.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onViewDocument();
            }
        });

        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setAutoCreateRowSorter(true);
        fileList.addListSelectionListener(this);

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

    public void filter() {
        fileList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void onOpenDocument() {
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
        chooser.setDialogTitle("Pilih Dokumen");

        chooser.setFileFilter(new FileFilter() {

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
                return "File Dokumen";
            }
        });

        int retVal = chooser.showDialog(mainframe, "Pilih");
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {

                fileExtension = file.getName();

                FileInputStream fileInputStream = new FileInputStream(file);

                fileStream = new byte[(int) file.length()];
                fileInputStream.read(fileStream);

                fieldDocument.setText(fileExtension);

            } catch (Exception ex) {
            }
        }

    }

    private void onSaveDocument() {
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
        chooser.setDialogTitle("Simpan Dokumen");

        chooser.setFileFilter(new FileFilter() {

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
                return "File Dokumen";
            }
        });

        int retVal = chooser.showDialog(mainframe, "Simpan");
        if (retVal == JFileChooser.APPROVE_OPTION) {
            OutputStream out = null;
            try {
                File file = chooser.getSelectedFile();
                File fileToCopy = new File(file.getPath()+ "." + getExtractedFileExtension());
                
                out = new FileOutputStream(fileToCopy);
                out.write(fileStream);
                out.close();
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

    private void onViewDocument() {
        if (fileExtension.toLowerCase().endsWith(".png") || fileExtension.toLowerCase().endsWith(".jpg")
                || fileExtension.toLowerCase().endsWith(".gif") || fileExtension.toLowerCase().endsWith(".bmp")) {

            ImageViewerDlg dlg = new ImageViewerDlg(mainframe, fileStream, fileExtension, true);
            dlg.showDialog();

        } else if (fileExtension.toLowerCase().endsWith(".doc") || fileExtension.toLowerCase().endsWith(".docx")
                || fileExtension.toLowerCase().endsWith(".pdf")) {
            OutputStream out = null;
            try {
                File fileToCopy = new File(System.getProperty("user.dir") + "\\temp" + File.separator + fileExtension);
                out = new FileOutputStream(fileToCopy);
                out.write(fileStream);
                out.close();
                String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileToCopy.getPath()};
                Runtime.getRuntime().exec(commands);
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
    
    private String getExtractedFileExtension() {
        String extFile = "";
        
        StringTokenizer token = new StringTokenizer(fileExtension, ".");
        
        while (token.hasMoreElements()) {
            extFile = token.nextToken();
        }
        
        return extFile;
    }

    private void setFormState() {
        fieldDocument.setEnabled(false);
        fieldDecription.setEnabled(iseditable);
        btFileAdd.setEnabled(iseditable);
        btFileSave.setEnabled(!iseditable);
        btFileView.setEnabled(!iseditable);

        fieldSearch.setEnabled(!iseditable);
        fileList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldDocument.setText("");
        fieldDecription.setText("");
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
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedDocumentFile = fileList.getSelectedDocumentFile();
            setFormValues();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Dokumen");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        statusLabel.setText("Ubah Dokumen");
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
                logic.deleteDocumentFile(mainframe.getSession(), selectedDocumentFile.getIndex());
                fileList.removeSelected(selectedDocumentFile);
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
            DocumentFile newDocumentFile = getDocumentFile();

            if (isnew) {
                newDocumentFile = logic.insertDocumentFile(mainframe.getSession(), newDocumentFile);
                newDocumentFile.setStyled(true);
                isnew = false;
                iseditable = false;
                fileList.addDocumentFile(newDocumentFile);
                selectedDocumentFile = newDocumentFile;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newDocumentFile = logic.updateDocumentFile(mainframe.getSession(), selectedDocumentFile, newDocumentFile);
                newDocumentFile.setStyled(true);
                isedit = false;
                iseditable = false;
                fileList.updateDocumentFile(newDocumentFile);
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
        if (fileList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
    }

    private void setFormValues() {
        if (selectedDocumentFile != null) {

            fileStream = selectedDocumentFile.getFileStream();
            fileExtension = selectedDocumentFile.getFileName();

            if (fileStream != null) {
                fieldDocument.setText(fileExtension);
            } else {
                fieldDocument.setText("");
            }
            
            fieldDecription.setText(selectedDocumentFile.getDescription());

            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private DocumentFile getDocumentFile() throws MotekarException {
        
        String description = fieldDecription.getText();
        
        if (fileExtension.equals("")) {
            errorString.append("<br>- Dokumen</br>");
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        DocumentFile file = new DocumentFile();

        file.setFileName(fileExtension);
        file.setFileStream(fileStream);
        file.setDescription(description);

        return file;
    }

    private class DocumentFileList extends JXList {

        private Icon PNG_ICON = Mainframe.getResizableIconFromSource("resource/Image-png.png", new Dimension(36, 36));
        private Icon JPG_ICON = Mainframe.getResizableIconFromSource("resource/Image-jpg.png", new Dimension(36, 36));
        private Icon BMP_ICON = Mainframe.getResizableIconFromSource("resource/Image-bmp.png", new Dimension(36, 36));
        private Icon GIF_ICON = Mainframe.getResizableIconFromSource("resource/Image-gif.png", new Dimension(36, 36));
        private Icon PDF_ICON = Mainframe.getResizableIconFromSource("resource/Adobe Acrobat.png", new Dimension(36, 36));
        private Icon XLS_ICON = Mainframe.getResizableIconFromSource("resource/Excel.png", new Dimension(36, 36));
        private Icon DOC_ICON = Mainframe.getResizableIconFromSource("resource/Word.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public DocumentFileList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadDocumentFile((DefaultListModel) getModel());
            progressListener = new DocumentFileProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public DocumentFile getSelectedDocumentFile() {
            DocumentFile file = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof DocumentFile) {
                file = (DocumentFile) obj;
            }
            return file;
        }

        public void addDocumentFile(DocumentFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(file);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateDocumentFile(DocumentFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(file, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<DocumentFile> files) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(files);
            filter();
        }

        public void removeSelected(DocumentFile file) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(file);
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

                    DocumentFile file = null;

                    if (o instanceof DocumentFile) {
                        file = (DocumentFile) o;
                    }

                    if (file != null) {
                        String fileName = file.getFileName();
                        String fileExt = "";

                        StringTokenizer token = new StringTokenizer(fileName, ".");

                        while (token.hasMoreElements()) {
                            fileExt = token.nextToken();
                        }

                        if (fileExt.equals("")) {
                            return DocumentFileList.this.NULL_ICON;
                        } else if (fileExt.toLowerCase().equals("bmp")) {
                            return DocumentFileList.this.BMP_ICON;
                        } else if (fileExt.toLowerCase().equals("jpg")) {
                            return DocumentFileList.this.JPG_ICON;
                        } else if (fileExt.toLowerCase().equals("gif")) {
                            return DocumentFileList.this.GIF_ICON;
                        } else if (fileExt.toLowerCase().equals("png")) {
                            return DocumentFileList.this.PNG_ICON;
                        } else if (fileExt.toLowerCase().equals("pdf")) {
                            return DocumentFileList.this.PDF_ICON;
                        } else if (fileExt.toLowerCase().equals("xls") || fileExt.toLowerCase().equals("xlsx")) {
                            return DocumentFileList.this.XLS_ICON;
                        } else if (fileExt.toLowerCase().equals("doc") || fileExt.toLowerCase().equals("docx")) {
                            return DocumentFileList.this.DOC_ICON;
                        }
                    }

                    return DocumentFileList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadDocumentFile extends SwingWorker<DefaultListModel, DocumentFile> {

        private DefaultListModel model;
        private Exception exception;

        public LoadDocumentFile(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<DocumentFile> chunks) {
            mainframe.stopInActiveListener();
            for (DocumentFile file : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Dokumen " + file.getFileName());
                model.addElement(file);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<DocumentFile> files = logic.getDocumentFile(mainframe.getSession());

                double progress = 0.0;
                if (!files.isEmpty()) {
                    for (int i = 0; i < files.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / files.size();
                        setProgress((int) progress);
                        publish(files.get(i));
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
                JXErrorPane.showDialog(DocumentUploaderPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class DocumentFileProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private DocumentFileProgressListener() {
        }

        DocumentFileProgressListener(JProgressBar progressBar) {
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
