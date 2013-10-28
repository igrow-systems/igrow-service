
DROP TABLE observations;

CREATE TABLE observations (
    location geography(POINTZ,4326) NOT NULL,
    obs_timestamp timestamp NOT NULL,
    device_id bigint NOT NULL,    
    hdop real,
    vdop real,
    obs_type smallint,
    value0 real,
    value1 real,
    value2 real,
    value3 real,
    value4 real
);


