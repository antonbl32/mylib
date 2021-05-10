package by.library.mylib.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class handling exception with exist user registration
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
