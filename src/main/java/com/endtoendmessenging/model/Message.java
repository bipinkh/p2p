package com.endtoendmessenging.model;

import com.endtoendmessenging.EndToEndMessengingNodeApplication;
import com.soriole.kademlia.core.messages.listeners.ListenerType;
import com.soriole.kademlia.core.store.Key;

import javax.validation.constraints.Max;
import java.util.Date;

/**
 * stores incoming messages for the subscribed client
 */
public class Message {
    int id;
    byte[] message;
    byte[] receiver;
    Date receicedTimestamp;
    boolean synced;
    public Message(){}
    public Message(byte[] receiver,byte[] message){
        this.message=message;
        this.receiver=receiver;
        this.receicedTimestamp=new Date();
    }
    public byte[] getMessage(){
        return message;
    }
    public byte[] getReceiver(){
        return receiver;
    }
    public Date getReceivedTimestamp(){
        return this.receicedTimestamp;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId(){return this.id;}

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public void setReceiver(byte[] receiver) {
        this.receiver = receiver;
    }

    public void setReceicedTimestamp(Date receicedTimestamp) {
        this.receicedTimestamp = receicedTimestamp;
    }
    public String toString(){
        return new Key(receiver).toString()+" -> "+new String(message);
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
