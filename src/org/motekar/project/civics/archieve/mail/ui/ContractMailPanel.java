package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
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
import org.jdesktop.swingx.JXDatePicker;
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
import org.motekar.project.civics.archieve.mail.objects.ContractMail;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
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

public class ContractMailPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private ContractMailList mailList = new ContractMailList();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    /**
     * 
     */
    private JXDatePicker fieldMailDate = new JXDatePicker();
    private JXTextField fieldMailNumber = new JXTextField();
    private JXTextField fieldReceiver = new JXTextField();
    private JXTextArea fieldReceiverAddress = new JXTextArea();
    private JXTextField fieldSubject = new JXTextField();
    private JXTextArea fieldDescription = new JXTextArea();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/mail_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    /**
     *
     */
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private ContractMail selectedContractMail = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadContractMail worker;
    private ContractMailProgressListener progressListener;
    private JFileChooser openChooser;
    private JFileChooser saveChooser;

    public ContractMailPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
        mailList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Keluar Kontrak");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(mailList), BorderLayout.CENTER);
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
                + "Fasilitas Surat Keluar Kontrak untuk pengisian data-data pegawai yang ada di suatu dinas\n\n"
                + "Tambah Surat Keluar Kontrak\n"
                + "Untuk menambah Surat Keluar Kontrak klik tombol paling kiri "
                + "kemudian isi data Surat Keluar Kontrak baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Surat Keluar Kontrak\n"
                + "Untuk merubah Surat Keluar Kontrak klik tombol kedua dari kiri "
                + "kemudian ubah data Surat Keluar Kontrak, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Surat Keluar Kontrak\n"
                + "Untuk menghapus Surat Keluar Kontrak klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Surat Keluar Kontrak "
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
        task.setTitle("Tambah / Edit / Hapus Surat Keluar Kontrak");
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
                + "fill:default,fill:default:grow,5px,fill:default,fill:default:grow,5px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 3, 5}});
        
        JScrollPane addressPane = new JScrollPane();
        addressPane.setViewportView(fieldReceiverAddress);
        
        JScrollPane descriptionPane = new JScrollPane();
        descriptionPane.setViewportView(fieldDescription);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tanggal Surat", cc.xy(1, 1));
        builder.add(fieldMailDate, cc.xy(3, 1));

        builder.addLabel("Nomor Surat", cc.xy(1, 3));
        builder.add(fieldMailNumber, cc.xy(3, 3));

        builder.addLabel("Perihal", cc.xy(1, 5));
        builder.add(fieldSubject, cc.xy(3, 5));
        
        builder.addLabel("Penerima", cc.xy(1, 7));
        builder.add(fieldReceiver, cc.xy(3, 7));
        
        builder.addLabel("Alamat Penerima", cc.xy(1, 9));
        builder.add(addressPane, cc.xywh(3, 9,1,2));
        
        builder.addLabel("Keterangan", cc.xy(1, 12));
        builder.add(descriptionPane, cc.xywh(3, 12,1,2));

        return builder.getPanel();
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
        addTooltip.setTitle("Tambah Surat Keluar Kontrak");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Surat Keluar Kontrak");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Surat Keluar Kontrak");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Surat Keluar Kontrak");

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
                mailList.loadData();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                mailList.loadData();
            }
        });

        fieldMailDate.setFormats("dd/MM/yyyy");

        mailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mailList.setAutoCreateRowSorter(true);
        mailList.addListSelectionListener(this);

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
        mailList.setRowFilter(new RowFilter<ListModel, Integer>() {

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
                return (fileName.toLowerCase().endsWith(".doc"));
            }

            @Override
            public String getDescription() {
                return "File Dokumen (*.doc)";
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
                return (fileName.toLowerCase().endsWith(".doc"));
            }

            @Override
            public String getDescription() {
                return "File Dokumen (*.doc)";
            }
        });
    }

    private void setFormState() {
        fieldMailNumber.setEnabled(iseditable);
        fieldMailDate.setEnabled(iseditable);
        fieldSubject.setEnabled(iseditable);
        fieldReceiver.setEnabled(iseditable);
        fieldReceiverAddress.setEnabled(iseditable);
        fieldDescription.setEnabled(iseditable);
        fieldSearch.setEnabled(!iseditable);
        mailList.setEnabled(!iseditable);

        checkBox.setEnabled(!iseditable);
        monthChooser.setEnabled(!iseditable && checkBox.isSelected());
        yearChooser.setEnabled(!iseditable && checkBox.isSelected());

    }

    private void clearForm() {
        fieldMailNumber.setText("");
        fieldMailDate.setDate(null);
        fieldSubject.setText("");
        fieldReceiver.setText("");
        fieldReceiverAddress.setText("");
        fieldDescription.setText("");
        if (fieldMailDate.isEnabled()) {
            fieldMailDate.requestFocus();
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
            mailList.loadData();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Surat Keluar Kontrak");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldMailDate.requestFocus();
        statusLabel.setText("Ubah Surat Keluar Kontrak");
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
                logic.deleteContractMail(mainframe.getSession(), selectedContractMail.getIndex());
                mailList.removeSelected(selectedContractMail);
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
            ContractMail newContractMail = getContractMail();

            if (isnew) {
                newContractMail = logic.insertContractMail(mainframe.getSession(), newContractMail);
                isnew = false;
                iseditable = false;
                mailList.addContractMail(newContractMail);
                selectedContractMail = newContractMail;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newContractMail = logic.updateContractMail(mainframe.getSession(), selectedContractMail, newContractMail);
                isedit = false;
                iseditable = false;
                mailList.updateContractMail(newContractMail);
                selectedContractMail = newContractMail;
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
        if (mailList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private ContractMail getContractMail() throws MotekarException {
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
        
        String receiver = fieldReceiver.getText();
        String receiverAddress = fieldReceiverAddress.getText();
        String description = fieldDescription.getText();

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ContractMail mail = new ContractMail();
        mail.setMailDate(mailDate);
        mail.setMailNumber(mailNumber);
        mail.setSubject(subject);
        mail.setReceiver(receiver);
        mail.setReceiverAddress(receiverAddress);
        mail.setDescription(description);

        return mail;
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldMailDate);
        comp.add(fieldMailNumber);
        comp.add(fieldSubject);
        comp.add(fieldReceiver);
        comp.add(fieldReceiverAddress);
        comp.add(fieldDescription);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedContractMail != null) {
            fieldMailDate.setDate(selectedContractMail.getMailDate());
            fieldMailNumber.setText(selectedContractMail.getMailNumber());
            fieldSubject.setText(selectedContractMail.getSubject());
            fieldReceiver.setText(selectedContractMail.getReceiver());
            fieldReceiverAddress.setText(selectedContractMail.getReceiverAddress());
            fieldDescription.setText(selectedContractMail.getDescription());
            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedContractMail = mailList.getSelectedContractMail();
            setFormValues();
        }
    }

    private class ContractMailList extends JXList {

        private Icon MAIL_ICON = Mainframe.getResizableIconFromSource("resource/mail.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public ContractMailList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            if (worker != null) {
                worker.cancel(true);
            }
            worker = new LoadContractMail((DefaultListModel) getModel());
            progressListener = new ContractMailProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public ContractMail getSelectedContractMail() {
            ContractMail mail = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof ContractMail) {
                mail = (ContractMail) obj;
            }
            return mail;
        }

        public void addContractMail(ContractMail mail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(mail);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateContractMail(ContractMail mail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(mail, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<ContractMail> mails) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(mails);
            filter();
        }

        public void removeSelected(ContractMail mail) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(mail);
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

                    ContractMail mail = null;

                    if (o instanceof ContractMail) {
                        mail = (ContractMail) o;
                    }

                    if (mail != null) {
                        return ContractMailList.this.MAIL_ICON;
                    }

                    return ContractMailList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadContractMail extends SwingWorker<DefaultListModel, ContractMail> {

        private DefaultListModel model;
        private Exception exception;

        public LoadContractMail(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ContractMail> chunks) {
            mainframe.stopInActiveListener();
            for (ContractMail mail : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Keluar Kontrak Nomor " + mail.getMailNumber());
                model.addElement(mail);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {

                ArrayList<ContractMail> mails = new ArrayList<ContractMail>();

                if (checkBox.isSelected()) {
                    mails = logic.getContractMail(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    mails = logic.getContractMail(mainframe.getSession());
                }

                double progress = 0.0;
                if (!mails.isEmpty()) {
                    for (int i = 0; i < mails.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / mails.size();
                        setProgress((int) progress);
                        publish(mails.get(i));
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
                JXErrorPane.showDialog(ContractMailPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ContractMailProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ContractMailProgressListener() {
        }

        ContractMailProgressListener(JProgressBar progressBar) {
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
