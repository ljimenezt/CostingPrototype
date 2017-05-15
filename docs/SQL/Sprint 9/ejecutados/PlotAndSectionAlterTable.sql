ALTER TABLE  life_cycle.section ADD COLUMN idcropname INTEGER;
ALTER TABLE life_cycle.section ADD CONSTRAINT "fk_section_crop_names"
  FOREIGN KEY (idcropname) REFERENCES life_cycle.crop_names (idcropname) ON DELETE No Action ON UPDATE No Action;
UPDATE life_cycle.section 
   SET idcropname = 5;
ALTER TABLE  life_cycle.section ALTER COLUMN idcropname SET NOT NULL;


ALTER TABLE  life_cycle.plot ADD COLUMN idsection INTEGER;
ALTER TABLE life_cycle.plot ADD CONSTRAINT "fk_plot_section"
  FOREIGN KEY (idsection) REFERENCES life_cycle.section (idsection) ON DELETE No Action ON UPDATE No Action;



ALTER TABLE  life_cycle.plot ADD COLUMN idcropname INTEGER;
ALTER TABLE life_cycle.plot ADD CONSTRAINT "fk_plot_crop_names"
  FOREIGN KEY (idcropname) REFERENCES life_cycle.crop_names (idcropname) ON DELETE No Action ON UPDATE No Action;
UPDATE life_cycle.plot 
   SET idcropname = 5;
ALTER TABLE  life_cycle.plot ALTER COLUMN idcropname SET NOT NULL;