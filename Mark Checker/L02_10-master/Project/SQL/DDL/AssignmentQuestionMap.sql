DROP TABLE IF EXISTS AssignmentQuestionMap;

Create Table AssignmentQuestionMap(
	Assignment_Id int NOT NULL REFERENCES Assignments(Id),
	Question_Id int NOT NULL REFERENCES Questions(Id)
)