package com.endtoendmessenging.security.communication.message;

public abstract class RawMessage extends Message{
    public byte[] messageData;

    @Override
    public byte[] toBytes() {
        return messageData;
    }

    @Override
    public void readBytes(int version, byte[] input) {
        if(version==1){
            this.messageData=input;
        }
        else{
            throw new RuntimeException("No idea what to do in this case");
        }
    }
}
