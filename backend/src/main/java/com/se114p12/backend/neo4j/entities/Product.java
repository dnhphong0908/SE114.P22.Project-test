package com.se114p12.backend.neo4j.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Product")
public class Product {
  @Id @GeneratedValue private Long id;

  @Relationship(type = "BELONGS_TO")
  private Category category;

  @Relationship(type = "BOUGHT_WITH")
  private List<BoughtWithRelationship> coPurchasedProducts;
}
