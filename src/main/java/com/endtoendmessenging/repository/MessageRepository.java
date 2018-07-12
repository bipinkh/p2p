package com.endtoendmessenging.repository;

import com.endtoendmessenging.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MessageRepository extends CrudRepository<Message,byte[]> {
//        int save(Message message);
//        int delete(int id);
//        Message get(int id);
//        List<Message> getMessages(int count, byte[] receiver);
        List<Message> findByReceiver(byte[] receiver);
}
