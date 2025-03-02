package com.tesco.price.buybox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.github.javafaker.Faker;
import com.tesco.price.buybox.model.BuyBoxOffer;
import com.tesco.price.buybox.model.BuyBoxOfferKey;
import com.tesco.price.buybox.model.LatestOffers;
import com.tesco.price.buybox.model.SearchProducts;
import com.tesco.price.buybox.repository.BuyBoxOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buybox")
@CrossOrigin(origins = "*")
public class ApiController {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    Map<String, BigDecimal> productBasePrices = Map.of(
            "LAPTOP_GAMING", BigDecimal.valueOf(1299.99),
            "SMARTPHONE_PRO", BigDecimal.valueOf(999.99),
            "WIRELESS_HEADPHONES", BigDecimal.valueOf(199.99),
            "BLUETOOTH_SPEAKER", BigDecimal.valueOf(149.99),
            "MECHANICAL_KEYBOARD", BigDecimal.valueOf(129.99),
            "ULTRAWIDE_MONITOR", BigDecimal.valueOf(499.99),
            "SMARTWATCH_ELITE", BigDecimal.valueOf(349.99),
            "GRAPHICS_CARD", BigDecimal.valueOf(799.99),
            "EXTERNAL_SSD", BigDecimal.valueOf(179.99),
            "GAMING_MOUSE", BigDecimal.valueOf(79.99)
    );
    List<String> productNames = List.of(
            "LAPTOP_GAMING",
            "SMARTPHONE_PRO",
            "WIRELESS_HEADPHONES",
            "BLUETOOTH_SPEAKER",
            "MECHANICAL_KEYBOARD",
            "ULTRAWIDE_MONITOR",
            "SMARTWATCH_ELITE",
            "GRAPHICS_CARD",
            "EXTERNAL_SSD",
            "GAMING_MOUSE"
    );
    List<String> sellerNames = List.of(
            "TECH_WORLD",
            "GADGET_HUB",
            "ELECTRO_STORE",
            "MEGA_RETAIL",
            "DIGITAL_MART",
            "SMART_TECH",
            "HYPER_SHOP",
            "INNOVATE_GEAR",
            "FAST_BUY",
            "TRENDY_TECH"
    );
    List<String> locationNames = List.of(
            "NEW_YORK_WAREHOUSE",
            "LOS_ANGELES_HUB",
            "CHICAGO_DISTRIBUTION",
            "HOUSTON_DEPOT",
            "MIAMI_CENTRAL",
            "SAN_FRANCISCO_STORE",
            "SEATTLE_OUTLET",
            "DALLAS_SUPPLY",
            "BOSTON_SHIPPING",
            "ATLANTA_STORAGE"
    );

    public BuyBoxOffer generateRandomOffer() {
        int index = random.nextInt(productBasePrices.size());
        String productId = productNames.get(index);
        String sellerId = sellerNames.get(random.nextInt(sellerNames.size()));
        String locationId = locationNames.get(random.nextInt(locationNames.size()));
        BigDecimal price = productBasePrices.get(productId).add(BigDecimal.valueOf(random.nextDouble(productBasePrices.get(productId).intValue() / 10)).setScale(2, RoundingMode.CEILING)); // Price between 0 and 500
        LocalDateTime effectiveAt = LocalDateTime.now().plusSeconds(random.nextInt(300)); // Up to 30 days in the future
        Map<String, String> tags = new HashMap<>();
        tags.put("color", faker.color().name());
        tags.put("size", faker.options().option("S", "M", "L", "XL"));
        tags.put("category", faker.commerce().department());
        return new BuyBoxOffer(new BuyBoxOfferKey(productId, locationId, effectiveAt, sellerId), price, LocalDateTime.now(), tags);
    }

    Comparator<BuyBoxOffer> comparator = Comparator.comparing(BuyBoxOffer::getPrice);

    @Autowired
    private BuyBoxOfferRepository buyBoxOfferRepository;

    @PostMapping("/offer")
    @CrossOrigin(origins = "*")
    public BuyBoxOffer getEvents() {
        return buyBoxOfferRepository.save(generateRandomOffer());
    }


    @GetMapping("/winner/{productId}/{locationId}")
    @CrossOrigin(origins = "*")
    public TreeSet<BuyBoxOffer> getWinners(@PathVariable String productId, @PathVariable String locationId) {
        List<LatestOffers> latestOffers = buyBoxOfferRepository.findLatestEffectiveAtPerSeller(productId, locationId);
        TreeSet<BuyBoxOffer> offers = new TreeSet<>(comparator);
        for (LatestOffers latestOffer : latestOffers) {
            BuyBoxOffer buyBoxOffer = buyBoxOfferRepository.findOfferByEffectiveAt(productId, locationId, latestOffer.getBuyBoxOfferKey().getSellerId(), latestOffer.getBuyBoxOfferKey().getEffectiveAt()).get();
            offers.add(buyBoxOffer);
        }
        return offers;
    }


    @GetMapping("/offers/{productId}/{locationId}")
    @CrossOrigin(origins = "*")
    public String getOffers(@PathVariable String productId, @PathVariable String locationId) throws JsonProcessingException {
        List<BuyBoxOffer> buyBoxOffers = buyBoxOfferRepository.fetchAllCurrentOffersByProductLocation(productId, locationId, LocalDateTime.now());
        return transformed(buyBoxOffers);

    }


    @GetMapping("/seller/{sellerId}/{locationId}")
    @CrossOrigin(origins = "*")
    public List<BuyBoxOffer> getSeller(@PathVariable String sellerId, @PathVariable String locationId) throws JsonProcessingException {
        List<BuyBoxOffer> offers;
        offers = buyBoxOfferRepository.findOfferBySellerId(sellerId, locationId, LocalDateTime.now());

        return offers;
    }

    @GetMapping("/product/{productId}")
    @CrossOrigin(origins = "*")
    public Set<String> searchProduct(@PathVariable String productId) {
        HashSet<String> products = new HashSet<>();
        buyBoxOfferRepository.searchAllByProduct("%"+productId.toUpperCase()+"%").forEach(x-> products.add(x.getBuyBoxOfferKey().getProductId()));
        return products;
    }

    @GetMapping("/location/{locationId}")
    @CrossOrigin(origins = "*")
    public Set<String> searchLocation(@PathVariable String locationId) {
        HashSet<String> location = new HashSet<>();
        buyBoxOfferRepository.searchAllByLocation("%" + locationId.toUpperCase()+"%").forEach(x-> location.add(x.getBuyBoxOfferKey().getLocationId()));
        return location;
    }


    private String transformed(List<BuyBoxOffer> offers) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        JsonNode rootNode = objectMapper.readTree(objectMapper.writeValueAsString(offers));

        // Group data by lastUpdated timestamp
        Map<String, Map<String, Double>> groupedData = new TreeMap<>();
        Map<String, Double> sellers = new HashMap<>();

        for (Iterator<JsonNode> it = rootNode.elements(); it.hasNext(); ) {
            JsonNode node = it.next();
            JsonNode lastUpdated = node.get("buyBoxOfferKey").get("effectiveAt");
            String time = lastUpdated.get(3).toString() + ":" + lastUpdated.get(4) + ":" + lastUpdated.get(5);
            String sellerId = node.get("buyBoxOfferKey").get("sellerId").asText();
            double price = node.get("price").asDouble();
            if (sellers.containsKey(sellerId)) {

            } else {
                sellers.put(sellerId, price);
            }
            groupedData.computeIfAbsent(time, k -> new HashMap<>()).put(sellerId, price);
        }

        // Convert to desired format
        List<ObjectNode> transformedList = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Map<String, Double>> entry : groupedData.entrySet()) {
            count++;
            ObjectNode transformedNode = objectMapper.createObjectNode();
            transformedNode.put("lastUpdated", entry.getKey());
            for (Map.Entry<String, Double> sell : sellers.entrySet()) {
                if (entry.getValue().containsKey(sell.getKey())) {
                    transformedNode.put(sell.getKey(), entry.getValue().get(sell.getKey()));

                } else {
                    transformedNode.put(sell.getKey(), sellers.get(sell.getKey()));

                }
            }
            transformedList.add(transformedNode);
            if (count == 25) {
                break;
            }
        }

        // Print the transformed JSON
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transformedList);

    }

}
