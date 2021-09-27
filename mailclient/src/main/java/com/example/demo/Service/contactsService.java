package com.example.demo.Service;

import com.example.demo.Dao.ContactsDao;
import com.example.demo.Entities.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class contactsService {
    @Autowired
    ContactsDao contactsDao;
    public Boolean addContact(Contact contact){
        return contactsDao.addContact(contact);
    }
    public boolean deleteContact(Contact contact){
        return deleteContact(contact);
    }
}
