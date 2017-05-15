CREATE TABLE human_resources.novelty
(
  id serial NOT NULL,
  idhr integer NOT NULL,
  idnoveltytype integer NOT NULL,
  initial_date_time timestamp NOT NULL,
  final_date_time timestamp NOT NULL,
  observations text,
  CONSTRAINT pk_novelty PRIMARY KEY (id),
  CONSTRAINT fk_novelty_hr FOREIGN KEY (idhr)
      REFERENCES human_resources.hr (idhr),
  CONSTRAINT fk_novelty_noveltytype FOREIGN KEY (idnoveltytype)
      REFERENCES human_resources.novelty_type (id)
)