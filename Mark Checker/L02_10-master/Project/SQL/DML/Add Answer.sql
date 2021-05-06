 CREATE OR REPLACE FUNCTION Add_Answer(Users varchar(100), assignmentID int , questionId int, answer varchar(255))
 	RETURNS varchar AS $$
 	BEGIN
 		INSERT INTO UserAssignQuesAnsMap (UTOR_Id, Assignment_Id,Question_Id,Answer) VALUES (Users, assignmentID,questionId,answer);
 		RETURN 'Answer Added';
 	END;
 	$$ LANGUAGE plpgsql;
