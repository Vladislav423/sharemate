package com.practice.sharemate.user;



import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);

    User create(User user);

    User update(long userId, User user);

    void deleteById(long userId);
}
