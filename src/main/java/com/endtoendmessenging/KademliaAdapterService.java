package com.endtoendmessenging;

import com.endtoendmessenging.security.communication.message.*;
import com.endtoendmessenging.security.communication.MessageFactory;
import com.endtoendmessenging.security.crypto.DHKeyPair;
import com.endtoendmessenging.security.crypto.KeyFactory;
import com.endtoendmessenging.model.Subscription;
import com.endtoendmessenging.repository.MessageRepositoryImplementaion;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import com.soriole.kademlia.core.KademliaExtendedDHT;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.core.store.NodeInfo;
import com.soriole.kademlia.core.store.TimeStampedData;
import com.soriole.kademlia.network.ServerShutdownException;
import com.soriole.kademlia.network.receivers.ByteReceiver;
import com.soriole.kademlia.service.KademliaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Messenging service uses the kademlia service to provide end-to-end messenging services.
 * It's a binding class that connects the end to end messenging with the kademlia.
 * Kademlia protocols should not be directly used in any other classes within the endtoendmessenging package.
 */
@Service
public class KademliaAdapterService implements ByteReceiver {
    static private Logger logger = LoggerFactory.getLogger(KademliaAdapterService.class);
    @Value("${local.key.seed}")
    String seed;

    // sessions to key mapping
    HashMap<byte[],Key> sessions;
    // Contains the messageFactories to encrypt/decrypt incoming messages
    HashMap<byte[],MessageFactory> messageFactories;
    HashMap<byte[],MessageFactory> unVerifiedFactories;
    HashMap<byte[],NodeInfo> sessionNodeInfo;
    // key to session mapping
    HashMap<Key,byte[]> localSessions;
    HashMap<byte[], byte[]> foreignSessions;


    public int bucketSize;

    @Autowired
    MessageRepositoryImplementaion messageStore;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    KademliaService kademliaService;


    MessageFactory messageFactory;

    @Autowired
    MessageListenerService listenerService;

    @PostConstruct
    public void init() {
        kademliaService.getDHT().setMessageReceiver(this);
        DHKeyPair keyPair = new DHKeyPair(seed.getBytes());
        KeyFactory factory = new KeyFactory(new DHKeyPair());
        messageFactory = new MessageFactory(factory);

    }

    public boolean addSubscription(byte[] clientId, long subscriptionTimeSeconds) {
        TimeStampedData<byte[]> data = kademliaService.getDHT().getLocal(new Key(clientId));
        // we cannot add the subscription, its already added.
        if (data != null) {
            return false;
        }
        if (kademliaService.getDHT().getLocalNode().getKey().equals(new Key(clientId))) {
            Subscription subscription = new Subscription();
            subscription.setSubscriber(new Key(clientId).toBytes());
            subscription.setSubscriptionStart(new Date());
            // subscription expires in a month period.
            subscription.setSubscriptionExpiry(new Date(new Date().getTime() + subscriptionTimeSeconds * 1000));
            subscriptionRepository.save(subscription);
            return true;
        }
        // we won't forward the subscription message to another node here.
        else return false;

    }

    public boolean updateSubscription(byte[] clientId, long addedSubscriptionTimeSeconds) {
        // TODO: function not implemented
        return false;
    }

    public boolean removeSubscription(byte[] clientId) {
        // TODO: funciton not implemented.
        return false;
    }

    /**
     * @param receiverKey we know the receiver
     * @param message     we need to send a byte message to receiver.
     * @return
     */
    public boolean sendMessageToNode(Key receiverKey,long sessionId, Message message) {
        try {
            Key receiver=sessions.get(receiverKey);

            byte[] messageBytes = messageFactory.serializeAndEncrypt(message);
            kademliaService.getDHT().sendMessageToNode(receiverKey, messageBytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            logger.info("Client not found for sending message : " + receiverKey);
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean sendMessageToNode(Key receiverKey, Message message) {
       return sendMessageToNode(receiverKey,0,message);
    }

    public boolean sendToClient(byte[] recieverID, Message message) {
        Key targetKey = new Key(recieverID);
        ;
        // if we are the subscribed node of the receiver
        if (subscriptionRepository.countBySubscriber(targetKey.toBytes())>0) {
            messageStore.save(new com.endtoendmessenging.model.Message(targetKey.toBytes(), message.toBytes()));
            return true;

        }
        // otherwise we need to find the node subscribed by the client and fordward it to that node.
        try {
            TimeStampedData<byte[]> data=kademliaService.getDHT().get(targetKey);
            if(data==null){
                return false;
            }
            Key nodeKey=new Key(data.getData());

            ClientMessage clientMessage = new ClientMessage();
            clientMessage.messageData=message.toBytes();
            clientMessage.receiver=targetKey.toBytes();
            return this.sendMessageToNode(nodeKey,clientMessage);

        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (com.soriole.kademlia.core.KadProtocol.ContentNotFoundException e) {

        }
        return false;

    }
    public Message query(Key receiverKey,long sessionId,Message message){

        try {
            byte[] messageBytes = messageFactory.serializeAndEncrypt(message);
            byte[] receivedMessage=kademliaService.getDHT().queryWithNode(receiverKey,sessionId, messageBytes);
            Message reply=messageFactory.deSerilizeAndDecrypt(receivedMessage);
            if(reply instanceof KeyExchangeMessage){
                localSessions.put(receiverKey,reply.sender);
                receivedMessage=kademliaService.getDHT().queryWithNode(receiverKey,sessionId, messageBytes);
                reply=messageFactory.deSerilizeAndDecrypt(receivedMessage);
                if(reply instanceof KeyExchangeMessage){
                    logger.warn("The server sent KeyExchangeMessage twice.");
                    return null;
                }
                return reply;
            }

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            e.printStackTrace();
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (MessageFactory.AuthenticationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * // The function to handle new incoming Non-Kademlia messages
     * @param info
     * @param sessionId
     * @param message
     */
    @Override
    public void onNewMessage(NodeInfo info, long sessionId, byte[] message) {
        Key key=info.getKey();
        try {

            // decode the message
            TransparentMessage transparentMessage=MessageFactory.decerialize(message);

            if(messageFactories.containsKey(transparentMessage.sender)){
                // the message is for us
                MessageFactory factory=messageFactories.get(transparentMessage.sender);
                Message m=factory.decryptMessage(transparentMessage);
                if(m instanceof ForwardMessage){
                    // this is a forward Message and needs to be handled here.
                    ForwardMessage forwardMessage= (ForwardMessage) m;
                    if(forwardMessage.receiverKey!=null){
                        this.kademliaService.getDHT().sendMessageToNode(forwardMessage.receiverKey,forwardMessage.messageByte);
                        this.foreignSessions.put(forwardMessage.receiver,transparentMessage.sender);
                    }
                }
                listenerService.listenMessage(key,sessionId,m);



            }
            else if(foreignSessions.containsKey(transparentMessage.sender)){
                // the message is sent to a session we know.
                MessageFactory factory=messageFactories.get(transparentMessage.sender);
                Message m=factory.decryptMessage(transparentMessage);
                if(m instanceof PingMessage){
                  // do nothing here.
                }


            }
            else{
                // no idea who's the receiver.
                KeyExchangeMessage message1 = new KeyExchangeMessage();
                MessageFactory factory=new MessageFactory(new KeyFactory());
                unVerifiedFactories.put(transparentMessage.sender,factory);
                factory.addSession(transparentMessage.sender);
                message1.nounce=factory.getMySessionId();
                this.kademliaService.getDHT().sendMessageToNode(key,sessionId,factory.serailizeTransparentLayer(message1));

            }
            Message message1 = MessageFactory.decerialize(message);
            if(message1 instanceof KeyExchangeMessage){
                this.localSessions.put(key,((KeyExchangeMessage) message1).nounce);
                this.sessions.put(message1.sender,key);
                this.sendMessageToNode(key,sessionId,message1);
            }
            else {
                listenerService.ListenMessage(message1);
            }


        } catch (InvalidProtocolBufferException | InstantiationException | InvocationTargetException | MessageFactory.AuthenticationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            e.printStackTrace();
        }
    }

    // seheduled every 5 minutes.
    @Scheduled(fixedRate = 5 * 60 * 1000)
    private void checkSubscription() {
        //todo: check if subscription of any client has expired and do what needs to be done.
    }
    public byte[] setupLocalSession(Key node){
        byte[] sessionId=this.localSessions.get(node);
        if(sessionId==null){
            KeyExchangeMessage exchangeMessage = new KeyExchangeMessage();
            exchangeMessage.nounce=new byte[32];
            new Random().nextBytes(exchangeMessage.nounce);
            try {
                exchangeMessage.sender=messageFactory.getMySessionId();
                byte[] returnData=kademliaService.getDHT().queryWithNode(node,exchangeMessage.toBytes());
                KeyExchangeMessage msg= (KeyExchangeMessage) messageFactory.deSerilizeAndDecrypt(returnData);
                if(Arrays.equals(msg.nounce,exchangeMessage.nounce)){
                    return msg.sender;
                }
                return null;
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
                e.printStackTrace();
            } catch (ServerShutdownException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            } catch (MessageFactory.AuthenticationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;

        }
        return this.localSessions.get(node);
    }
}
