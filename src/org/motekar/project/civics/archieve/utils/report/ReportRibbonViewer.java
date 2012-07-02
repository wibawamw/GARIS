package org.motekar.project.civics.archieve.utils.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRPrintAnchorIndex;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.engine.xml.JRPrintXmlLoader;
import net.sf.jasperreports.view.JRHyperlinkListener;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.save.JRPrintSaveContributor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTextField;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.CommandToggleButtonGroup;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.ribbon.JFlowRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReportRibbonViewer extends JRibbonFrame implements JRHyperlinkListener {

    private JCommandButton btnSave = new JCommandButton("Simpan", Mainframe.getResizableIconFromSource("resource/Save.png"));
    private JCommandButton btnPrint = new JCommandButton("Cetak", Mainframe.getResizableIconFromSource("resource/Printer.png"));
    private JCommandButton btnReload = new JCommandButton("Reload", Mainframe.getResizableIconFromSource("resource/Refresh.png"));
    private JCommandButton btnFirst = new JCommandButton("First", Mainframe.getResizableIconFromSource("resource/old-go-first.png"));
    private JCommandButton btnLast = new JCommandButton("Last", Mainframe.getResizableIconFromSource("resource/old-go-last.png"));
    private JCommandButton btnPrevious = new JCommandButton("Previous", Mainframe.getResizableIconFromSource("resource/old-go-previous.png"));
    private JCommandButton btnNext = new JCommandButton("Next", Mainframe.getResizableIconFromSource("resource/old-go-next.png"));
    //
    private JCommandButton btnZoomOut = new JCommandButton("Zoom Out", Mainframe.getResizableIconFromSource("resource/Misc-Zoom-Out-icon.png"));
    private JCommandButton btnZoomIn = new JCommandButton("Zoom In", Mainframe.getResizableIconFromSource("resource/Misc-Zoom-In-icon.png"));
    private JCommandToggleButton btnActualSize = new JCommandToggleButton("Actual Size", Mainframe.getResizableIconFromSource("resource/Actions-zoom-fit-best-icon.png"));
    private JCommandToggleButton btnFitPage = new JCommandToggleButton("Fit Page", Mainframe.getResizableIconFromSource("resource/Actions-zoom-fit-height-icon.png"));
    private JCommandToggleButton btnFitWidth = new JCommandToggleButton("Fit Width", Mainframe.getResizableIconFromSource("resource/Actions-zoom-fit-width-icon.png"));
    private CommandToggleButtonGroup pageGroup = new CommandToggleButtonGroup();
    //
    private int zooms[] = {50, 75, 100, 125, 150, 175, 200, 250, 400, 800};
    private JComboBox cmbZoom = new JComboBox();
    private JTextField txtGoTo = new JXTextField();
    private JPanel viewerPanel = new JPanel();
    private static final Log log = LogFactory.getLog(MotekarViewerPanel.class);
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
    /**
     * Maximum size (in pixels) of a buffered image that would be used by {@link JRViewer JRViewer} to render a report page.
     * <p>
     * If rendering a report page would require an image larger than this threshold
     * (i.e. image width x image height > maximum size), the report page will be rendered directly on the viewer component.
     * </p>
     * <p>
     * If this property is zero or negative, buffered images will never be user to render a report page.
     * By default, this property is set to 0.
     * </p>
     */
    public static final String VIEWER_RENDER_BUFFER_MAX_SIZE = JRProperties.PROPERTY_PREFIX + "viewer.render.buffer.max.size";
    /**
     *
     */
    protected static final int TYPE_FILE_NAME = 1;
    protected static final int TYPE_INPUT_STREAM = 2;
    protected static final int TYPE_OBJECT = 3;
    /**
     * The DPI of the generated report.
     */
    public static final int REPORT_RESOLUTION = 72;
    protected final float MIN_ZOOM = 0.5f;
    protected final float MAX_ZOOM = 10f;
    protected int defaultZoomIndex = 2;
    protected int type = TYPE_FILE_NAME;
    protected boolean isXML;
    protected String reportFileName;
    protected SimpleFileResolver fileResolver;
    JasperPrint jasperPrint;
    private int pageIndex;
    private boolean pageError;
    protected float zoom;
    private JRGraphics2DExporter exporter;
    /**
     * the screen resolution.
     */
    private int screenResolution = REPORT_RESOLUTION;
    /**
     * the zoom ration adjusted to the screen resolution.
     */
    protected float realZoom;
    private DecimalFormat zoomDecimalFormat = new DecimalFormat("#.##");
    private ResourceBundle resourceBundle;
    private int downX;
    private int downY;
    private java.util.List hyperlinkListeners = new ArrayList();
    private Map linksMap = new HashMap();
    private MouseListener mouseListener =
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    hyperlinkClicked(evt);
                }
            };
    protected KeyListener keyNavigationListener =
            new KeyListener() {

                @Override
                public void keyTyped(KeyEvent evt) {
                }

                @Override
                public void keyPressed(KeyEvent evt) {
                    keyNavigate(evt);
                }

                @Override
                public void keyReleased(KeyEvent evt) {
                }
            };
    protected List saveContributors = new ArrayList();
    protected File lastFolder;
    protected JRSaveContributor lastSaveContributor;
    private JLabel jLabel1;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private PageRenderer lblPage;
    protected JLabel lblStatus;
    private JPanel pnlInScroll;
    private JPanel pnlLinks;
    private JPanel pnlMain;
    private JPanel pnlPage;
    protected JPanel pnlSep01;
    protected JPanel pnlSep02;
    protected JPanel pnlSep03;
    protected JPanel pnlStatus;
    private JScrollPane scrollPane;

    public ReportRibbonViewer(String Title, String fileName, boolean isXML) throws JRException {
        this(Title, fileName, isXML, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, InputStream is, boolean isXML) throws JRException {
        this(Title, is, isXML, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, JasperPrint jrPrint) {
        this(Title, jrPrint, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, String fileName, boolean isXML, Locale locale) throws JRException {
        this(Title, fileName, isXML, locale, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, InputStream is, boolean isXML, Locale locale) throws JRException {
        this(Title, is, isXML, locale, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, JasperPrint jrPrint, Locale locale) {
        this(Title, jrPrint, locale, null);
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, String fileName, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException {
        super(Title);
        setApplicationIcon(Mainframe.getResizableIconFromSource("resource/area_chart256.png"));
        constructMainframe();
        initResources(locale, resBundle);

        setScreenDetails();

        setZooms();

        initComponents();

        loadReport(fileName, isXML);

        initSaveContributors();

        addHyperlinkListener(this);
        constructMainframe();
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, InputStream is, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException {
        super(Title);
        setApplicationIcon(Mainframe.getResizableIconFromSource("resource/area_chart256.png"));
        initResources(locale, resBundle);

        setScreenDetails();

        setZooms();

        initComponents();

        loadReport(is, isXML);

        initSaveContributors();

        addHyperlinkListener(this);
        constructMainframe();
    }

    /** Creates new form JRViewer */
    public ReportRibbonViewer(String Title, JasperPrint jrPrint, Locale locale, ResourceBundle resBundle) {
        super(Title);
        setApplicationIcon(Mainframe.getResizableIconFromSource("resource/area_chart256.png"));
        initResources(locale, resBundle);

        setScreenDetails();

        setZooms();

        initComponents();

        loadReport(jrPrint);


        initSaveContributors();

        addHyperlinkListener(this);

        constructMainframe();
    }

    private void constructMainframe() {
        configureRibbon();

        cmbZoom.setSelectedIndex(defaultZoomIndex);

        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setPreferredSize(new Dimension(600, 700));
        setMinimumSize(new Dimension(r.width / 10, r.height / 2));
        pack();
        setAlwaysOnTop(true);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void configureRibbon() {

        JRibbon ribbon = this.getRibbon();

        JRibbonBand rBandMain = new JRibbonBand("Main", null);
        rBandMain.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandMain.getControlPanel())));

        JRibbonBand rBandNavi = new JRibbonBand("Navigasi Halaman", null);
        rBandNavi.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandNavi.getControlPanel())));

        JFlowRibbonBand rBandPage = createPageBand();
        rBandPage.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(rBandPage.getControlPanel())));


        RichTooltip saveTooltip = new RichTooltip();
        saveTooltip.setTitle("Simpan Laporan");
        saveTooltip.addDescriptionSection("Penyimpanan laporan ke Format File yang tersedia");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Save.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            saveTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnSave.setActionRichTooltip(saveTooltip);

        RichTooltip printTooltip = new RichTooltip();
        printTooltip.setTitle("Cetak Laporan");
        printTooltip.addDescriptionSection("Mencetak Laporan yang ditampilkan ke Printer");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Printer.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            printTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnPrint.setActionRichTooltip(printTooltip);


        RichTooltip reloadTooltip = new RichTooltip();
        reloadTooltip.setTitle("Reload Data");
        reloadTooltip.addDescriptionSection("Memuat ulang data laporan");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Refresh.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            reloadTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnReload.setActionRichTooltip(reloadTooltip);

        RichTooltip firstTooltip = new RichTooltip();
        firstTooltip.setTitle("First");
        firstTooltip.addDescriptionSection("Kembali ke Halaman Pertama Laporan");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/old-go-first.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            firstTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnFirst.setActionRichTooltip(firstTooltip);

        RichTooltip lastTooltip = new RichTooltip();
        lastTooltip.setTitle("Last");
        lastTooltip.addDescriptionSection("Maju ke Halaman Terakhir Laporan");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/old-go-last.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lastTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnLast.setActionRichTooltip(lastTooltip);

        RichTooltip previousTooltip = new RichTooltip();
        previousTooltip.setTitle("Previous");
        previousTooltip.addDescriptionSection("Kembali ke Halaman Sebelumnya");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/old-go-previous.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            previousTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnPrevious.setActionRichTooltip(previousTooltip);

        RichTooltip nextTooltip = new RichTooltip();
        nextTooltip.setTitle("Next");
        nextTooltip.addDescriptionSection("Maju ke Halaman Berikutnya");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/old-go-next.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            nextTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnNext.setActionRichTooltip(nextTooltip);

        RichTooltip zoomoutTooltip = new RichTooltip();
        zoomoutTooltip.setTitle("Zoom Out");
        zoomoutTooltip.addDescriptionSection("Perkecil Tampilan Laporan di Layar");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Misc-Zoom-Out-icon.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            zoomoutTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnZoomOut.setActionRichTooltip(zoomoutTooltip);

        RichTooltip zoominTooltip = new RichTooltip();
        zoominTooltip.setTitle("Zoom In");
        zoominTooltip.addDescriptionSection("Perbesar Tampilan Laporan di Layar");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Misc-Zoom-In-icon.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            zoominTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnZoomIn.setActionRichTooltip(zoominTooltip);

        RichTooltip actualSizeTooltip = new RichTooltip();
        actualSizeTooltip.setTitle("Actual Size");
        actualSizeTooltip.addDescriptionSection("Ukuran Kertas Sebenarnya");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Actions-zoom-fit-best-icon.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            actualSizeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnActualSize.setActionRichTooltip(actualSizeTooltip);

        RichTooltip fitPageTooltip = new RichTooltip();
        fitPageTooltip.setTitle("Fit Page");
        fitPageTooltip.addDescriptionSection("Sesuaikan Ukuran Kertas di Layar dengan ukuran Layar");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Actions-zoom-fit-height-icon.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            fitPageTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnFitPage.setActionRichTooltip(fitPageTooltip);

        RichTooltip fitWidthTooltip = new RichTooltip();
        fitWidthTooltip.setTitle("Fit Width");
        fitWidthTooltip.addDescriptionSection("Sesuaikan Ukuran Kertas di Layar dengan ukuran Layar");
        try {
            BufferedImage img = ImageIO.read(Mainframe.class.getResource("/resource/Actions-zoom-fit-width-icon.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            fitWidthTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btnFitWidth.setActionRichTooltip(fitWidthTooltip);

        rBandMain.addCommandButton(btnSave, RibbonElementPriority.TOP);
        rBandMain.addCommandButton(btnPrint, RibbonElementPriority.MEDIUM);
        rBandMain.addCommandButton(btnReload, RibbonElementPriority.MEDIUM);

        rBandNavi.addCommandButton(btnFirst, RibbonElementPriority.TOP);
        rBandNavi.addCommandButton(btnPrevious, RibbonElementPriority.MEDIUM);
        rBandNavi.addCommandButton(btnNext, RibbonElementPriority.MEDIUM);
        rBandNavi.addCommandButton(btnLast, RibbonElementPriority.MEDIUM);

        rBandMain.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(rBandMain.getControlPanel()),
                new IconRibbonBandResizePolicy(rBandMain.getControlPanel())));
        rBandNavi.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(rBandNavi.getControlPanel()),
                new IconRibbonBandResizePolicy(rBandNavi.getControlPanel())));
        rBandPage.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.FlowThreeRows(rBandPage.getControlPanel()),
                new IconRibbonBandResizePolicy(rBandPage.getControlPanel())));

        RibbonTask task1 = new RibbonTask("Preview Laporan", rBandMain, rBandNavi, rBandPage);

        ribbon.addTask(task1);
        configureApplicationMenu();

        this.add(viewerPanel, BorderLayout.CENTER);
    }

    private JFlowRibbonBand createPageBand() {
        JFlowRibbonBand zoomBand = new JFlowRibbonBand("Zoom", null);

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < zooms.length; i++) {
            model.addElement("" + zooms[i] + "%");
        }
        cmbZoom.setModel(model);

        JCommandButtonStrip pageStrip = new JCommandButtonStrip();


        pageStrip.add(btnActualSize);
        pageGroup.add(btnActualSize);
        pageStrip.add(btnFitPage);
        pageGroup.add(btnFitPage);
        pageStrip.add(btnFitWidth);
        pageGroup.add(btnFitWidth);

        zoomBand.addFlowComponent(pageStrip);

        JCommandButtonStrip zoomStrip = new JCommandButtonStrip();

        zoomStrip.add(btnZoomIn);
        zoomStrip.add(btnZoomOut);

        JRibbonComponent zoomComboWrapper = new JRibbonComponent(cmbZoom);

        zoomBand.addFlowComponent(zoomStrip);
        zoomBand.addFlowComponent(zoomComboWrapper);

        return zoomBand;
    }

    protected void configureApplicationMenu() {

        RibbonApplicationMenuEntryPrimary amEntryExit = new RibbonApplicationMenuEntryPrimary(
                Mainframe.getResizableIconFromSource("resource/Shutdown.png"), "Keluar", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        }, CommandButtonKind.ACTION_ONLY);

        amEntryExit.setActionKeyTip("X");


        RibbonApplicationMenuEntryFooter amFooterExit = new RibbonApplicationMenuEntryFooter(
                Mainframe.getResizableIconFromSource("resource/Shutdown.png"), "Keluar", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
        applicationMenu.addMenuEntry(amEntryExit);
        applicationMenu.addFooterEntry(amFooterExit);


        this.getRibbon().setApplicationMenu(applicationMenu);
    }

    private void setScreenDetails() {
        screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
    }

    /**
     *
     */
    public void clear() {
        emptyContainer(this);
        jasperPrint = null;
    }

    /**
     *
     */
    private void setZooms() {
    }

    /**
     *
     */
    public void addSaveContributor(JRSaveContributor contributor) {
        saveContributors.add(contributor);
    }

    /**
     *
     */
    public void removeSaveContributor(JRSaveContributor contributor) {
        saveContributors.remove(contributor);
    }

    /**
     *
     */
    public JRSaveContributor[] getSaveContributors() {
        return (JRSaveContributor[]) saveContributors.toArray(new JRSaveContributor[saveContributors.size()]);
    }

    /**
     * Replaces the save contributors with the ones provided as parameter.
     */
    public void setSaveContributors(JRSaveContributor[] saveContribs) {
        this.saveContributors = new ArrayList();
        if (saveContributors != null) {
            this.saveContributors.addAll(Arrays.asList(saveContribs));
        }
    }

    /**
     *
     */
    public final void addHyperlinkListener(JRHyperlinkListener listener) {
        hyperlinkListeners.add(listener);
    }

    /**
     *
     */
    public void removeHyperlinkListener(JRHyperlinkListener listener) {
        hyperlinkListeners.remove(listener);
    }

    /**
     *
     */
    public JRHyperlinkListener[] getHyperlinkListeners() {
        return (JRHyperlinkListener[]) hyperlinkListeners.toArray(new JRHyperlinkListener[hyperlinkListeners.size()]);
    }

    /**
     *
     */
    protected final void initResources(Locale locale, ResourceBundle resBundle) {
        if (locale != null) {
            setLocale(locale);
        } else {
            setLocale(Locale.getDefault());
        }
        if (resBundle == null) {
            this.resourceBundle = ResourceBundle.getBundle("net/sf/jasperreports/view/viewer", getLocale());
        } else {
            this.resourceBundle = resBundle;
        }
    }

    /**
     *
     */
    protected String getBundleString(String key) {
        return resourceBundle.getString(key);
    }

    /**
     *
     */
    protected final void initSaveContributors() {
        final String[] DEFAULT_CONTRIBUTORS = {
            "net.sf.jasperreports.view.save.JRPdfSaveContributor",
            "net.sf.jasperreports.view.save.JRRtfSaveContributor",
            "net.sf.jasperreports.view.save.JRDocxSaveContributor",
            "net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor",
            "net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor",};

        for (int i = 0; i < DEFAULT_CONTRIBUTORS.length; i++) {
            try {
                Class saveContribClass = JRClassLoader.loadClassForName(DEFAULT_CONTRIBUTORS[i]);
                Constructor constructor = saveContribClass.getConstructor(new Class[]{Locale.class, ResourceBundle.class});
                JRSaveContributor saveContrib = (JRSaveContributor) constructor.newInstance(new Object[]{getLocale(), resourceBundle});
                saveContributors.add(saveContrib);
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     */
    @Override
    public void gotoHyperlink(JRPrintHyperlink hyperlink) {
        switch (hyperlink.getHyperlinkTypeValue()) {
            case REFERENCE: {
                if (isOnlyHyperlinkListener()) {
                    System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
                    System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
                }
                break;
            }
            case LOCAL_ANCHOR: {
                if (hyperlink.getHyperlinkAnchor() != null) {
                    Map anchorIndexes = jasperPrint.getAnchorIndexes();
                    JRPrintAnchorIndex anchorIndex = (JRPrintAnchorIndex) anchorIndexes.get(hyperlink.getHyperlinkAnchor());
                    if (anchorIndex.getPageIndex() != pageIndex) {
                        setPageIndex(anchorIndex.getPageIndex());
                        refreshPage();
                    }
                    Container container = pnlInScroll.getParent();
                    if (container instanceof JViewport) {
                        JViewport viewport = (JViewport) container;

                        int newX = (int) (anchorIndex.getElementAbsoluteX() * realZoom);
                        int newY = (int) (anchorIndex.getElementAbsoluteY() * realZoom);

                        int maxX = pnlInScroll.getWidth() - viewport.getWidth();
                        int maxY = pnlInScroll.getHeight() - viewport.getHeight();

                        if (newX < 0) {
                            newX = 0;
                        }
                        if (newX > maxX) {
                            newX = maxX;
                        }
                        if (newY < 0) {
                            newY = 0;
                        }
                        if (newY > maxY) {
                            newY = maxY;
                        }

                        viewport.setViewPosition(new Point(newX, newY));
                    }
                }

                break;
            }
            case LOCAL_PAGE: {
                int page = pageIndex + 1;
                if (hyperlink.getHyperlinkPage() != null) {
                    page = hyperlink.getHyperlinkPage().intValue();
                }

                if (page >= 1 && page <= jasperPrint.getPages().size() && page != pageIndex + 1) {
                    setPageIndex(page - 1);
                    refreshPage();
                    Container container = pnlInScroll.getParent();
                    if (container instanceof JViewport) {
                        JViewport viewport = (JViewport) container;
                        viewport.setViewPosition(new Point(0, 0));
                    }
                }

                break;
            }
            case REMOTE_ANCHOR: {
                if (isOnlyHyperlinkListener()) {
                    System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
                    System.out.println("Hyperlink anchor    : " + hyperlink.getHyperlinkAnchor());
                    System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
                }
                break;
            }
            case REMOTE_PAGE: {
                if (isOnlyHyperlinkListener()) {
                    System.out.println("Hyperlink reference : " + hyperlink.getHyperlinkReference());
                    System.out.println("Hyperlink page      : " + hyperlink.getHyperlinkPage());
                    System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
                }
                break;
            }
            case CUSTOM: {
                if (isOnlyHyperlinkListener()) {
                    System.out.println("Hyperlink of type " + hyperlink.getLinkType());
                    System.out.println("Implement your own JRHyperlinkListener to manage this type of event.");
                }
                break;
            }
            case NONE:
            default: {
                break;
            }
        }
    }

    protected boolean isOnlyHyperlinkListener() {
        int listenerCount;
        if (hyperlinkListeners == null) {
            listenerCount = 0;
        } else {
            listenerCount = hyperlinkListeners.size();
            if (hyperlinkListeners.contains(this)) {
                --listenerCount;
            }
        }
        return listenerCount == 0;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlSep01 = new javax.swing.JPanel();
        pnlSep02 = new javax.swing.JPanel();
        pnlSep03 = new javax.swing.JPanel();

        pnlMain = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);

        pnlInScroll = new javax.swing.JPanel();
        pnlPage = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        pnlLinks = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lblPage = new PageRenderer(this);
        pnlStatus = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();

        viewerPanel.setLayout(new java.awt.BorderLayout());

        viewerPanel.setMinimumSize(new java.awt.Dimension(450, 150));
        viewerPanel.setPreferredSize(new java.awt.Dimension(450, 150));

        btnSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(keyNavigationListener);

        btnPrint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        btnPrint.addKeyListener(keyNavigationListener);

        btnReload.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });
        btnReload.addKeyListener(keyNavigationListener);
        pnlSep01.setMaximumSize(new java.awt.Dimension(10, 10));

        btnFirst.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        btnFirst.addKeyListener(keyNavigationListener);

        btnPrevious.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        btnPrevious.addKeyListener(keyNavigationListener);

        btnNext.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        btnNext.addKeyListener(keyNavigationListener);

        btnLast.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        btnLast.addKeyListener(keyNavigationListener);

        txtGoTo.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGoToActionPerformed(evt);
            }
        });
        txtGoTo.addKeyListener(keyNavigationListener);

        pnlSep02.setMaximumSize(new java.awt.Dimension(10, 10));

        btnActualSize.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualSizeActionPerformed(evt);
            }
        });
        btnActualSize.addKeyListener(keyNavigationListener);

        btnFitPage.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFitPageActionPerformed(evt);
            }
        });
        btnFitPage.addKeyListener(keyNavigationListener);

        btnFitWidth.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFitWidthActionPerformed(evt);
            }
        });
        btnFitWidth.addKeyListener(keyNavigationListener);

        pnlSep03.setMaximumSize(new java.awt.Dimension(10, 10));

        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });
        btnZoomIn.addKeyListener(keyNavigationListener);

        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });
        btnZoomOut.addKeyListener(keyNavigationListener);

        cmbZoom.setEditable(true);
        cmbZoom.setToolTipText(getBundleString("zoom.ratio"));
        cmbZoom.setMaximumSize(new java.awt.Dimension(80, 23));
        cmbZoom.setMinimumSize(new java.awt.Dimension(80, 23));
        cmbZoom.setPreferredSize(new java.awt.Dimension(80, 23));
        cmbZoom.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbZoomActionPerformed(evt);
            }
        });
        cmbZoom.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbZoomItemStateChanged(evt);
            }
        });
        cmbZoom.addKeyListener(keyNavigationListener);

        pnlMain.setLayout(new java.awt.BorderLayout());
        pnlMain.addComponentListener(new java.awt.event.ComponentAdapter() {

            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pnlMainComponentResized(evt);
            }
        });

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pnlInScroll.setLayout(new java.awt.GridBagLayout());

        pnlPage.setLayout(new java.awt.BorderLayout());
        pnlPage.setMinimumSize(new java.awt.Dimension(100, 100));
        pnlPage.setPreferredSize(new java.awt.Dimension(100, 100));

        jPanel4.setLayout(new java.awt.GridBagLayout());
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 120));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 120));

        pnlLinks.setLayout(null);
        pnlLinks.setMinimumSize(new java.awt.Dimension(5, 5));
        pnlLinks.setPreferredSize(new java.awt.Dimension(5, 5));
        pnlLinks.setOpaque(false);
        pnlLinks.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLinksMousePressed(evt);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLinksMouseReleased(evt);
            }
        });
        pnlLinks.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLinksMouseDragged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(pnlLinks, gridBagConstraints);

        jPanel5.setBackground(java.awt.Color.gray);
        jPanel5.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel5.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel4.add(jPanel5, gridBagConstraints);

        jPanel6.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel6.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jPanel6, gridBagConstraints);

        jPanel7.setBackground(java.awt.Color.gray);
        jPanel7.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel7.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jPanel7, gridBagConstraints);

        jPanel8.setBackground(java.awt.Color.gray);
        jPanel8.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel8.setPreferredSize(new java.awt.Dimension(5, 5));
        jLabel1.setText("jLabel1");
        jPanel8.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jPanel8, gridBagConstraints);

        jPanel9.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel9.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel4.add(jPanel9, gridBagConstraints);

        lblPage.setBackground(java.awt.Color.white);
        lblPage.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lblPage.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(lblPage, gridBagConstraints);

        pnlPage.add(jPanel4, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInScroll.add(pnlPage, gridBagConstraints);

        scrollPane.setViewportView(pnlInScroll);
        pnlMain.add(scrollPane, java.awt.BorderLayout.CENTER);
        viewerPanel.add(pnlMain, java.awt.BorderLayout.CENTER);

        pnlStatus.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 10));
        lblStatus.setText("Page i of n");
        pnlStatus.add(lblStatus);
        viewerPanel.add(pnlStatus, java.awt.BorderLayout.SOUTH);
        viewerPanel.addKeyListener(keyNavigationListener);
    }
    // </editor-fold>//GEN-END:initComponents

    void txtGoToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGoToActionPerformed
        try {
            int pageNumber = Integer.parseInt(txtGoTo.getText());
            if (pageNumber != pageIndex + 1
                    && pageNumber > 0
                    && pageNumber <= jasperPrint.getPages().size()) {
                setPageIndex(pageNumber - 1);
                refreshPage();
            }
        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtGoToActionPerformed

    void cmbZoomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbZoomItemStateChanged
        // Add your handling code here:
        pageGroup.clearSelection();
    }//GEN-LAST:event_cmbZoomItemStateChanged

    void pnlMainComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlMainComponentResized
        // Add your handling code here:
        JCommandToggleButton btn = pageGroup.getSelected();
        if (btn == btnFitPage) {
            fitPage();
            pageGroup.setSelected(btnFitPage, true);
        } else if (btn == btnFitWidth) {
            setRealZoomRatio(((float) pnlInScroll.getVisibleRect().getWidth() - 20f) / jasperPrint.getPageWidth());
            pageGroup.setSelected(btnFitWidth, true);
        }

    }//GEN-LAST:event_pnlMainComponentResized

    void btnActualSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualSizeActionPerformed
        // Add your handling code here:
        JCommandToggleButton btn = pageGroup.getSelected();
        if (btn == btnActualSize) {
            pageGroup.setSelected(btnFitPage, false);
            pageGroup.setSelected(btnFitWidth, false);
            cmbZoom.setSelectedIndex(-1);
            setZoomRatio(1);
            pageGroup.setSelected(btnActualSize, true);
        }
    }//GEN-LAST:event_btnActualSizeActionPerformed

    void btnFitWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFitWidthActionPerformed
        // Add your handling code here:
        JCommandToggleButton btn = pageGroup.getSelected();
        if (btn == btnFitWidth) {
            pageGroup.setSelected(btnActualSize, false);
            pageGroup.setSelected(btnFitPage, false);
            cmbZoom.setSelectedIndex(-1);
            setRealZoomRatio(((float) pnlInScroll.getVisibleRect().getWidth() - 20f) / jasperPrint.getPageWidth());
            pageGroup.setSelected(btnFitWidth, true);
        }
    }//GEN-LAST:event_btnFitWidthActionPerformed

    void btnFitPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFitPageActionPerformed
        // Add your handling code here:
        JCommandToggleButton btn = pageGroup.getSelected();
        if (btn == btnFitPage) {
            pageGroup.setSelected(btnActualSize, false);
            pageGroup.setSelected(btnFitWidth, false);
            cmbZoom.setSelectedIndex(-1);
            fitPage();
            pageGroup.setSelected(btnFitPage, true);
        }
    }//GEN-LAST:event_btnFitPageActionPerformed

    void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setLocale(this.getLocale());
        fileChooser.updateUI();
        for (int i = 0; i < saveContributors.size(); i++) {
            fileChooser.addChoosableFileFilter((JRSaveContributor) saveContributors.get(i));
        }

        if (saveContributors.contains(lastSaveContributor)) {
            fileChooser.setFileFilter(lastSaveContributor);
        } else if (saveContributors.size() > 0) {
            fileChooser.setFileFilter((JRSaveContributor) saveContributors.get(0));
        }

        if (lastFolder != null) {
            fileChooser.setCurrentDirectory(lastFolder);
        }

        int retValue = fileChooser.showSaveDialog(this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
            FileFilter fileFilter = fileChooser.getFileFilter();
            File file = fileChooser.getSelectedFile();

            lastFolder = file.getParentFile();

            JRSaveContributor contributor = null;

            if (fileFilter instanceof JRSaveContributor) {
                contributor = (JRSaveContributor) fileFilter;
            } else {
                int i = 0;
                while (contributor == null && i < saveContributors.size()) {
                    contributor = (JRSaveContributor) saveContributors.get(i++);
                    if (!contributor.accept(file)) {
                        contributor = null;
                    }
                }

                if (contributor == null) {
                    contributor = new JRPrintSaveContributor(getLocale(), this.resourceBundle);
                }
            }

            lastSaveContributor = contributor;

            try {
                contributor.save(jasperPrint, file);
            } catch (JRException e) {
                if (log.isErrorEnabled()) {
                    log.error("Save error.", e);
                }
                JOptionPane.showMessageDialog(this, getBundleString("error.saving"));
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    void pnlLinksMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMouseDragged
        // Add your handling code here:

        Container container = pnlInScroll.getParent();
        if (container instanceof JViewport) {
            JViewport viewport = (JViewport) container;
            Point point = viewport.getViewPosition();
            int newX = point.x - (evt.getX() - downX);
            int newY = point.y - (evt.getY() - downY);

            int maxX = pnlInScroll.getWidth() - viewport.getWidth();
            int maxY = pnlInScroll.getHeight() - viewport.getHeight();

            if (newX < 0) {
                newX = 0;
            }
            if (newX > maxX) {
                newX = maxX;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newY > maxY) {
                newY = maxY;
            }

            viewport.setViewPosition(new Point(newX, newY));
        }
    }//GEN-LAST:event_pnlLinksMouseDragged

    void pnlLinksMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMouseReleased
        // Add your handling code here:
        pnlLinks.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pnlLinksMouseReleased

    void pnlLinksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLinksMousePressed
        // Add your handling code here:
        pnlLinks.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        downX = evt.getX();
        downY = evt.getY();
    }//GEN-LAST:event_pnlLinksMousePressed

    void btnPrintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintActionPerformed
    {//GEN-HEADEREND:event_btnPrintActionPerformed
        // Add your handling code here:

        Thread thread =
                new Thread(
                new Runnable() {

                    public synchronized void run() {
                        try {
                            btnPrint.setEnabled(false);
                            ReportRibbonViewer.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            JasperPrintManager.printReport(jasperPrint, false);
                        } catch (Exception ex) {
                            if (log.isErrorEnabled()) {
                                log.error("Print error.", ex);
                            }
                            JOptionPane.showMessageDialog(ReportRibbonViewer.this, getBundleString("error.printing"));
                        } finally {
                            ReportRibbonViewer.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            ReportRibbonViewer.this.dispose();
                        }
                    }
                });

        thread.start();

    }//GEN-LAST:event_btnPrintActionPerformed

    void btnLastActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLastActionPerformed
    {//GEN-HEADEREND:event_btnLastActionPerformed
        // Add your handling code here:
        setPageIndex(jasperPrint.getPages().size() - 1);
        refreshPage();
    }//GEN-LAST:event_btnLastActionPerformed

    void btnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextActionPerformed
    {//GEN-HEADEREND:event_btnNextActionPerformed
        // Add your handling code here:
        setPageIndex(pageIndex + 1);
        refreshPage();
    }//GEN-LAST:event_btnNextActionPerformed

    void btnPreviousActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPreviousActionPerformed
    {//GEN-HEADEREND:event_btnPreviousActionPerformed
        // Add your handling code here:
        setPageIndex(pageIndex - 1);
        refreshPage();
    }//GEN-LAST:event_btnPreviousActionPerformed

    void btnFirstActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFirstActionPerformed
    {//GEN-HEADEREND:event_btnFirstActionPerformed
        // Add your handling code here:
        setPageIndex(0);
        refreshPage();
    }//GEN-LAST:event_btnFirstActionPerformed

    void btnReloadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReloadActionPerformed
    {//GEN-HEADEREND:event_btnReloadActionPerformed
        // Add your handling code here:
        if (type == TYPE_FILE_NAME) {
            try {
                loadReport(reportFileName, isXML);
            } catch (JRException e) {
                if (log.isErrorEnabled()) {
                    log.error("Reload error.", e);
                }
                jasperPrint = null;
                setPageIndex(0);
                refreshPage();

                JOptionPane.showMessageDialog(this, getBundleString("error.loading"));
            }

            forceRefresh();
        }
    }//GEN-LAST:event_btnReloadActionPerformed

    protected void forceRefresh() {
        zoom = 0;//force pageRefresh()
        realZoom = 0f;
        setZoomRatio(1);
    }

    void btnZoomInActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomInActionPerformed
    {//GEN-HEADEREND:event_btnZoomInActionPerformed
        // Add your handling code here:
        pageGroup.clearSelection();

        int newZoomInt = (int) (100 * getZoomRatio());
        int index = Arrays.binarySearch(zooms, newZoomInt);
        if (index < 0) {
            setZoomRatio(zooms[-index - 1] / 100f);
        } else if (index < cmbZoom.getModel().getSize() - 1) {
            setZoomRatio(zooms[index + 1] / 100f);
        }
    }//GEN-LAST:event_btnZoomInActionPerformed

    void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomOutActionPerformed
    {//GEN-HEADEREND:event_btnZoomOutActionPerformed
        // Add your handling code here:
        pageGroup.clearSelection();

        int newZoomInt = (int) (100 * getZoomRatio());
        int index = Arrays.binarySearch(zooms, newZoomInt);
        if (index > 0) {
            setZoomRatio(zooms[index - 1] / 100f);
        } else if (index < -1) {
            setZoomRatio(zooms[-index - 2] / 100f);
        }
    }//GEN-LAST:event_btnZoomOutActionPerformed

    void cmbZoomActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbZoomActionPerformed
    {//GEN-HEADEREND:event_cmbZoomActionPerformed
        // Add your handling code here:
        float newZoom = getZoomRatio();

        if (newZoom < MIN_ZOOM) {
            newZoom = MIN_ZOOM;
        }

        if (newZoom > MAX_ZOOM) {
            newZoom = MAX_ZOOM;
        }

        setZoomRatio(newZoom);
    }//GEN-LAST:event_cmbZoomActionPerformed

    /**
     */
    void hyperlinkClicked(MouseEvent evt) {
        JPanel link = (JPanel) evt.getSource();
        JRPrintHyperlink element = (JRPrintHyperlink) linksMap.get(link);
        hyperlinkClicked(element);
    }

    protected void hyperlinkClicked(JRPrintHyperlink hyperlink) {
        try {
            JRHyperlinkListener listener = null;
            for (int i = 0; i < hyperlinkListeners.size(); i++) {
                listener = (JRHyperlinkListener) hyperlinkListeners.get(i);
                listener.gotoHyperlink(hyperlink);
            }
        } catch (JRException e) {
            if (log.isErrorEnabled()) {
                log.error("Hyperlink click error.", e);
            }
            JOptionPane.showMessageDialog(this, getBundleString("error.hyperlink"));
        }
    }

    /**
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     */
    private void setPageIndex(int index) {
        if (jasperPrint != null
                && jasperPrint.getPages() != null
                && jasperPrint.getPages().size() > 0) {
            if (index >= 0 && index < jasperPrint.getPages().size()) {
                pageIndex = index;
                pageError = false;
                btnFirst.setEnabled((pageIndex > 0));
                btnPrevious.setEnabled((pageIndex > 0));
                btnNext.setEnabled((pageIndex < jasperPrint.getPages().size() - 1));
                btnLast.setEnabled((pageIndex < jasperPrint.getPages().size() - 1));
                txtGoTo.setEnabled(btnFirst.isEnabled() || btnLast.isEnabled());
                txtGoTo.setText("" + (pageIndex + 1));
                lblStatus.setText(
                        MessageFormat.format(
                        getBundleString("page"),
                        new Object[]{Integer.valueOf(pageIndex + 1), Integer.valueOf(jasperPrint.getPages().size())}));
            }
        } else {
            btnFirst.setEnabled(false);
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
            txtGoTo.setEnabled(false);
            txtGoTo.setText("");
            lblStatus.setText("");
        }
    }

    /**
     */
    protected final void loadReport(String fileName, boolean isXmlReport) throws JRException {
        if (isXmlReport) {
            jasperPrint = JRPrintXmlLoader.load(fileName);
        } else {
            jasperPrint = (JasperPrint) JRLoader.loadObject(fileName);
        }

        type = TYPE_FILE_NAME;
        this.isXML = isXmlReport;
        reportFileName = fileName;
        fileResolver = new SimpleFileResolver(Arrays.asList(new File[]{new File(fileName).getParentFile(), new File(".")}));
        fileResolver.setResolveAbsolutePath(true);
        btnReload.setEnabled(true);
        setPageIndex(0);
        defaultPageView();
    }

    /**
     */
    protected final void loadReport(InputStream is, boolean isXmlReport) throws JRException {
        if (isXmlReport) {
            jasperPrint = JRPrintXmlLoader.load(is);
        } else {
            jasperPrint = (JasperPrint) JRLoader.loadObject(is);
        }

        type = TYPE_INPUT_STREAM;
        this.isXML = isXmlReport;
        btnReload.setEnabled(false);
        setPageIndex(0);
        defaultPageView();
    }

    /**
     */
    protected final void loadReport(JasperPrint jrPrint) {
        jasperPrint = jrPrint;
        type = TYPE_OBJECT;
        isXML = false;
        btnReload.setEnabled(false);
        setPageIndex(0);
        defaultPageView();
    }

    private void defaultPageView() {
        pageGroup.clearSelection();
        cmbZoom.setSelectedIndex(-1);
        setRealZoomRatio(((float) pnlInScroll.getVisibleRect().getWidth() - 20f) / jasperPrint.getPageWidth());
        pageGroup.setSelected(btnFitWidth, true);
    }

    /**
     */
    protected void refreshPage() {
        if (jasperPrint == null
                || jasperPrint.getPages() == null
                || jasperPrint.getPages().isEmpty()) {
            pnlPage.setVisible(false);
            btnSave.setEnabled(false);
            btnPrint.setEnabled(false);
            btnActualSize.setEnabled(false);
            btnFitPage.setEnabled(false);
            btnFitWidth.setEnabled(false);
            btnZoomIn.setEnabled(false);
            btnZoomOut.setEnabled(false);
            cmbZoom.setEnabled(false);

            return;
        }

        pnlPage.setVisible(true);
        btnSave.setEnabled(true);
        btnPrint.setEnabled(true);
        btnActualSize.setEnabled(true);
        btnFitPage.setEnabled(true);
        btnFitWidth.setEnabled(true);
        btnZoomIn.setEnabled(zoom < MAX_ZOOM);
        btnZoomOut.setEnabled(zoom > MIN_ZOOM);
        cmbZoom.setEnabled(true);

        Dimension dim = new Dimension(
                (int) (jasperPrint.getPageWidth() * realZoom) + 8, // 2 from border, 5 from shadow and 1 extra pixel for image
                (int) (jasperPrint.getPageHeight() * realZoom) + 8);
        pnlPage.setMaximumSize(dim);
        pnlPage.setMinimumSize(dim);
        pnlPage.setPreferredSize(dim);

        long maxImageSize = JRProperties.getLongProperty(VIEWER_RENDER_BUFFER_MAX_SIZE);
        boolean renderImage;
        if (maxImageSize <= 0) {
            renderImage = false;
        } else {
            long imageSize = JRPrinterAWT.getImageSize(jasperPrint, realZoom);
            renderImage = imageSize <= maxImageSize;
        }

        lblPage.setRenderImage(renderImage);

        if (renderImage) {
            setPageImage();
        }

        pnlLinks.removeAll();
        linksMap = new HashMap();

        createHyperlinks();

        if (!renderImage) {
            lblPage.setIcon(null);

            pnlMain.validate();
            pnlMain.repaint();
        }
    }

    protected void setPageImage() {
        Image image;
        if (pageError) {
            image = getPageErrorImage();
        } else {
            try {
                image = JasperPrintManager.printPageToImage(jasperPrint, pageIndex, realZoom);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Print page to image error.", e);
                }
                pageError = true;

                image = getPageErrorImage();
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("net/sf/jasperreports/view/viewer").getString("error.displaying"));
            }
        }
        ImageIcon imageIcon = new ImageIcon(image);
        lblPage.setIcon(imageIcon);
    }

    protected Image getPageErrorImage() {
        Image image = new BufferedImage(
                (int) (jasperPrint.getPageWidth() * realZoom) + 1,
                (int) (jasperPrint.getPageHeight() * realZoom) + 1,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D grx = (Graphics2D) image.getGraphics();
        AffineTransform transform = new AffineTransform();
        transform.scale(realZoom, realZoom);
        grx.transform(transform);

        drawPageError(grx);

        return image;
    }

    protected void createHyperlinks() {
        java.util.List pages = jasperPrint.getPages();
        JRPrintPage page = (JRPrintPage) pages.get(pageIndex);
        createHyperlinks(page.getElements(), 0, 0);
    }

    protected void createHyperlinks(List elements, int offsetX, int offsetY) {
        if (elements != null && elements.size() > 0) {
            for (Iterator it = elements.iterator(); it.hasNext();) {
                JRPrintElement element = (JRPrintElement) it.next();

                JRImageMapRenderer imageMap = null;
                if (element instanceof JRPrintImage) {
                    JRRenderable renderer = ((JRPrintImage) element).getRenderer();
                    if (renderer instanceof JRImageMapRenderer) {
                        imageMap = (JRImageMapRenderer) renderer;
                        if (!imageMap.hasImageAreaHyperlinks()) {
                            imageMap = null;
                        }
                    }
                }
                boolean hasImageMap = imageMap != null;

                JRPrintHyperlink hyperlink = null;
                if (element instanceof JRPrintHyperlink) {
                    hyperlink = (JRPrintHyperlink) element;
                }
                boolean hasHyperlink = !hasImageMap
                        && hyperlink != null && hyperlink.getHyperlinkTypeValue() != HyperlinkTypeEnum.NONE;
                boolean hasTooltip = hyperlink != null && hyperlink.getHyperlinkTooltip() != null;

                if (hasHyperlink || hasImageMap || hasTooltip) {
                    JPanel link;
                    if (hasImageMap) {
                        Rectangle renderingArea = new Rectangle(0, 0, element.getWidth(), element.getHeight());
                        link = new ImageMapPanel(renderingArea, imageMap);
                    } else //hasImageMap
                    {
                        link = new JPanel();
                        if (hasHyperlink) {
                            link.addMouseListener(mouseListener);
                        }
                    }

                    if (hasHyperlink) {
                        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    link.setLocation(
                            (int) ((element.getX() + offsetX) * realZoom),
                            (int) ((element.getY() + offsetY) * realZoom));
                    link.setSize(
                            (int) (element.getWidth() * realZoom),
                            (int) (element.getHeight() * realZoom));
                    link.setOpaque(false);

                    String toolTip = getHyperlinkTooltip(hyperlink);
                    if (toolTip == null && hasImageMap) {
                        toolTip = "";//not null to register the panel as having a tool tip
                    }
                    link.setToolTipText(toolTip);

                    pnlLinks.add(link);
                    linksMap.put(link, element);
                }

                if (element instanceof JRPrintFrame) {
                    JRPrintFrame frame = (JRPrintFrame) element;
                    int frameOffsetX = offsetX + frame.getX() + frame.getLineBox().getLeftPadding().intValue();
                    int frameOffsetY = offsetY + frame.getY() + frame.getLineBox().getTopPadding().intValue();
                    createHyperlinks(frame.getElements(), frameOffsetX, frameOffsetY);
                }
            }
        }
    }

    protected class ImageMapPanel extends JPanel implements MouseListener, MouseMotionListener {

        private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
        protected final List imageAreaHyperlinks;

        public ImageMapPanel(Rectangle renderingArea, JRImageMapRenderer imageMap) {
            try {
                imageAreaHyperlinks = imageMap.getImageAreaHyperlinks(renderingArea);//FIXMECHART
            } catch (JRException e) {
                throw new JRRuntimeException(e);
            }

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            String tooltip = null;
            JRPrintImageAreaHyperlink imageMapArea = getImageMapArea(event);
            if (imageMapArea != null) {
                tooltip = getHyperlinkTooltip(imageMapArea.getHyperlink());
            }

            if (tooltip == null) {
                tooltip = super.getToolTipText(event);
            }

            return tooltip;
        }

        public void mouseDragged(MouseEvent e) {
            pnlLinksMouseDragged(e);
        }

        public void mouseMoved(MouseEvent e) {
            JRPrintImageAreaHyperlink imageArea = getImageMapArea(e);
            if (imageArea != null
                    && imageArea.getHyperlink().getHyperlinkTypeValue() != HyperlinkTypeEnum.NONE) {
                e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                e.getComponent().setCursor(Cursor.getDefaultCursor());
            }
        }

        protected JRPrintImageAreaHyperlink getImageMapArea(MouseEvent e) {
            return getImageMapArea((int) (e.getX() / realZoom), (int) (e.getY() / realZoom));
        }

        protected JRPrintImageAreaHyperlink getImageMapArea(int x, int y) {
            JRPrintImageAreaHyperlink image = null;
            if (imageAreaHyperlinks != null) {
                for (ListIterator it = imageAreaHyperlinks.listIterator(imageAreaHyperlinks.size()); image == null && it.hasPrevious();) {
                    JRPrintImageAreaHyperlink area = (JRPrintImageAreaHyperlink) it.previous();
                    if (area.getArea().containsPoint(x, y)) {
                        image = area;
                    }
                }
            }
            return image;
        }

        public void mouseClicked(MouseEvent e) {
            JRPrintImageAreaHyperlink imageMapArea = getImageMapArea(e);
            if (imageMapArea != null) {
                hyperlinkClicked(imageMapArea.getHyperlink());
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            pnlLinksMousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
            pnlLinksMouseReleased(e);
        }
    }

    protected String getHyperlinkTooltip(JRPrintHyperlink hyperlink) {
        String toolTip;
        toolTip = hyperlink.getHyperlinkTooltip();
        if (toolTip == null) {
            toolTip = getFallbackTooltip(hyperlink);
        }
        return toolTip;
    }

    protected String getFallbackTooltip(JRPrintHyperlink hyperlink) {
        String toolTip = null;
        switch (hyperlink.getHyperlinkTypeValue()) {
            case REFERENCE: {
                toolTip = hyperlink.getHyperlinkReference();
                break;
            }
            case LOCAL_ANCHOR: {
                if (hyperlink.getHyperlinkAnchor() != null) {
                    toolTip = "#" + hyperlink.getHyperlinkAnchor();
                }
                break;
            }
            case LOCAL_PAGE: {
                if (hyperlink.getHyperlinkPage() != null) {
                    toolTip = "#page " + hyperlink.getHyperlinkPage();
                }
                break;
            }
            case REMOTE_ANCHOR: {
                toolTip = "";
                if (hyperlink.getHyperlinkReference() != null) {
                    toolTip = toolTip + hyperlink.getHyperlinkReference();
                }
                if (hyperlink.getHyperlinkAnchor() != null) {
                    toolTip = toolTip + "#" + hyperlink.getHyperlinkAnchor();
                }
                break;
            }
            case REMOTE_PAGE: {
                toolTip = "";
                if (hyperlink.getHyperlinkReference() != null) {
                    toolTip = toolTip + hyperlink.getHyperlinkReference();
                }
                if (hyperlink.getHyperlinkPage() != null) {
                    toolTip = toolTip + "#page " + hyperlink.getHyperlinkPage();
                }
                break;
            }
            default: {
                break;
            }
        }
        return toolTip;
    }

    /**
     */
    private void emptyContainer(Container container) {
        Component[] components = container.getComponents();

        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof Container) {
                    emptyContainer((Container) components[i]);
                }
            }
        }

        components = null;
        container.removeAll();
        container = null;
    }

    /**
     */
    private float getZoomRatio() {
        float newZoom = zoom;

        try {
            newZoom =
                    zoomDecimalFormat.parse(
                    String.valueOf(cmbZoom.getEditor().getItem())).floatValue() / 100f;
        } catch (ParseException e) {
        }

        return newZoom;
    }

    /**
     */
    public void setZoomRatio(float newZoom) {
        if (newZoom > 0) {
            cmbZoom.getEditor().setItem(
                    zoomDecimalFormat.format(newZoom * 100) + "%");

            if (zoom != newZoom) {
                zoom = newZoom;
                realZoom = zoom * screenResolution / REPORT_RESOLUTION;

                refreshPage();
            }
        }
    }

    /**
     */
    private void setRealZoomRatio(float newZoom) {
        if (newZoom > 0 && realZoom != newZoom) {
            zoom = newZoom * REPORT_RESOLUTION / screenResolution;
            realZoom = newZoom;

            cmbZoom.getEditor().setItem(
                    zoomDecimalFormat.format(zoom * 100) + "%");

            refreshPage();
        }
    }

    /**
     *
     */
    public void setFitWidthZoomRatio() {
        setRealZoomRatio(((float) pnlInScroll.getVisibleRect().getWidth() - 20f) / jasperPrint.getPageWidth());

    }

    public void setFitPageZoomRatio() {
        setRealZoomRatio(((float) pnlInScroll.getVisibleRect().getHeight() - 20f) / jasperPrint.getPageHeight());
    }

    /**
     *
     */
    protected JRGraphics2DExporter getGraphics2DExporter() throws JRException {
        return new JRGraphics2DExporter();
    }

    /**
     *
     */
    protected void paintPage(Graphics2D grx) {
        if (pageError) {
            paintPageError(grx);
            return;
        }

        try {
            if (exporter == null) {
                exporter = getGraphics2DExporter();
            } else {
                exporter.reset();
            }

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, grx.create());
            exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(pageIndex));
            exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, new Float(realZoom));
            exporter.setParameter(JRExporterParameter.OFFSET_X, Integer.valueOf(1)); //lblPage border
            exporter.setParameter(JRExporterParameter.OFFSET_Y, Integer.valueOf(1));
            if (type == TYPE_FILE_NAME) {
                exporter.setParameter(JRExporterParameter.FILE_RESOLVER, fileResolver);
            }
            exporter.exportReport();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Page paint error.", e);
            }
            pageError = true;

            paintPageError(grx);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(ReportRibbonViewer.this, getBundleString("error.displaying"));
                }
            });
        }

    }

    protected void paintPageError(Graphics2D grx) {
        AffineTransform origTransform = grx.getTransform();

        AffineTransform transform = new AffineTransform();
        transform.translate(1, 1);
        transform.scale(realZoom, realZoom);
        grx.transform(transform);

        try {
            drawPageError(grx);
        } finally {
            grx.setTransform(origTransform);
        }
    }

    protected void drawPageError(Graphics grx) {
        grx.setColor(Color.white);
        grx.fillRect(0, 0, jasperPrint.getPageWidth() + 1, jasperPrint.getPageHeight() + 1);
    }

    protected void keyNavigate(KeyEvent evt) {
        boolean refresh = true;
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_PAGE_DOWN:
                dnNavigate(evt);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_PAGE_UP:
                upNavigate(evt);
                break;
            case KeyEvent.VK_HOME:
                homeEndNavigate(0);
                break;
            case KeyEvent.VK_END:
                homeEndNavigate(jasperPrint.getPages().size() - 1);
                break;
            default:
                refresh = false;
        }

        if (refresh) {
            refreshPage();
        }
    }

    private void dnNavigate(KeyEvent evt) {
        int bottomPosition = scrollPane.getVerticalScrollBar().getValue();
        scrollPane.dispatchEvent(evt);
        if ((scrollPane.getViewport().getHeight() > pnlPage.getHeight()
                || scrollPane.getVerticalScrollBar().getValue() == bottomPosition)
                && pageIndex < jasperPrint.getPages().size() - 1) {
            setPageIndex(pageIndex + 1);
            if (scrollPane.isEnabled()) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        }
    }

    private void upNavigate(KeyEvent evt) {
        if ((scrollPane.getViewport().getHeight() > pnlPage.getHeight()
                || scrollPane.getVerticalScrollBar().getValue() == 0)
                && pageIndex > 0) {
            setPageIndex(pageIndex - 1);
            if (scrollPane.isEnabled()) {
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            }
        } else {
            scrollPane.dispatchEvent(evt);
        }
    }

    private void homeEndNavigate(int pageNumber) {
        setPageIndex(pageNumber);
        if (scrollPane.isEnabled()) {
            scrollPane.getVerticalScrollBar().setValue(0);
        }
    }

    /**
     *
     */
    private void fitPage() {
        float heightRatio = ((float) pnlInScroll.getVisibleRect().getHeight() - 20f) / jasperPrint.getPageHeight();
        float widthRatio = ((float) pnlInScroll.getVisibleRect().getWidth() - 20f) / jasperPrint.getPageWidth();
        setRealZoomRatio(heightRatio < widthRatio ? heightRatio : widthRatio);
    }

    /**
     */
    class PageRenderer extends JLabel {

        private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
        private boolean renderImage;
        ReportRibbonViewer viewer = null;

        public PageRenderer(ReportRibbonViewer viewer) {
            this.viewer = viewer;
        }

        @Override
        public void paintComponent(Graphics g) {
            if (isRenderImage()) {
                super.paintComponent(g);
            } else {
                viewer.paintPage((Graphics2D) g.create());
            }
        }

        public boolean isRenderImage() {
            return renderImage;
        }

        public void setRenderImage(boolean renderImage) {
            this.renderImage = renderImage;
        }
    }
}
