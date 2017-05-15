CREATE TABLE general.system_profile
(
id serial NOT NULL,
break_start time NOT NULL,
break_end time NOT NULL,
break_duration double precision  NOT NULL,
activity_default_start time NOT NULL,
activity_default_end time NOT NULL,
activity_default_duration double precision  NOT NULL,
CONSTRAINT pk_system_profile PRIMARY KEY (id)
);