package com.se114p12.backend.entities.neo4j;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Category")
public class Category {
  @Id @GeneratedValue private Long id;
}
