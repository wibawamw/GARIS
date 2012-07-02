package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.master.objects.BudgetSubDetailChild;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetSubDetailChildDlg implements ActionListener {

    private JFrame frame;
    private JDialog dlg;
    private JXTextField fieldDescription = new JXTextField();
    private JXComboBox comboEselon = new JXComboBox();
    private JSpinField fieldCounted = new JSpinField();
    private JXTextField fieldUnits = new JXTextField();
    private JXFormattedTextField fieldAmount;
    private JXFormattedTextField fieldTotal;
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private BudgetSubDetailChild subDetailChild = null;
    private int response = JOptionPane.NO_OPTION;

    public BudgetSubDetailChildDlg(JFrame frame) {
        this.frame = frame;
    }
    
    public BudgetSubDetailChildDlg(JFrame frame,BudgetSubDetailChild subDetailChild) {
        this.frame = frame;
        this.subDetailChild = subDetailChild;
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Rincian Kegiatan");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 270));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    private Component construct() {

        contructNumberField();
        loadEselon();

        fieldTotal.setEditable(false);

        fieldCounted.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fieldTotal.setValue(calculateTotal());
            }
        });

        fieldAmount.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fieldTotal.setValue(calculateTotal());
            }
        });

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        setBudgetSubDetailChild();

        return panel;
    }

    private BigDecimal calculateTotal() {
        BigDecimal amount = BigDecimal.ZERO;
        Object obj = fieldAmount.getValue();

        if (obj instanceof BigDecimal) {
            amount = (BigDecimal) obj;
        } else if (obj instanceof Long) {
            amount = BigDecimal.valueOf(((Long) obj).longValue());
        } else if (obj instanceof Integer) {
            amount = BigDecimal.valueOf(((Integer) obj).longValue());
        }

        BigDecimal total = BigDecimal.ZERO;
        total = total.add(amount);
        total = total.multiply(BigDecimal.valueOf(fieldCounted.getValue()));

        return total;
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,pref,5px,pref,5px,pref,5px,pref,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Uraian", cc.xy(1, 1));
        builder.add(fieldDescription, cc.xy(3, 1));
        
        builder.addLabel("Eselon", cc.xy(1, 3));
        builder.add(comboEselon, cc.xy(3, 3));

        builder.addLabel("Jumlah", cc.xy(1, 5));
        builder.add(fieldCounted, cc.xy(3, 5));

        builder.addLabel("Satuan", cc.xy(1, 7));
        builder.add(fieldUnits, cc.xy(3, 7));

        builder.addLabel("Sebesar (Rp.)", cc.xy(1, 9));
        builder.add(fieldAmount, cc.xy(3, 9));

        builder.addLabel("Total", cc.xy(1, 11));
        builder.add(fieldTotal, cc.xy(3, 11));

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

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldAmount = new JXFormattedTextField();
        fieldAmount.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldTotal = new JXFormattedTextField();
        fieldTotal.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));
    }
    
    private void loadEselon() {
        comboEselon.removeAllItems();
        comboEselon.setModel(new ListComboBoxModel<String>(BudgetSubDetailChild.eselonAsList()));
        AutoCompleteDecorator.decorate(comboEselon);
    }

    public int getResponse() {
        return response;
    }

    public BudgetSubDetailChild getSubDetailChild() {
        return subDetailChild;
    }

    private void setBudgetSubDetailChild() {
        if (subDetailChild != null) {
            fieldDescription.setText(subDetailChild.getDescription());
            comboEselon.setSelectedIndex(subDetailChild.getEselon());
            fieldCounted.setValue(subDetailChild.getCounted());
            fieldUnits.setText(subDetailChild.getUnits());
            fieldAmount.setValue(subDetailChild.getAmount());
        }
    }
    
    private BudgetSubDetailChild getBudgetSubDetailChild() throws MotekarException {
        StringBuilder errorString = new StringBuilder();


        String description = fieldDescription.getText();
        Integer eselon = comboEselon.getSelectedIndex();
        Integer counted = fieldCounted.getValue();
        String units = fieldUnits.getText();
        BigDecimal amount = BigDecimal.ZERO;
        Object obj = fieldAmount.getValue();

        if (obj instanceof BigDecimal) {
            amount = (BigDecimal) obj;
        } else if (obj instanceof Long) {
            amount = BigDecimal.valueOf(((Long) obj).longValue());
        } else if (obj instanceof Integer) {
            amount = BigDecimal.valueOf(((Integer) obj).longValue());
        }

        if (description.equals("")) {
            errorString.append("<br>- Uraian</br>");
        }

        if (eselon.equals(Integer.valueOf(0))) {
            errorString.append("<br>- Eselon</br>");
        }
        
        if (counted.equals(Integer.valueOf(0))) {
            errorString.append("<br>- Jumlah</br>");
        }

        if (units.equals("")) {
            errorString.append("<br>- Satuan</br>");
        }

        if (amount.equals(BigDecimal.ZERO)) {
            errorString.append("<br>- Sebesar (Rp.)</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        BudgetSubDetailChild child = new BudgetSubDetailChild();
        child.setDescription(description);
        child.setEselon(eselon);
        child.setCounted(counted);
        child.setUnits(units);
        child.setAmount(amount);
        return child;
    }

    private void onOK() {
        try {
            response = JOptionPane.YES_OPTION;
            subDetailChild = getBudgetSubDetailChild();
            dlg.dispose();
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(this.dlg, info);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            dlg.dispose();
        }
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }
}
