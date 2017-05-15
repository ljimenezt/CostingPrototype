-- Table: costs.pay_period

-- DROP TABLE costs.pay_period;

CREATE TABLE costs.pay_period
(
  id_pay_period integer NOT NULL,
  id_fortnight integer NOT NULL,
  month integer, -- Month to be represent in a 1-12 format. i.e. 1 = January, 2 = February , etc.
  first_day timestamp with time zone NOT NULL,
  last_day timestamp with time zone NOT NULL,
  CONSTRAINT pk_id_pay_period PRIMARY KEY (id_pay_period),
  CONSTRAINT fk_id_fortnight FOREIGN KEY (id_fortnight)
      REFERENCES costs.fortnight (id_fortnight) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE costs.pay_period
  OWNER TO postgres;
COMMENT ON COLUMN costs.pay_period.month IS 'Month to be represent in a 1-12 format. i.e. 1 = January, 2 = February , etc.';

