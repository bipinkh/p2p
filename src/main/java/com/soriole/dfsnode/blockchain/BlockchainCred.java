package com.soriole.dfsnode.blockchain;

import com.soriole.dfsnode.blockchain.service.BlockchainService;
import org.web3j.crypto.Credentials;

/**
 * @author github.com/bipinkh
 * created on : 07 Aug 2018
 */
public class BlockchainCred {

    private static BlockchainService blockchainService;

    public static BlockchainService getBlockchainService() {
        return blockchainService;
    }

    public static void setBlockchainService(BlockchainService blockchainService) {
        BlockchainCred.blockchainService = blockchainService;
    }

}
