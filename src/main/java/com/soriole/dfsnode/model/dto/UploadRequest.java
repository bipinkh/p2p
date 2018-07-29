package com.soriole.dfsnode.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@Data
@AllArgsConstructor
public class UploadRequest {

    MultipartFile file;
    String userKey;
    String fileHash;

}
