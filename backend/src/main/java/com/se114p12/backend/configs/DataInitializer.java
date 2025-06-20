package com.se114p12.backend.configs;

import com.se114p12.backend.dtos.authentication.RegisterRequestDTO;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.repositories.authentication.RoleRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final Validator validator;

  // Admin user details
  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.password}")
  private String adminPassword;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.phone}")
  private String adminPhone;

  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {
    // Create default roles if they do not exist
    createRoles();
    Role adminRole = roleRepository.findByName(RoleName.ADMIN.getValue()).orElseThrow();

    if (adminRole.getUsers().isEmpty()) {
      // Validate admin user information
      validateAdminInfo();

      // Create admin user
      createAdminUser();
    }
  }

  private void createRoles() {
    for (RoleName roleName : RoleName.values()) {
      if (!roleRepository.existsByName(roleName.getValue())) {
        Role role = new Role();
        role.setName(roleName.getValue());
        role.setDescription(roleName.getDescription());
        role.setActive(true);
        role = roleRepository.save(role);
      }
    }
  }

  private void createAdminUser() {
    if (userRepository.existsByUsername(adminUsername)) {
      return;
    }
    if (userRepository.existsByEmail(adminEmail)) {
      return;
    }
    if (userRepository.existsByPhone(adminPhone)) {
      return;
    }
    User adminUser = new User();
    adminUser.setRole(roleRepository.findByName(RoleName.ADMIN.getValue()).orElseThrow());
    adminUser.setUsername(adminUsername);
    adminUser.setPhone(adminPhone);
    adminUser.setEmail(adminEmail);
    adminUser.setPassword(passwordEncoder.encode(adminPassword));
    adminUser.setFullname(adminUsername);
    adminUser.setLoginProvider(LoginProvider.LOCAL);
    adminUser.setStatus(UserStatus.ACTIVE);
    userRepository.save(adminUser);
  }

  private void validateAdminInfo() {
    // use spring validation annotations in RegisterRequestDTO class
    RegisterRequestDTO request = new RegisterRequestDTO();
    request.setFullname(adminUsername);
    request.setUsername(adminUsername);
    request.setEmail(adminEmail);
    request.setPhone(adminPhone);
    request.setPassword(adminPassword);

    Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("Invalid admin user information: ");
      for (ConstraintViolation<RegisterRequestDTO> violation : violations) {
        errorMessage
            .append(violation.getPropertyPath())
            .append(" ")
            .append(violation.getMessage())
            .append("; ");
      }
      throw new IllegalArgumentException(errorMessage.toString());
    }

    if (userRepository.existsByUsername(adminUsername)) {
      throw new IllegalArgumentException("Username already exists: " + adminUsername);
    }

    if (userRepository.existsByEmail(adminEmail)) {
      throw new IllegalArgumentException("Email already exists: " + adminEmail);
    }

    if (userRepository.existsByPhone(adminPhone)) {
      throw new IllegalArgumentException("Phone number already exists: " + adminPhone);
    }
  }
}
