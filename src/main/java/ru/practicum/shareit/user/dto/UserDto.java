package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {
    private long id;
    @NotBlank
    @NotEmpty
    private String name;
    @NotBlank
    @Email
    private String email;
}
