package org.motekar.project.civics.archieve.utils.misc;

/**
 *
 * @author Muhamad Wibawa
 */
public class ProfileAccount {

    public static final String KABUPATEN = "Kabupaten";
    public static final String KOTAMADYA = "Kota";
    
    private String company = "";
    private String address = "";
    
    private String state = "";
    private String stateType = "";
    private String capital = "";
    private String province = "";

    private byte[] byteLogo = null;
    private byte[] byteLogo2 = null;
    private byte[] byteLogo3 = null;
    
    public ProfileAccount() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public byte[] getByteLogo() {
        return byteLogo;
    }

    public void setByteLogo(byte[] byteLogo) {
        this.byteLogo = byteLogo;
    }

    public byte[] getByteLogo2() {
        return byteLogo2;
    }

    public void setByteLogo2(byte[] byteLogo2) {
        this.byteLogo2 = byteLogo2;
    }

    public byte[] getByteLogo3() {
        return byteLogo3;
    }

    public void setByteLogo3(byte[] byteLogo3) {
        this.byteLogo3 = byteLogo3;
    }
    
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }
}
