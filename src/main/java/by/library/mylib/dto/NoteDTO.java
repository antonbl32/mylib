package by.library.mylib.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NoteDTO {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String desc;
    private String username;
}
