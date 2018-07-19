package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public interface UserSubscriptionRepo extends JpaRepository<UserSubscription,Long> {
}
