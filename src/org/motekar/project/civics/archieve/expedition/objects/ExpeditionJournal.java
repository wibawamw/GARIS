package org.motekar.project.civics.archieve.expedition.objects;

import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionJournal {

    private Long journalIndex = Long.valueOf(0);
    private String reportNumber = "";
    private String reportPlace = "";
    private Date reportDate = null;

    private AssignmentLetter letter = null;

    private ArrayList<Expedition> expeditions = new ArrayList<Expedition>();
    private ArrayList<ExpeditionResult> result = new ArrayList<ExpeditionResult>();

    private boolean styled = false;
    
    private Unit unit = null;

    public ExpeditionJournal() {
    }

    public ExpeditionJournal(Long journalIndex) {
        this.journalIndex = journalIndex;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
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

    public String getReportPlace() {
        return reportPlace;
    }

    public void setReportPlace(String reportPlace) {
        this.reportPlace = reportPlace;
    }

    public AssignmentLetter getLetter() {
        return letter;
    }

    public void setLetter(AssignmentLetter letter) {
        this.letter = letter;
    }

    public ArrayList<ExpeditionResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<ExpeditionResult> result) {
        this.result = result;
    }

    public ArrayList<Expedition> getExpeditions() {
        return expeditions;
    }

    public void setExpeditions(ArrayList<Expedition> expeditions) {
        this.expeditions = expeditions;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + reportNumber + "</b>"
                    + "<br style=\"font-size:40%\">Laporan Atas Perjalanan Dinas Nomor</br>"
                    + "<br style=\"font-size:40%\">"+ letter.getDocumentNumber()+ "</br>";
        }
        return "[" + reportNumber + "] "
                + "Laporan Perjalanan Dinas "
                + letter.getDocumentNumber() ;
    }

}
