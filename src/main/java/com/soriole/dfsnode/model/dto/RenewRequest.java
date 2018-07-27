package com.soriole.dfsnode.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@Data
@AllArgsConstructor
public class RenewRequest {
    String userKey;
    String filehash;
}
