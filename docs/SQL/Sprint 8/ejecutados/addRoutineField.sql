ALTER TABLE costs.activities
ADD COLUMN  routine boolean;
UPDATE costs.activities  SET routine=true;