package com.practice.sharemate.user;

import com.practice.sharemate.exception.EmailAlreadyExistsException;
import com.practice.sharemate.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Пользователь с такой почтой существует");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("пользователь не найден"));

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            existingUser.setName(userDto.getName());
        }

        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            if (userByEmail.isPresent() && userByEmail.get().getId() != userId) {
                throw new EmailAlreadyExistsException("Пользователь с такой почтой существует");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        userRepository.save(existingUser);

        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }
}
