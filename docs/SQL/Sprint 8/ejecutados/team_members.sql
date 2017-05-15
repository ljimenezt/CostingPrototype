CREATE TABLE human_resources.team_members ( 
	idteam smallint NOT NULL,
	idhr integer NOT NULL,
	lead  boolean NOT NULL,
	statistician  boolean NOT NULL,
	
	CONSTRAINT pk_team_members PRIMARY KEY (idteam, idhr),
    CONSTRAINT fk_team_members_team FOREIGN KEY (idteam)
      REFERENCES human_resources.team (idteam) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fk_team_members_hr FOREIGN KEY (idhr)
      REFERENCES human_resources.hr (idhr) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
;

