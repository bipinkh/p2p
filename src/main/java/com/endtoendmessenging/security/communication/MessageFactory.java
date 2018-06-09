package com.endtoendmessenging.security.communication;

import com.endtoendmessenging.security.communication.message.*;
import com.endtoendmessenging.security.crypto.AESInputStream;
import com.endtoendmessenging.security.crypto.AESOutputStream;
import com.endtoendmessenging.security.crypto.MAC;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.endtoendmessenging.security.crypto.KeyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessageFactory does the task of
 * <ul>
 * <li>Converting bytes to Message class instance and vice versa.</li>
 * <li>Encrypting and decrypting the bytes properly using session keys.</li>
 * <li>Adding signature to the message while sending </li>
 * <li>Verifying receiver when message is received</li>
 * </ul>
 *
 * @author github.com/mesudip
 */
public class MessageFactory {
    static public class AuthenticationException extends Exception {
    }

    private static Logger logger = LoggerFactory.getLogger(MessageFactory.class);
    static Map<Integer, Class<? extends Message>> messageTypes = buildMessageTypes();


    KeyFactory factory;

    public void addSession(byte[] newKey){
        factory.getSecret(newKey);
    }
    public MessageFactory(KeyFactory factory) {
        this.factory = factory;
    }

    public Message deSerilizeAndDecrypt(byte[] _data) throws InvalidProtocolBufferException, AuthenticationException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        return  decryptMessage(TransparentMessage.fromBytes(_data));
    }
    static public TransparentMessage decerialize(byte[] message) throws InvalidProtocolBufferException {
        return TransparentMessage.fromBytes(message);
    }

    public Message decryptMessage(TransparentMessage transparentMessage) throws AuthenticationException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidProtocolBufferException {

        if (!factory.hasSecret(transparentMessage.sender)) {
            throw new AuthenticationException();
        }
        byte[] secret = factory.getSecret(transparentMessage.sender, (int) transparentMessage.index);

        MAC mac = new MAC(secret);
        if (!new MAC(secret)
                .addData(transparentMessage.encryptedData)
                .verify(transparentMessage.signature)) {
            throw new AuthenticationException();
        }

        byte[] message = AESOutputStream.directDecrypt(secret, transparentMessage.encryptedData);
        MessageSerializationProtocol.EncryptedLayer decryptedMessage = MessageSerializationProtocol.EncryptedLayer.parseFrom(message);

        Message newMessage = createMessageInstance(decryptedMessage.getType());

        if (decryptedMessage.hasReceiver()) {
            newMessage.receiver = decryptedMessage.getReceiver().toByteArray();
        }
        try {
            newMessage.readBytes(decryptedMessage.getVersion(), decryptedMessage.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        newMessage.sender=transparentMessage.sender;
        return newMessage;
    }


    public byte[] serializeAndEncrypt(Message message) {
        int index = new Random().nextInt();
        if (index < 0)
            index = -index;
        byte[] secret = factory.getSecret(message.receiver);
        MessageType messageType = message.getClass().getAnnotation(MessageType.class);


        MessageSerializationProtocol.EncryptedLayer.Builder builder1 = MessageSerializationProtocol.EncryptedLayer.newBuilder()
                .setType(messageType.type())
                .setVersion(messageType.version())
                .setMessageByte(ByteString.copyFrom(message.toBytes()));
        if (message.receiver != null) {
            builder1.setReceiver(ByteString.copyFrom(message.receiver));
        }


        byte[] _message = AESInputStream.directEncrypt(secret, builder1.build().toByteArray());
        byte[] mac = new MAC(secret).addData(_message).getMac(message.sender);
        byte[] sender = null;
        if (message.sender == null) {
            message.sender = factory.getMyKey().getPublic();
        }
        MessageSerializationProtocol.TransparentLayer.Builder builder = MessageSerializationProtocol.TransparentLayer.getDefaultInstance().toBuilder();
        builder.setSender(ByteString.copyFrom(message.sender))
                .setAuthenticationCode(ByteString.copyFrom(mac))
                .setEncryptedData(ByteString.copyFrom(_message))
                .setIndex(index);
        return builder.build().toByteArray();
    }




    public byte[] getMySessionId() {
        return this.factory.getMyKey().getPublic();
    }

    // we might require that the message need not be signed.
    // just copies the message data transparently to the outer layer.
    public byte[] serailizeTransparentLayer(Message message) {
        MessageSerializationProtocol.TransparentLayer.Builder builder = MessageSerializationProtocol.TransparentLayer.getDefaultInstance().toBuilder();
        if(message.sender==null){
            builder.setSender(ByteString.copyFrom(factory.getMyKey().getPublic()));
        }
        else{
            builder.setSender(ByteString.copyFrom(message.sender));
        }

        builder.setAuthenticationCode(ByteString.EMPTY)
                .setEncryptedData(ByteString.copyFrom(message.toBytes()))
                .setIndex(0);
        return builder.build().toByteArray();
    }

    private static Map<Integer, Class<? extends Message>> buildMessageTypes() {
        Map<Integer, Class<? extends Message>> typeToClass = new Hashtable<>(100);
        org.reflections.Reflections reflections = new org.reflections.Reflections("com.endtoendmessenging.security.communication.message");

        // all the subclasses fo Message.class
        Set<Class<? extends Message>> allMessageClasses = reflections.getSubTypesOf(Message.class);
        Set<Class<?>> allMessageTypes = reflections.getTypesAnnotatedWith(MessageType.class);

        for (Class _class : allMessageTypes) {
            if (allMessageClasses.contains(_class) && _class.isAnnotationPresent(MessageType.class)) {
                int type = ((MessageType) _class.getAnnotation(MessageType.class)).type();
                typeToClass.put(type, _class);

            }
        }
        LoggerFactory.getLogger("MessageFactory").info("Message Types : " + typeToClass.toString());
        return typeToClass;
    }
    public static Message createMessageInstance(int type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class messageClass = messageTypes.get(type);
        return (Message) messageClass.getConstructor().newInstance(new Object[]{});
    }


}