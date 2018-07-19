package com.soriole.filestorage.service;

import com.soriole.filestorage.exception.ResourceNotFoundException;
import com.soriole.filestorage.model.db.Pricing;
import com.soriole.filestorage.model.db.Subscription;
import com.soriole.filestorage.model.dto.PricingDto;
import com.soriole.filestorage.model.dto.SubscriptionDto;
import com.soriole.filestorage.repository.PricingRepo;
import com.soriole.filestorage.repository.SubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Service
public class SubscriptionService {

    public static final int DEACTIVATE_SUBSCRIPTION_PACKAGE = 2;
    public static final int ACTIVE_SUBSCRIPTION_PACKAGE = 1;

    @Autowired
    SubscriptionRepo subscriptionRepo;
    @Autowired
    PricingRepo pricingRepo;

    // method to save the subscription package that will be offered by the node.
    // default status of added package will be 1
    public SubscriptionDto addPackage(SubscriptionDto subsRequest) {

        Subscription subs = subscriptionRepo.save( Subscription.fromSubscriptionDto(subsRequest) );

        List<PricingDto> pricingDtos = subsRequest.getPricings();
        for (PricingDto pricingDto : pricingDtos){
            Pricing pricing = Pricing.fromPricingDto(pricingDto);
            pricing = pricingRepo.save( pricing );
            subs.addPricing( pricing );
        }
        return SubscriptionDto.fromSubscription( subscriptionRepo.saveAndFlush( subs ) );
    }


    // get the details of a subscription by subscription_id offered by node
    public SubscriptionDto getPackage(Long id) {
        Optional<Subscription> subs = subscriptionRepo.findById(id);
        if (! subs.isPresent())
            throw new ResourceNotFoundException("cannot get the subscription package for given id");
        return SubscriptionDto.fromSubscription( subs.get() );
    }


    // get the details of all subscription packages offered by node
    public List<SubscriptionDto> getAllPackages() {
        List<Subscription> subs = subscriptionRepo.findAll();
        List<SubscriptionDto> returnList = new ArrayList<SubscriptionDto>();
        for (Subscription dto: subs)
            returnList.add( SubscriptionDto.fromSubscription(dto) );
        return returnList;
    }

    // get all the active subscription packages offered by node
    public List<SubscriptionDto> getActivePackages() {
        List<Subscription> subs = subscriptionRepo.findByStatus(ACTIVE_SUBSCRIPTION_PACKAGE);
        List<SubscriptionDto> returnList = new ArrayList<SubscriptionDto>();
        for (Subscription dto: subs)
            returnList.add( SubscriptionDto.fromSubscription(dto) );
        return returnList;
    }


    // deactivate the subscription by subscription_id offered by node
    // status of deactivated subscription package is 2
    public SubscriptionDto deactivatePackage(Long id) {
        Optional<Subscription> subs = subscriptionRepo.findById(id);
        if (! subs.isPresent())
            throw new ResourceNotFoundException("cannot get the subscription package for given id");
        subs.get().setStatus(DEACTIVATE_SUBSCRIPTION_PACKAGE);
        return SubscriptionDto.fromSubscription( subscriptionRepo.saveAndFlush( subs.get() ) );
    }


}
