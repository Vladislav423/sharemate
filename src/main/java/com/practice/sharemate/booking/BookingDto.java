package com.practice.sharemate.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.sharemate.item.ItemDto;
import com.practice.sharemate.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    private long id;

    @JsonProperty("start")
    private LocalDateTime startDate;

    @JsonProperty("end")
    private LocalDateTime endDate;

    private ItemDto item;

    private UserDto booker;

    private Status status;
}