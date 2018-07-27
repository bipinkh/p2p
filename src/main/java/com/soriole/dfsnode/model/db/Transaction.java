package com.soriole.dfsnode.model.db;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp timestamp;

    private TransactionTypeEnum transactionsType;

    private String sender;

    private String receiver;

    private String fileHash;

    private Integer tokenAmountReceived;

    private Integer tokenAmountDeducted;

}
