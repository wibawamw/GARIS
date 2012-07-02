package org.motekar.project.civics.archieve.report.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.motekar.project.civics.archieve.expedition.ui.ExpeditionReportPanel;
import org.motekar.project.civics.archieve.mail.ui.ContractMailReportPanel;
import org.motekar.project.civics.archieve.mail.ui.HealthMailReportPanel;
import org.motekar.project.civics.archieve.mail.ui.InboxReportPanel;
import org.motekar.project.civics.archieve.mail.ui.OutboxReportPanel;
import org.motekar.project.civics.archieve.mail.ui.ReferenceMailReportPanel;
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
public class ReportRibbonBand implements ActionListener {

    private JCommandButton btDUKReport = new JCommandButton("Daftar Urutan Kepangkatan",
            Mainframe.getResizableIconFromSource("resource/duk.png"));
    
    private JCommandButton btNonPNSReport = new JCommandButton("Daftar Pegawai Non PNS",
            Mainframe.getResizableIconFromSource("resource/user_list.png"));

    private JCommandButton btInboxReport = new JCommandButton("Laporan Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/new_mail_accept.png"));
    private JCommandButton btInboxStatistics = new JCommandButton("Statistik Surat Masuk",
            Mainframe.getResizableIconFromSource("resource/column_chart.png"));
    private JCommandButton btOutboxReport = new JCommandButton("Laporan Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/mail_accept.png"));
    private JCommandButton btOutboxStatistics = new JCommandButton("Statistik Surat Keluar",
            Mainframe.getResizableIconFromSource("resource/line_chart.png"));
    //
    
    private JCommandButton btReferenceMail = new JCommandButton("Daftar Surat Keluar Rujukan",
            Mainframe.getResizableIconFromSource("resource/referencemail.png"));
    private JCommandButton btContractMail = new JCommandButton("Daftar Surat Keluar Kontrak",
            Mainframe.getResizableIconFromSource("resource/contractmail.png"));
    private JCommandButton btHealthMail = new JCommandButton("Daftar Surat Keluar Keterangan Sehat",
            Mainframe.getResizableIconFromSource("resource/healthmail.png"));
    /**
     *
     */
    private JCommandButton btExpeditionReport = new JCommandButton("Daftar Perjalanan Dinas",
            Mainframe.getResizableIconFromSource("resource/plane.png"));
    private JCommandButton btExpeditionStatistics = new JCommandButton("Statistik Perjalanan Dinas",
            Mainframe.getResizableIconFromSource("resource/pie_chart.png"));
    private JCommandButton btBudgetRealization = new JCommandButton("Realisasi Anggaran",
            Mainframe.getResizableIconFromSource("resource/Treasure.png"));
    /**
     * 
     */
    private ArchieveMainframe mainframe;

    public ReportRibbonBand(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
    }

    public JRibbonBand getDataMasterRibbon() {
        JRibbonBand masterReport = new JRibbonBand("Data Master", null);
        masterReport.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(masterReport.getControlPanel())));

        RichTooltip dukTooltip = new RichTooltip();
        dukTooltip.setTitle("Data Urutan Kepangkatan");
        dukTooltip.addDescriptionSection("Berisi daftar urutan kepangkatan pegawai");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/duk.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            dukTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btDUKReport.setActionRichTooltip(dukTooltip);
        
        RichTooltip nonPNSTooltip = new RichTooltip();
        nonPNSTooltip.setTitle("Daftar Pegawai Non PNS");
        nonPNSTooltip.addDescriptionSection("Berisi daftar semua pegawai non PNS");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/user_list.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            nonPNSTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btNonPNSReport.setActionRichTooltip(nonPNSTooltip);

        masterReport.addCommandButton(btDUKReport, RibbonElementPriority.TOP);
        masterReport.addCommandButton(btNonPNSReport, RibbonElementPriority.MEDIUM);

        btDUKReport.addActionListener(this);
        btNonPNSReport.addActionListener(this);

        masterReport.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(masterReport.getControlPanel()),
                new IconRibbonBandResizePolicy(masterReport.getControlPanel())));

        return masterReport;
    }

    public JRibbonBand getMailReportRibbon() {
        JRibbonBand mailReport = new JRibbonBand("Surat Menyurat", null);
        mailReport.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(mailReport.getControlPanel())));

        RichTooltip reportInboxTooltip = new RichTooltip();
        reportInboxTooltip.setTitle("Laporan Surat Masuk");
        reportInboxTooltip.addDescriptionSection("Berisi daftar surat-surat yang masuk");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/new_mail_accept.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            reportInboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInboxReport.setActionRichTooltip(reportInboxTooltip);


        RichTooltip statisticsInboxTooltip = new RichTooltip();
        statisticsInboxTooltip.setTitle("Statistik Surat Masuk");
        statisticsInboxTooltip.addDescriptionSection("Berisi statistik daftar surat-surat yang masuk untuk periode tertentu");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/column_chart.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            statisticsInboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btInboxStatistics.setActionRichTooltip(statisticsInboxTooltip);

        RichTooltip reportOutboxTooltip = new RichTooltip();
        reportOutboxTooltip.setTitle("Laporan Surat Keluar");
        reportOutboxTooltip.addDescriptionSection("Berisi daftar surat-surat yang keluar");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/mail_accept.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            reportOutboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btOutboxReport.setActionRichTooltip(reportOutboxTooltip);


        RichTooltip statisticsOutboxTooltip = new RichTooltip();
        statisticsOutboxTooltip.setTitle("Statistik Surat Keluar");
        statisticsOutboxTooltip.addDescriptionSection("Berisi statistik daftar surat-surat yang keluar untuk periode tertentu");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/line_chart.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            statisticsOutboxTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btOutboxStatistics.setActionRichTooltip(statisticsOutboxTooltip);
        
        
        RichTooltip referenceMailTooltip = new RichTooltip();
        referenceMailTooltip.setTitle("Daftar Surat Keluar Rujukan");
        referenceMailTooltip.addDescriptionSection("Berisi daftar surat-surat yang keluar rujukan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/referencemail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            referenceMailTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btReferenceMail.setActionRichTooltip(referenceMailTooltip);
        
        RichTooltip contractMailTooltip = new RichTooltip();
        contractMailTooltip.setTitle("Daftar Surat Keluar Kontrak");
        contractMailTooltip.addDescriptionSection("Berisi daftar surat-surat yang keluar kontrak");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/contractmail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            contractMailTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btContractMail.setActionRichTooltip(contractMailTooltip);
        
        RichTooltip healthMailTooltip = new RichTooltip();
        healthMailTooltip.setTitle("Daftar Surat Keluar Keterangan Sehat");
        healthMailTooltip.addDescriptionSection("Berisi daftar surat-surat yang keluar Keterangan Sehat");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/healthmail.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            healthMailTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btHealthMail.setActionRichTooltip(healthMailTooltip);


        mailReport.addCommandButton(btInboxReport, RibbonElementPriority.TOP);
//        mailReport.addCommandButton(btInboxStatistics, RibbonElementPriority.MEDIUM);
        mailReport.addCommandButton(btOutboxReport, RibbonElementPriority.MEDIUM);
//        mailOutbox.addCommandButton(btOutboxStatistics, RibbonElementPriority.MEDIUM);
        mailReport.addCommandButton(btReferenceMail, RibbonElementPriority.MEDIUM);
        mailReport.addCommandButton(btContractMail, RibbonElementPriority.MEDIUM);
        mailReport.addCommandButton(btHealthMail, RibbonElementPriority.MEDIUM);

        btInboxReport.addActionListener(this);
        btInboxStatistics.addActionListener(this);
        btOutboxReport.addActionListener(this);
        btOutboxStatistics.addActionListener(this);
        btReferenceMail.addActionListener(this);
        btContractMail.addActionListener(this);
        btHealthMail.addActionListener(this);

        mailReport.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(mailReport.getControlPanel()),
                new IconRibbonBandResizePolicy(mailReport.getControlPanel())));

        return mailReport;
    }

    public JRibbonBand getExpeditionReport() {

        JRibbonBand expeditionReport = new JRibbonBand("Perjalanan Dinas", null);
        expeditionReport.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(expeditionReport.getControlPanel())));

        RichTooltip statusTooltip = new RichTooltip();
        statusTooltip.setTitle("Daftar Perjalanan Dinas");
        statusTooltip.addDescriptionSection("Pengelolaan status perkembangan perjalanan dinas, surat serta perjalananan dinas itu sendiri");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/plane.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            statusTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btExpeditionReport.setActionRichTooltip(statusTooltip);


        RichTooltip statisticsTooltip = new RichTooltip();
        statisticsTooltip.setTitle("Statistik Perjalanan Dinas");
        statisticsTooltip.addDescriptionSection("Berisi perkembangan perjalanan dinas untuk periode tertentu");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/pie_chart.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            statisticsTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btExpeditionStatistics.setActionRichTooltip(statisticsTooltip);

        RichTooltip budgetTooltip = new RichTooltip();
        budgetTooltip.setTitle("Realisasi Anggaran");
        budgetTooltip.addDescriptionSection("Berisi laporan realisasi anggaran untuk perjalanan dinas");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Treasure.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            budgetTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btBudgetRealization.setActionRichTooltip(budgetTooltip);

        btExpeditionReport.addActionListener(this);
        btExpeditionStatistics.addActionListener(this);
        btBudgetRealization.addActionListener(this);

        expeditionReport.addCommandButton(btExpeditionReport, RibbonElementPriority.TOP);
        expeditionReport.addCommandButton(btBudgetRealization, RibbonElementPriority.MEDIUM);
//        expeditionReport.addCommandButton(btExpeditionStatistics, RibbonElementPriority.MEDIUM);


        expeditionReport.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(expeditionReport.getControlPanel()),
                new IconRibbonBandResizePolicy(expeditionReport.getControlPanel())));

        return expeditionReport;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btInboxReport) {
            mainframe.addChildFrame(btInboxReport.getText(), new InboxReportPanel(mainframe));
        } else if (source == btInboxStatistics) {
        } else if (source == btOutboxReport) {
            mainframe.addChildFrame(btOutboxReport.getText(), new OutboxReportPanel(mainframe));
        } else if (source == btOutboxStatistics) {
        } else if (source == btExpeditionReport) {
            mainframe.addChildFrame(btExpeditionReport.getText(), new ExpeditionReportPanel(mainframe));
        } else if (source == btExpeditionStatistics) {

        } else if (source == btBudgetRealization) {
            mainframe.addChildFrame(btBudgetRealization.getText(), new BudgetRealizationPanel(mainframe));
        } else if (source == btDUKReport) {
            mainframe.addChildFrame(btDUKReport.getText(), new EmployeeReportPanel(mainframe));
        } else if (source == btReferenceMail) {
            mainframe.addChildFrame(btReferenceMail.getText(), new ReferenceMailReportPanel(mainframe));
        } else if (source == btContractMail) {
            mainframe.addChildFrame(btContractMail.getText(), new ContractMailReportPanel(mainframe));
        } else if (source == btHealthMail) {
            mainframe.addChildFrame(btHealthMail.getText(), new HealthMailReportPanel(mainframe));
        } else if (source == btNonPNSReport) {
            mainframe.addChildFrame(btNonPNSReport.getText(), new EmployeeNonPNSReportPanel(mainframe));
        }
    }
}
