package com.tesco.price.buybox.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.*;

@PrimaryKeyClass
@Data
public class BuyBoxOfferKey implements Serializable {

    @PrimaryKeyColumn(name = "product_id", type = PARTITIONED)
    private String productId;

    @PrimaryKeyColumn(name = "location_id", type = PARTITIONED)
    private String locationId;

    @PrimaryKeyColumn(name = "seller_id", type = CLUSTERED,  ordering = Ordering.DESCENDING)
    private String sellerId;

    @PrimaryKeyColumn(name = "effective_at", type = CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime effectiveAt;

        // Constructors
    public BuyBoxOfferKey() {}

    public BuyBoxOfferKey(String productId, String sellerId, String locationId, LocalDateTime effectiveAt) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.locationId = locationId;
        this.effectiveAt = effectiveAt;
    }

}
