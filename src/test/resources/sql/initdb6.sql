-- CREATE SCHEMA IF NOT EXISTS TEST6;

-- SET SCHEMA TEST6 ;

DROP ALL OBJECTS;
-- Many to One

CREATE TABLE teacher (
  code INTEGER NOT NULL,
  name VARCHAR(40),
  PRIMARY KEY(code)
);

CREATE TABLE student (
  id INTEGER NOT NULL ,
  first_name VARCHAR(40),
  last_name VARCHAR(40),
  teacher_code INTEGER,
  PRIMARY KEY(id),
  CONSTRAINT fk_teacher FOREIGN KEY(teacher_code) REFERENCES teacher(code),
);
