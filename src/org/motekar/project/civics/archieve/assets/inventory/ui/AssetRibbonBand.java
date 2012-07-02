package org.motekar.project.civics.archieve.assets.inventory.ui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ButtonComparator;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssetRibbonBand extends JRibbonBand implements ActionListener {

    private JCommandButton btInventoryBook = new JCommandButton("Buku Inventaris", Mainframe.getResizableIconFromSource("resource/Book.png"));
    private JCommandButton btInventoryRekap = new JCommandButton("Rekap Buku Inventaris", Mainframe.getResizableIconFromSource("resource/Book Rekap.png"));
    private JCommandButton btInventoryCard = new JCommandButton("Kartu Inventaris Barang (KIB)", Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_A = new JCommandMenuButton("KIB-A (Bidang Barang Tanah)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_B = new JCommandMenuButton("KIB-B (Bidang Peralatan dan Mesin)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_C = new JCommandMenuButton("KIB-C (Bidang Gedung dan Bangunan)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_D = new JCommandMenuButton("KIB-D (Bidang Jalan, Irigasi, Jaringan)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_E = new JCommandMenuButton("KIB-E (Bidang Aset Tetap Lainnya)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandMenuButton menuKIB_F = new JCommandMenuButton("KIB-F (Bidang Konstruksi dalam Pengerjaan)",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandButton btRoomCard = new JCommandButton("Kartu Inventaris Ruang (KIR)", Mainframe.getResizableIconFromSource("resource/paper_text.png"));
    private ArchieveMainframe mainframe;
    //
    private List<JCommandButton> btArrays = new ArrayList<JCommandButton>();

    public AssetRibbonBand(ArchieveMainframe mainframe, String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        constructInvetarizationRibbon();
    }

    private void constructInvetarizationRibbon() {

        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip bookToolTip = new RichTooltip();
        bookToolTip.setTitle("Buku Inventaris");
        bookToolTip.addDescriptionSection("Input Buku Inventaris");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Book.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            bookToolTip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInventoryBook.setActionRichTooltip(bookToolTip);


        RichTooltip rekapTooltip = new RichTooltip();
        rekapTooltip.setTitle("Rekap Buku Inventaris");
        rekapTooltip.addDescriptionSection("Pengelolaan Rekap Buku Inventaris");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Book Rekap.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            rekapTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInventoryRekap.setActionRichTooltip(rekapTooltip);

        btInventoryCard.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                JCommandPopupMenu popupMenu = new JCommandPopupMenu();
                popupMenu.addMenuButton(menuKIB_A);
                popupMenu.addMenuButton(menuKIB_B);
                popupMenu.addMenuButton(menuKIB_C);
                popupMenu.addMenuButton(menuKIB_D);
                popupMenu.addMenuButton(menuKIB_E);
                popupMenu.addMenuButton(menuKIB_F);
                return popupMenu;
            }
        });

        btInventoryCard.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        menuKIB_A.addActionListener(this);
        menuKIB_B.addActionListener(this);
        menuKIB_C.addActionListener(this);
        menuKIB_D.addActionListener(this);
        menuKIB_E.addActionListener(this);
        menuKIB_F.addActionListener(this);

        btArrays.add(menuKIB_A);
        btArrays.add(menuKIB_B);
        btArrays.add(menuKIB_C);
        btArrays.add(menuKIB_D);
        btArrays.add(menuKIB_E);
        btArrays.add(menuKIB_F);

        RichTooltip roomCardTooltip = new RichTooltip();
        roomCardTooltip.setTitle("Kartu Inventaris Ruang");
        roomCardTooltip.addDescriptionSection("Pengelolaan Kartu Inventaris Ruang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/paper_text.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            roomCardTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btRoomCard.setActionRichTooltip(roomCardTooltip);


        addCommandButton(btInventoryCard, RibbonElementPriority.TOP);
        addCommandButton(btInventoryBook, RibbonElementPriority.MEDIUM);
        addCommandButton(btInventoryRekap, RibbonElementPriority.MEDIUM);
        addCommandButton(btRoomCard, RibbonElementPriority.MEDIUM);

        btInventoryBook.addActionListener(this);
        btInventoryRekap.addActionListener(this);
        btInventoryCard.addActionListener(this);
        btRoomCard.addActionListener(this);

        btArrays.add(btInventoryBook);
        btArrays.add(btInventoryRekap);
        btArrays.add(btInventoryCard);
        btArrays.add(btRoomCard);

        setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()),
                new IconRibbonBandResizePolicy(getControlPanel())));
    }

    public void setButtonEnable(String name, boolean visible) {
        Collections.sort(btArrays, new ButtonComparator());

        JCommandButton comButton = new JCommandButton(name);
        int index = Collections.binarySearch(btArrays, comButton, new ButtonComparator());
        if (index >= 0) {
            btArrays.get(index).setEnabled(visible);
        }
    }

    public void setAllEnable() {
        if (!btArrays.isEmpty()) {
            for (JCommandButton bt : btArrays) {
                bt.setEnabled(true);
            }
        }

    }

    public boolean isAllDisable() {
        boolean disableAll = false;

        if (!btArrays.isEmpty()) {
            for (JCommandButton bt : btArrays) {
                disableAll = bt.isEnabled() == false;
            }
        }

        return disableAll;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Cursor old = mainframe.getCursor();
        if (source == btInventoryBook) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btInventoryBook.getText(), new InventoryBookPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btInventoryRekap) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btInventoryRekap.getText(), new InventoryRekapPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btRoomCard) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btRoomCard.getText(), new RoomInventoryPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_A) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_A.getText(), new ItemsLandPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_B) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_B.getText(), new ItemsMachinePanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_C) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_C.getText(), new ItemsBuildingPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_D) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_D.getText(), new ItemsNetworkPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_E) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_E.getText(), new ItemsFixedAssetPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuKIB_F) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuKIB_F.getText(), new ItemsConstructionPanel(mainframe));
            mainframe.setCursor(old);
        }
    }
}
