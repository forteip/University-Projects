package zodiac.util;

import java.time.YearMonth;

public class ValidationUtil {

  /**
   * Check if a string is a valid year.
   *
   * @param year the year as a string
   * @return true if year is a year and between 1914 AD and 294276 AD (max range of PostgreSQL)
   */
  public static boolean isValidYear(String year) {
    return isValidRange(year, 1914, 294276);
  }

  /**
   * Check if a string is a valid month as a number.
   *
   * @param month the month as a string in number form
   * @return true if month is a number between 1 and 12
   */
  public static boolean isValidMonth(String month) {
    return isValidRange(month, 1, 12);
  }

  /**
   * Check if a string is a valid day.
   *
   * @param day the day as a string
   * @param month the month as a number between 1 and 12
   * @param year the year as a number
   * @return true if day is a number and exists in that month of that year
   */
  public static boolean isValidDay(String day, int month, int year) {
    YearMonth yearMonth = YearMonth.of(year, month);
    int max = yearMonth.lengthOfMonth();
    return isValidRange(day, 1, max);
  }

  /**
   * Check if a string is a valid hour.
   *
   * @param hour the hour as a string
   * @return true if hour is a number between 0 and 23
   */
  public static boolean isValidHour(String hour) {
    return isValidRange(hour, 0, 23);
  }

  /**
   * Check if a string is a valid minute.
   *
   * @param minute the minute as a string
   * @return true if minute is a number between 0 and 59
   */
  public static boolean isValidMinute(String minute) {
    return isValidRange(minute, 0, 59);
  }

  private static boolean isValidRange(String number, int min, int max) {
    int convertedNum;

    try {
      convertedNum = Integer.parseInt(number);
    } catch (NumberFormatException e) {
      return false;
    }

    return convertedNum >= min && convertedNum <= max;
  }

}
