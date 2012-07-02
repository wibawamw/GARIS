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
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXPanel;
import org.motekar.project.civics.archieve.assets.master.objects.AssetObject;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class CustomFilterPanel extends JXPanel {

    private ArchieveMainframe mainframe;
    private AssetMasterBusinessLogic mLogic;
    //
    private JCheckBox checkBoxUnit = new JCheckBox("UPB/Sub UPB");
    private JCheckBox checkBoxUrban = new JCheckBox("Kecamatan");
    private JCheckBox checkBoxSubUrban = new JCheckBox("Kelurahan");
    private JCheckBox checkBoxCustom = new JCheckBox("Filter Berdasarkan");
    //
    private JXComboBox comboUnit = new JXComboBox();
    private JXComboBox comboUrban = new JXComboBox();
    private JXComboBox comboSubUrban = new JXComboBox();
    private JXComboBox comboCustom = new JXComboBox();
    private FilterCardPanel cardPanel = new FilterCardPanel();
    //
    private ArrayList<FilterValue> filterValues;
    //
    private boolean isUrban = true;
    private boolean isSubUrban = true;
    private boolean isCustomField = true;
    private boolean filtered = false;
    private EventList<Region> urbanRegions;

    public CustomFilterPanel(ArchieveMainframe mainframe, ArrayList<FilterValue> filterValues, boolean isUrban, boolean isSubUrban) {
        this.mainframe = mainframe;
        this.mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        this.filterValues = filterValues;
        this.isUrban = isUrban;
        this.isSubUrban = isSubUrban;
        this.isCustomField = true;
        construct();
    }

    public CustomFilterPanel(ArchieveMainframe mainframe, ArrayList<FilterValue> filterValues, boolean isUrban, boolean isSubUrban, boolean isCustomField) {
        this.mainframe = mainframe;
        this.mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        this.filterValues = filterValues;
        this.isUrban = isUrban;
        this.isSubUrban = isSubUrban;
        this.isCustomField = isCustomField;
        construct();
    }

    private void construct() {
        if (isCustomField && !filterValues.isEmpty()) {
            loadComboCustom();
        }

        comboUrban.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    reloadComboSubUrban();
                }
            }
        });

        comboUnit.setEnabled(checkBoxUnit.isSelected());
        comboUrban.setEnabled(checkBoxUrban.isSelected());
        comboSubUrban.setEnabled(checkBoxSubUrban.isSelected());
        comboCustom.setEnabled(checkBoxCustom.isSelected());
        cardPanel.setVisible(checkBoxCustom.isSelected());
        cardPanel.setPanelState(checkBoxCustom.isSelected());

        checkBoxUnit.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboUnit.setEnabled(checkBoxUnit.isSelected());
                checkFiltered();
            }
        });

        checkBoxUrban.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboUrban.setEnabled(checkBoxUrban.isSelected());
                checkFiltered();
            }
        });

        checkBoxSubUrban.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboSubUrban.setEnabled(checkBoxSubUrban.isSelected());
                checkFiltered();
            }
        });

        checkBoxCustom.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboCustom.setEnabled(checkBoxCustom.isSelected());
                cardPanel.setVisible(checkBoxCustom.isSelected());
                cardPanel.setPanelState(checkBoxCustom.isSelected());
                checkFiltered();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(constructMainPanel(), BorderLayout.CENTER);
    }

    public void setUnitEnable(boolean enable) {
        checkBoxUnit.setSelected(true);
        checkBoxUnit.setEnabled(enable);
        comboUnit.setEnabled(enable);
    }

    public void setComboUnit(Unit unit) {
        comboUnit.setSelectedItem(unit);
    }
    
    public Unit getUnit() {
        Unit unit = null;
        
        if (checkBoxUnit.isSelected()) {
            Object obj = comboUnit.getSelectedItem();
            if (obj instanceof Unit) {
                unit = (Unit) obj;
            }
        }
        
        return unit;
    }

    private void checkFiltered() {
        setFiltered(checkBoxUnit.isSelected() || checkBoxUrban.isSelected()
                || checkBoxSubUrban.isSelected() || checkBoxCustom.isSelected());
    }

    private Component constructMainPanel() {
        if (isUrban && isSubUrban) {
            if (!isSubUrban) {
                if (isCustomField) {
                    FormLayout lm = new FormLayout(
                            "fill:default,10px,pref,5px, fill:default:grow,50px",
                            "pref,5px,pref,5px,pref");
                    DefaultFormBuilder builder = new DefaultFormBuilder(lm);
                    builder.setDefaultDialogBorder();

                    CellConstraints cc = new CellConstraints();

                    builder.add(checkBoxUnit, cc.xy(1, 1));
                    builder.add(comboUnit, cc.xyw(3, 1, 3));

                    builder.add(checkBoxUrban, cc.xy(1, 3));
                    builder.add(comboUrban, cc.xyw(3, 3, 3));

                    builder.add(checkBoxCustom, cc.xy(1, 5));
                    builder.add(comboCustom, cc.xyw(3, 5, 1));
                    builder.add(cardPanel, cc.xyw(5, 5, 1));

                    return builder.getPanel();
                } else {
                    FormLayout lm = new FormLayout(
                            "fill:default,10px,pref,5px, fill:default:grow,50px",
                            "pref,5px,pref");
                    DefaultFormBuilder builder = new DefaultFormBuilder(lm);
                    builder.setDefaultDialogBorder();

                    CellConstraints cc = new CellConstraints();

                    builder.add(checkBoxUnit, cc.xy(1, 1));
                    builder.add(comboUnit, cc.xyw(3, 1, 3));

                    builder.add(checkBoxUrban, cc.xy(1, 3));
                    builder.add(comboUrban, cc.xyw(3, 3, 3));

                    return builder.getPanel();
                }


            } else {
                if (isCustomField) {
                    FormLayout lm = new FormLayout(
                            "fill:default,10px,pref,5px, fill:default:grow,50px,fill:default,10px,pref,5px, fill:default:grow,50px",
                            "pref,5px,pref");
                    DefaultFormBuilder builder = new DefaultFormBuilder(lm);
                    builder.setDefaultDialogBorder();

                    CellConstraints cc = new CellConstraints();

                    builder.add(checkBoxUnit, cc.xy(1, 1));
                    builder.add(comboUnit, cc.xyw(3, 1, 3));
                    builder.add(checkBoxSubUrban, cc.xy(7, 1));
                    builder.add(comboSubUrban, cc.xyw(9, 1, 3));

                    builder.add(checkBoxUrban, cc.xy(1, 3));
                    builder.add(comboUrban, cc.xyw(3, 3, 3));
                    builder.add(checkBoxCustom, cc.xy(7, 3));
                    builder.add(comboCustom, cc.xyw(9, 3, 1));
                    builder.add(cardPanel, cc.xyw(11, 3, 1));

                    return builder.getPanel();
                } else {
                    FormLayout lm = new FormLayout(
                            "fill:default,10px,pref,5px, fill:default:grow,50px",
                            "pref,5px,pref,5px,pref");
                    DefaultFormBuilder builder = new DefaultFormBuilder(lm);
                    builder.setDefaultDialogBorder();

                    CellConstraints cc = new CellConstraints();

                    builder.add(checkBoxUnit, cc.xy(1, 1));
                    builder.add(comboUnit, cc.xyw(3, 1, 3));

                    builder.add(checkBoxUrban, cc.xy(1, 3));
                    builder.add(comboUrban, cc.xyw(3, 3, 3));

                    builder.add(checkBoxSubUrban, cc.xy(1, 5));
                    builder.add(comboSubUrban, cc.xyw(3, 5, 3));

                    return builder.getPanel();
                }


            }
        } else if (isCustomField) {
            FormLayout lm = new FormLayout(
                    "fill:default,10px,pref,5px, fill:default:grow,50px",
                    "pref,5px,pref");
            DefaultFormBuilder builder = new DefaultFormBuilder(lm);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();

            builder.add(checkBoxUnit, cc.xy(1, 1));
            builder.add(comboUnit, cc.xyw(3, 1, 3));

            builder.add(checkBoxCustom, cc.xy(1, 3));
            builder.add(comboCustom, cc.xyw(3, 3, 1));
            builder.add(cardPanel, cc.xyw(5, 3, 1));

            return builder.getPanel();
        } else {
            FormLayout lm = new FormLayout(
                    "fill:default,10px,pref,5px, fill:default:grow,50px",
                    "pref");
            DefaultFormBuilder builder = new DefaultFormBuilder(lm);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();

            builder.add(checkBoxUnit, cc.xy(1, 1));
            builder.add(comboUnit, cc.xyw(3, 1, 3));

            return builder.getPanel();
        }


    }

    public void loadComboUnit(ArrayList<Unit> unit) {
        final EventList<Unit> units = GlazedLists.eventList(unit);
        AutoCompleteSupport support = AutoCompleteSupport.install(comboUnit, units);
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }
    
    public void loadComboUnit(final EventList<Unit> units) {
        AutoCompleteSupport support = AutoCompleteSupport.install(comboUnit, units);
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    private void loadComboCustom() {

        final EventList<FilterValue> values = GlazedLists.eventList(filterValues);

        values.add(0, new FilterValue());
        AutoCompleteSupport support = AutoCompleteSupport.install(comboCustom, values);
        support.setFilterMode(TextMatcherEditor.CONTAINS);

        comboCustom.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    FilterValue value = null;
                    Object obj = comboCustom.getSelectedItem();
                    if (obj instanceof FilterValue) {
                        value = (FilterValue) obj;
                    }

                    if (value != null) {
                        if (value.getColumnTypes().equals(FilterCardPanel.COMBO_LONG_PANEL)) {
                            cardPanel.showComboLongPanel(value.getComboObjectValues());
                        } else if (value.getColumnTypes().equals(FilterCardPanel.COMBO_PANEL)) {
                            cardPanel.showComboPanel(value.getComboValues());
                        } else {
                            cardPanel.showPanel(value.getColumnTypes());
                        }
                    }
                }
            }
        });

    }

    public void loadComboUrban(final EventList<Region> urbans) {
        AutoCompleteSupport support = AutoCompleteSupport.install(comboUrban, urbans);
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    public void loadComboSubUrban(final EventList<Region> subUrbans) {
        urbanRegions = subUrbans;

        urbanRegions.add(0, new Region());
        AutoCompleteSupport support = AutoCompleteSupport.install(comboSubUrban, urbanRegions);
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    private void reloadComboSubUrban() {
        try {
            SwingWorker<ArrayList<Region>, Void> lw = new SwingWorker<ArrayList<Region>, Void>() {

                @Override
                protected ArrayList<Region> doInBackground() throws Exception {
                    Object obj = comboUrban.getSelectedItem();
                    Region pr = null;
                    if (obj instanceof Region) {
                        pr = (Region) obj;
                    }

                    ArrayList<Region> regions = new ArrayList<Region>();

                    if (pr != null) {
                        regions = mLogic.getSubUrbanRegion(mainframe.getSession(), pr);

                    }
                    return regions;
                }
            };
            lw.execute();
            comboSubUrban.getEditor().setItem(null);
            urbanRegions.clear();
            EventList<Region> evtRegion = GlazedLists.eventList(lw.get());
            urbanRegions.addAll(evtRegion);
            urbanRegions.add(0, new Region());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public FilterCardPanel getCardPanel() {
        return cardPanel;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        boolean old = isFiltered();
        this.filtered = filtered;
        this.firePropertyChange("filtered", old, isFiltered());
    }

    public String getModifier() throws MotekarException {
        String str = "";

        StringBuilder builder = new StringBuilder();

        if (checkBoxUnit.isSelected()) {
            Unit unit = null;
            Object obj = comboUnit.getSelectedItem();
            if (obj instanceof Unit) {
                unit = (Unit) obj;
            }

            if (unit == null) {
                throw new MotekarException("Pilih UPB / Sub UPB");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%') ");
                } else {
                    builder.append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%') ");
                }
            }
        }

        if (checkBoxUrban.isSelected()) {
            Region region = null;
            Object obj = comboUrban.getSelectedItem();
            if (obj instanceof Region) {
                region = (Region) obj;
            }

            if (region == null) {
                throw new MotekarException("Pilih Kecamatan");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and urban = ").append(region.getIndex());
                } else {
                    builder.append(" where urban = ").append(region.getIndex());
                }
            }
        }

        if (checkBoxSubUrban.isSelected()) {
            Region region = null;
            Object obj = comboSubUrban.getSelectedItem();
            if (obj instanceof Region) {
                region = (Region) obj;
            }

            if (region == null) {
                throw new MotekarException("Pilih Kelurahan");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and suburban = ").append(region.getIndex());
                } else {
                    builder.append(" where suburban = ").append(region.getIndex());
                }
            }
        }

        if (checkBoxCustom.isSelected()) {
            FilterValue value = null;
            Object obj = comboCustom.getSelectedItem();
            if (obj instanceof FilterValue) {
                value = (FilterValue) obj;
            }

            if (value == null) {
                throw new MotekarException("Pilih Filter");
            } else {
                if (value.getColumnTypes().equals(FilterCardPanel.STRING_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and lower(").append(value.getColumnName()).append(" )").
                                append(" like '%").append(cardPanel.getStringText().toLowerCase()).
                                append("%' ");
                    } else {
                        builder.append(" where lower(").append(value.getColumnName()).append(" )").
                                append(" like '%").append(cardPanel.getStringText().toLowerCase()).
                                append("%' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.NUMBER_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getBigDecimal());
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getBigDecimal());
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.DATE_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = '").append(new java.sql.Date(cardPanel.getDate().getTime())).
                                append("' ");
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = '").append(new java.sql.Date(cardPanel.getDate().getTime())).
                                append("' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.YEAR_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getYear());
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getYear());
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.COMBO_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = '").append(cardPanel.getSelectedStringCombo()).append("' ");
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = '").append(cardPanel.getSelectedStringCombo()).append("' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.COMBO_LONG_PANEL)) {
                    Object object = cardPanel.getSelectedObjectCombo();

                    if (object instanceof AssetObject) {
                        AssetObject assetObject = (AssetObject) object;
                        if (builder.toString().contains("where")) {
                            builder.append(" and ").append(value.getColumnName()).
                                    append(" = ").append(assetObject.getIndex());
                        } else {
                            builder.append(" where ").append(value.getColumnName()).
                                    append(" = ").append(assetObject.getIndex());
                        }
                    }



                }
            }
        }

        str = builder.toString();

        return str;
    }

    public String getCustomModifier() throws MotekarException {
        String str = "";

        StringBuilder builder = new StringBuilder();

        if (checkBoxUnit.isSelected()) {
            Unit unit = null;
            Object obj = comboUnit.getSelectedItem();
            if (obj instanceof Unit) {
                unit = (Unit) obj;
            }

            if (unit == null) {
                throw new MotekarException("Pilih UPB / Sub UPB");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%') ");
                } else {
                    builder.append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%') ");
                }
            }
        }

        if (checkBoxUrban.isSelected() && !checkBoxSubUrban.isSelected()) {
            Region urban = null;
            Object objUrban = comboUrban.getSelectedItem();
            if (objUrban instanceof Region) {
                urban = (Region) objUrban;
            }

            if (urban == null) {
                throw new MotekarException("Pilih Kecamatan");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and unit in ").
                            append("(select autoindex from unit ").
                            append("where urban = ").
                            append(urban.getIndex()).append(") ");
                } else {
                    builder.append(" where unit in ").
                            append("(select autoindex from unit ").
                            append("where urban = ").
                            append(urban.getIndex()).append(") ");
                }
            }

        } else if (checkBoxUrban.isSelected() && checkBoxSubUrban.isSelected()) {
            Region urban = null;
            Object objUrban = comboUrban.getSelectedItem();
            if (objUrban instanceof Region) {
                urban = (Region) objUrban;
            }

            Region subUrban = null;
            Object objSubUrban = comboSubUrban.getSelectedItem();
            if (objSubUrban instanceof Region) {
                subUrban = (Region) objSubUrban;
            }

            if (urban == null || subUrban == null) {
                throw new MotekarException("Pilih Kecamatan dan Kelurahan");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and unit in ").
                            append("(select autoindex from unit ").
                            append("where urban = ").
                            append(urban.getIndex()).
                            append(" and suburban = ").
                            append(subUrban.getIndex()).append(") ");
                } else {
                    builder.append(" where unit in").
                            append("(select autoindex from unit ").
                            append("where urban = ").
                            append(urban.getIndex()).
                            append(" and suburban = ").
                            append(subUrban.getIndex()).append(") ");
                }
            }
        } else if (!checkBoxUrban.isSelected() && checkBoxSubUrban.isSelected()) {
            Region subUrban = null;
            Object objSubUrban = comboSubUrban.getSelectedItem();
            if (objSubUrban instanceof Region) {
                subUrban = (Region) objSubUrban;
            }

            if (subUrban == null) {
                throw new MotekarException("Pilih Kelurahan");
            } else {
                if (builder.toString().contains("where")) {
                    builder.append(" and unit in ").
                            append("(select autoindex from unit ").
                            append("where suburban = ").
                            append(subUrban.getIndex()).append(") ");
                } else {
                    builder.append(" where unit in ").
                            append("(select autoindex from unit ").
                            append("where suburban = ").
                            append(subUrban.getIndex()).append(") ");
                }
            }
        }

        if (checkBoxCustom.isSelected()) {
            FilterValue value = null;
            Object obj = comboCustom.getSelectedItem();
            if (obj instanceof FilterValue) {
                value = (FilterValue) obj;
            }

            if (value == null) {
                throw new MotekarException("Pilih Filter");
            } else {
                if (value.getColumnTypes().equals(FilterCardPanel.STRING_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and lower(").append(value.getColumnName()).append(" )").
                                append(" like '%").append(cardPanel.getStringText().toLowerCase()).
                                append("%' ");
                    } else {
                        builder.append(" where lower(").append(value.getColumnName()).append(" )").
                                append(" like '%").append(cardPanel.getStringText().toLowerCase()).
                                append("%' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.NUMBER_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getBigDecimal());
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getBigDecimal());
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.DATE_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = '").append(new java.sql.Date(cardPanel.getDate().getTime())).
                                append("' ");
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = '").append(new java.sql.Date(cardPanel.getDate().getTime())).
                                append("' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.YEAR_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getYear());
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = ").append(cardPanel.getYear());
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.COMBO_PANEL)) {
                    if (builder.toString().contains("where")) {
                        builder.append(" and ").append(value.getColumnName()).
                                append(" = '").append(cardPanel.getSelectedStringCombo()).append("' ");
                    } else {
                        builder.append(" where ").append(value.getColumnName()).
                                append(" = '").append(cardPanel.getSelectedStringCombo()).append("' ");
                    }
                } else if (value.getColumnTypes().equals(FilterCardPanel.COMBO_LONG_PANEL)) {
                    Object object = cardPanel.getSelectedObjectCombo();

                    if (object instanceof AssetObject) {
                        AssetObject assetObject = (AssetObject) object;
                        if (builder.toString().contains("where")) {
                            builder.append(" and ").append(value.getColumnName()).
                                    append(" = ").append(assetObject.getIndex());
                        } else {
                            builder.append(" where ").append(value.getColumnName()).
                                    append(" = ").append(assetObject.getIndex());
                        }
                    }



                }
            }
        }

        str = builder.toString();

        return str;
    }
}
