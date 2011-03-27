package org.motekar.project.civics.archieve.mail.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class InboxFile {

    private Long inboxIndex = Long.valueOf(0);
    private String fileName = "";
    private byte[] fileStream = null;

    public InboxFile() {
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

    public Long getInboxIndex() {
        return inboxIndex;
    }

    public void setInboxIndex(Long inboxIndex) {
        this.inboxIndex = inboxIndex;
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
