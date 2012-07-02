package org.motekar.project.civics.archieve.assets.procurement.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class Signer {
    
    public static final Integer TYPE_COMMANDER = Integer.valueOf(1);
    public static final Integer TYPE_SIGNER = Integer.valueOf(2);
    public static final Integer TYPE_GOVERNOR = Integer.valueOf(3);
    
    private Long index = Long.valueOf(0);
    private String signerName = "";
    private String signerNIP = "";
    private Integer signerType = Integer.valueOf(0);
    
    public Signer() {
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getSignerNIP() {
        return signerNIP;
    }

    public void setSignerNIP(String signerNIP) {
        this.signerNIP = signerNIP;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public Integer getSignerType() {
        return signerType;
    }

    public void setSignerType(Integer signerType) {
        this.signerType = signerType;
    }

    @Override
    public String toString() {
        return signerName;
    }
}
