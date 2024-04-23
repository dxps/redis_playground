package org.devisions.sb3redisom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class CacheConfig {

    @Value("${cache.redis.hostname}")
    private String hostname;

    @Value("${cache.redis.port}")
    private int port;

    @Value("${cache.docitems.keyspace}")
    private String docitemsKeyspace;

    @Value("${cache.hashitems.keyspace}")
    private String hashitemsKeyspace;

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
