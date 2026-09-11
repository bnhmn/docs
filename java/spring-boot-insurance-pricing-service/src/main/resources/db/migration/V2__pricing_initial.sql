CREATE TABLE pricing
(
    id             UUID PRIMARY KEY,
    vehicle_type   TEXT           NOT NULL,
    postal_code    TEXT           NOT NULL,
    annual_mileage INT            NOT NULL,
    annual_premium NUMERIC(12, 2) NOT NULL
);
