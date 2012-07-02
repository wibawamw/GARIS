package org.motekar.project.civics.archieve.assets.inventory.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.util.user.misc.MotekarException;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class InsertRoomDialog {

    private JFrame frame;
    private JDialog dlg;
    private AssetMasterBusinessLogic logic;
    //
    private JXTextField fieldCode = new JXTextField();
    private JXTextField fieldName = new JXTextField();
    //
    private JXButton btSave = new JXButton("Simpan");
    private JXButton btCancel = new JXButton("Batal");
    //
    private int response = JOptionPane.NO_OPTION;
    private Long session = null;
    private Room room = null;

    public InsertRoomDialog(JFrame frame, Connection conn, Long session) {
        this.frame = frame;
        this.session = session;
        this.logic = new AssetMasterBusinessLogic(conn);
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Tambah Ruangan");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 200));
        dlg.setModal(true);
        dlg.setResizable(false);


        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private Component construct() {
        btSave.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    room = getRooms();

                    if (room != null) {
                        room = logic.insertRoom(session, room);
                        response = JOptionPane.YES_OPTION;
                        InsertRoomDialog.this.dlg.dispose();
                    }

                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                    ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                            null, "Error", ex, Level.ALL, null);
                    JXErrorPane.showDialog(frame, info);
                } catch (MotekarException ex) {
                    CustomOptionDialog.showDialog(frame, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btCancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                InsertRoomDialog.this.dlg.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);


        return panel;
    }

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,5px,fill:default:grow",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Kode Ruangan", cc.xy(1, 1));
        builder.add(fieldCode, cc.xy(3, 1));

        builder.addLabel("Nama Ruangan", cc.xy(1, 3));
        builder.add(fieldName, cc.xy(3, 3));

        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        return panel;
    }

    private Component createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(btSave);
        panel.add(btCancel);

        return panel;
    }

    public int getResponse() {
        return response;
    }

    private Room getRooms() throws MotekarException {

        StringBuilder errorString = new StringBuilder();

        String code = fieldCode.getText();
        String name = fieldName.getText();

        if (code.equals("")) {
            errorString.append("<br>- Kode Ruangan</br>");
        }

        if (name.equals("")) {
            errorString.append("<br>- Nama Ruangan</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        Room rooms = new Room();

        rooms.setRoomCode(code);
        rooms.setRoomName(name);

        return rooms;
    }

    public Room getRoom() {
        return room;
    }
}
