package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.neo4j.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendServiceImpl implements RecommendService {

  private final UserRepository userRepository;
}
