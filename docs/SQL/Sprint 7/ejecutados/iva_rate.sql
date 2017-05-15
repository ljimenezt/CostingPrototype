
CREATE TABLE general.iva_rate
(
	id_iva serial NOT NULL,
	name varchar(100) NOT NULL,
	rate double precision NOT NULL,
	CONSTRAINT pk_tax_rate PRIMARY KEY (id_iva)
)
;


