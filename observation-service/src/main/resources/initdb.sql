
DROP TABLE observations;

CREATE TABLE observations (
    location geometry(POINTZ,4326) NOT NULL,
    obs_timestamp timestamp NOT NULL,
    device_id varchar(255) NOT NULL,    
    sensor_id integer,
    hdop real,
    vdop real,
    obs_type varchar(64) NOT NULL,
    obs_mode varchar(64) NOT NULL,
    value0 real,
    value1 real,
    value2 real,
    value3 real,
    value4 real
);


