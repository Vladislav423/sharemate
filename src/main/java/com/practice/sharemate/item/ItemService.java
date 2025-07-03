package com.practice.sharemate.item;

import java.util.List;

public interface ItemService {

    ItemDto findById(long itemId);

    List<ItemDto> findAllByOwnerId(long ownerId);

    ItemDto create(long ownerId, ItemDto itemDto);

    ItemDto update(long ownerId, long itemId, ItemDto itemDto);

    List<ItemDto> search(String text);

}
