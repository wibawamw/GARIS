package org.motekar.project.civics.archieve.utils.misc;

import java.io.File;
import org.motekar.util.user.misc.MotekarProperties;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchieveProperties extends MotekarProperties{

    public static final String KABUPATEN = "Kabupaten";
    public static final String KOTAMADYA = "Kotamadya";

    private String state = "";
    private String stateType = "";
    private String capital = "";
    private String province = "";

    private String divisionCode = "";
    private String divisionName = "";

    private File logo = new File("images/logo_daerah.jpg");
    private File logo2 = new File("images/logo_daerah.jpg");
    private File logo3 = new File("images/logo_daerah.jpg");

    public ArchieveProperties() {
    }

    public ArchieveProperties(String fileName) {
        super(fileName);
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public File getLogo() {
        return logo;
    }

    public String getLogoFileName() {
        return logo.getPath();
    }

    public void setLogo(File logo) {
        this.logo = logo;
    }

    public void setLogo(String fileName) {
        this.logo = new File(fileName);
    }

    public File getLogo2() {
        return logo2;
    }

    public void setLogo2(File logo2) {
        this.logo2 = logo2;
    }

    public String getLogo2FileName() {
        return logo2.getPath();
    }

    public void setLogo2(String fileName) {
        this.logo2 = new File(fileName);
    }

    public File getLogo3() {
        return logo3;
    }

    public void setLogo3(File logo3) {
        this.logo3 = logo3;
    }

    public String getLogo3FileName() {
        return logo3.getPath();
    }

    public void setLogo3(String fileName) {
        this.logo3 = new File(fileName);
    }
}
