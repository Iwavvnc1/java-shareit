package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Value
public class BookingMapper {
    public static BookingIdOutDto toBookingIdOutDto(Booking booking) {
        return BookingIdOutDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getItem().getId())
                .itemName(booking.getItem().getName())
                .build();
    }

    public static BookingOutDto toBookingOutDto(Booking booking) {
        return BookingOutDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .build();
    }

    public static Booking toBooking(BookingInDto bookingDto, User user, Item item, Status status) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setItem(item);
        booking.setStatus(status);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(user);
        return booking;
    }
}
