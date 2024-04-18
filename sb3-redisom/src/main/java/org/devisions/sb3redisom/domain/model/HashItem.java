package org.devisions.sb3redisom.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@AllArgsConstructor
@Data
public class HashItem {

    @Id
    private String id;

    @NonNull
    private final String name;

    @NonNull
    private final ItemType type;

}
