package org.motekar.project.civics.archieve.mail.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class SP2DFile {
    private Long sp2dIndex = Long.valueOf(0);
    private String fileName = "";
    private byte[] fileStream = null;

    public SP2DFile() {
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

    public Long getSp2dIndex() {
        return sp2dIndex;
    }

    public void setSp2dIndex(Long sp2dIndex) {
        this.sp2dIndex = sp2dIndex;
    }
    
    public String getFileExtension() {
        String extension = "";

        StringTokenizer token = new StringTokenizer(fileName,".");

        while (token.hasMoreElements()) {
            extension = token.nextToken();
        }

        return extension;
    }
    
    @Override
    public String toString() {
        return fileName;
    }
}
