package com.example.demo.Dao;

import com.example.demo.Entities.User;

public interface UsersDao {
    void insertUser(User user);
    Boolean findUser(User user);

}
