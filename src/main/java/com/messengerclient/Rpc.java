package com.messengerclient;

import com.endtoendmessenging.security.communication.MessageFactory;
import com.endtoendmessenging.security.communication.message.*;
import com.endtoendmessenging.security.crypto.KeyFactory;
import com.google.protobuf.InvalidProtocolBufferException;
import com.soriole.kademlia.core.KademliaConfig;
import com.soriole.kademlia.core.KademliaExtendedDHT;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.core.store.NodeInfo;
import com.soriole.kademlia.core.util.BlockingHashTable2;
import com.soriole.kademlia.network.ServerShutdownException;
import com.soriole.kademlia.network.receivers.ByteReceiver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;

public class Rpc implements ByteReceiver {
    TorCircuit circuit;

    KademliaExtendedDHT dht;
    Key localKey;

    HashMap<byte[] ,MessageFactory> factories;
    HashMap<byte[], MessageFactory> unVerifiedfactories;
    HashMap<byte[], byte[]> unVerifiedNounces;

    HashMap<Key,byte[]> clientSessions;

    BlockingHashTable2<byte[],Message> waiters=new BlockingHashTable2(6000);
    IncomingMessageProcessor processor;

    public Rpc(Key localKey, NodeInfo bootstrapNode) throws SocketException {
        this.localKey = localKey;
        dht = new KademliaExtendedDHT(localKey, KademliaConfig.getDefaultConfig());
        dht.start();
        dht.join(bootstrapNode);

        try {
            circuit=new TorCircuit(dht.getRoutingTable(),3,dht);

        } catch (TorCircuit.InsufficientNodesException e) {
            e.printStackTrace();
        }
        dht.setMessageReceiver(this);
        processor=new IncomingMessageProcessor(this);
    }

    public Collection<NodeInfo> collectNodes() {
        return dht.getRoutingTable();
    }

    public boolean sendText(byte[] receiver, String message) throws KademliaExtendedDHT.NoSuchNodeFoundException, ServerShutdownException, IOException {

        return false;
    }

    public boolean subscribe(String postman) {
        return false;
    }

    public boolean sendOverTor(byte[] receiver,Message message) throws KademliaExtendedDHT.NoSuchNodeFoundException, ServerShutdownException, IOException {
        MessageFactory factory=factories.get(receiver);
        if(factory==null){
            startSession(receiver);
            return false;
        }
        else{
            new ClientMessage();
            circuit.sendMessage(factory.serializeAndEncrypt(message));

        }
        return true;
    }
    public boolean sendOverTorToclient(byte[] receiver,Message message) throws KademliaExtendedDHT.NoSuchNodeFoundException, ServerShutdownException, IOException {

        MessageFactory factory=factories.get(receiver);
        if(factory==null){
            startSession(receiver);
            return false;
        }
        else{
            new ClientMessage();
            circuit.sendMessage(factory.serializeAndEncrypt(message));

        }
    }
    public void startSession(byte[] clientPublicKey) throws KademliaExtendedDHT.NoSuchNodeFoundException, ServerShutdownException, IOException {
        KeyExchangeMessage msg=null;
        MessageFactory factory=null;
        if(factories.containsKey(clientPublicKey)){
            // we have a session. just need to make sure another client or peer
            // also remembers it.
        }
        else if(unVerifiedfactories.containsKey(clientPublicKey)){
            factory=unVerifiedfactories.get(clientPublicKey);
            msg=new KeyExchangeMessage();
            msg.nounce=unVerifiedNounces.get(clientPublicKey);
        }
        else {
            factory = new MessageFactory(new KeyFactory());
            msg = KeyExchangeMessage.newInstance();
            unVerifiedNounces.put(clientPublicKey, msg.nounce);
            unVerifiedfactories.put(clientPublicKey, factory);
        }
        circuit.sendMessage(factory.serailizeTransparentLayer(msg));
    }

    @Override
    public void onNewMessage(NodeInfo key, long sessionId, byte[] message) {
        try {
            TransparentMessage msg=TransparentMessage.fromBytes(message);
            MessageFactory factory=factories.get(msg.sender);
            if(factory!=null){
            }
            else{
                factory=unVerifiedfactories.get(msg.sender);
                if(factory!=null){
                    Message m=factory.decryptMessage(msg);
                    if(m instanceof KeyExchangeMessage){
                        //((KeyExchangeMessage) m).nounce;
                    }
                }
                else{
                    factory=new MessageFactory(new KeyFactory());

                    KeyExchangeMessage message1 = new KeyExchangeMessage();
                    message1.nounce=msg.encryptedData;

                    try {
                        dht.sendMessageToNode(key,factory.serializeAndEncrypt(message1));
                        factories.put(msg.sender,factory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (KademliaExtendedDHT.NoSuchNodeFoundException e) {
                        e.printStackTrace();
                    } catch (ServerShutdownException e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (MessageFactory.AuthenticationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
