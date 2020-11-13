 CREATE OR REPLACE FUNCTION Add_Student(utorId varchar(100), courseCode varchar(100), lastName varchar(255) default NULL, firstName varchar(255) default NULL)
 	RETURNS varchar AS $$
 	BEGIN
 		IF EXISTS (SELECT 1 FROM Classes WHERE Course_Code = courseCode) THEN
	 		IF EXISTS (SELECT 1 FROM Users WHERE UTOR_Id = utorId) THEN
	 			IF EXISTS (SELECT 1 FROM UserClassMap WHERE UTOR_Id=utorId AND Course_Code = courseCode) THEN
	 				RETURN 'Student already exists in class';
	 			ELSE
	 				INSERT INTO UserClassMap (UTOR_Id, Course_Code) VALUES (utorId, courseCode);
	 				RETURN 'Success';
	 			END IF;
	 		ELSE
	 			CASE WHEN (lastName IS NULL OR firstName IS NULL) THEN
	 				RETURN 'Name required to create new student';
	 			ELSE
	 				INSERT INTO Users (UTOR_Id, Last_Name, First_Name, Role) VALUES (utorId, lastName, firstName, 'Student');
	 				INSERT INTO UserClassMap (UTOR_Id, Course_Code) VALUES (utorId, courseCode);
	 				RETURN 'Success and student created';
	 			END CASE;
	 		END IF;
	 	ELSE
	 		RETURN 'Class does not exist';
	 	END IF;
 	END;
 	$$ LANGUAGE plpgsql;
 