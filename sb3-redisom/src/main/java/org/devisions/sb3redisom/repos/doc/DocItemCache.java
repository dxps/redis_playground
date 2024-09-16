package org.devisions.sb3redisom.repos.doc;

import java.util.List;

import com.redis.om.spring.annotations.Query;
import org.devisions.sb3redisom.domain.model.doc.DocItem;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.repository.query.Param;

public interface DocItemCache extends RedisDocumentRepository<DocItem, String> {

    List<DocItem> findByName(String name);

    @Query("@type:{$type}")
    List<DocItem> findByType(@Param("type") String type);

}
