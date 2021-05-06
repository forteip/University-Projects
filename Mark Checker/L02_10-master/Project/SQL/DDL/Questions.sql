DROP TABLE IF EXISTS Questions;

Create Table Questions(
	Id	SERIAL PRIMARY KEY,
	Question varchar(100),
	Question_Type varchar(100) NOT NULL DEFAULT 'Multiple Choice',
  Automark boolean NOT NULL DEFAULT TRUE
)