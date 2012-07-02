package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.misc.MotekarFocusTraversalPolicy;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class StandardPricePanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private StandardPricePanel.StandardPriceList priceList = new StandardPricePanel.StandardPriceList();
    private JXTextField fieldName = new JXTextField();
    private JXComboBox comboEselon = new JXComboBox();
    private JXTextField fieldDeparture = new JXTextField();
    private JXTextField fieldDestination = new JXTextField();
    private JXComboBox comboTranscType = new JXComboBox();
    private JXComboBox comboTransportType = new JXComboBox();
    private JXFormattedTextField fieldPrice;
    private JXTextArea fieldNotes = new JXTextArea();
    private StandardPricePanel.LoadStandardPrice worker;
    private StandardPricePanel.PriceProgressListener progressListener;
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private StandardPrice selectedPrice = null;
    private StringBuilder errorString = new StringBuilder();

    public StandardPricePanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        this.logic = new MasterBusinessLogic(this.mainframe.getConnection());
        construct();
        priceList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Standar Harga");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(priceList), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);

        return titledPanel;
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Rincian Data");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createButtonsPanel(), BorderLayout.NORTH);
        collapasepanel.add(createMainPanel(), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Standar Harga merupakan pengelompokan harga-harga standar "
                + "yang diperlukan untuk perjalanan dinas\n\n"
                + "Tambah Standar Harga\n"
                + "Untuk menambah Standar Harga klik tombol paling kiri "
                + "kemudian isi data standar harga yang diinginkan "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Standar Harga\n"
                + "Untuk merubah Standar Harga klik tombol kedua dari kiri "
                + "kemudian ubah data standar harga yang diingin dirubah, "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Standar Harga\n"
                + "Untuk menghapus Standar Harga klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Standar "
                + "Harga tersebut, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan\n\n";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Standar Harga");
        task.getContentPane().add(helpLabel);
        task.setAnimated(true);

        container.add(task);

        helpLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titledPanel.setContentContainer(scPane);

        return titledPanel;
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

    protected JPanel createMainPanel() {
        contructNumberField();

        FormLayout lm = new FormLayout(
                "right:pref,10px,fill:default:grow,50px",
                "pref,4px,pref,4px,pref,4px,pref,4px,pref,4px,pref,4px,pref,4px,fill:default,pref,fill:default:grow,4px,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldNotes);

        fieldNotes.setMargin(new Insets(0, 2, 0, 0));

        lm.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama", cc.xy(1, 1));
        builder.add(fieldName, cc.xy(3, 1));

        builder.addLabel("Eselon", cc.xy(1, 3));
        builder.add(comboEselon, cc.xy(3, 3));
        
        builder.addLabel("Dari", cc.xy(1, 5));
        builder.add(fieldDeparture, cc.xy(3, 5));

        builder.addLabel("Tujuan", cc.xy(1, 7));
        builder.add(fieldDestination, cc.xy(3, 7));

        builder.addLabel("Tipe", cc.xy(1, 9));
        builder.add(comboTranscType, cc.xy(3, 9));

        builder.addLabel("Transportasi", cc.xy(1, 11));
        builder.add(comboTransportType, cc.xy(3, 11));

        builder.addLabel("Harga", cc.xy(1, 13));
        builder.add(fieldPrice, cc.xy(3, 13));

        builder.addLabel("Keterangan", cc.xy(1, 15));
        builder.addLabel("(Opsional)", cc.xy(1, 16));
        builder.add(scPane, cc.xywh(3, 15, 1, 4));

        return builder.getPanel();
    }

    protected JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "fill:default,10px, fill:default:grow", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(new JXLabel("Cari"), fieldSearch);

        return builder.getPanel();
    }

    protected JPanel createButtonsPanel() {
        FormLayout lm = new FormLayout(
                "right:pref, 4dlu, left:pref, 4dlu, left:pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        builder.append(createStrip(3.0, 3.0));
        return builder.getPanel();
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Standar Harga");

        btAdd.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Standar Harga");

        btEdit.setActionRichTooltip(editTooltip);

        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Standar Harga");

        btSave.setActionRichTooltip(saveTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Standar Harga");

        btDelete.setActionRichTooltip(deleteTooltip);

        RichTooltip cancelTooltip = new RichTooltip();
        cancelTooltip.setTitle("Batalkan Perubahan");

        btCancel.setActionRichTooltip(cancelTooltip);

        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btAdd);
        buttonStrip.add(btEdit);
        buttonStrip.add(btSave);
        buttonStrip.add(btDelete);
        buttonStrip.add(btCancel);


        return buttonStrip;
    }

    private void construct() {

        loadTransactionType();
        loadTransportType();
        loadEselon();

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });


        fieldNotes.setWrapStyleWord(true);
        fieldNotes.setLineWrap(true);

        priceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        priceList.setAutoCreateRowSorter(true);
        priceList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

        comboTranscType.setEditable(true);
        comboTransportType.setEditable(true);

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.25)"
                + "(LEAF name=editor2 weight=0.5) (LEAF name=editor3 weight=0.25))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createLeftComponent(), "editor1");
        splitPane.add(createCenterComponent(), "editor2");
        splitPane.add(panel, "editor3");

        panel.setVisible(true);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setFormState();
        setButtonState("");
    }

    public void filter() {
        priceList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(RowFilter.Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldName.setEnabled(iseditable);
        comboEselon.setEnabled(iseditable);
        fieldDeparture.setEnabled(iseditable);
        fieldDestination.setEnabled(iseditable);
        fieldPrice.setEnabled(iseditable);
        comboTranscType.setEnabled(iseditable);
        comboTransportType.setEnabled(iseditable);
        fieldNotes.setEnabled(iseditable);
        fieldSearch.setEnabled(!iseditable);
        priceList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldName.setText("");
        comboEselon.setSelectedIndex(0);
        fieldDeparture.setText("");
        fieldDestination.setText("");
        fieldPrice.setValue(BigDecimal.ZERO);
        comboTranscType.setSelectedIndex(0);
        comboTransportType.setSelectedIndex(0);
        fieldNotes.setText("");
        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
        }
    }

    private void loadTransactionType() {
        comboTranscType.removeAllItems();
        comboTranscType.setModel(new ListComboBoxModel<String>(StandardPrice.transactionTypeAsList()));
        AutoCompleteDecorator.decorate(comboTranscType);
    }

    private void loadTransportType() {
        comboTransportType.removeAllItems();
        comboTransportType.setModel(new ListComboBoxModel<String>(StandardPrice.transportTypeAsList()));
        AutoCompleteDecorator.decorate(comboTransportType);
    }
    
    private void loadEselon() {
        comboEselon.removeAllItems();
        comboEselon.setModel(new ListComboBoxModel<String>(StandardPrice.eselonAsList()));
        AutoCompleteDecorator.decorate(comboEselon);
    }

    private void setButtonState(String state) {
        if (state.equals("New")) {
            btAdd.setEnabled(false);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(true);
            btCancel.setEnabled(true);
        } else if (state.equals("Save")) {
            btAdd.setEnabled(true);
            btEdit.setEnabled(true);
            btDelete.setEnabled(true);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);

        } else {
            btAdd.setEnabled(true);
            btEdit.setEnabled(false);
            btDelete.setEnabled(false);
            btSave.setEnabled(false);
            btCancel.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == btAdd) {
            onNew();
        } else if (obj == btEdit) {
            onEdit();
        } else if (obj == btDelete) {
            onDelete();
        } else if (obj == btSave) {
            onSave();
        } else if (obj == btCancel) {
            onCancel();
        }
    }

    private void contructNumberField() {
        NumberFormat amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(0);
        amountDisplayFormat.setMaximumFractionDigits(2);
        amountDisplayFormat.setCurrency(Currency.getInstance(new Locale("in","id","id")));
        NumberFormat amountEditFormat = NumberFormat.getNumberInstance();
        amountEditFormat.setCurrency(Currency.getInstance(new Locale("in","id","id")));

        fieldPrice = new JXFormattedTextField();
        fieldPrice.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountDisplayFormat),
                new NumberFormatter(amountEditFormat)));
    }

    private StandardPrice getStandardPrice() throws MotekarException {
        errorString = new StringBuilder();

        String name = fieldName.getText();

        if (name.equals("")) {
            errorString.append("<br>- Nama</br>");
        }
        
        Integer eselon = comboEselon.getSelectedIndex();
        
        if (eselon == 0) {
            errorString.append("<br>- Eselon</br>");
        }

        String departure = fieldDeparture.getText();

        if (departure.equals("")) {
            errorString.append("<br>- Dari</br>");
        }

        String destination = fieldDestination.getText();

        if (destination.equals("")) {
            errorString.append("<br>- Tujuan</br>");
        }

        Integer trType = comboTranscType.getSelectedIndex();

        if (trType == 0) {
            errorString.append("<br>- Tipe</br>");
        }

        Integer trpType = comboTransportType.getSelectedIndex();

        if (trpType == 0) {
            errorString.append("<br>- Transportasi</br>");
        }

        BigDecimal pr = BigDecimal.ZERO;

        Object prObj = fieldPrice.getValue();

        if (prObj instanceof Long) {
            pr = BigDecimal.valueOf((Long) prObj);
        } else if (prObj instanceof Double) {
            pr = BigDecimal.valueOf((Double) prObj);
        } else if (prObj instanceof BigDecimal) {
            pr = (BigDecimal) prObj;
        }

        if (pr.equals(BigDecimal.ZERO)) {
            errorString.append("<br>- Harga</br>");
        }

        if (errorString.length() > 0) {
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        StandardPrice price = new StandardPrice();
        price.setTransactionName(name);
        price.setEselon(eselon);
        price.setDeparture(departure);
        price.setDestination(destination);
        price.setPrice(pr);
        price.setTransactionType(trType);
        price.setTransportType(trpType);
        price.setNotes(fieldNotes.getText());

        return price;
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Standard Harga");
        defineCustomFocusTraversalPolicy();
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        fieldName.selectAll();
        statusLabel.setText("Ubah Standard Harga");
        defineCustomFocusTraversalPolicy();
    }

    private void onDelete() {
        Object[] options = {"Ya", "Tidak"};
        int choise = JOptionPane.showOptionDialog(this,
                " Anda yakin menghapus data ini ? (Y/T)",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[1]);
        if (choise == JOptionPane.YES_OPTION) {
            try {
                logic.deleteStandardPrice(mainframe.getSession(), selectedPrice.getIndex());
                priceList.removeSelected(selectedPrice);
                clearForm();
            } catch (SQLException ex) {
                ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan atau Error Ketika menghapus data",
                        null, "ERROR", ex, Level.ALL, null);
                JXErrorPane.showDialog(this, info);
            }
        }
    }

    private void onSave() {
        try {
            StandardPrice newPrice = getStandardPrice();

            if (isnew) {
                newPrice = logic.insertStandardPrice(mainframe.getSession(), newPrice);
                isnew = false;
                iseditable = false;
                priceList.addStandardPrice(newPrice);
                selectedPrice = newPrice;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newPrice = logic.updateStandardPrice(mainframe.getSession(), selectedPrice, newPrice);
                isedit = false;
                iseditable = false;
                priceList.updateStandardPrice(newPrice);
                setFormState();
                setButtonState("Save");
            }
            statusLabel.setText("Ready");
            mainframe.setFocusTraversalPolicy(null);
        } catch (MotekarException ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    ex.getMessage(), "Error", null, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        } catch (Exception ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi kesalahan pada saat menyimpan data ",
                    null, "Error", ex, Level.ALL, null);
            JXErrorPane.showDialog(this, info);
        }
    }

    private void onCancel() {
        iseditable = false;
        isedit = false;
        isnew = false;
        setFormState();
        if (priceList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
        mainframe.setFocusTraversalPolicy(null);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedPrice = priceList.getSelectedStandardPrice();
            setFormValues();
        }
    }

    private void defineCustomFocusTraversalPolicy() {
        ArrayList<Component> comp = new ArrayList<Component>();
        comp.add(fieldName);
        comp.add(comboEselon.getEditor().getEditorComponent());
        comp.add(fieldDeparture);
        comp.add(fieldDestination);
        comp.add(comboTranscType.getEditor().getEditorComponent());
        comp.add(comboTransportType.getEditor().getEditorComponent());
        comp.add(fieldPrice);
        comp.add(fieldNotes);

        MotekarFocusTraversalPolicy policy = new MotekarFocusTraversalPolicy(comp);
        mainframe.setFocusTraversalPolicy(policy);
    }

    private void setFormValues() {
        if (selectedPrice != null) {
            fieldName.setText(selectedPrice.getTransactionName());
            comboEselon.setSelectedIndex(selectedPrice.getEselon());
            fieldDeparture.setText(selectedPrice.getDeparture());
            fieldDestination.setText(selectedPrice.getDestination());
            fieldPrice.setValue(selectedPrice.getPrice());
            fieldNotes.setText(selectedPrice.getNotes());

            comboTranscType.setSelectedIndex(selectedPrice.getTransactionType());
            comboTransportType.setSelectedIndex(selectedPrice.getTransportType());

            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private class StandardPriceList extends JXList {

        private Icon CAR_ICON = Mainframe.getResizableIconFromSource("resource/Cars.png", new Dimension(36, 36));
        private Icon PLANE_ICON = Mainframe.getResizableIconFromSource("resource/Mini_Plane.png", new Dimension(36, 36));
        private Icon BUS_ICON = Mainframe.getResizableIconFromSource("resource/Travel_Bus.png", new Dimension(36, 36));
        private Icon TRAIN_ICON = Mainframe.getResizableIconFromSource("resource/Train.png", new Dimension(36, 36));
        private Icon SAIL_ICON = Mainframe.getResizableIconFromSource("resource/Sailing_Ship.png", new Dimension(36, 36));
        private Icon OTHER_ICON = Mainframe.getResizableIconFromSource("resource/OtherTrans.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public StandardPriceList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new StandardPricePanel.LoadStandardPrice((DefaultListModel) getModel());
            progressListener = new StandardPricePanel.PriceProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public StandardPrice getSelectedStandardPrice() {
            StandardPrice price = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof StandardPrice) {
                price = (StandardPrice) obj;
            }
            return price;
        }

        public void addStandardPrice(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(price);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateStandardPrice(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(price, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<StandardPrice> prices) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(prices);
            filter();
        }

        public void removeSelected(StandardPrice price) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(price);
            filter();
        }

        @Override
        public ListCellRenderer getCellRenderer() {
            return new DefaultListRenderer(new StringValue() {

                public String getString(Object o) {
                    return o.toString();
                }
            }, new IconValue() {

                public Icon getIcon(Object o) {
                    StandardPrice price = null;
                    if (o instanceof StandardPrice) {
                        price = (StandardPrice) o;
                    }

                    if (price != null) {
                        if (price.getTransportType() == StandardPrice.TYPE_CAR) {
                            return StandardPricePanel.StandardPriceList.this.CAR_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_BUS || 
                                price.getTransportType() == StandardPrice.TYPE_LAND) {
                            return StandardPricePanel.StandardPriceList.this.BUS_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_PLANE || 
                                price.getTransportType() == StandardPrice.TYPE_AIR) {
                            return StandardPricePanel.StandardPriceList.this.PLANE_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_SAILING || 
                                price.getTransportType() == StandardPrice.TYPE_SEA) {
                            return StandardPricePanel.StandardPriceList.this.SAIL_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_TRAIN) {
                            return StandardPricePanel.StandardPriceList.this.TRAIN_ICON;
                        } else if (price.getTransportType() == StandardPrice.TYPE_LAND_AIR || 
                                price.getTransportType() == StandardPrice.TYPE_LAND_SEA || 
                                price.getTransportType() == StandardPrice.TYPE_LAND_SEA_AIR) {
                            return StandardPricePanel.StandardPriceList.this.OTHER_ICON;
                        }
                    }

                    return StandardPricePanel.StandardPriceList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadStandardPrice extends SwingWorker<DefaultListModel, StandardPrice> {

        private DefaultListModel model;
        private Exception exception;

        public LoadStandardPrice(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<StandardPrice> chunks) {
            mainframe.stopInActiveListener();
            for (StandardPrice price : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Harga " + price.getTransactionName());
                model.addElement(price);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<StandardPrice> price = logic.getStandardPrice(mainframe.getSession());

                double progress = 0.0;
                if (!price.isEmpty()) {
                    for (int i = 0; i < price.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / price.size();
                        setProgress((int) progress);
                        publish(price.get(i));
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
                ErrorInfo info = new ErrorInfo("Kesalahan", e.getMessage(),
                        null, "ERROR", e, Level.ALL, null);
                JXErrorPane.showDialog(StandardPricePanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class PriceProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private PriceProgressListener() {
        }

        PriceProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
            this.progressBar.setVisible(true);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String strPropertyName = evt.getPropertyName();
            if ("progress".equals(strPropertyName)) {
                progressBar.setIndeterminate(false);
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            } else if ("state".equals(strPropertyName) && worker.getState() == SwingWorker.StateValue.DONE) {
                this.progressBar.setVisible(false);
                this.progressBar.setValue(0);
                statusLabel.setText("Ready");
            }
        }
    }
}
