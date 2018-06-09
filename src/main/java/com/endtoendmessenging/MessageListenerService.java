package com.endtoendmessenging;

import com.endtoendmessenging.model.Subscription;
import com.endtoendmessenging.security.communication.message.*;
import com.endtoendmessenging.repository.MessageRepositoryImplementaion;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.core.store.TimeStampedData;
import com.soriole.kademlia.network.ServerShutdownException;
import com.soriole.kademlia.service.KademliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageListenerService {
    private int subscriptionTimeSeconds =60*60*24*30;

    @Autowired
    MessageRepositoryImplementaion messageStore;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    KademliaService kadService;

    @Autowired
    KademliaAdapterService kadAdapterservice;

    long currentSessionId;
    Key key;

    synchronized  void listenMessage(Key key, long sessionId, Message message){
        currentSessionId=sessionId;
        this.key=key;
        ListenMessage(message);
    }
    public boolean ListenMessage(Message m){
        if(m instanceof AcknowledgeMessage){
            // we probably don't need this.
        }
        else if(m instanceof ClientMessage){
            ClientMessage message=(ClientMessage)m;
            Key clientKey= new Key(message.receiver);
            if(subscriptionRepository.countBySubscriber(message.receiver)>0){
                messageStore.save(new com.endtoendmessenging.model.Message(message.receiver,message.messageData));
            }
            else {
                try {
                    kadAdapterservice.sendMessageToNode(clientKey, m);
                } catch (Exception e) {
                    e.printStackTrace();
                    reply(new AcknowledgeMessage(false));
                }
            }
            reply(new AcknowledgeMessage());

        }

        else if (m instanceof SubscriptionMessage){
            Key clientKey=new Key(m.sender);
            TimeStampedData<byte[]> data = kadService.getDHT().getLocal(clientKey);
            // we cannot add the subscription, its already added.
            if (data != null) {
                return false;
            }
            if (kadService.getDHT().getLocalNode().getKey().equals(((SubscriptionMessage) m).nodeToSubscribe)) {
                Subscription subscription = new Subscription();
                subscription.setSubscriber(clientKey.toBytes());
                subscription.setSubscriptionStart(new Date());
                // subscription expires in a month period.
                subscription.setSubscriptionExpiry(new Date(new Date().getTime() + subscriptionTimeSeconds * 1000));
                subscriptionRepository.save(subscription);
                try {
                    kadService.getDHT().put(clientKey,((SubscriptionMessage) m).nodeToSubscribe.toBytes());
                } catch (ServerShutdownException e) {
                    e.printStackTrace();
                }
                return true;
            }
            // the user doesn't want to subscrbe us but another node.
            try {
                return kadAdapterservice.sendMessageToNode(((SubscriptionMessage) m).nodeToSubscribe,m);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }
        else if(m instanceof KeyExchangeMessage){
            reply(m);
        }
        else if(m instanceof ForwardMessage){

        }
        else if (m instanceof TextMessage){

        }

        return true;
    }
    private boolean reply(Message replyMessage){
        return kadAdapterservice.sendMessageToNode(key,currentSessionId,replyMessage);
    }

}
