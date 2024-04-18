package org.devisions.sb3redisom.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devisions.sb3redisom.domain.model.hash.HashItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class CacheConfig {

    @Value("${cache.redis.hostname}")
    private String hostname;

    @Value("${cache.redis.port}")
    private int port;

    @Value("${cache.hashitems.keyspace}")
    private String hashitemsKeyspace;

    @Bean
    public RedisMappingContext keyValueMappingContext() {
        return new RedisMappingContext(
                new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration() {
                    @Override
                    protected Iterable<KeyspaceSettings> initialConfiguration() {
                        return () -> List.of(
                                new KeyspaceSettings(HashItem.class, hashitemsKeyspace)
                        ).iterator();
                    }
                }));
    }

}
