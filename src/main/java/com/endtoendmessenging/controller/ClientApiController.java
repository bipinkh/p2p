package com.endtoendmessenging.controller;

import com.endtoendmessenging.repository.MessageRepository;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.endtoendmessenging.service.KademliaAdapterService;
import com.endtoendmessenging.model.remote.MessageCollecitonBean;
import com.endtoendmessenging.security.communication.message.TextMessage;
import com.soriole.kademlia.core.KadProtocol;
import com.soriole.kademlia.core.KademliaExtendedDHT;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.model.remote.NodeInfoCollectionBean;
import com.soriole.kademlia.network.ServerShutdownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/client/v1")
public class ClientApiController {

    private static Logger logger = LoggerFactory.getLogger(ClientApiController.class);

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    KademliaAdapterService kademliaService;

    @GetMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam(value = "nodeid") String nodeid, @RequestParam(value = "clientid") String clientid) {
        try {
            if (kademliaService.subscribe(new Key(clientid), new Key(nodeid), 3600 * 24 * 30))
                return ResponseEntity.ok("Success");
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            logger.info("/subscribe  -> Node not found : " + nodeid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The specified node doesn't exist in the network");

        } catch (TimeoutException e) {
            logger.info("/subscribe  -> Timeout connecting to node : " + nodeid);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The specified node Didn't respond");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error while processing your request");

    }

    @RequestMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam(value = "clientid") String id) {
        try {
            if (new Key(kademliaService.getValue(new Key(id))).equals(kademliaService.getLocalKey())) {
                if (subscriptionRepository.findById(new Key(id).toBytes()).isPresent()) {
                    subscriptionRepository.deleteById(new Key(id).toBytes());
                    return ResponseEntity.ok("Success");
                }
            } else {
                //todo: give the unsubscribe message to the postman of the client.
            }
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (KadProtocol.ContentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client is not subscribed to any node");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error while processing your request");
    }

    @GetMapping(value = "/get_subscribed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSubscribed(@RequestParam String clientid) {
        try {
            if (subscriptionRepository.findById(new Key(clientid).toBytes()).isPresent()) {
                return ResponseEntity.ok(kademliaService.getLocalKey().toString());
            }
            return ResponseEntity.ok(new Key(kademliaService.getValue(new Key(clientid))).toString());
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        } catch (com.soriole.kademlia.core.KadProtocol.ContentNotFoundException ignored) {
            return ResponseEntity.ok("null");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error while processing your request");
    }

    @GetMapping("get_peers")
    public NodeInfoCollectionBean getPeerNodes() {
        return NodeInfoCollectionBean
                .fromNodeInfoCollection(kademliaService.getPeers());
    }

    @GetMapping("send_message")
    public ResponseEntity<String> sendMessage(@RequestParam String clientid, @RequestParam String message, @RequestParam String receiver) {
        TextMessage txtMessage = new TextMessage(message);
        txtMessage.sender = new Key(clientid).toBytes();
        try {

            if (kademliaService.sendToClient(new Key(receiver).toBytes(), txtMessage.toBytes())) {
                return ResponseEntity.ok("Success");
            } else {
                return new ResponseEntity<>("Unexpected Error while processing your request", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            logger.info("/send_message  --> Client `" + receiver + "` has subscription but the node is not found");
            return new ResponseEntity<>("The Postmen of client is not found in the network", HttpStatus.EXPECTATION_FAILED);
        } catch (TimeoutException e) {
            logger.info("/send_message  --> Client `" + receiver + "` has subscription but the node didn't reply");
            return new ResponseEntity<>("The Postmen was discovered but didn't rely", HttpStatus.EXPECTATION_FAILED);

        } catch (KadProtocol.ContentNotFoundException e) {
            logger.info("/send_message  --> Client `" + receiver + "` not found in the network");
            return new ResponseEntity<>("The client doesn't use our service", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("get_message")
    public ResponseEntity getMessage(
            @RequestParam(value = "clientid") String clientId,
            @RequestParam(value = "count", required = false, defaultValue = " 10") int count,
            @RequestParam(value = "laterThan", required = false, defaultValue = "") String laterThan) {
        if (this.subscriptionRepository.findById(new Key(clientId).toBytes()).isPresent()) {
            return ResponseEntity.ok(MessageCollecitonBean.fromMessageCollection(messageRepository.findByReceiver(new Key(clientId).toBytes())));
        } else {
            return new ResponseEntity<>("The client doesn't use our service", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("signed_messages")
    public boolean SignedMessaeg(
            @RequestBody byte[] body
    ) {
        //TODO: To receive the encrypted and signed messages.
        return false;
    }

    @GetMapping("test")
    public String info() {
        return "test";
    }


}
