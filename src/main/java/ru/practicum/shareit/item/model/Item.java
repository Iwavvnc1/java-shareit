package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.*;


/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {
    public Item(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    ItemRequest request;
}
