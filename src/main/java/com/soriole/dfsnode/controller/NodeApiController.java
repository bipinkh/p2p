package com.soriole.dfsnode.controller;

import com.soriole.blockchain.service.BlockchainService;
import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.NodeDetails;
import com.soriole.dfsnode.model.dto.NodeTransactionDetails;
import com.soriole.dfsnode.service.NodeService;
import com.soriole.dfsnode.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 03 Aug 2018
 */
@RestController
@RequestMapping("/dfsnode/v1/")
public class NodeApiController {

    @Autowired
    NodeService nodeService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    BlockchainService blockchainService;

    /**
     * -- Other Apis --
     **/

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Node is Online");
    }

    /**
     * -- Stat Apis --
     **/
    @GetMapping("/stats")
    public ResponseEntity<NodeDetails> getDetails() {
        return nodeService.getStats();
    }

    /**
     * -- Transaction Apis --
     **/
    @GetMapping("/transactions")
    public ResponseEntity<NodeTransactionDetails> getTxnDetails() {
        return nodeService.getTxns();
    }

    /**
     * -- Files Apis --
     **/
    @GetMapping("/files")
    public List<ClientDataDto> listOfAllFiles() {
        return nodeService.getFiles();
    }

    /** -- BlockChain services-- **/

    /**
     * -- Files Apis --
     **/
    @GetMapping("/token_balance")
    public BigInteger getTokenBalance() {
        try {
            return blockchainService.getTokenBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/eth_balance")
    public BigInteger getEthBalance() {
        try {
            return blockchainService.getEthereumBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/node_id")
    public BigInteger getNodeId() {
        try {
            return blockchainService.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/verify/data")
    public BigInteger verifyData(@RequestParam("chunk_hash") byte[] chunkHash, @RequestParam("verify") Boolean verify) {
        try {
            blockchainService.verifyChunkData(chunkHash, verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/verify/download/data")
    public BigInteger verifyDownloadData(@RequestParam("chunk_hash") byte[] chunkHash, @RequestParam("verify") Boolean verify) {
        try {
            blockchainService.verifyDownloadChunkData(chunkHash, verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/buy/token")
    public BigInteger buyToken(@RequestParam("token_amt") BigInteger tokenAmt) {
        try {
            blockchainService.buyToken(tokenAmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping("/load/wallet_and_contract")
    public BigInteger loadWalletAndContract(@RequestParam("password") String password,@RequestParam("wallet_path") String walletPath) {
        try {
            blockchainService.loadWallet(password,walletPath);
            blockchainService.loadContract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
