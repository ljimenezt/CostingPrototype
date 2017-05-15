CREATE TABLE costs.activity_materials
(
  idactivity integer NOT NULL,
  idmaterial integer NOT NULL,
  quantity_budget double precision NOT NULL,
  cost_budget double precision NOT NULL,
  quantity_actual double precision,
  cost_actual double precision,
  date_time timestamp without time zone NOT NULL,
  CONSTRAINT pk_activity_materials PRIMARY KEY (idactivity, idmaterial),
  CONSTRAINT fk_activity_materials_activity FOREIGN KEY (idactivity)
      REFERENCES costs.activities (idactivity)
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_activity_materials_materials FOREIGN KEY (idmaterial)
      REFERENCES warehouse.materials (idmaterial)
      ON UPDATE NO ACTION ON DELETE NO ACTION
);