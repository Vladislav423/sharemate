package com.practice.sharemate.item;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class ItemStorageImpl implements ItemStorage {
    private final HashMap<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    @Override
    public Optional<Item> findById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findAllByOwnerId(long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        item.setId(nextId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> result = new ArrayList<>();

        if (text.isEmpty()){
            return result;
        }

        for (Item item : items.values()){
            if (!item.getAvailable()){
                continue;
            }

            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            if (name.contains(text.toLowerCase()) || description.contains(text.toLowerCase())){
                result.add(item);
            }
        }

        return result;
    }

}
