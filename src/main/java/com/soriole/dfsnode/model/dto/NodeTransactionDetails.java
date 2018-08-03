package com.soriole.dfsnode.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 03 Aug 2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeTransactionDetails {

    //tokens
    private Integer availableTokens;
    private Integer lockedDepositTokens;
    private Integer lockedEarningTokens;

    //transactions
    private List<TransactionDto> transactions;
}
