package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Contact {
    private String contactEmail;
    private String userEmail;
    private String userName;
    public Contact(){}
    public Contact(String contactEmail,String userEmail,String userName){
        this.contactEmail=contactEmail;
        this.userEmail=userEmail;
        this.userName=userName;
    }

    @JsonSetter("userEmail")
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    @JsonSetter("contactEmail")
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    @JsonSetter("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getUserName() {
        return userName;
    }
    public static void main(String [] args) throws JsonProcessingException {
        Contact one = new Contact("ahmedyasser","jimmy","abc");
        Contact two = new Contact("jimmy","AhmedYasser","efg");
        Contact three = new Contact("gabal","jimmy","hij");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(one));
        System.out.println(mapper.writeValueAsString(two));
        System.out.println(mapper.writeValueAsString(three));
    }
}
