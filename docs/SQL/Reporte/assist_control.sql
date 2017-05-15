CREATE TABLE human_resources.assist_control
(
  id serial NOT NULL,
  idhr integer NOT NULL,
  date timestamp NOT NULL,
  absent boolean NOT NULL,
  observations text,
  CONSTRAINT pk_assist_control PRIMARY KEY (id),
  CONSTRAINT fk_assist_control_hr FOREIGN KEY (idhr)
      REFERENCES human_resources.hr (idhr)
)