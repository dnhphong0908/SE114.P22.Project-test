package com.se114p12.backend.configs;

import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.RoleName;
import com.se114p12.backend.enums.UserStatus;
import com.se114p12.backend.repositories.authentication.RoleRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.util.LoginUtil;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // Admin user details
  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.password}")
  private String adminPassword;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.phone}")
  private String adminPhone;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // Create default roles if they do not exist
    createEnumRoles();

    // Validate admin user information
    validateAdminInfo();

    // Clean up any existing user with the same information
    // cleanUpUserWithSameAdminInfomation();

    // Create admin user
    createAdminUser();
  }

  private void createEnumRoles() {
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

  // private void cleanUpUserWithSameAdminInfomation() {
  //   Optional<User> existsByUsername = userRepository.findByUsername(adminUsername);
  //   Optional<User> existsByEmail = userRepository.findByEmail(adminEmail);
  //   Optional<User> existsByPhone = userRepository.findByPhone(adminPhone);
  //   Set<Long> ids = new HashSet<>();
  //   if (existsByUsername.isPresent()) {
  //     ids.add(existsByUsername.get().getId());
  //   }
  //   if (existsByEmail.isPresent()) {
  //     ids.add(existsByEmail.get().getId());
  //   }
  //   if (existsByPhone.isPresent()) {
  //     ids.add(existsByPhone.get().getId());
  //   }
  //   userRepository.deleteAllById(ids);
  // }

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
    if (adminUsername == null || adminUsername.isEmpty()) {
      throw new IllegalArgumentException("Admin username must not be empty");
    }
    if (adminPassword == null || adminPassword.isEmpty()) {
      throw new IllegalArgumentException("Admin password must not be empty");
    }
    if (adminEmail == null || adminEmail.isEmpty()) {
      throw new IllegalArgumentException("Admin email must not be empty");
    }
    if (!LoginUtil.EMAIL_PATTERN.matcher(adminEmail).matches()) {
      throw new IllegalArgumentException("Admin email is not valid");
    }
    if (adminPhone == null || adminPhone.isEmpty()) {
      throw new IllegalArgumentException("Admin phone must not be empty");
    }
    if (!LoginUtil.PHONE_PATTERN.matcher(adminPhone).matches()) {
      throw new IllegalArgumentException("Admin phone is not valid");
    }
  }
}
