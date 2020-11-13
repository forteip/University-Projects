 CREATE OR REPLACE FUNCTION Add_Class(courseCode varchar(100), className varchar(255))
 	RETURNS varchar AS $$
 	BEGIN
 		IF EXISTS (SELECT 1 FROM Classes WHERE Course_Code = courseCode) THEN
 			UPDATE Classes SET Class_Name = className WHERE Course_Code = courseCode;
 			RETURN 'Class Name Modified';
 		ELSE
 			INSERT INTO Classes (Course_Code, Class_Name) VALUES (courseCode, className);
 			RETURN 'New Class Added';
 		END IF;
 	END;
 	$$ LANGUAGE plpgsql;
 