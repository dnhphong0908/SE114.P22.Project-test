package com.se114p12.backend.constants;

public class AppConstant {
  public static final String API_VERSION = "v1";
  public static final String API_BASE_PATH = "/api/" + API_VERSION;

  private AppConstant() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
