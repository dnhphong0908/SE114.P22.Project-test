package com.se114p12.backend.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  @ConditionalOnProperty(name = "redis.enabled", havingValue = "true", matchIfMissing = true)
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();

    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

    return template;
  }

  // Fallback khi Redis không có: dùng in-memory cache
  @Bean
  @ConditionalOnMissingBean(RedisConnectionFactory.class)
  public CacheManager fallbackCacheManager() {
    System.out.println(
        "⚠️ Redis not available. Using ConcurrentMapCacheManager (in-memory cache).");
    return new ConcurrentMapCacheManager("default");
  }
}
