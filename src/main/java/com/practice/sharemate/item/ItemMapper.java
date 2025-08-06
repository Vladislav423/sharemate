package com.practice.sharemate.item;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item){
        List<CommentDto> commentsDto;
        if (item.getComments() != null){
            commentsDto = item.getComments().stream()
                    .map(CommentMapper::toCommentDto)
                    .toList();
        } else{
            commentsDto = Collections.emptyList();
        }
        return new ItemDto(item.getId(),item.getName(), item.getDescription(),item.getAvailable(), commentsDto);
    }

   public static Item toItem(ItemDto itemDto){
        return new Item(itemDto.getId(),itemDto.getName(), itemDto.getDescription(),itemDto.getAvailable(), null, Collections.emptyList());
    }
}

/*ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(item.getComments());

        return itemDto;*/
