package com.endtoendmessenging.repository;

import com.endtoendmessenging.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, byte[]> {
    //findBy[entityname] automatically implements the method
    Subscription findBySubscriber(byte[] subscriber);
    int countBySubscriber(byte[] subscriber);
    boolean deleteBySubscriber(byte[] subscriber);



}
