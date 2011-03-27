package org.motekar.project.civics.archieve.master.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class CurriculumVitae {

    private Long cvIndex = Long.valueOf(0);
    private Long employeeIndex = Long.valueOf(0);
    private String fileName = "";

    private byte[] fileStream = null;

    public CurriculumVitae() {
    }

    public Long getCvIndex() {
        return cvIndex;
    }

    public void setCvIndex(Long cvIndex) {
        this.cvIndex = cvIndex;
    }

    public Long getEmployeeIndex() {
        return employeeIndex;
    }

    public void setEmployeeIndex(Long employeeIndex) {
        this.employeeIndex = employeeIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        String extension = "";

        StringTokenizer token = new StringTokenizer(fileName,".");

        while (token.hasMoreElements()) {
            extension = token.nextToken();
        }

        return extension;
    }

    public byte[] getFileStream() {
        return fileStream;
    }

    public void setFileStream(byte[] fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
