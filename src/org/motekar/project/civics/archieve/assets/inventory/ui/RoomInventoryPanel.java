package org.motekar.project.civics.archieve.assets.inventory.ui;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JYearChooser;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.ColumnGroup;
import org.motekar.lib.common.listener.CommonButtonListener;
import org.motekar.lib.common.listener.PrintButtonListener;
import org.motekar.lib.common.listener.ProgressListener;
import org.motekar.lib.common.sql.CommonException;
import org.motekar.lib.common.swing.CustomOptionDialog;
import org.motekar.lib.common.swing.ManipulationButtonPanel;
import org.motekar.lib.common.swing.PrintButtonPanel;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryBook;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAssetRoom;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachineRoom;
import org.motekar.project.civics.archieve.assets.inventory.objects.RoomInventory;
import org.motekar.project.civics.archieve.assets.inventory.report.RoomInventoryJasper;
import org.motekar.project.civics.archieve.assets.inventory.sqlapi.InventoryBusinessLogic;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.RadioButtonPanel;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class RoomInventoryPanel extends JXPanel implements CommonButtonListener, PrintButtonListener {

    private ArchieveMainframe mainframe;
    private InventoryBusinessLogic logic;
    private AssetMasterBusinessLogic mLogic;
    private ManipulationButtonPanel btPanel = new ManipulationButtonPanel(true, true, true, false, false, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelUp = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private ManipulationButtonPanel btSavePanelBottom = new ManipulationButtonPanel(false, false, false, true, true, FlowLayout.LEFT);
    private PrintButtonPanel btPrintPanel = new PrintButtonPanel(true, true, false, false, FlowLayout.RIGHT);
    private JCheckBox checkRoomFilter = new JCheckBox("Ruangan");
    private JXComboBox comboRoomFilter = new JXComboBox();
    private JCheckBox checkUnitFilter = new JCheckBox("UPB / Sub UPB");
    private JXComboBox comboUnitFilter = new JXComboBox();
    //
    private JXComboBox comboRoom = new JXComboBox();
    private JCommandButton btAddRoom = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png", new Dimension(32, 32)));
    private JCommandButton btChooseItems = new JCommandButton(Mainframe.getResizableIconFromSource("resource/search.png"));
    private JXTextField fieldItems = new JXTextField();
    private JXTextField fieldType = new JXTextField();
    private JXTextField fieldFactoryNumber = new JXTextField();
    private JSpinField fieldVolume = new JSpinField();
    private JXTextField fieldMaterial = new JXTextField();
    private JYearChooser fieldAcquisitionYear = new JYearChooser();
    private JXFormattedTextField fieldPrice;
    private RadioButtonPanel fieldCondition = new RadioButtonPanel(new String[]{"B", "KB", "RB"}, 0, 3);
    private JXTextArea fieldDescription = new JXTextArea();
    //
    private RoomInventoryTable table = new RoomInventoryTable();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXPanel cardPanel = new JXPanel();
    private LoadRoomInventory worker;
    private ProgressListener progressListener;
    private Object selectedObject = null;
    private Object selectedItemObject = null;
    private TableRowSorter<TableModel> sorter;
    private EventList<Room> rooms = null;
    //
    private Unit unit;
    private ArrayList<Unit> units = new ArrayList<Unit>();
    //
    private JFileChooser xlsChooser;

    public RoomInventoryPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new InventoryBusinessLogic(mainframe.getConnection());
        mLogic = new AssetMasterBusinessLogic(mainframe.getConnection());
        construct();
        checkLogin();
    }

    private void checkLogin() {
        UserGroup userGroup = mainframe.getUserGroup();
        if (!userGroup.getGroupName().equals("Administrator")) {
            unit = mainframe.getUnit();
            checkUnitFilter.setSelected(true);
            checkUnitFilter.setEnabled(false);
            comboUnitFilter.setEnabled(false);
            comboUnitFilter.setSelectedItem(unit);
        }
    }

    private void construct() {

        contructNumberField();
        constructFileChooser();

        loadComboRoom();
        loadComboUnit();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        pbar.setVisible(false);

        table.addHighlighter(HighlighterFactory.createAlternateStriping());
        table.setShowGrid(false, false);

        btPanel.addListener(this);
        btSavePanelUp.addListener(this);
        btSavePanelBottom.addListener(this);
        btPrintPanel.addListener(this);

        cardPanel.setLayout(new CardLayout());
        cardPanel.add(createDataViewPanel(), "0");
        cardPanel.add(createInputPanel(), "1");

        comboRoomFilter.setEnabled(checkRoomFilter.isSelected());
        comboUnitFilter.setEnabled(checkUnitFilter.isSelected());

        checkRoomFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboRoomFilter.setEnabled(checkRoomFilter.isSelected());
                if (!checkUnitFilter.isSelected() && !checkRoomFilter.isSelected()) {
                    if (worker != null) {
                        worker.cancel(true);
                    }
                    table.clear();
                    btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
                }
            }
        });

        checkUnitFilter.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                comboUnitFilter.setEnabled(checkUnitFilter.isSelected());
                if (!checkUnitFilter.isSelected() && !checkRoomFilter.isSelected()) {
                    if (worker != null) {
                        worker.cancel(true);
                    }
                    table.clear();
                    btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
                }
            }
        });

        comboRoomFilter.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    table.loadData(getMachineModifier(), getFixedAssetModifier());
                }
            }
        });

        comboUnitFilter.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    table.loadData(getMachineModifier(), getFixedAssetModifier());
                }
            }
        });

        table.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("swingx.clicked")) {
                    ArrayList<Object> objects = table.getSelectedObjects();
                    if (!objects.isEmpty()) {
                        if (objects.size() == 1) {
                            selectedObject = objects.get(0);
                            btPanel.setButtonState(ManipulationButtonPanel.ENABLEALL);
                        } else {
                            selectedObject = null;
                            btPanel.setButtonState(ManipulationButtonPanel.UNEDIT);
                        }
                    } else {
                        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                    }
                }
            }
        });

        btAddRoom.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                InsertRoomDialog dlg = new InsertRoomDialog(mainframe, mainframe.getConnection(), mainframe.getSession());
                dlg.showDialog();

                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    Room room = dlg.getRoom();
                    rooms.add(room);
                    comboRoom.setSelectedItem(room);
                }
            }
        });

        btChooseItems.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ItemsRoomPickDialog dlg = new ItemsRoomPickDialog(mainframe, mainframe.getConnection(), mainframe.getSession(), units);
                dlg.showDialog();

                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    selectedItemObject = dlg.getSelectedItemObject();
                    setItemsValues();
                }
            }
        });


        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        
        btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
        
        btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        setFormItemsEnabled(false);
    }

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));


        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));

        fieldPrice.setHorizontalAlignment(JLabel.RIGHT);

    }

    private void constructFileChooser() {
        if (xlsChooser == null) {
            xlsChooser = new JFileChooser();
        }

        xlsChooser.setDialogTitle("Simpan File ke Excel");
        xlsChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);

        xlsChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String fileName = f.getName();
                return fileName.toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Excel(*.xls)";
            }
        });

    }

    private void setFormItemsEnabled(boolean enabled) {
        fieldAcquisitionYear.setEnabled(enabled);
        fieldItems.setEditable(enabled);
        fieldType.setEditable(enabled);
        fieldFactoryNumber.setEditable(enabled);
        fieldVolume.setEnabled(enabled);
        fieldMaterial.setEditable(enabled);
        fieldPrice.setEditable(enabled);
        fieldCondition.setAllDisabled();
        fieldDescription.setEditable(enabled);
    }

    private void setItemsValues() {
        if (selectedItemObject instanceof ItemsMachine) {
            ItemsMachine machine = (ItemsMachine) selectedItemObject;
            fieldAcquisitionYear.setValue(machine.getAcquisitionYear());
            fieldItems.setText(machine.getItemCode() + " " + machine.getFullItemName());
            fieldType.setText(machine.getMachineType());

            fieldFactoryNumber.setText(machine.getFactoryNumber());
            fieldVolume.setValue(machine.getVolume());
            fieldMaterial.setText(machine.getMaterial());
            fieldPrice.setValue(machine.getPrice());

            Condition condition = machine.getCondition();

            if (condition != null) {
                fieldCondition.setSelectedData(condition.getConditionCode());
            } else {
                fieldCondition.setAllDeselected();
            }

            fieldDescription.setText(machine.getDescription());


        } else if (selectedItemObject instanceof ItemsFixedAsset) {
            ItemsFixedAsset fixedAsset = (ItemsFixedAsset) selectedItemObject;
            fieldAcquisitionYear.setValue(fixedAsset.getAcquisitionYear());
            fieldItems.setText(fixedAsset.getItemCode() + " " + fixedAsset.getFullItemName());

            fieldFactoryNumber.setText("");
            fieldVolume.setValue(1);
            fieldMaterial.setText(fixedAsset.getArtMaterial());
            fieldPrice.setValue(fixedAsset.getPrice());
            Condition condition = fixedAsset.getCondition();

            if (condition != null) {
                fieldCondition.setSelectedData(condition.getConditionCode());
            } else {
                fieldCondition.setAllDeselected();
            }

            fieldDescription.setText(fixedAsset.getDescription());


            if (!fixedAsset.getBookAuthor().equals("")) {
                fieldType.setText(fixedAsset.getBookAuthor());
            } else if (!fixedAsset.getArtAuthor().equals("")) {
                fieldType.setText(fixedAsset.getArtAuthor());
            } else if (!fixedAsset.getCattleType().equals("")) {
                fieldType.setText(fixedAsset.getCattleType());
            } else {
                fieldType.setText("");
            }

        }
    }

    private void loadComboRoom() {
        try {
            SwingWorker<ArrayList<Room>, Void> lw = new SwingWorker<ArrayList<Room>, Void>() {

                @Override
                protected ArrayList<Room> doInBackground() throws Exception {
                    return mLogic.getRoom(mainframe.getSession());
                }
            };
            lw.execute();
            rooms = GlazedLists.eventList(lw.get());

            rooms.add(0, new Room());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboRoom, rooms);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

            AutoCompleteSupport support2 = AutoCompleteSupport.install(comboRoomFilter, rooms);
            support2.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void loadComboUnit() {
        try {
            SwingWorker<ArrayList<Unit>, Void> lw = new SwingWorker<ArrayList<Unit>, Void>() {

                @Override
                protected ArrayList<Unit> doInBackground() throws Exception {
                    ArrayList<Unit> units = mLogic.getUnit(mainframe.getSession());
                    if (!units.isEmpty()) {
                        for (Unit unit : units) {
                            unit.setHirarchiecal(true);
                        }
                    }
                    return units;
                }
            };
            lw.execute();
            units = lw.get();
            final EventList<Unit> unitList = GlazedLists.eventList(units);

            unitList.add(0, new Unit());
            AutoCompleteSupport support = AutoCompleteSupport.install(comboUnitFilter, unitList);
            support.setFilterMode(TextMatcherEditor.CONTAINS);

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private Component createDataViewPanel() {
        JXPanel viewPanel = new JXPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(createTablePanel(), BorderLayout.CENTER);

        return viewPanel;
    }

    private Component createInputPanel() {

        FormLayout lm = new FormLayout(
                "300px,right:pref,10px, 100px,5px,fill:default:grow,pref,300px",
                "50px,pref,5px,pref,20px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,pref,5px,pref,5px,"
                + "pref,5px,"
                + "20px,pref,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JXLabel requiredLabel = new JXLabel("Field tidak boleh kosong");
        requiredLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        requiredLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);
        requiredLabel.setFont(new Font(requiredLabel.getFont().getName(), Font.BOLD, requiredLabel.getFont().getSize()));

        JXPanel panelRequired = new JXPanel(new FlowLayout(FlowLayout.LEFT));
        panelRequired.add(requiredLabel, null);

        btSavePanelUp.setBorder(new EmptyBorder(5, 0, 5, 5));
        btSavePanelBottom.setBorder(new EmptyBorder(5, 0, 5, 5));

        CellConstraints cc = new CellConstraints();

        builder.add(btSavePanelUp, cc.xyw(1, 1, 8));
        builder.addSeparator("", cc.xyw(1, 2, 8));

        builder.add(panelRequired, cc.xyw(1, 4, 8));


        JXLabel roomNameLabel = new JXLabel("Ruangan");
        roomNameLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        roomNameLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        JXLabel itemNameLabel = new JXLabel("Kode / Nama Barang");
        itemNameLabel.setIcon(Mainframe.getResizableIconFromSource("resource/required.gif", new Dimension(7, 14)));
        itemNameLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldDescription);

        builder.add(roomNameLabel, cc.xy(2, 6));
        builder.add(comboRoom, cc.xyw(4, 6, 3));
        builder.add(createStrip(1.0, 1.0), cc.xyw(7, 6, 1));

        builder.add(itemNameLabel, cc.xy(2, 8));
        builder.add(fieldItems, cc.xyw(4, 8, 3));
        builder.add(createStrip2(1.0, 1.0), cc.xyw(7, 8, 1));

        builder.addLabel("Merk / Tipe / Judul / Pencipta", cc.xy(2, 10));
        builder.add(fieldType, cc.xyw(4, 10, 4));

        builder.addLabel("No.Seri Pabrik", cc.xy(2, 12));
        builder.add(fieldFactoryNumber, cc.xyw(4, 12, 4));

        builder.addLabel("Ukuran", cc.xy(2, 14));
        builder.add(fieldVolume, cc.xyw(4, 14, 1));

        builder.addLabel("Bahan", cc.xy(2, 16));
        builder.add(fieldMaterial, cc.xyw(4, 16, 4));

        builder.addLabel("Tahun Pembuatan / Pembelian", cc.xy(2, 18));
        builder.add(fieldAcquisitionYear, cc.xyw(4, 18, 1));

        builder.addLabel("Harga Pembelian/Perolehan", cc.xy(2, 20));
        builder.add(fieldPrice, cc.xyw(4, 20, 4));

        builder.addLabel("Keadaan Barang", cc.xy(2, 22));
        builder.add(fieldCondition, cc.xyw(4, 22, 4));

        builder.addLabel("Keterangan", cc.xy(2, 24));
        builder.add(scPane, cc.xywh(4, 24, 4, 8));

        builder.addSeparator("", cc.xyw(1, 32, 8));
        builder.add(btSavePanelBottom, cc.xyw(1, 33, 8));


        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JScrollPane mainSCPane = new JScrollPane();
        mainSCPane.setViewportView(panel);

        JScrollBar sb = mainSCPane.getVerticalScrollBar();
        sb.setUnitIncrement(20);

        JXTitledPanel titledPanel = new JXTitledPanel("Input Kartu Inventaris Ruang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
//        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(mainSCPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createTablePanel() {

        btPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BorderLayout());
        panelButton.add(btPanel, BorderLayout.WEST);
        panelButton.add(btPrintPanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(panelButton, BorderLayout.CENTER);

        JXTitledPanel titledPanel = new JXTitledPanel("Data View");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(panel, BorderLayout.NORTH);
        collapasepanel.add(scPane, BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,10px, fill:default:grow,50px",
                "pref,5px,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.add(checkUnitFilter, cc.xy(1, 1));
        builder.add(comboUnitFilter, cc.xy(3, 1));

        builder.add(checkRoomFilter, cc.xy(1, 3));
        builder.add(comboRoomFilter, cc.xy(3, 3));


        return builder.getPanel();
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Ruangan");

        btAddRoom.setActionRichTooltip(addTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btAddRoom);

        return buttonStrip;
    }

    private JCommandButtonStrip createStrip2(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Pilih Barang");

        btChooseItems.setActionRichTooltip(addTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btChooseItems);

        return buttonStrip;
    }

    private JXStatusBar createStatusBar() {
        JXStatusBar bar = new JXStatusBar();
        JXStatusBar.Constraint c1 = new JXStatusBar.Constraint(
                JXStatusBar.Constraint.ResizeBehavior.FILL);
        bar.add(statusLabel, c1);
        JXStatusBar.Constraint c2 = new JXStatusBar.Constraint();
        c2.setFixedWidth(300);
        bar.add(pbar, c2);

        return bar;
    }

    private void clearYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        fieldAcquisitionYear.setYear(year);
    }

    private void clearForm() {
        fieldItems.setText("");
        clearYear();
        fieldType.setText("");
        fieldFactoryNumber.setText("");
        fieldVolume.setValue(0);
        fieldMaterial.setText("");
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldCondition.setAllDeselected();
        fieldDescription.setText("");
    }

    private void setFormValues() {
        if (selectedObject != null) {

            if (selectedObject instanceof ItemsMachineRoom) {
                ItemsMachineRoom roomInventory = (ItemsMachineRoom) selectedObject;
                if (roomInventory.getRoom() == null) {
                    comboRoom.getEditor().setItem(null);
                } else {
                    comboRoom.setSelectedItem(roomInventory.getRoom());
                }

                selectedItemObject = roomInventory.getMachine();
                setItemsValues();

            } else if (selectedObject instanceof ItemsFixedAssetRoom) {
                ItemsFixedAssetRoom roomInventory = (ItemsFixedAssetRoom) selectedObject;
                if (roomInventory.getRoom() == null) {
                    comboRoom.getEditor().setItem(null);
                } else {
                    comboRoom.setSelectedItem(roomInventory.getRoom());
                }
                selectedItemObject = roomInventory.getFixedAsset();
                setItemsValues();
            }
        } else {
            clearForm();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
        }
    }

    private Object getRoomInventory() throws MotekarException {
        StringBuilder errorString = new StringBuilder();

        Room room = null;
        Object roomObject = comboRoom.getSelectedItem();
        if (roomObject instanceof Room) {
            room = (Room) roomObject;
        }

        if (room != null) {
            if (room.getIndex().equals(Long.valueOf(0))) {
                room = null;
            }
        }


        if (room == null) {
            errorString.append("<br>- Ruangan</br>");
        }

        if (selectedItemObject == null) {
            errorString.append("<br>- Barang</br>");
        }


        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        if (selectedItemObject instanceof ItemsMachine) {
            ItemsMachineRoom inventory = new ItemsMachineRoom();
            inventory.setRoom(room);
            inventory.setMachine((ItemsMachine) selectedItemObject);
            return inventory;
        } else {
            ItemsFixedAssetRoom inventory = new ItemsFixedAssetRoom();
            inventory.setRoom(room);
            inventory.setFixedAsset((ItemsFixedAsset) selectedItemObject);
            return inventory;
        }
    }

    @Override
    public void onInsert() throws SQLException, CommonException {
        clearForm();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboRoom.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onEdit() {
        setFormValues();
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
        comboRoom.getEditor().getEditorComponent().requestFocus();
    }

    @Override
    public void onDelete() {
        Object[] options = {"Ya", "Tidak"};
        int choise = JOptionPane.showOptionDialog(this,
                " Anda yakin menghapus data ini ? (Y/T)",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[1]);
        if (choise == JOptionPane.YES_OPTION) {
            try {
                ArrayList<Object> selectedRoomInventorys = table.getSelectedObjects();
                if (!selectedRoomInventorys.isEmpty()) {
                    logic.deleteRoomInventory(mainframe.getSession(), selectedRoomInventorys);
                    table.removeObject(selectedRoomInventorys);
                    clearForm();
                    btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
                }
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    @Override
    public void onSave() {
        try {
            Object newObject = getRoomInventory();
            if (btPanel.isNewstate()) {
                newObject = logic.insertRoomInventory(mainframe.getSession(), newObject);
                table.add(newObject);
                selectedObject = newObject;
            } else if (btPanel.isEditstate()) {
                newObject = logic.updateRoomInventory(mainframe.getSession(), selectedObject, newObject);
                table.updateSelectedObject(newObject);
                selectedObject = newObject;
            }
            btPanel.setStateFalse();
            btPanel.setButtonState(ManipulationButtonPanel.ONLY_NEW);
            statusLabel.setText("Ready");
            ((CardLayout) cardPanel.getLayout()).first(cardPanel);
            table.packAll();
        } catch (MotekarException ex) {
            CustomOptionDialog.showDialog(this, ex.getMessage(), "Perhatian", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    null, "Error", ex, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        }
    }

    @Override
    public void onCancel() throws SQLException, CommonException {
        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
        btPanel.setStateFalse();
    }

    private String getMachineModifier() {
        String str = "";

        StringBuilder builder = new StringBuilder();

        if (checkRoomFilter.isSelected()) {
            Room room = null;
            Object obj = comboRoomFilter.getSelectedItem();
            if (obj instanceof Room) {
                room = (Room) obj;
            }

            if (room != null) {
                if (builder.toString().contains("where")) {
                    builder.append(" and room = ").
                            append(room.getIndex());
                } else {
                    builder.append(" where room = ").
                            append(room.getIndex());
                }
            }
        }


        if (checkUnitFilter.isSelected()) {
            Unit unit = null;
            Object obj = comboUnitFilter.getSelectedItem();
            if (obj instanceof Unit) {
                unit = (Unit) obj;
            }

            if (unit != null) {
                if (builder.toString().contains("where")) {
                    builder.append(" and items in ").
                            append(" (select autoindex from itemsmachine ").
                            append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%')) ");
                } else {
                    builder.append(" where items in ").
                            append(" (select autoindex from itemsmachine ").
                            append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%')) ");
                }
            }
        }



        str = builder.toString();

        return str;
    }

    private String getFixedAssetModifier() {
        String str = "";

        StringBuilder builder = new StringBuilder();

        if (checkRoomFilter.isSelected()) {
            Room room = null;
            Object obj = comboRoomFilter.getSelectedItem();
            if (obj instanceof Room) {
                room = (Room) obj;
            }

            if (room != null) {
                if (builder.toString().contains("where")) {
                    builder.append(" and room = ").
                            append(room.getIndex());
                } else {
                    builder.append(" where room = ").
                            append(room.getIndex());
                }
            }
        }


        if (checkUnitFilter.isSelected()) {
            Unit unit = null;
            Object obj = comboUnitFilter.getSelectedItem();
            if (obj instanceof Unit) {
                unit = (Unit) obj;
            }

            if (unit != null) {
                if (builder.toString().contains("where")) {
                    builder.append(" and items in ").
                            append(" (select autoindex from itemsfixedasset ").
                            append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%')) ");
                } else {
                    builder.append(" where items in ").
                            append(" (select autoindex from itemsfixedasset ").
                            append(" where unit in ").
                            append(" (select autoindex from unit ").
                            append(" where unitcode like '").
                            append(unit.getUnitCode()).append("%')) ");
                }
            }
        }



        str = builder.toString();

        return str;
    }

    public void onPrint() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Object objUnit = comboUnitFilter.getSelectedItem();
        unit = null;
        if (objUnit instanceof Unit) {
            unit = (Unit) objUnit;
        }
        Object objRoom = comboRoomFilter.getSelectedItem();
        Room room = null;
        if (objRoom instanceof Room) {
            room = (Room) objRoom;
        }

        if (unit != null) {
            RoomInventoryJasper report = new RoomInventoryJasper("Buku Inventaris Ruangan (KIR)",
                    table.getInventoryBooks(), unit, room, mainframe.getProfileAccount());
            report.showReport();
        }

        mainframe.setCursor(old);
    }

    public void onPrintExcel() throws Exception, CommonException {
        Cursor old = mainframe.getCursor();
        mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Object objUnit = comboUnitFilter.getSelectedItem();
        unit = null;
        if (objUnit instanceof Unit) {
            unit = (Unit) objUnit;
        }
        Object objRoom = comboRoomFilter.getSelectedItem();
        Room room = null;
        if (objRoom instanceof Room) {
            room = (Room) objRoom;
        }

        if (unit != null) {
            int retVal = xlsChooser.showDialog(mainframe, "Simpan");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = xlsChooser.getSelectedFile();
                String fileName = file.getAbsolutePath() + ".xls";

                RoomInventoryJasper report = new RoomInventoryJasper("Buku Inventaris Ruangan (KIR)",
                        table.getInventoryBooks(), unit, room, mainframe.getProfileAccount());
                report.exportToExcel(fileName);
            }
        }

        mainframe.setCursor(old);
    }

    public void onPrintPDF() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPrintGraph() throws Exception, CommonException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class RoomInventoryTable extends JXTable {

        private RoomInventoryTableModel model;

        public RoomInventoryTable() {
            model = new RoomInventoryTableModel();
            setModel(model);
            TableColumnModel colModel = getColumnModel();
            colModel.getColumn(0).setMinWidth(50);
            colModel.getColumn(0).setMaxWidth(50);

            ColumnGroup group = new ColumnGroup("<html><center><b>Keadaan Barang</b></center>");
            group.add(colModel.getColumn(10));
            group.add(colModel.getColumn(11));
            group.add(colModel.getColumn(12));

            JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
            header.addColumnGroup(group);
            header.setReorderingAllowed(false);
            setTableHeader(header);

            setColumnMargin(5);

            setHorizontalScrollEnabled(true);

            setRowSelectionAllowed(false);
            setColumnSelectionAllowed(false);

            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }

        public void loadData(String mModifier, String faModifier) {
            if (worker != null) {
                worker.cancel(true);
            }

            worker = new LoadRoomInventory(mModifier, faModifier);
            progressListener = new ProgressListener(pbar, statusLabel, worker, false);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public void clear() {
            model.clear();
        }

        public Object getSelectedObject() {
            ArrayList<Object> selectedObjects = getSelectedObjects();
            return selectedObjects.get(0);
        }

        public ArrayList<Object> getInventoryBooks() {
            return model.getObjects();
        }

        public ArrayList<Object> getSelectedObjects() {

            ArrayList<Object> objects = new ArrayList<Object>();

            int rows = getRowSorter().getModelRowCount();

            for (int i = 0; i < rows; i++) {
                Object object = null;
                Object objBool = model.getValueAt(i, 0);

                if (objBool instanceof Boolean) {
                    Boolean selected = (Boolean) objBool;
                    if (selected.booleanValue()) {
                        object = model.getValueAt(i, 2);
                        objects.add(object);
                    }
                }
            }

            return objects;
        }

        public void updateSelectedObject(Object object) {
            model.updateRow(getSelectedObject(), object);
        }

        public void removeObject(ArrayList<Object> objects) {
            if (!objects.isEmpty()) {
                for (Object object : objects) {
                    model.remove(object);
                }
            }

        }

        public void add(ArrayList<Object> objects) {
            if (!objects.isEmpty()) {
                for (Object object : objects) {
                    model.add(object);
                }
            }
        }

        public void add(Object object) {
            model.add(object);
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass.equals(Boolean.class)) {
                return new DefaultTableRenderer(new CheckBoxProvider());
            } else if (columnClass.equals(BigDecimal.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(2);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.RIGHT);
            } else if (columnClass.equals(Integer.class)) {
                NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
                amountDisplayFormat.setMinimumFractionDigits(0);
                amountDisplayFormat.setMaximumFractionDigits(0);
                amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in", "id", "id")));
                amountDisplayFormat.setGroupingUsed(false);

                return new DefaultTableRenderer(new FormatStringValue(amountDisplayFormat), JLabel.CENTER);
            } else if (columnClass.equals(Date.class)) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd MMM yyyy",
                        new Locale("in", "id", "id"))), JLabel.CENTER);
            }
            return super.getDefaultRenderer(columnClass);
        }
    }

    private static class RoomInventoryTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 14;
        private static final String[] COLUMNS = {"", "<html><b>No. Urut</b>",
            "<html><center><b>Nama Barang/<br>Jenis Barang</br></b></center>",
            "<html><center><b>Merk / Tipe / Model<br>Judul / Pencipta</br></b></center>",
            "<html><center><b>No.Seri<br>Pabrik</br></b></center>",
            "<html><b>Ukuran</b>", "<html><b>Bahan</b>",
            "<html><center><b>Tahun Pembuatan/<br>Pembelian</br></b></center>",
            "<html><center><b>No.Kode/<br>Barang</br></b></center>",
            "<html><center><b>Harga Pembelian/<br>Perolehan (Rp.)</br></b></center>",
            "<html><center><b>Baik<br>(B)</br></b></center>",
            "<html><center><b>Kurang Baik<br>(KB)</br></b></center>",
            "<html><center><b>Rusak Berat<br>(RB)</br></b></center>",
            "<html><b>Keterangan</b>"};
        private ArrayList<Object> books = new ArrayList<Object>();

        public RoomInventoryTableModel() {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return true;
            }
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0 || columnIndex == 10 || columnIndex == 11 || columnIndex == 12) {
                return Boolean.class;
            } else if (columnIndex == 1 || columnIndex == 5 || columnIndex == 7) {
                return Integer.class;
            } else if (columnIndex == 9) {
                return BigDecimal.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public void add(Object object) {
            insertRow(getRowCount(), object);
        }

        public void insertRow(int row, Object object) {
            books.add(row, object);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(Object oldObject, Object newObject) {
            int index = books.indexOf(oldObject);
            books.set(index, newObject);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Object object) {
            int row = books.indexOf(object);
            books.remove(object);
            fireTableRowsDeleted(row, row);
        }

        public void clear() {
            setRowCount(0);
        }

        protected void setRowCount(int rowCount) {
            int old = getRowCount();
            if (old == rowCount) {
                return;
            }

            if (rowCount <= old) {
                books.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                books.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            books.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (books.get(i) == null) {
                    books.set(i, new RoomInventory());
                }
            }
        }

        public ArrayList<Object> getObjects() {
            return books;
        }

        @Override
        public int getRowCount() {
            return books.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Object getObject(int row) {
            if (!books.isEmpty()) {
                return books.get(row);
            }
            return new InventoryBook();
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Object object = getObject(row);
            switch (column) {
                case 0:
                    if (aValue instanceof Boolean) {
                        ((RoomInventory) object).setSelected((Boolean) aValue);
                    }

                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Object object = getObject(row);
            switch (column) {
                case 0:
                    if (object instanceof RoomInventory) {
                        toBeDisplayed = ((RoomInventory) object).isSelected();
                    }
                    break;
                case 1:
                    toBeDisplayed = Integer.valueOf(row + 1);
                    break;
                case 2:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = (ItemsMachineRoom) object;
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = (ItemsFixedAssetRoom) object;
                    }

                    break;

                case 3:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getMachineType();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        ItemsFixedAsset fixedAsset = ((ItemsFixedAssetRoom) object).getFixedAsset();

                        if (!fixedAsset.getBookAuthor().equals("")) {
                            toBeDisplayed = fixedAsset.getBookAuthor();
                        } else if (!fixedAsset.getArtAuthor().equals("")) {
                            toBeDisplayed = fixedAsset.getArtAuthor();
                        } else if (!fixedAsset.getCattleType().equals("")) {
                            toBeDisplayed = fixedAsset.getCattleType();
                        } else {
                            toBeDisplayed = "";
                        }
                    }
                    break;
                case 4:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getFactoryNumber();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = "";
                    }
                    break;
                case 5:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getVolume();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = Integer.valueOf(1);
                    }
                    break;
                case 6:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getMaterial();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = ((ItemsFixedAssetRoom) object).getFixedAsset().getArtMaterial();
                    }
                    break;
                case 7:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getAcquisitionYear();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = ((ItemsFixedAssetRoom) object).getFixedAsset().getAcquisitionYear();
                    }
                    break;
                case 8:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getItemCode();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = ((ItemsFixedAssetRoom) object).getFixedAsset().getItemCode();
                    }
                    break;
                case 9:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getPrice();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = ((ItemsFixedAssetRoom) object).getFixedAsset().getPrice();
                    }
                    break;
                case 10:
                    if (object instanceof ItemsMachineRoom) {
                        Condition condition = ((ItemsMachineRoom) object).getMachine().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("B")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        Condition condition = ((ItemsFixedAssetRoom) object).getFixedAsset().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("B")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    }
                    break;
                case 11:
                    if (object instanceof ItemsMachineRoom) {
                        Condition condition = ((ItemsMachineRoom) object).getMachine().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("KB")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        Condition condition = ((ItemsFixedAssetRoom) object).getFixedAsset().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("KB")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    }
                    break;
                case 12:
                    if (object instanceof ItemsMachineRoom) {
                        Condition condition = ((ItemsMachineRoom) object).getMachine().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("RB")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        Condition condition = ((ItemsFixedAssetRoom) object).getFixedAsset().getCondition();
                        if (condition != null) {
                            if (condition.getConditionCode().equals("RB")) {
                                toBeDisplayed = Boolean.TRUE;
                            } else {
                                toBeDisplayed = Boolean.FALSE;
                            }

                        } else {
                            toBeDisplayed = Boolean.FALSE;
                        }
                    }
                    break;
                case 13:
                    if (object instanceof ItemsMachineRoom) {
                        toBeDisplayed = ((ItemsMachineRoom) object).getMachine().getDescription();
                    } else if (object instanceof ItemsFixedAssetRoom) {
                        toBeDisplayed = ((ItemsFixedAssetRoom) object).getFixedAsset().getDescription();
                    }
                    break;

            }
            return toBeDisplayed;
        }
    }

    private class LoadRoomInventory extends SwingWorker<RoomInventoryTableModel, Object> {

        private RoomInventoryTableModel model;
        private Exception exception;
        private String mModifier = "";
        private String faModifier = "";

        public LoadRoomInventory(String mModifier, String faModifier) {
            this.model = (RoomInventoryTableModel) table.getModel();
            this.model.clear();
            this.mModifier = mModifier;
            this.faModifier = faModifier;
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Object> chunks) {
            mainframe.stopInActiveListener();
            for (Object inventory : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Kartu Inventaris Ruang " + inventory.toString());
                model.add(inventory);
            }
        }

        private String getCompleteItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = null;

            if (!iLookUp.isEmpty()) {
                int size = iLookUp.size();
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        str.append(iLookUp.get(i).getCategoryName());
                    } else {
                        str.append(iLookUp.get(i).getCategoryName()).append(">");
                    }
                }

                if (str.length() > 0) {
                    itemsName = str.toString();
                }
            }

            return itemsName;
        }

        private String getLastItemsName(ArrayList<ItemsCategory> iLookUp) {
            String itemsName = "";

            if (!iLookUp.isEmpty()) {
                itemsName = iLookUp.get(iLookUp.size() - 1).getCategoryName();
            }

            return itemsName;
        }

        @Override
        protected RoomInventoryTableModel doInBackground() throws Exception {
            try {
                ArrayList<ItemsMachineRoom> machineRooms = logic.getItemsMachineRoom(mainframe.getSession(), mModifier);
                ArrayList<ItemsFixedAssetRoom> assetRooms = logic.getItemsFixedAssetRoom(mainframe.getSession(), faModifier);

                ArrayList<Object> objects = new ArrayList<Object>();
                objects.addAll(machineRooms);
                objects.addAll(assetRooms);

                double progress = 0.0;
                if (!objects.isEmpty()) {
                    for (int i = 0; i < objects.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / objects.size();

                        Object book = objects.get(i);

                        if (book instanceof ItemsMachineRoom) {
                            ItemsMachineRoom machineRoom = (ItemsMachineRoom) book;

                            ItemsMachine machine = machineRoom.getMachine();

                            machine = logic.getItemsMachine(mainframe.getSession(), machine.getIndex());

                            if (machineRoom.getRoom() != null) {
                                machineRoom.setRoom(mLogic.getRoom(mainframe.getSession(), machineRoom.getRoom()));
                            }

                            if (machine.getUnit() != null) {
                                machine.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), machine.getUnit()));
                            }

                            if (machine.getCondition() != null) {
                                machine.setCondition(mLogic.getCondition(mainframe.getSession(), machine.getCondition()));
                            }

                            ItemsCategory category = new ItemsCategory();
                            category.setCategoryCode(machine.getItemCode());
                            category.setCategoryName(machine.getItemName());
                            category.setTypes(ItemsCategory.ITEMS_MACHINE);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            machine.setItemName(getLastItemsName(iLookUp));
                            machine.setFullItemName(getCompleteItemsName(iLookUp));

                            machineRoom.setMachine(machine);

                            publish(machineRoom);

                        } else if (book instanceof ItemsFixedAssetRoom) {
                            ItemsFixedAssetRoom assetRoom = (ItemsFixedAssetRoom) book;

                            ItemsFixedAsset fixedAsset = assetRoom.getFixedAsset();

                            fixedAsset = logic.getItemsFixedAsset(mainframe.getSession(), fixedAsset.getIndex());

                            if (assetRoom.getRoom() != null) {
                                assetRoom.setRoom(mLogic.getRoom(mainframe.getSession(), assetRoom.getRoom()));
                            }

                            if (fixedAsset.getUnit() != null) {
                                fixedAsset.setUnit(mLogic.getUnitByIndex(mainframe.getSession(), fixedAsset.getUnit()));
                            }

                            if (fixedAsset.getCondition() != null) {
                                fixedAsset.setCondition(mLogic.getCondition(mainframe.getSession(), fixedAsset.getCondition()));
                            }

                            ItemsCategory category = new ItemsCategory();
                            category.setCategoryCode(fixedAsset.getItemCode());
                            category.setCategoryName(fixedAsset.getItemName());
                            category.setTypes(ItemsCategory.ITEMS_FIXED_ASSET);
                            category.setStyled(true);

                            ArrayList<ItemsCategory> iLookUp = mLogic.getItemsCategorys(mainframe.getSession(), category);
                            fixedAsset.setItemName(getLastItemsName(iLookUp));
                            fixedAsset.setFullItemName(getCompleteItemsName(iLookUp));

                            assetRoom.setFixedAsset(fixedAsset);

                            publish(assetRoom);
                        }

                        setProgress((int) progress);
                        Thread.sleep(100L);


                    }
                }
                return model;
            } catch (Exception anyException) {
                exception = anyException;
                throw exception;
            }
        }

        @Override
        protected void done() {
            try {
                if (isCancelled()) {
                    return;
                }
                get();
            } catch (InterruptedException e) {
                //ignore
            } catch (ExecutionException e) {
                Exceptions.printStackTrace(e);
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(RoomInventoryPanel.this, info);
            }

            table.packAll();

            if (table.getRowCount() > 0) {
                btPrintPanel.setButtonState(PrintButtonPanel.ENABLEALL);
            } else {
                btPrintPanel.setButtonState(PrintButtonPanel.DISABLEALL);
            }

            setProgress(100);
            mainframe.startInActiveListener();
        }
    }
}