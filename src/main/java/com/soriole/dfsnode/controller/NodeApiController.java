package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.db.Client;
import com.soriole.dfsnode.model.db.ClientData;
import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.NodeDetails;
import com.soriole.dfsnode.model.dto.NodeTransactionDetails;
import com.soriole.dfsnode.service.NodeService;
import com.soriole.dfsnode.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 03 Aug 2018
 */
@Controller
@MessageMapping("/dfsnode/v1/")
public class NodeApiController {
//todo: remove getmapping and use messagemapping in the app if it didn't worked !
    @Autowired
    NodeService nodeService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void onReceivedMessage(String message){
        this.messagingTemplate.convertAndSend("/chat",
                new SimpleDateFormat("HH:mm:ss").format(new Date())+ " - " + message);
    }

    /** -- Other Apis -- **/

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        System.out.println("check");
        this.messagingTemplate.convertAndSend("/chat",
                new SimpleDateFormat("HH:mm:ss").format(new Date())+ " - " + "server running");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Node is Online");
    }

    /** -- Stat Apis -- **/
    @GetMapping("/stats")
    public ResponseEntity<NodeDetails> getDetails(){
        ResponseEntity<NodeDetails> entity = nodeService.getStats();
        this.messagingTemplate.convertAndSend("/chat",
                entity.getBody().toString());
        return entity;
    }

    /** -- Transaction Apis -- **/
    @GetMapping("/transactions")
    public ResponseEntity<NodeTransactionDetails> getTxnDetails(){
        ResponseEntity<NodeTransactionDetails> entity = nodeService.getTxns();
        this.messagingTemplate.convertAndSend("/chat",
                entity.getBody().toString());
        return entity;
    }

    /** -- Files Apis -- **/
    @GetMapping("/files")
    public List<ClientDataDto> listOfAllFiles(){
        List<ClientDataDto> clientDataDtos = nodeService.getFiles();

        List<String> entity = new ArrayList<>();
        for (ClientDataDto dataDto : clientDataDtos)
            entity.add(dataDto.toString());
        this.messagingTemplate.convertAndSend("/chat",
                entity.toString());

        return clientDataDtos;
    }


}
