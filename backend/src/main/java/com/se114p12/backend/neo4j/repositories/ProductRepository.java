package com.se114p12.backend.neo4j.repositories;

import com.se114p12.backend.neo4j.entities.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends Neo4jRepository<Product, Long> {}
