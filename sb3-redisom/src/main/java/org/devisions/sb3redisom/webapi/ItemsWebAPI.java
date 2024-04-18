package org.devisions.sb3redisom.webapi;

import lombok.RequiredArgsConstructor;
import org.devisions.sb3redisom.domain.model.HashItem;
import org.devisions.sb3redisom.domain.model.ItemType;
import org.devisions.sb3redisom.repos.HashItemCache;
import org.devisions.sb3redisom.webapi.dtos.CreateItemDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemsWebAPI {

    private final HashItemCache cache;

    @GetMapping
    public List<HashItem> getAll(@RequestParam(name="type", required = false) String type) {
        if (type != null) {
            return cache.findByType(type);
        }
        return cache.findAll();
    }

    @PostMapping
    public HashItem create(@RequestBody CreateItemDto dto) {
        var item = new HashItem(UUID.randomUUID().toString(), dto.getName(), ItemType.valueOf(dto.getType()));
        return cache.save(item);
    }

}
