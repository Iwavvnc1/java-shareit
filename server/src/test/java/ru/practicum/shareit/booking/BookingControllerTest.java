package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;

    @Test
    void create() {
        Long userId = 0L;
        BookingInDto booking = BookingInDto.builder().build();
        BookingOutDto returnBooking = BookingOutDto.builder().build();
        when(bookingService.create(userId, booking)).thenReturn(returnBooking);
        ResponseEntity<BookingOutDto> response = bookingController.create(userId, booking);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnBooking, response.getBody());
    }

    @Test
    void updateStatus() {
        Long userId = 0L;
        Long bookingId = 0L;
        Boolean approved = true;
        BookingOutDto returnBooking = BookingOutDto.builder().build();
        when(bookingService.updateStatus(userId, bookingId, approved)).thenReturn(returnBooking);
        ResponseEntity<BookingOutDto> response = bookingController.updateStatus(userId, bookingId, approved);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnBooking, response.getBody());
    }

    @Test
    void getAllByUser() {
        Long userId = 0L;
        String state = "state";
        Integer from = 0;
        Integer size = 0;
        List<BookingOutDto> bookings = List.of(BookingOutDto.builder().build());
        when(bookingService.getAllByUser(userId, state, from, size)).thenReturn(bookings);
        ResponseEntity<List<BookingOutDto>> response = bookingController.getAllByUser(userId, state, from, size);
        assertEquals(OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void getById() {
        Long userId = 0L;
        Long bookingId = 0L;
        BookingOutDto returnBooking = BookingOutDto.builder().build();
        when(bookingService.getById(userId, bookingId)).thenReturn(returnBooking);
        ResponseEntity<BookingOutDto> response = bookingController.getById(userId, bookingId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnBooking, response.getBody());
    }

    @Test
    void getAllByItems() {
        Long userId = 0L;
        String state = "state";
        Integer from = 0;
        Integer size = 0;
        List<BookingOutDto> bookings = List.of(BookingOutDto.builder().build());
        when(bookingService.getAllByItem(userId, state, from, size)).thenReturn(bookings);
        ResponseEntity<List<BookingOutDto>> response = bookingController.getAllByItems(userId, state, from, size);
        assertEquals(OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }
}