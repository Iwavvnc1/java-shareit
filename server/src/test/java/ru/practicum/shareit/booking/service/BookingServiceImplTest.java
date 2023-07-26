package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void updateStatus_whenInvokedWithStatusTrue_thenReturnBooking() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        assertEquals(Status.APPROVED, bookingService.updateStatus(userId, bookingId, true).getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateStatus_whenInvokedWithStatusFalse_thenReturnBooking() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        assertEquals(Status.REJECTED, bookingService.updateStatus(userId, bookingId, false).getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateStatus_whenInvokedWithOutUser_thenReturnBooking() {
        Long userId = 0L;
        Long userId1 = 1L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () -> bookingService.updateStatus(userId1, bookingId, true).getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void updateStatus_whenInvokedWithStatusApproved_thenReturnBooking() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.APPROVED);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(InCorrectDataException.class, () -> bookingService.updateStatus(userId, bookingId, true).getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void getAllByItem_whenInvokedStateAll_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "ALL";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStatePAST_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "PAST";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(5), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStateFUTURE_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "FUTURE";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStateWAITING_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "WAITING";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStateREJECTED_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "REJECTED";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.REJECTED);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStateNON_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "CURRENTs";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertThrows(InCorrectDataException.class, () -> bookingService
                .getAllByItem(userId, state, from, size));
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedStateCURRENT_thenReturnListBookings() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Integer from = 1;
        Integer size = 1;
        Integer page = from / size;
        String state = "CURRENT";
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        List<Item> items = List.of(item);
        List<Long> itemIds = List.of(item.getId());
        List<Booking> bookings = List.of(booking);
        PageImpl<Booking> bookingsPage = new PageImpl<>(bookings);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        when(bookingRepository.findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending())))
                .thenReturn(bookingsPage);
        assertEquals(bookings.get(0).getId(), bookingService.getAllByItem(userId, state, from, size).get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
        verify(bookingRepository).findByItemIdIn(itemIds,
                PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @Test
    void getAllByItem_whenInvokedFailedUser_thenReturnListBookings() {
        Long userId = 0L;
        Integer from = 1;
        Integer size = 1;
        String state = null;
        when(userRepository.existsUserById(userId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bookingService.getAllByItem(userId, state, from, size));
        verify(userRepository).existsUserById(userId);
        verify(itemRepository, never()).findAllByOwnerIdIs(userId);
        verify(bookingRepository, never()).findByItemIdIn(anyList());
    }

    @Test
    void getAllByUser_whenInvokedFailedUser_thenReturnListBookings() {
        Long userId = 0L;
        Integer from = 1;
        Integer size = 1;
        String state = null;
        when(userRepository.existsUserById(userId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bookingService.getAllByUser(userId, state, from, size));
        verify(userRepository).existsUserById(userId);
        verify(bookingRepository, never()).findByBookerId(any());
    }

    @Test
    void create_whenInvokedWithFalseAvailable_thenReturnException() {
        Long userId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", false, user, null);
        BookingInDto bookingInDto = BookingInDto.builder().itemId(item.getId()).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.existsUserById(userId)).thenReturn(true);
        assertThrows(InCorrectDataException.class, () -> bookingService.create(userId, bookingInDto));
        verify(userRepository).findById(userId);
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(bookingInDto.getItemId());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenInvokedWithOwnerEqualsUser_thenReturnException() {
        Long userId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        BookingInDto bookingInDto = BookingInDto.builder().itemId(item.getId()).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.existsUserById(userId)).thenReturn(true);
        assertThrows(NotFoundException.class, () -> bookingService.create(userId, bookingInDto));
        verify(userRepository).findById(userId);
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(bookingInDto.getItemId());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenInvokedWithoutTime_thenReturnException() {
        Long userId = 0L;
        Long userId2 = 1L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        BookingInDto bookingInDto = BookingInDto.builder().itemId(item.getId()).build();
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.existsUserById(userId2)).thenReturn(true);
        assertThrows(InCorrectDataException.class, () -> bookingService.create(userId2, bookingInDto));
        verify(userRepository).findById(userId2);
        verify(userRepository).existsUserById(userId2);
        verify(itemRepository).findById(bookingInDto.getItemId());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenInvokedWithTime_thenReturnBooking() {
        Long userId = 0L;
        Long userId2 = 1L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        BookingInDto bookingInDto = BookingInDto.builder()
                .itemId(item.getId())
                .bookerId(userId2)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        Booking booking = new Booking(0L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), item, user, Status.WAITING);
        when(userRepository.findById(userId2)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.existsUserById(userId2)).thenReturn(true);
        when(bookingRepository.save(any())).thenReturn(booking);
        assertEquals(Status.WAITING, bookingService.create(userId2, bookingInDto).getStatus());
        verify(userRepository).findById(userId2);
        verify(userRepository).existsUserById(userId2);
        verify(itemRepository).findById(bookingInDto.getItemId());
        verify(bookingRepository).save(any());
    }

    @Test
    void getById_whenInvoked_thenReturnBooking() {
        Long userId = 0L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(0L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), item, user, Status.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        BookingOutDto bookingOutDto = bookingService.getById(userId, bookingId);
        assertEquals(booking.getId(), bookingOutDto.getId());
        assertEquals(booking.getStart(), bookingOutDto.getStart());
        assertEquals(booking.getStatus(), bookingOutDto.getStatus());
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getById_whenInvokedWithFailedUser_thenReturnException() {
        Long userId = 0L;
        Long userId2 = 1L;
        Long bookingId = 0L;
        User user = new User(userId);
        Item item = new Item(0L, "name", "description", true, user, null);
        Booking booking = new Booking(0L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), item, user, Status.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () -> bookingService.getById(userId2, bookingId));
        verify(bookingRepository).findById(bookingId);
    }
}