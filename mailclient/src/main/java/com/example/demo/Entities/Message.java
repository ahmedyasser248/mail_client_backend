package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class Message {
    private int idMessages;
    private ArrayList<String> receivers = new ArrayList<>();
    private String sender ;
    private ArrayList<String> fileNames= new ArrayList<>();
    private String subject ;
    private String date;
    private String source;
    public void addReceiver(String receiver){
        this.receivers.add(receiver);
    }
    public ArrayList<String> getReceiver() {
        return receivers;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }


    @JsonSetter("fileNames")
    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }
    public void addFileName(String fileName){
        fileNames.add(fileName);
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    public int getIdMessages() {
        return idMessages;
    }

    public String getDate() {
        return date;
    }

    public void setIdMessages(int idMessages) {
        this.idMessages = idMessages;
    }

    public String getSender() {
        return sender;
    }

    public String getSource() {
        return source;
    }
    @JsonSetter("receiver")
    public void setReceiver(ArrayList<String> receiver) {
        this.receivers = receiver;
    }
    @JsonSetter("sender")
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }
    @JsonSetter("date")
    public void setDate(String date) {
        this.date = date;
    }
    @JsonSetter("subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }
    @JsonSetter("source")
    public void setSource(String source) {
        this.source = source;
    }
    public String receiverString(){
        String temp="";
        for (int i = 0 ; i < receivers.size();i++){
            temp=temp+this.receivers.get(i)+" ";
        }
        System.out.println(temp);
      return temp ;
    }
    public static void main(String [] args) throws JsonProcessingException {
        ObjectMapper test = new ObjectMapper();
        Message message1 = new Message();
        message1.setSubject("first");
        message1.setDate("2020-01-10");
        message1.addReceiver("yasser@gmail.com");

        message1.setSender("jimmy@gmail.com");


        Message message2 = new Message();
        message2.setSubject("test1");
        message2.setDate("2020-02-10");
        message2.addReceiver("gabal@gmail.com");
        message2.setSender("yasser@gmail.com");


        Message message3 = new Message();
        message3.setSubject("deal");
        message3.setDate("2020-03-10");
        message3.addReceiver("yasser@gmail.com");
        message3.setSender("jimmy@gmail.com");


        Message message4 = new Message();
        message4.setSubject("hope");
        message4.setDate("2020-04-10");
        message4.addReceiver("gabal@gmail.com");

        message4.setSender("jimmy@gmail.com");



        Message message5 = new Message();
        message5.setSubject("pain");
        message5.setDate("2020-05-10");
        message5.addReceiver("jimmy@gmail.com");
        message5.addReceiver("yasser@gmail.com");
        message5.setSender("gabal@gmail.com");

        Message message6 = new Message();
        message6.setSubject("gain");
        message6.setDate("2020-06-10");
        message6.addReceiver("yasser@gmail.com");
        message6.addReceiver("jimmy@gmail.com");
        message6.setSender("gabal@gmail.com");

        Message message7 = new Message();
        message7.setSubject("shit");
        message7.setDate("2020-07-10");
        message7.addReceiver("gabal@gmail.com");
        message7.addReceiver("jimmy@gmail.com");
        message7.setSender("yasser@gmail.com");

        Message message8 = new Message();
        message8.setDate("2020-09-10");
        message8.addReceiver("yasser@gmail.com");
        message8.addReceiver("jimmy@gmail.com");
        message8.setSender("gabal@gmail.com");
        System.out.println(test.writeValueAsString(message1));
        System.out.println(test.writeValueAsString(message2));
        System.out.println(test.writeValueAsString(message3));
        System.out.println(test.writeValueAsString(message4));
        System.out.println(test.writeValueAsString(message5));
        System.out.println(test.writeValueAsString(message6));
        System.out.println(test.writeValueAsString(message7));
        System.out.println(test.writeValueAsString(message8));

    }
}
