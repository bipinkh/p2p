package com.soriole.dfsnode.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author github.com/bipinkh
 * created on : 03 Aug 2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeDetails {

    // counter from inception
    private long totalFileReceived;
    private long totalFileDownloaded;
    private double totalStorageProvided;


    // counter for user
    private long totalClients;


}
