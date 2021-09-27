package com.example.demo.Service;

import com.example.demo.Dao.UsersDao;
import com.example.demo.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    UsersDao usersDao;
    public void insertUser(User user){
        usersDao.insertUser(user);
    }
    public boolean checkUser(User user){
        return usersDao.findUser(user);
    }
}
