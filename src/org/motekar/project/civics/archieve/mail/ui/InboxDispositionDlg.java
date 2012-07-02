package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.mail.objects.InboxDisposition;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class InboxDispositionDlg implements ActionListener {

    private ArchieveMainframe mainframe;
    private MasterBusinessLogic mLogic;
    private JXComboBox comboDivision = new JXComboBox();
    private JXDatePicker fieldReceivedDate = new JXDatePicker();
    private JXComboBox comboStatus = new JXComboBox();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private int response = JOptionPane.NO_OPTION;
    private InboxDisposition disposition = null;
    private InboxDisposition oldDisposition = null;
    private StringBuilder errorString = new StringBuilder();

    public InboxDispositionDlg(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
    }

    public InboxDispositionDlg(ArchieveMainframe mainframe, InboxDisposition oldDisposition) {
        this.mainframe = mainframe;
        this.oldDisposition = oldDisposition;
        mLogic = new MasterBusinessLogic(mainframe.getConnection());
    }

    public void showDialog() {
        dlg = new JDialog(mainframe);

        dlg.setTitle("Input Disposisi");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(450, 250));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }

    public int getResponse() {
        return response;
    }

    private Component construct() {

        loadComboDivision();
        loadComboStatus();

        setInboxDisposition();

        comboDivision.setEditable(true);
        comboStatus.setEditable(true);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createInputPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JXPanel createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();
        okTooltip.setTitle("Pilih");
        btOK.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");

        btCancel.setActionRichTooltip(cancelTooltip);

        btOK.addActionListener(this);
        btCancel.addActionListener(this);

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    private JPanel createInputPanel() {
        FormLayout lm = new FormLayout(
                "right:pref,5px,fill:default:grow,10px",
                "pref,5px,pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Penerima", cc.xy(1, 1));
        builder.add(comboDivision, cc.xy(3, 1));

        builder.addLabel("Tanggal Terima", cc.xy(1, 3));
        builder.add(fieldReceivedDate, cc.xy(3, 3));

        builder.addLabel("Status", cc.xy(1, 5));
        builder.add(comboStatus, cc.xy(3, 5));

        return builder.getPanel();
    }

    private String generateUnitModifier(Unit unit) {
        StringBuilder query = new StringBuilder();

        if (unit != null) {
            query.append(" where unit = ").append(unit.getIndex());
        }

        return query.toString();
    }

    private void loadComboDivision() {
        try {
            ArrayList<Division> divisions = new ArrayList<Division>();

            UserGroup userGroup = mainframe.getUserGroup();
            if (userGroup.getGroupName().equals("Administrator")) {
                divisions = mLogic.getDivision(mainframe.getSession(), false, "");
            } else {
                Unit units = mainframe.getUnit();
                divisions = mLogic.getDivision(mainframe.getSession(), false, generateUnitModifier(units));
            }

            divisions.add(0, new Division());
            comboDivision.setModel(new ListComboBoxModel<Division>(divisions));
            AutoCompleteDecorator.decorate(comboDivision);

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboStatus() {
        ArrayList<String> status = InboxDisposition.mailStatusAsList();

        comboStatus.setModel(new ListComboBoxModel<String>(status));
        AutoCompleteDecorator.decorate(comboStatus);

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            dlg.dispose();
        }
    }

    public InboxDisposition getDisposition() {
        return disposition;
    }

    private void onOK() {
        try {
            disposition = getInboxDisposition();
            response = JOptionPane.YES_OPTION;
            dlg.dispose();
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(dlg, info);
        }
    }

    private void setInboxDisposition() {
        if (oldDisposition != null) {
            comboDivision.setSelectedItem(oldDisposition.getDivision());
            fieldReceivedDate.setDate(oldDisposition.getReceiveDate());
            comboStatus.setSelectedIndex(oldDisposition.getStatus());
        }
    }

    private InboxDisposition getInboxDisposition() throws MotekarException {
        errorString = new StringBuilder();

        Division division = null;

        Object obj = comboDivision.getSelectedItem();

        if (obj instanceof Division) {
            division = (Division) obj;
        }

        if (division == null || division.equals(new Division())) {
            errorString.append("<br>- Bidang/Bagian</br>");
        }

        Date receiveDate = fieldReceivedDate.getDate();

        if (receiveDate == null) {
            errorString.append("<br>- Tanggal Terima</br>");
        }

        Integer status = comboStatus.getSelectedIndex();

        if (status < 0) {
            errorString.append("<br>- Status</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        InboxDisposition dispo = new InboxDisposition();
        dispo.setDivision(division);
        dispo.setReceiveDate(receiveDate);
        dispo.setStatus(status);

        return dispo;
    }
}
