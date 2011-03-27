package org.motekar.project.civics.archieve.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
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
public class NavigationRibbonBand extends JRibbonBand implements ActionListener {
    private JCommandButton btHome = new JCommandButton("Home", Mainframe.getResizableIconFromSource("resource/home.png"));
    private JCommandButton btBack = new JCommandButton("Back", Mainframe.getResizableIconFromSource("resource/arrow_right.png"));

    private ArchieveMainframe mainframe;

    public NavigationRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        construcButtons();
    }

    private void construcButtons() {
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip homeTooltip = new RichTooltip();
        homeTooltip.setTitle("Home");
        homeTooltip.addDescriptionSection("Kembali ke halaman muka aplikasi");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/home.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            homeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btHome.setActionRichTooltip(homeTooltip);


        RichTooltip backTooltip = new RichTooltip();
        backTooltip.setTitle("Back");
        backTooltip.addDescriptionSection("Kembali ke halaman input aplikasi");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/arrow_right.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            backTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btBack.setActionRichTooltip(backTooltip);

        addCommandButton(btHome, RibbonElementPriority.TOP);
        addCommandButton(btBack, RibbonElementPriority.MEDIUM);

        btHome.addActionListener(this);
        btBack.addActionListener(this);

        setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()),
                new IconRibbonBandResizePolicy(getControlPanel())));

        btHome.setEnabled(false);
        btBack.setEnabled(true);
    }

    public void setButtonFlags(boolean flag) {
        btHome.setEnabled(flag);
        btBack.setEnabled(!flag);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btHome) {
            mainframe.goToHome();
            mainframe.setNavigationButtonState(false);
        } else if (source == btBack) {
            mainframe.goBack();
            mainframe.setNavigationButtonState(true);
        } 
    }

    

}
