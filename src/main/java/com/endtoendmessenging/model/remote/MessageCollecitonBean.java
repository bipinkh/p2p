package com.endtoendmessenging.model.remote;

import com.endtoendmessenging.model.Message;

import java.util.ArrayList;
import java.util.Collection;

public class MessageCollecitonBean {
    MessageBean[] messages;


    public MessageBean[] getMessages() {
        return messages;
    }

    public void setMessages(MessageBean[] messages) {
        this.messages = messages;
    }
    public static MessageCollecitonBean fromMessageCollection(Collection<Message> messages){
        MessageCollecitonBean bean = new MessageCollecitonBean();
        bean.messages=new MessageBean[messages.size()];
        int i=0;
        for(Message m:messages){
            bean.messages[i++]=new MessageBean(m);
        }
        return bean;
    }

    public static MessageCollecitonBean fromMessageArray(Message[] messages){
        MessageCollecitonBean bean = new MessageCollecitonBean();
        bean.messages=new MessageBean[messages.length];
        for(int i=0;i<messages.length;i++){
            bean.messages[i]=new MessageBean(messages[i]);
        }
        return bean;
    }
}
