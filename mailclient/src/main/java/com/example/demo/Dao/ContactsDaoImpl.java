package com.example.demo.Dao;

import com.example.demo.Entities.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
@Service
public class ContactsDaoImpl extends JdbcDaoSupport implements ContactsDao{
    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Boolean addContact(Contact contact) {
        String sql= "INSERT INTO contacts Values(?,?,?) ";
        try{
            getJdbcTemplate().update(sql,new Object[]{contact.getContactEmail(),contact.getUserEmail(),contact.getUserName()});
        }catch (Exception e){
            System.out.println(e);
            System.out.println("error in add Contacts");
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteContact(Contact contact) {
        String sql ="DELETE FROM contacts WHERE user_email " +
                "= \""+contact.getUserEmail()+"\" AND username = \""+contact.getUserName()+"\"";
        try{
            getJdbcTemplate().update(sql);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("error in the delete contact");
        }
        return true;
    }

    @Override
    public Boolean updateContact(Contact contact) {
        return null;
    }
}
