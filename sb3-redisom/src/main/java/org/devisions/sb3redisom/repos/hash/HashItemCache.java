package org.devisions.sb3redisom.repos.hash;

import java.util.List;

import org.devisions.sb3redisom.domain.model.hash.HashItem;

import com.redis.om.spring.repository.RedisEnhancedRepository;

public interface HashItemCache extends RedisEnhancedRepository<HashItem, String> {

    List<HashItem> findByName(String name);

    List<HashItem> findByType(String type);

}
