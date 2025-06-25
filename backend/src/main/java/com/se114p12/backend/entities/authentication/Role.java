package com.se114p12.backend.entities.authentication;

import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.user.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  private Boolean active;

  @OneToMany(mappedBy = "role")
  private List<User> users = new ArrayList<>();
}
