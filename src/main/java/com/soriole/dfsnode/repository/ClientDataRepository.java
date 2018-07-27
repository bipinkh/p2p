package com.soriole.dfsnode.repository;

import com.soriole.dfsnode.model.db.Client;
import com.soriole.dfsnode.model.db.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {

    Optional<ClientData> findByFileHash(String hash);
}
