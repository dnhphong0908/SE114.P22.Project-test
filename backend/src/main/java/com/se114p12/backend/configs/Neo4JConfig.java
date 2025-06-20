package com.se114p12.backend.configs;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class Neo4JConfig {

  @Bean
  public Neo4jTransactionManager transactionManager(Driver driver) {
    return new Neo4jTransactionManager(driver);
  }
}
