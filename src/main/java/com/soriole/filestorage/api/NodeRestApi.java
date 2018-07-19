package com.soriole.filestorage.api;

import com.soriole.filestorage.model.dto.SubscriptionDto;
import com.soriole.filestorage.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@RestController
@RequestMapping("/node-api/v1/")
public class NodeRestApi {

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping("/ping")
    public String checkStatus(){
        return "Admin api service is running";
    }

    // add a subscription offer
    @PostMapping("/subscription/add")
    public SubscriptionDto addSubscriptionPackage(@RequestBody SubscriptionDto subsRequest){
        return subscriptionService.addPackage(subsRequest);
    }

    // get a subscription offer details by id
    @GetMapping("/subscription/{subscriptionId}")
    public SubscriptionDto getPackage(@PathVariable(value="subscriptionId") String subscriptionId){
        return subscriptionService.getPackage(Long.valueOf(subscriptionId));
    }

    //get all subscription offers available
    @GetMapping("/subscription/all")
    public List<SubscriptionDto> getAllPackages(){
        return subscriptionService.getAllPackages();
    }

    // get all active subscription offers available
    @GetMapping("/subscription/active")
    public List<SubscriptionDto> getActivePackage(){
        return subscriptionService.getActivePackages();
    }

    // deactivate a subscription offer by id
    @GetMapping("/subscription/deactivate")
    public SubscriptionDto deactivateSubscriptionPackage(@RequestParam String subscriptionId){
        return subscriptionService.deactivatePackage(Long.valueOf(subscriptionId));
    }

}
