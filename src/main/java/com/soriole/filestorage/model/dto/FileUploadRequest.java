package com.filestorage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {

    String userKey;
    MultipartFile[] files;

}
