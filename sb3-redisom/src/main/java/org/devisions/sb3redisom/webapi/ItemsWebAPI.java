package org.devisions.sb3redisom.webapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devisions.sb3redisom.domain.model.Item;
import org.devisions.sb3redisom.domain.model.ItemType;
import org.devisions.sb3redisom.domain.model.doc.DocItem;
import org.devisions.sb3redisom.domain.model.hash.HashItem;
import org.devisions.sb3redisom.repos.doc.DocItemCache;
import org.devisions.sb3redisom.repos.hash.HashItemCache;
import org.devisions.sb3redisom.webapi.dtos.CreateItemDto;
import org.devisions.sb3redisom.webapi.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Slf4j
public class ItemsWebAPI {

    private final HashItemCache hashItemCache;
    private final DocItemCache docItemCache;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(
            name = "type", required = false) String type) {

        if (type != null) {
            var itemType = ItemType.valueOf(type.toUpperCase());
            switch (itemType) {
                case HASH -> {
                    var items = hashItemCache.findAll();
                    log.debug("Fetched {} hash items.", items.size());
                    return ResponseEntity.ok(items);
                }
                case DOC -> {
                    var items = docItemCache.findAll();
                    log.debug("Fetched {} doc items.", items.size());
                    return ResponseEntity.ok(items);
                }
            }
        }
        var hashItems = hashItemCache.findAll();
        var docItems = docItemCache.findAll();
        var all = new ArrayList<>();
        all.addAll(hashItems);
        all.addAll(docItems);
        log.debug("Fetched {} items.", all.size());
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateItemDto dto) {

        ItemType itemType;
        try {
            itemType = ItemType.valueOf(dto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorDto("The type must be doc or hash."));
        }
        Item item;
        if (itemType == ItemType.HASH) {
            var hashItem = new HashItem(UUID.randomUUID().toString(), dto.getName(), itemType);
            item = hashItemCache.save(hashItem);

        } else {
            var docItem = new DocItem(UUID.randomUUID().toString(), dto.getName(), itemType);
            item = docItemCache.save(docItem);
        }
        log.debug("Saved {}", item);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTypeAndName(
            @RequestParam(name = "type", required = true) String type,
            @RequestParam(name = "name", required = true) String name) {

        ItemType itemType;
        try {
            itemType = ItemType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorDto("The type must be doc or hash."));
        }
        Item item;
        if (itemType == ItemType.HASH) {
            var result = hashItemCache.findByName(name);
            log.debug("Search found {} hash items with name '{}'.", result.size(), name);
            return ResponseEntity.ok(result);
        } else {
            var result = docItemCache.findByName(name);
            log.debug("Search found {} doc items with name '{}'.", result.size(), name);
            return ResponseEntity.ok(result);
        }
    }

}
