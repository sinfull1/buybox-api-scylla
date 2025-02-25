package com.tesco.price.buybox.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
@Data
@AllArgsConstructor
@Table("buybox_offers")

public class BuyBoxOffer {

    @PrimaryKey
    private BuyBoxOfferKey buyBoxOfferKey;
    private BigDecimal price;
    private LocalDateTime lastUpdated;
    private Map<String, String> tags;
}
