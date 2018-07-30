package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.dto.*;
import com.soriole.dfsnode.service.ClientDataService;
import com.soriole.dfsnode.service.TransactionService;
import com.soriole.kademlia.controller.KademliaApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        return clientDataService.uploadFile(request);
    }

    @PostMapping("/file/download")
    public ResponseEntity<File> downloadFile(DownloadRequest request){
        return clientDataService.getFile(request);
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
