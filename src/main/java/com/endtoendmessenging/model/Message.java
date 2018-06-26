package com.endtoendmessenging.model;

import com.soriole.kademlia.core.store.Key;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * stores incoming messages for the subscribed client
 */
@Entity
public class Message {
    @Id
    @GeneratedValue
    int id;
    @Column
    byte[] message;
    @Column
    byte[] receiver;
    @Column
    Date receivedTimestamp;
    @Column
    boolean synced;
    public Message(){}
    public Message(byte[] receiver,byte[] message){
        this.message=message;
        this.receiver=receiver;
        this.receivedTimestamp=new Date();
    }
    public byte[] getMessage(){
        return message;
    }
    public byte[] getReceiver(){
        return receiver;
    }
    public Date getReceivedTimestamp(){
        return this.receivedTimestamp;
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

    public void setReceivedTimestamp(Date receicedTimestamp) {
        this.receivedTimestamp = receicedTimestamp;
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
