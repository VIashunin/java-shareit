package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private long id;
    private String name;
    private String description;
    private String available;
    private long owner;
    private String request;
}
