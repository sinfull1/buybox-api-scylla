package com.tesco.price.buybox.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@PrimaryKeyClass
@Data
public class BuyBoxOfferKey implements Serializable {

    @PrimaryKeyColumn(name = "product_id", type = PARTITIONED)
    private String productId;

    @PrimaryKeyColumn(name = "location_id", type = PARTITIONED)
    private String locationId;

    @PrimaryKeyColumn(name = "seller_id", type = CLUSTERED, ordering = Ordering.DESCENDING)
    private String sellerId;

    @PrimaryKeyColumn(name = "effective_at", type = CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime effectiveAt;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public LocalDateTime getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(LocalDateTime effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    // Constructors
    public BuyBoxOfferKey() {
    }

    public BuyBoxOfferKey(String productId, String locationId, LocalDateTime effectiveAt, String sellerId) {
        this.productId = productId;
        this.locationId = locationId;
        this.effectiveAt = effectiveAt;
        this.sellerId = sellerId;
    }

}
