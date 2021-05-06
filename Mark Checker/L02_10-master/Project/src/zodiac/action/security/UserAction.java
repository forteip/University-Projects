package zodiac.action.security;

import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import zodiac.dao.security.UserDao;
import zodiac.definition.security.User;
import zodiac.util.ActiveUser;

public class UserAction {

  /**
   * Attempt to register the given user with a entered password.
   *
   * @param utorId the UTOR_Id of the user
   * @param password the password the user wants to use
   * @return message to display on the UI
   */
  public String register(String utorId, String password) {
    boolean isRegistered;
    try {
      isRegistered = new UserDao().getRegistered(utorId);
    } catch (SQLException e) {
      return "Unable to register, Contact your system admin";
    }
    if (isRegistered) {
      // Don't tell potential hackers why they can't register
      return "Unable to register, Contact your system admin";
    } else {
      password = StringUtils.trimToEmpty(password);
      if (password.length() < 6) {
        return "Password length must be at least 6";
      } else {
        // Recommend work factor is at least 12 log rounds
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        if (new UserDao().insertHash(utorId, hash).equals("Success")) {
          return "Registered";
        } else {
          return "Unable to register, Contact your system admin";
        }
      }
    }
  }

  /**
   * Attempt to login to the system. The active user can be retrieved using
   * ActiveUser.INSTANCE.getUser()
   *
   * @param utorId the UTOR_Id of the user
   * @param password the password to verify against the DB
   * @return message to display on the UI
   */
  public String login(String utorId, String password) {
    if (ActiveUser.INSTANCE.getUser() != null) {
      return "Error: Already logged in";
    } else {
      String hash = new UserDao().getHash(utorId);
      if (hash == null) {
        return "Failed to login";
      }
      if (BCrypt.checkpw(password, hash)) {
        // If password is correct
        User user = new UserDao().getUser(utorId);
        ActiveUser.INSTANCE.setUser(user);
        if (ActiveUser.INSTANCE.getUser().getUtorId().equals(utorId)) {
          return "Logged in";
        } else {
          ActiveUser.INSTANCE.logOff();
          return "Failed to login";
        }
      } else {
        // Show generic error to avoid revealing too much information to attackers
        return "Failed to login";
      }
    }
  }

  /**
   * Log off the current user.
   *
   * @return message to display on the UI
   */
  public String logoff() {
    if (ActiveUser.INSTANCE.getUser() == null) {
      return "Not logged in";
    } else {
      ActiveUser.INSTANCE.logOff();
      return "Logged off";
    }
  }
}
