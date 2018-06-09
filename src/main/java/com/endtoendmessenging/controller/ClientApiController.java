package com.endtoendmessenging.controller;

import com.endtoendmessenging.service.KademliaAdapterService;
import com.endtoendmessenging.service.MessageListenerService;
import com.endtoendmessenging.model.Message;
import com.endtoendmessenging.model.remote.MessageCollecitonBean;
import com.endtoendmessenging.repository.MessageRepositoryImplementaion;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.endtoendmessenging.security.communication.message.SubscriptionMessage;
import com.endtoendmessenging.security.communication.message.TextMessage;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.model.remote.NodeInfoCollectionBean;
import com.soriole.kademlia.network.ServerShutdownException;
import com.soriole.kademlia.service.KademliaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/v1")
public class ClientApiController {

    private static Logger logger = LoggerFactory.getLogger(ClientApiController.class);
    @Autowired
    KademliaService kademliaService;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    MessageRepositoryImplementaion messageStore;

    @Autowired
    KademliaAdapterService server;

    @Autowired
    MessageListenerService listenerService;

    @GetMapping("subscribe")
    public boolean subscribe(@RequestParam(value = "nodeid") String nodeid, @RequestParam(value = "clientid") String clientid) {
        SubscriptionMessage message = new SubscriptionMessage();
        message.sender = new Key(clientid).toBytes();
        message.nodeToSubscribe = new Key(nodeid);
        return listenerService.ListenMessage(message);

    }

    @RequestMapping("unsubscribe")
    public boolean unsubscribe(@RequestParam(value = "id") String id) {
        if (subscriptionRepository.deleteBySubscriber(new Key(id).toBytes())) {
            return true;
        } else {
            // TODO: send unsubscription message to the subscrbed node if client has subscribed someone other than us.
        }
        return false;

    }

    @GetMapping("get_subscribed")
    public String subscribed(@RequestParam(value = "id") String id) {
        try {
            if (subscriptionRepository.countBySubscriber(new Key(id).toBytes()) > 0) {
                return kademliaService.getDHT().getLocalNode().toString();
            }
            return new Key(kademliaService.getDHT().get(new Key(id)).getData()).toString();

        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (com.soriole.kademlia.core.KadProtocol.ContentNotFoundException ignored) {
        }
        return "null";
    }

    @GetMapping("get_peer_nodes")
    public NodeInfoCollectionBean getPeerNodes() {
        return NodeInfoCollectionBean
                .fromNodeInfoCollection(kademliaService.getDHT().getRoutingTable());
    }

    @GetMapping("send_message")
    public boolean sendMessage(@RequestParam String clientId, @RequestParam String message) {
        TextMessage txtMessage = new TextMessage(message);
        txtMessage.sender = new Key(clientId).toBytes();
        return server.sendToClient(new Key(clientId).toBytes(), txtMessage);
    }

    @GetMapping("get_messages")
    public MessageCollecitonBean sendMessage(
            @RequestParam(value = "clientid") String clientId,
            @RequestParam(value = "count", required = false, defaultValue = " 10") int count) {
        List<Message> messageList = messageStore.getMessages(count, new Key(clientId).toBytes());
        for (Message m : messageList) {
            messageStore.delete(m.getId());
        }
        return MessageCollecitonBean.fromMessageCollection(messageList);
    }
    @GetMapping("signed_messages")
    public boolean SignedMessaeg(
            @RequestBody byte[] body
    ){
        //TODO: To receive the encrypted and signed messages.
        return false;
    }


}
