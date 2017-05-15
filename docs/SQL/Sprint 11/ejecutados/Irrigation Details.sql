/** CacaoSoft - Sprint 11

   	Irrigation details
   	1- Create table irrigation_details.
   
	Author: Patricia.Patinio
	Date:	2017-02-21
*/

DROP TABLE IF EXISTS diesel.irrigation_details;

CREATE TABLE diesel.irrigation_details
(
  id_irrigation_details serial NOT NULL,
  id_engine_log integer NOT NULL,
  id_machine integer NOT NULL,
  id_zone smallint NOT NULL,
  hidrometer_on double precision NOT NULL,
  hidrometer_off double precision NOT NULL,
  water_usage double precision NOT NULL,
  water_cost double precision,
  duration double precision,
  depreciation_cost double precision,
  maintenance_cost double precision,
  observations text,
  CONSTRAINT pk_irrigation_details PRIMARY KEY (id_irrigation_details),
  CONSTRAINT fk_irrigation_details_engine_log FOREIGN KEY (id_engine_log)
      REFERENCES diesel.engine_log (id_engine_log) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_irrigation_details_machines FOREIGN KEY (id_machine)
      REFERENCES machines.machines (idmachine) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_irrigation_details_zone FOREIGN KEY (id_zone)
      REFERENCES diesel.zone (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unique_serial_irrigation_details UNIQUE (id_irrigation_details)
);