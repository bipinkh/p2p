package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.Renewals;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author github.com/bipinkh
 * created on : 23 Jul 2018
 */
public interface RenewalsRepo extends JpaRepository<Renewals, Long> {
}
