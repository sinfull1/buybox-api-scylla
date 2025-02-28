CREATE KEYSPACE IF NOT EXISTS buybox WITH replication = { 'class': 'NetworkTopologyStrategy', 'replication_factor': '1' };
DROP TABLE buybox.sellers;
CREATE TABLE IF NOT EXISTS buybox.sellers
(
    seller_id   text,
    seller_name text,
    PRIMARY KEY (seller_id)
);
DROP TABLE buybox.products;
CREATE TABLE IF NOT EXISTS buybox.products
(
    product_id   text,
    product_name text,
    PRIMARY KEY (product_id)
);

DROP TABLE buybox.events;
CREATE TABLE IF NOT EXISTS buybox.events
(
    event_id       uuid,
    seller_id      text,
    product_id     text,
    modifiers      map<text, text>,
    price          double,
    effective_from timestamp,
    PRIMARY KEY (event_id, effective_from)
);
DROP MATERIALIZED VIEW buybox.submissions;
CREATE MATERIALIZED VIEW IF NOT EXISTS buybox.submissions AS
SELECT *
FROM buybox.events
WHERE event_id IS NOT null
  AND seller_id IS NOT null
  AND effective_from IS NOT null
PRIMARY KEY ((seller_id), effective_from, event_id)
WITH CLUSTERING ORDER BY (effective_from DESC);

DROP TABLE buybox.product_buybox;
CREATE TABLE IF NOT EXISTS buybox.product_buybox
(
    event_id   uuid,
    product_id text,
    seller_id  text,
    modifiers  map<text, text>,
    price      double,
    created_at timestamp,
    PRIMARY KEY ((product_id, modifiers), price)
) WITH CLUSTERING ORDER BY (price DESC);

