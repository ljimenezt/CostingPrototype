ALTER TABLE recursos_humanos.persona
  ADD CONSTRAINT fk_persona_civil_status FOREIGN KEY (id_civil_status)
      REFERENCES general.civil_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;