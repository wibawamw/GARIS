package org.motekar.project.civics.archieve.assets.master.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class Condition extends AssetObject {

    private String conditionCode = "";
    private String conditionName = "";
    private Integer startValue = Integer.valueOf(0);
    private Integer endValue = Integer.valueOf(0);
    private boolean styled = false;

    public Condition() {
    }

    public Condition(Long index) {
        super(index);
    }

    public Condition(String text, boolean iscode) {
        if (iscode) {
            this.conditionCode = text;
        } else {
            this.conditionName = text;
        }
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public Integer getEndValue() {
        return endValue;
    }

    public void setEndValue(Integer endValue) {
        this.endValue = endValue;
    }

    public Integer getStartValue() {
        return startValue;
    }

    public void setStartValue(Integer startValue) {
        this.startValue = startValue;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + conditionCode + "</b>"
                    + "<br style=\"font-size:40%\">" + conditionName + "(" + startValue + " - " + endValue + ")</br>";
        }

        return conditionName;
    }
}
