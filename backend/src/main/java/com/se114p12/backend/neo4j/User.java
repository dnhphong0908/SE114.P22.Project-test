package com.se114p12.backend.neo4j;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node("User")
public class User {
  @Id @GeneratedValue private Long id;
}
