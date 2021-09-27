package com.example.demo.Entities;

public class RangeHolder {
    private int usedMessageID;
    private int firstMessageID;
    private int lastMessageID;
    private int firstAttachmentID;
    private int lastAttachmentID;
    RangeHolder(){

    }
    RangeHolder(int usedMessageID,int firstMessageID, int lastMessageID , int firstAttachmentID,int lastAttachmentID){
        this.usedMessageID=usedMessageID;
        this.firstMessageID=firstMessageID;
        this.lastMessageID=lastMessageID;
        this.firstAttachmentID=firstAttachmentID;
        this.lastAttachmentID=lastAttachmentID;
    }

    public int getFirstAttachmentID() {
        return firstAttachmentID;
    }

    public int getFirstMessageID() {
        return firstMessageID;
    }

    public int getLastAttachmentID() {
        return lastAttachmentID;
    }

    public int getLastMessageID() {
        return lastMessageID;
    }

    public int getUsedMessageID() {
        return usedMessageID;
    }

    public void setFirstAttachmentID(int firstAttachmentID) {
        this.firstAttachmentID = firstAttachmentID;
    }

    public void setFirstMessageID(int firstMessageID) {
        this.firstMessageID = firstMessageID;
    }

    public void setLastAttachmentID(int lastAttachmentID) {
        this.lastAttachmentID = lastAttachmentID;
    }

    public void setLastMessageID(int lastMessageID) {
        this.lastMessageID = lastMessageID;
    }

    public void setUsedMessageID(int usedMessageID) {
        this.usedMessageID = usedMessageID;
    }
}
