package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.dto.UploadRequest;
import com.soriole.dfsnode.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@RestController
@RequestMapping("/dfsclient/v1/")
public class ClientApiController {

    @Autowired
    ClientDataService clientDataService;

    @GetMapping("/ping")
    public String ping(){
        return "Server Running";
    }

    @PostMapping("/file")
    public boolean uploadFile(UploadRequest request){
        return clientDataService.uploadFile(request);
    }
}
