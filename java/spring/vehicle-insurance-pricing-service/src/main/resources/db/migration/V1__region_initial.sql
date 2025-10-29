CREATE TABLE region
(
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    state       TEXT NOT NULL,
    district    TEXT NOT NULL,
    city        TEXT NOT NULL,
    postal_code TEXT NOT NULL,
    location    TEXT
);

CREATE INDEX idx_region_postal_code ON region (postal_code);


-- import region csv file
-- https://www.postgresql.org/docs/17/sql-copy.html
CREATE TEMP TABLE tmp_postcodes (
    iso_3166_1_alpha_2 TEXT,
    iso_3166_1_alpha_2_region_code TEXT,
    region1 TEXT,
    region2 TEXT,
    region3 TEXT,
    region4 TEXT,
    postleitzahl TEXT,
    ort TEXT,
    area1 TEXT,
    area2 TEXT,
    latitude TEXT,
    longitude TEXT,
    zeitzone TEXT,
    utc TEXT,
    sommerzeit TEXT,
    active TEXT
);
COPY tmp_postcodes
    FROM '/data/postcodes.csv'
    WITH (
    FORMAT csv,
    DELIMITER ',',
    HEADER
    );

-- transform and load into region table
-- https://www.postgresql.org/docs/17/sql-insert.html
INSERT INTO region(state, district, city, postal_code, location)
SELECT trim(region1)      AS state,
       trim(region3)      AS district,
       trim(region4)      AS city,
       trim(postleitzahl) AS postal_code,
       trim(ort)          AS location
FROM tmp_postcodes;
