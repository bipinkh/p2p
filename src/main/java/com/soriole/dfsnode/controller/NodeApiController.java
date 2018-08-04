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
import org.springframework.messaging.handler.annotation.SendTo;
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
public class NodeApiController {
//todo: remove getmapping and use messagemapping in the app if it didn't worked !
    @Autowired
    NodeService nodeService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void onReceivedMessage(String message){
        this.messagingTemplate.convertAndSend("/nodedetails",
                new SimpleDateFormat("HH:mm:ss").format(new Date())+ " - " + message);
    }

    /** -- Other Apis -- **/

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public String ping(){
        return "bhusal is a bad boy !";
    }

    /** -- Stat Apis -- **/
    @MessageMapping("/stats")
    @SendTo("/topic/stats")
    public NodeDetails getDetails(){
        ResponseEntity<NodeDetails> entity = nodeService.getStats();
        return entity.getBody();
    }

    /** -- Transaction Apis -- **/
    @MessageMapping("/transactions")
    @SendTo("/topic/transactions")
    public NodeTransactionDetails getTxnDetails(){
        ResponseEntity<NodeTransactionDetails> entity = nodeService.getTxns();
        return entity.getBody();
    }

    /** -- Files Apis -- **/
    @MessageMapping("/files")
    @SendTo("/topic/files")
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
