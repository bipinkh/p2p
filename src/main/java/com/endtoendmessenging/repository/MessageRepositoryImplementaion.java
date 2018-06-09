package com.endtoendmessenging.repository;

import com.endtoendmessenging.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;


@Repository
public class MessageRepositoryImplementaion implements MessageRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public MessageRepositoryImplementaion(){}
    public MessageRepositoryImplementaion(DataSource source) {
        jdbcTemplate = new JdbcTemplate(source);

    }

    @PostConstruct
    public void init(){
        String sql="create table if not exists message_store(" +
                "id integer auto_increment primary key ," +
                "message blob," +
                "receiver blob," +
                "received_timestamp datetime)";
        jdbcTemplate.execute(sql);
    }

    @Override
    public int save(Message message) {
        String sql = "insert into message_store (message,receiver,received_timestamp)"
                + " VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, message.getMessage(), message.getReceiver(),
                message.getReceivedTimestamp());
    }

    @Override
    public int delete(int id) {
        String sql="delete from message_store where id=?";
        return jdbcTemplate.update(sql);
    }

    @Override
    public Message get(int id) {

        String sql="select * from message_store where id=?";
        return jdbcTemplate.queryForObject(sql,defaultMapper);
    }

    @Override
    public List<Message> getMessages(int count, byte[] receiver) {
        String sql="select * from message_store where receiver=?";
        return jdbcTemplate.query(sql,new Object[]{receiver},defaultMapper);
    }

    static RowMapper<Message> defaultMapper= (rs, rowNum) -> {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setMessage(rs.getBytes("message"));
        message.setReceiver(rs.getBytes("receiver"));
        message.setReceicedTimestamp(rs.getDate("received_timestamp"));
        return message;
    };
    public List<Message> getAllMessages(){
        String sql="select * from message_store";
        return jdbcTemplate.query(sql,defaultMapper);
    }

}
