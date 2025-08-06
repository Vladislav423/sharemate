package com.practice.sharemate.booking;

import com.practice.sharemate.exception.*;
import com.practice.sharemate.item.Item;
import com.practice.sharemate.item.ItemRepository;
import com.practice.sharemate.user.User;
import com.practice.sharemate.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto findById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto create(long userId, BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        log.debug("Пользователь найден: {}",user);

        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Товар не найден"));
        log.debug("Товар найден: {}",user);

        if (!item.getAvailable()) {
            log.warn("Товар не доступен {}", item);
            throw new ItemNotAvailableException("Товар не доступен");
        }


        if (item.getOwner().getId() == userId) {
            log.warn("Только владелец может подтверждать бронирование {}", userId);
            throw new BookingPermissionException("Только владелец может подтверждать бронирование");
        }


        if (bookingRequestDto.getStartDate() == null || bookingRequestDto.getEndDate() == null ||
                bookingRequestDto.getStartDate().isAfter(bookingRequestDto.getEndDate()) ||
                bookingRequestDto.getStartDate().isEqual(bookingRequestDto.getEndDate())) {
            log.warn("Некорректная дата бронирования startDate = {}, endDate = {}", bookingRequestDto.getStartDate(), bookingRequestDto.getEndDate());
            throw new InvalidDateTimeException("Некорректная дата бронирования");
        }

        Booking booking = new Booking();
        booking.setStartDate(bookingRequestDto.getStartDate());
        booking.setEndDate(bookingRequestDto.getEndDate());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(long userId, long bookingId, boolean approve) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));

       if (booking.getItem().getOwner().getId() != userId) {
            throw new BookingPermissionException("Только владелец может подтверждать бронирование");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new InvalidStatusException("Статус бронирования уже изменен");
        }

        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> findUserBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByStartDateDesc(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartDateAfterOrderByStartDate(userId, now);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartDateBeforeAndEndDateIsAfterOrderByStartDateDesc(userId, now, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDateDesc(userId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDateDesc(userId, Status.REJECTED);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный статус заказа");
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findOwnerBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (state.toUpperCase()){
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDateDesc(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(userId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItemOwnerIdAndStartDateAfterOrderByStartDate(userId, now);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId, now, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, Status.REJECTED);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный статус заказа");
        }
        return  bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }


}
