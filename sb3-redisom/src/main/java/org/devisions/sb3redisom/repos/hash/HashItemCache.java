package org.devisions.sb3redisom.repos.hash;

import com.redis.om.spring.repository.RedisEnhancedRepository;
import org.devisions.sb3redisom.domain.model.hash.HashItem;

import java.util.List;


public interface HashItemCache extends RedisEnhancedRepository<HashItem, String> {

    List<HashItem> findByName(String name);

    List<HashItem> findByType(String type);

}
