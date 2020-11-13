package zodiac.action;

import java.util.ArrayList;
import java.util.List;

import zodiac.dao.ClassDao;
import zodiac.definition.Class;
import zodiac.util.ActiveUser;

public class ClassAction {

  /**
   * Add API to create a Class object.
   */
  public String addClass(String courseCode, String className) {
    return new ClassDao().addEditClass(courseCode, className);
  }

  /**
   * Gets a list of all classes with read or write access.
   *
   * @return a string the contains
   */
  public List<Class> getClasses() {
    List<Class> classes = new ClassDao().getClasses();

    List<Class> classesToRemove = new ArrayList<>();

    // Remove courses without read permission
    for (Class course : classes) {
      if (!ActiveUser.INSTANCE.canRead(course)) {
        classesToRemove.add(course);
      }
    }

    classes.removeAll(classesToRemove);
    return classes;

  }

}
