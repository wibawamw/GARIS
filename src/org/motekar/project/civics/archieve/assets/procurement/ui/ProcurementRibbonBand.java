package org.motekar.project.civics.archieve.assets.procurement.ui;

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
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ButtonComparator;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

/**
 *
 * @author Muhamad Wibawa
 */
public class ProcurementRibbonBand implements ActionListener {

    private JCommandButton btProcurementResult = new JCommandButton("Daftar Hasil Pengadaan Barang", Mainframe.getResizableIconFromSource("resource/TheBoxPlus.png"));
    private JCommandButton btItemReceive = new JCommandButton("Buku Penerimaan Barang", Mainframe.getResizableIconFromSource("resource/TheBoxDown.png"));
    private JCommandButton btItemRelease = new JCommandButton("Buku Pengeluaran Barang", Mainframe.getResizableIconFromSource("resource/TheBoxUp.png"));
    private JCommandButton btUnrecycledItems = new JCommandButton("Buku Barang Pakai Habis", Mainframe.getResizableIconFromSource("resource/unrecycled.png"));
    private JCommandButton btItemCard = new JCommandButton("Kartu Persediaan Barang", Mainframe.getResizableIconFromSource("resource/TheBoxNote.png"));
    private JCommandButton btItemReceiveOther = new JCommandButton("Daftar Penerimaan Barang Dari Pihak Ketiga", Mainframe.getResizableIconFromSource("resource/TheBoxChecklist.png"));
    //
    private JCommandButton btProcurementReport = new JCommandButton("Laporan Penerimaan & Pengeluaran Barang", Mainframe.getResizableIconFromSource("resource/TheBoxPaper.png"));
    private ArchieveMainframe mainframe;
    private List<JCommandButton> btArrays = new ArrayList<JCommandButton>();

    public ProcurementRibbonBand(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
    }

    public JRibbonBand getProcurementRibbon() {

        JRibbonBand ribbonBand = new JRibbonBand("Pengadaan,Penerimaan & Penyaluran Barang", null);

        ribbonBand.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(ribbonBand.getControlPanel())));

        RichTooltip procurementToolTip = new RichTooltip();
        procurementToolTip.setTitle("Daftar Hasil Pengadaan Barang");
        procurementToolTip.addDescriptionSection("Pengelolaan Daftar Hasil Pengadaan Barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxPlus.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            procurementToolTip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btProcurementResult.setActionRichTooltip(procurementToolTip);

        RichTooltip receivedToolTip = new RichTooltip();
        receivedToolTip.setTitle("Buku Penerimaan Barang");
        receivedToolTip.addDescriptionSection("Input Buku Penerimaan Barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxDown.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            receivedToolTip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemReceive.setActionRichTooltip(receivedToolTip);


        RichTooltip releasedTooltip = new RichTooltip();
        releasedTooltip.setTitle("Buku Pengeluaran Barang");
        releasedTooltip.addDescriptionSection("Input Buku Pengeluaran Barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxUp.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            releasedTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemRelease.setActionRichTooltip(releasedTooltip);


        RichTooltip cardTooltip = new RichTooltip();
        cardTooltip.setTitle("Kartu Persediaan Barang");
        cardTooltip.addDescriptionSection("Pengelolaan Kartu Persediaan Barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxNote.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            cardTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemCard.setActionRichTooltip(cardTooltip);
        
        RichTooltip unrecycledTooltip = new RichTooltip();
        unrecycledTooltip.setTitle("Buku Barang Pakai Habis");
        unrecycledTooltip.addDescriptionSection("Pengelolaan Buku Barang Pakai Habis");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/unrecycled.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            unrecycledTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btUnrecycledItems.setActionRichTooltip(unrecycledTooltip);

        RichTooltip otherTooltip = new RichTooltip();
        otherTooltip.setTitle("Daftar Penerimaan Barang Dari Pihak Ketiga");
        otherTooltip.addDescriptionSection("Pengelolaan Daftar Penerimaan Barang Dari Pihak Ketiga");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxChecklist.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            otherTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemReceiveOther.setActionRichTooltip(otherTooltip);


        ribbonBand.addCommandButton(btProcurementResult, RibbonElementPriority.TOP);
        ribbonBand.addCommandButton(btItemReceive, RibbonElementPriority.MEDIUM);
        ribbonBand.addCommandButton(btItemRelease, RibbonElementPriority.MEDIUM);
        ribbonBand.addCommandButton(btUnrecycledItems, RibbonElementPriority.MEDIUM);
        ribbonBand.addCommandButton(btItemCard, RibbonElementPriority.MEDIUM);
        ribbonBand.addCommandButton(btItemReceiveOther, RibbonElementPriority.MEDIUM);

        btItemReceive.addActionListener(this);
        btItemRelease.addActionListener(this);
        btUnrecycledItems.addActionListener(this);
        btItemCard.addActionListener(this);
        btProcurementResult.addActionListener(this);
        btItemReceiveOther.addActionListener(this);

        btArrays.add(btProcurementResult);
        btArrays.add(btItemReceive);
        btArrays.add(btItemRelease);
        btArrays.add(btUnrecycledItems);
        btArrays.add(btItemCard);
        btArrays.add(btItemReceiveOther);

        ribbonBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(ribbonBand.getControlPanel()),
                new IconRibbonBandResizePolicy(ribbonBand.getControlPanel())));

        return ribbonBand;
    }

    public JRibbonBand getReportRibbon() {

        JRibbonBand ribbonBand = new JRibbonBand("Laporan", null);

        ribbonBand.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(ribbonBand.getControlPanel())));

        RichTooltip procurementToolTip = new RichTooltip();
        procurementToolTip.setTitle("Laporan Penerimaan & Pengeluaran Barang");
        procurementToolTip.addDescriptionSection("Pengelolaan Laporan Penerimaan & Pengeluaran Barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/TheBoxPaper.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            procurementToolTip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btProcurementReport.setActionRichTooltip(procurementToolTip);


        ribbonBand.addCommandButton(btProcurementReport, RibbonElementPriority.TOP);

        btProcurementReport.addActionListener(this);

        btArrays.add(btProcurementReport);

        ribbonBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(ribbonBand.getControlPanel()),
                new IconRibbonBandResizePolicy(ribbonBand.getControlPanel())));

        return ribbonBand;
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
        if (source == btItemReceive) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemReceive.getText(), new ApprovalBookPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btItemRelease) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemRelease.getText(), new ReleaseBookPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btItemCard) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemCard.getText(), new ItemCardPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btProcurementResult) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btProcurementResult.getText(), new ProcurementPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btItemReceiveOther) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemReceiveOther.getText(), new ThirdPartyItemsPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btProcurementReport) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btProcurementReport.getText(), new InventoryReportPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btUnrecycledItems) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btUnrecycledItems.getText(), new UnrecycledItemsPanel(mainframe));
            mainframe.setCursor(old);
        }

    }
}
