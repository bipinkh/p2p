package com.endtoendmessenging.security.communication.message;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.Serializable;

public abstract class Message implements Serializable {
    // for the spring test only
    public byte[] sender;
    public byte[] receiver;
    // well each messages neds to be handled when received.
    abstract public void handle(int version);

    // other information rather than sender/receiver needs to be converted to byte.
    abstract public byte[] toBytes();

    abstract public void readBytes(int version,byte[] input) throws InvalidProtocolBufferException;

}
