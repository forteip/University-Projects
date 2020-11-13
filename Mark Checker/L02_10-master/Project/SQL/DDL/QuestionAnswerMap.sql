DROP TABLE IF EXISTS QuestionAnswerMap;

Create Table QuestionAnswerMap(
	Question_Id int NOT NULL REFERENCES Questions(Id),
	Answer varchar(255) NOT NULL,
	Correct boolean NOT NULL
)