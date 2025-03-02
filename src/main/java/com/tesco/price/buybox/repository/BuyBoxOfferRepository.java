package com.tesco.price.buybox.repository;

import com.tesco.price.buybox.model.BuyBoxOffer;
import com.tesco.price.buybox.model.BuyBoxOfferKey;
import com.tesco.price.buybox.model.LatestOffers;
import com.tesco.price.buybox.model.SearchProducts;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface BuyBoxOfferRepository extends CassandraRepository<BuyBoxOffer, BuyBoxOfferKey> {
    @Query("SELECT seller_id, max(effective_at) AS effective_at " +
            "FROM buybox.buybox_offers " +
            "WHERE product_id = ?0 AND location_id = ?1 group by seller_id")
    List<LatestOffers> findLatestEffectiveAtPerSeller(String productId, String locationId);

    // Step 2: Fetch price using latest effective_at
    @Query("SELECT * FROM buybox.buybox_offers " +
            "WHERE product_id = ?0 AND location_id = ?1 AND seller_id = ?2 AND effective_at = ?3")
    Optional<BuyBoxOffer> findOfferByEffectiveAt(String productId, String locationId, String sellerId, LocalDateTime effectiveAt);



    @Query("select distinct product_id,location_id from buybox.buybox_offers where product_id like ?0  ALLOW FILTERING ")
    List<BuyBoxOffer> searchAllByProduct(String productIdKey);

    @Query("select distinct product_id,location_id from buybox.buybox_offers where location_id like ?0  ALLOW FILTERING ")
    List<BuyBoxOffer> searchAllByLocation(String locationIdKey);

    @Query("SELECT * FROM buybox.buybox_offers " +
            "WHERE product_id = ?0 AND location_id = ?1  ")
    List<BuyBoxOffer > fetchAllCurrentOffersByProductLocation(String productId, String locationId, LocalDateTime effectiveAt);

    // Step 2: Fetch price using latest effective_at
    @Query("SELECT * FROM buybox.buybox_offers " +
            "WHERE seller_id = ?0 AND location_id = ?1 AND effective_at <= ?2 ALLOW FILTERING")
    List<BuyBoxOffer> findOfferBySellerId(String sellerId, String locationId, LocalDateTime effectiveAt);


}