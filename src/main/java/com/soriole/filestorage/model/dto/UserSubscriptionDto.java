package com.soriole.filestorage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soriole.filestorage.model.db.Renewals;
import com.soriole.filestorage.model.db.Subscription;
import com.soriole.filestorage.model.db.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDto {

    private boolean isActive;
    @JsonProperty("started_date")
    private Timestamp startedDate;
    @JsonProperty("ending_date")
    private Timestamp endingDate;
    @JsonProperty("renewals")
    private List<Renewals> renewals;
    @JsonProperty("size_consumed")
    private double sizeConsumed;
    @JsonProperty("bandwidth_consumed")
    private double bandwidthConsumed;
    @JsonProperty("subscriber_user_id")
    private Long userId;
    @JsonProperty("subscription_package_details")
    Subscription subscriptionPackage;

    public static UserSubscriptionDto fromUserSubscription(UserSubscription subscription) {
        return new UserSubscriptionDto(
                subscription.isActiveStatus(),
                subscription.getStartedDate(),
                subscription.getEndingDate(),
                subscription.getRenewals(),
                subscription.getSizeConsumed(),
                subscription.getBandwidthConsumed(),
                subscription.getUser().getId(),
                subscription.getSubscriptionPackage()
        );
    }
}
