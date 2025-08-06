package com.practice.sharemate.item;

import com.practice.sharemate.booking.BookingRepository;
import com.practice.sharemate.booking.Status;
import com.practice.sharemate.exception.ItemNotFoundException;
import com.practice.sharemate.exception.UserNotFoundException;
import com.practice.sharemate.exception.ItemHasAnotherUserException;
import com.practice.sharemate.user.User;
import com.practice.sharemate.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    @Override
    public ItemDto findById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Пользователь не найден"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllByOwnerId(long ownerId) {
        List<Item> items = itemRepository.findAllByOwner_Id(ownerId);
        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(ItemMapper.toItemDto(item));
        }

        return result;
    }

    @Override
    public ItemDto create(long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);

        Optional<User> ownerOptional = userRepository.findById(ownerId);

        if (ownerOptional.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        User owner = ownerOptional.get();

        item.setOwner(owner);

        itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long ownerId, long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
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

        itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> items = new ArrayList<>();

        if (text.isEmpty()) {
            return items;
        }

        List<Item> result = itemRepository.search(text);

        return result.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentRequestDto commentRequestDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар не найден"));

        bookingRepository.findByBookerIdAndItemIdAndStatusAndEndDateIsBefore(userId,itemId, Status.APPROVED,LocalDateTime.now())
                .stream().
                findFirst().orElseThrow(() -> new RuntimeException("вы не можете оставить комментарий"));

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }


}
