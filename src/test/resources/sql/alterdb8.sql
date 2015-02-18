-- SET SCHEMA TEST8 ;

-- 1 entity created
CREATE TABLE team ( 
	code VARCHAR(2),
	name VARCHAR(45),
    teacher_code INTEGER,
	PRIMARY KEY(code),
	FOREIGN KEY(teacher_code) REFERENCES teacher(code)
);
