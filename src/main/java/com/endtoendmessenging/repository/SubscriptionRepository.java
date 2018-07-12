package com.endtoendmessenging.repository;

import com.endtoendmessenging.model.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, byte[]> {
    //findBy[entityname] automatically implements the method

}
