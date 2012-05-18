create table zappar AS
SELECT "key", ST_GeomFromWKB(geom,99999) from "data" 
where layer='zappar' group by key, geom ;

create table kladpar AS
SELECT "key", ST_GeomFromWKB(geom,99999) from "data"
where layer='kladpar' group by key, geom;

CREATE TABLE kladpar_popisky_sep
(
   "key" bigint PRIMARY KEY, 
   parcela text NOT NULL, 
   vymera integer NOT NULL, 
   druh_p text NOT NULL, 
   vyuzitie_p text NOT NULL, 
   cislo_lv integer
);

create table kladpar2
as 
select K.key, geom,parcela1, parcela2, vymera, 
  (select id from kladpar_druh_pozemku D where D.name = druh_p) as druh_p,
  (select id from kladpar_vyuzitie_pozemku D where D.name = vyuzitie_p) as vyuzitie_p,
  cislo_lv

from kladpar K, kladpar_popisky_sep KS
where K.key = KS.key;

create table zappar2
as
select
key,geom,
(select id from zappar_druh ZD where name=
(select name from zappar_popisky ZP where ZP.key = Z.key))
from zappar Z;