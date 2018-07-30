package com.soriole.dfsnode.service;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author github.com/bipinkh
 * created on : 29 Jul 2018
 */
public class HashService {
    public static String hash(String message, String algorithm){
        String encodedHashString = null;
        try{
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashValue = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            encodedHashString = Base64.encodeBase64String(hashValue);
            return encodedHashString;
        }
        catch(Exception e){
            System.out.println("Exception occured during hashing ::: "+e);
        }
        return encodedHashString;
    }
}
