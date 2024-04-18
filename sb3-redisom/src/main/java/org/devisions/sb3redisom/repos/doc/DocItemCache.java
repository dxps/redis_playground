package org.devisions.sb3redisom.repos.doc;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.devisions.sb3redisom.domain.model.doc.DocItem;

import java.util.List;


public interface DocItemCache extends RedisDocumentRepository<DocItem, String> {

    List<DocItem> findByName(String name);

    List<DocItem> findByType(String type);

}
