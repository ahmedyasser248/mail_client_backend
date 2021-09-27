package com.example.demo.Service;

import com.example.demo.Entities.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message= new Message();
        System.out.println("i got here");
        ArrayList<String> receivers= new ArrayList<>();
        message.setIdMessages(resultSet.getInt("idmessages"));
        message.setSource(resultSet.getString("source"));
        message.setSender(resultSet.getString("sender_email"));
        message.setSubject(resultSet.getString("subject"));
        message.setDate(resultSet.getString("date"));
        String tempReceivers = resultSet.getString("receiver");
        String tempFileNames = resultSet.getString("file_names");
        if(tempReceivers!=null) {
            String[] array = tempReceivers.split("\\s+");
            for (int z = 0; z < array.length; z++) {
                message.addReceiver(array[z]);
            }
        }if(tempFileNames!=null){
            String[]array=tempFileNames.split("\\s+");
            for (int z = 0 ; z<array.length;z++){
                message.addFileName(array[z]);
            }
        }
        return message;
    }
}
