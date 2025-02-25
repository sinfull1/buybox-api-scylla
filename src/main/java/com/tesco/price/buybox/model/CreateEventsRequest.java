package com.tesco.price.buybox.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class CreateEventsRequest {
    private String productId;
    private String sellerId;
    private String locationId;
    private BigDecimal price;
    private LocalDateTime effectiveAt;
    private Integer stock;
    private Float rating;
    private Map<String, String> tags;
}
