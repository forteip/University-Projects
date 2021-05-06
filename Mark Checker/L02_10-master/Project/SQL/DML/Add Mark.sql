 CREATE OR REPLACE FUNCTION Add_Mark(Users varchar(100), assignmentID int , newMark int)
 	RETURNS varchar AS $$
 	BEGIN
	  IF EXISTS (SELECT 1 FROM UserAssignMarkMap WHERE Assignment_Id = assignmentID AND UTOR_Id = Users) THEN
			
			UPDATE userassignmarkmap
 		  SET UsedAttempts = (
 		    (SELECT coalesce(MAX(usedattempts), 0) FROM userassignmarkmap WHERE utor_id = Users AND assignment_id = assignmentID) + 1
 		   ) WHERE utor_id = Users AND assignment_id = assignmentID;
			
			IF EXISTS (SELECT 1 FROM UserAssignMarkMap WHERE Assignment_Id = assignmentID AND UTOR_Id = Users AND Mark < newMark)  THEN
				UPDATE UserAssignMarkMap SET Mark = newMark WHERE  Assignment_Id = assignmentID AND UTOR_Id = Users;
			ELSE
			END IF;

 			
 			RETURN 'Mark Modified, update atttemp';
 		ELSE
		 	INSERT INTO UserAssignMarkMap (UTOR_Id, Assignment_Id,Mark) VALUES (Users, assignmentID,newMark);
 			RETURN 'Mark added';
 		END IF;
 	END;
 	$$ LANGUAGE plpgsql;
