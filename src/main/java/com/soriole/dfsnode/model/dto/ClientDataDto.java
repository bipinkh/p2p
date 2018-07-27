package com.soriole.dfsnode.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soriole.dfsnode.model.db.ClientData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@Data
@AllArgsConstructor
public class ClientDataDto {

    @Value("${dfs.params.totalDownloads}")
    private static int total_download;

    @JsonProperty("file_hash")
    private String fileHash;
    @JsonProperty("renewed_date")
    private Timestamp renewedDate;
    @JsonProperty("ending_date")
    private Timestamp endingDate;
    @JsonProperty("current_download_count")
    private Integer currentDownloadCount;
    @JsonProperty("remaining_download_count")
    private Integer remainingDownloadCount;
    @JsonProperty("total_download_count")
    private Integer totalDownloadCount;


    public static ClientDataDto fromClientData(ClientData clientData) {
        return new ClientDataDto(clientData.getFileHash(),
                clientData.getRenewedDate(),
                clientData.getEndingDate(),
                clientData.getCurrentDownloadCount(),
                total_download - clientData.getCurrentDownloadCount(),
                clientData.getTotalDownloadCount());
    }
}
