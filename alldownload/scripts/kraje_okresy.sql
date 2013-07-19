begin transaction;

CREATE TABLE kraje
(
  key bigint NOT NULL,
  name text NOT NULL,
  PRIMARY KEY (key)
);
SELECT AddGeometryColumn( 'kraje', 'geom', 99999, 'POLYGON', 2);

CREATE TABLE okresy
(
  key bigint NOT NULL,
  name text NOT NULL,
  PRIMARY KEY (key)
);
SELECT AddGeometryColumn( 'okresy', 'geom', 99999, 'POLYGON', 2);

ALTER TABLE okresy DROP CONSTRAINT enforce_geotype_geom;
ALTER TABLE okresy
  ADD CONSTRAINT enforce_geotype_geom
  CHECK (geometrytype(geom) = 'POLYGON'::text OR
  geometrytype(geom) = 'MULTIPOLYGON'::text );

insert into kraje
select P.key, P.name, ST_SetSRID(geom, 99999) from popisky P , data D
  where P.layer= D.layer and P.key=D.key and P.layer='kraje'
  group by P.layer, P.key, P.name, geom;

insert into okresy
select P.key, P.name, ST_SetSRID(geom, 99999) from popisky P , data D
  where P.layer= D.layer and P.key=D.key and P.layer='okresy'
  group by P.layer, P.key, P.name, geom;


commit;
