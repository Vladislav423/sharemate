package com.practice.sharemate.user;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UserStorageImpl implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email){
       return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public User create(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteById(long userId){
       users.remove(userId);
    }
}
