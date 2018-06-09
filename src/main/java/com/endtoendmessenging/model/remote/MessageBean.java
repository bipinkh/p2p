package com.endtoendmessenging.model.remote;

import com.endtoendmessenging.model.Message;
import com.soriole.kademlia.core.store.NodeInfo;
import com.soriole.kademlia.model.remote.NodeInfoBean;

import java.util.Collection;
import java.util.Date;

public final class MessageBean {
  private Message message;
  public MessageBean(){}
  public MessageBean(Message message){
    this.message=message;
  }
  public String getText(){
    return new String(message.getMessage());
  }
  public Date getReceivedDate(){
    return message.getReceivedTimestamp();
  }

  public void setText(String text) {
    message.setMessage(text.getBytes());
  }
  public void setReceivedDate(Date date){
    message.setReceicedTimestamp(date);
  }
}
