package com.endtoendmessenging.security.communication.listener;

import com.endtoendmessenging.KademliaAdapterService;
import com.endtoendmessenging.security.communication.message.Message;

/**
 * @author github.com/mesudip
 */
public abstract class Listener {
    private KademliaAdapterService server;

    protected void reply(Message message){

    }
    protected Message query(Message message){
        return null;
    }
    abstract public void onReceive(Message m);
    public void setServer(KademliaAdapterService server){
        this.server=server;
    }
    public KademliaAdapterService getServer(){return server;}
}
