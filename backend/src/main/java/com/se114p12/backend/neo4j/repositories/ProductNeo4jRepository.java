package com.se114p12.backend.neo4j.repositories;

import com.se114p12.backend.neo4j.entities.ProductNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductNeo4jRepository extends Neo4jRepository<ProductNode, Long> {

  @Query(
      "MATCH (p:ProductNode)<-[:ORDERED]-(u:UserNode) " + "RETURN p ORDER BY count(u) DESC LIMIT 5")
  List<Long> findGeneralRecommended();

  @Query(
      "MATCH (u:UserNode {id:"
          + " $userId})-[:ORDERED]->(p:ProductNode)-[:BELONGS_TO]->(c:CategoryNode) MATCH"
          + " (c)<-[:BELONGS_TO]-(recommended:ProductNode) WHERE NOT (u)-[:ORDERED]->(recommended)"
          + " RETURN recommended.id ORDER BY count(*) DESC LIMIT 5")
  // + " ORDER BY recommended.rating DESC")
  List<Long> findRecommendByCategory(Long userId);

  // collaborative filtering by user
  @Query(
      "MATCH (u1:UserNode {id: $userId})-[:ORDERED]->(p:ProductNode)<-[:ORDERED]-(u2:UserNode) "
          + "WHERE u2.id <> $userId "
          + "MATCH (u2)-[o:ORDERED]->(recommended:ProductNode) "
          + "WHERE NOT (u1)-[:ORDERED]->(recommended) "
          + "RETURN recommended.id ORDER BY o.count DESC, count(*) DESC LIMIT 5")
  List<Long> findRecommendedByHistory(Long userId);

  // co-purchase analysis.
  @Query(
      "MATCH (u1:UserNode {id: $userId})-[:ORDERED]->(p:ProductNode)<-[:ORDERED]-(u2:UserNode) "
          + "WHERE u2.id <> $userId "
          + "MATCH (u2)-[o:ORDERED]->(recommended:ProductNode)<-[:BOUTH_WITH]-(p) "
          + "WHERE NOT (u1)-[:ORDERED]->(recommended) "
          + "RETURN recommended.id ORDER BY o.count DESC, count(*) DESC LIMIT 5")
  List<Long> findRecommendedByCoPurchase(Long userId);
}
