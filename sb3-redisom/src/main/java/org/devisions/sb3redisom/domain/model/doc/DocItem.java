package org.devisions.sb3redisom.domain.model.doc;

import org.devisions.sb3redisom.domain.model.Item;
import org.devisions.sb3redisom.domain.model.ItemType;
import org.springframework.data.annotation.Id;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Document()
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocItem extends Item {

    @Id
    private String id;

    @NonNull
    @Indexed
    private final String name;

    @NonNull
    private final ItemType type;

}
