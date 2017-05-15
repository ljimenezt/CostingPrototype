CREATE TABLE costs.activity_team ( 
	idteam smallint NOT NULL,
	idactivity integer NOT NULL,
	
	CONSTRAINT pk_team_activity PRIMARY KEY (idteam, idactivity),
    CONSTRAINT fk_team FOREIGN KEY (idteam)
      REFERENCES human_resources.team (idteam) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fk_activty FOREIGN KEY (idactivity)
      REFERENCES costs.activities (idactivity) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
;

