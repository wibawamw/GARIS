package org.motekar.project.civics.archieve.payroll.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmissionFile {
    private Long submissionIndex = Long.valueOf(0);
    private String fileName = "";
    private byte[] fileStream = null;

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

    public Long getSubmissionIndex() {
        return submissionIndex;
    }

    public void setSubmissionIndex(Long submissionIndex) {
        this.submissionIndex = submissionIndex;
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
