
DROP TABLE devices;

CREATE TABLE devices (
    device_id  varchar(255) NOT NULL,    
    os_type    varchar(32), 
    os_version varchar(64),
    push_token varchar(255)
);


