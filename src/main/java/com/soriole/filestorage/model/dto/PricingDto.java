package com.soriole.filestorage.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soriole.filestorage.model.db.Pricing;
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
public class PricingDto {
    @JsonProperty("coin_type")
    private int coinType;
    @JsonProperty("price")
    private double price;

    public static PricingDto fromPricing(Pricing price) {
        PricingDto dto = new PricingDto(
                price.getCoinType(),
                price.getPrice()
        );
        return dto;
    }
}
