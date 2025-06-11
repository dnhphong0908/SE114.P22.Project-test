package com.se114p12.backend.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mapbox")
public class MapboxConfig {
    private String accessToken;
    private String baseUrl;
}