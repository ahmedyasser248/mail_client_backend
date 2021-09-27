package com.example.demo.Entities;

public class MessagesAttachments {
    private int messageID;
    private int attachmentID;
    private String fileName;
   public MessagesAttachments(int messageID,int attachmentID,String fileName ){
        this.messageID=messageID;
        this.attachmentID=attachmentID;
        this.fileName=fileName;
    }
    public MessagesAttachments(){}
    public String getFileName() {
        return fileName;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    public int getAttachmentID() {
        return attachmentID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
