package com.soriole.dfsnode.blockchain.service;

import com.soriole.dfsnode.blockchain.wrapper.DecentralizedDB;
import com.soriole.dfsnode.service.ClientDataService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Service
public class BlockchainService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ClientDataService.class);
    final String NODE_HTTP_ADDRESS="https://rinkeby.infura.io/v3/f4660278a7ca484f8c0d412410cbc6ac";
    final String CONTRACT_ADDRESS="0xb7FdDA60EbD878f18F1c47270F67896C0E9F78Fa";
    private Web3j web3j;
    private Credentials credentials;
    private DecentralizedDB decentralizedDB;
    @Autowired
    ClientDataService clientDataService;
    public boolean loadWallet(String password,String walletPath){

        try {
            credentials = WalletUtils.loadCredentials(password,walletPath);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public DecentralizedDB getDecentralizedDB() {
        return decentralizedDB;
    }

    public boolean loadCredFromPrivKey(String privKey){
        credentials = Credentials.create(privKey);
        return true;
    }

    public void loadContract() throws Exception {
        LOGGER.info("contract","public key:"+credentials.getEcKeyPair().getPublicKey());
        LOGGER.info("contract","address :"+credentials.getAddress());
        LOGGER.info("contract","Credentials loaded");

        web3j= Web3j.build(new HttpService(NODE_HTTP_ADDRESS));
        LOGGER.info("blockchain","Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        decentralizedDB=DecentralizedDB.load(CONTRACT_ADDRESS,web3j,credentials,ManagedTransaction.GAS_PRICE,Contract.GAS_LIMIT);
        LOGGER.info("contract","contract address"+decentralizedDB.getContractAddress());
        if(decentralizedDB.isValid()){
            LOGGER.info("contract","contract loaded ");

            LOGGER.info("contract","contract address"+decentralizedDB.getContractAddress());
        }
   }

    public void buyToken(BigInteger weiAmount){
        decentralizedDB.buyToken(weiAmount).sendAsync();
    }
    public BigInteger getTokenBalance() throws Exception {
        return decentralizedDB.balanceOf(credentials.getAddress()).send();
    }
    public  BigInteger getEthereumBalance() throws IOException {
        Request<?, EthGetBalance> getBalanceRequest = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST);
        EthGetBalance ethGetBalance = getBalanceRequest.send();
        LOGGER.info("contract","Balance of wallet"+ethGetBalance.getBalance());
        return  ethGetBalance.getBalance();

    }

    public void getChunkDataOfMainFile(byte[] chunkhash) throws Exception {
        Tuple5<String , byte[], Boolean, Boolean,BigInteger> chunkData=decentralizedDB.getChunckDataOfMainFile(chunkhash).sendAsync().get();
        String  nodeAddress=chunkData.getValue1();
        byte[] chunkHash=chunkData.getValue2();
        Boolean clientVerify=chunkData.getValue3();
        Boolean nodeVerify=chunkData.getValue4();
        BigInteger timeStamp=chunkData.getValue5();
    }
    public BigInteger getUserId() throws ExecutionException, InterruptedException {
       return decentralizedDB.getUserId().sendAsync().get();
    }
}
