package org.motekar.project.civics.archieve.master.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class Division {

    private String code = "";
    private String name = "";
    //
    private boolean styled = false;

    public Division() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof Division) {
            Division division = (Division) obj;
            if (this.getCode().equals(division.getCode()) &&
                    this.getName().equals(division.getName())) {
               return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.code != null ? this.code.hashCode() : 0);
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
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
            return "<html><b>" + code + "</b>"
                    + "<br style=\"font-size:40%\"><i>" + name + "</i></br>";
        }
        if (code.equals("") || (name.equals(""))) {
            return code + " " + name;
        }
        return name;
    }
}
