package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.dto.ItemWithTimeAndCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.Storage.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void create_whenInvokedWithoutRequestId_thenReturnItem() {
        Long userId = 0L;
        ItemWithRequestDto inItem = ItemWithRequestDto.builder().build();
        User user = new User();
        Item item = toItem(inItem, user);
        ItemDto returnItem = ItemDto.builder().build();
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        assertEquals(returnItem, itemService.create(userId, inItem));
        verify(userRepository).existsUserById(userId);
        verify(userRepository).findById(userId);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void create_whenInvokedWithRequestId_thenReturnItem() {
        Long userId = 0L;
        Long requestId = 0L;
        ItemWithRequestDto inItem = ItemWithRequestDto.builder()
                .requestId(requestId)
                .build();
        User user = new User();
        Item item = toItem(inItem, user);
        ItemDto returnItem = ItemDto.builder().build();
        ItemRequest itemRequest = new ItemRequest();
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(inItem.getRequestId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        assertEquals(returnItem, itemService.create(userId, inItem));
        verify(userRepository).existsUserById(userId);
        verify(userRepository).findById(userId);
        verify(itemRepository).save(any(Item.class));
        verify(itemRequestRepository).findById(inItem.getRequestId());

    }

    @Test
    void getAll() {
        Long userId = 0L;
        ItemWithTimeAndCommentDto returnItem = ItemWithTimeAndCommentDto.builder()
                .comments(new ArrayList<>())
                .build();
        Item item = new Item();
        List<ItemWithTimeAndCommentDto> returnItems = List.of(returnItem);
        List<Item> items = List.of(item);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findAllByOwnerIdIs(userId)).thenReturn(items);
        assertEquals(returnItems, itemService.getAll(userId));
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findAllByOwnerIdIs(userId);
    }

    @Test
    void getAll_whenUserIdInCorrect_thenReturnException() {
        Long userId = 0L;
        when(userRepository.existsUserById(userId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemService.getAll(userId));
        verify(userRepository).existsUserById(userId);
        verify(itemRepository, never()).findAllByOwnerIdIs(userId);
    }

    @Test
    void getById_whenInvokedWithoutTimeAndOneBookingAndEndIsAfter_thenReturnItem() {
        Long userId = 0L;
        Long itemId = 0L;
        Long bookingId = 0L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user,
                Status.APPROVED);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        List<Booking> itemBookings = List.of(booking);
        List<Comment> itemComments = List.of(comment);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemId(itemId)).thenReturn(itemBookings);
        when(commentRepository.findCommentsByItemId(itemId)).thenReturn(itemComments);
        ItemWithTimeAndCommentDto returnItem = itemService.getById(userId, itemId);
        assertEquals("name", returnItem.getName());
        assertEquals("description", returnItem.getDescription());
        assertEquals(true, returnItem.getAvailable());
        assertEquals(booking.getId(), returnItem.getLastBooking().getId());
        assertEquals(comment.getId(), returnItem.getComments().get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(itemId);
        verify(bookingRepository).findBookingByItemId(itemId);
        verify(commentRepository).findCommentsByItemId(itemId);
    }

    @Test
    void getById_whenInvokedWithoutTimeAndOneBookingAndEndIsBefore_thenReturnItem() {
        Long userId = 0L;
        Long itemId = 0L;
        Long bookingId = 0L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Booking booking = new Booking(bookingId, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(2),
                item, user, Status.APPROVED);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        List<Booking> itemBookings = List.of(booking);
        List<Comment> itemComments = List.of(comment);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemId(itemId)).thenReturn(itemBookings);
        when(commentRepository.findCommentsByItemId(itemId)).thenReturn(itemComments);
        ItemWithTimeAndCommentDto returnItem = itemService.getById(userId, itemId);
        assertEquals("name", returnItem.getName());
        assertEquals("description", returnItem.getDescription());
        assertEquals(true, returnItem.getAvailable());
        assertEquals(booking.getId(), returnItem.getNextBooking().getId());
        assertEquals(comment.getId(), returnItem.getComments().get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(itemId);
        verify(bookingRepository).findBookingByItemId(itemId);
        verify(commentRepository).findCommentsByItemId(itemId);
    }

    @Test
    void getById_whenInvokedWithoutTimeAndTwoBooking_thenReturnItem() {
        Long userId = 0L;
        Long itemId = 0L;
        Long bookingId = 0L;
        Long bookingId2 = 2L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Booking booking = new Booking(bookingId, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(2),
                item, user, Status.APPROVED);
        Booking booking2 = new Booking(bookingId2, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
                item, user, Status.APPROVED);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        List<Booking> itemBookings = List.of(booking, booking2);
        List<Comment> itemComments = List.of(comment);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemId(itemId)).thenReturn(itemBookings);
        when(commentRepository.findCommentsByItemId(itemId)).thenReturn(itemComments);
        ItemWithTimeAndCommentDto returnItem = itemService.getById(userId, itemId);
        assertEquals("name", returnItem.getName());
        assertEquals("description", returnItem.getDescription());
        assertEquals(true, returnItem.getAvailable());
        assertEquals(booking2.getId(), returnItem.getNextBooking().getId());
        assertEquals(booking.getId(), returnItem.getLastBooking().getId());
        assertEquals(comment.getId(), returnItem.getComments().get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(itemId);
        verify(bookingRepository).findBookingByItemId(itemId);
        verify(commentRepository).findCommentsByItemId(itemId);
    }

    @Test
    void getById_whenInvokedWithTimeAndTwoBooking_thenReturnItem() {
        Long userId = 0L;
        Long userId2 = 1L;
        Long itemId = 0L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        List<Comment> itemComments = List.of(comment);
        when(userRepository.existsUserById(userId2)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentsByItemId(itemId)).thenReturn(itemComments);
        ItemWithTimeAndCommentDto returnItem = itemService.getById(userId2, itemId);
        assertEquals("name", returnItem.getName());
        assertEquals("description", returnItem.getDescription());
        assertEquals(true, returnItem.getAvailable());
        assertEquals(comment.getId(), returnItem.getComments().get(0).getId());
        verify(userRepository).existsUserById(userId2);
        verify(itemRepository).findById(itemId);
        verify(commentRepository).findCommentsByItemId(itemId);
    }

    @Test
    void update_whenInvoked_thenReturnUpdateItem() {
        Long userId = 0L;
        Long itemId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        ItemDto inItem = ItemDto.builder()
                .name("name1")
                .description("description1")
                .available(false)
                .build();
        ItemRequest itemRequest = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        Item item = new Item(itemId, "name", "description", true, user, itemRequest);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto savedItem = itemService.update(userId, itemId, inItem);
        assertEquals(inItem.getName(), savedItem.getName());
        assertEquals(inItem.getDescription(), savedItem.getDescription());
        assertEquals(inItem.getAvailable(), savedItem.getAvailable());
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void update_whenInvokedAndUserHaventItem_thenReturnException() {
        Long userId = 0L;
        Long userId2 = 1L;
        Long itemId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        User user2 = new User(userId2);
        ItemDto inItem = ItemDto.builder()
                .name("name1")
                .description("description1")
                .available(false)
                .build();
        ItemRequest itemRequest = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        Item item = new Item(itemId, "name", "description", true, user2, itemRequest);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> itemService.update(userId, itemId, inItem));
        verify(userRepository).existsUserById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void search_whenInvokedWithText_thenReturnListItems() {
        Long userId = 0L;
        String text = "text";
        List<Item> items = List.of(new Item(userId));
        when(itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true))
                .thenReturn(items);
        List<ItemDto> returnItems = itemService.search(userId, text);
        assertEquals(returnItems.size(), 1);
        verify(itemRepository)
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true);
    }

    @Test
    void search_whenInvokedWithoutText_thenReturnListEmpty() {
        Long userId = 0L;
        String text = null;
        List<ItemDto> returnItems = itemService.search(userId, text);
        assertEquals(returnItems.size(), 0);
        verify(itemRepository, never())
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true);
    }

    @Test
    void search_whenInvokedWithEmptyText_thenReturnListEmpty() {
        Long userId = 0L;
        String text = "";
        List<ItemDto> returnItems = itemService.search(userId, text);
        assertEquals(returnItems.size(), 0);
        verify(itemRepository, never())
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true);
    }

    @Test
    void createComment_whenInvokedWithBooking_thenReturnComment() {
        Long userId = 0L;
        Long itemId = 0L;
        Long bookingId = 0L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Booking booking = new Booking(bookingId, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(2),
                item, user, Status.APPROVED);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        List<Booking> itemBookings = List.of(booking);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemId(itemId)).thenReturn(itemBookings);
        when(commentRepository.save(comment)).thenReturn(comment);
        CommentDto returnComment = itemService.createComment(userId, itemId, comment);
        assertEquals(comment.getText(), returnComment.getText());
        assertEquals(comment.getId(), returnComment.getId());
        assertEquals(comment.getAuthor().getName(), returnComment.getAuthorName());
        verify(userRepository).existsUserById(userId);
        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(bookingRepository).findBookingByItemId(itemId);
        verify(commentRepository).save(comment);
    }

    @Test
    void createComment_whenInvokedWithOutBooking_thenReturnComment() {
        Long userId = 0L;
        Long itemId = 0L;
        Long commentId = 0L;
        User user = new User(userId);
        Item item = new Item(userId, "name", "description", true, user);
        Comment comment = new Comment(commentId, "text", item, user, LocalDateTime.now());
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(bookingRepository.findBookingByItemId(itemId)).thenReturn(new ArrayList<>());
        assertThrows(InCorrectDataException.class, () -> itemService.createComment(userId, itemId, comment));
        verify(userRepository).existsUserById(userId);
        verify(userRepository, never()).findById(userId);
        verify(itemRepository, never()).findById(itemId);
        verify(bookingRepository).findBookingByItemId(itemId);
        verify(commentRepository, never()).save(comment);
    }
}