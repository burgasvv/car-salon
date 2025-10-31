package org.burgas.carrental.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.burgas.carrental.dto.brand.BrandFullResponse
import org.burgas.carrental.dto.car.CarFullResponse
import org.burgas.carrental.dto.identity.IdentityFullResponse
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@EnableCaching
@Configuration
class RedisConfig {

    @Bean
    @Primary
    fun identityCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper

    ): CacheManager {
        val keyRedisSerializer = StringRedisSerializer()
        val jsonValueRedisSerializer = Jackson2JsonRedisSerializer(
            objectMapper, IdentityFullResponse::class.java
        )
        val redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keyRedisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonValueRedisSerializer))

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfig)
            .transactionAware()
            .build()
    }

    @Bean
    fun brandCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ) : CacheManager {
        val keyRedisSerializer = StringRedisSerializer()
        val jsonValueRedisSerializer = Jackson2JsonRedisSerializer(
            objectMapper, BrandFullResponse::class.java
        )
        val redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keyRedisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonValueRedisSerializer))

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfig)
            .transactionAware()
            .build()
    }

    @Bean
    fun carCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ) : CacheManager {
        val keyRedisSerializer = StringRedisSerializer()
        val jsonValueRedisSerializer = Jackson2JsonRedisSerializer(
            objectMapper, CarFullResponse::class.java
        )
        val redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keyRedisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonValueRedisSerializer))

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfig)
            .transactionAware()
            .build()
    }
}