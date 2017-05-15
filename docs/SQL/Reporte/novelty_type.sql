CREATE TABLE human_resources.novelty_type
(
  id serial NOT NULL,
  name varchar(250) NOT NULL,
  description text,
  id_color integer NOT NULL,
  CONSTRAINT pk_novelty_type PRIMARY KEY (id),
  CONSTRAINT fk_novelty_type_color FOREIGN KEY (id_color)
      REFERENCES general.color (id),
  CONSTRAINT uq_novelty_type_color UNIQUE (id_color),
  CONSTRAINT uq_novelty_type_name UNIQUE (name)
)