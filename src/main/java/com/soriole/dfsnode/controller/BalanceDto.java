package com.soriole.dfsnode.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author github.com/bipinkh
 * created on : 07 Aug 2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class BalanceDto {
    @JsonProperty("tokenBalance")
    double tokenBalance;
    @JsonProperty("ethBalance")
    double ethBalance;

}