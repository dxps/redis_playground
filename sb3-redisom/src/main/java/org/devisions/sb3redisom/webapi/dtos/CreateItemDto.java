package org.devisions.sb3redisom.webapi.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateItemDto {

    @NonNull
    private final String name;

    @NonNull
    private final String type;

}