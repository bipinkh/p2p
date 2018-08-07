package com.soriole.kademlia.service;

import com.soriole.dfsnode.blockchain.BlockchainCred;
import com.soriole.dfsnode.blockchain.service.BlockchainService;
import com.soriole.kademlia.core.KademliaConfig;
import com.soriole.kademlia.core.KademliaExtendedDHT;
import com.soriole.kademlia.core.store.Key;
import com.soriole.kademlia.core.store.NodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Random;

@Service("kademliaService")
public class KademliaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KademliaService.class);

    @Value("${local.key:0}")
    public String localKeyValue;
    @Value("${local.address.ip}")
    public String localIp;
    @Value("${local.address.port:0}")
    public int localPort;

    @Value("${bootstrap.key}")
    public String bootstrapKeyValue;
    @Value("${bootstrap.address.ip}")
    public String bootstrapIp;
    @Value("${bootstrap.address.port}")
    public int bootstrapPort;

    @Value("${bootstrap}")
    boolean isBoostrap;

    @Value("${kademlia.bucket.size}")
    public int bucketSize;

    @Value("${dfs.params.privatekey}")
    public String privateKey;


    private KademliaExtendedDHT kademliaDHT;

    @Autowired
    private PersistedStorageService storageService;

    @PostConstruct
    public void init() throws SocketException {
        // load contract
        BlockchainCred.setBlockchainService(new BlockchainService());
        BlockchainCred.getBlockchainService().loadCredFromPrivKey(privateKey);
        System.out.println("Loaded Credentials");
        try {
            BlockchainCred.getBlockchainService().loadContract();
            System.out.println("Loaded Contract");
        } catch (Exception e) {
            System.out.println("Loading Contract failed");
            e.printStackTrace();
        }


        // if the key is zero create a random key.
        Key localKey = new Key(localKeyValue);
        if (localKey.equals(new Key("0"))) {
            byte[] info = new byte[20];
            new Random().nextBytes(info);
            localKey = new Key(info);

        }
        if (localKeyValue.equals(bootstrapKeyValue)) {
            localPort = bootstrapPort;
        }


        KademliaConfig.Builder configBuilder = KademliaConfig.newBuilder();
        configBuilder.setKadeliaProtocolPort(localPort);
        configBuilder.setK(bucketSize);


        // create kademliaExtendedDHT Instance using the autowired storageService
        kademliaDHT = new KademliaExtendedDHT(localKey, storageService, configBuilder.build());
        kademliaDHT.start();
        if (this.isBoostrap) {
            return;
        }
        if (!localKeyValue.equals(bootstrapKeyValue)) {
            for (int i = 0; i < 4; i++) {
                if (kademliaDHT.join(new NodeInfo(new Key(bootstrapKeyValue), new InetSocketAddress(bootstrapIp, bootstrapPort)))) {
                    return;
                }

            }
            throw new RuntimeException("Cannot Connect with Bootstrap node");
        }

    }

    // returns ID of the node subscribed by the client

    public KademliaExtendedDHT getDHT() {
        return kademliaDHT;
    }

    @PreDestroy
    public void destroy() {
        try {
            kademliaDHT.shutDown(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void main() {
        NodeInfo myNodeInfo = new NodeInfo(new Key("122345"), null, null);


    }
}
