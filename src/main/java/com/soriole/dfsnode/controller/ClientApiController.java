package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.blockchain.BlockchainCred;
import com.soriole.dfsnode.blockchain.service.BlockchainService;
import com.soriole.dfsnode.model.dto.*;
import com.soriole.dfsnode.service.ClientDataService;
import com.soriole.dfsnode.service.TransactionService;
import com.soriole.kademlia.controller.KademliaApiController;
import com.soriole.kademlia.model.remote.NodeInfoCollectionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@RestController
@RequestMapping("/dfsclient/v1/")
public class ClientApiController {

    @Autowired
    ClientDataService clientDataService;
    @Autowired
    TransactionService transactionService;


    /** -- Other Apis -- **/

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Server Running");
    }

    /** -- File Service Apis -- **/

    @PostMapping("/file/upload")
    public ResponseEntity<Boolean> uploadFile(UploadRequest request){
        ResponseEntity<Boolean> returnObj = clientDataService.uploadFile(request);
        BlockchainCred.getBlockchainService().getDecentralizedDB().decreaseToken(BigInteger.valueOf(4)).sendAsync();
        return returnObj;
    }

    @PostMapping(value = "/file/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<FileSystemResource> downloadFile(DownloadRequest request){
        ResponseEntity<FileSystemResource> app =  clientDataService.getFile(request);
        BlockchainCred.getBlockchainService().getDecentralizedDB().increaseToken(BigInteger.valueOf(6)).sendAsync();
        return app;
    }

    @PostMapping("/file/renew")
    public ResponseEntity<Boolean> renewFile(RenewRequest request){
        return clientDataService.renewFile(request);
    }

    @GetMapping("/file/getstatus")
    public ResponseEntity<ClientDataDto> renewFile(@RequestParam("file_hash") String fileHash){
        return clientDataService.getStatusOfFile(fileHash);
    }

    @GetMapping("/file/listmyfiles")
    public List<ClientDataDto> listOfFiles(@RequestParam("userKey") String userKey){
        return clientDataService.listAllFiles(userKey);
    }

    /** -- Transaction Apis -- **/

    @GetMapping("/transactions")
    public List<TransactionDto> getAllTransactionsOfUser(@RequestParam("userKey") String userKey){
        return transactionService.getAllTransactions(userKey);
    }
}
