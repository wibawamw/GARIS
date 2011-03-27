package org.motekar.project.civics.archieve.mail.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
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
public class RegulationRibbonBand extends JRibbonBand implements ActionListener {

    private JCommandButton btRegulation = new JCommandButton("Peraturan Daerah",
            Mainframe.getResizableIconFromSource("resource/Regulation.png"));
    
    private JCommandButton btDecree = new JCommandButton("Surat Keputusan",
            Mainframe.getResizableIconFromSource("resource/Decree.png"));
    private ArchieveMainframe mainframe;

    public RegulationRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        construcButtons();
    }

    private void construcButtons() {
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));

        RichTooltip regulationTooltip = new RichTooltip();
        regulationTooltip.setTitle("Peraturan Daerah");
        regulationTooltip.addDescriptionSection("Pengelolaan peraturan-peraturan daerah");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Regulation.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            regulationTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btRegulation.setActionRichTooltip(regulationTooltip);

        RichTooltip decreeTooltip = new RichTooltip();
        decreeTooltip.setTitle("Surat Keputusan");
        decreeTooltip.addDescriptionSection("Pengelolaan data surat keputusan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Decree.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            decreeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btDecree.setActionRichTooltip(decreeTooltip);


        this.addCommandButton(btDecree, RibbonElementPriority.TOP);
        this.addCommandButton(btRegulation, RibbonElementPriority.MEDIUM);

        btRegulation.addActionListener(this);
        btDecree.addActionListener(this);
        

        this.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(this.getControlPanel()),
                new IconRibbonBandResizePolicy(this.getControlPanel())));

    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btRegulation) {
            mainframe.addChildFrame(btRegulation.getText(), new RegulationPanel(mainframe));
        } else if (source == btDecree) {
            mainframe.addChildFrame(btDecree.getText(), new DecreePanel(mainframe));
        } 
    }

}
