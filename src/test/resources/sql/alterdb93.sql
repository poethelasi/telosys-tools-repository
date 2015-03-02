
-- Just remove a FK 
ALTER TABLE customer DROP FOREIGN KEY fk1 ;

ALTER TABLE customer ADD CONSTRAINT fk1 FOREIGN KEY(department_code) REFERENCES department(code);

