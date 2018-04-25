package com.soriole.kademlia.network;

import com.soriole.kademlia.core.messages.Message;
import com.soriole.kademlia.core.messages.listeners.ListenerFactory;
import com.soriole.kademlia.core.messages.listeners.MessageListener;
import com.soriole.kademlia.core.store.ContactBucket;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.core.store.NodeInfo;
import com.soriole.kademlia.core.NodeInteractionListener;
import com.soriole.kademlia.network.server.SessionServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.*;

public class KademliaMessageServer extends SessionServer {
    private static Logger LOGGER = LoggerFactory.getLogger(KademliaMessageServer.class);
    NodeInteractionListener nodeInteractionListener = getDefaultInteractionListener();

    int port;
    private static final int nThreadCount = 10;
    ExecutorService workerPool = null;

    public void setAlpha(int a) {
    }

    public KademliaMessageServer(int listeningPort, ContactBucket bucket,ExecutorService service) throws SocketException {
        super(new DatagramSocket(listeningPort), bucket);
        this.workerPool=service;
        this.port = listeningPort;

    }
    public KademliaMessageServer(int listeningPort, ContactBucket bucket) throws SocketException {
        this(listeningPort, bucket,null);
    }

    public void sendMessage(NodeInfo receiver, Message message) throws IOException {
        message.mDestNodeInfo = receiver;
        super.sendMessage(message);
    }

    public void replyFor(Message incoming, Message reply) throws IOException {
        reply.sessionId = incoming.sessionId;
        reply.mDestNodeInfo = incoming.mSrcNodeInfo;
        super.sendMessage(reply);
    }

    public Message queryFor(Message incoming, Message reply) throws TimeoutException, IOException {
        reply.sessionId = incoming.sessionId;
        reply.mDestNodeInfo = incoming.mSrcNodeInfo;
        try {
            return super.query(reply);
        } catch (TimeoutException e) {
            // catch and rethrow it. so that stack trace looks good.
            throw e;
        }

    }

    public void asynQueryFor(Message incoming, Message reply, MessageReceiver msgReceiver) throws ServerShutdownException {
        reply.sessionId = incoming.sessionId;
        reply.mDestNodeInfo = incoming.mSrcNodeInfo;
        submitQuerytoPool(reply, msgReceiver);
    }

    public Message startQuery(NodeInfo receiver, Message message) throws TimeoutException, IOException {
        message.mDestNodeInfo = receiver;
        message.sessionId = new Random().nextLong();
        try {
            message = super.query(message);
            return message;
        }
        catch (TimeoutException e){
            // catch and rethrow it so that the stack trace is small
            throw e;
        }

    }


    public void startQueryAsync(NodeInfo receiver, Message message, final MessageReceiver msgReceiver) throws ServerShutdownException {

        message.mDestNodeInfo = receiver;
        message.sessionId = new Random().nextLong();
        submitQuerytoPool(message, msgReceiver);
    }

    private void submitQuerytoPool(final Message message, final MessageReceiver msgReceiver) throws ServerShutdownException {
        if (workerPool == null || socket.isClosed()) {
            throw new ServerShutdownException();
        }
        workerPool.submit(() -> {
            try {
                msgReceiver.onReceive(super.query(message));
                return;
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            msgReceiver.onTimeout();
        });
    }


    // This method called by the super class when new message needs to be handled.
    @Override
    protected void OnNewMessage(final Message message) {
        final MessageListener listener;
        try {
            listener = ListenerFactory.getListener(message, bucket, this);
            workerPool.submit(() -> {
                try {
                    listener.onReceive(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (ListenerFactory.NoListenerException e) {
            LOGGER.warn(message.getClass().getSimpleName() + " : sent by `" + message.mSrcNodeInfo.getKey() + "` received and dropped ");
            return;
        }
        ;
    }

    public boolean shutDown(int timeSeconds) throws InterruptedException {
        if (workerPool.isShutdown()) {
            return false;
        }
        super.stopListening();
        this.workerPool.awaitTermination(timeSeconds, TimeUnit.SECONDS);
        this.workerPool.shutdown();
        this.workerPool = null;
        return true;

    }
    public boolean stop(){
        if(this.socket.isClosed()){
            return false;
        }
        super.stopListening();
        return true;
    }

    /**
     * @return True if server begins starts , False if server was already running
     * @throws SocketException
     */

    public boolean start() throws SocketException {
        if (this.workerPool == null) {
            this.workerPool = Executors.newFixedThreadPool(nThreadCount);
        }

        if (this.socket.isClosed()) {
            this.socket = new DatagramSocket(port);
            workerPool.submit(() -> listen());
            return true;
        }
        // socket is created but not started to read
        else if (!this.socket.isConnected()) {
            workerPool.submit(() -> listen());
            return true;
        }

        return false;
    }

    @Override
    protected void onNetworkAddressChange(Key senderKey, InetSocketAddress newSocketAddress) {
        workerPool.submit(() -> nodeInteractionListener.onNetworkAddressChange(senderKey, newSocketAddress));
    }

    @Override
    protected void onNewNodeFound(NodeInfo info) {
        workerPool.submit(() -> nodeInteractionListener.onNewNodeFound(info));
    }

    @Override
    protected void onNewForwardMessage(Message message, NodeInfo destination) {
        if(destination!=null){
            try {
                sendMessage(destination,message);
            } catch (IOException e) {
                LOGGER.warn("Forwarding message to `"+destination.toString()+"` failed!");
            }
        }
        workerPool.submit(() -> nodeInteractionListener.onNewForwardMessage(message,destination));
    }

    public void setNodeInteractionListener(NodeInteractionListener listener) {
        this.nodeInteractionListener = listener;
    }

    // the default interaction listener.
    private static NodeInteractionListener getDefaultInteractionListener() {
        return new NodeInteractionListener() {
            @Override
            public void onNetworkAddressChange(Key key, InetSocketAddress address) {
                LOGGER.warn("Ignoring Network Address change of : " + key.toString());

            }

            @Override
            public void onNewNodeFound(NodeInfo info) {
                LOGGER.warn("Bucket overflow occured and the contact is ignored : " + info.getKey().toString());

            }
            @Override
            public void onNewForwardMessage(Message message, NodeInfo destination) {
                LOGGER.warn("Message to be forwarded to `"+destination.toString()+"` is dropped");
            }
        };
    }

}
