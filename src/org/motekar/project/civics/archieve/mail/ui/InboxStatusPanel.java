package org.motekar.project.civics.archieve.mail.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.motekar.project.civics.archieve.mail.objects.Inbox;
import org.motekar.project.civics.archieve.mail.objects.InboxDisposition;
import org.motekar.project.civics.archieve.mail.sqlapi.MailBusinessLogic;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

/**
 *
 * @author Muhamad Wibawa
 */
public class InboxStatusPanel extends JXPanel implements ActionListener {

    private ArchieveMainframe mainframe = null;
    private MailBusinessLogic logic;
    private JXMultiSplitPane splitPane = new JXMultiSplitPane();
    private JXLabel statusLabel = new JXLabel("Ready");
    private JProgressBar pbar = new JProgressBar();
    private JXTextField fieldSearch = new JXTextField();
    private JYearChooser yearChooser = new JYearChooser();
    private JMonthChooser monthChooser = new JMonthChooser();
    private JCheckBox checkBox = new JCheckBox();
    private InboxStatusTable table = new InboxStatusTable();
    private LoadInbox worker;
    private InboxProgressListener progressListener;
    private Inbox selectedInbox = null;
    private JCommandButton btInsAction = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Add.png"));
    private JCommandButton btEditAction = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Edit.png"));
    private JCommandButton btDelAction = new JCommandButton(Mainframe.getResizableIconFromSource("resource/Del.png"));
    private InboxDispositionTable dispoTable = new InboxDispositionTable();

    public InboxStatusPanel(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
        logic = new MailBusinessLogic(mainframe.getConnection());
        construct();
        table.loadInbox();
    }

    private JXPanel createCenterComponent() {

        JXTitledPanel titledPanel = new JXTitledPanel("Daftar Surat Masuk");

        JXCollapsiblePane collapasepanel = new JXCollapsiblePane();
        collapasepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        collapasepanel.setLayout(new BorderLayout());
        collapasepanel.add(createSearchPanel(), BorderLayout.NORTH);
        collapasepanel.add(createTablePanel(), BorderLayout.CENTER);
        titledPanel.setContentContainer(collapasepanel);


        return titledPanel;
    }

    private JXPanel createRightComponent() {
        JXTitledPanel titledPanel = new JXTitledPanel("Bantuan");

        JXLabel helpLabel = new JXLabel();
        helpLabel.setTextAlignment(JXLabel.TextAlignment.JUSTIFY);

        String text = "Penjelasan Singkat\n"
                + "Status surat masuk merupakan keadaan / tindak lanjut surat "
                + "setelah diterima dari luar. Bisa itu diajukan (misalkan berbentuk "
                + "proposal), disahkan maupun ditolak\n\n"
                + "Ubah Status\n"
                + "Untuk merubah status surat masuk pilih data yang akan dirubah pada "
                + "tabel sebelah kiri kemudian pilih status dengan menekan tombol "
                + "masing-masing status.\n"
                + "Status yang tersedia adalah Diterima yaitu status setelah data "
                + "surat masuk dibuat dan disimpan. Diajukan jika surat masuk berupa "
                + "proposal dari pihak tertentu. Kemudian Disahkan jika proposal / "
                + "surat masuk tersebut disahkan dan ditindak lanjuti. "
                + "Atau ditolak jika memang proposal tersebut tidak diperlukan.\n"
                + "Jika status sudah dirubah maka tombol yang status yang baru "
                + "menjadi tidak aktif, menandakan bahwa status surat saat ini adalah "
                + "status baru tersebut";


        helpLabel.setLineWrap(true);
        helpLabel.setMaxLineSpan(350);
        helpLabel.setText(text);

        JXTaskPaneContainer container = new JXTaskPaneContainer();

        JScrollPane scPane = new JScrollPane();
        scPane.getViewport().add(container);
        scPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTaskPane task = new JXTaskPane();
        task.setTitle("Ubah Status Surat Masuk");
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

    private JPanel createSearchPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px, fill:default:grow,5px,pref,5px,pref,pref,200dlu", "pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        monthChooser.setYearChooser(yearChooser);
        monthChooser.setEnabled(checkBox.isSelected());
        yearChooser.setEnabled(checkBox.isSelected());

        checkBox.addActionListener(this);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xyw(3, 1, 2));

        builder.add(checkBox, cc.xy(5, 1));
        builder.add(monthChooser, cc.xy(7, 1));
        builder.add(yearChooser, cc.xy(8, 1));

        return builder.getPanel();
    }

    private Component createTablePanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,fill:default:grow,5px,5px,pref,5px,pref,fill:default:grow,5px,10px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        lm.setRowGroups(new int[][]{{1, 2, 5, 7, 8}});

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(table);

        JScrollPane scPane2 = new JScrollPane();
        scPane2.setViewportView(dispoTable);

        CellConstraints cc = new CellConstraints();

        builder.add(scPane, cc.xywh(1, 1, 3, 3));

        builder.addSeparator("Disposisi Surat Masuk", cc.xywh(1, 5, 3, 3));

        builder.add(createStrip(1.0, 1.0), cc.xy(1, 7));
        builder.add(scPane2, cc.xywh(1, 8, 3, 2));

        return builder.getPanel();
    }

    private JCommandButtonStrip createStrip(double hgapScaleFactor,
            double vgapScaleFactor) {

        RichTooltip addTooltip = new RichTooltip();
        addTooltip.setTitle("Tambah Disposisi");

        btInsAction.setActionRichTooltip(addTooltip);

        RichTooltip editTooltip = new RichTooltip();
        editTooltip.setTitle("Ubah Disposisi");

        btEditAction.setActionRichTooltip(editTooltip);

        RichTooltip deleteTooltip = new RichTooltip();
        deleteTooltip.setTitle("Hapus Disposisi");

        btDelAction.setActionRichTooltip(deleteTooltip);


        JCommandButtonStrip buttonStrip = new JCommandButtonStrip();
        buttonStrip.setHGapScaleFactor(hgapScaleFactor);
        buttonStrip.setVGapScaleFactor(vgapScaleFactor);
        buttonStrip.add(btInsAction);
        buttonStrip.add(btEditAction);
        buttonStrip.add(btDelAction);


        return buttonStrip;
    }

    private void construct() {

        fieldSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
                table.clearSelection();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
                table.clearSelection();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
                table.clearSelection();
            }
        });

        monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadInbox();
            }
        });

        yearChooser.addPropertyChangeListener("year", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                table.loadInbox();
            }
        });

        table.addPropertyChangeListener("swingx.clicked", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                selectedInbox = table.getSelectedInbox();
                dispoTable.clear();
                if (selectedInbox != null) {
                    try {
                        setButtonState(true);
                        ArrayList<InboxDisposition> dispositions = logic.getInboxDisposition(mainframe.getSession(), selectedInbox.getIndex());
                        dispoTable.addDisposition(dispositions);
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });

        btInsAction.addActionListener(this);
        btEditAction.addActionListener(this);
        btDelAction.addActionListener(this);

        SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
        SubstanceColorScheme color = skin.getColorScheme(table, ComponentState.DEFAULT);

        table.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        table.setColumnControlVisible(true);
        table.setShowGrid(false, false);

        color = skin.getColorScheme(dispoTable, ComponentState.DEFAULT);

        dispoTable.addHighlighter(HighlighterFactory.createSimpleStriping(color.getWatermarkDarkColor()));
        dispoTable.setColumnControlVisible(true);
        dispoTable.setShowGrid(false, false);

        table.setAutoCreateRowSorter(true);

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String LAY_OUT = "(ROW weight=1.0 (LEAF name=editor1 weight=0.7)"
                + "(LEAF name=editor2 weight=0.3))";

        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(LAY_OUT);
        splitPane.getMultiSplitLayout().setModel(modelRoot);

        splitPane.getMultiSplitLayout().setLayoutByWeight(true);

        splitPane.setPreferredSize(modelRoot.getBounds().getSize());

        JXPanel panel = createRightComponent();

        splitPane.add(createCenterComponent(), "editor1");
        splitPane.add(panel, "editor2");

        panel.setVisible(true);

        splitPane.setDividerSize(1);

        splitPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setButtonState(false);
    }

    private void setButtonState(boolean flag) {
        btInsAction.setEnabled(flag);
        btEditAction.setEnabled(flag);
        btDelAction.setEnabled(flag);
    }

    public void filter() {
        table.setRowFilter(new RowFilter<TableModel, Integer>() {

            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == checkBox) {
            monthChooser.setEnabled(checkBox.isSelected());
            yearChooser.setEnabled(checkBox.isSelected());
            table.loadInbox();
        } else if (source == btInsAction) {
            InboxDispositionDlg dlg = new InboxDispositionDlg(mainframe);
            dlg.showDialog();
            if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                InboxDisposition disposition = dlg.getDisposition();
                if (disposition != null) {
                    try {
                        disposition.setInboxIndex(selectedInbox.getIndex());
                        logic.insertInboxDisposition(mainframe.getSession(), selectedInbox, disposition);
                        dispoTable.addDisposition(disposition);
                    } catch (SQLException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

            }
        } else if (source == btEditAction) {
            if (selectedInbox != null) {
                InboxDisposition disposition = dispoTable.getSelectedDisposition();
                InboxDispositionDlg dlg = new InboxDispositionDlg(mainframe, disposition);
                dlg.showDialog();
                if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                    InboxDisposition newDisposition = dlg.getDisposition();
                    if (newDisposition != null) {
                        try {
                            newDisposition.setInboxIndex(selectedInbox.getIndex());
                            logic.updateInboxDisposition(mainframe.getSession(), disposition, newDisposition);
                            dispoTable.updateDisposition(disposition, newDisposition);
                        } catch (SQLException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        } else if (source == btDelAction) {
            if (selectedInbox != null) {
                try {
                    InboxDisposition disposition = dispoTable.getSelectedDisposition();
                    Object[] options = {"Ya", "Tidak"};
                    int choise = JOptionPane.showOptionDialog(this,
                            " Anda yakin menghapus semua data ini ? (Y/T)",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            options, options[1]);
                    if (choise == JOptionPane.YES_OPTION) {
                        logic.deleteInboxDisposition(mainframe.getSession(), disposition);
                        dispoTable.removeDisposition(disposition);
                    }
                } catch (SQLException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private class InboxStatusTable extends JXTable {

        private InboxStatusTableModel model = new InboxStatusTableModel();

        public InboxStatusTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public void loadInbox() {
            worker = new LoadInbox(model);
            progressListener = new InboxProgressListener(pbar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Inbox getSelectedInbox() {
            Inbox inbox = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 1);
            if (obj instanceof Inbox) {
                inbox = (Inbox) obj;
            }

            return inbox;
        }

        public void updateSelectedInbox() {
            int row = getSelectedRow();
            model.updateRow(row, getSelectedInbox(), selectedInbox);
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 0) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy")), JLabel.CENTER);
            } else if (column == 1) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            } else if (column == 2) {
                return super.getCellRenderer(row, column);
            } else if (column == 3) {
                return super.getCellRenderer(row, column);
            } else if (column == 4) {
                return super.getCellRenderer(row, column);
            } else if (column == 5) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy")), JLabel.CENTER);
            } else if (column == 6) {
                return super.getCellRenderer(row, column);
            }
            return super.getCellRenderer(row, column);
        }
    }

    private static class InboxStatusTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 7;
        private static final String[] columnIds = {"Tanggal Surat", "Nomor Surat", "Perihal", "Pengirim", "Alamat Pengirim",
            "Tanggal Disposisi", "Penerima"};
        private ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(ArrayList<Inbox> inboxs) {
            int first = inboxs.size();
            int last = first + inboxs.size() - 1;
            inboxs.addAll(inboxs);
            fireTableRowsInserted(first, last);
        }

        public void add(Inbox inbox) {
            insertRow(getRowCount(), inbox);
        }

        public void insertRow(int row, Inbox inbox) {
            inboxs.add(row, inbox);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(int row, Inbox oldInbox, Inbox newInbox) {
            int index = inboxs.indexOf(oldInbox);
            inboxs.set(index, newInbox);
            fireTableRowsUpdated(index, index);
        }

        public void remove(Inbox inbox) {
            int index = inboxs.indexOf(inbox);
            inboxs.remove(inbox);
            fireTableRowsDeleted(index, index);
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
                inboxs.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                inboxs.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            inboxs.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (inboxs.get(i) == null) {
                    inboxs.set(i, new Inbox());
                }
            }
        }

        @Override
        public int getRowCount() {
            return inboxs.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public Inbox getInbox(int row) {
            if (!inboxs.isEmpty()) {
                return inboxs.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Inbox inbox = getInbox(row);
            switch (column) {
                case 0:
                    inbox.setMailDate((Date) aValue);
                    break;
                case 1:
                    inbox = (Inbox) aValue;
                    break;
                case 2:
                    inbox.setSubject((String) aValue);
                    break;
                case 3:
                    inbox.setSender((String) aValue);
                    break;
                case 4:
                    inbox.setSenderAddress((String) aValue);
                    break;
                case 5:
                    inbox.setDispositionDate((Date) aValue);
                    break;

                case 6:
                    Division receiver = null;
                    if (aValue instanceof Division) {
                        receiver = (Division) aValue;
                    }
                    inbox.setReceipient(receiver);
                    break;

            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            Inbox inbox = getInbox(row);
            switch (column) {
                case 0:
                    toBeDisplayed = inbox.getMailDate();
                    break;
                case 1:
                    toBeDisplayed = inbox;
                    break;
                case 2:
                    toBeDisplayed = inbox.getSubject();
                    break;
                case 3:
                    toBeDisplayed = inbox.getSender();
                    break;
                case 4:
                    toBeDisplayed = inbox.getSenderAddress();
                    break;
                case 5:
                    toBeDisplayed = inbox.getDispositionDate();
                    break;
                case 6:
                    Division receiver = inbox.getReceipient();
                    receiver.setStyled(false);
                    toBeDisplayed = receiver;
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class InboxDispositionTable extends JXTable {

        private InboxDispositionTableModel model = new InboxDispositionTableModel();

        public InboxDispositionTable() {
            setModel(model);
            getTableHeader().setReorderingAllowed(false);
        }

        public void clear() {
            model.clear();
        }

        public void addDisposition(InboxDisposition disposition) {
            model.add(disposition);
        }

         public void updateDisposition(InboxDisposition oldDisposition,InboxDisposition newDisposition) {
            model.updateRow(oldDisposition, newDisposition);
        }

        public void removeDisposition(InboxDisposition disposition) {
            model.remove(disposition);
        }

        public void addDisposition(ArrayList<InboxDisposition> dispositions) {
            if (!dispositions.isEmpty()) {
                for (InboxDisposition disposition : dispositions) {
                    model.add(disposition);
                }
            }

        }

        public InboxDisposition getSelectedDisposition() {
            InboxDisposition disposition = null;

            int row = getSelectedRow();

            Object obj = model.getValueAt(row, 0);
            if (obj instanceof InboxDisposition) {
                disposition = (InboxDisposition) obj;
            }

            return disposition;
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 0) {
                return super.getCellRenderer(row, column);
            } else if (column == 1) {
                return new DefaultTableRenderer(new FormatStringValue(new SimpleDateFormat("dd/MM/yyyy")), JLabel.CENTER);
            } else if (column == 2) {
                return new DefaultTableRenderer(new FormatStringValue(), JLabel.CENTER);
            }
            return super.getCellRenderer(row, column);
        }
    }

    private static class InboxDispositionTableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT = 3;
        private static final String[] columnIds = {"Penerima", "Tanggal Terima", "Status"};
        private ArrayList<InboxDisposition> dispositions = new ArrayList<InboxDisposition>();

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnIds[column];
        }

        public void add(InboxDisposition disposition) {
            insertRow(getRowCount(), disposition);
        }

        public void insertRow(int row, InboxDisposition disposition) {
            dispositions.add(row, disposition);
            justifyRows(row, row + 1);
            fireTableRowsInserted(row, row);
        }

        public void updateRow(InboxDisposition oldDisposition, InboxDisposition newDisposition) {
            int index = dispositions.indexOf(oldDisposition);
            dispositions.set(index, newDisposition);
            fireTableRowsUpdated(index, index);
        }

        public void remove(InboxDisposition disposition) {
            int index = dispositions.indexOf(disposition);
            dispositions.remove(disposition);
            fireTableRowsDeleted(index, index);
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
                dispositions.clear();
                fireTableRowsDeleted(rowCount, old - 1);
            } else {
                dispositions.ensureCapacity(rowCount);
                justifyRows(old, rowCount);
                fireTableRowsInserted(old, rowCount - 1);
            }
        }

        private void justifyRows(int from, int to) {
            dispositions.ensureCapacity(getRowCount());

            for (int i = from; i < to; i++) {
                if (dispositions.get(i) == null) {
                    dispositions.set(i, new InboxDisposition());
                }
            }
        }

        @Override
        public int getRowCount() {
            return dispositions.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public InboxDisposition getInboxDisposition(int row) {
            if (!dispositions.isEmpty()) {
                return dispositions.get(row);
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            InboxDisposition disposition = getInboxDisposition(row);
            switch (column) {
                case 0:
                    disposition = (InboxDisposition) aValue;
                    break;
                case 1:
                    disposition.setReceiveDate((Date) aValue);
                    break;
                case 2:
                    disposition.setStatus((Integer) aValue);
                    break;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object toBeDisplayed = "n/a";
            InboxDisposition disposition = getInboxDisposition(row);
            switch (column) {
                case 0:
                    toBeDisplayed = disposition;
                    break;
                case 1:
                    toBeDisplayed = disposition.getReceiveDate();
                    break;
                case 2:
                    toBeDisplayed = InboxDisposition.MAIL_STATUS[disposition.getStatus()];
                    break;
            }
            return toBeDisplayed;
        }
    }

    private class LoadInbox extends SwingWorker<InboxStatusTableModel, Inbox> {

        private InboxStatusTableModel model;
        private Exception exception;

        public LoadInbox(InboxStatusTableModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Inbox> chunks) {
            mainframe.stopInActiveListener();
            for (Inbox inbox : chunks) {
                if (isCancelled()) {
                    break;
                }
                statusLabel.setText("Memuat Surat Masuk Nomor " + inbox.getMailNumber());
                inbox.setStyled(false);
                model.add(inbox);
            }
        }

        @Override
        protected InboxStatusTableModel doInBackground() throws Exception {
            try {
                ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

                if (checkBox.isSelected()) {
                    inboxs = logic.getInbox(mainframe.getSession(), monthChooser.getMonth() + 1, yearChooser.getYear());
                } else {
                    inboxs = logic.getInbox(mainframe.getSession());
                }

                double progress = 0.0;
                if (!inboxs.isEmpty()) {
                    for (int i = 0; i < inboxs.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / inboxs.size();
                        setProgress((int) progress);
                        publish(inboxs.get(i));
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
                JXErrorPane.showDialog(InboxStatusPanel.this, info);
            }
            setProgress(100);
            mainframe.startInActiveListener();
        }
    }

    private class InboxProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private InboxProgressListener() {
        }

        InboxProgressListener(JProgressBar progressBar) {
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
