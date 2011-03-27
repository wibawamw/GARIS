package org.motekar.project.civics.archieve.mail.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class OutboxFile {

    private Long outboxIndex = Long.valueOf(0);
    private String fileName = "";
    private byte[] fileStream = null;

    public OutboxFile() {
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

    public Long getOutboxIndex() {
        return outboxIndex;
    }

    public void setOutboxIndex(Long outboxIndex) {
        this.outboxIndex = outboxIndex;
    }

    public String getFileExtension() {
        String extension = "";

        StringTokenizer token = new StringTokenizer(fileName, ".");

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
