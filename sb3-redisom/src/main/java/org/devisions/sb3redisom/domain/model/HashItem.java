package org.devisions.sb3redisom.domain.model;

import com.redis.om.spring.annotations.Indexed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class HashItem extends Item {

    @Id
    private String id;

    @NonNull
    @Indexed
    private final String name;

    @NonNull
    private final ItemType type;

}
