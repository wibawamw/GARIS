package org.motekar.project.civics.archieve.mail.ui;

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
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
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
public class MailRibbonBand implements ActionListener {

    private JCommandButton btInbox = new JCommandButton("Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/new_mail.png"));
    private JCommandButton btInboxStatus = new JCommandButton("Status Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/Spam.png"));
    
    //
    
    private JCommandButton btOutbox = new JCommandButton("Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/mail.png"));
    private JCommandButton btOutboxStatus = new JCommandButton("Status Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/All mail.png"));
    
    //
    
    private JCommandButton btDocumentUploader = new JCommandButton("Upload Dokumen",
            Mainframe.getResizableIconFromSource("resource/UploadDocument.png"));
    
    //
    
    private JCommandButton btOtherMail = new JCommandButton("Surat Lainnya",
            Mainframe.getResizableIconFromSource("resource/othermail.png"));
    
    private JCommandMenuButton menuSicknessMail = new JCommandMenuButton("Surat Keterangan Sakit",
            Mainframe.getResizableIconFromSource("resource/healthmail.png"));
    private JCommandMenuButton menuDoctorMail = new JCommandMenuButton("Surat Keterangan Dokter",
            Mainframe.getResizableIconFromSource("resource/contractmail.png"));
    
    private List<JCommandButton> btArrays = new ArrayList<JCommandButton>();

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
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/All mail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            inboxStatusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInboxStatus.setActionRichTooltip(inboxStatusTooltip);

        mailInbox.addCommandButton(btInbox, RibbonElementPriority.TOP);
        mailInbox.addCommandButton(btInboxStatus, RibbonElementPriority.MEDIUM);
        
        btArrays.add(btInbox);
        btArrays.add(btInboxStatus);

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
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Spam.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            outboxStatusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btOutboxStatus.setActionRichTooltip(outboxStatusTooltip);

        mailOutbox.addCommandButton(btOutbox, RibbonElementPriority.TOP);
        mailOutbox.addCommandButton(btOutboxStatus, RibbonElementPriority.MEDIUM);
        
        btArrays.add(btOutbox);
        btArrays.add(btOutboxStatus);

        btOutbox.addActionListener(this);
        btOutboxStatus.addActionListener(this);

        mailOutbox.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(mailOutbox.getControlPanel()),
                new IconRibbonBandResizePolicy(mailOutbox.getControlPanel())));

        return mailOutbox;
    }
    
    public JRibbonBand getDocumentUploaderRibbon() {
        JRibbonBand docUpload = new JRibbonBand("Dokumen", null);

        docUpload.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(docUpload.getControlPanel())));


        RichTooltip uploadTooltip = new RichTooltip();
        uploadTooltip.setTitle("Upload Dokumen");
        uploadTooltip.addDescriptionSection("Pengelolaan upload dokument");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/UploadDocument.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            uploadTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btDocumentUploader.setActionRichTooltip(uploadTooltip);

        docUpload.addCommandButton(btDocumentUploader, RibbonElementPriority.TOP);
        
        
        btArrays.add(btDocumentUploader);

        btDocumentUploader.addActionListener(this);

        docUpload.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(docUpload.getControlPanel()),
                new IconRibbonBandResizePolicy(docUpload.getControlPanel())));

        return docUpload;
    }
    
    public JRibbonBand getOtherMailRibbon() {
        JRibbonBand mailOther = new JRibbonBand("Keterangan", null);

        mailOther.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(mailOther.getControlPanel())));
        
        btOtherMail.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                JCommandPopupMenu popupMenu = new JCommandPopupMenu();
                popupMenu.addMenuButton(menuSicknessMail);
                popupMenu.addMenuButton(menuDoctorMail);
                return popupMenu;
            }
        });
        
        btOtherMail.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);

        menuSicknessMail.addActionListener(this);
        menuDoctorMail.addActionListener(this);
        
        mailOther.addCommandButton(btOtherMail, RibbonElementPriority.TOP);
        

        btArrays.add(btOtherMail);
        btArrays.add(menuSicknessMail);
        btArrays.add(menuDoctorMail);

        mailOther.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(mailOther.getControlPanel()),
                new IconRibbonBandResizePolicy(mailOther.getControlPanel())));

        return mailOther;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Cursor old = mainframe.getCursor();
        if (source == btInbox) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btInbox.getText(), new InboxPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btInboxStatus) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btInboxStatus.getText(), new InboxStatusPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btOutbox) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btOutbox.getText(), new OutboxPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btOutboxStatus) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btOutboxStatus.getText(), new OutboxStatusPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == btDocumentUploader) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(btDocumentUploader.getText(), new DocumentUploaderPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuSicknessMail) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuSicknessMail.getText(), new SicknessMailPanel(mainframe));
            mainframe.setCursor(old);
        } else if (source == menuDoctorMail) {
            mainframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mainframe.addChildFrame(menuDoctorMail.getText(), new DoctorMailPanel(mainframe));
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
