package org.motekar.project.civics.archieve.utils.misc;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXComboBox;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ComboBoxPanel2 extends JPanel {

    private static final String PREF = "pref";
    private static final String FIVE_PX = "5px";
    private static final String FILL_DEFAULT_GROW = "fill:default:grow";
    private DefaultFormBuilder builder;
    private List<JXComboBox> comboArray = new ArrayList<JXComboBox>();
    private ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();
    private CellConstraints cc = new CellConstraints();
    //
    private int row = 1;
    //
    private Long session = null;
    private AssetMasterBusinessLogic logic;
    //
    private ItemsCategory parentCategory = null;
    private boolean viewFullName = false;

    public ComboBoxPanel2(Connection conn, Long session, ItemsCategory parentCategory,boolean viewFullName) {
        this.session = session;
        this.viewFullName = viewFullName;
        this.logic = new AssetMasterBusinessLogic(conn);
        this.parentCategory = parentCategory;
        construct();
    }

    public void addComponent(JXComboBox comboBox) {
        comboArray.add(comboBox);
        builder.appendRow(FIVE_PX);
        builder.appendRow(PREF);

        builder.add(comboBox, cc.xy(3, row));
        row += 2;

        JPanel panel = builder.getPanel();

        panel.repaint();
        panel.revalidate();
    }

    public void removeLastComponent() {
        JPanel panel = builder.getPanel();
        int size = comboArray.size();
        int isize = itemsCategorys.size();

        panel.remove(comboArray.get(size - 1));
        comboArray.remove(size - 1);
        itemsCategorys.remove(isize - 1);
        builder.getLayout().removeRow(row);
        row--;
        builder.getLayout().removeRow(row);
        row--;


        panel.repaint();
        panel.revalidate();
    }

    public void removeLastComponentToIndex(Integer index) {

        int size = comboArray.size();

        for (int i = size - 1; i >= 0; i--) {
            if (i == index) {
                break;
            } else {
                removeLastComponent();
            }
        }
    }

    public void removeComponent(int fromIndex) {

        JPanel panel = builder.getPanel();

        if (!comboArray.isEmpty()) {
            int size = comboArray.size();

            for (int i = size - 1; i >= 0; i--) {
                if (fromIndex == i) {
                    break;
                } else {
                    panel.remove(comboArray.get(i));
                    builder.getLayout().removeRow(row);
                    row--;
                    builder.getLayout().removeRow(row);
                    row--;
                }
            }


            panel.repaint();
            panel.revalidate();
        }
    }

    private void construct() {
        this.setLayout(new BorderLayout());
        this.add(createMainPanel(), BorderLayout.CENTER);
    }

    private Component createMainPanel() {
        FormLayout lm = new FormLayout(PREF + "," + FIVE_PX + "," + FILL_DEFAULT_GROW);
        builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.appendRow(PREF);

        builder.addLabel("Bidang Barang", cc.xy(1, row));

        addComponent(createComboBox());

        return builder.getPanel();
    }

    private JXComboBox createComboBox() {
        JXComboBox comboBox = new JXComboBox();

        ArrayList<ItemsCategory> iCategorys = loadData(null);
        if (!iCategorys.isEmpty()) {
            loadComboCategory(comboBox, iCategorys);
        }

        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JXComboBox parentCombo = (JXComboBox) e.getSource();
                    removeLastComponentToIndex(comboArray.indexOf(parentCombo));
                    itemsCategorys.add(getCategoryFormCombo(parentCombo));
                    JXComboBox comboBox = createComboBox(parentCombo);
                    if (comboBox.getItemCount() > 0) {
                        addComponent(comboBox);
                    }
                }
            }
        });

        return comboBox;
    }

    private JXComboBox createComboBox(JXComboBox parentCombo) {
        JXComboBox comboBox = new JXComboBox();

        ArrayList<ItemsCategory> iCategorys = loadData(parentCombo);
        if (!iCategorys.isEmpty()) {
            loadComboCategory(comboBox, iCategorys);
        }

        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JXComboBox parentCombo = (JXComboBox) e.getSource();
                    removeLastComponentToIndex(comboArray.indexOf(parentCombo));
                    itemsCategorys.add(getCategoryFormCombo(parentCombo));
                    JXComboBox comboBox = createComboBox(parentCombo);
                    if (comboBox.getItemCount() > 0) {
                        addComponent(comboBox);
                    }
                }
            }
        });

        return comboBox;
    }

    private ItemsCategory getCategoryFormCombo(JXComboBox comboBox) {
        ItemsCategory category = null;

        Object obj = comboBox.getSelectedItem();
        if (obj instanceof ItemsCategory) {
            category = (ItemsCategory) obj;
        }

        return category;
    }

    private ArrayList<ItemsCategory> loadData(JXComboBox parentCombo) {
        ArrayList<ItemsCategory> iCategorys = new ArrayList<ItemsCategory>();
        if (parentCombo == null) {
            try {
                iCategorys = logic.getItemsCategory(session, ItemsCategory.ROOT_PARENT_INDEX);
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            ItemsCategory category = null;
            Object obj = parentCombo.getSelectedItem();
            if (obj instanceof ItemsCategory) {
                category = (ItemsCategory) obj;
            }

            if (category != null) {
                try {
                    iCategorys = logic.getItemsCategory(session, category.getTypes(), category.getIndex());
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return iCategorys;
    }

    private void loadComboCategory(JXComboBox childCombo, ArrayList<ItemsCategory> icategory) {
        final EventList<ItemsCategory> categorys = GlazedLists.eventList(icategory);

        AutoCompleteSupport support = AutoCompleteSupport.install(childCombo, categorys);
        support.setFilterMode(TextMatcherEditor.CONTAINS);

    }

    public ItemsCategory getSelectedItemsCategory() {
        ItemsCategory itemsCategory = null;

        if (!itemsCategorys.isEmpty()) {
            if (viewFullName) {
                int size = itemsCategorys.size();
                itemsCategory = new ItemsCategory(itemsCategorys.get(size - 1));
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        str.append(itemsCategorys.get(i).getCategoryName());
                    } else {
                        str.append(itemsCategorys.get(i).getCategoryName()).append(">");
                    }
                }

                if (str.length() > 0) {
                    itemsCategory.setCategoryName(str.toString());
                }
            } else {
                int size = itemsCategorys.size();
                
                if (size > 2) {
                    itemsCategory = new ItemsCategory(itemsCategorys.get(size - 1));
                    ItemsCategory tempCategory = new ItemsCategory(itemsCategorys.get(1));
                    ItemsCategory tempCategory2 = new ItemsCategory(itemsCategorys.get(size - 2));
                    
                    StringBuilder str = new StringBuilder();
                    
                    str.append(tempCategory.getCategoryName());
                    str.append(">");
                    str.append("...");
                    str.append(">");
                    str.append(tempCategory2.getCategoryName());
                    str.append(">");
                    str.append(itemsCategory.getCategoryName());
                    
                    itemsCategory.setCategoryName(str.toString());
                    
                } else {
                    itemsCategory = new ItemsCategory(itemsCategorys.get(size - 1));
                }
                
            }
        }

        return itemsCategory;
    }
    
    public ItemsCategory getRootCategory() {
        if (!itemsCategorys.isEmpty()) {
            return itemsCategorys.get(0);
        }
        return null;
    }

    public ArrayList<ItemsCategory> getSelectedItemsCategorys() {
        return itemsCategorys;
    }
}
