DROP DATABASE IF EXISTS proiectMES;
CREATE DATABASE IF NOT EXISTS proiectMES;
use proiectMES;

DROP TABLE IF EXISTS note;
CREATE TABLE note (
	id int not null auto_increment primary key,
    frecventa decimal(5, 2),
    notatie_muz varchar(5),
    notatie_uzuala varchar(5),
    octava varchar(20));