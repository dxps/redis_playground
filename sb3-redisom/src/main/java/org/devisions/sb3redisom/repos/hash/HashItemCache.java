package org.devisions.sb3redisom.repos.hash;

import java.util.List;

import com.redis.om.spring.annotations.Query;
import org.devisions.sb3redisom.domain.model.hash.HashItem;

import com.redis.om.spring.repository.RedisEnhancedRepository;
import org.springframework.data.repository.query.Param;

public interface HashItemCache extends RedisEnhancedRepository<HashItem, String> {

    List<HashItem> findByName(String name);

    @Query("@type:{$type}")
    List<HashItem> findByType(@Param("type") String type);

}
