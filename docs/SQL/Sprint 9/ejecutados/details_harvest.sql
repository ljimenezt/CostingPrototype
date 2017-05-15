
CREATE TABLE life_cycle.details_harvest
(
  id serial NOT NULL,
  idactivity integer NOT NULL,
  previous_sacks double precision,
  sacks_day double precision,
  current_sacks double precision,
  dispatch double precision,
  poundage double precision,
  total_sacks double precision,
  CONSTRAINT pk_details_harvest PRIMARY KEY (id),
  CONSTRAINT fk_details_harvest_activities FOREIGN KEY (idactivity)
      REFERENCES costs.activities (idactivity) MATCH Unknown
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
