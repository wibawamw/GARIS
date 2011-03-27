package org.motekar.project.civics.archieve.master.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.sqlapi.MasterBusinessLogic;
import org.motekar.util.user.ui.Mainframe;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeePickDlg implements ActionListener {

    private JFrame frame;
    private JProgressBar pBar = new JProgressBar();
    private MasterBusinessLogic logic;
    private Long session = null;
    private JXTextField fieldSearch = new JXTextField();
    private LoadEmployee worker;
    private EmployeeProgressListener progressListener;
    private EmployeeList employeeList = new EmployeeList();
    private JCommandButton btOK = new JCommandButton(Mainframe.getResizableIconFromSource("resource/checkmark.png", new Dimension(32, 32)));
    private JCommandButton btCancel = new JCommandButton(Mainframe.getResizableIconFromSource("resource/cancel_sign.png", new Dimension(32, 32)));
    private JDialog dlg;
    private ArrayList<Employee> selectedEmployees = new ArrayList<Employee>();
    private Employee selectedEmployee = null;
    private int response = JOptionPane.NO_OPTION;
    private boolean multi = false;

    public EmployeePickDlg(JFrame frame, Long session, Connection conn, boolean multi) {
        this.frame = frame;
        this.session = session;
        this.multi = multi;
        logic = new MasterBusinessLogic(conn);
        employeeList.loadData();
    }

    public void showDialog() {
        dlg = new JDialog(frame);

        dlg.setTitle("Pilih Pegawai");
        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(construct(), BorderLayout.CENTER);
        dlg.setSize(new Dimension(350, 500));
        dlg.setModal(true);
        dlg.setResizable(false);

        registerGlobalKeys();

        dlg.setLocation(WindowUtils.getPointForCentering(dlg));
        dlg.setVisible(true);
    }

    public void filter() {
        employeeList.setRowFilter(new RowFilter<ListModel, Integer>() {

            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(fieldSearch.getText().toLowerCase());
            }
        });
    }

    protected JPanel createMainPanel() {
        FormLayout lm = new FormLayout(
                "pref,10px,fill:default:grow,10px",
                "pref,5px,fill:default:grow,5px,pref,5px");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        JScrollPane scPane = new JScrollPane();
        scPane.setViewportView(employeeList);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Cari", cc.xy(1, 1));
        builder.add(fieldSearch, cc.xy(3, 1));

        builder.add(scPane, cc.xywh(1, 3, 3, 2));

        builder.add(pBar, cc.xyw(1, 5, 3));

        return builder.getPanel();
    }

    private Component construct() {

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

        if (multi) {
            employeeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        employeeList.setAutoCreateRowSorter(true);

        JXPanel panel = new JXPanel();
        panel.setLayout(new BorderLayout());

        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
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

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btOK) {
            onOK();
        } else if (source == btCancel) {
            if (worker != null) {
                worker.cancel(true);
            }
            dlg.dispose();
        }
    }

    public int getResponse() {
        return response;
    }

    private void onOK() {
        response = JOptionPane.YES_OPTION;
        if (worker != null) {
            worker.cancel(true);
        }
        selectedEmployee = employeeList.getSelectedEmployee();
        selectedEmployees = employeeList.getSelectedEmployees();
        dlg.dispose();
    }

    public ArrayList<Employee> getSelectedEmployees() {
        return selectedEmployees;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    private void registerGlobalKeys() {
        dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        dlg.getRootPane().getActionMap().put("escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (worker != null) {
                    worker.cancel(true);
                }
                dlg.dispose();
            }
        });
    }

    private class EmployeeList extends JXList {

        private Icon MALE_ICON = Mainframe.getResizableIconFromSource("resource/user_male.png", new Dimension(36, 36));
        private Icon FEMALE_ICON = Mainframe.getResizableIconFromSource("resource/user_female.png", new Dimension(36, 36));
        private Icon GOVERNOR_ICON = Mainframe.getResizableIconFromSource("resource/business_user.png", new Dimension(36, 36));
        private Icon NULL_ICON = Mainframe.getResizableIconFromSource("resource/Question.png", new Dimension(36, 36));

        public EmployeeList() {
            DefaultListModel model = new DefaultListModel();
            setModel(model);
        }

        public void loadData() {
            worker = new LoadEmployee((DefaultListModel) getModel());
            progressListener = new EmployeeProgressListener(pBar);
            worker.addPropertyChangeListener(progressListener);
            worker.execute();
        }

        public Employee getSelectedEmployee() {
            Employee employee = null;
            Object obj = this.getSelectedValue();
            if (obj instanceof Employee) {
                employee = (Employee) obj;
            }
            return employee;
        }

        public ArrayList<Employee> getSelectedEmployees() {

            ArrayList<Employee> employees = new ArrayList<Employee>();

            Object[] objects = this.getSelectedValues();

            if (objects != null) {
                Employee employee = null;
                for (Object obj : objects) {
                    if (obj instanceof Employee) {
                        employee = (Employee) obj;
                        employees.add(employee);
                    }
                }
            }


            return employees;
        }

        @Override
        public ListCellRenderer getCellRenderer() {
            return new DefaultListRenderer(new StringValue() {

                public String getString(Object o) {
                    return o.toString();
                }
            }, new IconValue() {

                public Icon getIcon(Object o) {

                    Employee employee = null;

                    if (o instanceof Employee) {
                        employee = (Employee) o;
                    }

                    if (employee != null) {
                        if (employee.isGorvernor()) {
                            return EmployeeList.this.GOVERNOR_ICON;
                        } else {
                            if (employee.getSex() == Employee.MALE) {
                                return EmployeeList.this.MALE_ICON;
                            } else {
                                return EmployeeList.this.FEMALE_ICON;
                            }
                        }
                    }

                    return EmployeeList.this.NULL_ICON;
                }
            });
        }
    }

    private class LoadEmployee extends SwingWorker<DefaultListModel, Employee> {

        private DefaultListModel model;
        private Exception exception;

        public LoadEmployee(DefaultListModel model) {
            this.model = model;
            model.clear();
        }

        public Exception getDoInBackgroundException() {
            return exception;
        }

        @Override
        protected void process(List<Employee> chunks) {
            for (Employee employee : chunks) {
                if (isCancelled()) {
                    break;
                }
                model.addElement(employee);
            }
        }

        @Override
        protected DefaultListModel doInBackground() throws Exception {
            try {
                ArrayList<Employee> employees = logic.getAssignedEmployee(session);

                double progress = 0.0;
                if (!employees.isEmpty()) {
                    for (int i = 0; i < employees.size() && !isCancelled(); i++) {
                        progress = 100 * (i + 1) / employees.size();
                        setProgress((int) progress);
                        publish(employees.get(i));
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
            if (isCancelled()) {
                return;
            }
            setProgress(100);
        }
    }

    private class EmployeeProgressListener implements PropertyChangeListener {

        private JProgressBar progressBar;

        private EmployeeProgressListener() {
        }

        EmployeeProgressListener(JProgressBar progressBar) {
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
            }
        }
    }
}
