package com.endtoendmessenging.security.communication.listener;

import com.endtoendmessenging.repository.MessageRepositoryImplementaion;
import com.endtoendmessenging.repository.SubscriptionRepository;
import com.endtoendmessenging.security.communication.message.Message;
import com.soriole.kademlia.service.KademliaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class clientMessageListener extends Listener {
    Logger logger=LoggerFactory.getLogger(clientMessageListener.class);
    @Autowired
    SubscriptionRepository repository;

    @Autowired
    MessageRepositoryImplementaion messageRepo;

    @Autowired
    KademliaService kadService;
    @Override
    public void onReceive(Message m) {


    }
}
