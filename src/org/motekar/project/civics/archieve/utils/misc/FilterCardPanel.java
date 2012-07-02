package org.motekar.project.civics.archieve.utils.misc;

import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

/**
 *
 * @author Muhamad Wibawa
 */
public class FilterCardPanel extends JXPanel {
    
    public static final String STRING_PANEL = "String Panel";
    public static final String NUMBER_PANEL = "Number Panel";
    public static final String DATE_PANEL = "Date Panel";
    public static final String YEAR_PANEL = "Year Panel";
    public static final String COMBO_PANEL = "Combo Panel";
    public static final String COMBO_LONG_PANEL = "Long Panel";
    
    private JXTextField fieldString = new JXTextField();
    private JXFormattedTextField fieldNumber;
    private JXDatePicker fieldDate = new JXDatePicker();
    private JYearChooser fieldYear = new JYearChooser();
    private JXComboBox comboBox = new JXComboBox();
    private JXComboBox comboBox2 = new JXComboBox();
    
    private String cardState = "";
    
    public FilterCardPanel() {
        contruct();
    }

    private void contruct() {
        this.setLayout(new CardLayout());
        this.add(createStringPanel(), STRING_PANEL);
        this.add(createNumberPanel(), NUMBER_PANEL);
        this.add(createDatePanel(), DATE_PANEL);
        this.add(createYearPanel(), YEAR_PANEL);
        this.add(createComboPanel(), COMBO_PANEL);
        this.add(createComboLongPanel(), COMBO_LONG_PANEL);
        
        ((CardLayout) this.getLayout()).show(this, STRING_PANEL);
    }
    
    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

        fieldNumber = new JXFormattedTextField();
        fieldNumber.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldNumber.setHorizontalAlignment(JLabel.LEFT);
    }
    
    private JXPanel createStringPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(fieldString,BorderLayout.CENTER);
        
        return panel;
    }
    
    private JXPanel createNumberPanel() {
        
        contructNumberField();
        
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(fieldNumber,BorderLayout.CENTER);
        
        return panel;
    }
    
    private JXPanel createDatePanel() {
        
        fieldDate.setFormats("dd/MM/yyyy");
        
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(fieldDate,BorderLayout.CENTER);
        
        return panel;
    }
    
    private JXPanel createYearPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(fieldYear,BorderLayout.CENTER);
        
        return panel;
    }
    
    private JXPanel createComboPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(comboBox,BorderLayout.CENTER);
        
        return panel;
    }
    
    private JXPanel createComboLongPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.add(comboBox2,BorderLayout.CENTER);
        
        return panel;
    }
    
    public void showPanel(String cardState) {
        this.cardState = cardState;
        clearAll();
        ((CardLayout) this.getLayout()).show(this, cardState);
    }
    
    public void showComboPanel(ArrayList<String> stringCombo) {
        this.cardState = COMBO_PANEL;
        reloadCombo(stringCombo);
        clearAll();
        ((CardLayout) this.getLayout()).show(this, COMBO_PANEL);
    }
    
    public void showComboLongPanel(ArrayList objectCombo) {
        this.cardState = COMBO_LONG_PANEL;
        reloadComboObject(objectCombo);
        clearAll();
        ((CardLayout) this.getLayout()).show(this, COMBO_LONG_PANEL);
    }
    
    private void reloadCombo(ArrayList<String> stringCombo) {
        comboBox.removeAllItems();
        if (!stringCombo.isEmpty()) {
            for (String string : stringCombo) {
                comboBox.addItem(string);
            }
        }
    }
    
    private void reloadComboObject(ArrayList objectCombo) {
        comboBox2.removeAllItems();
        if (!objectCombo.isEmpty()) {
            for (Object object : objectCombo) {
                comboBox2.addItem(object);
            }
        }
    }
    
    private void clearAll() {
        fieldString.setText("");
        fieldNumber.setValue(null);
        fieldDate.setDate(null);
        fieldYear.setYear(2011);
        comboBox.getEditor().setItem(null);
    }
    
    public void setPanelState(boolean enabled) {
        fieldString.setEnabled(enabled);
        fieldNumber.setEnabled(enabled);
        fieldDate.setEnabled(enabled);
        fieldYear.setEnabled(enabled);
        comboBox.setEnabled(enabled);
    }

    public String getCardState() {
        return cardState;
    }
    
    public String getStringText() {
        return fieldString.getText();
    }
    
    public BigDecimal getBigDecimal() {
        
        BigDecimal bigDecimal = BigDecimal.ZERO;
        
        Object obj = fieldNumber.getValue();
        
        if (obj instanceof Long) {
            bigDecimal = BigDecimal.valueOf((Long)obj);
        } else if (obj instanceof Integer) {
            bigDecimal = BigDecimal.valueOf(((Integer)obj).longValue());
        } else if (obj instanceof BigDecimal) {
            bigDecimal = (BigDecimal) obj;
        }
        
        return bigDecimal;
    } 
    
    public Integer getInteger() {
        
        Integer integer = Integer.valueOf(0);
        
        Object obj = fieldNumber.getValue();
        
        if (obj instanceof Long) {
            integer = Integer.valueOf(((Long)obj).intValue());
        } else if (obj instanceof Integer) {
            integer = (Integer) obj;
        } else if (obj instanceof BigDecimal) {
            integer = Integer.valueOf(((BigDecimal)obj).intValue());
        }
        
        return integer;
    }
    
    public Integer getYear() {
        return Integer.valueOf(fieldYear.getYear());
    }
    
    public Date getDate() {
        return fieldDate.getDate();
    }
    
    public String getSelectedStringCombo() {
        String text = "";
        
        Object obj = comboBox.getSelectedItem();
        if (obj instanceof String){
            text = (String) obj;
        }
        
        return text;
    }
    
    public Object getSelectedObjectCombo() {
        return comboBox2.getSelectedItem();
    }
}
