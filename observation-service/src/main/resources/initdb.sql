

CREATE TABLE observations (
    device_id bigserial primary key,
    obs_timestamp timestamp NOT NULL,
    article_desc text NOT NULL,
    date_added timestamp default NULL
);


