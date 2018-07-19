package com.soriole.filestorage.service;


import com.soriole.filestorage.exception.ResourceNotFoundException;
import com.soriole.filestorage.model.db.Subscription;
import com.soriole.filestorage.model.db.User;
import com.soriole.filestorage.model.db.UserSubscription;
import com.soriole.filestorage.model.dto.SubscribeRequest;
import com.soriole.filestorage.model.dto.UserSubscriptionDto;
import com.soriole.filestorage.repository.SubscriptionRepo;
import com.soriole.filestorage.repository.UserRepo;
import com.soriole.filestorage.repository.UserSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.soriole.filestorage.service.SubscriptionService.ACTIVE_SUBSCRIPTION_PACKAGE;


/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Service
public class SubscriberService {

    private static final int DEFAULT_USER_STATUS = 1;

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserSubscriptionRepo userSubscriptionRepo;
    @Autowired
    SubscriptionRepo subscriptionRepo;

    // subscribe user

    public UserSubscriptionDto createSubscription(SubscribeRequest request){

        User user;
        Subscription subscriptionPack;

        Optional<Subscription> subs =  subscriptionRepo.findById( Long.valueOf( request.getSubscriptionPackId() ) );
        if ( ! subs.isPresent() )
            throw new ResourceNotFoundException(" Subscription pack with given id doesn't exist.");
        else if ( subs.get().getStatus() != ACTIVE_SUBSCRIPTION_PACKAGE)
            throw new ResourceNotFoundException(" Subscription pack with given id doesn't exist or isn't available.");
        else
            subscriptionPack = subs.get();

        // get user
        // or, register user if they are not already registered
        Optional<User> u = userRepo.findByUserKey(request.getUserKey());
        if ( ! u.isPresent()){
            user = new User(null, request.getUserKey(), DEFAULT_USER_STATUS, null, null);
            user = userRepo.save(user);
        } else
            user = u.get();

        // create subscription and save it
        UserSubscription subscription = UserSubscription.fromSubscribeRequest(request);
        subscription = userSubscriptionRepo.save(subscription);

        // add bidirectional mapping
        user.addUserSubscription(subscription);
        subscriptionPack.addSubscriber(subscription);

        return UserSubscriptionDto.fromUserSubscription( userSubscriptionRepo.saveAndFlush(subscription) );

    }


    // view user subscription details

    public UserSubscriptionDto viewSubscription(String userKey) {
        Optional<User> usr = userRepo.findByUserKey(userKey);
        if (!usr.isPresent())
            throw new ResourceNotFoundException("cannot find the user subscription for given id");
        return UserSubscriptionDto.fromUserSubscription( usr.get().getUserSubscription());
    }

    // unsubscribe user

    public UserSubscriptionDto unsubscribe(String userKey) {
        Optional<User> usr = userRepo.findByUserKey(userKey);
        if (!usr.isPresent())
            throw new ResourceNotFoundException("cannot find the user subscription for given id");
        usr.get().getUserSubscription().setActiveStatus(false);
        return UserSubscriptionDto.fromUserSubscription( userSubscriptionRepo.save(usr.get().getUserSubscription()) );
    }


}
