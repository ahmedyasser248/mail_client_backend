package com.example.demo.Service;

import com.example.demo.Entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper{

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        User obj = new User();
        obj.setEmail(resultSet.getString("user_email"));
        obj.setUserName(resultSet.getString("username"));
        obj.setPassword(resultSet.getString("password"));
        System.out.println("From row mapper"+obj.getEmail());

        return obj;
    }
}
