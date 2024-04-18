package org.devisions.sb3redisom.repos;

import com.redis.om.spring.repository.RedisEnhancedRepository;
import org.devisions.sb3redisom.domain.model.HashItem;

import java.util.List;

public interface HashItemCache extends RedisEnhancedRepository<HashItem, String> {

    List<HashItem> findByType(String type);

}
