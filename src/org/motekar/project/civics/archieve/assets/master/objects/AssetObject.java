package org.motekar.project.civics.archieve.assets.master.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssetObject {

    private Long index = Long.valueOf(0);
    
    public AssetObject() {
    }

    public AssetObject(Long index) {
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
    
}
