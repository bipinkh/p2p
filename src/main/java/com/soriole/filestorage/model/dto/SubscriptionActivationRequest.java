package com.soriole.filestorage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author github.com/bipinkh
 * created on : 23 Jul 2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionActivationRequest {
    @JsonProperty("subscription_package_id")
    private String subscriptionPackId;
    @JsonProperty("user_key")
    private String userKey;
    @JsonProperty("payment_slip")
    private String payment_slip;    // payment slip is just a transaction hash

}
