package com.practice.sharemate.item;

import com.practice.sharemate.exception.ItemNotFoundException;
import com.practice.sharemate.exception.UserNotFoundException;
import com.practice.sharemate.exception.ItemHasAnotherUserException;
import com.practice.sharemate.user.User;
import com.practice.sharemate.user.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    @Override
    public ItemDto findById(long itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Пользователь не найден"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllByOwnerId(long ownerId) {
        List<Item> items = itemStorage.findAllByOwnerId(ownerId);
        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(ItemMapper.toItemDto(item));
        }

        return result;
    }

    @Override
    public ItemDto create(long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);

        Optional<User> ownerOptional = userStorage.findById(ownerId);

        if (ownerOptional.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        User owner = ownerOptional.get();

        item.setOwner(owner);

        itemStorage.create(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long ownerId, long itemId, ItemDto itemDto) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        if (item.getOwner().getId() != ownerId) {
            throw new ItemHasAnotherUserException("id у пользователя вещи и запрошенного id пользователя не совпадают");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        itemStorage.update(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text){
        List<ItemDto> items = new ArrayList<>();

        if (text.isEmpty()){
           return items;
        }

       List<Item> result = itemStorage.search(text);

        return result.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
