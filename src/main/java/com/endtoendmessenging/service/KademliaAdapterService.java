package com.endtoendmessenging.service;

import com.endtoendmessenging.repository.MessageRepository;
import com.endtoendmessenging.security.communication.message.*;
import com.endtoendmessenging.security.communication.MessageFactory;
import com.endtoendmessenging.security.crypto.DHKeyPair;
import com.endtoendmessenging.security.crypto.KeyFactory;
import com.endtoendmessenging.model.Subscription;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.soriole.kademlia.core.KadProtocol;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
    HashMap<byte[], Key> sessions;
    // Contains the messageFactories to encrypt/decrypt incoming messages
    HashMap<byte[], MessageFactory> messageFactories;
    HashMap<byte[], MessageFactory> unVerifiedFactories;
    HashMap<byte[], NodeInfo> sessionNodeInfo;
    // key to session mapping
    HashMap<Key, byte[]> localSessions;
    HashMap<byte[], byte[]> foreignSessions;


    public int bucketSize;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    KademliaService kademliaService;


    MessageFactory messageFactory;


    @PostConstruct
    public void init() {
        kademliaService.getDHT().setMessageReceiver(this);
        DHKeyPair keyPair = new DHKeyPair(seed.getBytes());
        KeyFactory factory = new KeyFactory(new DHKeyPair());
        messageFactory = new MessageFactory(factory);

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
     * @param receiverID we know the receiver
     * @param message    we need to send a byte message to receiver.
     * @return
     */

    public boolean sendToClient(byte[] receiverID, byte[] message) throws KademliaExtendedDHT.NoSuchNodeFoundException,TimeoutException,KadProtocol.ContentNotFoundException {

        // if we are the subscribed node of the receiver
        if (subscriptionRepository.findById(receiverID).isPresent()) {
            messageRepository.save(new com.endtoendmessenging.model.Message(receiverID, message));
            return true;

        }
        // otherwise we need to find the node subscribed by the client and fordward it to that node.
        try {

            TimeStampedData<byte[]> data = kademliaService.getDHT().get(new Key(receiverID));
            Key nodeKey = new Key(data.getData());

            ClientMessage clientMessage = new ClientMessage();
            clientMessage.messageData = message;
            clientMessage.receiver = receiverID;

            Message m = MessageFactory.createMessageInstance(kademliaService.getDHT().queryWithNode(nodeKey, MessageFactory.toUnencryptedBytes(clientMessage)));
            if (m instanceof AcknowledgeMessage) {
                if (((AcknowledgeMessage) m).isSuccess()) {
                    return true;
                }
            }

        } catch (ServerShutdownException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * // The function to handle new incoming Non-Kademlia messages
     *
     * @param info
     * @param byteMessage
     */
    @Override
    public byte[] onNewMessage(NodeInfo info, byte[] byteMessage) {
        Key key = info.getKey();
        try {

            Message message = MessageFactory.createMessageInstance(byteMessage);

            if (message.receiver != null) {
                if (new Key(message.receiver) != getLocalKey()) {
                    // TODO: Forwarding messages not considered till now
                }
            }
            if (message instanceof ClientMessage) {

                if (new Key(kademliaService.getDHT().getLocal(new Key(message.receiver)).getData()).equals(getLocalKey())) {
                    this.sendToClient(message.receiver, ((ClientMessage) message).messageData);
                    return MessageFactory.toUnencryptedBytes(new AcknowledgeMessage(true));
                }
                return MessageFactory.toUnencryptedBytes(new AcknowledgeMessage(false));
            }
            if (message instanceof SubscriptionMessage) {
                if (((SubscriptionMessage) message).nodeToSubscribe.equals(getLocalKey())) {
                    if (this.subscribe(new Key(message.sender), ((SubscriptionMessage) message).nodeToSubscribe, 3600 * 24 * 30)) {
                        return MessageFactory.toUnencryptedBytes(new AcknowledgeMessage(true));
                    }
                }
                return MessageFactory.toUnencryptedBytes(new AcknowledgeMessage(false));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // seheduled every 5 minutes.
    @Scheduled(fixedRate = 5 * 60 * 1000)
    private void checkSubscription() {
        //todo: check if subscription of any client has expired and do what needs to be done.
    }


    public Key getLocalKey() {
        return kademliaService.getDHT().getLocalNode().getKey();
    }

    public Collection<NodeInfo> getPeers() {
        return kademliaService.getDHT().getRoutingTable();
    }

    public boolean subscribe(Key clientKey, Key nodeKey, int subscriptionTimeSeconds) throws
            KademliaExtendedDHT.NoSuchNodeFoundException, TimeoutException {
        try {
            try {
                TimeStampedData<byte[]> data = kademliaService.getDHT().get(clientKey);
                return false;
            } catch (KadProtocol.ContentNotFoundException e) {

            }

            if (nodeKey.equals(getLocalKey())) {
                Subscription subscription = new Subscription();
                subscription.setSubscriber(clientKey.toBytes());
                subscription.setSubscriptionStart(new Date());
                // subscription expires in a month period.
                subscription.setSubscriptionExpiry(new Date(new Date().getTime() + subscriptionTimeSeconds * 1000));
                subscriptionRepository.save(subscription);

                try {
                    kademliaService.getDHT().put(clientKey, nodeKey.toBytes());
                } catch (ServerShutdownException e) {
                    e.printStackTrace();
                }
                return true;

            }
            SubscriptionMessage message = new SubscriptionMessage();
            message.sender = clientKey.toBytes();
            message.nodeToSubscribe = nodeKey;


            Message m = MessageFactory.createMessageInstance(kademliaService.getDHT().queryWithNode(nodeKey, MessageFactory.toUnencryptedBytes(message)));
            if (m instanceof AcknowledgeMessage) {
                return ((AcknowledgeMessage) m).isSuccess();
            }
            throw new TimeoutException();
        } catch (ServerShutdownException e) {
            e.printStackTrace();
            throw new TimeoutException();
        }

    }

    public byte[] getValue(Key key) throws KadProtocol.ContentNotFoundException, ServerShutdownException {
        return kademliaService.getDHT().get(key).getData();
    }

}
