package com.practice.sharemate.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId){
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId){
        return itemService.findAllByOwnerId(ownerId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long ownerId,@Valid @RequestBody ItemDto itemDto){
        return  itemService.create(ownerId,itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long ownerId,@PathVariable long itemId,@RequestBody ItemDto itemDto){
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text){
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,@PathVariable long itemId,@RequestBody CommentRequestDto commentRequestDto){
        return itemService.addComment(userId,itemId,commentRequestDto);

    }


}
