package com.messengerclient;

import com.endtoendmessenging.security.communication.message.KeyExchangeMessage;
import com.endtoendmessenging.security.communication.message.Message;
import com.soriole.kademlia.core.KademliaExtendedDHT;

public class IncomingMessageProcessor {
    Rpc rpc;
    IncomingMessageProcessor(Rpc rpc){
        this.rpc=rpc;
    }
    public void process(Message message){
        if(message instanceof KeyExchangeMessage){
            // we don't receive KeyExchangeMessage by any means.
        }
    }
}
