package org.motekar.project.civics.archieve.expedition.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.expedition.objects.Approval;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ApprovalDlg implements ActionListener {

    private JFrame frame;
    private JXTextField fieldPlace = new JXTextField();
    private JXDatePicker fieldDate = new JXDatePicker();
    private Approval approval = null;
    private int response = JOptionPane.NO_OPTION;
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private StringBuilder errorString = new StringBuilder();

    public ApprovalDlg(JFrame frame, Approval approval) {
        this.frame = frame;
        this.approval = approval;
    }

    public Approval getApproval() {
        return approval;
    }

    public int getResponse() {
        return response;
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Tempat & Tanggal Pengesahan");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 200));
        dlg.setModal(true);
        dlg.setResizable(false);

        setData();
        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        fieldDate.setFormats("dd/MM/yyyy");

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Tempat", cc.xy(1, 1));
        builder.add(fieldPlace, cc.xy(3, 1));

        builder.addLabel("Tanggal", cc.xy(1, 3));
        builder.add(fieldDate, cc.xy(3, 3));

        return builder.getPanel();
    }

    private Component construct() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
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

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            dlg.dispose();
        }
    }

    private void setData() {
        if (approval != null) {
            fieldPlace.setText(approval.getPlace());
            fieldDate.setDate(approval.getDate());
        }
    }

    private Approval getData() throws MotekarException {
        errorString = new StringBuilder();

        String place = fieldPlace.getText();

        if (place.equals("")) {
            errorString.append("<br>- Tempat</br>");
        }

        Date date = fieldDate.getDate();

        if (date == null) {
            errorString.append("<br>- Tanggal</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Approval appr = new Approval();
        appr.setPlace(place);
        appr.setDate(date);

        return appr;
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }

    private void onOK() {
        try {
            response = JOptionPane.YES_OPTION;
            approval = getData();
            dlg.dispose();
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat mengambil data",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(dlg, info);
        }
    }
}
