package org.motekar.project.civics.archieve.assets.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.utils.misc.ComboBoxPanel;
import org.motekar.project.civics.archieve.utils.misc.ComboBoxPanel2;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsCategoryChooserDlg {

    private JFrame frame;
    private JDialog dlg;
    //
    private ComboBoxPanel comboBoxPanel;
    private ComboBoxPanel2 comboBoxPanel2;
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private int response = JOptionPane.NO_OPTION;
    
    public ItemsCategoryChooserDlg(JFrame frame, Long session, Connection conn, Integer categoryType, ItemsCategory parentCategory,boolean viewFullName) {
        this.frame = frame;
        if (categoryType != null) {
            this.comboBoxPanel = new ComboBoxPanel(categoryType, conn, session, parentCategory,viewFullName);
            this.comboBoxPanel2 = null;
        } else {
            this.comboBoxPanel = null;
            this.comboBoxPanel2 = new ComboBoxPanel2(conn, session, parentCategory,viewFullName);

        }
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Bidang Barang");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(600, 350));
        dlg.setModal(true);
        dlg.setResizable(false);


        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private Component construct() {

        btOK.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                response = JOptionPane.YES_OPTION;
                ItemsCategoryChooserDlg.this.dlg.dispose();
            }
        });

        btCancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsCategoryChooserDlg.this.dlg.dispose();
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
                "fill:default:grow",
                "fill:default:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        if (comboBoxPanel != null) {
            builder.add(comboBoxPanel, cc.xy(1, 1));
        } else {
            builder.add(comboBoxPanel2, cc.xy(1, 1));
        }


        return builder.getPanel();
    }

    private Component createButtonPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RichTooltip okTooltip = new RichTooltip();
        okTooltip.setTitle("Pilih");
        btOK.setActionRichTooltip(okTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batal");

        btCancel.setActionRichTooltip(cancelTooltip);

        panel.add(btOK);
        panel.add(btCancel);

        return panel;
    }

    public int getResponse() {
        return response;
    }

    public ItemsCategory getSelectedItemsCategory() {
        if (comboBoxPanel != null) {
            return comboBoxPanel.getSelectedItemsCategory();
        }
        return comboBoxPanel2.getSelectedItemsCategory();
    }

    public ArrayList<ItemsCategory> getSelectedItemsCategorys() {
        if (comboBoxPanel != null) {
            return comboBoxPanel.getSelectedItemsCategorys();
        }
        return comboBoxPanel2.getSelectedItemsCategorys();
    }
    
    public ItemsCategory getRootCategory() {
        if (comboBoxPanel != null) {
            return comboBoxPanel.getRootCategory();
        }
        return comboBoxPanel2.getRootCategory();
    }
}
