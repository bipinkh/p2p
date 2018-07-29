package com.soriole.dfsnode.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientData {


    @Value("${dfs.params.totalDownloads}")
    public static int max_download_for_one_renewal;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Client client;

    private String fileHash;

    private String fileDataPath;

    private Timestamp renewedDate;

    private Timestamp endingDate;

    private Integer currentDownloadCount = 0;   // download count in this last renewed period

    private Integer totalDownloadCount = 0;


}
