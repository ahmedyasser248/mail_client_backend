package com.example.demo.Service;

import com.example.demo.Entities.MessagesAttachments;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonAttachmentRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        MessagesAttachments messagesAttachments = new MessagesAttachments();
        messagesAttachments.setAttachmentID(resultSet.getInt("idattachment"));
        messagesAttachments.setMessageID(resultSet.getInt("idmessages"));
        messagesAttachments.setFileName(resultSet.getString("messages_attachmentscol"));
        return messagesAttachments;
    }
}
