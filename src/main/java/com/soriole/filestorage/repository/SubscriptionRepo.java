package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public interface SubscriptionRepo extends JpaRepository<Subscription,Long> {

    List<Subscription> findByStatus(int active);
}
