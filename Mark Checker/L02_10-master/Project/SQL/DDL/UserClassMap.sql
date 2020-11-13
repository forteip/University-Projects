DROP TABLE IF EXISTS UserClassMap;

Create Table UserClassMap(
	UTOR_Id varchar(100) NOT NULL REFERENCES Users(UTOR_Id),
	Course_Code varchar(100) NOT NULL REFERENCES Classes(Course_Code)
)