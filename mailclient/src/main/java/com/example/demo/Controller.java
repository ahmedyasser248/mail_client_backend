package com.example.demo;

import com.example.demo.Entities.Contact;
import com.example.demo.Entities.Message;
import com.example.demo.Entities.User;
import com.example.demo.Service.contactsService;
import com.example.demo.Service.messageService;
import com.example.demo.Service.userService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    userService userServiceObj;
    @Autowired
    messageService messageServiceObj;
    @Autowired
    contactsService contactsServiceObj;
    @PostMapping(value = "/signUp")
    public Boolean signup(@RequestBody User user){
        System.out.println("hello");
        userServiceObj.insertUser(user);
        return true;
    }
    @PostMapping(value="/signIn")
    public boolean signIn(@RequestBody User user){
        System.out.println(user.getUserName()+" "+user.getPassword());
       return userServiceObj.checkUser(user);
    }
    @PostMapping(value="/sendMail")
    public Boolean sendMessage(@RequestParam(value = "message") String messageStr, @RequestParam(value = "file",required = false)MultipartFile[] multipartFiles){
        System.out.println("i reach here");
        ObjectMapper mapper =new ObjectMapper();
       Message message= null;
        try{
         message = mapper.readValue(messageStr,Message.class);
        }
       catch (Exception e){
           System.out.println("error enbr 7");
       }
      return   messageServiceObj.sendMessage(message,multipartFiles);
    }
    @PostMapping(value="/saveDraft")
    public Boolean saveDraft(@RequestParam(value = "message") String messageStr,@RequestParam(value = "file",required = false)MultipartFile[] multipartFiles){
        ObjectMapper mapper = new ObjectMapper();
        Message message= null;
        System.out.println("got here");
        try{
            message=mapper.readValue(messageStr,Message.class);
        }catch (Exception e){
            System.out.println("error in save draft controller");
        }
        return messageServiceObj.saveDraft(message,multipartFiles);
    }
    @PostMapping(value = "/sendFromDraft")
    public boolean sendFromDraft(@RequestParam(value = "message") String messageStr , @RequestParam (value ="file",required = false)MultipartFile[] multipartFiles){
        ObjectMapper mapper = new ObjectMapper();
        Message message =null ;
        System.out.println("got here");
        try{
            message=mapper.readValue(messageStr,Message.class);
        }catch (Exception e){
            System.out.println("error in send from draft");
        }
        return messageServiceObj.sendFromDraft(message,multipartFiles);
    }
    @PostMapping(value="/deleteMessage")
    public Boolean DeleteMessage(@RequestBody int num){
        return messageServiceObj.deleteMessage(num);
    }
    @GetMapping(value="/getInbox")
    public List<Message> readInbox(@RequestParam("user") String userStr,@RequestParam(value = "page")int page,@RequestParam(value = "sort" , required = false)String sortType){
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
             user = mapper.readValue(userStr, User.class);
            System.out.println(user.getEmail());
            System.out.println(page);
            System.out.println(sortType);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("error in getDraft method");
        }
    return  messageServiceObj.readInbox(user,page,sortType);
    }
    @GetMapping(value="/getDraft")
    public List<Message> readDraft(@RequestParam("user") String userStr,@RequestParam(value = "page")int page,@RequestParam(value = "sort",required = false) String sortType){
       // System.out.println(user.getEmail());
        ObjectMapper mapper = new ObjectMapper();
        User user =null;
      try {
           user = mapper.readValue(userStr, User.class);
      }catch (Exception e){
          System.out.println(e);
          System.out.println("error in getDraft method");
      }
      return messageServiceObj.readDraft(user,page,sortType);
    }
    @GetMapping(value="/getSent")
    public List<Message> readSent(@RequestParam("user") User user,@RequestParam(value = "page")int page,@RequestParam(value = "sort",required = false)String sortType ){
        System.out.println(user.getEmail());
        return messageServiceObj.readSent(user,page,sortType);
    }
    @PostMapping(value = "/downloadAttachment")
    public boolean downloadAttachment(@RequestParam(value = "messageId") int messageID,@RequestParam(value = "fileName") String fileName){
        return messageServiceObj.downloadAttachment(messageID,fileName);
    }
    @PostMapping(value = "/addContact")
    public Boolean addContact(@RequestParam Contact contact){
        return  contactsServiceObj.addContact(contact);
    }
    @PostMapping(value ="/deleteContact")
    public Boolean deleteContact(@RequestParam Contact contact){
        return contactsServiceObj.deleteContact(contact);
    }
}
