/** CacaoSoft - Sprint 11

   	Engine Log
   	1- Create table engine_log.
   
	Author: Patricia.Patinio
	Date:	2017-02-20
*/

DROP TABLE IF EXISTS diesel.engine_log;

CREATE TABLE diesel.engine_log
(
  id_engine_log serial NOT NULL,
  date timestamp NOT NULL,
  delivered_by integer,
  received_by integer,
  hour_on time without time zone NOT NULL,
  hour_off time without time zone NOT NULL,
  hourmeter_on double precision,
  hourmeter_off double precision,
  duration double precision,
  irrigation boolean NOT NULL,
  id_machine integer,
  id_activity integer,
  CONSTRAINT pk_engine_log PRIMARY KEY (id_engine_log),
  CONSTRAINT fk_engine_log_activity_machines FOREIGN KEY (id_activity, id_machine)
      REFERENCES costs.activity_machine (id_activity, id_machine) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_engine_log_delivered FOREIGN KEY (delivered_by)
      REFERENCES human_resources.hr (idhr) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_engine_log_received FOREIGN KEY (received_by)
      REFERENCES human_resources.hr (idhr) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unique_serial_engine_log UNIQUE (id_engine_log)
);