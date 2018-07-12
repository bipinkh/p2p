package com.endtoendmessenging.security.communication.message;

import java.util.Random;

@MessageType(type=4,version =1)
public class AcknowledgeMessage extends Message {
    boolean acknowledge=true;

    public AcknowledgeMessage(){}
    public AcknowledgeMessage(boolean ack){
        acknowledge=ack;
    }
    public void handle(int version) {

    }

    @Override
    public byte[] toBytes() {
        // just to make sure that nobody gets hint that it's a acknowledge message from it's smaller size.
        byte[] data=new byte[32];
        new Random().nextBytes(data);
        data[0]=1;
        return data;

    }

    @Override
    public void readBytes(int version, byte[] input) {
        if(version==1) {
            if (input[0] == 1) {
                acknowledge = true;
            } else {
                acknowledge = false;
            }
        }
        else{
            throw new RuntimeException("Please update the software");
        }
    }
    public boolean isSuccess(){
        return acknowledge;
    }
}
