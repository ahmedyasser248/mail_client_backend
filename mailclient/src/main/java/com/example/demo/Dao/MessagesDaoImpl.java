package com.example.demo.Dao;

import com.example.demo.Entities.*;
import com.example.demo.Service.AttachmentRowMapper;
import com.example.demo.Service.CommonAttachmentRowMapper;
import com.example.demo.Service.MessageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class
MessagesDaoImpl extends JdbcDaoSupport implements MessagesDao{

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Boolean sendFromDraft(Message message, MultipartFile[] multipartFiles) {
        List<MessagesAttachments> fileNamesObjs = AttachmentsFromDraft(message.getIdMessages());
        ArrayList<String> finalFileNames = new ArrayList<>();
        ArrayList<MessagesAttachments> fileNamesToBeDeleted = new ArrayList<>();
        ArrayList<MessagesAttachments> finalFileNamesObjs =new ArrayList<>();
        String fileNamesStr = "";
        for (int i = 0; i < fileNamesObjs.size(); i++) {
            if (message.getFileNames().contains(fileNamesObjs.get(i).getFileName())) {
                finalFileNames.add(fileNamesObjs.get(i).getFileName());
                finalFileNamesObjs.add(fileNamesObjs.get(i));
                fileNamesStr=fileNamesStr+fileNamesObjs.get(i).getFileName()+" ";
            }else{
                fileNamesToBeDeleted.add(fileNamesObjs.get(i));
            }
            if(fileNamesToBeDeleted.size()!=0){
                deleteBatchAttachmentsMessages(fileNamesToBeDeleted);
            }
        }
        int firstIdAttachments=0;
        int lastIdAttachments=0;
        String sqlSetAttachments = "INSERT INTO attachments(content,file_name) Values(?,?) ";
        try {
            firstIdAttachments=getLastIdAttachments();
            firstIdAttachments++;
            if (multipartFiles != null) {
                for (MultipartFile attachment : multipartFiles) {
                    fileNamesStr=fileNamesStr+attachment.getOriginalFilename()+" ";
                    int insertCount = getJdbcTemplate().update(sqlSetAttachments, attachment.getBytes(), attachment.getOriginalFilename());
                }
                lastIdAttachments=firstIdAttachments-1+multipartFiles.length;
            }
            String sqlUpdate = "UPDATE messages SET source = \"sent\", file_names = \"" +fileNamesStr+"\" WHERE idmessages = "+message.getIdMessages();
            getJdbcTemplate().update(sqlUpdate);
            message.setSource("inbox");
            String sqlReceivers="INSERT INTO messages(sender_email,receiver,subject,date,source,file_names) Values(?,?,?,?,?,?)";
            int firstMessageID=getLastIdmessages();
            firstMessageID++;

            for (int i = 0 ; i<message.getReceiver().size();i++){
                getJdbcTemplate().update(sqlReceivers,new Object[]{message.getSender(),
                        message.getReceiver().get(i),message.getSubject(),message.getDate(),message.getSource(),fileNamesStr});
            }
            int lasMessageID=getLastIdmessages();
        if(multipartFiles!=null){
            updateBatchForReceiversFromDraft(finalFileNamesObjs,message.getIdMessages(),firstMessageID
                    ,lasMessageID,multipartFiles,firstIdAttachments,lastIdAttachments);
        }
        }catch (Exception e){
            System.out.println("error in send from Draft due to adding files");
            return false;
        }
        return  true;
    }
    @Override
    public Boolean sendMessage(Message message, MultipartFile [] multipartFiles) {
        message.setSource("sent");
        String sqlInsert ="INSERT INTO messages (sender_email,receiver,subject,date,source,file_names,user_email) Values(?,?,?,?,?,?,?)";
        String sqlSetAttachments ="INSERT INTO attachments(content,file_name) Values(?,?) ";
        String fileNames="";
        int firstIdAttachment=0;

        int lastIdAttachment=0;
        try{
            firstIdAttachment=getLastIdAttachments();
            firstIdAttachment++;
            if(multipartFiles!=null) {
                for (MultipartFile attachment : multipartFiles) {
                    fileNames=fileNames+attachment.getOriginalFilename()+" ";
                 int insertCount = getJdbcTemplate().update(sqlSetAttachments, attachment.getBytes(), attachment.getOriginalFilename());
                }
                lastIdAttachment=firstIdAttachment-1+multipartFiles.length;
            }
            getJdbcTemplate().update(sqlInsert,new Object[]{message.getSender(),message.receiverString(),message.getSubject()
                    ,message.getDate(),message.getSource(),fileNames,message.getSender()});
            int firstIdMessages=getLastIdmessages();
            firstIdMessages++;
            message.setSource("inbox");
            for(int i =0 ; i< message.getReceiver().size();i++){
                getJdbcTemplate().update(sqlInsert,new Object[]{message.getSender(),message.getReceiver().get(i),
                        message.getSubject(),message.getDate(),message.getSource(),fileNames,message.getReceiver().get(i)});
            }
            int lastIdMessages=getLastIdmessages();
            if(multipartFiles!=null){
                updateBatchForSendAndDraft(firstIdAttachment,lastIdAttachment,firstIdMessages,lastIdMessages,multipartFiles);
            }
        }catch (Exception e){
            System.out.println(e);
            System.out.println("error here");
            return false;
        }
        return true;
    }
    public void updateBatchForSendAndDraft(int firstAttachment ,int lastAttachment , int firstMessage , int lastMessage,MultipartFile[] multipartFiles){
        String sql ="INSERT INTO messages_attachments Values(?,?,?)";
        List<MessagesAttachments> messagesAndAttachments = new ArrayList<>();
        int counter=0;
        for(int i=firstMessage;i<=lastMessage;i++){
            for(int y=firstAttachment;y<=lastAttachment;y++){
                messagesAndAttachments.add(new MessagesAttachments(i,y,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
                counter++;
            }
        }
        getJdbcTemplate().batchUpdate(sql, messagesAndAttachments, 1, new ParameterizedPreparedStatementSetter<MessagesAttachments>() {
            @Override
            public void setValues(PreparedStatement preparedStatement, MessagesAttachments messagesAttachments) throws SQLException {
                preparedStatement.setInt(1,messagesAttachments.getMessageID());
                preparedStatement.setInt(2,messagesAttachments.getAttachmentID());
                preparedStatement.setString(3,messagesAttachments.getFileName());
            }
        });

        System.out.println("Records Updated");
    }
    public void updateBatchForReceiversFromDraftModified(ArrayList<MessagesAttachments> messagesAttachments){
        String sql ="INSERT INTO messages_attachments Values(?,?,?)";
        getJdbcTemplate().batchUpdate(sql, messagesAttachments, 1, new ParameterizedPreparedStatementSetter<MessagesAttachments>() {
            @Override
            public void setValues(PreparedStatement preparedStatement, MessagesAttachments messagesAttachments) throws SQLException {
                preparedStatement.setInt(1,messagesAttachments.getMessageID());
                preparedStatement.setInt(2,messagesAttachments.getAttachmentID());
                preparedStatement.setString(3,messagesAttachments.getFileName());
            }
        });
    }
    public void updateBatchForReceiversFromDraft(ArrayList<MessagesAttachments> filesFromDraft,int messageId,int firstMessageId,int lastMessageId,MultipartFile[]multipartFiles,int firstIdAttachment,int lastIdAttachment){
        String sql ="INSERT INTO messages_attachments Values(?,?,?)";
        int counter = 0;
        ArrayList<MessagesAttachments>messagesAttachments = new ArrayList<>();
       /*for (int z  = firstIdAttachment ; z<=lastIdAttachment;z++){
            messagesAttachments.add(new MessagesAttachments(messageId,z,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
            counter++;
        }
       for (int i = firstMessageId;i<=lastMessageId ;i++){
           for (int y = 0; y<filesFromDraft.size() ;y++){
               messagesAttachments.add(new MessagesAttachments(i,filesFromDraft.get(y).getAttachmentID(),filesFromDraft.get(y).getFileName()));
           }
       }
       counter=0;
        for (int i = firstMessageId ;i<=lastMessageId;i++){
                for (int z= firstIdAttachment;z<=lastIdAttachment;z++){
                    messagesAttachments.add(new MessagesAttachments(i,z,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
                    counter++;
                }
        }*/
        addNewAttachmentsToOldMessage(messageId,firstIdAttachment,lastIdAttachment,multipartFiles,messagesAttachments);
        addRelationBetweenReceiversAndOldAttachment(firstMessageId,lastMessageId,messagesAttachments,filesFromDraft);
        addRelationBetweenReceiverAndNewAttachment(firstMessageId,lastMessageId,firstIdAttachment,lastIdAttachment,messagesAttachments,multipartFiles);

        getJdbcTemplate().batchUpdate(sql, messagesAttachments, 1, new ParameterizedPreparedStatementSetter<MessagesAttachments>() {
            @Override
            public void setValues(PreparedStatement preparedStatement, MessagesAttachments messagesAttachments) throws SQLException {
                preparedStatement.setInt(1,messagesAttachments.getMessageID());
                preparedStatement.setInt(2,messagesAttachments.getAttachmentID());
                preparedStatement.setString(3,messagesAttachments.getFileName());
            }
        });
    }
    public void addNewAttachmentsToOldMessage(int messageId,int firstIdAttachment ,int lastIdAttachment ,MultipartFile [] multipartFiles ,ArrayList<MessagesAttachments>messagesAttachments){
        int counter= 0 ;
        for (int z  = firstIdAttachment ; z<=lastIdAttachment;z++){
            messagesAttachments.add(new MessagesAttachments(messageId,z,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
            counter++;
        }
    }
    public ArrayList<MessagesAttachments> addNewAttachmentsToOldMessageModified(RangeHolder rangeHolder,MultipartFile[] multipartFiles){
        ArrayList<MessagesAttachments> messagesAttachments = new ArrayList<>();
        int counter= 0 ;
        for (int z  = rangeHolder.getFirstAttachmentID() ; z<= rangeHolder.getLastAttachmentID();z++){
            messagesAttachments.add(new MessagesAttachments(rangeHolder.getUsedMessageID(),z,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
            counter++;
        }
        return  messagesAttachments;
    }
    public void addRelationBetweenReceiversAndOldAttachment(int firstMessageId, int lastMessageId ,ArrayList<MessagesAttachments> messagesAttachments,ArrayList<MessagesAttachments>filesFromDraft ){
        for (int i = firstMessageId;i<=lastMessageId ;i++){
            for (int y = 0; y<filesFromDraft.size() ;y++){
                messagesAttachments.add(new MessagesAttachments(i,filesFromDraft.get(y).getAttachmentID(),filesFromDraft.get(y).getFileName()));
            }
        }
    }
    public ArrayList<MessagesAttachments> addRelationBetweenReceiversAndOldAttachmentModified(RangeHolder rangeHolder,ArrayList<MessagesAttachments>filesFromDraft ){
        ArrayList<MessagesAttachments> messagesAttachments = new ArrayList<>();
        for (int i = rangeHolder.getFirstMessageID();i<=rangeHolder.getLastMessageID() ;i++){
            for (int y = 0; y<filesFromDraft.size() ;y++){
                messagesAttachments.add(new MessagesAttachments(i,filesFromDraft.get(y).getAttachmentID(),filesFromDraft.get(y).getFileName()));
            }
        }
        return messagesAttachments;
    }
    public void addRelationBetweenReceiverAndNewAttachment(int firstMessageId ,int lastMessageId,int firstIdAttachment,int lastIdAttachment,ArrayList<MessagesAttachments> messagesAttachments,MultipartFile[] multipartFiles ){
        int counter=0;
        for (int i = firstMessageId ;i<=lastMessageId;i++){
            for (int z= firstIdAttachment;z<=lastIdAttachment;z++){
                messagesAttachments.add(new MessagesAttachments(i,z,multipartFiles[counter% multipartFiles.length].getOriginalFilename()));
                counter++;
            }
        }



    }
    public void deleteBatchAttachmentsMessages(ArrayList<MessagesAttachments> messagesAttachments){
        String sql = "DELETE FROM messages_attachments WHERE idmessages = ? AND idattachment = ? AND messages_attachmentscol = ?";
        getJdbcTemplate().batchUpdate(sql,messagesAttachments,1,new ParameterizedPreparedStatementSetter<MessagesAttachments>(){
            @Override
            public void setValues(PreparedStatement preparedStatement, MessagesAttachments messagesAttachments) throws SQLException {
                preparedStatement.setInt(1,messagesAttachments.getMessageID());
                preparedStatement.setInt(2,messagesAttachments.getAttachmentID());
                preparedStatement.setString(3,messagesAttachments.getFileName());
            }
        });
    }
    public int getLastIdAttachments(){
        String sql = "SELECT COUNT(*) FROM attachments";
        int count=getJdbcTemplate().queryForObject(sql,Integer.class);
        return  count;
    }

    public int getLastIdmessages(){
        String sql = "SELECT COUNT(*) FROM messages ";
        int count =getJdbcTemplate().queryForObject(sql,Integer.class);
        return count;

    }


    @Override
    public Boolean saveToDraft(Message message,MultipartFile [] multipartFiles) {
        message.setSource("draft");
        String sql ="INSERT INTO messages (sender_email,receiver,subject,date,source,file_names,user_email) Values(?,?,?,?,?,?,?)";
        String sqlSetAttachments ="INSERT INTO attachments(content,file_name) Values(?,?) ";
        int firstIdAttachment=getLastIdAttachments();
        firstIdAttachment++;
        String fileNames="";
        int lastIdAttachment=0;
       try{
        if(multipartFiles!=null) {
            for (MultipartFile attachment : multipartFiles) {
                fileNames=fileNames+attachment.getOriginalFilename()+" ";
                int insertCount = getJdbcTemplate().update(sqlSetAttachments, attachment.getBytes(), attachment.getOriginalFilename());
            }
           lastIdAttachment=firstIdAttachment-1+multipartFiles.length;
        }
            int firstMessageId=getLastIdmessages();
                firstMessageId++;

            getJdbcTemplate().update(sql, new Object[]{message.getSender(), message.receiverString(),message.getSubject(), message.getDate(), message.getSource(),fileNames,message.getSender()});
            int lastMessageId=getLastIdmessages();
            if(multipartFiles!=null) {
                updateBatchForSendAndDraft(firstIdAttachment, lastIdAttachment, firstMessageId, lastMessageId, multipartFiles);
            }
       }catch (Exception e){
           System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteMessage(int messageID) {
        String sql ="UPDATE messages SET source = \"trash\" WHERE idmessages = "+messageID;
       try {
          getJdbcTemplate().update(sql,new MessageRowMapper());
       }
       catch (Exception e){
           return false;
       }
       return true;
    }

    @Override
    public List<Message> readMessagesFromInbox(User user,int page,String sortType) {
        if(sortType==null){
            sortType="date";
        }
        String  order ="DESC";
        if(sortType.equals("subject")||sortType.equals("sender_email")||sortType.equals("receiver"))
            order="ASC";
        int offset=page * 10 - 10 ;
        int show =10;
        String sql = "SELECT * FROM messages WHERE user_email = "+"\""+user.getEmail()+"\""+" AND "
                +"source  = "+"\""+"inbox"+"\" ORDER BY " +sortType+" "+order+ " LIMIT "+offset+" , "+show;

        List<Message> messages=null;
       try {
           messages = getJdbcTemplate().query(sql, new MessageRowMapper());
       }catch (Exception e){
           System.out.println("got here");
           return null;
       }
        System.out.println(messages.size());
       return messages;
    }
    @Override
    public List<Message> readMessagesFromDraft(User user,int page,String sortType){
        if(sortType==null){
            sortType ="date";
        }
        String  order ="DESC";
        if(sortType.equals("subject")||sortType.equals("sender_email")||sortType.equals("receiver"))
            order="ASC";
        int offset=page * 10 - 10 ;
        int show =10;
        String sql = "SELECT * FROM messages WHERE user_email = "+"\""+user.getEmail()+"\""+" AND "
                +"source  = "+"\""+"draft"+"\" ORDER BY " + sortType+" "+order+"  LIMIT "+offset+" , "+show;
        List<Message> messages = null;
        try{
            messages = getJdbcTemplate().query(sql,new MessageRowMapper());
        }catch (Exception e){
            System.out.println("in read draft");
            return null;
        }
        System.out.println(messages.size());
        return messages;
    }

    @Override
    public List<Message> readMessagesFromSent(User user,int page,String sortType) {
        if(sortType==null){
            sortType="date";
        }
        int offset=page * 10 - 10 ;
        int show =10;
        String  order ="DESC";
        if(sortType.equals("subject")||sortType.equals("sender_email")||sortType.equals("receiver"))
            order="ASC";
        String sql = "SELECT * FROM messages WHERE user_email = "+"\""+user.getEmail()+"\""+" AND "
                +"source  = "+"\""+"sent"+"\" ORDER BY "+sortType+ " "+order+" LIMIT "+offset+" , "+show;

        List<Message> messages = null;
        try{
            messages = getJdbcTemplate().query(sql , new MessageRowMapper());
        }catch (Exception e){
            System.out.println(e);
            System.out.println("there is an error happens here in readMeassagesFromSent method");
            return null;
        }
        return messages;
    }
    public List<MessagesAttachments>AttachmentsFromDraft(int messageID){
        String sql = "SELECT * FROM messages_attachments WHERE idmessages = "+messageID;
        List<MessagesAttachments>messagesAttachmentsObj=null;
       try{
            messagesAttachmentsObj= getJdbcTemplate().query(sql, new CommonAttachmentRowMapper());
       }catch (Exception e){
           System.out.println("error while reading file names in attachmentsFrom draft method ");
       }
        return messagesAttachmentsObj;
    }

    @Override
    public Boolean downloadAttachment(int messageId, String fileName) {
        String sql = "SELECT * FROM messages_attachments WHERE idmessages = "+messageId +" AND messages_attachmentscol =\""+ fileName+"\" LIMIT 1";
        try{
            MessagesAttachments test  =(MessagesAttachments) getJdbcTemplate().queryForObject(sql,new CommonAttachmentRowMapper());
            String sql2 ="SELECT * FROM attachments Where idattachments = "+test.getAttachmentID() ;
            Attachment attachment =(Attachment) getJdbcTemplate().queryForObject(sql2,new AttachmentRowMapper());
            String separator = System.getProperty("file.separator");
            String home =System.getProperty("user.home");
            File file = new File(home+separator+"Downloads"+separator+attachment.getFileName());
            OutputStream os = new FileOutputStream(file);
            os.write(attachment.getData());
        }catch (Exception e){
            System.out.println(e);
            System.out.println("Error while download attachments");

            return false;
        }
        return true;
    }
}
