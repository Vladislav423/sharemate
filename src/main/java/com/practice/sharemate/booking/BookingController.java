package com.practice.sharemate.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.findById(userId,bookingId);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,@RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId,bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDto> findUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,@RequestParam(defaultValue = "ALL") String state){
        return bookingService.findUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,@RequestParam(defaultValue = "ALL") String state){
        return bookingService.findOwnerBookings(userId, state);
    }

}
