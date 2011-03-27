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
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;

/**
 *
 * @author Muhamad Wibawa
 */
public class MailRibbonBand implements ActionListener {

    private JCommandButton btInbox = new JCommandButton("Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/new_mail.png"));
    private JCommandButton btInboxStatus = new JCommandButton("Status Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/Spam.png"));
    

    private JCommandButton btOutbox = new JCommandButton("Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/mail.png"));
    private JCommandButton btOutboxStatus = new JCommandButton("Status Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/All mail.png"));
    

    private ArchieveMainframe mainframe;

    public MailRibbonBand(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
    }

    public JRibbonBand getMailInboxRibbon() {
        JRibbonBand mailInbox = new JRibbonBand("Surat Masuk", null);

        mailInbox.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(mailInbox.getControlPanel())));


        RichTooltip inboxTooltip = new RichTooltip();
        inboxTooltip.setTitle("Surat Masuk");
        inboxTooltip.addDescriptionSection("Pengelolaan surat-surat yang masuk");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/new_mail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            inboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInbox.setActionRichTooltip(inboxTooltip);

        RichTooltip inboxStatusTooltip = new RichTooltip();
        inboxStatusTooltip.setTitle("Status Surat Masuk");
        inboxStatusTooltip.addDescriptionSection("Pengelolaan status surat-surat yang masuk");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Spam.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            inboxStatusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInboxStatus.setActionRichTooltip(inboxStatusTooltip);

        mailInbox.addCommandButton(btInbox, RibbonElementPriority.TOP);
        mailInbox.addCommandButton(btInboxStatus, RibbonElementPriority.MEDIUM);
        

        btInbox.addActionListener(this);
        btInboxStatus.addActionListener(this);

        mailInbox.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(mailInbox.getControlPanel()),
                new IconRibbonBandResizePolicy(mailInbox.getControlPanel())));

        return mailInbox;
    }

    public JRibbonBand getMailOutboxRibbon() {
        JRibbonBand mailOutbox = new JRibbonBand("Surat Keluar", null);

        mailOutbox.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(mailOutbox.getControlPanel())));


        RichTooltip outboxTooltip = new RichTooltip();
        outboxTooltip.setTitle("Surat Keluar");
        outboxTooltip.addDescriptionSection("Pengelolaan surat-surat yang keluar");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/mail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            outboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btOutbox.setActionRichTooltip(outboxTooltip);

        RichTooltip outboxStatusTooltip = new RichTooltip();
        outboxStatusTooltip.setTitle("Status Surat Keluar");
        outboxStatusTooltip.addDescriptionSection("Pengelolaan status surat-surat yang keluar");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/All mail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            outboxStatusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btOutboxStatus.setActionRichTooltip(outboxStatusTooltip);

        

        mailOutbox.addCommandButton(btOutbox, RibbonElementPriority.TOP);
        mailOutbox.addCommandButton(btOutboxStatus, RibbonElementPriority.MEDIUM);
        

        btOutbox.addActionListener(this);
        btOutboxStatus.addActionListener(this);
        

        mailOutbox.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(mailOutbox.getControlPanel()),
                new IconRibbonBandResizePolicy(mailOutbox.getControlPanel())));

        return mailOutbox;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btInbox) {
            mainframe.addChildFrame(btInbox.getText(), new InboxPanel(mainframe));
        } else if (source == btInboxStatus) {
            mainframe.addChildFrame(btInboxStatus.getText(), new InboxStatusPanel(mainframe));
        } else if (source == btOutbox) {
            mainframe.addChildFrame(btOutbox.getText(), new OutboxPanel(mainframe));
        } else if (source == btOutboxStatus) {
            mainframe.addChildFrame(btOutboxStatus.getText(), new OutboxStatusPanel(mainframe));
        } 
    }

}
