package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.motekar.project.civics.archieve.master.objects.ItemStandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemStandardPricePanel extends JXPanel implements ActionListener, ListSelectionListener {

    private ArchieveMainframe mainframe = null;
    private MasterBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private ItemStandardPriceList itemStandardPriceList = new ItemStandardPriceList();
    private JXTextField fieldName = new JXTextField();
    private JXTextArea fieldSpecification = new JXTextArea();
    private JXTextField fieldItemsUnit = new JXTextField();
    private JXFormattedTextField fieldPrice;
    private JXTextArea fieldDescription = new JXTextArea();
    private JCommandButton btAdd = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_add.png"));
    private JCommandButton btEdit = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_compose.png"));
    private JCommandButton btSave = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_accept.png"));
    private JCommandButton btDelete = new JCommandButton(Mainframe.getResizableIconFromSource("resource/blog_delete.png"));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/block_blue.png"));
    private boolean iseditable = false;
    private boolean isnew = false;
    private boolean isedit = false;
    private ItemStandardPrice selectedItemStandardPrice = null;
    private LoadItemStandardPrice worker;
    private ItemStandardPriceProgressListener progressListener;
    private StringBuilder errorString = new StringBuilder();

    public ItemStandardPricePanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MasterBusinessLogic(mainframe.getConnection());
        construct();
        itemStandardPriceList.loadData();
    }

    private JXPanel createLeftComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Standar Harga Barang");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(new JScrollPane(itemStandardPriceList), BorderLayout.CENTER);
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
                + "Fasilitas Standar Harga Barang merupakan Form Input Standar Harga Barang.\n\n"
                + "Tambah Standar Harga Barang\n"
                + "Untuk menambah Standar Harga Barang klik tombol paling kiri "
                + "kemudian isi data Standar Harga Barang baru yang akan ditambah "
                + "kemudian klik tombol simpan (tombol tengah) untuk menyimpan "
                + "atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan pencatatan\n\n"
                + "Edit Standar Harga Barang\n"
                + "Untuk merubah Standar Harga Barang klik tombol kedua dari kiri "
                + "kemudian ubah data Standar Harga Barang, kemudian klik tombol simpan (tombol tengah) "
                + "untuk menyimpan atau klik tombol batal (tombol paling kiri) "
                + "untuk melakukan pembatalan perubahan\n\n"
                + "Hapus Standar Harga Barang\n"
                + "Untuk menghapus Standar Harga Barang klik tombol paling kanan "
                + "kemudian akan muncul peringatan untuk menghapus Standar Harga Barang "
                + "tersebut, pilih Ya untuk mengapus atau pilih Tidak untuk "
                + "membatalkan penghapusan";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(300);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Tambah / Edit / Hapus Standar Harga Barang");
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
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,30px",
                "pref,5px,pref,fill:default:grow,5px,"
                + "pref,5px,pref,5px,pref,fill:default:grow,50px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(fieldSpecification);
        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(fieldDescription);

        lm.setRowGroups(new int[][]{{1, 3}});

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Nama Barang", cc.xy(1, 1));
        builder.add(fieldName, cc.xy(3, 1));

        builder.addLabel("Spesifikasi", cc.xy(1, 3));
        builder.add(scPane, cc.xywh(3, 3, 1, 2));

        builder.addLabel("Satuan", cc.xy(1, 6));
        builder.add(fieldItemsUnit, cc.xy(3, 6));

        builder.addLabel("Harga (Rp.)", cc.xy(1, 8));
        builder.add(fieldPrice, cc.xy(3, 8));

        builder.addLabel("Keterangan", cc.xy(1, 10));
        builder.add(scPane2, cc.xywh(3, 10, 1, 2));

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

        contructNumberField();

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

        itemStandardPriceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemStandardPriceList.setAutoCreateRowSorter(true);
        itemStandardPriceList.addListSelectionListener(this);

        btAdd.addActionListener(this);
        btEdit.addActionListener(this);
        btDelete.addActionListener(this);
        btSave.addActionListener(this);
        btCancel.addActionListener(this);

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
    }

    public void filter() {
        itemStandardPriceList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    private void setFormState() {
        fieldName.setEnabled(iseditable);
        fieldSpecification.setEnabled(iseditable);
        fieldItemsUnit.setEnabled(iseditable);
        fieldPrice.setEnabled(iseditable);
        fieldDescription.setEnabled(iseditable);

        fieldSearch.setEnabled(!iseditable);
        itemStandardPriceList.setEnabled(!iseditable);
    }

    private void clearForm() {
        fieldName.setText("");
        fieldSpecification.setText("");
        fieldItemsUnit.setText("");
        fieldPrice.setValue(BigDecimal.ZERO);
        fieldDescription.setText("");
        if (fieldName.isEnabled()) {
            fieldName.requestFocus();
            fieldName.selectAll();
        }
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

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            selectedItemStandardPrice = itemStandardPriceList.getSelectedItemStandardPrice();
            setFormValues();
        }
    }

    private void onNew() {
        iseditable = true;
        isnew = true;
        setFormState();
        setButtonState("New");
        clearForm();
        statusLabel.setText("Tambah Standar Harga Barang");
    }

    private void onEdit() {
        iseditable = true;
        isedit = true;
        setFormState();
        setButtonState("New");
        fieldName.requestFocus();
        statusLabel.setText("Ubah Standar Harga Barang");
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
                logic.deleteItemStandardPrice(mainframe.getSession(), selectedItemStandardPrice);
                itemStandardPriceList.removeSelected(selectedItemStandardPrice);
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
            ItemStandardPrice newItemStandardPrice = getItemStandardPrice();

            if (isnew) {
                newItemStandardPrice = logic.insertItemStandardPrice(mainframe.getSession(), newItemStandardPrice);
                isnew = false;
                iseditable = false;
                itemStandardPriceList.addItemStandardPrice(newItemStandardPrice);
                selectedItemStandardPrice = newItemStandardPrice;
                setFormState();
                setButtonState("Save");
            } else if (isedit) {
                newItemStandardPrice = logic.updateItemStandardPrice(mainframe.getSession(), selectedItemStandardPrice, newItemStandardPrice);
                isedit = false;
                iseditable = false;
                itemStandardPriceList.updateItemStandardPrice(newItemStandardPrice);
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
        if (itemStandardPriceList.getElementCount() > 0) {
            setButtonState("Save");
        } else {
            setButtonState("");
        }
        setFormValues();
        statusLabel.setText("Ready");
    }

    private void setFormValues() {
        if (selectedItemStandardPrice != null) {
            fieldName.setText(selectedItemStandardPrice.getItemName());
            fieldSpecification.setText(selectedItemStandardPrice.getSpecification());
            fieldItemsUnit.setText(selectedItemStandardPrice.getItemsUnit());
            fieldPrice.setValue(selectedItemStandardPrice.getPrice());
            fieldDescription.setText(selectedItemStandardPrice.getDescription());
            setButtonState("Save");
        } else {
            clearForm();
            setButtonState("");
        }
    }

    private ItemStandardPrice getItemStandardPrice() throws MotekarException {
        String itemName = fieldName.getText();
        String specification = fieldSpecification.getText();
        String itemsUnit = fieldItemsUnit.getText();
        
        BigDecimal price = BigDecimal.ZERO;

        Object prObj = fieldPrice.getValue();

        if (prObj instanceof Long) {
            price = BigDecimal.valueOf((Long) prObj);
        } else if (prObj instanceof Double) {
            price = BigDecimal.valueOf((Double) prObj);
        } else if (prObj instanceof BigDecimal) {
            price = (BigDecimal) prObj;
        }
        
        String description = fieldDescription.getText();

        if (itemName.equals("")) {
            errorString.append("<br>- Nama Barang</br>");
            throw new MotekarException("<html>Harap diperhatikan untuk diisi : " + errorString.toString() + "</html>");
        }

        ItemStandardPrice itemStandardPrice = new ItemStandardPrice();

        itemStandardPrice.setItemName(itemName);
        itemStandardPrice.setSpecification(specification);
        itemStandardPrice.setItemsUnit(itemsUnit);
        itemStandardPrice.setPrice(price);
        itemStandardPrice.setDescription(description);

        return itemStandardPrice;
    }

    private class ItemStandardPriceList extends JXList {

        private Icon STANDARDPRICE_ICON = Mainframe.getResizableIconFromSource("resource/ItemsPrice.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public ItemStandardPriceList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadItemStandardPrice((DefaultListModel) getModel());
            progressListener = new ItemStandardPriceProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public ItemStandardPrice getSelectedItemStandardPrice() {
            ItemStandardPrice itemStandardPrice = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof ItemStandardPrice) {
                itemStandardPrice = (ItemStandardPrice) obj;
            }
            return itemStandardPrice;
        }

        public void addItemStandardPrice(ItemStandardPrice itemStandardPrice) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.addElement(itemStandardPrice);
            setSelectedIndex(model.getSize() - 1);
        }

        public void updateItemStandardPrice(ItemStandardPrice itemStandardPrice) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.setElementAt(itemStandardPrice, getSelectedIndex());
        }

        public void removeSelecteds(ArrayList<ItemStandardPrice> itemStandardPrices) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(itemStandardPrices);
            filter();
        }

        public void removeSelected(ItemStandardPrice itemStandardPrice) {
            DefaultListModel model = (DefaultListModel) getModel();
            model.removeElement(itemStandardPrice);
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

                    ItemStandardPrice itemStandardPrice = null;

                    if (o instanceof ItemStandardPrice) {
                        itemStandardPrice = (ItemStandardPrice) o;
                    }

                    if (itemStandardPrice != null) {
                        return ItemStandardPriceList.this.STANDARDPRICE_ICON;
                    }

                    return ItemStandardPriceList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadItemStandardPrice extends SwingWorker<DefaultListModel, ItemStandardPrice> {

        private DefaultListModel model;
        private Exception exception;

        public LoadItemStandardPrice(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<ItemStandardPrice> chunks) {
            mainframe.stopInActiveListener();
            for (ItemStandardPrice itemStandardPrice : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Standar Harga Barang " + itemStandardPrice.getItemName());
                model.addElement(itemStandardPrice);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<ItemStandardPrice> itemStandardPrices = logic.getItemStandardPrice(mainframe.getSession());

                double progress = 0.0;
                if (!itemStandardPrices.isEmpty()) {
                    for (int i = 0; i < itemStandardPrices.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / itemStandardPrices.size();

                        ItemStandardPrice itemStandardPrice = itemStandardPrices.get(i);
                        itemStandardPrice.setStyled(true);
                        
                        setProgress((int) progress);
                        publish(itemStandardPrice);
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
                JXErrorPane.showDialog(ItemStandardPricePanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class ItemStandardPriceProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private ItemStandardPriceProgressListener() {
        }

        ItemStandardPriceProgressListener(JProgressBar progressBar) {
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
