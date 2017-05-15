CREATE TABLE general.day
(
  id serial NOT NULL,
  name varchar(10) NOT NULL,
  CONSTRAINT pk_day PRIMARY KEY (id),
  CONSTRAINT uq_day_name UNIQUE (name)
)

CREATE TABLE general.type_food
(
  id serial NOT NULL,
  name varchar(50) NOT NULL,
  description text,
  CONSTRAINT pk_type_food PRIMARY KEY (id)
)

CREATE TABLE human_resources.day_type_food
(
  id serial NOT NULL,
  id_type_food integer NOT NULL,
  id_day integer NOT NULL,
  after_holiday boolean,
  status boolean NOT NULL,
  CONSTRAINT pk_day_type_food PRIMARY KEY (id),
  CONSTRAINT fk_day_type_food_type_food FOREIGN KEY (id_type_food)
      REFERENCES general.type_food (id),
  CONSTRAINT fk_day_type_food_day FOREIGN KEY (id_day)
      REFERENCES general.day (id)
)

CREATE TABLE human_resources.food_control(
id SERIAL NOT NULL,
id_hr integer NOT NULL,
id_type_food integer NOT NULL,
quantity integer NOT NULL,
date timestamp NOT NULL,
other text,
unit_cost double precision NOT NULL,
CONSTRAINT pk_food_control PRIMARY KEY (id),
CONSTRAINT fk_food_control_hr FOREIGN KEY (id_hr)
      REFERENCES human_resources.hr (idhr),
CONSTRAINT fk_food_control_type_food FOREIGN KEY (id_type_food)
      REFERENCES general.type_food (id)
)

CREATE TABLE general.holiday
(
  id serial NOT NULL,
  date timestamp NOT NULL,
  description text NOT NULL,
  CONSTRAINT pk_holiday PRIMARY KEY (id),
  CONSTRAINT uq_date UNIQUE (date)
)