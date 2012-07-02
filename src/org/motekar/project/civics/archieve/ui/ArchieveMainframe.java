package org.motekar.project.civics.archieve.ui;

import org.motekar.project.civics.archieve.mail.ui.MailRibbonBand;
import org.motekar.project.civics.archieve.expedition.ui.ExpeditionRibbonBand;
import org.motekar.project.civics.archieve.master.ui.MasterDataRibbonBand;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.UserNameStore;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.report.ui.ReportRibbonBand;
import org.motekar.project.civics.archieve.sqlapi.ConnectionManager;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.util.user.misc.InactivityListener;
import org.motekar.util.user.objects.SessionType;
import org.motekar.util.user.objects.UserAccount;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;
import org.motekar.util.user.ui.BackupDialog;
import org.motekar.util.user.ui.ChangePasswordDialog;
import org.motekar.util.user.ui.LoginDialog;
import org.motekar.util.user.ui.Mainframe;
import org.motekar.util.user.ui.RestoreDialog;
import org.openide.awt.TabbedPaneFactory;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.LayeredIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessSkin;
import org.pushingpixels.substance.flamingo.ribbon.gallery.oob.ColorSchemeResizableIcon;
import org.pushingpixels.substance.flamingo.ribbon.gallery.oob.SubstanceSkinRibbonGallery;
import org.pushingpixels.substance.flamingo.ribbon.gallery.oob.WatermarkResizableIcon;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchieveMainframe extends JRibbonFrame implements ActionListener{
    private JTabbedPane mainTabPane;
    private Connection connection;

    private InactivityListener inactive;
    private ArchieveProperties properties;

    private AuthBusinessLogic auth;
    private SessionType sessionType;
    private UserAccount userAccount = new UserAccount();

    // Login
    private JXLoginPane loginPane;
    private LoginDialog loginDialog;
    private UserNameStore userNameStore;

    private JCommandButton btBackupDB = new JCommandButton("Backup Database", Mainframe.getResizableIconFromSource("resource/database_up.png"));
    private JCommandButton btRestoreDB = new JCommandButton("Restore Database", Mainframe.getResizableIconFromSource("resource/database_down.png"));
    private JCommandButton btHelp = new JCommandButton("Bantuan", Mainframe.getResizableIconFromSource("resource/help.png"));
    private JCommandButton btAbout = new JCommandButton("Tentang", Mainframe.getResizableIconFromSource("resource/info.png"));

    private KeyEventPostProcessor postProcessor;

    private NavigationRibbonBand nav1 = new NavigationRibbonBand(this, "Navigasi", null);
    private NavigationRibbonBand nav2 = new NavigationRibbonBand(this, "Navigasi", null);
    private NavigationRibbonBand nav3 = new NavigationRibbonBand(this, "Navigasi", null);

    private JPanel cardPanel = new JPanel();

    private BackgroundPanel background;

    public ArchieveMainframe() {
        super("Government Archieves Information System (GARIS) 1.0.5");
        setApplicationIcon(Mainframe.getResizableIconFromSource("resource/mail_logo.png"));
        constructMainframe();
        constructAndShowLoginPane();
    }

    private void constructMainframe() {
        createConnection();
        createCloseableTab();
        configureRibbon();
        createInactiveListener();
        installKeyEventPostProcessor();

        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setPreferredSize(new Dimension(r.width, r.height));
        setMinimumSize(new Dimension(r.width / 10, r.height / 2));
        pack();
        setLocation(r.x, r.y);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Ya", "Tidak"};

                int choise = JOptionPane.showOptionDialog(null,
                        " Anda yakin akan keluar  ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    try {
                        auth.doLogout(userAccount);
                        mainTabPane.removeAll();
                        System.exit(0);
                    } catch (SQLException ex) {
                        ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat keluar dari aplikasi",
                                null, "ERROR", ex, Level.ALL, null);
                        JXErrorPane.showDialog(null, info);
                    }

                }
            }
        });
        setVisible(true);

    }

    private void createConnection() {
        ConnectionManager connectionManager = new ConnectionManager(new File("app.xml"));
        this.connection = connectionManager.connect();
        properties = connectionManager.getConfig();
        auth = new AuthBusinessLogic(this.connection);
    }

    private void createCloseableTab() {
        mainTabPane = new JTabbedPane();
        mainTabPane = TabbedPaneFactory.createCloseButtonTabbedPane();
        mainTabPane.setFocusCycleRoot(false);
        mainTabPane.addPropertyChangeListener(TabbedPaneFactory.PROP_CLOSE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JTabbedPane pane = (JTabbedPane) evt.getSource();
                JComponent c = (JComponent) evt.getNewValue();
                pane.remove(c);
                setFocusTraversalPolicy(null);
            }
        });

        background = new BackgroundPanel(getProperties());

        constructCardPanel();
    }

    private void constructCardPanel() {
        cardPanel.setLayout(new CardLayout());
        cardPanel.add(background, "0");
        cardPanel.add(mainTabPane, "1");
    }

    private void configureRibbon() {

        JRibbon ribbon = this.getRibbon();

        MasterDataRibbonBand rBandMasterData = new MasterDataRibbonBand(this,"Master Data", null);
        ExpeditionRibbonBand rBandExpedition = new ExpeditionRibbonBand(this, "Perjalanan Dinas", null);
        MailRibbonBand mail = new MailRibbonBand(this);
        ReportRibbonBand report = new ReportRibbonBand(this);

        JRibbonBand rBandDB = new JRibbonBand("Database", null);
        rBandDB.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandDB.getControlPanel())));

        JRibbonBand rBandHelp = new JRibbonBand("Bantuan", null);
        rBandHelp.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandHelp.getControlPanel())));


        RichTooltip backupDBTooltip = new RichTooltip();
        backupDBTooltip.setTitle("Backup Database");
        backupDBTooltip.addDescriptionSection("Membuat file backup untuk database yang aktif");
        try {
            backupDBTooltip.setMainImage(ImageIO.read(Mainframe.class.getResource("/resource/database_up2.png")));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btBackupDB.setActionRichTooltip(backupDBTooltip);


        RichTooltip restoreDBTooltip = new RichTooltip();
        restoreDBTooltip.setTitle("Restore Database");
        restoreDBTooltip.addDescriptionSection("Memuat ulang database dari file backup");
        try {
            restoreDBTooltip.setMainImage(ImageIO.read(Mainframe.class.getResource("/resource/database_down2.png")));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btRestoreDB.setActionRichTooltip(restoreDBTooltip);

        RichTooltip helpTooltip = new RichTooltip();
        helpTooltip.setTitle("Bantuan");
        helpTooltip.addDescriptionSection("Membuka Arsip Bantuan Aplikasii");
        try {
            helpTooltip.setMainImage(ImageIO.read(Mainframe.class.getResource("/resource/help2.png")));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btHelp.setActionRichTooltip(helpTooltip);

        RichTooltip aboutTooltip = new RichTooltip();
        aboutTooltip.setTitle("Bantuan");
        aboutTooltip.addDescriptionSection("Tentang Aplikasi dan Kontak Informasi");
        try {
            aboutTooltip.setMainImage(ImageIO.read(Mainframe.class.getResource("/resource/info2.png")));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btAbout.setActionRichTooltip(aboutTooltip);


        btBackupDB.addActionListener(this);
        btRestoreDB.addActionListener(this);
        btHelp.addActionListener(this);
        btAbout.addActionListener(this);

        rBandDB.addCommandButton(btBackupDB, RibbonElementPriority.TOP);
        rBandDB.addCommandButton(btRestoreDB, RibbonElementPriority.MEDIUM);

        rBandHelp.addCommandButton(btHelp, RibbonElementPriority.TOP);
        rBandHelp.addCommandButton(btAbout, RibbonElementPriority.MEDIUM);

        rBandDB.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(rBandDB.getControlPanel()),
                new IconRibbonBandResizePolicy(rBandDB.getControlPanel())));
        rBandHelp.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(rBandHelp.getControlPanel()),
                new IconRibbonBandResizePolicy(rBandHelp.getControlPanel())));

        JRibbonBand skinBand = new JRibbonBand("Tema", new LayeredIcon(
                new WatermarkResizableIcon(null, 32, 32),
                new ColorSchemeResizableIcon(null, 32, 32)), null);
        SubstanceSkinRibbonGallery.addSkinGallery(skinBand);

        skinBand.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("UI")) {
                    if (background != null) {
                        background.repaintAll();
                    }
                }
            }
        });

        RibbonTask task1 = new RibbonTask("Menu Aplikasi",nav1, rBandMasterData,
                mail.getMailInboxRibbon(),mail.getMailOutboxRibbon(),rBandExpedition);

        RibbonTask task2 = new RibbonTask("Laporan-Laporan", nav2,report.getDataMasterRibbon(),report.getMailReportRibbon(),
                report.getExpeditionReport());
        RibbonTask task3 = new RibbonTask("Konfigurasi & Bantuan",nav3, rBandDB, rBandHelp,skinBand);

        ribbon.addTask(task1);
        ribbon.addTask(task2);
        ribbon.addTask(task3);
        configureApplicationMenu();

        configureApplicationMenu();
        this.add(cardPanel, BorderLayout.CENTER);
    }

    private void createInactiveListener() {
         Action act = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                constructAndShowLoginPane();
            }
        };

        inactive = new InactivityListener(act, 15);
        inactive.start();
    }

    protected void configureApplicationMenu() {

        RibbonApplicationMenuEntryPrimary amEntryBackupDB = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/database_up.png"), "Backup Database", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BackupDialog dlg = new BackupDialog(ArchieveMainframe.this, properties.getServerName(), properties.getDatabase());
                dlg.showDialog();
            }
        }, CommandButtonKind.ACTION_ONLY);

        amEntryBackupDB.setActionKeyTip("B");

        RibbonApplicationMenuEntryPrimary amEntryRestoreDB = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/database_down.png"), "Restore Database", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RestoreDialog dlg = new RestoreDialog(ArchieveMainframe.this, properties.getServerName(), properties.getDatabase());
                dlg.showDialog();
            }
        }, CommandButtonKind.ACTION_ONLY);

        amEntryRestoreDB.setActionKeyTip("R");


        RibbonApplicationMenuEntryPrimary amEntryHelp = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/help.png"), "Bantuan",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Invoked creating new document");
                    }
                }, CommandButtonKind.ACTION_ONLY);

        RibbonApplicationMenuEntrySecondary amEntryAppHelp = new RibbonApplicationMenuEntrySecondary(
                Mainframe.getResizableIconFromSource("resource/help.png"), "Help Contents", null,
                CommandButtonKind.ACTION_ONLY);
        amEntryAppHelp.setDescriptionText("Membuka Arsip Bantuan Aplikasi");
        amEntryAppHelp.setActionKeyTip("H");

        RibbonApplicationMenuEntrySecondary amEntryAppAbout = new RibbonApplicationMenuEntrySecondary(
                Mainframe.getResizableIconFromSource("resource/info.png"), "Tentang", null,
                CommandButtonKind.ACTION_ONLY);
        amEntryAppAbout.setDescriptionText("Tentang Aplikasi dan Kontak Informasi");
        amEntryAppAbout.setActionKeyTip("I");

        amEntryHelp.addSecondaryMenuGroup("Bantuan",
                amEntryAppHelp, amEntryAppAbout);

        RibbonApplicationMenuEntryPrimary amEntryChagePass = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/Unlock.png"),
                "Ganti Password",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ChangePasswordDialog dlg = new ChangePasswordDialog(ArchieveMainframe.this,userAccount,
                                ArchieveMainframe.this.getConnection());
                        dlg.showDialog();
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryChagePass.setActionKeyTip("O");

        RibbonApplicationMenuEntryPrimary amEntryLogoff = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/LogOff.png"),
                "LogOff",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (mainTabPane.getTabCount() > 0) {
                                Object[] options = {"Ya", "Tidak"};

                                int choise = JOptionPane.showOptionDialog(null,
                                        " Anda yakin akan logoff  ? (Y/T)",
                                        "Konfirmasi",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null,
                                        options, options[1]);
                                if (choise == JOptionPane.YES_OPTION) {
                                    auth.doLogout(userAccount);
                                    mainTabPane.removeAll();
                                    constructAndShowLoginPane();
                                }
                            } else {
                                auth.doLogout(userAccount);
                                mainTabPane.removeAll();
                                constructAndShowLoginPane();
                            }

                        } catch (SQLException ex) {
                            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat Logoff",
                                    null, "ERROR", ex, Level.ALL, null);
                            JXErrorPane.showDialog(null, info);
                        }
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntryLogoff.setActionKeyTip("O");


        RibbonApplicationMenuEntryPrimary amEntrySwitch = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/Restart.png"),
                "Ganti User",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Invoked creating new document");
                    }
                }, CommandButtonKind.ACTION_ONLY);
        amEntrySwitch.setActionKeyTip("S");

        RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/Shutdown.png"), "Keluar", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Ya", "Tidak"};

                int choise = JOptionPane.showOptionDialog(null,
                        " Anda yakin akan keluar  ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    try {
                        auth.doLogout(userAccount);
                        mainTabPane.removeAll();
                        System.exit(0);
                    } catch (SQLException ex) {
                        ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat keluar dari aplikasi",
                                null, "ERROR", ex, Level.ALL, null);
                        JXErrorPane.showDialog(null, info);
                    }

                }

            }
        }, CommandButtonKind.ACTION_ONLY);

        amEntryExit.setActionKeyTip("X");

        RibbonApplicationMenuEntryFooter amFooterProps = new RibbonApplicationMenuEntryFooter(
                Mainframe.getResizableIconFromSource("resource/process_accept.png"), "Opsi",
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        OptionDialog dlg = new OptionDialog(ArchieveMainframe.this,properties);
                        dlg.showDialog();
                        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
                            background.setProperties(dlg.getProperties());
                            repaintBackground();
                        }
                    }
                });
        RibbonApplicationMenuEntryFooter amFooterExit = new RibbonApplicationMenuEntryFooter(
                Mainframe.getResizableIconFromSource("resource/Shutdown.png"), "Keluar", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Ya", "Tidak"};

                int choise = JOptionPane.showOptionDialog(null,
                        " Anda yakin akan keluar  ? (Y/T)",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);
                if (choise == JOptionPane.YES_OPTION) {
                    try {
                        auth.doLogout(userAccount);
                        mainTabPane.removeAll();
                        System.exit(0);
                    } catch (SQLException ex) {
                        ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat keluar dari aplikasi",
                                null, "ERROR", ex, Level.ALL, null);
                        JXErrorPane.showDialog(null, info);
                    }

                }
            }
        });

        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
        applicationMenu.addMenuEntry(amEntryBackupDB);
        applicationMenu.addMenuEntry(amEntryRestoreDB);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryHelp);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryChagePass);
        applicationMenu.addMenuEntry(amEntryLogoff);
        applicationMenu.addMenuEntry(amEntrySwitch);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryExit);

        applicationMenu.addFooterEntry(amFooterProps);
        applicationMenu.addFooterEntry(amFooterExit);


        this.getRibbon().setApplicationMenu(applicationMenu);
    }

    private void constructAndShowLoginPane() {
        try {
            goToHome();

            loginPane = new JXLoginPane();
            loginPane.setBannerText("Otorisasi User");

            loginPane.setSaveMode(JXLoginPane.SaveMode.USER_NAME);
            loginPane.setLoginService(new SimpleLoginService());

            loginPane.setUserNameStore(userNameStore);

            Window w = WindowUtils.findWindow(this);
            loginDialog = new LoginDialog((Frame) w, loginPane);
            loginDialog.setVisible(true);

        } catch (Exception ex) {
            ErrorInfo info = new ErrorInfo("Kesalahan", "Terjadi Kesalahan pada saat menampilkan form login",
                    null, "ERROR", ex, Level.ALL, null);
            JXErrorPane.showDialog(null, info);
        }
    }

    public void startInActiveListener() {
        inactive.start();
    }

    public void stopInActiveListener() {
        inactive.stop();
    }

    public Connection getConnection() {
        return connection;
    }

    public void repaintBackground() {
        background.repaintAll();
    }

    public void addChildFrame(String title, Component comp) {
        mainTabPane.addTab(title, comp);
        mainTabPane.setSelectedIndex(mainTabPane.getTabCount() - 1);
        goBack();
        setNavigationButtonState(true);
    }

    public void goToHome() {
        ((CardLayout) cardPanel.getLayout()).first(cardPanel);
    }

    public void goBack() {
        ((CardLayout) cardPanel.getLayout()).last(cardPanel);
    }

    public void setNavigationButtonState(boolean flag) {
        nav1.setButtonFlags(flag);
        nav2.setButtonFlags(flag);
        nav3.setButtonFlags(flag);
    }

    public Long getSession() {
        return sessionType.getSession();
    }

    public ArchieveProperties getProperties() {
        return properties;
    }

    public void setProperties(ArchieveProperties properties) {
        this.properties = properties;
    }

    private void installKeyEventPostProcessor() {
        if (postProcessor != null) {
            return;
        }
        postProcessor = new KeyEventPostProcessor() {

            public boolean postProcessKeyEvent(KeyEvent e) {
                KeyStroke stroke = KeyStroke.getKeyStrokeForEvent(e);
                if (stroke.equals(KeyStroke.getKeyStroke("ENTER"))) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(postProcessor);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btBackupDB) {
            BackupDialog dlg = new BackupDialog(this, properties.getServerName(), properties.getDatabase());
            dlg.showDialog();
        } else if (source == btRestoreDB) {
            RestoreDialog dlg = new RestoreDialog(this, properties.getServerName(), properties.getDatabase());
            dlg.showDialog();
        } else if (source == btHelp) {
        } else if (source == btAbout) {
        }
    }

    private class SimpleLoginService extends LoginService {

        public SimpleLoginService() {
            this.addLoginListener(new SimpleLoginListener());
        }

        @Override
        public boolean authenticate(String username, char[] password, String server) throws Exception {
            userAccount = new UserAccount(username, new String(password));
            sessionType = auth.doLogin(userAccount);
            if (sessionType.getSessionType() == SessionType.NEW_SESSION
                    || sessionType.getSessionType() == SessionType.RESUME_SAME
                    || sessionType.getSessionType() == SessionType.RESUME_DIFF) {
                return true;
            }
            return false;
        }
    }


    private class SimpleLoginListener extends LoginAdapter {

        @Override
        public void loginFailed(LoginEvent source) {
            if (sessionType.getSessionType() == SessionType.AUTH_FAILED) {
                loginPane.setErrorMessage("<html><b>Login gagal</b>"
                        + "<br>Cek username dan password anda. Kemungkinan <b>Caps Locks</b> "
                        + "di keyboard anda aktif</br>");
            } else if (sessionType.getSessionType() == SessionType.PERMISSION_DENIED) {
                loginPane.setErrorMessage("<html><b>Login gagal</b>"
                        + "<br>Username anda tidak berhak mengakses aplikasi ini</br>");
            }
        }

        @Override
        public void loginSucceeded(LoginEvent source) {
            if (sessionType.getSessionType() == SessionType.NEW_SESSION) {
                if (userNameStore != null) {
                    userNameStore.addUserName(loginPane.getUserName());
                } else {
                    userNameStore = loginPane.getUserNameStore();
                }

            }
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    LookAndFeel oldLaf = (LookAndFeel) evt.getOldValue();
                    LookAndFeel newLaf = (LookAndFeel) evt.getNewValue();
                    System.out.println("Look-and-feel change from "
                            + ((oldLaf == null) ? "null" : oldLaf.getName())
                            + " to "
                            + ((newLaf == null) ? "null" : newLaf.getName()));
                }
            }
        });

        UIManager.put("TextArea.margin", new Insets(0,3,0,3));

        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception exc) {
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SubstanceLookAndFeel.setSkin(new BusinessSkin());
                ArchieveMainframe c = new ArchieveMainframe();
                c.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }

    
}
