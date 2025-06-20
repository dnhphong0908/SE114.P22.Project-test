package com.se114p12.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.neo4j.driver.Driver;

@Configuration
@EnableTransactionManagement
public class Neo4jConfig {
    @Bean
    public Neo4jTransactionManager transactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }
}