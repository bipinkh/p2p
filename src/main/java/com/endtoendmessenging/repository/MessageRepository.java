package com.endtoendmessenging.repository;

import com.endtoendmessenging.model.Message;

import java.util.List;

public interface MessageRepository {
        int save(Message message);
        int delete(int id);
        Message get(int id);
        List<Message> getMessages(int count, byte[] receiver);

}
