package com.soriole.dfsnode.controller;

import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.NodeDetails;
import com.soriole.dfsnode.model.dto.NodeStore;
import com.soriole.dfsnode.model.dto.NodeTransactionDetails;
import com.soriole.dfsnode.service.NodeDataService;
import com.soriole.dfsnode.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 03 Aug 2018
 */
@RestController
@RequestMapping("/dfsnode/v1/")
public class NodeApiController {

    @Autowired
    NodeDataService nodeDataService;
    @Autowired
    TransactionService transactionService;

    /** -- Other Apis -- **/

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Node is Online");
    }

    /** -- Stat Apis -- **/
    @GetMapping("/stats")
    public ResponseEntity<NodeDetails> getDetails(){
        //todo: make here !
        return null;
    }

    /** -- Transaction Apis -- **/
    @GetMapping("/transactions")
    public ResponseEntity<NodeTransactionDetails> getTxnDetails(){
        //todo: make here !
        return null;
    }

    /** -- Files Apis -- **/
    @GetMapping("/files")
    public List<ClientDataDto> listOfAllFiles(){
        return null;
    }


}
