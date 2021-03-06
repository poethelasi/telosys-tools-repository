DROP ALL OBJECTS;

CREATE TABLE country ( 
	code VARCHAR(2),
	name VARCHAR(45),
	PRIMARY KEY(code)
);

CREATE TABLE department ( 
	code VARCHAR(3),
	name VARCHAR(45),
	PRIMARY KEY(code)
);

CREATE TABLE customer (
	code VARCHAR(5),
	country_code VARCHAR(2) NOT NULL,
	department_code VARCHAR(3) NOT NULL,
	first_name VARCHAR(40),
	last_name VARCHAR(40),
	login VARCHAR(20) NOT NULL,
	password VARCHAR(20),
	age INTEGER,
	city VARCHAR(45),
	zip_code INTEGER,
	phone VARCHAR(20),
	reviewer SMALLINT,
	PRIMARY KEY(code),
	CONSTRAINT fk1 FOREIGN KEY(country_code) REFERENCES country(code)
);

