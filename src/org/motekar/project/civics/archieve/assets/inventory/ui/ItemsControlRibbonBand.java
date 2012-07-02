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
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.misc.ButtonComparator;
import org.motekar.util.user.ui.Mainframe;
import org.openide.util.Exceptions;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsControlRibbonBand extends JRibbonBand implements ActionListener {

    private JCommandButton btUsedItems = new JCommandButton("Daftar BMD Yang Digunausahakan", Mainframe.getResizableIconFromSource("resource/usedcheck.png"));
    private JCommandButton btDeletedItems = new JCommandButton("Daftar Usulan BMD Yang Akan Dihapus", Mainframe.getResizableIconFromSource("resource/deleteditems.png"));
    
    private JCommandButton btItemStatus = new JCommandButton("Status Penggunaan BMD", Mainframe.getResizableIconFromSource("resource/rule.png"));
    
    private ArchieveMainframe mainframe;
    //
    private List<JCommandButton> btArrays = new ArrayList<JCommandButton>();

    public ItemsControlRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        constructControlingRibbon();
    }
    
    private void constructControlingRibbon() {
        
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip usedToolTip = new RichTooltip();
        usedToolTip.setTitle("Daftar BMD Yang Digunausahakan");
        usedToolTip.addDescriptionSection("Pengelolaan Daftar BMD Yang Digunausahakan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/usedcheck.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            usedToolTip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btUsedItems.setActionRichTooltip(usedToolTip);


        RichTooltip deletedTooltip = new RichTooltip();
        deletedTooltip.setTitle("Daftar Usulan BMD Yang Akan Dihapus");
        deletedTooltip.addDescriptionSection("Pengelolaan Daftar Usulan Barang Milik Daerah Yang Akan Dihapus");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/deleteditems.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            deletedTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btDeletedItems.setActionRichTooltip(deletedTooltip);
        
        RichTooltip itemStatusTooltip = new RichTooltip();
        itemStatusTooltip.setTitle("Status Penggunaan BMD");
        itemStatusTooltip.addDescriptionSection("Pengelolaan Status Penggunaan Barang Milik Daerah");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/rule.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            itemStatusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemStatus.setActionRichTooltip(itemStatusTooltip);


        btArrays.add(btUsedItems);
        btArrays.add(btDeletedItems);
        btArrays.add(btItemStatus);


        addCommandButton(btUsedItems, RibbonElementPriority.TOP);
        addCommandButton(btDeletedItems, RibbonElementPriority.MEDIUM);
        addCommandButton(btItemStatus, RibbonElementPriority.MEDIUM);

        btUsedItems.addActionListener(this);
        btDeletedItems.addActionListener(this);
        btItemStatus.addActionListener(this);

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
        if (source == btDeletedItems) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btDeletedItems.getText(), new DeleteDraftItemsPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btUsedItems) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btUsedItems.getText(), new UsedItemsPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btItemStatus) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemStatus.getText(), new ItemsStatusPanel(mainframe));
            mainframe.setCursor(old);
        }
    }
}
