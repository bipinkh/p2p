package com.soriole.dfsnode.exceptions;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
public class UserNotFound extends RuntimeException {
    public UserNotFound(String key){
        super("User with given userKey " + key + " cannot be found !");
    }

    public UserNotFound(Long id){
        super("User with given userKey " + id + " cannot be found !");
    }
}
