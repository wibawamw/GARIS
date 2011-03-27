package org.motekar.project.civics.archieve.expedition.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionResult {

    private Long resultIndex = Long.valueOf(0);
    private String notes = "";

    public ExpeditionResult() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getResultIndex() {
        return resultIndex;
    }

    public void setResultIndex(Long resultIndex) {
        this.resultIndex = resultIndex;
    }

    @Override
    public String toString() {
        return notes;
    }

}
