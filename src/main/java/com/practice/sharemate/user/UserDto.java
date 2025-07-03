package com.practice.sharemate.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

}
