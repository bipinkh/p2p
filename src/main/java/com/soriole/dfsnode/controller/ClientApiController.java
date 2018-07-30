package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.dto.*;
import com.soriole.dfsnode.service.ClientDataService;
import com.soriole.dfsnode.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("ping request");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Server Running");
    }

    /** -- File Service Apis -- **/

    @PostMapping("/file/upload")
    public ResponseEntity<Boolean> uploadFile(UploadRequest request){
        System.out.println("file upload request");
        return clientDataService.uploadFile(request);
    }

    @PostMapping("/file/download")
    public ResponseEntity<File> downloadFile(DownloadRequest request){
        System.out.println("download request");
        return clientDataService.getFile(request);
    }

    @PostMapping("/file/renew")
    public ResponseEntity<Boolean> renewFile(RenewRequest request){
        System.out.println("renew request");
        return clientDataService.renewFile(request);
    }

    @GetMapping("/file/getstatus")
    public ResponseEntity<ClientDataDto> renewFile(@RequestParam("file_hash") String fileHash){
        System.out.println("file status request");
        return clientDataService.getStatusOfFile(fileHash);
    }

    @GetMapping("/file/listmyfiles")
    public List<ClientDataDto> listOfFiles(@RequestParam("userKey") String userKey){
        System.out.println("list my files request");
        return clientDataService.listAllFiles(userKey);
    }

    /** -- Transaction Apis -- **/

    @GetMapping("/transactions")
    public List<TransactionDto> getAllTransactionsOfUser(@RequestParam("userKey") String userKey){
        System.out.println("transaction request");
        return transactionService.getAllTransactions(userKey);
    }
}
