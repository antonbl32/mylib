package by.library.mylib.dto;

import by.library.mylib.entity.enums.ERole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Data transfer object User
 */
@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String lastname;
    private ERole role;
}
