package org.devisions.sb3redisom.repos.doc;

import java.util.List;

import org.devisions.sb3redisom.domain.model.doc.DocItem;

import com.redis.om.spring.repository.RedisDocumentRepository;

public interface DocItemCache extends RedisDocumentRepository<DocItem, String> {

    List<DocItem> findByName(String name);

    List<DocItem> findByType(String type);

}
