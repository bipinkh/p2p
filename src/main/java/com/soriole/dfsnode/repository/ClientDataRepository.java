package com.soriole.dfsnode.repository;

import com.soriole.dfsnode.model.db.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
public interface ClientDataRepository extends JpaRepository<ClientData, Long> {
}
