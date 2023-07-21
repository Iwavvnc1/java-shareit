package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static BookingIdOutDto toBookingIdOutDto(Booking booking) {
        booking.getItem().setOwner(Hibernate.unproxy(booking.getItem().getOwner(), User.class));
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
        booking.getItem().setOwner(Hibernate.unproxy(booking.getItem().getOwner(), User.class));
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
