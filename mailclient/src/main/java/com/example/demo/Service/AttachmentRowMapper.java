package com.example.demo.Service;

import com.example.demo.Entities.Attachment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttachmentRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.setFileName(resultSet.getString("file_name"));
        attachment.setData(resultSet.getBytes("content"));
        return attachment;
    }
}
