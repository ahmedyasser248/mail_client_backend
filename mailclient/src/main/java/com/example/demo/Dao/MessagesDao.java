package com.example.demo.Dao;

import com.example.demo.Entities.Message;
import com.example.demo.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessagesDao {
    public Boolean sendMessage(Message message, MultipartFile [] multipartFiles);
    public Boolean saveToDraft(Message message, MultipartFile [] multipartFiles);
    public Boolean sendFromDraft(Message message,MultipartFile[] multipartFiles);
    public Boolean deleteMessage(int messageID);
    public List<Message> readMessagesFromInbox(User user,int page,String sortType);
    public List<Message> readMessagesFromDraft(User user, int page,String sortType);
    public List<Message> readMessagesFromSent(User user, int page,String sortType);
    public Boolean downloadAttachment(int messageId , String fileName);
}
