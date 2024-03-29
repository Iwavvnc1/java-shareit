package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long userId);

    Page<Booking> findByBookerId(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id in :itemIds")
    List<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id in :itemIds")
    Page<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id = ?1 " +
            "and b.status = 1")
    List<Booking> findBookingByItemId(Long itemId);

}

