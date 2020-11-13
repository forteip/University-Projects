package zodiac.definition.security;

import java.util.Map;
import zodiac.dao.security.UserDao;

public class User {

  private String utorId;
  private String lastName;
  private String firstName;
  private String role;
  private Map<String, String> permissions;

  /**
   * Generate the user object. There should only be one user object in the system at the time which
   * represents the currently logged in user.
   *
   * @param utorId the UTORId of the user
   * @param role the role of the user
   * @param lastName the user's last name
   * @param firstName the user's first name
   */
  public User(String utorId, String role, String lastName, String firstName) {
    this.utorId = utorId;
    this.role = role;
    this.lastName = lastName;
    this.firstName = firstName;
    refreshPermissions();
  }

  /**
   * Refreshes the user's permissions from the database. Should be run every time the user creates a
   * new course so they will have access to it.
   */
  public void refreshPermissions() {
    permissions = new UserDao().getPermissions(utorId);
  }

  public String getUtorId() {
    return utorId;
  }

  public String getRole() {
    return role;
  }

  /**
   * Get a map of courses the user has permissions to and whether they are read or write
   * permissions.
   *
   * @return A map of Course ids mapped to 'Read' or 'Write'
   */
  public Map<String, String> getPermissions() {
    return permissions;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }
}
