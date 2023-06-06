package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;


/**
 * TODO Sprint add-controllers.
 */
@Value
@Builder(toBuilder = true)
public class Item {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    User owner;
    ItemRequest request;

    public User getOwner() {
        return owner;
    }

    public ItemRequest getRequest() {
        return request;
    }
}
