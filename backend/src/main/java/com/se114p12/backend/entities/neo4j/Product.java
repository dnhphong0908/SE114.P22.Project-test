package com.se114p12.backend.entities.neo4j;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Product")
public class Product {
  @Id @GeneratedValue private Long id;

  @Relationship(type = "BELONGS_TO")
  private Category category;
}
