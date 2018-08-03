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
    private Integer totalFileReceived;
    private Integer totalFileDownloaded;
    private Integer totalStorageProvided;

    // counter of active
    private Integer activeFileReceived;
    private Integer activeFileDownloaded;

    // counter for today
    private Integer todayFileReceived;
    private Integer todayFileDownloaded;

    // counter for user
    private Integer totalClients;
    private Integer activeClients;


}
