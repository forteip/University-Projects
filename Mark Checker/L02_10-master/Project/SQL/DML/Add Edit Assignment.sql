 CREATE OR REPLACE FUNCTION AddEdit_Assignment(checkId int, courseCode varchar(100),  assignmentName varchar(255))
 	RETURNS varchar AS $$
 	DECLARE returnId int;
 	BEGIN
 		IF EXISTS (SELECT 1 FROM Assignments WHERE Id = checkId) THEN
 			UPDATE Assignments SET Assignment_Name = assignmentName WHERE Id = checkId;
 			RETURN 'Assignment Name Modified';
 		ELSE
 			INSERT INTO Assignments (Course_Code, Assignment_Name) VALUES (courseCode, assignmentName) returning Id INTO returnId;
 			RETURN returnId;
 		END IF;
 	END;
 	$$ LANGUAGE plpgsql;