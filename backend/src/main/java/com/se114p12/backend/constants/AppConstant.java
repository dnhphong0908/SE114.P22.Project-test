package com.se114p12.backend.constants;

public class AppConstant {
  public static final String API_VERSION = "v1";
  public static final String API_BASE_PATH = "/api/" + API_VERSION;
  public static final String BACKEND_URL = "http://localhost:8080" + API_BASE_PATH;
  public static final String CATEGORY_FOLDER = "category";

  private AppConstant() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
