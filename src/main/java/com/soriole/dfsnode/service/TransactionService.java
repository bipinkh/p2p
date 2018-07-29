package com.soriole.dfsnode.service;

import com.soriole.dfsnode.model.db.Transaction;
import com.soriole.dfsnode.model.db.TransactionTypeEnum;
import com.soriole.dfsnode.model.dto.TransactionDto;
import com.soriole.dfsnode.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.soriole.dfsnode.Constants.not_applicable;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public TransactionDto txnFileUpload(String clientKey, String filehash){
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        Transaction transaction =
                transactionRepository.save(
                new Transaction(
                    null,
                    currentTimeStamp,
                    TransactionTypeEnum.UPLOAD,
                    clientKey,
                    not_applicable,
                    filehash,
                    not_applicable,
                    not_applicable
                )
            );
        System.out.println("new transaction : "+transaction.toString());
        return TransactionDto.fromTransaction(transaction);
    }


    public TransactionDto txnFileDownload(String clientPublicKey, String fileHash) {
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        Transaction transaction =
                transactionRepository.save(
                        new Transaction(
                                null,
                                currentTimeStamp,
                                TransactionTypeEnum.DOWNLOAD,
                                not_applicable,
                                clientPublicKey,
                                fileHash,
                                not_applicable,
                                not_applicable
                        )
                );
        System.out.println("new transaction : "+transaction.toString());
        return TransactionDto.fromTransaction(transaction);
    }

    public TransactionDto txnSubsRenew(String clientPublicKey, String fileHash) {
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        Transaction transaction =
                transactionRepository.save(
                        new Transaction(
                                null,
                                currentTimeStamp,
                                TransactionTypeEnum.RENEW,
                                clientPublicKey,
                                not_applicable,
                                fileHash,
                                not_applicable,
                                not_applicable
                        )
                );
        System.out.println("new transaction : "+transaction.toString());
        return TransactionDto.fromTransaction(transaction);
    }

    public List<TransactionDto> getAllTransactions(String clientPublicKey){
        List<TransactionDto> dtos = new ArrayList<>();
        List<Transaction> txns = transactionRepository.findAllBySenderOrReceiver(clientPublicKey, clientPublicKey);
        for (Transaction txn : txns){
            dtos.add(TransactionDto.fromTransaction(txn));
        }
        return dtos;
    }
}
