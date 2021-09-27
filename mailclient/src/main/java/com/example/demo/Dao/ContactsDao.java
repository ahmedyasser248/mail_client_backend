package com.example.demo.Dao;

import com.example.demo.Entities.Contact;

public interface ContactsDao {
    public Boolean addContact(Contact contact);
    public Boolean deleteContact(Contact contact);
    public Boolean updateContact(Contact contact);
}
