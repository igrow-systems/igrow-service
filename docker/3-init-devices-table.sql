
DROP TABLE IF EXISTS devices;

CREATE TABLE devices (
    device_id           varchar(255) NOT NULL,    
    os_type             varchar(64)  NOT NULL, 
    os_version          varchar(64)  NOT NULL,
    push_token          varchar(255),
    manufacturer        varchar(255) NOT NULL,
    model               varchar(255) NOT NULL,
    product             varchar(255) NOT NULL,
    device              varchar(255) NOT NULL,
    last_known_location geometry(POINTZ,4326)
);


