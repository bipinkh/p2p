package com.endtoendmessenging.security.communication.message;

import java.util.Random;

@MessageType(type = 11,version = 1)
public class PingMessage extends KeyExchangeMessage {
    static public PingMessage newInstance(){
        PingMessage msg=new PingMessage();
        int k = new Random().nextInt(20) + 15;
        new Random().nextBytes(msg.nounce);
        return msg;
    }
}
