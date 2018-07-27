package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.dto.*;
import com.soriole.dfsnode.service.ClientDataService;
import com.soriole.dfsnode.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/ping")
    public String ping(){
        return "Server Running";
    }

    @PostMapping("/file/upload")
    public boolean uploadFile(UploadRequest request){
        return clientDataService.uploadFile(request);
    }

    @PostMapping("/file/download")
    public File uploadFile(DownloadRequest request){
        return clientDataService.getFile(request);
    }

    @PostMapping("/file/renew")
    public boolean renewFile(RenewRequest request){
        return clientDataService.renewFile(request);
    }

    @GetMapping("/file/getstatus")
    public ClientDataDto renewFile(@RequestParam("file_hash") String fileHash){
        return clientDataService.getStatusOfFile(fileHash);
    }

    @GetMapping("/transactions")
    public List<TransactionDto> getAllTransactionsOfUser(@RequestParam("userKey") String userKey){
        return transactionService.getAllTransactions(userKey);
    }
}
