 CREATE OR REPLACE FUNCTION Add_Mark(Users varchar(100), assignmentID int , mark int)
 	RETURNS varchar AS $$
 	BEGIN
 		INSERT INTO UserAssignMarkMap (UTOR_Id, Assignment_Id,Mark) VALUES (Users, assignmentID,mark);
 		UPDATE userassignmarkmap
 		  SET UsedAttempts = (
 		    (SELECT coalesce(MAX(usedattempts), 0) FROM userassignmarkmap WHERE utor_id = Users AND assignment_id = assignmentID) + 1
 		   ) WHERE utor_id = UTOR_Id AND assignment_id = assignmentID;
 		RETURN 'Answer Added';
 	END;
 	$$ LANGUAGE plpgsql;
