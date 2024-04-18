package org.devisions.sb3redisom.repos.doc;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.devisions.sb3redisom.domain.model.doc.DocItem;
import org.devisions.sb3redisom.domain.model.hash.HashItem;

import java.util.List;


public interface DocItemCache extends RedisDocumentRepository<DocItem, String> {

    List<HashItem> findByName(String name);

    List<HashItem> findByType(String type);

}
