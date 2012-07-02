package org.motekar.project.civics.archieve.master.ui;

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
public class MasterDataRibbonBand extends JRibbonBand implements ActionListener {

    private JCommandButton btEmployee = new JCommandButton("Pegawai", Mainframe.getResizableIconFromSource("resource/business_user.png"));
    private JCommandButton btEmployeeNonPNS = new JCommandButton("Pegawai Non PNS", Mainframe.getResizableIconFromSource("resource/emblem-people.png"));
    private JCommandButton btDivision = new JCommandButton("Bagian", Mainframe.getResizableIconFromSource("resource/Division.png"));
    private JCommandButton btPrice = new JCommandButton("Standar Harga", Mainframe.getResizableIconFromSource("resource/Postage stamp.png"));
    private JCommandButton btAccount = new JCommandButton("Struktur Rekening", Mainframe.getResizableIconFromSource("resource/Account.png"));
    private JCommandButton btProgram = new JCommandButton("Program dan Kegiatan", Mainframe.getResizableIconFromSource("resource/program.png"));
    private JCommandButton btBudget = new JCommandButton("Anggaran", Mainframe.getResizableIconFromSource("resource/Coins.png"));

    private ArchieveMainframe mainframe;

    public MasterDataRibbonBand(ArchieveMainframe mainframe,String title, ResizableIcon icon) {
        super(title, icon);
        this.mainframe = mainframe;
        construcButtons();
    }

    private void construcButtons() {
        setResizePolicies((List) Arrays.asList(new IconRibbonBandResizePolicy(getControlPanel())));


        RichTooltip employeeTooltip = new RichTooltip();
        employeeTooltip.setTitle("Pegawai Negeri Sipil");
        employeeTooltip.addDescriptionSection("Pengelolaan Pegawai yang ada di lingkungan SKPD");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/business_user.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            employeeTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btEmployee.setActionRichTooltip(employeeTooltip);
        
        RichTooltip employeeNonPNSTooltip = new RichTooltip();
        employeeNonPNSTooltip.setTitle("Pegawai Non PNS");
        employeeNonPNSTooltip.addDescriptionSection("Pengelolaan Pegawai selain PNS seperti tenaga kontrak, tenaga honorer, PTTP, "
                + "yang ada di lingkungan SKPD");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/emblem-people.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            employeeNonPNSTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btEmployeeNonPNS.setActionRichTooltip(employeeTooltip);


        RichTooltip divisionTooltip = new RichTooltip();
        divisionTooltip.setTitle("Bagian / Bidang");
        divisionTooltip.addDescriptionSection("Pengelolaan Bagian yang ada di di lingkungan Dinas / Badan / Kantor");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Division.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            divisionTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btDivision.setActionRichTooltip(divisionTooltip);

        RichTooltip priceTooltip = new RichTooltip();
        priceTooltip.setTitle("Standar Harga");
        priceTooltip.addDescriptionSection("Pengelolaan dan penetapan standar harga untuk perjalanan dinas");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Postage stamp.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            priceTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btPrice.setActionRichTooltip(priceTooltip);

        RichTooltip accountTooltip = new RichTooltip();
        accountTooltip.setTitle("Struktur Rekening");
        accountTooltip.addDescriptionSection("Mendefinisikan Struktur Kode Rekening pada suatu Dinas");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Account.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            accountTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btAccount.setActionRichTooltip(accountTooltip);


        RichTooltip programTooltip = new RichTooltip();
        programTooltip.setTitle("Program dan Kegiatan");
        programTooltip.addDescriptionSection("Mendefinisikan Program dan Kegiatan yang dilaksanakan pada suatu Dinas");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/program.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            programTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btProgram.setActionRichTooltip(programTooltip);

        RichTooltip budgetTooltip = new RichTooltip();
        budgetTooltip.setTitle("Anggaran");
        budgetTooltip.addDescriptionSection("Mendefinisikan besarnya anggaran untuk masing-masing program dan kegiatan");
        try {
            BufferedImage img = ImageIO.read(ArchieveMainframe.class.getResource("/resource/Coins.png"));
            Image scaleImage = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            budgetTooltip.setMainImage(scaleImage);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        btBudget.setActionRichTooltip(budgetTooltip);

        addCommandButton(btEmployee, RibbonElementPriority.TOP);
        addCommandButton(btEmployeeNonPNS, RibbonElementPriority.MEDIUM);
        addCommandButton(btDivision, RibbonElementPriority.MEDIUM);
        addCommandButton(btPrice, RibbonElementPriority.MEDIUM);
        addCommandButton(btProgram, RibbonElementPriority.MEDIUM);
        addCommandButton(btAccount, RibbonElementPriority.MEDIUM);
        addCommandButton(btBudget, RibbonElementPriority.MEDIUM);

        btEmployee.addActionListener(this);
        btEmployeeNonPNS.addActionListener(this);
        btDivision.addActionListener(this);
        btPrice.addActionListener(this);
        btProgram.addActionListener(this);
        btAccount.addActionListener(this);
        btBudget.addActionListener(this);

        setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(getControlPanel()),
                new IconRibbonBandResizePolicy(getControlPanel())));
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btEmployee) {
            mainframe.addChildFrame(btEmployee.getText(), new EmployeePanel(mainframe));
        } else if (source == btEmployeeNonPNS) {
            mainframe.addChildFrame(btEmployeeNonPNS.getText(), new EmployeeNonPNSPanel(mainframe));
        } else if (source == btPrice) {
            mainframe.addChildFrame(btPrice.getText(), new StandardPricePanel(mainframe));
        } else if (source == btDivision) {
            mainframe.addChildFrame(btDivision.getText(), new DivisionPanel(mainframe));
        } else if (source == btAccount) {
            mainframe.addChildFrame(btAccount.getText(), new AccountPanel(mainframe));
        } else if (source == btProgram) {
            mainframe.addChildFrame(btProgram.getText(), new ProgramActivityPanel(mainframe));
        } else if (source == btBudget) {
            mainframe.addChildFrame(btBudget.getText(), new BudgetPanel(mainframe));
        }
    }


}
