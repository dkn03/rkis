CREATE DATABASE RKIS;

\connect rkis;

CREATE TABLE IF NOT EXISTS perfumery
(
    id            serial
        primary key,
    type          varchar,
    color         varchar,
    aroma         varchar,
    volume        integer,
    concentration double precision,
    constraint perfumery_check
        CHECK ((volume > 0) AND ((0)::double precision < concentration) AND (concentration < (1)::double precision))
);

CREATE TABLE IF NOT EXISTS users
(
    id serial PRIMARY KEY,
    username varchar UNIQUE,
    password varchar NOT NULL,
    roles varchar ARRAY NOT NULL
);
