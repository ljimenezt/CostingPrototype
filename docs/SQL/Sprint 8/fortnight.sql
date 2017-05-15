-- Table: costs.fortnight

-- DROP TABLE costs.fortnight;

CREATE TABLE costs.fortnight
(
  id_fortnight integer NOT NULL,
  description text,
  CONSTRAINT pk_id_fortnight PRIMARY KEY (id_fortnight)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE costs.fortnight
  OWNER TO postgres;
