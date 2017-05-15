ALTER TABLE human_resources.hr
  ADD CONSTRAINT fk_hr_civil_status FOREIGN KEY (id_civil_status)
      REFERENCES general.civil_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;