package by.library.mylib.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String s) {
        super(s);
    }
}
