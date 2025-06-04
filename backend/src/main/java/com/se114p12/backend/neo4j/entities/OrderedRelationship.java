package com.se114p12.backend.neo4j.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@RelationshipProperties
public class OrderedRelationship {

  private Long count;

  @TargetNode private Long productNodeId;
}
