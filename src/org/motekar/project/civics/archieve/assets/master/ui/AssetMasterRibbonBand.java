package org.motekar.project.civics.archieve.assets.master.ui;

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
public class AssetMasterRibbonBand extends JRibbonBand implements ActionListener {
    
    private JCommandButton btRegions = new JCommandButton("Kecamatan / Kelurahan", Mainframe.getResizableIconFromSource("resource/Region.png"));
    private JCommandButton btUnit = new JCommandButton("UPB / Sub UPB", Mainframe.getResizableIconFromSource("resource/Museum.png"));
    private JCommandButton btItemCategory = new JCommandButton("Bidang Barang", Mainframe.getResizableIconFromSource("resource/box2.png"));
    private JCommandButton btCondition = new JCommandButton("Kondisi Barang", Mainframe.getResizableIconFromSource("resource/Condition.png"));
    private JCommandButton btRoom = new JCommandButton("Ruangan", Mainframe.getResizableIconFromSource("resource/New room.png"));
    
    private ArchieveMainframe mainframe;
    private List<JCommandButton> btArrays = new ArrayList<JCommandButton>();
    
    public AssetMasterRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        construcButtons();
    }
    
    private void construcButtons() {
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip regionTooltip = new RichTooltip();
        regionTooltip.setTitle("Kecamatan / Kelurahan");
        regionTooltip.addDescriptionSection("Input wilayah Kecamatan dan Kelurahan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Region.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            regionTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btRegions.setActionRichTooltip(regionTooltip);


        RichTooltip unitTooltip = new RichTooltip();
        unitTooltip.setTitle("UPB / Sub UPB");
        unitTooltip.addDescriptionSection("Pengelolaan Unit atau Sub Unit peguna Barang ");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Museum.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            unitTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btUnit.setActionRichTooltip(unitTooltip);

        RichTooltip itemCategoryTooltip = new RichTooltip();
        itemCategoryTooltip.setTitle("Bidang Barang");
        itemCategoryTooltip.addDescriptionSection("Pengelolaan dan pengelompokan barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/box2.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            itemCategoryTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btItemCategory.setActionRichTooltip(itemCategoryTooltip);
        
        RichTooltip conditionTooltip = new RichTooltip();
        conditionTooltip.setTitle("Kondisi Barang");
        conditionTooltip.addDescriptionSection("Pengelolaan kondisi barang");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Condition.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            conditionTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btCondition.setActionRichTooltip(conditionTooltip);
        
        RichTooltip roomTooltip = new RichTooltip();
        roomTooltip.setTitle("Ruangan");
        roomTooltip.addDescriptionSection("Pengelolaan ruangan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/New room.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            roomTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btRoom.setActionRichTooltip(roomTooltip);

        addCommandButton(btRegions, RibbonElementPriority.TOP);
        addCommandButton(btUnit, RibbonElementPriority.MEDIUM);
        addCommandButton(btItemCategory, RibbonElementPriority.MEDIUM);
        addCommandButton(btCondition, RibbonElementPriority.MEDIUM);
        addCommandButton(btRoom, RibbonElementPriority.MEDIUM);

        btRegions.addActionListener(this);
        btUnit.addActionListener(this);
        btItemCategory.addActionListener(this);
        btCondition.addActionListener(this);
        btRoom.addActionListener(this);
        
        btArrays.add(btRegions);
        btArrays.add(btUnit);
        btArrays.add(btItemCategory);
        btArrays.add(btCondition);
        btArrays.add(btRoom);

        setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()),
                new IconRibbonBandResizePolicy(getControlPanel())));
    }
    

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Cursor old = mainframe.getCursor();
        if (source == btRegions) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btRegions.getText(), new RegionPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btUnit) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btUnit.getText(), new UnitPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btItemCategory) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btItemCategory.getText(), new ItemsCategoryPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btCondition) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btCondition.getText(), new ConditionPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btRoom) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btRoom.getText(), new RoomPanel(mainframe));
            mainframe.setCursor(old);
        } 
    }
    
    public void setButtonEnable(String name, boolean visible) {
        Collections.sort(btArrays,new ButtonComparator());
        
        JCommandButton comButton = new JCommandButton(name);
        int index  = Collections.binarySearch(btArrays, comButton, new ButtonComparator());
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
    
}
