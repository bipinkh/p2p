package com.messengerclient;

import com.endtoendmessenging.security.communication.MessageFactory;
import com.endtoendmessenging.security.communication.message.ForwardMessage;
import com.endtoendmessenging.security.communication.message.KeyExchangeMessage;
import com.endtoendmessenging.security.crypto.KeyFactory;
import com.soriole.kademlia.core.KademliaExtendedDHT;
import com.soriole.kademlia.core.store.NodeInfo;
import com.soriole.kademlia.network.ServerShutdownException;
import com.soriole.kademlia.network.receivers.ByteReceiver;

import java.io.IOException;
import java.util.*;

public class TorCircuit implements ByteReceiver {

    LinkedList<byte[]> msgs = new LinkedList<>();

    @Override
    synchronized public void onNewMessage(NodeInfo key, long sessionId, byte[] message) {
        this.msgs.addLast(message);
        notify();
    }

    long timeoutMs = 2 * 1000;

    public static class InsufficientNodesException extends Exception {
    }

    KademliaExtendedDHT dht;

    LinkedList<NodeInfo> nodeAddress = new LinkedList<>();
    LinkedList<MessageFactory> secrets;


    public TorCircuit(Collection<NodeInfo> nodes, int circuitLength, KademliaExtendedDHT dht) throws InsufficientNodesException {
        dht.setMessageReceiver(this);
        // set the limit to depth
        if (circuitLength < 3) {
            circuitLength = 3;
        }
        if (circuitLength > 6) {
            circuitLength = 6;
        }


        // shuffle the collection and pick nodes equal to the circuitLength.
        nodes = new ArrayList(nodes);
        Collections.shuffle((List<?>) nodes);

        for (NodeInfo n : nodes) {
            createSession(n);
            if (nodeAddress.size() == circuitLength) {
                break;
            }
        }

        // if there were no sufficient nodes, throw an exception.
        if (nodeAddress.size() < circuitLength) {
            throw new InsufficientNodesException();
        }

    }

    private boolean createSession(NodeInfo node) {
        // create a KeyExchange Message to send to node.
        KeyExchangeMessage msg = KeyExchangeMessage.newInstance();

        // create a new MessageFactory
        MessageFactory secret=new MessageFactory(new KeyFactory());

        // set the sessionId.
        msg.sender = secret.getMySessionId();
        // convert the message to byte.

        //serialize the message
        byte[] message = secret.serailizeTransparentLayer(msg);

        Iterator<MessageFactory> secretFactory = this.secrets.iterator();
        Iterator<NodeInfo> nodeInfoIterator=this.nodeAddress.iterator();

        // for each node, wrap the message byte by the node's Address.
        // for the first node, the nodeAddress will not be in the table.
        for (NodeInfo n : nodeAddress) {
            ForwardMessage fMessage = new ForwardMessage();
            fMessage.receiverKey=nodeInfoIterator.next().getKey();
            fMessage.messageByte = message;
            message = secretFactory.next().serailizeTransparentLayer(fMessage);
        }



        // now we have have wrapped the messages.
        // send the message to the first node.
        try {
            dht.sendMessageToNode(node.getKey(), message);

            // wait for the reply of the message.
            long waitTime = timeoutMs;
            long endTime = System.currentTimeMillis() + waitTime;
            synchronized (this) {
                while (timeoutMs > 0) {
                    try {
                        //TODO: PROPERLY DECERIALIZE TO RECEIVE IN CORRECT FORM
                        wait(waitTime);
                        KeyExchangeMessage m = (KeyExchangeMessage) secrets.getLast().deSerilizeAndDecrypt(msgs.pollFirst());
                        if(m!=null){
                            if(Arrays.equals(m.nounce,msg.nounce)){
                                waitTime+=2*1000;
                                nodeAddress.addLast(node);
                                secrets.addLast(secret);
                                return true;
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    waitTime = endTime - System.currentTimeMillis();
                }
            }
            // wait for receiving message.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
            e.printStackTrace();
        } catch (ServerShutdownException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] wrapMessage(byte[] message){
        Iterator<MessageFactory> secretFactory = this.secrets.iterator();
        for (NodeInfo n : nodeAddress) {
            ForwardMessage fMessage = new ForwardMessage();
            fMessage.messageByte = message;
            fMessage.receiver=n.getKey().toBytes();
            message = secretFactory.next().serializeAndEncrypt(fMessage);
        }
        return message;
    }
    public void sendMessage(byte[] message) throws KademliaExtendedDHT.NoSuchNodeFoundException, ServerShutdownException, IOException {
        message=wrapMessage(message);
        dht.sendMessageToNode(nodeAddress.getLast().getKey(),message);
    }
    public void decodeReply(byte[] message){
        
    }
    public void receiveMessage(byte[] message){

    }
}
