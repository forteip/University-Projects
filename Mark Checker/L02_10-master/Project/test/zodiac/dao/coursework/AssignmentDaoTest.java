package zodiac.dao.coursework;

import org.junit.Test;
import zodiac.action.AssignmentAction;
import zodiac.definition.coursework.Assignment;

import static org.junit.Assert.*;
import java.util.List;

public class AssignmentDaoTest {

    @Test
    public void testGetAssignment()
    {
        List<Assignment> res = new AssignmentDao().getAssignments("CSCC01");
        int tick = 0;
        for (Assignment a : res)
        {
            if (a.getName().equals("Test01") || a.getName().equals("Test03"))
            {
                tick++;
            }
        }
        assertTrue(tick >= 2);
    }

    @Test
    public void testGetAssignmentCourseDne()
    {
        List<Assignment> res = new AssignmentDao().getAssignments("XDXD69");
        assertTrue(res.size() == 0);
    }

    @Test
    public void testAddAssignment()
    {
        new AssignmentDao().addAssignment("UnitTesting", "CSCC01");
        List<Assignment> res = new AssignmentDao().getAssignments("CSCC01");
        int i = 0;
        boolean found = false;
        while (!found && i < res.size())
        {
            if (res.get(i).getName().equals("UnitTesting"))
            {
                found = true;
            }
            i++;
        }
        assertTrue(found);
    }

    @Test
    public void testEditMaxAttemptId()
    {
        int newVal = 12;
        int targetId = 27;
        new AssignmentDao().editAssignmentMaxAttempt(targetId, newVal);
        assertEquals(newVal, new AssignmentAction().getAssignment(targetId).getMaxAttempt());
    }

    @Test
    public void testEditMaxAttemptAss()
    {
        int targetId = 27;
        int newVal = 14;
        Assignment a = new Assignment("testAssign", targetId);
        a.setMaxAttempt(newVal);
        new AssignmentDao().editAssignmentMaxAttempt(a);
        assertEquals(newVal, new AssignmentAction().getAssignment(targetId).getMaxAttempt());
    }

    @Test
    public void testEditAssignmentName()
    {
        int targetId = 26;
        Assignment a = new Assignment("MemeLord", targetId);
//        a.setMaxAttempt(5);
        String res = new AssignmentDao().editAssignment(a);
        assertEquals("Assignment Name Modified", res);
    }

    @Test
    public void testChangeAssigmentVisibility()
    {
        int targetId = 26;
        Assignment a = new Assignment("MemeLord", targetId);
        boolean res = new AssignmentDao().changeAssignmentVisibility(targetId, false);
        assertTrue(res);

    }
}
