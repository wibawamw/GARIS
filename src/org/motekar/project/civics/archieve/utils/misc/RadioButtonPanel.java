package org.motekar.project.civics.archieve.utils.misc;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jdesktop.swingx.JXTextField;

/**
 *
 * @author Muhamad Wibawa
 */
public class RadioButtonPanel extends JPanel {

    private static final String PREF = "pref";
    private static final String TEN_PX = "10px";
    private static final String FIVE_PX = "5px";
    private static final String ONE_HUNDRED_PX = "100px";
    private static final String FILL_DEFAULT_GROW = "fill:default:grow";
    private ArrayList<JRadioButton> radioButtons = new ArrayList<JRadioButton>();
    private JXTextField fieldOther = new JXTextField();
    private int row = 0;
    private int column = 0;

    public RadioButtonPanel(int row, int column) {
        this.row = row;
        this.column = column;
        construct();
    }

    public RadioButtonPanel(String[] datas, int row, int column) {
        addData(datas);
        this.row = row;
        this.column = column;
        construct();
    }

    public RadioButtonPanel(ArrayList<String> datas, int row, int column) {
        addData(datas);
        this.row = row;
        this.column = column;
        construct();
    }

    private void addData(String data) {
        JRadioButton radioButton = new JRadioButton(data);
        radioButtons.add(radioButton);
    }

    private void addData(ArrayList<String> datas) {
        if (!datas.isEmpty()) {
            for (String data : datas) {
                addData(data);
            }
        }
    }

    private void addData(String[] datas) {
        if (datas != null) {
            for (String data : datas) {
                addData(data);
            }
        }
    }

    public void removeData(String data) {
        removeAll();
        repaint();
        revalidate();

        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                if (radioButton.getText().equals(data)) {
                    radioButtons.remove(radioButton);
                    break;
                }
            }
        }

        addAlltoPanel();
    }

    public void setSelectedData(String data) {
        setAllDeselected();
        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                if (radioButton.getText().equals(data)) {
                    radioButton.setSelected(true);
                    fieldOther.setText("");
                    break;
                } else if (data.contains("Lainnya")) {
                    radioButton.setSelected(true);
                    fieldOther.setEnabled(true);
                    fieldOther.setText(seperateOtherString(data));
                    break;
                }
            }
        }
    }
    
    private String seperateOtherString(String data) {
        String sepString = "";
            
        StringTokenizer token = new StringTokenizer(data,":");
        
        while(token.hasMoreElements()) {
            sepString = token.nextToken();
        }
        
        return sepString;
    }
    
    public String getSelectedData() {
        String selectedText = "";
        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                if (radioButton.isSelected()) {
                    if (radioButton.getText().equals("Lainnya")) {
                        selectedText = radioButton.getText()+":"+fieldOther.getText();
                    } else {
                        selectedText = radioButton.getText();
                    }
                    break;
                }
            }
        }
        return selectedText;
    }

    public void setAllDeselected() {
        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                radioButton.setSelected(false);
            }
            fieldOther.setEnabled(false);
        }
    }

    public void setAllDisabled() {
        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                radioButton.setEnabled(false);
            }
            fieldOther.setEnabled(false);
        }
    }

    private void addAlltoPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        this.add(createCreateRadioPanel(),BorderLayout.CENTER);
    }

    private String createRowSpec() {

        StringBuilder builder = new StringBuilder();

        if (row > 0) {
            for (int i = 0; i < row; i++) {
                if (i == row - 1) {
                    builder.append(PREF);
                } else {
                    builder.append(PREF).
                            append(",").
                            append(TEN_PX).
                            append(",");
                }
            }
        } else {
            builder.append(PREF);
        }

        return builder.toString();
    }

    private String createColumnSpec() {

        StringBuilder builder = new StringBuilder();

        if (column > 0) {
            for (int i = 0; i < column; i++) {
                if (i == column - 1) {
                    builder.append(PREF);
                    if (column == 2) {
                        builder.append(",").
                                append(ONE_HUNDRED_PX);
                    } else {
                        builder.append(",").
                                append(FIVE_PX).
                                append(",").
                                append(FILL_DEFAULT_GROW);
                    }
                } else {
                    builder.append(PREF).
                            append(",").
                            append(TEN_PX).
                            append(",");
                }
            }
        } else {
            builder.append(PREF);
        }

        return builder.toString();
    }

    private Component createCreateRadioPanel() {
        FormLayout lm = new FormLayout(
                createColumnSpec(),
                createRowSpec());
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        int x = 1;
        int y = 1;
        
        int columned = (column * 2) - 1; 
        
        for (JRadioButton radioButton : radioButtons) {
            if (radioButton.getText().equals("Lainnya")) {
                builder.add(radioButton,cc.xy(x, y));
                builder.add(fieldOther,cc.xy(x+2, y));
            } else {
                builder.add(radioButton,cc.xy(x, y));
            }

            if (row == 0) {
                x += 2;
            } else {
                if (x < columned) {
                    x += 2;
                } else if (x >= columned) {
                    y += 2;
                    x = 1;
                } 
            }
        }


        return builder.getPanel();
    }

    private void addButtonAction() {
        if (!radioButtons.isEmpty()) {
            for (JRadioButton radioButton : radioButtons) {
                radioButton.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        Object source = e.getSource();
                        if (source instanceof JRadioButton) {
                            JRadioButton rb = (JRadioButton) source;
                            boolean selected = rb.isSelected();
                            setAllDeselected();
                            rb.setSelected(selected);
                            if (rb.getText().equals("Lainnya")) {
                                fieldOther.setEnabled(true);
                                fieldOther.requestFocus();
                                fieldOther.selectAll();
                            }
                        }
                    }
                });
            }
        }
    }

    private void construct() {
        addButtonAction();
        addAlltoPanel();
    }
}
