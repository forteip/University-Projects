DROP TABLE IF EXISTS Questions;

Create Table Questions(
	Id	SERIAL PRIMARY KEY,
	Question varchar(100),
	Answer varchar(255) NOT NULL,
)