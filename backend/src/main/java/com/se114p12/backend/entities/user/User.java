package com.se114p12.backend.entities.user;

import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.authentication.RefreshToken;
import com.se114p12.backend.entities.authentication.Role;
import com.se114p12.backend.entities.authentication.Verification;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.notification.NotificationUser;
import com.se114p12.backend.enums.LoginProvider;
import com.se114p12.backend.enums.UserStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Column(nullable = false)
  private String fullname;

  @Column(unique = true, nullable = false)
  private String username;

  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String phone;

  private String avatarUrl;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  private LoginProvider loginProvider;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NotificationUser> notifications = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Verification> verifications = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<RefreshToken> roles = new ArrayList<>();

    public User(Long userId) {
        super();
    }

    //    @OneToMany(mappedBy = "user")
  //    private List<Review> reviews = new ArrayList<>();
}
