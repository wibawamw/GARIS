package org.motekar.project.civics.archieve.expedition.ui;

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
public class ExpeditionRibbonBand extends JRibbonBand implements ActionListener {

    private JCommandButton btAssignmentLetter = new JCommandButton("Surat Tugas",
            Mainframe.getResizableIconFromSource("resource/paper_text.png"));

    private JCommandButton btExpeditionLetter = new JCommandButton("Surat Perintah Perjalanan Dinas",
            Mainframe.getResizableIconFromSource("resource/Spd.png"));
    private JCommandButton btExpeditionJournal = new JCommandButton("Pertanggungjawaban Perjalanan Dinas",
            Mainframe.getResizableIconFromSource("resource/Journal.png"));

    private JCommandButton btPaymentCheque = new JCommandButton("Kwitansi Pembayaran",
            Mainframe.getResizableIconFromSource("resource/payment.png"));

     private ArchieveMainframe mainframe;

    public ExpeditionRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        construcButtons();
    }

    private void construcButtons() {
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip assLetterTooltip = new RichTooltip();
        assLetterTooltip.setTitle("Surat Tugas");
        assLetterTooltip.addDescriptionSection("Pengelolaan surat tugas untuk pegawai negeri sipil");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/paper_text.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            assLetterTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btAssignmentLetter.setActionRichTooltip(assLetterTooltip);


        RichTooltip expLetterTooltip = new RichTooltip();
        expLetterTooltip.setTitle("Surat Perjalanan Dinas");
        expLetterTooltip.addDescriptionSection("Pengelolaan surat perintah perjalanan dinas untuk pegawai negeri sipil");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Spd.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            expLetterTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btExpeditionLetter.setActionRichTooltip(expLetterTooltip);

        RichTooltip expJournalTooltip = new RichTooltip();
        expJournalTooltip.setTitle("Laporan Perjalanan Dinas");
        expJournalTooltip.addDescriptionSection("Pengelolaan laporan-laporan perjalanan dinas berisi hasil dan tindak lanjut");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Journal.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            expJournalTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btExpeditionJournal.setActionRichTooltip(expJournalTooltip);

        RichTooltip paymentTooltip = new RichTooltip();
        paymentTooltip.setTitle("Kwitansi Pembayaran");
        paymentTooltip.addDescriptionSection("Berisi biaya-biaya pembayaran untuk perjalanan dinas");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/payment.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            paymentTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btPaymentCheque.setActionRichTooltip(paymentTooltip);

        addCommandButton(btAssignmentLetter, RibbonElementPriority.TOP);
        addCommandButton(btExpeditionLetter, RibbonElementPriority.MEDIUM);
        addCommandButton(btExpeditionJournal, RibbonElementPriority.MEDIUM);
        addCommandButton(btPaymentCheque, RibbonElementPriority.MEDIUM);

        btAssignmentLetter.addActionListener(this);
        btExpeditionLetter.addActionListener(this);
        btExpeditionJournal.addActionListener(this);
        btPaymentCheque.addActionListener(this);

        setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()),
                new IconRibbonBandResizePolicy(getControlPanel())));
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btAssignmentLetter) {
            mainframe.addChildFrame(btAssignmentLetter.getText(), new AssignmentLetterPanel(mainframe));
        } else if (source == btExpeditionLetter) {
            mainframe.addChildFrame(btExpeditionLetter.getText(), new ExpeditionPanel(mainframe));
        } else if (source == btExpeditionJournal) {
            mainframe.addChildFrame(btExpeditionJournal.getText(), new ExpeditionJournalPanel(mainframe));
        } else if (source == btPaymentCheque) {
            mainframe.addChildFrame(btPaymentCheque.getText(), new ExpeditionChequePanel(mainframe));
        }
    }
    
}
