
DROP TABLE IF EXISTS observations;

CREATE TABLE observations (
    obs_timestamp timestamptz           NOT NULL,
    location      geometry(POINTZ,4326) NOT NULL,
    device_id     varchar(255),    
    sensor_id     varchar(255) NOT NULL,
    hdop          real,
    vdop          real,
    obs_type      varchar(64) NOT NULL,
    obs_mode      varchar(64) NOT NULL,
    value0        real,
    value1        real,
    value2        real,
    value3        real,
    value4        real
);

-- Then we convert it into a hypertable that is partitioned by time
SELECT create_hypertable('observations', 'obs_timestamp', 'location', 4);

