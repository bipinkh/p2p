package com.endtoendmessenging.security.communication.message;

import com.endtoendmessenging.security.communication.MessageSerializationProtocol;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.soriole.kademlia.core.store.Key;

@MessageType(type=3,version=1)
public class ForwardMessage extends Message {
    public byte[] messageByte;
    public Key receiverKey;
    @Override
    public void handle(int version) {

    }

    @Override
    public byte[] toBytes() {
        return messageByte;
    }

    @Override
    public void readBytes(int version, byte[] input) {
        MessageSerializationProtocol.ForwardMessage.Builder builder = MessageSerializationProtocol.ForwardMessage.newBuilder();
        if(receiverKey!=null){
            builder.setReceiverNode(ByteString.copyFrom(receiverKey.toBytes()));
        }
        builder.setMessageData(ByteString.copyFrom(messageByte));
        if(version==1){
            this.messageByte=input;
        }

    }
    public void setData(byte[] data) throws InvalidProtocolBufferException {
        MessageSerializationProtocol.ForwardMessage forwardMessage = MessageSerializationProtocol.ForwardMessage.parseFrom(data);
        if(forwardMessage.hasReceiverNode()){
            this.receiverKey=new Key(forwardMessage.getReceiverNode().toByteArray());
        }
        this.messageByte=forwardMessage.getMessageData().toByteArray();
    }
}
