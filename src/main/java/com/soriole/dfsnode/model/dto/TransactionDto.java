package com.soriole.dfsnode.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soriole.dfsnode.model.db.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author github.com/bipinkh
 * created on : 27 Jul 2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @JsonProperty("time")
    private Timestamp timestamp;
    @JsonProperty("txn_type")
    private String transactionsType;
    @JsonProperty("sender_address")
    private String sender;
    @JsonProperty("receiver_address")
    private String receiver;
    @JsonProperty("file_hash")
    private String fileHash;
    @JsonProperty("token_received")
    private String tokenAmountReceived;
    @JsonProperty("token_deducted")
    private String tokenAmountDeducted;

    public static TransactionDto fromTransaction(Transaction txn){
        return new TransactionDto(
                txn.getTimestamp(),
                txn.getTransactionsType().toString(),
                txn.getSender(),
                txn.getReceiver(),
                txn.getFileHash(),
                txn.getTokenAmountReceived(),
                txn.getTokenAmountDeducted()
        );
    }
}
