package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.blockchain.BlockchainCred;
import com.soriole.dfsnode.model.dto.ClientDataDto;
//import com.soriole.dfsnode.model.dto.Balances;
import com.soriole.dfsnode.model.dto.NodeDetails;
import com.soriole.dfsnode.model.dto.NodeTransactionDetails;
import com.soriole.dfsnode.service.NodeService;
import com.soriole.dfsnode.service.TransactionService;
import com.soriole.kademlia.controller.KademliaApiController;
import com.soriole.kademlia.model.remote.NodeInfoCollectionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
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
    @Autowired
    KademliaApiController kademliaApiController;

    public void onReceivedMessage(String message){
        this.messagingTemplate.convertAndSend("/nodedetails",
                new SimpleDateFormat("HH:mm:ss").format(new Date())+ " - " + message);
    }

    /** -- Other Apis -- **/

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public String ping(){
        return "Server Active in WebSocket !";
    }

    @MessageMapping("/routingtable")
    @SendTo("/topic/routingtable")
    public NodeInfoCollectionBean routingtable(){
        System.out.println("Broadcasting routing table");
        return kademliaApiController.getRoutingTable();
    }

    @MessageMapping("/buytoken")
    @SendTo("/topic/hahaha")
    public void getTokens(){
        System.out.println("buying tokens");
        BlockchainCred.getBlockchainService().buyToken(BigInteger.valueOf(1000));
        System.out.println("bought tokens");
    }



    @MessageMapping("/checktoken")
    @SendTo("/topic/tokens")
    public BalanceDto myTokens(){
        try {
            BigInteger tokens =  BlockchainCred.getBlockchainService().getTokenBalance();
            BigInteger ethereum =  BlockchainCred.getBlockchainService().getEthereumBalance();
            return new BalanceDto(tokens.doubleValue(),ethereum.doubleValue() / (10^18));
        } catch (Exception e) {
            e.printStackTrace();
            return new BalanceDto(0,0);
        }
    }


    /** -- for pop up notification -- **/
    @MessageMapping("/popup")
    @SendTo("/topic/popup")
    public String popop(String message){
        System.out.println("Sending notification : "+message);
        return message;
    }


    /** -- Stat Apis -- **/
    @MessageMapping("/stats")
    @SendTo("/topic/stats")
    public NodeDetails getDetails(){
        System.out.println("Broadcasting stats to websocket clients");
        ResponseEntity<NodeDetails> entity = nodeService.getStats();
        return entity.getBody();
    }

    /** -- Transaction Apis -- **/
    @MessageMapping("/transactions")
    @SendTo("/topic/transactions")
    public NodeTransactionDetails getTxnDetails(){
        System.out.println("Broadcasting txns to websocket clients");
        ResponseEntity<NodeTransactionDetails> entity = nodeService.getTxns();
        return entity.getBody();
    }

    /** -- Files Apis -- **/
    @MessageMapping("/files")
    @SendTo("/topic/files")
    public List<ClientDataDto> listOfAllFiles(){
        System.out.println("Broadcasting files to websocket clients");
        List<ClientDataDto> clientDataDtos = nodeService.getFiles();

        List<String> entity = new ArrayList<>();
        for (ClientDataDto dataDto : clientDataDtos)
            entity.add(dataDto.toString());

        return clientDataDtos;
    }


}
