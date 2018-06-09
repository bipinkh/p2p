package com.endtoendmessenging.security.util;

import com.soriole.kademlia.core.store.Key;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;

import java.math.BigInteger;
import java.security.*;
import java.util.Random;

public class CryptographicKey {

    static ECDomainParameters secp25641Curve;

    // The CURVE_PARAMS for Neo wallet is secp25641
    static {
        X9ECParameters curveParams = CustomNamedCurves.getByName("secp256r1");
        if (curveParams == null) {
            throw new RuntimeException("Curve secp256r1 implementation not found");
        }
        secp25641Curve = new ECDomainParameters(
                curveParams.getCurve(), curveParams.getG(), curveParams.getN(), curveParams.getH());
    }

    byte[] publicKey;
    byte[] privateKey;

    private CryptographicKey(){

    }

    @Override
    public String toString() {
        if(privateKey!=null)
            WIF.encode(privateKey);
        return "";
    }

    public String publicKeyString(){
        return (new BigInteger(1,publicKey)).toString(16);
    }

    public static CryptographicKey getInstance(com.soriole.kademlia.core.store.Key key) {
        CryptographicKey cKey=new CryptographicKey();
        cKey.publicKey=key.toBytes();
        return cKey;
    }
    public static CryptographicKey getInstance(String privateKeyWif) throws WIF.InvalidWIFException {
        CryptographicKey cKey=new CryptographicKey();

        cKey.privateKey=WIF.decode(privateKeyWif);
        cKey.publicKey=secp25641Curve.getG().multiply(new BigInteger(1,cKey.publicKey)).getEncoded(true);
        return cKey;

    }
    public static CryptographicKey getInstance(byte[] seed){
        CryptographicKey ckey=new CryptographicKey();
        ckey.privateKey=new byte[32];
        if(seed==null){
            Random random=new SecureRandom();
            random.nextBytes(ckey.privateKey);
        }
        else{
            Random random=new SecureRandom(seed);
            random.nextBytes(ckey.privateKey);
        }
        ckey.publicKey=secp25641Curve.getG().multiply(new BigInteger(1,ckey.publicKey)).getEncoded(true);
        return ckey;
    }
    public byte[] getPublicKey(){
        return publicKey;
    }
    public com.soriole.kademlia.core.store.Key getKademliaKey(){
        return new Key(publicKey);
    }
}
