package com.soriole.dfsnode.service;

import com.soriole.dfsnode.exceptions.UserNotFound;
import com.soriole.dfsnode.model.db.Client;
import com.soriole.dfsnode.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    public boolean ifUserExists(String userKey){
        Optional<Client> optionalClient = clientRepository.findByClientPublicKey(userKey);
        if (!optionalClient.isPresent())
            return false;
        return true;
    }

    public Client verifyUserExists(Long userId){
        Optional<Client> optionalClient = clientRepository.findById(userId);
        if (!optionalClient.isPresent())
            throw new UserNotFound(userId);
        return optionalClient.get();
    }
}
