package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByUserKey(String key);
}
