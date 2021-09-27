package com.example.demo.Service;

import com.example.demo.Dao.MessagesDao;
import com.example.demo.Entities.Message;
import com.example.demo.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class messageService {
    @Autowired
    MessagesDao messagesDao;

    public Boolean sendMessage(Message message, MultipartFile[] multipartFiles){
    return messagesDao.sendMessage(message,multipartFiles);
    }
    public Boolean saveDraft(Message message,MultipartFile [] multipartFiles){
        return messagesDao.saveToDraft(message,multipartFiles);
    }
    public boolean sendFromDraft(Message message , MultipartFile[] multipartFiles){
        return messagesDao.sendFromDraft(message,multipartFiles);
    }
    public boolean deleteMessage(int messageID){
        return messagesDao.deleteMessage(messageID);
    }
    public List<Message> readInbox(User user, int page,String sortType){
       return messagesDao.readMessagesFromInbox(user,page,sortType);
    }
    public List<Message> readDraft(User user, int page,String sortType){
        return messagesDao.readMessagesFromDraft(user,page,sortType);
    }
    public List<Message>readSent(User user, int page,String sortType){
        return messagesDao.readMessagesFromSent(user,page,sortType);
    }
    public Boolean downloadAttachment (int messageId , String fileName){
        return messagesDao.downloadAttachment(messageId,fileName);
    }


}
