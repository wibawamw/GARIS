package org.motekar.project.civics.archieve.assets.procurement.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.sqlapi.ProcurementBusinessLogic;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class SignerDlg implements ActionListener {

    private JFrame frame;
    private JDialog dlg;
    private ProcurementBusinessLogic logic;
    private JXComboBox comboName = new JXComboBox();
    private JXTextField fieldNIP = new JXTextField();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private int response = JOptionPane.NO_OPTION;
    private Signer signer;
    private Integer signerType = Integer.valueOf(0);
    private Long sessionid;

    public SignerDlg(JFrame frame, Connection conn, Long sessionid, Signer signer, Integer signerType) {
        this.frame = frame;
        this.signer = signer;
        this.signerType = signerType;
        this.sessionid = sessionid;
        this.logic = new ProcurementBusinessLogic(conn);
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Penandatangan");
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

    private void loadComboSigner() {
        try {
            comboName.setEditable(true);

            SwingWorker<ArrayList<Signer>, Void> lw = new SwingWorker<ArrayList<Signer>, Void>() {

                @Override
                protected ArrayList<Signer> doInBackground() throws Exception {
                    return logic.getSigner(sessionid, signerType);
                }
            };
            lw.execute();
            final EventList<Signer> signers = GlazedLists.eventList(lw.get());

            signers.add(0, new Signer());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboName, signers);
            support.setFilterMode(TextMatcherEditor.CONTAINS);


        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public Signer getSigner() {
        return signer;
    }

    public int getResponse() {
        return response;
    }

    private Component construct() {

        loadComboSigner();

        comboName.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object obj = comboName.getSelectedItem();
                if (obj instanceof Signer) {
                    Signer sg = (Signer) obj;
                    if (!sg.getSignerName().equals("")) {
                        fieldNIP.setText(sg.getSignerNIP());
                    } else {
                        fieldNIP.setText("");
                    }
                } else {
                    fieldNIP.setText("");
                }
            }
        });

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama", cc.xy(1, 1));
        builder.add(comboName, cc.xy(3, 1));

        if (!signerType.equals(Signer.TYPE_GOVERNOR) ) {
            builder.addLabel("NIP", cc.xy(1, 3));
            builder.add(fieldNIP, cc.xy(3, 3));
        }

        return builder.getPanel();
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

    private void setData() {
        if (signer != null) {
            comboName.setSelectedItem(signer);
            fieldNIP.setText(signer.getSignerNIP());
        } else {
            comboName.getEditor().setItem(null);
            fieldNIP.setText("");
        }
    }

    private Signer getData() {
        String NIP = fieldNIP.getText();
        
        if (signerType.equals(Signer.TYPE_GOVERNOR)) {
            NIP = "";
        }

        Signer sig = null;

        Object object = comboName.getSelectedItem();
        
        if (object instanceof Signer) {
            sig = (Signer) object;
            sig.setSignerNIP(NIP);
            sig.setSignerType(signerType);
        } else if (object instanceof String) {
            String name = (String) object;
            sig = new Signer();
            sig.setSignerName(name);
            sig.setSignerNIP(NIP);
            sig.setSignerType(signerType);
        }

        return sig;
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }

    private void onOK() throws MotekarException, SQLException {
        response = JOptionPane.YES_OPTION;

        Signer newSigner = getData();

        if (newSigner == null) {
            throw new MotekarException("Isi data penandatangan dengan lengkap");
        }

        Signer tempNIP = logic.getSigner(sessionid, newSigner.getSignerName());

        if (tempNIP != null) {
            signer = logic.updateSigner(sessionid, tempNIP, newSigner);
        } else {
            signer = logic.insertSigner(sessionid, newSigner);
        }


        dlg.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            try {
                onOK();
            } catch (MotekarException ex) {
                CustomOptionDialog.showDialog(frame, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                        null, "Error", ex, Level.ALL, null);
                JXErrorPane.showDialog(frame, info);
            }

        } else if (source == btCancel) {
            dlg.dispose();
        }
    }
}
