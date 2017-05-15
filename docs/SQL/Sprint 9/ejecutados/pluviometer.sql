
CREATE TABLE life_cycle.pluviometer
(
  id serial NOT NULL,
  date_record timestamp without time zone NOT NULL,
  reading integer NOT NULL,
  CONSTRAINT pk_pluviometer PRIMARY KEY (id)
);
