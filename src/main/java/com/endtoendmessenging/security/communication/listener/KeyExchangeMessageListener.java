package com.endtoendmessenging.security.communication.listener;

import com.endtoendmessenging.security.communication.message.KeyExchangeMessage;
import com.endtoendmessenging.security.communication.message.Message;

public class KeyExchangeMessageListener extends Listener{

    @Override
    public void onReceive(Message m) {
        reply(m);
    }
}
