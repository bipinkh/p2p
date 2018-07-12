package com.endtoendmessenging.security.communication.message;

import java.util.Random;

@MessageType(type=2,version=1)
public class KeyExchangeMessage extends Message{

    public byte[] nounce;

    @Override
    public void handle(int version){

    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public void readBytes(int version,byte[] input) {

    }
    static public KeyExchangeMessage newInstance(){
        KeyExchangeMessage msg=new KeyExchangeMessage();
        int k = new Random().nextInt(20) + 15;
        new Random().nextBytes(msg.nounce);
        return msg;
    }
}
