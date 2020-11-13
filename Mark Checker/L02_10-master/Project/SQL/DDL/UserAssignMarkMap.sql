DROP TABLE IF EXISTS UserAssignMarkMap;

Create Table UserAssignMarkMap(
	UTOR_Id varchar(100) NOT NULL REFERENCES Users(UTOR_Id),
	Assignment_Id int NOT NULL REFERENCES Assignments(Id),
	Mark int,
	UsedAttempts int
)