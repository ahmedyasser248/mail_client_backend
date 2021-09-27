package com.example.demo.Entities;

public class Attachment {
    private  byte [] data ;
    private String fileName,fileDescription ;

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
