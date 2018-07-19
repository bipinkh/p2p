package com.filestorage.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Renewals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    UserSubscription userSubscription;

    private int status;     //pending or completed

    private Timestamp renewedTime;

    private String transactionHash;

}
