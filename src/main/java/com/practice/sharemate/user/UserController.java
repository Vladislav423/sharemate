package com.practice.sharemate.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable long userId,@RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);
    }

}
