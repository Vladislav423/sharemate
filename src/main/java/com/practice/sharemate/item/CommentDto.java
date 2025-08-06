package com.practice.sharemate.item;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private int id;

    private String text;

    private String authorName;

    private LocalDateTime created;

}
