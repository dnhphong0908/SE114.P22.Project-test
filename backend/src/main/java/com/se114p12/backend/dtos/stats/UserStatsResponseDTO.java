package com.se114p12.backend.dtos.stats;

import com.se114p12.backend.dtos.user.UserResponseDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatsResponseDTO {
  private Long totalUsers;
  private List<UserResponseDTO> topUsers;
}
