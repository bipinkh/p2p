package com.soriole.filestorage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeRequest {

    @JsonProperty("subscription_package_id")
    private String subscriptionPackId;
    @JsonProperty("time_in_months")
    private int month;
    @JsonProperty("user_key")
    private String userKey;

    //todo: implement the machanism for verifying request

}
