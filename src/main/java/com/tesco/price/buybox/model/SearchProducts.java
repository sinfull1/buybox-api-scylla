package com.tesco.price.buybox.model;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface SearchProducts {
    @Value("#{target.product_id}")
    public String getProductId();

    @Value("#{target.location_id}")
    public LocalDateTime getLocationId();
}
