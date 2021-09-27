package com.example.demo.Dao;

import com.example.demo.Entities.User;
import com.example.demo.Service.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
@Service
public class UsersDaoImpl extends JdbcDaoSupport implements UsersDao {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void insertUser(User user) {
        String sql = "INSERT INTO users " + "Values (?,?,?)";
        getJdbcTemplate().update(sql, new Object[]{
                user.getEmail(), user.getUserName(), user.getPassword()
        });
    }

    @Override
    public Boolean findUser(User user) {
        String sql = "SELECT * FROM users WHERE username = "+"\""+ user.getUserName()+ "\""+" AND "+" password = "+"\""+user.getPassword()+"\"" ;
        System.out.println(sql);
        try {
            var count = getJdbcTemplate().queryForObject(sql, new UserRowMapper());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

