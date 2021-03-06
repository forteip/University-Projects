CREATE DATABASE youngry2;
USE youngry2;
CREATE TABLE Professors (ProfessorIdentificationCode CHAR(16) NOT NULL, name VARCHAR(20), address VARCHAR(25), phone CHAR(12), CONSTRAINT Professors_PK PRIMARY KEY (ProfessorIdentificationCode));
CREATE TABLE Books (ISBN BIGINT(13) NOT NULL, PublicationDate DATE NOT NULL, type VARCHAR(10) CHECK (type IN (‘scientific’, ‘fiction’, ‘novel’)), CONSTRAINT Books_PK PRIMARY KEY (ISBN, PublicationDate));
CREATE TABLE publishing (ProfessorIdentificationCode CHAR(16) NOT NULL, ISBN BIGINT(13) NOT NULL, Price INTEGER, CONSTRAINT publishing_PK PRIMARY KEY(ProfessorIdentificationCode, ISBN), CONSTRAINT publishing_FK1 FOREIGN KEY(ProfessorIdentificationCode) REFERENCES Professors(ProfessorIdentificationCode), CONSTRAINT publishing_FK2 FOREIGN KEY(ISBN) REFERENCES Books(ISBN));
ALTER TABLE Professors RENAME Professor;
ALTER TABLE Professor ADD UNIQUE (phone);
ALTER TABLE Professor MODIFY phone BIGINT(13);
ALTER TABLE publishing DROP PRIMARY KEY, ADD CONSTRAINT publishing_PK PRIMARY KEY (ProfessorIdentificationCode, ISBN);
ALTER TABLE publishing ADD CONSTRAINT publishing_FK1_d FOREIGN KEY (ProfessorIdentificationCode) REFERENCES Professor(ProfessorIdentificationCode) ON DELETE RESTRICT, ADD CONSTRAINT publishing_FK2_d FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON DELETE RESTRICT;
ALTER TABLE Professor ADD INDEX (name);
ALTER TABLE Books DROP type;
ALTER TABLE publishing DROP FOREIGN KEY publishing_FK1_d, DROP FOREIGN KEY publishing_FK2_d, DROP FOREIGN KEY publishing_FK1, DROP FOREIGN KEY publishing_FK2, DROP PRIMARY KEY, ADD CONSTRAINT publishing_pk PRIMARY KEY (ProfessorIdentificationCode, ISBN);
DROP TABLE Professor;
DROP DATABASE youngry2;