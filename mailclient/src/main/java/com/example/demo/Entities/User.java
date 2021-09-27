package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {

    private String Email;
    private String userName;
    private String password;
    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }
    @JsonSetter("Email")
    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return Email;
    }

    public String getUserName() {
        return userName;
    }
    @JsonSetter("username")
    public void setUserName(String userName) {
        this.userName = userName;
    }
 public  static void main(String [] args) throws JsonProcessingException {
     ObjectMapper test = new ObjectMapper();
     User User = new User();
     User.setEmail("yasser@gmail");
     User.setUserName("ahmedYasser");
     User.setPassword("ahmedyasser2");
     System.out.println(test.writeValueAsString(User));

 }
}
