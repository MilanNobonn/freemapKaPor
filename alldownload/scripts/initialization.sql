begin transaction;

# You need to have the slovak grid file.
INSERT INTO spatial_ref_sys
VALUES (
99999,'',99999,
'PROJCS["S-JTSK_Krovak_East_North + SR TOWGS84",GEOGCS["GCS_S_JTSK",DATUM["Jednotne_Trigonometricke_Site_Katastralni",SPHEROID["Bessel_1841",6377397.155,299.1528128],TOWGS84[485.021,169.465,483.839,7.786342,4.397554,4.102655,0]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Krovak"],PARAMETER["False_Easting",0],PARAMETER["False_Northing",0],PARAMETER["Pseudo_Standard_Parallel_1",78.5],PARAMETER["Scale_Factor",0.9999],PARAMETER["Azimuth",30.28813975277778],PARAMETER["Longitude_Of_Center",24.83333333333333],PARAMETER["Latitude_Of_Center",49.5],PARAMETER["X_Scale",-1],PARAMETER["Y_Scale",1],PARAMETER["XY_Plane_Rotation",90],UNIT["Meter",1],AUTHORITY["EPSG","102067"]]'
,'+proj=krovak +ellps=bessel +units=m +no_defs +nadgrids=slovak'
);

CREATE TABLE downloaded
(
  x integer NOT NULL,
  y integer NOT NULL,
  t timestamp without time zone NOT NULL
 );

CREATE TABLE data
(
  layer text NOT NULL,
  key bigint NOT NULL,
  geom geometry NOT NULL
 );

CREATE TABLE popisky
(
  layer text NOT NULL,
  key bigint NOT NULL,
  name text NOT NULL
 );

commit;
