package org.devisions.sb3redisom.config;

import static org.springframework.util.StringUtils.commaDelimitedListToSet;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.devisions.sb3redisom.domain.model.doc.DocItem;
import org.devisions.sb3redisom.domain.model.hash.HashItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheConfig {

    @Value("${cache.redis.host}")
    private String hostname;

    @Value("${cache.redis.port}")
    private int port;

    @Value("${cache.redis.password}")
    private String password;

    @Value("${cache.redis.ha.enabled}")
    private boolean redisHaEnabled;

    @Value("${cache.redis.ha.sentinel.nodes}")
    private String redisHaSentinelNodes;

    @Value("${cache.redis.ha.master.name}")
    private String redisHaMasterName;

    @Value("${cache.docitems.keyspace}")
    private String docitemsKeyspace;

    @Value("${cache.hashitems.keyspace}")
    private String hashitemsKeyspace;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        if (!redisHaEnabled) {
            JedisConnectionFactory factory = new JedisConnectionFactory(
                    new RedisStandaloneConfiguration(hostname, port));
            factory.afterPropertiesSet();
            factory.start();
            log.info("Initialized connection for a Redis standalone setup.");
            return factory;
        }

        var sentinelNodes = commaDelimitedListToSet(redisHaSentinelNodes);
        var redisNodes = sentinelNodes.stream().map(RedisNode::fromString).collect(Collectors.toSet());
        var sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.setMaster(redisHaMasterName);
        sentinelConfig.setSentinels(redisNodes);
        sentinelConfig.setPassword(password);

        var poolConfig = new JedisPoolConfig();
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));
        poolConfig.setNumTestsPerEvictionRun(-1);
        poolConfig.setTestWhileIdle(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestOnBorrow(false);

        final int timeout = 10000;

        final JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(timeout))
                .readTimeout(Duration.ofMillis(timeout))
                .usePooling()
                .poolConfig(poolConfig)
                .build();

        log.info("Initialized connection for a Redis HA setup.");
        return new JedisConnectionFactory(sentinelConfig, jedisClientConfiguration);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisMappingContext keyValueMappingContext() {
        return new RedisMappingContext(
                new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration() {
                    @Override
                    protected Iterable<KeyspaceSettings> initialConfiguration() {
                        return () -> List.of(
                                new KeyspaceSettings(HashItem.class, hashitemsKeyspace),
                                new KeyspaceSettings(DocItem.class, docitemsKeyspace)).iterator();
                    }
                }));
    }

}
