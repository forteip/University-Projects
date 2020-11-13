 CREATE OR REPLACE FUNCTION Edit_Student(utorId varchar(100), lastName varchar(255), firstName varchar(255))
 	RETURNS varchar AS $$
 	BEGIN
	 	IF EXISTS (SELECT 1 FROM Users WHERE UTOR_Id = utorId) THEN
	 		UPDATE Users SET Last_Name = lastName, First_Name = firstName WHERE UTOR_Id = utorId;
	 		RETURN 'Success';
	 	ELSE
	 		RETURN 'Student not found';
	 	END IF;
 	END;
 	$$ LANGUAGE plpgsql;
 