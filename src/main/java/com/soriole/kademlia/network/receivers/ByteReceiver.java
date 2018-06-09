package com.soriole.kademlia.network.receivers;

import com.soriole.kademlia.core.store.NodeInfo;

public interface ByteReceiver {
    public void onNewMessage(NodeInfo key, long sessionId, byte[] message);
}