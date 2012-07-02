package org.motekar.project.civics.archieve.mail.objects;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class DocumentFile {
    
    private Long index = Long.valueOf(0);
    private String fileName = "";
    private byte[] fileStream = null;
    private String description = "";
    
    private boolean styled = false;
    
    public DocumentFile() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileStream() {
        return fileStream;
    }

    public void setFileStream(byte[] fileStream) {
        this.fileStream = fileStream;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    private String getExtractedFileName() {
        ArrayList<String> aString = new ArrayList<String>();
        
        StringTokenizer token = new StringTokenizer(fileName, ".");
        
        while (token.hasMoreElements()) {
            aString.add(token.nextToken());
        }
        
        if (!aString.isEmpty()) {
            return aString.get(0);
        }
        
        return "";
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>"+getExtractedFileName()+"</b>";
        }
        return fileName;
    }
}
