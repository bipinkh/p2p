package com.filestorage.api;

import com.filestorage.model.dto.SubscribeRequest;
import com.filestorage.model.dto.UserSubscriptionDto;
import com.filestorage.service.FileService;
import com.filestorage.service.SubscriberService;
import com.filestorage.model.dto.FileUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

@RestController
@RequestMapping("/api/v1/")
public class ClientRestApi {

    @Autowired
    SubscriberService subscriberService;
    @Autowired
    FileService fileService;

    @GetMapping("")
    public String status(){
        return "Rest API Server is running";
    }

    @GetMapping("/ping")
    public String ping(){
        return "Online.";
    }

    @PostMapping("/subscribe/new")
    public UserSubscriptionDto newSubscription(@RequestBody SubscribeRequest subscribeRequest){
        return subscriberService.createSubscription(subscribeRequest);
    }

    @GetMapping("/subscribe/view")
    public UserSubscriptionDto newSubscription(@RequestParam String userKey){
        return subscriberService.viewSubscription(userKey);
    }

    @GetMapping("/unsubscribe")
    public UserSubscriptionDto unsubscribe(@RequestParam String userKey){
        return subscriberService.unsubscribe(userKey);
    }

    // this is just for temporary purpose
    @GetMapping("/temp-form")
    public String redirect() {
        return "redirect:/uploadImage.html";
    }

    @PostMapping("/file")
    public boolean submit(FileUploadRequest request) throws IOException {
        return fileService.saveFile(request);
    }



}
