package com.leminhbao.eventmanagement.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtils {

  private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private CommonUtils() {
    // Utility class - private constructor
  }

  public static String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  public static String formatDateTime(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DEFAULT_DATE_FORMAT) : null;
  }

  public static String formatDateTime(LocalDateTime dateTime, String pattern) {
    if (dateTime == null || pattern == null) {
      return null;
    }
    return dateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static boolean isNotNullOrEmpty(String str) {
    return !isNullOrEmpty(str);
  }
}
