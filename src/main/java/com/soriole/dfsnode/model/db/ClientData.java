package com.soriole.dfsnode.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Integer currentDownloadCount;   // download count in this last renewed period

    private Integer totalDownloadCount;


}
