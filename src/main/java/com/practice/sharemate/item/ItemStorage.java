package com.practice.sharemate.item;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Optional<Item> findById(long itemId);

    List<Item> findAllByOwnerId(long ownerId);

    Item create(Item item);

    Item update(Item item);

    List<Item> search(String text);
}
