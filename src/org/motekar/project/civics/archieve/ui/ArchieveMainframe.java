package org.motekar.project.civics.archieve.ui;

import org.motekar.project.civics.archieve.mail.ui.MailRibbonBand;
import org.motekar.project.civics.archieve.expedition.ui.ExpeditionRibbonBand;
import org.motekar.project.civics.archieve.master.ui.MasterDataRibbonBand;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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
import java.util.ArrayList;
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
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.auth.LoginAdapter;
import org.jdesktop.swingx.auth.LoginEvent;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.UserNameStore;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.plaf.basic.CalendarHeaderHandler;
import org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler;
import org.jdesktop.swingx.util.WindowUtils;
import org.motekar.project.civics.archieve.assets.inventory.ui.AssetRibbonBand;
import org.motekar.project.civics.archieve.assets.inventory.ui.ItemsControlRibbonBand;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.master.sqlapi.AssetMasterBusinessLogic;
import org.motekar.project.civics.archieve.assets.procurement.ui.ProcurementRibbonBand;
import org.motekar.project.civics.archieve.assets.master.ui.AssetMasterRibbonBand;
import org.motekar.project.civics.archieve.report.ui.ReportRibbonBand;
import org.motekar.project.civics.archieve.sqlapi.ConfigBusinessLogic;
import org.motekar.project.civics.archieve.sqlapi.ConnectionManager;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.util.user.misc.InactivityListener;
import org.motekar.util.user.objects.Menu;
import org.motekar.util.user.objects.MenuRight;
import org.motekar.util.user.objects.SessionType;
import org.motekar.util.user.objects.UserAccount;
import org.motekar.util.user.objects.UserGroup;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;
import org.motekar.util.user.sqlapi.UserBusinessLogic;
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
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
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
public class ArchieveMainframe extends JRibbonFrame implements ActionListener {

    private JTabbedPane mainTabPane;
    private Connection connection;
    private InactivityListener inactive;
    private ArchieveProperties properties;
    private ProfileAccount profileAccount;
    private AuthBusinessLogic auth;
    private UserBusinessLogic userLogic;
    private AssetMasterBusinessLogic amLogic;
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
    private NavigationRibbonBand nav4 = new NavigationRibbonBand(this, "Navigasi", null);
    private NavigationRibbonBand nav5 = new NavigationRibbonBand(this, "Navigasi", null);
    private JPanel cardPanel = new JPanel();
    private BackgroundPanel background;
    //
    private RibbonContextualTaskGroup archiveTaskGroup;
    private RibbonContextualTaskGroup archiveTaskGroup2;
    private RibbonContextualTaskGroup assetTaskGroup;
    private RibbonContextualTaskGroup assetTaskGroup2;
    private ArrayList<MenuRight> menuRights = new ArrayList<MenuRight>();
    private UserGroup userGroup = null;
    private Unit unit = null;
    //
    private MasterDataRibbonBand rBandMasterData = new MasterDataRibbonBand(this, "Master Data", null);
    private ExpeditionRibbonBand rBandExpedition = new ExpeditionRibbonBand(this, "Perjalanan Dinas", null);
    private AssetMasterRibbonBand rBandMasterAsset = new AssetMasterRibbonBand(this, "Master Data", null);
    private ItemsControlRibbonBand rBandItemsControl = new ItemsControlRibbonBand(this, "Pengelolaan Aset", null);
    private ProcurementRibbonBand rBandProcurement = new ProcurementRibbonBand(this);
    private AssetRibbonBand rBandInventory = new AssetRibbonBand(this, "Inventarisasi", null);
    private MailRibbonBand rBandMail = new MailRibbonBand(this);
    private ReportRibbonBand rBandReport = new ReportRibbonBand(this);
    
    private GlasspaneBusyPanel busyPanel = new GlasspaneBusyPanel();

    public ArchieveMainframe() {
//        super("Government Assets and Archieves Information System (GARIS) 1.1.0");
        super("GARIS 1.1.0");
        setApplicationIcon(Mainframe.getResizableIconFromSource("resource/eGov 150p.png"));
        constructMainframe();
        constructAndShowLoginPane();
    }

    private void constructMainframe() {
        try {

            SplashScreenPanel sp = new SplashScreenPanel();
            com.sun.awt.AWTUtilities.setWindowOpaque(sp, false);

            createConnection();
            loadProfileAccount();
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
            
            createGlassPane();

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
            Thread.sleep(1000);
            sp.dispose();
            setVisible(true);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
    
    private void createGlassPane() {
        busyPanel = new GlasspaneBusyPanel();
        busyPanel.setAlpha(0.50f);

        setGlassPane(busyPanel);
        setGlassPaneVisible(false);

    }

    private void createConnection() {
        ConnectionManager connectionManager = new ConnectionManager(new File(System.getProperty("user.dir") + File.separator + "app.xml"));
        this.connection = connectionManager.connect();
        properties = connectionManager.getConfig();
        auth = new AuthBusinessLogic(this.connection);
        userLogic = new UserBusinessLogic(this.connection);
        amLogic = new AssetMasterBusinessLogic(this.connection);

    }

    private void loadProfileAccount() throws SQLException {
        ConfigBusinessLogic cLogic = new ConfigBusinessLogic(this.connection);
        profileAccount = cLogic.getProfileAccount();
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

        background = new BackgroundPanel();

        constructCardPanel();
    }

    private void constructCardPanel() {
        cardPanel.setLayout(new CardLayout());
        cardPanel.add(background, "0");
        cardPanel.add(mainTabPane, "1");
    }
    
    public void setGlassPaneVisible(boolean visible) {
        busyPanel.setVisible(visible);
    }

    private void configureRibbon() {

        JRibbon ribbon = this.getRibbon();

//        JRibbonBand rBandDB = new JRibbonBand("Database", null);
//        rBandDB.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandDB.getControlPanel())));

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

//        rBandDB.addCommandButton(btBackupDB, RibbonElementPriority.TOP);
//        rBandDB.addCommandButton(btRestoreDB, RibbonElementPriority.MEDIUM);

        rBandHelp.addCommandButton(btHelp, RibbonElementPriority.TOP);
        rBandHelp.addCommandButton(btAbout, RibbonElementPriority.MEDIUM);

//        rBandDB.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(rBandDB.getControlPanel()),
//                new IconRibbonBandResizePolicy(rBandDB.getControlPanel())));
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

        RibbonTask task1 = new RibbonTask("Konfigurasi & Bantuan", nav4, rBandHelp, skinBand);

        RibbonTask task2 = new RibbonTask("Arsip", nav1, rBandMasterData,
                rBandMail.getMailInboxRibbon(), rBandMail.getMailOutboxRibbon(),
                rBandMail.getDocumentUploaderRibbon(), 
                rBandMail.getOtherMailRibbon(),
                rBandExpedition);

        RibbonTask task3 = new RibbonTask("Master Data & Pengadaan Aset", nav2, rBandMasterAsset,
                rBandProcurement.getProcurementRibbon(), rBandProcurement.getReportRibbon());
        RibbonTask task4 = new RibbonTask("Inventarisasi & Pengelolaan", nav5,
                rBandInventory, rBandItemsControl);

        RibbonTask task5 = new RibbonTask("Laporan Arsip", nav3, rBandReport.getDataMasterRibbon(), 
                rBandReport.getMailReportRibbon(),
                rBandReport.getExpeditionReport());
//        RibbonTask task4 = new RibbonTask("Konfigurasi & Bantuan", nav4, rBandDB, rBandHelp, skinBand);
        
        RibbonTask[] archiveTask = new RibbonTask[1];
        archiveTask[0] = task2;

        RibbonTask[] archiveTask2 = new RibbonTask[1];
        archiveTask2[0] = task5;

        RibbonTask[] assetTask = new RibbonTask[1];
        assetTask[0] = task3;
        RibbonTask[] assetTask2 = new RibbonTask[1];
        assetTask2[0] = task4;

        archiveTaskGroup = new RibbonContextualTaskGroup("", Color.WHITE, archiveTask);
        archiveTaskGroup2 = new RibbonContextualTaskGroup("", Color.WHITE, archiveTask2);
        assetTaskGroup = new RibbonContextualTaskGroup("", Color.WHITE, assetTask);
        assetTaskGroup2 = new RibbonContextualTaskGroup("", Color.WHITE, assetTask2);
        
        ribbon.addTask(task1);
        ribbon.addContextualTaskGroup(archiveTaskGroup);
        ribbon.addContextualTaskGroup(archiveTaskGroup2);
        ribbon.addContextualTaskGroup(assetTaskGroup);
        ribbon.addContextualTaskGroup(assetTaskGroup2);
        configureApplicationMenu();

        this.add(cardPanel, BorderLayout.CENTER);
    }

    public void setAllUnvisibleTaskGroup(boolean visible) {
        JRibbon ribbon = this.getRibbon();

        ribbon.setVisible(archiveTaskGroup, visible);
        ribbon.setVisible(archiveTaskGroup2, visible);
        ribbon.setVisible(assetTaskGroup, visible);
        ribbon.setVisible(assetTaskGroup2, visible);

        ribbon.setSelectedTask(ribbon.getTask(0));
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
                        ChangePasswordDialog dlg = new ChangePasswordDialog(ArchieveMainframe.this, userAccount,
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
                                    setAllUnvisibleTaskGroup(false);
                                    auth.doLogout(userAccount);
                                    mainTabPane.removeAll();
                                    constructAndShowLoginPane();
                                }
                            } else {
                                setAllUnvisibleTaskGroup(false);
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
                        OptionDialog dlg = new OptionDialog(ArchieveMainframe.this, profileAccount);
                        dlg.showDialog();
                        if (dlg.getResponse() == JOptionPane.YES_OPTION) {
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
//        applicationMenu.addMenuEntry(amEntryBackupDB);
//        applicationMenu.addMenuEntry(amEntryRestoreDB);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryHelp);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryChagePass);
        applicationMenu.addMenuEntry(amEntryLogoff);
        applicationMenu.addMenuEntry(amEntrySwitch);
        applicationMenu.addMenuSeparator();
        applicationMenu.addMenuEntry(amEntryExit);

//        applicationMenu.addFooterEntry(amFooterProps);
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
        nav4.setButtonFlags(flag);
        nav5.setButtonFlags(flag);
    }

    public Unit getUnit() {
        return unit;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public UserAccount getUserAccount() {
        return userAccount;
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

    public ProfileAccount getProfileAccount() {
        return profileAccount;
    }

    public void setProfileAccount(ProfileAccount profileAccount) {
        this.profileAccount = profileAccount;
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

    public void setVisibleTaskGroup() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JRibbon ribbon = ArchieveMainframe.this.getRibbon();

                if (userGroup != null) {
                    if (userGroup.equals(new UserGroup("Administrator"))) {
                        setAllUnvisibleTaskGroup(true);
                        ribbon.setSelectedTask(archiveTaskGroup.getTask(0));
                        rBandMasterData.setAllEnable();
                        rBandMail.setAllEnable();
                        rBandExpedition.setAllEnable();
                        rBandReport.setAllEnable();
                        rBandMasterAsset.setAllEnable();
                        rBandProcurement.setAllEnable();
                        rBandInventory.setAllEnable();
                        rBandItemsControl.setAllEnable();
                    } else {
                        setAllUnvisibleTaskGroup(true);

                        if (!menuRights.isEmpty()) {
                            for (MenuRight mr : menuRights) {
                                Menu menu = mr.getUserMenu();
                                rBandMasterData.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandMail.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandExpedition.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandReport.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                //
                                rBandMasterAsset.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandProcurement.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandInventory.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                                rBandItemsControl.setButtonEnable(menu.getMenuName(), mr.isAuthorize());
                            }
                        }

                        if (rBandMasterData.isAllDisable() && rBandMail.isAllDisable()
                                && rBandExpedition.isAllDisable()) {
                            ribbon.setVisible(archiveTaskGroup, false);
                        }
                        if (rBandReport.isAllDisable()) {
                            ribbon.setVisible(archiveTaskGroup2, false);
                        }

                        if (rBandMasterAsset.isAllDisable() && rBandProcurement.isAllDisable()) {
                            ribbon.setVisible(assetTaskGroup, false);

                        }
                        if (rBandInventory.isAllDisable() && rBandItemsControl.isAllDisable()) {
                            ribbon.setVisible(assetTaskGroup2, false);
                        }
                        
                        int tgCount = ribbon.getContextualTaskGroupCount();
                        
                        for (int i=0;i<tgCount;i++) {
                            RibbonContextualTaskGroup ctg = ribbon.getContextualTaskGroup(i);
                            if (ribbon.isVisible(ctg)) {
                                ribbon.setSelectedTask(ctg.getTask(0));
                                break;
                            }
                        }
                        
                    }
                }
            }
        });
    }

    private class SimpleLoginService extends LoginService {

        public SimpleLoginService() {
            this.addLoginListener(new SimpleLoginListener());
        }

        @Override
        public boolean authenticate(String username, char[] password, String server) throws Exception {
            userAccount = new UserAccount(username, new String(password));
            sessionType = auth.simpleLogin(userAccount);
            if (sessionType.getSessionType() == SessionType.NEW_SESSION
                    || sessionType.getSessionType() == SessionType.RESUME_SAME
                    || sessionType.getSessionType() == SessionType.RESUME_DIFF) {
                menuRights = auth.getMenuRights(getSession(), userAccount);
                userGroup = userLogic.getUserGroup(userAccount);
                unit = amLogic.getUnitByUser(getSession(), userAccount);
                setVisibleTaskGroup();
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

        UIManager.put("TextArea.margin", new Insets(0, 3, 0, 3));
        UIManager.put("TextField.margin", new Insets(0, 2, 0, 2));

        UIManager.put("JXDatePicker.forceZoomable", Boolean.TRUE);
        UIManager.put(JXMonthView.uiClassID,
                "org.jdesktop.swingx.plaf.basic.BasicMonthViewUI");
        UIManager.put(SpinningCalendarHeaderHandler.FOCUSABLE_SPINNER_TEXT,
                Boolean.TRUE);
        UIManager.put(SpinningCalendarHeaderHandler.ARROWS_SURROUND_MONTH,
                Boolean.TRUE);
        UIManager.put(CalendarHeaderHandler.uiControllerID,
                SpinningCalendarHeaderHandler.class.getName());

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
