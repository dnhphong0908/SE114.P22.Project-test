package com.se114p12.backend.enums;

import lombok.Getter;

@Getter
public enum RoleName {
  USER("USER"),
  ADMIN("ADMIN");

  private String value;

  RoleName(String value) {
    this.value = value;
  }
}
