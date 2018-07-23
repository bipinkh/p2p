package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.File;
import com.soriole.filestorage.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

public interface FileRepo extends JpaRepository<File,Long> {

    Optional<File> findByFileHashAndUser(String hash, User user);
}
