package ru.practicum.shareit.booking.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Future
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    @NotNull
    private LocalDateTime end;
    @ManyToOne
    private Item item;
    @ManyToOne
    private User booker;
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status;
}
/*id — уникальный идентификатор бронирования;
start — дата и время начала бронирования;
end — дата и время конца бронирования;
item — вещь, которую пользователь бронирует;
booker — пользователь, который осуществляет бронирование;
status — статус бронирования. Может принимать одно из следующих значений:*/