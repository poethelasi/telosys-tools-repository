-- SET SCHEMA TEST5 ;

-- 1 entity created
CREATE TABLE badge ( 
	code VARCHAR(2),
	name VARCHAR(45),
	PRIMARY KEY(code)
);

-- 1 entity created
CREATE TABLE team ( 
	code VARCHAR(2),
	name VARCHAR(45),
    teacher_code INTEGER,
	PRIMARY KEY(code),
	FOREIGN KEY(teacher_code) REFERENCES teacher(code)
);

-- 1 entity updated
ALTER TABLE teacher ADD COLUMN badge_code VARCHAR(2);
ALTER TABLE teacher ADD FOREIGN KEY(badge_code) REFERENCES badge(code) ;

ALTER TABLE student DROP FOREIGN KEY fk_teacher ;
