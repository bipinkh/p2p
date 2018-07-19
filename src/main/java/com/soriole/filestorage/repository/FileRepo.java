package com.filestorage.repository;

import com.filestorage.model.db.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

public interface FileRepo extends JpaRepository<File,Long> {
}
