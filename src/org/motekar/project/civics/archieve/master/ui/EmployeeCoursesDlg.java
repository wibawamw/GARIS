package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.project.civics.archieve.master.objects.EmployeeCourses;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeCoursesDlg implements ActionListener {
    
    private JFrame frame;
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/disk.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    
    //
    private JXComboBox comboCoursesType = new JXComboBox();
    private JXTextField fieldCoursesName = new JXTextField();
    private JYearChooser fieldYearAttended = new JYearChooser();
    private JSpinField fieldTotalHour = new JSpinField();
    //
    private EmployeeCourses courses = null;
    private EmployeeCourses editedCourses = null;
    
    private int response = JOptionPane.NO_OPTION;
    
    public EmployeeCoursesDlg() {
        editedCourses = null;
    }
    
    public EmployeeCoursesDlg(EmployeeCourses editedCourses) {
        this.editedCourses = editedCourses;
    }
    
    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Tambah/Ubah Diklat");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 250));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }
    
    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Jenis Diklat", cc.xy(1, 1));
        builder.add(comboCoursesType, cc.xy(3, 1));
        builder.addLabel("Nama Diklat", cc.xy(1, 3));
        builder.add(fieldCoursesName, cc.xy(3, 3));
        builder.addLabel("Tahun", cc.xy(1, 5));
        builder.add(fieldYearAttended, cc.xy(3, 5));
        builder.addLabel("Jumlah Jam", cc.xy(1, 7));
        builder.add(fieldTotalHour, cc.xy(3, 7));

        return builder.getPanel();
    }

    private Component construct() {
        
        loadComboCourses();
        
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        setCourses();

        return panel;
    }
    
    private JXPanel createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();
        okTooltip.setTitle("Simpan");
        btSave.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");


        btCancel.setActionRichTooltip(cancelTooltip);

        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        panel.add(btSave);
        panel.add(btCancel);

        return panel;
    }
    
    private void loadComboCourses() {
        comboCoursesType.removeAllItems();
        comboCoursesType.setModel(new ListComboBoxModel<String>(EmployeeCourses.coursesAsList()));
        AutoCompleteDecorator.decorate(comboCoursesType);
    }

    
    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }
    
    private void setCourses() {
        if (editedCourses != null) {
            comboCoursesType.setSelectedIndex(editedCourses.getCoursesType());
            fieldCoursesName.setText(editedCourses.getCoursesName());
            fieldYearAttended.setYear(editedCourses.getYearAttended());
            fieldTotalHour.setValue(editedCourses.getTotalHour());
        }
    }
    
    private EmployeeCourses getEditedCourses() throws MotekarException {
        StringBuilder errorString = new StringBuilder();


        Integer coursesType = comboCoursesType.getSelectedIndex();
        
        String coursesName = fieldCoursesName.getText();
        Integer yearAttended = fieldYearAttended.getYear();
        Integer totalHour = fieldTotalHour.getValue();
        
        if (coursesType.equals(Integer.valueOf(0))) {
            errorString.append("<br>- Jenis Diklat</br>");
        }
        
        if (coursesName.equals("")) {
            errorString.append("<br>- Nama Diklat</br>");
        }


        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        EmployeeCourses courses = new EmployeeCourses();
        courses.setCoursesType(coursesType);
        courses.setCoursesName(coursesName);
        courses.setYearAttended(yearAttended);
        courses.setTotalHour(totalHour);

        return courses;
    }

    public int getResponse() {
        return response;
    }

    public EmployeeCourses getCourses() {
        return courses;
    }
    
    private void onSave() {
        try {
            courses = getEditedCourses();
            response = JOptionPane.YES_OPTION;
            dlg.dispose();
        } catch (MotekarException ex) {
            Exceptions.printStackTrace(ex);
            response = JOptionPane.NO_OPTION;
            CustomOptionDialog.showDialog(frame, ex.getMessage(), "Perhatian", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btSave) {
            onSave();
        } else if (source == btCancel) {
            dlg.dispose();
        }
    }
    
}
