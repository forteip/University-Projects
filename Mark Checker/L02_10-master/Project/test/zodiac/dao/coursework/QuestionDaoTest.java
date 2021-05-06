package zodiac.dao.coursework;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import zodiac.dao.ClassDao;

public class QuestionDaoTest {

  @Test
  public void testAddClasses() {
    String res = new ClassDao().addEditClass("XDXD69", "UnitTest Class");
    assertEquals("New Class Added", res);
  }

  @Test
  public void testEditClass() {
    String res = new ClassDao().addEditClass("XDXD69", "UnitTest Class");
    assertEquals("New Class Added", res);
  }
}
