/*DROP TABLE IF EXISTS Account;*/

CREATE TABLE IF NOT EXISTS Account (
id serial PRIMARY KEY,
username varchar (20) UNIQUE NOT NULL,
password varchar (250) NOT NULL,
"role" int4 DEFAULT 0 NOT NULL,
active boolean DEFAULT TRUE NOT NULL
);
/*CREATE TABLE IF NOT EXISTS Profile (

);*/