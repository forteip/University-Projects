 CREATE OR REPLACE FUNCTION Add_Answer(Users varchar(100), assignmentID int , questionId int, right_answer varchar(255))
 	RETURNS varchar AS $$
 	BEGIN
	 	IF EXISTS (SELECT 1 FROM UserAssignQuesAnsMap WHERE UTOR_Id=Users AND Assignment_Id=assignmentID AND Question_Id=questionId) THEN
 			UPDATE UserAssignQuesAnsMap SET Answer = right_answer, Temp_Answer = NULL WHERE UTOR_Id=Users AND Assignment_Id=assignmentID AND Question_Id=questionId;
 			RETURN 'Offical Answer Modified';
 		ELSE
		  	INSERT INTO UserAssignQuesAnsMap (UTOR_Id, Assignment_Id,Question_Id,Answer,Temp_Answer) VALUES (Users, assignmentID,questionId,right_answer,NULL);
 			RETURN 'Offical Answer Added';
 		END IF;


 	END;
 	$$ LANGUAGE plpgsql;

 CREATE OR REPLACE FUNCTION Add_Temp_Answer(Users varchar(100), assignmentID int , questionId int,temporal_answer varchar(255))
 	RETURNS varchar AS $$
 	BEGIN
	 	IF EXISTS (SELECT 1 FROM UserAssignQuesAnsMap WHERE UTOR_Id=Users AND Assignment_Id=assignmentID AND Question_Id=questionId) THEN
 			UPDATE UserAssignQuesAnsMap SET Temp_Answer=temporal_answer WHERE UTOR_Id=Users AND Assignment_Id=assignmentID AND Question_Id=questionId;
 			RETURN 'Temporal Answer Modified';
 		ELSE
		  	INSERT INTO UserAssignQuesAnsMap (UTOR_Id, Assignment_Id,Question_Id,Temp_Answer) VALUES (Users, assignmentID,questionId,temporal_answer);
 			RETURN 'Temporal Answer Added';
 		END IF;


 	END;
 	$$ LANGUAGE plpgsql;
