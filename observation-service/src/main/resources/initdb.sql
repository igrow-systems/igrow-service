
DROP TABLE observations;

CREATE TABLE observations (
    location geography(POINTZ,4326),
    obs_timestamp timestamp NOT NULL,
    hdop real,
    vdop real,
    device_id bigint,
    obs_type int,
    value0 real,
    value1 real,
    value2 real,
    value3 real,
    value4 real
);


