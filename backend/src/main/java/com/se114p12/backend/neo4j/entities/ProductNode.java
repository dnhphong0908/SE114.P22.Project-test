package com.se114p12.backend.neo4j.entities;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("ProductNode")
public class ProductNode {
  @Id @GeneratedValue private Long id;

  @Relationship(type = "BELONGS_TO")
  private CategoryNode category;

  @Relationship(type = "BOUGHT_WITH")
  private List<BoughtWithRelationship> coPurchasedProducts;
}
