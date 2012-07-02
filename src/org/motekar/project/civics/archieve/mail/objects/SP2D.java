package org.motekar.project.civics.archieve.mail.objects;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.SKPD;

/**
 *
 * @author Muhamad Wibawa
 */
public class SP2D {

    private Long index = Long.valueOf(0);
    private SKPD skpd = null;
    private String sp2dNumber = "";
    private Date sp2dDate = null;
    private String receiver = "";
    private String purpose = "";
    private BigDecimal amount = BigDecimal.ZERO;
    private String description = "";
    
    private boolean styled = false;
    
    private ArrayList<SP2DFile> sP2DFiles = new ArrayList<SP2DFile>();
    
    public SP2D() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public SKPD getSkpd() {
        return skpd;
    }

    public void setSkpd(SKPD skpd) {
        this.skpd = skpd;
    }

    public Date getSp2dDate() {
        return sp2dDate;
    }

    public void setSp2dDate(Date sp2dDate) {
        this.sp2dDate = sp2dDate;
    }

    public String getSp2dNumber() {
        return sp2dNumber;
    }

    public void setSp2dNumber(String sp2dNumber) {
        this.sp2dNumber = sp2dNumber;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public ArrayList<SP2DFile> getsP2DFiles() {
        return sP2DFiles;
    }

    public void setsP2DFiles(ArrayList<SP2DFile> sP2DFiles) {
        this.sP2DFiles = sP2DFiles;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return "<html><b>" + sp2dNumber +" ("+sdf.format(sp2dDate) +")"+ "</b>"
                    + "<br style=\"font-size:40%\"><i>" + skpd.getName() + "</i></br>";
        }
        
        return sp2dNumber;
    }
}
