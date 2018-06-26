package com.endtoendmessenging.model.remote;

import com.endtoendmessenging.model.Message;

import java.util.Date;

public final class MessageBean {
    private Message message;

    public MessageBean() {
    }

    public MessageBean(Message message) {
        this.message = message;
    }

    public String getText() {
        return new String(message.getMessage());
    }

    public Date getReceivedDate() {
        return message.getReceivedTimestamp();
    }

    public void setText(String text) {
        message.setMessage(text.getBytes());
    }

    public void setReceivedDate(Date date) {
        message.setReceivedTimestamp(date);
    }

    public int getId() {
        return message.getId();
    }

    public void setId(int id) {
        message.setId(id);
    }
}
