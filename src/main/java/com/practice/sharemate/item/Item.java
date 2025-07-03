package com.practice.sharemate.item;

import com.practice.sharemate.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
