package org.devisions.sb3redisom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheConfig {

    @Value("${cache.redis.hostname}")
    private String hostname;

    @Value("${cache.redis.port}")
    private int port;

    // @Value("${cache.docitems.keyspace}")
    // private String docitemsKeyspace;

    // @Value("${cache.hashitems.keyspace}")
    // private String hashitemsKeyspace;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(
                new RedisStandaloneConfiguration(hostname, port));
        factory.afterPropertiesSet();
        factory.start();
        return factory;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    // @Bean
    // public RedisMappingContext keyValueMappingContext() {
    //     return new RedisMappingContext(
    //             new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration() {
    //                 @Override
    //                 protected Iterable<KeyspaceSettings> initialConfiguration() {
    //                     return () -> List.of(
    //                             new KeyspaceSettings(HashItem.class, hashitemsKeyspace),
    //                             new KeyspaceSettings(DocItem.class, docitemsKeyspace)).iterator();
    //                 }
    //             }));
    // }

}
