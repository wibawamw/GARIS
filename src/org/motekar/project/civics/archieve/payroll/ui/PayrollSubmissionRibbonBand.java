package org.motekar.project.civics.archieve.payroll.ui;

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

public class PayrollSubmissionRibbonBand implements ActionListener {

    private JCommandButton btSubmissionType = new JCommandButton("Jenis Pengajuan Gaji",
            Mainframe.getResizableIconFromSource("resource/SubmissionType.png"));
    private JCommandButton btSubmissionRequirement = new JCommandButton("Syarat-syarat Pengajuan",
            Mainframe.getResizableIconFromSource("resource/SubmissionRequirement.png"));
    private JCommandButton btPayrollSubmission = new JCommandButton("Pengajuan Gaji",
            Mainframe.getResizableIconFromSource("resource/PayrollSubmission.png"));
    private JCommandButton btSubmissionReport = new JCommandButton("Laporan Pengajuan Gaji",
            Mainframe.getResizableIconFromSource("resource/SubmissionReport.png"));
    
    
    
    
    /**
     * 
     */
    private ArchieveMainframe mainframe;

    public PayrollSubmissionRibbonBand(ArchieveMainframe mainframe) {
        this.mainframe = mainframe;
    }

    public JRibbonBand getSubmisionMasterRibbon() {
        JRibbonBand payrollSubmission = new JRibbonBand("Pengajuan Gaji", null);
        payrollSubmission.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(payrollSubmission.getControlPanel())));

        RichTooltip typeTooltip = new RichTooltip();
        typeTooltip.setTitle("Jenis Pengajuan Gaji");
        typeTooltip.addDescriptionSection("Berisi daftar jenis pengajuan gaji");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/SubmissionType.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            typeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btSubmissionType.setActionRichTooltip(typeTooltip);
        
        RichTooltip reqTooltip = new RichTooltip();
        reqTooltip.setTitle("Syarat-syarat Pengajuan");
        reqTooltip.addDescriptionSection("Berisi daftar syarat-syarat dokumen yang harus dipenuhi");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/SubmissionRequirement.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            reqTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btSubmissionRequirement.setActionRichTooltip(reqTooltip);
        
        RichTooltip psTooltip = new RichTooltip();
        psTooltip.setTitle("Pengajuan Gaji");
        psTooltip.addDescriptionSection("Berisi daftar pengajuan gaji oleh pegawai");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/PayrollSubmission.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            psTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btPayrollSubmission.setActionRichTooltip(psTooltip);

        payrollSubmission.addCommandButton(btSubmissionType, RibbonElementPriority.TOP);
        payrollSubmission.addCommandButton(btSubmissionRequirement, RibbonElementPriority.MEDIUM);
        payrollSubmission.addCommandButton(btPayrollSubmission, RibbonElementPriority.MEDIUM);

        btSubmissionType.addActionListener(this);
        btSubmissionRequirement.addActionListener(this);
        btPayrollSubmission.addActionListener(this);

        payrollSubmission.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(payrollSubmission.getControlPanel()),
                new IconRibbonBandResizePolicy(payrollSubmission.getControlPanel())));

        return payrollSubmission;
    }
    
    public JRibbonBand getSubmisionReportRibbon() {
        JRibbonBand payrollSubmissionRep = new JRibbonBand("Pengajuan Gaji", null);
        payrollSubmissionRep.setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(payrollSubmissionRep.getControlPanel())));

        RichTooltip typeTooltip = new RichTooltip();
        typeTooltip.setTitle("Pengajuan Gaji");
        typeTooltip.addDescriptionSection("Laporan-laporan pengajuan gaji dari daftar pengajuan gaji");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/SubmissionReport.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            typeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btSubmissionReport.setActionRichTooltip(typeTooltip);
        

        payrollSubmissionRep.addCommandButton(btSubmissionReport, RibbonElementPriority.TOP);

        btSubmissionReport.addActionListener(this);

        payrollSubmissionRep.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(payrollSubmissionRep.getControlPanel()),
                new IconRibbonBandResizePolicy(payrollSubmissionRep.getControlPanel())));

        return payrollSubmissionRep;
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btSubmissionType) {
            mainframe.addChildFrame(btSubmissionType.getText(), new SubmissionTypePanel(mainframe));
        } else if (source == btSubmissionRequirement) {
            mainframe.addChildFrame(btSubmissionRequirement.getText(), new SubmissionRequirementPanel(mainframe));
        } else if (source == btPayrollSubmission) {
            mainframe.addChildFrame(btPayrollSubmission.getText(), new PayrollSubmissionPanel(mainframe));
        } else if (source == btSubmissionReport) {
        } 
    }
}

