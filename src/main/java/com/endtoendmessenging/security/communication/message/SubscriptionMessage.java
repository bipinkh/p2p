package com.endtoendmessenging.security.communication.message;

import com.soriole.kademlia.core.store.Key;

@MessageType(version = 1,type = 10)
public class SubscriptionMessage extends Message {
    public Key nodeToSubscribe;
    @Override
    public void handle(int version) {

    }

    @Override
    public byte[] toBytes() {
        return nodeToSubscribe.toBytes();
    }

    @Override
    public void readBytes(int version, byte[] input) {
        if(version==1) {
            nodeToSubscribe = new Key(input);
        }
        else{
            throw new RuntimeException("We don't support newer version please update the software");
        }
    }
}
