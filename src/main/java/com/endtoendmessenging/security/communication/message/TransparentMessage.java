package com.endtoendmessenging.security.communication.message;

import com.endtoendmessenging.security.communication.MessageSerializationProtocol;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class TransparentMessage extends Message{
    public byte[] signature;
    public byte[] encryptedData;
    public long index;
    @Override
    public void handle(int version) {

    }

    @Override
    public byte[] toBytes() {
        MessageSerializationProtocol.TransparentLayer.Builder builder = MessageSerializationProtocol.TransparentLayer.newBuilder();
        builder.setEncryptedData(ByteString.copyFrom(encryptedData));
        if(signature!=null){
            builder.setEncryptedData(ByteString.copyFrom(encryptedData));
        }
        builder.setSender(ByteString.copyFrom(sender));
        builder.setIndex((int)index);
        return builder.build().toByteArray();
    }

    // we don't need the version information.
    @Override
    public void readBytes(int version, byte[] input) throws InvalidProtocolBufferException {
            MessageSerializationProtocol.TransparentLayer transparentLayer = MessageSerializationProtocol.TransparentLayer.parseFrom(input);
            // if the message is public key encrypted
            // this is the case only for keyExchange message.

            sender = transparentLayer.getSender().toByteArray();
            encryptedData = transparentLayer.getEncryptedData().toByteArray();
            if (transparentLayer.hasAuthenticationCode()) {
                signature = transparentLayer.getAuthenticationCode().toByteArray();
            }
            if (transparentLayer.hasIndex()) {
                index = transparentLayer.getIndex();
            }
    }
    public static TransparentMessage fromBytes(byte[] message) throws InvalidProtocolBufferException {
        TransparentMessage msg=new TransparentMessage();
        msg.readBytes(0,message);
        return msg;
    }

}
