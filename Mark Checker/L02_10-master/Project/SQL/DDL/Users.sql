DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
	UTOR_Id varchar(100) PRIMARY KEY,
	Last_Name varchar(255) NOT NULL,
	First_Name varchar(255) NOT NULL,
	Role varchar(255) NOT NULL,
  Password varchar(60),
  Registered boolean NOT NULL DEFAULT False
)