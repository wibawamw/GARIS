package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
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
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.Employee;
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
public class EmployeeNonPNSPanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private EmployeeList employeeList = new EmployeeList();
    //
    private JXTextField fieldName = new JXTextField();
    private JXTextField fieldBirthPlace = new JXTextField();
    private JXDatePicker fieldBirthDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXComboBox comboSex = new JXComboBox();
    private JXComboBox comboReligion = new JXComboBox();
    private JXComboBox comboWorkforce = new JXComboBox();
    private JXComboBox comboEducation = new JXComboBox();
    private JXTextField fieldDepartment = new JXTextField();
    private JXDatePicker fieldEntryDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldStartDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXDatePicker fieldEndDate = new JXDatePicker(new Locale("in", "ID", "ID"));
    private JXTextArea fieldDescription = new JXTextArea();
    /**
     * 
     */
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_edit.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/business_user_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private Employee selectedEmployee = null;
    private StringBuilder errorString = new StringBuilder();
    private LoadEmployee worker;
    private EmployeeProgressListener progressListener;

    public EmployeeNonPNSPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        employeeList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Pegawai");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);
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
                + "Fasilitas Pegawai untuk pengisian data-data pegawai yang ada di suatu dinas\n\n"
                + "Tambah Pegawai\n"
                + "Untuk menambah Pegawai klik tombol paling kiri "
                + "kemudian isi data Pegawai baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Pegawai\n"
                + "Untuk merubah Pegawai klik tombol kedua dari kiri "
                + "kemudian ubah data Pegawai, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Pegawai\n"
                + "Untuk menghapus Pegawai klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Pegawai "
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
        task.setTitle("Tambah / Edit / Hapus Pegawai");
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
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,pref,fill:default:grow,5px,10px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();


        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17}});
        
        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);
        
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama", cc.xy(1, 1));
        builder.add(fieldName, cc.xy(3, 1));

        builder.addLabel("Jenis Kelamin", cc.xy(1, 3));
        builder.add(comboSex, cc.xy(3, 3));

        builder.addLabel("Tempat Lahir", cc.xy(1, 5));
        builder.add(fieldBirthPlace, cc.xy(3, 5));

        builder.addLabel("Tanggal Lahir", cc.xy(1, 7));
        builder.add(fieldBirthDate, cc.xy(3, 7));

        builder.addSeparator("Pendidikan Terakhir", cc.xy(1, 9));
        builder.add(comboEducation, cc.xy(3, 9));

        builder.addLabel("Jurusan", cc.xy(1, 11));
        builder.add(fieldDepartment, cc.xy(3, 11));
        
        builder.addLabel("Jenis Ketenagaan", cc.xy(1, 13));
        builder.add(comboWorkforce, cc.xy(3, 13));

        builder.addLabel("Agama", cc.xy(1, 15));
        builder.add(comboReligion, cc.xy(3, 15));

        builder.addLabel("TMT Masuk", cc.xy(1, 17));
        builder.add(fieldEntryDate, cc.xy(3, 17));

        builder.addLabel("TMT SK Awal", cc.xy(1, 19));
        builder.add(fieldStartDate, cc.xy(3, 19));

        builder.addLabel("TMT SK Akhir", cc.xy(1, 21));
        builder.add(fieldEndDate, cc.xy(3, 21));

        builder.addLabel("Keterangan", cc.xy(1, 23));
        builder.add(scPane, cc.xywh(3, 23,1,2));

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
        addTooltip.setTitle("Tambah Pegawai");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Pegawai");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Pegawai");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Pegawai");

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

        loadSex();
        loadReligion();
        loadWorkforce();
        loadEducation();

        comboSex.setEditable(true);
        comboReligion.setEditable(true);
        comboWorkforce.setEditable(true);

        fieldBirthDate.setFormats("dd/MM/yyyy");
        fieldEntryDate.setFormats("dd/MM/yyyy");
        fieldStartDate.setFormats("dd/MM/yyyy");
        fieldEndDate.setFormats("dd/MM/yyyy");

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

        fieldDescription.setWrapStyleWord(true);
        fieldDescription.setLineWrap(true);

        employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeList.setAutoCreateRowSorter(true);
        employeeList.addListSelectionListener(this);

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
        employeeList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldName.setEnabled(iseditable);
        fieldBirthPlace.setEnabled(iseditable);
        fieldBirthDate.setEnabled(iseditable);
        comboSex.setEnabled(iseditable);
        comboReligion.setEnabled(iseditable);
        comboWorkforce.setEnabled(iseditable);
        fieldDescription.setEnabled(iseditable);
        fieldSearch.setEnabled(!iseditable);
        employeeList.setEnabled(!iseditable);

        comboEducation.setEnabled(iseditable);
        fieldDepartment.setEnabled(iseditable);
        fieldEntryDate.setEnabled(iseditable);
        fieldStartDate.setEnabled(iseditable);
        fieldEndDate.setEnabled(iseditable);

    }

    private void clearForm() {
        fieldName.setText("");
        fieldBirthPlace.setText("");
        fieldBirthDate.setDate(null);
        comboSex.setSelectedIndex(0);
        comboReligion.setSelectedIndex(0);
        comboWorkforce.setSelectedIndex(0);
        fieldDescription.setText("");

        comboEducation.setSelectedIndex(0);
        fieldDepartment.setText("");
        fieldEntryDate.setDate(null);
        fieldStartDate.setDate(null);
        fieldEndDate.setDate(null);

        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
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

    private void loadSex() {
        comboSex.removeAllItems();
        comboSex.setModel(new ListComboBoxModel<String>(Employee.sexAsList()));
        AutoCompleteDecorator.decorate(comboSex);
    }

    private void loadReligion() {
        comboReligion.removeAllItems();
        comboReligion.setModel(new ListComboBoxModel<String>(Employee.religionAsList()));
        AutoCompleteDecorator.decorate(comboReligion);
    }

    private void loadWorkforce() {
        comboWorkforce.removeAllItems();
        comboWorkforce.setModel(new ListComboBoxModel<String>(Employee.workforceAsList()));
        AutoCompleteDecorator.decorate(comboWorkforce);
    }
    

    private void loadEducation() {
        comboEducation.removeAllItems();
        comboEducation.setModel(new ListComboBoxModel<String>(Employee.educationAsList()));
        AutoCompleteDecorator.decorate(comboEducation);
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

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Pegawai");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Pegawai");
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

                boolean inexp = logic.getEmployeeInExpeditionCheque(mainframe.getSession(), selectedEmployee.getIndex());

                if (!inexp) {
                    logic.deleteEmployee(mainframe.getSession(), selectedEmployee.getIndex());
                    employeeList.removeSelected(selectedEmployee);
                    clearForm();
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<html>Pegawai dengan NIP ").
                            append("<b>").
                            append(selectedEmployee.getNip()).
                            append("</b>").
                            append(" tidak boleh dihapus karena sudah dipakai di Kwitansi Pembayaran");
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
            Employee newEmployee = getEmployee();

            if (isnew) {
                newEmployee = logic.insertEmployee(mainframe.getSession(), newEmployee);
                isnew = false;
                iseditable = false;
                employeeList.addEmployee(newEmployee);
                selectedEmployee = newEmployee;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newEmployee = logic.updateEmployee(mainframe.getSession(), selectedEmployee, newEmployee);
                isedit = false;
                iseditable = false;
                employeeList.updateEmployee(newEmployee);
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
        if (employeeList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    private void setFormValues() {
        if (selectedEmployee != null) {
            try {
                selectedEmployee = logic.getCompleteEmployee(mainframe.getSession(), selectedEmployee);
                fieldName.setText(selectedEmployee.getName());
                fieldBirthPlace.setText(selectedEmployee.getBirthPlace());
                fieldBirthDate.setDate(selectedEmployee.getBirthDate());
                comboSex.setSelectedIndex(selectedEmployee.getSex());
                comboReligion.setSelectedIndex(selectedEmployee.getReligion());
                comboWorkforce.setSelectedIndex(selectedEmployee.getWorkforce());
                fieldDescription.setText(selectedEmployee.getPositionNotes());

                comboEducation.setSelectedIndex(selectedEmployee.getEducation());
                fieldDepartment.setText(selectedEmployee.getDepartment());
                fieldEntryDate.setDate(selectedEmployee.getCpnsTMT());
                fieldStartDate.setDate(selectedEmployee.getPnsTMT());
                fieldEndDate.setDate(selectedEmployee.getGradeTMT());

                setButtonState("Save");
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat memuat data ",
                        null, "Error", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private Employee getEmployee() throws MotekarException {
        errorString = new StringBuilder();


        String name = fieldName.getText();
        Integer sex = comboSex.getSelectedIndex();
        Integer religion = comboReligion.getSelectedIndex();
        Integer workforce = comboWorkforce.getSelectedIndex();

        if (name.equals("")) {
            errorString.append("<br>- Nama</br>");
        }

        if (sex <= 0) {
            errorString.append("<br>- Jenis Kelamin</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }


        Employee employee = new Employee();
        employee.setName(name);
        employee.setBirthPlace(fieldBirthPlace.getText());
        employee.setBirthDate(fieldBirthDate.getDate());
        employee.setSex(sex);
        employee.setReligion(religion);
        employee.setWorkforce(workforce);
        employee.setPositionNotes(fieldDescription.getText());
        employee.setNonEmployee(true);


        employee.setEducation(comboEducation.getSelectedIndex());
        employee.setDepartment(fieldDepartment.getText());

        if (employee.getEselon() == -1) {
            employee.setEselon(Integer.valueOf(0));
        }

        if (employee.getMarital() == -1) {
            employee.setMarital(Integer.valueOf(0));
        }

        if (employee.getChildren() == -1) {
            employee.setChildren(Integer.valueOf(0));
        }

        if (employee.getEducation() == -1) {
            employee.setEducation(Integer.valueOf(0));
        }


        employee.setCpnsTMT(fieldEntryDate.getDate());
        employee.setPnsTMT(fieldStartDate.getDate());
        employee.setGradeTMT(fieldEndDate.getDate());


        return employee;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedEmployee = employeeList.getSelectedEmployee();
            setFormValues();
        }
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldName);
        comp.add(comboSex.getEditor().getEditorComponent());
        comp.add(comboReligion.getEditor().getEditorComponent());
        comp.add(comboWorkforce.getEditor().getEditorComponent());
        comp.add(fieldBirthPlace);
        comp.add(fieldBirthDate.getEditor());

        comp.add(fieldEntryDate.getEditor());
        comp.add(fieldStartDate.getEditor());

        comp.add(fieldEndDate.getEditor());
        comp.add(fieldDescription);
        comp.add(comboEducation.getEditor().getEditorComponent());
        comp.add(fieldDepartment);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private class EmployeeList extends JXList {

        private Icon MALE_ICON = Mainframe.getResizableIconFromSource("resource/male_user.png", new Dimension(36, 36));
        private Icon FEMALE_ICON = Mainframe.getResizableIconFromSource("resource/female_user.png", new Dimension(36, 36));
        //
        private Icon GOVERNOR_ICON = Mainframe.getResizableIconFromSource("resource/business_user.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public EmployeeList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadEmployee((DefaultListModel) getModel());
            progressListener = new EmployeeProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Employee getSelectedEmployee() {
            Employee employee = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Employee) {
                employee = (Employee) obj;
            }
            return employee;
        }

        public void addEmployee(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(employee);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateEmployee(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(employee, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<Employee> employees) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(employees);
            filter();
        }

        public void removeSelected(Employee employee) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(employee);
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

                    Employee employee = null;

                    if (o instanceof Employee) {
                        employee = (Employee) o;
                    }

                    if (employee != null) {
                        if (employee.isGorvernor()) {
                            return EmployeeList.this.GOVERNOR_ICON;
                        } else {
                            if (employee.getSex() == Employee.MALE) {
                                return EmployeeList.this.MALE_ICON;
                            } else {
                                return EmployeeList.this.FEMALE_ICON;
                            }
                        }
                    }

                    return EmployeeList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadEmployee extends SwingWorker<DefaultListModel, Employee> {

        private DefaultListModel model;
        private Exception exception;

        public LoadEmployee(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Employee> chunks) {
            mainframe.stopInActiveListener();
            for (Employee employee : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Pegawai " + employee.getName());
                model.addElement(employee);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Employee> employees = logic.getEmployee(mainframe.getSession(), true);

                double progress = 0.0;
                if (!employees.isEmpty()) {
                    for (int i = 0; i < employees.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / employees.size();
                        setProgress((int) progress);
                        publish(employees.get(i));
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
                JXErrorPane.showDialog(EmployeeNonPNSPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class EmployeeProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private EmployeeProgressListener() {
        }

        EmployeeProgressListener(JProgressBar progressBar) {
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
