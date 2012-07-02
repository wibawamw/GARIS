package org.motekar.project.civics.archieve.expedition.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionJournal {

    private Long journalIndex = Long.valueOf(0);
    private Date reportDate = null;
    
    private Expedition expedition = null;
    private String fundingSource = "";
    private ArrayList<ExpeditionResult> result = new ArrayList<ExpeditionResult>();
    private boolean styled = false;

    public ExpeditionJournal() {
    }

    public ExpeditionJournal(Long journalIndex) {
        this.journalIndex = journalIndex;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public Long getJournalIndex() {
        return journalIndex;
    }

    public void setJournalIndex(Long journalIndex) {
        this.journalIndex = journalIndex;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Expedition getExpedition() {
        return expedition;
    }

    public void setExpedition(Expedition expedition) {
        this.expedition = expedition;
    }

    public ArrayList<ExpeditionResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<ExpeditionResult> result) {
        this.result = result;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" 
                    + "Laporan SPPD Nomor" + "</b>"
                    + "<br>"+ expedition.getDocumentNumber()+ "</br>";
        }
        return "Laporan SPPD "
                + expedition.getDocumentNumber() ;
    }

}
