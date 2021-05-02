package by.library.mylib.payload.request;

import by.library.mylib.annotation.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @NotBlank(message = "User name is required")
    @NotEmpty(message = "please enter name")
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

    private String confirmPassword;
}
