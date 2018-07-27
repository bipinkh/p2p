package com.soriole.dfsnode.repository;

import com.soriole.dfsnode.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientPublicKey(String publicKey);
}
