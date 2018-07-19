package com.soriole.filestorage.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.soriole.filestorage.model.db.Pricing;
import com.soriole.filestorage.model.db.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    @JsonProperty("subscription_id")
    private Long subsId;
    @JsonProperty("subscription_status")
    private int status;
    @JsonProperty("package_name")
    private String packageName;
    @JsonProperty("size_in_mb")
    private double sizeProvided;
    @JsonProperty("coverage_time")
    private int timeSpan;
    @JsonProperty("bandwidth_provided")
    private double bandwidthProvided;
    @JsonProperty("pricings")
    List<PricingDto> pricings;

    public static SubscriptionDto fromSubscription(Subscription subs){
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                subs.getId(),
                subs.getStatus(),
                subs.getPackageName(),
                subs.getSizeProvided(),
                subs.getTimeSpan(),
                subs.getBandwidthProvided(),
                new ArrayList<PricingDto>()
        );
        List<Pricing> pricings = subs.getPricings();
        List<PricingDto> priceDtos = new ArrayList<PricingDto>();
        for (Pricing price : pricings){
            priceDtos.add( PricingDto.fromPricing(price) );
        }
        subscriptionDto.setPricings(priceDtos);
        return subscriptionDto;
    }

}
