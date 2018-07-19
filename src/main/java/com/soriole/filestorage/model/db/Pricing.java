package com.filestorage.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.filestorage.model.dto.PricingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int coinType=1;       // Ethereum and other tokens

    private double price;       // amount of subscriptionPackage

    @ManyToOne
    @JsonBackReference
    Subscription subscriptionPackage;

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + id +
                ", coinType=" + coinType +
                ", price=" + price +
                '}';
    }

    public static Pricing fromPricingDto(PricingDto pricingDto) {
        Pricing pricing = new Pricing();
        pricing.setCoinType(pricingDto.getCoinType());
        pricing.setPrice(pricingDto.getPrice());
        return pricing;
    }
}
