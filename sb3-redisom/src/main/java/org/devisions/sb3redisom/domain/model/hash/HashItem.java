package org.devisions.sb3redisom.domain.model.hash;

import org.devisions.sb3redisom.domain.model.Item;
import org.devisions.sb3redisom.domain.model.ItemType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.redis.om.spring.annotations.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

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
