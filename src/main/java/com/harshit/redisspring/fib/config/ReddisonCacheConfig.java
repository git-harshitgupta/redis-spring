package com.harshit.redisspring.fib.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
class RedissonCacheConfig {

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient){
        return new RedissonSpringCacheManager(redissonClient);
    }

    @Bean
    public RedissonClient syncRedisClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

    @Bean
    public RedissonReactiveClient reactiveRedisConnectionFactory(RedissonClient redissonClient) {
        return redissonClient.reactive();
    }

}
