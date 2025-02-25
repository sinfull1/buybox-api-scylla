package com.tesco.price.buybox.model;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LatestOffers {

    @Value("#{target.seller_id}")
    public String getSellerId();
    @Value("#{target.effective_at}")
    public LocalDateTime getEffectiveAt();
    public BuyBoxOfferKey getBuyBoxOfferKey();

}
