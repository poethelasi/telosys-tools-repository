
-- Just add a FK 
ALTER TABLE customer ADD FOREIGN KEY(country_code) REFERENCES country(code) ;

