package com.soriole.dfsnode.repository;

import com.soriole.dfsnode.model.db.Client;
import com.soriole.dfsnode.model.db.ClientData;
import org.hibernate.sql.Select;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;


/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {

    Optional<ClientData> findByFileHash(String hash);
    List<ClientData> findAllByClient(Client client);
    Optional<ClientData> findByFileHashAndClient(String fileHash, Client client);
    @NotNull
    List<ClientData> findAll();

    Integer countDistinctByEndingDateBefore(Timestamp today);
    Integer countDistinctByClient();

    Integer countDistinctByFileHash();
    Integer countDistinctByClientAndEndingDateBefore(Timestamp today);
}
