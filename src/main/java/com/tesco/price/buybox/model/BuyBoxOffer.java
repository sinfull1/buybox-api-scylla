package com.tesco.price.buybox.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table("buybox_offers")

public class BuyBoxOffer {
    public BuyBoxOffer(BuyBoxOfferKey buyBoxOfferKey, BigDecimal price, LocalDateTime lastUpdated, Map<String, String> tags) {
        this.buyBoxOfferKey = buyBoxOfferKey;
        this.price = price;
        this.lastUpdated = lastUpdated;
        this.tags = tags;
    }

    @PrimaryKey
    private BuyBoxOfferKey buyBoxOfferKey;
    private BigDecimal price;

    public void setBuyBoxOfferKey(BuyBoxOfferKey buyBoxOfferKey) {
        this.buyBoxOfferKey = buyBoxOfferKey;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    private LocalDateTime lastUpdated;
    private Map<String, String> tags;

    public BuyBoxOfferKey getBuyBoxOfferKey() {
        return buyBoxOfferKey;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
